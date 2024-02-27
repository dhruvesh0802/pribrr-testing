package com.pb.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.*;
import com.pb.exception.CustomException;
import com.pb.model.ProfileEntity;
import com.pb.model.SubscriptionEntity;
import com.pb.model.UserEntity;
import com.pb.repository.ProfileRepository;
import com.pb.repository.SubscriptionRepository;
import com.pb.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.net.Webhook;
import com.stripe.param.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StripeService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapper objectMapper;

    @Value("${stripe.API_KEY}")
    private String apiKey;
    @Autowired
    private ProfileRepository profileRepository;

    public ProductCustomerDTO addCustomerPaymentAndSubscription(ProductCustomerDTO productCustomerDTO) {
        Stripe.apiKey = apiKey;
        SubscriptionEntity subscriptionEntity = new SubscriptionEntity();

        UserEntity userEntity = userRepository.findById(productCustomerDTO.getUserId()).
                orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.USER_NOT_FOUND));

        //create customer
        Customer customer = createCustomer(userEntity);
        subscriptionEntity.setCustomerId(customer.getId());

        //retrieve payment
        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.retrieve(productCustomerDTO.getPaymentId());
        } catch (StripeException e) {
            throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.PAYMENT_METHOD_NOT_FOUND);
        }

        //attach payment to customer

        attachPaymentMethod(subscriptionEntity, paymentMethod, customer);

        //bind customer and product
        createSubscription(productCustomerDTO, subscriptionEntity);

        subscriptionEntity.setUserEntity(userEntity);
        subscriptionRepository.save(subscriptionEntity);
        return productCustomerDTO;
    }

    public Customer createCustomer(UserEntity userEntity) {
        Optional<ProfileEntity> byUserEntityId = profileRepository.findByUserEntityId(userEntity.getId());

        CustomerCreateParams customerCreateParams = CustomerCreateParams.builder()
                .setName(userEntity.getUserName())
                .setEmail(userEntity.getEmail())
                .build();

        Customer customer;
        try {
            customer = Customer.create(customerCreateParams);
        } catch (StripeException e) {
            throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.CUSTOMER_CANT_BE_CREATED);
        }
        return customer;
    }

    public void attachPaymentMethod(SubscriptionEntity subscriptionEntity, PaymentMethod paymentMethod, Customer customer) {
        Map<String, Object> payment = new HashMap<>();
        payment.put("customer", subscriptionEntity.getCustomerId());

        PaymentMethod updatedPaymentMethod;
        try {
            updatedPaymentMethod = paymentMethod.attach(payment);
        } catch (StripeException e) {
            throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.PAYMENT_METHOD_NOT_FOUND);
        }
        subscriptionEntity.setPaymentId(updatedPaymentMethod.getId());

        //set default payment method
        CustomerUpdateParams customerUpdateParams = CustomerUpdateParams.builder()
                .setInvoiceSettings(CustomerUpdateParams.InvoiceSettings.builder().setDefaultPaymentMethod(updatedPaymentMethod.getId()).build())
                .build();

        try {
            Customer updatedCustomer = customer.update(customerUpdateParams);
        } catch (StripeException e) {
            throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.CUSTOMER_NOT_FOUND);
        }
    }
    public Long CancelSubscription(ProductCustomerDTO productCustomerDTO) {
        /*SubscriptionEntity subscriptionEntity = subscriptionRepository.findByUserEntityId(productCustomerDTO.getUserId());
        productCustomerDTO.setSubscriptionId(subscriptionEntity.getSubscriptionId());
        retrieveAndCancelSubscription(productCustomerDTO);
        productCustomerDTO.setSubscriptionId(subscriptionEntity.getNextSubscriptionId());
        return retrieveAndCancelSubscription(productCustomerDTO);*/
        Stripe.apiKey = apiKey;
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findByUserEntityId(productCustomerDTO.getUserId());
        Subscription subscription;
        Subscription nextSubscription = null;
        try {
            subscription = Subscription.retrieve(subscriptionEntity.getSubscriptionId());
            if(subscriptionEntity.getNextSubscriptionId() != null) {
                nextSubscription = Subscription.retrieve(subscriptionEntity.getNextSubscriptionId());
            }
        } catch (StripeException e) {
            throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.SUBSCRIPTION_NOT_FOUND);
        }
        Long previousDate = subscription.getCurrentPeriodEnd();

        //update sub to cancel it
        SubscriptionUpdateParams subscriptionUpdateParams = SubscriptionUpdateParams.builder()
                .setCancelAtPeriodEnd(true)
                .build();

        try {
            subscription.update(subscriptionUpdateParams);
            if(subscriptionEntity.getNextSubscriptionId() != null) {
                nextSubscription.cancel();
            }
            subscriptionRepository.delete(subscriptionEntity);
        } catch (StripeException e) {
            throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.SUBSCRIPTION_NOT_FOUND);
        }
        return previousDate;
    }
    public Long retrieveAndCancelSubscription(ProductCustomerDTO productCustomerDTO) {
        Stripe.apiKey = apiKey;
        SubscriptionEntity subscriptionEntity = subscriptionRepository.findByUserEntityId(productCustomerDTO.getUserId());
        Subscription subscription;

        try {
            subscription = Subscription.retrieve(productCustomerDTO.getSubscriptionId());
        } catch (StripeException e) {
            throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.SUBSCRIPTION_NOT_FOUND);
        }

        Long previousDate = subscription.getCurrentPeriodEnd();

        //update sub to cancel it
        SubscriptionUpdateParams subscriptionUpdateParams = SubscriptionUpdateParams.builder()
                .setCancelAtPeriodEnd(true)
                .build();
        try {
            subscription.update(subscriptionUpdateParams);

        } catch (StripeException e) {
            throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.SUBSCRIPTION_NOT_FOUND);
        }
        return previousDate;
    }

    public void createSubscription(ProductCustomerDTO productCustomerDTO, SubscriptionEntity subscriptionEntity) {
        Map<String, Object> product = new HashMap<>();
        product.put("product", productCustomerDTO.getProductId());

        PriceCollection prices;
        try {
            prices = Price.list(product);
        } catch (StripeException e) {
            throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.PRICE_NOT_FOUND);
        }
        List<Object> items = new ArrayList<>();

        for (int i = 0; i < prices.getData().size(); i++) {
            Map<String, Object> item1 = new HashMap<>();
            item1.put(
                    "price",
                    prices.getData().get(i).getId());
            items.add(item1);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("customer", subscriptionEntity.getCustomerId());
        params.put("items", items);

        Subscription subscription1;
        try {
            subscription1 = Subscription.create(params);
        } catch (StripeException e) {
            throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.CUSTOMER_CANT_BE_CREATED);
        }

        subscriptionEntity.setProductId(productCustomerDTO.getProductId());
        subscriptionEntity.setSubscriptionId(subscription1.getId());
    }

    public List<StripeProductDTO> getAllProducts() {
        Stripe.apiKey = apiKey;
        ProductCollection products;
        try {
            products = Product.list(ProductListParams.builder().build());
        } catch (StripeException e) {
            throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.PRODUCT_NOT_FOUND);
        }
        List<StripeProductDTO> stripeProductDTOS = new ArrayList<>();
        List<Product> data = products.getData();
        data = data.stream().filter(d -> d.getActive()).collect(Collectors.toList());
        for (Product datum : data) {

            PriceListParams listParams = PriceListParams.builder()
                    .setProduct(datum.getId())
                    .build();

            List<Price> prices;
            try {
                prices = Price.list(listParams).getData();
            } catch (StripeException e) {
                throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.PRICE_NOT_FOUND);
            }
            StripeProductDTO stripeProductDTO = new StripeProductDTO();
            stripeProductDTO.setName(datum.getName());
            stripeProductDTO.setActive(datum.getActive());

            BigDecimal num = new BigDecimal(String.valueOf(prices.get(0).getUnitAmountDecimal())).divide(new BigDecimal(100));
            DecimalFormat f1 = new DecimalFormat("#.00");
            String format = f1.format(num);
            stripeProductDTO.setDefault_price(format);
            stripeProductDTO.setDescription(datum.getDescription());
            stripeProductDTO.setId(datum.getId());
            if(stripeProductDTO.getName().equals("Premium")){
                List<String> features = new ArrayList<>();
                features.add("Full access, all features");
                features.add("Exclusive content, features");
                features.add("Best customer support");
                features.add("Additional benefits included");
                stripeProductDTO.setFeatures(features);
            } else if(stripeProductDTO.getName().equals("Standard")){
                List<String> features = new ArrayList<>();
                features.add("More features, content");
                features.add("Better customer support");
                features.add("Money-saving option");
                features.add("Fewer limitations");
                stripeProductDTO.setFeatures(features);
            }
            stripeProductDTOS.add(stripeProductDTO);
        }
        return stripeProductDTOS;
    }

//    public String createSubscriptions(ProductCustomerDTO productCustomerDTO) throws StripeException {
//        Stripe.apiKey = apiKey;
//
//        Map<String, Object> params1 = new HashMap<>();
//        params1.put("product", productCustomerDTO.getProductId());
//
//        PriceCollection prices = Price.list(params1);
//        List<Object> items = new ArrayList<>();
//
//        for (int i = 0; i < prices.getData().size(); i++) {
//            Map<String, Object> item1 = new HashMap<>();
//            item1.put(
//                    "price",
//                    prices.getData().get(i).getId());
//            items.add(item1);
//        }
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("customer", productCustomerDTO.getCustomerId());
//        params.put("items", items);
//
//        Subscription subscription1 =
//                Subscription.create(params);
//        return "Customer and Product bind successfully";
//    }

    public String upgradeSubscription(ProductCustomerDTO productCustomerDTO) {
        Stripe.apiKey = apiKey;

        SubscriptionEntity subscriptionEntity = subscriptionRepository.findByUserEntityId(productCustomerDTO.getUserId());

        if (Objects.nonNull(subscriptionEntity)) {
            //retrieve subscription and cancel it
            productCustomerDTO.setSubscriptionId(subscriptionEntity.getSubscriptionId());
            if(subscriptionEntity.getNextProductId() != null){
                /* Subscription next = Subscription.retrieve(subscriptionEntity.getNextSubscriptionId());
                 next.cancel();
                 Subscription current = Subscription.retrieve(subscriptionEntity.getSubscriptionId());
                 SubscriptionUpdateParams subscriptionUpdateParams = SubscriptionUpdateParams.builder()
                         .setCancelAtPeriodEnd(false)
                         .build();
                 current.update(subscriptionUpdateParams);

                 subscriptionEntity.setNextSubscriptionId(null);
                 subscriptionRepository.save(subscriptionEntity);*/
                return "This Plan will activate after current billing cycle.";
            }
            Long previousDate = retrieveAndCancelSubscription(productCustomerDTO);

            //add new subscription
            Map<String, Object> price = new HashMap<>();
            price.put("product", productCustomerDTO.getProductId());

            PriceCollection prices;
            try {
                prices = Price.list(price);
            } catch (StripeException e) {
                throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.PRICE_NOT_FOUND);
            }

            SubscriptionCreateParams.Item item = null;
            for (int i = 0; i < prices.getData().size(); i++) {
                item = SubscriptionCreateParams.Item.builder()
                        .setPrice(prices.getData().get(i).getId())
                        .build();
            }

            SubscriptionCreateParams subscriptionCreateParams = SubscriptionCreateParams.builder()
                    .setBillingCycleAnchor(previousDate)
                    .setCustomer(subscriptionEntity.getCustomerId())
                    .addItem(item)
                    .setProrationBehavior(SubscriptionCreateParams.ProrationBehavior.NONE)  //also set proration off for billing cycle
                    .build();

            try {
                Subscription subscription1 =
                        Subscription.create(subscriptionCreateParams);
                subscriptionEntity.setNextSubscriptionId(subscription1.getId());
                subscriptionEntity.setNextProductId(productCustomerDTO.getProductId());
                subscriptionRepository.save(subscriptionEntity);
            } catch (StripeException e) {
                throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.SUBSCRIPTION_NOT_FOUND);
            }

            return "subscription cancelled and Updated Successfully";

        } else {
            return null;
        }
    }

//    public List<SubscriptionDTO> getParticularSubscription(ProductCustomerDTO productCustomerDTO) throws StripeException {
//        Stripe.apiKey = apiKey;
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("customer", productCustomerDTO.getCustomerId());
//
//        //get current active subscription
//        SubscriptionCollection subscriptions =
//                Subscription.list(params);
//        List<SubscriptionDTO> subscriptionDTOS = new ArrayList<>();
//        for (int i = 0; i < subscriptions.getData().size(); i++) {
//
//            if (subscriptions.getData().size() < 2) {
//                assignSubscription(subscriptions, i, subscriptionDTOS);
//            } else {
//                if (Objects.nonNull(subscriptions.getData().get(i).getCancelAt())) {
//                    assignSubscription(subscriptions, i, subscriptionDTOS);
//                }
//            }
//        }
//        return subscriptionDTOS;
//    }
//
//    public void assignSubscription(SubscriptionCollection subscriptions, int i, List<SubscriptionDTO> subscriptionDTOS) {
//        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();
//        subscriptionDTO.setSubscriptionId(subscriptions.getData().get(i).getId());
//        subscriptionDTO.setProductId(subscriptions.getData().get(i).getItems().getData().get(0).getPrice().getProduct());
//        subscriptionDTO.setPriceId(subscriptions.getData().get(i).getItems().getData().get(0).getPrice().getId());
//        subscriptionDTO.setCancelAt(subscriptions.getData().get(i).getCancelAt());
//        subscriptionDTO.setStatus(subscriptions.getData().get(i).getStatus());
//        subscriptionDTOS.add(subscriptionDTO);
//    }

//    public List<CustomerDTO> getAllPayments(CustomerDTO customerDTO) throws StripeException {
//        Stripe.apiKey = apiKey;
//
//        Map<String, Object> params = new HashMap<>();
//        params.put("customer", customerDTO.getId());
//
//        PaymentMethodCollection paymentMethods =
//                PaymentMethod.list(params);
//
//        List<CustomerDTO> customerDTOS = new ArrayList<>();
//        for (int i = 0; i < paymentMethods.getData().size(); i++) {
//            CustomerDTO customer = new CustomerDTO();
//            customer.setPaymentId(paymentMethods.getData().get(i).getId());
//            customerDTOS.add(customer);
//        }
//        return customerDTOS;
//    }

    public CustomerDTO createPayment() {
        Stripe.apiKey = apiKey;

        Map<String, Object> card = new HashMap<>();
        card.put("number", "4242424242424242");
        card.put("exp_month", 8);
        card.put("exp_year", 2024);
        card.put("cvc", "314");
        Map<String, Object> params = new HashMap<>();
        params.put("type", "card");
        params.put("card", card);

        PaymentMethod paymentMethod;
        try {
            paymentMethod = PaymentMethod.create(params);
        } catch (StripeException e) {
            throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.SOMETHING_WENT_WRONG);
        }
        CustomerDTO customer = new CustomerDTO();
        customer.setPaymentId(paymentMethod.getId());
        return customer;
    }

    public PaymentDTO getPayment(ProductCustomerDTO productCustomerDTO) {
        Stripe.apiKey = apiKey;

        SubscriptionEntity subscriptionEntity = subscriptionRepository.findByUserEntityId(productCustomerDTO.getUserId());
        if (Objects.nonNull(subscriptionEntity)) {
            Customer customer;
            try {
                customer = Customer.retrieve(subscriptionEntity.getCustomerId());
            } catch (StripeException e) {
                throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.CUSTOMER_NOT_FOUND);
            }

            if (Objects.nonNull(customer.getInvoiceSettings().getDefaultPaymentMethod())) {
                PaymentMethod paymentMethod;
                try {
                    paymentMethod = PaymentMethod.retrieve(customer.getInvoiceSettings().getDefaultPaymentMethod());
                } catch (StripeException e) {
                    throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.PAYMENT_METHOD_NOT_FOUND);
                }

                PaymentDTO paymentDTO = new PaymentDTO();
                paymentDTO.setId(paymentMethod.getId());
                paymentDTO.setCardType(paymentMethod.getCard().getBrand());
                paymentDTO.setCardNumber("XXXX-XXXX-XXXX-" + paymentMethod.getCard().getLast4());
                paymentDTO.setExpMonth(paymentMethod.getCard().getExpMonth());
                paymentDTO.setExpYear(paymentMethod.getCard().getExpYear());
                return paymentDTO;
            }
        }
        return null;
    }
    public SubscriptionDTO getCustomerDetails(Long userId) {
        Stripe.apiKey = apiKey;

        SubscriptionEntity subscriptionEntity = subscriptionRepository.findByUserEntityId(userId);
        SubscriptionDTO subscriptionDTO = modelMapper.map(subscriptionEntity, SubscriptionDTO.class);
        Subscription subscription = null;
        try {
            if(subscriptionEntity.getNextSubscriptionId() != null) {
                subscription = Subscription.retrieve(subscriptionEntity.getNextSubscriptionId());
                LocalDate ld = Instant.ofEpochMilli(subscription.getBillingCycleAnchor() * 1000)
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                subscriptionDTO.setNextSubscriptionStartDate(ld);
            }
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }
        subscriptionDTO.getUserEntity().setNetwork(null);
        return subscriptionDTO;
    }
    public String getResponse(String payload, HttpServletRequest httpServletRequest) throws StripeException {
        Stripe.apiKey = apiKey;
        String endpointSecret = "whsec_cr07bwTrP7e3L0qynojV2RdqhX44VcN0";
        String sigHeader = httpServletRequest.getHeader("Stripe-Signature");
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            throw new CustomException(HttpStatus.NOT_FOUND, ResponseMessageConstant.SOMETHING_WENT_WRONG);
        }
        StripeObject object = event.getData().getObject();
        HashMap hashMap = objectMapper.convertValue(object, HashMap.class);
        String customer = (String) hashMap.get("customer");
        Customer customer1 = Customer.retrieve(customer);

        switch (event.getType()) {
            case "invoice.payment_failed":
                handlePaymentFailed(customer1);

            case "customer.subscription.deleted":
               handleSubscriptionDeleted(customer1);
        }
        return payload;
    }

    private void handlePaymentFailed(Customer customer){
        SubscriptionEntity subscription = subscriptionRepository.findByCustomerId(customer.getId());
        if (Objects.nonNull(subscription)){
            subscriptionRepository.delete(subscription);
        }
    }

    private void handleSubscriptionDeleted(Customer customer){
        SubscriptionEntity subscription = subscriptionRepository.findByCustomerId(customer.getId());
        if (Objects.nonNull(subscription)){
            subscription.setProductId(subscription.getNextProductId());
            subscription.setSubscriptionId(subscription.getNextSubscriptionId());
            subscription.setNextProductId(null);
            subscription.setNextSubscriptionId(null);
            subscriptionRepository.save(subscription);
        }
    }

}
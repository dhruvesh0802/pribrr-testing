package com.pb.controller;

import com.pb.constants.APIEndpointConstant;
import com.pb.constants.ResponseMessageConstant;
import com.pb.dto.PaymentDTO;
import com.pb.dto.ProductCustomerDTO;
import com.pb.dto.SubscriptionDTO;
import com.pb.response.CustomResponse;
import com.pb.service.StripeService;
import com.stripe.exception.StripeException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Service
@RequestMapping(value = APIEndpointConstant.STRIPE_URLS.BASE_URL)
@Slf4j
@Api(value = APIEndpointConstant.STRIPE_URLS.BASE_URL,
        tags = "Stripe")
@CrossOrigin
public class StripeController {

    @Autowired
    private StripeService stripeService;

    @PostMapping(value = APIEndpointConstant.STRIPE_URLS.ADD_CUSTOMER)
    public ResponseEntity<?> addCustomerPaymentAndSubscription(@RequestBody ProductCustomerDTO productCustomerDTO) {
        return new ResponseEntity<>(
                new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CUSTOMER_PAYMENT_SUBSCRIPTION_CREATED, stripeService.addCustomerPaymentAndSubscription(productCustomerDTO)),
                HttpStatus.OK);
    }

    @GetMapping(value = APIEndpointConstant.STRIPE_URLS.GET_ALL_SUBSCRIPTION)
    public ResponseEntity<?> getAllProducts() {
        return new ResponseEntity<>(
                new CustomResponse(HttpStatus.OK, "Subscription fetched Successfully", stripeService.getAllProducts()),
                HttpStatus.OK);
    }

//    @PostMapping(value = APIEndpointConstant.STRIPE_URLS.CUSTOMER_SUBSCRIPTION)
//    public ResponseEntity<?> createSubscription(@RequestBody ProductCustomerDTO productCustomerDTO) throws StripeException {
//        return new ResponseEntity<>(
//                new CustomResponse(HttpStatus.OK, "Subscription created Successfully", stripeService.createSubscriptions(productCustomerDTO)),
//                HttpStatus.OK);
//    }

    @PostMapping(value = APIEndpointConstant.STRIPE_URLS.UPGRADE_SUBSCRIPTION)
    public ResponseEntity<?> upgradeSubscription(@RequestBody ProductCustomerDTO productCustomerDTO) {
        String msg = stripeService.upgradeSubscription(productCustomerDTO);
        if (Objects.nonNull(msg))
        {
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK, ResponseMessageConstant.SUBSCRIPTION_CANCELLED_AND_BIND, null),
                            HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.NOT_FOUND, ResponseMessageConstant.SUBSCRIPTION_NOT_FOUND, null),
                    HttpStatus.OK);
        }

    }

    @PostMapping(value = APIEndpointConstant.STRIPE_URLS.CANCEL_SUBSCRIPTION)
    public ResponseEntity<?> cancelSubscription(@RequestBody ProductCustomerDTO productCustomerDTO) {
        return new ResponseEntity<>(
                new CustomResponse(HttpStatus.OK, "Subscription cancelled Successfully", stripeService.CancelSubscription(productCustomerDTO)),
                HttpStatus.OK);
    }

//    @PostMapping(value = APIEndpointConstant.STRIPE_URLS.GET_PARTICULAR_SUBSCRIPTION)
//    public ResponseEntity<?> getParticularSubscription(@RequestBody ProductCustomerDTO productCustomerDTO) throws StripeException {
//        return new ResponseEntity<>(
//                new CustomResponse(HttpStatus.OK, "Subscription fetched Successfully", stripeService.getParticularSubscription(productCustomerDTO)),
//                HttpStatus.OK);
//    }
//
//    @PostMapping(value = APIEndpointConstant.STRIPE_URLS.GET_ALL_PAYMENTS)
//    public ResponseEntity<?> getAllPayments(@RequestBody CustomerDTO customer) throws StripeException {
//        return new ResponseEntity<>(
//                new CustomResponse(HttpStatus.OK, "Payments fetched Successfully", stripeService.getAllPayments(customer)),
//                HttpStatus.OK);
//    }

    @GetMapping(value = APIEndpointConstant.STRIPE_URLS.CREATE_PAYMENT)
    public ResponseEntity<?> createPayment() {
        return new ResponseEntity<>(
                new CustomResponse(HttpStatus.OK, "Payments created Successfully", stripeService.createPayment()),
                HttpStatus.OK);
    }

    @PostMapping(value = APIEndpointConstant.STRIPE_URLS.GET_PAYMENT)
    public ResponseEntity<?> getPayment(@RequestBody ProductCustomerDTO productCustomerDTO) {

        PaymentDTO paymentDTO = stripeService.getPayment(productCustomerDTO);
        if (Objects.nonNull(paymentDTO)){
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PAYMENT_RETRIEVED_SUCCESSFULLY,paymentDTO),
                    HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK, ResponseMessageConstant.PAYMENT_METHOD_NOT_FOUND,null),
                    HttpStatus.OK);
        }
    }
    @GetMapping(value = APIEndpointConstant.STRIPE_URLS.GET_CUSTOMER_DETAILS+"/{userId}")
    public ResponseEntity<?> getCustomerDetails(@PathVariable("userId") Long userId) {
        SubscriptionDTO customerDetails = stripeService.getCustomerDetails(userId);

        if (Objects.nonNull(customerDetails)){
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CUSTOMER_RETRIEVED_SUCCESSFULLY,customerDetails),
                    HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(
                    new CustomResponse(HttpStatus.OK, ResponseMessageConstant.CUSTOMER_NOT_FOUND,null),
                    HttpStatus.OK);
        }
    }
    @PostMapping(value = APIEndpointConstant.STRIPE_URLS.GET_RESPONSE)
    public ResponseEntity<?> getResponse(@RequestBody String payload, HttpServletRequest httpServletRequest) throws StripeException {

        return new ResponseEntity<>(
                new CustomResponse(HttpStatus.OK, "Payments fetched Successfully",stripeService.getResponse(payload,httpServletRequest)),
                HttpStatus.OK);
    }
}
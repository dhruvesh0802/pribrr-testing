package com.pb.constants;

public class APIEndpointConstant {
    public interface COMMON_URL{
        String GET_ALL_USER = "/get-all-user";
        String CHANGE_STATUS = "/change-status";
        String REGISTER = "/register";

        String LOGIN = "/login";
    }
    public interface ADMIN_URLS{
        String BASE_URL = "/admin";
        String EDIT_ADMIN = "/edit-admin";
        String DELETE_ADMIN = "/delete-admin";
        String GET_ALL_ADMIN = "/get-all-admin";
        String SEND_OTP = "/send-otp";
        String VERIFY_OTP = "/verify-otp";
        String FORGOT_PASSWORD = "/forgot-password";
        String ADD_STATIC_PAGE = "/add-static-page";
        String GET_ALL_STATIC_PAGE = "/get-all-static-page";
        String DASHBOARD = "/dashboard";
        String DELETE_STATIC_PAGE = "/delete-static-page";
    }

    public interface COUNTRY_URL{
        String BASE_URL = "/country";
        String GET_ALL_COUNTRY = "/get-all-country";
        String GET_ALL_TIMEZONE = "/get-all-timezone";
        String CLEAR_COUNTRY_CACHE = "/clear-country-cache";
    }
    
    public interface BANNER_URLS{
        String BASE_URL = "/banner";
        String ADD_BANNER = "/add-banner";
        String GET_ALL_BANNER = "/get-all-banner";
        String GET_ALL_BANNER_BY_LOCATION = "/get-all-banner-by-location";
        String UPDATE_BANNER = "/update-banner";
        String DELETE_BANNER = "/delete-banner";
    }
    public interface PLAN_MANAGEMENT_URLS{
        String BASE_URL = "/plan";
        String ADD_PLAN = "/add-plan";
        String GET_ALL_PLAN = "/get-all-plan";
        String DELETE_PLAN = "/delete-plan";
        String UPDATE_PLAN = "/update-plan";
        String MANAGE_PLAN_PERMISSION = "/manage-plan-permission";
        String GET_ALL_PERMISSIONS = "/get-all-permissions";
        String GET_PERMISSIONS_BY_PLAN_ID = "/get-permissions-by-plan-id";
        String CHANGE_PLAN_STATUS = "/change-plan-status";
    }

    public interface RETAILER_URLS{
        String BASE_URL = "/retailer";
        String DELETE_RETAILER = "/delete-retailer";
    }
    public interface SUPPLIER_URLS{
        String BASE_URL = "/supplier";
    }
    public interface SEARCH_URLS{
        String BASE_URL = "/search";
    }
    public interface PROFILE_URLS{
        String BASE_URL = "/update-profile";
        String COMPANY_DETAILS = "/company-details";
        String MEMBER_DETAILS = "/member-details";
        String PRODUCT_DETAILS = "/product-details";
        String ARTWORK_DETAILS = "/artwork-details";
        String QA_DETAILS = "/qa-details";
        String GET_PERCENTAGE = "/get-percentage";
    }

    public interface DEPARTMENT_URLS{
        String BASE_URL = "/department";
        String ADD_DEPARTMENT="/add-department";
        String UPDATE_DEPARTMENT="/update-department";
        String GET_ALL_DEPARTMENT = "/get-all-department";
        String GET_DEPARTMENT_BY_ID = "/get-department-by-id";

        String DELETE_DEPARTMENT = "/delete-department";
        String CHANGE_DEPARTMENT_STATUS = "/change-department-status";
    }
    
    public interface CATEGORY_URLS{
        String BASE_URL = "/category";
        String ADD_CATEGORY="/add-category";
        String UPDATE_CATEGORY="/update-category";
        String GET_ALL_CATEGORY = "/get-all-category";
        String GET_ALL_CATEGORY_DEPARTMENT_ID = "/get-all-category-department-id";
        String DELETE_CATEGORY = "/delete-category";
        String CHANGE_CATEGORY_STATUS = "/change-category-status";
        String GET_CATEGORY_BY_DEPARTMENT_ID="/get-category-by-department-id";
        String GET_ALL_SUB_CATEGORY_BY_CATEGORY_ID="/get-all-sub-category-by-category-id";
    }
    
    public interface USER_URLS{
        String BASE_URL = "/user";
        String ADD_PROFILE = "/add-profile";
        String CHANGE_PASSWORD = "/change-password";
        String ADD_TO_NETWORK = "/add-to-network";

    }

    public interface SUB_CATEGORY_URLS{

        String BASE_URL = "/sub-category";
        String ADD_SUB_CATEGORY="/add-sub-category";
        String UPDATE_SUB_CATEGORY="/update-sub-category";
        String GET_ALL_SUB_CATEGORY = "/get-all-sub-category";
        String DELETE_SUB_CATEGORY = "/delete-sub-category";
        String CHANGE_SUB_CATEGORY_STATUS = "/change-sub-category-status";
    }

    public interface PRODUCT_URLS{
        String BASE_URL = "/product";
        String ADD_PRODUCT = "/add-product";
        String GET_ALL_PRODUCT_OF_USER = "/get-all-product-of-user";
        String UPDATE_PRODUCT = "/update-product";
        String DELETE_PRODUCT = "/delete-product";
    }

    public interface PAYMENT_URLS{
        String BASE_URL = "/payment";

        String CHARGE_PAYMENT = "/charge";
    }

    public interface STRIPE_URLS{
        String BASE_URL = "/stripe";
        String ADD_CUSTOMER = "/customers";
        String GET_ALL_SUBSCRIPTION= "/allSubscriptions";
        String CUSTOMER_SUBSCRIPTION = "/bindCustomer";
        String CANCEL_SUBSCRIPTION = "/cancelSubscription";
        String UPGRADE_SUBSCRIPTION = "/upgradeSubscription";
        String GET_PARTICULAR_SUBSCRIPTION = "/getSubscription";
        String GET_ALL_PAYMENTS = "/getAllPayments";
        String CREATE_PAYMENT = "/payment";
        String GET_PAYMENT = "/payment/get";
        String GET_CUSTOMER_DETAILS = "/get/customer/details";
        String GET_RESPONSE = "/get";
    }
}

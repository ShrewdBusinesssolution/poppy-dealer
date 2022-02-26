package com.shrewd.poppydealers.utilities;

import java.util.HashMap;

public class Constants {
    public static final String id = "369";
    public static final String description = "Harikii the future";


    public static final String KEY_PREFERENCE_NAME = "PoppyPreference";
    public static final String KEY_INTRO = "isIntroOpened";
    public static final String KEY_LOGIN = "isLogin";
    public static final String KEY_LOCATION_STATUS = "location_status";
    public static final String VERSION = "version";

    public static final String KEY_SUCCESS = "success";
    public static final String KEY_MESSAGE = "message";


    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_USER_MOBILE = "user_mobile";
    public static final String KEY_USER_FULL_NAME = "full_name";
    public static final String KEY_LAST_INDEX_STRING = "";


    public static final String DEALER_ID = "dealer_id";
    public static final String KEY_NAME = "shop_name";
    public static final String KEY_MOBILE = "dealer_mobile";
    public static final String KEY_EMAIL = "dealer_email";
    public static final String KEY_EXPERIENCE = "dealer_experience";
    public static final String KEY_EXPERIENCE_MONTH = "dealer_experience_month";
    public static final String KEY_ADDRESS = "dealer_address";
    public static final String KEY_DISTRICT = "dealer_district";
    public static final String KEY_STATE = "dealer_state";
    public static final String KEY_PINCODE = "dealer_pincode";
    public static final String KEY_FCM = "dealer_fcm";
    public static final String KEY_TYPE = "user_type";
    public static final String KEY_LANDLINE = "landline";
    public static final String KEY_BUSINESS = "current_business";
    public static final String KEY_GST = "gst_no";
    public static final String KEY_GST_DOC = "gst_doc";
    public static final String KEY_PANCARD = "pancard_no";
    public static final String KEY_PANCARD_DOC = "pancard_doc";


    public static final String KEY_SORT = "sort";
    public static final String KEY_STATUS = "status";
    public static final String KEY_TO_DATE = "to_date";
    public static final String KEY_FROM_DATE = "from_date";
    public static final String KEY_VALUE = "value";
    public static final String KEY_PRODUCT_ID = "product_id";
    public static final String KEY_PRODUCT_QUANTITY = "product_quantity";
    public static final String KEY_PRODUCT_TYPE = "product_type";
    public static final String KEY_PRODUCT_NAME = "product_name";
    public static final String KEY_PRODUCT_IMAGE = "product_image";
    public static final String KEY_PRODUCT_RATE = "product_rate";
    public static final String KEY_PRODUCT_PIECES = "product_pieces";

    public static final String KEY_PRODUCT_CUSTOMIZE = "product_customize";
    public static final String KEY_PRODUCT_CART = "product_cart";
    public static final String KEY_ORDER = "product_order";

    public static final String KEY_ORDER_ID = "order_id";

    public static final String KEY_PRODUCT_INCHES = "thickness";
    public static final String KEY_PRODUCT_SIZE = "size";
    public static final String KEY_PRODUCT_COLOR = "color";
    public static final String KEY_PRODUCT_COLOR_CODE = "color_code";
    public static final String KEY_PRODUCT_GROUP_COUNT = "group_count";
    public static final String KEY_PRODUCT_GROUP_ID = "group_id";

    public static final String KEY_TOTAL_AMOUNT = "total_amount";


    public static final String KEY_MINIMUM_PURCHASE = "minimum_purchase";
    public static final String KEY_DELIVERY_COST = "delivery_cost";

    public static final String KEY_SUB_CATEGORY_TYPE = "sub_category_type";
    public static final String KEY_CATEGORY_TYPE = "category_type";


    public static final String REMOTE_MSG_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MSG_CONTENT_TYPE = "Content-Type";
    public static final String KEY_FCM_TOKEN = "fcm_token";
    public static final String KEY_CLASS_TYPE = "class_type";
    public static final String KEY_ALERT = "payment_alert";
    public static final String KEY_CREDIT_AMOUNT = "credit_amount";


    public static final String KEY_BUNDLE = "bundle";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_MAP_ADDRESS = "map_address";
    public static final String KEY_MAP_LATLNG = "lat_lng";


    public static HashMap<String, String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(Constants.REMOTE_MSG_AUTHORIZATION, "Key=AAAAXF5-KdE:APA91bGUN2jE2kSPWAs9JBvnmybvwyVe4pZ016JxXJQrTneZCatBpVLnEeyY76bf6Ikmihn_xsthP-uNYYVTB2RsXSigX8hUYKM9hptWzhQDsRLX8-x5fsd9n9ETvXtcVLpQYmKGcUVT");
        headers.put(Constants.REMOTE_MSG_CONTENT_TYPE, "application/json");
        return headers;

    }

}

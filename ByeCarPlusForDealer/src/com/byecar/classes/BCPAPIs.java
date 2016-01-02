package com.byecar.classes;

public class BCPAPIs {

	public static String BASE_API_URL = "http://dev.bye-car.com";
	public static String UPLOAD_URL = BASE_API_URL + "/files/upload/image.json";
	public static String REGISTER_URL = BASE_API_URL + "/devices/register/android.json";
	public static String PUSH_SETTING_URL = BASE_API_URL + "/users/update/to_get_pushed";
	public static String AREA_URL = BASE_API_URL + "/zipcodes/areas.json";
	public static String AREA_SEARCH_URL = BASE_API_URL + "/dongs/search.json";
	
	public static String SIGN_CHECK_URL = BASE_API_URL + "/users/login_check.json";
	public static String SIGN_IN_URL = BASE_API_URL + "/users/login/dealer.json";
	public static String FIND_PW_URL = BASE_API_URL + "/users/find/password.json";
	public static String TERM_OF_USE_URL = BASE_API_URL + "/appinfo/policies";
	public static String EMAIL_CHECK_URL = BASE_API_URL + "/users/check/email.json";
	public static String SIGN_UP_URL = BASE_API_URL + "/users/join.json";
	public static String SIGN_OUT_URL = BASE_API_URL + "/users/logout.json";
	public static String WITHDRAW_URL = BASE_API_URL + "/users/withdraw.json";
	public static String DEALER_INFO_URL = BASE_API_URL + "/dealers/show.json";
	public static String EDIT_DEALER_INFO_URL = BASE_API_URL + "/users/update/additional_info.json";
	public static String CHANGE_PASSWORD_URL = BASE_API_URL + "/users/update/password.json";
	
	public static String PHONE_AUTH_REQUEST_URL = BASE_API_URL + "/users/auth/request";
	public static String PHONE_AUTH_RESPONSE_URL = BASE_API_URL + "/users/auth/response";
	public static String PHONE_UPDATE_URL = BASE_API_URL + "/users/update/phone_number.json";
	
	public static String MAIN_COVER_URL = BASE_API_URL + "/appinfo/cover.json";
	
	public static String NOTIFICATION_URL = BASE_API_URL + "/notifications/mine.json";
	public static String NOTIFICATION_READ_URL = BASE_API_URL + "/notifications/read.json?";
	public static String NOTIFICATION_DELETE_URL = BASE_API_URL + "/notifications/delete.json";
	public static String NOTIFICATION_READ_All_URL = BASE_API_URL + "/notifications/read_all.json?";
	
	public static String NOTICE_URL = BASE_API_URL + "/posts/notices/dealer.json";
	public static String FAQ_URL = BASE_API_URL + "/posts/faqs/dealer.json";
	
	public static String CAR_BID_LIST_URL = BASE_API_URL + "/onsalecars/bids/list.json";
	public static String CAR_BID_SHOW_URL = BASE_API_URL + "/onsalecars/bids/show.json";
	public static String CAR_BID_SAVE_URL = BASE_API_URL + "/onsalecars/bids/save.json";
	public static String CAR_BID_URL = BASE_API_URL + "/onsalecars/bids/bid.json";
	public static String CAR_BID_CHECK_PAY = BASE_API_URL + "/onsalecars/bids/check_pay.json";
	public static String CAR_BID_PAY = BASE_API_URL + "/onsalecars/bids/pay.json";
	
	public static String CAR_DEALER_LIST_URL = BASE_API_URL + "/onsalecars/dealer/list.json";
	public static String CAR_DEALER_SHOW_URL = BASE_API_URL + "/onsalecars/dealer/show.json";
	public static String CAR_DEALER_SAVE_URL = BASE_API_URL + "/onsalecars/dealer/save.json";
	public static String CAR_DEALER_CHECK_SAVE_URL = BASE_API_URL + "/onsalecars/dealer/check_save.json";
	public static String CAR_DEALER_DELETE_URL = BASE_API_URL + "/onsalecars/dealer/delete.json";
	public static String CAR_DEALER_STATUS_URL = BASE_API_URL + "/onsalecars/dealer/set_status.json";
	public static String CAR_DEALER_COMPLETE_URL = BASE_API_URL + "/onsalecars/dealer/set_status.json";
	public static String CAR_DEALER_REPORT_URL = BASE_API_URL + "/onsalecars/dealer/report.json";

	public static String CAR_DIRECT_NORMAL_SAVE_URL = BASE_API_URL + "/onsalecars/normal/save.json";
	
	public static String CAR_HISTORY_URL = BASE_API_URL + "/carhistories/show.json";
	
	public static String DEALER_SHOW_URL = BASE_API_URL + "/dealers/show.json";
	public static String REVIEW_DEALER_URL = BASE_API_URL + "/dealers/reviews.json";
	public static String REVIEW_WRITE_REPLY_URL = BASE_API_URL + "/dealers/reviews/save.json";
	
	public static String MY_BIDS_URL = BASE_API_URL + "/users/mine/bids.json";
	public static String MY_CAR_URL = BASE_API_URL + "/users/mine/onsalecars.json";
	public static String MY_REVIEW_URL = BASE_API_URL + "/dealers/reviews.json";
	public static String MY_TRANSACTION_URL = BASE_API_URL + "/transactions/mine.json";
	
	public static String EDIT_USER_INFO_UR_COMMON = BASE_API_URL + "/users/update/additional_info.json";
	
	public static String SEARCH_CAR_BRAND = BASE_API_URL + "/cars/brands.json";
	public static String SEARCH_CAR_MODELGROUP = BASE_API_URL + "/cars/modelgroups.json";
	public static String SEARCH_CAR_MODEL = BASE_API_URL + "/cars/models.json";
	public static String SEARCH_CAR_TRIM = BASE_API_URL + "/cars/trims.json";
	public static String SEARCH_CAR_DETAIL_INFO = BASE_API_URL + "/cars/show.json";

	public static String SOCKET_URL = BASE_API_URL + ":23004/";
	public static String GOTO_KAKAO_URL = "http://goto.kakao.com/fgv62tld";

	public static String IMAGE_SERVER_URL = "http://175.126.232.36";
}

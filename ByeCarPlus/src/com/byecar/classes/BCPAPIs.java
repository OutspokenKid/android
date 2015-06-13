package com.byecar.classes;

public class BCPAPIs {

	public static String BASE_API_URL = "http://dev.bye-car.com";
	public static String UPLOAD_URL = BASE_API_URL + "/files/upload/image.json";
	public static String TERM_OF_USE_URL = BASE_API_URL + "/appinfo/policies";
	public static String REGISTER_URL = BASE_API_URL + "/devices/register/android.json";
	public static String PUSH_SETTING_URL = BASE_API_URL + "/users/update/to_get_pushed";
	public static String AREA_URL = BASE_API_URL + "/zipcodes/areas.json";
	public static String AREA_SEARCH_URL = BASE_API_URL + "/dongs/search.json";
	public static String DEALER_COUNT_URL = BASE_API_URL + "/users/count/dealer.json";
	public static String CDN_URL = BASE_API_URL + "/files/upload/link.json";
	
	public static String EMAIL_CHECK_URL = BASE_API_URL + "/users/check/email.json";
	public static String NICKNAME_CHECK_URL = BASE_API_URL + "/users/check/nickname.json";
	public static String SIGN_UP_URL = BASE_API_URL + "/users/join.json";
	public static String SIGN_IN_URL = BASE_API_URL + "/users/login/user.json";
	public static String SIGN_IN_WITH_SNS_URL = BASE_API_URL + "/users/sns_join.json";
	public static String FIND_PW_URL = BASE_API_URL + "/users/find/password.json";
	public static String SIGN_CHECK_URL = BASE_API_URL + "/users/login_check.json";
	public static String SIGN_OUT_URL = BASE_API_URL + "/users/logout.json";
	public static String WITHDRAW_URL = BASE_API_URL + "/users/withdraw.json";
	
	public static String MAIN_COVER_URL = BASE_API_URL + "/appinfo/cover.json";

	public static String CAR_BID_LIST_URL = BASE_API_URL + "/onsalecars/bids/list.json";
	public static String CAR_BID_SHOW_URL = BASE_API_URL + "/onsalecars/bids/show.json";
	public static String CAR_BID_SAVE_URL = BASE_API_URL + "/onsalecars/bids/save.json";
	public static String CAR_BID_CAR_BID_URL = BASE_API_URL + "/onsalecars/bids/bid.json";
	public static String CAR_BID_SELECT_URL = BASE_API_URL + "/onsalecars/bids/select.json";
	public static String CAR_BID_LIKE_URL = BASE_API_URL + "/onsalecars/bids/like.json";
	public static String CAR_BID_UNLIKE_URL = BASE_API_URL + "/onsalecars/bids/unlike.json";
	public static String CAR_BID_DELETE_URL = BASE_API_URL + "/onsalecars/bids/delete.json";
	public static String CAR_BID_COMPLETE_URL = BASE_API_URL + "/onsalecars/bids/set_status.json";
	public static String CAR_BID_REVIEW_URL = BASE_API_URL + "/onsalecars/bids/reviews.json";
	public static String CAR_BID_REQUEST_URL = BASE_API_URL + "/onsalecars/bids/request.json";
	
	public static String CAR_DEALER_LIST_URL = BASE_API_URL + "/onsalecars/dealer/list.json";
	public static String CAR_DEALER_SHOW_URL = BASE_API_URL + "/onsalecars/dealer/show.json";
	public static String CAR_DEALER_SAVE_URL = BASE_API_URL + "/onsalecars/dealer/save.json";
	public static String CAR_DEALER_SET_STATUS_URL = BASE_API_URL + "/onsalecars/dealer/set_status.json";
	public static String CAR_DEALER_CANCEL_URL = BASE_API_URL + "/onsalecars/dealer/cancel.json";
	public static String CAR_DEALER_LIKE_URL = BASE_API_URL + "/onsalecars/dealer/like.json";
	public static String CAR_DEALER_UNLIKE_URL = BASE_API_URL + "/onsalecars/dealer/unlike.json";
	public static String CAR_DEALER_PURCHASES_URL = BASE_API_URL + "/onsalecars/dealer/purchase.json";
	public static String CAR_DEALER_REPORT_URL = BASE_API_URL + "/onsalecars/dealer/report.json";
	
	public static String CAR_DIRECT_NORMAL_LIST_URL = BASE_API_URL + "/onsalecars/normal/list.json";
	public static String CAR_DIRECT_NORMAL_SHOW_URL = BASE_API_URL + "/onsalecars/normal/show.json";
	public static String CAR_DIRECT_NORMAL_SAVE_URL = BASE_API_URL + "/onsalecars/normal/save.json";
	public static String CAR_DIRECT_NORMAL_LIKE_URL = BASE_API_URL + "/onsalecars/normal/like.json";
	public static String CAR_DIRECT_NORMAL_UNLIKE_URL = BASE_API_URL + "/onsalecars/normal/unlike.json";
	public static String CAR_DIRECT_NORMAL_PURCHASES_URL = BASE_API_URL + "/onsalecars/normal/purchase.json";
	public static String CAR_DIRECT_NORMAL_DELETE_URL = BASE_API_URL + "/onsalecars/normal/delete.json";
	public static String CAR_DIRECT_NORMAL_REPORT_URL = BASE_API_URL + "/onsalecars/normal/report.json";

	public static String DEALER_SHOW_URL = BASE_API_URL + "/dealers/show.json";
	public static String REVIEW_DEALER_URL = BASE_API_URL + "/dealers/reviews.json";
	public static String REVIEW_DEALER_SHOW_URL = BASE_API_URL + "/dealers/reviews/show.json";
	public static String REVIEW_DEALER_WRITE_URL = BASE_API_URL + "/dealers/reviews/save.json";
	
	public static String CERTIFIER_SHOW_URL = BASE_API_URL + "/users/certifier/show.json";
	public static String REVIEW_CERTIFIER_URL = BASE_API_URL + "/users/certifier/reviews.json";
	public static String REVIEW_CERTIFIER_SHOW_URL = BASE_API_URL + "/users/certifier/reviews/show.json";
	public static String REVIEW_CERTIFIER_WRITE_URL = BASE_API_URL + "/users/certifier/reviews/save.json";
	
	public static String EDIT_USER_INFO_UR_COMMON = BASE_API_URL + "/users/update/additional_info.json";
	
	public static String SEARCH_CAR_BRAND = BASE_API_URL + "/cars/brands.json";
	public static String SEARCH_CAR_MODELGROUP = BASE_API_URL + "/cars/modelgroups.json";
	public static String SEARCH_CAR_MODEL = BASE_API_URL + "/cars/models.json";
	public static String SEARCH_CAR_TRIM = BASE_API_URL + "/cars/trims.json";
	public static String SEARCH_CAR_DETAIL_INFO = BASE_API_URL + "/cars/show.json";
	
	public static String NOTICE_URL = BASE_API_URL + "/posts/notices/normal.json";
	public static String FAQ_URL = BASE_API_URL + "/posts/faqs/normal.json";
	
	public static String MY_CAR_URL = BASE_API_URL + "/users/mine/onsalecars.json";
	public static String MY_PURCHASE_URL = BASE_API_URL + "/users/mine/purchases.json";
	public static String MY_LIKE_URL = BASE_API_URL + "/users/mine/likes.json";
	public static String MY_REVIEW_URL = BASE_API_URL + "/users/mine/reviews.json";
	public static String NOTIFICATION_URL = BASE_API_URL + "/notifications/mine.json";
	public static String NOTIFICATION_READ_URL = BASE_API_URL + "/notifications/read.json?";
	
	public static String FORUM_LIST_URL = BASE_API_URL + "/posts/forum.json";
	public static String FORUM_DETAIL_URL = BASE_API_URL + "/posts/show.json";
	public static String FORUM_WRITE_URL = BASE_API_URL + "/posts/forum/save.json";
	public static String FORUM_DELETE_URL = BASE_API_URL + "/posts/forum/delete.json";
	public static String FORUM_LIKE_URL = BASE_API_URL + "/posts/like.json";
	public static String FORUM_UNLIKE_URL = BASE_API_URL + "/posts/unlike.json";
	public static String FORUM_REPORT_URL = BASE_API_URL + "/posts/report.json";
	public static String FORUM_REPLY_WRITE_URL = BASE_API_URL + "/posts/reply/save.json";
	public static String FORUM_REPLY_DELETE_URL = BASE_API_URL + "/posts/reply/delete.json";
	public static String FORUM_READ_URL = BASE_API_URL + "/posts/read.json";
	public static String FORUM_BOARDS_URL = BASE_API_URL + "/boards.json";
	public static String FORUM_MY_URL = BASE_API_URL + "/users/mine/posts.json";
	
	public static String VIDEO_LIST_URL = BASE_API_URL + "/posts/videos.json";
	
	public static String PHONE_AUTH_REQUEST_URL = BASE_API_URL + "/users/auth/request";
	public static String PHONE_AUTH_RESPONSE_URL = BASE_API_URL + "/users/auth/response";
	public static String PHONE_UPDATE_URL = BASE_API_URL + "/users/update/phone_number.json";
	
	public static String SOCKET_URL = BASE_API_URL + ":23004/";
	public static String GOTO_KAKAO_URL = "http://goto.kakao.com/fgv62tld";
	
	public static String IMAGE_SERVER_URL = "http://175.126.232.36";
}

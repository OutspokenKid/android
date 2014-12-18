package com.byecar.byecarplus.classes;

public class BCPAPIs {

	public static String BASE_API_URL = "http://byecar.minsangk.com";
	public static String UPLOAD_URL = BASE_API_URL + "/files/upload/image.json";
	public static String TERM_OF_USE_URL = BASE_API_URL + "/appinfo/rules";
	
	public static String EMAIL_CHECK_URL = BASE_API_URL + "/users/check/email.json";
	public static String NICKNAME_CHECK_URL = BASE_API_URL + "/users/check/nickname.json";
	public static String SIGN_UP_FOR_USER_URL = BASE_API_URL + "/users/join.json";
	public static String SIGN_IN_URL = BASE_API_URL + "/users/login.json";
	public static String SIGN_IN_WITH_SNS_URL = BASE_API_URL + "/users/sns_join.json";
	public static String FIND_PW_URL = BASE_API_URL + "/users/find/password.json";
	public static String SIGN_CHECK_URL = BASE_API_URL + "/users/login_check.json";
	
	public static String MAIN_COVER_URL = BASE_API_URL + "/appinfo/cover.json";
	public static String BIDS_LIST_URL = BASE_API_URL + "/onsalecars/bids/list.json";
	public static String BID_SHOW_URL = BASE_API_URL + "/onsalecars/bids/show.json";
	public static String EDIT_USER_INFO_UR_COMMON = BASE_API_URL + "/users/update/additional_info.json";
}

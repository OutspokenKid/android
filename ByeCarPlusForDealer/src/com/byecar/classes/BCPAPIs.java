package com.byecar.classes;

public class BCPAPIs {

	public static String BASE_API_URL = "http://byecar.minsangk.com";
	public static String UPLOAD_URL = BASE_API_URL + "/files/upload/image.json";
	public static String REGISTER_URL = BASE_API_URL + "/devices/register/android.json";
	
	public static String SIGN_CHECK_URL = BASE_API_URL + "/users/login_check.json";
	public static String SIGN_IN_URL = BASE_API_URL + "/users/login.json";
	public static String FIND_PW_URL = BASE_API_URL + "/users/find/password.json";
	public static String TERM_OF_USE_URL = BASE_API_URL + "/appinfo/policies";
	public static String EMAIL_CHECK_URL = BASE_API_URL + "/users/check/email.json";
	public static String SIGN_UP_URL = BASE_API_URL + "/users/join.json";
	public static String SIGN_OUT_URL = BASE_API_URL + "/users/logout.json";
	public static String WITHDRAW_URL = BASE_API_URL + "/users/withdraw.json";
	
	public static String PHONE_AUTH_REQUEST_URL = BASE_API_URL + "/users/auth/request";
	public static String PHONE_AUTH_RESPONSE_URL = BASE_API_URL + "/users/auth/response";
	public static String PHONE_UPDATE_URL = BASE_API_URL + "/users/update/phone_number.json";
}

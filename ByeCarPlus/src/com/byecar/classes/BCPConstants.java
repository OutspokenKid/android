package com.byecar.classes;

public class BCPConstants {

	//구글 플레이 콘솔의 프로젝트 아이디.
	public static final String GCM_SENDER_ID = "479358642498";
	
	public static final int SOCKET_PORT = 23004;
	
	public static final int REQUEST_CAMERA = 1;
	public static final int REQUEST_ALBUM = 2;
	public static final int REQUEST_SEARCH_TRIM = 4;
	public static final int REQUEST_SEARCH_AREA = 5;
	public static final int REQUEST_SEARCH_MONTH = 6;
	
	public static final String PREFS_PUSH = "pushSetting";
	public static final String PREFS_NOTICE = "noticePopup";
	public static final String PREFS_REG = "carRegistration";
	public static final String PREFS_CERTIFY = "certifyPhoneNumber";
	public static final String PREFS_TUTORIAL = "tutorial";
	public static final String PREFS_REQUESTDEALING = "requestDealing";
	
	public static final String COOKIE_NAME_D1 = "BYECAR_D1";
	public static final String COOKIE_NAME_S = "BYECAR_S";
	
	//Items for user.
	public static final int ITEM_NOTICE = 1;
	public static final int ITEM_FAQ = 2;
	public static final int ITEM_NOTIFICATION = 3;
	public static final int ITEM_REVIEW = 4;
	public static final int ITEM_FORUM = 5;
	public static final int ITEM_FORUM_BEST = 6;
	public static final int ITEM_VIDEO = 7;
	public static final int ITEM_AREA_FOR_SEARCH = 8;
	public static final int ITEM_MY_BIDS_REVIEW = 9;
	
	//Items for car.
	public static final int ITEM_CAR_BID = 11;
	public static final int ITEM_CAR_DEALER = 12;
	public static final int ITEM_CAR_DIRECT = 14;
	public static final int ITEM_CAR_BRAND = 15;
	public static final int ITEM_CAR_TEXT = 16;
	public static final int ITEM_CAR_TEXT_DESC = 17;
	public static final int ITEM_CAR_MY = 18;
	public static final int ITEM_CAR_MY_PURCHASE = 19;
	public static final int ITEM_CAR_MY_LIKE = 20;
	
	//Pages for sign.
	public static final int PAGE_SIGN = 1;
	public static final int PAGE_SIGN_IN = 2;
	public static final int PAGE_SIGN_UP_FOR_USER = 3;
	public static final int PAGE_SIGN_UP_FOR_DEALER = 4;
	public static final int PAGE_FIND_PW = 5;
	public static final int PAGE_TERM_OF_USE = 6;
	
	//Pages for user.
	public static final int PAGE_MAIN = 11;
	public static final int PAGE_PHONE_INFO = 12;
	public static final int PAGE_CERTIFY_PHONE_NUMBER = 13;
	public static final int PAGE_DEALER = 14;
	public static final int PAGE_MY = 15;
	public static final int PAGE_WRITE_REVIEW = 16;
	public static final int PAGE_OPENABLE_POST_LIST = 17;
	public static final int PAGE_ASK = 18;
	public static final int PAGE_NOTIFICATION = 19;
	public static final int PAGE_SETTING = 20;
	public static final int PAGE_PROFILE = 21;
	public static final int PAGE_CHANGE_PASSWORD = 22;
	
	//Pages for car.
	public static final int PAGE_CAR_DETAIL = 31;
	public static final int PAGE_CAR_LIST = 32;
	public static final int PAGE_CAR_REGISTRATION = 33;
	public static final int PAGE_SEARCH_CAR = 37;
	public static final int PAGE_TYPE_SEARCH_CAR = 39;
	public static final int PAGE_CAR_HISTORY = 40;
	
	//Pages for community.
	public static final int PAGE_FORUM_LIST = 41;
	public static final int PAGE_MY_FORUM_DETAIL = 42;
	public static final int PAGE_WRITE_FORUM = 43;
	public static final int PAGE_FORUM_DETAIL = 44;
	public static final int PAGE_FORUM_WRITE_REPLY = 45;
	public static final int PAGE_VIDEO_LIST = 46;
	public static final int PAGE_BID_REVIEW_LIST = 47;
	public static final int PAGE_BID_REVIEW_SEARCH = 48;

	public static final int PAGE_WEB_BROWSER = 50;
	public static final int PAGE_SEARCH_AREA = 51;
	
	//For tracking.
	public static final String TRACKING_LIST_CAR_AUCTION = "입찰진행중_Android";
	public static final String TRACKING_LIST_CAR_DEALER = "중고차마켓_Android";
	public static final String TRACKING_LIST_CAR_DIRECT = "직거래_Android";
	public static final String TRACKING_LIST_CAR_ENDED = "입찰완료/후기_Android";
	public static final String TRACKING_REG_AUCTION = "내 차 입찰요청_Android";
	public static final String TRACKING_REG_DIRECT = "내 차 직거래등록_Android";
	public static final String TRACKING_BOARD = "자유게시판_Android";
}

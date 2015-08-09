package com.byecar.classes;

public class BCPConstants {

	//구글 플레이 콘솔의 프로젝트 아이디.
	public static final String GCM_SENDER_ID = "140706259547";
	
	public static final int SOCKET_PORT = 23004;
	
	public static final int REQUEST_CAMERA = 1;
	public static final int REQUEST_ALBUM = 2;
	public static final int REQUEST_ALBUM_MULTI = 3;
	public static final int REQUEST_SEARCH_TRIM = 4;
	public static final int REQUEST_SEARCH_AREA = 5;
	public static final int REQUEST_SEARCH_MONTH = 6;
	public static final int REQUEST_GUIDE = 7;
	
	public static final String PREFS_PUSH = "pushSetting";
	public static final String PREFS_CERTIFY = "certifyPhoneNumber";
	public static final String PREFS_REG = "carRegistratin";
	public static final String PREFS_TUTORIAL = "tutorial";

	public static final String COOKIE_NAME_D1 = "BYECAR_D1";
	public static final String COOKIE_NAME_S = "BYECAR_S";
	
	//Items for dealer.
	public static final int ITEM_NOTICE = 1;
	public static final int ITEM_FAQ = 2;
	public static final int ITEM_NOTIFICATION = 3;
	public static final int ITEM_REVIEW = 4;
	public static final int ITEM_AREA_FOR_SEARCH = 8;
	
	//Items for car.
	public static final int ITEM_CAR_BID_IN_PROGRESS = 10;	//입찰중.
	public static final int ITEM_CAR_BID_MINE = 11;			//내 입찰.
	public static final int ITEM_CAR_BID_SUCCESS = 12;		//입찰 성공.
	public static final int ITEM_CAR_BID_COMPLETED = 13;	//마이페이지 - 거래완료내역 - 바이카옥션.
	public static final int ITEM_CAR_DEALER = 14;			//중고차마켓.
	public static final int ITEM_CAR_DEALER_MINE = 15;		//중고차마켓 - 내 차 보기.
	public static final int ITEM_CAR_DEALER_COMPLETED = 16;	//마이페이지 - 거래완료내역 - 중고차마켓.
	public static final int ITEM_CAR_BRAND = 17;
	public static final int ITEM_CAR_TEXT = 18;
	public static final int ITEM_CAR_TEXT_DESC = 19;
	
	public static final int ITEM_BANNER = 30;
	
	//Pages for sign.
	public static final int PAGE_SIGN = 1;
	public static final int PAGE_SIGN_UP_FOR_COMMON = 2;
	public static final int PAGE_SIGN_UP_FOR_DEALER = 3;
	public static final int PAGE_FIND_PW = 4;
	public static final int PAGE_TERM_OF_USE = 5;
	public static final int PAGE_CERTIFY_PHONE_NUMBER = 6;
	public static final int PAGE_EDIT_DEALER_INFO = 7;
	
	//Pages for dealer.
	public static final int PAGE_MAIN = 10;
	public static final int PAGE_NOTIFICATION = 11;
	public static final int PAGE_MY = 12;
	public static final int PAGE_MY_INFO_REVIEW = 13;
	public static final int PAGE_MY_TICKET = 14;
	public static final int PAGE_MY_COMPLETED_LIST = 15;
	public static final int PAGE_MY_POINT = 16;
	public static final int PAGE_OPENABLE_POST_LIST = 17;
	public static final int PAGE_ASK = 18;
	public static final int PAGE_SETTING = 19;
	public static final int PAGE_DEALER = 20;
	public static final int PAGE_WRITE_REVIEW = 21;
	public static final int PAGE_CHANGE_PASSWORD = 22;
	
	//Pages for car.
	public static final int PAGE_CAR_DETAIL = 31;
	public static final int PAGE_CAR_REGISTRATION = 32;
	public static final int PAGE_CAR_MY = 33;
	public static final int PAGE_CAR_MY_DEALER = 34;
	public static final int PAGE_SEARCH_CAR = 35;
	public static final int PAGE_TYPE_SEARCH_CAR = 36;
	public static final int PAGE_CAR_HISTORY = 37;
	
	public static final int PAGE_WEB_BROWSER = 40;
	public static final int PAGE_SEARCH_AREA = 51;
	
	//For tracking.
	public static final String TRACKING_LIST_CAR_BIDDING = "딜러_입찰중_Android";
	public static final String TRACKING_LIST_CAR_MY_BID = "딜러_내 입찰_Android";
	public static final String TRACKING_LIST_CAR_DEALER = "딜러_중고차마켓_Android";
	public static final String TRACKING_REG_DEALER = "딜러_내 차 등록하기_Android";
}

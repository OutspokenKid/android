package com.zonecomms.golfn.classes;

public class ZoneConstants {
	
	//서버와 통신할때 앱을 구분할 id값.
	public static String PAPP_ID;
	public static String DOMAIN;
	
	//기본 url.
	public static String BASE_URL;
	
	//회원 탈퇴 url.
	public static String URL_FOR_LEAVEMEMBER;
	
	//아이디, 비밀번호 찾기 url.
	public static String URL_FOR_FIND_ID_AND_PW;
	
	//약관 페이지 url.
	public static String URL_FOR_CLAUSE1;
	public static String URL_FOR_CLAUSE2;
	
	//이미지 업로드 url.
	public static final String IMG_BASE_URL = "http://imgupload.itgling.com/GMKT.INC.FileUpload/Upload.aspx";
	
	//구글 플레이 콘솔의 프로젝트 아이디.
	public static final String GCM_SENDER_ID = "726585413534";

	//앱 내부에서 사용되는 리스트 또는 그리드 아이템의 타입.
	public static final int ITEM_NOTICE = 0;
	public static final int ITEM_ARTICLE = 1;
	public static final int ITEM_MESSAGE = 2;
	public static final int ITEM_MESSAGESAMPLE = 3;
	public static final int ITEM_POST = 4;
	public static final int ITEM_SCHEDULE = 5;
	public static final int ITEM_USER = 6;
	public static final int ITEM_TOUR = 7;
	public static final int ITEM_GETHERING = 8;
	
	//리스트, 그리드 페이지의 타입.
	public static final int	TYPE_EVENT = 1;
	public static final int	TYPE_NEWS = 2;
	public static final int	TYPE_LESSON = 3;
	public static final int	TYPE_MANIATV = 4;
	public static final int	TYPE_MARKET1 = 5;		//드라이버
	public static final int	TYPE_MARKET2 = 6;		//우드, 유틸
	public static final int	TYPE_MARKET3 = 7;		//아이언
	public static final int	TYPE_MARKET4 = 8;		//기타
	public static final int	TYPE_ATTENDANCE = 9;
	public static final int	TYPE_REVIEW = 10;
	public static final int	TYPE_QNA = 11;
	public static final int	TYPE_PHOTO = 12;
	public static final int	TYPE_BANNER1 = 13;		//이벤트
	public static final int	TYPE_BANNER2 = 14;		//제품소개
	public static final int	TYPE_BANNER3 = 15;		//나도 한마디
	public static final int	TYPE_BANNER4 = 16;		//브랜드 로고
	public static final int	TYPE_SCHEDULE = 17;
	public static final int	TYPE_GOLFCOURSE1 = 18;	//골프장
	public static final int	TYPE_GOLFCOURSE2 = 19;	//파3 골프장
	public static final int	TYPE_GOLFCOURSE3 = 20;	//연습장
	public static final int	TYPE_GOLFCOURSE4 = 21;	//스크린 골프장
	public static final int	TYPE_MEMBER = 22;
	public static final int	TYPE_GETHRING = 23;
	public static final int	TYPE_GETHERING_SEARCH = 24;
	public static final int	TYPE_POST_GETHERING = 25;
	public static final int	TYPE_POST_GETHERING_INTRO = 26;
	public static final int	TYPE_NOTICE = 27;
	
	//마이홈으로 넘어갈때 쓰이는 메뉴 값.
	public static final int MENU_PROFILE = 0;
	public static final int MENU_STORY = 1;
	public static final int MENU_SCRAP = 2;
	public static final int MENU_MESSAGE = 3;
	
	//엑티비티를 이동할때 쓰이는 키 값.
	public static final int REQUEST_GALLERY = 1;
	public static final int REQUEST_CAMERA = 2;
	public static final int REQUEST_WRITE = 3;
	public static final int REQUEST_EDIT = 4;
	
	//SharedPreferences의 키 값.
	public static final String PREFS_SIGN = "signInfo";
	public static final String PREFS_SPONSER = "sponser";
	public static final String PREFS_PUSH = "pushSetting";
	public static final String PREFS_POPUP = "popup";
}
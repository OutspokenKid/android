package com.zonecomms.clubcage.classes;

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
	
	//앱 내부에서 사용되는 리스트 또는 그리드 아이템의 타입을 나타내는 코드.
	public static final int ITEM_NONE = 0;
	public static final int ITEM_POST = 1;
	public static final int ITEM_NOTICE = 2;
	public static final int ITEM_SCHEDULE = 3;
	public static final int ITEM_VIDEO = 4;
	public static final int ITEM_USER = 5;
	public static final int ITEM_PHOTO = 6;
	public static final int ITEM_EVENT = 7;
	public static final int ITEM_MUSIC = 8;
	public static final int ITEM_MESSAGE = 9;
	public static final int ITEM_MESSAGESAMPLE = 10;
	
	//1. 왁자지껄, 2.생생후기, 3.함께가기, 4.공개수배
	public static final int BOARD_FREETALK = 1;
	public static final int BOARD_REVIEW = 2;
	public static final int BOARD_WITH = 3;
	public static final int BOARD_FIND = 4;
	
	//엑티비티를 이동할때 쓰이는 키 값.
	public static final int REQUEST_GALLERY = 1;
	public static final int REQUEST_CAMERA = 2;
	public static final int REQUEST_WRITE = 3;
	public static final int REQUEST_EDIT = 4;
	public static final int REQUEST_SIGN = 5;

	//서버와 약속된 타입 넘버.
	public static final int	TYPE_NOTICE = 1;
	public static final int	TYPE_EVENT = 2;
	public static final int	TYPE_SCHEDULE = 3;
	public static final int	TYPE_PHOTO = 1;
	public static final int	TYPE_VIDEO = 2;
	public static final int	TYPE_MUSIC = 3;
	
	//SharedPreferences의 키 값.
	public static final String PREFS_SIGN = "signInfo";
	public static final String PREFS_SPONSER = "sponser";
	public static final String PREFS_PUSH = "pushSetting";
	public static final String PREFS_POPUP = "popup";
}
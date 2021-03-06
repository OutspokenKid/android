package com.zonecomms.napp.classes;

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
	public static final int ITEM_NOTICE = 0;
	public static final int ITEM_MESSAGE = 1;
	public static final int ITEM_MESSAGESAMPLE = 2;
	public static final int ITEM_PAPP = 3;
	public static final int ITEM_GETHERING = 4;
	public static final int ITEM_USER = 5;
	public static final int ITEM_POST = 6;
	
	//리스트 페이지의 타입.
	public static final int	TYPE_LIST_NOTICE = 0;
	public static final int	TYPE_LIST_PAPP = 1;
	public static final int	TYPE_LIST_GUIDE = 2;
	public static final int	TYPE_LIST_QNA = 3;
	public static final int TYPE_LIST_GETHERING = 4;
	public static final int TYPE_LIST_GETHERING_SEARCH = 6;
	
	//그리드 페이지의 타입.
	public static final int	TYPE_GRID_PAPP = 0;
	public static final int	TYPE_GRID_POST = 1;
	public static final int	TYPE_GRID_POST_FRIENDS = 2;
	public static final int	TYPE_GRID_POST_CATEGORY = 3;
	public static final int	TYPE_GRID_MEMBER = 4;
	public static final int	TYPE_GRID_MEMBER_SEARCH = 5;
	public static final int	TYPE_GRID_POST_GETHERING = 6;
	public static final int TYPE_GRID_POST_GETHERING_INTRO = 7;
	
	//카테고리 페이지의 타입.
	public static final int TYPE_CATEGORY_THEMA = 1;
	public static final int TYPE_CATEGORY_REGION = 2;
	
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
	public static final int REQUEST_SIGNUP = 5;
	
	//SharedPreferences의 키 값.
	public static final String PREFS_SIGN = "signInfo";
	public static final String PREFS_SPONSER = "sponser";
	public static final String PREFS_PUSH = "pushSetting";
	public static final String PREFS_COLOR = "color";
	public static final String PREFS_POPUP = "popup";
}
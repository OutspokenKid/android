package com.zonecomms.clubmania.classes;

import android.graphics.Color;

public class ZoneConstants {
	
	//서버와 통신할때 앱을 구분할 id값.
	public static final String PAPP_ID = "clubmania";
	
	//메인페이지의 308x308 그리드의 색상 값.
	public static final int COLOR_MAIN_BIG = Color.rgb(137, 137, 137);
	//메인페이지의 150x150 그리드의 색상 값.
	public static final int COLOR_MAIN_SMALL = Color.rgb(181, 181, 181);
	//게시판 팝업 그리드의 색상 값.
	public static final int COLOR_MAIN_BOARDMENU = Color.rgb(137, 137, 137);
	//버튼 커버의 색상 값.
	public static final int COLOR_BUTTON_COVER = Color.argb(150, 137, 137, 137);
	
	public static final String DOMAIN = "http://www.napp.co.kr";
//	public static final String DOMAIN = "http://112.169.61.103";
	
	//구글 플레이 콘솔의 프로젝트 아이디.
	public static final String GCM_SENDER_ID = "726585413534";

	//앱 내부에서 사용되는 리스트 아이템의 타입.
	public static final int ITEM_NOTICE = 0;
	public static final int ITEM_VIDEO = 1;
	public static final int ITEM_EVENT = 2;
	public static final int ITEM_MUSIC = 3;
	public static final int ITEM_MESSAGE = 4;
	public static final int ITEM_MESSAGESAMPLE = 5;
	public static final int ITEM_SMARTPASS = 6;
	
	//앱 내부에서 사용되는 그리드 아이템의 타입.
	public static final int ITEM_POST = 0;
	public static final int ITEM_SCHEDULE = 1;
	public static final int ITEM_USER = 2;
	public static final int ITEM_PHOTO = 3;
	public static final int ITEM_PAPP = 4;
	
	//1.클럽, 2.라운지.
	public static final int BOARD_CLUB = 1;
	public static final int BOARD_LOUNGE = 2;
	
	//엑티비티를 이동할때 쓰이는 키 값.
	public static final int REQUEST_GALLERY = 1;
	public static final int REQUEST_CAMERA = 2;
	public static final int REQUEST_WRITE = 3;
	public static final int REQUEST_EDIT = 4;

	//리스트, 그리드 페이지의 타입.
	public static final int	TYPE_NOTICE = 1;
	public static final int	TYPE_VIDEO = 2;
	public static final int	TYPE_MUSIC = 3;
	public static final int	TYPE_SMARTPASS = 4;
	public static final int	TYPE_MEMBER = 5;
	public static final int	TYPE_PHOTO = 6;
	public static final int	TYPE_EVENT = 7;
	public static final int	TYPE_POST = 8;
	public static final int	TYPE_SCHEDULE = 9;
	public static final int	TYPE_PAPP_PHOTO = 10;
	public static final int	TYPE_PAPP_SCHEDULE = 11;
	
	//SharedPreferences의 키 값.
	public static final String PREFS_SIGN = "signInfo";
	public static final String PREFS_SPONSER = "sponser";
	public static final String PREFS_PUSH = "pushSetting";
	public static final String PREFS_POPUP = "popup";

	//기본 url.
	public static final String BASE_URL = DOMAIN + "/externalapi/public/";
	
	//이미지 업로드 url.
	public static final String IMG_BASE_URL = "http://imgupload.itgling.com/GMKT.INC.FileUpload/Upload.aspx";
	
	//회원 탈퇴 url.
	public static final String URL_FOR_LEAVEMEMBER = DOMAIN + "/withdraw/page";
	
	//아이디, 비밀번호 찾기 url.
	public static final String URL_FOR_FIND_ID_AND_PW = DOMAIN + "/m_find_info/page";
	
	//약관 페이지 url.
	public static final String URL_FOR_CLAUSE1 = DOMAIN + "/resource/id_integration.html";
	public static final String URL_FOR_CLAUSE2 = DOMAIN + "/resource/terms.html";
}
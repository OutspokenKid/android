package com.zonecomms.golfn;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gcm.GCMBaseIntentService;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.golfn.classes.ApplicationManager;
import com.zonecomms.golfn.classes.ZoneConstants;
import com.zonecomms.golfn.fragments.GetheringPage;
import com.zonecomms.golfn.fragments.MessagePage;
import com.zonecomms.golfn.fragments.PostPage;

/**
 * v1.0.1
 * @author HyungGunKim
 *
 * v1.0.1 - Encode message. 
 */
public class GCMIntentService extends GCMBaseIntentService {
	
	public static NotificationManager notiManager;
	private boolean sound = true;
	private boolean vibration = true;
	
	public GCMIntentService() {
		
		super(ZoneConstants.GCM_SENDER_ID);
	}
	
	public GCMIntentService(String projectId) {
		
		super(projectId);
	}
	
	@Override
	protected void onMessage(Context context, Intent intent) {

		try {
			if(!SharedPrefsUtils.checkPrefs(ZoneConstants.PREFS_PUSH, "sound")) {
				sound = SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_PUSH, "sound", true);
			}
			
			if(!SharedPrefsUtils.checkPrefs(ZoneConstants.PREFS_PUSH, "vibration")) {
				vibration = SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_PUSH, "vibration", true);
			}
			
			sound = SharedPrefsUtils.getBooleanFromPrefs(ZoneConstants.PREFS_PUSH, "sound");
			vibration = SharedPrefsUtils.getBooleanFromPrefs(ZoneConstants.PREFS_PUSH, "vibration");
		} catch(Exception e) {
			LogUtils.trace(e);
		}
		
		try {
			if(intent.getExtras() != null) {
				
				/**
				 * push_msg : 사용자에게 보여줄 메세지. 
				 * msg_type : 푸쉬의 종류.
				 * member_id : 댓글인 경우 해당 글의 페이지로 넘길 때 사용.
				 * post_member_id : 메세지를 보낸 유저의 아이디.
				 * spot_nid : 메세지의 경우 유저 홈으로 들어갈 때 사용.
				 * sb_id : 모임 관련 푸시의 경우 모임 페이지로 진입할 때 사용.
				 */
				String push_msg = null;
				String msg_type = null;
				String member_id = null;
				String post_member_id = null;
				int spot_nid = 0;
				String sb_id = null;
				
				Bundle bundle = intent.getExtras();
				
				if(bundle.containsKey("push_msg")) {
					push_msg = bundle.getString("push_msg");
				}
				
				if(bundle.containsKey("msg_type")) {
					msg_type = bundle.getString("msg_type");
				}
				
				if(bundle.containsKey("member_id")) {
					member_id = bundle.getString("member_id");
				}
				
				if(bundle.containsKey("post_member_id")) {
					post_member_id = bundle.getString("post_member_id");
				}
				
				if(bundle.containsKey("spot_nid")) {
					spot_nid = Integer.parseInt(bundle.getString("spot_nid"));
				}
				
				if(bundle.containsKey("sb_id")) {
					sb_id = bundle.getString("sb_id");
				}
				
				LogUtils.log("########################\nGCMIntentService.onRegistered." +
						"\nbundle : " + bundle.toString() +
						"\npush_msg : " + push_msg +
						"\nmsg_type : " + msg_type +
						"\nmember_id : " + member_id +
						"\npost_member_id : " + post_member_id +
						"\nspot_nid : " + spot_nid +
						"\nsb_id : " + sb_id +
						"\n########################");
				
				handleMessage(context, push_msg, msg_type, member_id, post_member_id, spot_nid, sb_id);
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	@Override
	protected void onError(Context arg0, String arg1) {
	}
	
	@Override
	protected void onRegistered(Context context, String regId) {

		try {
			AsyncStringDownloader.download(
					ZoneConstants.BASE_URL + "push/androiddevicetoken" +
							"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
							"&registration_id=" + regId,
					null, null);
		} catch(Exception e) {
			//Do nothing.
		}
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
	}

	/**
	 * 
	 * 앱이 실행중인 경우.
	 * 전체푸시 - Notification(앱 실행).
	 * 메세지 - 해당 사용자와 대화 중인 경우 바로 갱신, 아닌 경우 Notification.
	 * 댓글 - Notification(해당 글로).
	 * 
	 * 앱이 실행중이지 않은 경우.
	 * 전체 푸시 - Notification(앱 실행).
	 * 메세지 - Notification(앱 실행, 메세지 페이지로).
	 * 댓글 - Notification(앱 실행, 해당 글로).
	 * 
	 * @param push_msg : 띄워줄 메세지.
	 * @param msg_type : 메세지 타입 - 000 (전체 푸시), 010 (메시지), 021 (댓글)
	 * @param member_id : 메세지의 경우 보낸 사람의 id.
	 * @param spot_nid : 댓글인 경우 글의 nid.
	 * @param sb_id : 모임 관련 푸시인 경우 모임의 sb_id.
	 */
	public void handleMessage(Context context, String push_msg, String msg_type, 
			String member_id, String post_member_id, int spot_nid, String sb_id) {

		if(msg_type == null) {
			return;
		}
		
		//해당 메세지 페이지가 열려있는 경우.
		if(ApplicationManager.getFragmentsSize() != 0 
				&& msg_type != null 
				&& msg_type.equals("010")
				&& member_id != null
				&& ApplicationManager.getTopFragment() instanceof MessagePage) {
			
			try {
				MessagePage mp = (MessagePage) ApplicationManager.getTopFragment();
				
				if(mp.getFriend_member_id() != null
						&& mp.getFriend_member_id().equals(member_id)) {
					mp.onRefreshPage();
				}
			} catch(Exception e) {
			}
			
		//해당 글 상세페이지가 열려있는 경우.
		} else if((msg_type.equals("021") || msg_type.equals("022"))
				&& ApplicationManager.getTopFragment() instanceof PostPage
				&& ((PostPage)ApplicationManager.getTopFragment()).getSpotNid() == spot_nid) {
			ApplicationManager.getTopFragment().onRefreshPage();
			((PostPage)ApplicationManager.getTopFragment()).setNeedFullScroll(true);
		} else {

			String uriString = "";

			//000 : 전체 푸시 (post_member_id : 보낸사람, member_id : 받을사람, push_msg : 메시지)
			if(msg_type.equals("000")) {
				try {
					uriString = "popup://android.zonecomms.com/?message=" + URLEncoder.encode(push_msg, "utf-8");
				} catch(Exception e) {
				}
				
			//010 : 메시지 푸시 (post_member_id : 보낸사람, member_id : 받을사람, push_msg : 메시지)
			} else if(msg_type.equals("010")) {
				uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/message?member_id=" + post_member_id;
				
			//021 : type1,type2 글의 댓글 (spot_nid : type1 or type2 글번호, member_id : 받을사람, push_msg : 댓글내용)
			} else if(msg_type.equals("021")) {
				uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/post?title=InStory" +
						"&spot_nid=" + spot_nid +
						"&isGethering=false";
			
			//022 : 모임 댓글 (spot_nid : 모임 글번호, member_id : 받을사람, sb_id : 모임ID, push_msg : 댓글내용)
			} else if(msg_type.equals("022")) {
				uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/post?title=InStory" +
						"&spot_nid=" + spot_nid +
						"&isGethering=true";
				
			//031 : type3 글 댓글 (spot_nid : type3 글번호, member_id : 받을사람, push_msg : 댓글내용)
			} else if(msg_type.equals("031")) {
				uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/article?spot_nid=" + spot_nid;
				
			//050 : 모임 상태변경 - 승인, 추방, 거부 ( member_id : 받을사람, push_msg : 상태변경 메시지, sb_id : 모임ID)	
			} else if(msg_type.equals("050")) {
				
				//해당 모임 페이지가 열려있는 경우,
				if(ApplicationManager.getTopFragment() instanceof GetheringPage
						&& sb_id != null
						&& sb_id.equals(((GetheringPage)ApplicationManager.getTopFragment()).getSb_id())) {
					//showMessage.
					
					//새로고침(인덱스0).
					((GetheringPage)ApplicationManager.getTopFragment()).showMenu(0);
					return;
				//해당 모임 페이지가 열려있지 않은 경우,
				} else {
					
					try {
						//해당 모임 페이지 열어주기.
						uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/gethering"
								+ "?sb_id=" + URLEncoder.encode(sb_id, "utf-8");
					} catch (UnsupportedEncodingException e) {
						LogUtils.trace(e);
						return;
					}
				}
			}
			
			showNotification(context, push_msg, uriString);
		}
	}
	
	public void showNotification(Context context, String push_msg, String uriString) {
		
		try {
			if(notiManager == null) {
				notiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			}

			int time = (int)(System.currentTimeMillis() % 10000);
			
			Intent intent = new Intent(context, IntentHandlerActivity.class);
			
			if(uriString != null) {
				intent.setData(Uri.parse(uriString));
			}
			
			intent.putExtra("time", time);
			
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			String title = getString(R.string.app_name);
			int pushSetting = Notification.DEFAULT_LIGHTS;
			
			if(sound) {
				pushSetting |= Notification.DEFAULT_SOUND;
			}
			
			if(vibration) {
				pushSetting |= Notification.DEFAULT_VIBRATE;
			}
			
			Notification noti = new NotificationCompat.Builder(context)
						.setContentTitle(title)
						.setContentText(push_msg)
						.setSmallIcon(R.drawable.app_icon)
						.setDefaults(pushSetting)
						.setTicker(push_msg)
						.setAutoCancel(true)
						.setContentIntent(pIntent)
						.build();
			
			notiManager.notify(time, noti);
		} catch (Exception e) {
			LogUtils.trace(e);
		}
	}

	@Override
	protected String[] getSenderIds(Context context) {
		
		String[] senderIds = null;
		
		try {
			senderIds = super.getSenderIds(context);
		} catch(Exception e) {
			LogUtils.trace(e);
		} finally {
			if(senderIds == null) {
				senderIds = new String[]{ZoneConstants.GCM_SENDER_ID};
			}
		}
		return senderIds;
	}
}

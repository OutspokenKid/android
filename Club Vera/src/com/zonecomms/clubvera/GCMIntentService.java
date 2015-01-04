package com.zonecomms.clubvera;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.android.gcm.GCMBaseIntentService;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.zonecomms.clubvera.classes.ZoneConstants;
import com.zonecomms.clubvera.classes.ZonecommsApplication;
import com.zonecomms.clubvera.fragments.MessagePage;
import com.zonecomms.clubvera.fragments.PostPage;
import com.zonecomms.common.utils.AppInfoUtils;

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
				 */
				String push_msg = null;
				String msg_type = null;
				String member_id = null;
				String post_member_id = null;
				int spot_nid = 0;
				
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
				
				LogUtils.log("########################\nGCMIntentService.onRegistered." +
						"\nbundle : " + bundle.toString() +
						"\npush_msg : " + push_msg +
						"\nmsg_type : " + msg_type +
						"\nmember_id : " + member_id +
						"\npost_member_id : " + post_member_id +
						"\nspot_nid : " + spot_nid +
						"\n########################");

				String id = SharedPrefsUtils.getStringFromPrefs(ZoneConstants.PREFS_SIGN, "id");
				
				//로그인이 안되어있는 경우 전체 푸시가 아니면 제한.
				if((id == null || id.length() == 0) && !push_msg.equals("000")) {
					return;
				}
				
				handleMessage(context, push_msg, msg_type, member_id, post_member_id, spot_nid);
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	@Override
	protected void onError(Context arg0, String arg1) {
	}
	
	@Override
	protected void onRegistered(final Context context, final String regId) {

		try {
			String url = ZoneConstants.BASE_URL + "push/androiddevicetoken" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&registration_id=" + regId;
			
			if(!url.contains("member_id=")) {
				url += "&member_id=";
			}
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {
				
				@Override
				public void onError(String url) {

					LogUtils.log("###GCMIntentService.onError.  \nurl : " + url);
				}
				
				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					LogUtils.log("###GCMIntentService.onCompleted.  \nresult : " + objJSON);
				}
			});
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
	 */
	public void handleMessage(Context context, String push_msg, String msg_type, 
			String member_id, String post_member_id, int spot_nid) {

		if(msg_type == null) {
			return;
		}

		if(ZonecommsApplication.getActivity() != null 
				&& ZonecommsApplication.getActivity().getFragmentsSize() != 0) {
			
			if("010".equals(msg_type)
					&& member_id != null
					&& ZonecommsApplication.getActivity().getTopFragment() instanceof MessagePage) {
				final MessagePage mp = (MessagePage) ZonecommsApplication.getActivity().getTopFragment();
				
				if(mp.getFriend_member_id() != null
						&& mp.getFriend_member_id().equals(post_member_id)) {
					ZonecommsApplication.getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							mp.refreshPage();
						}
					});
				
					return;
				}
			} else if(("021".equals(msg_type))
					&& ZonecommsApplication.getActivity().getTopFragment() instanceof PostPage
					&& ((PostPage)ZonecommsApplication.getActivity().getTopFragment()).getSpotNid() == spot_nid) {
				ZonecommsApplication.getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						((PostPage)ZonecommsApplication.getActivity().getTopFragment()).setNeedToShowBottom(true);
						ZonecommsApplication.getActivity().getTopFragment().refreshPage();
					}
				});
				
				return;
			}
		}
		
		LogUtils.log("###GCMIntentService.handleMessage.  mainActivity is not running.");

		String uriString = context.getString(R.string.sb_id);
		
		if("000".equals(msg_type)) {
			try {
				uriString = "popup://android.zonecomms.com/?message=" + URLEncoder.encode(push_msg, "utf-8");
			} catch(Exception e) {
			}
		} else if("010".equals(msg_type)) {
			uriString += "://android.zonecomms.com/message?member_id=" + post_member_id;
		} else if("021".equals(msg_type)) {
			uriString += "://android.zonecomms.com/post?spot_nid=" + spot_nid;
		}
		
		showNotification(context, push_msg, uriString);
	}
	
	public void showNotification(Context context, String push_msg, String uriString) {
		
		LogUtils.log("###GCMIntentService.showNotification.  " +
				"\npush_msg : " + push_msg + "\nuriString : " + uriString);
		
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

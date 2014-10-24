package com.cmons.cph;

import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.PushObject;
import com.google.android.gcm.GCMBaseIntentService;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;

/**
 * v1.0.1
 * @author HyungGunKim
 *
 * v1.0.1 - Encode message. 
 */
public class GCMIntentService extends GCMBaseIntentService {
	
	public static NotificationManager notiManager;
	
	public GCMIntentService() {
		
		super(CphConstants.GCM_SENDER_ID);
	}
	
	public GCMIntentService(String projectId) {
		
		super(projectId);
	}
	
	@Override
	protected void onMessage(Context context, Intent intent) {

		LogUtils.log("###GCMIntentService.onMessage.  " +
				"\nintent : " + intent);
		
		/*
		{
			"id":null,
			"message":"푸시 메세지 테스트",
			"title":"청평화 알림",
			"sound":true,
			"vibrate":true,
			"uri":"cph:\/\/wholesales\/orders"
		}
		*/
		try {
			if(intent != null && intent.getExtras() != null) {
				BasicClientCookie cookie = SharedPrefsUtils.getCookie(CphConstants.PREFS_COOKIE_CPH_D1);
				
				//로그인이 안되어있는 경우 전체 푸시가 아니면 제한.
				if(cookie == null) {
					LogUtils.log("###GCMIntentService.onMessage.  token is null, return.");
					return;
				}
				
				PushObject po = new PushObject(new JSONObject(intent.getStringExtra("msg")));
				
				//앱이 실행중이고 탈퇴 푸시인 경우 바로 처리.
				if(ShopActivity.getInstance() != null
						&& po != null
						&& po.uri != null
						&& po.uri.contains("/disable")) {
					ShopActivity.getInstance().checkSignStatus();
					
				//그 외의 경우 Notification 전송.
				} else {
					LogUtils.log("###GCMIntentService.onMessage.  App is not running, show notification."
							+ "\nmsg : " + intent.getStringExtra("msg")
							+ "\nmessage : " + po.message
							+ "\nuri : " + po.uri);
					showNotification(context, po);
				}
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
			String url = CphConstants.BASE_API_URL + "users/token_register/android" +
					"?user_id=" + ShopActivity.getInstance().user.getId() +
					"&device_token=" + regId;
					
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {
				
				@Override
				public void onError(String url) {

					LogUtils.log("###GCMIntentService.onRegistered.onError.  \nurl : " + url);
				}
				
				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					LogUtils.log("###GCMIntentService.onRegistered.onCompleted.  \nresult : " + objJSON);
				}
			});
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
	}

	/**
	 * 탈퇴 푸시가 아닌 경우.
	 */
	public void showNotification(Context context, PushObject pushObject) {

		LogUtils.log("###GCMIntentService.showNotification.  " +
				"\npushObject : " + pushObject);

		try {
			String message = pushObject.message;
			String uri = pushObject.uri;
			
			LogUtils.log("###GCMIntentService.showNotification.  " +
					"\nmessage : " + message + 
					"\nuri : " + uri);
			
			if(notiManager == null) {
				notiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			}

			int time = (int)(System.currentTimeMillis() % 10000);
			
			Intent pushIntent = new Intent(context, IntentHandlerActivity.class);
			pushIntent.putExtra("pushObject", pushObject);

			pushIntent.putExtra("time", time);
			pushIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			pushIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			pushIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			
			int pushSetting = Notification.DEFAULT_LIGHTS
					| Notification.DEFAULT_SOUND
					| Notification.DEFAULT_VIBRATE;
			String push_msg = pushObject.message;
			
			PendingIntent pIntent = PendingIntent.getActivity(context, pushObject.id, 
					pushIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			Notification noti = new NotificationCompat.Builder(context)
						.setContentTitle(getString(R.string.app_name))
						.setContentText(message)
						.setSmallIcon(R.drawable.app_icon_noti)
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
				senderIds = new String[]{CphConstants.GCM_SENDER_ID};
			}
		}
		return senderIds;
	}
}

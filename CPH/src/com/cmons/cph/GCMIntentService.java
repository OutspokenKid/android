package com.cmons.cph;

import org.json.JSONObject;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.cmons.cph.classes.CphConstants;
import com.google.android.gcm.GCMBaseIntentService;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.StringUtils;

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
		
		try {
			if(intent != null && intent.getExtras() != null) {

				String token = SharedPrefsUtils.getStringFromPrefs(CphConstants.PREFS_SIGN, "id");
				
				//로그인이 안되어있는 경우 전체 푸시가 아니면 제한.
				if(StringUtils.isEmpty(token)) {
					return;
				}

				//앱이 실행중인 경우 ShopActivity로 바로 넘김.
				if(ShopActivity.getInstance() != null) {
					handleIntent(context, intent);
					
				//앱이 실행중이지 않은 경우 Notification 전송.
				} else {
					showNotification(context, intent);
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

		//http://cph.minsangk.com/users/token_register/ios?user_id=minsangk&device_token=1234
		
		LogUtils.log("############\n1\n1\n1\n1\n1.onRegistered.  regId : " + regId);
		
		try {
			String url = CphConstants.BASE_API_URL + "users/token_register/android" +
					"?user_id=" + SharedPrefsUtils.getStringFromPrefs(CphConstants.PREFS_SIGN, "id") +
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
	 * 앱이 실행중인 경우.
	 */
	public void handleIntent(Context context, Intent intent) {
		
		LogUtils.log("###GCMIntentService.handleIntent.  " +
				"\nintent : " + intent);
		
		//처리할 부분이 떠있는 경우.
		//처리할 부분이 떠있지 않은 경우 showNotification으로 넘김..
	}

	/**
	 * 앱이 실행중이지 않은 경우.
	 */
	public void showNotification(Context context, Intent intent) {

		LogUtils.log("###GCMIntentService.showNotification.  " +
				"\nintent : " + intent);
		
//		String message = intent.getStringExtra("message");
//		String uri = intent.getStringExtra("url");
//		
//		LogUtils.log("###GCMIntentService.showNotification.  " +
//				"\npush_msg : " + message + 
//				"\nuri : " + uri);
		
//		try {
//			if(notiManager == null) {
//				notiManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//			}
//
//			int time = (int)(System.currentTimeMillis() % 10000);
//			
//			Intent pushIntent = new Intent(context, IntentHandlerActivity.class);
//			
//			if(uriString != null) {
//				pushIntent.setData(Uri.parse(uriString));
//			}
//			
//			pushIntent.putExtra("time", time);
//			
//			pushIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			pushIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//			pushIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			
//			PendingIntent pIntent = PendingIntent.getActivity(context, 0, pushIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//			
//			Notification noti = new NotificationCompat.Builder(context)
//						.setContentTitle(getString(R.string.app_name))
//						.setContentText(push_msg)
//						.setSmallIcon(R.drawable.app_icon)
//						.setDefaults(pushSetting)
//						.setTicker(push_msg)
//						.setAutoCancel(true)
//						.setContentIntent(pIntent)
//						.build();
//			
//			notiManager.notify(time, noti);
//		} catch (Exception e) {
//			LogUtils.trace(e);
//		}
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

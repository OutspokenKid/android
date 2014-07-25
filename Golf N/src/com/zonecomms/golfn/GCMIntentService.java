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
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import com.google.android.gcm.GCMBaseIntentService;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.golfn.classes.ApplicationManager;
import com.zonecomms.golfn.classes.ZoneConstants;
import com.zonecomms.golfn.fragments.ArticlePage;
import com.zonecomms.golfn.fragments.GetheringPage;
import com.zonecomms.golfn.fragments.MainPage;
import com.zonecomms.golfn.fragments.MessagePage;
import com.zonecomms.golfn.fragments.PostPage;

/**
 * v1.0.1
 * @author HyungGunKim
 *
 * v1.0.1 - Encode message. 
 */
public class GCMIntentService extends GCMBaseIntentService {
	
	private static NotificationManager notiManager;
	private static boolean sound = true;
	private static boolean vibration = true;
	
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
	 * @param msg_type : 메세지 타입 - 000 (전체 푸시), 010 (메시지), 021 (댓글), 022 (모임 댓글), 031 (type3 댓글), 050 (모임 상태 변경)
	 * @param member_id : 메세지의 경우 보낸 사람의 id.
	 * @param spot_nid : 댓글인 경우 글의 nid.
	 * @param sb_id : 모임 관련 푸시인 경우 모임의 sb_id.
	 */
	public void handleMessage(Context context, final String push_msg, String msg_type, 
			String member_id, String post_member_id, int spot_nid, String sb_id) {
		
		if(msg_type == null) {
			return;
		}
		
		PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

		//앱이 실행 중, 메세지 푸시, 메인 페이지인 경우 'New' 보이도록.
		if(ApplicationManager.getFragmentsSize() != 0
				&& msg_type.equals("010")
				&& ApplicationManager.getTopFragment() instanceof MainPage) {
			
			ApplicationManager.getInstance().getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					((MainPage)ApplicationManager.getTopFragment()).showHasNewMessage();
				}
			});
		}
		
		//화면이 켜져있고 앱이 실행 중인 경우.
		if (pm.isScreenOn() && ApplicationManager.getFragmentsSize() != 0) {
			
			//해당 메세지 페이지가 열려있는 경우.
			if(msg_type.equals("010")
					&& member_id != null
					&& ApplicationManager.getTopFragment() instanceof MessagePage
					&& post_member_id.equals(((MessagePage) ApplicationManager.getTopFragment()).getFriend_member_id())) {
				ApplicationManager.getInstance().getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						ApplicationManager.getTopFragment().onRefreshPage();
					}
				});
				return;
				
			//해당 글 상세페이지가 열려있는 경우.
			} else if((msg_type.equals("021") || msg_type.equals("022"))
					&& ApplicationManager.getTopFragment() instanceof PostPage
					&& ((PostPage)ApplicationManager.getTopFragment()).getSpotNid() == spot_nid) {
				ApplicationManager.getInstance().getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						((PostPage)ApplicationManager.getTopFragment()).setNeedToShowBottom(true);
						ApplicationManager.getTopFragment().onRefreshPage();
					}
				});
				return;
				
			//해당 기사 상세페이지가 열려있는 경우.
			} else if(msg_type.equals("031")
					&& ApplicationManager.getTopFragment() instanceof ArticlePage
					&& ((ArticlePage)ApplicationManager.getTopFragment()).getSpotNid() == spot_nid) {
				ApplicationManager.getInstance().getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						((ArticlePage)ApplicationManager.getTopFragment()).setNeedToShowBottom(true);
						ApplicationManager.getTopFragment().onRefreshPage();
					}
				});
				return;
				
			//해당 모임 페이지가 열려있는 경우,
			} else if(msg_type.equals("050")
					&& ApplicationManager.getTopFragment() instanceof GetheringPage
					&& sb_id != null
					&& sb_id.equals(((GetheringPage)ApplicationManager.getTopFragment()).getSb_id())) {
				ApplicationManager.getInstance().getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						//새로고침(인덱스0).
						((GetheringPage)ApplicationManager.getTopFragment()).showMenu(0);
						ToastUtils.showToast(push_msg);
					}
				});
				return;
			}
		}
		
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
					"&isGethering=false" +
					"&isNeedToShowBottom=true";
		
		//022 : 모임 댓글 (spot_nid : 모임 글번호, member_id : 받을사람, sb_id : 모임ID, push_msg : 댓글내용)
		} else if(msg_type.equals("022")) {
			uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/post?title=InStory" +
					"&spot_nid=" + spot_nid +
					"&isGethering=true" +
					"&isNeedToShowBottom=true";
			
		//031 : type3 글 댓글 (spot_nid : type3 글번호, member_id : 받을사람, push_msg : 댓글내용)
		} else if(msg_type.equals("031")) {
			uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/article?spot_nid=" + spot_nid;
			
		//050 : 모임 상태변경 - 승인, 추방, 거부 ( member_id : 받을사람, push_msg : 상태변경 메시지, sb_id : 모임ID)	
		} else if(msg_type.equals("050")) {

			try {
				//해당 모임 페이지 열어주기.
				uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/gethering"
						+ "?sb_id=" + URLEncoder.encode(sb_id, "utf-8");
			} catch (UnsupportedEncodingException e) {
				LogUtils.trace(e);
				return;
			}
		}
		
		if(pm.isScreenOn()) {
			showNotification(context, push_msg, uriString);
		} else {
			showDialog(context, push_msg, msg_type, member_id, post_member_id, spot_nid, sb_id, uriString);
		}
	}

	public void showDialog(Context context, String push_msg, String msg_type, 
			String member_id, String post_member_id, int spot_nid, String sb_id, String uriString) {
		
		Intent intent = new Intent(context, DialogActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		intent.putExtra("push_msg", push_msg);
		intent.putExtra("msg_type", msg_type);
		intent.putExtra("member_id", member_id);
		intent.putExtra("post_member_id", post_member_id);
		intent.putExtra("spot_nid", spot_nid);
		intent.putExtra("sb_id", sb_id);
		intent.putExtra("uriString", uriString);
		
	    context.startActivity(intent);
	}
	
	public static void showNotification(Context context, String push_msg, String uriString) {
		
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
			
			String title = context.getString(R.string.app_name);
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

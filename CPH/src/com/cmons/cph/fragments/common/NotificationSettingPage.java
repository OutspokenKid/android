package com.cmons.cph.fragments.common;

import org.json.JSONObject;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForShop;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.ToastUtils;

public class NotificationSettingPage extends CmonsFragmentForShop {

	private Button btnNotification;
	private TextView tvNotification;
	private Button btnDoNotDisturb;
	private TextView tvTime;
	private TextView tvDoNotDisturb;
	
	private boolean allowNotification;
	private int startHour, startMinute, endHour, endMinute;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.notificationSettingPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.notificationSettingPage_ivBg);
		
		btnNotification = (Button) mThisView.findViewById(R.id.notificationSettingPage_btnNotification);
		tvNotification = (TextView) mThisView.findViewById(R.id.notificationSettingPage_tvNotification);
		btnDoNotDisturb = (Button) mThisView.findViewById(R.id.notificationSettingPage_btnDoNotDisturb);
		tvTime = (TextView) mThisView.findViewById(R.id.notificationSettingPage_tvTime);
		tvDoNotDisturb = (TextView) mThisView.findViewById(R.id.notificationSettingPage_tvDoNotDisturb);
	}

	@Override
	public void setVariables() {

		title = "알림설정";
		
		startHour = SharedPrefsUtils.getIntegerFromPrefs(CphConstants.PREFS_DISTURB, "sh");
		startMinute = SharedPrefsUtils.getIntegerFromPrefs(CphConstants.PREFS_DISTURB, "sm");
		endHour = SharedPrefsUtils.getIntegerFromPrefs(CphConstants.PREFS_DISTURB, "eh");
		endMinute = SharedPrefsUtils.getIntegerFromPrefs(CphConstants.PREFS_DISTURB, "em");
		
		allowNotification = SharedPrefsUtils.getBooleanFromPrefs(CphConstants.PREFS_NOTIFICATION, "allow");
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
		
		//알림 설정.
		if(allowNotification) {
			btnNotification.setBackgroundResource(R.drawable.setting_notification_btn_switch_b);
		} else{
			btnNotification.setBackgroundResource(R.drawable.setting_notification_btn_switch_a);
		}
		
		//시간 설정.
		checkTime();
	}

	@Override
	public void setListeners() {

		btnNotification.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeNotificationSetting();
			}
		});

		btnDoNotDisturb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				String title = "시간 선택";
				String[] strings = new String[] {
						"시작시간",
						"종료시간",
						"방해금지모드 해제"
				};
				
				mActivity.showSelectDialog(title, strings, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						if(which == 0) {
							setStartTime();
						} else if(which == 1) {
							setEndTime();
						} else {
							disableTime();
						}
					}
				});
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		int p = ResizeUtils.getSpecificLength(20);
		
		//btnNotification.
		rp = (RelativeLayout.LayoutParams) btnNotification.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
		rp.topMargin = ResizeUtils.getSpecificLength(84);
		
		//tvNotification.
		rp = (RelativeLayout.LayoutParams) tvNotification.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(140);
		rp.leftMargin = ResizeUtils.getSpecificLength(44);
		FontUtils.setFontSize(tvNotification, 30);
		tvNotification.setPadding(p, p, p, p);
		
		//btnDoNotDisturb.
		rp = (RelativeLayout.LayoutParams) btnDoNotDisturb.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
		
		//tvTime.
		rp = (RelativeLayout.LayoutParams) tvTime.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
		rp.rightMargin = ResizeUtils.getSpecificLength(120);
		FontUtils.setFontSize(tvTime, 40);
		
		//tvDoNotDisturb.
		rp = (RelativeLayout.LayoutParams) tvDoNotDisturb.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(150);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		FontUtils.setFontSize(tvDoNotDisturb, 30);
		tvDoNotDisturb.setPadding(p, p, p, p);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_notificationsetting;
	}

	@Override
	public void refreshPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.setting_bg2;
	}

//////////////////// Custom classes.
	
	public void changeNotificationSetting() {
		
		//http://cph.minsangk.com/users/update/to_get_pushed?to_get_pushed=Y
		String url = CphConstants.BASE_API_URL + "users/update/to_get_pushed" +
				"?to_get_pushed=" + (allowNotification? "N" : "Y");
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("NotificationSettingPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("NotificationSettingPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						allowNotification = !allowNotification;
						
						SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_NOTIFICATION, "allow", allowNotification);
						
						if(allowNotification) {
							btnNotification.setBackgroundResource(R.drawable.setting_notification_btn_switch_b);
						} else{
							btnNotification.setBackgroundResource(R.drawable.setting_notification_btn_switch_a);
						}
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void setStartTime() {
		
		TimePickerDialog.OnTimeSetListener otsl = new OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

				startHour = hourOfDay;
				startMinute = minute;
				checkTime();
			}
		};
		
		new TimePickerDialog(mContext, otsl, startHour, startMinute, false).show();
	}
	
	public void setEndTime() {
		
		TimePickerDialog.OnTimeSetListener otsl = new OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

				endHour = hourOfDay;
				endMinute = minute;
				checkTime();
			}
		};
		
		new TimePickerDialog(mContext, otsl, endHour, endMinute, false).show();
	}
	
	public void checkTime() {
		
		String text = "";

		text += getTimeText(startHour, startMinute);
		text += "\n";
		text += getTimeText(endHour, endMinute);
		
		tvTime.setText(text);
		submitTime();
	}
	
	public String getTimeText(int hour, int minute) {
		
		String text = "";
		
		hour %= 24;
		minute %= 60;
		
		if(hour < 12) {
			text += "AM ";
			
			if(hour == 0) {
				text += "12:";
			} else {
				text += hour + ":";
			}
		} else if(hour == 12) {
			text += "PM " + hour + ":";
		} else {
			text += "PM " + (hour - 12) + ":";
		}
		
		if(minute < 10) {
			text += "0" + minute;
		} else {
			text += minute;
		}
		
		return text;
	}

	public void submitTime() {
		
		String startTimeString = null;
		
		if(startHour < 10) {
			startTimeString = "0" + startHour;
		} else {
			startTimeString = "" + startHour;
		}
		
		if(startMinute < 10) {
			startTimeString += "0" + startMinute;
		} else {
			startTimeString += "" + startMinute;
		}
		
		String endTimeString = null;
		
		if(endHour < 10) {
			endTimeString = "0" + endHour;
		} else {
			endTimeString = "" + endHour;
		}
		
		if(endMinute < 10) {
			endTimeString += "0" + endMinute;
		} else {
			endTimeString += "" + endMinute;
		}
		
		//http://cph.minsangk.com/users/update/dnd_time?dnd_time=2300-0900
		String url = CphConstants.BASE_API_URL + "users/update/dnd_time" +
				"?dnd_time=" + startTimeString + "-" + endTimeString;

		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("NotificationSettingPage.submitTime.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("NotificationSettingPage.submitTime.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					if(objJSON.getInt("result") == 1) {
						SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_DISTURB, "sh", startHour);
						SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_DISTURB, "sm", startMinute);
						SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_DISTURB, "eh", endHour);
						SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_DISTURB, "em", endMinute);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	public void disableTime() {
		
		//http://cph.minsangk.com/users/update/dnd_time?dnd_time=disable
		String url = CphConstants.BASE_API_URL + "users/update/dnd_time" +
				"?dnd_time=disable";
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("NotificationSettingPage.disableTime.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("NotificationSettingPage.disableTime.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					if(objJSON.getInt("result") == 1) {
						SharedPrefsUtils.removeVariableFromPrefs(CphConstants.PREFS_DISTURB, "sh");
						SharedPrefsUtils.removeVariableFromPrefs(CphConstants.PREFS_DISTURB, "sm");
						SharedPrefsUtils.removeVariableFromPrefs(CphConstants.PREFS_DISTURB, "eh");
						SharedPrefsUtils.removeVariableFromPrefs(CphConstants.PREFS_DISTURB, "em");
						startHour = 0;
						startMinute = 0;
						endHour = 0;
						endMinute = 0;
						tvTime.setText("사용 안함");
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
}

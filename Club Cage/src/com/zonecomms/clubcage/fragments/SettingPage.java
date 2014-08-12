package com.zonecomms.clubcage.fragments;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.WebBrowser;
import com.outspoken_kid.views.WebBrowser.OnActionWithKeywordListener;
import com.outspoken_kid.views.holo.holo_light.HoloConstants;
import com.outspoken_kid.views.holo.holo_light.HoloStyleButton;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;
import com.outspoken_kid.views.holo.holo_light.HoloStyleSpinnerButton;
import com.outspoken_kid.views.holo.holo_light.HoloStyleSpinnerPopup;
import com.outspoken_kid.views.holo.holo_light.HoloStyleSpinnerPopup.OnItemClickedListener;
import com.outspoken_kid.views.holo.holo_light.HoloStyleTextView;
import com.zonecomms.clubcage.IntentHandlerActivity;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.clubcage.classes.ZonecommsApplication;
import com.zonecomms.clubcage.classes.ZonecommsFragment;
import com.zonecomms.common.utils.AppInfoUtils;

public class SettingPage extends ZonecommsFragment {

	private boolean isOnMain = true;
	
	private FrameLayout mainLayout;
	private FrameLayout frameForMain;
	private FrameLayout frameForEditPw;
	private FrameLayout frameForEditPush;
	private WebBrowser webBrowser;

	private HoloStyleTextView tvId;
	private HoloStyleTextView tvPw;
	private HoloStyleEditText etEmail;
	private HoloStyleSpinnerButton spEmail;
	private HoloStyleEditText etEmail2;
	private HoloStyleSpinnerPopup pEmail;
	private HoloStyleEditText etPhoneNumber;
	private HoloStyleTextView tvPush;
	private HoloStyleTextView tvSNS;
	private HoloStyleTextView tvSignOut;
	private HoloStyleTextView tvLeaveMember;
	private HoloStyleTextView tvBaseProfile;
	private HoloStyleTextView tvAddedProfile;
	
	private HoloStyleEditText etPwBefore;
	private HoloStyleEditText etPw;
	private HoloStyleEditText etConfirmPw;
	
	private CheckBox cbNotiAll;
	private CheckBox cbNotiMessage;
	private CheckBox cbNotiReply;
	private CheckBox cbSound;
	private CheckBox cbVibration;
	
	@Override
	public void bindViews() {
		
		mainLayout = (FrameLayout) mThisView.findViewById(R.id.settingPage_mainLayout);
	}

	@Override
	public void setVariables() {
		
		title = getString(R.string.setting);
	}

	@Override
	public void createPage() {
		
		pEmail = new HoloStyleSpinnerPopup(mContext);
		pEmail.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		pEmail.setTitle("Select you Email");
		pEmail.addItem(getString(R.string.mail1));
		pEmail.addItem(getString(R.string.mail2));
		pEmail.addItem(getString(R.string.mail3));
		pEmail.addItem(getString(R.string.mail4));
		pEmail.addItem(getString(R.string.mail5));
		pEmail.addItem(getString(R.string.mail6));
		pEmail.addItem(getString(R.string.inputDirectly));
		pEmail.notifyDataSetChanged();
		
		addFrameForMain();
		addFrameForEditPw();
		addFrameForEditPush();
		addWebBrowser();
		
		mainLayout.addView(pEmail);
	}

	@Override
	public void setListeners() {
		
		tvPw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showFrameForEditPw();
			}
		});

		tvPush.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showFrameForEditPush();
			}
		});	
	
		pEmail.setOnItemClickedListener(new OnItemClickedListener() {
			
			@Override
			public void onItemClicked(int position, String itemString) {
				
				if(itemString.equals(getString(R.string.inputDirectly))) {
					spEmail.setText("");
					etEmail2.setVisibility(View.VISIBLE);
					etEmail2.requestFocus();
					SoftKeyboardUtils.showKeyboard(mContext, etEmail2);
				} else {
					etEmail2.setVisibility(View.INVISIBLE);
				}
			}
		});

		tvSNS.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				ToastUtils.showToast(R.string.preparingService);
			}
		});
		
		tvSignOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				mainActivity.showAlertDialog(R.string.signOut, R.string.wannaSignOut, 
						R.string.confirm, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								mainActivity.signOut();
							}
						});
			}
		});

		tvLeaveMember.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String url = ZoneConstants.URL_FOR_LEAVEMEMBER + 
						"?member_id=" + ZonecommsApplication.myInfo.getMember_id();
				webBrowser.open(url, null);
			}
		});

		tvBaseProfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/baseprofile";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		
		tvAddedProfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/addedprofile";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
	
		cbNotiAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				String url = ZoneConstants.BASE_URL + "member/update/setting_push" +
						"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
						"&type=1" +
						"&data=";
				
				if(isChecked) {
					url += "Y";
				} else{
					url += "N";
				}

				DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

					@Override
					public void onError(String url) {
						// TODO Auto-generated method stub		
					}

					@Override
					public void onCompleted(String url, JSONObject objJSON) {

						try {
							LogUtils.log("SettingPage.onCompleted." + "\nurl : " + url
									+ "\nresult : " + objJSON);
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (OutOfMemoryError oom) {
							LogUtils.trace(oom);
						}
					}
				});
			}
		});
		
		cbNotiMessage.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				String url = ZoneConstants.BASE_URL + "member/update/setting_push" +
						"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
						"&type=2" +
						"&data=";
				
				if(isChecked) {
					url += "Y";
				} else{
					url += "N";
				}

				DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

					@Override
					public void onError(String url) {
						// TODO Auto-generated method stub		
					}

					@Override
					public void onCompleted(String url, JSONObject objJSON) {

						try {
							LogUtils.log("SettingPage.onCompleted." + "\nurl : " + url
									+ "\nresult : " + objJSON);
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (OutOfMemoryError oom) {
							LogUtils.trace(oom);
						}
					}
				});
			}
		});
		
		cbNotiReply.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				String url = ZoneConstants.BASE_URL + "member/update/setting_push" +
						"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
						"&type=3" +
						"&data=";
				
				if(isChecked) {
					url += "Y";
				} else{
					url += "N";
				}
				DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

					@Override
					public void onError(String url) {
						// TODO Auto-generated method stub		
					}

					@Override
					public void onCompleted(String url, JSONObject objJSON) {

						try {
							LogUtils.log("SettingPage.onCompleted." + "\nurl : " + url
									+ "\nresult : " + objJSON);
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (OutOfMemoryError oom) {
							LogUtils.trace(oom);
						}
					}
				});
			}
		});
	}

	@Override
	public void setSizes() {
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, frameForEditPw, 2, 0, null);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, frameForEditPush, 2, 0, null);
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, frameForMain, 2, 0, null);
		ResizeUtils.viewResize(540, 80, tvId, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 60, 0, 0}, new int[]{200, 0, 20, 0});
		ResizeUtils.viewResize(540, 80, tvPw, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 160, 0, 0}, new int[]{200, 0, 20, 0});
		ResizeUtils.viewResize(270, 70, etEmail, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 260, 0, 0});
		ResizeUtils.viewResize(230, 70, spEmail, 2, Gravity.LEFT|Gravity.TOP, new int[]{360, 260, 0, 0});
		ResizeUtils.viewResize(180, 70, etEmail2, 2, Gravity.LEFT|Gravity.TOP, new int[]{360, 260, 0, 0});
		ResizeUtils.viewResize(540, 80, etPhoneNumber, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 360, 0, 0}, new int[]{200, 0, 20, 0});
		ResizeUtils.viewResize(540, 80, tvPush, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 460, 0, 0});
		ResizeUtils.viewResize(540, 80, tvSNS, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 560, 0, 0});
		ResizeUtils.viewResize(540, 80, tvSignOut, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 660, 0, 0});
		ResizeUtils.viewResize(540, 80, tvLeaveMember, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 760, 0, 0});
		ResizeUtils.viewResize(540, 80, tvBaseProfile, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 860, 0, 0});
		ResizeUtils.viewResize(540, 80, tvAddedProfile, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 960, 0, 0});
		
		FontUtils.setFontSize(tvId.getTextView(), 22);
		FontUtils.setFontSize(tvPw.getTextView(), 22);
		FontUtils.setFontSize(etEmail.getEditText(), 22);
		FontUtils.setFontSize(tvPw.getTextView(), 22);
		FontUtils.setFontSize(etEmail2.getEditText(), 22);
		FontUtils.setFontSize(etPhoneNumber.getEditText(), 22);
		FontUtils.setFontSize(tvPush.getTextView(), 22);
		FontUtils.setFontSize(tvSNS.getTextView(), 22);
		FontUtils.setFontSize(tvSignOut.getTextView(), 22);
		FontUtils.setFontSize(tvLeaveMember.getTextView(), 22);
		FontUtils.setFontSize(tvAddedProfile.getTextView(), 22);
		FontUtils.setFontSize(tvBaseProfile.getTextView(), 22);
	}

	@Override
	public void downloadInfo() {
	}

	@Override
	public void setPage(boolean successDownload) {
		
		hideLoadingView();
		
		if(successDownload) {
			tvId.setText(ZonecommsApplication.myInfo.getMember_id());
			tvPw.setText("**********");
			
			String strEmail = ZonecommsApplication.myInfo.getMember_email();
			
			if(!StringUtils.isEmpty(strEmail) && strEmail.contains("@")) {
				etEmail.getEditText().setText(strEmail.split("@")[0]);
				spEmail.setText(strEmail.split("@")[1]);
			}
			
			etPhoneNumber.getEditText().setText(ZonecommsApplication.myInfo.getMobile_no());
		} else {
			ToastUtils.showToast(R.string.failToLoadUserInfo);
		}
	}

	@Override
	public String getTitleText() {

		return title;
	}

	@Override
	public int getContentViewId() {
		
		return R.layout.page_setting;
	}
	
	@Override
	public boolean onBackPressed() {

		if(webBrowser.getVisibility() == View.VISIBLE) {
			webBrowser.handleBackKey();
			return true;
		} if(pEmail.getVisibility() == View.VISIBLE) {
			pEmail.hidePopup();
			return true;
		} else if(!isOnMain) {
			showFrameForMain();
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refreshPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		super.onResume();
		
		mainActivity.showTitleBar();
		mainActivity.getTitleBar().hideCircleButton();
		mainActivity.getTitleBar().showHomeButton();
		mainActivity.getTitleBar().hideWriteButton();
		
		if(mainActivity.getSponserBanner() != null) {
			mainActivity.getSponserBanner().hideBanner();
		}
		
		setPage(true);
	}
	
	@Override
	public void finish(boolean needAnim, boolean isBeforeMain) {
		
		SoftKeyboardUtils.hideKeyboard(mContext, etPw);
		super.finish(needAnim, isBeforeMain);
	}
	
/////////////////////// Custom methods.
	
	public void addFrameForMain() {

		ScrollView scrollView = new ScrollView(mContext);
		scrollView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		scrollView.setFillViewport(true);
		mainLayout.addView(scrollView);
		
		frameForMain = new FrameLayout(mContext);
		frameForMain.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		frameForMain.setClickable(true);
		scrollView.addView(frameForMain);

		tvId = new HoloStyleTextView(mContext);
		tvId.getTextView().setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
		tvId.setBgOn(false);
		frameForMain.addView(tvId);
		
		tvPw = new HoloStyleTextView(mContext);
		tvPw.getTextView().setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
		tvPw.setText("**********");
		tvPw.setBgOn(false);
		frameForMain.addView(tvPw);

		etEmail = new HoloStyleEditText(mContext);
		etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		frameForMain.addView(etEmail);
		
		spEmail = new HoloStyleSpinnerButton(mContext);
		spEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(pEmail != null) {
					
					SoftKeyboardUtils.hideKeyboard(mContext, spEmail.getTextView());
					spEmail.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							pEmail.showPopup();
						}
					}, 200);
				}
			}
		});
		spEmail.setText(R.string.selectEmail);
		frameForMain.addView(spEmail);
		pEmail.setTargetTextView(spEmail.getTextView());
		
		etEmail2 = new HoloStyleEditText(mContext);
		etEmail2.setHint(R.string.inputDirectly);
		etEmail2.setVisibility(View.INVISIBLE);
		etEmail2.setBackgroundColor(Color.TRANSPARENT);
		etEmail2.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		frameForMain.addView(etEmail2);
		
		etPhoneNumber = new HoloStyleEditText(mContext);
		etPhoneNumber.getEditText().setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
		frameForMain.addView(etPhoneNumber);
		
		tvPush = new HoloStyleTextView(mContext);
		tvPush.getTextView().setGravity(Gravity.CENTER);
		tvPush.setText(R.string.pushSetting);
		tvPush.setBgOn(false);
		frameForMain.addView(tvPush);
		
		tvSNS = new HoloStyleTextView(mContext);
		tvSNS.getTextView().setGravity(Gravity.CENTER);
		tvSNS.setText(R.string.SNSLinkSetting);
		tvSNS.setBgOn(false);
		frameForMain.addView(tvSNS);
		
		tvSignOut = new HoloStyleTextView(mContext);
		tvSignOut.getTextView().setGravity(Gravity.CENTER);
		tvSignOut.setText(R.string.signOut);
		tvSignOut.setBgOn(false);
		frameForMain.addView(tvSignOut);
		
		tvLeaveMember = new HoloStyleTextView(mContext);
		tvLeaveMember.getTextView().setGravity(Gravity.CENTER);
		tvLeaveMember.setText(R.string.leaveMember);
		tvLeaveMember.setBgOn(false);
		frameForMain.addView(tvLeaveMember);
		
		tvBaseProfile = new HoloStyleTextView(mContext);
		tvBaseProfile.getTextView().setGravity(Gravity.CENTER);
		tvBaseProfile.setText(R.string.baseProfile);
		tvBaseProfile.setBgOn(false);
		frameForMain.addView(tvBaseProfile);
		
		tvAddedProfile = new HoloStyleTextView(mContext);
		tvAddedProfile.getTextView().setGravity(Gravity.CENTER);
		tvAddedProfile.setText(R.string.addedProfile);
		tvAddedProfile.setBgOn(false);
		frameForMain.addView(tvAddedProfile);
		
		TextView id = new TextView(mContext);
		ResizeUtils.viewResize(170, 80, id, 2, Gravity.LEFT|Gravity.TOP, new int[]{70, 60, 0, 0});
		id.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		id.setText(R.string.id);
		id.setTextColor(HoloConstants.COLOR_HOLO_TEXT_HINT);
		FontUtils.setFontSize(id, 22);
		frameForMain.addView(id);
		
		TextView pw = new TextView(mContext);
		ResizeUtils.viewResize(170, 80, pw, 2, Gravity.LEFT|Gravity.TOP, new int[]{70, 160, 0, 0});
		pw.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		pw.setText(R.string.password);
		pw.setTextColor(HoloConstants.COLOR_HOLO_TEXT_HINT);
		FontUtils.setFontSize(pw, 22);
		frameForMain.addView(pw);
		
		TextView at = new TextView(mContext);
		ResizeUtils.viewResize(60, 60, at, 2, Gravity.LEFT|Gravity.TOP, new int[]{310, 260, 0, 0});
		at.setText("@");
		at.setGravity(Gravity.CENTER);
		FontUtils.setFontSize(at, 36);
		frameForMain.addView(at);
		
		TextView phoneNumber = new TextView(mContext);
		ResizeUtils.viewResize(170, 80, phoneNumber, 2, Gravity.LEFT|Gravity.TOP, new int[]{70, 360, 0, 0});
		phoneNumber.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		phoneNumber.setText(R.string.phoneNumber);
		phoneNumber.setTextColor(HoloConstants.COLOR_HOLO_TEXT_HINT);
		FontUtils.setFontSize(phoneNumber, 22);
		frameForMain.addView(phoneNumber);
		
		HoloStyleButton btnSubmit = new HoloStyleButton(mContext);
		ResizeUtils.viewResize(540, 70, btnSubmit, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 1060, 0, 0});
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(etEmail.getEditText().getText() == null) {
					ToastUtils.showToast(R.string.invalidEmail);
					return;
				}
				
				if(etPhoneNumber.getEditText().getText() == null) {
					ToastUtils.showToast(R.string.invalidPhoneNumber);
					return;
				}
				
				String emailText = etEmail.getEditText().getText().toString();
				String phoneNumber = etPhoneNumber.getEditText().getText().toString();
				
				if(StringUtils.isEmpty(emailText)
						|| emailText.length() < 2
						|| emailText.length() > 20
						|| emailText.contains(" ")) {
					ToastUtils.showToast(R.string.invalidEmail);
					return;
				}

				//직접 입력
				if(etEmail2.getVisibility() == View.VISIBLE) {
					emailText += "@" + etEmail2.getEditText().getText();
				//이메일 선택
				} else {
					emailText += "@" + spEmail.getTextView().getText(); 
				}
				
				if(StringUtils.isEmpty(phoneNumber)) {
					ToastUtils.showToast(R.string.invalidPhoneNumber);
					return;
				}
				
				submit(emailText, phoneNumber);
			}
		});
		btnSubmit.setText(R.string.submit);
		FontUtils.setFontSize(btnSubmit.getTextView(), 26);
		frameForMain.addView(btnSubmit);
		
		View line = new View(mContext);
		ResizeUtils.viewResize(1, 1160, line, 2, Gravity.LEFT, null);
		frameForMain.addView(line);
	}
	
	public void addFrameForEditPw() {
		
		frameForEditPw = new FrameLayout(mContext);
		frameForEditPw.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		frameForEditPw.setVisibility(View.INVISIBLE);
		frameForEditPw.setBackgroundColor(Color.parseColor("#f5f5f5"));
		frameForEditPw.setClickable(true);
		mainLayout.addView(frameForEditPw);
		
		etPwBefore = new HoloStyleEditText(mContext);
		ResizeUtils.viewResize(540, 70, etPwBefore, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 50, 0, 0});
		etPwBefore.setHint(R.string.hintForCurrentPw);
		etPwBefore.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
		FontUtils.setFontSize(etPwBefore.getEditText(), 22);
		frameForEditPw.addView(etPwBefore);
		
		etPw = new HoloStyleEditText(mContext);
		ResizeUtils.viewResize(540, 70, etPw, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 150, 0, 0});
		etPw.setHint(R.string.hintForNewPw);
		etPw.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
		FontUtils.setFontSize(etPw.getEditText(), 22);
		frameForEditPw.addView(etPw);
		
		etConfirmPw = new HoloStyleEditText(mContext);
		ResizeUtils.viewResize(540, 70, etConfirmPw, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 250, 0, 0});
		etConfirmPw.setHint(R.string.hintForConfirmNewPw);
		etConfirmPw.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
		FontUtils.setFontSize(etConfirmPw.getEditText(), 22);
		frameForEditPw.addView(etConfirmPw);
		
		HoloStyleButton btnSubmit = new HoloStyleButton(mContext);
		ResizeUtils.viewResize(540, 70, btnSubmit, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 350, 0, 0});
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String before = "";
				String pw = "";
				
				if(etPwBefore.getEditText().getText() != null) {
					before = etPwBefore.getEditText().getText().toString();
					
					if(before.length() < 4 
							|| before.length() > 20) {
						ToastUtils.showToast(R.string.invalidPw);
						return;
					}
				} else {
					ToastUtils.showToast(R.string.invalidPw);
					return;
				}
				
				if(etPw.getEditText().getText() != null) {
					pw = etPw.getEditText().getText().toString();
					
					if(pw.length() < 4 
							|| pw.length() > 20) {
						ToastUtils.showToast(R.string.invalidPw);
						return;
					} else {
						if(etConfirmPw.getEditText().getText() == null
								&& !etConfirmPw.getEditText().getText().equals(pw)) {
							ToastUtils.showToast(R.string.invalidConfirmPw);
						}
					}
				} else {
					ToastUtils.showToast(R.string.invalidPw);
					return;
				}
				
				editPw(before, pw);
			}
		});
		btnSubmit.setText(R.string.submit);
		FontUtils.setFontSize(btnSubmit.getTextView(), 26);
		frameForEditPw.addView(btnSubmit);
	}
	
	public void addFrameForEditPush() {
		
		frameForEditPush = new FrameLayout(mContext);
		frameForEditPush.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		frameForEditPush.setVisibility(View.INVISIBLE);
		frameForEditPush.setBackgroundColor(Color.parseColor("#f5f5f5"));
		frameForEditPush.setClickable(true);
		mainLayout.addView(frameForEditPush);
		
		TextView tvNotiAll = new TextView(mContext);
		ResizeUtils.viewResize(300, 70, tvNotiAll, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 25, 0, 0});
		tvNotiAll.setGravity(Gravity.CENTER_VERTICAL);
		tvNotiAll.setText(R.string.notificationAll);
		tvNotiAll.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvNotiAll, 26);
		frameForEditPush.addView(tvNotiAll);
		
		TextView tvNotiMessage = new TextView(mContext);
		ResizeUtils.viewResize(300, 70, tvNotiMessage, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 125, 0, 0});
		tvNotiMessage.setGravity(Gravity.CENTER_VERTICAL);
		tvNotiMessage.setText(R.string.notificationMessage);
		tvNotiMessage.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvNotiMessage, 26);
		frameForEditPush.addView(tvNotiMessage);
		
		TextView tvNotiReply = new TextView(mContext);
		ResizeUtils.viewResize(300, 70, tvNotiReply, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 225, 0, 0});
		tvNotiReply.setGravity(Gravity.CENTER_VERTICAL);
		tvNotiReply.setText(R.string.notificationReply);
		tvNotiReply.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvNotiReply, 26);
		frameForEditPush.addView(tvNotiReply);
		
		TextView tvSound = new TextView(mContext);
		ResizeUtils.viewResize(300, 70, tvSound, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 325, 0, 0});
		tvSound.setGravity(Gravity.CENTER_VERTICAL);
		tvSound.setText("Sound");
		tvSound.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvSound, 26);
		frameForEditPush.addView(tvSound);
		
		TextView tvVibration = new TextView(mContext);
		ResizeUtils.viewResize(300, 70, tvVibration, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 425, 0, 0});
		tvVibration.setGravity(Gravity.CENTER_VERTICAL);
		tvVibration.setText("Vibration");
		tvVibration.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvVibration, 26);
		frameForEditPush.addView(tvVibration);
		
		cbNotiAll = new CheckBox(mContext);
		ResizeUtils.viewResize(70, 70, cbNotiAll, 2, Gravity.RIGHT|Gravity.TOP, new int[]{0, 25, 50, 0});
		frameForEditPush.addView(cbNotiAll);
		
		cbNotiMessage = new CheckBox(mContext);
		ResizeUtils.viewResize(70, 70, cbNotiMessage, 2, Gravity.RIGHT|Gravity.TOP, new int[]{0, 125, 50, 0});
		frameForEditPush.addView(cbNotiMessage);
		
		cbNotiReply = new CheckBox(mContext);
		ResizeUtils.viewResize(70, 70, cbNotiReply, 2, Gravity.RIGHT|Gravity.TOP, new int[]{0, 225, 50, 0});
		frameForEditPush.addView(cbNotiReply);
		
		cbSound = new CheckBox(mContext);
		ResizeUtils.viewResize(70, 70, cbSound, 2, Gravity.RIGHT|Gravity.TOP, new int[]{0, 325, 50, 0});
		frameForEditPush.addView(cbSound);
		
		cbVibration = new CheckBox(mContext);
		ResizeUtils.viewResize(70, 70, cbVibration, 2, Gravity.RIGHT|Gravity.TOP, new int[]{0, 425, 50, 0});
		frameForEditPush.addView(cbVibration);
		
		HoloStyleButton btnSubmit = new HoloStyleButton(mContext);
		ResizeUtils.viewResize(540, 70, btnSubmit, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 525, 0, 0});
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				editPushSetting();
			}
		});
		btnSubmit.setText(R.string.submit);
		FontUtils.setFontSize(btnSubmit.getTextView(), 26);
		frameForEditPush.addView(btnSubmit);
		
		View view = new View(mContext);
		ResizeUtils.viewResize(600, 4, view, 2, Gravity.LEFT|Gravity.TOP, new int[]{20, 310, 0, 0});
		view.setBackgroundColor(HoloConstants.COLOR_HOLO_TARGET_ON);
		frameForEditPush.addView(view);
	}

	public void addWebBrowser() {
		
		webBrowser = new WebBrowser(mContext);
		webBrowser.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		webBrowser.putAction("withdraw-success", new OnActionWithKeywordListener() {
			
			@Override
			public void onActionWithKeyword() {
				mainActivity.signOut();
			}
		});
		mainLayout.addView(webBrowser);
	}
	
	public void showFrameForMain() {
		
		AlphaAnimation aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(200);
		
		if(frameForEditPw.getVisibility() == View.VISIBLE) {
			frameForEditPw.setVisibility(View.INVISIBLE);
			frameForEditPw.startAnimation(aaOut);
		}
		
		if(frameForEditPush.getVisibility() == View.VISIBLE) {
			frameForEditPush.setVisibility(View.INVISIBLE);
			frameForEditPush.startAnimation(aaOut);
		}
		
		isOnMain = true;
	}
	
	public void showFrameForEditPw() {
		
		if(frameForEditPw.getVisibility() != View.VISIBLE) {
			AlphaAnimation aaIn = new AlphaAnimation(0, 1);
			aaIn.setDuration(200);
			
			frameForEditPw.setVisibility(View.VISIBLE);
			frameForEditPw.startAnimation(aaIn);
			isOnMain = false;
		}
	}
	
	public void showFrameForEditPush() {
		
		boolean sound = false;
		boolean vibration = false;
		
		try {
			if(!SharedPrefsUtils.checkPrefs(ZoneConstants.PREFS_PUSH, "sound")) {
				sound = SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_PUSH, "sound", true);
			}
			
			if(!SharedPrefsUtils.checkPrefs(ZoneConstants.PREFS_PUSH, "vibration")) {
				sound = SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_PUSH, "vibration", true);
			}
			
			sound = SharedPrefsUtils.getBooleanFromPrefs(ZoneConstants.PREFS_PUSH, "sound");
			vibration = SharedPrefsUtils.getBooleanFromPrefs(ZoneConstants.PREFS_PUSH, "vibration");
		} catch(Exception e) {
			LogUtils.trace(e);
		}
		
		cbSound.setChecked(sound);
		cbVibration.setChecked(vibration);
		
		if(frameForEditPush.getVisibility() != View.VISIBLE) {
			AlphaAnimation aaIn = new AlphaAnimation(0, 1);
			aaIn.setDuration(200);
			
			frameForEditPush.setVisibility(View.VISIBLE);
			frameForEditPush.startAnimation(aaIn);
			isOnMain = false;
		}
		
		String url = ZoneConstants.BASE_URL + "member/push/info" +
				"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL);

		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {
				// TODO Auto-generated method stub		
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("SettingPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.has("push_all") && !StringUtils.isEmpty(objJSON.getString("push_all"))
							&& objJSON.getString("push_all").equals("Y")) {
						cbNotiAll.setChecked(true);
					} else {
						cbNotiAll.setChecked(false);
					}
					
					if(objJSON.has("push_message") && !StringUtils.isEmpty(objJSON.getString("push_message"))
							&& objJSON.getString("push_message").equals("Y")) {
						cbNotiMessage.setChecked(true);
					} else {
						cbNotiMessage.setChecked(false);
					}
					
					if(objJSON.has("push_reply") && !StringUtils.isEmpty(objJSON.getString("push_reply"))
							&& objJSON.getString("push_reply").equals("Y")) {
						cbNotiReply.setChecked(true);
					} else {
						cbNotiReply.setChecked(false);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	public void editPw(String member_pwd, String new_member_pwd) {
		
		String url = ZoneConstants.BASE_URL + "member/update/password" +
				"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
				"&member_pwd=" + member_pwd +
				"&new_member_pwd=" + new_member_pwd;
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {
				
				ToastUtils.showToast(R.string.failToSubmitPassword);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("SettingPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.has("errorCode")
							&& objJSON.getInt("errorCode") == 1) {
						ToastUtils.showToast(R.string.submitCompleted);
						etPwBefore.getEditText().setText("");
						etPw.getEditText().setText("");
						etConfirmPw.getEditText().setText("");
						showFrameForMain();
						SoftKeyboardUtils.hideKeyboard(mContext, etConfirmPw);
					} else {
						ToastUtils.showToast(R.string.failToSubmitPassword);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToSubmitPassword);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToSubmitPassword);
				}
			}
		});
	}
	
	public void editPushSetting() {
		
		boolean sound = cbSound.isChecked();
		boolean vibration = cbVibration.isChecked();
		
		try {
			SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_PUSH, "sound", sound);
			SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_PUSH, "vibration", vibration);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
		
		showFrameForMain();
	}

	public void submit(final String email, final String phoneNumber) {
		
		showLoadingView();
		
		try {
			String url = ZoneConstants.BASE_URL + "member/update/mobile_email" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&mobile_no=" + URLEncoder.encode(phoneNumber, "UTF-8") +
					"&email=" + URLEncoder.encode(email, "UTF-8");
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {
					
					LogUtils.log("SettingPage.onError.  url : " + url);
					ToastUtils.showToast(R.string.failToSubmitBaseProfile);
					setPage(false);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("SettingPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("errorCode") == 1) {
							ToastUtils.showToast(R.string.submitCompleted);
							ZonecommsApplication.myInfo.setMobile_no(phoneNumber);
							ZonecommsApplication.myInfo.setMember_email(email);
							mainActivity.closeTopPage();
							mainActivity.getTopFragment().refreshPage();
							setPage(true);
						} else {
							ToastUtils.showToast(R.string.failToSubmitBaseProfile);
							setPage(false);
						}
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToSubmitBaseProfile);
						setPage(false);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToSubmitBaseProfile);
						setPage(false);
					}
				}
			});
			ToastUtils.showToast(R.string.submittingToServer);
		} catch(Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToSubmitBaseProfile);
		}
	}

	@Override
	public void showLoadingView() {

		mainActivity.showLoadingView();
	}

	@Override
	public void hideLoadingView() {

		mainActivity.hideLoadingView();
	}
}

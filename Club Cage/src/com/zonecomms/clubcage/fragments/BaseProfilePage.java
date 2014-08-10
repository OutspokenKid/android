
package com.zonecomms.clubcage.fragments;

import java.io.File;
import java.net.URLEncoder;
import java.sql.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleButton;
import com.outspoken_kid.views.holo_dark.HoloStyleEditText;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerButton;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup.OnItemClickedListener;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.clubcage.classes.ZonecommsApplication;
import com.zonecomms.clubcage.classes.ZonecommsFragment;
import com.zonecomms.common.models.MyStoryInfo;
import com.zonecomms.common.models.UploadImageInfo;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.utils.ImageUploadUtils.OnAfterUploadImage;
import com.zonecomms.common.views.CustomDatePicker;

public class BaseProfilePage extends ZonecommsFragment {

	private FrameLayout innerFrame;
	
	private MyStoryInfo myStoryInfo;
	
	private String nickname;
	private String gender;
	private int year;
	private int month;
	private int date;
	private String introduce;

	private ProgressBar progress;
	private ImageView ivImage;
	private HoloStyleEditText etNickname;
	private HoloStyleSpinnerButton spGender;
	private HoloStyleSpinnerPopup pGender;
	private CustomDatePicker dpBirth;
	private HoloStyleEditText etIntroduce;
	private HoloStyleSpinnerPopup pPhoto;
	
	@Override
	public void bindViews() {
		
		innerFrame = (FrameLayout) mThisView.findViewById(R.id.baseProfilePage_innerFrame);
	}

	@Override
	public void setVariables() {
	}

	@SuppressLint("NewApi")
	@Override
	public void createPage() {

		FrameLayout imageFrame = new FrameLayout(mContext);
		ResizeUtils.viewResize(200, 200, imageFrame, 2, Gravity.CENTER_HORIZONTAL|Gravity.TOP, new int[]{0, 50, 0, 0});
		imageFrame.setBackgroundResource(R.drawable.bg_profile_308);
		innerFrame.addView(imageFrame);

		progress = new ProgressBar(mContext);
		ResizeUtils.viewResize(60, 60, progress, 2, Gravity.CENTER, null);
		imageFrame.addView(progress);
		
		ivImage = new ImageView(mContext);
		ivImage.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
															LayoutParams.MATCH_PARENT));
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				try {
					pPhoto.showPopup();
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			}
		});
		imageFrame.addView(ivImage);
		
		etNickname = new HoloStyleEditText(mContext);
		ResizeUtils.viewResize(540, 70, etNickname, 2, Gravity.LEFT|Gravity.TOP, 
				new int[]{50, 300, 0, 0}, new int[]{160, 0, 30, 0});
		etNickname.getEditText().setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
		FontUtils.setFontSize(etNickname.getEditText(), 22);
		innerFrame.addView(etNickname);
		
		TextView tvNickname = new TextView(mContext);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 70, tvNickname, 2, Gravity.LEFT|Gravity.TOP, new int[]{70, 290, 0, 0});
		tvNickname.setGravity(Gravity.CENTER_VERTICAL);
		tvNickname.setText(R.string.nickname);
		tvNickname.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvNickname, 25);
		innerFrame.addView(tvNickname);
		
		spGender = new HoloStyleSpinnerButton(mContext);
		ResizeUtils.viewResize(230, 80, spGender, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 390, 0, 0});
		spGender.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(pGender != null) {
					SoftKeyboardUtils.hideKeyboard(mContext, spGender.getTextView());
					spGender.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							pGender.showPopup();
						}
					}, 200);
				}
			}
		});
		innerFrame.addView(spGender);
		
		TextView tvBirth = new TextView(mContext);
		tvBirth.setText(R.string.birth);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 40, tvBirth, 2, Gravity.LEFT|Gravity.TOP, new int[]{40, 510, 0, 0});
		tvBirth.setGravity(Gravity.CENTER_VERTICAL);
		FontUtils.setFontSize(tvBirth, 25);
		tvBirth.setTextColor(Color.WHITE);
		innerFrame.addView(tvBirth);
		
		FrameLayout ff = new FrameLayout(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, ff, 2, 
				Gravity.LEFT|Gravity.TOP, new int[]{0, 550, 0, 0});
		innerFrame.addView(ff);
		
		dpBirth = new CustomDatePicker(mContext);
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	    if (currentapiVersion >= 11) {
	    	dpBirth.setCalendarViewShown(false);
	    	dpBirth.setMaxDate(System.currentTimeMillis());
	    	dpBirth.setMinDate((new Date(0)).getTime());
	    }
		ff.addView(dpBirth);
		
		TextView tvIntroduce = new TextView(mContext);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 40, tvIntroduce, 2, Gravity.LEFT|Gravity.TOP,
				new int[]{40, (currentapiVersion >= 11? 940 : 820), 0, 0});
		tvIntroduce.setGravity(Gravity.CENTER_VERTICAL);
		tvIntroduce.setText(R.string.introduce);
		FontUtils.setFontSize(tvIntroduce, 25);
		tvIntroduce.setTextColor(Color.WHITE);
		innerFrame.addView(tvIntroduce);
		
		etIntroduce = new HoloStyleEditText(mContext);
		ResizeUtils.viewResize(540, 90, etIntroduce, 2, Gravity.LEFT|Gravity.TOP, 
				new int[]{50, (currentapiVersion >= 11? 980 : 860), 0, 0});
		etIntroduce.setHint(R.string.hintForIntroduce);
		etIntroduce.getEditText().setSingleLine(false);
		FontUtils.setFontSize(etIntroduce.getEditText(), 22);
		innerFrame.addView(etIntroduce);
		
		HoloStyleButton btnSubmit = new HoloStyleButton(mContext);
		ResizeUtils.viewResize(540, 70, btnSubmit, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, (currentapiVersion >= 11? 1100 : 980), 0, 0});
		btnSubmit.setText(R.string.inputComplete);
		FontUtils.setFontSize(btnSubmit.getTextView(), 23);
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(etNickname.getEditText().getText() == null) {
					ToastUtils.showToast(R.string.invalidNickname);
					return;
				}
				
				String nickname = etNickname.getEditText().getText().toString();
				String gender = spGender.getTextView().getText().toString().equals(getString(R.string.male))? "M" : "F";
				String yearString = "" + year;
				String monthAndDateString = (month+1 < 10? "0" : "") + (month + 1) + (date < 10? "0" : "") + date;
				String myStoryTitle = "";
				
				if(etIntroduce.getEditText().getText() != null) {
					myStoryTitle = etIntroduce.getEditText().getText().toString();
				}
				
				if(StringUtils.isEmpty(nickname)
						|| nickname.length() < 2
						|| nickname.length() > 14
						|| nickname.contains(" ")) {
					ToastUtils.showToast(R.string.invalidNickname);
					return;
				}
				
				if(spGender.getTextView().getText().toString().equals(getString(R.string.selectGender))) {
					ToastUtils.showToast(R.string.selectYourGender);
					return;
				}
				
				submit(nickname, gender, yearString, monthAndDateString, myStoryTitle);
				
			}
		});
		innerFrame.addView(btnSubmit);
		
		View v = new View(mContext);
		ResizeUtils.viewResize(10, (currentapiVersion >= 11? 1210 : 1090), v, 2, Gravity.LEFT|Gravity.TOP, null);
		innerFrame.addView(v);
		
		pGender = new HoloStyleSpinnerPopup(mContext);
		pGender.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
		pGender.setTitle("Select your gender");
		pGender.addItem(getString(R.string.male));
		pGender.addItem(getString(R.string.female));
		pGender.notifyDataSetChanged();
		pGender.setTargetTextView(spGender.getTextView());
		innerFrame.addView(pGender);
		
		pPhoto = new HoloStyleSpinnerPopup(mContext);
		pPhoto.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
		pPhoto.setTitle(getString(R.string.uploadPhoto));
		pPhoto.addItem(getString(R.string.photo_take));
		pPhoto.addItem(getString(R.string.photo_album));
		pPhoto.notifyDataSetChanged();
		((FrameLayout)(mThisView.findViewById(R.id.baseProfilePage_mainLayout))).addView(pPhoto);
	}

	@Override
	public void setListeners() {

		pPhoto.setOnItemClickedListener(new OnItemClickedListener() {
			
			@Override
			public void onItemClicked(int position, String itemString) {

				String filePath = null;
				String fileName = null;
				int requestCode = 0;
				Intent intent = new Intent();
				
				if(StringUtils.isEmpty(itemString)) {
					return;
				} else if(itemString.equals(getString(R.string.photo_take))){
					intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

				    File fileDerectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				    filePath = fileDerectory.getAbsolutePath();
				    fileName = System.currentTimeMillis() + ".jpg";
				    File file = new File(fileDerectory, fileName);
				    
				    if(!fileDerectory.exists()) {
				    	fileDerectory.mkdirs();
				    }
				    
				    Uri uri = Uri.fromFile(file);
				    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
				    requestCode = ZoneConstants.REQUEST_CAMERA;
				    
				} else if(itemString.equals(getString(R.string.photo_album))){
//					intent.setAction(Intent.ACTION_GET_CONTENT);
					intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					requestCode = ZoneConstants.REQUEST_GALLERY;
				}
				
				OnAfterUploadImage oaui = new OnAfterUploadImage() {
					
					@Override
					public void onAfterUploadImage(UploadImageInfo uploadImageInfo,
							Bitmap thumbnail) {
						
						if(uploadImageInfo != null) {
							
							try {
								final Bitmap fBitmap = thumbnail;
								
								String url = ZoneConstants.BASE_URL + "member/update/profileimg" +
										"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
										"&profile_image=" + uploadImageInfo.getImageUrl() +
										"&img_width=" + uploadImageInfo.getImageWidth() +
										"&img_height=" + + uploadImageInfo.getImageHeight() +
										"&image_size=" + ResizeUtils.getSpecificLength(308);
								DownloadUtils.downloadJSONString(url,
										new OnJSONDownloadListener() {

											@Override
											public void onError(String url) {
												
												ToastUtils.showToast(R.string.failToLoadBitmap);
												mainActivity.hideCover();
												mainActivity.hideLoadingView();
											}

											@Override
											public void onCompleted(String url,
													JSONObject objJSON) {

												try {
													LogUtils.log("BaseProfilePage.onCompleted."
															+ "\nurl : "
															+ url
															+ "\nresult : "
															+ objJSON);

													mainActivity.hideCover();
													mainActivity.hideLoadingView();
													
													if(objJSON.getInt("errorCode") == 1) {
														ivImage.setImageBitmap(fBitmap);
													} else {
														ToastUtils.showToast(R.string.failToLoadBitmap);
													}
												} catch (Exception e) {
													LogUtils.trace(e);
													ToastUtils.showToast(R.string.failToLoadBitmap);
												} catch (OutOfMemoryError oom) {
													LogUtils.trace(oom);
													ToastUtils.showToast(R.string.failToLoadBitmap);
												}
											}
										});
							} catch(Exception e) {
								LogUtils.trace(e);
								ToastUtils.showToast(R.string.failToLoadBitmap);
							}
						}
					}
				};
				
				mainActivity.imageUploadSetting(filePath, fileName, true, oaui);
				mainActivity.startActivityForResult(intent, requestCode);
			}
		});
	}
	
	@Override
	public void setSizes() {
		// TODO Auto-generated method stub

	}

	@Override
	public void downloadInfo() {

		if(isDownloading) {
			return;
		}
		
		isDownloading = true;
		mainActivity.showLoadingView();
		mainActivity.showCover();

		try {
			String url = ZoneConstants.BASE_URL + "member/info" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&mystory_member_id=" + ZonecommsApplication.myInfo.getMember_id() +
					"&image_size=" + ResizeUtils.getSpecificLength(308);

			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {
					
					ToastUtils.showToast(R.string.failToLoadUserInfo);
					setPage(false);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("BaseProfilePage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						JSONArray arJSON = objJSON.getJSONArray("result");
						
						myStoryInfo = new MyStoryInfo(arJSON.getJSONObject(0));
						
						String monthString = ZonecommsApplication.myInfo.getMember_birty_md().substring(0, 2);
						String dateString = ZonecommsApplication.myInfo.getMember_birty_md().substring(2, 4);
						
						nickname = myStoryInfo.getMystory_member_nickname();
						gender = myStoryInfo.getMember_gender();
						
						if(gender.equals(getString(R.string.maleInit))) {
							gender = getString(R.string.male);
						} else {
							gender = getString(R.string.female);
						}
						
						year = Integer.parseInt(ZonecommsApplication.myInfo.getMember_birty_yy());
						month = Integer.parseInt(monthString) - 1;
						date = Integer.parseInt(dateString);
						introduce = myStoryInfo.getMystory_title();

						setPage(true);
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToLoadUserInfo);
						setPage(false);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToLoadUserInfo);
						setPage(false);
					}
				}
			});
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void setPage(boolean downloadSuccess) {
		
		mainActivity.hideCover();
		mainActivity.hideLoadingView();
		
		if(downloadSuccess) {
			etNickname.getEditText().setText(nickname);
			spGender.setText(gender);
		    dpBirth.init(year, month, date, new OnDateChangedListener() {
				
				@Override
				public void onDateChanged(DatePicker view, int _year, int monthOfYear,
						int dayOfMonth) {
					 year =  _year;
					 month = monthOfYear;
					 date = dayOfMonth;
				}
			});
			etIntroduce.getEditText().setText(introduce);
			
			loadProfile();
		}
	}

	@Override
	public int getContentViewId() {

		return R.layout.page_baseprofile;
	}
	
	@Override
	public boolean onBackPressed() {
		
		if(pPhoto.getVisibility() == View.VISIBLE) {
			pPhoto.hidePopup();
			return true;
		} else if(pGender.getVisibility() == View.VISIBLE) {
			pGender.hidePopup();
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
		
		downloadInfo();
		
		if(mainActivity.getSponserBanner() != null) {
			mainActivity.getSponserBanner().hideBanner();
		}
		
		mainActivity.showTitleBar();
		mainActivity.getTitleBar().hideCircleButton();
		mainActivity.getTitleBar().showHomeButton();
		mainActivity.getTitleBar().hideWriteButton();
	}

	@Override
	public void finish(boolean needAnim, boolean isBeforeMain) {
		
		SoftKeyboardUtils.hideKeyboard(mContext, etIntroduce);
		super.finish(needAnim, isBeforeMain);
	}
	
///////////////////// Custom methods.
	
	public void loadProfile() {
		
		if(StringUtils.isEmpty(myStoryInfo.getMystory_member_profile())) {
			progress.setVisibility(View.INVISIBLE);
			return;
		}
		
		progress.setVisibility(View.VISIBLE);
		ivImage.setVisibility(View.INVISIBLE);
		
		String url = myStoryInfo.getMystory_member_profile();
		DownloadUtils.downloadBitmap(url, new OnBitmapDownloadListener() {
			
			@Override
			public void onError(String url) {

				ivImage.setVisibility(View.VISIBLE);
				progress.setVisibility(View.INVISIBLE);
			}
			
			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				if(bitmap != null && !bitmap.isRecycled()) {
					ivImage.setImageBitmap(bitmap);
					ivImage.setVisibility(View.VISIBLE);
					progress.setVisibility(View.INVISIBLE);
				}
			}
		});
	}
	
	public void submit(final String nickname, final String gender, final String yearString, 
			final String monthAndDateString, final String myStoryTitle) {
		
		try {
			String url = ZoneConstants.BASE_URL + "member/update/default_profile" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&member_nickname=" + URLEncoder.encode(nickname, "UTF-8") +
					"&gender=" + gender +
					"&birth_year=" + yearString +
					"&birth_day=" + monthAndDateString +
					"&mystory_title=" + URLEncoder.encode(myStoryTitle, "UTF-8");
			
			ToastUtils.showToast(R.string.submittingToServer);
			
			mainActivity.showLoadingView();
			mainActivity.showCover();
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {
					
					ToastUtils.showToast(R.string.failToSubmitBaseProfile);
					setPage(false);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("BaseProfilePage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("errorCode") == 1) {
							ToastUtils.showToast(R.string.submitCompleted);
							ZonecommsApplication.myInfo.setMember_birty_yy(yearString);
							ZonecommsApplication.myInfo.setMember_birty_md(monthAndDateString);
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
		} catch(Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToSubmitBaseProfile);
		}
	}

}

package com.zonecomms.golfn;

import java.io.File;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.outspoken_kid.activities.RecyclingActivity;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.BitmapUtils;
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
import com.zonecomms.common.models.UploadImageInfo;
import com.zonecomms.common.utils.ImageUploadUtils;
import com.zonecomms.common.utils.ImageUploadUtils.OnAfterUploadImage;
import com.zonecomms.common.views.CustomDatePicker;
import com.zonecomms.golfn.SignInActivity.OnAfterSigningInListener;
import com.zonecomms.golfn.classes.ZoneConstants;

public class SignUpActivity extends RecyclingActivity {

	public static SignInActivity signInActivity;
	
	private Context context;
	private TextView tvTitle;
	
	private String yearString = "1994";
	private String monthAndDateString = "0101";
	private boolean isLoading;
	
	//Clause.
	private FrameLayout frameForClause;
	
	//MainInfo.
	private ScrollView scrollForMainInfo;
	private HoloStyleEditText etId;
	private HoloStyleEditText etPw;
	private HoloStyleEditText etConfirmPw;
	private HoloStyleEditText etEmail;
	private HoloStyleSpinnerButton spEmail;
	private HoloStyleEditText etEmail2;
	private HoloStyleEditText etPhoneNumber;
	private HoloStyleSpinnerPopup pEmail;
	
	//SubInfo.
	private ScrollView scrollForSubInfo;
	private ImageView ivProfile;
	private HoloStyleEditText etNickname;
	private HoloStyleEditText etIntroduce;
	private HoloStyleSpinnerButton spGender;
	private HoloStyleSpinnerPopup pGender;
	private HoloStyleSpinnerPopup pPhoto;
	private CustomDatePicker dpBirth;
	
	private View cover;
	
	private File filePath;
	private String fileName;
	private UploadImageInfo uii;
	
	@Override
 	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
		context = this;
		
		bindViews();
		setVariables();
		createPage();
		setSizes();
		setListeners();
	}
	
	@Override
	protected void bindViews() {
		
		tvTitle = (TextView) findViewById(R.id.signUpActivity_tvTitle);
		frameForClause = (FrameLayout) findViewById(R.id.signUpActivity_frameForClause);
		scrollForMainInfo = (ScrollView) findViewById(R.id.signUpActivity_scrollForMainInfo);
		scrollForSubInfo = (ScrollView) findViewById(R.id.signUpActivity_scrollForSubInfo);
		
		cover = findViewById(R.id.signUpActivity_cover);
		
		pEmail = (HoloStyleSpinnerPopup) findViewById(R.id.signUpActivity_pEmail);
		pGender = (HoloStyleSpinnerPopup) findViewById(R.id.signUpActivity_pGender);
		
		pPhoto = (HoloStyleSpinnerPopup) findViewById(R.id.signUpActivity_pPhoto);
		
		loadingView = findViewById(R.id.signUpActivity_loadingView);
	}

	@Override
	protected void setVariables() {

		pEmail.setTitle("Select you Email");
		pEmail.addItem(getString(R.string.mail1));
		pEmail.addItem(getString(R.string.mail2));
		pEmail.addItem(getString(R.string.mail3));
		pEmail.addItem(getString(R.string.mail4));
		pEmail.addItem(getString(R.string.mail5));
		pEmail.addItem(getString(R.string.mail6));
		pEmail.addItem(getString(R.string.inputDirectly));
		pEmail.notifyDataSetChanged();
		
		pGender.setTitle("Select your gender");
		pGender.addItem(getString(R.string.male));
		pGender.addItem(getString(R.string.female));
		pGender.notifyDataSetChanged();
		
		pPhoto.setTitle(getString(R.string.uploadPhoto));
		pPhoto.addItem(getString(R.string.photo_take));
		pPhoto.addItem(getString(R.string.photo_album));
		pPhoto.notifyDataSetChanged();
	}

	@Override
	protected void createPage() {
		
		addViewsForClause();
		addViewsForMainInfo();
		addViewsForSubInfo();
	}

	@Override
	protected void setSizes() {
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 90, tvTitle, 2, Gravity.LEFT|Gravity.TOP, null);
		FontInfo.setFontSize(tvTitle, 36);
		FontInfo.setFontStyle(tvTitle, FontInfo.BOLD);
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, frameForClause, 2, 
				Gravity.LEFT|Gravity.TOP, new int[]{0, 90, 0, 0}, new int[]{40, 40, 40, 40});
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, scrollForMainInfo, 2, 
				Gravity.LEFT|Gravity.TOP, new int[]{0, 88, 0, 0}, null);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, scrollForSubInfo, 2, 
				Gravity.LEFT|Gravity.TOP, new int[]{0, 88, 0, 0}, null);
	}

	@Override
	protected void setListeners() {

		pEmail.setOnItemClickedListener(new OnItemClickedListener() {
			
			@Override
			public void onItemClicked(int position, String itemString) {
				
				if(itemString.equals(getString(R.string.inputDirectly))) {
					spEmail.setText("");
					etEmail2.setVisibility(View.VISIBLE);
					etEmail2.requestFocus();
					SoftKeyboardUtils.showKeyboard(context, etEmail2);
				} else {
					etEmail2.setVisibility(View.INVISIBLE);
				}
			}
		});

		pPhoto.setOnItemClickedListener(new OnItemClickedListener() {
			
			@Override
			public void onItemClicked(int position, String itemString) {

				if(StringUtils.isEmpty(itemString)) {
					return;
				} else if(itemString.equals(getString(R.string.photo_take))){
					Intent intent = new Intent();
				    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
				    startActivityForResult(intent, ZoneConstants.REQUEST_CAMERA);
				} else if(itemString.equals(getString(R.string.photo_album))){
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					startActivityForResult(intent, ZoneConstants.REQUEST_GALLERY);
				}
			}
		});
	}

	@Override
	protected void downloadInfo() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setPage() {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getContentViewId() {

		return R.id.signUpActivity_mainLayout;
	}

	@Override
	public void onBackPressed() {
		
		if(cover.getVisibility() == View.VISIBLE) {
			//Just wait.
		} else if(pGender.getVisibility() == View.VISIBLE) {
			pGender.hidePopup();
		} else if(pEmail.getVisibility() == View.VISIBLE) {
			pEmail.hidePopup();
		} else if(pPhoto.getVisibility() == View.VISIBLE) {
			pPhoto.hidePopup();
		} else if(scrollForSubInfo.getVisibility() == View.VISIBLE) {
			showMain();
		} else if(scrollForMainInfo.getVisibility() == View.VISIBLE) {
			showClause();
		} else {
			super.onBackPressed();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode == RESULT_OK) {
			
			try {
				File file = null;
				
				switch(requestCode) {
				
				case ZoneConstants.REQUEST_GALLERY:
					file = new File(ImageUploadUtils.getRealPathFromUri(this, data.getData()));
					break;
					
				case ZoneConstants.REQUEST_CAMERA:	
					file = new File(filePath, fileName);
					filePath = null;
					fileName = null;
					break;
				}
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 16;
				BitmapFactory.decodeFile(file.getPath(), options);

				int width = 0;
				
				if(BitmapUtils.GetExifOrientation(file.getPath()) % 180 == 0) {
					width = options.outWidth;
				} else {
					width = options.outHeight;
				}
				
				if(width > 600) {
                	//Use last inSampleSize.
                } else if(width > 300) {
                	options.inSampleSize = 8;
                } else if(width > 150) {
                	options.inSampleSize = 4;
                } else {
                	options.inSampleSize = 2;
                }
				
				cover.setVisibility(View.VISIBLE);
				showLoadingView();
				OnAfterUploadImage oaui = new OnAfterUploadImage() {
					
					@Override
					public void onAfterUploadImage(UploadImageInfo uploadImageInfo, Bitmap thumbnail) {
						cover.setVisibility(View.INVISIBLE);
						hideLoadingView();
						
						if(uploadImageInfo != null && thumbnail != null && !thumbnail.isRecycled()) {
							ivProfile.setImageBitmap(thumbnail);
							uii = uploadImageInfo;
						} else {
							thumbnail.recycle();
							thumbnail = null;
						}
					}
				}; 
				ImageUploadUtils.uploadImage(this, oaui, file.getPath(), options.inSampleSize);
			} catch(OutOfMemoryError oom) {
				oom.printStackTrace();
				ToastUtils.showToast(R.string.failToLoadBitmap_OutOfMemory);
			} catch(Error e) {
				LogUtils.trace(e);
				ToastUtils.showToast(R.string.failToLoadBitmap);
			} catch(Exception e) {
				LogUtils.trace(e);
				ToastUtils.showToast(R.string.failToLoadBitmap);
			}
		} else {
			ToastUtils.showToast(R.string.canceled);
		}
	}
	
///////////// Custom methods.
	
	public void addViewsForClause() {
		
		int clauseHeight = (ResizeUtils.getScreenHeight() 
							- ResizeUtils.getStatusBarHeight()
							- ResizeUtils.getSpecificLength(370))/2;
		int width = ResizeUtils.getSpecificLength(600);
		int progressLength = ResizeUtils.getSpecificLength(60);
		int p = ResizeUtils.getSpecificLength(20);

		View webViewBg1 = new View(this);
		FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(width, clauseHeight, 
				Gravity.CENTER_HORIZONTAL|Gravity.TOP);
		webViewBg1.setLayoutParams(fp);
		webViewBg1.setBackgroundColor(Color.WHITE);
		frameForClause.addView(webViewBg1);
		
		final WebView webView1 = new WebView(this);
		fp = new FrameLayout.LayoutParams(width - p*2, clauseHeight, Gravity.CENTER_HORIZONTAL|Gravity.TOP);
		webView1.setLayoutParams(fp);
		frameForClause.addView(webView1);
		
		ProgressBar progress1 = new ProgressBar(this);
		fp = new FrameLayout.LayoutParams(progressLength, progressLength, 
				Gravity.CENTER_HORIZONTAL|Gravity.TOP);
		fp.topMargin = (clauseHeight - progressLength)/2;
		progress1.setLayoutParams(fp);
		frameForClause.addView(progress1);
		
		View webViewBg2 = new View(this);
		fp = new FrameLayout.LayoutParams(width, clauseHeight, 
				Gravity.CENTER_HORIZONTAL|Gravity.TOP);
		fp.topMargin = clauseHeight + ResizeUtils.getSpecificLength(40);
		webViewBg2.setLayoutParams(fp);
		webViewBg2.setBackgroundColor(Color.WHITE);
		frameForClause.addView(webViewBg2);
		
		final WebView webView2 = new WebView(this);
		fp = new FrameLayout.LayoutParams(width - p*2, clauseHeight, 
				Gravity.CENTER_HORIZONTAL|Gravity.TOP);
		fp.topMargin = clauseHeight + ResizeUtils.getSpecificLength(40);
		webView2.setLayoutParams(fp);
		webView2.setPadding(p, p, p, p);
		webView2.setBackgroundColor(Color.WHITE);
		frameForClause.addView(webView2);
		
		ProgressBar progress2 = new ProgressBar(this);
		fp = new FrameLayout.LayoutParams(progressLength, progressLength, 
				Gravity.CENTER_HORIZONTAL|Gravity.TOP);
		fp.topMargin = clauseHeight + ResizeUtils.getSpecificLength(40)
							+ (clauseHeight - progressLength)/2;
		progress2.setLayoutParams(fp);
		frameForClause.addView(progress2);

		webView1.setWebViewClient(new WebViewClientForClause(progress1));
		webView2.setWebViewClient(new WebViewClientForClause(progress2));
		webView1.post(new Runnable() {
			
			@Override
			public void run() {
				webView1.loadUrl(ZoneConstants.URL_FOR_CLAUSE1);
				webView2.loadUrl(ZoneConstants.URL_FOR_CLAUSE2);
			}
		});
		
		HoloStyleButton btnAgree = new HoloStyleButton(context);
		ResizeUtils.viewResize(360, 60, btnAgree, 2, Gravity.LEFT|Gravity.BOTTOM, new int[]{0, 0, 20, 0});
		btnAgree.setText(getString(R.string.agreeWithClause));
		FontInfo.setFontSize(btnAgree.getTextView(), 20);
		btnAgree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showMain();
			}
		});
		frameForClause.addView(btnAgree);
		
		HoloStyleButton btnDisagree = new HoloStyleButton(context);
		ResizeUtils.viewResize(180, 60, btnDisagree, 2, Gravity.RIGHT|Gravity.BOTTOM, new int[]{0, 0, 0, 0});
		btnDisagree.setText(getString(R.string.disagreeWithClause));
		FontInfo.setFontSize(btnDisagree.getTextView(), 20);
		btnDisagree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		frameForClause.addView(btnDisagree);
	}
	
	public void addViewsForMainInfo() {
		
		FrameLayout frameForMain = (FrameLayout) findViewById(R.id.signUpActivity_frameForMainInfo);
		
		etId = new HoloStyleEditText(context);
		ResizeUtils.viewResize(540, 70, etId, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 80, 0, 0});
		etId.setHint(R.string.hintForId);
		etId.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		FontInfo.setFontSize(etId.getEditText(), 22);
		frameForMain.addView(etId);
		
		etPw = new HoloStyleEditText(context);
		ResizeUtils.viewResize(540, 70, etPw, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 180, 0, 0});
		etPw.setHint(R.string.hintForPw);
		etPw.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
		FontInfo.setFontSize(etPw.getEditText(), 22);
		frameForMain.addView(etPw);
		
		etConfirmPw = new HoloStyleEditText(context);
		ResizeUtils.viewResize(540, 70, etConfirmPw, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 280, 0, 0});
		etConfirmPw.setHint(R.string.hintForConfirmPw);
		etConfirmPw.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
		FontInfo.setFontSize(etConfirmPw.getEditText(), 22);
		frameForMain.addView(etConfirmPw);
		
		etEmail = new HoloStyleEditText(context);
		ResizeUtils.viewResize(270, 70, etEmail, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 380, 0, 0});
		etEmail.setHint(R.string.hintForEmail);
		etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		FontInfo.setFontSize(etEmail.getEditText(), 22);
		frameForMain.addView(etEmail);
		
		TextView tvAt = new TextView(context);
		ResizeUtils.viewResize(60, 60, tvAt, 2, Gravity.LEFT|Gravity.TOP, new int[]{310, 380, 0, 0});
		tvAt.setText("@");
		tvAt.setGravity(Gravity.CENTER);
		FontInfo.setFontSize(tvAt, 40);
		frameForMain.addView(tvAt);
		
		spEmail = new HoloStyleSpinnerButton(context);
		ResizeUtils.viewResize(230, 70, spEmail, 2, Gravity.LEFT|Gravity.TOP, new int[]{360, 380, 0, 0});
		spEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(pEmail != null) {
					
					SoftKeyboardUtils.hideKeyboard(context, spEmail.getTextView());
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
		
		etEmail2 = new HoloStyleEditText(context);
		ResizeUtils.viewResize(180, 70, etEmail2, 2, Gravity.LEFT|Gravity.TOP, new int[]{360, 380, 0, 0});
		etEmail2.setHint(R.string.inputDirectly);
		etEmail2.setVisibility(View.INVISIBLE);
		etEmail2.setBackgroundColor(Color.TRANSPARENT);
		etEmail2.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		FontInfo.setFontSize(etEmail2.getEditText(), 22);
		frameForMain.addView(etEmail2);
		
		etPhoneNumber = new HoloStyleEditText(context);
		ResizeUtils.viewResize(540, 70, etPhoneNumber, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 480, 0, 0});
		etPhoneNumber.setHint(R.string.hintForPhoneNumber);
		etPhoneNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
		FontInfo.setFontSize(etPhoneNumber.getEditText(), 22);
		frameForMain.addView(etPhoneNumber);
		
		HoloStyleButton btnInputMoreInfo = new HoloStyleButton(context);
		ResizeUtils.viewResize(540, 70, btnInputMoreInfo, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 620, 0, 0});
		btnInputMoreInfo.setText(R.string.inputMoreInfo);
		FontInfo.setFontSize(btnInputMoreInfo.getTextView(), 23);
		btnInputMoreInfo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(isLoading) {
					return;
				}
				
				//Check Id.
				if(etId.getEditText().getText() == null) {
					ToastUtils.showToast(R.string.invalidId);
					return;
				}
				
				String idText = etId.getEditText().getText().toString();
				
				if(StringUtils.isEmpty(idText)
						|| idText.length() < 4
						|| idText.length() > 20
						|| idText.contains(" ")) {
					ToastUtils.showToast(R.string.invalidId);
					return;
				}
				
				//Check Pw.
				if(etPw.getEditText().getText() == null) {
					ToastUtils.showToast(R.string.invalidPw);
					return;
				}
				
				String pwText = etPw.getEditText().getText().toString();
				
				if(StringUtils.isEmpty(pwText)
						|| pwText.length() < 4
						|| pwText.length() > 20
						|| pwText.contains(" ")) {
					ToastUtils.showToast(R.string.invalidPw);
					return;
				}
				
				//Check confirm pw.
				if(etPw.getEditText().getText() == null 
						|| !pwText.equals(etConfirmPw.getEditText().getText().toString())) {
					ToastUtils.showToast(R.string.invalidConfirmPw);
					return;
				}
				
				//Check e-mail.
				if(etEmail.getEditText().getText() == null) {
					ToastUtils.showToast(R.string.invalidEmail);
					return;
				}
				
				String emailText = etEmail.getEditText().getText().toString();
				
				if(StringUtils.isEmpty(emailText)
						|| emailText.length() < 2
						|| emailText.length() > 20
						|| emailText.contains(" ")) {
					ToastUtils.showToast(R.string.invalidEmail);
					return;
				}
				
				//Check phone number.
				if(etPhoneNumber.getEditText().getText() == null) {
					ToastUtils.showToast(R.string.invalidPhoneNumber);
					return;
				}
				
				String phoneNumberText = etPhoneNumber.getEditText().getText().toString();
				
				if(StringUtils.isEmpty(phoneNumberText)
						|| phoneNumberText.length() < 8
						|| phoneNumberText.length() > 20
						|| phoneNumberText.contains(" ")) {
					ToastUtils.showToast(R.string.invalidPhoneNumber);
					return;
				}
				
				isLoading = true;
				showLoadingView();
				AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
					
					@Override
					public void onErrorRaised(String url, Exception e) {
						ToastUtils.showToast(R.string.failToSignUp);
						hideLoadingView();
						isLoading = false;
					}
					
					@Override
					public void onCompleted(String url, String result) {

						try {
							JSONObject objJSON = new JSONObject(result);
							
							if(objJSON.has("errorCode") && objJSON.getInt("errorCode") == 1) {
								
								AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
									
									@Override
									public void onErrorRaised(String url, Exception e) {
										ToastUtils.showToast(R.string.failToSignUp);
										hideLoadingView();
										isLoading = false;
									}
									
									@Override
									public void onCompleted(String url, String result) {
										
										hideLoadingView();
										isLoading = false;

										try {
											JSONObject objJSON = new JSONObject(result);
											
											if(objJSON.has("errorCode") && objJSON.getInt("errorCode") == 1) {
												showSub();
											} else if(objJSON.has("errorMsg")) {
												ToastUtils.showToast(objJSON.getString("errorMsg"));
											}
										} catch(Exception e) {
											LogUtils.trace(e);
										}
									}
								};
								
								String emailText = etEmail.getEditText().getText().toString();
								
								//직접 입력
								if(etEmail2.getVisibility() == View.VISIBLE) {
									emailText += "@" + etEmail2.getEditText().getText();
								//이메일 선택
								} else {
									emailText += "@" + spEmail.getTextView().getText(); 
								}
								
								String url2 = ZoneConstants.BASE_URL + "member/check/email?member_email=" + emailText;
								AsyncStringDownloader.download(url2, downloadKey, ocl);
							} else if(objJSON.has("errorMsg")) {
								ToastUtils.showToast(objJSON.getString("errorMsg"));
								hideLoadingView();
								isLoading = false;
							}
						} catch(Exception e) {
							LogUtils.trace(e);
							hideLoadingView();
							isLoading = false;
						}
					}
				};
				String url = ZoneConstants.BASE_URL + "member/check/id?member_id=" + etId.getEditText().getText();
				AsyncStringDownloader.download(url, downloadKey, ocl);
			}
		});
		frameForMain.addView(btnInputMoreInfo);
		
		View v = new View(context);
		ResizeUtils.viewResize(10, 640, v, 2, Gravity.LEFT|Gravity.TOP, null);
		frameForMain.addView(v);
	}
	
	@SuppressLint("NewApi")
	public void addViewsForSubInfo() {
		
		FrameLayout frameForSub = (FrameLayout) findViewById(R.id.signUpActivity_frameForSubInfo);
		
		ivProfile = new ImageView(context);
		ResizeUtils.viewResize(300, 300, ivProfile, 2, Gravity.CENTER_HORIZONTAL|Gravity.TOP, new int[]{0, 40, 0, 0});
		ivProfile.setBackgroundResource(R.drawable.configuration_profile_image);
		ivProfile.setScaleType(ScaleType.CENTER_CROP);
		ivProfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pPhoto.showPopup();
			}
		});
		frameForSub.addView(ivProfile);
		
		TextView tv = new TextView(context);
		ResizeUtils.viewResize(320, LayoutParams.WRAP_CONTENT, tv, 2, Gravity.
				CENTER_HORIZONTAL|Gravity.TOP, new int[]{0, 350, 0, 0});
		tv.setText(R.string.profilePhotoCondition);
		tv.setGravity(Gravity.CENTER_HORIZONTAL);
		FontInfo.setFontSize(tv, 20);
		frameForSub.addView(tv);
		
		etNickname = new HoloStyleEditText(context);
		ResizeUtils.viewResize(540, 70, etNickname, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 410, 0, 0});
		etNickname.setHint(R.string.hintForNickname);
		FontInfo.setFontSize(etNickname.getEditText(), 22);
		frameForSub.addView(etNickname);
		
		spGender = new HoloStyleSpinnerButton(context);
		ResizeUtils.viewResize(230, 80, spGender, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, 520, 0, 0});
		frameForSub.addView(spGender);
		spGender.setText(R.string.selectGender);
		spGender.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(pGender != null) {
					SoftKeyboardUtils.hideKeyboard(context, spGender.getTextView());
					spGender.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							pGender.showPopup();
						}
					}, 200);
				}
			}
		});
		pGender.setTargetTextView(spGender.getTextView());
		
		TextView tvBirth = new TextView(context);
		tvBirth.setText(R.string.birth);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 40, tvBirth, 2, Gravity.LEFT|Gravity.TOP, new int[]{40, 640, 0, 0});
		tvBirth.setGravity(Gravity.CENTER_VERTICAL);
		FontInfo.setFontSize(tvBirth, 25);
		tvBirth.setTextColor(Color.WHITE);
		frameForSub.addView(tvBirth);
		
		FrameLayout ff = new FrameLayout(context);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, ff, 2, 
				Gravity.LEFT|Gravity.TOP, new int[]{0, 680, 0, 0});
		frameForSub.addView(ff);
		
		dpBirth = new CustomDatePicker(context);
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	    if (currentapiVersion >= 11) {
	    	dpBirth.setCalendarViewShown(false);
	    	dpBirth.setMaxDate(System.currentTimeMillis());
	    }
	    dpBirth.init(1994, 0, 1, new OnDateChangedListener() {
			
			@Override
			public void onDateChanged(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				yearString = "" + year;
				monthAndDateString = (monthOfYear+1 < 10?"0": "") + (monthOfYear+1) 
						+ (dayOfMonth<10?"0":"") + dayOfMonth;
			}
		});
		ff.addView(dpBirth);
		
		etIntroduce = new HoloStyleEditText(context);
		ResizeUtils.viewResize(540, 90, etIntroduce, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, (currentapiVersion >= 11? 1080 : 960), 0, 0});
		etIntroduce.setHint(R.string.hintForIntroduce);
		etIntroduce.getEditText().setSingleLine(false);
		FontInfo.setFontSize(etIntroduce.getEditText(), 22);
		frameForSub.addView(etIntroduce);
		
		HoloStyleButton btnCompleteSignUp = new HoloStyleButton(context);
		ResizeUtils.viewResize(540, 70, btnCompleteSignUp, 2, Gravity.LEFT|Gravity.TOP, new int[]{50, (currentapiVersion >= 11? 1180 : 1060), 0, 0});
		btnCompleteSignUp.setText(R.string.completeSignUp);
		FontInfo.setFontSize(btnCompleteSignUp.getTextView(), 23);
		btnCompleteSignUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				if(isLoading) {
					return;
				}
				
				if(etNickname.getEditText().getText() == null) {
					ToastUtils.showToast(R.string.invalidNickname);
					return;
				}
				
				String nickname = etNickname.getEditText().getText().toString();
				
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
				
				isLoading = true;
				showLoadingView();
				AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
					
					@Override
					public void onErrorRaised(String url, Exception e) {
						ToastUtils.showToast(R.string.failToSignUp);
						hideLoadingView();
						isLoading = false;
					}
					
					@Override
					public void onCompleted(String url, String result) {

						isLoading = false;
						hideLoadingView();
						
						try {
							JSONObject objJSON = new JSONObject(result);
							
							if(objJSON.has("errorCode") && objJSON.getInt("errorCode") == 1) {
								signUp();
							} else if(objJSON.has("errorMsg")) {
								ToastUtils.showToast(objJSON.getString("errorMsg"));
							}
						} catch(Exception e) {
							LogUtils.trace(e);
						}
					}
				};
				String url = ZoneConstants.BASE_URL + "member/check/nickname?member_nickname=" + etNickname.getEditText().getText();
				AsyncStringDownloader.download(url, downloadKey, ocl);
			}
		});
		frameForSub.addView(btnCompleteSignUp);
		
		View v = new View(context);
		ResizeUtils.viewResize(10, (currentapiVersion >= 11? 1290 : 1170), v, 2, Gravity.LEFT|Gravity.TOP, null);
		frameForSub.addView(v);
	}
	
	public void showClause() {
		
		if(scrollForMainInfo.getVisibility() == View.VISIBLE) {
			AlphaAnimation aaOut = new AlphaAnimation(1, 0);
			aaOut.setDuration(200);
			aaOut.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {}
				
				@Override
				public void onAnimationRepeat(Animation animation) {}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					AlphaAnimation aaIn = new AlphaAnimation(0, 1);
					aaIn.setDuration(200);
					frameForClause.setVisibility(View.VISIBLE);
					frameForClause.startAnimation(aaIn);
				}
			});
			scrollForMainInfo.setVisibility(View.INVISIBLE);
			scrollForMainInfo.startAnimation(aaOut);
		}
	}
	
	public void showMain() {
		
		if(scrollForSubInfo.getVisibility() == View.VISIBLE) {
			AlphaAnimation aaOut = new AlphaAnimation(1, 0);
			aaOut.setDuration(200);
			aaOut.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {}
				
				@Override
				public void onAnimationRepeat(Animation animation) {}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					AlphaAnimation aaIn = new AlphaAnimation(0, 1);
					aaIn.setDuration(200);
					scrollForMainInfo.setVisibility(View.VISIBLE);
					scrollForMainInfo.startAnimation(aaIn);
				}
			});
			scrollForSubInfo.setVisibility(View.INVISIBLE);
			scrollForSubInfo.startAnimation(aaOut);
		} else if(frameForClause.getVisibility() == View.VISIBLE){
			AlphaAnimation aaOut = new AlphaAnimation(1, 0);
			aaOut.setDuration(200);
			aaOut.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {}
				
				@Override
				public void onAnimationRepeat(Animation animation) {}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					AlphaAnimation aaIn = new AlphaAnimation(0, 1);
					aaIn.setDuration(200);
					scrollForMainInfo.setVisibility(View.VISIBLE);
					scrollForMainInfo.startAnimation(aaIn);
				}
			});
			frameForClause.setVisibility(View.INVISIBLE);
			frameForClause.startAnimation(aaOut);
		}
	}
	
	public void showSub() {

		String id = etId.getEditText().getText().toString();
		
		if(StringUtils.isEmpty(id)
				|| id.length() < 4
				|| id.length() > 20) {
			ToastUtils.showToast(R.string.invalidId);
			return;
		}
		
		String pw = etPw.getEditText().getText().toString();
		
		if(pw == null || pw.length() < 4 || pw.length() > 20) {
			ToastUtils.showToast(R.string.invalidPw);
			return;
		}
		
		String confirmPw = etConfirmPw.getEditText().getText().toString();
		
		if(confirmPw == null || confirmPw.length() < 4 || confirmPw.length() > 20) {
			ToastUtils.showToast(R.string.invalidPw);
			return;
		}
		
		String email = etEmail.getEditText().getText().toString();
		
		if(StringUtils.isEmpty(email)) {
			ToastUtils.showToast(R.string.invalidEmail);
			return;
		}
		
		String email2 = etEmail2.getEditText().getText().toString();
		
		if(etEmail2.getVisibility() == View.VISIBLE) {
			
			if(StringUtils.isEmpty(email2)
					|| !email2.contains(".")) {
				ToastUtils.showToast(R.string.invalidEmail);
				return;
			}
		} else {

			if(spEmail.getTextView().getText().toString().equals(getString(R.string.selectEmail))) {
				ToastUtils.showToast(R.string.invalidEmail);
				return;
			}
		}
		
		if(scrollForMainInfo.getVisibility() == View.VISIBLE) {
			AlphaAnimation aaOut = new AlphaAnimation(1, 0);
			aaOut.setDuration(200);
			aaOut.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {}
				
				@Override
				public void onAnimationRepeat(Animation animation) {}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					AlphaAnimation aaIn = new AlphaAnimation(0, 1);
					aaIn.setDuration(200);
					scrollForSubInfo.setVisibility(View.VISIBLE);
					scrollForSubInfo.startAnimation(aaIn);
				}
			});
			scrollForMainInfo.setVisibility(View.INVISIBLE);
			scrollForMainInfo.startAnimation(aaOut);
		}
	}
	
	public void signUp() {
		
		if(isLoading) {
			return;
		}
		
		isLoading = true;
		
		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {
				ToastUtils.showToast(R.string.failToSignUp);
				isLoading = false;
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				isLoading = false;
				
				try {
					JSONObject objJSON = new JSONObject(result);
					if(objJSON.has("errorMsg") 
							&& objJSON.getString("errorMsg") != null
							&& objJSON.getString("errorMsg").equals("set_member success")) {
						
						ToastUtils.showToast(R.string.signUpCompleted);
						SignInActivity.OnAfterSigningInListener oasl = new OnAfterSigningInListener() {
							
							@Override
							public void OnAfterSigningIn(boolean successSignIn) {

								if(successSignIn) {
									launchToMainActivity();
								} else {
									ToastUtils.showToast(R.string.failToSignIn);
								}
							}
						};
						
						SignInActivity.signIn(etId.getEditText().getText().toString(), etPw.getEditText().getText().toString(), oasl);
					} else {
						ToastUtils.showToast(R.string.failToSignUp);
					}
				} catch(Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToSignUp);
				}
			}
		};
		
		try {
			ToastUtils.showToast(R.string.signingUp);
			
			String imageUrl = "";
			String imageWidth = "";
			String imageHeight = "";
			
			if(uii != null) {
				
				if(!StringUtils.isEmpty(uii.getImageUrl())) {
					imageUrl = uii.getImageUrl();
				}
				
				if(uii.getImageWidth() != 0) {
					imageWidth = "" + uii.getImageWidth();
				}
				
				if(uii.getImageHeight() != 0) {
					imageHeight = "" + uii.getImageHeight();
				}
			}
			
			String emailText = etEmail.getEditText().getText().toString();
			
			//직접 입력
			if(etEmail2.getVisibility() == View.VISIBLE) {
				emailText += "@" + etEmail2.getEditText().getText();
			//이메일 선택
			} else {
				emailText += "@" + spEmail.getTextView().getText(); 
			}
			
			String url = ZoneConstants.BASE_URL + "auth/join?" +
					"member_id=" + URLEncoder.encode(etId.getEditText().getText().toString(), "UTF-8") +
					"&member_nickname=" + URLEncoder.encode(etNickname.getEditText().getText().toString(), "UTF-8") +
					"&member_pwd=" + URLEncoder.encode(etPw.getEditText().getText().toString(), "UTF-8") + 
					"&member_email=" + URLEncoder.encode(emailText, "UTF-8") +
					"&gender=" + (spGender.getTextView().getText().toString().equals(getString(R.string.male))? "M" : "F") +
					"&birth_day=" + monthAndDateString +
					"&birth_year=" + yearString + 
					"&mobile_no=" + URLEncoder.encode(etPhoneNumber.getEditText().getText().toString(), "UTF-8") + 
					"&profile_image=" + imageUrl +
					"&img_width=" + imageWidth +
					"&img_height=" + imageHeight +
					"&auth_key=1" +
					"&sb_id=" + ZoneConstants.PAPP_ID;
			
			AsyncStringDownloader.download(url, null, ocl);
		} catch(Exception e) {
			LogUtils.trace(e);
			isLoading = false;
		}
	}
	
	public void launchToMainActivity() {
		
		Intent intent = new Intent(this, MainActivity.class);
		Intent i = getIntent();				//'i' is intent that passed intent from before.
		
		if(i!= null) {
			if(i.getData() != null) {
				intent.setData(i.getData());
			}
		}
		
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
		
		if(signInActivity != null) {
			signInActivity.finish();
		}
	}

////////////////////// Classes.
	
	public class WebViewClientForClause extends WebViewClient {
		
		ProgressBar progress;
		
		public WebViewClientForClause(ProgressBar progress) {
			
			this.progress = progress;
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			if(progress != null) {
				progress.setVisibility(View.VISIBLE);
			}
			
			super.onPageStarted(view, url, favicon);
		}
		
		@Override
		public void onPageFinished(WebView view, String url) {
			
			if(progress != null) {
				progress.setVisibility(View.INVISIBLE);
			}
			
			super.onPageFinished(view, url);
		}
	}
}

package com.zonecomms.golfn;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

import com.google.android.gcm.GCMRegistrar;
import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.BitmapUtils;
import com.outspoken_kid.utils.IntentUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.NetworkUtils;
import com.outspoken_kid.utils.PackageUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.GestureSlidingLayout;
import com.outspoken_kid.views.GestureSlidingLayout.OnAfterCloseListener;
import com.outspoken_kid.views.GestureSlidingLayout.OnAfterOpenListener;
import com.outspoken_kid.views.SoftKeyboardDetector;
import com.outspoken_kid.views.SoftKeyboardDetector.OnHiddenSoftKeyboardListener;
import com.outspoken_kid.views.SoftKeyboardDetector.OnShownSoftKeyboardListener;
import com.outspoken_kid.views.WebBrowser;
import com.zonecomms.common.models.Banner;
import com.zonecomms.common.models.Media;
import com.zonecomms.common.models.MyInfo;
import com.zonecomms.common.models.Popup;
import com.zonecomms.common.models.SideMenu;
import com.zonecomms.common.models.StartupInfo;
import com.zonecomms.common.models.UploadImageInfo;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.utils.ImageUploadUtils;
import com.zonecomms.common.utils.ImageUploadUtils.OnAfterUploadImage;
import com.zonecomms.common.views.NoticePopup;
import com.zonecomms.common.views.ProfilePopup;
import com.zonecomms.common.views.SideView;
import com.zonecomms.common.views.SponserBanner;
import com.zonecomms.common.views.TitleBar;
import com.zonecomms.common.views.TitleBar.OnSideMenuButtonClickedListener;
import com.zonecomms.common.views.TitleBar.OnWriteButtonClickedListener;
import com.zonecomms.golfn.classes.ApplicationManager;
import com.zonecomms.golfn.classes.ZoneConstants;
import com.zonecomms.golfn.fragments.AddedProfilePage;
import com.zonecomms.golfn.fragments.ArticleListPage;
import com.zonecomms.golfn.fragments.ArticlePage;
import com.zonecomms.golfn.fragments.BaseProfilePage;
import com.zonecomms.golfn.fragments.GetheringListPage;
import com.zonecomms.golfn.fragments.GetheringOpenPage;
import com.zonecomms.golfn.fragments.GetheringPage;
import com.zonecomms.golfn.fragments.GridPage;
import com.zonecomms.golfn.fragments.ListPage;
import com.zonecomms.golfn.fragments.MainPage;
import com.zonecomms.golfn.fragments.MessagePage;
import com.zonecomms.golfn.fragments.PostPage;
import com.zonecomms.golfn.fragments.SettingPage;
import com.zonecomms.golfn.fragments.UserPage;

public class MainActivity extends BaseFragmentActivity {

	public static MyInfo myInfo;
	public static StartupInfo startupInfo;
	public static boolean isGoToLeaveMember;
	
	private GestureSlidingLayout gestureSlidingLayout;
	private ScrollView leftView;
	private LinearLayout leftViewInner;
	private LinearLayout topView;
	private TitleBar titleBar;
	private ProfilePopup profilePopup;
	private NoticePopup noticePopup;
	private SideView profileView;
	private SoftKeyboardDetector softKeyboardDetector;
	private SponserBanner sponserBanner;
	private WebBrowser webBrowser;
	
	//For uploadImage.
	private String filePath;
	private String fileName;
	private boolean isProfileUpload;
	private OnAfterUploadImage onAfterUploadImage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			checkGCM();
		} catch(Exception e) {
			finish();
		}
	}
	
	public void bindViews() {
		
		gestureSlidingLayout = (GestureSlidingLayout) findViewById(R.id.mainActivity_gestureSlidingLayout);
		leftView = (ScrollView) findViewById(R.id.mainActivity_leftView);
		leftViewInner = (LinearLayout) findViewById(R.id.mainActivity_leftViewInner);
		topView = (LinearLayout) findViewById(R.id.mainActivity_topView);
		titleBar = (TitleBar) findViewById(R.id.mainActivity_titleBar);
		softKeyboardDetector = (SoftKeyboardDetector) findViewById(R.id.mainActivity_softKeyboardDetector);
	}
	
	public void setVariables() {
		
		gestureSlidingLayout.setTopView(topView);
		gestureSlidingLayout.setLeftView(leftView);
		gestureSlidingLayout.setOnAfterOpenToLeftListener(new OnAfterOpenListener() {
			
			@Override
			public void onAfterOpen() {
				SoftKeyboardUtils.hideKeyboard(context, gestureSlidingLayout);
			}
		});
	}
	
	public void createPage() {
		
		ApplicationManager.getInstance().setActivity(this);
		
		try {
			addSideViewsToSideMenu();
			
			webBrowser = new WebBrowser(this);
			gestureSlidingLayout.addView(webBrowser);
		} catch(Exception e) {
			finish();
		}
	}

	public void setListeners() {
		
		titleBar.setOnSideMenuButtonClickedListener(new OnSideMenuButtonClickedListener() {
			
			@Override
			public void onSideMenuButtonClicked() {
				gestureSlidingLayout.open(true, null);
			}
		});
		
		titleBar.setOnWriteButtonClickedListener(new OnWriteButtonClickedListener() {
			
			@Override
			public void onWriteButtonClicked() {
				
				try {
					//모임 글 작성.
					if(ApplicationManager.getTopFragment() instanceof GetheringPage) {
						String sb_id = ((GetheringPage)ApplicationManager.getTopFragment()).getSb_id();
						String title = getString(R.string.gethering);
						showWriteActivity(title, sb_id);
						
					} else if(ApplicationManager.getTopFragment() instanceof GridPage) {
						
						String title = ApplicationManager.getTopFragment().getTitleText();
						
						//모임 소개.
						if(title.equals(getString(R.string.titleText_getheringIntro))) {
							showWriteActivity(title);
							
						//QnA.
						} else if(title.equals(getString(R.string.title_qna))) {
							showWriteActivity(title, 2);
							
						//포토.
						} else if(title.equals(getString(R.string.title_photo))) {
							showWriteActivity(title, 3);
							
						//나도 한마디!
						} else if(title.equals(getString(R.string.title_banner3))) {
							showWriteActivity(title, 4);
						
						//마켓1.
						} else if(title.equals(getString(R.string.title_market1))) {
							showWriteForFleaActivity(5);
							
						//마켓2.
						} else if(title.equals(getString(R.string.title_market2))) {
							showWriteForFleaActivity(6);
							
						//마켓3.
						} else if(title.equals(getString(R.string.title_market3))) {
							showWriteForFleaActivity(7);
							
						//마켓4.
						} else if(title.equals(getString(R.string.title_market4))) {
							showWriteForFleaActivity(8);
							
						//출석부.
						} else if(title.equals(getString(R.string.title_attendance))) {
							showWriteActivity(title, 9);
							
						//리뷰.
						} else if(title.equals(getString(R.string.title_review))) {
							showWriteActivity(title, 10);
						}
						
					}
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			}
		});
	
		softKeyboardDetector.setOnShownKeyboardListener(new OnShownSoftKeyboardListener() {
			
			@Override
			public void onShownSoftKeyboard() {
				
				if(ApplicationManager.getTopFragment() != null) {
					ApplicationManager.getTopFragment().onSoftKeyboardShown();
				}
			}
		});
		
		softKeyboardDetector.setOnHiddenKeyboardListener(new OnHiddenSoftKeyboardListener() {
			
			@Override
			public void onHiddenSoftKeyboard() {
				
				if(ApplicationManager.getTopFragment() != null) {
					ApplicationManager.getTopFragment().onSoftKeyboardHidden();
				}
			}
		});
	}
	
	public void setSizes() {
		
		ResizeUtils.viewResize(550, LayoutParams.MATCH_PARENT, leftView, 2, 0, null);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 90, titleBar, 1, 0, null);
	}
	
	public void downloadInfo() {
		
		setPage(true);
	}
	
	@Override
	protected void onMenuKeyPressed() {

		try {
			if(GestureSlidingLayout.isOpenToLeft()) {
				gestureSlidingLayout.close(true, null);
			} else {
				gestureSlidingLayout.open(true, null);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	@Override
	protected void onBackKeyPressed() {
		
		try {
			if(GestureSlidingLayout.isOpenToLeft()) {
				gestureSlidingLayout.close(true, null);
			} else if(ApplicationManager.getTopFragment() != null
					&& ApplicationManager.getTopFragment().onBackKeyPressed()) {
				//Do nothing.
			} else if(noticePopup != null && noticePopup.getVisibility() == View.VISIBLE) {
				noticePopup.hide(null);
			} else if(profilePopup != null && profilePopup.getVisibility() == View.VISIBLE) {
				profilePopup.hide(null);
			} else if(webBrowser.getVisibility() == View.VISIBLE) {
				webBrowser.close();
			} else if(ApplicationManager.getFragmentsSize() > 1){
				ApplicationManager.getTopFragment().finish();
			} else {
				finish();
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void finish() {
		super.finish();
		ApplicationManager.clearFragments();
		ApplicationManager.getInstance().setActivity(null);
		ViewUnbindHelper.unbindReferences(this, R.id.mainActivity_gestureSlidingLayout);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch(requestCode) {
		
		case ZoneConstants.REQUEST_GALLERY:
		case ZoneConstants.REQUEST_CAMERA:

			if(resultCode == RESULT_OK) {
				
				try {
					File file = null;

					if(requestCode == ZoneConstants.REQUEST_GALLERY) {
						file = new File(ImageUploadUtils.getRealPathFromUri(this, data.getData()));
					} else {
						file = new File(filePath, fileName);
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
					
					if(width > 640) {
	                	//Use last inSampleSize.
	                } else if(width > 320) {
	                	options.inSampleSize = 8;
	                } else if(width > 160) {
	                	options.inSampleSize = 4;
	                } else if(width > 80){
	                	options.inSampleSize = 2;
	                } else {
	                	options.inSampleSize = 1;
	                }
					
					showLoadingView();
					
					OnAfterUploadImage oaui = new OnAfterUploadImage() {
						
						@Override
						public void onAfterUploadImage(UploadImageInfo uploadImageInfo,
								Bitmap thumbnail) {
							
							hideLoadingView();
							
							if(onAfterUploadImage != null && thumbnail != null && !thumbnail.isRecycled()) {
								onAfterUploadImage.onAfterUploadImage(uploadImageInfo, thumbnail);
								
								if(isProfileUpload) {
									changeProfileUpload(thumbnail);
								}
							}
							
							clearImageUploadSetting();
						}
					};
					ImageUploadUtils.uploadImage(this, oaui, file.getPath(), options.inSampleSize);
				} catch(OutOfMemoryError oom) {
					oom.printStackTrace();
					ToastUtils.showToast(R.string.failToLoadBitmap_OutOfMemory);
					clearImageUploadSetting();
				} catch(Error e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToLoadBitmap);
					clearImageUploadSetting();
				} catch(Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToLoadBitmap);
					clearImageUploadSetting();
				}
			} else {
				ToastUtils.showToast(R.string.canceled);
				hideLoadingView();
				
				if(onAfterUploadImage != null) {
					onAfterUploadImage.onAfterUploadImage(null, null);
				}
				
				clearImageUploadSetting();
			}
			break;
			
		case ZoneConstants.REQUEST_WRITE:
			
			if(resultCode == RESULT_OK) {
				if(ApplicationManager.getTopFragment() != null) {
					
					if(ApplicationManager.getTopFragment() instanceof GridPage) {
						GridPage gridPage = (GridPage)ApplicationManager.getTopFragment();
						gridPage.onRefreshPage();
					} else if(ApplicationManager.getTopFragment() instanceof GetheringPage) {
						GetheringPage getheringPage = (GetheringPage)ApplicationManager.getTopFragment();
						getheringPage.onRefreshPage();
					} else if(ApplicationManager.getTopFragment() instanceof UserPage) {
						UserPage userPage = (UserPage)ApplicationManager.getTopFragment();
						userPage.onRefreshPage();
					}
				}

				if(data != null && data.hasExtra("spot_nid") && data.hasExtra("isGethering")) {
					
					LogUtils.log("###MainActivity.onActivityResult.  spot_nid : " + 
							data.getIntExtra("spot_nid", 0) +
							", isGethering :  " + data.getBooleanExtra("isGethering", false));
					
					showRecentPostPage(data.getIntExtra("spot_nid", 0), 
							data.getBooleanExtra("isGethering", false));
				}
			}
			break;
		
		case ZoneConstants.REQUEST_EDIT:
			
			if(resultCode == RESULT_OK && ApplicationManager.getTopFragment() != null) {
				((PostPage)ApplicationManager.getTopFragment()).onRefreshPage();
			}
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		try {
			if(NetworkUtils.checkNetworkStatus(this) == NetworkUtils.TYPE_NONE) {
				gestureSlidingLayout.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						ToastUtils.showToast(R.string.checkNetworkStatus);
					}
				}, 500);
				gestureSlidingLayout.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						finish();
					}
				}, 2000);
			} else if(isGoToLeaveMember) {
				isGoToLeaveMember = false;
				showLoadingView();
				
				AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
					
					@Override
					public void onErrorRaised(String url, Exception e) {
						
						hideLoadingView();
					}
					
					@Override
					public void onCompleted(String url, String result) {

						hideLoadingView();
						
						try {
							JSONObject objResult = new JSONObject(result);
							
							if(objResult.has("errorCode")
									&& objResult.getInt("errorCode") != 1) {
								signOut();
							}
						} catch(Exception e) {
							LogUtils.trace(e);
							ToastUtils.showToast(R.string.failToSignIn);
						}
					}
				};
				
				try {
					String id = SharedPrefsUtils.getStringFromPrefs(ZoneConstants.PREFS_SIGN, "id");
					String pw = SharedPrefsUtils.getStringFromPrefs(ZoneConstants.PREFS_SIGN, "pw");
					
					String url = ZoneConstants.BASE_URL + "auth/login" +
							"?id=" + URLEncoder.encode(id, "UTF-8") + 
							"&password=" + URLEncoder.encode(pw, "UTF-8") + 
							"&image_size=" + ResizeUtils.getSpecificLength(308) +
							"&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID);
					AsyncStringDownloader.download(url, null, ocl);
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			}
			
			if(MainActivity.myInfo == null) {
				reloadMyInfo();
			}
			
			if(sponserBanner != null && sponserBanner.getVisibility() == View.VISIBLE) {
				sponserBanner.playBanner();
			}
		} catch(Exception e) {
			finish();
		}
	}
	
	@Override
	protected void onPause() {
		
		if(sponserBanner != null && sponserBanner.getVisibility() == View.VISIBLE) {
			sponserBanner.pauseBanner();
		}
		
		super.onPause();
	}

	@Override
	protected void setPage(boolean downloadSuccess) {

		setLoadingView();
		setAnimationLoadingView();
		checkVersion();
		
		if(ApplicationManager.getFragmentsSize() == 0) {
			showMainPage();
			checkPopup();
		}
		
		final Intent i = getIntent();				//'i' is intent that passed intent from before.
		
		if(i!= null && i.getData() != null) {
			gestureSlidingLayout.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					IntentHandlerActivity.actionByUri(i.getData());
				}
			}, 500);
		}
	}

	@Override
	protected int getXmlResId() {
		return R.layout.activity_main;
	}

	@Override
	protected int getMainLayoutResId() {
		return R.id.mainActivity_gestureSlidingLayout;
	}

	@Override
	protected int getFragmentFrameResId() {
		return R.id.mainActivity_fragmentFrame;
	}

	@Override
	public View getMainLayout() {
		return gestureSlidingLayout;
	}

	@Override
	public void setTitleText(String title) {
		titleBar.setTitleText(title);
	}

	@Override
	public void setAnim(FragmentTransaction ft, boolean atMain, boolean onStartPage) {
		
		if(atMain) {
			if(onStartPage) {
				ft.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top);
			} else {
				ft.setCustomAnimations(R.anim.slide_in_from_top, R.anim.slide_out_to_bottom);
			}
			
		} else {
			if(onStartPage) {
				ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
			} else {
				ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
			}
		}
	}

	@Override
	public void setLoadingView() {
		loadingView = gestureSlidingLayout.findViewById(R.id.mainActivity_loadingView);
		ResizeUtils.viewResize(120, 150, loadingView, 2, Gravity.CENTER, new int[]{0, 45, 0, 0});
	}

	@Override
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void setAnimationLoadingView() {

		try {
			ivAnimationLoadingView = (ImageView) findViewById(R.id.mainActivity_ivAnimationLoadingView);
			ResizeUtils.viewResize(150, 150, ivAnimationLoadingView, 2, Gravity.CENTER, null);
			
			int size = startupInfo.getLoadingImageSet().getDrawables().length;
			int time = startupInfo.getLoadingImageSet().getTime();
			
			if(size == 0) {
				return;
			}
			
			if(time == 0) {
				if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
					ivAnimationLoadingView.setBackground(startupInfo.getLoadingImageSet().getDrawables()[0]);
				} else {
					ivAnimationLoadingView.setBackgroundDrawable(startupInfo.getLoadingImageSet().getDrawables()[0]);
				}
				
				animationLoaded = true;
			} else if(startupInfo != null
					&& startupInfo.getLoadingImageSet() != null
					&& startupInfo.getLoadingImageSet().getDrawables() != null) {
				animationDrawable = new AnimationDrawable();

				for(int i=0; i<size; i++) {
					if(startupInfo.getLoadingImageSet().getDrawables()[i] != null) {
						animationDrawable.addFrame(startupInfo.getLoadingImageSet().getDrawables()[i], time);
					}
				}
				animationDrawable.setOneShot(false);
				
				if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
					ivAnimationLoadingView.setBackground(animationDrawable);
				} else {
					ivAnimationLoadingView.setBackgroundDrawable(animationDrawable);
				}
				
				animationLoaded = true;
			} else {
				animationDrawable = null;
				animationLoaded = false;
			}
		} catch(Exception e) {
			animationDrawable = null;
			animationLoaded = false;
		}
	}

	@Override
	public void clearFragmentsWithoutMain() {
		super.clearFragmentsWithoutMain();
		SoftKeyboardUtils.hideKeyboard(context, gestureSlidingLayout);
	}
	
///////////////////////// Custom methods.

	public void imageUploadSetting(String filePath, String fileName, 
			boolean isProfileUpload, OnAfterUploadImage onAfterUploadImage) {
		this.filePath = filePath;
		this.fileName = fileName;
		this.isProfileUpload = isProfileUpload;
		this.onAfterUploadImage = onAfterUploadImage;
	}
	
	public void changeProfileUpload(Bitmap thumbnail) {
		
		if(thumbnail != null && !thumbnail.isRecycled() 
				&& getProfileView() != null && getProfileView().getIcon() != null) {
			getProfileView().getIcon().setImageBitmap(thumbnail);
		}
	}
	
	public void clearImageUploadSetting() {
		
		filePath = null;
		fileName = null;
		isProfileUpload = false;
		onAfterUploadImage = null;
	}
	
	public void showMainPage() {
		
		try {
			if(ApplicationManager.getFragmentsSize() == 0) {
				MainPage mp = new MainPage();
				startPage(mp, null);
			} else {
				ApplicationManager.clearFragmentsWithoutMain();
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showGridPage(int numOfColumn, String title, int type, String data) {
		
		try {
			GridPage gp = new GridPage();
			
			Bundle bundle = new Bundle();
			bundle.putInt("numOfColumn", numOfColumn);
			bundle.putString("title", title);
			bundle.putInt("type", type);
			
			if(!StringUtils.isEmpty(data)) {
				bundle.putString("data", data);
			}
			
			startPage(gp, bundle);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showPostPage(String title, int spot_nid, boolean isGethering) {
		
		showPostPage(title, spot_nid, isGethering, false);
	}
	
	public void showPostPage(String title, int spot_nid, boolean isGethering, boolean isNeedToShowBottom) {
		
		try {
			PostPage pp = new PostPage();
			
			Bundle bundle = new Bundle();
			bundle.putString("title", title);
			bundle.putInt("spot_nid", spot_nid);
			bundle.putBoolean("isGethering", isGethering);
			bundle.putBoolean("isNeedToShowBottom", isNeedToShowBottom);
			
			startPage(pp, bundle);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showUserPage(String userId, int menuIndex) {
		
		try {
			UserPage up = new UserPage();
			Bundle bundle = new Bundle();
			bundle.putString("userId", userId);
			bundle.putInt("menuIndex", menuIndex);
			startPage(up, bundle);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showListPage(String title, int type) {
		
		try {
			ListPage lp = new ListPage();
			
			Bundle bundle = new Bundle();
			bundle.putString("title", title);
			bundle.putInt("type", type);
			
			startPage(lp, bundle);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public void showSettingPage() {
		
		try {
			SettingPage sp = new SettingPage();
			startPage(sp, null);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showMessagePage(String userId) {
		
		try {
			MessagePage mp = new MessagePage();
			Bundle bundle = new Bundle();
			bundle.putString("member_id", userId);
			startPage(mp, bundle);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showBaseProfilePage() {
		
		try {
			BaseProfilePage bp = new BaseProfilePage();
			startPage(bp, null);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showBaseProfilePage(String nickname, String gender, 
			int year, int month, int date, String introduce) {
		
		try {
			BaseProfilePage bp = new BaseProfilePage();
			Bundle bundle = new Bundle();
			bundle.putString("nickname", nickname);
			bundle.putString("gender", gender);
			bundle.putInt("year", year);
			bundle.putInt("month", month);
			bundle.putInt("date", date);
			bundle.putString("introduce", introduce);
			startPage(bp, bundle);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showAddedProfilePage() {
		
		try {
			AddedProfilePage pp = new AddedProfilePage();
			startPage(pp, null);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showAddedProfilePage(int textIndex, String status, String interested, 
			String job, String company, String liveLocation, String activeLocation) {
		
		try {
			AddedProfilePage pp = new AddedProfilePage();
			Bundle bundle = new Bundle();
			bundle.putInt("textIndex", textIndex);
			
			bundle.putString("status", status);
			bundle.putString("interested", interested);
			bundle.putString("job", job);
			bundle.putString("company", company);
			bundle.putString("liveLocation", liveLocation);
			bundle.putString("activeLocation", activeLocation);
			
			startPage(pp, bundle);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showGetheringOpenPage() {

		showGetheringOpenPage(null, null, null, 0, null);
	}
	
	public void showGetheringOpenPage(String imageUrl, String nickname, 
			String id, int needPublic, String intro) {
		
		try {
			GetheringOpenPage gp = new GetheringOpenPage();
			
			LogUtils.log("showGetheringOpenPage.imageUrl : " + imageUrl);
			
			Bundle bundle = new Bundle();
			bundle.putString("imageUrl", imageUrl);
			bundle.putString("nickname", nickname);
			bundle.putString("id", id);
			bundle.putInt("needPublic", needPublic);
			bundle.putString("intro", intro);
			
			startPage(gp, bundle);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public void showGetheringPage(String sb_id) {
		
		try {
			GetheringPage gp = new GetheringPage();
			
			Bundle bundle = new Bundle();
			bundle.putString("sb_id", sb_id);
			
			startPage(gp, bundle);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showGetheringListPage(String title, int type) {

		try {
			GetheringListPage gp = new GetheringListPage();
			
			Bundle bundle = new Bundle();
			bundle.putString("title", title);
			bundle.putInt("type", type);
			
			startPage(gp, bundle);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showArticleListPage(String title, int l_cate_id) {
		
		try {
			ArticleListPage ap = new ArticleListPage();
			
			Bundle bundle = new Bundle();
			bundle.putString("title", title);
			bundle.putInt("l_cate_id", l_cate_id);
			
			startPage(ap, bundle);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showArticleListPage(String title, int l_cate_id, int menuIndex) {
		
		try {
			ArticleListPage ap = new ArticleListPage();
			
			Bundle bundle = new Bundle();
			bundle.putString("title", title);
			bundle.putInt("l_cate_id", l_cate_id);
			bundle.putInt("menuIndex", menuIndex);
			
			startPage(ap, bundle);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showArticlePage(String title, int spot_nid) {

		showArticlePage(title, spot_nid, false);
	}
	
	public void showArticlePage(String title, int spot_nid, boolean isNeedToShowBottom) {
		
		try {
			ArticlePage ap = new ArticlePage();
			
			Bundle bundle = new Bundle();
			bundle.putString("title", title);
			bundle.putInt("spot_nid", spot_nid);
			bundle.putBoolean("isNeedToShowBottom", isNeedToShowBottom);
			
			startPage(ap, bundle);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public void showDeviceBrowser(String url) {
		
		if(url == null || url.equals("")) {
			ToastUtils.showToast(R.string.failToLoadWebBrowser);
		} else {
			try {
				Uri uri = Uri.parse(url);
			    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			    startActivityWithAnim(intent);
			} catch(Exception e) {
				LogUtils.trace(e);
				ToastUtils.showToast(R.string.failToLoadWebBrowser);
			}
		}
	}
	
	public void showDeviceBrowser(String url, int requestCode) {
		
		if(url == null || url.equals("")) {
			ToastUtils.showToast(R.string.failToLoadWebBrowser);
		} else {
			try {
				Uri uri = Uri.parse(url);
			    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			    startActivityWithAnim(intent, requestCode);
			} catch(Exception e) {
				LogUtils.trace(e);
				ToastUtils.showToast(R.string.failToLoadWebBrowser);
			}
		}
	}
	
	public void showWebBrowser(String url, byte[] postData) {
		
		webBrowser.open(url, postData);
	}
	
	public void hideWebBrowser() {
		
		webBrowser.close();
	}
	
	/**
	 * 모임 글 작성.
	 * 
	 * @param titleText
	 * @param sb_id
	 */
	public void showWriteActivity(String titleText, String sb_id) {
		
		Intent intent = new Intent(this, WriteActivity.class);
		intent.putExtra("titleText", titleText);
		intent.putExtra("sb_id", sb_id);
		intent.putExtra("isGethering", true);
		startActivityWithAnim(intent, ZoneConstants.REQUEST_WRITE);
	}
	
	/**
	 * 모임 찾기.
	 * 
	 * @param isGethering
	 */
	public void showWriteActivity(String titleText) {
		
		Intent intent = new Intent(this, WriteActivity.class);
		intent.putExtra("titleText", titleText);
		intent.putExtra("isGethering", false);
		intent.putExtra("isFinding", true);
		startActivityWithAnim(intent, ZoneConstants.REQUEST_WRITE);
	}
	
	/**
	 * 일반 글 쓰기 - board_id
	 * 1 : 벼룩시장
	 * 2 : QnA
	 * 3 : 포토
	 * 4 : 나도 한마디! 
	 * 
	 * @param titleText
	 * @param board_id
	 * @param mustValues
	 */
	public void showWriteActivity(String titleText, int board_id) {
		
		Intent intent = new Intent(this, WriteActivity.class);
		intent.putExtra("titleText", titleText);
		intent.putExtra("board_id", board_id);
		intent.putExtra("isGethering", false);
		intent.putExtra("isFinding", false);
		startActivityWithAnim(intent, ZoneConstants.REQUEST_WRITE);
	}

	/**
	 * 글 수정.
	 * 모임 수정.
	 * 
	 * @param isGethering
	 * @param spot_nid
	 * @param content
	 * @param imageUrls
	 */
	public void showWriteActivity(String titleText, boolean isGethering, int spot_nid, String content, String[] imageUrls) {
	
		Intent intent = new Intent(this, WriteActivity.class);
		intent.putExtra("titleText", titleText);
		intent.putExtra("isEdit", true);
		intent.putExtra("isGethering", isGethering);
		intent.putExtra("spot_nid", spot_nid);
		intent.putExtra("content", content);
		intent.putExtra("imageUrls", imageUrls);
		startActivityWithAnim(intent, ZoneConstants.REQUEST_EDIT);
	}
	
	public void showImageViewerActivity(String title, Media[] medias, int index) {
		
		if(medias == null || medias.length == 0) {
			return;
		}
		
		int size = medias.length;
		String[] imageUrls = new String[size];
		String[] thumbnailUrls = new String[size];
		
		for(int i=0; i<size; i++) {
			imageUrls[i] = medias[i].getMedia_src();
			thumbnailUrls[i] = medias[i].getThumbnail();
		}
		
		showImageViewerActivity(title, imageUrls, thumbnailUrls, index);
	}
	
	public void showImageViewerActivity(String title, String[] imageUrls, String [] thumbnailUrls, int index) {
		
		if(imageUrls == null || imageUrls.length == 0) {
			return;
		}
		
		Intent intent = new Intent(this, ImageViewer.class);

		if(!StringUtils.isEmpty(title)) {
			intent.putExtra("title", title);
		}
		
		if(imageUrls != null && imageUrls.length != 0) {
			intent.putExtra("imageUrls", imageUrls);
		}
		
		if(thumbnailUrls != null && thumbnailUrls.length != 0) {
			intent.putExtra("thumbnailUrls", thumbnailUrls);
		}

		intent.putExtra("index", index);
		
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_from_bottom, android.R.anim.fade_out);
	}
	
	public void showCooperateActivity() {
		
		Intent intent = new Intent(this, CooperateActivity.class);
		startActivityWithAnim(intent);
	}
	
	public void showDownloadPage(String uri, String packageName) {
		
		try {
			if(uri == null) {
				ToastUtils.showToast(R.string.invalidUri);
			} else if(uri.contains("http://") || uri.contains("https://")) {
				IntentHandlerActivity.actionByUri(Uri.parse(uri));
			} else if(uri.contains("market://")) {
				// 마켓으로 연결
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(uri));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivityWithAnim(intent);
			} else if(uri.contains("tstore://")) {
				
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(uri));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				
				if(getPackageManager().queryIntentActivities(intent, 
						PackageManager.MATCH_DEFAULT_ONLY).size() > 0 ) {
					startActivityWithAnim(intent);
				} else {
					ToastUtils.showToast(getResources().getString(R.string.thereIsNoTStore));
					Intent i = new Intent(Intent.ACTION_VIEW);
		    		i.setData(Uri.parse("http://m.tstore.co.kr"));
		    		startActivityWithAnim(i);
				}
			} else {
				ToastUtils.showToast(R.string.invalidUri);
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showWriteForFleaActivity(int board_id) {
		
		Intent intent = new Intent(this, WriteForFleaActivity.class);
		intent.putExtra("board_id", board_id);
		startActivityWithAnim(intent, ZoneConstants.REQUEST_WRITE);
	}
	
	public void showWriteForFleaActivity(int board_id, String titleText, boolean isGethering, int spot_nid, 
			String content, String[] imageUrls, String[] mustValues) {
		
		Intent intent = new Intent(this, WriteForFleaActivity.class);
		intent.putExtra("board_id", board_id);
		intent.putExtra("titleText", titleText);
		intent.putExtra("isEdit", true);
		intent.putExtra("isGethering", isGethering);
		intent.putExtra("spot_nid", spot_nid);
		intent.putExtra("content", content);
		intent.putExtra("imageUrls", imageUrls);
		intent.putExtra("mustValues", mustValues);
		startActivityWithAnim(intent, ZoneConstants.REQUEST_EDIT);
	}
	
	public void startActivityWithAnim(Intent intent) {
		
		startActivity(intent);
		
		if(ApplicationManager.getTopFragment() instanceof MainPage) {
			overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top);
		} else {
			overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
		}
	}
	
	public void startActivityWithAnim(Intent intent, int requestCode) {
		
		startActivityForResult(intent, requestCode);
		
		if(ApplicationManager.getTopFragment() instanceof MainPage) {
			overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top);
		} else {
			overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
		}
	}
	
	public void checkNApp(final String uriString) {
		
		final String packageName = "com.zonecomms.napp";
		final boolean installed = PackageUtils.checkApplicationInstalled(this, packageName);

		String title = "N";
		String message = "";
		OnPositiveClickedListener opcl = new OnPositiveClickedListener() {
			
			@Override
			public void onPositiveClicked() {
				
				if(installed) {
					String id = SharedPrefsUtils.getStringFromPrefs(ZoneConstants.PREFS_SIGN, "id");
					String pw = SharedPrefsUtils.getStringFromPrefs(ZoneConstants.PREFS_SIGN, "pw");
					
					if(!StringUtils.isEmpty("id") && !StringUtils.isEmpty("pw")) {
						IntentUtils.invokeApp(context, packageName, id, pw, uriString);
					} else{
						IntentUtils.invokeApp(context, packageName, uriString);
					}
				} else {
					IntentUtils.showMarket(context, packageName);
				}
			}
		};
		
		if(installed) {
			message = getString(R.string.wannaLaunchNApp);
		} else {
			message = getString(R.string.wannaMoveToStore);
		}
		
		showAlertDialog(title, message, opcl);
	}
	
	public void addSideViewsToSideMenu() {

		final ArrayList<SideMenu> sideMenus = new ArrayList<SideMenu>();
		
		int[] titleIds = new int[]{
				R.string.id,
				R.string.sidemenu_notice,
				R.string.sidemenu_event,
				R.string.sidemenu_manianews,
				R.string.sidemenu_maniatv,
				R.string.sidemenu_golflesson,
				R.string.sidemenu_hotsale,
				R.string.sidemenu_maniamall,
				R.string.sidemenu_rentalservice,
				R.string.sidemenu_member,
				R.string.sidemenu_setting,
			};
		
		int[] iconResIds = new int[]{
				R.drawable.btn_side_profile,
				R.drawable.btn_side_notice,
				R.drawable.btn_side_event,
				R.drawable.btn_side_manianews,
				R.drawable.btn_side_maniatv,
				R.drawable.btn_side_golflesson,
				R.drawable.btn_side_hotsale,
				R.drawable.btn_side_maniamall,
				R.drawable.btn_side_rentalservice,
				R.drawable.btn_side_member,
				R.drawable.btn_side_setting,
			};
		
		final int SIZE = titleIds.length;
		for(int i=0; i<SIZE; i++) {
			SideMenu sideMenu = new SideMenu();
			sideMenu.setTitleResId(titleIds[i]);
			sideMenus.add(sideMenu);
		}
		
		for(int i=0; i<SIZE; i++){
			final int I = i;
			SideView sideView = new SideView(context);
			
			if(i==0 && (myInfo == null || StringUtils.isEmpty(myInfo.getMember_id()))) {
				ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 0, sideView, 1, 0, null);
			} else {
				ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 120, sideView, 1, 0, null);
			}
			
			sideView.setTitle(sideMenus.get(i).getTitleResId());
			sideView.setIcon(iconResIds[i]);
			sideView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
					OnAfterCloseListener oacl = new OnAfterCloseListener() {
						
						@Override
						public void onAfterClose() {
							
							if(I >=0 && I<=SIZE) {
								ApplicationManager.clearFragmentsForSideMenu();
							}
							
							String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/";
							
							switch(I) {
							case 0:
								uriString += "userhome?member_id=" + MainActivity.myInfo.getMember_id();
								break;
							case 1:
								uriString += "notice";
								break;
							case 2:
								uriString += "event";
								break;
							case 3:
								uriString += "news";
								break;
							case 4:
								uriString += "maniatv";								
								break;
							case 5:
								uriString += "lesson";
								break;
							case 6:
								uriString += "hotsale";
								break;
							case 7:
								uriString += "maniamall";
								break;
							case 8:
								uriString += "rentalservice";
								break;
							case 9:
								uriString += "member";
								break;
							case 10:
								uriString += "setting";
								break;
							}

							if(uriString != null) {
								IntentHandlerActivity.actionByUri(Uri.parse(uriString));
							}
						}
					};
					
					if(oacl != null) {
						gestureSlidingLayout.close(true, oacl);
					}
				}
			});

			if(i==0) {
				profileView = sideView;
			}
			leftViewInner.addView(sideView);
		}
	}

	public void addSponserBanner(Banner[] banners) {
		
		sponserBanner = new SponserBanner(this, banners);
		ResizeUtils.viewResize(640, 96, sponserBanner, 2, Gravity.BOTTOM, null);
		gestureSlidingLayout.addView(sponserBanner, gestureSlidingLayout.getChildCount() - 2);
	}
	
	public void addProfilePopup() {

		profilePopup = new ProfilePopup(this);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, profilePopup, 2, 0, null);
		gestureSlidingLayout.addView(profilePopup, gestureSlidingLayout.getChildCount() - 2);
	}
	
	public void showProfilePopup(String userId, int status) {
		
		if(status == -1 || status == -9) {
			ToastUtils.showToast(R.string.withdrawnMember);
			return;
		}
		
		if(profilePopup == null) {
			addProfilePopup();
		}
		
		profilePopup.show(userId);
	}

	public void addNoticePopup() {
		
		noticePopup = new NoticePopup(context);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, noticePopup, 2, 0, null);
		gestureSlidingLayout.addView(noticePopup);
	}

	public void showNoticePopup(Popup popup) {
		
		if(noticePopup == null) {
			addNoticePopup();
		}
		
		noticePopup.show(popup);
	}

	public void checkPopup() {
		
		if(startupInfo != null && startupInfo.getPopup() != null) {
			int lastIndexno = SharedPrefsUtils.getIntegerFromPrefs(ZoneConstants.PREFS_POPUP, "lastIndexno");
			int lastDate = SharedPrefsUtils.getIntegerFromPrefs(ZoneConstants.PREFS_POPUP, "lastDate");
			int lastMonth = SharedPrefsUtils.getIntegerFromPrefs(ZoneConstants.PREFS_POPUP, "lastMonth");
			
			int currentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

			LogUtils.log("###MainActivity.checkPopup.  " +
					"\nlastIndexno : " + lastIndexno +
					"\nlastDate : " + lastDate +
					"\nlastMonth : " + lastMonth +
					"\ncurrentIndexno : " + startupInfo.getPopup().getNotice_nid() +
					"\ncurrentDate : " + currentDate +
					"\ncurrentMonth : " + currentMonth);
			
			if(lastIndexno != startupInfo.getPopup().getNotice_nid() 
					|| lastDate != currentDate 
					|| lastMonth != currentMonth ) {
				gestureSlidingLayout.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						showNoticePopup(startupInfo.getPopup());
					}
				}, 1000);
			}
		}
	}
	
	public void checkVersion() {
		
		String url = ZoneConstants.BASE_URL + "common/androidAppStore" +
				"?sb_id=" + ZoneConstants.PAPP_ID;

		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {
				LogUtils.log("###MainActivity.onErrorRaised.  \nurl : " + url);
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				LogUtils.log("###MainActivity.onCompleted.  \nurl : " + url + "\nresult : " + result);
				
				try {
					JSONObject objJSON = new JSONObject(result);
					int version = com.outspoken_kid.utils.AppInfoUtils.getVersionCode();
					
					if(version < objJSON.getJSONObject("data").getInt("version_nid")) {
						showAlertDialog(getString(R.string.notice), 
								getString(R.string.wannaUpdate), 
								getString(R.string.update), 
								getString(R.string.cancel), 
								new OnPositiveClickedListener() {
							
							@Override
							public void onPositiveClicked() {
								IntentUtils.showMarket(context, "com.zonecomms." + ZoneConstants.PAPP_ID);
							}
						}, true);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				}
			}
		};
		AsyncStringDownloader.download(url, null, ocl);
	}
	
	public SideView getProfileView() {
		
		return profileView;
	}

	public SponserBanner getSponserBanner() {

		if(sponserBanner == null && startupInfo != null) {
			addSponserBanner(startupInfo.getBanners());
		}
		
		return sponserBanner;
	}
	
	public void checkGCM() {
		
		try {
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			final String regId = GCMRegistrar.getRegistrationId(this);

			if(regId == null || regId.equals("")) {
				GCMRegistrar.register(this, ZoneConstants.GCM_SENDER_ID);
			} else {
				updateInfo(regId);
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void updateInfo(String regId) {
		
		AsyncStringDownloader.download(
				ZoneConstants.BASE_URL + "push/androiddevicetoken" +
						"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
						"&registration_id=" + regId,
						null, null);
	}

	public void showRecentPostPage(int spot_nid, boolean isGethering) {
		
		String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/post" +
				"?title=" + ApplicationManager.getTopFragment().getTitleText() +
				"&spot_nid=" + spot_nid +
				"&isGethering=" + isGethering;
		IntentHandlerActivity.actionByUri(Uri.parse(uriString));
	}

	public void signOut() {
		
		try{
			String url = ZoneConstants.BASE_URL + "push/androiddevicetoken" +
					"?member_id=" + URLEncoder.encode(MainActivity.myInfo.getMember_id(), "utf-8") +
					"&device_token=" +
					"&registration_id=" +
					"&sb_id=" + ZoneConstants.PAPP_ID;
			
			AsyncStringDownloader.download(url, null, null);
			
			SharedPrefsUtils.removeVariableFromPrefs(ZoneConstants.PREFS_SIGN, "id");
			SharedPrefsUtils.removeVariableFromPrefs(ZoneConstants.PREFS_SIGN, "pw");
			MainActivity.myInfo = null;
			
			Intent intent = new Intent(this, SignInActivity.class);
			startActivity(intent);
			finish();
		} catch(Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToSignOut);
		}
	}

	public void checkIntent() {
		
		if(getIntent().getData() != null
				&& getIntent().getData().getScheme().equals("popup")) {
			IntentHandlerActivity.actionByUri(getIntent().getData());
			getIntent().setData(null);
		}
	}

	public static void reloadMyInfo() {

		String id = SharedPrefsUtils.getStringFromPrefs(ZoneConstants.PREFS_SIGN, "id");
		String pw = SharedPrefsUtils.getStringFromPrefs(ZoneConstants.PREFS_SIGN, "pw");

		if(StringUtils.isEmpty(id) || StringUtils.isEmpty(pw)) {
			ToastUtils.showToast(R.string.restartApp);
			ApplicationManager.getInstance().getActivity().finish();
		} else{
			SignInActivity.OnAfterSigningInListener osl = new SignInActivity.OnAfterSigningInListener() {

				@Override
				public void OnAfterSigningIn(boolean successSignIn) {

					ApplicationManager.getInstance().getActivity().hideLoadingView();
				}
			};

			ApplicationManager.getInstance().getActivity().showLoadingView();
			SignInActivity.signIn(id, pw, osl);
		}
	}

	public TitleBar getTitleBar() {
		
		return titleBar;
	}
	
/////////////////////////// Interfaces.

	public interface OnAfterCheckNAppListener {
		
		public void onAfterCheckNApp();
	}
}

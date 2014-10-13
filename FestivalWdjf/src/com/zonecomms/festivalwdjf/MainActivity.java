package com.zonecomms.festivalwdjf;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.text.Html;
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
import com.zonecomms.common.utils.ImageDownloadUtils;
import com.zonecomms.common.utils.ImageUploadUtils;
import com.zonecomms.common.utils.ImageUploadUtils.OnAfterUploadImage;
import com.zonecomms.common.views.NoticePopup;
import com.zonecomms.common.views.ProfilePopup;
import com.zonecomms.common.views.SideView;
import com.zonecomms.common.views.SponserBanner;
import com.zonecomms.common.views.TitleBar;
import com.zonecomms.common.views.TitleBar.OnNButtonClickedListener;
import com.zonecomms.common.views.TitleBar.OnSideMenuButtonClickedListener;
import com.zonecomms.common.views.TitleBar.OnWriteButtonClickedListener;
import com.zonecomms.festivalwdjf.classes.ApplicationManager;
import com.zonecomms.festivalwdjf.classes.BaseFragment;
import com.zonecomms.festivalwdjf.classes.SetupClass;
import com.zonecomms.festivalwdjf.classes.ZoneConstants;
import com.zonecomms.festivalwdjf.fragments.AddedProfilePage;
import com.zonecomms.festivalwdjf.fragments.BaseProfilePage;
import com.zonecomms.festivalwdjf.fragments.GridPage;
import com.zonecomms.festivalwdjf.fragments.InformationPage;
import com.zonecomms.festivalwdjf.fragments.LineupPage;
import com.zonecomms.festivalwdjf.fragments.ListPage;
import com.zonecomms.festivalwdjf.fragments.MainPage;
import com.zonecomms.festivalwdjf.fragments.MessagePage;
import com.zonecomms.festivalwdjf.fragments.PostPage;
import com.zonecomms.festivalwdjf.fragments.SettingPage;
import com.zonecomms.festivalwdjf.fragments.StaffPage;
import com.zonecomms.festivalwdjf.fragments.UserPage;

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
	
	//For animationDrawable.
	private AnimationDrawable animationDrawable;
	private ImageView ivAnimationLoadingView;
	private boolean animationLoaded;
	
	//For uploadImage.
	private String filePath;
	private String fileName;
	private boolean isProfileUpload;
	private boolean fadePageAnim;
	private OnAfterUploadImage onAfterUploadImage;
	
	private OnAfterLoginListener onAfterLoginListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			SetupClass.setupApplication(this);
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
				
				checkLoginAndExecute(new OnAfterLoginListener() {
					
					@Override
					public void onAfterLogin() {

						try {
							showWriteActivity(1);
						} catch(Exception e) {
							LogUtils.trace(e);
						}
					}
				});
			}
		});
		
		titleBar.setOnNButtonClickedListener(new OnNButtonClickedListener() {
			
			@Override
			public void onNButtonClicked() {

				checkNApp(null);
			}
		});
	
		softKeyboardDetector.setOnShownKeyboardListener(new OnShownSoftKeyboardListener() {
			
			@Override
			public void onShownSoftKeyboard() {
				try {
					ApplicationManager.getTopFragment().onSoftKeyboardShown();
				} catch(Exception e) {
				}
			}
		});
		
		softKeyboardDetector.setOnHiddenKeyboardListener(new OnHiddenSoftKeyboardListener() {
			
			@Override
			public void onHiddenSoftKeyboard() {
				
				try {
					ApplicationManager.getTopFragment().onSoftKeyboardHidden();
				} catch(Exception e) {
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
		ApplicationManager.clearActivities();
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
							} else {
								
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
					e.printStackTrace();
					ToastUtils.showToast(R.string.failToLoadBitmap);
					clearImageUploadSetting();
				} catch(Exception e) {
					e.printStackTrace();
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
				if(ApplicationManager.getTopFragment() != null
						&& ApplicationManager.getTopFragment() instanceof GridPage) {
					GridPage gridPage = (GridPage)ApplicationManager.getTopFragment();
					gridPage.onRefreshPage();
				}
				
				if(data != null && data.hasExtra("spot_nid")) {
					int spot_nid = data.getIntExtra("spot_nid", 0);
					
					if(spot_nid != 0) {
						showRecentPostPage(spot_nid);
					}
				}
			}
			break;
		
		case ZoneConstants.REQUEST_EDIT:
			
			if(resultCode == RESULT_OK) {
				if(ApplicationManager.getTopFragment() != null) {
					((PostPage)ApplicationManager.getTopFragment()).onRefreshPage();
				}
			}
			break;
			
		case ZoneConstants.REQUEST_SIGN:
			
			if(resultCode == RESULT_OK) {
				
				if(onAfterLoginListener != null) {
					onAfterLoginListener.onAfterLogin();
				}

				ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 120, getProfileView(), 1, 0, null);
			}
			
			onAfterLoginListener = null;
			checkGCM();
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		try {
			checkProfileView();
			
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
							e.printStackTrace();
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
							"&image_size=308" +
							"&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID);
					AsyncStringDownloader.download(url, null, ocl);
				} catch(Exception e) {
					e.printStackTrace();
				}
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

		ApplicationManager.getInstance().setActivity(this);
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}
	
	public void showPostPage(int spot_nid) {
		
		try {
			PostPage pp = new PostPage();
			
			Bundle bundle = new Bundle();
			bundle.putInt("spot_nid", spot_nid);
			
			startPage(pp, bundle);
		} catch(Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}

	public void showInformationPage() {
		
		try {
			InformationPage ip = new InformationPage();
			startPage(ip, null);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void showSettingPage() {
		
		try {
			SettingPage sp = new SettingPage();
			startPage(sp, null);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showMessagePage(String userId) {
		
		try {
			MessagePage mp = new MessagePage();
			Bundle bundle = new Bundle();
			bundle.putString("member_id", userId);
			startPage(mp, bundle);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showBaseProfilePage() {
		
		try {
			BaseProfilePage bp = new BaseProfilePage();
			startPage(bp, null);
		} catch(Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}
	
	public void showAddedProfilePage() {
		
		try {
			AddedProfilePage pp = new AddedProfilePage();
			startPage(pp, null);
		} catch(Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
		}
	}
	
	public void showLineupPage() {
		
		try {
			LineupPage lp = new LineupPage();
			startPage(lp, null);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showStaffPage() {

		try {
			StaffPage sp = new StaffPage();
			startPage(sp, null);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startPage(BaseFragment fragment, Bundle bundle) {

		try {
			if(bundle != null) {
				fragment.setArguments(bundle);
			}

			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

			if(fadePageAnim) {
				fadePageAnim = false;
				//Exclude animation.
			} else if(ApplicationManager.getTopFragment() == null || ApplicationManager.getFragmentsSize() == 0) {
				//MainPage.
			} else if(ApplicationManager.getTopFragment() != null
					&& ApplicationManager.getTopFragment() instanceof MainPage) {
				ft.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top);
			} else {
				ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
			}
			
			if(ApplicationManager.getInstance().getFragments().size() != 0) {
				ft.hide(ApplicationManager.getTopFragment());
			}
			ft.add(R.id.mainActivity_fragmentFrame, fragment);
			ft.commitAllowingStateLoss();
			
			SoftKeyboardUtils.hideKeyboard(this, gestureSlidingLayout);
		} catch(Exception e) {
			e.printStackTrace();
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
				e.printStackTrace();
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
				e.printStackTrace();
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
	
	public void showWriteActivity(int board_nid) {
		Intent intent = new Intent(this, WriteActivity.class);
		intent.putExtra("board_nid", board_nid);
		startActivityWithAnim(intent, ZoneConstants.REQUEST_WRITE);
	}
	
	public void showWriteActivity(int spot_nid, String content, String[] imageUrls, String member_id) {
		Intent intent = new Intent(this, WriteActivity.class);
		intent.putExtra("isEdit", true);
		intent.putExtra("spot_nid", spot_nid);
		intent.putExtra("content", content);
		intent.putExtra("imageUrls", imageUrls);
		intent.putExtra("member_id", member_id);
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
			e.printStackTrace();
		}
	}
	
	public void showLocationWithGoogleMap(double latitude, double longitude) {
		
		Intent intent = new Intent(this, MapActivity.class);
		intent.putExtra("latitude", latitude);
		intent.putExtra("longitude", longitude);
		startActivity(intent);
	}
	
	public void startActivityWithAnim(Intent intent) {
		
		startActivity(intent);
		
		if(ApplicationManager.getTopFragment() != null
				&& ApplicationManager.getTopFragment() instanceof MainPage) {
			overridePendingTransition(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top);
		} else {
			overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_left);
		}
	}
	
	public void startActivityWithAnim(Intent intent, int requestCode) {
		
		startActivityForResult(intent, requestCode);
		
		if(ApplicationManager.getTopFragment() != null
				&& ApplicationManager.getTopFragment() instanceof MainPage) {
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
				R.string.notice,
				R.string.event,
				R.string.story,
				R.string.lineup,
				R.string.music,
				R.string.video,
				R.string.photo,
				R.string.schedule,
				R.string.setting,
				R.string.location,
			};
		
		int[] iconResIds = new int[]{
				R.drawable.bg_profile,
				R.drawable.btn_side_notice,
				R.drawable.btn_side_event,
				R.drawable.btn_side_story,
				R.drawable.btn_side_lineup,
				R.drawable.btn_side_music,
				R.drawable.btn_side_video,
				R.drawable.btn_side_photo,
				R.drawable.btn_side_schedule,
				R.drawable.btn_side_setting,
				R.drawable.btn_side_location,
			};
		
		int size = titleIds.length;
		for(int i=0; i<size; i++) {
			SideMenu sideMenu = new SideMenu();
			sideMenu.setTitleResId(titleIds[i]);
			sideMenus.add(sideMenu);
		}
		
		for(int i=0; i<size; i++){
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
							
							if(I >=0 && I<=11) {
								ApplicationManager.clearFragmentsWithoutMain();
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
								uriString += "story";
								break;
							case 4:
								uriString += "lineup";
								break;
							case 5:
								uriString += "music";
								break;
							case 6:
								uriString += "video";
								break;
							case 7:
								uriString += "photo";
								break;
							case 8:
								uriString += "schedule";
								break;
							case 9:
								uriString += "setting";
								break;
							case 10:
								uriString += "location";
								break;
							}
							
							if(uriString != null) {
								fadePageAnim = true;
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
			
			if(lastIndexno != startupInfo.getPopup().getNotice_nid() && 
					lastDate != currentDate && lastMonth != currentMonth ) {
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
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				try {
					JSONObject objJSON = new JSONObject(result);
					int version = com.outspoken_kid.utils.AppInfoUtils.getVersionCode();
					
					if(version != objJSON.getJSONObject("data").getInt("version_nid")) {
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
	
	public void showAlertDialog(int title, int message, 
			int positive, int negative, 
			DialogInterface.OnClickListener onPositive, 
			DialogInterface.OnClickListener onNegative) {

		String titleString = null;
		String messageString = null;
		String positiveString = null;
		String negativeString = null;
		
		if(title != 0) {
			titleString = getString(title);
		}
		
		if(message != 0) {
			messageString = getString(message);
		}
		
		if(positive != 0) {
			positiveString = getString(positive);
		}
		
		if(negative != 0) {
			negativeString = getString(negative);
		}
		
		showAlertDialog(titleString, messageString, positiveString, negativeString, 
				onPositive, null);
	}
	
	public void showAlertDialog(String title, String message, 
			String positive, String negative, 
			DialogInterface.OnClickListener onPositive, 
			DialogInterface.OnClickListener onNegative) {

		try {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle(title);
			adb.setPositiveButton(positive, onPositive);
			
			if(negative != null) {
				adb.setNegativeButton(negative, onNegative);
			}
			adb.setCancelable(true);
			adb.setOnCancelListener(null);
			adb.setMessage(message);
			adb.show();
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showAlertDialog(String title, String message, 
			final OnPositiveClickedListener onPositiveClickedListener) {
		
		showAlertDialog(title, message, onPositiveClickedListener, true);
	}
	
	public void showAlertDialog(String title, String message, 
			final OnPositiveClickedListener onPositiveClickedListener,
			boolean needCancel) {
		
		try {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle(title);
			adb.setPositiveButton(Html.fromHtml("<B>Ok</B>"), new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {

					if(onPositiveClickedListener != null) {
						
						try {
							onPositiveClickedListener.onPositiveClicked();
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				}
			});
			
			if(needCancel) {
				adb.setNegativeButton(Html.fromHtml("<B>Cancel</B>"), null);
			}
			adb.setCancelable(true);
			adb.setOnCancelListener(null);
			adb.setMessage(message);
			adb.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void showLoadingView() {
		
		if(animationLoaded) {
			try {
				ivAnimationLoadingView.setVisibility(View.VISIBLE);
				animationDrawable.start();
			} catch(Exception e) {
				animationLoaded = false;
				ivAnimationLoadingView = null;
				animationDrawable = null;
				showLoadingView();
			}
		} else if(loadingView != null && loadingView.getVisibility() != View.VISIBLE) {
			loadingView.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideLoadingView() {
		
		if(animationLoaded
				&& ivAnimationLoadingView != null
				&& ivAnimationLoadingView.getVisibility() == View.VISIBLE) {
			try {
				animationDrawable.stop();
				ivAnimationLoadingView.setVisibility(View.INVISIBLE);
			} catch(Exception e) {
				animationLoaded = false;
				ivAnimationLoadingView = null;
				animationDrawable = null;
			}
		}
		
		if(loadingView != null && loadingView.getVisibility() == View.VISIBLE) {
			loadingView.setVisibility(View.INVISIBLE);
		}
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
			e.printStackTrace();
		}
	}
	
	public void updateInfo(String regId) {
		
		AsyncStringDownloader.download(
				ZoneConstants.BASE_URL + "push/androiddevicetoken" +
						"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
						"&registration_id=" + regId,
				null, null);
	}

	public void showRecentPostPage(int spot_nid) {
		
		String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/post?spot_nid=" 
				+ spot_nid;
		IntentHandlerActivity.actionByUri(Uri.parse(uriString));
	}

	public void signOut() {
		
		try{
			String url = ZoneConstants.BASE_URL + "push/androiddevicetoken" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL);
			
			AsyncStringDownloader.download(url, null, null);
			
			SharedPrefsUtils.removeVariableFromPrefs(ZoneConstants.PREFS_SIGN, "id");
			SharedPrefsUtils.removeVariableFromPrefs(ZoneConstants.PREFS_SIGN, "pw");
			MainActivity.myInfo = null;
			
			clearFragmentsWithoutMain();
			
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 0, getProfileView(), 1, 0, null);
		} catch(Exception e) {
			e.printStackTrace();
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
	
	public TitleBar getTitleBar() {
		
		return titleBar;
	}

	public void checkLoginAndExecute(final OnAfterLoginListener listener) {

		//로그인 되어있지 않은 경우 팝업.
		if(MainActivity.myInfo == null) {
			DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {

					onAfterLoginListener = listener;
					launchToSignInActivity();
				}
			};
			showAlertDialog(R.string.signIn, R.string.needSignIn, R.string.confirm, R.string.cancel, ocl, null);
			
		//로그인 되어있는 경우 바로 실행.
		} else {
			listener.onAfterLogin();
		}
	}
	
	public void launchToSignInActivity() {

		Intent intent = new Intent(this, SignInActivity.class);
		startActivityForResult(intent, ZoneConstants.REQUEST_SIGN);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	public void checkProfileView() {
		
		if(MainActivity.myInfo != null) {
			
			if(getProfileView() != null) {

				String url = MainActivity.myInfo.getMember_media_src();
				
				if(!StringUtils.isEmpty(url)) {
					ImageDownloadUtils.downloadImageImmediately(url, null, getProfileView().getIcon(), 150, true);
				} else {
					getProfileView().getIcon().setImageDrawable(null);
				}
				
				String nickname = MainActivity.myInfo.getMember_nickname();
				if(!StringUtils.isEmpty(nickname)) {
					getProfileView().setTitle(nickname);
				}
			}
		}
	}
	
/////////////////////////// Interfaces.

	public interface OnAfterCheckNAppListener {
		
		public void onAfterCheckNApp();
	}

	public interface OnAfterLoginListener {
		
		public void onAfterLogin();
	}
}

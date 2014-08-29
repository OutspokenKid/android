package com.zonecomms.napp;

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
import com.zonecomms.common.views.TitleBar.OnPlusAppButtonClickedListener;
import com.zonecomms.common.views.TitleBar.OnRegionButtonClickedListener;
import com.zonecomms.common.views.TitleBar.OnSideMenuButtonClickedListener;
import com.zonecomms.common.views.TitleBar.OnThemaButtonClickedListener;
import com.zonecomms.common.views.TitleBar.OnWriteButtonClickedListener;
import com.zonecomms.napp.classes.ApplicationManager;
import com.zonecomms.napp.classes.BaseFragment;
import com.zonecomms.napp.classes.ZoneConstants;
import com.zonecomms.napp.fragments.AddedProfilePage;
import com.zonecomms.napp.fragments.BaseProfilePage;
import com.zonecomms.napp.fragments.CategoryPage;
import com.zonecomms.napp.fragments.GetheringListPage;
import com.zonecomms.napp.fragments.GetheringOpenPage;
import com.zonecomms.napp.fragments.GetheringPage;
import com.zonecomms.napp.fragments.GridPage;
import com.zonecomms.napp.fragments.ListPage;
import com.zonecomms.napp.fragments.MainPage;
import com.zonecomms.napp.fragments.MessagePage;
import com.zonecomms.napp.fragments.PostPage;
import com.zonecomms.napp.fragments.SettingPage;
import com.zonecomms.napp.fragments.UserPage;

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
	
	//For uploadImage.
	private String filePath;
	private String fileName;
	private boolean isProfileUpload;
	private boolean fadePageAnim;
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
		
		try {
			addSideViewsToSideMenu();
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
					//마이 홈 작성.
					if(ApplicationManager.getTopFragment() instanceof UserPage) {
						showWriteActivity(getString(R.string.writeRecentPost), false, "", 0, 0, false);
						
					//모임 글 작성.
					} else if(ApplicationManager.getTopFragment() instanceof GetheringPage) {
						String sb_id = ((GetheringPage)ApplicationManager.getTopFragment()).getSb_id();
						String title = getString(R.string.gethering) + " " + getString(R.string.write);
						showWriteActivity(title, true, sb_id, 0, 0, false);
						
					} else if(ApplicationManager.getTopFragment() instanceof GridPage) {
						
						int s_cate_id = ((GridPage)ApplicationManager.getTopFragment()).getS_cate_id();
						String title = ApplicationManager.getTopFragment().getTitleText();
						
						//카테고리 작성.
						if(s_cate_id != 0) {
							showWriteActivity(title + " " + getString(R.string.write), 
									false, "", 0, s_cate_id, true);
							
						//친구 찾기.
						} else if(title.equals(getString(R.string.friend_be))) {
							showWriteActivity(title, false);
								
						//모임 찾기.
						} else if(title.equals(getString(R.string.titleText_getheringIntro))) {
							showWriteActivity(title, true);
						
						//최신 글 작성.
						} else{
							showWriteActivity(getString(R.string.writeRecentPost), false, "", 0, 0, true);
						}
					}
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			}
		});
		
		titleBar.setOnPlusAppButtonClickedListener(new OnPlusAppButtonClickedListener() {
			
			@Override
			public void onPlusAppButtonClicked() {
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/category";
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
	
		titleBar.setOnThemaButtonClickedListener(new OnThemaButtonClickedListener() {
			
			@Override
			public void onThemaButtonClicked() {
				
				try {
					if(ApplicationManager.getTopFragment() instanceof CategoryPage) {
						CategoryPage cp = (CategoryPage) ApplicationManager.getTopFragment(); 
						
						if(cp.getPageType() != ZoneConstants.TYPE_CATEGORY_THEMA) {
							titleBar.hideThemaButton();
							titleBar.showRegionButton();
							
							cp.setPageType(ZoneConstants.TYPE_CATEGORY_THEMA);
							cp.onRefreshPage();
						}
					}
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			}
		});
		
		titleBar.setOnRegionButtonClickedListener(new OnRegionButtonClickedListener() {
			
			@Override
			public void onRegionButtonClicked() {
				
				try {
					if(ApplicationManager.getTopFragment() instanceof CategoryPage) {
						CategoryPage cp = (CategoryPage) ApplicationManager.getTopFragment(); 
						
						if(cp.getPageType() != ZoneConstants.TYPE_CATEGORY_REGION) {
							titleBar.showThemaButton();
							titleBar.hideRegionButton();
							
							cp.setPageType(ZoneConstants.TYPE_CATEGORY_REGION);
							cp.onRefreshPage();
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
						public void onAfterUploadImage(UploadImageInfo uploadImageInfo, Bitmap thumbnail) {
							
							hideLoadingView();
							
							if(onAfterUploadImage != null && thumbnail != null && !thumbnail.isRecycled()) {
								onAfterUploadImage.onAfterUploadImage(uploadImageInfo, thumbnail);
								
								if(isProfileUpload) {
									changeProfileUpload(thumbnail);
									changeMainProfile(thumbnail);
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
					
					int spot_nid = data.getIntExtra("spot_nid", 0);
					boolean isGethering = data.getBooleanExtra("isGethering", false);
					
					if(spot_nid != 0) {
						showRecentPostPage(spot_nid, isGethering);
					}
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
							"&image_size=308" +
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
	
	public void changeMainProfile(Bitmap thumbnail) {

		MainPage.getProfileImageView().setImageBitmap(thumbnail);
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
			
			if(type == ZoneConstants.TYPE_GRID_POST) {
				bundle.putString("concern_kind", data);
			} else if(!StringUtils.isEmpty(data)) {
				
				if(type == ZoneConstants.TYPE_GRID_PAPP || type == ZoneConstants.TYPE_GRID_POST_CATEGORY) {
					bundle.putInt("s_cate_id", Integer.parseInt(data));
				} else if(type == ZoneConstants.TYPE_GRID_MEMBER) {
					bundle.putString("userId", data);
				} else if(type == ZoneConstants.TYPE_GRID_POST_GETHERING) {
					bundle.putString("sb_id", data);
				}
			}
			
			startPage(gp, bundle);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showPostPage(String title, int spot_nid, boolean isGethering) {
		
		try {
			PostPage pp = new PostPage();
			
			Bundle bundle = new Bundle();
			bundle.putString("title", title);
			bundle.putInt("spot_nid", spot_nid);
			bundle.putBoolean("isGethering", isGethering);
			
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
	
	public void showListPage(String title, int type, String data) {
		
		try {
			ListPage lp = new ListPage();
			
			Bundle bundle = new Bundle();
			bundle.putString("title", title);
			bundle.putInt("type", type);

			if(!StringUtils.isEmpty(data) && type == ZoneConstants.TYPE_LIST_PAPP) {
				bundle.putInt("s_cate_id", Integer.parseInt(data));
			}
			
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
	
	public void showCategoryPage(int type, boolean forPost) {
		
		try {
			CategoryPage cp = new CategoryPage();
			
			Bundle bundle = new Bundle();
			bundle.putInt("type", type);
			bundle.putBoolean("forPost", forPost);
			
			if(!forPost) {
				bundle.putString("title", "플러스앱");
			} else if(type == ZoneConstants.TYPE_CATEGORY_THEMA){
				bundle.putString("title", "주제별 최신글");
			} else {
				bundle.putString("title", "지역별 최신글");
			}
			
			startPage(cp, bundle);
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

	/**
	 * 최신 글 작성.
	 * 마이 홈 글 작성.
	 * 카테고리 글 작성.
	 * 모임 글 작성.
	 * 
	 * @param titleText
	 * @param isGethering
	 * @param sb_id
	 * @param board_nid
	 * @param s_cate_id
	 * @param needPosting
	 */
	public void showWriteActivity(String titleText, boolean isGethering, String sb_id, int board_nid, 
			int s_cate_id, boolean needPosting) {
		
		Intent intent = new Intent(this, WriteActivity.class);
		intent.putExtra("titleText", titleText);
		intent.putExtra("isGethering", isGethering);
		intent.putExtra("needPosting", needPosting);
		intent.putExtra("sb_id", sb_id);
		intent.putExtra("board_nid", board_nid);
		intent.putExtra("s_cate_id", s_cate_id);
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
	
	/**
	 * 친구 하기.
	 * 모임 찾기.
	 * 
	 * @param isGethering
	 */
	public void showWriteActivity(String titleText, boolean isGethering) {
		
		Intent intent = new Intent(this, WriteActivity.class);
		intent.putExtra("titleText", titleText);
		intent.putExtra("isFinding", true);
		intent.putExtra("isGethering", isGethering);
		startActivityWithAnim(intent, ZoneConstants.REQUEST_WRITE);
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

	public void addSideViewsToSideMenu() {

		final ArrayList<SideMenu> sideMenus = new ArrayList<SideMenu>();
		
		int[] titleIds = new int[]{
				R.string.id,
				R.string.notice,
				R.string.fullListOfPost,
				R.string.newPostByTheme,
				R.string.newPostByRegion,
				R.string.plusApp,
				R.string.friend_newPost,
				R.string.friend_management,
				R.string.friend_be,
				R.string.friend_search,
				R.string.setting
			};
		
		int[] iconResIds = new int[]{
				R.drawable.bg_profile,
				R.drawable.btn_side_notice,
				R.drawable.btn_side_newpost,
				R.drawable.btn_side_newpostbythema,
				R.drawable.btn_side_newpostbyregion,
				R.drawable.btn_side_plusapp,
				R.drawable.btn_side_friendnewpost,
				R.drawable.btn_side_friendmanagement,
				R.drawable.btn_side_friendbe,
				R.drawable.btn_side_friendsearch,
				R.drawable.btn_side_setting,
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
							
							if(I >=0 && I<=8) {
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
								uriString += "newpost";
								break;
							case 3:
								uriString += "category?forpost=true&order=thema";
								break;
							case 4:
								uriString += "category?forpost=true&order=region";
								break;
							case 5:
								uriString += "category?forpost=false&order=thema";
								break;
							case 6:
								uriString += "friendnewpost";
								break;
							case 7:
								uriString += "friend";
								break;
							case 8:
								uriString += "friendto";
								break;
							case 9:
								uriString += "friendsearch";
								break;
							case 10:
								uriString += "setting";
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
					!(lastDate == currentDate && lastMonth == currentMonth)) {
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
				"?sb_id=";

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
						"?" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_SB_ID) +
						"&registration_id=" + regId,
				null, new OnCompletedListener() {
					
					@Override
					public void onErrorRaised(String url, Exception e) {
						LogUtils.log("GCMIntentService.onRegistered.onError.\nurl : " + url);
					}
					
					@Override
					public void onCompleted(String url, String result) {
						LogUtils.log("GCMIntentService.onRegistered.onCompleted." +
								"\nurl : " + url +
								"\nresult : " + result);
					}
				});
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
			
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					
					LogUtils.log("MainActivity.signOut.onError." +
							"\nurl : " + url);
				}
				
				@Override
				public void onCompleted(String url, String result) {
					
					LogUtils.log("MainActivity.signOut.onCompleted." +
							"\nurl : " + url + "\nresult : " + result);
				}
			};
			
			AsyncStringDownloader.download(url, null, ocl);
			
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

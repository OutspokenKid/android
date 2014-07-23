package com.zonecomms.clubcage;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;

import com.google.android.gcm.GCMRegistrar;
import com.outspoken_kid.classes.BaseFragment;
import com.outspoken_kid.classes.ApplicationManager;
import com.outspoken_kid.classes.SetupClass;
import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.zonecomms.common.utils.AppInfoUtils;
import com.outspoken_kid.utils.BitmapUtils;
import com.outspoken_kid.utils.IntentUtils;
import com.outspoken_kid.utils.NetworkUtils;
import com.outspoken_kid.utils.PackageUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.GestureSlidingLayout;
import com.outspoken_kid.views.SoftKeyboardDetector;
import com.outspoken_kid.views.GestureSlidingLayout.OnAfterCloseListener;
import com.outspoken_kid.views.GestureSlidingLayout.OnAfterOpenListener;
import com.outspoken_kid.views.SoftKeyboardDetector.OnHiddenSoftKeyboardListener;
import com.outspoken_kid.views.SoftKeyboardDetector.OnShownSoftKeyboardListener;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.clubcage.fragments.AddedProfilePage;
import com.zonecomms.clubcage.fragments.BaseProfilePage;
import com.zonecomms.clubcage.fragments.GridPage;
import com.zonecomms.clubcage.fragments.InformationPage;
import com.zonecomms.clubcage.fragments.ListPage;
import com.zonecomms.clubcage.fragments.MainPage;
import com.zonecomms.clubcage.fragments.MessagePage;
import com.zonecomms.clubcage.fragments.PostPage;
import com.zonecomms.clubcage.fragments.SettingPage;
import com.zonecomms.clubcage.fragments.UserPage;
import com.zonecomms.common.models.Banner;
import com.zonecomms.common.models.Media;
import com.zonecomms.common.models.MyInfo;
import com.zonecomms.common.models.Popup;
import com.zonecomms.common.models.SideMenu;
import com.zonecomms.common.models.StartupInfo;
import com.zonecomms.common.models.UploadImageInfo;
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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import com.outspoken_kid.utils.StringUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;

public class MainActivity extends FragmentActivity {

	public static MyInfo myInfo;
	public static StartupInfo startupInfo;
	public static boolean isGoToLeaveMember;
	
	private Context context;
	
	private GestureSlidingLayout gestureSlidingLayout;
	private ScrollView leftView;
	private LinearLayout leftViewInner;
	private LinearLayout topView;
	private TitleBar titleBar;
	private ProfilePopup profilePopup;
	private NoticePopup noticePopup;
	private View cover;
	private View loadingView;
	private SideView profileView;
	private SoftKeyboardDetector softKeyboardDetector;
	private SponserBanner sponserBanner;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		try {
			setContentView(R.layout.activity_main);
			SetupClass.setupApplication(this);
			context = this;
			
			bindViews();
			setVariables();
			createPage();
			setListener();
			setSize();
			
			downloadInfo();
			
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
		cover = findViewById(R.id.mainActivity_cover);
		loadingView = findViewById(R.id.mainActivity_loadingView);
		ivAnimationLoadingView = (ImageView) findViewById(R.id.mainActivity_ivAnimationLoadingView);
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

	public void setListener() {
		
		titleBar.setOnSideMenuButtonClickedListener(new OnSideMenuButtonClickedListener() {
			
			@Override
			public void onSideMenuButtonClicked() {
				gestureSlidingLayout.open(true, null);
			}
		});
		
		titleBar.setOnWriteButtonClickedListener(new OnWriteButtonClickedListener() {
			
			@Override
			public void onWriteButtonClicked() {
				
				BaseFragment bf = ApplicationManager.getTopFragment();
				
				try {
					if(bf instanceof MainPage) {
						((MainPage) bf).showBoardMenu(true);
					} else {
						showWriteActivity(ApplicationManager.getTopFragment().getBoardIndex());
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
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
	
	public void setSize() {
		
		ResizeUtils.viewResize(550, LayoutParams.MATCH_PARENT, leftView, 2, 0, null);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 90, titleBar, 1, 0, null);
		ResizeUtils.viewResize(120, 150, loadingView, 2, Gravity.CENTER, new int[]{0, 45, 0, 0});
		ResizeUtils.viewResize(150, 150, ivAnimationLoadingView, 2, Gravity.CENTER, null);
	}
	
	public void downloadInfo() {
		
		setPage();
	}
	
	public void setPage() {
		
		ApplicationManager.getInstance().setMainActivity(this);
		setAnimationDrawable();
		
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
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(event.getAction() == KeyEvent.ACTION_DOWN) {
			
			switch(keyCode) {
			
			case KeyEvent.KEYCODE_MENU :

				try {
					if(GestureSlidingLayout.isOpenToLeft()) {
						gestureSlidingLayout.close(true, null);
					} else {
						gestureSlidingLayout.open(true, null);
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				break;
			
			case KeyEvent.KEYCODE_BACK :
				
				try {
					if(GestureSlidingLayout.isOpenToLeft()) {
						gestureSlidingLayout.close(true, null);
					} else if(cover.getVisibility() == View.VISIBLE) {
						//Just wait.
					} else if(ApplicationManager.getTopFragment() != null 
							&& ApplicationManager.getTopFragment().onBackKeyPressed()) {
						//Do nothing.
					} else if(noticePopup != null && noticePopup.getVisibility() == View.VISIBLE) {
						noticePopup.hide(null);
					} else if(profilePopup != null && profilePopup.getVisibility() == View.VISIBLE) {
						profilePopup.hide(null);
					} else if(ApplicationManager.getFragmentsSize() > 1){
						closeTopPage();
					} else {
						finish();
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
				break;
				
				default:
					return super.onKeyDown(keyCode, event);
			}
		}
		
		return true;
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
					
					showCover();
					showLoadingView();
					
					OnAfterUploadImage oaui = new OnAfterUploadImage() {
						
						@Override
						public void onAfterUploadImage(UploadImageInfo uploadImageInfo,
								Bitmap thumbnail) {
							
							hideCover();
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
				hideCover();
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
				showCover();
				showLoadingView();
				
				AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
					
					@Override
					public void onErrorRaised(String url, Exception e) {
						
						hideCover();
						hideLoadingView();
					}
					
					@Override
					public void onCompleted(String url, String result) {

						hideCover();
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
							"&image_size=" + ResizeUtils.getSpecificLength(308) +
							"&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID);
					
					AsyncStringDownloader.download(url, null, ocl);
				} catch(Exception e) {
					e.printStackTrace();
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
	
	public void closeTopPage() {
		
		if(ApplicationManager.getTopFragment() != null) {
			ApplicationManager.getTopFragment().finish(true, ApplicationManager.getFragmentsSize() == 2? true : false);
		}
		showTopFragment();
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
	
	public void showGridPage(int numOfColumn, String title, int boardIndex) {
		
		try {
			GridPage gp = new GridPage();
			
			Bundle bundle = new Bundle();
			bundle.putInt("numOfColumn", numOfColumn);
			bundle.putString("title", title);
			bundle.putInt("boardIndex", boardIndex);
			
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
	
	public void showListPage(String title) {
		
		try {
			ListPage lp = new ListPage();
			
			Bundle bundle = new Bundle();
			bundle.putString("title", title);
			
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
	
	public void showTopFragment() {

		try {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			
			if(ApplicationManager.getTopFragment() instanceof MainPage) {
				ft.setCustomAnimations(R.anim.slide_in_from_top, R.anim.slide_out_to_bottom);
			} else {
				ft.setCustomAnimations(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
			}
			
			ft.show(ApplicationManager.getTopFragment());
			ft.commitAllowingStateLoss();
		} catch(Exception e) {
			e.printStackTrace();
		}
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
		
		Intent intent = new Intent(this, ImageViewerActivity.class);

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
	
	public void showLocationWithNaverMap() {
		
		Intent intent = new Intent(this, NaverMapActivity.class);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
	
	public TitleBar getTitleBar() {
		
		return titleBar;
	}

	public void addSideViewsToSideMenu() {

		final ArrayList<SideMenu> sideMenus = new ArrayList<SideMenu>();
		
		int[] titleIds = new int[]{
				R.string.id,
				R.string.notice,
				R.string.schedule,
				R.string.event,
				R.string.board_story,
				R.string.board_review,
				R.string.board_with,
				R.string.board_findPeople,
				R.string.image,
				R.string.video,
				R.string.showMember,
				R.string.setting
			};
		
		int[] iconResIds = new int[]{
				R.drawable.bg_profile,
				R.drawable.btn_side_notice,
				R.drawable.btn_side_schedule,
				R.drawable.btn_side_event,
				R.drawable.btn_side_story,
				R.drawable.btn_side_story,
				R.drawable.btn_side_story,
				R.drawable.btn_side_story,
				R.drawable.btn_side_image,
				R.drawable.btn_side_video,
				R.drawable.btn_side_member,
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
								uriString += "schedule";
								break;
							case 3:
								uriString += "event";
								break;
							case 4:
								uriString += "freetalk";
								break;
							case 5:
								uriString += "review";
								break;
							case 6:
								uriString += "with";
								break;
							case 7:
								uriString += "find";
								break;
							case 8:
								uriString += "image";
								break;
							case 9:
								uriString += "video";
								break;
							case 10:
								uriString += "member";
								break;
							case 11:
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
	
	public void showCover() {
		
		if(cover != null && cover.getVisibility() != View.VISIBLE) {
			cover.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideCover() {

		if(cover != null && cover.getVisibility() == View.VISIBLE) {
			cover.setVisibility(View.INVISIBLE);
		}
	}
	
	public SideView getProfileView() {
		
		return profileView;
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
			e.printStackTrace();
			ToastUtils.showToast(R.string.failToSignOut);
		}
	}

	public void reloadMyInfo() {

		String id = SharedPrefsUtils.getStringFromPrefs(ZoneConstants.PREFS_SIGN, "id");
		String pw = SharedPrefsUtils.getStringFromPrefs(ZoneConstants.PREFS_SIGN, "pw");

		if(StringUtils.isEmpty(id) || StringUtils.isEmpty(pw)) {
			ToastUtils.showToast(R.string.restartApp);
			ApplicationManager.getInstance().getMainActivity().finish();
		} else{
			SignInActivity.OnAfterSigningInListener osl = new SignInActivity.OnAfterSigningInListener() {

				@Override
				public void OnAfterSigningIn(boolean successSignIn) {

					ApplicationManager.getInstance().getMainActivity().hideCover();
					ApplicationManager.getInstance().getMainActivity().hideLoadingView();
				}
			};

			ApplicationManager.getInstance().getMainActivity().showCover();
			ApplicationManager.getInstance().getMainActivity().showLoadingView();
			SignInActivity.signIn(id, pw, osl);
		}
	}
	
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void setAnimationDrawable() {
		
		try {
			int size = startupInfo.getLoadingImageSet().getDrawables().length;
			int time = startupInfo.getLoadingImageSet().getTime();
			
			if(size == 0 || time == 0) {
				return;
			}
			
			animationDrawable = new AnimationDrawable();

			for(int i=0; i<size; i++) {
				animationDrawable.addFrame(startupInfo.getLoadingImageSet().getDrawables()[i], time);
			}
			animationDrawable.setOneShot(false);
			
			if (VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
				ivAnimationLoadingView.setBackground(animationDrawable);
			} else {
				ivAnimationLoadingView.setBackgroundDrawable(animationDrawable);
			}
			
			animationLoaded = true;
		} catch(Exception e) {
			animationDrawable = null;
			animationLoaded = false;
		}
	}
	
/////////////////////////// Interfaces.
	
	public interface OnPositiveClickedListener {
		
		public void onPositiveClicked();
	}

	public interface OnAfterCheckNAppListener {
		
		public void onAfterCheckNApp();
	}
}

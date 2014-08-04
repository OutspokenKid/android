package com.zonecomms.clubcage;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.ImageCacheUtils;
import com.outspoken_kid.utils.ImageCacheUtils.OnAfterLoadBitmap;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.NetworkUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.clubcage.classes.ZonecommsApplication;
import com.zonecomms.common.models.StartupInfo;

public class IntroActivity extends Activity {

	public static boolean isInIntro;
	private boolean isDownloading;
	
	private Bitmap sponserBitmap;
	private View colorBg;
	private View logo;
	private View company;
	private ImageView ivSponser;
	
	private int downloadCount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ZonecommsApplication.initWithActivity(this);
		ZonecommsApplication.setupResources(this);
		setContentView(R.layout.activity_intro);
		bindViews();
		setSizes();
		
		isInIntro = true;
	}
	
	@Override
	protected void onResume() {

		super.onResume();
		
		if(NetworkUtils.checkNetworkStatus(this) == NetworkUtils.TYPE_NONE) {
			
			logo.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					ToastUtils.showToast(R.string.checkNetworkStatus);
				}
			}, 500);

			logo.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					finish();
				}
			}, 2000);
		} else if(!isDownloading) {
			isDownloading = true;
			checkSponserVersion();
		}
	}
	
	public void bindViews() {
		colorBg = findViewById(R.id.introActivity_colorBg);
		logo = findViewById(R.id.introActivity_logo);
		company = findViewById(R.id.introActivity_company);
		ivSponser = (ImageView) findViewById(R.id.introActivity_ivSponser);
	}
	
	public void setSizes() {
		
		ResizeUtils.viewResize(Integer.parseInt(getString(R.string.logo_width)), 
				Integer.parseInt(getString(R.string.logo_height)), 
				logo, 2, Gravity.CENTER, null);
		ResizeUtils.viewResize(328, 32, company, 2, Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, new int[]{0, 0, 0, 30});
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		
		isInIntro = false;
	}
	
////////////////////// Custom methods.
	
	public void checkSponserVersion() {

		int currentSponserVersion = SharedPrefsUtils.getIntegerFromPrefs(ZoneConstants.PREFS_SPONSER, "version");
		String url = ZoneConstants.BASE_URL + "common/mainbanner" +
				"?sb_id=" + ZoneConstants.PAPP_ID +
				"&image_size=640" +
				"&ver=" + currentSponserVersion;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {
			
			@Override
			public void onError(String url) {

				LogUtils.log("IntroActivity.checkSponserVersion.onError.  url : " + url);
				loadOldSponserBitmap();
			}
			
			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				LogUtils.log("IntroActivity.checkSponserVersion.onCompleted.  url : " + url);
				
				try {
					int errorCode = objJSON.getInt("errorCode");

					if(errorCode == 1) {
						int newVersionCode = Integer.parseInt(objJSON.getString("ver"));
						String sponserUrl = objJSON.getString("data");
						downloadSponserBitmap(newVersionCode, sponserUrl);
						
					} else if(errorCode == -1) {
						downloadStartupInfo();
					} else {
						loadOldSponserBitmap();
					}
				} catch(Exception e) {
					LogUtils.trace(e);
					loadOldSponserBitmap();
				}
			}
		});
	}
	
	public void downloadSponserBitmap(final int newVersionCode, String sponserUrl) {
		
		LogUtils.log("IntroActivity.downloadSponserBitmap.  versionCode : " + newVersionCode);
		
		DownloadUtils.downloadBitmap(sponserUrl, new OnBitmapDownloadListener() {
			
			@Override
			public void onError(String url) {

				LogUtils.log("IntroActivity.downloadAndCacheSponser.onError.  url : " + url);
				loadOldSponserBitmap();
			}
			
			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				sponserBitmap = bitmap;
				SharedPrefsUtils.addDataToPrefs(ZoneConstants.PREFS_SPONSER, "version", newVersionCode);
				String fileName = ZoneConstants.PAPP_ID + "_sponser_bitmap";
				ImageCacheUtils.BackgroundFileCaching bfc = new ImageCacheUtils.BackgroundFileCaching(
						IntroActivity.this, bitmap, fileName, false);
				bfc.execute();
				downloadStartupInfo();
			}
		});
	}
	
	public void loadOldSponserBitmap() {

		String key = ZoneConstants.PAPP_ID + "_sponser_bitmap";
		ImageCacheUtils.OnAfterLoadBitmap oalb = new OnAfterLoadBitmap() {
			
			@Override
			public void onAfterLoadBitmap(Bitmap bitmap) {
				
				if(bitmap != null && !bitmap.isRecycled()) {
					sponserBitmap = bitmap;
				}
				
				downloadStartupInfo();
			}
		};
		ImageCacheUtils.BackgroundLoadFileCache blfc = new ImageCacheUtils.BackgroundLoadFileCache(this, key, oalb);
		blfc.execute();
	}

	public void downloadStartupInfo() {

		String url = ZoneConstants.BASE_URL + "common/common_data" +
				"?sb_id=" + ZoneConstants.PAPP_ID +
				"&image_size=640";
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {
			
			@Override
			public void onError(String url) {

				selectAccount();
			}
			
			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					MainActivity.startupInfo = new StartupInfo(objJSON);
					
					if(MainActivity.startupInfo.getBgInfo().urls.size() > 0) {
						downloadBgs();
					} else {
						downloadLoadingImages();
					}
				} catch(Exception e) {
					LogUtils.trace(e);
					downloadLoadingImages();
				}
			}
		});
	}
	
	public void downloadBgs() {
		
		DownloadUtils.downloadBitmap(MainActivity.startupInfo.getBgInfo().urls.get(0),
				new OnBitmapDownloadListener() {

					@Override
					public void onError(String url) {
						
						LogUtils.log("IntroActivity.onError." + "\nurl : " + url);
						downloadLoadingImages();
					}

					@Override
					public void onCompleted(String url, Bitmap bitmap) {

						LogUtils.log("IntroActivity.onCompleted." + "\nurl : " + url);
						downloadLoadingImages();
					}
				});
	}
	
	public void downloadLoadingImages() {
		
		if(MainActivity.startupInfo != null 
				&& MainActivity.startupInfo.getLoadingImageSet() != null
				&& MainActivity.startupInfo.getLoadingImageSet().getImages() != null
				&& MainActivity.startupInfo.getLoadingImageSet().getImages().length != 0) {
			String[] images = MainActivity.startupInfo.getLoadingImageSet().getImages();
			
			final int size = images.length;
			for(int i=0; i<size; i++) {
			
				final int I = i;
				
				DownloadUtils.downloadBitmap(images[i], new OnBitmapDownloadListener() {
					
					@Override
					public void onError(String url) {

						if(++downloadCount == size) {
							selectAccount();
						}
					}
					
					@Override
					public void onCompleted(String url, Bitmap bitmap) {

						try {
							Drawable d = new BitmapDrawable(getResources(), bitmap);
							MainActivity.startupInfo.getLoadingImageSet().getDrawables()[I] = d;
						} catch(Exception e) {
						} catch(OutOfMemoryError oom) {
						}
						
						if(++downloadCount == size) {
							selectAccount();
						}
					}
				});
			}
		} else {
			selectAccount();
		}
	}
	
	public void selectAccount() {

		final String id = SharedPrefsUtils.getStringFromPrefs(ZoneConstants.PREFS_SIGN, "id");
		final String pw = SharedPrefsUtils.getStringFromPrefs(ZoneConstants.PREFS_SIGN, "pw");
		
		LogUtils.log("IntroActivity.selectAccount.  id : " + id + "  pw : " + pw
				+ "  newId : " + getIntent().getStringExtra("id"));
		
		boolean hasLastAccount = !StringUtils.isEmpty(id) && !StringUtils.isEmpty(pw);
		boolean hasNewAccount = getIntent() != null 
				&& getIntent().hasExtra("id")
				&& getIntent().hasExtra("pw");
		boolean duplicatedAccount = hasLastAccount && hasNewAccount && !getIntent().getStringExtra("id").equals(id); 
		
		if(duplicatedAccount) {
			final String newId = getIntent().getStringExtra("id"); 
			
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle(R.string.selectAccount);
			adb.setPositiveButton(id, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					signCheck(id, pw);
				}
			});
			adb.setNegativeButton(newId, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					MainActivity.myInfo = null;
					String newPw = getIntent().getStringExtra("pw");
					signCheck(newId, newPw);
				}
			});
			adb.setCancelable(true);
			adb.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					MainActivity.myInfo = null;
					signCheck(null, null);
				}
			});
			adb.setMessage(R.string.retainLastSession);
			adb.show();
			
		} else if(hasLastAccount){
			signCheck(id, pw);
			
		} else if(hasNewAccount) {
			MainActivity.myInfo = null;
			String newId = getIntent().getStringExtra("id");
			String newPw = getIntent().getStringExtra("pw");
			signCheck(newId, newPw);
		} else {
			MainActivity.myInfo = null;
			signCheck(null, null);
		}
	}

	public void signCheck(String id, String pw) {
		
		LogUtils.log("IntroActivity.signCheck.  id : " + id + ",  pw : " + pw);
		
		if(StringUtils.isEmpty(id) || StringUtils.isEmpty(pw)) {
			isDownloading = false;
			checkStatus();
		} else {
			SignInActivity.OnAfterSigningInListener osl = new SignInActivity.OnAfterSigningInListener() {

				@Override
				public void OnAfterSigningIn(boolean successSignIn) {

					LogUtils.log("IntroActivity.onAfterSignIn.  successSignIn : " + successSignIn);
					
					isDownloading = false;
					checkStatus();
				}
			};
			SignInActivity.signIn(id, pw, osl);
		}
	}

	public void checkStatus() {
		
		LogUtils.log("checkStatus.  isDownloading : "+ isDownloading);
		
		if(!isDownloading) {
			hideLogo();
		}
	}
	
	public void hideLogo() {
		
		AlphaAnimation aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(2000);
		aaOut.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				colorBg.setVisibility(View.INVISIBLE);
				showSponserImage();
			}
		});
		colorBg.startAnimation(aaOut);
		
		AlphaAnimation aaOut2 = new AlphaAnimation(1, 0);
		aaOut2.setDuration(2000);
		aaOut2.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				logo.setVisibility(View.INVISIBLE);
			}
		});
//		logo.startAnimation(aaOut2);
		
		AlphaAnimation aaOut3 = new AlphaAnimation(1, 0);
		aaOut3.setDuration(2000);
		aaOut3.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				company.setVisibility(View.INVISIBLE);
			}
		});
		company.startAnimation(aaOut3);
	}
	
	public void showSponserImage() {

		if(sponserBitmap == null || sponserBitmap.isRecycled()) {
			
			if(MainActivity.startupInfo.getBgInfo() != null
					&& MainActivity.startupInfo.getBgInfo().urls.size() > 0
					&& MainActivity.startupInfo.getBgInfo().colors.size() > 0) {
				launchToCircleMainActivity();
			} else{
				launchToMainActivity();
			}
			return;
		}
		
		AlphaAnimation aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(700);
		aaIn.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				ivSponser.setImageBitmap(sponserBitmap);
				ivSponser.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				
				ivSponser.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						hideSponserImage();
					}
				}, 800);
			}
		});
		ivSponser.startAnimation(aaIn);
	}
	
	public void hideSponserImage() {

		AlphaAnimation aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(500);
		aaOut.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {

				ivSponser.setVisibility(View.INVISIBLE);
				ViewUnbindHelper.unbindReferences(ivSponser);
				
				if(MainActivity.startupInfo.getBgInfo() != null
						&& MainActivity.startupInfo.getBgInfo().urls.size() > 0
						&& MainActivity.startupInfo.getBgInfo().colors.size() > 0) {
					launchToCircleMainActivity();
				} else{
					launchToMainActivity();
				}
			}
		});
		ivSponser.startAnimation(aaOut);
	}

	public void launchToCircleMainActivity() {
		
		Intent intent = new Intent(this, CircleMainActivity.class);
		Intent i = getIntent();				//'i' is intent that passed intent from before.
		
		if(i!= null && i.getData() != null) {
			intent.setData(i.getData());
		}
		
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}
	
	public void launchToMainActivity() {
		
		Intent intent = new Intent(this, MainActivity.class);
		Intent i = getIntent();				//'i' is intent that passed intent from before.
		
		if(i!= null && i.getData() != null) {
			intent.setData(i.getData());
		}
		
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
		finish();
	}
}

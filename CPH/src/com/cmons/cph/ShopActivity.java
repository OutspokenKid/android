package com.cmons.cph;

import java.util.List;
import java.util.Set;

import org.apache.http.cookie.Cookie;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import com.cmons.cph.classes.CmonsFragment;
import com.cmons.cph.classes.CmonsFragmentActivity;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Category;
import com.cmons.cph.models.PushObject;
import com.cmons.cph.models.User;
import com.google.android.gcm.GCMRegistrar;
import com.outspoken_kid.classes.RequestManager;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.ToastUtils;

public abstract class ShopActivity extends CmonsFragmentActivity {

	private static ShopActivity instance;
	
	public User user;
	public Category[] categories;

	public abstract CmonsFragment getFragmentByPageCode(int pageCode);
	public abstract void handleUri(Uri uri);
	
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		instance = this;
		
		checkGCM();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		checkSignStatus();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}

	@Override
	public void setVariables() {

		if(getIntent() != null) {
			user = (User) getIntent().getSerializableExtra("user");
		}
	}
	
	@Override
	public void onBackPressed() {
		
		try {
			if(getTopFragment() != null && getTopFragment().onBackPressed()) {
				//Do nothing.
				return;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		super.onBackPressed();
	}
	
//////////////////// Custom methods.
	
	public static ShopActivity getInstance() {
		
		return instance;
	}
	
	public void checkSignStatus() {
		
		LogUtils.log("###ShopActivity.checkSession.  Get Cookies from cookieStore. =====================");
		
		try {
			List<Cookie> cookies = RequestManager.getCookieStore().getCookies();
			
			for(Cookie cookie : cookies) {
				LogUtils.log("		key : " + cookie.getName() + ", value : " + cookie.getValue());
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		String url = CphConstants.BASE_API_URL + "users/login_check";
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ShopActivity.checkSignStatus.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("ShopActivity.checkSignStatus.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					if(objJSON.getInt("result") != 1) {
						ToastUtils.showToast(objJSON.getString("message"));
						launchSignInActivity();
					} else {
						saveCookies();
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void checkGCM() {

		try {
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			
			final String regId = GCMRegistrar.getRegistrationId(this);
			
			if(regId == null || regId.equals("")) {
				GCMRegistrar.register(this, CphConstants.GCM_SENDER_ID);
			} else {
				updateInfo(regId);
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void checkIntent() {
		
		try {
			if(getIntent() != null && getIntent().hasExtra("pushObject")) {
				PushObject po = (PushObject) getIntent().getSerializableExtra("pushObject");
				handleUri(Uri.parse(po.uri));
				po.uri = null;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void updateInfo(String regId) {
	
		try {
			String url = CphConstants.BASE_API_URL + "users/token_register/android" +
					"?user_id=" + user.getId() +
					"&device_token=" + regId;
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {
				
				@Override
				public void onError(String url) {

					LogUtils.log("###ShopActivity.updateInfo.onError.  \nurl : " + url);
				}
				
				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					LogUtils.log("###ShopActivity.updateInfo.onCompleted.  \nresult : " + objJSON);
				}
			});
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showPage(int pageCode, Bundle bundle) {

		String logString = "###ShopActivity.showPage.  ====================" +
				"\npageCode : " + pageCode;
		
		if(bundle != null) {
			logString += "\nhas bundle.";
			
			Set<String> keySet = bundle.keySet();
			
			for(String key : keySet) {
				logString += "\n    " + key + " : " + bundle.get(key);
			}
		} else {
			logString += "\nhas not bundle.";
		}
		
		CmonsFragment cf = getFragmentByPageCode(pageCode);
		
		if(cf != null) {
			logString += "\n===============================================";
			LogUtils.log(logString);
			startPage(cf, bundle);
		} else {
			logString += "\nFail to get fragment from activity" +
					"\n===============================================";
			LogUtils.log(logString);
		}
	}
	
	public void downloadCategory() {
		
		String url = CphConstants.BASE_API_URL + "categories";
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleActivity.onError." + "\nurl : " + url);

				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {

						downloadCategory();
					}
				}, 100);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleActivity.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					JSONArray arJSON = objJSON.getJSONArray("categories");
					
					int size = arJSON.length();
					categories = new Category[size];
					for(int i=0; i<size; i++) {
						categories[i] = new Category(arJSON.getJSONObject(i));
					}
					
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void signOut() {

		String url = CphConstants.BASE_API_URL + "users/logout";
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForSettingPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForSettingPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					SharedPrefsUtils.clearCookie(getCookieName_D1());
					SharedPrefsUtils.clearCookie(getCookieName_S());
					
					sendRemoveRegId();
					
					launchSignInActivity();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void sendRemoveRegId() {
		
		String url = CphConstants.BASE_API_URL + "users/token_register/android" +
				"?user_id=" + user.getId() +
				"&device_token=";
		DownloadUtils.downloadJSONString(url, null);
	}
	
	public void launchSignInActivity() {
		
		Intent intent = new Intent(this, SignInActivity.class);
		startActivity(intent);
		finish();
	}

	public void showCategorySelectPopup(final boolean needWhole,
			final OnAfterSelectCategoryListener onAfterSelectCategoryListener) {
		
		if(categories == null) {
			return;
		}
		
		int size = categories.length;
		String[] strings = null;
		
		if(needWhole) {
			strings = new String[size + 1];
			
			strings[0] = "전체";
			
			for(int i=0; i<size; i++) {
				strings[i + 1] = categories[i].getName();
			}
		} else {
			strings = new String[size];
			
			for(int i=0; i<size; i++) {
				strings[i] = categories[i].getName();
			}
		}
		
		showSelectDialog(null, strings, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(needWhole && which == 0) {
					if(onAfterSelectCategoryListener != null) {
						onAfterSelectCategoryListener.onAfterSelectCategory(null, null);
					}
					return;
				}
				
				int selectedIndex = needWhole? which - 1 : which;
				
				Category selectedCategory = categories[selectedIndex];
				
				if(selectedCategory.getCategories() != null) {
					showSubCategorySelectPopup(needWhole, selectedCategory, 
							onAfterSelectCategoryListener);
				} else {
					//select completed.
					if(onAfterSelectCategoryListener != null) {
						onAfterSelectCategoryListener.onAfterSelectCategory(selectedCategory, null);
					}
				}
			}
		});
	}
	
	public void showSubCategorySelectPopup(
			final boolean needWhole,
			final Category category,
			final OnAfterSelectCategoryListener onAfterSelectCategoryListener) {
		
		int size = category.getCategories().length;
		
		String[] strings = null;
		
		if(needWhole) {
			strings = new String[size + 1];
			
			strings[0] = "전체";
			
			for(int i=0; i<size; i++) {
				strings[i + 1] = category.getCategories()[i].getName();
			}
		} else {
			strings = new String[size];

			for(int i=0; i<size; i++) {
				strings[i] = category.getCategories()[i].getName();
			}
		}
		
		showSelectDialog(null, strings, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(needWhole && which == 0) {
					if(onAfterSelectCategoryListener != null) {
						onAfterSelectCategoryListener.onAfterSelectCategory(category, null);
					}
					
					return;
				}
				
				int selectedIndex = needWhole? which - 1 : which;
				
				Category selectedCategory = category.getCategories()[selectedIndex];
				
				if(onAfterSelectCategoryListener != null) {
					onAfterSelectCategoryListener.onAfterSelectCategory(category, selectedCategory);
				}
			}
		});
	}
	
	public void setPushRead(int push_id) {
		
		String url = CphConstants.BASE_API_URL + "/notifications/read" +
				"?notification_id=" + push_id;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ShopActivity.setPushRead.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("ShopActivity.setPushRead.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	/**
	 * id : push id. 
	 * receiver_id : 받는 사람의 id.
	 * message : 유저에게 보여줄 메세지.
	 * uri : 연결할 uri.
	 * created_at : 생성된 시간.
	 * pushed_at : 보내진 시간.
	 * read_at : 유저가 읽은 시간.
	 */
	public void handlePushObject(final PushObject pushObject) {
		
		LogUtils.log("###WholesaleActivity.handleIntent.  ");
		
		try {
			String message = pushObject.message;
			String uriString = pushObject.uri;
			
			LogUtils.log("message : " + message);
			LogUtils.log("uriString : " + uriString);

			//팝업을 보여준 후 확인을 누르면 처리.
			showAlertDialog("알림", message, "확인", "닫기", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {

					try {
						setPushRead(pushObject.id);
						handleUri(Uri.parse(pushObject.uri));
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (Error e) {
						LogUtils.trace(e);
					}
				}
			}, null);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public void checkGuidePage(String id, boolean isWholesale) {

		if(!SharedPrefsUtils.getBooleanFromPrefs(CphConstants.PREFS_GUIDE, id)) {
			SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_GUIDE, id, true);
			
			Bundle bundle = new Bundle();
			bundle.putBoolean("isWholesale", isWholesale);
			showPage(CphConstants.PAGE_COMMON_GUIDE, bundle);
		}
	}
	
//////////////////// Interfaces.
	
	public interface OnAfterSelectCategoryListener {
		
		public void onAfterSelectCategory(Category category, Category subCategory);
	}
}

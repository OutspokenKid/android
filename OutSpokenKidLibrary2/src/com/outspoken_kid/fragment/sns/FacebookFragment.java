//package com.outspoken_kid.fragment.sns;
//
//import java.util.Arrays;
//import java.util.List;
//
//import org.json.JSONObject;
//
//import com.facebook.FacebookRequestError;
//import com.facebook.HttpMethod;
//import com.facebook.Request;
//import com.facebook.RequestAsyncTask;
//import com.facebook.Response;
//import com.facebook.Session;
//import com.facebook.SessionState;
//import com.facebook.UiLifecycleHelper;
//import com.facebook.model.GraphUser;
//import com.facebook.widget.LoginButton;
//import com.outspoken_kid.R;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.Toast;
//
//public class FacebookFragment extends SNSFragment {
//
//	private static final String PERMISSION = "publish_actions";
//	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
//	
//	private UiLifecycleHelper uiHelper;
//	
//	private Session.StatusCallback callback = new Session.StatusCallback() {
//		
//		@Override
//		public void call(Session session, SessionState state, Exception exception) {
//			onSessionStateChanged(session, state, exception);
//		}
//	};
//	
//	private String name;
//	private String caption;
//	private String description;
//	private String imageUrl;
//	private String link;
//	
//	private boolean pendingPublishReauthorization = false;
//	
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		
//		if(savedInstanceState != null) {
//			pendingPublishReauthorization = savedInstanceState.getBoolean(PENDING_PUBLISH_KEY);
//			name = savedInstanceState.getString("name");
//			caption = savedInstanceState.getString("caption");
//			description = savedInstanceState.getString("description");
//			imageUrl = savedInstanceState.getString("imageUrl");
//			link = savedInstanceState.getString("link");
//		}
//	}
//	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		uiHelper = new UiLifecycleHelper(getActivity(), callback);
//		uiHelper.onCreate(savedInstanceState);
//	}
//	
//	@Override
//	public void onResume() {
//		super.onResume();
//		Session session = Session.getActiveSession();
//		
//		if(session != null && (session.isOpened() || session.isClosed())) {
//			onSessionStateChanged(session, session.getState(), null);
//		}
//		
//		uiHelper.onResume();
//	}
//	
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		Log.i("notice", "onActivityResult. requestCode : " + requestCode + ",  resultCode : "+ resultCode);
//		uiHelper.onActivityResult(requestCode, resultCode, data);
//		((LoginButton)loginButton).onActivityResult(requestCode, resultCode, data);
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		uiHelper.onPause();
//	}
//	
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		uiHelper.onDestroy();
//	}
//	
//	@Override
//	public void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		
//		try {
//			setUserVisibleHint(true);
//			outState.putBoolean(PENDING_PUBLISH_KEY, pendingPublishReauthorization);
//			outState.putString("name", name);
//			outState.putString("caption", caption);
//			outState.putString("description", description);
//			outState.putString("imageUrl", imageUrl);
//			outState.putString("link", link);
//			uiHelper.onSaveInstanceState(outState);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	public void onSessionStateChanged(Session session, SessionState state, Exception exception) {
//
//		Log.i("notice", "onSessionStateChanged.  opend? : " + state.isOpened());
//		
//		if(state.isOpened()) {
//			
//			if(forPosting) {
//				if(pendingPublishReauthorization &&
//						state.equals(SessionState.OPENED_TOKEN_UPDATED)) {
//					pendingPublishReauthorization = false;
//					posting(new String[]{name, caption, description, imageUrl, link});
//				}
//				
//				needPosting = true;
//				publishButton.setVisibility(View.VISIBLE);
//				coverImage.setBackgroundResource(getPostableImageResId());
//			} else {
//				Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
//					
//					@Override
//					public void onCompleted(GraphUser user, Response response) {
//						
//						userInfo = new FBUserInfo(user, response);
//						executeListener();
//					}
//				});
//				Request.executeBatchAsync(request);
//			}
//		} else {
//			if(forPosting) {
//				needPosting = false;
//				publishButton.setVisibility(View.INVISIBLE);
//				coverImage.setBackgroundResource(getUnPostableImageResId());
//			}
//		}
//	}
//	
//	@Override
//	public boolean isLoggedIn() {
//		
//		try {
//			Session session = Session.getActiveSession();
//			return (session != null && session.isOpened());
//		} catch(Exception e) {
//			e.printStackTrace();
//			return false;
//		}
//	}
//
//	@Override
//	public void logout() {
//
//		clear();
//		
//		try {
//			Session session = Session.getActiveSession();
//	        if (!session.isClosed()) {
//	            session.closeAndClearTokenInformation();
//	        }
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public int posting(String[] params) {
//		
//		try {
//			Session session = Session.getActiveSession();
//			
//			if(session != null) {
//				List<String> permissions = session.getPermissions();
//				if(!permissions.contains(PERMISSION)) {
//					pendingPublishReauthorization = true;
//					this.name = params[0];
//					this.caption = params[1];
//					this.description = params[2];
//					this.imageUrl = params[3];
//					this.link = params[4];
//					session.requestNewPublishPermissions(new Session.NewPermissionsRequest(this, PERMISSION));
//					return PUBLISH_RESULT_FAIL; 
//				}
//				
//				Bundle postParams = new Bundle();
//				postParams.putString("name", name);
//				postParams.putString("caption", caption);
//				postParams.putString("description", description);
//				postParams.putString("link", link);
//				postParams.putString("picture", imageUrl);
//				
//				Request.Callback callback = new Request.Callback() {
//					
//					@Override
//					public void onCompleted(Response response) {
//						
//						try {
//							JSONObject graphResponse = response.getGraphObject().getInnerJSONObject();
//							String postId = graphResponse.getString("id");
//							
//							FacebookRequestError error = response.getError();
//							if(error != null) {
//								Toast.makeText(getActivity().getApplicationContext(), error.getErrorMessage(), Toast.LENGTH_SHORT).show();
//							} else {
//								Toast.makeText(getActivity().getApplicationContext(), postId, Toast.LENGTH_SHORT).show();
//							}
//						} catch(Exception e) {
//							e.printStackTrace();
//						}
//						
//					}
//				};
//				
//				(new RequestAsyncTask(new Request(session, "me/feed", postParams, HttpMethod.POST, callback))).execute();
//				return PUBLISH_RESULT_SUCCESS;
//				
//			}
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		
//		return PUBLISH_RESULT_FAIL;
//	}
//
//	@Override
//	protected int getLoginImageResId() {
//
//		return R.drawable.btn_login_with_fb;
//	}
//
//	@Override
//	protected int getPostableImageResId() {
//		return R.drawable.btn_write_fb_on;
//	}
//
//	@Override
//	protected int getUnPostableImageResId() {
//		return R.drawable.btn_write_fb_off;
//	}
//
//	@Override
//	protected Button getLoginButton(Activity activity) {
//		
//		LoginButton loginButton = new LoginButton(getActivity());
//		loginButton.setFragment(this);
//		loginButton.setBackgroundColor(Color.TRANSPARENT);
//		loginButton.setReadPermissions(Arrays.asList("email", "user_birthday"));
//		return loginButton;
//	}
//
/////////////////////////////////////// Classes.
//	
//	public class FBUserInfo extends UserInfo {
//		
//		private String link;
//		private String fb_id;
//		private String gender;
//		private String quotes;
//		private String bio;
//		private String birthday;
//		private String[] birthdayStrings;
//		
//		public FBUserInfo(GraphUser user, Response response) {
//
//			super();
//			
//			try {
//				if(user != null) {
//					location = (String) response.getGraphObject().getProperty("locale");
//					setLink(user.getLink());
//					setFb_id(user.getId());
//					name = user.getName();
//					setGender((String) response.getGraphObject().getProperty("gender"));
//					setQuotes((String) response.getGraphObject().getProperty("quotes"));
//					setBio((String) response.getGraphObject().getProperty("bio"));
//					email = (String) response.getGraphObject().getProperty("email");
//					setBirthday(user.getBirthday());
//				}
//			} catch(Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		@Override
//		public String toString() {
//			
//			String string = "\n==========================================" +
//					"\nemail : " + email +
//					"\nlocation : " + location +
//					"\nname : " + name +
//					"\nprofileUrl : " + profileUrl +
//					"\nlink : " + link +
//					"\nfb_id : " + fb_id +
//					"\ngender : " + quotes +
//					"\nbio : " + bio +
//					"\nbirthday : " + birthday +
//					"\n==========================================\n";
//			
//			return string;
//		}
//		
//		public String getLink() {
//			return link;
//		}
//
//		public void setLink(String link) {
//			this.link = link;
//		}
//
//		public String getFb_id() {
//			return fb_id;
//		}
//
//		public void setFb_id(String fb_id) {
//			this.fb_id = fb_id;
//		}
//
//		public String getGender() {
//			return gender;
//		}
//
//		public void setGender(String gender) {
//			this.gender = gender;
//		}
//
//		public String getQuotes() {
//			return quotes;
//		}
//
//		public void setQuotes(String quotes) {
//			this.quotes = quotes;
//		}
//
//		public String getBio() {
//			return bio;
//		}
//
//		public void setBio(String bio) {
//			this.bio = bio;
//		}
//
//		public String getBirthday() {
//			return birthday;
//		}
//
//		public void setBirthday(String birthday) {
//			this.birthday = birthday;
//		}
//
//		public String[] getBirthdayStrings() {
//			return birthdayStrings;
//		}
//
//		public void setBirthdayStrings(String[] birthdayStrings) {
//			this.birthdayStrings = birthdayStrings;
//		}
//		
//		public int getBirth_year() {
//			
//			try {
//				if(birthdayStrings == null) {
//					birthdayStrings = birthday.split("/");
//				}
//				return Integer.parseInt(birthdayStrings[2]);
//			} catch(Exception e) {
//			}
//			
//			return 0;
//		}
//
//		public int getBirth_Month() {
//			
//			try {
//				if(birthdayStrings == null) {
//					birthdayStrings = birthday.split("/");
//				}
//				return Integer.parseInt(birthdayStrings[0]);
//			} catch(Exception e) {
//			}
//			
//			return 0;
//		}
//		
//		public int getBirth_date() {
//			
//			try {
//				if(birthdayStrings == null) {
//					birthdayStrings = birthday.split("/");
//				}
//				return Integer.parseInt(birthdayStrings[1]);
//			} catch(Exception e) {
//			}
//			
//			return 0;
//		}
//	}
//}

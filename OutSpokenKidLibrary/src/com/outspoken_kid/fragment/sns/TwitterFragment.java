//package com.outspoken_kid.fragment.sns;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.util.Date;
//
//import com.outspoken_kid.R;
//
//import twitter4j.StatusUpdate;
//import twitter4j.Twitter;
//import twitter4j.TwitterException;
//import twitter4j.TwitterFactory;
//import twitter4j.User;
//import twitter4j.auth.AccessToken;
//import twitter4j.auth.RequestToken;
//import twitter4j.conf.Configuration;
//import twitter4j.conf.ConfigurationBuilder;
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//
///**
// * 
// * This fragment must be contained by 'singleInstance' or 'singleTask' activity.
// * Call 'twFragment.onNewIntent(intent);' in 'onNewIntent(Intent intent)'. 
// * @author HyungGunKim
// *
// */
//public class TwitterFragment extends SNSFragment {
//	
//	public static String TWITTER_CONSUMER_KEY = "0Rk2aKRwkJ4TzPUpAfBA";
//    public static String TWITTER_CONSUMER_SECRET = "HjCZTs9qHs0pmtx7HnkN4xMd5Gik6a68LkPcA80BQ";
//    
//    public static String TWITTER_CALLBACK_URL = "napp://com.zonecomms.Twitter_oAuth";
//    public static String URL_PARAMETER_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
//    public static String PREFERENCE_TWITTER_OAUTH_TOKEN="TWITTER_OAUTH_TOKEN";
//    public static String PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET="TWITTER_OAUTH_TOKEN_SECRET";
//    public static String PREFERENCE_TWITTER_IS_LOGGED_IN="TWITTER_IS_LOGGED_IN";
//	
//	private RequestToken requestToken = null;
//    private TwitterFactory twitterFactory = null;
//    private Twitter twitter;
//    
//    private String text;
//	private String filePath;
//
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//    	super.onActivityCreated(savedInstanceState);
//
//    	if(savedInstanceState != null) {
//    		text = savedInstanceState.getString("text");
//    		filePath = savedInstanceState.getString("filePath");
//    	}
//    	
//        setTwitter();
//    }
//    
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//    	super.onSaveInstanceState(outState);
//    	
//    	try {
//			setUserVisibleHint(true);
//			outState.putString("text", text);
//			outState.putString("filePath", filePath);
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//    }
//    
//	@Override
//	public boolean isLoggedIn() {
//
//		try {
//			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//	        if (sharedPreferences.getBoolean(PREFERENCE_TWITTER_IS_LOGGED_IN,false)) {
//	        	return true;
//	        }
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}
//
//	@Override
//	public void logout() {
//
//		clear();
//		
//		try {
//			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//	        SharedPreferences.Editor editor = sharedPreferences.edit();
//	        editor.remove(PREFERENCE_TWITTER_OAUTH_TOKEN);
//	        editor.remove(PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET);
//	        editor.remove(PREFERENCE_TWITTER_IS_LOGGED_IN);
//	        editor.commit();
//	        requestToken = null;
//		    twitterFactory = null;
//		    twitter = null;
//	        text = null;
//	        filePath = null;
//		} catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public int posting(String[] params) {
//
//		try {
//			(new TwitterUpdateStatusTask(params[0], null)).execute(params);
//			return PUBLISH_RESULT_SUCCESS;
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
//		return R.drawable.btn_login_with_tw;
//	}
//
//	@Override
//	protected int getPostableImageResId() {
//		
//		return R.drawable.btn_write_tw_on;
//	}
//
//	@Override
//	protected int getUnPostableImageResId() {
//		
//		return R.drawable.btn_write_tw_off;
//	}
//
//	@Override
//	protected Button getLoginButton(Activity activity) {
//		
//		Button loginButton = new Button(activity);
//		loginButton.setText("twitterLogin");
//		loginButton.setBackgroundColor(Color.TRANSPARENT);
//		loginButton.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				
//				if(isLoggedIn()) {
//					if(forPosting) {
//						//Do nothing.
//					} else {
//						executeListener();
//					}
//				} else {
//					login();
//				}
//			}
//		});
//		return loginButton;
//	}
//	
//////////////////////////// Custom methods.
//	
//	public void onNewIntent(Intent intent) {
//	
//		if(intent == null || intent.getData() == null) {
//			return;
//		}
//		
//		Uri uri = intent.getData();
//		if (uri != null && uri.toString().startsWith(TWITTER_CALLBACK_URL)) {
//            String verifier = uri.getQueryParameter(URL_PARAMETER_TWITTER_OAUTH_VERIFIER);
//            new TwitterGetAccessTokenTask().execute(verifier);
//		}
//	}
//	
//	public void login() {
//		
//		setTwitter();
//		
//		new TwitterAuthenticateTask().execute();
//	}
//	
//	public void setTwitterFactory(AccessToken accessToken) {
//		
//        twitter = twitterFactory.getInstance(accessToken);
//    }
//	
//	public RequestToken getRequestToken() {
//
//		if (requestToken == null) {
//            try {
//                requestToken = twitterFactory.getInstance().getOAuthRequestToken(TWITTER_CALLBACK_URL);
//            } catch (TwitterException e) {
//                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//        }
//		
//        return requestToken;
//    }
//	
//	private void setTwitter() {
//		
//		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
//        configurationBuilder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
//        configurationBuilder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
//        Configuration configuration = configurationBuilder.build();
//        twitterFactory = new TwitterFactory(configuration);
//        twitter = twitterFactory.getInstance();
//	}
//	
//	public void onUpdateLoginStatus() {
//
//		if(isLoggedIn()) {
//			
//			if(forPosting) {
//				needPosting = true;
//				publishButton.setVisibility(View.VISIBLE);
//				coverImage.setBackgroundResource(getPostableImageResId());
//			} else {
//				executeListener();
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
//////////////////////////// Classes.
//
//	class TWUserInfo extends UserInfo {
//		
//		private long id;
//		private String description;
//		private String lang;
//		private String screenName;
//		private String timeZone;
//		private Date createdAt;
//		private String url;
//		private String backgroundImageUrl;
//		private String bannerUrl;
//		
//		public TWUserInfo(User user) {
//
//			try {
//				if(user != null) {
//					location = user.getLocation();
//					name = user.getName();
//					profileUrl = user.getOriginalProfileImageURL();
//					id = user.getId();
//					description = user.getDescription();
//					lang = user.getLang();
//					screenName = user.getScreenName();
//					timeZone = user.getTimeZone();
//					createdAt = user.getCreatedAt();
//					url = user.getURL();
//					backgroundImageUrl = user.getProfileBackgroundImageURL();
//					bannerUrl = user.getProfileBannerURL();
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
//					"\nid : " + id +
//					"\ndescription : " + description +
//					"\nlang : " + lang +
//					"\nscreenName : " + screenName +
//					"\ntimeZone : " + timeZone +
//					"\ncreatedAt : " + createdAt +
//					"\nurl : " + url +
//					"\nbackgroundImageUrl : " + backgroundImageUrl +
//					"\nbannerUrl : " + bannerUrl +
//					"\n==========================================\n";
//			
//			return string;
//		}
//		
//		public long getId() {
//			return id;
//		}
//
//		public void setId(long id) {
//			this.id = id;
//		}
//
//		public String getDescription() {
//			return description;
//		}
//
//		public void setDescription(String description) {
//			this.description = description;
//		}
//
//		public String getLang() {
//			return lang;
//		}
//
//		public void setLang(String lang) {
//			this.lang = lang;
//		}
//
//		public String getScreenName() {
//			return screenName;
//		}
//
//		public void setScreenName(String screenName) {
//			this.screenName = screenName;
//		}
//
//		public String getTimeZone() {
//			return timeZone;
//		}
//
//		public void setTimeZone(String timeZone) {
//			this.timeZone = timeZone;
//		}
//
//		public Date getCreatedAt() {
//			return createdAt;
//		}
//
//		public void setCreatedAt(Date createdAt) {
//			this.createdAt = createdAt;
//		}
//
//		public String getUrl() {
//			return url;
//		}
//
//		public void setUrl(String url) {
//			this.url = url;
//		}
//
//		public String getBackgroundImageUrl() {
//			return backgroundImageUrl;
//		}
//
//		public void setBackgroundImageUrl(String backgroundImageUrl) {
//			this.backgroundImageUrl = backgroundImageUrl;
//		}
//
//		public String getBannerUrl() {
//			return bannerUrl;
//		}
//
//		public void setBannerUrl(String bannerUrl) {
//			this.bannerUrl = bannerUrl;
//		}
//	}
//	
//	class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {
//
//        @Override
//        protected void onPostExecute(RequestToken requestToken) {
//        	
//        	if(requestToken != null) {
//        		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(requestToken.getAuthenticationURL()));
//                startActivity(intent);
//        	}
//        }
//
//        @Override
//        protected RequestToken doInBackground(String... params) {
//            return getRequestToken();
//        }
//    }
//
//	class TwitterGetAccessTokenTask extends AsyncTask<String, String, User> {
//
//        @Override
//        protected void onPostExecute(User user) {
//
//        	if(user != null) {
//        		userInfo = new TWUserInfo(user);
//        	}
//
//        	onUpdateLoginStatus();
//        	executeListener();
//        }
//
//        @Override
//        protected User doInBackground(String... params) {
//
//        	try {
//        		RequestToken requestToken = getRequestToken();
//        		
//                if (params[0] != null && params[0].length() != 0) {
//                    try {
//                        AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
//                        
//                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putString(PREFERENCE_TWITTER_OAUTH_TOKEN, accessToken.getToken());
//                        editor.putString(PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, accessToken.getTokenSecret());
//                        editor.putBoolean(PREFERENCE_TWITTER_IS_LOGGED_IN, true);
//                        editor.commit();
//                        
//                        return twitter.showUser(accessToken.getUserId());
//                    } catch (TwitterException e) {
//                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                    }
//                } else {
//                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//                    String accessTokenString = sharedPreferences.getString(PREFERENCE_TWITTER_OAUTH_TOKEN, "");
//                    String accessTokenSecret = sharedPreferences.getString(PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
//                    AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
//                    
//                    try {
//                        setTwitterFactory(accessToken);
//                        return twitter.showUser(accessToken.getUserId());
//                    } catch (TwitterException e) {
//                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                    }
//                }
//        	} catch(Exception e) {
//        		e.printStackTrace();
//        	}
//        	
//            return null;  //To change body of implemented methods use File | Settings | File Templates.
//        }
//    }
//
//	class TwitterUpdateStatusTask extends AsyncTask<String, String, Boolean> {
//
//		private String message;
//		private Bitmap bitmap;
//		
//		public TwitterUpdateStatusTask(String message, Bitmap bitmap) {
//			
//			this.message = message;
//			this.bitmap = bitmap;
//		}
//		
//        @Override
//        protected Boolean doInBackground(String... params) {
//        	
//        	try{
//        		StatusUpdate status = new StatusUpdate(message);
//            	ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            	bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
//            	byte[] imageInByte = stream.toByteArray();
//            	ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
//            	status.setMedia("uploadFile", bis);
//        		
//        		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//                String accessTokenString = sharedPreferences.getString(PREFERENCE_TWITTER_OAUTH_TOKEN, "");
//                String accessTokenSecret = sharedPreferences.getString(PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
//                
//        		if(accessTokenString != null
//        				&& accessTokenString.length() != 0
//        				&& accessTokenSecret != null
//        				&& accessTokenSecret.length() != 0) {
//        			AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
//        			twitterFactory.getInstance(accessToken).updateStatus(status);
//        		} else {
//        			twitter.updateStatus(status);
//        		}
//                
//                text = null;
//    	        filePath = null;
//        		return true;
//        		
//        	} catch(Exception e){
//        		e.printStackTrace();
//        	} catch(Error e){
//            	e.printStackTrace();
//            }
//        	
//        	return false;
//        }
//    }
//}

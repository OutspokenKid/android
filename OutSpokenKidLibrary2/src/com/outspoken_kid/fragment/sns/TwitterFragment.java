package com.outspoken_kid.fragment.sns;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.outspoken_kid.model.SNSUserInfo;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.views.WebBrowser;
import com.outspoken_kid.views.WebBrowser.OnActionWithKeywordListener;

/**
 * 
 * This fragment must be contained by 'singleInstance' or 'singleTask' activity.
 * Call 'twFragment.onNewIntent(intent);' in 'onNewIntent(Intent intent)'. 
 * @author HyungGunKim
 *
 */
public abstract class TwitterFragment extends SNSFragment {
    
    protected String URL_PARAMETER_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    protected String PREFERENCE_TWITTER_OAUTH_TOKEN="TWITTER_OAUTH_TOKEN";
    protected String PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET="TWITTER_OAUTH_TOKEN_SECRET";
    protected String PREFERENCE_TWITTER_IS_LOGGED_IN="TWITTER_IS_LOGGED_IN";
	
	private RequestToken requestToken = null;
    private TwitterFactory twitterFactory = null;
    private Twitter twitter;

    private TwitterUserInfo twitterUserInfo;
    
    private String text;
	private String filePath;

	public abstract WebBrowser getWebBrowser();
	public abstract String getTwitterConsumerKey();
	public abstract String getTwitterConsumerSecret();
	public abstract String getTwitterCallBackUrl();
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);

    	if(savedInstanceState != null) {
    		text = savedInstanceState.getString("text");
    		filePath = savedInstanceState.getString("filePath");
    	}
    	
        setTwitter();
        
        getCoverImage().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(isLoggedIn()) {
					executeListener();
				} else {
					loginButton.performClick();
				}
			}
		});
        
        getWebBrowser().putAction("http://clubanswer.twitter.oauth.response", 
        		new OnActionWithKeywordListener() {
			
			@Override
			public void onActionWithKeyword(String keyword, String url) {

				LogUtils.log("###\n\n.onActionWithKeyword. " +
						"\nkeyword : " + keyword + 
						"\nurl : " + url +
						" \n\n########################");
				getWebBrowser().close();
				
				//http://clubanswer.twitter.oauth.response/?oauth_token=GpN5ttV3GaZ9OXZ3f6Lb45UoOJPxw3j0&oauth_verifier=beL0EAePg7PmUXCI2ZLMU3bwDEGGBZ4J 
				Uri uri = Uri.parse(url);
				Intent intent = new Intent();
				intent.setData(uri);
				onNewIntent(intent);
			}
		});

        logout();
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	
    	try {
			setUserVisibleHint(true);
			outState.putString("text", text);
			outState.putString("filePath", filePath);
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
	@Override
	public boolean isLoggedIn() {

		try {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
	        if (sharedPreferences.getBoolean(PREFERENCE_TWITTER_IS_LOGGED_IN,false)) {
	        	return true;
	        }
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void logout() {

		try {
			SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
	        SharedPreferences.Editor editor = sharedPreferences.edit();
	        editor.remove(PREFERENCE_TWITTER_OAUTH_TOKEN);
	        editor.remove(PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET);
	        editor.remove(PREFERENCE_TWITTER_IS_LOGGED_IN);
	        editor.commit();
	        requestToken = null;
		    twitterFactory = null;
		    twitter = null;
	        text = null;
	        filePath = null;
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Button getLoginButton(Activity activity) {
		
		Button loginButton = new Button(activity);
		loginButton.setText("twitterLogin");
		loginButton.setBackgroundColor(Color.TRANSPARENT);
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				login();
			}
		});
		return loginButton;
	}
	
////////////////////////// Custom methods.
	
	public void onNewIntent(Intent intent) {
	
		if(intent == null || intent.getData() == null) {
			return;
		}
		
		Uri uri = intent.getData();
		if (uri != null && uri.toString().startsWith(getTwitterCallBackUrl())) {
            String verifier = uri.getQueryParameter(URL_PARAMETER_TWITTER_OAUTH_VERIFIER);
            new TwitterGetAccessTokenTask().execute(verifier);
		}
	}
	
	public void login() {
		
		setTwitter();
		
		new TwitterAuthenticateTask().execute();
	}
	
	public void setTwitterFactory(AccessToken accessToken) {
		
        twitter = twitterFactory.getInstance(accessToken);
    }
	
	public RequestToken getRequestToken() {

		if (requestToken == null) {
            try {
                requestToken = twitterFactory.getInstance().getOAuthRequestToken(getTwitterCallBackUrl());
            } catch (TwitterException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
		
        return requestToken;
    }
	
	private void setTwitter() {
		
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(getTwitterConsumerKey());
        configurationBuilder.setOAuthConsumerSecret(getTwitterConsumerSecret());
        Configuration configuration = configurationBuilder.build();
        twitterFactory = new TwitterFactory(configuration);
        twitter = twitterFactory.getInstance();
	}

	public void executeListener() {
		
		if(onAfterSignInListener != null) {
			onAfterSignInListener.OnAfterSignIn(twitterUserInfo);
		}
	}
	
////////////////////////// Classes.

	public class TwitterUserInfo extends SNSUserInfo {

		public String location;
		public String name;
		public String profileUrl;
		public long id;
		public String description;
		public String lang;
		public String screenName;
		public String timeZone;
		public Date createdAt;
		public String url;
		public String backgroundImageUrl;
		public String bannerUrl;
		
		public TwitterUserInfo(User user) {

			try {
				if(user != null) {
					location = user.getLocation();
					name = user.getName();
					profileUrl = user.getOriginalProfileImageURL();
					id = user.getId();
					description = user.getDescription();
					lang = user.getLang();
					screenName = user.getScreenName();
					timeZone = user.getTimeZone();
					createdAt = user.getCreatedAt();
					url = user.getURL();
					backgroundImageUrl = user.getProfileBackgroundImageURL();
					bannerUrl = user.getProfileBannerURL();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void printSNSUserInfo() {

			LogUtils.log("###TwitterFragment.printSNSUserInfo.  " +
					"######################################" +
					"\nlocation : " + location +
					"\nname : " + name +
					"\nprofileUrl : " + profileUrl +
					"\nid : " + id +
					"\ndescription : " + description +
					"\nlang : " + lang +
					"\nscreenName : " + screenName +
					"\ntimeZone : " + timeZone +
					"\ncreatedAt : " + createdAt +
					"\nurl : " + url +
					"\nbackgroundImageUrl : " + backgroundImageUrl +
					"\nbannerUrl : " + bannerUrl +
					"\n######################################");
		}
	}
	
	class TwitterAuthenticateTask extends AsyncTask<String, String, RequestToken> {

        @Override
        protected RequestToken doInBackground(String... params) {
            return getRequestToken();
        }
        
        @Override
        protected void onPostExecute(RequestToken requestToken) {
        	
        	if(requestToken != null) {
                getWebBrowser().open(requestToken.getAuthenticationURL(), null);
        	}
        }
    }

	class TwitterGetAccessTokenTask extends AsyncTask<String, String, User> {

        @Override
        protected User doInBackground(String... params) {

        	try {
        		RequestToken requestToken = getRequestToken();
        		
                if (params[0] != null && params[0].length() != 0) {
                    try {
                        AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, params[0]);
                        
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(PREFERENCE_TWITTER_OAUTH_TOKEN, accessToken.getToken());
                        editor.putString(PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, accessToken.getTokenSecret());
                        editor.putBoolean(PREFERENCE_TWITTER_IS_LOGGED_IN, true);
                        editor.commit();
                        
                        return twitter.showUser(accessToken.getUserId());
                    } catch (TwitterException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                } else {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    String accessTokenString = sharedPreferences.getString(PREFERENCE_TWITTER_OAUTH_TOKEN, "");
                    String accessTokenSecret = sharedPreferences.getString(PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
                    AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
                    
                    try {
                        setTwitterFactory(accessToken);
                        return twitter.showUser(accessToken.getUserId());
                    } catch (TwitterException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
        	} catch(Exception e) {
        		e.printStackTrace();
        	}
        	
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        protected void onPostExecute(User user) {

        	if(user != null) {
        		twitterUserInfo = new TwitterUserInfo(user);
        	}

        	executeListener();
        }
	}

	class TwitterUpdateStatusTask extends AsyncTask<String, String, Boolean> {

		private String message;
		private Bitmap bitmap;
		
		public TwitterUpdateStatusTask(String message, Bitmap bitmap) {
			
			this.message = message;
			this.bitmap = bitmap;
		}
		
        @Override
        protected Boolean doInBackground(String... params) {
        	
        	try{
        		StatusUpdate status = new StatusUpdate(message);
            	ByteArrayOutputStream stream = new ByteArrayOutputStream();
            	bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
            	byte[] imageInByte = stream.toByteArray();
            	ByteArrayInputStream bis = new ByteArrayInputStream(imageInByte);
            	status.setMedia("uploadFile", bis);
        		
        		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String accessTokenString = sharedPreferences.getString(PREFERENCE_TWITTER_OAUTH_TOKEN, "");
                String accessTokenSecret = sharedPreferences.getString(PREFERENCE_TWITTER_OAUTH_TOKEN_SECRET, "");
                
        		if(accessTokenString != null
        				&& accessTokenString.length() != 0
        				&& accessTokenSecret != null
        				&& accessTokenSecret.length() != 0) {
        			AccessToken accessToken = new AccessToken(accessTokenString, accessTokenSecret);
        			twitterFactory.getInstance(accessToken).updateStatus(status);
        		} else {
        			twitter.updateStatus(status);
        		}
                
                text = null;
    	        filePath = null;
        		return true;
        		
        	} catch(Exception e){
        		e.printStackTrace();
        	} catch(Error e){
            	e.printStackTrace();
            }
        	
        	return false;
        }
    }
}

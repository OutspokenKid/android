package com.zonecomms.common.views;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.classes.ApplicationManager;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.GestureSlidingLayout;
import com.zonecomms.clubmania.IntentHandlerActivity;
import com.zonecomms.clubmania.MainActivity;
import com.zonecomms.clubmania.R;
import com.zonecomms.clubmania.classes.ZoneConstants;
import com.zonecomms.common.models.MyStoryInfo;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.utils.ImageDownloadUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
//import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ProfilePopup extends FrameLayout {

	private static int madeCount = 2681;

	private MyStoryInfo myStoryInfo;
	
	private TextView tvNickname;
	private TextView tvGenderWithAge;
	private ImageView ivImage;
	private View home, friend, message, close;
	
	private boolean isAnimating;
	private AlphaAnimation aaIn;
	
	public ProfilePopup(Context context) {
		super(context);
		init();
	}

	private void init() {
	
		madeCount += 10;
		
		this.setVisibility(View.INVISIBLE);

		View bg = new View(getContext());
		bg.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
														LayoutParams.MATCH_PARENT));
		bg.setBackgroundColor(Color.argb(180, 0, 0, 0));
		bg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hide(null);
			}
		});
		this.addView(bg);

		FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		fp.gravity = Gravity.CENTER;
		RelativeLayout relative = new RelativeLayout(getContext());
		relative.setLayoutParams(fp);
		relative.setClickable(true);
		relative.setBackgroundColor(Color.BLACK);
		this.addView(relative);
		
		RelativeLayout.LayoutParams rp = null; 
		int width = ResizeUtils.getSpecificLength(400);
		
		//id : 0
		tvNickname = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(width, ResizeUtils.getSpecificLength(50));
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		tvNickname.setLayoutParams(rp);
		tvNickname.setBackgroundColor(Color.rgb(100, 100, 100));
		tvNickname.setGravity(Gravity.CENTER);
		tvNickname.setId(madeCount);
		tvNickname.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvNickname, 30);
		FontInfo.setFontStyle(tvNickname, FontInfo.BOLD);
		relative.addView(tvNickname);
		
		tvGenderWithAge = new TextView(getContext());
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ResizeUtils.getSpecificLength(50));
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		tvGenderWithAge.setLayoutParams(rp);
		tvGenderWithAge.setGravity(Gravity.CENTER_VERTICAL);
		tvGenderWithAge.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvGenderWithAge, 26);
		relative.addView(tvGenderWithAge);
		
		View line1 = new View(getContext());
		rp = new RelativeLayout.LayoutParams(width, 2);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		line1.setLayoutParams(rp);
		line1.setBackgroundColor(Color.rgb(50, 50, 50));
		relative.addView(line1);
		
		View imageBg = new View(getContext());
		rp = new RelativeLayout.LayoutParams(width, width);
		rp.addRule(RelativeLayout.BELOW, madeCount);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		imageBg.setLayoutParams(rp);
		imageBg.setBackgroundResource(R.drawable.bg_profile_400);
		relative.addView(imageBg);
		
		//id : 1
		ivImage = new ImageView(getContext());
		rp = new RelativeLayout.LayoutParams(width, width);
		rp.addRule(RelativeLayout.BELOW, madeCount);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		ivImage.setLayoutParams(rp);
		ivImage.setId(madeCount + 1);
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		ivImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(myStoryInfo != null && !TextUtils.isEmpty(myStoryInfo.getMystory_member_profile())) {
					String title = myStoryInfo.getMystory_member_nickname();
					String[] imageUrls = new String[]{myStoryInfo.getMystory_member_profile()};
					ApplicationManager.getInstance().getMainActivity().showImageViewerActivity(title, imageUrls, null, 0);
				}
			}
		});
		relative.addView(ivImage);

		int l = (width - 8) / 4;
		int mod = (width - 8) % 4;
		
		LinearLayout buttonLinear = new LinearLayout(getContext());
		rp = new RelativeLayout.LayoutParams(width, l);
		rp.addRule(RelativeLayout.BELOW, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		buttonLinear.setLayoutParams(rp);
		buttonLinear.setBackgroundColor(Color.rgb(100, 100, 100));
		relative.addView(buttonLinear);
		
		addButtons(buttonLinear, l, mod);
		
		View line2 = new View(getContext());
		rp = new RelativeLayout.LayoutParams(width, 2);
		rp.addRule(RelativeLayout.BELOW, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		line2.setLayoutParams(rp);
		line2.setBackgroundColor(Color.rgb(50, 50, 50));
		relative.addView(line2);
		
		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(200);
		aaIn.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				isAnimating = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				isAnimating = false;
			}
		});
	}
	
	public void show(String userId) {
		
		if(isAnimating || this.getVisibility() == View.VISIBLE || TextUtils.isEmpty(userId)) {
			return;
		}
		
		this.setVisibility(View.VISIBLE);
		this.startAnimation(aaIn);
		GestureSlidingLayout.setScrollLock(true);
		downloadUserInfo(userId);
	}
	
	public void hide(final OnAfterHideListener onAfterHideListener) {

		if(isAnimating || this.getVisibility() != View.VISIBLE) {
			return;
		}
		
		AlphaAnimation aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(200);
		aaOut.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				isAnimating = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				isAnimating = false;
				GestureSlidingLayout.setScrollLock(false);
				setVisibility(View.INVISIBLE);
				
				if(onAfterHideListener != null) {
					onAfterHideListener.onAfterHide();
				}
				
				clear();
			}
		});
		
		this.startAnimation(aaOut);
	}

	public void downloadUserInfo(String userId) {

		try {
			ApplicationManager.getInstance().getMainActivity().showLoadingView();
			String url = ZoneConstants.BASE_URL + "member/info" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&mystory_member_id=" + URLEncoder.encode(userId, "UTF-8") +
					"&image_size=400";
			String key = ApplicationManager.getDownloadKeyFromTopFragment();
			
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					ToastUtils.showToast(R.string.failToLoadUserInfo);
					ApplicationManager.getInstance().getMainActivity().hideLoadingView();
				}
				
				@Override
				public void onCompleted(String url, String result) {
					
					ApplicationManager.getInstance().getMainActivity().hideLoadingView();
					
					try {
						JSONObject objResult = new JSONObject(result);
						
						if(objResult.has("result")) {
							JSONArray arJSON = objResult.getJSONArray("result");
					
							myStoryInfo = new MyStoryInfo(arJSON.getJSONObject(0));
							setInfo();
						}
					} catch(Exception e) {
						e.printStackTrace();
						ToastUtils.showToast(R.string.failToLoadUserInfo);
					}
				}
			};
			AsyncStringDownloader.download(url, key, ocl);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setInfo() {
		try {
			if(myStoryInfo.getStatus() == -1 || myStoryInfo.getStatus() == -9) {
				ToastUtils.showToast(R.string.withdrawnMember);
				myStoryInfo = null;
				return;
			}
			
			tvNickname.setText(myStoryInfo.getMystory_member_nickname());
			tvGenderWithAge.setText(myStoryInfo.getMember_gender() + "/" + myStoryInfo.getMember_age());
			
			if(!TextUtils.isEmpty(myStoryInfo.getMystory_member_profile())) {
				String key = ApplicationManager.getDownloadKeyFromTopFragment();
				ImageDownloadUtils.downloadImageImmediately(myStoryInfo.getMystory_member_profile(), key, ivImage, 400, true);
			}
			
			if(!TextUtils.isEmpty(myStoryInfo.getIs_friend())
					&& myStoryInfo.getIs_friend().equals("true")) {
				friend.setBackgroundResource(R.drawable.img_profile_people_delete);
			} else {
				friend.setBackgroundResource(R.drawable.img_profile_people_add);
			}
		} catch(Exception e) {
			e.printStackTrace();
			ToastUtils.showToast(R.string.failToLoadUserInfo);
		}
	}
	
	private void clear() {

		this.setVisibility(View.INVISIBLE);
		
		myStoryInfo = null;
		tvNickname.setText("");
        
		Drawable d = ivImage.getDrawable();
        ivImage.setImageDrawable(null);
        ivImage.setImageBitmap(null);

        if (d != null) {
            d.setCallback(null);
        }
         
        if (d instanceof BitmapDrawable) {
            Bitmap bm = ((BitmapDrawable)d).getBitmap();
            
            if(bm != null && !bm.isRecycled()) {
            	bm.recycle();
            }
        }
        
        ivImage.setVisibility(View.INVISIBLE);
	}
	
	private void addButtons(LinearLayout targetLinear, int l, int mod) {
		
		home = new View(getContext());
		home.setLayoutParams(new LinearLayout.LayoutParams(l, l));
		home.setBackgroundColor(Color.rgb(100, 100, 100));
		home.setBackgroundResource(R.drawable.img_profile_home);
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(myStoryInfo != null && !TextUtils.isEmpty(myStoryInfo.getMystory_member_id())) {
					hide(new OnAfterHideListener() {
						
						@Override
						public void onAfterHide() {
							if(myStoryInfo != null && !TextUtils.isEmpty(myStoryInfo.getMystory_member_id())) {
								String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/userhome?member_id=" 
										+ myStoryInfo.getMystory_member_id();
								IntentHandlerActivity.actionByUri(Uri.parse(uriString));
							}
						}
					});	
				}
			}
		});
		targetLinear.addView(home);
		
		friend = new View(getContext());
		friend.setLayoutParams(new LinearLayout.LayoutParams(l, l));
		friend.setBackgroundResource(R.drawable.img_profile_people_add);
		friend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				try {
					if(myStoryInfo != null && !TextUtils.isEmpty(myStoryInfo.getMystory_member_id())) {
						
						if(myStoryInfo.getMystory_member_id().equals(MainActivity.myInfo.getMember_id())) {
							ToastUtils.showToast(R.string.addMe);
							return;
						}
						
						if(myStoryInfo.getIs_friend() != null
								&& myStoryInfo.getIs_friend().equals("true")) {
							ToastUtils.showToast(R.string.deletedFromFriendList);
							friend.setBackgroundResource(R.drawable.img_profile_people_add);
							myStoryInfo.setIs_friend("false");
							deleteFriend();
						} else {
							ToastUtils.showToast(R.string.addedToFriendList);
							friend.setBackgroundResource(R.drawable.img_profile_people_delete);
							myStoryInfo.setIs_friend("true");
							addFriend();
						}
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		targetLinear.addView(friend);

		message = new View(getContext());
		message.setLayoutParams(new LinearLayout.LayoutParams(l, l));
		message.setBackgroundResource(R.drawable.img_profile_message);
		message.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				try {
					if(myStoryInfo != null && !TextUtils.isEmpty(myStoryInfo.getMystory_member_id())) {
						
						if(myStoryInfo.getMystory_member_id().equals(MainActivity.myInfo.getMember_id())) {
							ToastUtils.showToast(R.string.withMe);
							return;
						}
						
						hide(new OnAfterHideListener() {
							
							@Override
							public void onAfterHide() {
								if(myStoryInfo != null && !TextUtils.isEmpty(myStoryInfo.getMystory_member_id())) {
									
									String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/message" +
											"?member_id=" + myStoryInfo.getMystory_member_id();  
									
									IntentHandlerActivity.actionByUri(Uri.parse(uriString));
								}
							}
						});
					}
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
		targetLinear.addView(message);
		
		close = new View(getContext());
		close.setLayoutParams(new LinearLayout.LayoutParams(0, l, 1));
		close.setBackgroundResource(R.drawable.img_profile_close);
		close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hide(null);
			}
		});
		targetLinear.addView(close);
	}
	
	public void addFriend() {
		
		try {
			String url = ZoneConstants.BASE_URL + "member/friendPlus" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&friend_member_id=" + URLEncoder.encode(myStoryInfo.getMystory_member_id(), "UTF-8") +
					"&status=1";
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
				}
				
				@Override
				public void onCompleted(String url, String result) {
				}
			};
			AsyncStringDownloader.download(url, null, ocl);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteFriend() {
		
		try {
			String url = ZoneConstants.BASE_URL + "member/friendPlus" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&friend_member_id=" + URLEncoder.encode(myStoryInfo.getMystory_member_id(), "UTF-8") +
					"&status=-1";
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
				}
				
				@Override
				public void onCompleted(String url, String result) {
				}
			};
			AsyncStringDownloader.download(url, null, ocl);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
////////////////// Interfaces.
	
	public interface OnAfterHideListener {
		
		public void onAfterHide();
	}
}

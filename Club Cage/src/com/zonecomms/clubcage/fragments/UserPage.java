package com.zonecomms.clubcage.fragments;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleSpinnerPopup;
import com.outspoken_kid.views.holo.holo_light.HoloStyleSpinnerPopup.OnItemClickedListener;
import com.zonecomms.clubcage.IntentHandlerActivity;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.clubcage.classes.ZonecommsApplication;
import com.zonecomms.clubcage.classes.ZonecommsFragment;
import com.zonecomms.common.adapters.GridAdapter;
import com.zonecomms.common.adapters.ListAdapter;
import com.zonecomms.common.models.MessageSample;
import com.zonecomms.common.models.MyStoryInfo;
import com.zonecomms.common.models.Post;
import com.zonecomms.common.models.UploadImageInfo;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.utils.ImageUploadUtils.OnAfterUploadImage;

public class UserPage extends ZonecommsFragment {

	private static final int ANIM_DURATION = 300;

	private FrameLayout mainLayout;
	private SwipeRefreshLayout swipeRefreshLayoutForGrid;
	private SwipeRefreshLayout swipeRefreshLayoutForList;
	private GridView gridView;
	private ListView listView;
	private RelativeLayout relative;
	private ProgressBar progress;
	private FrameLayout imageFrame;
	private ImageView ivImage;
	private TextView tvNickname;
	private TextView tvIdGenderAge;
	private TextView tvIntroduce;
	private TextView tvPostCount;
	private TextView tvFriendCount;
	private FrameLayout contentFrame;
	private ScrollView profileScroll;
	private TextView tvStatus;
	private TextView tvInterested;
	private TextView tvJob;
	private TextView tvCompany;
	private TextView tvLiveLocation;
	private TextView tvActiveLocation;
	private HoloStyleSpinnerPopup pPhoto;

	private MyStoryInfo myStoryInfo;
	
	private int mode;
	private String userId;
	private boolean isAnimating;
	private boolean isInfoHidden;
	private boolean isUploadingProfileImage;
	public boolean isLastList;
	public String url;
	
	public ArrayList<BaseModel> models = new ArrayList<BaseModel>();
	private ArrayList<BaseModel> modelsForList = new ArrayList<BaseModel>();
	private ArrayList<BaseModel> modelsForGrid = new ArrayList<BaseModel>();
	
	public BaseAdapter targetAdapter;
	private GridAdapter gridAdapter;
	private ListAdapter listAdapter;
	
	private int lastIndexno;
	private int numOfColumn = 2;
	private boolean isDrag;
	
	private View[] bgMenus = new View[4];
	
	@Override
	public void bindViews() {
		
		mainLayout = (FrameLayout) mThisView.findViewById(R.id.userPage_mainLayout);
	}

	@Override
	public void setVariables() {

		LogUtils.log("###UserPage.setVariables.  menuIndex :  " + mode);
		
		if(getArguments() != null) {
			
			if(getArguments().containsKey("userId")) {
				userId = getArguments().getString("userId");
			}

			if(getArguments().containsKey("menuIndex")) {
				try {
					mode = getArguments().getInt("menuIndex");
					
					if(mode < 0) {
						mode = 0;
					} else if(mode > 3) {
						mode = 3;
					}
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			}
		}
	}

	@Override
	public void createPage() {

		relative = new RelativeLayout(mContext);
		relative.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mainLayout.addView(relative);
		
		RelativeLayout.LayoutParams rp;
		
		//Box 'l'ength.
		int l = ResizeUtils.getSpecificLength(150);
		
		//Box 's'pace.
		int s = ResizeUtils.getSpecificLength(8);
		
		madeCount += 27;

		//id : 0
		imageFrame = new FrameLayout(mContext);
		rp = new RelativeLayout.LayoutParams(l*2 + s, l*2 + s);
		rp.topMargin = s;
		rp.leftMargin = s;
		rp.rightMargin = s;
		rp.bottomMargin = s;
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		imageFrame.setId(madeCount);
		imageFrame.setLayoutParams(rp);
		imageFrame.setBackgroundResource(R.drawable.bg_profile_308);
		relative.addView(imageFrame);

		progress = new ProgressBar(mContext);
		ResizeUtils.viewResize(60, 60, progress, 2, Gravity.CENTER, null);
		imageFrame.addView(progress);
		
		ivImage = new ImageView(mContext);
		ivImage.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
															LayoutParams.MATCH_PARENT));
		ivImage.setScaleType(ScaleType.CENTER_CROP);
		imageFrame.addView(ivImage);

		//id : 1
		View bgForBaseProfile = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l*2 + s, l/2);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount);
		bgForBaseProfile.setLayoutParams(rp);
		bgForBaseProfile.setId(madeCount + 1);
		bgForBaseProfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(myStoryInfo == null) {
					return;
				}
				
				try {
					if(ZonecommsApplication.myInfo.getMember_id().equals(myStoryInfo.getMystory_member_id())) {
						String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/baseprofile";
						IntentHandlerActivity.actionByUri(Uri.parse(uriString));
					}
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			}
		});
		relative.addView(bgForBaseProfile);
		
		tvNickname = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, l/2);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 1);
		tvNickname.setLayoutParams(rp);
		tvNickname.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		tvNickname.setTextColor(Color.parseColor(getString(R.color.renewal_text)));
		tvNickname.setGravity(Gravity.CENTER);
		tvNickname.setMaxWidth(l);
		tvNickname.setEllipsize(TruncateAt.END);
		FontUtils.setFontSize(tvNickname, 26);
		relative.addView(tvNickname);
		
		tvIdGenderAge = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, l/2);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_RIGHT, madeCount + 1);
		tvIdGenderAge.setLayoutParams(rp);
		tvIdGenderAge.setPadding(0, 0, ResizeUtils.getSpecificLength(20), 0);
		tvIdGenderAge.setTextColor(Color.parseColor(getString(R.color.renewal_hint)));
		tvIdGenderAge.setGravity(Gravity.CENTER);
		tvIdGenderAge.setMaxWidth(l);
		tvIdGenderAge.setEllipsize(TruncateAt.END);
		FontUtils.setFontSize(tvIdGenderAge, 18);
		relative.addView(tvIdGenderAge);
		
		tvIntroduce = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, l/2);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 1);
		rp.addRule(RelativeLayout.BELOW, madeCount + 1);
		rp.rightMargin = s;
		tvIntroduce.setLayoutParams(rp);
		tvIntroduce.setPadding(ResizeUtils.getSpecificLength(20), 0, 
				ResizeUtils.getSpecificLength(20), 0);
		tvIntroduce.setTextColor(Color.parseColor(getString(R.color.renewal_text)));
		tvIntroduce.setMaxLines(2);
		tvIntroduce.setEllipsize(TruncateAt.END);
		tvIntroduce.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		FontUtils.setFontSize(tvIntroduce, 18);
		relative.addView(tvIntroduce);
		
		//id : 2
		tvPostCount = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount);
		rp.rightMargin = s;
		tvPostCount.setLayoutParams(rp);
		tvPostCount.setPadding(0, ResizeUtils.getSpecificLength(40), 0, 0);
		tvPostCount.setId(madeCount + 2);
		tvPostCount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//Napp -> userhome -> story.
				mainActivity.checkNApp("napp://android.zonecomms.com/userhome?member_id=" + userId + "&menuindex=1");
			}
		});
		tvPostCount.setBackgroundResource(R.drawable.bg_home_post);
		tvPostCount.setTextColor(Color.WHITE);
		tvPostCount.setGravity(Gravity.CENTER);
		FontUtils.setFontSize(tvPostCount, 36);
		FontUtils.setFontStyle(tvPostCount, FontUtils.BOLD);
		relative.addView(tvPostCount);
		
		tvFriendCount = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount + 2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 2);
		tvFriendCount.setLayoutParams(rp);
		tvFriendCount.setPadding(0, ResizeUtils.getSpecificLength(40), 0, 0);
		tvFriendCount.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(ZonecommsApplication.myInfo.getMember_id().equals(myStoryInfo.getMystory_member_id())) {
					String uriString = "napp://android.zonecomms.com/friend?userId=" + 
											myStoryInfo.getMystory_member_id();
					mainActivity.checkNApp(uriString);
				}
			}
		});
		tvFriendCount.setBackgroundResource(R.drawable.bg_home_friend);
		tvFriendCount.setTextColor(Color.WHITE);
		tvFriendCount.setGravity(Gravity.CENTER);
		FontUtils.setFontSize(tvFriendCount, 36);
		FontUtils.setFontStyle(tvFriendCount, FontUtils.BOLD);
		relative.addView(tvFriendCount);

		for(int i=0; i<4; i++) {
			
			final int I = i;
			int resId = 0;
			
			Button bgMenu = new Button(mContext);
			rp = new RelativeLayout.LayoutParams(l, l);
			
			switch(i) {
			
			case 0:
				resId = R.string.menu1;
				rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount);
				rp.addRule(RelativeLayout.BELOW, madeCount);
				break;
				
			case 1:
				resId = R.string.menu2;
				rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 3);
				rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 3);
				break;
				
			case 2:
				resId = R.string.menu3;
				rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 4);
				rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 4);
				break;
				
			case 3:
				resId = R.string.menu4;
				rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 5);
				rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 5);
				break;
			}
			
			rp.rightMargin = s;
			bgMenu.setId(madeCount + 3 + i);
			bgMenu.setText(resId);
			bgMenu.setLayoutParams(rp);
			bgMenu.setPadding(0, 0, 0, 0);
			
			if(i == mode) {
				bgMenu.setBackgroundColor(Color.rgb(119, 119, 119));
			} else {
				bgMenu.setBackgroundColor(Color.rgb(217, 217, 217));
			}
			
			bgMenu.setTextColor(Color.WHITE);
			bgMenu.setGravity(Gravity.CENTER);
			FontUtils.setFontSize(bgMenu, 28);
			FontUtils.setFontStyle(bgMenu, FontUtils.BOLD);
			bgMenu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					if(I == 3 && myStoryInfo != null && !StringUtils.isEmpty(myStoryInfo.getMystory_member_id()) 
							&& !myStoryInfo.getMystory_member_id().equals(ZonecommsApplication.myInfo.getMember_id())) {
						String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/message" +
								"?member_id=" + myStoryInfo.getMystory_member_id();  
						IntentHandlerActivity.actionByUri(Uri.parse(uriString));
					} else {
						setMode(I);
					}
				}
			});
			relative.addView(bgMenu);
			bgMenus[i] = bgMenu;
		}
		
		contentFrame = new FrameLayout(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, contentFrame, 2, 
				Gravity.TOP, new int[]{0, 482, 0, 0});
		mainLayout.addView(contentFrame);
		
		addProfileScroll();
		addGridView();
		addListView();
		
		pPhoto = new HoloStyleSpinnerPopup(mContext);
		pPhoto.setTitle(getString(R.string.uploadPhoto));
		pPhoto.addItem(getString(R.string.photo_take));
		pPhoto.addItem(getString(R.string.photo_album));
		pPhoto.notifyDataSetChanged();
		pPhoto.setOnItemClickedListener(new OnItemClickedListener() {
			
			@Override
			public void onItemClicked(int position, String itemString) {

				String filePath = null;
				String fileName = null;
				int requestCode = 0;
				Intent intent = new Intent();
				
				if(StringUtils.isEmpty(itemString)) {
					return;
				} else if(itemString.equals(getString(R.string.photo_take))){
					intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

				    File fileDerectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				    filePath = fileDerectory.getAbsolutePath();
				    fileName = System.currentTimeMillis() + ".jpg";
				    File file = new File(fileDerectory, fileName);
				    
				    if(!fileDerectory.exists()) {
				    	fileDerectory.mkdirs();
				    }
				    
				    Uri uri = Uri.fromFile(file);
				    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
				    requestCode = ZoneConstants.REQUEST_CAMERA;
				    
				} else if(itemString.equals(getString(R.string.photo_album))){
//					intent.setAction(Intent.ACTION_GET_CONTENT);
					intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					requestCode = ZoneConstants.REQUEST_GALLERY;
				}
				
				OnAfterUploadImage oaui = new OnAfterUploadImage() {
					
					@Override
					public void onAfterUploadImage(UploadImageInfo uploadImageInfo,
							Bitmap thumbnail) {

						isUploadingProfileImage = false;
						
						if(uploadImageInfo != null) {
							try {
								String url = ZoneConstants.BASE_URL + "member/update/profileimg" +
										"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
										"&profile_image=" + uploadImageInfo.getImageUrl() +
										"&img_width=" + uploadImageInfo.getImageWidth() +
										"&img_height=" + + uploadImageInfo.getImageHeight() +
										"&image_size=308";
								
								DownloadUtils.downloadJSONString(url,
										new OnJSONDownloadListener() {

											@Override
											public void onError(String url) {
												
												ToastUtils.showToast(R.string.failToLoadBitmap);
											}

											@Override
											public void onCompleted(String url,
													JSONObject objJSON) {

												try {
													LogUtils.log("UserPage.onCompleted."
															+ "\nurl : "
															+ url
															+ "\nresult : "
															+ objJSON);

													if(objJSON.getInt("errorCode") == 1) {
														isDownloading = false;
														isLastList = false;
														isRefreshing = false;
														loadProfile();
													} else {
														ToastUtils.showToast(R.string.failToLoadBitmap);
													}													
												} catch (Exception e) {
													LogUtils.trace(e);
													ToastUtils.showToast(R.string.failToLoadBitmap);
												} catch (OutOfMemoryError oom) {
													LogUtils.trace(oom);
													ToastUtils.showToast(R.string.failToLoadBitmap);
												}
											}
										});
								
							} catch(Exception e) {
								LogUtils.trace(e);
								ToastUtils.showToast(R.string.failToLoadBitmap);
							}
						}
					}
				};
				
				isUploadingProfileImage = true;
				mainActivity.imageUploadSetting(filePath, fileName, true, oaui);
				mainActivity.startActivityForResult(intent, requestCode);
			}
		});
		mainLayout.addView(pPhoto);
	}

	@Override
	public void setListeners() {
	}

	@Override
	public void setSizes() {
	}

	@Override
	public void downloadInfo() {

		if(mode == 1 || mode == 2) {
			loadPosts();
		}
	}

	@Override
	public void setPage(boolean successDownload) {

		LogUtils.log("###where.setPage.  hideLoadingView");
		mainActivity.hideLoadingView();
		isRefreshing = false;
		isDownloading = false;
		
		contentFrame.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				if(mode == 1 || mode == 2) {
					swipeRefreshLayoutForGrid.setRefreshing(false);
				} else if(mode == 3) {
					swipeRefreshLayoutForList.setRefreshing(false);
				}
			}
		}, 1000);

		if(successDownload) {
			
			if(models != null && models.size() > 0) {
				lastIndexno = models.get(models.size() - 1).getIndexno();
			}
			
			if(targetAdapter != null) {
				targetAdapter.notifyDataSetChanged();
			}
		} else {
			ToastUtils.showToast(R.string.failToLoadList);
		}
	}
	
	@Override
	public String getTitleText() {
		
		if(StringUtils.isEmpty(title)) {
			title = "Loading..";
		}
		
		return title;
	}

	@Override
	public int getContentViewId() {

		return R.layout.page_user;
	}
	
	@Override
	public void hideLoadingView() {

		mainActivity.hideLoadingView();
	}

	@Override
	public void showLoadingView() {

		mainActivity.showLoadingView();
	}
	
	@Override
	public boolean onBackPressed() {
		
		if(pPhoto.getVisibility() == View.VISIBLE) {
			pPhoto.hidePopup();
		} else {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refreshPage() {

		if(isRefreshing) {
			return;
		}

		isRefreshing = true;
		isLastList = false;
		lastIndexno = 0;
		models.clear();
		targetAdapter.notifyDataSetChanged();

		if(targetAdapter instanceof GridAdapter) {
			((GridAdapter)targetAdapter).clearHardCache();
		} else if(targetAdapter instanceof ListAdapter) {
			((ListAdapter)targetAdapter).clearHardCache();
		}
		
		switch(mode) {
		case 0:
			loadProfile();
			break;
		case 1:
			loadPosts();
			break;
		case 2:
			loadPosts();
			break;
		case 3:
			loadMessageSample();
			break;
		default:
			return;
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();

		LogUtils.log("###USerPage.onResume.  mode : " + mode);
		
		mainActivity.showTitleBar();
		mainActivity.getTitleBar().hideCircleButton();
		mainActivity.getTitleBar().showHomeButton();
		mainActivity.getTitleBar().hideWriteButton();
		
		if(mainActivity.getSponserBanner() != null) {
			mainActivity.getSponserBanner().hideBanner();
		}
	
		if(mode == 0) {
			setMode(mode);
		} else {
			loadProfile();
		}
	}
	
////////////////////////// Custom methods.	

	public void addProfileScroll() {
		
		profileScroll = new ScrollView(mContext);
		profileScroll.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
		profileScroll.setFillViewport(true);
		profileScroll.setVisibility(mode==0?View.VISIBLE:View.INVISIBLE);
		contentFrame.addView(profileScroll);
		
		RelativeLayout relative = new RelativeLayout(mContext);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, relative, 
				2, Gravity.CENTER_HORIZONTAL, null);
		profileScroll.addView(relative);

		madeCount += 10;
		RelativeLayout.LayoutParams rp;
		int l = ResizeUtils.getSpecificLength(150);
		int p = ResizeUtils.getSpecificLength(8);
		int width = l*2 + p;
		int height = l;
		
		//현재 상태		id : 0
		tvStatus = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(width, height);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.rightMargin = p;
		rp.bottomMargin = p;
		tvStatus.setLayoutParams(rp);
		tvStatus.setBackgroundResource(R.drawable.bg_home_status);
		tvStatus.setPadding(p, p, p, p);
		tvStatus.setSingleLine();
		tvStatus.setEllipsize(TruncateAt.END);
		tvStatus.setGravity(Gravity.CENTER);
		tvStatus.setTextColor(Color.WHITE);
		tvStatus.setId(madeCount);
		FontUtils.setFontSize(tvStatus, 30);
		relative.addView(tvStatus);
		
		//관심사
		tvInterested = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(width, height);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount);
		tvInterested.setLayoutParams(rp);
		tvInterested.setBackgroundResource(R.drawable.bg_home_interested);
		tvInterested.setPadding(p, p, p, p);
		tvInterested.setSingleLine();
		tvInterested.setEllipsize(TruncateAt.END);
		tvInterested.setGravity(Gravity.CENTER);
		tvInterested.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvInterested, 30);
		relative.addView(tvInterested);
		
		//직업			id : 1
		tvJob = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(width, height);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.BELOW, madeCount);
		rp.rightMargin = p;
		rp.bottomMargin = p;
		tvJob.setLayoutParams(rp);
		tvJob.setBackgroundResource(R.drawable.bg_home_job);
		tvJob.setPadding(p, p, p, p);
		tvJob.setSingleLine();
		tvJob.setEllipsize(TruncateAt.END);
		tvJob.setGravity(Gravity.CENTER);
		tvJob.setTextColor(Color.WHITE);
		tvJob.setId(madeCount + 1);
		FontUtils.setFontSize(tvJob, 30);
		relative.addView(tvJob);
		
		//직장/학교
		tvCompany = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(width, height);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 1);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 1);
		tvCompany.setLayoutParams(rp);
		tvCompany.setBackgroundResource(R.drawable.bg_home_company);
		tvCompany.setPadding(p, p, p, p);
		tvCompany.setSingleLine();
		tvCompany.setEllipsize(TruncateAt.END);
		tvCompany.setGravity(Gravity.CENTER);
		tvCompany.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvCompany, 30);
		relative.addView(tvCompany);
		
		//사는 곳			id : 2
		tvLiveLocation = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(width, height);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.BELOW, madeCount + 1);
		rp.rightMargin = p;
		tvLiveLocation.setLayoutParams(rp);
		tvLiveLocation.setBackgroundResource(R.drawable.bg_home_livelocation);
		tvLiveLocation.setPadding(p, p, p, p);
		tvLiveLocation.setSingleLine();
		tvLiveLocation.setEllipsize(TruncateAt.END);
		tvLiveLocation.setGravity(Gravity.CENTER);
		tvLiveLocation.setTextColor(Color.WHITE);
		tvLiveLocation.setId(madeCount + 2);
		FontUtils.setFontSize(tvLiveLocation, 30);
		relative.addView(tvLiveLocation);
		
		//활동 지역
		tvActiveLocation = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(width, height);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 2);
		tvActiveLocation.setLayoutParams(rp);
		tvActiveLocation.setBackgroundResource(R.drawable.bg_home_activelocation);
		tvActiveLocation.setPadding(p, p, p, p);
		tvActiveLocation.setSingleLine();
		tvActiveLocation.setEllipsize(TruncateAt.END);
		tvActiveLocation.setGravity(Gravity.CENTER);
		tvActiveLocation.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvActiveLocation, 30);
		relative.addView(tvActiveLocation);
		
		View bottomBlank = new View(mContext);
		rp = new RelativeLayout.LayoutParams(1, p);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.BELOW, madeCount + 2);
		bottomBlank.setLayoutParams(rp);
		relative.addView(bottomBlank);
	}
	
	public void addGridView() {
		
		swipeRefreshLayoutForGrid = new SwipeRefreshLayout(mContext);
		
		gridAdapter = new GridAdapter(mContext, modelsForGrid, true);
		gridView = new GridView(mContext);
		gridView.setLayoutParams(new SwipeRefreshLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		gridView.setAdapter(gridAdapter);
		gridView.setBackgroundColor(Color.parseColor("#f5f5f5"));
		
		gridView.setNumColumns(numOfColumn);
		gridView.setPadding(0, 0, 0, 0);
		gridView.setSelector(R.drawable.list_selector);
		gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
				if(scrollState == 0) {
					if(gridView.getFirstVisiblePosition() == 0) {
						showInformation();
					} else {
						hideInformation();
					}

					isDrag = false;
					
				} else if(scrollState == 1) {
					isDrag = true;
					
				} else if(scrollState == 2) {
					isDrag = false;
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(visibleItemCount < totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}

				if(isDrag) {
					return;
				}
				
				if(firstVisibleItem == 0 && gridView.getChildCount() != 0 
						&& gridView.getChildAt(0).getTop() <= 10) {
					showInformation();
				} else if(firstVisibleItem == numOfColumn) {
					hideInformation();
				}
			}
		});
		swipeRefreshLayoutForGrid.addView(gridView);
		
		swipeRefreshLayoutForGrid.setVisibility(mode == 1 || mode == 2 ? View.VISIBLE : View.INVISIBLE);
		swipeRefreshLayoutForGrid.setColorSchemeColors(
        		Color.argb(255, 255, 102, 153), 
        		Color.argb(255, 255, 153, 153), 
        		Color.argb(255, 255, 204, 153), 
        		Color.argb(255, 255, 255, 153));
		swipeRefreshLayoutForGrid.setEnabled(true);
		swipeRefreshLayoutForGrid.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				
				swipeRefreshLayoutForGrid.setRefreshing(true);
				refreshPage();
			}
		});
		contentFrame.addView(swipeRefreshLayoutForGrid);
	}
	
	public void addListView() {

		swipeRefreshLayoutForList = new SwipeRefreshLayout(mContext);
		
		listAdapter = new ListAdapter(mContext, modelsForList, true);
		listView = new ListView(mContext);
		listView.setLayoutParams(new SwipeRefreshLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		listView.setAdapter(listAdapter);
		listView.setBackgroundColor(Color.parseColor("#f5f5f5"));
		listView.setCacheColorHint(Color.argb(0, 0, 0, 0));
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
				LogUtils.log("###where.onScrollStateChanged.  sS ; " + scrollState);
				
				if(scrollState == 0) {
					
					if(listView.getFirstVisiblePosition() == 0) {
						showInformation();
					} else {
						hideInformation();
					}
					
					isDrag = false;
					
				} else if(scrollState == 1) {
					isDrag = true;
					
				} else if(scrollState == 2) {
					isDrag = false;
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(isDrag) {
					return;
				}
				
				if(firstVisibleItem == 0 && listView.getChildCount() != 0 
						&& listView.getChildAt(0).getTop() == 0) {
					showInformation();
				} else if(firstVisibleItem == numOfColumn) {
					hideInformation();
				}
			}
		});
		swipeRefreshLayoutForList.addView(listView);

		swipeRefreshLayoutForList.setVisibility(mode == 3? View.VISIBLE : View.INVISIBLE);
		swipeRefreshLayoutForList.setColorSchemeColors(
        		Color.argb(255, 255, 102, 153), 
        		Color.argb(255, 255, 153, 153), 
        		Color.argb(255, 255, 204, 153), 
        		Color.argb(255, 255, 255, 153));
		swipeRefreshLayoutForList.setEnabled(true);
		swipeRefreshLayoutForList.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				
				swipeRefreshLayoutForList.setRefreshing(true);
				refreshPage();
			}
		});
		contentFrame.addView(swipeRefreshLayoutForList);
	}
	
	public void setMode(int mode) {
		
		if(isUploadingProfileImage) {
			return;
		}
		
		isRefreshing = false;
		isDownloading = false;
		isLastList = false;
		mainActivity.showLoadingView();
		
		if(targetAdapter != null) {
			lastIndexno = 0;
			models.clear();
			targetAdapter.notifyDataSetChanged();
			
			if(targetAdapter instanceof GridAdapter) {
				((GridAdapter)targetAdapter).clearHardCache();
			}
			
			targetAdapter = null;
		}

		bgMenus[this.mode].setBackgroundColor(Color.rgb(217, 217, 217));
		bgMenus[mode].setBackgroundColor(Color.rgb(119, 119, 119));
		
		this.mode = mode;
		
		switch(mode) {
		case 0:
			targetAdapter = null;
			profileScroll.setVisibility(View.VISIBLE);
			swipeRefreshLayoutForGrid.setVisibility(View.INVISIBLE);
			swipeRefreshLayoutForList.setVisibility(View.INVISIBLE);
			loadProfile();
			break;
		case 1:
		case 2:
			targetAdapter = gridAdapter;
			models = modelsForGrid;
			profileScroll.setVisibility(View.INVISIBLE);
			swipeRefreshLayoutForGrid.setVisibility(View.VISIBLE);
			swipeRefreshLayoutForList.setVisibility(View.INVISIBLE);
			loadPosts();
			break;
		case 3:
			targetAdapter = listAdapter;
			models = modelsForList;
			profileScroll.setVisibility(View.INVISIBLE);
			swipeRefreshLayoutForGrid.setVisibility(View.INVISIBLE);
			swipeRefreshLayoutForList.setVisibility(View.VISIBLE);
			loadMessageSample();  
			break;
		
		default:
			return;
		}
	}
	
	public void loadProfile() {

		try {
			if(isDownloading) {
				return;
			}

			url = ZoneConstants.BASE_URL + "member/info" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&mystory_member_id=" + userId +
					"&image_size=308";
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					setPage(false);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("UserPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						JSONArray arJSON = objJSON.getJSONArray("result");
						
						myStoryInfo = new MyStoryInfo(arJSON.getJSONObject(0));
						setMyStoryInfo();
						setPage(true);
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToLoadUserInfo);
						setPage(false);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToLoadUserInfo);
						setPage(false);
					}
				}
			});
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public void setMyStoryInfo() {

		if(!StringUtils.isEmpty(myStoryInfo.getMystory_member_nickname())) {
			
			if(myStoryInfo.getMystory_member_nickname().equals(ZonecommsApplication.myInfo.getMember_nickname())) {
				title = "MY HOME";
			} else {
				title = myStoryInfo.getMystory_member_nickname();
			}
			
			mainActivity.getTitleBar().setTitleText(title);
			
			tvNickname.setText(myStoryInfo.getMystory_member_nickname());
		}

		String idGenderAgeString = "";
		
		if(!StringUtils.isEmpty(myStoryInfo.getMystory_member_id())) {
			idGenderAgeString += myStoryInfo.getMystory_member_id();
			
			if(myStoryInfo.getMystory_member_id().equals(ZonecommsApplication.myInfo.getMember_id())) {
				
				ivImage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						pPhoto.showPopup();
					}
				});
				
				OnClickListener ocl = new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/addedprofile";
						IntentHandlerActivity.actionByUri(Uri.parse(uriString));
					}
				};
				
				tvStatus.setTag("" + 0);
				tvInterested.setTag("" + 1);
				tvJob.setTag("" + 2);
				tvCompany.setTag("" + 3);
				tvLiveLocation.setTag("" + 4);
				tvActiveLocation.setTag("" + 5);
				
				tvStatus.setOnClickListener(ocl);
				tvInterested.setOnClickListener(ocl);
				tvJob.setOnClickListener(ocl);
				tvCompany.setOnClickListener(ocl);
				tvLiveLocation.setOnClickListener(ocl);
				tvActiveLocation.setOnClickListener(ocl);
			} else {
				imageFrame.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mainActivity.showImageViewerActivity(myStoryInfo.getMystory_member_nickname(), 
								new String[] {myStoryInfo.getMystory_member_profile()}, null, 0);
					}
				});
				
				tvStatus.setOnClickListener(null);
				tvInterested.setOnClickListener(null);
				tvJob.setOnClickListener(null);
				tvCompany.setOnClickListener(null);
				tvLiveLocation.setOnClickListener(null);
				tvActiveLocation.setOnClickListener(null);
			}
		}

		idGenderAgeString += "\n" + myStoryInfo.getMember_gender() +
				" / " + myStoryInfo.getMember_age();
		tvIdGenderAge.setText(idGenderAgeString);

		if(!StringUtils.isEmpty(myStoryInfo.getMystory_title())) {
			tvIntroduce.setText(myStoryInfo.getMystory_title());
		} else {
			tvIntroduce.setText(R.string.noContent);
		}
		
		if(!StringUtils.isEmpty(myStoryInfo.getMystory_spot_cnt())) {
			tvPostCount.setText(myStoryInfo.getMystory_spot_cnt());
		}
		
		if(!StringUtils.isEmpty(myStoryInfo.getMystory_friend_plus_cnt())) {
			tvFriendCount.setText(myStoryInfo.getMystory_friend_plus_cnt());
		}
		
		if(!StringUtils.isEmpty(myStoryInfo.getMystory_member_profile())) {
			progress.setVisibility(View.VISIBLE);
			ivImage.setVisibility(View.INVISIBLE);
			
			ivImage.setTag(myStoryInfo.getMystory_member_profile());
			DownloadUtils.downloadBitmap(myStoryInfo.getMystory_member_profile(),
					new OnBitmapDownloadListener() {

						@Override
						public void onError(String url) {
							ivImage.setVisibility(View.VISIBLE);
							progress.setVisibility(View.INVISIBLE);
						}

						@Override
						public void onCompleted(String url, Bitmap bitmap) {

							try {
								LogUtils.log("UserPage.onCompleted." + "\nurl : "
										+ url);

								if (ivImage != null
										&& ivImage.getTag() != null
										&& ivImage.getTag().toString()
												.equals(url)) {
									ivImage.setVisibility(View.VISIBLE);
									progress.setVisibility(View.INVISIBLE);
								}
							} catch (Exception e) {
								LogUtils.trace(e);
							} catch (OutOfMemoryError oom) {
								LogUtils.trace(oom);
							}
						}
					});
		} else {
			ivImage.setVisibility(View.VISIBLE);
			progress.setVisibility(View.INVISIBLE);
		}
		
		if(!StringUtils.isEmpty(myStoryInfo.getMstatus_name())) {
			tvStatus.setText(myStoryInfo.getMstatus_name());
		}
		
		if(!StringUtils.isEmpty(myStoryInfo.getIlike())) {
			tvInterested.setText(myStoryInfo.getIlike());
		} else if(ZonecommsApplication.myInfo.getMember_id().equals(myStoryInfo.getMystory_member_id())){
			tvInterested.setText(R.string.touchHere);
		}
		
		if(!StringUtils.isEmpty(myStoryInfo.getJob())) {
			tvJob.setText(myStoryInfo.getJob());
		} else if(ZonecommsApplication.myInfo.getMember_id().equals(myStoryInfo.getMystory_member_id())){
			tvJob.setText(R.string.touchHere);
		}
		
		if(!StringUtils.isEmpty(myStoryInfo.getJobname())) {
			tvCompany.setText(myStoryInfo.getJobname());
		} else if(ZonecommsApplication.myInfo.getMember_id().equals(myStoryInfo.getMystory_member_id())){
			tvCompany.setText(R.string.touchHere);
		}
		
		if(!StringUtils.isEmpty(myStoryInfo.getAddress())) {
			tvLiveLocation.setText(myStoryInfo.getAddress());
		} else if(ZonecommsApplication.myInfo.getMember_id().equals(myStoryInfo.getMystory_member_id())){
			tvLiveLocation.setText(R.string.touchHere);
		}
		
		if(!StringUtils.isEmpty(myStoryInfo.getPlayground())) {
			tvActiveLocation.setText(myStoryInfo.getPlayground());
		} else if(ZonecommsApplication.myInfo.getMember_id().equals(myStoryInfo.getMystory_member_id())){
			tvActiveLocation.setText(R.string.touchHere);
		}
	}
	
	public void loadPosts() {
		
		if(isDownloading || isLastList) {
			return;
		}
		
		isDownloading = true;
		
		url = ZoneConstants.BASE_URL + "sb/posting_list_by_story" +
				"?member_id=" + userId + 
				"&last_spot_nid=" + lastIndexno +
				"&image_size=308" +
				"&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID);

		if(mode == 1) {
			url += "&story=in";
		} else if(mode == 2) {
			url += "&story=other";
		} else {
			return;
		}

		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {
				
				setPage(false);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("UserPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					JSONArray arJSON = objJSON.getJSONArray("data");
					int length = arJSON.length();
					
					if(length > 0) {
						for(int i=0; i<length; i++) {
							try {
								Post post = new Post(arJSON.getJSONObject(i));
								post.setItemCode(ZoneConstants.ITEM_POST);
								
								if(mode == 2) {
									post.setPostForNApp(true);
								}
								
								models.add(post);
							} catch(Exception e) {
							}
						}
					} else {
						isLastList = true;
						ToastUtils.showToast(R.string.lastPage);
					}
					
					setPage(true);
				} catch (Exception e) {
					LogUtils.trace(e);
					setPage(false);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					setPage(false);
				}
			}
		});
	}
	
	public void loadMessageSample() {

		if(isDownloading) {
			return;
		}
		
		isDownloading = true;
		
		String url = null;
		
		if(userId.equals(ZonecommsApplication.myInfo.getMember_id())) {
			url = ZoneConstants.BASE_URL + "microspot/relationList" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&image_size=308";
					
		} else {
			url = ZoneConstants.BASE_URL + "microspot/message_tap" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&friend_member_id=" + userId +
					"&last_microspot_nid=0" +
					"&image_size=308";
		}

		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {
				
				setPage(false);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("UserPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					JSONArray arJSON = objJSON.getJSONArray("data");
					int length = arJSON.length();
					if(length > 0) {
						for(int i=0; i<length; i++) {
							try {
								MessageSample ms = new MessageSample(arJSON.getJSONObject(i));
								ms.setItemCode(ZoneConstants.ITEM_MESSAGESAMPLE);
								models.add(ms);
							} catch(Exception e) {
							}
						}
					} else {
						isLastList = true;
						ToastUtils.showToast(R.string.lastPage);
						
						if(!userId.equals(ZonecommsApplication.myInfo.getMember_id())) {
							
							String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/message" +
									"?member_id=" + myStoryInfo.getMystory_member_id();  
							IntentHandlerActivity.actionByUri(Uri.parse(uriString));
						}
					}

					setPage(true);
				} catch (Exception e) {
					LogUtils.trace(e);
					setPage(false);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					setPage(false);
				}
			}
		});
	}
	
	public void showInformation() {

		if(!isAnimating && isInfoHidden) {
			isInfoHidden = false;
			isAnimating = true;
			
			swipeRefreshLayoutForGrid.setEnabled(true);
			swipeRefreshLayoutForList.setEnabled(true);

			if(mode == 1 || mode == 2) {
				int scrollOffset = gridView.getScrollX();
				gridView.scrollTo(0, scrollOffset);
			} else if(mode == 3) {
				int scrollOffset = listView.getScrollX();
				listView.scrollTo(0, scrollOffset);
			}
			
			float animOffset = ((float)relative.getMeasuredHeight() - ResizeUtils.getSpecificLength(8))
					/ (float)mainLayout.getMeasuredHeight();
			
			TranslateAnimation taDown = new TranslateAnimation(
					TranslateAnimation.RELATIVE_TO_PARENT, 0,
					TranslateAnimation.RELATIVE_TO_PARENT, 0,
					TranslateAnimation.RELATIVE_TO_PARENT, 0,
					TranslateAnimation.RELATIVE_TO_PARENT, animOffset);
			taDown.setDuration(ANIM_DURATION);
			taDown.setFillAfter(true);
			
			contentFrame.startAnimation(taDown);
			contentFrame.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					isAnimating = false;
					contentFrame.clearAnimation();
					ResizeUtils.setMargin(contentFrame, new int[]{0, 482, 0, 0});
					
					if(mode == 1 || mode == 2) {
						gridView.setSelection(0);
					} else if(mode == 3) {
						listView.setSelection(0);
					}
				}
			}, ANIM_DURATION + 100);
		}
	}
	
	public void hideInformation() {

		if(!isAnimating && !isInfoHidden) {
		
			isInfoHidden = true;
			isAnimating = true;

			swipeRefreshLayoutForGrid.setEnabled(false);
			swipeRefreshLayoutForList.setEnabled(false);
			
			float animOffset = (float)relative.getMeasuredHeight() / (float)mainLayout.getMeasuredHeight();
			
			TranslateAnimation taUp = new TranslateAnimation(
					TranslateAnimation.RELATIVE_TO_PARENT, 0,
					TranslateAnimation.RELATIVE_TO_PARENT, 0,
					TranslateAnimation.RELATIVE_TO_PARENT, animOffset,
					TranslateAnimation.RELATIVE_TO_PARENT, 0);
			taUp.setDuration(ANIM_DURATION);
			
			ResizeUtils.setMargin(contentFrame, new int[]{0, 8, 0, 0});
			
			contentFrame.startAnimation(taUp);
			contentFrame.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					isAnimating = false;

					try {
						if(mode == 1 || mode == 2) {
							int fp = gridView.getFirstVisiblePosition();
							
							if(fp < 2) {
								fp = 2;
							}
							
							gridView.smoothScrollBy(listView.getChildAt(fp).getBottom(), ANIM_DURATION/2);
						} else if(mode == 3) {
							int fp = listView.getFirstVisiblePosition();
							
							if(fp != 0) {
								listView.smoothScrollBy(listView.getChildAt(fp).getBottom(), ANIM_DURATION/2);
							}
						}
					} catch(Exception e) {
						LogUtils.trace(e);
					}
				}
			}, ANIM_DURATION + 200);
		}
	}
}

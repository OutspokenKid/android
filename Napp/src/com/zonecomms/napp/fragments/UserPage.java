package com.zonecomms.napp.fragments;

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
import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup.OnItemClickedListener;
import com.zonecomms.common.adapters.GridAdapter;
import com.zonecomms.common.adapters.ListAdapter;
import com.zonecomms.common.models.MessageSample;
import com.zonecomms.common.models.MyStoryInfo;
import com.zonecomms.common.models.Post;
import com.zonecomms.common.models.UploadImageInfo;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.utils.ImageUploadUtils.OnAfterUploadImage;
import com.zonecomms.napp.IntentHandlerActivity;
import com.zonecomms.napp.MainActivity;
import com.zonecomms.napp.R;
import com.zonecomms.napp.classes.BaseListFragment;
import com.zonecomms.napp.classes.ZoneConstants;

public class UserPage extends BaseListFragment {

	private static final int ANIM_DURATION = 300;

	private MyStoryInfo myStoryInfo;
	
	private int mode;
	private String userId;
	private boolean isAnimating;
	private boolean isInfoHidden;
	private boolean isUploadingProfileImage;
	
	private FrameLayout mainLayout;
	private RelativeLayout relative;
	private ProgressBar progress;
	private ImageView ivImage;
	private TextView tvNickname;
	private TextView tvId;
	private TextView tvGender;
	private TextView tvAge;
	private TextView tvIntroduce;
	private TextView tvPostCount;
	private TextView tvFriendCount;
	private FrameLayout contentFrame;
	private ScrollView profileScroll;
//	private Button btnFacebook;
//	private Button btnKakaoTalk;
//	private Button btnKakaoStory;
//	private Button btnMobileNumber;
	private TextView tvStatus;
	private TextView tvInterested;
	private TextView tvJob;
	private TextView tvCompany;
	private TextView tvLiveLocation;
	private TextView tvActiveLocation;
	private HoloStyleSpinnerPopup pPhoto;

	private PullToRefreshGridView gridView;
	private PullToRefreshListView listView;
	private GridAdapter gridAdapter;
	private ListAdapter listAdapter;
	private int lastIndexno;
	private int numOfColumn = 2;
	
	private ArrayList<BaseModel> modelsForList = new ArrayList<BaseModel>();
	private ArrayList<BaseModel> modelsForGrid = new ArrayList<BaseModel>();
	private View[] bgMenus = new View[4];
	
	@Override
	protected void bindViews() {
		
		mainLayout = (FrameLayout) mThisView.findViewById(R.id.userPage_mainLayout);
	}

	@Override
	protected void setVariables() {

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
	protected void createPage() {

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
		FrameLayout imageFrame = new FrameLayout(mContext);
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
		
		View bgForBaseProfile = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l*2 + s, l/2);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount);
		bgForBaseProfile.setBackgroundColor(Color.rgb(55, 55, 55));
		bgForBaseProfile.setLayoutParams(rp);
		bgForBaseProfile.setId(madeCount + 1);
		relative.addView(bgForBaseProfile);
		
		//id : 1
		tvNickname = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, l/4);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 1);
		tvNickname.setLayoutParams(rp);
		tvNickname.setPadding(s, 0, 0, 0);
		tvNickname.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvNickname, 24);
		relative.addView(tvNickname);
		
		tvId = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, l/4);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 1);
		tvId.setLayoutParams(rp);
		tvId.setPadding(s, 0, 0, 0);
		tvId.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvId, 24);
		relative.addView(tvId);

		tvGender = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, l/4);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_RIGHT, madeCount + 1);
		tvGender.setLayoutParams(rp);
		tvGender.setPadding(0, 0, s, 0);
		tvGender.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvGender, 24);
		relative.addView(tvGender);
		
		tvAge = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, l/4);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_RIGHT, madeCount + 1);
		tvAge.setLayoutParams(rp);
		tvAge.setPadding(0, 0, s, 0);
		tvAge.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvAge, 24);
		relative.addView(tvAge);
		
		tvIntroduce = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, l - ResizeUtils.getSpecificLength(40));
		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 1);
		rp.addRule(RelativeLayout.BELOW, madeCount + 1);
		rp.rightMargin = s;
		tvIntroduce.setLayoutParams(rp);
		tvIntroduce.setPadding(s, 0, s, 0);
		tvIntroduce.setMaxLines(2);
		tvIntroduce.setTextColor(Color.WHITE);
		tvIntroduce.setEllipsize(TruncateAt.END);
		FontInfo.setFontSize(tvIntroduce, 24);
		relative.addView(tvIntroduce);
		
		View coverForBaseProfile = new View(mContext);
		rp = new RelativeLayout.LayoutParams(l*2 + s, l);
		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount);
		coverForBaseProfile.setLayoutParams(rp);
		coverForBaseProfile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(myStoryInfo == null) {
					return;
				}
				
				try {
					if(MainActivity.myInfo.getMember_id().equals(myStoryInfo.getMystory_member_id())) {
						String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/baseprofile";
						IntentHandlerActivity.actionByUri(Uri.parse(uriString));
					}
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			}
		});
		relative.addView(coverForBaseProfile);
		
		//id : 2
		FrameLayout postCountFrame = new FrameLayout(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount);
		rp.rightMargin = s;
		postCountFrame.setLayoutParams(rp);
		postCountFrame.setId(madeCount + 2);
		postCountFrame.setBackgroundColor(Color.rgb(15, 117, 188));
		postCountFrame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				setMode(ZoneConstants.MENU_STORY);
			}
		});
		relative.addView(postCountFrame);
		
		tvPostCount = new TextView(mContext);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvPostCount, 
				2, Gravity.TOP|Gravity.RIGHT, new int[]{0, 10, 10, 0});
		tvPostCount.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvPostCount, 55);
		FontInfo.setFontStyle(tvPostCount, FontInfo.BOLD);
		postCountFrame.addView(tvPostCount);
		
		//'작성글' 기본 텍스트.
		TextView tvPostCount2 = new TextView(mContext);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvPostCount2, 
				2, Gravity.RIGHT|Gravity.BOTTOM, new int[]{0, 0, 10, 10});
		tvPostCount2.setTextColor(Color.WHITE);
		tvPostCount2.setText(R.string.post);
		FontInfo.setFontSize(tvPostCount2, 30);
		FontInfo.setFontStyle(tvPostCount2, FontInfo.BOLD);
		postCountFrame.addView(tvPostCount2);
		
		FrameLayout friendCountFrame = new FrameLayout(mContext);
		rp = new RelativeLayout.LayoutParams(l, l);
		rp.addRule(RelativeLayout.ALIGN_BOTTOM, madeCount + 2);
		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 2);
		friendCountFrame.setLayoutParams(rp);
		friendCountFrame.setBackgroundColor(Color.rgb(57, 181, 74));
		friendCountFrame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/friend"
										+ "?userId=" + userId;
				IntentHandlerActivity.actionByUri(Uri.parse(uriString));
			}
		});
		relative.addView(friendCountFrame);
		
		tvFriendCount = new TextView(mContext);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvFriendCount, 
				2, Gravity.TOP|Gravity.RIGHT, new int[]{0, 10, 10, 0});
		tvFriendCount.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvFriendCount, 55);
		FontInfo.setFontStyle(tvFriendCount, FontInfo.BOLD);
		friendCountFrame.addView(tvFriendCount);

		//'친구' 기본 텍스트.
		TextView tvFriendCount2 = new TextView(mContext);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvFriendCount2, 
				2, Gravity.RIGHT|Gravity.BOTTOM, new int[]{0, 0, 10, 10});
		tvFriendCount2.setText(R.string.friend);
		tvFriendCount2.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvFriendCount2, 30);
		FontInfo.setFontStyle(tvFriendCount2, FontInfo.BOLD);
		friendCountFrame.addView(tvFriendCount2);
		
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
			bgMenu.setBackgroundColor(i==0?Color.BLACK:Color.rgb(55, 55, 55));
			bgMenu.setTextColor(Color.WHITE);
			bgMenu.setGravity(Gravity.CENTER);
			FontInfo.setFontSize(bgMenu, 28);
			FontInfo.setFontStyle(bgMenu, FontInfo.BOLD);
			bgMenu.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					if(I == 3 && myStoryInfo != null && !StringUtils.isEmpty(myStoryInfo.getMystory_member_id()) 
							&& !myStoryInfo.getMystory_member_id().equals(MainActivity.myInfo.getMember_id())) {
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
		
		gridAdapter = new GridAdapter(mContext, modelsForGrid, true);
		gridView = new PullToRefreshGridView(mContext);
		gridView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		gridView.setAdapter(gridAdapter);
		gridView.setBackgroundColor(Color.BLACK);
		gridView.setVisibility(View.INVISIBLE);
		gridView.getRefreshableView().setNumColumns(numOfColumn);
		gridView.getRefreshableView().setPadding(0, 0, 0, 0);
		gridView.getRefreshableView().setSelector(R.drawable.list_selector);
		gridView.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				onRefreshPage();
			}
		});
		gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
				if(scrollState == 0) {
					if(gridView.getRefreshableView().getFirstVisiblePosition() == 0) {
						showInformation();
					} else {
						hideInformation();
					}
					
					gridView.setTouched(false);
				} else if(scrollState == 2) {
					gridView.setTouched(false);
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(visibleItemCount < totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}

				if(gridView.isTouched()) {
					return;
				}
				
				if(firstVisibleItem == 0 && gridView.getRefreshableView().getChildCount() != 0 
						&& gridView.getRefreshableView().getChildAt(0).getTop() <= 10) {
					showInformation();
				} else if(firstVisibleItem == numOfColumn) {
					hideInformation();
				}
			}
		});
		contentFrame.addView(gridView);
		
		listAdapter = new ListAdapter(mContext, modelsForList, true);
		listView = new PullToRefreshListView(mContext);
		listView.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		listView.setAdapter(listAdapter);
		listView.setBackgroundColor(Color.BLACK);
		listView.getRefreshableView().setCacheColorHint(Color.argb(0, 0, 0, 0));
		listView.setVisibility(View.INVISIBLE);
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				onRefreshPage();
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == 0) {
					
					if(listView.getRefreshableView().getFirstVisiblePosition() == 0) {
						showInformation();
					} else {
						hideInformation();
					}
					
					listView.setTouched(false);
				} else if(scrollState == 2) {
					listView.setTouched(false);
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(listView.isTouched()) {
					return;
				}
				
				if(firstVisibleItem == 0 && listView.getRefreshableView().getChildCount() != 0 
						&& listView.getRefreshableView().getChildAt(0).getTop() == 0) {
					showInformation();
				} else if(firstVisibleItem == numOfColumn) {
					hideInformation();
				}
			}
		});
		contentFrame.addView(listView);
		
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
					intent.setAction(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					requestCode = ZoneConstants.REQUEST_GALLERY;
				}
				
				OnAfterUploadImage oaui = new OnAfterUploadImage() {
					
					@Override
					public void onAfterUploadImage(UploadImageInfo uploadImageInfo, Bitmap thumbnail) {

						isUploadingProfileImage = false;
						
						if(uploadImageInfo != null) {
							try {
								String url = ZoneConstants.BASE_URL + "member/update/profileimg" +
										"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
										"&profile_image=" + uploadImageInfo.getImageUrl() +
										"&img_width=" + uploadImageInfo.getImageWidth() +
										"&img_height=" + + uploadImageInfo.getImageHeight() +
										"&image_size=" + ResizeUtils.getSpecificLength(308);
								AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
									
									@Override
									public void onErrorRaised(String url, Exception e) {
										ToastUtils.showToast(R.string.failToLoadBitmap);
									}
									
									@Override
									public void onCompleted(String url, String result) {

										try {
											if((new JSONObject(result)).getInt("errorCode") == 1) {
												isDownloading = false;
												isLastList = false;
												isRefreshing = false;
												loadProfile();
											} else {
												ToastUtils.showToast(R.string.failToLoadBitmap);
											}
										} catch(Exception e) {
											LogUtils.trace(e);
											ToastUtils.showToast(R.string.failToLoadBitmap);
										}
									}
								};
								AsyncStringDownloader.download(url, getDownloadKey(), ocl);
								
							} catch(Exception e) {
								LogUtils.trace(e);
								ToastUtils.showToast(R.string.failToLoadBitmap);
							}
						}
					}
				};
				
				isUploadingProfileImage = true;
				mActivity
					.imageUploadSetting(filePath, fileName, true, oaui);
				mActivity.startActivityForResult(intent, requestCode);
			}
		});
		mainLayout.addView(pPhoto);
	}

	@Override
	protected void setListeners() {
	}

	@Override
	protected void setSizes() {
	}

	@Override
	protected void downloadInfo() {
		
		if(mode == 1 || mode == 2) {
			loadPosts();
		}
		
		super.downloadInfo();
	}

	@Override
	protected void setPage(boolean successDownload) {

		mActivity.hideLoadingView();
		isRefreshing = false;
		isDownloading = false;
		
		contentFrame.postDelayed(new Runnable() {
			
			@Override
			public void run() {

				if(mode == 1 || mode == 2) {
					gridView.onRefreshComplete();
				} else if(mode == 3) {
					listView.onRefreshComplete();
				}
			}
		}, 500);

		if(successDownload) {
			
			if(models != null && models.size() > 0) {
				lastIndexno = models.get(models.size() - 1).getIndexno();
			}
			
			if(zoneAdapter != null) {
				zoneAdapter.notifyDataSetChanged();
			}
		} else {
			ToastUtils.showToast(R.string.failToLoadList);
		}
	}

	@Override
	protected int getContentViewId() {

		return R.id.userPage_mainLayout;
	}

	@Override
	public boolean onBackKeyPressed() {
		
		if(pPhoto.getVisibility() == View.VISIBLE) {
			pPhoto.hidePopup();
		} else {
			return false;
		}
		return true;
	}

	@Override
	public void onRefreshPage() {

		if(isRefreshing) {
			return;
		}

		isRefreshing = true;
		lastIndexno = 0;
		models.clear();
		zoneAdapter.notifyDataSetChanged();

		if(zoneAdapter instanceof GridAdapter) {
			((GridAdapter)zoneAdapter).clearHardCache();
		} else if(zoneAdapter instanceof ListAdapter) {
			((ListAdapter)zoneAdapter).clearHardCache();
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
	public void onHiddenChanged(boolean hidden) {
		
		if(mode != 0) {
			super.onHiddenChanged(hidden);
		}

		if(!hidden) {
			if(mActivity.getSponserBanner() != null) {
				mActivity.getSponserBanner().hideBanner();
			}
			
			mActivity.getTitleBar().showHomeButton();
			
			if(userId != null && userId.equals(MainActivity.myInfo.getMember_id())) {
				mActivity.getTitleBar().showWriteButton();
			} else{
				mActivity.getTitleBar().hideWriteButton();
			}
			
			mActivity.getTitleBar().showPlusAppButton();
			mActivity.getTitleBar().hideThemaButton();
			mActivity.getTitleBar().hideRegionButton();
			
			if(mode == 0) {
				setMode(0);
			} else if(models.size() == 0) {
				
				int _mode = this.mode;
				setMode(0);
				setMode(_mode);
			}
		}
	}
	
	@Override
	public void onSoftKeyboardShown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSoftKeyboardHidden() {
		// TODO Auto-generated method stub
		
	}
	
////////////////////////// Custom methods.	

	public void addProfileScroll() {
		
		profileScroll = new ScrollView(mContext);
		profileScroll.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
		profileScroll.setFillViewport(true);
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
		
//		//Facebook.		id : 3
//		btnFacebook = new Button(mContext);
//		rp = new RelativeLayout.LayoutParams(l, l);
//		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
//		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//		rp.rightMargin = p;
//		rp.bottomMargin = p;
//		btnFacebook.setLayoutParams(rp);
//		btnFacebook.setId(madeCount + 3);
//		btnFacebook.setBackgroundColor(Color.RED);
//		btnFacebook.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				if(!StringUtils.isEmpty(myStoryInfo.getFacebook())) {
//					mActivity.showDeviceBrowser(myStoryInfo.getFacebook());
//				}
//			}
//		});
//		relative.addView(btnFacebook);
//		
//		//Kakao talk.	id : 4
//		btnKakaoTalk = new Button(mContext);
//		rp = new RelativeLayout.LayoutParams(l, l);
//		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 3);
//		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 3);
//		rp.rightMargin = p;
//		rp.bottomMargin = p;
//		btnKakaoTalk.setLayoutParams(rp);
//		btnKakaoTalk.setId(madeCount + 4);
//		btnKakaoTalk.setBackgroundColor(Color.GREEN);
//		btnKakaoTalk.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//
//				if(!StringUtils.isEmpty(myStoryInfo.getKakao())) {
//					mActivity.showAlertDialog(getString(R.string.kakaoTalkId), myStoryInfo.getKakao(), null, false);
//				}
//			}
//		});
//		relative.addView(btnKakaoTalk);
//		
//		//Kakao story.	id : 5
//		btnKakaoStory = new Button(mContext);
//		rp = new RelativeLayout.LayoutParams(l, l);
//		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 4);
//		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 4);
//		rp.rightMargin = p;
//		rp.bottomMargin = p;
//		btnKakaoStory.setLayoutParams(rp);
//		btnKakaoStory.setId(madeCount + 5);
//		btnKakaoStory.setBackgroundColor(Color.BLUE);
//		relative.addView(btnKakaoStory);
//		
//		//Mobile number.
//		btnMobileNumber = new Button(mContext);
//		rp = new RelativeLayout.LayoutParams(l, l);
//		rp.addRule(RelativeLayout.RIGHT_OF, madeCount + 5);
//		rp.addRule(RelativeLayout.ALIGN_TOP, madeCount + 5);
//		btnMobileNumber.setLayoutParams(rp);
//		btnMobileNumber.setBackgroundColor(Color.YELLOW);
//		btnMobileNumber.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				if(!StringUtils.isEmpty(myStoryInfo.getMystory_mobile_no())) {
//					boolean result = IntentUtils.call(mContext, myStoryInfo.getMystory_mobile_no());
//					
//					if(!result) {
//						ToastUtils.showToast(R.string.unusableNumber);
//					}
//				}
//			}
//		});
//		relative.addView(btnMobileNumber);
		
		//현재 상태		id : 0
		tvStatus = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(width, height);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//		rp.addRule(RelativeLayout.ALIGN_LEFT, madeCount + 3);
//		rp.addRule(RelativeLayout.BELOW, madeCount + 3);
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
		FontInfo.setFontSize(tvStatus, 30);
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
		FontInfo.setFontSize(tvInterested, 30);
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
		FontInfo.setFontSize(tvJob, 30);
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
		FontInfo.setFontSize(tvCompany, 30);
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
		FontInfo.setFontSize(tvLiveLocation, 30);
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
		FontInfo.setFontSize(tvActiveLocation, 30);
		relative.addView(tvActiveLocation);
		
		View bottomBlank = new View(mContext);
		rp = new RelativeLayout.LayoutParams(1, p);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.BELOW, madeCount + 2);
		bottomBlank.setLayoutParams(rp);
		relative.addView(bottomBlank);
	}
	
	public void setMode(int mode) {

		if(isUploadingProfileImage) {
			return;
		}
		
		isRefreshing = false;
		isDownloading = false;
		isLastList = false;
		mActivity.showLoadingView();
		
		if(zoneAdapter != null) {
			lastIndexno = 0;
			models.clear();
			zoneAdapter.notifyDataSetChanged();
			
			if(zoneAdapter instanceof GridAdapter) {
				((GridAdapter)zoneAdapter).clearHardCache();
			}
			
			zoneAdapter = null;
		}

		bgMenus[this.mode].setBackgroundColor(Color.rgb(55, 55, 55));
		bgMenus[mode].setBackgroundColor(Color.rgb(0, 0, 0));
		
		this.mode = mode;
		
		switch(mode) {
		case ZoneConstants.MENU_PROFILE:
			zoneAdapter = null;
			profileScroll.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.INVISIBLE);
			listView.setVisibility(View.INVISIBLE);
			loadProfile();
			break;
		case ZoneConstants.MENU_STORY:
		case ZoneConstants.MENU_SCRAP:
			zoneAdapter = gridAdapter;
			models = modelsForGrid;
			profileScroll.setVisibility(View.INVISIBLE);
			gridView.setVisibility(View.VISIBLE);
			listView.setVisibility(View.INVISIBLE);
			loadPosts();
			break;
		case ZoneConstants.MENU_MESSAGE:
			zoneAdapter = listAdapter;
			models = modelsForList;
			profileScroll.setVisibility(View.INVISIBLE);
			gridView.setVisibility(View.INVISIBLE);
			listView.setVisibility(View.VISIBLE);
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
			
			AsyncStringDownloader.OnCompletedListener ocl = new AsyncStringDownloader.OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {

					LogUtils.log("UserPage.loadProfile.onError.  url : " + url);
					setPage(false);
				}
				
				@Override
				public void onCompleted(String url, String result) {
					
					LogUtils.log("UserPage.loadProfile.onCompleted.\nurl : " + url + "\nresult : " + result);
					
					try {
						JSONObject objResult = new JSONObject(result);
						
						JSONArray arJSON = objResult.getJSONArray("result");
						
						myStoryInfo = new MyStoryInfo(arJSON.getJSONObject(0));
						setMyStoryInfo();
						setPage(true);
					} catch(Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToLoadUserInfo);
						setPage(false);
					}
				}
			};
			
			url = ZoneConstants.BASE_URL + "member/info" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&mystory_member_id=" + userId +
					"&image_size=" + ResizeUtils.getSpecificLength(308);
			
			mActivity.showLoadingView();
			AsyncStringDownloader.download(url, getDownloadKey(), ocl);
			
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public void setMyStoryInfo() {

		if(!StringUtils.isEmpty(myStoryInfo.getMystory_member_nickname())) {
			
			if(myStoryInfo.getMystory_member_nickname().equals(MainActivity.myInfo.getMember_nickname())) {
				title = "MY HOME";
			} else {
				title = myStoryInfo.getMystory_member_nickname();
			}
			
			mActivity.setTitleText(title);
			tvNickname.setText(myStoryInfo.getMystory_member_nickname());
		}

		if(!StringUtils.isEmpty(myStoryInfo.getMystory_member_id())) {
			tvId.setText("(" + myStoryInfo.getMystory_member_id() + ")");
			
			if(myStoryInfo.getMystory_member_id().equals(MainActivity.myInfo.getMember_id())) {
				
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
				ivImage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mActivity
							.showImageViewerActivity(myStoryInfo.getMystory_member_nickname(), 
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

		tvGender.setText(myStoryInfo.getMember_gender());
		tvAge.setText("" + myStoryInfo.getMember_age());

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
			
			BitmapDownloader.OnCompletedListener ocl = new BitmapDownloader.OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
				}
				
				@Override
				public void onCompleted(String url, Bitmap bitmap, ImageView view) {
					
					view.setImageBitmap(bitmap);
					ivImage.setVisibility(View.VISIBLE);
					progress.setVisibility(View.INVISIBLE);
				}
			};
			BitmapDownloader.download(myStoryInfo.getMystory_member_profile(), null, ocl, null, ivImage, false);
		} else {
			progress.setVisibility(View.INVISIBLE);
		}
		
		if(!StringUtils.isEmpty(myStoryInfo.getMstatus_name())) {
			tvStatus.setText(myStoryInfo.getMstatus_name());
		}
		
		if(!StringUtils.isEmpty(myStoryInfo.getIlike())) {
			tvInterested.setText(myStoryInfo.getIlike());
		} else if(MainActivity.myInfo.getMember_id().equals(myStoryInfo.getMystory_member_id())){
			tvInterested.setText(R.string.touchHere);
		}
		
		if(!StringUtils.isEmpty(myStoryInfo.getJob())) {
			tvJob.setText(myStoryInfo.getJob());
		} else if(MainActivity.myInfo.getMember_id().equals(myStoryInfo.getMystory_member_id())){
			tvJob.setText(R.string.touchHere);
		}
		
		if(!StringUtils.isEmpty(myStoryInfo.getJobname())) {
			tvCompany.setText(myStoryInfo.getJobname());
		} else if(MainActivity.myInfo.getMember_id().equals(myStoryInfo.getMystory_member_id())){
			tvCompany.setText(R.string.touchHere);
		}
		
		if(!StringUtils.isEmpty(myStoryInfo.getAddress())) {
			tvLiveLocation.setText(myStoryInfo.getAddress());
		} else if(MainActivity.myInfo.getMember_id().equals(myStoryInfo.getMystory_member_id())){
			tvLiveLocation.setText(R.string.touchHere);
		}
		
		if(!StringUtils.isEmpty(myStoryInfo.getPlayground())) {
			tvActiveLocation.setText(myStoryInfo.getPlayground());
		} else if(MainActivity.myInfo.getMember_id().equals(myStoryInfo.getMystory_member_id())){
			tvActiveLocation.setText(R.string.touchHere);
		}
	}
	
	public void loadPosts() {
		
		if(isDownloading || isLastList) {
			return;
		}

		super.downloadInfo();
		
		url = ZoneConstants.BASE_URL + "spot/list" +
				"?member_id=" + userId + 
				"&last_spot_nid=" + lastIndexno +
				"&image_size=" + ResizeUtils.getSpecificLength(308) +
				"&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID);

		if(mode == 1) {
			url += "&concern_kind=010";
		} else if(mode == 2) {
			url += "&concern_kind=020";
		} else {
			return;
		}

		if(!StringUtils.isEmpty(url)) {
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					
					LogUtils.log("onError. url : " + url);
					setPage(false);
				}
				
				@Override
				public void onCompleted(String url, String result) {
					
					LogUtils.log("onCompleted. url : " + url + "\nresult : "+ result);
					
					try {
						JSONObject objJSON = new JSONObject(result);
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
					} catch(Exception e) {
						LogUtils.trace(e);
						setPage(false);
					}
				}
			};
			AsyncStringDownloader.download(url, getDownloadKey(), ocl);
		}
	}
	
	public void loadMessageSample() {

		if(isDownloading) {
			return;
		}
		
		super.downloadInfo();
		
		String url = null;
		
		if(userId.equals(MainActivity.myInfo.getMember_id())) {
			url = ZoneConstants.BASE_URL + "microspot/relationList" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&image_size=" + ResizeUtils.getSpecificLength(308);
					
		} else {
			url = ZoneConstants.BASE_URL + "microspot/message_tap" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&friend_member_id=" + userId +
					"&last_microspot_nid=0" +
					"&image_size=" + ResizeUtils.getSpecificLength(308);
		}

		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {
				
				LogUtils.log("UserPage.loadMessageSample.onError.\nurl : " + url);
				
				setPage(false);
			}
			
			@Override
			public void onCompleted(String url, String result) {

				LogUtils.log("UserPage.loadMessageSample.onCompleted.\nurl : " + url + "\nresult : " + result);
				
				try {
					JSONObject objJSON = new JSONObject(result);
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
						
						if(!userId.equals(MainActivity.myInfo.getMember_id())) {
							
							String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/message" +
									"?member_id=" + myStoryInfo.getMystory_member_id();  
							IntentHandlerActivity.actionByUri(Uri.parse(uriString));
						}
					}

					setPage(true);
				} catch(Exception e) {
					LogUtils.trace(e);
					setPage(false);
				}
			}
		};
		AsyncStringDownloader.download(url, getDownloadKey(), ocl);
	}
	
	public void showInformation() {

		if(!isAnimating && isInfoHidden) {
			isInfoHidden = false;
			isAnimating = true;

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
						gridView.getRefreshableView().setSelection(0);
					} else if(mode == 3) {
						listView.getRefreshableView().setSelection(0);
					}
				}
			}, ANIM_DURATION + 100);
		}
	}
	
	public void hideInformation() {

		if(!isAnimating && !isInfoHidden) {
		
			isInfoHidden = true;
			isAnimating = true;

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
							int fp = gridView.getRefreshableView().getFirstVisiblePosition();
							
							if(fp < 2) {
								fp = 2;
							}
							
							gridView.getRefreshableView().smoothScrollBy(listView.getChildAt(fp).getBottom(), ANIM_DURATION/2);
						} else if(mode == 3) {
							int fp = listView.getRefreshableView().getFirstVisiblePosition();
							
							if(fp != 0) {
								listView.getRefreshableView().smoothScrollBy(listView.getChildAt(fp).getBottom(), ANIM_DURATION/2);
							}
						}
					} catch(Exception e) {
						LogUtils.trace(e);
					}
				}
			}, ANIM_DURATION + 200);
		}
	}

	@Override
	protected String generateDownloadKey() {
		return "USERPAGE" + madeCount;
	}


	@Override
	protected int getXmlResId() {
		return R.layout.page_user;
	}
}

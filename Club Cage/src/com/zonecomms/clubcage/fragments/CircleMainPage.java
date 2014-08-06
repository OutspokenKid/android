package com.zonecomms.clubcage.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.clubcage.classes.ZonecommsApplication;
import com.zonecomms.clubcage.classes.ZonecommsFragment;
import com.zonecomms.common.adapters.CircleListAdapter;
import com.zonecomms.common.models.Post;
import com.zonecomms.common.models.StartupInfo.BgInfo;
import com.zonecomms.common.views.CircleHeaderView;
import com.zonecomms.common.views.ImagePlayViewer;

public class CircleMainPage extends ZonecommsFragment {

	private static final int ANIM_DURATION = 2000;
	public static final int MENU_BUTTON_LENGTH = 50;
	
	private View menuCover;
	private ScrollView menuScroll;
	private LinearLayout menuLinear;
	private ImagePlayViewer imagePlayViewer;
	private TextView[] tvTitles = new TextView[2];
	private FrameLayout frameForMenu, frameForHome, frameForWrite, frameForN; 
	private SwipeRefreshLayout swipeLayout;
	private ListView listView;
	private CircleListAdapter listAdapter;
	private CircleHeaderView circleHeaderView;
	
	private ArrayList<BaseModel> models = new ArrayList<BaseModel>();
	private BgInfo bgInfo;
	private boolean isInit;
	private boolean isDownloading;
	private boolean isLastList;
	private int lastIndexno;
	
	private AlphaAnimation aaIn, aaOut;
	private TranslateAnimation taIn, taOut;
	private boolean animating;
	
	@Override
	public void setVariables() {
		super.setVariables();
	}
	
	@Override
	public void bindViews() {

		menuCover = mThisView.findViewById(R.id.circleMainPage_menuCover);
    	menuScroll = (ScrollView) mThisView.findViewById(R.id.circleMainPage_menuScroll);
    	menuLinear = (LinearLayout) mThisView.findViewById(R.id.circleMainPage_menuLinear);
    	imagePlayViewer = (ImagePlayViewer) mThisView.findViewById(R.id.circleMainPage_imagePlayViewer);
    	tvTitles[0] = (TextView) mThisView.findViewById(R.id.circleMainPage_tvTitle1);
    	tvTitles[1] = (TextView) mThisView.findViewById(R.id.circleMainPage_tvTitle2);
    
    	frameForMenu = (FrameLayout) mThisView.findViewById(R.id.circleMainPage_frameForMenu);
    	frameForHome = (FrameLayout) mThisView.findViewById(R.id.circleMainPage_frameForHome);
    	frameForWrite = (FrameLayout) mThisView.findViewById(R.id.circleMainPage_frameForWrite);
    	frameForN = (FrameLayout) mThisView.findViewById(R.id.circleMainPage_frameForN);
    	
    	swipeLayout = (SwipeRefreshLayout) mThisView.findViewById(R.id.circleMainPage_swipe_container);
    	listView = (ListView) mThisView.findViewById(R.id.circleMainPage_listView);
	}
	
	@Override
	public void createPage() {

		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(ANIM_DURATION);
		
		aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(ANIM_DURATION);

		AnimationListener al = new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				animating = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				animating = false;
			}
		};
		
		taIn = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_SELF, 0,
				TranslateAnimation.RELATIVE_TO_SELF, 0,
				TranslateAnimation.RELATIVE_TO_SELF, -1,
				TranslateAnimation.RELATIVE_TO_SELF, 0);
		taIn.setDuration(ANIM_DURATION/5);
		taIn.setAnimationListener(al);
		taIn.setInterpolator(getActivity(), android.R.anim.accelerate_decelerate_interpolator);
		
		taOut = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_SELF, 0,
				TranslateAnimation.RELATIVE_TO_SELF, 0,
				TranslateAnimation.RELATIVE_TO_SELF, 0,
				TranslateAnimation.RELATIVE_TO_SELF, -1);
		taOut.setDuration(ANIM_DURATION/5);
		taOut.setAnimationListener(al);
		taOut.setInterpolator(getActivity(), android.R.anim.accelerate_decelerate_interpolator);
		
    	bgInfo = ZonecommsApplication.startupInfo.getBgInfo();
    	
    	if(bgInfo == null){
    		LogUtils.log("###where.setVariables.  bgInfo is null.");
    	} else {
    		LogUtils.log("###where.setVariables.  bgInfo.url.size : " + bgInfo.urls.size());
    	}
    	
    	imagePlayViewer.setImageUrls(bgInfo.urls);
		
		circleHeaderView = new CircleHeaderView(mContext);
    	listAdapter = new CircleListAdapter(mContext, circleHeaderView, mActivity.getLayoutInflater(), models);
    	listView.setAdapter(listAdapter);
    	listView.setCacheColorHint(Color.TRANSPARENT);
    	listView.setDivider(new ColorDrawable(Color.LTGRAY));
    	listView.setDividerHeight(1);
    	
        swipeLayout.setColorSchemeColors(
        		Color.argb(255, 255, 102, 153), 
        		Color.argb(255, 255, 153, 153), 
        		Color.argb(255, 255, 204, 153), 
        		Color.argb(255, 255, 255, 153));
        swipeLayout.setEnabled(true);
        
        addMenus();
	}

	@Override
	public void setListeners() {

    	tvTitles[0].setClickable(true);
    	tvTitles[1].setClickable(true);
    	menuLinear.setClickable(true);
    	
    	menuCover.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				hideMenuScroll();
			}
		});
    	
    	listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

				if(firstVisibleItem <= 1) {
					
					if(-circleHeaderView.getTop() >= CircleHeaderView.diffLength) {
						showTitleBar();
						circleHeaderView.hideTitleBar();
						imagePlayViewer.setNeedPlayImage(false);
					} else {
						hideTitleBar();
						circleHeaderView.showTitleBar();
						imagePlayViewer.setNeedPlayImage(true);
					}
				}
				
				if(visibleItemCount < totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}
			}
		});
    	
    	listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				try {
					if(position > 0) {
						mainActivity.showPostPage(((Post)models.get(position - 1)).getSpot_nid());
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
    	
    	frameForMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(menuScroll.getVisibility() == View.VISIBLE) {
					hideMenuScroll();
				} else {
					showMenuScroll();
				}
			}
		});
    	
    	frameForHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mainActivity.showMainPage();
			}
		});
    	
    	frameForWrite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ToastUtils.showToast("Write button clicked.");
			}
		});
    	
    	frameForN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ToastUtils.showToast("N button clicked.");
			}
		});
    	
    	swipeLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {

				swipeLayout.setRefreshing(true);
				
				new Handler().postDelayed(new Runnable() {
			        @Override public void run() {
			        	
			        	//ImagePlayViewer가 슬라이드 중인 경우 ImagePlayViewer.SLIDE_DURATION 만큼 대기했다가 새로고침.
			        	if(imagePlayViewer.isSliding()) {
			        		new Handler().postDelayed(new Runnable() {
								
								@Override
								public void run() {
									refreshPage();
								}
							}, ImagePlayViewer.SLIDE_DURATION);
			        	} else{
			        		refreshPage();
			        	}
			        }
			    }, 2000);
			}
		});
	}
	
	@Override
	public void setSizes() {

		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, menuScroll, 
    			2, Gravity.TOP, null, new int[]{0, CircleHeaderView.TITLE_BAR_HEIGHT, 0, 0});
    	ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, CircleHeaderView.TITLE_BAR_HEIGHT, 
    			tvTitles[0], 2, Gravity.TOP, null);
    	ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, CircleHeaderView.TITLE_BAR_HEIGHT, 
    			tvTitles[1], 2, Gravity.TOP, null);
    	
    	ResizeUtils.viewResize(60, 60, frameForMenu, 2, Gravity.LEFT|Gravity.TOP, new int[]{17, 11, 0, 0});
    	ResizeUtils.viewResize(60, 60, frameForHome, 2, Gravity.LEFT|Gravity.TOP, new int[]{81, 11, 0, 0});
    	ResizeUtils.viewResize(60, 60, frameForWrite, 2, Gravity.RIGHT|Gravity.TOP, new int[]{0, 11, 81, 0});
    	ResizeUtils.viewResize(60, 60, frameForN, 2, Gravity.RIGHT|Gravity.TOP, new int[]{0, 11, 17, 0});
    	
    	ResizeUtils.viewResize(34, 34, mThisView.findViewById(R.id.circleMainPage_menu), 2, Gravity.CENTER, null);
    	ResizeUtils.viewResize(34, 34, mThisView.findViewById(R.id.circleMainPage_home), 2, Gravity.CENTER, null);
    	ResizeUtils.viewResize(34, 34, mThisView.findViewById(R.id.circleMainPage_write), 2, Gravity.CENTER, null);
    	ResizeUtils.viewResize(34, 34, mThisView.findViewById(R.id.circleMainPage_n), 2, Gravity.CENTER, null);
    	
    	FontUtils.setFontSize(tvTitles[0], 36);
    	FontUtils.setFontSize(tvTitles[1], 36);
	}

	@Override
	public void downloadInfo() {
		
		if(isDownloading || isLastList) {
			return;
		}
    	
    	isDownloading = true;
    	
    	String url = ZoneConstants.BASE_URL + "sb/partner_spot_list?" +
    			"sb_id=" + ZoneConstants.PAPP_ID +
    			"&board_nid=1" +
    			"&last_sb_spot_nid=" + lastIndexno +
    			"&image_size=640";
    	
    	DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {
			
			@Override
			public void onError(String url) {
				
				LogUtils.log("###CircleMainPage.Error.  \nurl : " + url);
				setPage(false);
			}
			
			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				LogUtils.log("###CircleMainPage.onCompleted.  \nurl : " + url +
						"\nresult  :  " + objJSON);
				
				try {
					JSONArray arJSON = objJSON.getJSONArray("data");
					
					int size = arJSON.length();
					
					if(size > 0) {
						for(int i=0; i<size; i++) {
							models.add(new Post(arJSON.getJSONObject(i)));
						}
					} else {
						isLastList = true;
						ToastUtils.showToast(R.string.lastPage);
					}
					
					listAdapter.notifyDataSetChanged();
					
					if(swipeLayout.getVisibility() != View.VISIBLE) {
						showContainer();
					} else {
						showNext();
					}
					
					setPage(true);
				} catch (Exception e) {
					LogUtils.trace(e);
					setPage(false);
				} catch (Error e) {
					LogUtils.trace(e);
					setPage(false);
				} finally  {
					swipeLayout.setRefreshing(false);
				}
			}
		});
	}
	
	@Override
	public void setPage(boolean successDownload) {

		isDownloading = false;
    	
    	if(successDownload) {

			if(models != null && models.size() > 0) {
				lastIndexno = models.get(models.size() - 1).getIndexno();
			}
			
			if(listAdapter != null) {
				listAdapter.notifyDataSetChanged();
			}
		} else {
			ToastUtils.showToast(R.string.failToLoadList);
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		mainActivity.hideLoadingView();
		mainActivity.hideTitleBar();
		
    	menuScroll.setVisibility(View.INVISIBLE);
		menuCover.setVisibility(View.INVISIBLE);
		animating = false;
			
    	imagePlayViewer.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
				if(!isInit) {
					isInit = true;

					//Set titleBars.
			    	setTitleBarText("STORY");
			    	
			    	String colorText = "#b2" + bgInfo.colors.get(0).replace("#", "");
			    	int color = Color.parseColor(colorText);
			    	
					tvTitles[0].setBackgroundColor(color);
					circleHeaderView.setTitleBarColor(color);
					changeMenuColor(color);
					listAdapter.changeColor(color);
					downloadInfo();
				
				} else {
					showNext();
				}
			}
		}, 1000);
	}
	
	@Override
	public void refreshPage() {
		
		isDownloading = false;
    	isLastList = false;
    	lastIndexno = 0;
    	models.clear();
    	downloadInfo();
	}
	
	@Override
	public int getContentViewId() {

		return R.layout.page_circlemain;
	}
	
	@Override
	public boolean onMenuPressed() {

		if(menuScroll.getVisibility() == View.VISIBLE) {
			hideMenuScroll();
		} else {
			showMenuScroll();
		}
		
		return true;
	}
	
	@Override
	public boolean onBackPressed() {

		if(menuScroll.getVisibility() == View.VISIBLE) {
			return true;
		}
		
		return false;
	}
	
	public void addMenus() {
    	
    	int size = 15;
    	for(int i=0; i<size; i++) {
			View line = new View(mContext);
			line.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2));
			line.setBackgroundColor(Color.WHITE);
			menuLinear.addView(line);
    		
    		TextView textView = new TextView(mContext);
    		textView.setLayoutParams(new LinearLayout.LayoutParams(
    				LayoutParams.MATCH_PARENT, 
    				ResizeUtils.getSpecificLength(CircleHeaderView.TITLE_BAR_HEIGHT)));
    		textView.setTextColor(Color.WHITE);
    		textView.setText("Menu text " + (i+1));
    		textView.setGravity(Gravity.CENTER);
    		FontUtils.setFontSize(textView, 30);
    		menuLinear.addView(textView);
    	}
    }

    public void showMenuScroll() {
    	
    	if(!animating && menuScroll.getVisibility() != View.VISIBLE) {
    		menuScroll.setVisibility(View.VISIBLE);
    		menuCover.setVisibility(View.VISIBLE);
    		menuScroll.startAnimation(taIn);
    	}
    }
    
    public void hideMenuScroll() {
    	
    	if(!animating && menuScroll.getVisibility() == View.VISIBLE) {
    		menuScroll.setVisibility(View.INVISIBLE);
    		menuCover.setVisibility(View.INVISIBLE);
    		menuScroll.startAnimation(taOut);
    	}
    }
    
    public void showContainer() {
    	
    	TranslateAnimation taIn = new TranslateAnimation(
    			TranslateAnimation.RELATIVE_TO_PARENT, 0,
    			TranslateAnimation.RELATIVE_TO_PARENT, 0,
    			TranslateAnimation.RELATIVE_TO_PARENT, 1,
    			TranslateAnimation.RELATIVE_TO_PARENT, 0);
    	taIn.setDuration(ANIM_DURATION);
    	taIn.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				circleHeaderView.showHeader();
				imagePlayViewer.start();
			}
		});
    	taIn.setInterpolator(mContext, android.R.anim.accelerate_decelerate_interpolator);
    	
    	swipeLayout.startAnimation(taIn);
    	swipeLayout.setVisibility(View.VISIBLE);
    }
    
    public void showTitleBar() {
    	
    	tvTitles[ImagePlayViewer.imageIndex % 2].setVisibility(View.VISIBLE);
    }
    
    public void hideTitleBar() {
    	
    	tvTitles[ImagePlayViewer.imageIndex % 2].setVisibility(View.INVISIBLE);
    }
 
    public void showNext() {
    	
    	ImagePlayViewer.imageIndex++;
		
		String colorText = "#b2" + bgInfo.colors.get(
    			ImagePlayViewer.imageIndex % bgInfo.colors.size()).replace("#", "");
    	int color = Color.parseColor(colorText);
		
		imagePlayViewer.showNext();
		changeTitleBarColor(color);
		changeMenuColor(color);
		listAdapter.changeColor(color);
    }

    public void setTitleBarText(String text) {
    	
    	tvTitles[0].setText(text);
    	tvTitles[1].setText(text);
    	circleHeaderView.setTitleBarText(text);
    }
    
    public void changeTitleBarColor(int color) {

    	int in = ImagePlayViewer.imageIndex % 2;
    	int out = (in + 1) % 2;
    	
    	if(tvTitles[out].getVisibility() == View.VISIBLE) {
    		tvTitles[out].setVisibility(View.INVISIBLE);
        	tvTitles[out].startAnimation(aaOut);

        	tvTitles[in].setBackgroundColor(color);
        	tvTitles[in].setVisibility(View.VISIBLE);
        	tvTitles[in].startAnimation(aaIn);
        	
        	circleHeaderView.setTitleBarColor(color);
        	
    	} else {
    		tvTitles[in].setBackgroundColor(color);
    		circleHeaderView.changeTitleBarColor(color);
    	}
    }

    public void changeMenuColor(int color) {
    	
    	menuScroll.setBackgroundColor(color);
    }
}

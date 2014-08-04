package com.example.androidvolleytest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.outspoken_kid.classes.OutSpokenApplication;
import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class CircleMainActivity extends Activity {

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
	
	private ArrayList<Post> posts = new ArrayList<Post>();
	private BgInfos bgInfos;
	private boolean isInit;
	
	private AlphaAnimation aaIn, aaOut;
	private TranslateAnimation taIn, taOut;
	private boolean animating;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circlemain);
        OutSpokenApplication.initWithActivity(this);
        
        bindViews();
        setVariables();
        createPage();
        
        setSizes();
        setListeners();
    }
    
    public void bindViews() {

    	menuCover = findViewById(R.id.menuCover);
    	menuScroll = (ScrollView) findViewById(R.id.menuScroll);
    	menuLinear = (LinearLayout) findViewById(R.id.menuLinear);
    	imagePlayViewer = (ImagePlayViewer) findViewById(R.id.imagePlayViewer);
    	tvTitles[0] = (TextView) findViewById(R.id.tvTitle1);
    	tvTitles[1] = (TextView) findViewById(R.id.tvTitle2);
    
    	frameForMenu = (FrameLayout) findViewById(R.id.frameForMenu);
    	frameForHome = (FrameLayout) findViewById(R.id.frameForHome);
    	frameForWrite = (FrameLayout) findViewById(R.id.frameForWrite);
    	frameForN = (FrameLayout) findViewById(R.id.frameForN);
    	
    	swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
    	listView = (ListView) findViewById(R.id.listView);
    }
    
    public void setVariables() {
    	
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
		taIn.setInterpolator(this, android.R.anim.accelerate_decelerate_interpolator);
		
		taOut = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_SELF, 0,
				TranslateAnimation.RELATIVE_TO_SELF, 0,
				TranslateAnimation.RELATIVE_TO_SELF, 0,
				TranslateAnimation.RELATIVE_TO_SELF, -1);
		taOut.setDuration(ANIM_DURATION/5);
		taOut.setAnimationListener(al);
		taOut.setInterpolator(this, android.R.anim.accelerate_decelerate_interpolator);
    	
    	bgInfos = (BgInfos) getIntent().getSerializableExtra("bgInfos");
    	imagePlayViewer.setImageUrls(bgInfos.urls);
    }
    
    public void createPage() {
    	
    	circleHeaderView = new CircleHeaderView(this);
    	
    	listAdapter = new CircleListAdapter(this, circleHeaderView, getLayoutInflater(), posts);
    	listView.setAdapter(listAdapter);
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
    	
    	ResizeUtils.viewResize(34, 34, findViewById(R.id.menu), 2, Gravity.CENTER, null);
    	ResizeUtils.viewResize(34, 34, findViewById(R.id.home), 2, Gravity.CENTER, null);
    	ResizeUtils.viewResize(34, 34, findViewById(R.id.write), 2, Gravity.CENTER, null);
    	ResizeUtils.viewResize(34, 34, findViewById(R.id.n), 2, Gravity.CENTER, null);
    	
    	FontUtils.setFontSize(tvTitles[0], 36);
    	FontUtils.setFontSize(tvTitles[1], 36);
    }
    
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

				ToastUtils.showToast("Home button clicked.");
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
									onRefreshPage();
								}
							}, ImagePlayViewer.SLIDE_DURATION);
			        	} else{
			        		onRefreshPage();
			        	}
			        }
			    }, 2000);
			}
		});
    }
    
    public void setTitleBarText(String text) {
    	
    	tvTitles[0].setText(text);
    	tvTitles[1].setText(text);
    	circleHeaderView.setTitleBarText(text);
    }
    
    public void addMenus() {
    	
    	int size = 15;
    	for(int i=0; i<size; i++) {
			View line = new View(this);
			line.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 2));
			line.setBackgroundColor(Color.WHITE);
			menuLinear.addView(line);
    		
    		TextView textView = new TextView(this);
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
    	taIn.setInterpolator(this, android.R.anim.accelerate_decelerate_interpolator);
    	
    	swipeLayout.startAnimation(taIn);
    	swipeLayout.setVisibility(View.VISIBLE);
    }

    public void onRefreshPage() {
    	
    	swipeLayout.setRefreshing(false);
    	showNext();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
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
			    	
			    	String colorText = "#b2" + bgInfos.colors.get(0).replace("#", "");
			    	int color = Color.parseColor(colorText);
			    	
					tvTitles[0].setBackgroundColor(color);
					circleHeaderView.setTitleBarColor(color);
					changeMenuColor(color);
					listAdapter.changeColor(color);
					downloadPosts();
				
				} else {
					showNext();
				}
			}
		}, 1000);
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	imagePlayViewer.clear();
    	ViewUnbindHelper.unbindReferences(this, R.layout.activity_circlemain);
    }
    
    @Override
    public void onBackPressed() {
    	
    	if(menuScroll.getVisibility() == View.VISIBLE) {
    		hideMenuScroll();
    	} else {
    		super.onBackPressed();
    	}
    }
    
    public void downloadPosts() {

    	String url = "http://112.169.61.103/externalapi/public/sb/partner_spot_list?sb_id=massclub&board_nid=1&last_sb_spot_nid=0&image_size=640";
    	DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {
			
			@Override
			public void onError(String url) {
				LogUtils.log("###MainActivity.onError.  \nurl : " + url);
				ToastUtils.showToast("서버에서 정보를 받아오지 못했습니다");
			}
			
			@Override
			public void onCompleted(String url, JSONObject objJSON) {
			
				try {
					JSONArray arJSON = objJSON.getJSONArray("data");
					
					int size = arJSON.length();
					for(int i=0; i<size; i++) {
						posts.add(new Post(arJSON.getJSONObject(i)));
					}
					
					listAdapter.notifyDataSetChanged();
					showContainer();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
    }
    
    public void showTitleBar() {
    	
    	tvTitles[ImagePlayViewer.imageIndex % 2].setVisibility(View.VISIBLE);
    }
    
    public void hideTitleBar() {
    	
    	tvTitles[ImagePlayViewer.imageIndex % 2].setVisibility(View.INVISIBLE);
    }
 
    public void showNext() {
    	
    	ImagePlayViewer.imageIndex++;
		
		String colorText = "#b2" + bgInfos.colors.get(
    			ImagePlayViewer.imageIndex % bgInfos.colors.size()).replace("#", "");
    	int color = Color.parseColor(colorText);
		
		imagePlayViewer.showNext();
		changeTitleBarColor(color);
		changeMenuColor(color);
		listAdapter.changeColor(color);
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

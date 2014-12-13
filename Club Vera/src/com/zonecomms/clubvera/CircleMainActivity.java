package com.zonecomms.clubvera;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.IntentUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.PackageUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.HoloConstants;
import com.zonecomms.clubvera.MainActivity.OnAfterLoginListener;
import com.zonecomms.clubvera.classes.ZoneConstants;
import com.zonecomms.clubvera.classes.ZonecommsApplication;
import com.zonecomms.clubvera.fragments.MainPage;
import com.zonecomms.common.adapters.CircleListAdapter;
import com.zonecomms.common.models.Post;
import com.zonecomms.common.models.StartupInfo.BgInfo;
import com.zonecomms.common.models.StartupInfo.Popup;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.views.CircleHeaderView;
import com.zonecomms.common.views.ImagePlayViewer;
import com.zonecomms.common.views.NoticePopup;
import com.zonecomms.common.views.ProfilePopup;
import com.zonecomms.common.views.TitleBar;

public class CircleMainActivity extends Activity {

	private static final int ANIM_DURATION = 2000;
	public static final int MENU_BUTTON_LENGTH = 50;
	
	public static int playIndex = 0;

	private Context context;
	
	private View menuCover;
	private ScrollView menuScroll;
	private LinearLayout menuLinear;
	private ImagePlayViewer imagePlayViewer;
	private TextView[] tvTitles = new TextView[2];
	private View menu;
	private View home;
	private View write;
	private View n;
	private SwipeRefreshLayout swipeLayout;
	private ListView listView;
	private CircleListAdapter listAdapter;
	private CircleHeaderView circleHeaderView;
	private NoticePopup noticePopup;
	private ProfilePopup profilePopup;
	
	private ArrayList<BaseModel> models = new ArrayList<BaseModel>();
	private BgInfo[] bgInfos;
	private boolean isDownloading;
	private boolean isLastList;
	private int titleBarIndex;
	private int lastIndexno;
	
	private AlphaAnimation aaIn, aaOut;
	private TranslateAnimation taIn, taOut;
	private boolean animating;
	
	private OnAfterLoginListener onAfterLoginListener;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circlemain);
        context = this;
        
        try {
        	ZonecommsApplication.initWithActivity(this);
    		ZonecommsApplication.setupResources(this);
            
            bindViews();
            setVariables();
            createPage();
            
            setSizes();
            setListeners();
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
    }
    
    @Override
    public void setContentView(int layoutResID) {
    	super.setContentView(layoutResID);
    	
    	FontUtils.setGlobalFont(this, layoutResID, getString(R.string.customFont));
    }
    
    public void bindViews() {

    	menuCover = findViewById(R.id.circleMainActivity_menuCover);
    	menuScroll = (ScrollView) findViewById(R.id.circleMainActivity_menuScroll);
    	menuLinear = (LinearLayout) findViewById(R.id.circleMainActivity_menuLinear);
    	imagePlayViewer = (ImagePlayViewer) findViewById(R.id.circleMainActivity_imagePlayViewer);
    	tvTitles[0] = (TextView) findViewById(R.id.circleMainActivity_tvTitle1);
    	tvTitles[1] = (TextView) findViewById(R.id.circleMainActivity_tvTitle2);
    
    	menu = findViewById(R.id.circleMainActivity_menu);
    	home = findViewById(R.id.circleMainActivity_home);
    	write = findViewById(R.id.circleMainActivity_write);
    	n = findViewById(R.id.circleMainActivity_n);
    	
    	swipeLayout = (SwipeRefreshLayout) findViewById(R.id.circleMainActivity_swipe_container);
    	listView = (ListView) findViewById(R.id.circleMainActivity_listView);
    	
    	FontUtils.setGlobalFont(tvTitles[0]);
    	FontUtils.setGlobalFont(tvTitles[1]);
    }
    
    public void setVariables() {
    	
    	playIndex = 0;
    	
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
		
    	bgInfos = ZonecommsApplication.startupInfo.getBgInfos();
    }
    
    public void createPage() {
    	
    	circleHeaderView = new CircleHeaderView(this);
    	setTitleBarText("STORY");
    	
    	listAdapter = new CircleListAdapter(this, circleHeaderView, getLayoutInflater(), models);
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
        checkPopup();
    }
    
    public void setSizes() {

    	ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, menuScroll, 
    			2, Gravity.TOP, null);
    	ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, CircleHeaderView.TITLE_BAR_HEIGHT, 
    			tvTitles[0], 2, Gravity.TOP, null);
    	ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, CircleHeaderView.TITLE_BAR_HEIGHT, 
    			tvTitles[1], 2, Gravity.TOP, null);
    	
    	ResizeUtils.viewResize(60, 82, menu, 2, Gravity.LEFT|Gravity.TOP, new int[]{10, 0, 0, 0});
    	ResizeUtils.viewResize(60, 82, home, 2, Gravity.LEFT|Gravity.TOP, new int[]{80, 0, 0, 0});
    	ResizeUtils.viewResize(60, 82, write, 2, Gravity.RIGHT|Gravity.TOP, new int[]{0, 0, 80, 0});
    	ResizeUtils.viewResize(60, 82, n, 2, Gravity.RIGHT|Gravity.TOP, new int[]{0, 0, 10, 0});
    	
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
						imagePlayViewer.setNeedToPlayImage(false);
					} else {
						hideTitleBar();
						circleHeaderView.showTitleBar();
						imagePlayViewer.setNeedToPlayImage(true);
					}
				} else if(firstVisibleItem == 2) {
					showTitleBar();
					circleHeaderView.hideTitleBar();
					imagePlayViewer.setNeedToPlayImage(false);
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
				
				LogUtils.log("###CircleMainActivity.listView.onItemClick.  position : " + position);
				
				try {
					if(position > 0) {
						String uriString = ZoneConstants.PAPP_ID + 
								"://android.zonecomms.com/post?spot_nid=" + 
								((Post)models.get(position - 1)).getSpot_nid();
						launchToMainActivity(Uri.parse(uriString));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
    	
    	menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(menuScroll.getVisibility() == View.VISIBLE) {
					hideMenuScroll();
				} else {
					showMenuScroll();
				}
			}
		});
    	
    	home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				launchToMainActivity(null);
			}
		});
    	
    	write.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				checkLoginAndExecute(new OnAfterLoginListener() {
					
					@Override
					public void onAfterLogin() {
						Intent intent = new Intent(CircleMainActivity.this, WriteActivity.class);
						intent.putExtra("board_nid", 1);
						startActivityForResult(intent, ZoneConstants.REQUEST_WRITE);
					}
				});
			}
		});
    	
    	n.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				checkNApp(null);
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

		int topPadding = ResizeUtils.getSpecificLength(CircleHeaderView.TITLE_BAR_HEIGHT);
		menuScroll.setPadding(0, topPadding, 0, 0);
    	
		int[] titleResIds = new int[] {
				R.string.myHome,
				R.string.notice,
				R.string.schedule,
				R.string.guest,
				R.string.story,
				R.string.image,
				R.string.video,
				R.string.showMember,
				R.string.setting
		};
		
		final String[] uriStrings = new String[] {
				"home",
				"notice",
				"schedule",
				"guest",
				"freetalk",
				"image",
				"video",
				"member",
				"setting"
		};
		
		final String defaultUriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/";
    	int height = ResizeUtils.getSpecificLength(80);
    	int size = titleResIds.length;
    	for(int i=0; i<size; i++) {
    		final int INDEX = i;
    		
			View line = new View(this);
			line.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1));
			line.setBackgroundColor(Color.WHITE);
			menuLinear.addView(line);
    		
    		TextView textView = new TextView(this);
    		textView.setLayoutParams(new LinearLayout.LayoutParams(
    				LayoutParams.MATCH_PARENT, height));
    		textView.setTextColor(Color.WHITE);
    		textView.setText(titleResIds[i]);
    		textView.setGravity(Gravity.CENTER);
    		textView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					try {
						if(INDEX == 0 || INDEX == 7 || INDEX == 8) {
							checkLoginAndExecute(new OnAfterLoginListener() {
								
								@Override
								public void onAfterLogin() {
									
									launchToMainActivity(Uri.parse(defaultUriString + uriStrings[INDEX]));
								}
							});
						} else{
							launchToMainActivity(Uri.parse(defaultUriString + uriStrings[INDEX]));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (Error e) {
						LogUtils.trace(e);
					}
				}
			});
    		FontUtils.setFontSize(textView, 28);
    		FontUtils.setGlobalFont(textView);
    		menuLinear.addView(textView);
    	}
    }

    public void setMenuColor(int color) {
    	
    	menuScroll.setBackgroundColor(color);
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
			}
		});
    	taIn.setInterpolator(this, android.R.anim.accelerate_decelerate_interpolator);
    	
    	showNext();
    	swipeLayout.startAnimation(taIn);
    	swipeLayout.setVisibility(View.VISIBLE);
    	
    	handleIntent();
    }

    public void onRefreshPage() {
    	
    	isDownloading = false;
    	isLastList = false;
    	lastIndexno = 0;
    	models.clear();
    	downloadInfo();
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
				
				if(models.size() == 0) {
					downloadInfo();
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
    }
    
    @Override
    public void onBackPressed() {
    	
    	if(menuScroll.getVisibility() == View.VISIBLE) {
    		hideMenuScroll();
    	} else if(profilePopup != null && profilePopup.getVisibility() == View.VISIBLE) {
			profilePopup.hide(null);
    	} else {
    		super.onBackPressed();
    	}
    }
    
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
					boolean isFirstLoading = models.size() == 0;
					
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
					
					if(isFirstLoading) {
						
						if(swipeLayout.getVisibility() != View.VISIBLE) {
							showContainer();
						} else {
							showNext();
						}
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
    
    public void showTitleBar() {
    	
    	tvTitles[titleBarIndex % 2].setVisibility(View.VISIBLE);
    	tvTitles[(titleBarIndex + 1) % 2].setVisibility(View.INVISIBLE);
    }
    
    public void hideTitleBar() {
    	
    	tvTitles[titleBarIndex % 2].setVisibility(View.INVISIBLE);
    	tvTitles[(titleBarIndex + 1) % 2].setVisibility(View.INVISIBLE);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		switch(requestCode) {

		case ZoneConstants.REQUEST_WRITE:
			
			if(resultCode == RESULT_OK) {
				
				if(data != null && data.hasExtra("spot_nid")) {
					int spot_nid = data.getIntExtra("spot_nid", 0);
					
					if(spot_nid != 0) {
						String uriString = ZoneConstants.PAPP_ID + "://android.zonecomms.com/post?spot_nid=" 
								+ spot_nid;
						launchToMainActivity(Uri.parse(uriString));
					}
				}
			}
			break;
			
		case ZoneConstants.REQUEST_SIGN:

			if(resultCode == RESULT_OK) {
				
				if(onAfterLoginListener != null) {
					onAfterLoginListener.onAfterLogin();
				}
			}
			
			onAfterLoginListener = null;
			checkGCM();
			break;
		}
	}
		
    public void showNext() {
    	
    	if(bgInfos == null || bgInfos.length == 0) {
    		return;
    	}
    	
    	int currentIndex = playIndex % bgInfos.length;
    	int nextIndex = (playIndex + 1) % bgInfos.length;
    	
    	LogUtils.log("###CircleMainActivity.showNext.  currentIndex : " + currentIndex);

    	//이미지 설정.
    	imagePlayViewer.showNext(bgInfos[currentIndex].url, bgInfos[nextIndex].url);
    	
    	//색 설정.
    	String colorText = "#b2" + bgInfos[currentIndex].color.replace("#", "");
    	int Alphacolor = Color.parseColor(colorText);
    	int NonAlphaColor = Color.parseColor(bgInfos[currentIndex].color);
    	
    	//리스트 아이템 설정.
    	listAdapter.changeColor(NonAlphaColor);
    	
    	//메뉴 색 설정.
    	setMenuColor(NonAlphaColor);
    	
    	//타이틀바 설정.
    	if(playIndex == 0) {
			tvTitles[0].setBackgroundColor(NonAlphaColor);
			circleHeaderView.setTitleBarColor(currentIndex, Alphacolor);
    		
    	} else {
    		changeTitleBarColor(Alphacolor, NonAlphaColor);
    	}
    	
    	//HoloConstants 설정.
    	HoloConstants.COLOR_HOLO_TARGET_ON = Alphacolor;
		HoloConstants.COLOR_HOLO_TARGET_OFF = Color.parseColor("#99" + bgInfos[currentIndex].color.replace("#", ""));
    	
		
		if(playIndex == 0) {
			//기존 메인 TitleBar 색 설정.
			TitleBar.titleBarColor = Color.parseColor(bgInfos[currentIndex].color);
			
			//기존 메인 버튼 색 설정.
			
			//  => 15% 더 밝게.
			int red = Math.min((int)((float)Color.red(TitleBar.titleBarColor) * 1.15f), 255);
			int green = Math.min((int)((float)Color.green(TitleBar.titleBarColor) * 1.15f), 255);
			int blue = Math.min((int)((float)Color.blue(TitleBar.titleBarColor) * 1.15f), 255);
			
			MainPage.colorBig = Color.rgb(red, green, blue);
			
			//  => 30% 더 밝게.
			red = Math.min((int)((float)Color.red(TitleBar.titleBarColor) * 1.3f), 255);
			green = Math.min((int)((float)Color.green(TitleBar.titleBarColor) * 1.3f), 255);
			blue = Math.min((int)((float)Color.blue(TitleBar.titleBarColor) * 1.3f), 255);
			
			MainPage.colorSmall = Color.rgb(red, green, blue);
		}
		
    	playIndex++;
    }
    
    public void changeTitleBarColor(int alphaColor, int nonAlphaColor) {

    	titleBarIndex++;
    	
    	int in = titleBarIndex % 2;
    	int out = (in + 1) % 2;

    	tvTitles[in].setBackgroundColor(nonAlphaColor);
    	circleHeaderView.changeTitleBarColor(alphaColor, imagePlayViewer.isNeedToPlayImage());
    	
    	//타이틀바가 보이는 상태.
    	if(!imagePlayViewer.isNeedToPlayImage()) {
    		tvTitles[in].setVisibility(View.VISIBLE);
    		tvTitles[in].startAnimation(aaIn);
    		
        	tvTitles[out].setVisibility(View.INVISIBLE);
    		tvTitles[out].startAnimation(aaOut);
    	}
    }

    public void launchToMainActivity(Uri uri) {
		
		Intent intent = new Intent(this, MainActivity.class);
		intent.setData(uri);
		startActivity(intent);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	public void checkPopup() {
		
		if(ZonecommsApplication.startupInfo != null && ZonecommsApplication.startupInfo.getPopup() != null) {
			int lastIndexno = SharedPrefsUtils.getIntegerFromPrefs(ZoneConstants.PREFS_POPUP, "lastIndexno");
			int lastDate = SharedPrefsUtils.getIntegerFromPrefs(ZoneConstants.PREFS_POPUP, "lastDate");
			int lastMonth = SharedPrefsUtils.getIntegerFromPrefs(ZoneConstants.PREFS_POPUP, "lastMonth");
			
			int currentDate = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
			int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
			
			if(lastIndexno != ZonecommsApplication.startupInfo.getPopup().getNotice_nid() && 
					lastDate != currentDate && lastMonth != currentMonth ) {
				swipeLayout.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						showNoticePopup(ZonecommsApplication.startupInfo.getPopup());
					}
				}, 1000);
			}
		}
	}

	public void addNoticePopup() {
		
		noticePopup = new NoticePopup(this);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, noticePopup, 2, 0, null);
		((FrameLayout) findViewById(R.id.circleMainActivity_mainLayout)).addView(noticePopup);
	}
	
	public void showNoticePopup(Popup popup) {
		
		if(noticePopup == null) {
			addNoticePopup();
		}
		
		noticePopup.show(popup);
	}

	public void addProfilePopup() {

		profilePopup = new ProfilePopup(this);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, profilePopup, 2, 0, null);
		((FrameLayout) findViewById(R.id.circleMainActivity_mainLayout)).addView(profilePopup);
	}
	
	public void showProfilePopup(String userId, int status) {
		
		if(status == -1 || status == -9) {
			ToastUtils.showToast(R.string.withdrawnMember);
			return;
		}
		
		if(profilePopup == null) {
			addProfilePopup();
		}
		
		profilePopup.show(userId);
	}

	public void checkLoginAndExecute(final OnAfterLoginListener listener) {

		if(ZonecommsApplication.myInfo == null) {
			DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {

					onAfterLoginListener = listener;
					launchToSignInActivity();
				}
			};
			showAlertDialog(R.string.signIn, R.string.needSignIn, R.string.confirm, R.string.cancel, ocl, null);
		} else {
			listener.onAfterLogin();
		}
	}
	
	public void launchToSignInActivity() {

		Intent intent = new Intent(this, SignInActivity.class);
		startActivityForResult(intent, ZoneConstants.REQUEST_SIGN);
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}

	public void showAlertDialog(int title, int message, int positive,
			int negative, 
			DialogInterface.OnClickListener onPositive, 
			DialogInterface.OnClickListener onNegative) {

		String titleString = null;
		String messageString = null;
		String positiveString = null;
		String negativeString = null;
		
		if(title != 0) {
			titleString = getString(title);
		}
		
		if(message != 0) {
			messageString = getString(message);
		}
		
		if(positive != 0) {
			positiveString = getString(positive);
		}
		
		if(negative != 0) {
			negativeString = getString(negative);
		}
		
		showAlertDialog(titleString, messageString, positiveString, negativeString, 
				onPositive, null);
	}
	
	public void showAlertDialog(String title, String message, String positive,
			String negative, 
			DialogInterface.OnClickListener onPositive, 
			DialogInterface.OnClickListener onNegative) {

		try {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle(title);
			adb.setPositiveButton(positive, onPositive);
			
			if(negative != null) {
				adb.setNegativeButton(negative, onNegative);
			}
			adb.setCancelable(true);
			adb.setOnCancelListener(null);
			adb.setMessage(message);
			adb.show();
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
	public void finish() {
		super.finish();
		
		if(ZonecommsApplication.getActivity() != null) {
			ZonecommsApplication.getActivity().finish();
			ZonecommsApplication.setActivity(null);
		}
		
		ZonecommsApplication.setCircleMainActivity(null);
	}

	public void handleIntent() {

		try {
			final Intent intent = getIntent();				//'i' is intent that passed intent from before.
			
			if(intent!= null && intent.getData() != null) {
				
				Uri uri = intent.getData();
				LogUtils.log("###CircleMainActivity.onCreate.handleUri  url : " + uri);

				String uriString = uri.toString();
				
				if(uriString.contains("null://")) {
					uriString.replace("null://", ZoneConstants.PAPP_ID + "://");
					uri = Uri.parse(uriString);
					
					LogUtils.log("###CircleMainActivity.onCreate.handleUri.changeUri.  url : " + uri);
				}
				
				if(!intent.getData().getScheme().equals("popup")) {
					swipeLayout.post(new Runnable() {
	    				
	    				@Override
	    				public void run() {
	    					
	    					launchToMainActivity(intent.getData());
	    				}
	    			});
				} else {
					String message = intent.getData().getQueryParameter("message");
					message = URLDecoder.decode(message, "utf-8");
					
					showAlertDialog(null, message, getString(R.string.confirm), null, null, null);
				}
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public void checkGCM() {
		
		try {
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			final String regId = GCMRegistrar.getRegistrationId(this);

			if(regId == null || regId.equals("")) {
				GCMRegistrar.register(this, ZoneConstants.GCM_SENDER_ID);
			} else {
				updateInfo(regId);
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void updateInfo(String regId) {
		
		String url = ZoneConstants.BASE_URL + "push/androiddevicetoken" +
				"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
				"&registration_id=" + regId;
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {
				
				LogUtils.log("MainActivity.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("MainActivity.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void showDownloadPage(String uri, String packageName) {
		
		try {
			if(uri == null) {
				ToastUtils.showToast(R.string.invalidUri);
			} else if(uri.contains("http://") || uri.contains("https://")) {
				IntentHandlerActivity.actionByUri(Uri.parse(uri));
			} else if(uri.contains("market://")) {
				// 마켓으로 연결
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(uri));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			} else if(uri.contains("tstore://")) {
				
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(uri));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				
				if(getPackageManager().queryIntentActivities(intent, 
						PackageManager.MATCH_DEFAULT_ONLY).size() > 0 ) {
					startActivity(intent);
				} else {
					ToastUtils.showToast(getResources().getString(R.string.thereIsNoTStore));
					Intent i = new Intent(Intent.ACTION_VIEW);
		    		i.setData(Uri.parse("http://m.tstore.co.kr"));
		    		startActivity(i);
				}
			} else {
				ToastUtils.showToast(R.string.invalidUri);
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void showDeviceBrowser(String url) {
		
		if(url == null || url.equals("")) {
			ToastUtils.showToast(R.string.failToLoadWebBrowser);
		} else {
			try {
				Uri uri = Uri.parse(url);
			    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			    startActivity(intent);
			} catch(Exception e) {
				LogUtils.trace(e);
				ToastUtils.showToast(R.string.failToLoadWebBrowser);
			}
		}
	}
	
	public void showImageViewerActivity(String title, String[] imageUrls, String [] thumbnailUrls, int index) {
		
		if(imageUrls == null || imageUrls.length == 0) {
			return;
		}
		
		Intent intent = new Intent(this, ImageViewer.class);

		if(!StringUtils.isEmpty(title)) {
			intent.putExtra("title", title);
		}
		
		if(imageUrls != null && imageUrls.length != 0) {
			intent.putExtra("imageUrls", imageUrls);
		}
		
		if(thumbnailUrls != null && thumbnailUrls.length != 0) {
			intent.putExtra("thumbnailUrls", thumbnailUrls);
		}

		intent.putExtra("index", index);
		
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_from_bottom, android.R.anim.fade_out);
	}

	public void checkNApp(final String uriString) {
		
		final String packageName = "com.zonecomms.napp";
		final boolean installed = PackageUtils.checkApplicationInstalled(this, packageName);

		String title = "N";
		String message = "";
		DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				if(installed) {
					String id = SharedPrefsUtils.getStringFromPrefs(ZoneConstants.PREFS_SIGN, "id");
					String pw = SharedPrefsUtils.getStringFromPrefs(ZoneConstants.PREFS_SIGN, "pw");
					
					if(!StringUtils.isEmpty("id") && !StringUtils.isEmpty("pw")) {
						IntentUtils.invokeApp(context, packageName, id, pw, uriString);
					} else{
						IntentUtils.invokeApp(context, packageName, uriString);
					}
				} else {
					IntentUtils.showMarket(context, packageName);
				}
				
			}
		};
		
		if(installed) {
			message = getString(R.string.wannaLaunchNApp);
		} else {
			message = getString(R.string.wannaMoveToStore);
		}
		
		showAlertDialog(title, message, getString(R.string.confirm), getString(R.string.cancel), ocl, null);
	}
}

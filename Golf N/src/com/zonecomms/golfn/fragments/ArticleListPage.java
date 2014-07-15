package com.zonecomms.golfn.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.wheel.ArrayWheelAdapter;
import com.wheel.OnWheelClickedListener;
import com.wheel.WheelView;
import com.zonecomms.common.adapters.ListAdapter;
import com.zonecomms.common.models.Article;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.golfn.R;
import com.zonecomms.golfn.classes.BaseListFragment;
import com.zonecomms.golfn.classes.ZoneConstants;

public class ArticleListPage extends BaseListFragment {

	private PullToRefreshListView listView;
	private TextView tvCategory;
	private View cover;
	private RelativeLayout wheelRelative;
	private WheelView wheel;
	private ArrayWheelAdapter<String> adapter;
	private TextView btnConfirm;
	
	private TranslateAnimation taUp, taDown;
	private AlphaAnimation aaIn, aaOut;
	
	private int l_cate_id;
	private int[] m_cate_ids;
	private String[] wheelItems;
	private int menuIndex;

	@Override
	protected void bindViews() {
		listView = (PullToRefreshListView) mThisView.findViewById(R.id.articleListPage_pullToRefreshView);
	}

	@Override
	protected void setVariables() {
		
		if(getArguments() !=  null) {
			
			if(getArguments().containsKey("l_cate_id")) {
				l_cate_id = getArguments().getInt("l_cate_id");
			}
			
			if(getArguments().containsKey("menuIndex")) {
				menuIndex = getArguments().getInt("menuIndex");
			}
		}
	}

	@Override
	protected void createPage() {

		ListAdapter listAdapter = new ListAdapter(mContext, models, false);
		listView.setAdapter(listAdapter);
		listView.setBackgroundColor(Color.BLACK);
		listView.getRefreshableView().setDividerHeight(0);
		listView.getRefreshableView().setCacheColorHint(Color.argb(0, 0, 0, 0));
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				
				onRefreshPage();
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(visibleItemCount < totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount) {
					loadArticles();
				}
			}
		});
		zoneAdapter = listAdapter;
	}

	@Override
	protected void setListeners() {
		
	}

	@Override
	protected void setSizes() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void downloadInfo() {

		loadCategory();
	}

	@Override
	protected void setPage(boolean successDownload) {

		if(isRefreshing && listView != null) {
			listView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					listView.onRefreshComplete();					
				}
			}, 500);
		}
		
		super.setPage(successDownload);
		
		if(models != null && models.size() < MAX_LOADING_COUNT) {
			isLastList = true;
		}
	}

	@Override
	public void onRefreshPage() {

		super.onRefreshPage();
		loadArticles();
	}
	
	@Override
	protected int getContentViewId() {

		return R.id.listPage_pullToRefreshView;
	}

	@Override
	public boolean onBackKeyPressed() {
		
		if(wheelRelative != null && wheelRelative.getVisibility() == View.VISIBLE) {
			hideWheelRelative();
			return true;
		}
		
		return false;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if(!hidden) {
			mActivity.getTitleBar().showHomeButton();
			mActivity.getTitleBar().hideWriteButton();
			
			if(mActivity.getSponserBanner() != null) {
				mActivity.getSponserBanner().downloadBanner();
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

	@Override
	protected String generateDownloadKey() {
		return "ARTICLELISTPAGE" + madeCount;
	}

	@Override
	protected int getXmlResId() {
		return R.layout.page_articlelist;
	}

///////////////////////////// Custom methods.
	
	public void loadCategory(){
		
		if(m_cate_ids != null) {
			loadArticles();
		}
		
		try {
			mActivity.showLoadingView();
			
			url = ZoneConstants.BASE_URL + "common/cate_list_all?l_cate_id=" + l_cate_id;
			
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					LogUtils.log("ArticleListPage.loadCategory.onError.\nurl : " + url);
					loadArticles();
				}
				
				@Override
				public void onCompleted(String url, String result) {
					
					try {
						LogUtils.log("ArticleListPage.loadCategory.onCompleted.\nurl : " + url + "\nresult : " + result);

						JSONObject objJSON = new JSONObject(result);
						JSONArray arJSON = objJSON.getJSONArray("data");
						int length = arJSON.length();
						wheelItems = new String[length];
						m_cate_ids = new int[length];
						if(length > 0) {
							for(int i=0; i<length; i++) {
								try {
									JSONObject objCategory = arJSON.getJSONObject(i);
									
									if(i != 0 && "1".equals(objCategory.getString("idx"))) {
										pushItems(i);
										wheelItems[0] = objCategory.getString("m_cate_nm");
										m_cate_ids[0] = objCategory.getInt("m_cate_id");
									} else {
										wheelItems[i] = objCategory.getString("m_cate_nm");
										m_cate_ids[i] = objCategory.getInt("m_cate_id");
									}
								} catch(Exception e) {
								}
							}
							
							if(length > 1) {
								AddCategoryMenu();
								addWheel();
								wheel.setCurrentItem(menuIndex);
								tvCategory.setText(wheelItems[wheel.getCurrentItem()]);
							}
						}
					} catch(Exception e) {
						LogUtils.trace(e);
					}

					loadArticles();
				}

				private void pushItems(int index) {

					for(int i=index; i>0; i++) {
						wheelItems[i] = wheelItems[i-1];
						m_cate_ids[i] = m_cate_ids[i-1];
					}
				}
			};
			AsyncStringDownloader.download(url, getDownloadKey(), ocl);
		} catch (Exception e) {
			LogUtils.trace(e);
			loadArticles();
		} catch (Error e) {
			LogUtils.trace(e);
			loadArticles();
		}
	}

	public void loadArticles() {
		
		if(isDownloading || isLastList) {
			return;
		}

		if(m_cate_ids == null || (wheel != null && wheel.getCurrentItem() >= m_cate_ids.length)) {
			return;
		}
		
		url = ZoneConstants.BASE_URL + "newSpot/list" +
				"?m_cate_id=" + + m_cate_ids[(wheel == null ? 0 : wheel.getCurrentItem())] +
				"&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID) +
				"&last_spot_nid=" + lastIndexno;
		
		if(!StringUtils.isEmpty(url)) {
			super.downloadInfo();
			
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					
					LogUtils.log("ArticleListPage.loadArticles.onError  url : " + url);
					setPage(false);
				}
				
				@Override
				public void onCompleted(String url, String result) {
					
					try {
						LogUtils.log("ArticleListPage.loadArticles.onCompleted.\nurl : " + url + "\nresult : " + result);
						
						JSONObject objJSON = new JSONObject(result);
						JSONArray arJSON = objJSON.getJSONArray("data");
						int length = arJSON.length();
						
						if(length > 0) {
							for(int i=0; i<length; i++) {
								try {
									Article article = new Article(arJSON.getJSONObject(i));
									article.setItemCode(ZoneConstants.ITEM_ARTICLE);
									models.add(article);
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
	
	public void AddCategoryMenu() {

		FrameLayout categoryMenu = new FrameLayout(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 80, categoryMenu, 2, 0, null);
		categoryMenu.setBackgroundColor(Color.rgb(43, 57, 79));
		((FrameLayout)mThisView.findViewById(R.id.articleListPage_mainLayout)).addView(categoryMenu);
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, listView, 2, Gravity.LEFT, new int[]{0, 80, 0, 0});

		tvCategory = new TextView(mContext);
		ResizeUtils.viewResize(400, 50, tvCategory, 2, Gravity.CENTER, null);
		tvCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				showWheelRelative();
			}
		});
		tvCategory.setTextColor(Color.argb(255, 50, 50, 50));
		tvCategory.setGravity(Gravity.CENTER);
		tvCategory.setBackgroundResource(R.drawable.btn_category);
		tvCategory.setText("Loading...");
		FontInfo.setFontSize(tvCategory, 26);
		categoryMenu.addView(tvCategory);
	}
	
	public void addWheel() {

		FrameLayout mainLayout = (FrameLayout)mThisView.findViewById(R.id.articleListPage_mainLayout); 
		
		cover = new View(mContext);
		cover.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		cover.setBackgroundColor(Color.argb(176, 0, 0, 0));
		cover.setVisibility(View.INVISIBLE);
		cover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hideWheelRelative();
			}
		});
		mainLayout.addView(cover);
		
		wheelRelative = new RelativeLayout(mContext);
		wheelRelative.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		wheelRelative.setVisibility(View.INVISIBLE);
		mainLayout.addView(wheelRelative);
		
		RelativeLayout.LayoutParams rp = null;
		
		adapter = new ArrayWheelAdapter<String>(mContext, wheelItems);
		
		wheel = new WheelView(mContext);
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		wheel.setLayoutParams(rp);
		wheel.setId(madeCount + 1);
		wheel.setViewAdapter(adapter);
		wheel.setCurrentItem(0);
		wheel.setVisibleItems(Math.min(5, wheelItems.length + 1));
		wheel.addClickingListener(new OnWheelClickedListener() {
			
			@Override
			public void onItemClicked(WheelView wheel, int itemIndex) {
				wheel.setCurrentItem(itemIndex, true);
			}
		});
		wheel.setCyclic(false);
		wheel.setEnabled(true);
		wheelRelative.addView(wheel);
		
		View buttonBg = new View(mContext);
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(80));
		rp.addRule(RelativeLayout.ABOVE, madeCount + 1);
		buttonBg.setLayoutParams(rp);
		buttonBg.setBackgroundColor(Color.rgb(230, 230, 230));
		buttonBg.setClickable(true);
		wheelRelative.addView(buttonBg);
		
		btnConfirm = new TextView(mContext);
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ResizeUtils.getSpecificLength(80));
		rp.addRule(RelativeLayout.ABOVE, madeCount + 1);
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rp.rightMargin = ResizeUtils.getSpecificLength(8);
		btnConfirm.setLayoutParams(rp);
		btnConfirm.setText(R.string.confirm);
		btnConfirm.setTextColor(Color.BLACK);
		FontInfo.setFontSize(btnConfirm, 30);
		btnConfirm.setGravity(Gravity.CENTER);
		btnConfirm.setOnTouchListener(new OnTouchListener() {
			
			@SuppressLint("ClickableViewAccessibility") @Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					btnConfirm.setTextColor(Color.GRAY);
					break;
					
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					btnConfirm.setTextColor(Color.BLACK);
					break;
				}
				return false;
			}
		});
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hideWheelRelative();
				wheel.postDelayed(new Runnable() {

					@Override
					public void run() {

						try {
							String selectedItem = adapter.getItemText(wheel.getCurrentItem()).toString();
							tvCategory.setText(selectedItem);
							mActivity.showLoadingView();
							onRefreshPage();
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (Error e) {
							LogUtils.trace(e);
						}
					}
				}, 300);
			}
		});
		wheelRelative.addView(btnConfirm);
	}
	
	public void showWheelRelative() {
		
		if(wheel == null) {
			return;
		}
		
		if(cover.getVisibility() != View.VISIBLE) {
			
			if(aaIn == null) {
				aaIn = new AlphaAnimation(0, 1);
				aaIn.setDuration(300);
			}
			
			cover.setVisibility(View.VISIBLE);
			cover.startAnimation(aaIn);
		}
		
		if(wheelRelative.getVisibility() != View.VISIBLE) {
			
			if(taUp == null) {
				taUp = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0, 
						TranslateAnimation.RELATIVE_TO_SELF, 0, 
						TranslateAnimation.RELATIVE_TO_SELF, 1,
						TranslateAnimation.RELATIVE_TO_SELF, 0);
				taUp.setDuration(300);
			}
			
			wheelRelative.setVisibility(View.VISIBLE);
			wheelRelative.startAnimation(taUp);
		}
	}
	
	public void hideWheelRelative() {
		
		if(wheel == null) {
			return;
		}
		
		if(cover.getVisibility() == View.VISIBLE) {
			
			if(aaOut == null) {
				aaOut = new AlphaAnimation(1, 0);
				aaOut.setDuration(300);
			}
			
			cover.setVisibility(View.INVISIBLE);
			cover.startAnimation(aaOut);
		}
		
		if(wheelRelative.getVisibility() == View.VISIBLE) {
			
			if(taDown == null) {
				taDown = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF, 0, 
						TranslateAnimation.RELATIVE_TO_SELF, 0, 
						TranslateAnimation.RELATIVE_TO_SELF, 0,
						TranslateAnimation.RELATIVE_TO_SELF, 1);
				taDown.setDuration(300);
			}
			
			wheelRelative.setVisibility(View.INVISIBLE);
			wheelRelative.startAnimation(taDown);
		}
	}
}

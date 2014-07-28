package com.zonecomms.clubcage.fragments;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.outspoken_kid.model.FontInfo;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.views.holo_dark.HoloStyleEditText;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.BaseListFragment;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.common.adapters.GridAdapter;
import com.zonecomms.common.models.Link;
import com.zonecomms.common.models.Member;
import com.zonecomms.common.models.Notice;
import com.zonecomms.common.models.Post;
import com.zonecomms.common.utils.AppInfoUtils;

public class GridPage extends BaseListFragment {
	
	private boolean isAnimating;

	private int numOfColumn;
	private int menuIndex;
	private int boardIndex;		// 1:왁자지껄, 2:생생후기, 3:함께가기, 4:공개수배

	private SwipeRefreshLayout swipeRefreshLayout;
	private GridView gridView;
	private HoloStyleEditText editText;
	private TextView[] menus;
	
	private AsyncSearchTask currentTask;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(container == null) {
			return null;
		}
	
		mThisView = inflater.inflate(R.layout.page_grid, null);
		return mThisView;
	}
	
	@Override
	protected void bindViews() {
		swipeRefreshLayout = (SwipeRefreshLayout) mThisView.findViewById(R.id.gridPage_swipeRefreshLayout);
		gridView = (GridView) mThisView.findViewById(R.id.gridPage_gridView);
	}

	@Override
	protected void setVariables() {

		numOfColumn = getArguments().getInt("numOfColumn");
		boardIndex = getArguments().getInt("boardIndex");
		
		//1. 왁자지껄, 2.생생후기, 3.함께가기, 4.공개수배
		switch(boardIndex) {
		
		case 1:
			title = getString(R.string.board_story);
			break;
		case 2:
			title = getString(R.string.board_review);
			break;
		case 3:
			title = getString(R.string.board_with);
			break;
		case 4:
			title = getString(R.string.board_findPeople);
			break;
		}
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD) @Override
	protected void createPage() {

		if(numOfColumn == 0) {
			numOfColumn = 2;
		}

		if(title.equals("MEMBER")) {
			addMenuForPeople();
			addSearchBar();
		}

		swipeRefreshLayout.setColorSchemeColors(
        		Color.argb(255, 255, 102, 153), 
        		Color.argb(255, 255, 153, 153), 
        		Color.argb(255, 255, 204, 153), 
        		Color.argb(255, 255, 255, 153));
        swipeRefreshLayout.setEnabled(true);
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {

				swipeRefreshLayout.setRefreshing(true);
				onRefreshPage();
			}
		});
		
		GridAdapter gridAdapter = new GridAdapter(mContext, mActivity, models, false);
		gridView.setAdapter(gridAdapter);
		gridView.setNumColumns(numOfColumn);
		gridView.setPadding(0, ResizeUtils.getSpecificLength(8), 0, 0);
		gridView.setSelector(R.drawable.list_selector);
		
	    if (android.os.Build.VERSION.SDK_INT >= 9) {
	    	gridView.setOverScrollMode(GridView.OVER_SCROLL_NEVER);
	    }

	    gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(visibleItemCount < totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}
			}
		});
		targetAdapter = gridAdapter;
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setSizes() {
	}

	@Override
	protected void downloadInfo() {

		if(isDownloading || isLastList) {
			return;
		}
		
		url = null;

		if(boardIndex != 0) {
			url = ZoneConstants.BASE_URL + "sb/partner_spot_list"
					+ "?board_nid=" + boardIndex
					+ "&last_sb_spot_nid=" + lastIndexno
					+ "&image_size=" + ResizeUtils.getSpecificLength(308);;
		} else if(title.equals("MEMBER")) {
			
			String keyword = "";
			
			if(editText != null && editText.getEditText().getText() != null 
					&& !StringUtils.isEmpty(editText.getEditText().getText().toString())) {
				try {
					keyword = URLEncoder.encode(editText.getEditText().getText().toString(), "UTF-8");
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			}
			
			url = ZoneConstants.BASE_URL + "sb/member_list"
					+ "?status=1"
					+ "&max_sb_member_nid=" + lastIndexno
					+ "&image_size=" + ResizeUtils.getSpecificLength(150)
					+ "&keyword=" + keyword;
			
			switch(menuIndex) {
			
			case 0:
				url += "&gender=";
				break;
				
			case 1:
				url += "&gender=f";
				break;
				
			case 2:
				url += "&gender=m";
				break;
				
			case 3:
				break;
			}
			
		} else if(title.equals("SCHEDULE")){
			url = ZoneConstants.BASE_URL + "notice/list" 
					+ "?notice_type=3"
					+ "&last_notice_nid=" + lastIndexno
					+ "&image_size=" + ResizeUtils.getSpecificLength(640);
		} else if(title.equals("PHOTO")) {
			url = ZoneConstants.BASE_URL + "link/list"
					+ "?link_type=1"
					+ "&last_link_nid=" + lastIndexno
					+ "&image_size=" + ResizeUtils.getSpecificLength(640);
		}

		if(!StringUtils.isEmpty(url)) {
			super.downloadInfo();
			
			
			url += "&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID);
			
			DownloadUtils.downloadString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {
					
					setPage(false);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("GridPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						JSONArray arJSON = objJSON.getJSONArray("data");
						int length = arJSON.length();
						
						if(length > 0) {
							for(int i=0; i<length; i++) {
								try {
									if(boardIndex != 0) {
										Post post = new Post(arJSON.getJSONObject(i));
										post.setItemCode(ZoneConstants.ITEM_POST);
										models.add(post);
									} else if(title.equals("MEMBER")) {
										Member member = new Member(arJSON.getJSONObject(i));
										member.setItemCode(ZoneConstants.ITEM_USER);
										models.add(member);
									} else if(title.equals("SCHEDULE")){
										Notice notice = new Notice(arJSON.getJSONObject(i));
										notice.setItemCode(ZoneConstants.ITEM_SCHEDULE);
										models.add(notice);
									} else if(title.equals("PHOTO")) {
										Link link = new Link(arJSON.getJSONObject(i));
										link.setItemCode(ZoneConstants.ITEM_PHOTO);
										models.add(link);
									}
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
	}

	@Override
	protected void setPage(boolean successDownload) {

		if(isRefreshing && gridView != null) {
			
			gridView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					
					swipeRefreshLayout.setRefreshing(false);
				}
			}, 1000);
		}
		
		super.setPage(successDownload);
	}

	@Override
	public void onRefreshPage() {

		super.onRefreshPage();
		downloadInfo();
	}
	
	@Override
	protected int getContentViewId() {

		return R.id.gridPage_mainLayout;
	}

	@Override
	public boolean onBackKeyPressed() {

		return false;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		
		if(!hidden) {
			mActivity.getTitleBar().showHomeButton();
			
			if(boardIndex != 0) {
				mActivity.getTitleBar().showWriteButton();
			} else {
				mActivity.getTitleBar().hideWriteButton();
			}

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
	
///////////////////////// Custom methods.
	
	public void addMenuForPeople() {
		
		RelativeLayout.LayoutParams rp = null;
		int length = ResizeUtils.getScreenWidth()/4;
		int p = ResizeUtils.getSpecificLength(8);
		
		LinearLayout linear = new LinearLayout(mContext);
		
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		linear.setLayoutParams(rp);
		linear.setOrientation(LinearLayout.HORIZONTAL);
		((RelativeLayout)mThisView.findViewById(R.id.gridPage_mainLayout)).addView(linear);

		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		rp.topMargin = length / 2;
		swipeRefreshLayout.setLayoutParams(rp);
		
		menus = new TextView[4];
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(length, length);
		
		for(int i=0; i<4; i++) {
			
			FrameLayout frame = new FrameLayout(mContext);
			frame.setLayoutParams(lp);
			
			if(i % 4 == 0) {
				frame.setPadding(p, p, p/2, 0);
			} else if(i % 4 != 3) {
				frame.setPadding(p/2, p, p/2, 0);
			} else {
				frame.setPadding(p/2, p, p, 0);
			}
			
			linear.addView(frame);
			
			TextView v = new TextView(mContext);
			v.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			v.setBackgroundColor(i==0?Color.BLACK:Color.rgb(55, 55, 55));
			v.setTextColor(Color.WHITE);
			v.setGravity(Gravity.CENTER);
			v.setPadding(0, 0, 0, 0);
			FontInfo.setFontStyle(v, FontInfo.BOLD);
			FontInfo.setFontSize(v, 26);
			frame.addView(v);

			menus[i] = v;
			
			final int I = i;
			v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				
					menus[menuIndex].setBackgroundColor(Color.rgb(55, 55, 55));
					menuIndex = I;
					menus[menuIndex].setBackgroundColor(Color.BLACK);	
					
					if(I != 3) {
						hideSearchBar();
						
						isLastList = false;
						isDownloading = false;
						isRefreshing = false;
						lastIndexno = 0;
						models.clear();
						targetAdapter.notifyDataSetChanged();
						((GridAdapter)targetAdapter).clearHardCache();
						downloadInfo();
					} else {
						showSearchBar();
					}
				}
			});
			
			switch(i) {
			
			case 0:
				v.setText(R.string.all);
				break;
				
			case 1:
				v.setText(R.string.female);
				break;
				
			case 2:
				v.setText(R.string.male);
				break;
				
			case 3:
				v.setText(R.string.search);
				break;
			}
		}
	}

	public void addSearchBar() {
		
		int m = ResizeUtils.getSpecificLength(8);
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				ResizeUtils.getSpecificLength(70));
		rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		rp.leftMargin = m;
		rp.topMargin = ResizeUtils.getSpecificLength(80);
		rp.rightMargin = m;
		
		editText = new HoloStyleEditText(mContext);
		editText.setLayoutParams(rp);
		editText.getEditText().setSingleLine();
		editText.setVisibility(View.INVISIBLE);
		FontInfo.setFontSize(editText.getEditText(), 20);
		editText.getEditText().addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(StringUtils.isEmpty(editText.getEditText().getText())) {
					
					if(editText.getVisibility() == View.VISIBLE) {
						isLastList = false;
						isRefreshing = true;
						lastIndexno = 0;
						models.clear();
						targetAdapter.notifyDataSetChanged();
						((GridAdapter)targetAdapter).clearHardCache();
					}
				} else {
					AsyncSearchTask ast = new AsyncSearchTask();
					
					if(currentTask != null) {
						currentTask.cancel(true);
						currentTask = null;
					}
					
					currentTask = ast;
					ast.execute();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		((RelativeLayout)mThisView.findViewById(R.id.gridPage_mainLayout)).addView(editText);
	}
	
	public void showSearchBar() {
		
		if(!isAnimating && editText.getVisibility() != View.VISIBLE) {
			isAnimating = true;

			float animOffset = (float)ResizeUtils.getSpecificLength(70) / (float)gridView.getMeasuredHeight();

			TranslateAnimation taDown = new TranslateAnimation(
					TranslateAnimation.RELATIVE_TO_SELF, 0, 
					TranslateAnimation.RELATIVE_TO_SELF, 0,
					TranslateAnimation.RELATIVE_TO_SELF, 0,
					TranslateAnimation.RELATIVE_TO_SELF, animOffset);
			taDown.setDuration(300);
			taDown.setFillAfter(true);
			taDown.setInterpolator(mContext, android.R.anim.accelerate_decelerate_interpolator);
			taDown.setAnimationListener(new AnimationListener() {
				
				@Override
				public void onAnimationStart(Animation animation) {
				}
				
				@Override
				public void onAnimationRepeat(Animation animation) {
				}
				
				@Override
				public void onAnimationEnd(Animation animation) {
					isAnimating = false;
					
					AlphaAnimation aaIn = new AlphaAnimation(0, 1);
					aaIn.setDuration(300);
					editText.requestFocus();
					editText.setVisibility(View.VISIBLE);
					editText.startAnimation(aaIn);
					
					gridView.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							gridView.clearAnimation();
							ResizeUtils.setMargin(gridView, new int[]{0, 70, 0, 0});
							
							isLastList = false;
							isRefreshing = true;
							lastIndexno = 0;
							models.clear();
							targetAdapter.notifyDataSetChanged();
							((GridAdapter)targetAdapter).clearHardCache();
						}
					}, 500);
				}
			});
			gridView.scrollTo(0, 0);
			gridView.startAnimation(taDown);
			
			gridView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.toggleSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
				}
			}, 1000);
		}
	}
	
	public void hideSearchBar() {
		
		if(!isAnimating && editText != null && editText.getVisibility() == View.VISIBLE) {
			isAnimating = true;
			editText.getEditText().setText("");
			
			AlphaAnimation aaOut = new AlphaAnimation(1, 0);
			aaOut.setDuration(300);
			editText.startAnimation(aaOut);
			editText.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					isAnimating = false;
					editText.setVisibility(View.INVISIBLE);
					
					float animOffset = (float)ResizeUtils.getSpecificLength(70) / (float)gridView.getMeasuredHeight();
					
					TranslateAnimation taUp = new TranslateAnimation(
							TranslateAnimation.RELATIVE_TO_SELF, 0, 
							TranslateAnimation.RELATIVE_TO_SELF, 0,
							TranslateAnimation.RELATIVE_TO_SELF, animOffset,
							TranslateAnimation.RELATIVE_TO_SELF, 0);
					taUp.setDuration(300);
					taUp.setInterpolator(mContext, android.R.anim.accelerate_decelerate_interpolator);
					ResizeUtils.setMargin(gridView, new int[]{0, 0, 0, 0});
					gridView.startAnimation(taUp);
					
					mActivity.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							SoftKeyboardUtils.hideKeyboard(mContext, editText);
						}
					});
				}
			}, 300);
		}
	}
	
	public int getBoardIndex() {
		
		return boardIndex;
	}

//////////////////////// Classes.
	
	public class AsyncSearchTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			
			mActivity.showLoadingView();
			ToastUtils.showToast(R.string.searching);
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
			onRefreshPage();
		}
	}
}

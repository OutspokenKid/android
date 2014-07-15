package com.zonecomms.golfn.fragments;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
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
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleEditText;
import com.zonecomms.common.adapters.GridAdapter;
import com.zonecomms.common.models.Member;
import com.zonecomms.common.models.Post;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.golfn.MainActivity;
import com.zonecomms.golfn.R;
import com.zonecomms.golfn.classes.BaseListFragment;
import com.zonecomms.golfn.classes.ZoneConstants;

public class GridPage extends BaseListFragment {

	private PullToRefreshGridView gridView;
	private HoloStyleEditText editText;

	private int numOfColumn;
	private String concern_kind;
	private int s_cate_id;
	private boolean isAnimating;
	private int menuIndex;
	
	private TextView[] menus;
	
	private AsyncSearchTask currentTask;
	
	@Override
	protected void bindViews() {
		gridView = (PullToRefreshGridView) mThisView.findViewById(R.id.gridPage_pullToRefreshView);
	}

	@Override
	protected void setVariables() {

		if(getArguments() != null) {
			numOfColumn = getArguments().getInt("numOfColumn");
			concern_kind = getArguments().getString("concern_kind");
			s_cate_id = getArguments().getInt("s_cate_id");
		}
	}

	@SuppressLint("InlinedApi")
	@Override
	protected void createPage() {

		if(numOfColumn == 0) {
			numOfColumn = 2;
		}
		
		if(type == ZoneConstants.TYPE_MEMBER) {
			addMenuForPeople();
			addSearchBar();
		}
		
		GridAdapter gridAdapter = new GridAdapter(mContext, models, false);
		gridView.setAdapter(gridAdapter);
		gridView.getRefreshableView().setNumColumns(numOfColumn);
		gridView.getRefreshableView().setPadding(0, ResizeUtils.getSpecificLength(8), 0, 0);
		gridView.getRefreshableView().setSelector(R.drawable.list_selector);
		
	    if (android.os.Build.VERSION.SDK_INT >= 9) {
	    	gridView.setOverScrollMode(GridView.OVER_SCROLL_NEVER);
	    }
		
		gridView.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				onRefreshPage();
			}
		});
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
		zoneAdapter = gridAdapter;
	}

	@Override
	protected void setListeners() {
	}

	@Override
	protected void setSizes() {
	}

	@Override
	protected void downloadInfo() {

		if(isDownloading || isLastList) {
			return;
		}
		
		url = ZoneConstants.BASE_URL;

		switch(type) {
			
		case ZoneConstants.TYPE_MARKET1:
			url += "sb/partner_spot_list" +
					"?image_size=308" +
					"&board_nid=5" +
					"&last_sb_spot_nid=" + lastIndexno;
			break;
			
		case ZoneConstants.TYPE_MARKET2:
			url += "sb/partner_spot_list" +
					"?image_size=308" +
					"&board_nid=6" +
					"&last_sb_spot_nid=" + lastIndexno;
			break;
			
		case ZoneConstants.TYPE_MARKET3:
			url += "sb/partner_spot_list" +
					"?image_size=308" +
					"&board_nid=7" +
					"&last_sb_spot_nid=" + lastIndexno;
			break;
			
		case ZoneConstants.TYPE_MARKET4:
			url += "sb/partner_spot_list" +
					"?image_size=308" +
					"&board_nid=8" +
					"&last_sb_spot_nid=" + lastIndexno;
			break;
			
		case ZoneConstants.TYPE_ATTENDANCE:
			url += "sb/partner_spot_list" +
					"?image_size=308" +
					"&board_nid=9" +
					"&last_sb_spot_nid=" + lastIndexno;
			break;
			
		case ZoneConstants.TYPE_REVIEW:
			url += "sb/partner_spot_list" +
					"?image_size=308" +
					"&board_nid=10" +
					"&last_sb_spot_nid=" + lastIndexno;
			break;
			
		case ZoneConstants.TYPE_QNA:
			url += "sb/partner_spot_list" +
					"?image_size=308" +
					"&board_nid=2" +
					"&last_sb_spot_nid=" + lastIndexno;
			break;
			
		case ZoneConstants.TYPE_PHOTO:
			url += "sb/partner_spot_list" +
					"?image_size=308" +
					"&board_nid=3" +
					"&last_sb_spot_nid=" + lastIndexno;
			break;
			
		case ZoneConstants.TYPE_BANNER3:
			url += "sb/partner_spot_list" +
					"?image_size=308" +
					"&board_nid=4" +
					"&last_sb_spot_nid=" + lastIndexno;
			break;

		case ZoneConstants.TYPE_MEMBER:
			
			String keyword = "";
			
			url += "sb/member_list" +
					"?image_size=150" +
					"&max_sb_member_nid=" + lastIndexno +
					"status=1";

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
					+ "&image_size=150"
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

			break;
			
		case ZoneConstants.TYPE_POST_GETHERING:
			url +=  "boardspot/list" +
					"?image_size=308" +
					"&last_spot_nid=" + lastIndexno +
					"&member_id=" + MainActivity.myInfo.getMember_id();
			break;

		case ZoneConstants.TYPE_POST_GETHERING_INTRO:
			url += "spot/list" +
					"?concern_kind=050" +
					"&image_size=308" +
					"&last_spot_nid=" + lastIndexno;
			break;
		}
		
		url += "&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID|AppInfoUtils.WITHOUT_SB_ID);
		
		if(type != ZoneConstants.TYPE_POST_GETHERING
				&& type != ZoneConstants.TYPE_POST_GETHERING_INTRO) {
			url += "&sb_id=" + ZoneConstants.PAPP_ID;
		} else {
			url += "&origin_sb_id=" + ZoneConstants.PAPP_ID;
		}
		
		if(!StringUtils.isEmpty(url)) {
			super.downloadInfo();
			
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					LogUtils.log("GridPage.onError.  url : " + url);
					setPage(false);
				}
				
				@Override
				public void onCompleted(String url, String result) {
					
					try {
						LogUtils.log("GridPage.onCompleted.  url : " + url + "\nresult : " + result);
						
						JSONObject objJSON = new JSONObject(result);
						JSONArray arJSON = objJSON.getJSONArray("data");
						int length = arJSON.length();
						
						if(length > 0) {
							for(int i=0; i<length; i++) {
								try {
									switch(type) {
									case ZoneConstants.TYPE_MARKET1:
									case ZoneConstants.TYPE_MARKET2:
									case ZoneConstants.TYPE_MARKET3:
									case ZoneConstants.TYPE_MARKET4:
									case ZoneConstants.TYPE_ATTENDANCE:
									case ZoneConstants.TYPE_REVIEW:
									case ZoneConstants.TYPE_QNA:
									case ZoneConstants.TYPE_PHOTO:
									case ZoneConstants.TYPE_BANNER3:
									case ZoneConstants.TYPE_POST_GETHERING:
									case ZoneConstants.TYPE_POST_GETHERING_INTRO:
										Post post = new Post(arJSON.getJSONObject(i));
										post.setItemCode(ZoneConstants.ITEM_POST);
										
										if(type == ZoneConstants.TYPE_POST_GETHERING) {
											post.setFromGethering(true);
											//Set indexno to spot_nid.
										} else if(type == ZoneConstants.TYPE_POST_GETHERING_INTRO) {
											//Set indexno to spot_nid.
										} else {
											post.setIndexno(post.getSb_spot_nid());
										}
										
										models.add(post);
										break;
									case ZoneConstants.TYPE_MEMBER:
										Member member = new Member(arJSON.getJSONObject(i));
										member.setItemCode(ZoneConstants.ITEM_USER);
										models.add(member);
										break;
									}
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

	@Override
	protected void setPage(boolean successDownload) {
		
		if(isRefreshing && gridView != null) {
			gridView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					gridView.onRefreshComplete();
				}
			}, 500);
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

		return R.id.gridPage_pullToRefreshView;
	}

	@Override
	public boolean onBackKeyPressed() {

		if(editText != null 
				&& editText.getVisibility() == View.VISIBLE) {
			hideSearchBar();
			return true;
		}
		
		return false;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		
		if(!hidden) {
			mActivity.getTitleBar().showHomeButton();
			
			//타입에 따라 타이틀바 버튼 설정.
			if(type == ZoneConstants.TYPE_MARKET1
				|| type == ZoneConstants.TYPE_MARKET1
				|| type == ZoneConstants.TYPE_MARKET2
				|| type == ZoneConstants.TYPE_MARKET3
				|| type == ZoneConstants.TYPE_MARKET4
				|| type == ZoneConstants.TYPE_ATTENDANCE
				|| type == ZoneConstants.TYPE_REVIEW
				|| type == ZoneConstants.TYPE_QNA
				|| type == ZoneConstants.TYPE_PHOTO
				|| type == ZoneConstants.TYPE_BANNER3
				|| type == ZoneConstants.TYPE_POST_GETHERING_INTRO) {
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

	@Override
	protected String generateDownloadKey() {
		return "GRIDPAGE" + madeCount;
	}

	@Override
	protected int getXmlResId() {
		return R.layout.page_grid;
	}
	
///////////////////////// Custom methods.
	
	public void addMenuForPeople() {
		
		LinearLayout linear = new LinearLayout(mContext);
		linear.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		linear.setOrientation(LinearLayout.HORIZONTAL);
		((LinearLayout)mThisView.findViewById(R.id.gridPage_mainLayout)).addView(linear, 0);

		menus = new TextView[4];
		
		int length = ResizeUtils.getScreenWidth()/4;
		int p = ResizeUtils.getSpecificLength(8);
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
						
						AsyncStringDownloader.cancelAllTasksByKey(getDownloadKey());
						isLastList = false;
						isDownloading = false;
						isRefreshing = false;
						lastIndexno = 0;
						models.clear();
						zoneAdapter.notifyDataSetChanged();
						zoneAdapter.clearHardCache();
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
		
		int p = ResizeUtils.getSpecificLength(8);
		editText = new HoloStyleEditText(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 70, editText, 2, Gravity.TOP, new int[]{p, 0, p, 0});
		editText.getEditText().setSingleLine();
		editText.setVisibility(View.INVISIBLE);
		FontInfo.setFontSize(editText.getEditText(), 20);
		editText.getEditText().addTextChangedListener(new TextWatcher() {
			
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) @Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(StringUtils.isEmpty(editText.getEditText().getText())) {
					
					if(editText.getVisibility() == View.VISIBLE) {
						isLastList = false;
						isRefreshing = true;
						lastIndexno = 0;
						models.clear();
						zoneAdapter.notifyDataSetChanged();
						zoneAdapter.clearHardCache();
					}
				} else {
					AsyncSearchTask ast = new AsyncSearchTask();
					
					if(currentTask != null) {
						currentTask.cancel(true);
						currentTask = null;
					}
					
					currentTask = ast;
					
					if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
						ast.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
					} else {
						ast.execute();
					}
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		((FrameLayout)mThisView.findViewById(R.id.gridPage_mainFrame)).addView(editText);
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
							zoneAdapter.notifyDataSetChanged();
							zoneAdapter.clearHardCache();
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
	
	public String getConcern_kind() {
		
		return concern_kind;
	}

	public int getS_cate_id() {
		
		return s_cate_id;
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
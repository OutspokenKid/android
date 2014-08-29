package com.zonecomms.clubmania.fragments;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
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
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.outspoken_kid.classes.BaseListFragment;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleEditText;
import com.zonecomms.clubmania.R;
import com.zonecomms.clubmania.classes.ZoneConstants;
import com.zonecomms.common.adapters.GridAdapter;
import com.zonecomms.common.models.Link;
import com.zonecomms.common.models.Member;
import com.zonecomms.common.models.Notice;
import com.zonecomms.common.models.Papp;
import com.zonecomms.common.models.Post;
import com.zonecomms.common.utils.AppInfoUtils;

public class GridPage extends BaseListFragment {

	private PullToRefreshGridView gridView;
	private HoloStyleEditText editText;
	
	private boolean isAnimating;

	private int numOfColumn;
	private int menuIndex;
	
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
		gridView = (PullToRefreshGridView) mThisView.findViewById(R.id.gridPage_pullToRefreshView);
	}

	@Override
	protected void setVariables() {

		numOfColumn = getArguments().getInt("numOfColumn");
		setDownloadKey("GRIDPAGE" + madeCount);
	}

	@Override
	protected void createPage() {

		if(numOfColumn == 0) {
			numOfColumn = 2;
		}
				
		if(type == ZoneConstants.TYPE_MEMBER) {
			addMenuForPeople();
			addSearchBar();
		} else if(type == ZoneConstants.TYPE_PAPP_PHOTO) {
			addTextView(R.string.textForPhoto);
		} else if(type == ZoneConstants.TYPE_PAPP_SCHEDULE) {
			
			String data = getArguments().getString("data");
			if(data.equals("1")) {
				addTextView(R.string.textForSchedule_club);
			} else {
				addTextView(R.string.textForSchedule_lounge);
			}
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

					if(type != ZoneConstants.TYPE_PAPP_PHOTO
							&& type != ZoneConstants.TYPE_PAPP_SCHEDULE) {
						downloadInfo();
					}
				}
			}
		});
		targetAdapter = gridAdapter;
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setSize() {
	}

	@Override
	protected void downloadInfo() {

		if(isDownloading || isLastList) {
			return;
		}
		
		url = ZoneConstants.BASE_URL;

		if(type == ZoneConstants.TYPE_POST) {
			s_cate_id = Integer.parseInt(getArguments().getString("data"));
			url += "spot/list/with_category"
					+ "?s_cate_id=" + s_cate_id
					+ "&last_spot_nid=" + lastIndexno
					+ "&image_size=308";
		} else if(type == ZoneConstants.TYPE_SCHEDULE) {
			String sb_id = getArguments().getString("data");
			url += "notice/list"
					+ "?notice_type=3"
					+ "&last_notice_nid=" + lastIndexno
					+ "&sb_id=" + sb_id
					+ "&image_size=640";
		} else if(type == ZoneConstants.TYPE_PHOTO) {
			String sb_id = getArguments().getString("data");
			url += "link/list"
					+ "?link_type=1"
					+ "&last_link_nid=" + lastIndexno
					+ "&sb_id=" + sb_id
					+ "&image_size=308";
		} else if(type == ZoneConstants.TYPE_MEMBER) {
			String keyword = "";
			if(editText != null && editText.getEditText().getText() != null 
					&& !TextUtils.isEmpty(editText.getEditText().getText().toString())) {
				try {
					keyword = URLEncoder.encode(editText.getEditText().getText().toString(), "UTF-8");
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			url += "sb/member_list"
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
			
		} else if(type == ZoneConstants.TYPE_PAPP_PHOTO) {
			url += "sb/clubmania/papplist"
					+ "?image_size=150";
			
		} else if(type == ZoneConstants.TYPE_PAPP_SCHEDULE) {
			s_cate_id = Integer.parseInt(getArguments().getString("data"));
			url += "sb/category_papp_list"
					+ "?s_cate_id=" + s_cate_id
					+ "&image_size=150";
		}

		if(!TextUtils.isEmpty(url)) {
			super.downloadInfo();
			
			if(!url.contains("sb_id")) {
				url += "&sb_id=" + ZoneConstants.PAPP_ID;
			}
			
			url += "&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID|AppInfoUtils.WITHOUT_SB_ID);
			
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					
					LogUtils.log("GridPage.onError.\nurl : " + url);
					setPage(false);
				}
				
				@Override
				public void onCompleted(String url, String result) {
					
					LogUtils.log("GridPage.onCompleted.\nurl : " + url + "\nresult : " + result);
					
					try {
						JSONObject objJSON = new JSONObject(result);
						JSONArray arJSON = objJSON.getJSONArray("data");
						int length = arJSON.length();
						
						if(length > 0) {
							for(int i=0; i<length; i++) {
								try {

									if(type == ZoneConstants.TYPE_POST) {
										Post post = new Post(arJSON.getJSONObject(i));
										post.setItemCode(ZoneConstants.ITEM_POST);
										models.add(post);
									} else if(type == ZoneConstants.TYPE_SCHEDULE) {
										Notice notice = new Notice(arJSON.getJSONObject(i));
										notice.setItemCode(ZoneConstants.ITEM_SCHEDULE);
										models.add(notice);
									} else if(type == ZoneConstants.TYPE_PHOTO) {
										Link link = new Link(arJSON.getJSONObject(i));
										link.setItemCode(ZoneConstants.ITEM_PHOTO);
										models.add(link);
									} else if(type == ZoneConstants.TYPE_MEMBER) {
										Member member = new Member(arJSON.getJSONObject(i));
										member.setItemCode(ZoneConstants.ITEM_USER);
										models.add(member);
									} else if(type == ZoneConstants.TYPE_PAPP_PHOTO) {
										Papp papp = new Papp(arJSON.getJSONObject(i));
										papp.setItemCode(ZoneConstants.ITEM_PAPP);
										papp.setType(Papp.TYPE_PHOTO);
										models.add(papp);
									} else if(type == ZoneConstants.TYPE_PAPP_SCHEDULE) {
										Papp papp = new Papp(arJSON.getJSONObject(i));
										papp.setItemCode(ZoneConstants.ITEM_PAPP);
										papp.setType(Papp.TYPE_SCHEDULE);
										models.add(papp);
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
						e.printStackTrace();
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
		
		if(models.size() == 0 && type == ZoneConstants.TYPE_PAPP_SCHEDULE) {
			ToastUtils.showToast(R.string.preparingPlusApp);
		}
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

		return false;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		
		if(!hidden) {
			mActivity.getTitleBar().showHomeButton();

			if(type == ZoneConstants.TYPE_POST) {
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

	public void addTextView(int resId) {
		
		TextView tvPosting = new TextView(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 80, tvPosting, 2, Gravity.TOP, null, new int[]{8, 8, 8, 8});
		tvPosting.setText(resId);
		tvPosting.setGravity(Gravity.CENTER);
		tvPosting.setBackgroundColor(Color.BLACK);
		tvPosting.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvPosting, 20);
		((LinearLayout)mThisView.findViewById(R.id.gridPage_mainLayout)).addView(tvPosting, 0);
	}
	
	public void addSearchBar() {
		
		int p = ResizeUtils.getSpecificLength(8);
		editText = new HoloStyleEditText(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 70, editText, 2, Gravity.TOP, new int[]{p, 0, p, 0});
		editText.getEditText().setSingleLine();
		editText.setVisibility(View.INVISIBLE);
		FontInfo.setFontSize(editText.getEditText(), 20);
		editText.getEditText().addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(TextUtils.isEmpty(editText.getEditText().getText())) {
					
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

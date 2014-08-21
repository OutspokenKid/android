package com.cmons.cph.fragments.wholesale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Notice;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;

public class WholesaleMainPage extends CmonsFragmentForWholesale {
	
	private Button btnShop;
	private Button btnNotice;
	private Button btnManagement;
	private Button btnOrder;
	private Button btnSample;
	private Button btnCustomer;
	private Button btnStaff;
	private Button btnSetting;
	private TextView tvHit;
	private TextView tvWholesale;
	private Button arrowLeft;
	private Button arrowRight;
	
	private View cover;
	private RelativeLayout noticeRelative;
	private Button btnClose;
	private ListView listView;
	
	private AlphaAnimation aaIn, aaOut;
	private boolean animating;
	
	@Override
	public void onResume() {
		super.onResume();
		downloadInfo();
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleMainPage_titleBar);
		
		btnShop = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnShop);
		btnNotice = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnNotice);
		btnManagement = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnManagement);
		btnOrder = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnOrder);
		btnSample = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnSample);
		btnCustomer = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnCustomer);
		btnStaff = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnStaff);
		btnSetting = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnSetting);
		
		tvHit = (TextView) mThisView.findViewById(R.id.wholesaleMainPage_tvHit);
		tvWholesale = (TextView) mThisView.findViewById(R.id.wholesaleMainPage_tvWholesale);
		arrowLeft = (Button) mThisView.findViewById(R.id.wholesaleMainPage_arrowLeft);
		arrowRight = (Button) mThisView.findViewById(R.id.wholesaleMainPage_arrowRight);
		
		cover = mThisView.findViewById(R.id.wholesaleMainPage_cover);
		noticeRelative = (RelativeLayout) mThisView.findViewById(R.id.wholesaleMainPage_noticeRelative);
		btnClose = (Button) mThisView.findViewById(R.id.wholesaleMainPage_btnClose);
		listView = (ListView) mThisView.findViewById(R.id.wholesaleMainPage_listView);
	}

	@Override
	public void setVariables() {

		AnimationListener al = new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {

				animating = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {

				animating = false;
			}
		};
		
		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);
		aaIn.setAnimationListener(al);
		
		aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(300);
		aaOut.setAnimationListener(al);
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.INVISIBLE);
		titleBar.getHomeButton().setVisibility(View.INVISIBLE);
		titleBar.getBtnAdd().setVisibility(View.VISIBLE);
		
		
		tvHit.setText("매장방문 200");
		
		SpannableStringBuilder sp1 = new SpannableStringBuilder("씨몬즈");
		sp1.setSpan(new RelativeSizeSpan(1.8f), 0, sp1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvWholesale.append(sp1); 
		
		SpannableStringBuilder sp2 = new SpannableStringBuilder("\n청평화 1층 특가5호");
		sp2.setSpan(new RelativeSizeSpan(1.0f), 0, sp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvWholesale.append(sp2);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
	}

	@Override
	public void setListeners() {

		titleBar.getBtnNotice()
				.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(noticeRelative.getVisibility() == View.VISIBLE) {
					hideNoticeRelative();
				} else {
					showNoticeRelative();
				}
			}
		});
		
		titleBar.getBtnAdd()
				.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showWritePage(null);
			}
		});
		
		btnShop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				mActivity.showShopPage();
			}
		});
		
		btnNotice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(noticeRelative.getVisibility() == View.VISIBLE) {
					hideNoticeRelative();
				} else {
					showNoticeRelative();
				}
			}
		});
		
		btnManagement.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showManagementPage();
			}
		});
		
		btnOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showOrderListPage();
			}
		});
		
		btnSample.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showSamplePage();
			}
		});
		
		btnCustomer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showCustomerPage();
			}
		});
		
		btnStaff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showStaffPage();
			}
		});
		
		btnSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showSettingPage();
			}
		});
		
		cover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hideNoticeRelative();
			}
		});
		
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hideNoticeRelative();
			}
		});
	
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				hideNoticeRelative();
				
				mActivity.showNoticePage((Notice)models.get(position));
			}
		});
	}

	@Override
	public void setSizes() {

		((FrameLayout.LayoutParams)mThisView.findViewById(
				R.id.wholesaleMainPage_scrollView).getLayoutParams())
				.topMargin = ResizeUtils.getSpecificLength(96); 
		
		RelativeLayout.LayoutParams rp = null;
		
		int length_short = ResizeUtils.getScreenWidth()/3;
		int length_long = length_short * 2; 
		
		//btnShop.
		rp = (RelativeLayout.LayoutParams) btnShop.getLayoutParams();
		rp.width = length_long;
		rp.height = length_long;
		
		//btnNotice.
		rp = (RelativeLayout.LayoutParams) btnNotice.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = length_short;
		
		//btnManagement.
		rp = (RelativeLayout.LayoutParams) btnManagement.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = length_short;
		
		//btnOrder.
		rp = (RelativeLayout.LayoutParams) btnOrder.getLayoutParams();
		rp.width = length_short;
		rp.height = length_short;
		
		//btnSample.
		rp = (RelativeLayout.LayoutParams) btnSample.getLayoutParams();
		rp.width = length_short;
		rp.height = length_short;
		
		//btnCustomer.
		rp = (RelativeLayout.LayoutParams) btnCustomer.getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = length_short;
		
		//btnStaff.
		rp = (RelativeLayout.LayoutParams) btnStaff.getLayoutParams();
		rp.width = length_short;
		rp.height = length_short;
		
		//btnSetting.
		rp = (RelativeLayout.LayoutParams) btnSetting.getLayoutParams();
		rp.width = length_short;
		rp.height = length_short;
		
		//hitIcon.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.wholesaleMainPage_hitIcon).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(28);
		rp.height = ResizeUtils.getSpecificLength(19);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		
		//tvHit.
		rp = (RelativeLayout.LayoutParams) tvHit.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//tvWholesale.
		rp = (RelativeLayout.LayoutParams) tvWholesale.getLayoutParams();
		rp.width = length_long;
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
		FontUtils.setFontSize(tvWholesale, 30);
		
		//arrowLeft.
		rp = (RelativeLayout.LayoutParams) arrowLeft.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(43);
		rp.height = ResizeUtils.getSpecificLength(66);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = length_short - ResizeUtils.getSpecificLength(33);
		
		//arrowRight.
		rp = (RelativeLayout.LayoutParams) arrowRight.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(43);
		rp.height = ResizeUtils.getSpecificLength(66);
		rp.rightMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = length_short - ResizeUtils.getSpecificLength(33);
		
		//cover
		((FrameLayout.LayoutParams)cover.getLayoutParams()).topMargin = ResizeUtils.getSpecificLength(96);
		
		//noticeRelative.
		ResizeUtils.viewResize(630, 711, noticeRelative, 2, Gravity.TOP|Gravity.LEFT, new int[]{62, 58, 0, 0});
		
		//btnClose.
		rp = (RelativeLayout.LayoutParams) btnClose.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(52);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(96);
		rp.rightMargin = ResizeUtils.getSpecificLength(32);
		
		//listView.
		rp = (RelativeLayout.LayoutParams) listView.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(530);
		rp.leftMargin = ResizeUtils.getSpecificLength(24);
		rp.rightMargin = ResizeUtils.getSpecificLength(24);
		rp.bottomMargin = ResizeUtils.getSpecificLength(24);
	}
	
	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_main;
	}

	@Override
	public boolean onBackPressed() {
		
		if(noticeRelative.getVisibility() == View.VISIBLE) {
			hideNoticeRelative();
			return true;
		}
		
		return false;
	}
	
	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {

		checkNewMessage();
		
		try {
			JSONArray arJSON = objJSON.getJSONArray("notices");
			
			int size = arJSON.length();
			
			for(int i=0; i<size; i++) {
				Notice notice = new Notice(arJSON.getJSONObject(i));
				notice.setItemCode(CphConstants.ITEM_NOTICE);
				models.add(new Notice());
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return false;
	}
	
	@Override
	public void downloadInfo() {
		
		url = CphConstants.BASE_API_URL + "wholesales/notices";
		
		if(isDownloading || isLastList) {
			return;
		}
		
		if(!isRefreshing) {
			showLoadingView();
		}
		
		isDownloading = true;
		
		if(url.contains("?")) {
			url += "&";
		} else {
			url += "?";
		}
		
		url += "num=0";
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CmonsFragment.onError." + "\nurl : " + url);
				setPage(false);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("CmonsFragment.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					isLastList = parseJSON(objJSON);
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
	
//////////////////// Custom methods.

	public void checkNewMessage() {
		
		int nonReadCount = models.size();
		
		String readListString = SharedPrefsUtils.getStringFromPrefs(CphConstants.PREFS_NOTICE, "readList");
		
		if(readListString != null) {
			String[] readList = (readListString).split(",");
			
			Notice notice;
			boolean isRead;
			
			for(int i=0; i<models.size(); i++) {

				notice = (Notice) models.get(i);
				isRead = false;
				
				for(String read : readList) {
					
					//읽은적 있음.
					if(read.equals("" + notice.getId())) {
						isRead = true;
						nonReadCount--;
						break;
					}
				}
				
				notice.setRead(isRead);
			}
		}
		
		//새로운 메시지가 있느냐의 여부에 따라 titleBar.getNotice() 노출 결정.
		if(nonReadCount > 0) {
			titleBar.getBtnNotice().setVisibility(View.VISIBLE);
		}
	}
	
	public void showNoticeRelative() {

		if(!animating && noticeRelative.getVisibility() != View.VISIBLE) {
			
			refreshPage();
			
			noticeRelative.setVisibility(View.VISIBLE);
			cover.setVisibility(View.VISIBLE);
			
			noticeRelative.startAnimation(aaIn);
			cover.startAnimation(aaIn);
		}
	}
	
	public void hideNoticeRelative() {

		if(!animating && noticeRelative.getVisibility() == View.VISIBLE) {
			
			noticeRelative.setVisibility(View.INVISIBLE);
			cover.setVisibility(View.INVISIBLE);
			
			noticeRelative.startAnimation(aaOut);
			cover.startAnimation(aaOut);
		}
	}
}
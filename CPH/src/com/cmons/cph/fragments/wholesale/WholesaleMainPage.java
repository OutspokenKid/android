package com.cmons.cph.fragments.wholesale;

import java.util.ArrayList;

import org.json.JSONObject;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Notice;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class WholesaleMainPage extends CmonsFragmentForWholesale {
	
	private Button btnShop;
	private Button btnNotice;
	private Button btnManagement;
	private Button btnOrder;
	private Button btnSample;
	private Button btnCustomer;
	private Button btnStaff;
	private Button btnSetting;
	
	private View cover;
	private RelativeLayout noticeRelative;
	private Button btnClose;
	private ListView listView;
	
	private AlphaAnimation aaIn, aaOut;
	private boolean animating;
	private int noticePageIndex;
	private ArrayList<Notice> notices = new ArrayList<Notice>();
	private NoticeAdapter noticeAdapter;
	
	@Override
	public void onResume() {
		super.onResume();
		
		checkNewNotice();
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

		titleBar.getBtnAdd().setVisibility(View.VISIBLE);
		
		noticeAdapter = new NoticeAdapter();
		listView.setAdapter(noticeAdapter);
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

				mActivity.showWritePage();
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

				mActivity.showOrderPage();
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
				
			}
		});
	}

	@Override
	public void setSizes() {
		
		titleBar.getLayoutParams().height = ResizeUtils.getSpecificLength(96);

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
	public void downloadInfo() {
		
		if(isDownloading || isLastList) {
			return;
		}
		
		super.downloadInfo();
		
		String url = CphConstants.BASE_API_URL + "wholesales/notices" +
				"?num=" + CphConstants.LIST_SIZE_WHOLESALE_NOTICE +
				"&page=" + noticePageIndex;
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleMainPage.onError." + "\nurl : " + url);
				setPage(false);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleMainPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					for(int i=0; i<10; i++) {
						notices.add(new Notice());
					}
					
					//isLastList 처리.
					
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
	
	@Override
	public void setPage(boolean successDownload) {
		super.setPage(successDownload);

		if(successDownload) {
			noticeAdapter.notifyDataSetChanged();
			noticePageIndex++;
		}
	}
	
//////////////////// Custom methods.

	public void checkNewNotice() {
		
		String url = CphConstants.BASE_API_URL + "wholesales/notices" +
				"?num=1" +
				"&page=0";
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleMainPage.onError." + "\nurl : " + url);
				setPage(false);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleMainPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					//새로운 메시지가 있느냐의 여부에 따라 titleBar.getNotice() 노출 결정.
					titleBar.getBtnNotice().setVisibility(View.VISIBLE);
					
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void showNoticeRelative() {

		if(!animating && noticeRelative.getVisibility() != View.VISIBLE) {
			
			noticePageIndex = 0;
			notices.clear();
			noticeAdapter.notifyDataSetChanged();
			downloadInfo();
			
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

////////////////////Custom classes.
	
	public class NoticeAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
	
			return notices.size();
		}
	
		@Override
		public Object getItem(int arg0) {
	
			return notices.get(arg0);
		}
	
		@Override
		public long getItemId(int arg0) {
	
			return arg0;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
	
			ViewHolderForNotice viewHolder = null;
			
			if(convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.list_wholesale_notice, parent, false);
				
				viewHolder = new ViewHolderForNotice(convertView);
				viewHolder.setValues(notices.get(position));
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolderForNotice) convertView.getTag();
			}
			
			return convertView;
		}
		
		public class ViewHolderForNotice {
			
			public View row;
			
			public TextView tvNotice;
			public TextView tvRegdate;
			public View icon;
			
			public ViewHolderForNotice(View row) {
				
				this.row = row;
				
				bindViews();
				setSizes();
			}
			
			public void bindViews() {
				
				tvNotice = (TextView) row.findViewById(R.id.list_wholesale_notice_tvNotice);
				tvRegdate = (TextView) row.findViewById(R.id.list_wholesale_notice_tvRegedit);
				icon = row.findViewById(R.id.list_wholesale_notice_icon);
			}
			
			public void setSizes() {

				ResizeUtils.viewResize(400, 120, tvNotice, 1, Gravity.CENTER_VERTICAL, null, new int[]{20, 0, 0, 0});
				ResizeUtils.viewResize(120, 120, tvRegdate, 1, Gravity.CENTER_VERTICAL, new int[]{0, 0, 10, 0});
				icon.getLayoutParams().width = ResizeUtils.getSpecificLength(29);
				icon.getLayoutParams().height = ResizeUtils.getSpecificLength(30);
				
				FontUtils.setFontSize(tvNotice, 28);
				FontUtils.setFontSize(tvRegdate, 20);
			}
			
			public void setValues(Notice notice) {
			
				tvNotice.setText("주문요청이 들어왔습니다. 확인해주세요.");
				tvRegdate.setText("2014.08.13\nPM:11:14");
				icon.setBackgroundResource(R.drawable.mail_icon_a);
			}
		}
	}
}
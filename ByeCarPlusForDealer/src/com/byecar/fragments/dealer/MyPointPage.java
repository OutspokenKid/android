package com.byecar.fragments.dealer;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.TextUtils.TruncateAt;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Dealer;
import com.byecar.models.Transaction;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class MyPointPage extends BCPFragment {

	private TextView tvTitle1;
	private View bgForAccount;
	private TextView tvAccount1;
	private TextView tvAccount2;
	private TextView tvTitle2;
	private View bgForPoint;
	private TextView tvPoint;
	private TextView tvTitle3;
	private LinearLayout linearForHistory;
	private View noList;
	
	private ArrayList<Transaction> transactions = new ArrayList<Transaction>(); 
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.myPointPage_titleBar);
		
		tvTitle1 = (TextView) mThisView.findViewById(R.id.myPointPage_tvTitle1);
		bgForAccount = mThisView.findViewById(R.id.myPointPage_bgForAccount);
		tvAccount1 = (TextView) mThisView.findViewById(R.id.myPointPage_tvAccount1);
		tvAccount2 = (TextView) mThisView.findViewById(R.id.myPointPage_tvAccount2);
		
		tvTitle2 = (TextView) mThisView.findViewById(R.id.myPointPage_tvTitle2);
		bgForPoint = mThisView.findViewById(R.id.myPointPage_bgForPoint);
		tvPoint = (TextView) mThisView.findViewById(R.id.myPointPage_tvPoint);
		
		tvTitle3 = (TextView) mThisView.findViewById(R.id.myPointPage_tvTitle3);
		linearForHistory = (LinearLayout) mThisView.findViewById(R.id.myPointPage_linearForHistory);
		noList = mThisView.findViewById(R.id.myPointPage_noList);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createPage() {

		tvTitle1.setText(R.string.myPointTitle1);
		tvAccount2.setText(R.string.myPointHintText);
		tvTitle2.setText(R.string.myPointTitle2);
		tvTitle3.setText(R.string.myPointTitle3);
	}

	@Override
	public void setListeners() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//tvTitle1.
		rp = (RelativeLayout.LayoutParams) tvTitle1.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(41);
		tvTitle1.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);

		//bgForAccount.
		rp = (RelativeLayout.LayoutParams) bgForAccount.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(588);
		rp.height = ResizeUtils.getSpecificLength(150);
		rp.topMargin = ResizeUtils.getSpecificLength(59);
		rp.bottomMargin = ResizeUtils.getSpecificLength(59);
		
		//tvAccount1.
		rp = (RelativeLayout.LayoutParams) tvAccount1.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(150);
		tvAccount1.setPadding(ResizeUtils.getSpecificLength(164), 0, 0, 0);
		
		//tvAccount2.
		rp = (RelativeLayout.LayoutParams) tvAccount2.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(60);
		
		//tvTitle2.
		rp = (RelativeLayout.LayoutParams) tvTitle2.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(41);
		tvTitle2.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		
		//bgForPoint.
		rp = (RelativeLayout.LayoutParams) bgForPoint.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(588);
		rp.height = ResizeUtils.getSpecificLength(150);
		rp.topMargin = ResizeUtils.getSpecificLength(59);
		rp.bottomMargin = ResizeUtils.getSpecificLength(59);
		
		//tvPoint.
		rp = (RelativeLayout.LayoutParams) tvPoint.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(150);
		tvPoint.setPadding(0, 0, ResizeUtils.getSpecificLength(66), 0);
		
		//tvTitle3.
		rp = (RelativeLayout.LayoutParams) tvTitle3.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(41);
		tvTitle3.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		
		//noList.
		rp = (RelativeLayout.LayoutParams) noList.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(246);
		rp.height = ResizeUtils.getSpecificLength(225);
		rp.topMargin = ResizeUtils.getSpecificLength(70);
		rp.bottomMargin = ResizeUtils.getSpecificLength(70);
		
		FontUtils.setFontSize(tvTitle1, 24);
		FontUtils.setFontStyle(tvTitle1, FontUtils.BOLD);
		
		FontUtils.setFontSize(tvAccount1, 28);
		FontUtils.setFontSize(tvAccount2, 20);
		
		FontUtils.setFontSize(tvTitle2, 24);
		FontUtils.setFontStyle(tvTitle2, FontUtils.BOLD);
		
		FontUtils.setFontSize(tvPoint, 28);
		
		FontUtils.setFontSize(tvTitle3, 24);
		FontUtils.setFontStyle(tvTitle3, FontUtils.BOLD);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_my_point;
	}

	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_myPoint;
	}

	@Override
	public int getRootViewResId() {

		return R.id.myPointPage_mainLayout;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		downloadDealerInfo();
		
		if(transactions.size() == 0) {
			loadHistory();
		}
	}
	
//////////////////// Custom methods.
	
	public void downloadDealerInfo() {
		
		String url = BCPAPIs.DEALER_INFO_URL + "?dealer_id=" + MainActivity.user.getDealer_id();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("MainActivity.downloadDealerInfo.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("MainActivity.downloadDealerInfo.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					MainActivity.dealer = new Dealer(objJSON.getJSONObject("dealer"));
					tvAccount1.setText(MainActivity.dealer.getVaccount_bank()
							+ " " + MainActivity.dealer.getVaccount_no());
					tvPoint.setText(StringUtils.getFormattedNumber(MainActivity.dealer.getCash()) + "원");
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void loadHistory() {
		
		String url = BCPAPIs.MY_TRANSACTION_URL;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("MyPointPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("MyPointPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					JSONArray arJSON = objJSON.getJSONArray("transactions");
					
					for(int i=0; i<arJSON.length(); i++) {
						Transaction tr = new Transaction(arJSON.getJSONObject(i));
						transactions.add(tr);
					}

					setHistory();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void setHistory() {
		
		linearForHistory.removeAllViews();
		
		if(transactions.size() > 0) {
			noList.setVisibility(View.GONE);
			linearForHistory.setVisibility(View.VISIBLE);
			
			for(int i=0;i<transactions.size(); i++) {
				addTransactionView(transactions.get(i));
			}
		} else {
			noList.setVisibility(View.VISIBLE);
			linearForHistory.setVisibility(View.GONE);
		}
	}
	
	public void addTransactionView(Transaction tr) {
		
		LinearLayout linear = new LinearLayout(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 84, linear, 1, 0, null);
		
		TextView tv1 = new TextView(mContext);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1);
		lp.leftMargin = ResizeUtils.getSpecificLength(21);
		tv1.setLayoutParams(lp);
		tv1.setGravity(Gravity.CENTER_VERTICAL);
		tv1.setSingleLine();
		tv1.setEllipsize(TruncateAt.END);
		FontUtils.setFontSize(tv1, 20);
		tv1.setTextColor(getResources().getColor(R.color.holo_text));
		tv1.setText(StringUtils.getDateString("yyyy년 MM월 dd일    ", tr.getCreated_at())
				+ tr.getTitle());
		linear.addView(tv1);
		
		TextView tv2 = new TextView(mContext);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, tv2, 1, 0, null, new int[]{25, 0, 25, 0});
		tv2.setGravity(Gravity.CENTER_VERTICAL);
		tv2.setSingleLine();
		tv2.setEllipsize(TruncateAt.END);
		FontUtils.setFontSize(tv2, 20);

		//note : type(1) 입금(초록), type(2) 취소, type(10) 사용(빨강)
		switch (tr.getType()) {
		case Transaction.TYPE_IN:
			tv2.setTextColor(getResources().getColor(R.color.color_green));
			tv2.setText(StringUtils.getFormattedNumber(tr.getAmount()) + " 원 적립");
			break;
			
		case Transaction.TYPE_CANCEL:
			tv2.setTextColor(getResources().getColor(R.color.holo_text));
			tv2.setText("--");
			break;
			
		case Transaction.TYPE_OUT:
			tv2.setTextColor(getResources().getColor(R.color.color_red));
			tv2.setText("-" + StringUtils.getFormattedNumber(tr.getAmount()) + " 원 차감");
			break;
		}
		
		linear.addView(tv2);
		
		linearForHistory.addView(linear);
	}
}

package com.byecar.fragments.dealer;

import org.json.JSONObject;

import android.view.View;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Dealer;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;

public class MyTicketPage extends BCPFragment {

	public static final int TYPE_AUCTION = 0;
	public static final int TYPE_DEALER = 1;
	
	private View ticketView;
	private View bg;
	private TextView tvRemainTicket;
	private TextView tvExpiration;
	
	int type;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.myTicketPage_titleBar);

		ticketView = mThisView.findViewById(R.id.myTicketPage_ticketView);
		bg = mThisView.findViewById(R.id.myTicketPage_bg);
		tvRemainTicket = (TextView) mThisView.findViewById(R.id.myTicketPage_tvRemainTicket);
		tvExpiration = (TextView) mThisView.findViewById(R.id.myTicketPage_tvExpiration);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			type = getArguments().getInt("type");
		}
	}

	@Override
	public void createPage() {

		checkMyTicket();
	}

	@Override
	public void setListeners() {
	}

	@Override
	public void setSizes() {

		ResizeUtils.viewResizeForRelative(188, 160, ticketView, null, null, new int[]{0, 60, 0, 0});
		ResizeUtils.viewResizeForRelative(588, 150, bg, null, null, new int[]{0, 60, 0, 0});
		ResizeUtils.viewResizeForRelative(588, 75, tvRemainTicket, null, null, null);
		ResizeUtils.viewResizeForRelative(588, 75, tvExpiration, null, null, null);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_my_ticket;
	}

	@Override
	public int getPageTitleTextResId() {

		if(type == 0) {
			return R.string.pageTitle_ticket_auction;
		} else {
			return R.string.pageTitle_ticket_dealer;
		}
	}

	@Override
	public int getRootViewResId() {

		return R.id.myTicketPage_mainLayout;
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

//////////////////// Custom methods.
	
	public void checkMyTicket() {

		String url = BCPAPIs.DEALER_INFO_URL + "?dealer_id=" + MainActivity.dealer.getId();
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
					
					int remainTicketCount = type==TYPE_AUCTION?
												MainActivity.dealer.getRight_to_bid_cnt()
												: MainActivity.dealer.getRight_to_sell_cnt();
					long expirationDate = 0;
					
					tvRemainTicket.setText("남은 등록권 수 : " + remainTicketCount + "개");
					tvExpiration.setText("사용 만료일 : " + StringUtils.getDateString("yyyy년 MM월 dd일", expirationDate));
					
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});

	}
}

package com.byecar.fragments.dealer;

import org.json.JSONObject;

import android.view.View;

import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPFragment;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.ResizeUtils;

public class MyTicketPage extends BCPFragment {

	private View ticketView;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.myTicketPage_titleBar);

		ticketView = mThisView.findViewById(R.id.myTicketPage_ticketView);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

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

		ResizeUtils.viewResizeForRelative(592, 318, ticketView, null, null, new int[]{0, 33, 0, 0});
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_my_ticket;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.ticket_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 282;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
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

		ticketView.setBackgroundResource(R.drawable.ticket_free);
	}
}

package com.byecar.fragments.dealer;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.ResizeUtils;

public class MyPage extends BCPFragment {

	private Button[] buttons;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.myPage_titleBar);

		buttons = new Button[5];
		buttons[0] = (Button) mThisView.findViewById(R.id.myPage_btn1);
		buttons[1] = (Button) mThisView.findViewById(R.id.myPage_btn2);
		buttons[2] = (Button) mThisView.findViewById(R.id.myPage_btn3);
		buttons[3] = (Button) mThisView.findViewById(R.id.myPage_btn4);
		buttons[4] = (Button) mThisView.findViewById(R.id.myPage_btn5);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListeners() {

		for(int i=0; i<buttons.length; i++) {
			
			final int INDEX = i;
			buttons[i].setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View view) {
	
					switch (INDEX) {
					
					//나의 정보/후기.
					case 0:
						mActivity.showPage(BCPConstants.PAGE_MY_INFO_REVIEW, null);
						break;
						
					//바이카 입찰권.
					case 1:
						Bundle bundle = new Bundle();
						bundle.putInt("type", MyTicketPage.TYPE_AUCTION);
						mActivity.showPage(BCPConstants.PAGE_MY_TICKET, bundle);
						break;
						
					//중고마켓 등록권.
					case 2:
						bundle = new Bundle();
						bundle.putInt("type", MyTicketPage.TYPE_DEALER);
						mActivity.showPage(BCPConstants.PAGE_MY_TICKET, bundle);
						break;
						
					//거래완료내역.
					case 3:
						mActivity.showPage(BCPConstants.PAGE_MY_COMPLETED_LIST, null);
						break;
						
					//적립금내역.
					case 4:
						mActivity.showPage(BCPConstants.PAGE_MY_POINT, null);
						break;
					}
				}
			});	
		}
	}

	@Override
	public void setSizes() {

		for(int i=0; i<buttons.length; i++) {
			ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, buttons[i], null, null, null);
		}
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_my;
	}
	
	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_my;
	}

	@Override
	public int getRootViewResId() {

		return R.id.myPage_mainLayout;
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
}

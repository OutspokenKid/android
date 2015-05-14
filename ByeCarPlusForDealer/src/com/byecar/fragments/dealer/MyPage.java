package com.byecar.fragments.dealer;

import org.json.JSONObject;

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

	private Button btnGrade;
	private Button btnTicket;
	private Button btnCompletedList;
	private Button btnReview;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.myPage_titleBar);
		
		btnGrade = (Button) mThisView.findViewById(R.id.myPage_btnGrade);
		btnTicket = (Button) mThisView.findViewById(R.id.myPage_btnTicket);
		btnCompletedList = (Button) mThisView.findViewById(R.id.myPage_btnCompletedList);
		btnReview = (Button) mThisView.findViewById(R.id.myPage_btnReview);
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

		btnGrade.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_MY_GRADE, null);
			}
		});
		
		btnTicket.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_MY_TICKET, null);
			}
		});
		
		btnCompletedList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_MY_COMPLETED_LIST, null);
			}
		});
		
		btnReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(BCPConstants.PAGE_MY_REVIEW, null);
			}
		});
	}

	@Override
	public void setSizes() {

		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, btnGrade, null, null, null);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, btnTicket, null, null, null);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, btnCompletedList, null, null, null);
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, btnReview, null, null, null);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_my;
	}
	
	@Override
	public int getPageTitleTextResId() {
		// TODO Auto-generated method stub
		return 0;
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

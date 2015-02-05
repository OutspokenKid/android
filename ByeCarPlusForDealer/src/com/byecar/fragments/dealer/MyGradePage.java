package com.byecar.fragments.dealer;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Dealer;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.IntentUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class MyGradePage extends BCPFragment {

	private View gradeView;
	private Button btnCall;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.myGradePage_titleBar);

		gradeView = mThisView.findViewById(R.id.myGradePage_gradeView);
		btnCall = (Button) mThisView.findViewById(R.id.myGradePage_btnCall);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

		checkMyGrade();
	}

	@Override
	public void setListeners() {

		btnCall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				IntentUtils.call(mContext, getString(R.string.callByeCar));
			}
		});
	}

	@Override
	public void setSizes() {

		ResizeUtils.viewResizeForRelative(592, 322, gradeView, null, null, new int[]{0, 33, 0, 0});
		ResizeUtils.viewResizeForRelative(587, 83, btnCall, null, null, new int[]{0, 30, 0, 0});
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_my_grade;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.grade_back_btn_a;
	}

	@Override
	public int getBackButtonWidth() {

		return 235;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
	}

	@Override
	public int getRootViewResId() {

		return R.id.myGradePage_mainLayout;
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
	
	public void checkMyGrade() {

		switch(MainActivity.dealer.getLevel()) {
		
		case Dealer.LEVEL_FRESH_MAN:
			gradeView.setBackgroundResource(R.drawable.grade1);
			btnCall.setVisibility(View.INVISIBLE);
			break;
			
		case Dealer.LEVEL_NORAML_DEALER:
			gradeView.setBackgroundResource(R.drawable.grade2);
			btnCall.setVisibility(View.INVISIBLE);
			break;
			
		case Dealer.LEVEL_SUPERB_DEALER:
			gradeView.setBackgroundResource(R.drawable.grade3);
			btnCall.setVisibility(View.VISIBLE);
			break;
			
		case Dealer.LEVEL_POWER_DEALER:
			gradeView.setBackgroundResource(R.drawable.grade4);
			btnCall.setVisibility(View.INVISIBLE);
			break;
		}
	}
}

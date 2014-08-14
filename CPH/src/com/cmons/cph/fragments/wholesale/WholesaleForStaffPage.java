package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.views.TitleBar;

public class WholesaleForStaffPage extends CmonsFragmentForWholesale {

	private static final int TYPE_REQUEST = 0;
	private static final int TYPE_STAFF = 1;
	
	private TitleBar titleBar;
	private Button btnRequest;
	private Button btnStaff;
	private ListView listView;
	
	private int type;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleStaffPage_titleBar);
		btnRequest = (Button) mThisView.findViewById(R.id.wholesaleStaffPage_btnRequest);
		btnStaff = (Button) mThisView.findViewById(R.id.wholesaleStaffPage_btnStaff);
		listView = (ListView) mThisView.findViewById(R.id.wholesaleStaffPage_listView);
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

		btnRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeType(TYPE_REQUEST);
			}
		});
		
		btnStaff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				changeType(TYPE_STAFF);
			}
		});
	}

	@Override
	public void setSizes() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getContentViewId() {
		// TODO Auto-generated method stub
		return 0;
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
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}
	
//////////////////// Custom methods.
	
	public void changeType(int type) {
		
	}
	
	public void request() {
		
	}
	
	public void fire() {
		
	}
}

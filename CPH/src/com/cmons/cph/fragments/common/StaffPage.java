package com.cmons.cph.fragments.common;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForShop;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Staff;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.ResizeUtils;

public class StaffPage extends CmonsFragmentForShop {
	
	private Button btnRequest;
	private Button btnStaff;
	
	private ListView listView;
	
	private int menuIndex;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.commonStaffPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.commonStaffPage_ivBg);
		
		btnRequest = (Button) mThisView.findViewById(R.id.commonStaffPage_btnRequest);
		btnStaff = (Button) mThisView.findViewById(R.id.commonStaffPage_btnStaff);
		
		listView = (ListView) mThisView.findViewById(R.id.commonStaffPage_listView);
	}

	@Override
	public void setVariables() {

		title = "직원관리";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
	}

	@Override
	public void setListeners() {

		btnRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(0);
			}
		});
		
		btnStaff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(1);
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if(menuIndex == 0) {
					approval((Staff)models.get(arg2));
				} else {
					fire((Staff)models.get(arg2));
				}
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//ivBg.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.commonStaffPage_ivBg).getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(96); 
		
		//btnRequest.
		rp = (RelativeLayout.LayoutParams) btnRequest.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/2;
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//btnStaff.
		rp = (RelativeLayout.LayoutParams) btnStaff.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_staff;
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
		
		for(int i=0; i<10; i++) {
			Staff staff = new Staff();
			staff.setItemCode(CphConstants.ITEM_STAFF);
			staff.setInRequest(menuIndex == 0);
			models.add(staff);
		}
		return false;
	}

	@Override
	public void downloadInfo() {
		
		url = CphConstants.BASE_API_URL + "commons/notices";
		super.downloadInfo();
	}
	
//////////////////// Custom methods.
	
	public void setMenu(int menuIndex) {
		
		switch (menuIndex) {
		
		case 0:
			btnRequest.setBackgroundResource(R.drawable.sample_recommand_btn_a);
			btnStaff.setBackgroundResource(R.drawable.staff_list_btn_b);
			break;
			
		case 1:
			btnRequest.setBackgroundResource(R.drawable.sample_recommand_btn_b);
			btnStaff.setBackgroundResource(R.drawable.staff_list_btn_a);
			break;
		}
		
		this.menuIndex = menuIndex;
		
		refreshPage();
	}
	
	public void approval(Staff staff) {
		
	}
	
	public void fire(Staff staff) {
		
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.staff_bg;
	}
}

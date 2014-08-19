package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Notice;
import com.cmons.cph.views.TitleBar;

public class WholesaleForNoticeListPage extends CmonsFragmentForWholesale {

	private ListView listView;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleNoticeListPage_titleBar);
		
		listView = (ListView) mThisView.findViewById(R.id.wholesaleNoticeListPage_listView);
	}

	@Override
	public void setVariables() {

		title = "전체공지사항";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDividerHeight(0);
	}

	@Override
	public void setListeners() {
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				mActivity.showNoticePage((Notice)models.get(arg2));
			}
		});
	}

	@Override
	public void setSizes() {
		
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_noticelist;
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
			Notice notice = new Notice();
			notice.setItemCode(CphConstants.ITEM_NOTICE);
			
			if(i>5) {
				notice.setRead(true);
			}
			
			models.add(notice);
		}
		return false;
	}
	
	@Override
	public void downloadInfo() {

		url = CphConstants.BASE_API_URL + "products";
		super.downloadInfo();
	}
}

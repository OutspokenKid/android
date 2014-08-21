package com.cmons.cph.fragments.wholesale;

import org.json.JSONArray;
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
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;

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

		try {
			String readListString = SharedPrefsUtils.getStringFromPrefs(CphConstants.PREFS_NOTICE, "readList");
			
			JSONArray arJSON = objJSON.getJSONArray("notices");

			int size = arJSON.length();
			for(int i=0; i<size; i++) {
				Notice notice = new Notice(arJSON.getJSONObject(i));
				notice.setItemCode(CphConstants.ITEM_NOTICE);

				if(readListString != null && readListString.contains("" + notice.getId())) {
					notice.setRead(true);
				} else {
					notice.setRead(false);
				}
				
				models.add(notice);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		return true;
	}
	
	@Override
	public void downloadInfo() {

		url = CphConstants.BASE_API_URL + "wholesales/notices?num=0";
		super.downloadInfo();
	}
}

package com.cmons.cph.fragments.common;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForShop;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Notice;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;

/**
 * 전체 공지.
 * 내 매장 공지.
 * 		대표
 * 		대표 x
 * 남의 매장 공지.
 * 
 * 
 * @author HyungGunKim
 *
 */
public class NoticeListPage extends CmonsFragmentForShop {

	private ListView listView;
	
	private boolean isAppNotice;
	private boolean isOurNotice;
	private int wholesale_id;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.noticeListPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.noticeListPage_ivBg);
		
		listView = (ListView) mThisView.findViewById(R.id.noticeListPage_listView);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			isAppNotice = getArguments().getBoolean("isAppNotice");
			isOurNotice = getArguments().getBoolean("isOurNotice");
			wholesale_id = getArguments().getInt("wholesale_id");
		}
		
		if(isAppNotice) {
			title = "전체 공지사항";
		} else {
			title = "매장 공지사항";
		}
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
		
		//우리매장 공지 && 대표.
		if(isOurNotice && mActivity.user.getRole() % 100 == 0) {
			titleBar.getBtnWrite().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					Bundle bundle = new Bundle();
					bundle.putBoolean("isEdit", true);
					bundle.putString("title", "공지사항 추가");
					mActivity.showPage(CphConstants.PAGE_COMMON_NOTICE, bundle);
				}
			});
		}
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				//읽음표시.
				((Notice)models.get(arg2)).setRead(true);
				adapter.notifyDataSetChanged();
				
				boolean isEdit = false;
				
				if(isAppNotice) {
					isEdit = false;
				} else {
					
					//우리매장 공지 && 대표.
					if(isOurNotice && mActivity.user.getRole() % 100 == 0) {
						isEdit = true;
						
					//아님.
					} else {
						isEdit = false;
					}
				}
				
				Bundle bundle = new Bundle();
				bundle.putSerializable("notice", (Notice)models.get(arg2));
				bundle.putBoolean("isEdit", isEdit);
				bundle.putString("title", title);
				mActivity.showPage(CphConstants.PAGE_COMMON_NOTICE, bundle);
			}
		});
	}

	@Override
	public void setSizes() {
		
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_noticelist;
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

		if(isAppNotice) {
			url = CphConstants.BASE_API_URL + "posts/notices?num=0";
		} else {
			//http://cph.minsangk.com/wholesales/notices?wholesale_id=190
			url = CphConstants.BASE_API_URL + "wholesales/notices?num=0";
			
			if(wholesale_id != 0) {
				url += "&wholesale_id=" + wholesale_id;
			}
		}
		
		super.downloadInfo();
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.setting_bg;
	}
}

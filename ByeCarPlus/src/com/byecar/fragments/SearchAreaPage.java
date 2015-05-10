package com.byecar.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPAdapter;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragment;
import com.byecar.models.AreaForSearch;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;

public class SearchAreaPage extends BCPFragment {

	private TextView tvTitle;
	private EditText etKeyword;
	private Button btnClear;
	private ListView listView;
	
	private AsyncSearchTask currentTask;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.searchAreaPage_titleBar);
		
		tvTitle = (TextView) mThisView.findViewById(R.id.searchAreaPage_tvTitle);
		etKeyword = (EditText) mThisView.findViewById(R.id.searchAreaPage_etKeyword);
		btnClear = (Button) mThisView.findViewById(R.id.searchAreaPage_btnClear);
		listView = (ListView) mThisView.findViewById(R.id.searchAreaPage_listView);
	}

	@Override
	public void setVariables() {

	}

	@Override
	public void createPage() {

		adapter = new BCPAdapter(mContext, mActivity, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.LTGRAY));
		listView.setDividerHeight(1);
	}

	@Override
	public void setListeners() {

		etKeyword.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(currentTask != null) {
					currentTask.cancel(true);
					currentTask = null;
				}
				
				if(StringUtils.isEmpty(etKeyword.getText())) {
					isLastList = false;
					last_priority = 0;
					models.clear();
					adapter.notifyDataSetChanged();
				} else {
					AsyncSearchTask ast = new AsyncSearchTask();
					currentTask = ast;
					ast.execute();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		btnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				etKeyword.setText(null);
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {

				AreaForSearch afs = (AreaForSearch) models.get(position);
				
				Bundle bundle = new Bundle();
				bundle.putInt("requestCode", BCPConstants.REQUEST_SEARCH_AREA);
				bundle.putString("address", afs.getAddress());
				bundle.putInt("dong_id", afs.getId());
				mActivity.bundle = bundle;
				mActivity.closeTopPage();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		//tvTitle.
		rp = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(76);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(12);
		
		//etKeyword.
		rp = (RelativeLayout.LayoutParams) etKeyword.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(52);
		etKeyword.setPadding(ResizeUtils.getSpecificLength(10), 0, ResizeUtils.getSpecificLength(10), 0);
		
		//btnClear.
		rp = (RelativeLayout.LayoutParams) btnClear.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(40);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.rightMargin = ResizeUtils.getSpecificLength(4);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		
		//listView.
		rp = (RelativeLayout.LayoutParams) listView.getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		
		FontUtils.setFontSize(tvTitle, 30);
		FontUtils.setFontSize(etKeyword, 24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_search_area;
	}

	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_searchArea;
	}

	@Override
	public int getRootViewResId() {

		return R.id.searchAreaPage_mainLayout;
	}

	@Override
	public void downloadInfo() {
		
		//http://dev.bye-car.com/dongs/search.json?keyword=
		url = BCPAPIs.AREA_SEARCH_URL 
				+ "?keyword=" + StringUtils.getUrlEncodedString(etKeyword);
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {

		try {
			int size = 0;
			JSONArray arJSON = objJSON.getJSONArray("dongs");
			size = arJSON.length();
			
			for(int i=0; i<size; i++) {
				AreaForSearch afs = new AreaForSearch(arJSON.getJSONObject(i));
				afs.setItemCode(BCPConstants.ITEM_AREA_FOR_SEARCH);
				models.add(afs);
			}
			
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
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

	@Override
	public void onResume() {
		super.onResume();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				SoftKeyboardUtils.showKeyboard(mContext, etKeyword);
			}
		}, 300);
	}
	
//////////////////// Classes.
	
	public class AsyncSearchTask extends AsyncTask<Void, Void, Void> {

		@Override
		public void onPreExecute() {
		}
		
		@Override
		public Void doInBackground(Void... params) {
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {}
			return null;
		}
		
		@Override
		public void onPostExecute(Void result) {

			refreshPage();
		}
	}
}

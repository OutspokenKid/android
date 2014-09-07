package com.cmons.cph.fragments.signup;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForSignUp;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Category;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SignUpForCategoryPage extends CmonsFragmentForSignUp {

	private ListView listView;
	private int type;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForCategoryPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.signUpForCategoryPage_ivBg);
		
		listView = (ListView) mThisView.findViewById(R.id.signUpForCategoryPage_listView);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null && getArguments().containsKey("type")) {
			type = getArguments().getInt("type");
		}
		
		title = getString(R.string.selectCategory);
	}

	@Override
	public void createPage() {
		
		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.WHITE));
		listView.setDividerHeight(1);
	}

	@Override
	public void setListeners() {

		titleBar.getBtnNext().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Category categoryForSignUp;
				String categoryString = "";
				int selectedCount = 0;
				
				int size = models.size();
				for(int i=0; i<size; i++) {
					
					try {
						categoryForSignUp = (Category) models.get(i);
						
						if(categoryForSignUp.isSelected()) {
							if(!"".equals(categoryString)) {
								categoryString += ",";
							}
							
							categoryString += categoryForSignUp.getId();
							
							selectedCount++;
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (Error e) {
						LogUtils.trace(e);
					}
				}
				
				if(selectedCount == 0) {
					ToastUtils.showToast(R.string.wrongCategory);
				} else {
					mActivity.showSearchPage(type, categoryString);
				}
			}
		});
	}
	
	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//shadow.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForCategoryPage_titleShadow).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(14);
		
		//tvCategory.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForCategoryPage_tvCategory).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(74);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
		
		//ScrollView.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForCategoryPage_listView).getLayoutParams();
	}
	
	@Override
	public int getContentViewId() {

		return R.layout.fragment_sign_up_category;
	}

	@Override
	public void refreshPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {

		try {
			JSONArray arJSON = objJSON.getJSONArray("categories");
			
			int size = arJSON.length();
			for(int i=0; i<size; i++) {
				try {
					Category category = new Category(arJSON.getJSONObject(i));
					category.setItemCode(CphConstants.ITEM_CATEGORY);
					models.add(category);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
			
			return (size < CphConstants.LIST_SIZE_WHOLESALE_NOTICE); 
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return false;
	}

	@Override
	public void downloadInfo() {
		
		url = CphConstants.BASE_API_URL + "categories";
		super.downloadInfo();		
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.bg2;
	}
}

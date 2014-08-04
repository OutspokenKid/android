package com.cmons.cph.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.classes.BaseFragmentForSignUp;
import com.cmons.classes.CphConstants;
import com.cmons.cph.R;
import com.cmons.cph.models.CategoryForSignUp;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SignUpForCategoryPage extends BaseFragmentForSignUp {

	private TitleBar titleBar;
	private ListView listView;
	private Button btnNext;
	private int type;
	
	private ArrayList<CategoryForSignUp> categories = new ArrayList<CategoryForSignUp>();
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(categories.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	protected void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForCategoryPage_titleBar);
		listView = (ListView) mThisView.findViewById(R.id.signUpForCategoryPage_listView);
		btnNext = (Button) mThisView.findViewById(R.id.signUpForCategoryPage_btnNext);
	}

	@Override
	protected void setVariables() {

		if(getArguments() != null && getArguments().containsKey("type")) {
			type = getArguments().getInt("type");
		}
	}

	@Override
	protected void createPage() {

		titleBar.addBackButton(R.drawable.btn_back_position, 162, 92);
		titleBar.setTitleText(R.string.selectCategory);
		
		listView.setAdapter(new CategoryListAdapter(mActivity.getLayoutInflater(), categories));
	}

	@Override
	protected void setListeners() {

		titleBar.getBackButton().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View convertView, int position,
					long id) {
				
				ViewHolderForCategory holder = (ViewHolderForCategory) convertView.getTag();
				
				if(categories.get(position).isSelected()) {
					categories.get(position).setSelected(false);
					holder.check.setVisibility(View.INVISIBLE);
				} else {
					categories.get(position).setSelected(true);
					holder.check.setVisibility(View.VISIBLE);
				}
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				String categoryString = "";
				int selectedCount = 0;
				
				int size = categories.size();
				for(int i=0; i<size; i++) {
					
					if(categories.get(i).isSelected()) {
						if(!"".equals(categoryString)) {
							categoryString += ",";
						}
						
						categoryString += categories.get(i).getId();
						
						selectedCount++;
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
	protected void setSizes() {

		//titleBar.
		titleBar.getLayoutParams().height = ResizeUtils.getSpecificLength(96);
		
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
		rp.bottomMargin = ResizeUtils.getSpecificLength(200);
		
		//btnNext.
		rp = (RelativeLayout.LayoutParams) btnNext.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(74);
		rp.bottomMargin = ResizeUtils.getSpecificLength(100);
		
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForCategoryPage_ivCopyright).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(352);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
	}
	
	@Override
	protected int getXmlResId() {

		return R.layout.fragment_sign_up_category;
	}

	@Override
	public void onRefreshPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onBackKeyPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	public void downloadInfo() {

		mActivity.showLoadingView();
		
		String url = CphConstants.BASE_API_URL + "wholesales/categories";
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("SignUpForCategoryPage.onError." + "\nurl : " + url);
				mActivity.hideLoadingView();
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				mActivity.hideLoadingView();
				
				try {
					LogUtils.log("SignUpForCategoryPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					JSONArray arJSON = objJSON.getJSONArray("wholesale_categories");
					JSONObject objItem = null;
					
					int size = arJSON.length();
					for(int i=0; i<size; i++) {
						try {
							objItem = arJSON.getJSONObject(i);
							categories.add(new CategoryForSignUp(objItem.getString("id"), objItem.getString("name")));
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (Error e) {
							LogUtils.trace(e);
						}
					}
					
					((CategoryListAdapter) listView.getAdapter()).notifyDataSetChanged();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public class CategoryListAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private ArrayList<CategoryForSignUp> categories = new ArrayList<CategoryForSignUp>();
		
		public CategoryListAdapter(LayoutInflater mInflater,
				ArrayList<CategoryForSignUp> categories) {
			this.mInflater = mInflater;
			this.categories = categories;
		}
		
		@Override
		public int getCount() {
			
			return categories.size();
		}

		@Override
		public Object getItem(int arg0) {

			return categories.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolderForCategory holder;
			
			if(convertView == null) {
				convertView = mInflater.inflate(R.layout.list_signup_category, parent, false);
				holder = new ViewHolderForCategory();
						
				holder.textView = (TextView) convertView.findViewById(R.id.list_signup_category_textView);
				holder.check = convertView.findViewById(R.id.list_signup_category_check);

				holder.check.getLayoutParams().width = ResizeUtils.getSpecificLength(52);
				holder.check.getLayoutParams().height = ResizeUtils.getSpecificLength(34);
				
				convertView.setTag(holder);
				
			} else {
				holder = (ViewHolderForCategory) convertView.getTag();
			}

			holder.textView.setText(categories.get(position).getName());
			
			if(categories.get(position).isSelected()) {
				holder.check.setVisibility(View.VISIBLE);
			} else {
				holder.check.setVisibility(View.INVISIBLE);
			}
			
			return convertView;
		}
	}
	
	public class ViewHolderForCategory {
		
		public TextView textView;
		public View check;
	}
}

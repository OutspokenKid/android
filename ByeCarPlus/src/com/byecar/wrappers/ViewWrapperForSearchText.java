package com.byecar.wrappers;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragmentActivity;
import com.byecar.fragments.TypeSearchCarPage;
import com.byecar.models.CarModel;
import com.byecar.models.CarModelGroup;
import com.byecar.models.CarSearchString;
import com.byecar.models.CarTrim;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForSearchText extends ViewWrapper {

	private BCPFragmentActivity mActivity;
	private BaseModel model;
	
	private TextView tvText;
	private View line;
	
	public ViewWrapperForSearchText(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			tvText = (TextView) row.findViewById(R.id.list_search_text_tvText);
			line = row.findViewById(R.id.list_search_text_line);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			row.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(80)));
			
			FontUtils.setFontSize(tvText, 30);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			model = baseModel;
			
			if(model instanceof CarModelGroup) {
				tvText.setText(((CarModelGroup)model).getName());
			
			} else if(model instanceof CarModel) {
				tvText.setText(((CarModel)model).getName());
				
			} else if(model instanceof CarTrim) {
				tvText.setText(((CarTrim)model).getName());
				
			} else if(model instanceof CarSearchString) {
				tvText.setText(((CarSearchString)model).getText());
				
				if(model.getItemCode() == BCPConstants.ITEM_CAR_TEXT_DESC) {
					row.setLayoutParams(new AbsListView.LayoutParams(
							LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(120)));
					FontUtils.setFontSize(tvText, 18);
					tvText.setTextColor(Color.RED);
					line.setVisibility(View.INVISIBLE);
				} else {
					row.setLayoutParams(new AbsListView.LayoutParams(
							LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(80)));
					FontUtils.setFontSize(tvText, 30);
					tvText.setTextColor(row.getContext().getResources().getColor(R.color.holo_text_hint));
					line.setVisibility(View.VISIBLE);
				}
			}
		} catch (Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {

		if(model != null && !model.isNeedClickListener()) {
			return;
		}
		
		try {
			if(model != null) {
				row.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						if(mActivity != null) {

							int type = 0;
							Bundle bundle = new Bundle();
							
							if(model instanceof CarModelGroup) {
								bundle.putInt("type", TypeSearchCarPage.TYPE_MODEL);
								bundle.putInt("modelgroup_id", ((CarModelGroup)model).getId());
								type = BCPConstants.PAGE_TYPE_SEARCH_CAR;
								mActivity.showPage(type, bundle);
							
							} else if(model instanceof CarModel) {
								bundle.putInt("type", TypeSearchCarPage.TYPE_TRIM);
								bundle.putInt("model_id", ((CarModel)model).getId());
								type = BCPConstants.PAGE_TYPE_SEARCH_CAR;
								mActivity.showPage(type, bundle);
								
							} else if(model instanceof CarTrim) {
								mActivity.bundle = new Bundle();
								mActivity.bundle.putInt("requestCode", BCPConstants.REQUEST_SEARCH_TRIM);
								mActivity.bundle.putInt("trim_id", ((CarTrim) model).getId());
								mActivity.closePages(4);
							
							} else if(model instanceof CarSearchString) {
								
								CarSearchString css = (CarSearchString) model;
								
								if(model.getItemCode() == BCPConstants.ITEM_CAR_TEXT_DESC) {
									return;
								}
								
								if(css.getType() == TypeSearchCarPage.TYPE_YEAR) {
									mActivity.bundle = new Bundle();
									mActivity.bundle.putString("year", css.getText().replace("년", ""));
									
									bundle.putInt("type", TypeSearchCarPage.TYPE_MONTH);
									type = BCPConstants.PAGE_TYPE_SEARCH_CAR;
									mActivity.showPage(type, bundle);
									return;
								} else if(css.getType() == TypeSearchCarPage.TYPE_MONTH) {
									mActivity.bundle.putInt("requestCode", BCPConstants.REQUEST_SEARCH_MONTH);
									mActivity.bundle.putString("month", css.getText().replace("월", ""));
									mActivity.closePages(2);
									return;
								}
								
								mActivity.bundle = new Bundle();
								mActivity.bundle.putInt("type", css.getType());
								mActivity.bundle.putString("text", css.getText());
								mActivity.closeTopPage();
							}
						}
					}
				});
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
	public void setUnusableView() {

	}
	
////////////////////Custom methods.
	
	public void setActivity(BCPFragmentActivity activity) {

		mActivity = activity;
	}
}

package com.byecar.wrappers;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragmentActivity;
import com.byecar.models.Banner;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForBanner extends ViewWrapper {

	private Banner banner;
	
	private BCPFragmentActivity mActivity;
	private ImageView ivImage;
	
	public ViewWrapperForBanner(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			ivImage = (ImageView) row.findViewById(R.id.list_banner_ivImage);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			row.setLayoutParams(new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(88)));
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Banner) {
				this.banner = (Banner) baseModel;
				setImage(ivImage, banner.getImageUrl());
			}
		} catch (Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {
		
		try {
			if(banner != null) {
				row.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						
						if(mActivity != null) {
							mActivity.showPage(BCPConstants.PAGE_TYPE_SEARCH_CAR, null);
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

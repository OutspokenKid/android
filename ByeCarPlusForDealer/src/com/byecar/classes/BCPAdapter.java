package com.byecar.classes;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.byecar.byecarplusfordealer.R;
import com.byecar.wrappers.ViewWrapperForBanner;
import com.byecar.wrappers.ViewWrapperForBrand;
import com.byecar.wrappers.ViewWrapperForCar;
import com.byecar.wrappers.ViewWrapperForMyCar;
import com.byecar.wrappers.ViewWrapperForMyReview;
import com.byecar.wrappers.ViewWrapperForNotification;
import com.byecar.wrappers.ViewWrapperForOpenablePost;
import com.byecar.wrappers.ViewWrapperForSearchText;
import com.outspoken_kid.classes.OutSpokenAdapter;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.ResizeUtils;

/**
 * @author HyungGunKim
 *
 */
public class BCPAdapter extends OutSpokenAdapter {

	private BCPFragmentActivity activity;
	private BCPAuctionableFragment fragment;
	
	public BCPAdapter(Context context, BCPFragmentActivity activity, 
			LayoutInflater inflater, ArrayList<BaseModel> models) {
		super(context, inflater, models);
		this.activity = activity;
	}
	
	public BCPAdapter(Context context, BCPFragmentActivity activity, 
			BCPAuctionableFragment fragment,
			LayoutInflater inflater, ArrayList<BaseModel> models) {
		super(context, inflater, models);
		this.activity = activity;
		this.fragment = fragment;
	}

	@Override
	public int getLayoutResIdByItemCode(int itemCode) {
	
		switch (itemCode) {

		case BCPConstants.ITEM_NOTICE:
		case BCPConstants.ITEM_FAQ:
			return R.layout.list_openable_post;

		case BCPConstants.ITEM_NOTIFICATION:
			return R.layout.list_notification;
			
		case BCPConstants.ITEM_REVIEW:
			return R.layout.list_my_review;
			
		case BCPConstants.ITEM_CAR_BID:
		case BCPConstants.ITEM_CAR_BID_MY:
		case BCPConstants.ITEM_CAR_DEALER:
		case BCPConstants.ITEM_CAR_DEALER_IN_PROGRESS:
			return R.layout.list_car;
			
		case BCPConstants.ITEM_CAR_BRAND: 
			return R.layout.grid_brand;
			
		case BCPConstants.ITEM_CAR_TEXT: 
			return R.layout.list_search_text;
			
		case BCPConstants.ITEM_CAR_MY_AUCTION:
		case BCPConstants.ITEM_CAR_MY_DEALER:
			return R.layout.list_my_car;
			
		case BCPConstants.ITEM_BANNER:
			return R.layout.list_banner;
		}

		return 0;
	}
	
	@Override
	public ViewWrapper getViewWrapperByItemCode(View convertView, int itemCode) {
		
		switch (itemCode) {

		case BCPConstants.ITEM_NOTICE:
		case BCPConstants.ITEM_FAQ:
			return new ViewWrapperForOpenablePost(convertView, itemCode);

		case BCPConstants.ITEM_NOTIFICATION:
			return new ViewWrapperForNotification(convertView, itemCode);

		case BCPConstants.ITEM_REVIEW:
			return new ViewWrapperForMyReview(convertView, itemCode);
			
		case BCPConstants.ITEM_CAR_BID:
		case BCPConstants.ITEM_CAR_BID_MY:
			ViewWrapperForCar vwfc = new ViewWrapperForCar(convertView, itemCode);
			vwfc.setFragment(fragment);
			vwfc.setActivity(activity);
			return vwfc;
			
		case BCPConstants.ITEM_CAR_DEALER:
		case BCPConstants.ITEM_CAR_DEALER_IN_PROGRESS:
			vwfc = new ViewWrapperForCar(convertView, itemCode);
			vwfc.setActivity(activity);
			return vwfc;
			
		case BCPConstants.ITEM_CAR_BRAND:
			ViewWrapperForBrand vwfb = new ViewWrapperForBrand(convertView, itemCode);
			vwfb.setActivity(activity);
			return vwfb;
			
		case BCPConstants.ITEM_CAR_TEXT:
			ViewWrapperForSearchText vwfst = new ViewWrapperForSearchText(convertView, itemCode);
			vwfst.setActivity(activity);
			return vwfst;
			
		case BCPConstants.ITEM_CAR_MY_AUCTION:
		case BCPConstants.ITEM_CAR_MY_DEALER:
			return new ViewWrapperForMyCar(convertView, itemCode);
			
		case BCPConstants.ITEM_BANNER:
			ViewWrapperForBanner vwfba = new ViewWrapperForBanner(convertView, itemCode);
			vwfba.setActivity(activity);
			return vwfba;
		}
		
		return null;
	}
	
	@Override
	public void setRow(int itemCode, int position, View convertView) {

		switch (itemCode) {
		
		case BCPConstants.ITEM_CAR_BID:
			if(position < 2) {
				convertView.setPadding(0, 0, 0, 0);
			} else if(position == models.size() - 1) {
				convertView.setPadding(0, 0, 0, ResizeUtils.getSpecificLength(38));
			} else {
				convertView.setPadding(0, ResizeUtils.getSpecificLength(16), 0, 0);
			}
			
			break;
		
		case BCPConstants.ITEM_NOTIFICATION:
			
			if(position == 0) {
				convertView.setPadding(0, ResizeUtils.getSpecificLength(38), 0, 0);
				
			} else if(position == models.size() - 1) {
				convertView.setPadding(0, 0, 0, ResizeUtils.getSpecificLength(38));
				
			} else {
				convertView.setPadding(0, 0, 0, 0);
			}
			
			break;
		
		case BCPConstants.ITEM_NOTICE:
		case BCPConstants.ITEM_FAQ:	
			
			if(position == 0) {
				convertView.setPadding(0, ResizeUtils.getSpecificLength(0), 0, 0);
				
			} else if(position == models.size() - 1) {
				convertView.setPadding(0, 0, 0, ResizeUtils.getSpecificLength(26));
				
			} else {
				convertView.setPadding(0, 0, 0, 0);
			}
			break;
			
		case BCPConstants.ITEM_REVIEW:
		case BCPConstants.ITEM_CAR_MY_AUCTION:
		case BCPConstants.ITEM_CAR_MY_DEALER:
			
			if(position == 0) {
				convertView.setPadding(0, ResizeUtils.getSpecificLength(20), 0, 0);
				
			} else if(position == models.size() - 1) {
				convertView.setPadding(0, 0, 0, ResizeUtils.getSpecificLength(20));
				
			} else {
				convertView.setPadding(0, 0, 0, 0);
			}
			break;
		}
	}
}

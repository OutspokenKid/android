package com.byecar.classes;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.byecar.byecarplus.R;
import com.byecar.wrappers.ViewWrapperForArea;
import com.byecar.wrappers.ViewWrapperForAuctionCar;
import com.byecar.wrappers.ViewWrapperForBrand;
import com.byecar.wrappers.ViewWrapperForForum;
import com.byecar.wrappers.ViewWrapperForForumBest;
import com.byecar.wrappers.ViewWrapperForMyBidsReview;
import com.byecar.wrappers.ViewWrapperForMyCar;
import com.byecar.wrappers.ViewWrapperForMyReview;
import com.byecar.wrappers.ViewWrapperForNotification;
import com.byecar.wrappers.ViewWrapperForOpenablePost;
import com.byecar.wrappers.ViewWrapperForOtherCar;
import com.byecar.wrappers.ViewWrapperForSearchText;
import com.byecar.wrappers.ViewWrapperForVideo;
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
	
	public BCPAdapter(Context context, BCPFragmentActivity activity, 
			LayoutInflater inflater, ArrayList<BaseModel> models) {
		super(context, inflater, models);
		this.activity = activity;
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
			return R.layout.list_biddingcar;

		case BCPConstants.ITEM_CAR_DEALER:
		case BCPConstants.ITEM_CAR_DIRECT:
			return R.layout.list_othercar;
			
		case BCPConstants.ITEM_CAR_BRAND: 
			return R.layout.grid_brand;
			
		case BCPConstants.ITEM_CAR_TEXT:
		case BCPConstants.ITEM_CAR_TEXT_DESC:
			return R.layout.list_search_text;
			
		case BCPConstants.ITEM_CAR_MY:
		case BCPConstants.ITEM_CAR_MY_PURCHASE:
		case BCPConstants.ITEM_CAR_MY_LIKE:
			return R.layout.list_my_car;
			
		case BCPConstants.ITEM_FORUM:
			return R.layout.list_forum;
			
		case BCPConstants.ITEM_FORUM_BEST:
			return R.layout.list_forum_best;
			
		case BCPConstants.ITEM_VIDEO:
			return R.layout.list_video;
		
		case BCPConstants.ITEM_AREA_FOR_SEARCH:
			return R.layout.list_area;
			
		case BCPConstants.ITEM_MY_BIDS_REVIEW:
			return R.layout.list_my_bids_review;
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
			ViewWrapperForMyReview vwfm = new ViewWrapperForMyReview(convertView, itemCode);
			vwfm.setActivity(activity);
			return vwfm;
			
		case BCPConstants.ITEM_CAR_BID:
			ViewWrapperForAuctionCar vwfc = new ViewWrapperForAuctionCar(convertView, itemCode);
			vwfc.setActivity(activity);
			return vwfc;
			
		case BCPConstants.ITEM_CAR_DEALER:
		case BCPConstants.ITEM_CAR_DIRECT:
			ViewWrapperForOtherCar vwfoc = new ViewWrapperForOtherCar(convertView, itemCode);
			vwfoc.setActivity(activity);
			return vwfoc;
			
		case BCPConstants.ITEM_CAR_BRAND:
			ViewWrapperForBrand vwfb = new ViewWrapperForBrand(convertView, itemCode);
			vwfb.setActivity(activity);
			return vwfb;
			
		case BCPConstants.ITEM_CAR_TEXT:
		case BCPConstants.ITEM_CAR_TEXT_DESC:
			ViewWrapperForSearchText vwfst = new ViewWrapperForSearchText(convertView, itemCode);
			vwfst.setActivity(activity);
			return vwfst;
			
		case BCPConstants.ITEM_CAR_MY:
		case BCPConstants.ITEM_CAR_MY_PURCHASE:
		case BCPConstants.ITEM_CAR_MY_LIKE:
			ViewWrapperForMyCar vwfmc = new ViewWrapperForMyCar(convertView, itemCode);
			vwfmc.setActivity(activity);
			return vwfmc;
			
		case BCPConstants.ITEM_FORUM:
			return new ViewWrapperForForum(convertView, itemCode);
			
		case BCPConstants.ITEM_FORUM_BEST:
			ViewWrapperForForumBest vwffb = new ViewWrapperForForumBest(convertView, itemCode);
			vwffb.setActivity(activity);
			return vwffb;
			
		case BCPConstants.ITEM_VIDEO:
			return new ViewWrapperForVideo(convertView, itemCode);
			
		case BCPConstants.ITEM_AREA_FOR_SEARCH:
			return new ViewWrapperForArea(convertView, itemCode);
			
		case BCPConstants.ITEM_MY_BIDS_REVIEW:
			return new ViewWrapperForMyBidsReview(convertView, itemCode);
		}
		
		return null;
	}
	
	@Override
	public void setRow(int itemCode, int position, View convertView) {
		
		switch (itemCode) {

		case BCPConstants.ITEM_NOTIFICATION:
			
			if(position == 0) {
				convertView.setPadding(0, ResizeUtils.getSpecificLength(38), 0, 0);
				
			} else if(position == models.size() - 1) {
				convertView.setPadding(0, 0, 0, ResizeUtils.getSpecificLength(38));
				
			} else {
				convertView.setPadding(0, 0, 0, 0);
			}
			break;
		
		case BCPConstants.ITEM_CAR_DEALER:
		case BCPConstants.ITEM_CAR_DIRECT:
			
			if(position == 0) {
				convertView.setPadding(0, ResizeUtils.getSpecificLength(30), 0, 0);
				
			} else if(position == models.size() - 1) {
				convertView.setPadding(0, 0, 0, ResizeUtils.getSpecificLength(30));
				
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
		case BCPConstants.ITEM_CAR_MY:
		case BCPConstants.ITEM_CAR_MY_PURCHASE:
		case BCPConstants.ITEM_CAR_MY_LIKE:
		case BCPConstants.ITEM_MY_BIDS_REVIEW:

			if(position == 0) {
				convertView.setPadding(0, ResizeUtils.getSpecificLength(20), 0, 0);
			
			} else if(position == models.size() - 1) {
				convertView.setPadding(0, 0, 0, ResizeUtils.getSpecificLength(20));
				
			} else {
				convertView.setPadding(0, 0, 0, 0);
			}
			break;
			
		case BCPConstants.ITEM_VIDEO:

			if(position == 0) {
				convertView.setPadding(0, ResizeUtils.getSpecificLength(26), 0, 0);
			
			} else if(position == models.size() - 1) {
				convertView.setPadding(0, 0, 0, ResizeUtils.getSpecificLength(26));
				
			} else {
				convertView.setPadding(0, 0, 0, 0);
			}
			break;
		}
	}
}

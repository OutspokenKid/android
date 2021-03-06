package com.cmons.cph.classes;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.cmons.cph.R;
import com.cmons.cph.wrappers.ViewWrapperForAccount;
import com.cmons.cph.wrappers.ViewWrapperForCategory;
import com.cmons.cph.wrappers.ViewWrapperForCustomer_retail;
import com.cmons.cph.wrappers.ViewWrapperForCustomer_wholesale;
import com.cmons.cph.wrappers.ViewWrapperForFavoriteShop;
import com.cmons.cph.wrappers.ViewWrapperForFloor;
import com.cmons.cph.wrappers.ViewWrapperForLine;
import com.cmons.cph.wrappers.ViewWrapperForNotice;
import com.cmons.cph.wrappers.ViewWrapperForNotification;
import com.cmons.cph.wrappers.ViewWrapperForOrder;
import com.cmons.cph.wrappers.ViewWrapperForOrderSet;
import com.cmons.cph.wrappers.ViewWrapperForOrderSetForWholesale2;
import com.cmons.cph.wrappers.ViewWrapperForProduct;
import com.cmons.cph.wrappers.ViewWrapperForReply;
import com.cmons.cph.wrappers.ViewWrapperForSample;
import com.cmons.cph.wrappers.ViewWrapperForStaff;
import com.cmons.cph.wrappers.ViewWrapperForWholesaleWish;
import com.cmons.cph.wrappers.ViewWrapperForWish;
import com.outspoken_kid.classes.OutSpokenAdapter;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;

/**
 * @author HyungGunKim
 *
 */
public class CphAdapter extends OutSpokenAdapter {
	
	public CphAdapter(Context context, LayoutInflater inflater, ArrayList<BaseModel> models) {

		super(context, inflater, models);
	}

	@Override
	public int getLayoutResIdByItemCode(int itemCode) {
		
		switch (itemCode) {
		
		case CphConstants.ITEM_NOTICE:
		case CphConstants.ITEM_NOTIFICATION:
			return R.layout.list_notice;
			
		case CphConstants.ITEM_CATEGORY:
			return R.layout.list_signup_category;
			
		case CphConstants.ITEM_FLOOR:
			return R.layout.list_floor;
			
		case CphConstants.ITEM_LINE:
			return R.layout.list_line;
			
		case CphConstants.ITEM_PRODUCT:
			return R.layout.grid_product;
			
		case CphConstants.ITEM_SAMPLE:
			return R.layout.list_sample;
			
		case CphConstants.ITEM_CUSTOMER_WHOLESALE:
			return R.layout.list_customerlist_wholesale;
			
		case CphConstants.ITEM_CUSTOMER_RETAIL:
			return R.layout.list_customerlist_retail;
			
		case CphConstants.ITEM_STAFF:
			return R.layout.list_staff;
			
		case CphConstants.ITEM_ORDERSET_WHOLESALE:
			return R.layout.list_orderset;
			
		case CphConstants.ITEM_ORDERSET_WHOLESALE2:
			return R.layout.list_orderset_wholesale2;
			
		case CphConstants.ITEM_ORDERSET_RETAIL:
			return R.layout.list_orderset;
			
		case CphConstants.ITEM_ACCOUNT:
			return R.layout.list_account;
			
		case CphConstants.ITEM_WHOLESALE_WISH:
			return R.layout.list_wholesale_wish;
			
		case CphConstants.ITEM_WISH:
			return R.layout.list_wish;
			
		case CphConstants.ITEM_WHOLESALE:
			return R.layout.list_favorite_shop;
			
		case CphConstants.ITEM_REPLY:
			return R.layout.list_reply;
			
		case CphConstants.ITEM_ORDER_WHOLESALE:
		case CphConstants.ITEM_ORDER_RETAIL:
			return R.layout.list_order;
		}
		
		return 0;
	}
	
	@Override
	public ViewWrapper getViewWrapperByItemCode(View convertView, int itemCode) {
		
		switch (itemCode) {
		
		case CphConstants.ITEM_NOTICE:
			return new ViewWrapperForNotice(convertView, itemCode);
			
		case CphConstants.ITEM_NOTIFICATION:
			return new ViewWrapperForNotification(convertView, itemCode);
			
		case CphConstants.ITEM_CATEGORY:
			return new ViewWrapperForCategory(convertView, itemCode);
			
		case CphConstants.ITEM_FLOOR:
			return new ViewWrapperForFloor(convertView, itemCode);
			
		case CphConstants.ITEM_LINE:
			return new ViewWrapperForLine(convertView, itemCode);
			
		case CphConstants.ITEM_PRODUCT:
			return new ViewWrapperForProduct(convertView, itemCode);
			
		case CphConstants.ITEM_SAMPLE:
			return new ViewWrapperForSample(convertView, itemCode);
		
		case CphConstants.ITEM_CUSTOMER_WHOLESALE:
			return new ViewWrapperForCustomer_wholesale(convertView, itemCode);
			
		case CphConstants.ITEM_CUSTOMER_RETAIL:
			return new ViewWrapperForCustomer_retail(convertView, itemCode);
			
		case CphConstants.ITEM_STAFF:
			return new ViewWrapperForStaff(convertView, itemCode);
			
		case CphConstants.ITEM_ORDERSET_WHOLESALE:
			return new ViewWrapperForOrderSet(convertView, itemCode);
			
		case CphConstants.ITEM_ORDERSET_WHOLESALE2:
			return new ViewWrapperForOrderSetForWholesale2(convertView, itemCode);
			
		case CphConstants.ITEM_ORDERSET_RETAIL:
			return new ViewWrapperForOrderSet(convertView, itemCode);
			
		case CphConstants.ITEM_ACCOUNT:
			return new ViewWrapperForAccount(convertView, itemCode);
			
		case CphConstants.ITEM_WHOLESALE_WISH:
			return new ViewWrapperForWholesaleWish(convertView, itemCode);
			
		case CphConstants.ITEM_WISH:
			return new ViewWrapperForWish(convertView, itemCode);
			
		case CphConstants.ITEM_WHOLESALE:
			return new ViewWrapperForFavoriteShop(convertView, itemCode);
			
		case CphConstants.ITEM_REPLY:
			return new ViewWrapperForReply(convertView, itemCode);
			
		case CphConstants.ITEM_ORDER_WHOLESALE:	
		case CphConstants.ITEM_ORDER_RETAIL:
			return new ViewWrapperForOrder(convertView, itemCode);
		}
		
		return null;
	}
	
	@Override
	public void setRow(int itemCode, int position, View convertView) {
	
		switch (itemCode) {
		
		case CphConstants.ITEM_PRODUCT:
			break;
		}
	}
}

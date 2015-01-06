package com.byecar.byecarplus.wrappers;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.models.Review;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ViewWrapperForMyReview extends ViewWrapper {
	
	private Review review;
	
	private View bgTop;
	private TextView tvCarName;
	private Button btnEditReview;
	private TextView tvRegdate;
	private TextView tvTo;
	private TextView tvContent;
	private View bgBottom;
	
	public ViewWrapperForMyReview(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			bgTop = row.findViewById(R.id.list_my_review_bgTop);
			tvCarName = (TextView) row.findViewById(R.id.list_my_review_tvCarName);
			btnEditReview = (Button) row.findViewById(R.id.list_my_review_btnEditReview);
			tvRegdate = (TextView) row.findViewById(R.id.list_my_review_tvRegdate);
			tvTo = (TextView) row.findViewById(R.id.list_my_review_tvTo);
			tvContent = (TextView) row.findViewById(R.id.list_my_review_tvContent);
			bgBottom = row.findViewById(R.id.list_my_review_bgBottom);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			ResizeUtils.viewResizeForRelative(608, 100, bgTop, null, null, new int[]{0, 0, 0, 0});
			ResizeUtils.viewResizeForRelative(290, 66, tvCarName, null, null, new int[]{20, 4, 0, 0});
			ResizeUtils.viewResizeForRelative(88, 34, btnEditReview, null, null, new int[]{0, 20, 16, 0});
			ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 
					tvRegdate, null, null, new int[]{0, 6, 16, 0});
			ResizeUtils.viewResizeForRelative(608, LayoutParams.WRAP_CONTENT, tvContent, null, null, null);
			tvContent.setMinHeight(ResizeUtils.getSpecificLength(90));
			ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvTo, 
					null, null, new int[]{30, 0, 0, 0});
			ResizeUtils.viewResizeForRelative(608, 29, bgBottom, null, null, new int[]{0, 0, 0, 0});
			
			int p = ResizeUtils.getSpecificLength(30);
			tvContent.setPadding(p, p, p, 0);
			
			FontUtils.setFontSize(tvRegdate, 16);
			FontUtils.setFontSize(tvCarName, 30);
			FontUtils.setFontStyle(tvCarName, FontUtils.BOLD);
			FontUtils.setFontSize(tvTo, 20);
			FontUtils.setFontStyle(tvTo, FontUtils.BOLD);
			FontUtils.setFontSize(tvContent, 20);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Review) {
				review = (Review) baseModel;
				
				tvRegdate.setText(StringUtils.getDateString(
						"등록일 yyyy년 MM월 dd일", review.getCreated_at() * 1000));
				tvCarName.setText(review.getCar_full_name());
				
//				if(review.getCertifier_id() != 0) {
					tvTo.setText(review.getCertifier_name() + " 검증사에게");
//				} else if(review.getDealer_id() != 0) {
//					tvTo.setText(review.getDealer_name() + " 딜러에게");
//				}
				
				tvContent.setText(review.getContent());
			}
		} catch (Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {
//	
//		if(product != null) {
//			row.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View arg0) {
//
//					Bundle bundle = new Bundle();
//					bundle.putBoolean("isWholesale", product.getType() == Product.TYPE_WHOLESALE);
//					bundle.putSerializable("product", product);
//					ShopActivity.getInstance().showPage(CphConstants.PAGE_COMMON_PRODUCT, bundle);
//				}
//			});
//			
//			if(product.isDeletable()) {
//				
//				row.setOnLongClickListener(new OnLongClickListener() {
//					
//					@Override
//					public boolean onLongClick(View arg0) {
//						
//						ShopActivity.getInstance().showAlertDialog("삭제", "해당 물품을 삭제하시겠습니까?",
//								"확인", "취소", 
//								new DialogInterface.OnClickListener() {
//									
//									@Override
//									public void onClick(DialogInterface dialog, int which) {
//										
//										deleteFavorite(product);
//									}
//								}, null);
//						return false;
//					}
//				});
//			}
//			
//			replyIcon.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View view) {
//
//					Bundle bundle = new Bundle();
//					bundle.putSerializable("product", product);
//					ShopActivity.getInstance().showPage(CphConstants.PAGE_COMMON_REPLY, bundle);
//				}
//			});
//		}
	}
	
	@Override
	public void setUnusableView() {

	}
}

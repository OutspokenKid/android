package com.cmons.cph.wrappers;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.ShopActivity;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.Product;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class ViewWrapperForProduct extends ViewWrapper {
	
	private Product product;

	public ImageView ivImage;
	public TextView tvTitle;
	public TextView tvPrice;
	public TextView tvBuyCount;
	public Button replyIcon;
	public View heartIcon;
	
	public ViewWrapperForProduct(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			ivImage = (ImageView) row.findViewById(R.id.grid_product_ivImage);
			tvTitle = (TextView) row.findViewById(R.id.grid_product_tvTitle);
			tvPrice = (TextView) row.findViewById(R.id.grid_product_tvPrice);
			tvBuyCount = (TextView) row.findViewById(R.id.grid_product_tvBuyCount);
			replyIcon = (Button) row.findViewById(R.id.grid_product_replyIcon);
			heartIcon = row.findViewById(R.id.grid_product_heartIcon);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			AbsListView.LayoutParams al = new AbsListView.LayoutParams(
					ResizeUtils.getScreenWidth()/2, 
					ResizeUtils.getSpecificLength(560));
			row.setLayoutParams(al);
			
			RelativeLayout.LayoutParams rp = null;
				
			//tvTitle.
			rp = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(92);
			tvTitle.setPadding(ResizeUtils.getSpecificLength(14), 0, 
					ResizeUtils.getSpecificLength(120), 0);
			
			//tvPrice.
			rp = (RelativeLayout.LayoutParams) tvPrice.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(50);
			rp.rightMargin = ResizeUtils.getSpecificLength(20);
			
			//tvBuyCount.
			rp = (RelativeLayout.LayoutParams) tvBuyCount.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(24);
			
			//heartIcon.
			rp = (RelativeLayout.LayoutParams) heartIcon.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(27);
			rp.height = ResizeUtils.getSpecificLength(24);
			rp.rightMargin = ResizeUtils.getSpecificLength(4);
			
			//replyIcon.
			rp = (RelativeLayout.LayoutParams) replyIcon.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(50);
			rp.height = ResizeUtils.getSpecificLength(50);
			rp.rightMargin = ResizeUtils.getSpecificLength(10);
			rp.topMargin = ResizeUtils.getSpecificLength(10);
			
			FontUtils.setFontSize(tvTitle, 24);
			FontUtils.setFontSize(tvBuyCount, 18);
			FontUtils.setFontSize(tvPrice, 22);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Product) {
				tvTitle.setVisibility(View.VISIBLE);
				tvPrice.setVisibility(View.VISIBLE);
				tvBuyCount.setVisibility(View.VISIBLE);
				replyIcon.setVisibility(View.VISIBLE);
				heartIcon.setVisibility(View.VISIBLE);
				
				product = (Product) baseModel;

				tvTitle.setText(product.getName());
				tvBuyCount.setText("" + product.getFavorited_cnt());
				
				if(product.getPrice() > 1000000) {
					tvPrice.setText("100만원 이상");
				} else {
					tvPrice.setText(StringUtils.getFormattedNumber(product.getPrice()) + "원");
				}
				
				if(product.getProduct_images() != null
						&& product.getProduct_images()[0] != null) {
					setImage(ivImage, product.getProduct_images()[0]);
				} else {
					ivImage.setImageBitmap(null);
				}
			} else {
				setUnusableView();
			}
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {
	
		if(product != null) {
			row.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {

					Bundle bundle = new Bundle();
					bundle.putBoolean("isWholesale", product.getType() == Product.TYPE_WHOLESALE);
					bundle.putSerializable("product", product);
					ShopActivity.getInstance().showPage(CphConstants.PAGE_COMMON_PRODUCT, bundle);
				}
			});
			
			if(product.isDeletable()) {
				
				row.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View arg0) {
						
						ShopActivity.getInstance().showAlertDialog("삭제", "해당 물품을 삭제하시겠습니까?",
								"확인", "취소", 
								new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										
										deleteFavorite(product);
									}
								}, null);
						return false;
					}
				});
			}
			
			replyIcon.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					Bundle bundle = new Bundle();
					bundle.putSerializable("product", product);
					ShopActivity.getInstance().showPage(CphConstants.PAGE_COMMON_REPLY, bundle);
				}
			});
		}
	}
	
	@Override
	public void setUnusableView() {

		tvTitle.setVisibility(View.INVISIBLE);
		tvPrice.setVisibility(View.INVISIBLE);
		tvBuyCount.setVisibility(View.INVISIBLE);
		replyIcon.setVisibility(View.INVISIBLE);
		heartIcon.setVisibility(View.INVISIBLE);
		ivImage.setImageDrawable(null);
	}

//////////////////// Custom methods.

	public void deleteFavorite(Product product) {

		String url = CphConstants.BASE_API_URL + "retails/favorite/products/delete" +
				"?id=" + product.getId();
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("RetailForFavoriteProductPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToDeleteFavorite);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("RetailForFavoriteProductPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_deleteFavorite);
						ShopActivity.getInstance().getTopFragment().refreshPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToDeleteFavorite);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToDeleteFavorite);
					LogUtils.trace(oom);
				}
			}
		});
	}
}

package com.cmons.cph.wrappers;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.ShopActivity;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.fragments.wholesale.WholesaleForOrderPage;
import com.cmons.cph.models.Order;
import com.cmons.cph.models.Product;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class ViewWrapperForOrder extends ViewWrapper {
	
	private Order order;
	
	public TextView tvOrder;
	public TextView tvPrice;
	public View checkbox;
	
	public ViewWrapperForOrder(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			tvOrder = (TextView) row.findViewById(R.id.list_order_tvOrder);
			tvPrice = (TextView) row.findViewById(R.id.list_order_tvPrice);
			checkbox = row.findViewById(R.id.list_order_checkbox);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			RelativeLayout.LayoutParams rp = null;
			
			rp = (RelativeLayout.LayoutParams) tvOrder.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(100);
			rp.rightMargin = ResizeUtils.getSpecificLength(100);
			FontUtils.setFontSize(tvOrder, 30);
			tvOrder.setPadding(ResizeUtils.getSpecificLength(20), 0, 
					ResizeUtils.getSpecificLength(20), 0);
			
			FontUtils.setFontSize(tvPrice, 30);
			tvPrice.setPadding(0, 0, ResizeUtils.getSpecificLength(20), 0);

			
			rp = (RelativeLayout.LayoutParams) checkbox.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(44);
			rp.height = ResizeUtils.getSpecificLength(43);
			rp.rightMargin = ResizeUtils.getSpecificLength(20);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Order) {
				
				order = (Order) baseModel;
				
				if(order.getItemCode() == CphConstants.ITEM_ORDER_WHOLESALE) {
					tvOrder.setText(order.getProduct_name() + " / " +
							order.getSize() + " / " +
							order.getColor() + " / " + 
							order.getAmount());
					tvPrice.setText(null);
					checkbox.setVisibility(View.VISIBLE);
					
					if(order.isChecked()) {
						checkbox.setBackgroundResource(R.drawable.order_check_box_b);
					} else {
						checkbox.setBackgroundResource(R.drawable.order_check_box_a);
					}
				} else {
					tvOrder.setText(order.getProduct_name() + " (" +
							order.getSize() + "/" +
							order.getColor() + "/" + 
							order.getAmount() + "개)");
					
					if(order.getParentStatus() != 0
							&& order.getStatus() == 0) {
						tvPrice.setText("품절");
					} else {
						long sum = order.getAmount() * order.getProduct_price();
						tvPrice.setText(StringUtils.getFormattedNumber(sum) + "원");
					}
					
					checkbox.setVisibility(View.INVISIBLE);
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
		
		if(order.getParentStatus() == 0) {
			row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					if(order.getStatus() != 0) {
						return;
					}
					
					order.setChecked(!order.isChecked());

					if(ShopActivity.getInstance().getTopFragment() instanceof WholesaleForOrderPage) {
						
						long addedPrice = 0;
						
						//추가된 경우.
						if(order.isChecked()) {
							addedPrice = order.getProduct_price() * order.getAmount();
							checkbox.setBackgroundResource(R.drawable.order_check_box_b);
						//삭제된 경우.
						} else {
							addedPrice = -order.getProduct_price() * order.getAmount();
							checkbox.setBackgroundResource(R.drawable.order_check_box_a);
						}
						
						((WholesaleForOrderPage)ShopActivity.getInstance().getTopFragment()).addToTotalPrice(addedPrice);
					}
				}
			});
		}
		
		tvOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				LogUtils.log("###ViewWrapperForOrder.row.onClick.  ");
				
				String url = CphConstants.BASE_API_URL + "products/show" +
						"?product_id=" + order.getProduct_id();
				DownloadUtils.downloadJSONString(url,
						new OnJSONDownloadListener() {

							@Override
							public void onError(String url) {

								LogUtils.log("ViewWrapperForOrder.onError." + "\nurl : " + url);

							}

							@Override
							public void onCompleted(String url,
									JSONObject objJSON) {

								try {
									LogUtils.log("ViewWrapperForOrder.onCompleted."
											+ "\nurl : " + url + "\nresult : "
											+ objJSON);

									if(objJSON.getInt("result") == 1) {
										showProductPage(new Product(objJSON.getJSONObject("product")));
									} else {
										ToastUtils.showToast(objJSON.getString("message"));
									}
								} catch (Exception e) {
									LogUtils.trace(e);
								} catch (OutOfMemoryError oom) {
									LogUtils.trace(oom);
								}
							}
						});
			}
		});
	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}
	
	public void showProductPage(Product product) {
		
		LogUtils.log("###ViewWrapperForOrder.showProductPage.  ");
		
		try {
			Bundle bundle = new Bundle();
			bundle.putSerializable("product", product);
			bundle.putBoolean("isWholesale", (ShopActivity.getInstance().user.getWholesale_id() != 0? true : false));
			ShopActivity.getInstance().showPage(CphConstants.PAGE_COMMON_PRODUCT, bundle);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}

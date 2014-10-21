package com.cmons.cph.views;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.ShopActivity;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.fragments.wholesale.WholesaleForOrderPage;
import com.cmons.cph.models.Order;
import com.cmons.cph.models.Product;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class OrderView extends RelativeLayout {

	private Order order;
	private Context context;
	
	public TextView tvOrder;
	public TextView tvPrice;
	public View checkbox;
	
	public OrderView(Context context) {
		this(context, null, 0);
	}
	
	public OrderView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public OrderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public void init() {

		this.setBackgroundColor(Color.TRANSPARENT);
		
		RelativeLayout.LayoutParams rp = null;
		
		tvOrder = new TextView(context);
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 
				LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		rp.addRule(RelativeLayout.CENTER_VERTICAL);
		rp.rightMargin = ResizeUtils.getSpecificLength(100);
		tvOrder.setLayoutParams(rp);
		tvOrder.setTextColor(Color.WHITE);
		FontUtils.setFontSize(tvOrder, 30);
		tvOrder.setPadding(ResizeUtils.getSpecificLength(20), 0, 
				ResizeUtils.getSpecificLength(20), 0);
		this.addView(tvOrder);
		
		tvPrice = new TextView(context);
		rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 
				LayoutParams.WRAP_CONTENT);
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rp.addRule(RelativeLayout.CENTER_VERTICAL);
		tvPrice.setLayoutParams(rp);
		tvPrice.setTextColor(Color.RED);
		FontUtils.setFontSize(tvPrice, 30);
		tvPrice.setPadding(0, 0, ResizeUtils.getSpecificLength(20), 0);
		this.addView(tvPrice);
		
		checkbox = new View(context);
		rp = new RelativeLayout.LayoutParams(
				ResizeUtils.getSpecificLength(44), 
				ResizeUtils.getSpecificLength(43));
		rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		rp.addRule(RelativeLayout.CENTER_VERTICAL);
		rp.rightMargin = ResizeUtils.getSpecificLength(20);
		checkbox.setLayoutParams(rp);
		this.addView(checkbox);
		
		View line = new View(context);
		rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1);
		rp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		line.setLayoutParams(rp);
		line.setBackgroundColor(Color.WHITE);
		this.addView(line);
	}

	public void setListeners() {
		
		if(order.getParentStatus() == 0) {
			this.setOnClickListener(new OnClickListener() {

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

				if(order == null) {
					return;
				}
				
				String url = CphConstants.BASE_API_URL + "products/show" +
						"?product_id=" + order.getProduct_id();
				DownloadUtils.downloadJSONString(url,
						new OnJSONDownloadListener() {

							@Override
							public void onError(String url) {

								LogUtils.log("OrderView.onError." + "\nurl : " + url);

							}

							@Override
							public void onCompleted(String url,
									JSONObject objJSON) {

								try {
									LogUtils.log("OrderView.onCompleted."
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
	
	public void setValues(Order order) {
		
		this.order = order;
		
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
		
		setListeners();
	}
	
	public void showProductPage(Product product) {
		
		LogUtils.log("###OrderView.showProductPage.  ");
		
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

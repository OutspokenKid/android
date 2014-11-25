package com.cmons.cph.wrappers;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.ShopActivity;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.fragments.retail.RetailForCustomerListPage;
import com.cmons.cph.models.Retail;
import com.cmons.cph.models.Wholesale;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class ViewWrapperForCustomer_retail extends ViewWrapper {
	
	private Wholesale wholesale;
	private Retail retail;
	
	public TextView tvCustomer;
	public TextView tvStatus;
	
	public ViewWrapperForCustomer_retail(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			tvCustomer = (TextView) row.findViewById(R.id.list_customer_tvCustomer);
			tvStatus = (TextView) row.findViewById(R.id.list_customer_tvStatus);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			int p = ResizeUtils.getSpecificLength(20);
			tvCustomer.setMaxWidth(ResizeUtils.getSpecificLength(540));
			tvCustomer.setPadding(p, p, p, p);
			FontUtils.setFontSize(tvCustomer, 30);
			
			tvStatus.setPadding(0, 0, p, 0);
			FontUtils.setFontSize(tvStatus, 30);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel.getItemCode() == CphConstants.ITEM_CUSTOMER_WHOLESALE) {
				
				if(baseModel instanceof Retail) {
					retail = (Retail) baseModel;
					
					tvCustomer.setText("도매 - 거래처목록 - " + retail.getName());
					tvStatus.setText(null);
				} else {
					setUnusableView();
				}
				
			} else if(baseModel.getItemCode() == CphConstants.ITEM_CUSTOMER_RETAIL) {
				
				if(baseModel instanceof Wholesale) {
					wholesale = (Wholesale) baseModel;
					tvCustomer.setText(wholesale.getName() + " 청평화몰 " + wholesale.getLocation());
					
					if(wholesale.isStandingBy()) {
						tvStatus.setText("승인대기중");
					} else {
						tvStatus.setText(null);
					}
				} else {
					setUnusableView();
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {
		
		if(wholesale != null) {
			row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					Bundle bundle = new Bundle();
					bundle.putSerializable("wholesale", wholesale);
					ShopActivity.getInstance().showPage(CphConstants.PAGE_RETAIL_SHOP, bundle);
				}
			});
			
			row.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					
					String message = null;
					
					if(wholesale.isStandingBy()) {
						message = "거래처 요청을 취소하시겠습니까?";
					} else {
						message = "거래처를 끊겠습니까?";
					}
					
					ShopActivity.getInstance().showAlertDialog("삭제", message, 
							"확인", "취소", 
							new DialogInterface.OnClickListener(){

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									
									if(wholesale.isStandingBy()) {
										cancelRequest();
									} else {
										breakPartnerShip();
									}
								}
					}, null);
					
					return false;
				}
			});
		}
	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}

//////////////////// Custom methods.
	
	public void cancelRequest() {
		
		String url = CphConstants.BASE_API_URL + "retails/customers/cancel" +
				"?wholesale_id=" + wholesale.getId();
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ViewWrapperForCustomer.cancelRequest.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToCancelRequest);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("ViewWrapperForCustomer.cancelRequest.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_cancelRequest);
						refreshPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToCancelRequest);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToCancelRequest);
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void breakPartnerShip() {
		
		String url = CphConstants.BASE_API_URL + "retails/customers/delete" +
				"?wholesale_id=" + wholesale.getId();

		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ViewWrapperForCustomer.breakPartnerShip.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failTobreakPartnerShip);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("ViewWrapperForCustomer.breakPartnerShip.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_breakPartnerShip);
						refreshPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failTobreakPartnerShip);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failTobreakPartnerShip);
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void refreshPage() {

		int menuIndex = ((RetailForCustomerListPage)ShopActivity.getInstance().getTopFragment()).getMenuIndex(); 
		((RetailForCustomerListPage)ShopActivity.getInstance().getTopFragment()).setMenu(menuIndex);
	}
}

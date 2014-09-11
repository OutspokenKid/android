package com.cmons.cph.fragments.retail;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForRetail;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;

public class RetailForManagementPage extends CmonsFragmentForRetail {

	private View icon;
	private TextView tvNameText;
	private TextView tvName;
	private TextView tvRegText;
	private TextView tvReg;
	private View changeReg;
	private TextView tvOwnerNameText;
	private TextView tvOwnerName;
	private TextView tvPhoneText;
	private TextView tvPhone;
	private View changePhone;
	private TextView tvAddressText;
	private TextView tvAddress;
	private View changeAddress;
	
	@Override
	public void bindViews() {
		
		titleBar = (TitleBar) mThisView.findViewById(R.id.retailManagementPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.retailManagementPage_ivBg);
		
		icon = mThisView.findViewById(R.id.retailManagementPage_icon);
		tvNameText = (TextView) mThisView.findViewById(R.id.retailManagementPage_tvNameText);
		tvName = (TextView) mThisView.findViewById(R.id.retailManagementPage_tvName);
		tvRegText = (TextView) mThisView.findViewById(R.id.retailManagementPage_tvRegText);
		tvReg = (TextView) mThisView.findViewById(R.id.retailManagementPage_tvReg);
		changeReg = mThisView.findViewById(R.id.retailManagementPage_changeReg);
		tvOwnerNameText = (TextView) mThisView.findViewById(R.id.retailManagementPage_tvOwnerNameText);
		tvOwnerName = (TextView) mThisView.findViewById(R.id.retailManagementPage_tvOwnerName);
		tvPhoneText = (TextView) mThisView.findViewById(R.id.retailManagementPage_tvPhoneText);
		tvPhone = (TextView) mThisView.findViewById(R.id.retailManagementPage_tvPhone);
		changePhone = mThisView.findViewById(R.id.retailManagementPage_changePhone);
		tvAddressText = (TextView) mThisView.findViewById(R.id.retailManagementPage_tvAddressText);
		tvAddress = (TextView) mThisView.findViewById(R.id.retailManagementPage_tvAddress);
		changeAddress = mThisView.findViewById(R.id.retailManagementPage_changeAddress);
	}

	@Override
	public void setVariables() {

		title = "매장관리";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);

		tvName.setText(getRetail().getName());
		tvReg.setText(getRetail().getCorp_reg_number());
		tvOwnerName.setText(getRetail().getOwner_name());
		tvPhone.setText(getRetail().getPhone_number());
		tvAddress.setText(getRetail().getAddress());
		
		if(StringUtils.isEmpty(getRetail().getMall_url())) {
			icon.setBackgroundResource(R.drawable.offline_shop_icon);
		} else {
			icon.setBackgroundResource(R.drawable.online_shop_icon);
		}
	}

	@Override
	public void setListeners() {	

		changeReg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				SoftKeyboardUtils.showKeyboard(mContext, tvPhone);
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
				// Setting Dialog Title
		        alertDialog.setTitle("사업자 등록번호 변경");
		        
		        final EditText input = new EditText(mContext);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				lp.leftMargin = ResizeUtils.getSpecificLength(10);
				lp.rightMargin = ResizeUtils.getSpecificLength(10);
				input.setLayoutParams(lp);
				input.setInputType(InputType.TYPE_CLASS_NUMBER);
				alertDialog.setView(input);
				
		        alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
				
						if(input.getText() == null ||input.getText().length() == 0) {
							return;
						}
						
						String keyword = input.getText().toString();
						changeReg(keyword);
					}
				});
		        alertDialog.setNegativeButton("취소", null);
				
				// Showing Alert Message
		        alertDialog.show();
			}
		});
		
		changePhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				SoftKeyboardUtils.showKeyboard(mContext, tvPhone);
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
				// Setting Dialog Title
		        alertDialog.setTitle("연락처 변경");
		        
		        final EditText input = new EditText(mContext);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				lp.leftMargin = ResizeUtils.getSpecificLength(10);
				lp.rightMargin = ResizeUtils.getSpecificLength(10);
				input.setLayoutParams(lp);
				input.setInputType(InputType.TYPE_CLASS_PHONE);
				alertDialog.setView(input);
				
		        alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
				
						if(input.getText() == null ||input.getText().length() == 0) {
							return;
						}
						
						String keyword = input.getText().toString();
						changePhoneNumber(keyword);
					}
				});
		        alertDialog.setNegativeButton("취소", null);
				
				// Showing Alert Message
		        alertDialog.show();
			}
		});
		
		changeAddress.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				SoftKeyboardUtils.showKeyboard(mContext, tvPhone);
				
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
				// Setting Dialog Title
		        alertDialog.setTitle("매장 주소 변경");
		        
		        final EditText input = new EditText(mContext);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT);
				lp.leftMargin = ResizeUtils.getSpecificLength(10);
				lp.rightMargin = ResizeUtils.getSpecificLength(10);
				input.setLayoutParams(lp);
				input.setInputType(InputType.TYPE_CLASS_TEXT);
				alertDialog.setView(input);
				
		        alertDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
				
						if(input.getText() == null ||input.getText().length() == 0) {
							return;
						}
						
						String keyword = input.getText().toString();
						changeAddress(keyword);
					}
				});
		        alertDialog.setNegativeButton("취소", null);
				
				// Showing Alert Message
		        alertDialog.show();
			}
		});
	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;
		int height = ResizeUtils.getSpecificLength(92);
		int margin = ResizeUtils.getSpecificLength(20);
		
		//tvNameText.
		rp = (RelativeLayout.LayoutParams) tvNameText.getLayoutParams();
		rp.height = height;
		rp.leftMargin = margin;
		
		//icon.
		rp = (RelativeLayout.LayoutParams) icon.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(146);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.topMargin = ResizeUtils.getSpecificLength(34);
		rp.rightMargin = margin;
		
		//tvName.
		rp = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
		rp.height = height;
		
		//tvRegText.
		rp = (RelativeLayout.LayoutParams) tvRegText.getLayoutParams();
		rp.height = height;
		rp.leftMargin = margin;
		
		//tvReg.
		rp = (RelativeLayout.LayoutParams) tvReg.getLayoutParams();
		rp.height = height;
		
		//changeReg.
		rp = (RelativeLayout.LayoutParams) changeReg.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(50);
		rp.height = ResizeUtils.getSpecificLength(50);
		rp.topMargin = margin;
		rp.rightMargin = margin;
		
		//tvOwnerNameText.
		rp = (RelativeLayout.LayoutParams) tvOwnerNameText.getLayoutParams();
		rp.height = height;
		rp.leftMargin = margin;
		
		//tvOwnerName.
		rp = (RelativeLayout.LayoutParams) tvOwnerName.getLayoutParams();
		rp.height = height;
		
		//tvPhoneText.
		rp = (RelativeLayout.LayoutParams) tvPhoneText.getLayoutParams();
		rp.height = height;
		rp.leftMargin = margin;
		
		//tvPhone.
		rp = (RelativeLayout.LayoutParams) tvPhone.getLayoutParams();
		rp.height = height;
		
		//changePhone.
		rp = (RelativeLayout.LayoutParams) changePhone.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(50);
		rp.height = ResizeUtils.getSpecificLength(50);
		rp.topMargin = margin;
		rp.rightMargin = margin;
		
		//tvAddressText.
		rp = (RelativeLayout.LayoutParams) tvAddressText.getLayoutParams();
		rp.height = height;
		rp.leftMargin = margin;
		
		//tvAddress.
		rp = (RelativeLayout.LayoutParams) tvAddress.getLayoutParams();
		rp.height = height;
		
		//changeAddress.
		rp = (RelativeLayout.LayoutParams) changeAddress.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(50);
		rp.height = ResizeUtils.getSpecificLength(50);
		rp.topMargin = margin;
		rp.rightMargin = margin;

		FontUtils.setFontSize(tvNameText, 30);
		FontUtils.setFontSize(tvName, 30);
		FontUtils.setFontSize(tvRegText, 30);
		FontUtils.setFontSize(tvReg, 30);
		FontUtils.setFontSize(tvOwnerNameText, 30);
		FontUtils.setFontSize(tvOwnerName, 30);
		FontUtils.setFontSize(tvPhoneText, 30);
		FontUtils.setFontSize(tvPhone, 30);
		FontUtils.setFontSize(tvAddressText, 30);
		FontUtils.setFontSize(tvAddress, 30);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_retail_management;
	}

	@Override
	public void refreshPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.shop_bg;
	}

//////////////////// Custom methods.
	
	public void changePhoneNumber(final String phoneNumber) {

		try {
			//http://cph.minsangk.com/retails/update/phone_number?phone_number=1234
			String url = CphConstants.BASE_API_URL + "retails/update/phone_number" +
					"?phone_number=" + URLEncoder.encode(phoneNumber, "utf-8");
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("RetailForManagementPage.changePhoneNumber.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToChangePhoneNumber);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("RetailForManagementPage.changePhoneNumber.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 1) {
							ToastUtils.showToast(R.string.complete_changePhoneNumber);
							getRetail().setPhone_number(phoneNumber);
							tvPhone.setText(phoneNumber);
							SoftKeyboardUtils.hideKeyboard(mContext, tvPhone);
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToChangePhoneNumber);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToChangePhoneNumber);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void changeAddress(final String address) {

		try {
			//http://cph.minsangk.com/retails/update/address?address=B3%A11%EB%8F%99
			String url = CphConstants.BASE_API_URL + "retails/update/address"
					+ "?address=" + URLEncoder.encode(address, "utf-8");

			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("RetailForManagementPage.changeAddress.onError."
							+ "\nurl : " + url);
					ToastUtils.showToast(R.string.failToChangePhoneNumber);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("RetailForManagementPage.changeAddress.onCompleted."
								+ "\nurl : " + url + "\nresult : " + objJSON);

						if (objJSON.getInt("result") == 1) {
							ToastUtils.showToast(R.string.complete_changeAddress);
							getRetail().setAddress(address);
							tvAddress.setText(address);
							SoftKeyboardUtils.hideKeyboard(mContext, tvAddress);
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToChangeAddress);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToChangeAddress);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void changeReg(final String reg) {

		try {
			//http://cph.minsangk.com/retails/update/address?address=B3%A11%EB%8F%99
			String url = CphConstants.BASE_API_URL + "retails/update/corp_reg_number"
					+ "?corp_reg_number=" + URLEncoder.encode(reg, "utf-8");

			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("RetailForManagementPage.changeReg.onError."
							+ "\nurl : " + url);
					ToastUtils.showToast(R.string.failToChangeRegistration);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("RetailForManagementPage.changeReg.onCompleted."
								+ "\nurl : " + url + "\nresult : " + objJSON);

						if (objJSON.getInt("result") == 1) {
							ToastUtils.showToast(R.string.complete_changeRegistration);
							getRetail().setCorp_reg_number(reg);
							tvReg.setText(reg);
							SoftKeyboardUtils.hideKeyboard(mContext, tvReg);
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToChangeRegistration);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToChangeRegistration);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}

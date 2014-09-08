package com.cmons.cph.fragments.wholesale;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Retail;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.ToastUtils;

public class WholesaleForCustomerListPage extends CmonsFragmentForWholesale {

	private Button btnRequest;
	private Button btnPartner;
	private TextView tvCustomer;
	private EditText editText;
	private Button btnSearch;
	
	private ListView listView;
	
	private RelativeLayout popupRelative;
	private Button btnClose;
	private TextView tvShopName;
	private TextView tvOwnerName;
	private TextView tvPhone;
	private TextView tvCategory;
	private TextView tvAddress;
	private Button btnConfirm;
	
	private int menuIndex;
	
	private AlphaAnimation aaIn, aaOut;
	private boolean isAnimating;
	
	private Retail selectedRetail;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleCustomerListPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.wholesaleCustomerListPage_ivBg);
		
		btnRequest = (Button) mThisView.findViewById(R.id.wholesaleCustomerListPage_btnRequest);
		btnPartner = (Button) mThisView.findViewById(R.id.wholesaleCustomerListPage_btnPartner);
		tvCustomer = (TextView) mThisView.findViewById(R.id.wholesaleCustomerListPage_tvCustomer);
		editText = (EditText) mThisView.findViewById(R.id.wholesaleCustomerListPage_editText);
		btnSearch = (Button) mThisView.findViewById(R.id.wholesaleCustomerListPage_btnSearch);
		
		listView = (ListView) mThisView.findViewById(R.id.wholesaleCustomerListPage_listView);
		
		popupRelative = (RelativeLayout) mThisView.findViewById(R.id.wholesaleCustomerListPage_popupRelative);
		btnClose = (Button) mThisView.findViewById(R.id.wholesaleCustomerListPage_btnClose);
		tvShopName = (TextView) mThisView.findViewById(R.id.wholesaleCustomerListPage_tvShopName);
		tvOwnerName = (TextView) mThisView.findViewById(R.id.wholesaleCustomerListPage_tvOwnerName);
		tvPhone = (TextView) mThisView.findViewById(R.id.wholesaleCustomerListPage_tvPhone);
		tvCategory = (TextView) mThisView.findViewById(R.id.wholesaleCustomerListPage_tvCategory);
		tvAddress = (TextView) mThisView.findViewById(R.id.wholesaleCustomerListPage_tvAddress);
		btnConfirm = (Button) mThisView.findViewById(R.id.wholesaleCustomerListPage_btnConfirm);
	}

	@Override
	public void setVariables() {

		title = "거래처관리";
		
		AnimationListener al = new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				isAnimating = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				isAnimating = false;				
			}
		};
		
		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);
		aaIn.setAnimationListener(al);
		
		aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(300);
		aaOut.setAnimationListener(al);
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		
		LogUtils.log("###where.createPage.  menuIndex : " + menuIndex);
		
		if(menuIndex == 0) {
			tvCustomer.setVisibility(View.GONE);
			editText.setVisibility(View.GONE);
			btnSearch.setVisibility(View.GONE);
			btnRequest.setBackgroundResource(R.drawable.business_confirm_btn_a);
			btnPartner.setBackgroundResource(R.drawable.business_costumer_btn_b);
		} else {
			tvCustomer.setVisibility(View.VISIBLE);
			editText.setVisibility(View.VISIBLE);
			btnSearch.setVisibility(View.VISIBLE);
			btnRequest.setBackgroundResource(R.drawable.business_confirm_btn_b);
			btnPartner.setBackgroundResource(R.drawable.business_costumer_btn_a);
		}
	}

	@Override
	public void setListeners() {

		btnRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(0);
			}
		});
		
		btnPartner.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(1);
			}
		});
		
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(editText.getText() == null) {
					ToastUtils.showToast(R.string.wrongSearchKeyword);
					return;
				}

				refreshPage();
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView, int position,
					long id) {
				
				selectedRetail = (Retail) models.get(position);
				
				if(menuIndex == 0) {
					showPopup(selectedRetail);
				} else {
					Bundle bundle = new Bundle();
					bundle.putSerializable("retail", selectedRetail);
					mActivity.showPage(CphConstants.PAGE_WHOLESALE_CUSTOMER, bundle);
				}
			}
		});

		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hidePopup();
			}
		});
		
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				approval(selectedRetail);
			}
		});
	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;
		
		//btnRequest.
		rp = (RelativeLayout.LayoutParams) btnRequest.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/2;
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//btnPartner.
		rp = (RelativeLayout.LayoutParams) btnPartner.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvCustomer.
		int p = ResizeUtils.getSpecificLength(10);
		tvCustomer.setPadding(p, p/2, p, p);
		FontUtils.setFontSize(tvCustomer, 30);
		
		//editText.
		rp = (RelativeLayout.LayoutParams) editText.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(550);
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontAndHintSize(editText, 30, 24);

		//btnSearch.
		rp = (RelativeLayout.LayoutParams) btnSearch.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//popupRelative.
		rp = (RelativeLayout.LayoutParams) popupRelative.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(626);
		rp.height = ResizeUtils.getSpecificLength(705);
		p = ResizeUtils.getSpecificLength(22);
		popupRelative.setPadding(p, p, p, p);
		
		//btnClose.
		rp = (RelativeLayout.LayoutParams) btnClose.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(52);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		rp.rightMargin = ResizeUtils.getSpecificLength(8);
		
		//tvShopName.
		rp = (RelativeLayout.LayoutParams) tvShopName.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(88);
		rp.topMargin = ResizeUtils.getSpecificLength(64);
		tvShopName.setPadding(p, 0, p, 0);
		FontUtils.setFontSize(tvShopName, 28);
		
		//tvOwnerName.
		rp = (RelativeLayout.LayoutParams) tvOwnerName.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(88);
		tvOwnerName.setPadding(p, 0, p, 0);
		FontUtils.setFontSize(tvOwnerName, 28);
		
		//tvPhone.
		rp = (RelativeLayout.LayoutParams) tvPhone.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(88);
		tvPhone.setPadding(p, 0, p, 0);
		FontUtils.setFontSize(tvPhone, 28);
		
		//tvCategory.
		rp = (RelativeLayout.LayoutParams) tvCategory.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(88);
		tvCategory.setPadding(p, 0, p, 0);
		FontUtils.setFontSize(tvCategory, 28);
		
		//tvAddress.
		rp = (RelativeLayout.LayoutParams) tvAddress.getLayoutParams();
		tvAddress.setPadding(p, p, p, p);
		FontUtils.setFontSize(tvAddress, 28);
		
		//btnConfirm.
		rp = (RelativeLayout.LayoutParams) btnConfirm.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(209);
		rp.height = ResizeUtils.getSpecificLength(62);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_customerlist;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {
		
		if(popupRelative.getVisibility() == View.VISIBLE) {
			hidePopup();
			return true;
		}
		
		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		
		try {
			JSONArray arJSON = null;
			
			if(menuIndex == 0) {
				arJSON = objJSON.getJSONArray("requestedCustomers");
			} else {
				arJSON = objJSON.getJSONArray("customers");
			}
			
			int size = arJSON.length();
			for(int i=0; i<size; i++) {
				Retail retail = new Retail(arJSON.getJSONObject(i));
				retail.setItemCode(CphConstants.ITEM_CUSTOMER_WHOLESALE);
				models.add(retail);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return false;
	}

	@Override
	public void downloadInfo() {
		
		if(menuIndex == 0) {
			//http://cph.minsangk.com/wholesales/customers/requested
			url = CphConstants.BASE_API_URL + "wholesales/customers/requested";
		} else {
			//http://cph.minsangk.com/wholesales/customers
			url = CphConstants.BASE_API_URL + "wholesales/customers";
			
			try {
				if(editText.getText() != null) {
					url += "?keyword=" + URLEncoder.encode(editText.getText().toString(), "utf-8");
				}
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
		
		super.downloadInfo();
	}
	
////////////////////Custom methods.
	
	public void setMenu(int menuIndex) {
		
		switch (menuIndex) {
		
		case 0:
			btnRequest.setBackgroundResource(R.drawable.business_confirm_btn_a);
			btnPartner.setBackgroundResource(R.drawable.business_costumer_btn_b);
			tvCustomer.setVisibility(View.GONE);
			editText.setVisibility(View.GONE);
			btnSearch.setVisibility(View.GONE);
			break;
			
		case 1:
			btnRequest.setBackgroundResource(R.drawable.business_confirm_btn_b);
			btnPartner.setBackgroundResource(R.drawable.business_costumer_btn_a);
			tvCustomer.setVisibility(View.VISIBLE);
			editText.setVisibility(View.VISIBLE);
			btnSearch.setVisibility(View.VISIBLE);
			break;
		}
		
		this.menuIndex = menuIndex;
		
		refreshPage();
	}

	public void showPopup(Retail retail) {

		if(isAnimating || popupRelative.getVisibility() == View.VISIBLE) {
			return;
		}
		
		tvShopName.setText("상호명 : " + retail.getName());
		tvOwnerName.setText("대표성함 : " + retail.getOwner_name());
		tvPhone.setText("연락처 : " + retail.getPhone_number());
		
		tvAddress.setText(null);
		SpannableStringBuilder sp1 = new SpannableStringBuilder("주소 : ");
		tvAddress.append(sp1);

		if(retail.getMall_url() == null) {
			SpannableStringBuilder sp2 = new SpannableStringBuilder(
					retail.getAddress());
			sp2.setSpan(new RelativeSizeSpan(0.8f), 0, sp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			tvAddress.append(sp2);
			
			tvCategory.setText("매장분류 : " + "오프라인");
		} else {
			SpannableStringBuilder sp2 = new SpannableStringBuilder(
					retail.getMall_url());
			tvAddress.append(sp2);
			
			tvCategory.setText("매장분류 : " + "온라인");
		}
		
		switch (menuIndex) {
		case 0:
			btnConfirm.setBackgroundResource(R.drawable.sample_confirm_popup_btn);
			break;
		}
		
		SoftKeyboardUtils.hideKeyboard(mContext, popupRelative);
		popupRelative.setVisibility(View.VISIBLE);
		popupRelative.startAnimation(aaIn);
	}
	
	public void hidePopup() {
		
		if(isAnimating || popupRelative.getVisibility() == View.INVISIBLE) {
			return;
		}
		
		popupRelative.setVisibility(View.INVISIBLE);
		popupRelative.startAnimation(aaOut);
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.order_bg;
	}

	public void approval(Retail retail) {
		
		//http://cph.minsangk.com/
		String url = CphConstants.BASE_API_URL + "wholesales/customers/answer" +
				"?retail_id=" + retail.getId() +
				"&answer=accept";
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForCustomerListPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToApproval);
				selectedRetail = null;
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForCustomerListPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_approval);
						selectedRetail = null;
						hidePopup();
						new Handler().postDelayed(new Runnable() {
							
							@Override
							public void run() {

								refreshPage();
							}
						}, 400);
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToApproval);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToApproval);
					LogUtils.trace(oom);
				}
			}
		});
	}
}

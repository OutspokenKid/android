package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Customer;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;

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
	private String keyword;
	
	private AlphaAnimation aaIn, aaOut;
	private boolean isAnimating;
	
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
		titleBar.getHomeButton().setVisibility(View.INVISIBLE);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
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
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView, int position,
					long id) {
				
				if(menuIndex == 0) {
					showPopup();
				} else {
					//Show CustomerPage.
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

				hidePopup();
			}
		});
	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;
		
		//ivBg.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.wholesaleCustomerListPage_ivBg).getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(96); 
		
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

		for(int i=0; i<10; i++) {
			Customer customer = new Customer();
			customer.setItemCode(CphConstants.ITEM_CUSTOMER);
			models.add(customer);
		}
		return false;
	}

	@Override
	public void downloadInfo() {
		
		url = CphConstants.BASE_API_URL + "wholesales/notices";
		super.downloadInfo();
	}
	
////////////////////Custom methods.
	
	public void setMenu(int menuIndex) {
		
		switch (menuIndex) {
		
		case 0:
			btnRequest.setBackgroundResource(R.drawable.business_confirm_btn_a);
			btnPartner.setBackgroundResource(R.drawable.business_costumer_btn_b);
			break;
			
		case 1:
			btnRequest.setBackgroundResource(R.drawable.business_confirm_btn_b);
			btnPartner.setBackgroundResource(R.drawable.business_costumer_btn_a);
			break;
		}
		
		this.menuIndex = menuIndex;
		
		refreshPage();
	}

	public void showPopup() {

		if(isAnimating || popupRelative.getVisibility() == View.VISIBLE) {
			return;
		}
		
		tvShopName.setText("상호명 : " + "매장이름1");
		tvOwnerName.setText("대표성함 : " + "대표이름1");
		tvPhone.setText("연락처 : " + "010-1111-1111");
		tvCategory.setText("매장분류 : " + "오프라인");
		
		tvAddress.setText(null);
		SpannableStringBuilder sp1 = new SpannableStringBuilder("주소 : ");
		tvAddress.append(sp1);
		
		SpannableStringBuilder sp2 = new SpannableStringBuilder(
				"서울특별시 강남구 역삼동 192-3번지 개나리아파트 101동 503호"
				);
		sp2.setSpan(new RelativeSizeSpan(0.8f), 0, sp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvAddress.append(sp2);
		
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

}

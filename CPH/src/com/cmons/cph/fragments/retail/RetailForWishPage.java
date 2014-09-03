package com.cmons.cph.fragments.retail;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForRetail;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.WholesaleWish;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class RetailForWishPage extends CmonsFragmentForRetail {

	private WholesaleWish wholesaleWish;
	
	private TextView tvInfo;
	private Button btnShop;
	private ListView listView;
	private TextView tvSelectPayment;
	private TextView tvPayment1;
	private View checkBox1;
	private TextView tvPayment2;
	private View checkBox2;
	private TextView tvAccount;
	private EditText etName;
	private EditText etPhone;
	private TextView tvPriceText;
	private TextView tvPrice;
	private Button btnDelete;
	private Button btnOrder;
	private boolean isPayment1, isPayment2;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.retailWishPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.retailWishPage_ivBg);
		
		tvInfo = (TextView) mThisView.findViewById(R.id.retailWishPage_tvInfo);
		btnShop = (Button) mThisView.findViewById(R.id.retailWishPage_btnShop);
		listView = (ListView) mThisView.findViewById(R.id.retailWishPage_listView);
		tvSelectPayment = (TextView) mThisView.findViewById(R.id.retailWishPage_tvSelectPayment);
		tvPayment1 = (TextView) mThisView.findViewById(R.id.retailWishPage_tvPayment1);
		checkBox1 = mThisView.findViewById(R.id.retailWishPage_checkBox1);
		tvPayment2 = (TextView) mThisView.findViewById(R.id.retailWishPage_tvPayment2);
		checkBox2 = mThisView.findViewById(R.id.retailWishPage_checkBox2);
		tvAccount = (TextView) mThisView.findViewById(R.id.retailWishPage_tvAccount);
		tvPriceText = (TextView) mThisView.findViewById(R.id.retailWishPage_tvPriceText);
		etName = (EditText) mThisView.findViewById(R.id.retailWishPage_etName);
		etPhone = (EditText) mThisView.findViewById(R.id.retailWishPage_etPhone);
		tvPrice = (TextView) mThisView.findViewById(R.id.retailWishPage_tvPrice);
		btnDelete = (Button) mThisView.findViewById(R.id.retailWishPage_btnDelete);
		btnOrder = (Button) mThisView.findViewById(R.id.retailWishPage_btnOrder);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			wholesaleWish = (WholesaleWish)getArguments().getSerializable("wholesaleWish");
		}
		
		title = "상세보기";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.WHITE));
		listView.setDividerHeight(1);
		
		if(wholesaleWish != null) {
			
			tvInfo.setText(null);

			String dateString = StringUtils.getDateString(
					"yyyy년 MM월 dd일 aa hh:mm", 
					wholesaleWish.getItems()[0].getCreated_at() * 1000);
			SpannableStringBuilder sp1 = new SpannableStringBuilder(dateString);
			sp1.setSpan(new RelativeSizeSpan(0.7f), 0, sp1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			tvInfo.append(sp1); 
			
			SpannableStringBuilder sp2 = new SpannableStringBuilder("\n" + wholesaleWish.getName());
			sp2.setSpan(new RelativeSizeSpan(1.3f), 0, sp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			tvInfo.append(sp2);
			
			SpannableStringBuilder sp3 = new SpannableStringBuilder("(청평화몰 " + wholesaleWish.getLocation() + ")");
			tvInfo.append(sp3);
			
			tvPrice.setText(StringUtils.getFormattedNumber(wholesaleWish.getSum()) + "원");
			
			int size = wholesaleWish.getItems().length;
			for(int i=0; i<size; i++) {
				models.add(wholesaleWish.getItems()[i]);
			}
			
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void setListeners() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
			}
		});
	
		tvPayment1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(!isPayment1) {
					selectAccount();
				}
			}
		});
		
		checkBox1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(!isPayment1) {
					selectAccount();
				}
			}
		});
		
		tvPayment2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(!isPayment2) {
					isPayment2 = true;
					checkBox2.setBackgroundResource(R.drawable.myshop_checkbox_b);
					
					tvAccount.setVisibility(View.INVISIBLE);
					etName.setVisibility(View.VISIBLE);
					etPhone.setVisibility(View.VISIBLE);
					
					isPayment1 = false;
					checkBox1.setBackgroundResource(R.drawable.myshop_checkbox_a);
				}
			}
		});
		
		checkBox2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(!isPayment2) {
					isPayment2 = true;
					checkBox2.setBackgroundResource(R.drawable.myshop_checkbox_b);
					
					tvAccount.setVisibility(View.INVISIBLE);
					etName.setVisibility(View.VISIBLE);
					etPhone.setVisibility(View.VISIBLE);
					
					isPayment1 = false;
					checkBox1.setBackgroundResource(R.drawable.myshop_checkbox_a);
				}
			}
		});

		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showAlertDialog("장바구니 비우기", "장바구니를 비우시겠습니까?", 
						"확인", "취소",
						new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						delete();
					}
				}, null);
			}
		});
		
		btnOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(!isPayment1 && !isPayment2) {
					ToastUtils.showToast(R.string.wrongPayment);
					return;
					
				} else if(isPayment1 && tvAccount.getText() == null) {
					ToastUtils.showToast(R.string.wrongAccount2);
					return;
					
				} else if (isPayment2 
						&& (etName.getEditableText() == null
						|| etName.getEditableText().length() == 0)) {
					ToastUtils.showToast(R.string.wrongAgentName);
					return;
					
				} else if (isPayment2 
						&& (etPhone.getEditableText() == null
						|| etPhone.getEditableText().length() == 0)) {
					ToastUtils.showToast(R.string.wrongAgentPhone);
					return;
				}
				
				order();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		int m = ResizeUtils.getSpecificLength(20);
		
		//tvInfo.
		rp = (RelativeLayout.LayoutParams) tvInfo.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		rp.leftMargin = m;
		FontUtils.setFontSize(tvInfo, 26);
		
		//btnShop.
		rp = (RelativeLayout.LayoutParams) btnShop.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(142);
		rp.height = ResizeUtils.getSpecificLength(36);
		rp.rightMargin = m;
		rp.bottomMargin = m;
		
		//tvSelectPayment.
		rp = (RelativeLayout.LayoutParams) tvSelectPayment.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.leftMargin = m;
		FontUtils.setFontSize(tvSelectPayment, 30);
		
		//tvPayment1.
		rp = (RelativeLayout.LayoutParams) tvPayment1.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.leftMargin = m;
		rp.bottomMargin = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontSize(tvPayment1, 30);
		
		//checkBox1.
		rp = (RelativeLayout.LayoutParams) checkBox1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(44);
		rp.height = ResizeUtils.getSpecificLength(43);
		rp.leftMargin = m;
		rp.topMargin = ResizeUtils.getSpecificLength(10);
		
		//tvPayment2.
		rp = (RelativeLayout.LayoutParams) tvPayment2.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.leftMargin = ResizeUtils.getSpecificLength(100);
		FontUtils.setFontSize(tvPayment2, 30);
		
		//checkBox2.
		rp = (RelativeLayout.LayoutParams) checkBox2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(44);
		rp.height = ResizeUtils.getSpecificLength(43);
		rp.leftMargin = m;
		
		//tvAccount.
		rp = (RelativeLayout.LayoutParams) tvAccount.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontSize(tvPriceText, 40);
		
		//etName.
		rp = (RelativeLayout.LayoutParams) etName.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(184);
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontAndHintSize(etName, 28, 24);
		
		//etPhone.
		rp = (RelativeLayout.LayoutParams) etPhone.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontAndHintSize(etPhone, 28, 24);
		
		//tvPriceText.
		rp = (RelativeLayout.LayoutParams) tvPriceText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(80);
		rp.leftMargin = m;
		FontUtils.setFontSize(tvPriceText, 30);
		
		//tvPrice.
		rp = (RelativeLayout.LayoutParams) tvPrice.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(80);
		rp.rightMargin = m;
		FontUtils.setFontSize(tvPrice, 34);

		//btnDelete.
		rp = (RelativeLayout.LayoutParams) btnDelete.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/2;
		rp.height = ResizeUtils.getSpecificLength(180);
		
		//btnOrder.
		rp = (RelativeLayout.LayoutParams) btnOrder.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);

	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_retail_wish;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		
		return true;
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.order_bg;
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

//////////////////// Custom methods.
	
	public void selectAccount() {

		ToastUtils.showToast("등록된 계좌가 없습니다.");

//		checkBox1.setBackgroundResource(R.drawable.myshop_checkbox_b);
//		isPayment1 = true;
//		isPayment2 = false;
//		checkBox2.setBackgroundResource(R.drawable.myshop_checkbox_a);
//		tvAccount.setVisibility(View.VISIBLE);
//		etName.setVisibility(View.INVISIBLE);
//		etPhone.setVisibility(View.INVISIBLE);
	}

	public void delete() {
		
		ToastUtils.showToast("장바구니 비우기. 아직 구현 안함");
	}
	
	public void order() {

		try {
			String url = CphConstants.BASE_API_URL + "retails/orders/order_from_cart" +
					"?wholesale_id=" + wholesaleWish.getId() +
					"&payment_type=" + (isPayment1? 1:2);
			
			if(isPayment1) {
				url += "&payment_account_id=" + URLEncoder.encode(tvAccount.getText().toString(), "utf-8");
			} else {
				url += "&payment_purchaser_info=" + URLEncoder.encode(etName.getText().toString() + 
						"/" + etPhone.getText().toString(), "utf-8");
			}
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("RetailForWishPage.order.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToOrder);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("RetailForWishPage.order.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);
						
						if(objJSON.getInt("result") == 1) {
							ToastUtils.showToast(R.string.complete_order);
							mActivity.closeTopPage();
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToOrder);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToOrder);
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

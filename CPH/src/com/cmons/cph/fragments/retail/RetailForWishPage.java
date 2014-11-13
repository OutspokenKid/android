package com.cmons.cph.fragments.retail;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import com.cmons.cph.models.Account;
import com.cmons.cph.models.Wholesale;
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

	private static final int PAYMENT_NONE = 0;
	private static final int PAYMENT_BANK = 1;
	private static final int PAYMENT_AGENT = 2;
	
	private WholesaleWish wholesaleWish;
	private Wholesale wholesale;
	
	private TextView tvInfo;
	private Button btnShop;
	private ListView listView;
	private TextView tvSelectPayment;
	private TextView tvBank;
	private View cbBank;
	private TextView tvAgent;
	private View cbAgent;
	private TextView tvAccount;
	private EditText etAgentName;
	private EditText etAgentPhone;
	private TextView tvPriceText;
	private TextView tvPrice;
	private Button btnDelete;
	private Button btnOrder;

	private int type;
	private Account selectedAccount;
	
	@Override
	public void onResume() {
		super.onResume();
		
		downloadWholesale();
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.retailWishPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.retailWishPage_ivBg);
		
		tvInfo = (TextView) mThisView.findViewById(R.id.retailWishPage_tvInfo);
		btnShop = (Button) mThisView.findViewById(R.id.retailWishPage_btnShop);
		listView = (ListView) mThisView.findViewById(R.id.retailWishPage_listView);
		tvSelectPayment = (TextView) mThisView.findViewById(R.id.retailWishPage_tvSelectPayment);
		tvBank = (TextView) mThisView.findViewById(R.id.retailWishPage_tvBank);
		cbBank = mThisView.findViewById(R.id.retailWishPage_cbBank);
		tvAgent = (TextView) mThisView.findViewById(R.id.retailWishPage_tvAgent);
		cbAgent = mThisView.findViewById(R.id.retailWishPage_cbAgent);
		tvAccount = (TextView) mThisView.findViewById(R.id.retailWishPage_tvAccount);
		tvPriceText = (TextView) mThisView.findViewById(R.id.retailWishPage_tvPriceText);
		etAgentName = (EditText) mThisView.findViewById(R.id.retailWishPage_etAgentName);
		etAgentPhone = (EditText) mThisView.findViewById(R.id.retailWishPage_etAgentPhone);
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
			
			models.clear();
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
	
		btnShop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(wholesaleWish != null) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("wholesale", wholesale);
					mActivity.showPage(CphConstants.PAGE_RETAIL_SHOP, bundle);
				}
			}
		});
		
		tvBank.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				selectAccount();
			}
		});
		
		tvAgent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(type != PAYMENT_AGENT) {
					type = PAYMENT_AGENT;
					cbBank.setBackgroundResource(R.drawable.myshop_checkbox_a);
					cbAgent.setBackgroundResource(R.drawable.myshop_checkbox_b);
					
					tvAccount.setVisibility(View.INVISIBLE);
					tvAccount.setText("");
					etAgentName.setText("");
					etAgentPhone.setText("");
					etAgentName.setVisibility(View.VISIBLE);
					etAgentPhone.setVisibility(View.VISIBLE);
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
				
				if(type == PAYMENT_NONE) {
					ToastUtils.showToast(R.string.wrongPayment);
					return;
					
				} else if(type == PAYMENT_BANK && tvAccount.getText() == null) {
					ToastUtils.showToast(R.string.wrongAccount2);
					return;
					
				} else if(type == PAYMENT_AGENT){
					
					if(etAgentName.getText() == null
							|| etAgentName.getText().length() == 0) {
						ToastUtils.showToast(R.string.wrongAgentName);
						return;
					} else if(etAgentPhone.getText() == null
							|| etAgentPhone.getText().length() == 0) {
						ToastUtils.showToast(R.string.wrongAgentPhone);
						return;
					}
				}
				
				order();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		int m = ResizeUtils.getSpecificLength(20);
		int padding = ResizeUtils.getSpecificLength(16);
		
		//tvInfo.
		rp = (RelativeLayout.LayoutParams) tvInfo.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		rp.leftMargin = m;
		
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
		
		//tvBank.
		rp = (RelativeLayout.LayoutParams) tvBank.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(260);
		rp.height = ResizeUtils.getSpecificLength(60);
		tvBank.setPadding(padding*2, 0, 0, 0);
		rp.bottomMargin = ResizeUtils.getSpecificLength(92);
		
		//cbBank.
		rp = (RelativeLayout.LayoutParams) cbBank.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(44);
		rp.height = ResizeUtils.getSpecificLength(43);
		rp.topMargin = ResizeUtils.getSpecificLength(8);
		rp.rightMargin = padding;
		
		//tvAgent.
		rp = (RelativeLayout.LayoutParams) tvAgent.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(260);
		rp.height = ResizeUtils.getSpecificLength(60);
		tvBank.setPadding(padding*2, 0, 0, 0);
		
		//cbAgent.
		rp = (RelativeLayout.LayoutParams) cbAgent.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(44);
		rp.height = ResizeUtils.getSpecificLength(43);
		rp.topMargin = ResizeUtils.getSpecificLength(8);
		rp.rightMargin = padding;
		
		//tvAccount.
		rp = (RelativeLayout.LayoutParams) tvAccount.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//etAgentName.
		rp = (RelativeLayout.LayoutParams) etAgentName.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(184);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//etAgentPhone.
		rp = (RelativeLayout.LayoutParams) etAgentPhone.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvPriceText.
		rp = (RelativeLayout.LayoutParams) tvPriceText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(80);
		rp.leftMargin = m;
		
		//tvPrice.
		rp = (RelativeLayout.LayoutParams) tvPrice.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(80);
		rp.rightMargin = m;
		
		//btnDelete.
		rp = (RelativeLayout.LayoutParams) btnDelete.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/2;
		rp.height = ResizeUtils.getSpecificLength(180);
		
		//btnOrder.
		rp = (RelativeLayout.LayoutParams) btnOrder.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);

		FontUtils.setFontSize(tvInfo, 26);
		FontUtils.setFontSize(tvSelectPayment, 30);
		FontUtils.setFontSize(tvBank, 30);
		FontUtils.setFontSize(tvAgent, 30);
		FontUtils.setFontSize(tvPriceText, 40);
		FontUtils.setFontAndHintSize(etAgentName, 28, 24);
		FontUtils.setFontAndHintSize(etAgentPhone, 28, 24);
		FontUtils.setFontSize(tvPriceText, 30);
		FontUtils.setFontSize(tvPrice, 34);
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

	public void downloadWholesale() {
		
		//http://cph.minsangk.com/wholesales/show?wholesale_id=632
		String url = CphConstants.BASE_API_URL + "wholesales/show" +
				"?wholesale_id=" + wholesaleWish.getId();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("RetailForWishPage.downloadWholesale.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("RetailForWishPage.downloadWholesale.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					wholesale = new Wholesale(objJSON.getJSONObject("wholesale"));
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	public void selectAccount() {

		if(wholesale == null
				|| wholesale.getAccounts() == null
				|| wholesale.getAccounts().length == 0) {
			ToastUtils.showToast("등록된 계좌가 없습니다.");
			return;
		}
		
		int size = wholesale.getAccounts().length;
		
		final String[] strings = new String[size];
		
		for(int i=0; i<size; i++) {
			Account account = wholesale.getAccounts()[i];
			strings[i] = account.getBank() + " " +
					account.getNumber() + "(" +
					account.getDepositor() + ")";
		}
		
		mActivity.showSelectDialog("계좌 선택", strings, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				selectedAccount = wholesale.getAccounts()[which];
				
				type = PAYMENT_BANK;
				cbBank.setBackgroundResource(R.drawable.myshop_checkbox_b);
				cbAgent.setBackgroundResource(R.drawable.myshop_checkbox_a);

				tvAccount.setText(strings[which]);
				tvAccount.setVisibility(View.VISIBLE);
				etAgentName.setVisibility(View.INVISIBLE);
				etAgentPhone.setVisibility(View.INVISIBLE);
				etAgentName.setText("");
				etAgentPhone.setText("");
			}
		});
	}

	public void delete() {
		
		try {
			String url = CphConstants.BASE_API_URL + "retails/cart/delete_item" +
					"?wholesale_id=" + wholesaleWish.getId();
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("RetailForWishPage.delete.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToDeleteWishList);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("RetailForWishPage.delete.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);
						
						if(objJSON.getInt("result") == 1) {
							ToastUtils.showToast(R.string.complete_deleteWishList);
							mActivity.closePageWithRefreshPreviousPage();
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToDeleteWishList);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToDeleteWishList);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void order() {

		/*
		http://cph.minsangk.com/retails/orders/order_from_cart
		?wholesale_id=1
		&payment_type=1
		&payment_account_id=1

		http://cph.minsangk.com/retails/orders/order_from_cart
		?wholesale_id=1
		&payment_type=2
		&payment_purchaser_info=%ED%99%8D%EA%B8%B8%EB%8F%99/010-2082-1803
		*/
		try {
			String url = CphConstants.BASE_API_URL + "retails/orders/order_from_cart" +
					"?wholesale_id=" + wholesaleWish.getId() +
					"&payment_type=" + type;
			
			if(type == PAYMENT_BANK) {
				url += "&payment_account_id=" + selectedAccount.getId();
			} else {
				String purchaser_info = (etAgentName.getText().toString() + "/" + 
						etAgentPhone.getText().toString()).replace(" ", "");
				url += "&payment_purchaser_info=" + URLEncoder.encode(purchaser_info, "utf-8");
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
							mActivity.closePageWithRefreshPreviousPage();
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

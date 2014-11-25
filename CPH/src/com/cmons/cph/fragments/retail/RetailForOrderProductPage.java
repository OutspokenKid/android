package com.cmons.cph.fragments.retail;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForRetail;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Account;
import com.cmons.cph.models.Order;
import com.cmons.cph.models.Product;
import com.cmons.cph.models.Wholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class RetailForOrderProductPage extends CmonsFragmentForRetail {
	
	private static final int PAYMENT_NONE = 0;
	private static final int PAYMENT_BANK = 1;
	private static final int PAYMENT_AGENT = 2;
	private static final int PAYMENT_VISIT = 3;
	
	private Product product;
	private Wholesale wholesale;
	private boolean isBasket;
	
	private TextView tvOrderInfo;
	private Button btnColor;
	private Button btnSize;
	private EditText etCount;
	private Button btnAdd;
	private LinearLayout innerLinear;
	private TextView tvSelectPayment;
	private TextView tvBank;
	private View cbBank;
	private TextView tvAgent;
	private View cbAgent;
	private TextView tvVisit;
	private View cbVisit;
	private TextView tvAccount;
	private EditText etAgentName;
	private EditText etAgentPhone;
	private TextView tvVisitSelected;
	private TextView tvTotalPriceText;
	private TextView tvTotalPrice;
	private Button btnOrder;
	private Button btnBasket;
	
	private int type;
	private long totalPrice;
	private Account selectedAccount;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.retailOrderProductPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.retailOrderProductPage_ivBg);
		
		tvOrderInfo = (TextView) mThisView.findViewById(R.id.retailOrderProductPage_tvOrderInfo);
		btnColor = (Button) mThisView.findViewById(R.id.retailOrderProductPage_btnColor);
		btnSize = (Button) mThisView.findViewById(R.id.retailOrderProductPage_btnSize);
		etCount = (EditText) mThisView.findViewById(R.id.retailOrderProductPage_etCount);
		btnAdd = (Button) mThisView.findViewById(R.id.retailOrderProductPage_btnAdd);
		tvSelectPayment = (TextView) mThisView.findViewById(R.id.retailOrderProductPage_tvSelectPayment);
		innerLinear = (LinearLayout) mThisView.findViewById(R.id.retailOrderProductPage_innerLinear);
		tvBank = (TextView) mThisView.findViewById(R.id.retailOrderProductPage_tvBank);
		cbBank = mThisView.findViewById(R.id.retailOrderProductPage_cbBank);
		tvAgent = (TextView) mThisView.findViewById(R.id.retailOrderProductPage_tvAgent);
		cbAgent = mThisView.findViewById(R.id.retailOrderProductPage_cbAgent);
		tvVisit = (TextView) mThisView.findViewById(R.id.retailOrderProductPage_tvVisit);
		cbVisit = mThisView.findViewById(R.id.retailOrderProductPage_cbVisit);
		tvAccount = (TextView) mThisView.findViewById(R.id.retailOrderProductPage_tvAccount);
		etAgentName = (EditText) mThisView.findViewById(R.id.retailOrderProductPage_etName);
		etAgentPhone = (EditText) mThisView.findViewById(R.id.retailOrderProductPage_etPhone);
		tvVisitSelected = (TextView) mThisView.findViewById(R.id.retailOrderProductPage_tvVisitSelected);
		tvTotalPriceText = (TextView) mThisView.findViewById(R.id.retailOrderProductPage_tvTotalPriceText);
		tvTotalPrice = (TextView) mThisView.findViewById(R.id.retailOrderProductPage_tvTotalPrice);
		btnOrder = (Button) mThisView.findViewById(R.id.retailOrderProductPage_btnOrder);
		btnBasket = (Button) mThisView.findViewById(R.id.retailOrderProductPage_btnBasket);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			product = (Product) getArguments().getSerializable("product");
			wholesale = (Wholesale) getArguments().getSerializable("wholesale");
			title = product.getName();
			
			isBasket = getArguments().getBoolean("isBasket");
		}
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
		
		if(isBasket) {
			tvSelectPayment.setVisibility(View.GONE);
			tvBank.setVisibility(View.GONE);
			cbBank.setVisibility(View.GONE);
			tvAgent.setVisibility(View.GONE);
			cbAgent.setVisibility(View.GONE);
			tvAccount.setVisibility(View.GONE);
			etAgentName.setVisibility(View.GONE);
			etAgentPhone.setVisibility(View.GONE);
			btnOrder.setVisibility(View.GONE);
		} else {
			btnBasket.setVisibility(View.GONE);
		}
		
		FontUtils.addSpan(tvOrderInfo, "주문정보입력", 0, 1);
		FontUtils.addSpan(tvOrderInfo, "   상품 옵션을 선택 후 추가 버튼을 눌러주세요.", 0, 0.7f);
	}

	@Override
	public void setListeners() {

		btnColor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				final String[] colors = product.getColors().split("\\|");
				mActivity.showSelectDialog(null, colors, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						btnColor.setText(colors[which]);
					}
				});
			}
		});
		
		btnSize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				final String[] sizes = product.getSizes().split("\\|");
				mActivity.showSelectDialog(null, sizes, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						btnSize.setText(sizes[which]);
					}
				});
			}
		});
		
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(btnColor.getText() == null) {
					ToastUtils.showToast(R.string.wrongColor);
					return;
					
				} else if(btnSize.getText() == null) {
					ToastUtils.showToast(R.string.wrongSize);
					return;
					
				} else if(etCount.getText() == null) {
					ToastUtils.showToast(R.string.wrongAmount);
					return;
				}
				
				addOrder();
			}
		});
		
		tvBank.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				selectBank();
			}
		});
		
		cbBank.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				selectBank();
			}
		});
		
		tvAgent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				selectAgent();
			}
		});
		
		cbAgent.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				selectAgent();
			}
		});
		
		tvVisit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				selectVisit();
			}
		});
		
		cbVisit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				selectVisit();
			}
		});
	
		btnOrder.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				if(totalPrice == 0) {
					ToastUtils.showToast(R.string.wrongItems);
					return;
					
				} else if(type == PAYMENT_NONE) {
					ToastUtils.showToast(R.string.wrongPayment);
					return;
					
				} else if(type == PAYMENT_BANK && tvAccount.getText() == null) {
					ToastUtils.showToast(R.string.wrongAccount2);
					return;
					
				} else if(type == PAYMENT_AGENT){
					
//					if(etAgentName.getText() == null
//							|| etAgentName.getText().length() == 0) {
//						ToastUtils.showToast(R.string.wrongAgentName);
//						return;
//					} else if(etAgentPhone.getText() == null
//							|| etAgentPhone.getText().length() == 0) {
//						ToastUtils.showToast(R.string.wrongAgentPhone);
//						return;
//					}
				} else if(type == PAYMENT_VISIT) {
					//Check nothing.
				}
				
				order();
			}
		});
		
		btnBasket.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				if(totalPrice == 0) {
					ToastUtils.showToast(R.string.wrongItems);
					return;
				}
				
				addToBasket();
			}
		});
	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;
		
		int padding = ResizeUtils.getSpecificLength(16);
		int titleTextHeight = ResizeUtils.getSpecificLength(100);
		int editTextHeight = ResizeUtils.getSpecificLength(92);
		int buttonHeight = ResizeUtils.getSpecificLength(180);
		
		//tvOrderInfo.
		rp = (RelativeLayout.LayoutParams) tvOrderInfo.getLayoutParams();
		rp.height = titleTextHeight;
		tvOrderInfo.setPadding(padding, 0, 0, 0);
		
		//btnColor.
		rp = (RelativeLayout.LayoutParams) btnColor.getLayoutParams();
		rp.width = buttonHeight;
		rp.height = editTextHeight;
		
		//btnSize.
		rp = (RelativeLayout.LayoutParams) btnSize.getLayoutParams();
		rp.width = buttonHeight;
		rp.height = editTextHeight;
		
		//etCount
		rp = (RelativeLayout.LayoutParams) etCount.getLayoutParams();
		rp.width = buttonHeight;
		rp.height = editTextHeight;
		
		//btnAdd.
		rp = (RelativeLayout.LayoutParams) btnAdd.getLayoutParams();
		rp.height = editTextHeight;

		//innerLinear.
		innerLinear.setMinimumHeight(ResizeUtils.getSpecificLength(370));
		
		//tvSelectPayment.
		rp = (RelativeLayout.LayoutParams) tvSelectPayment.getLayoutParams();
		rp.height = titleTextHeight/2;
		tvSelectPayment.setPadding(padding, 0, 0, 0);
		
		//tvBank.
		rp = (RelativeLayout.LayoutParams) tvBank.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.leftMargin = padding;
		tvBank.setPadding(0, 0, 0, ResizeUtils.getSpecificLength(6));
		
		//cbBank.
		rp = (RelativeLayout.LayoutParams) cbBank.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(44);
		rp.height = ResizeUtils.getSpecificLength(43);
		rp.leftMargin = padding / 2;
		rp.topMargin = ResizeUtils.getSpecificLength(8);
		rp.rightMargin = padding;
		
		//tvAgent.
		rp = (RelativeLayout.LayoutParams) tvAgent.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(60);
		tvAgent.setPadding(0, 0, 0, ResizeUtils.getSpecificLength(6));
		
		//cbAgent.
		rp = (RelativeLayout.LayoutParams) cbAgent.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(44);
		rp.height = ResizeUtils.getSpecificLength(43);
		rp.leftMargin = padding / 2;
		rp.topMargin = ResizeUtils.getSpecificLength(8);
		rp.rightMargin = padding;
		
		//tvVisit.
		rp = (RelativeLayout.LayoutParams) tvVisit.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(60);
		tvVisit.setPadding(0, 0, 0, ResizeUtils.getSpecificLength(6));
		
		//cbVisit.
		rp = (RelativeLayout.LayoutParams) cbVisit.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(44);
		rp.height = ResizeUtils.getSpecificLength(43);
		rp.leftMargin = padding / 2;
		rp.topMargin = ResizeUtils.getSpecificLength(8);
		rp.rightMargin = padding;
		
		//tvAccount.
		rp = (RelativeLayout.LayoutParams) tvAccount.getLayoutParams();
		rp.height = editTextHeight;
		
		//etAgentName.
		rp = (RelativeLayout.LayoutParams) etAgentName.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(184);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//etAgentPhone.
		rp = (RelativeLayout.LayoutParams) etAgentPhone.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvVisitSelected.
		rp = (RelativeLayout.LayoutParams) tvVisitSelected.getLayoutParams();
		rp.height = editTextHeight;
		
		//tvTotalPriceText.
		rp = (RelativeLayout.LayoutParams) tvTotalPriceText.getLayoutParams();
		rp.height = titleTextHeight;
		tvTotalPriceText.setPadding(padding, 0, 0, 0);
		
		//tvTotalPrice.
		rp = (RelativeLayout.LayoutParams) tvTotalPrice.getLayoutParams();
		rp.height = titleTextHeight;
		tvTotalPrice.setPadding(0, 0, padding, 0);
		
		//btnOrder.
		rp = (RelativeLayout.LayoutParams) btnOrder.getLayoutParams();
		rp.height = buttonHeight;
		
		//btnBasket.
		rp = (RelativeLayout.LayoutParams) btnBasket.getLayoutParams();
		rp.height = buttonHeight;
		
		FontUtils.setFontSize(tvOrderInfo, 30);
		FontUtils.setFontSize(btnColor, 30);
		FontUtils.setFontSize(btnSize, 30);
		FontUtils.setFontSize(etCount, 30);
		FontUtils.setFontSize(tvSelectPayment, 30);
		FontUtils.setFontSize(tvBank, 30);
		FontUtils.setFontSize(tvAgent, 30);
		FontUtils.setFontSize(tvTotalPriceText, 30);
		FontUtils.setFontSize(tvTotalPrice, 30);
		FontUtils.setFontSize(etAgentName, 30);
		FontUtils.setFontSize(etAgentPhone, 30);
		FontUtils.setFontSize(tvAccount, 34);
		FontUtils.setFontStyle(tvAccount, FontUtils.BOLD);
		FontUtils.setFontSize(tvVisitSelected, 34);
		FontUtils.setFontStyle(tvVisitSelected, FontUtils.BOLD);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_retail_order_product;
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
		
		return false;
	}
	
	@Override
	public void downloadInfo() {

	}

	@Override
	public int getBgResourceId() {

		return R.drawable.myshop_bg;
	}
	
//////////////////// Custom methods.

	public void selectBank() {

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
				cbVisit.setBackgroundResource(R.drawable.myshop_checkbox_a);

				tvAccount.setText(strings[which]);
				tvAccount.setVisibility(View.VISIBLE);
				etAgentName.setVisibility(View.INVISIBLE);
				etAgentPhone.setVisibility(View.INVISIBLE);
				etAgentName.setText("");
				etAgentPhone.setText("");
				tvVisitSelected.setVisibility(View.INVISIBLE);
			}
		});
	}
	
	public void selectAgent() {
		
		if(type != PAYMENT_AGENT) {
			type = PAYMENT_AGENT;
			cbBank.setBackgroundResource(R.drawable.myshop_checkbox_a);
			cbAgent.setBackgroundResource(R.drawable.myshop_checkbox_b);
			cbVisit.setBackgroundResource(R.drawable.myshop_checkbox_a);
			
			tvAccount.setVisibility(View.INVISIBLE);
			tvAccount.setText("");
			etAgentName.setText("");
			etAgentPhone.setText("");
			etAgentName.setVisibility(View.VISIBLE);
			etAgentPhone.setVisibility(View.VISIBLE);
			tvVisitSelected.setVisibility(View.INVISIBLE);
		}
	}
	
	public void selectVisit() {

		if(type != PAYMENT_VISIT) {
			type = PAYMENT_VISIT;
			cbBank.setBackgroundResource(R.drawable.myshop_checkbox_a);
			cbAgent.setBackgroundResource(R.drawable.myshop_checkbox_a);
			cbVisit.setBackgroundResource(R.drawable.myshop_checkbox_b);
			
			tvAccount.setVisibility(View.INVISIBLE);
			tvAccount.setText("");
			etAgentName.setText("");
			etAgentPhone.setText("");
			etAgentName.setVisibility(View.INVISIBLE);
			etAgentPhone.setVisibility(View.INVISIBLE);
			tvVisitSelected.setVisibility(View.VISIBLE);
		}
	}
	
	public void addOrder() {
		
		try {
			if(btnColor.getText() != null
					&& btnSize.getText() != null
					&& etCount.getText() != null) {

				String color = btnColor.getText().toString();
				String size = btnSize.getText().toString();
				int amount = Integer.parseInt(etCount.getText().toString());
				
				Order duplicatedOrder = getDuplicatedOrder(
						color, size, amount);
				
				if(duplicatedOrder != null) {
					return;
				}
				
				final FrameLayout orderFrame = new FrameLayout(mContext);
				ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 100, orderFrame, 1, 0, null);
				innerLinear.addView(orderFrame);
				
				TextView tv1 = new TextView(mContext);
				tv1.setLayoutParams(new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, Gravity.LEFT));
				tv1.setTextColor(Color.WHITE);
				FontUtils.setFontSize(tv1, 28);
				tv1.setGravity(Gravity.CENTER_VERTICAL);
				tv1.setPadding(ResizeUtils.getSpecificLength(16), 0, 
						ResizeUtils.getSpecificLength(16), 0);
				orderFrame.addView(tv1);
				
				TextView tv2 = new TextView(mContext);
				tv2.setLayoutParams(new FrameLayout.LayoutParams(
						LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT, Gravity.RIGHT));
				tv2.setTextColor(Color.WHITE);
				FontUtils.setFontSize(tv2, 28);
				tv2.setGravity(Gravity.CENTER_VERTICAL);
				tv2.setPadding(ResizeUtils.getSpecificLength(16), 0, 
						ResizeUtils.getSpecificLength(16), 0);
				orderFrame.addView(tv2);
				
				View line = new View(mContext);
				line.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 1, Gravity.BOTTOM));
				line.setBackgroundColor(Color.WHITE);
				orderFrame.addView(line);
				
				final long price =  amount * product.getPrice();
				
				FontUtils.addSpan(tv1, product.getName() + 
						"(" + size + "/" + 
						color + "/" + 
						amount + "개" + ")", 0, 1);
				
				FontUtils.addSpan(tv2, StringUtils.getFormattedNumber(price) + "원", Color.RED, 1);
				
				final Order order = new Order();
				order.setProduct_id(product.getId());
				order.setSize(size);
				order.setColor(color);
				order.setAmount(amount);
				models.add(order);
				orderFrame.setTag(order);
				
				totalPrice += price;
				tvTotalPrice.setText(StringUtils.getFormattedNumber(totalPrice) + "원");
				orderFrame.setOnLongClickListener(new OnLongClickListener() {
					
					@Override
					public boolean onLongClick(View v) {

						DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								totalPrice -= price;
								tvTotalPrice.setText(StringUtils.getFormattedNumber(totalPrice) + "원");
								innerLinear.removeView(orderFrame);
								models.remove(order);
							}
						};
						
						mActivity.showAlertDialog("삭제", "해당 물품을 삭제하시겠습니까?", "확인", "취소", ocl, null);
						return false;
					}
				});
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public Order getDuplicatedOrder(String color, String size, int amount) {
		
		LogUtils.log("###RetailForOrderProductPage.getDuplicatedOrder.  " +
				"\nsize : " + size + ", color : " + color + ", amount : " + amount);
		
		try {
			Order order = null;
			
			int length = innerLinear.getChildCount();
			for(int i=0; i<length; i++) {
				order = (Order) innerLinear.getChildAt(i).getTag();
				
				LogUtils.log("		size : " + order.getSize() + 
						", color : " + order.getColor() + 
						", amount : " + order.getAmount());
				
				if(order.getSize().equals(size)
						&& order.getColor().equals(color)) {
					
					order.setAmount(order.getAmount() + amount);
					
					FrameLayout orderFrame = (FrameLayout) innerLinear.getChildAt(i);
					((TextView)orderFrame.getChildAt(0)).setText(product.getName() + 
							"(" + size + "/" + 
							color + "/" + 
							order.getAmount() + "개" + ")");

					final long price =  order.getAmount() * product.getPrice();
					
					TextView tv = (TextView)orderFrame.getChildAt(1);
					tv.setText(null);
					FontUtils.addSpan(tv, StringUtils.getFormattedNumber(price) + "원", Color.RED, 1);
					
					totalPrice += amount * product.getPrice();
					tvTotalPrice.setText(StringUtils.getFormattedNumber(totalPrice) + "원");
					
					return order;
				}
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return null;
	}

	public void order() {

		try {
			String url = CphConstants.BASE_API_URL + "retails/orders/direct_order" +
					"?product_id=" + product.getId() +
					"&payment_type=" + type;
			
			if(type == PAYMENT_BANK) {
				url += "&payment_account_id=" + selectedAccount.getId();
				
			} else if(type == PAYMENT_AGENT) {
//				String purchaser_info = etAgentName.getText().toString() + "/" + 
//						etAgentPhone.getText().toString();
				
				String purchaser_info = null;
				
				if(!StringUtils.isEmpty(etAgentName)) {
					purchaser_info = etAgentName.getText().toString();
				}
				
				if(!StringUtils.isEmpty(etAgentPhone)) {

					if(purchaser_info != null) {
						purchaser_info += "/" + etAgentPhone.getText().toString();
					} else {
						purchaser_info = etAgentPhone.getText().toString();
					}
				}
				
				if(purchaser_info != null) {
					purchaser_info.replace(" ", "");
				}
				
				url += "&payment_purchaser_info=" + URLEncoder.encode(purchaser_info, "utf-8");
			}
			
			int size = models.size();
			for(int i=0; i<size; i++) {
				Order order = (Order) models.get(i);
				url += "&options[" + i + "][color]=" + URLEncoder.encode(order.getColor(), "utf-8") +
						"&options[" + i + "][size]=" + URLEncoder.encode(order.getSize(), "utf-8") +
						"&options[" + i + "][amount]=" + URLEncoder.encode("" + order.getAmount(), "utf-8");
			}
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("RetailForOrderProductPage.order.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToOrder);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("RetailForOrderProductPage.order.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 1) {
							ToastUtils.showToast(R.string.complete_order);
							
							if(isBasket) {
								mActivity.closeTopPage();
							} else {
								mActivity.closePagesWithRefreshPreviousPage(2);
							}
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
			ToastUtils.showToast(R.string.failToOrder);
		} catch (Error e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToOrder);
		}
	}
	
	public void addToBasket() {

		try {
			String purchaser_info = (etAgentName.getText().toString() + "/" + etAgentPhone.getText().toString()).replace(" ", "");
			String url = CphConstants.BASE_API_URL + "retails/cart/add_item" +
					"?product_id=" + product.getId() +
					"&payment_type=" + type +
					"&payment_purchaser_info=" + URLEncoder.encode(purchaser_info, "utf-8");
			
			int size = models.size();
			for(int i=0; i<size; i++) {
				Order order = (Order) models.get(i);
				url += "&options[" + i + "][color]=" + URLEncoder.encode(order.getColor(), "utf-8") +
						"&options[" + i + "][size]=" + URLEncoder.encode(order.getSize(), "utf-8") +
						"&options[" + i + "][amount]=" + URLEncoder.encode("" + order.getAmount(), "utf-8");
			}
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("RetailForOrderProductPage.addToBasket.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToAddBasket);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("RetailForOrderProductPage.addToBasket.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 1) {
							ToastUtils.showToast(R.string.complete_addBasket);
							mActivity.closePagesWithRefreshPreviousPage(2);
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToAddBasket);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToAddBasket);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToAddBasket);
		} catch (Error e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToAddBasket);
		}
	}
}

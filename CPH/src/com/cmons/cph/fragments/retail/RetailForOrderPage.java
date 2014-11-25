package com.cmons.cph.fragments.retail;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForRetail;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Account;
import com.cmons.cph.models.Order;
import com.cmons.cph.models.OrderSet;
import com.cmons.cph.models.Reply;
import com.cmons.cph.models.Wholesale;
import com.cmons.cph.views.OrderView;
import com.cmons.cph.views.ReplyView;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class RetailForOrderPage extends CmonsFragmentForRetail {

	private OrderSet orderSet;
	
	private TextView tvOrder;
	private Button btnShop;
	private View order;
	private View standBy;
	private View deposit;
	private View completed;
	private ScrollView scrollView;
	private LinearLayout orderLinear;
	private TextView tvPriceText;
	private TextView tvPrice;
	private TextView tvAccount;
	private Button btnConfirm;
	private LinearLayout replyLinear;
	private EditText etReply;
	private Button btnSubmit;
	
	private Wholesale wholesale;
	
	private boolean needAutoScroll;
	
	ArrayList<Order> orders = new ArrayList<Order>();
	ArrayList<Reply> replies = new ArrayList<Reply>();
	
	@Override
	public void onResume() {
		super.onResume();

		downloadWholesale(orderSet.getWholesale_id());
		
		if(orders.size() == 0 && orderSet.getItems() != null) {
			int size = orderSet.getItems().length;
			for(int i=0; i<size; i++) {
				Order order = orderSet.getItems()[i];
				order.setItemCode(CphConstants.ITEM_ORDER_RETAIL);
				order.setParentStatus(orderSet.getStatus());
				orders.add(order);
			}
		}
		
		addOrders();
		loadReplies();
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.retailOrderPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.retailOrderPage_ivBg);
		
		tvOrder = (TextView) mThisView.findViewById(R.id.retailOrderPage_tvOrder);
		btnShop = (Button) mThisView.findViewById(R.id.retailOrderPage_btnShop);
		
		order = mThisView.findViewById(R.id.retailOrderPage_order);
		standBy = mThisView.findViewById(R.id.retailOrderPage_standBy);
		deposit = mThisView.findViewById(R.id.retailOrderPage_deposit);
		completed = mThisView.findViewById(R.id.retailOrderPage_completed);
		
		scrollView = (ScrollView) mThisView.findViewById(R.id.retailOrderPage_scrollView);
		orderLinear = (LinearLayout) mThisView.findViewById(R.id.retailOrderPage_orderLinear);
		
		tvPriceText = (TextView) mThisView.findViewById(R.id.retailOrderPage_tvPriceText);
		tvPrice = (TextView) mThisView.findViewById(R.id.retailOrderPage_tvPrice);
		tvAccount = (TextView) mThisView.findViewById(R.id.retailOrderPage_tvAccount);
		btnConfirm = (Button) mThisView.findViewById(R.id.retailOrderPage_btnConfirm);
		
		replyLinear = (LinearLayout) mThisView.findViewById(R.id.retailOrderPage_replyLinear);
		
		etReply = (EditText) mThisView.findViewById(R.id.retailOrderPage_etReply);
		btnSubmit = (Button) mThisView.findViewById(R.id.retailOrderPage_btnSubmit);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			orderSet = (OrderSet) getArguments().getSerializable("orderSet");
		}
		
		title = "주문내역";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
		
		setMenu(orderSet.getStatus());
		
		String dateString = StringUtils.getDateString(
				"yyyy년 MM월 dd일 aa hh:mm", 
				orderSet.getItems()[0].getCreated_at() * 1000);
		SpannableStringBuilder sp1 = new SpannableStringBuilder(dateString + "\n");
		sp1.setSpan(new RelativeSizeSpan(0.8f), 0, sp1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvOrder.append(sp1); 
		
		SpannableStringBuilder sp2 = new SpannableStringBuilder(orderSet.getWholesale_name());
		sp2.setSpan(new RelativeSizeSpan(1.3f), 0, sp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvOrder.append(sp2);
		
		SpannableStringBuilder sp3 = new SpannableStringBuilder(
				"(청평화몰 " + orderSet.getWholesale_location() + ")");
		tvOrder.append(sp3);
		
		setMenu(orderSet.getStatus());
	}

	@Override
	public void setListeners() {
		
		btnShop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(wholesale != null) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("wholesale", wholesale);
					mActivity.showPage(CphConstants.PAGE_RETAIL_SHOP, bundle);
				}
			}
		});
	
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				switch(orderSet.getStatus()) {
				
				case Order.STATUS_CANCELED:
				case Order.STATUS_REQUESTED:
					return;
					
				//입금완료.
				case Order.STATUS_STANDBY:
					requestStatus(2);
					break;
					
				//거래완료.
				case Order.STATUS_DEPOSIT:
					break;
				//내역삭제.
				case Order.STATUS_COMPLETED:
					deleteOrder();
					break;
				}
			}
		});

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(etReply.getText() == null) {
					ToastUtils.showToast(R.string.wrongReply);
					return;
				}
				
				writeReply();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		int p = ResizeUtils.getSpecificLength(10);
		
		//tvOrder.
		rp = (RelativeLayout.LayoutParams) tvOrder.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		tvOrder.setPadding(p, p, p, p);
		
		//btnShop.
		rp = (RelativeLayout.LayoutParams) btnShop.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(142);
		rp.height = ResizeUtils.getSpecificLength(36);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
		
		//order.
		rp = (RelativeLayout.LayoutParams) order.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(195);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//standBy.
		rp = (RelativeLayout.LayoutParams) standBy.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(220);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.leftMargin = -ResizeUtils.getSpecificLength(40);
		
		//deposit.
		rp = (RelativeLayout.LayoutParams) deposit.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(222);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.leftMargin = -ResizeUtils.getSpecificLength(36);
		
		//completed.
		rp = (RelativeLayout.LayoutParams) completed.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(197);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.leftMargin = -ResizeUtils.getSpecificLength(36);

		//tvAccount.
		tvAccount.setPadding(p, p, p, p);
		
		//tvPriceText.
		rp = (RelativeLayout.LayoutParams) tvPriceText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(80);
		tvPriceText.setPadding(p, p, p, p);
		
		//tvPrice.
		rp = (RelativeLayout.LayoutParams) tvPrice.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(80);
		tvPrice.setPadding(p, p, p, p);
		
		//btnConfirm.
		rp = (RelativeLayout.LayoutParams) btnConfirm.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
		
		//etReply.
		rp = (RelativeLayout.LayoutParams) etReply.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(550);
		rp.height = ResizeUtils.getSpecificLength(92);
		etReply.setPadding(p, 0, p, 0);
		
		//btnSubmit.
		rp = (RelativeLayout.LayoutParams) btnSubmit.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);

	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_retail_order;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {

		return false;
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
	public int getBgResourceId() {

		return R.drawable.order_bg;
	}
	
////////////////////Custom classes.
	
	public void setMenu(int menuIndex) {

		switch(orderSet.getStatus()) {
		
		case Order.STATUS_CANCELED:
		case Order.STATUS_REQUESTED:
			order.setBackgroundResource(R.drawable.order_recommand_a);
			standBy.setBackgroundResource(R.drawable.order_wait_b);
			deposit.setBackgroundResource(R.drawable.order_done_b);
			completed.setBackgroundResource(R.drawable.order_complete_b);
			btnConfirm.setVisibility(View.INVISIBLE);
			break;
			
		case Order.STATUS_STANDBY:
			order.setBackgroundResource(R.drawable.order_recommand_b);
			standBy.setBackgroundResource(R.drawable.order_wait_a);
			deposit.setBackgroundResource(R.drawable.order_done_b);
			completed.setBackgroundResource(R.drawable.order_complete_b);
			btnConfirm.setBackgroundResource(R.drawable.retail_complete3_btn);
			btnConfirm.setVisibility(View.VISIBLE);
			break;
			
		case Order.STATUS_DEPOSIT:
			order.setBackgroundResource(R.drawable.order_recommand_b);
			standBy.setBackgroundResource(R.drawable.order_wait_b);
			deposit.setBackgroundResource(R.drawable.order_done_a);
			completed.setBackgroundResource(R.drawable.order_complete_b);
			btnConfirm.setVisibility(View.INVISIBLE);
			break;
			
		case Order.STATUS_COMPLETED:
			order.setBackgroundResource(R.drawable.order_recommand_b);
			standBy.setBackgroundResource(R.drawable.order_wait_b);
			deposit.setBackgroundResource(R.drawable.order_done_b);
			completed.setBackgroundResource(R.drawable.order_complete_a);
			btnConfirm.setBackgroundResource(R.drawable.retail_delete4_btn);
			btnConfirm.setVisibility(View.VISIBLE);
			break;
		}
	}
	
	public void downloadWholesale(final int wholesale_id) {

		String url = CphConstants.BASE_API_URL + "wholesales/show" +
				"?wholesale_id=" + wholesale_id;
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("RetailForOrderPage.downloadWholesale.onError." + "\nurl : " + url);
				
				new Handler().postDelayed(new Runnable() {
					
					@Override
					public void run() {

						downloadWholesale(wholesale_id);
					}
				}, 100);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("RetailForOrderPage.downloadWholesale.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.has("wholesale")) {
						wholesale = new Wholesale(objJSON.getJSONObject("wholesale"));
						
						String accountString = "결제방식 : ";
						
						if(orderSet.getPayment_type() == 1) {
							accountString += "무통장입금";

							if(wholesale.getAccounts() != null) {

								for(Account account : wholesale.getAccounts()) {
									
									if(account.getId() == orderSet.getPayment_account_id()) {
										accountString += "\n" + account.getBank() + 
												" " + account.getNumber() + 
												"(" + account.getDepositor() + ")";
									}
								}
							}
						} else if(orderSet.getPayment_type() == 2) {
							accountString += "사입자대납" +
									"\n" + orderSet.getPayment_purchaser_info();
						} else {
							accountString += "매장 방문시 결제";
						}
						
						tvAccount.setText(accountString);
						tvPrice.setText(StringUtils.getFormattedNumber(orderSet.getSum()) +"원");
						
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

	public void requestStatus(final int status) {

		String order_ids = null;
		
		int size = orderSet.getItems().length;
		for(int i=0; i<size; i++) {
			
			if(orderSet.getItems()[i].isChecked()) {
				
				if(order_ids == null) {
					order_ids = "" + orderSet.getItems()[i].getId();
				} else {
					order_ids += "," + orderSet.getItems()[i].getId();
				}
			}
		}

		//http://cph.minsangk.com/retails/orders/delete?order_id=10001
		String url = CphConstants.BASE_API_URL + "retails/orders/set_status" +
				"?order_ids[]=" + order_ids +
				"&status=" + status;
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("RetailForOrderPage.requestStatus.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToChangeOrderStatus);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("RetailForOrderPage.requestStatus.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						
						switch(status) {
						
						case 1:
							ToastUtils.showToast(R.string.complete_changeOrderStatus1);
							break;
						case 2:
							ToastUtils.showToast(R.string.complete_changeOrderStatus2);
							break;
						case 3:
							ToastUtils.showToast(R.string.complete_changeOrderStatus3);
							break;
						}
						
						mActivity.closePageWithRefreshPreviousPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToChangeOrderStatus);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToChangeOrderStatus);
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void deleteOrder() {
		
		//http://cph.minsangk.com/retails/orders/delete?collapse_key=10001
		String url = CphConstants.BASE_API_URL + "retails/orders/delete" +
				"?collapse_key=" + orderSet.getCollapse_key();
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("RetailForOrderPage.deleteOrder.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToDeleteOrder);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("RetailForOrderPage.deleteOrder.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_deleteOrder);
						mActivity.closePageWithRefreshPreviousPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToDeleteOrder);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToDeleteOrder);
					LogUtils.trace(oom);
				}
			}
		});
	}

	public void clearOrders() {
		
		orders.clear();
		orderLinear.removeAllViews();
	}
	
	public void addOrders() {

		orderLinear.removeAllViews();

		for(Order order : orders) {
			
			LogUtils.log("###RetailForOrderpage.addOrders.  order : " + order.getProduct_name());
			
			try {
				OrderView orderView = new OrderView(mContext);
				ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 100, orderView, 1, 0, null);
				orderView.setValues(order);
				orderLinear.addView(orderView);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
	}
	
	public void loadReplies() {

		//http://cph-app.co.kr/orders/replies?order_collapse_key=CICPYwiRYAoqAVoPebtnYSvuVzRmxwdL
		String url = CphConstants.BASE_API_URL + "orders/replies"
				+ "?order_collapse_key=" + orderSet.getCollapse_key();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("RetailForOrderPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToLoadReplies);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("RetailForOrderPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						replies.clear();
						JSONArray arJSON = objJSON.getJSONArray("replies");
						
						int size = arJSON.length();
						for(int i=0; i<size; i++) {
							replies.add(new Reply(arJSON.getJSONObject(i)));
						}
						
						addReplies();
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToLoadReplies);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToLoadReplies);
				}
			}
		});
	}
	
	public void addReplies() {

		replyLinear.removeAllViews();
		
		for(Reply reply : replies) {
			
			LogUtils.log("###RetailForOrderpage.addReplies.  reply.content : " + reply.getContent());
			
			try {
				ReplyView replyView = new ReplyView(mContext);
				ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, replyView, 1, 0, null);
				replyView.setValues(reply);
				replyLinear.addView(replyView);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
		
		if(needAutoScroll) {
			needAutoScroll = !needAutoScroll;
			
			mThisView.postDelayed(new Runnable() {

				@Override
				public void run() {

					scrollView.smoothScrollTo(0, replyLinear.getTop());
				}
			}, 500);
		}
	}

	public void writeReply() {

		try {
			//http://cph-app.co.kr/orders/replies/save?order_id=20&content=%EB%8C%93%EA%B8%80%EB%8C%93%EA%B8%80
			String url = CphConstants.BASE_API_URL + "orders/replies/save" +
					"?order_collapse_key=" + orderSet.getCollapse_key() +
					"&content=" + URLEncoder.encode(etReply.getText().toString(), "utf-8");
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("RetailForOrderPage.writeReply.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToWriteReply);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("RetailForOrderPage.writeReply.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 1) {
							etReply.setText(null);
							needAutoScroll = true;
							loadReplies();
							ToastUtils.showToast(R.string.complete_writeReply);
							SoftKeyboardUtils.hideKeyboard(mContext, etReply);
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						ToastUtils.showToast(R.string.failToWriteReply);
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						ToastUtils.showToast(R.string.failToWriteReply);
						LogUtils.trace(oom);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public void deleteReply(final Reply reply) {
		
		String url = CphConstants.BASE_API_URL + "orders/replies/delete" +
				"?order_id=" + reply.getProduct_id() +
				"&reply_id=" + reply.getId();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("RetailForOrderPage.deleteReply.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToDeleteReply);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("RetailForOrderPage.deleteReply.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						replies.remove(reply);
						addReplies();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToDeleteReply);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToDeleteReply);
					LogUtils.trace(oom);
				}
			}
		});
	}
}

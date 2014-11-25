package com.cmons.cph.fragments.wholesale;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
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
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Account;
import com.cmons.cph.models.Order;
import com.cmons.cph.models.OrderSet;
import com.cmons.cph.models.Reply;
import com.cmons.cph.models.Retail;
import com.cmons.cph.views.OrderView;
import com.cmons.cph.views.ReplyView;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class WholesaleForOrderPage extends CmonsFragmentForWholesale {

	private OrderSet orderSet;
	
	private TextView tvOrder;
	private View type;
	private View order;
	private View standBy;
	private View deposit;
	private View completed;
	private TextView tvSoldOut;
	private ScrollView scrollView;
	private LinearLayout orderLinear;
	private TextView tvAccount;
	private TextView tvPrice;
	private Button btnConfirm;
	private LinearLayout replyLinear;
	private EditText etReply;
	private Button btnSubmit;
	
	private long totalPrice = -1;
	private boolean needAutoScroll;
	
	ArrayList<Order> orders = new ArrayList<Order>();
	ArrayList<Reply> replies = new ArrayList<Reply>();
	
	@Override
	public void onResume() {
		super.onResume();
		setTypeImage();
		
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

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleOrderPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.wholesaleOrderPage_ivBg);
		
		tvOrder = (TextView) mThisView.findViewById(R.id.wholesaleOrderPage_tvOrder);
		type = mThisView.findViewById(R.id.wholesaleOrderPage_type);
		
		order = mThisView.findViewById(R.id.wholesaleOrderPage_order);
		standBy = mThisView.findViewById(R.id.wholesaleOrderPage_standBy);
		deposit = mThisView.findViewById(R.id.wholesaleOrderPage_deposit);
		completed = mThisView.findViewById(R.id.wholesaleOrderPage_completed);
		
		scrollView = (ScrollView) mThisView.findViewById(R.id.wholesaleOrderPage_scrollView);
		orderLinear = (LinearLayout) mThisView.findViewById(R.id.wholesaleOrderPage_orderLinear);
		tvSoldOut = (TextView) mThisView.findViewById(R.id.wholesaleOrderPage_tvSoldOut);
		tvAccount = (TextView) mThisView.findViewById(R.id.wholesaleOrderPage_tvAccount);
		tvPrice = (TextView) mThisView.findViewById(R.id.wholesaleOrderPage_tvPrice);
		btnConfirm = (Button) mThisView.findViewById(R.id.wholesaleOrderPage_btnConfirm);
		
		replyLinear = (LinearLayout) mThisView.findViewById(R.id.wholesaleOrderPage_replyLinear);
		etReply = (EditText) mThisView.findViewById(R.id.wholesaleOrderPage_etReply);
		btnSubmit = (Button) mThisView.findViewById(R.id.wholesaleOrderPage_btnSubmit);
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

		switch(orderSet.getStatus()) {
		
		case 0:
			order.setBackgroundResource(R.drawable.order_recommand_a);
			standBy.setBackgroundResource(R.drawable.order_wait_b);
			deposit.setBackgroundResource(R.drawable.order_done_b);
			completed.setBackgroundResource(R.drawable.order_complete_b);
			tvSoldOut.setVisibility(View.VISIBLE);
			btnConfirm.setVisibility(View.VISIBLE);
			btnConfirm.setBackgroundResource(R.drawable.order_approve_btn);
			break;
			
		case 1:
			order.setBackgroundResource(R.drawable.order_recommand_b);
			standBy.setBackgroundResource(R.drawable.order_wait_a);
			deposit.setBackgroundResource(R.drawable.order_done_b);
			completed.setBackgroundResource(R.drawable.order_complete_b);
			tvSoldOut.setVisibility(View.GONE);
			btnConfirm.setVisibility(View.GONE);
			break;
			
		case 2:
			order.setBackgroundResource(R.drawable.order_recommand_b);
			standBy.setBackgroundResource(R.drawable.order_wait_b);
			deposit.setBackgroundResource(R.drawable.order_done_a);
			completed.setBackgroundResource(R.drawable.order_complete_b);
			tvSoldOut.setVisibility(View.GONE);
			btnConfirm.setVisibility(View.VISIBLE);
			btnConfirm.setBackgroundResource(R.drawable.retail_complete4_btn);
			break;
			
		case 3:
			order.setBackgroundResource(R.drawable.order_recommand_b);
			standBy.setBackgroundResource(R.drawable.order_wait_b);
			deposit.setBackgroundResource(R.drawable.order_done_b);
			completed.setBackgroundResource(R.drawable.order_complete_a);
			tvSoldOut.setVisibility(View.GONE);
			btnConfirm.setVisibility(View.VISIBLE);
			btnConfirm.setBackgroundResource(R.drawable.retail_delete4_btn);
			break;
		}
		
		if(orderSet.getStatus() != 3) {
			titleBar.getBtnDeny().setVisibility(View.VISIBLE);
		} else {
			titleBar.getBtnDeny().setVisibility(View.INVISIBLE);
		}
		
		tvOrder.setText(null);
		String dateString = StringUtils.getDateString("yyyy.MM.dd aa hh:mm", 
				orderSet.getItems()[0].getCreated_at() * 1000);
		FontUtils.addSpan(tvOrder, dateString, 0, 0.8f);
		FontUtils.addSpan(tvOrder, "\n" + orderSet.getRetail_name(), 0, 1.5f);
		FontUtils.addSpan(tvOrder, " (" + orderSet.getRetail_phone_number() + ")", 0, 1);
		
		String accountString = "결제방식 : ";

		if(orderSet.getPayment_type() == 1) {
			accountString += "무통장입금";

			if(getWholesale().getAccounts() != null) {

				for(Account account : getWholesale().getAccounts()) {
					
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
		
		accountString += "\n총 금액";
		
		tvAccount.setText(accountString);

		if(orders.size() == 0 && orderSet != null) {
			
			for(Order order : orderSet.getItems()) {
				order.setItemCode(CphConstants.ITEM_ORDER_WHOLESALE);
				
				if(order.getStatus() == orderSet.getStatus()) {
					order.setChecked(true);
				} else {
					order.setChecked(false);
				}
				
				order.setParentStatus(orderSet.getStatus());
				orders.add(order);
			}
		}
		
		totalPrice = 0;
		int size = orderSet.getItems().length;
		for(int i=0; i<size; i++) {
			
			if(orderSet.getItems()[i].isChecked()) {
				totalPrice += orderSet.getItems()[i].getProduct_price()
						* orderSet.getItems()[i].getAmount();
			}
		}
		
		tvPrice.setText(StringUtils.getFormattedNumber(totalPrice) + "원");
	}

	@Override
	public void setListeners() {
		
		titleBar.getBtnDeny().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showAlertDialog(R.string.deny, R.string.wannaDenyOrder, 
						R.string.confirm, R.string.cancel, 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {

								denyOrder();
							}
						}, null);
			}
		});
		
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(totalPrice == 0) {
					ToastUtils.showToast("선택된 상품이 없습니다");
					return;
				}
				
				if(orderSet.getStatus() != 3) {
					changeOrderStatus();
				} else {
					deleteOrder();
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
		
		//tvOrder.
		rp = (RelativeLayout.LayoutParams) tvOrder.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		int p = ResizeUtils.getSpecificLength(10);
		tvOrder.setPadding(p, p, p, p);
		
		//type.
		rp = (RelativeLayout.LayoutParams) type.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(146);
		rp.height = ResizeUtils.getSpecificLength(30);
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
		
		//tvSoldOut.
		rp = (RelativeLayout.LayoutParams) tvSoldOut.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);

		//tvAccount.
		tvAccount.setPadding(p, p, p, p);
		
		//tvPrice.
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

		return R.layout.fragment_wholesale_order;
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

//////////////////// Custom methods.

	public void addToTotalPrice(long addedPrice) {
		
		totalPrice += addedPrice;
		tvPrice.setText(StringUtils.getFormattedNumber(totalPrice) + "원");
	}

	public void changeOrderStatus() {
		
		//status : 변경할 상태값 (-1: 주문취소, 0: 주문요청, 1: 입금대기, 2: 입금완료, 3: 거래완료)
		String url = CphConstants.BASE_API_URL + "wholesales/orders/set_status" +
				"?status=" + (orderSet.getStatus() + 1);
		
		//"?order_ids[]=" + order_ids +
		int size = orderSet.getItems().length;
		for(int i=0; i<size; i++) {
			
			if(orderSet.getItems()[i].isChecked()) {
				url += "&order_ids[]=" + orderSet.getItems()[i].getId();
			}
		}
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForOrderPage.changeOrderStatus.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToChangeOrderStatus);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForOrderPage.changeOrderStatus.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						
						switch(orderSet.getStatus()) {
						
						case 0:
							ToastUtils.showToast(R.string.complete_changeOrderStatus1);
							orderSet.setStatus(1);
							
							long sum = 0;
							
							int size = orderSet.getItems().length;
							for(int i=0; i<size; i++) {
								
								if(orderSet.getItems()[i].isChecked()) {
									sum += orderSet.getItems()[i].getProduct_price()
											* orderSet.getItems()[i].getAmount();
									orderSet.getItems()[i].setStatus(1);
								}
							}
							
							orderSet.setSum(sum);
							break;
						
						case 2:
							ToastUtils.showToast(R.string.complete_changeOrderStatus3);
							orderSet.setStatus(3);
							mActivity.closePageWithRefreshPreviousPage();
							break;
						}
						
						createPage();
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
	
	public void denyOrder() {
		
		String url = CphConstants.BASE_API_URL + "wholesales/orders/set_status" +
				"?status=-1";
		
		//"?order_ids[]=" + order_ids +
		int size = orderSet.getItems().length;
		for(int i=0; i<size; i++) {
			
			if(orderSet.getItems()[i].isChecked()) {
				url += "&order_ids[]=" + orderSet.getItems()[i].getId();
			}
		}
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForOrderPage.changeOrderStatus.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToDeny);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForOrderPage.changeOrderStatus.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						mActivity.closePageWithRefreshPreviousPage();
						ToastUtils.showToast(R.string.complete_deny);
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToDeny);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToDeny);
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void deleteOrder() {

		//http://cph.minsangk.com/wholesales/orders/delete?collapse_key=10001
		String url = CphConstants.BASE_API_URL + "wholesales/orders/delete" +
				"?collapse_key=" + orderSet.getCollapse_key();
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForOrderPage.changeOrderStatus.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToDeleteOrder);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForOrderPage.changeOrderStatus.onCompleted." + "\nurl : " + url
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

	public void setTypeImage() {
		
		String url = CphConstants.BASE_API_URL + "retails/show" + 
				"?retail_id=" + orderSet.getRetail_id();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForOrderPage.setTypeImage.onError." + "\nurl : " + url);
				type.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForOrderPage.setTypeImage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					Retail retail = new Retail(objJSON.getJSONObject("retail"));
					
					if(StringUtils.isEmpty(retail.getMall_url())) {
						type.setBackgroundResource(R.drawable.offline_shop_icon);
					} else {
						type.setBackgroundResource(R.drawable.online_shop_icon);
					}
					
					type.setVisibility(View.VISIBLE);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
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
			
			LogUtils.log("###WholesaleForOrderpage.addOrders.  order : " + order.getProduct_name());
			
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
	
	public void clearReplies() {
		
		replies.clear();
		replyLinear.removeAllViews();
	}

	public void loadReplies() {

		//http://cph-app.co.kr/orders/replies?order_collapse_key=CICPYwiRYAoqAVoPebtnYSvuVzRmxwdL
		String url = CphConstants.BASE_API_URL + "orders/replies"
				+ "?order_collapse_key=" + orderSet.getCollapse_key();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForOrderPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToLoadReplies);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForOrderPage.onCompleted." + "\nurl : " + url
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
			
			LogUtils.log("###WholesaleForOrderpage.addReplies.  reply.content : " + reply.getContent());
			
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

					LogUtils.log("WholesaleForOrderPage.writeReply.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToWriteReply);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("WholesaleForOrderPage.writeReply.onCompleted." + "\nurl : " + url
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

				LogUtils.log("WholesaleForOrderPage.deleteReply.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToDeleteReply);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForOrderPage.deleteReply.onCompleted." + "\nurl : " + url
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

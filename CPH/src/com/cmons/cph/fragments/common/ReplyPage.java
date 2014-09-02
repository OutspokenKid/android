package com.cmons.cph.fragments.common;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.cmons.cph.classes.CmonsFragmentForShop;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Product;
import com.cmons.cph.models.Reply;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;

public class ReplyPage extends CmonsFragmentForShop {

	private Product product;

	private EditText etReply;
	private Button btnSubmit;
	private TextView tvPrivate;
	private View cbPrivate;
	private View icPrivate;
	private ListView listView;
	
	private boolean isPrivate;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.replyPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.replyPage_ivBg);
		
		etReply = (EditText) mThisView.findViewById(R.id.replyPage_etReply);
		btnSubmit = (Button) mThisView.findViewById(R.id.replyPage_btnSubmit);
		tvPrivate = (TextView) mThisView.findViewById(R.id.replyPage_tvPrivate);
		cbPrivate = mThisView.findViewById(R.id.replyPage_cbPrivate);
		icPrivate = mThisView.findViewById(R.id.replyPage_icPrivate);
		
		listView = (ListView) mThisView.findViewById(R.id.replyPage_listView);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			product = (Product) getArguments().getSerializable("product");
			title = product.getName();
		}
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.WHITE));
	}

	@Override
	public void setListeners() {

		icPrivate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				isPrivate = !isPrivate;
				
				if(isPrivate) {
					cbPrivate.setBackgroundResource(R.drawable.reply_checkbox_b);
				} else {
					cbPrivate.setBackgroundResource(R.drawable.reply_checkbox_a);
				}
			}
		});
		
		cbPrivate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				isPrivate = !isPrivate;
				
				if(isPrivate) {
					cbPrivate.setBackgroundResource(R.drawable.reply_checkbox_b);
				} else {
					cbPrivate.setBackgroundResource(R.drawable.reply_checkbox_a);
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
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		int p = ResizeUtils.getSpecificLength(10);
		//etReply.
		rp = (RelativeLayout.LayoutParams) etReply.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(550);
		rp.height = ResizeUtils.getSpecificLength(92);
		etReply.setPadding(p, 0, p, 0);
		
		//btnSubmit.
		rp = (RelativeLayout.LayoutParams) btnSubmit.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);

		//tvPrivate.
		rp = (RelativeLayout.LayoutParams) tvPrivate.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(56);
		tvPrivate.setPadding(p, 0, 0, 0);

		//cbPrivate.
		rp = (RelativeLayout.LayoutParams) cbPrivate.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(44);
		rp.height = ResizeUtils.getSpecificLength(43);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		rp.bottomMargin = ResizeUtils.getSpecificLength(6);
		
		//icPrivate.
		rp = (RelativeLayout.LayoutParams) icPrivate.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(97);
		rp.height = ResizeUtils.getSpecificLength(27);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		rp.bottomMargin = ResizeUtils.getSpecificLength(15);
		
		FontUtils.setFontSize(etReply, 30);
		FontUtils.setFontSize(tvPrivate, 24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_reply;
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

		try {
			JSONArray arJSON = objJSON.getJSONArray("replies");
			int size = arJSON.length();
			
			for(int i=0; i<size; i++) {
				Reply reply = new Reply(arJSON.getJSONObject(i));
				reply.setItemCode(CphConstants.ITEM_REPLY);
				models.add(reply);
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
		
		url = CphConstants.BASE_API_URL + "products/replies" +
				"?product_id=" + product.getId();
		super.downloadInfo();
	}
	
	@Override
	public int getBgResourceId() {

		return R.drawable.staff_bg;
	}
	
//////////////////// Custom methods.

	public void writeReply() {

		try {
			String url = CphConstants.BASE_API_URL + "products/replies/save" +
					"?product_id=" + product.getId() +
					"&is_private=" + (isPrivate? 1 : 0) +
					"&content=" + URLEncoder.encode(etReply.getText().toString(), "utf-8");
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("ReplyPage.writeReply.onError." + "\nurl : " + url);

				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("ReplyPage.writeReply.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 1) {
							etReply.setText(null);
							refreshPage();
							SoftKeyboardUtils.hideKeyboard(mContext, etReply);
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
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}

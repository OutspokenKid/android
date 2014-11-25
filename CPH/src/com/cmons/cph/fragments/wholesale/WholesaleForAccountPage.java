package com.cmons.cph.fragments.wholesale;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
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
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Account;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class WholesaleForAccountPage extends CmonsFragmentForWholesale {

	private TextView tvAccountText;
	private Button btnBank;
	private EditText etName;
	private Button btnAdd;
	private EditText etAccount;

	private ListView listView;
	
	private int bankIndex = -1;

	@Override
	public void onResume() {
		super.onResume();
		
		downloadInfo();
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleAccountPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.wholesaleAccountPage_ivBg);
		
		tvAccountText = (TextView) mThisView.findViewById(R.id.wholesaleAccountPage_tvAccountText);
		btnBank = (Button) mThisView.findViewById(R.id.wholesaleAccountPage_btnBank);
		btnAdd = (Button) mThisView.findViewById(R.id.wholesaleAccountPage_btnAdd);
		
		etName = (EditText) mThisView.findViewById(R.id.wholesaleAccountPage_etName);
		etAccount = (EditText) mThisView.findViewById(R.id.wholesaleAccountPage_etAccount);
		
		listView = (ListView) mThisView.findViewById(R.id.wholesaleAccountPage_listView);
	}

	@Override
	public void setVariables() {

		title = "계좌번호 관리";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
	}

	@Override
	public void setListeners() {

		btnBank.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				final String[] banks = new String[]{
						"국민", "기업", "농협", "부산", "신한", 
						"외환", "우리", "제일", "하나", "경남", "광주", "새마을금고"
				};
				
				mActivity.showSelectDialog(null, banks, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						bankIndex = which;
						btnBank.setText(banks[which]);
					}
				});
			}
		});
		
		btnAdd.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View view) {

				if(bankIndex == -1) {
					ToastUtils.showToast(R.string.wrongBank);
					return;
					
				} else if(etName.getText() == null || etName.getText().length() == 0) {
					ToastUtils.showToast(R.string.wrongName);
					return;
					
				} else if(etAccount.getText() == null || etAccount.getText().length() == 0) {
					ToastUtils.showToast(R.string.wrongAccount);
					return;
				}
				
				addAccount();
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				final int clickedIndex = arg2;
				
				mActivity.showAlertDialog("계좌 삭제", "해당 계좌를 삭제하시겠습니까?\n\n", 
						"확인", "취소", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						
						deleteAccount(((Account)models.get(clickedIndex)));
					}
				}, null);
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		int p = ResizeUtils.getSpecificLength(10);
		
		//tvTitleText.
		rp = (RelativeLayout.LayoutParams) tvAccountText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(98);
		FontUtils.setFontSize(tvAccountText, 30);
		tvAccountText.setPadding(p, 0, p, p);
		
		//btnBank.
		rp = (RelativeLayout.LayoutParams) btnBank.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(183);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//etName.
		rp = (RelativeLayout.LayoutParams) etName.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(367);
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontAndHintSize(etName, 30, 24);
		
		//btnAdd.
		rp = (RelativeLayout.LayoutParams) btnAdd.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//etAccount.
		rp = (RelativeLayout.LayoutParams) etAccount.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontAndHintSize(etAccount, 30, 24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_account;
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
			JSONArray arJSON = objJSON.getJSONArray("accounts");

			int size = arJSON.length();
			for(int i=0; i<size; i++) {
				Account account = new Account(arJSON.getJSONObject(i));
				account.setItemCode(CphConstants.ITEM_ACCOUNT);
				models.add(account);
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

		url = CphConstants.BASE_API_URL + "wholesales/accounts?num=0";
		super.downloadInfo();
	}
	
//////////////////// Custom methods.
	
	public void addAccount() {
		
		try {
			String url = CphConstants.BASE_API_URL + "wholesales/accounts/add" +
					"?bank=" + URLEncoder.encode(btnBank.getText().toString(), "utf-8") +
					"&depositor=" + URLEncoder.encode(etName.getText().toString(), "utf-8") +
					"&number=" + URLEncoder.encode(etAccount.getText().toString(), "utf-8");
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("WholesaleForAccountPage.onError." + "\nurl : " + url);

				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("WholesaleForAccountPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						refreshPage();
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
	
	public void deleteAccount(Account account) {
		
		try {
			String url = CphConstants.BASE_API_URL + "wholesales/accounts/delete" +
					"?account_id=" + account.getId();
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("WholesaleForAccountPage.onError." + "\nurl : " + url);

				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("WholesaleForAccountPage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						refreshPage();
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

	@Override
	public int getBgResourceId() {

		return R.drawable.shop_bg;
	}
}

package com.cmons.cph.fragments.wholesale;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class WholesaleForChangePasswordPage extends CmonsFragmentForWholesale {

	private TextView tvCurrentPassword;
	private EditText etCurrentPassword;
	private TextView tvNewPassword;
	private EditText etNewPassword;
	private TextView tvNewPasswordConfirm;
	private EditText etNewPasswordConfirm;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleChangePasswordPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.wholesaleChangePasswordPage_ivBg);
		
		tvCurrentPassword = (TextView) mThisView.findViewById(R.id.wholesaleChangePasswordPage_tvCurrentPassword);
		etCurrentPassword = (EditText) mThisView.findViewById(R.id.wholesaleChangePasswordPage_etCurrentPassword);
		
		tvNewPassword = (TextView) mThisView.findViewById(R.id.wholesaleChangePasswordPage_tvNewPassword);
		etNewPassword = (EditText) mThisView.findViewById(R.id.wholesaleChangePasswordPage_etNewPassword);
		
		tvNewPasswordConfirm = (TextView) mThisView.findViewById(R.id.wholesaleChangePasswordPage_tvNewPasswordConfirm);
		etNewPasswordConfirm = (EditText) mThisView.findViewById(R.id.wholesaleChangePasswordPage_etNewPasswordConfirm);
	}

	@Override
	public void setVariables() {

		title = "비밀번호 변경";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
	}

	@Override
	public void setListeners() {

		titleBar.getBtnSubmit().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(StringUtils.checkTextLength(etCurrentPassword, 6, 64) != StringUtils.PASS) {
					ToastUtils.showToast(R.string.wrongCurrentPw);
					return;
					
				} else if(StringUtils.checkTextLength(etNewPassword, 6, 64) != StringUtils.PASS) {
					ToastUtils.showToast(R.string.wrongNewPw);
					return;
					
				} else if(StringUtils.checkTextLength(etNewPasswordConfirm, 6, 64) != StringUtils.PASS
						|| !etNewPassword.getText().toString().equals(etNewPasswordConfirm.getText().toString())) {
					ToastUtils.showToast(R.string.check_pwConfirm);
					return;
				}
				
				submit();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		int p = ResizeUtils.getSpecificLength(10);
		
		//tvCurrentPassword.
		rp = (RelativeLayout.LayoutParams) tvCurrentPassword.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		tvCurrentPassword.setPadding(p, 0, 0, p);
		FontUtils.setFontSize(tvCurrentPassword, 30);
		
		//etCurrentPassword.
		rp = (RelativeLayout.LayoutParams) etCurrentPassword.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontAndHintSize(etCurrentPassword, 30, 24);
		
		//tvNewPassword.
		rp = (RelativeLayout.LayoutParams) tvNewPassword.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		tvNewPassword.setPadding(p, 0, 0, p);
		FontUtils.setFontSize(tvNewPassword, 30);
		
		//etNewPassword.
		rp = (RelativeLayout.LayoutParams) etNewPassword.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontAndHintSize(etNewPassword, 30, 24);
		
		//tvNewPasswordConfirm.
		rp = (RelativeLayout.LayoutParams) tvNewPasswordConfirm.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		tvNewPasswordConfirm.setPadding(p, 0, 0, p);
		FontUtils.setFontSize(tvNewPasswordConfirm, 30);
		
		//etNewPasswordConfirm.
		rp = (RelativeLayout.LayoutParams) etNewPasswordConfirm.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontAndHintSize(etNewPasswordConfirm, 30, 24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_changepassword;
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

//////////////////// Custom classes.
	
	public void submit() {
		
		try {
			final String pw = etNewPassword.getText().toString();
			
			url = CphConstants.BASE_API_URL + "users/update/password" +
					"?old=" + URLEncoder.encode(etCurrentPassword.getText().toString(), "utf-8") +
					"&new=" + URLEncoder.encode(pw, "utf-8");
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("from.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToChangePw);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("from.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 1) {
							ToastUtils.showToast(R.string.complete_changePw);
							SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_SIGN, "pw", pw);
							mActivity.closeTopPage();
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToChangePw);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToChangePw);
					}
				}
			});
		} catch (UnsupportedEncodingException e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.setting_bg2;
	}
}

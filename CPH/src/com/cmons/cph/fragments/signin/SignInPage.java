package com.cmons.cph.fragments.signin;

import org.json.JSONObject;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cmons.cph.R;
import com.cmons.cph.SignInActivity;
import com.cmons.cph.classes.CmonsFragment;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SignInPage extends CmonsFragment {

	private View logo;
	private EditText etId;
	private EditText etPw;
	private Button btnLogin;
	private Button btnSignUp;
	private Button btnFindId;
	private Button btnFindPw;
	
	@Override
	public void onResume() {
		super.onResume();
		
		SoftKeyboardUtils.hideKeyboard(mContext, etId);
		
		/*
		http://cph.minsangk.com/users/join
		?user[id]=korea2001
		&user[pw]=qqqqqq
		&user[role]=200
		&user[name]=%EB%8D%94%EB%8D%94
		&retail[name]=%EB%8D%94%EB%8D%94
		&retail[address]=%EB%8D%94%EB%8D%94%EB%8D%94%EB%8D%94
		&retail[mall_url]=
		&retail[phone_number]=46464646
		&retail[corp_reg_number]=4646464664646
		&phone_auth_key=BGwYCjTYNWEnhbEFkANfGLGLJJjnFcCwPAQrtzzNUXkpYdDrscxjWfQJcrLsAGzK
		*/
	}
	
	@Override
	public void bindViews() {
		
		ivBg = (ImageView) mThisView.findViewById(R.id.signInPage_ivBg);
		
		logo = mThisView.findViewById(R.id.signInPage_logo);
		etId = (EditText) mThisView.findViewById(R.id.signInPage_etId);
		etPw = (EditText) mThisView.findViewById(R.id.signInPage_etPw);
		btnLogin = (Button) mThisView.findViewById(R.id.signInPage_btnLogin);
		btnSignUp = (Button) mThisView.findViewById(R.id.signInPage_btnSignUp);
		btnFindId = (Button) mThisView.findViewById(R.id.signInPage_btnFindId);
		btnFindPw = (Button) mThisView.findViewById(R.id.signInPage_btnFindPw);
	}

	@Override
	public void setVariables() {

	}

	@Override
	public void createPage() {

		View bottomBlank = new View(mContext);
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(10, ResizeUtils.getSpecificLength(110));
		rp.addRule(RelativeLayout.BELOW, R.id.signInPage_btnFindId);
		bottomBlank.setLayoutParams(rp);
		
		if(((RelativeLayout) mThisView.findViewById(R.id.signInPage_relative)).getChildCount() == 0) {
			((RelativeLayout) mThisView.findViewById(R.id.signInPage_relative)).addView(bottomBlank);
		}
	}

	@Override
	public void setListeners() {
		
		etPw.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					
					checkIdPw();
				}
				
				return false;
			}
		});
		
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				checkIdPw();
			}
		});
		
		btnSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				((SignInActivity) getActivity()).launchSignUpActivity();
			}
		});
		
		btnFindId.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				((SignInActivity) getActivity()).showFindPage(FindIdPwPage.TYPE_FIND_ID);
			}
		});
		
		btnFindPw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				((SignInActivity) getActivity()).showFindPage(FindIdPwPage.TYPE_FIND_PW);
			}
		});
	}

	@Override
	public void setSizes() {

		int padding = ResizeUtils.getSpecificLength(30);
		RelativeLayout.LayoutParams rp = null;
		
		rp = (RelativeLayout.LayoutParams) logo.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(369);
		rp.height = ResizeUtils.getSpecificLength(210);
		rp.topMargin = ResizeUtils.getSpecificLength(150);
		
		rp = (RelativeLayout.LayoutParams) etId.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.topMargin = ResizeUtils.getSpecificLength(180);
		etId.setPadding(padding, 0, padding, 0);
		FontUtils.setFontSize(etId, 25);
		
		rp = (RelativeLayout.LayoutParams) etPw.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		etPw.setPadding(padding, 0, padding, 0);
		FontUtils.setFontSize(etPw, 25);
		
		rp = (RelativeLayout.LayoutParams) btnFindId.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/2;
		rp.height = ResizeUtils.getScreenWidth()/4;
		
		rp = (RelativeLayout.LayoutParams) btnFindPw.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/2;
		rp.height = ResizeUtils.getScreenWidth()/4;
		
		rp = (RelativeLayout.LayoutParams) btnLogin.getLayoutParams();
		rp.height = ResizeUtils.getScreenWidth()/2;
		
		rp = (RelativeLayout.LayoutParams) btnSignUp.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
	}
	
	@Override
	public int getContentViewId() {
		
		return R.layout.fragment_sign_in;
	}

	@Override
	public void refreshPage() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void checkIdPw() {
		
		if(StringUtils.checkTextLength(etId, 6, 64) != StringUtils.PASS
				|| StringUtils.checkForbidContains(etId, false, true, false, true, true, true)) {
			ToastUtils.showToast(R.string.wrongId);
			
		} else if(StringUtils.checkTextLength(etPw, 6, 64) != StringUtils.PASS
				|| StringUtils.checkForbidContains(etPw, false, true, false, true, true, true)) {
			ToastUtils.showToast(R.string.wrongPw);
			
		} else {
			((SignInActivity) getActivity()).signIn(etId.getText().toString(), etPw.getText().toString());
		}
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.bg;
	}
}

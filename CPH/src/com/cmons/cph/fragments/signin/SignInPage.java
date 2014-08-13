package com.cmons.cph.fragments.signin;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.cmons.classes.CmonsFragment;
import com.cmons.cph.R;
import com.cmons.cph.SignInActivity;
import com.outspoken_kid.utils.ResizeUtils;
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
	public void bindViews() {

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
		((RelativeLayout) mThisView.findViewById(R.id.signInPage_relative)).addView(bottomBlank);
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
		rp.topMargin = ResizeUtils.getSpecificLength(110);
		
		rp = (RelativeLayout.LayoutParams) etId.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.topMargin = ResizeUtils.getSpecificLength(78);
		etId.setPadding(padding, 0, padding, 0);
		
		rp = (RelativeLayout.LayoutParams) etPw.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.topMargin = ResizeUtils.getSpecificLength(12);
		etPw.setPadding(padding, 0, padding, 0);
		
		rp = (RelativeLayout.LayoutParams) btnLogin.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(74);
		rp.topMargin = ResizeUtils.getSpecificLength(12);
		
		rp = (RelativeLayout.LayoutParams) btnSignUp.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(583);
		rp.height = ResizeUtils.getSpecificLength(74);
		rp.topMargin = ResizeUtils.getSpecificLength(12);
		
		rp = (RelativeLayout.LayoutParams) btnFindId.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(286);
		rp.height = ResizeUtils.getSpecificLength(74);
		rp.topMargin = ResizeUtils.getSpecificLength(12);
		
		rp = (RelativeLayout.LayoutParams) btnFindPw.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(286);
		rp.height = ResizeUtils.getSpecificLength(74);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signInPage_ivCopyright).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(352);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
	}
	
	@Override
	public int getContentViewId() {
		
		return R.layout.fragment_sign_in;
	}

	@Override
	public void onRefreshPage() {
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
}

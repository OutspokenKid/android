package com.byecar.byecarplusfordealer.fragments.user;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.byecar.byecarplusfordealer.R;
import com.byecar.byecarplusfordealer.SignActivity;
import com.byecar.byecarplusfordealer.classes.BCPFragment;
import com.byecar.byecarplusfordealer.classes.BCPFragmentActivity.OnAfterCheckSessionListener;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class SignPage extends BCPFragment {

	private ImageView ivBg;
	private HoloStyleEditText etEmail;
	private HoloStyleEditText etPw;
	private Button btnSignUp;
	private Button btnSignIn;
	private Button btnFindPw;
	
	@Override
	public void bindViews() {
		
		ivBg = (ImageView) mThisView.findViewById(R.id.signPage_ivBg);
		
		etEmail = (HoloStyleEditText) mThisView.findViewById(R.id.signPage_etEmail);
		etPw = (HoloStyleEditText) mThisView.findViewById(R.id.signPage_etPw);
		
		btnSignUp = (Button) mThisView.findViewById(R.id.signPage_btnSignUp);
		btnSignIn = (Button) mThisView.findViewById(R.id.signPage_btnSignIn);
		btnFindPw = (Button) mThisView.findViewById(R.id.signPage_btnFindPw);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

	}

	@Override
	public void setListeners() {
		
		btnSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

//				mActivity.showPage(BCPConstants.PAGE_SIGN_UP_FOR_COMMON, null);
			}
		});
		
		btnSignIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

//				mActivity.showPage(BCPConstants.PAGE_SIGN_UP_FOR_COMMON, null);
			}
		});
		
		btnFindPw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

//				mActivity.showPage(BCPConstants.PAGE_SIGN_UP_FOR_COMMON, null);
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
			
		//logo.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signPage_logo).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(299);
		rp.height = ResizeUtils.getSpecificLength(309);
		rp.topMargin = ResizeUtils.getSpecificLength(160);
		
		//btnSignUp.
		rp = (RelativeLayout.LayoutParams) btnSignUp.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(422);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.bottomMargin = ResizeUtils.getSpecificLength(190);
		
		//btnSignIn.
		rp = (RelativeLayout.LayoutParams) btnSignIn.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(80);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.rightMargin = ResizeUtils.getSpecificLength(40);
		rp.bottomMargin = ResizeUtils.getSpecificLength(40);
		
		//btnFindPw.
		rp = (RelativeLayout.LayoutParams) btnFindPw.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(80);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.rightMargin = ResizeUtils.getSpecificLength(40);
		rp.bottomMargin = ResizeUtils.getSpecificLength(40);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_sign;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
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
	public int getBackButtonResId() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getBackButtonWidth() {

		return 0;
	}

	@Override
	public int getBackButtonHeight() {

		return 0;
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public int getRootViewResId() {

		return R.id.signPage_mainLayout;
	}
	
//////////////////// Custom methods.

	public void checkSession() {

		mActivity.checkSession(new OnAfterCheckSessionListener() {
			
			@Override
			public void onAfterCheckSession(boolean isSuccess, JSONObject objJSON) {

				if(isSuccess) {
					((SignActivity)mActivity).launchMainForUserActivity();
				} else {
					showButtons();
				}
			}
		});
	}
	
	public void showButtons() {
		
		AlphaAnimation aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);
		
		etEmail.setVisibility(View.VISIBLE);
		etPw.setVisibility(View.VISIBLE);
		btnSignUp.setVisibility(View.VISIBLE);
		btnSignIn.setVisibility(View.VISIBLE);
		btnFindPw.setVisibility(View.VISIBLE);
		
		etEmail.startAnimation(aaIn);
		etPw.startAnimation(aaIn);
		btnSignUp.startAnimation(aaIn);
		btnSignIn.startAnimation(aaIn);
		btnFindPw.startAnimation(aaIn);
	}
}

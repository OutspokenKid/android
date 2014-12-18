package com.byecar.byecarplus.fragments.main_for_user;

import org.json.JSONObject;

import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPFragmentForMainForUser;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class CertifyPhoneNumberPage extends BCPFragmentForMainForUser {

	private static int MODE_PHONE_NUMBER = 0;
	private static int MODE_CERTIFICATION_NUMBER = 1;
	
	private TextView tvCertifyPhoneNumber;
	private HoloStyleEditText etPhoneNumber;
	private HoloStyleEditText etCertificationNumber;
	private Button btnConfirm;

	private int mode;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.certifyPhoneNumberPage_titleBar);
		
		tvCertifyPhoneNumber = (TextView) mThisView.findViewById(R.id.certifyPhoneNumberPage_tvCertifyPhoneNumber);
		etPhoneNumber = (HoloStyleEditText) mThisView.findViewById(R.id.certifyPhoneNumberPage_etPhoneNumber);
		etCertificationNumber = (HoloStyleEditText) mThisView.findViewById(R.id.certifyPhoneNumberPage_etCertificationNumber);
		btnConfirm = (Button) mThisView.findViewById(R.id.certifyPhoneNumberPage_btnConfirm);
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {

		etPhoneNumber.setHint(R.string.hintForPhoneNumber);
		etPhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
		
		etCertificationNumber.setHint(R.string.hintForCertificationNumber);
		etCertificationNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
	}

	@Override
	public void setListeners() {
		
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(mode == MODE_PHONE_NUMBER) {
					
					if(checkPhoneNumberLength()) {
						etCertificationNumber.getEditText().requestFocus();
						btnConfirm.setBackgroundResource(R.drawable.phone_confirm_btn);
						mode = MODE_CERTIFICATION_NUMBER;
						sendCertiyingMessage();
					} else {
						ToastUtils.showToast(R.string.checkPhoneNumber);
					}
				} else {
					
					if(checkCertificationNumberLength()) {
						requestCertifying();
					} else {
						ToastUtils.showToast(R.string.checkCertificationNumber);
					}
				}
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		int width = ResizeUtils.getSpecificLength(586);
		int textViewHeight = ResizeUtils.getSpecificLength(60);
		int buttonHeight = ResizeUtils.getSpecificLength(82);
		
		//tvCertifyPhoneNumber.
		rp = (RelativeLayout.LayoutParams) tvCertifyPhoneNumber.getLayoutParams();
		rp.leftMargin = ResizeUtils.getSpecificLength(30);
		rp.topMargin = ResizeUtils.getSpecificLength(60);
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
		
		//etPhoneNumber.
		rp = (RelativeLayout.LayoutParams) etPhoneNumber.getLayoutParams();
		rp.width = width;
		rp.height = textViewHeight;
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);

		//etCertificationNumber.
		rp = (RelativeLayout.LayoutParams) etCertificationNumber.getLayoutParams();
		rp.width = width;
		rp.height = textViewHeight;
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
		
		//btnConfirm.
		rp = (RelativeLayout.LayoutParams) btnConfirm.getLayoutParams();
		rp.width = width;
		rp.height = buttonHeight;
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);

		FontUtils.setFontAndHintSize(etPhoneNumber.getEditText(), 30, 20);
		FontUtils.setFontAndHintSize(etCertificationNumber.getEditText(), 30, 20);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_certify_phone_number;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.phone_reg_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 305;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
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

//////////////////// Custom methods.

	public boolean checkPhoneNumberLength() {
		
		if(etPhoneNumber.getEditText().getText() != null
				&& etPhoneNumber.getEditText().getText().length() == 11) {
			return true;
		}
		
		return false;
	}
	
	public boolean checkCertificationNumberLength() {
		
		if(etCertificationNumber.getEditText().getText() != null
				&& etCertificationNumber.getEditText().getText().length() == 4) {
			return true;
		}
		
		return false;
	}
	
	public void sendCertiyingMessage() {
		
	}
	
	public void requestCertifying() {
		
	}
}

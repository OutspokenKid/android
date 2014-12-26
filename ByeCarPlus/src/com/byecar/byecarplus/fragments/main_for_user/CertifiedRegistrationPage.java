package com.byecar.byecarplus.fragments.main_for_user;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class CertifiedRegistrationPage extends BCPFragment {
	
	private ProgressBar progressBar;
	private TextView tvPercentage;
	private TextView tvPercentage2;
	private TextView tvWriteAllContents;
	private TextView tvInputDealerInfoText;
	private TextView tvDealerInfoCertified;
	private Button btnEditDealerInfo;
	private TextView tvDealerInfo;
	private TextView tvCarInfoText;
	private Button btnCarInfo1;
	private Button btnCarInfo2;
	private Button btnCarInfo3;
	private Button btnCarInfo4;
	private Button btnCarInfo5;
	private Button btnCarInfo6;
	private TextView tvDetailCarInfo;
	private HoloStyleEditText etDetailCarInfo1;
	private HoloStyleEditText etDetailCarInfo2;
	private HoloStyleEditText etDetailCarInfo3;
	private HoloStyleEditText etDetailCarInfo4;
	private HoloStyleEditText etDetailCarInfo5;
	private View termOfUse;
	private Button btnTermOfUse;
	private Button btnComplete;
	
	private int progressValue;
	
	@Override
	public void bindViews() {
		
		titleBar = (TitleBar) mThisView.findViewById(R.id.certifiedRegistrationPage_titleBar);
		
		progressBar = (ProgressBar) mThisView.findViewById(R.id.certifiedRegistrationPage_progressBar);
		tvPercentage = (TextView) mThisView.findViewById(R.id.certifiedRegistrationPage_tvPercentage);
		tvPercentage2 = (TextView) mThisView.findViewById(R.id.certifiedRegistrationPage_tvPercentage2);
		tvWriteAllContents = (TextView) mThisView.findViewById(R.id.certifiedRegistrationPage_tvWriteAllContents);
		tvInputDealerInfoText = (TextView) mThisView.findViewById(R.id.certifiedRegistrationPage_tvInputDealerInfo);
		tvDealerInfoCertified = (TextView) mThisView.findViewById(R.id.certifiedRegistrationPage_tvDealerInfoCertified);
		btnEditDealerInfo = (Button) mThisView.findViewById(R.id.certifiedRegistrationPage_btnEditDealerInfo);
		tvDealerInfo = (TextView) mThisView.findViewById(R.id.certifiedRegistrationPage_tvDealerInfo);
		tvCarInfoText = (TextView) mThisView.findViewById(R.id.certifiedRegistrationPage_tvCarInfo);
		btnCarInfo1 = (Button) mThisView.findViewById(R.id.certifiedRegistrationPage_btnCarInfo1);
		btnCarInfo2 = (Button) mThisView.findViewById(R.id.certifiedRegistrationPage_btnCarInfo2);
		btnCarInfo3 = (Button) mThisView.findViewById(R.id.certifiedRegistrationPage_btnCarInfo3);
		btnCarInfo4 = (Button) mThisView.findViewById(R.id.certifiedRegistrationPage_btnCarInfo4);
		btnCarInfo5 = (Button) mThisView.findViewById(R.id.certifiedRegistrationPage_btnCarInfo5);
		btnCarInfo6 = (Button) mThisView.findViewById(R.id.certifiedRegistrationPage_btnCarInfo6);
		tvDetailCarInfo = (TextView) mThisView.findViewById(R.id.certifiedRegistrationPage_tvDetailCarInfo);
		etDetailCarInfo1 = (HoloStyleEditText) mThisView.findViewById(R.id.certifiedRegistrationPage_etDetailCarInfo1);
		etDetailCarInfo2 = (HoloStyleEditText) mThisView.findViewById(R.id.certifiedRegistrationPage_etDetailCarInfo2);
		etDetailCarInfo3 = (HoloStyleEditText) mThisView.findViewById(R.id.certifiedRegistrationPage_etDetailCarInfo3);
		etDetailCarInfo4 = (HoloStyleEditText) mThisView.findViewById(R.id.certifiedRegistrationPage_etDetailCarInfo4);
		etDetailCarInfo5 = (HoloStyleEditText) mThisView.findViewById(R.id.certifiedRegistrationPage_etDetailCarInfo5);
		termOfUse = mThisView.findViewById(R.id.certifiedRegistrationPage_termOfUse);
		btnTermOfUse = (Button) mThisView.findViewById(R.id.certifiedRegistrationPage_btnTermOfUse);
		btnComplete = (Button) mThisView.findViewById(R.id.certifiedRegistrationPage_btnComplete);
		
	}

	@Override
	public void setVariables() {
		
	}

	@Override
	public void createPage() {

		titleBar.hideBottomLine();
		
		etDetailCarInfo1.getEditText().setTextColor(getResources().getColor(R.color.holo_text));
		etDetailCarInfo2.getEditText().setTextColor(getResources().getColor(R.color.holo_text));
		etDetailCarInfo3.getEditText().setTextColor(getResources().getColor(R.color.holo_text));
		etDetailCarInfo4.getEditText().setTextColor(getResources().getColor(R.color.holo_text));
		etDetailCarInfo5.getEditText().setTextColor(getResources().getColor(R.color.holo_text));
		
		etDetailCarInfo1.getEditText().setHintTextColor(getResources().getColor(R.color.holo_text_hint));
		etDetailCarInfo2.getEditText().setHintTextColor(getResources().getColor(R.color.holo_text_hint));
		etDetailCarInfo3.getEditText().setHintTextColor(getResources().getColor(R.color.holo_text_hint));
		etDetailCarInfo4.getEditText().setHintTextColor(getResources().getColor(R.color.holo_text_hint));
		etDetailCarInfo5.getEditText().setHintTextColor(getResources().getColor(R.color.holo_text_hint));
		
		etDetailCarInfo1.setHint(R.string.hintForDetailCarInfo1);
		etDetailCarInfo2.setHint(R.string.hintForDetailCarInfo2);
		etDetailCarInfo3.setHint(R.string.hintForDetailCarInfo3);
		etDetailCarInfo4.setHint(R.string.hintForDetailCarInfo4);
		etDetailCarInfo5.setHint(R.string.hintForDetailCarInfo5);
	}

	@Override
	public void setListeners() {

		btnEditDealerInfo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putInt("from", EditUserInfoPage.FROM_REGISTRATION);
				mActivity.showPage(BCPConstants.PAGE_EDIT_USER_INFO, bundle);
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		//progressBar.
		rp = (RelativeLayout.LayoutParams) progressBar.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(30);
		
		//tvPercentage.
		rp = (RelativeLayout.LayoutParams) tvPercentage.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(105);
		rp.height = ResizeUtils.getSpecificLength(30);
		
		//tvPercentage2.
		rp = (RelativeLayout.LayoutParams) tvPercentage2.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(30);
		
		//writeIcon.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.certifiedRegistrationPage_writeIcon).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(16);
		rp.height = ResizeUtils.getSpecificLength(16);
		rp.topMargin = ResizeUtils.getSpecificLength(7);
		rp.rightMargin = ResizeUtils.getSpecificLength(4);
		
		//tvInputDealerInfo.
		rp = (RelativeLayout.LayoutParams) tvInputDealerInfoText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//tvDealerInfoCertified.
		rp = (RelativeLayout.LayoutParams) tvDealerInfoCertified.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);

		//btnEditDealerInfo.
		rp = (RelativeLayout.LayoutParams) btnEditDealerInfo.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(160);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(15);
		rp.rightMargin = ResizeUtils.getSpecificLength(26);
		
		//tvDealerInfo.
		rp = (RelativeLayout.LayoutParams) tvDealerInfo.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(220);

		//tvCarInfo.
		rp = (RelativeLayout.LayoutParams) tvCarInfoText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//btnCarInfo1.
		rp = (RelativeLayout.LayoutParams) btnCarInfo1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		
		//btnCarInfo2.
		rp = (RelativeLayout.LayoutParams) btnCarInfo2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		
		//btnCarInfo3.
		rp = (RelativeLayout.LayoutParams) btnCarInfo3.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		
		//btnCarInfo4.
		rp = (RelativeLayout.LayoutParams) btnCarInfo4.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		
		//btnCarInfo5.
		rp = (RelativeLayout.LayoutParams) btnCarInfo5.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		
		//btnCarInfo6.
		rp = (RelativeLayout.LayoutParams) btnCarInfo6.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		
		//tvDetailCarInfo.
		rp = (RelativeLayout.LayoutParams) tvDetailCarInfo.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//etDetailCarInfo1.
		rp = (RelativeLayout.LayoutParams) etDetailCarInfo1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(60);
		
		//etDetailCarInfo2.
		rp = (RelativeLayout.LayoutParams) etDetailCarInfo2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(32);
		
		//etDetailCarInfo3.
		rp = (RelativeLayout.LayoutParams) etDetailCarInfo3.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(32);
		
		//etDetailCarInfo4.
		rp = (RelativeLayout.LayoutParams) etDetailCarInfo4.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(32);
		
		//etDetailCarInfo5.
		rp = (RelativeLayout.LayoutParams) etDetailCarInfo5.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(32);
		
		//termOfUse.
		rp = (RelativeLayout.LayoutParams) termOfUse.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(313);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		rp.topMargin = ResizeUtils.getSpecificLength(40);

		//btnTermOfUse.
		rp = (RelativeLayout.LayoutParams) btnTermOfUse.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(259);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.rightMargin = ResizeUtils.getSpecificLength(30);
		
		//btnComplete.
		rp = (RelativeLayout.LayoutParams) btnComplete.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		rp.topMargin = ResizeUtils.getSpecificLength(25);
		
		FontUtils.setFontSize(tvPercentage, 24);
		FontUtils.setFontSize(tvPercentage2, 16);
		FontUtils.setFontSize(tvWriteAllContents, 18);
		
		FontUtils.setFontSize(tvInputDealerInfoText, 30);
		FontUtils.setFontSize(tvDealerInfoCertified, 18);
		FontUtils.setFontSize(tvDealerInfo, 28);
		
		FontUtils.setFontSize(tvCarInfoText, 30);
		FontUtils.setFontSize(tvDetailCarInfo, 30);
		FontUtils.setFontAndHintSize(etDetailCarInfo1.getEditText(), 26, 20);
		FontUtils.setFontAndHintSize(etDetailCarInfo2.getEditText(), 26, 20);
		FontUtils.setFontAndHintSize(etDetailCarInfo3.getEditText(), 26, 20);
		FontUtils.setFontAndHintSize(etDetailCarInfo4.getEditText(), 26, 20);
		FontUtils.setFontAndHintSize(etDetailCarInfo5.getEditText(), 26, 20);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_certified_registration;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.demand_back_btn_a;
	}

	@Override
	public int getBackButtonWidth() {

		return 208;
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

	@Override
	public void onResume() {
		super.onResume();
		
		setProgress();
		setDealerInfo();
	}
	
//////////////////// Custom method.
	
	public void setProgress() {
		
		progressValue = 90;

		progressValue = Math.max(progressValue, 100);
		progressValue = Math.min(progressValue, 0);
		
		if(progressValue == 100) {
			tvPercentage.getLayoutParams().width = ResizeUtils.getSpecificLength(105);
		} else {
			tvPercentage.getLayoutParams().width = ResizeUtils.getSpecificLength(95);
		}
		
		
		progressBar.setProgress(progressValue);
		tvPercentage.setText(progressValue + "%");
	}
	
	public void setDealerInfo() {

		tvDealerInfo.setTextColor(getResources().getColor(R.color.color_red));
		tvDealerInfo.setText(R.string.requireCertifyDealerInfo);
	}
}

package com.byecar.byecarplus.fragments.main_for_user;

import org.json.JSONObject;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPFragmentForMainForUser;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class AuctionRegistrationPage extends BCPFragmentForMainForUser {

	private ProgressBar progressBar;
	private TextView tvPercentage;
	private TextView tvPercentage2;
	private TextView tvWriteAllContents;
	private TextView tvCertifyPhoneNumberText;
	private TextView tvRequireCertifyPhoneNumber;
	private Button btnEditPhoneNumber;
	private Button btnCertifyPhoneNumber;
	private TextView tvPhoneNumber;
	private TextView tvInputDealerInfoText;
	private TextView tvRequireInputDealerInfo;
	private Button btnEditDealerInfo;
	private Button btnInputDealerInfo;
	private TextView tvDealerInfo;
	private TextView tvCarPhotoText;
	private Button btnCarPhoto1;
	private Button btnCarPhoto2;
	private Button btnCarPhoto3;
	private Button btnCarPhoto4;
	private TextView tvMainImageText;
	private Button btnAddedPhoto1;
	private Button btnAddedPhoto2;
	private Button btnAddedPhoto3;
	private Button btnAddedPhoto4;
	private ImageView ivAddedPhoto1;
	private ImageView ivAddedPhoto2;
	private ImageView ivAddedPhoto3;
	private ImageView ivAddedPhoto4;
	private Button btnCarInfo1;
	private Button btnCarInfo2;
	private Button btnCarInfo3;
	private Button btnCarInfo4;
	private Button btnCarInfo5;
	private Button btnCarInfo6;
	private HoloStyleEditText etDetailCarInfo1;
	private HoloStyleEditText etDetailCarInfo2;
	private HoloStyleEditText etDetailCarInfo3;
	private HoloStyleEditText etDetailCarInfo4;
	private HoloStyleEditText etDetailCarInfo5;
	private RelativeLayout relativeForOption;
	private TextView tvCarDescriptionFromDealer;
	private EditText etCarDescriptionFromDealer;
	private TextView tvTextCount;
	private View termOfUse;
	private Button btnTermOfUse;
	private View immediatlySale;
	private Button btnImmediatlySale;
	private Button btnComplete;
	
	@Override
	public void bindViews() {
		
		titleBar = (TitleBar) mThisView.findViewById(R.id.auctionRegistrationPage_titleBar);
		
		progressBar = (ProgressBar) mThisView.findViewById(R.id.auctionRegistrationPage_progressBar);
		tvPercentage = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvPercentage);
		tvPercentage2 = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvPercentage2);
		tvWriteAllContents = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvWriteAllContents);
		tvCertifyPhoneNumberText = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvCertifyPhoneNumber);
		tvRequireCertifyPhoneNumber = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvRequireCertifyPhoneNumber);
		btnEditPhoneNumber = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnEditPhoneNumber);
		btnCertifyPhoneNumber = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCertifyPhoneNumber);
		tvPhoneNumber = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvPhoneNumber);
		tvInputDealerInfoText = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvInputDealerInfo);
		tvRequireInputDealerInfo = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvRequireInputDealerInfo);
		btnEditDealerInfo = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnEditDealerInfo);
		btnInputDealerInfo = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnInputDealerInfo);
		tvDealerInfo = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvDealerInfo);
		tvCarPhotoText = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvCarPhoto);
		btnCarPhoto1 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarPhoto1);
		btnCarPhoto2 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarPhoto2);
		btnCarPhoto3 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarPhoto3);
		btnCarPhoto4 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarPhoto4);
		tvMainImageText = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvMainImage);
		btnAddedPhoto1 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnAddedPhoto1);
		btnAddedPhoto2 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnAddedPhoto2);
		btnAddedPhoto3 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnAddedPhoto3);
		btnAddedPhoto4 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnAddedPhoto4);
		ivAddedPhoto1 = (ImageView) mThisView.findViewById(R.id.auctionRegistrationPage_ivAddedPhoto1);
		ivAddedPhoto2 = (ImageView) mThisView.findViewById(R.id.auctionRegistrationPage_ivAddedPhoto2);
		ivAddedPhoto3 = (ImageView) mThisView.findViewById(R.id.auctionRegistrationPage_ivAddedPhoto3);
		ivAddedPhoto4 = (ImageView) mThisView.findViewById(R.id.auctionRegistrationPage_ivAddedPhoto4);
		btnCarInfo1 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarInfo1);
		btnCarInfo2 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarInfo2);
		btnCarInfo3 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarInfo3);
		btnCarInfo4 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarInfo4);
		btnCarInfo5 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarInfo5);
		btnCarInfo6 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarInfo6);
		etDetailCarInfo1 = (HoloStyleEditText) mThisView.findViewById(R.id.auctionRegistrationPage_etDetailCarInfo1);
		etDetailCarInfo2 = (HoloStyleEditText) mThisView.findViewById(R.id.auctionRegistrationPage_etDetailCarInfo2);
		etDetailCarInfo3 = (HoloStyleEditText) mThisView.findViewById(R.id.auctionRegistrationPage_etDetailCarInfo3);
		etDetailCarInfo4 = (HoloStyleEditText) mThisView.findViewById(R.id.auctionRegistrationPage_etDetailCarInfo4);
		etDetailCarInfo5 = (HoloStyleEditText) mThisView.findViewById(R.id.auctionRegistrationPage_etDetailCarInfo5);
		relativeForOption = (RelativeLayout) mThisView.findViewById(R.id.auctionRegistrationPage_relativeForOption);
		tvCarDescriptionFromDealer = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvCarDescriptionFromDealer);
		etCarDescriptionFromDealer = (EditText) mThisView.findViewById(R.id.auctionRegistrationPage_etCarDescriptionFromDealer);
		tvTextCount = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvTextCount);
		termOfUse = mThisView.findViewById(R.id.auctionRegistrationPage_termOfUse);
		btnTermOfUse = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnTermOfUse);
		immediatlySale = mThisView.findViewById(R.id.auctionRegistrationPage_immediatlySale);
		btnImmediatlySale = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnImmediatlySale);
		btnComplete = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnComplete);
		
	}

	@Override
	public void setVariables() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		//progressBar.
		rp = (RelativeLayout.LayoutParams) progressBar.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(30);
		
		//tvPercentage.
		rp = (RelativeLayout.LayoutParams) tvPercentage.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(200);
		rp.height = ResizeUtils.getSpecificLength(30);
		
		//tvPercentage2.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.auctionRegistrationPage_tvPercentage2).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(30);
		
		//writeIcon.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.auctionRegistrationPage_writeIcon).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(16);
		rp.height = ResizeUtils.getSpecificLength(16);
		rp.topMargin = ResizeUtils.getSpecificLength(7);
		rp.rightMargin = ResizeUtils.getSpecificLength(4);
		
		//tvCertifyPhoneNumber.
		rp = (RelativeLayout.LayoutParams) tvCertifyPhoneNumberText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);

		//tvRequireCertifyPhoneNumber.
		rp = (RelativeLayout.LayoutParams) tvRequireCertifyPhoneNumber.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.rightMargin = ResizeUtils.getSpecificLength(26);

		//btnEditPhoneNumber.
		rp = (RelativeLayout.LayoutParams) btnEditPhoneNumber.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(160);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(15);
		rp.rightMargin = ResizeUtils.getSpecificLength(26);

		//btnCertifyPhoneNumber.
		rp = (RelativeLayout.LayoutParams) btnCertifyPhoneNumber.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		
		//tvPhoneNumber.
		rp = (RelativeLayout.LayoutParams) tvPhoneNumber.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(126);
		
		//tvInputDealerInfo.
		rp = (RelativeLayout.LayoutParams) tvInputDealerInfoText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//tvRequireInputDealerInfo.
		rp = (RelativeLayout.LayoutParams) tvRequireInputDealerInfo.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.rightMargin = ResizeUtils.getSpecificLength(26);

		//btnEditDealerInfo.
		rp = (RelativeLayout.LayoutParams) btnEditDealerInfo.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(160);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(15);
		rp.rightMargin = ResizeUtils.getSpecificLength(26);

		//btnInputDealerInfo.
		rp = (RelativeLayout.LayoutParams) btnInputDealerInfo.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		
		//tvDealerInfo.
		rp = (RelativeLayout.LayoutParams) tvDealerInfo.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(126);
		
		//tvCarPhoto.
		rp = (RelativeLayout.LayoutParams) tvCarPhotoText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//btnCarPhoto1.
		rp = (RelativeLayout.LayoutParams) btnCarPhoto1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		
		//btnCarPhoto2.
		rp = (RelativeLayout.LayoutParams) btnCarPhoto2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//btnCarPhoto3.
		rp = (RelativeLayout.LayoutParams) btnCarPhoto3.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//btnCarPhoto4.
		rp = (RelativeLayout.LayoutParams) btnCarPhoto4.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//tvMainImage.
		rp = (RelativeLayout.LayoutParams) tvMainImageText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(32);
		
		//btnAddedPhoto1.
		rp = (RelativeLayout.LayoutParams) btnAddedPhoto1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		
		//ivAddedPhoto1.
		rp = (RelativeLayout.LayoutParams) ivAddedPhoto1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		
		//btnAddedPhoto2.
		rp = (RelativeLayout.LayoutParams) btnAddedPhoto2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//ivAddedPhoto2.
		rp = (RelativeLayout.LayoutParams) ivAddedPhoto2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//btnAddedPhoto3.
		rp = (RelativeLayout.LayoutParams) btnAddedPhoto3.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//ivAddedPhoto3.
		rp = (RelativeLayout.LayoutParams) ivAddedPhoto3.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//btnAddedPhoto4.
		rp = (RelativeLayout.LayoutParams) btnAddedPhoto4.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);
		
		//ivAddedPhoto4.
		rp = (RelativeLayout.LayoutParams) ivAddedPhoto4.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		rp.height = ResizeUtils.getSpecificLength(110);
		rp.leftMargin = ResizeUtils.getSpecificLength(10);

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
		
		//etDetailCarInfo1.
		rp = (RelativeLayout.LayoutParams) etDetailCarInfo1.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(50);
		
		//etDetailCarInfo2.
		rp = (RelativeLayout.LayoutParams) etDetailCarInfo2.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(50);
		rp.topMargin = ResizeUtils.getSpecificLength(32);
		
		//etDetailCarInfo3.
		rp = (RelativeLayout.LayoutParams) etDetailCarInfo3.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(50);
		rp.topMargin = ResizeUtils.getSpecificLength(32);
		
		//etDetailCarInfo4.
		rp = (RelativeLayout.LayoutParams) etDetailCarInfo4.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(50);
		rp.topMargin = ResizeUtils.getSpecificLength(32);
		
		//etDetailCarInfo5.
		rp = (RelativeLayout.LayoutParams) etDetailCarInfo5.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(50);
		rp.topMargin = ResizeUtils.getSpecificLength(32);
		
		//tvCarDescriptionFromDealer.
		rp = (RelativeLayout.LayoutParams) tvCarDescriptionFromDealer.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//etCarDescriptionFromDealer.
		rp = (RelativeLayout.LayoutParams) etCarDescriptionFromDealer.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(160);
		
		//termOfUse.
		rp = (RelativeLayout.LayoutParams) termOfUse.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(313);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.topMargin = ResizeUtils.getSpecificLength(40);
		
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
		
		//immediatlySale.
		rp = (RelativeLayout.LayoutParams) immediatlySale.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(100);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
		//btnImmediatlySale.
		rp = (RelativeLayout.LayoutParams) btnImmediatlySale.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(179);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.rightMargin = ResizeUtils.getSpecificLength(30);
		
		//btnComplete.
		rp = (RelativeLayout.LayoutParams) btnComplete.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		rp.topMargin = ResizeUtils.getSpecificLength(25);
		
		
		FontUtils.setFontSize(tvPercentage, 20);
		FontUtils.setFontSize(tvPercentage2, 16);
		FontUtils.setFontSize(tvWriteAllContents, 18);
		
		FontUtils.setFontSize(tvCertifyPhoneNumberText, 30);
		FontUtils.setFontSize(tvRequireCertifyPhoneNumber, 18);
		FontUtils.setFontSize(tvPhoneNumber, 30);
		
		FontUtils.setFontSize(tvInputDealerInfoText, 30);
		FontUtils.setFontSize(tvRequireInputDealerInfo, 18);
		FontUtils.setFontSize(tvDealerInfo, 30);

		FontUtils.setFontSize(tvCarPhotoText, 30);
		
		FontUtils.setFontSize(tvMainImageText, 30);
		
		
		
//		private TextView tvMainImageText;
//		private HoloStyleEditText etDetailCarInfo1;
//		private HoloStyleEditText etDetailCarInfo2;
//		private HoloStyleEditText etDetailCarInfo3;
//		private HoloStyleEditText etDetailCarInfo4;
//		private HoloStyleEditText etDetailCarInfo5;
//		private TextView tvCarDescriptionFromDealer;
//		private EditText etCarDescriptionFromDealer;
//		private TextView tvTextCount;
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_auction_registration;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.registration_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 220;
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
}

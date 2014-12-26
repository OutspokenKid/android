package com.byecar.byecarplus.fragments.main_for_user;

import java.text.SimpleDateFormat;
import java.util.Locale;

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
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPConstants;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.models.CarModelDetailInfo;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class AuctionRegistrationPage extends BCPFragment {

	private static final int MAX_DESC_COUNT = 200;
	
	private ProgressBar progressBar;
	private TextView tvPercentage;
	private TextView tvPercentage2;
	private TextView tvWriteAllContents;
	private TextView tvInputDealerInfoText;
	private TextView tvDealerInfoCertified;
	private Button btnEditDealerInfo;
	private TextView tvDealerInfo;
	private TextView tvCarPhotoText;
	private Button btnCarPhoto1;
	private Button btnCarPhoto2;
	private Button btnCarPhoto3;
	private Button btnCarPhoto4;
	private TextView tvMainImageText;
	private TextView tvAddedPhotoText;
	private Button btnAddedPhoto1;
	private Button btnAddedPhoto2;
	private Button btnAddedPhoto3;
	private Button btnAddedPhoto4;
	private ImageView ivAddedPhoto1;
	private ImageView ivAddedPhoto2;
	private ImageView ivAddedPhoto3;
	private ImageView ivAddedPhoto4;
	private TextView tvCarInfoText;
	private Button btnCarInfos[];
	private TextView tvDetailCarInfo;
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
	
	private View[] optionViews;
	private boolean[] checked;
	
	private int progressValue;
	
	private CarModelDetailInfo carModelDetailInfo;
	private String[] carInfoStrings = new String[5];
	
	@Override
 	public void bindViews() {
		
		titleBar = (TitleBar) mThisView.findViewById(R.id.auctionRegistrationPage_titleBar);
		
		progressBar = (ProgressBar) mThisView.findViewById(R.id.auctionRegistrationPage_progressBar);
		tvPercentage = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvPercentage);
		tvPercentage2 = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvPercentage2);
		tvWriteAllContents = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvWriteAllContents);
		tvInputDealerInfoText = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvInputDealerInfo);
		tvDealerInfoCertified = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvDealerInfoCertified);
		btnEditDealerInfo = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnEditDealerInfo);
		tvDealerInfo = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvDealerInfo);
		tvCarPhotoText = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvCarPhoto);
		btnCarPhoto1 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarPhoto1);
		btnCarPhoto2 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarPhoto2);
		btnCarPhoto3 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarPhoto3);
		btnCarPhoto4 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarPhoto4);
		tvMainImageText = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvMainImage);
		tvAddedPhotoText = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvAddedPhoto);
		btnAddedPhoto1 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnAddedPhoto1);
		btnAddedPhoto2 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnAddedPhoto2);
		btnAddedPhoto3 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnAddedPhoto3);
		btnAddedPhoto4 = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnAddedPhoto4);
		ivAddedPhoto1 = (ImageView) mThisView.findViewById(R.id.auctionRegistrationPage_ivAddedPhoto1);
		ivAddedPhoto2 = (ImageView) mThisView.findViewById(R.id.auctionRegistrationPage_ivAddedPhoto2);
		ivAddedPhoto3 = (ImageView) mThisView.findViewById(R.id.auctionRegistrationPage_ivAddedPhoto3);
		ivAddedPhoto4 = (ImageView) mThisView.findViewById(R.id.auctionRegistrationPage_ivAddedPhoto4);
		tvCarInfoText = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvCarInfo);
		
		btnCarInfos = new Button[6];
		btnCarInfos[0] = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarInfo1);
		btnCarInfos[1] = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarInfo2);
		btnCarInfos[2] = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarInfo3);
		btnCarInfos[3] = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarInfo4);
		btnCarInfos[4] = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarInfo5);
		btnCarInfos[5] = (Button) mThisView.findViewById(R.id.auctionRegistrationPage_btnCarInfo6);
		
		tvDetailCarInfo = (TextView) mThisView.findViewById(R.id.auctionRegistrationPage_tvDetailCarInfo);
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
		
		etCarDescriptionFromDealer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(MAX_DESC_COUNT)});
		tvTextCount.setText("0 / " + MAX_DESC_COUNT + "자");
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
		
		for(int i=0; i<btnCarInfos.length; i++) {
			final int INDEX = i;
			
			btnCarInfos[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					if(INDEX + 1 != SearchCarPage.TYPE_BRAND && carModelDetailInfo == null) {
						ToastUtils.showToast(R.string.selectCarFirst);
						return;
					}
					
					Bundle bundle = new Bundle();

					switch(INDEX) {
					
					case 0:
						bundle.putInt("type", SearchCarPage.TYPE_BRAND);
						break;
					case 1:
						bundle.putInt("type", SearchCarPage.TYPE_YEAR);
						
						if(!StringUtils.isEmpty(carModelDetailInfo.getYear_begin())) {
							bundle.putInt("year_begin", Integer.parseInt(carModelDetailInfo.getYear_begin()));
						} else {
							bundle.putInt("year_begin", 1990);
						}
						
						if(!StringUtils.isEmpty(carModelDetailInfo.getYear_end())) {
							bundle.putInt("year_end", Integer.parseInt(carModelDetailInfo.getYear_end()));
						} else {
							
							try {
								SimpleDateFormat format = new SimpleDateFormat("yyyy", Locale.getDefault());
								bundle.putInt("year_end", Integer.parseInt(format.format(System.currentTimeMillis())));
							} catch (Exception e) {
								LogUtils.trace(e);
							}
						}
						
						break;
					case 2:
						bundle.putInt("type", SearchCarPage.TYPE_FUEL);
						break;
					case 3:
						bundle.putInt("type", SearchCarPage.TYPE_TRANSMISSION);
						break;
					case 4:
						bundle.putInt("type", SearchCarPage.TYPE_ACCIDENT);
						break;
					case 5:
						bundle.putInt("type", SearchCarPage.TYPE_ONEMANOWNED);
						break;
					}
					
					mActivity.showPage(BCPConstants.PAGE_SEARCH_CAR, bundle);
				}
			});
		}
		
		etCarDescriptionFromDealer.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(s != null && s.length() != 0) {
					tvTextCount.setText(s.length() + " / " + MAX_DESC_COUNT + "자");
				} else {
					tvTextCount.setText("0 / " + MAX_DESC_COUNT + "자");
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {}
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
				R.id.auctionRegistrationPage_writeIcon).getLayoutParams();
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
		
		//tvMainImageText.
		rp = (RelativeLayout.LayoutParams) tvMainImageText.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(144);
		
		//tvAddedPhotoText.
		rp = (RelativeLayout.LayoutParams) tvAddedPhotoText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
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

		//tvCarInfo.
		rp = (RelativeLayout.LayoutParams) tvCarInfoText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//btnCarInfos.
		for(int i=0; i<btnCarInfos.length; i++) {
			rp = (RelativeLayout.LayoutParams) btnCarInfos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(586);
			rp.height = ResizeUtils.getSpecificLength(82);
			rp.topMargin = ResizeUtils.getSpecificLength(i==0?16:40);
		}
		
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
		
		//writeIcon.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.auctionRegistrationPage_tvOption).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
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
		
		FontUtils.setFontSize(tvPercentage, 24);
		FontUtils.setFontSize(tvPercentage2, 16);
		FontUtils.setFontSize(tvWriteAllContents, 18);
		
		FontUtils.setFontSize(tvInputDealerInfoText, 30);
		FontUtils.setFontSize(tvDealerInfoCertified, 18);
		FontUtils.setFontSize(tvDealerInfo, 28);

		FontUtils.setFontSize(tvCarPhotoText, 30);
		FontUtils.setFontSize(tvMainImageText, 20);
		FontUtils.setFontSize(tvAddedPhotoText, 30);
		
		FontUtils.setFontSize(tvCarInfoText, 30);
		FontUtils.setFontSize(tvDetailCarInfo, 30);
		
		for(int i=0; i<btnCarInfos.length; i++) {
			FontUtils.setFontSize(btnCarInfos[i], 26);
		}
		
		FontUtils.setFontAndHintSize(etDetailCarInfo1.getEditText(), 26, 20);
		FontUtils.setFontAndHintSize(etDetailCarInfo2.getEditText(), 26, 20);
		FontUtils.setFontAndHintSize(etDetailCarInfo3.getEditText(), 26, 20);
		FontUtils.setFontAndHintSize(etDetailCarInfo4.getEditText(), 26, 20);
		FontUtils.setFontAndHintSize(etDetailCarInfo5.getEditText(), 26, 20);
		
		FontUtils.setFontSize(tvCarDescriptionFromDealer, 30);
		FontUtils.setFontAndHintSize(etCarDescriptionFromDealer, 26, 20);
		FontUtils.setFontSize(tvTextCount, 20);
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

	@Override
	public void onResume() {
		super.onResume();
		
		if(relativeForOption.getChildCount() == 1) {
			addOptionButtons();
		}
		
		setProgress();
		setDealerInfo();
		
		checkCarInfos();
		checkBundle();
	}
	
//////////////////// Custom method.
	
	public void addOptionButtons() {

		int size = 30;
		optionViews = new View[size];
		checked = new boolean[size];
		
		RelativeLayout.LayoutParams rp = null;
		
		for(int i=0; i<size; i++) {
			
			optionViews[i] = new View(mContext);
			rp = new RelativeLayout.LayoutParams(
					ResizeUtils.getSpecificLength(160), 
					ResizeUtils.getSpecificLength(80));
			
			switch(i % 3) {
			
			case 0:
				rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				rp.leftMargin = ResizeUtils.getSpecificLength(35);
				
				if(i == 0) {
					rp.addRule(RelativeLayout.BELOW, 
							R.id.auctionRegistrationPage_tvOption);
				} else {
					rp.addRule(RelativeLayout.BELOW, 
							getResources().getIdentifier("optionView" + (i - 2),	//i - 3 + 1, 윗줄 아이콘. 
									"id", "com.byecar.byecarplus"));
				}
				
				rp.topMargin = ResizeUtils.getSpecificLength(24);
				break;
			case 1:
				rp.addRule(RelativeLayout.ALIGN_TOP, 
						getResources().getIdentifier("optionView" + i,				//i - 1 + 1. 왼쪽 아이콘.
								"id", "com.byecar.byecarplus"));
				rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
				break;
			case 2:
				rp.addRule(RelativeLayout.ALIGN_TOP, 
						getResources().getIdentifier("optionView" + i,				//i - 1 + 1. 왼쪽 아이콘.
								"id", "com.byecar.byecarplus"));
				rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				rp.rightMargin = ResizeUtils.getSpecificLength(35);
				break;
			}
			
			optionViews[i].setLayoutParams(rp);
			optionViews[i].setId(getResources().getIdentifier("optionView" + (i + 1), 
							"id", "com.byecar.byecarplus"));
			optionViews[i].setBackgroundResource(
					getResources().getIdentifier("detail_optioin" + (i + 1) + "_btn_a", 
							"drawable", "com.byecar.byecarplus"));

			final int INDEX = i;
			optionViews[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					checked[INDEX] = !checked[INDEX];
					
					if(checked[INDEX]) {
						optionViews[INDEX].setBackgroundResource(
								getResources().getIdentifier("detail_optioin" + (INDEX + 1) + "_btn_b", 
										"drawable", "com.byecar.byecarplus"));
					} else {
						optionViews[INDEX].setBackgroundResource(
								getResources().getIdentifier("detail_optioin" + (INDEX + 1) + "_btn_a", 
										"drawable", "com.byecar.byecarplus"));
					}
				}
			});
			
			relativeForOption.addView(optionViews[i]);
		}
	}
	
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

	public void checkBundle() {
		
		if(mActivity.bundle != null) {
			int type = mActivity.bundle.getInt("type");
			
			switch(type) {
			
			case SearchCarPage.TYPE_TRIM:
				String url = BCPAPIs.SEARCH_CAR_DETAIL_INFO
				+ "?trim_id=" + mActivity.bundle.getInt("trim_id");
				DownloadUtils.downloadJSONString(url,
					new OnJSONDownloadListener() {
	
						@Override
						public void onError(String url) {
	
							LogUtils.log("AuctionRegistrationPage.onError." + "\nurl : " + url);
	
						}
	
						@Override
						public void onCompleted(String url,
								JSONObject objJSON) {
	
							try {
								LogUtils.log("AuctionRegistrationPage.onCompleted."
										+ "\nurl : " + url
										+ "\nresult : " + objJSON);

								carModelDetailInfo = new CarModelDetailInfo(objJSON.getJSONObject("car"));
								checkCarInfos();
							} catch (Exception e) {
								LogUtils.trace(e);
							} catch (OutOfMemoryError oom) {
								LogUtils.trace(oom);
							}
						}
					});
				break;
				
			case SearchCarPage.TYPE_YEAR:
			case SearchCarPage.TYPE_FUEL:
			case SearchCarPage.TYPE_TRANSMISSION:
			case SearchCarPage.TYPE_ACCIDENT:
			case SearchCarPage.TYPE_ONEMANOWNED:
				carInfoStrings[type - 5] = mActivity.bundle.getString("text");
				checkCarInfos();
				break;
			}
			
			mActivity.bundle = null;
		}
	}

	public void checkCarInfos() {
		
		if(carModelDetailInfo != null) {
			btnCarInfos[0].setBackgroundResource(R.drawable.registration_car_info_box);
			btnCarInfos[0].setText(carModelDetailInfo.getFull_name());

			for(int i=0; i<5; i++) {
				
				if(!StringUtils.isEmpty(carInfoStrings[i])) {
					btnCarInfos[i + 1].setText(carInfoStrings[i]);
					btnCarInfos[i + 1].setBackgroundResource(R.drawable.registration_car_info_box);
				} else {
					int resId = getResources().getIdentifier("registration_car_info" + (i + 2) + "_btn", "drawable", "com.byecar.byecarplus");
					btnCarInfos[i + 1].setBackgroundResource(resId);
					btnCarInfos[i + 1].setText(null);
				}
			}
		} else {
			btnCarInfos[0].setBackgroundResource(R.drawable.registration_car_info1_btn);
			btnCarInfos[0].setText(null);
		}
	}
}


package com.byecar.fragments.dealer;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPFragment;
import com.byecar.views.TitleBar;
import com.outspoken_kid.activities.BaseFragmentActivity;
import com.outspoken_kid.activities.MultiSelectGalleryActivity.OnAfterPickImageListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;

public class EditDealerInfoPage extends BCPFragment {

	public static final int TYPE_EDIT_PROFILE = 0;
	public static final int TYPE_EDIT_DEALER =1;
	
	private Button[] btnImages;
	private ImageView[] ivImages;
	private TextView tvProfile;
	
	private Button btnEditPhoneNumber;
	private TextView tvPhoneNumber;
	private View checkIcon;
	
	private TextView tvCommonInfoText;
	private HoloStyleEditText[] etInfos;
	
	private TextView tvAddedInfoText;
	private TextView tvUploadText;
	
	private TextView tvIntroduceText;
	private EditText etIntroduce;
	private Button btnComplete;
	
	private int selectedImageIndex;
	private String[] selectedImageSdCardPaths = new String[3];
	
	private int type;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		BaseFragmentActivity.onAfterPickImageListener = new OnAfterPickImageListener() {
			
			@Override
			public void onAfterPickImage(String[] sdCardPaths, Bitmap[] thumbnails) {
				
				if(thumbnails != null && thumbnails.length > 0) {
					ivImages[selectedImageIndex].setImageBitmap(thumbnails[0]);
					selectedImageSdCardPaths[selectedImageIndex] = sdCardPaths[0];
				}
			}
		};
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.editDealerInfoPage_titleBar);
		
		btnImages = new Button[3];
		btnImages[0] = (Button) mThisView.findViewById(R.id.editDealerInfoPage_btnProfile);
		btnImages[1] = (Button) mThisView.findViewById(R.id.editDealerInfoPage_btnImage1);
		btnImages[2] = (Button) mThisView.findViewById(R.id.editDealerInfoPage_btnImage2);
		
		ivImages = new ImageView[3];
		ivImages[0] = (ImageView) mThisView.findViewById(R.id.editDealerInfoPage_ivProfile);
		ivImages[1] = (ImageView) mThisView.findViewById(R.id.editDealerInfoPage_ivImage1);
		ivImages[2] = (ImageView) mThisView.findViewById(R.id.editDealerInfoPage_ivImage2);
		
		tvProfile = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvProfile);
		
		btnEditPhoneNumber = (Button) mThisView.findViewById(R.id.editDealerInfoPage_btnEditPhoneNumber);
		tvPhoneNumber = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvPhoneNumber);
		checkIcon = mThisView.findViewById(R.id.editDealerInfoPage_checkIcon);
		
		tvCommonInfoText = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvCommonInfoText);
		
		etInfos = new HoloStyleEditText[7];
		etInfos[0] = (HoloStyleEditText) mThisView.findViewById(R.id.editDealerInfoPage_etCommonInfo1);
		etInfos[1] = (HoloStyleEditText) mThisView.findViewById(R.id.editDealerInfoPage_etCommonInfo2);
		etInfos[2] = (HoloStyleEditText) mThisView.findViewById(R.id.editDealerInfoPage_etCommonInfo3);
		etInfos[3] = (HoloStyleEditText) mThisView.findViewById(R.id.editDealerInfoPage_etAddedInfo1);
		etInfos[4] = (HoloStyleEditText) mThisView.findViewById(R.id.editDealerInfoPage_etAddedInfo2);
		etInfos[5] = (HoloStyleEditText) mThisView.findViewById(R.id.editDealerInfoPage_etAddedInfo3);
		etInfos[6] = (HoloStyleEditText) mThisView.findViewById(R.id.editDealerInfoPage_etAddedInfo4);
		
		tvAddedInfoText = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvAddedInfoText);
		tvUploadText = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvUploadText);
		
		tvIntroduceText = (TextView) mThisView.findViewById(R.id.editDealerInfoPage_tvIntroduceText);
		etIntroduce = (EditText) mThisView.findViewById(R.id.editDealerInfoPage_etIntroduce);
		btnComplete = (Button) mThisView.findViewById(R.id.editDealerInfoPage_btnComplete);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			type = getArguments().getInt("type");
		}
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
		
		int width = ResizeUtils.getSpecificLength(586);
		int textViewHeight = ResizeUtils.getSpecificLength(60);
		int buttonHeight = ResizeUtils.getSpecificLength(82);

		//profileFrame
		rp = (RelativeLayout.LayoutParams) (mThisView.findViewById(R.id.editDealerInfoPage_profileFrame)).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(219);
		rp.height = ResizeUtils.getSpecificLength(219);
		rp.topMargin = ResizeUtils.getSpecificLength(60);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);

		//tvCertifyPhoneNumberText.
		rp = (RelativeLayout.LayoutParams) (mThisView.findViewById(R.id.editDealerInfoPage_tvCertifyPhoneNumberText)).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);

		//btnEditPhoneNumber.
		rp = (RelativeLayout.LayoutParams) btnEditPhoneNumber.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(160);
		rp.height = ResizeUtils.getSpecificLength(30);
		rp.topMargin = ResizeUtils.getSpecificLength(20);
		rp.rightMargin = ResizeUtils.getSpecificLength(26);
		
		//tvPhoneNumber.
		rp = (RelativeLayout.LayoutParams) tvPhoneNumber.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(95);
		
		//checkIcon.
		rp = (RelativeLayout.LayoutParams) checkIcon.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(24);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.topMargin = ResizeUtils.getSpecificLength(38);
		rp.rightMargin = ResizeUtils.getSpecificLength(10);
		
		//tvCommonInfoText.
		rp = (RelativeLayout.LayoutParams) tvCommonInfoText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//etInfos.
		int size = etInfos.length;
		for(int i=0; i<size; i++) {
			rp = (RelativeLayout.LayoutParams) etInfos[i].getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(586);
			rp.height = ResizeUtils.getSpecificLength(60);
			
			if(i != 0) {
				rp.topMargin = ResizeUtils.getSpecificLength(32);
			}
			
			FontUtils.setFontAndHintSize(etInfos[i], 26, 20);
		}
		
		//tvIntroduceText.
		rp = (RelativeLayout.LayoutParams) tvIntroduceText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(70);
		rp.leftMargin = ResizeUtils.getSpecificLength(26);
		
		//etIntroduce.
		rp = (RelativeLayout.LayoutParams) etIntroduce.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(160);
		
		//btnComplete.
		rp = (RelativeLayout.LayoutParams) btnComplete.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(586);
		rp.height = ResizeUtils.getSpecificLength(82);
		rp.topMargin = ResizeUtils.getSpecificLength(36);
		
		FontUtils.setFontSize(tvUploadText, 20);
		
		FontUtils.setFontSize(tvIntroduceText, 30);
		FontUtils.setFontAndHintSize(etIntroduce, 26, 20);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_edit_dealer_info;
	}

	@Override
	public int getBackButtonResId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBackButtonWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
	}

	@Override
	public int getRootViewResId() {

		return R.id.editDealerInfoPage_mainLayout;
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

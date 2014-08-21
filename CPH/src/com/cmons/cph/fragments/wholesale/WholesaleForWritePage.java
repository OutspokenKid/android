package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.models.Product;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class WholesaleForWritePage extends CmonsFragmentForWholesale {

    private TextView tvStatus;
    private TextView tvSoldOut;
    private View checkbox;
    private TextView tvImageText;
    private ImageView ivImage1;
    private ImageView ivImage2;
    private ImageView ivImage3;
    private TextView tvImageSizeText;
    private TextView tvName;
    private EditText etName;
    private TextView tvPrice;
    private EditText etPrice;
    private TextView tvCategory;
    private Button btnCategory;
    private TextView tvColor;
    private Button btnColor;
    private TextView tvSize;
    private Button btnSize;
    private TextView tvMixtureRate;
    private EditText etMixtureRate;
    private TextView tvDescription;
    private EditText etDescription;
    private TextView tvPublic;
    private Button btnPublic1;
    private Button btnPublic2;
    private TextView tvNotification;
    private Button btnNotification;
    private Button btnPullUp;
    private TextView tvNotificationText;
    private Button btnDelete;

    private Product product;
    private boolean isSoldOut;
    private boolean isPublic;
	private boolean needPush;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleWritePage_titleBar);
		
		tvStatus = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvStatus);
		tvSoldOut = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvSoldOut);
		checkbox = mThisView.findViewById(R.id.wholesaleWritePage_checkbox);
		tvImageText = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvImageText);
		ivImage1 = (ImageView) mThisView.findViewById(R.id.wholesaleWritePage_ivImage1);
		ivImage2 = (ImageView) mThisView.findViewById(R.id.wholesaleWritePage_ivImage2);
		ivImage3 = (ImageView) mThisView.findViewById(R.id.wholesaleWritePage_ivImage3);
		tvImageSizeText = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvImageSizeText);
		tvName = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvName);
		etName = (EditText) mThisView.findViewById(R.id.wholesaleWritePage_etName);
		tvPrice = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvPrice);
		etPrice = (EditText) mThisView.findViewById(R.id.wholesaleWritePage_etPrice);
		tvCategory = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvCategory);
		btnCategory = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnCategory);
		tvColor = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvColor);
		btnColor = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnColor);
		tvSize = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvSize);
		btnSize = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnSize);
		tvMixtureRate = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvMixtureRate);
		etMixtureRate = (EditText) mThisView.findViewById(R.id.wholesaleWritePage_etMixtureRate);
		tvDescription = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvDescription);
		etDescription = (EditText) mThisView.findViewById(R.id.wholesaleWritePage_etDescription);
		tvPublic = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvPublic);
		btnPublic1 = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnPublic1);
		btnPublic2 = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnPublic2);
		tvNotification = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvNotification);
		btnNotification = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnNotification);
		btnPullUp = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnPullUp);
		tvNotificationText = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvNotificationText);
		btnDelete = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnDelete);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			product = (Product) getArguments().getSerializable("product");
		}
		
		if(product != null) {
			title = "공지사항 수정";
			
			//isSoldOut 설정.
			//isPublic 설정.
			//needPush 설정.
		} else {
			title = "공지사항";
			
			isPublic = true;
			needPush = true;
		}
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
		
		//수정.
		if(product != null) {
			tvStatus.setVisibility(View.VISIBLE);
			tvSoldOut.setVisibility(View.VISIBLE);
			checkbox.setVisibility(View.VISIBLE);
			
			if(isSoldOut) {
				checkbox.setBackgroundResource(R.drawable.myshop_checkbox_b);
				tvStatus.setText(R.string.productSoldOut);
			} else{
				checkbox.setBackgroundResource(R.drawable.myshop_checkbox_b);
				tvStatus.setText(R.string.productSelling);
			}
			
			btnPullUp.setVisibility(View.VISIBLE);
			btnNotification.setVisibility(View.GONE);
			btnDelete.setVisibility(View.VISIBLE);
			
			tvNotificationText.setText(R.string.productPullUpDesc);
			
		//등록.
		} else{
			tvStatus.setVisibility(View.GONE);
			tvSoldOut.setVisibility(View.GONE);
			checkbox.setVisibility(View.GONE);
			
			btnPullUp.setVisibility(View.GONE);
			btnNotification.setVisibility(View.VISIBLE);
			btnDelete.setVisibility(View.GONE);
			
			tvNotificationText.setText(R.string.productNotificationDesc);
		}
	}

	@Override
	public void setListeners() {

		btnPublic1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				isPublic = true;
				
				btnPublic1.setBackgroundResource(R.drawable.myshop_open1_a);
				btnPublic2.setBackgroundResource(R.drawable.myshop_open2_b);
			}
		});
		
		btnPublic2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				isPublic = false;
				
				btnPublic1.setBackgroundResource(R.drawable.myshop_open1_b);
				btnPublic2.setBackgroundResource(R.drawable.myshop_open2_a);
			}
		});
		
		if(product != null) {
			btnPullUp.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					pullUp();
				}
			});
			
			btnDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					delete();
				}
			});
		} else {
			btnNotification.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					needPush = !needPush;
					
					if(needPush) {
						btnNotification.setBackgroundResource(R.drawable.myshop_notification_a);
					} else {
						btnNotification.setBackgroundResource(R.drawable.myshop_notification_b);
					}
				}
			});
		}
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		int padding = ResizeUtils.getSpecificLength(16);
		int imageViewWidth = ResizeUtils.getSpecificLength(240);
		int imageViewHeight = ResizeUtils.getSpecificLength(375);
		int titleTextHeight = ResizeUtils.getSpecificLength(100);
		int editTextHeight = ResizeUtils.getSpecificLength(92);
		int buttonHeight = ResizeUtils.getSpecificLength(180);

		if(product != null) {
			//tvStatus.
			rp = (RelativeLayout.LayoutParams) tvStatus.getLayoutParams();
			rp.height = titleTextHeight;
			FontUtils.setFontSize(tvStatus, 30);
			tvStatus.setPadding(padding, 0, padding, 0);
			
			//checkbox.
			rp = (RelativeLayout.LayoutParams) checkbox.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(44);
			rp.height = ResizeUtils.getSpecificLength(43);
			rp.topMargin = ResizeUtils.getSpecificLength(45);
			rp.rightMargin = ResizeUtils.getSpecificLength(20);
			
			//tvSoldOut.
			rp = (RelativeLayout.LayoutParams) tvSoldOut.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(43);
		}
		
		//tvImageText.
		rp = (RelativeLayout.LayoutParams) tvImageText.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvImageText, 30);
		tvImageText.setPadding(padding, 0, 0, padding);
		
		//ivImage1.
		rp = (RelativeLayout.LayoutParams) ivImage1.getLayoutParams();
		rp.width = imageViewWidth;
		rp.height = imageViewHeight;
		
		//ivImage2.
		rp = (RelativeLayout.LayoutParams) ivImage2.getLayoutParams();
		rp.width = imageViewWidth;
		rp.height = imageViewHeight;
		
		//ivImage3.
		rp = (RelativeLayout.LayoutParams) ivImage3.getLayoutParams();
		rp.height = imageViewHeight;
		
		//tvImageSizeText.
		rp = (RelativeLayout.LayoutParams) tvImageSizeText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(66);
		FontUtils.setFontSize(tvImageSizeText, 20);
		tvImageText.setPadding(padding, 0, 0, padding);
		
		//tvName.
		rp = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvName, 30);
		tvName.setPadding(padding, 0, 0, padding);
		
		//etName.
		rp = (RelativeLayout.LayoutParams) etName.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontAndHintSize(etName, 30, 24);
		etName.setPadding(padding, 0, padding, 0);
		
		//tvPrice.
		rp = (RelativeLayout.LayoutParams) tvPrice.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvPrice, 30);
		tvPrice.setPadding(padding, 0, 0, padding);
		
		//etPrice.
		rp = (RelativeLayout.LayoutParams) etPrice.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontAndHintSize(etPrice, 30, 24);
		etPrice.setPadding(padding, 0, padding, 0);
		
		//tvCategory.
		rp = (RelativeLayout.LayoutParams) tvCategory.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvCategory, 30);
		tvCategory.setPadding(padding, 0, 0, padding);
		
		//btnCategory.
		rp = (RelativeLayout.LayoutParams) btnCategory.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontSize(btnCategory, 24);
		
		//tvColor.
		rp = (RelativeLayout.LayoutParams) tvColor.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvColor, 30);
		tvColor.setPadding(padding, 0, 0, padding);
		
		//btnColor.
		rp = (RelativeLayout.LayoutParams) btnColor.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontSize(btnColor, 24);
		
		//tvSize.
		rp = (RelativeLayout.LayoutParams) tvSize.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvSize, 30);
		tvSize.setPadding(padding, 0, 0, padding);
		
		//btnSize.
		rp = (RelativeLayout.LayoutParams) btnSize.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontSize(btnSize, 24);
		
		//tvMixtureRate.
		rp = (RelativeLayout.LayoutParams) tvMixtureRate.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvMixtureRate, 30);
		tvMixtureRate.setPadding(padding, 0, 0, padding);
		
		//etMixtureRate.
		rp = (RelativeLayout.LayoutParams) etMixtureRate.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontAndHintSize(etMixtureRate, 30, 24);
		etMixtureRate.setPadding(padding, 0, padding, 0);
		
		//tvDescription.
		rp = (RelativeLayout.LayoutParams) tvDescription.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvDescription, 30);
		tvDescription.setPadding(padding, 0, 0, padding);
		
		//etDescription.
		rp = (RelativeLayout.LayoutParams) etDescription.getLayoutParams();
		rp.height = editTextHeight * 4;
		FontUtils.setFontAndHintSize(etDescription, 30, 24);
		etDescription.setPadding(padding, padding, padding, padding);
		
		//tvPublic.
		rp = (RelativeLayout.LayoutParams) tvPublic.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvPublic, 30);
		tvPublic.setPadding(padding, 0, 0, padding);
		
		//btnPublic1.
		rp = (RelativeLayout.LayoutParams) btnPublic1.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/2;
		rp.height = editTextHeight;
		
		//btnPublic2.
		rp = (RelativeLayout.LayoutParams) btnPublic2.getLayoutParams();
		rp.height = editTextHeight;
		
		//tvNotification.
		rp = (RelativeLayout.LayoutParams) tvNotification.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvNotification, 30);
		tvNotification.setPadding(padding, 0, 0, padding);
		
		if(product != null) {
			//btnPullUp.
			rp = (RelativeLayout.LayoutParams) btnPullUp.getLayoutParams();
			rp.height = buttonHeight;
			
			//btnDelete.
			rp = (RelativeLayout.LayoutParams) btnDelete.getLayoutParams();
			rp.height = buttonHeight;
		} else {
			//btnNotification.
			rp = (RelativeLayout.LayoutParams) btnNotification.getLayoutParams();
			rp.height = buttonHeight;
		}
		
		//tvNotificationText.
		rp = (RelativeLayout.LayoutParams) tvNotificationText.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvNotificationText, 20);
		tvNotificationText.setPadding(padding, padding, 0, 0);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_write;
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
	
//////////////////// Custom methods.
	
	public void pullUp() {
		
	}
	
	public void delete() {
		
	}
}

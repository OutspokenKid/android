package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.utils.ImageUploadUtils.OnAfterUploadImage;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class WholesaleForProfileImagePage extends CmonsFragmentForWholesale {

	private ImageView ivImage;
	private Button btnUpload;
	private TextView tvProfileDesc;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleProfileImagePage_titleBar);
		
		ivImage = (ImageView) mThisView.findViewById(R.id.wholesaleProfileImagePage_ivImage);
		btnUpload = (Button) mThisView.findViewById(R.id.wholesaleProfileImagePage_btnUpload);
		tvProfileDesc = (TextView) mThisView.findViewById(R.id.wholesaleProfileImagePage_tvImageText);
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

		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showUploadPhotoPopup(new OnAfterUploadImage() {
					
					@Override
					public void onAfterUploadImage(String resultString, Bitmap thumbnail) {
						
						if(thumbnail != null && !thumbnail.isRecycled() && ivImage != null) {
							ivImage.setImageBitmap(thumbnail);
						}
					}
				});
			}
		});
	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;
		
		//ivImage.
		rp = (RelativeLayout.LayoutParams) ivImage.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(440);
		
		//btnUpload.
		rp = (RelativeLayout.LayoutParams) btnUpload.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(180);
		
		//tvProfileDesc
		rp = (RelativeLayout.LayoutParams) tvProfileDesc.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(74);
		FontUtils.setFontSize(tvProfileDesc, 20);
		tvProfileDesc.setPadding(ResizeUtils.getSpecificLength(10), 0, 0, 0);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_profileimage;
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
}

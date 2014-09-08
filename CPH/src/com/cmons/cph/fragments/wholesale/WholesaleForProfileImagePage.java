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
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.utils.ImageUploadUtils.OnAfterUploadImage;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class WholesaleForProfileImagePage extends CmonsFragmentForWholesale {

	private ImageView ivImage;
	private Button btnUpload;
	private TextView tvProfileDesc;
	
	private String selectedImageUrl;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(ivImage != null) {
			downloadProfile();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.profileImagePage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.profileImagePage_ivBg);
		
		ivImage = (ImageView) mThisView.findViewById(R.id.profileImagePage_ivImage);
		btnUpload = (Button) mThisView.findViewById(R.id.profileImagePage_btnUpload);
		tvProfileDesc = (TextView) mThisView.findViewById(R.id.profileImagePage_tvImageText);
	}

	@Override
	public void setVariables() {

		title = "매장 프로필 사진";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
	}

	@Override
	public void setListeners() {

		titleBar.getBtnSubmit().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(selectedImageUrl != null) {
					uploadImage();
				} else {
					mActivity.closePageWithRefreshPreviousPage();
				}
			}
		});
		
		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showUploadPhotoPopup(new OnAfterUploadImage() {
					
					@Override
					public void onAfterUploadImage(String resultString, Bitmap thumbnail) {
						
						LogUtils.log("###WholesaleForProfileImagepage.onAfterUploadImage.  " +
								"\nresultString : " + resultString);

						/*
						{
							"result":1,
							"message":"OK",
							"file":{
								"type":"image",
								"path":"\/var\/www\/cph.minsangk.com\/images\/20140908\/3199396c502128445ebab645447aba91.jpeg",
								"url":"http:\/\/cph.minsangk.com\/images\/20140908\/3199396c502128445ebab645447aba91.jpeg",
								"original_name":"temp.jpeg",
								"image_width":720,
								"image_height":960
							}
						}
						*/
						
						try {
							JSONObject objJSON = new JSONObject(resultString);

							if(objJSON.getInt("result") == 1) {
								JSONObject objFile = objJSON.getJSONObject("file");
								selectedImageUrl = objFile.getString("url");
								getWholesale().setRep_image_url(selectedImageUrl);
								downloadProfile();
							}
							
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (Error e) {
							LogUtils.trace(e);
						}
						
						if(thumbnail == null) {
							LogUtils.log("###WholesaleForProfileImagepage.onAfterUploadImage.  bitmap is null.");
						}
						
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
		
		return false;
	}
	
//////////////////// Custom methods.
	
	public void downloadProfile() {

		ivImage.setTag(getWholesale().getRep_image_url());
		DownloadUtils.downloadBitmap(getWholesale().getRep_image_url(), new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForProfileImagePage.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("WholesaleForProfileImagePage.onCompleted." + "\nurl : " + url);

					if(ivImage != null) {
						ivImage.setImageBitmap(bitmap);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void uploadImage() {

		String url = CphConstants.BASE_API_URL + "wholesales/update/rep_image_url" +
				"?rep_image_url=" + selectedImageUrl;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForProfileImagePage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToChangeProfileImage);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForProfileImagePage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_changeShopProfile);
						mActivity.closeTopPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToChangeProfileImage);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToChangeProfileImage);
				}
			}
		});
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.shop_bg;
	}
}

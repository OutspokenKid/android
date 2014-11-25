package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Wholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.activities.BaseFragmentActivity;
import com.outspoken_kid.activities.MultiSelectGalleryActivity.OnAfterPickImageListener;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ImageUploadUtils;
import com.outspoken_kid.utils.ImageUploadUtils.OnAfterUploadImage;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class WholesaleForProfileImagePage extends CmonsFragmentForWholesale {

//	private static Bitmap selectedBitmap;
//	private static String selectedImageUrl;
	
	private ImageView ivImage;
	private ProgressBar progress;
	private Button btnUpload;
	private TextView tvProfileDesc;
	
	private String selectedSdCardPath;
	private Bitmap selectedBitmap;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		BaseFragmentActivity.onAfterPickImageListener = new OnAfterPickImageListener() {

			@Override
			public void onAfterPickImage(String[] sdCardPaths,
					Bitmap[] thumbnails) {

				if(thumbnails != null &&thumbnails.length > 0) {
					LogUtils.log("###WholesaleForProfileImagePage.onAfterPickImage.  "
							+ "\nsdCardPath : " + sdCardPaths[0]);
					selectedSdCardPath = sdCardPaths[0];
					selectedBitmap = thumbnails[0];
					ivImage.setImageBitmap(thumbnails[0]);
				}
			}
		};
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.profileImagePage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.profileImagePage_ivBg);
		
		ivImage = (ImageView) mThisView.findViewById(R.id.profileImagePage_ivImage);
		progress = (ProgressBar) mThisView.findViewById(R.id.profileImagePage_progress);
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
		
		downloadProfile();
	}

	@Override
	public void setListeners() {

		titleBar.getBtnSubmit().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				//선택된 이미지가 있으면 업로드, 아니면 종료.
				if(selectedSdCardPath != null) {
					
					ToastUtils.showToast(R.string.uploadingImage);
					
					OnAfterUploadImage oaui = new OnAfterUploadImage() {
						
						@Override
						public void onAfterUploadImage(String resultString) {
							
							/*
							{
								"result":1,
								"message":"OK",
								"file":{
									"type":"image",
									"path":"\/var\/www\/cph.minsangk.com\/images\/20140908\/3199396c502128445ebab645447aba91.jpeg",
						이거 쓰면 됨.	"url":"http:\/\/cph.minsangk.com\/images\/20140908\/3199396c502128445ebab645447aba91.jpeg",
									"original_name":"temp.jpeg",
									"image_width":720,
									"image_height":960
								}
							}
							*/
							
							try {
								uploadImage(new JSONObject(resultString).getJSONObject("file").getString("url"));
							} catch (Exception e) {
								LogUtils.trace(e);
								ToastUtils.showToast(R.string.failToChangeProfileImage);
							} catch (Error e) {
								LogUtils.trace(e);
								ToastUtils.showToast(R.string.failToChangeProfileImage);
							}
						}
					};
					ImageUploadUtils.uploadImage(CphConstants.UPLOAD_URL, oaui, selectedSdCardPath);
				} else {
					mActivity.closeTopPage();
				}
			}
		});
		
		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				mActivity.showUploadPhotoPopup(1, Color.rgb(96, 183, 202));
			}
		});
	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;
		
		//ivImage.
		rp = (RelativeLayout.LayoutParams) ivImage.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(440);
		
		//progress.
		rp = (RelativeLayout.LayoutParams) progress.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(190);
		
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
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		
		return false;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		clear();
	}
	
	@Override
	public int getBgResourceId() {

		return R.drawable.shop_bg;
	}
	
//////////////////// Custom methods.
	
	public void downloadProfile() {
		
		if(selectedSdCardPath != null 
				&& selectedBitmap != null 
				&& !selectedBitmap.isRecycled()) {
			ivImage.setImageBitmap(selectedBitmap);
			return;
		}
		
		progress.setVisibility(View.VISIBLE);
		DownloadUtils.downloadBitmap(Wholesale.profileImage, new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForProfileImagePage.onError." + "\nurl : " + url
						+ "\npage : BaseFragment." + fragmentTag);
				progress.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("WholesaleForProfileImagePage.downloadProfile.onCompleted." + 
							"\nurl : " + url +
							"\nselectedImageUrl : " + Wholesale.profileImage +
							"\npage : BaseFragment." + fragmentTag);
					
					progress.setVisibility(View.INVISIBLE);
					
					if(url.equals(Wholesale.profileImage)) {
						selectedBitmap = bitmap;
						ivImage.setImageBitmap(selectedBitmap);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void uploadImage(final String imageUrl) {

		ivImage.setImageDrawable(null);
		String url = CphConstants.BASE_API_URL + "wholesales/update/rep_image_url" +
				"?rep_image_url=" + imageUrl;
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
						Wholesale.profileImage = imageUrl;
						mActivity.closePageWithRefreshPreviousPage();
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

	public void clear() {
		
		selectedSdCardPath = null;
		
		if(selectedBitmap != null) {
			
			if(!selectedBitmap.isRecycled()) {
				selectedBitmap.recycle();
			}
			
			selectedBitmap = null;
		}
				
	}
}

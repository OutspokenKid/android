package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentActivity;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Wholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ImageUploadUtils.OnAfterUploadImage;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

/**
 * 페이지가 날아가는 버그를 수정해야 함.
 * 
 * url, downloadedBitmap을 static으로 관리하면 여러 페이지가 중복해서 나와도 상관 없음.
 * 단, onResume, onPause에서 Bitmap을 해제해줘야 함.
 * 
 * @author HyungGunKim
 *
 */
public class WholesaleForProfileImagePage extends CmonsFragmentForWholesale {

	private static Bitmap selectedBitmap;
	private static String selectedImageUrl;
	private static OnAfterUploadImage onAfterUploadImage;
	
	private ImageView ivImage;
	private ProgressBar progress;
	private Button btnUpload;
	private TextView tvProfileDesc;
	
	private boolean uploading;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		onAfterUploadImage = new OnAfterUploadImage() {
			
			@Override
			public void onAfterUploadImage(String resultString, Bitmap thumbnail) {
				
				LogUtils.log("###WholesaleForProfileImagepage.onAfterUploadImage.  " +
						"\nresultString : " + resultString);

				if(!uploading) {
					return;
				}
				
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
			}
		};
		CmonsFragmentActivity.onAfterUploadImage = onAfterUploadImage;
	}
	
	@Override
	public void onResume() {
		super.onResume();

		titleBar.getBackButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				getActivity().getSupportFragmentManager().popBackStack();
				clear();
			}
		});
		
		downloadProfile();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		Drawable d = ivImage.getDrawable();
        ivImage.setImageDrawable(null);
        ivImage.setImageBitmap(null);

        if (d != null) {
            d.setCallback(null);
        }
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
	}

	@Override
	public void setListeners() {

		titleBar.getBtnSubmit().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(selectedImageUrl != null) {
					uploadImage(selectedImageUrl);
				} else {
					clear();
					mActivity.closeTopPage();
				}
			}
		});
		
		btnUpload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(uploading) {
					ToastUtils.showToast("이미지 업로드중입니다\n잠시만 기다려주세요");
					return;
				}
				
				uploading = true;
				
				mActivity.showUploadPhotoPopup(onAfterUploadImage);
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
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {

		clear();
		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		
		return false;
	}
	
//////////////////// Custom methods.
	
	public void downloadProfile() {
		
		if(selectedImageUrl == null) {
			selectedImageUrl = Wholesale.profileImage;
		}
		
		progress.setVisibility(View.VISIBLE);
		DownloadUtils.downloadBitmap(selectedImageUrl, new OnBitmapDownloadListener() {

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
							"\nselectedImageUrl : " + selectedImageUrl +
							"\npage : BaseFragment." + fragmentTag);
					progress.setVisibility(View.INVISIBLE);
					if(url.equals(selectedImageUrl)) {
						selectedBitmap = bitmap;
						ivImage.setImageBitmap(selectedBitmap);
					} else {
						downloadProfile();
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

	@Override
	public int getBgResourceId() {

		return R.drawable.shop_bg;
	}
	
	public void clear() {
		
		selectedBitmap = null;
		selectedImageUrl = null;
		uploading = false;
	}
}

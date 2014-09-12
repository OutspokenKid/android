package com.cmons.cph.classes;

import java.io.File;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;

import com.cmons.cph.R;
import com.outspoken_kid.activities.BaseFragmentActivity;
import com.outspoken_kid.utils.ImageUploadUtils;
import com.outspoken_kid.utils.ImageUploadUtils.OnAfterUploadImage;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public abstract class CmonsFragmentActivity extends BaseFragmentActivity {

	//For uploadImage.
	private static String filePath;
	private static String fileName;
	private OnAfterUploadImage onAfterUploadImage;
	
	public abstract void onRefreshPage();
	public abstract void setTitleText(String title);
	public abstract void showLoadingView();
	public abstract void hideLoadingView();
	
	@Override
	public void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		//Init application.
		CphApplication.initWithActivity(this);
	}
	
	@Override
	public void setCustomAnimations(FragmentTransaction ft) {

		if(getFragmentsSize() == 0) {
			//MainPage.
		} else if(getFragmentsSize() == 1) {
			ft.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out, 
					R.anim.abc_fade_in, R.anim.abc_fade_out);
		} else {
			ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left,
					R.anim.slide_in_from_left, R.anim.slide_out_to_right);
		}
	}
	
	@Override
	public int getCustomFontResId() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
	public void showSelectDialog(String title, String[] strings, 
			DialogInterface.OnClickListener onClickListener) {
		
		android.app.AlertDialog.Builder builder = null;
		
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			builder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
		} else {
			builder = new AlertDialog.Builder(this);
		}
		
		builder.setTitle(title); 
		builder.setItems(strings, onClickListener); 
		AlertDialog alert = builder.create(); 
		alert.show(); 
	}
	
	public void showUploadPhotoPopup(OnAfterUploadImage onAfterUploadImage) {
		
		this.onAfterUploadImage = onAfterUploadImage;
		
		showSelectDialog("프로필 사진 업로드", 
				new String[]{"앨범", "카메라"}, 
				new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				Intent intent = null;
				int requestCode = 0;
				
				//앨범.
				if(which == 0) {
					intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					requestCode = CphConstants.REQUEST_ALBUM;
					
				//카메라.
				} else if(which == 1) {
					intent = new Intent();
					intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

				    File fileDerectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				    String fileName = System.currentTimeMillis() + ".jpg";
				    File file = new File(fileDerectory, fileName);
				    
				    CmonsFragmentActivity.fileName = file.getName();
				    CmonsFragmentActivity.filePath = fileDerectory.getPath();

				    if(!fileDerectory.exists()) {
				    	fileDerectory.mkdirs();
				    }
				    
				    Uri uri = Uri.fromFile(file);
				    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
				    requestCode = CphConstants.REQUEST_CAMERA;
				}
				
				if(intent != null) {
					startActivityForResult(intent, requestCode);
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		LogUtils.log("###############################.onActivityResult.  " +
				"\nresultCode : " + resultCode +
				"\nrequestCode : " + requestCode +
				"\ndata : " + data);
		
		switch(requestCode) {
		
		case CphConstants.REQUEST_CAMERA:
		case CphConstants.REQUEST_ALBUM:
			
			if(resultCode == RESULT_OK) {
				
				try {
					File file = null;

					if (requestCode == CphConstants.REQUEST_ALBUM) {
						file = new File(ImageUploadUtils.getRealPathFromUri(
								this, data.getData()));
					} else {
						file = new File(filePath, fileName);
					}

					int standardLength = 640;
					
					if(ResizeUtils.getScreenWidth() == 1080) {
						standardLength = 720;
					}
					
					int inSampleSize = ImageUploadUtils.getBitmapInSampleSize(file, standardLength);
					
					LogUtils.log("################## standardLength : " + standardLength + ", inSampleSize : " + inSampleSize);
					
					showLoadingView();
					String uploadUrl = CphConstants.BASE_API_URL + "files/upload/image";
					ImageUploadUtils.uploadImage(uploadUrl, onAfterUploadImage,
							file.getPath(), inSampleSize);
				} catch (OutOfMemoryError oom) {
					oom.printStackTrace();
					ToastUtils.showToast(R.string.failToLoadBitmap_OutOfMemory);
				} catch (Error e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToLoadBitmap);
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToLoadBitmap);
				}
			} else {
				ToastUtils.showToast(R.string.canceled);
				hideLoadingView();

				if (onAfterUploadImage != null) {
					onAfterUploadImage.onAfterUploadImage(null, null);
				}
			}

			break;
		}
	}
	
//////////////////////////// Interfaces.
	
	public interface OnPositiveClickedListener {
		
		public void onPositiveClicked();
	}
}
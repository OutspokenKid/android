package com.zonecomms.clubvera;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.utils.BitmapUtils;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleSpinnerPopup;
import com.outspoken_kid.views.holo.holo_light.HoloStyleSpinnerPopup.OnItemClickedListener;
import com.zonecomms.clubvera.classes.ZoneConstants;
import com.zonecomms.clubvera.classes.ZonecommsActivity;
import com.zonecomms.clubvera.classes.ZonecommsApplication;
import com.zonecomms.common.models.UploadImageInfo;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.utils.ImageUploadUtils;
import com.zonecomms.common.utils.ImageUploadUtils.OnAfterUploadImage;

public class WriteActivity extends ZonecommsActivity {

	private TextView tvTitle;
	private View btnComplete;
	private EditText editText;
	private FrameLayout photoFrame;
	private Button btnPhoto;
	private HoloStyleSpinnerPopup spinner;
	private View loadingView;
	private View cover;
	
//	private int board_nid;
	private int downloadImageCount;
	private int spot_nid;
	private boolean isEdit;
	private String member_id;
	
	private File filePath;
	private String fileName;
	private ArrayList<FrameLayout> frames = new ArrayList<FrameLayout>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setPage(true);
	}
	
	@Override
	public void bindViews() {

		tvTitle = (TextView) findViewById(R.id.writeActivity_tvTitle);
		btnComplete = findViewById(R.id.writeActivity_btnComplete);
		editText = (EditText) findViewById(R.id.writeActivity_editText);
		photoFrame = (FrameLayout) findViewById(R.id.writeActivity_photoFrame);
		btnPhoto = (Button) findViewById(R.id.writeActivity_btnPhoto);
		spinner = (HoloStyleSpinnerPopup) findViewById(R.id.writeActivity_spinner);
		loadingView = findViewById(R.id.writeActivity_loadingView);
		cover = findViewById(R.id.writeActivity_cover);
	}

	@Override
	public void setVariables() {
		
		if(getIntent() != null) {
//			board_nid = getIntent().getIntExtra("board_nid", 0);
			spot_nid = getIntent().getIntExtra("spot_nid", 0);
			member_id = getIntent().getStringExtra("member_id");
			
//			final int[] titleResIds = new int[] {
//					0,
//					R.string.board_story,
//					R.string.board_review,
//					R.string.board_with,
//					R.string.board_findPeople,
//			};
			
//			if(board_nid != 0) {
//				tvTitle.setText(getString(titleResIds[board_nid]) + " " + getString(R.string.write));
//			}
		}
		
		tvTitle.setText(R.string.write);
	}

	@Override
	public void createPage() {

		spinner.setTitle(getString(R.string.uploadPhoto));
		spinner.addItem(getString(R.string.photo_take));
		spinner.addItem(getString(R.string.photo_album));
		spinner.notifyDataSetChanged();
	}

	@Override
	public void setSizes() {
		
		//ScrollView
		int p = ResizeUtils.getSpecificLength(8);
		findViewById(R.id.writeActivity_scrollView).setPadding(p, p, p, p);
		
		//titleBar.
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 90, findViewById(R.id.writeActivity_titleBar), 1, 0, null);
		ResizeUtils.viewResize(60, 82, btnComplete, 2, Gravity.RIGHT|Gravity.CENTER_VERTICAL, new int[]{0, 0, 10, 0});
		FontUtils.setFontSize((TextView)findViewById(R.id.writeActivity_tvTitle), 40);
		
		//EditText.
		FontUtils.setFontSize(editText, 36);
		
		ResizeUtils.viewResize(66, 53, btnPhoto, 1, Gravity.CENTER_VERTICAL, new int[]{10, 10, 10, 10});
		
		ResizeUtils.viewResize(120, 150, loadingView, 2, Gravity.CENTER, null);
	}

	@Override
	public void setListeners() {
		
		btnComplete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(cover.getVisibility() == View.VISIBLE) {
					return;
				}
				
				if(StringUtils.isEmpty(editText.getText())) {
					ToastUtils.showToast(R.string.inputContent);
				} else {
					post(editText.getText().toString());
				}
			}
		});
		
		btnPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(photoFrame.getChildCount() < 4) {
					spinner.showPopup();
				} else{
					ToastUtils.showToast(R.string.cantUploadMorePhoto);
				}
			}
		});

		spinner.setOnItemClickedListener(new OnItemClickedListener() {
			
			@Override
			public void onItemClicked(int position, String itemString) {

				if(StringUtils.isEmpty(itemString)) {
					return;
				} else if(itemString.equals(getString(R.string.photo_take))){
					Intent intent = new Intent();
				    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

				    filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				    fileName = System.currentTimeMillis() + ".jpg";
				    File file = new File(filePath, fileName);
				    
				    if(!filePath.exists()) {
				    	filePath.mkdirs();
				    }
				    
				    Uri uri = Uri.fromFile(file);
				    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
				    startActivityForResult(intent, ZoneConstants.REQUEST_CAMERA);
				    
				} else if(itemString.equals(getString(R.string.photo_album))){
//					Intent intent = new Intent();
//					intent.setAction(Intent.ACTION_GET_CONTENT);
					Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					startActivityForResult(intent, ZoneConstants.REQUEST_GALLERY);
				}
			}
		});
	}

	@Override
	public void downloadInfo() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getContentViewId() {

		return R.layout.activity_write;
	}
	
	@Override
	public void onBackPressed() {
		
		if(spinner.getVisibility() == View.VISIBLE) {
			spinner.hidePopup();
		} else {
			super.onBackPressed();
		}
	}

	public void showLoadingView() {
		
		loadingView.setVisibility(View.VISIBLE);
	}

	public void hideLoadingView() {

		loadingView.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(resultCode == RESULT_OK) {
			
			try {
				File file = null;
				
				switch(requestCode) {
				
				case ZoneConstants.REQUEST_GALLERY:
					file = new File(ImageUploadUtils.getRealPathFromUri(this, data.getData()));
					break;
					
				case ZoneConstants.REQUEST_CAMERA:	
					file = new File(filePath, fileName);
					filePath = null;
					fileName = null;
					break;
				}
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 16;
				BitmapFactory.decodeFile(file.getPath(), options);
				
				int width = 0;
				
				if(BitmapUtils.GetExifOrientation(file.getPath()) % 180 == 0) {
					width = options.outWidth;
				} else {
					width = options.outHeight;
				}
				
				if(width > 600) {
                	//Use last inSampleSize.
                } else if(width > 300) {
                	options.inSampleSize = 8;
                } else if(width > 150) {
                	options.inSampleSize = 4;
                } else {
                	options.inSampleSize = 2;
                }
				
				cover.setVisibility(View.VISIBLE);
				showLoadingView();
				OnAfterUploadImage oaui = new OnAfterUploadImage() {
					
					@Override
					public void onAfterUploadImage(UploadImageInfo uploadImageInfo, Bitmap thumbnail) {
						cover.setVisibility(View.INVISIBLE);
						hideLoadingView();
						
						if(uploadImageInfo != null && thumbnail != null && !thumbnail.isRecycled()) {
							addThumbnailView(thumbnail, uploadImageInfo);
						} else if(thumbnail != null && !thumbnail.isRecycled()){
							thumbnail.recycle();
							thumbnail = null;
						}
					}
				}; 
				ImageUploadUtils.uploadImage(this, oaui, file.getPath(), options.inSampleSize);
			} catch(OutOfMemoryError oom) {
				oom.printStackTrace();
				ToastUtils.showToast(R.string.failToLoadBitmap_OutOfMemory);
			} catch(Error e) {
				LogUtils.trace(e);
				ToastUtils.showToast(R.string.failToLoadBitmap);
			} catch(Exception e) {
				LogUtils.trace(e);
				ToastUtils.showToast(R.string.failToLoadBitmap);
			}
		} else {
			ToastUtils.showToast(R.string.canceled);
		}
	}

	@Override
	public void setPage(boolean successDownload) {

		Intent intent = getIntent();
		
		if(intent != null) {
			
			isEdit = intent.getBooleanExtra("isEdit", false);
			
			if(intent.getStringExtra("content") != null) {
				editText.setText(intent.getStringExtra("content"));
				Editable et = editText.getText();
				Selection.setSelection(et, editText.length());
			}
			
			if(intent.getStringArrayExtra("imageUrls") != null) {
				final String[] imageUrls = intent.getStringArrayExtra("imageUrls");
				
				if(imageUrls != null) {
					
					showLoadingView();
					
					for(int i=0; i<imageUrls.length; i++) {
						
						DownloadUtils.downloadBitmap(imageUrls[i], new OnBitmapDownloadListener() {
							
							@Override
							public void onError(String url) {

								ToastUtils.showToast(R.string.failToLoadBitmap);
								
								downloadImageCount ++;
								
								if(downloadImageCount == imageUrls.length) {
									hideLoadingView();
								}
							}
							
							@Override
							public void onCompleted(String url, Bitmap bitmap) {

								downloadImageCount ++;
								
								if(downloadImageCount == imageUrls.length) {
									hideLoadingView();
								}
								
								try {
									UploadImageInfo uii = new UploadImageInfo();
									uii.setImageUrl(url);
									uii.setImageWidth(bitmap.getWidth());
									uii.setImageHeight(bitmap.getHeight());
									addThumbnailView(bitmap, uii);
								} catch(Exception e) {
									LogUtils.trace(e);
									ToastUtils.showToast(R.string.failToLoadBitmap);
								}
							}
						});
					}
				}
			}
		}
	}
	
	@Override
	public void onMenuPressed() {
		// TODO Auto-generated method stub
		
	}
	
//////////////Custom methods.
	
	public void addThumbnailView(Bitmap thumbnail, UploadImageInfo uploadImageInfo) {
	
		//Frame has uploadImageInfo.
		FrameLayout frame = new FrameLayout(this);
		frame.setTag(uploadImageInfo);
		frames.add(frame);
		photoFrame.addView(frame);

		ImageView imageView = new ImageView(this);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, imageView, 
				2, Gravity.LEFT, new int[]{0, 20, 20, 0});
		imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.setImageBitmap(thumbnail);
		frame.addView(imageView);
		
		//Remove buttons has its frame.
		View remove = new View(this);
		ResizeUtils.viewResize(50, 50, remove, 2, Gravity.RIGHT, null);
		remove.setBackgroundResource(R.drawable.img_delete);
		remove.setTag(frame);
		remove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				FrameLayout frame = null;
				
				try {
					frame = (FrameLayout) v.getTag();
					frames.remove(frame);
					photoFrame.removeView(frame);
					ViewUnbindHelper.unbindReferences(frame);
					setPhotoFrame();
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			}
		});
		frame.addView(remove);
		setPhotoFrame();
	}
	
	public void setPhotoFrame() {
		
		int size = frames.size();
		for(int i=0; i<size; i++) {
			ResizeUtils.viewResize(140, 140, frames.get(i), 2, Gravity.LEFT, new int[]{25 + (i*150), 10, 0, 10});
		}
	}

	public void post(String input) {

		try {
			String imageUrls = "";
			String imageWidths = "";
			String imageHeights = "";
			
			int size = frames.size();
			for(int i=0; i<size; i++) {
				
				if(frames.get(i).getTag() != null && frames.get(i).getTag() instanceof UploadImageInfo) {
					UploadImageInfo uii = (UploadImageInfo) frames.get(i).getTag();
					
					//Set url.
					if(!imageUrls.equals("")) {
						imageUrls += ":::";
					}
					imageUrls += uii.getImageUrl();
					
					//Set width.
					if(!imageWidths.equals("")) {
						imageWidths += "/";
					}
					
					imageWidths += uii.getImageWidth();
					
					//Set height.
					if(!imageHeights.equals("")) {
						imageHeights += "/";
					}
					
					imageHeights += uii.getImageHeight();
				}
			}

			String url = "";
			
			if(isEdit) {
				url += ZoneConstants.BASE_URL + "spot/update" +
						"?spot_nid=" + spot_nid;
			} else {
				url += ZoneConstants.BASE_URL + "spot/write" +
						"?concern_kind=010" +
//						"&board_nid=" + board_nid;
						"&board_nid=1";
			}
			
			url += "&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_MEMBER_ID) +
					"&spot_content=" + URLEncoder.encode(input, "UTF-8") +
					"&media_src=" + imageUrls +
					"&img_width=" + imageWidths +
					"&img_height=" + imageHeights;
			
			if(isEdit) {
				url += "&member_id=" + member_id;
			} else {
				url += "&member_id=" + ZonecommsApplication.myInfo.getMember_id();
			}

			cover.setVisibility(View.VISIBLE);
			showLoadingView();
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {
				
				@Override
				public void onError(String url) {

					LogUtils.log("WriteActivity.onError.  url : " + url);
					
					ToastUtils.showToast(R.string.failToSendPost);
					cover.setVisibility(View.INVISIBLE);
					hideLoadingView();
				}
				
				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					LogUtils.log("WriteActivity.onCompleted.  url : " + url + "\nresult : " + objJSON.toString());
					
					int spot_nid = 0;
					String message = null;
					
					try {
						
						if(objJSON.getInt("errorCode") == 1) {
							spot_nid = Integer.parseInt(objJSON.getString("data"));
							
							SoftKeyboardUtils.hideKeyboard(editText.getContext(), editText);
							
							Intent intent = new Intent();
							intent.putExtra("spot_nid", spot_nid);
							
							message = getString(R.string.postingCompleted);
							setResult(RESULT_OK, intent);
							finish();
						} else {
							message = objJSON.getString("errorMsg");
						}
					} catch(Exception e) {
						LogUtils.trace(e);
					} finally {
						ToastUtils.showToast(message);
					}
				}
			});
		} catch(Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToSendPost);
		}
	}
}
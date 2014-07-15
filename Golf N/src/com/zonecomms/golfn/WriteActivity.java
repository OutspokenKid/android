package com.zonecomms.golfn;

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

import com.outspoken_kid.activities.RecyclingActivity;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.BitmapUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup.OnItemClickedListener;
import com.zonecomms.common.models.UploadImageInfo;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.utils.ImageUploadUtils;
import com.zonecomms.common.utils.ImageUploadUtils.OnAfterUploadImage;
import com.zonecomms.golfn.classes.ZoneConstants;

public class WriteActivity extends RecyclingActivity {

	private static int madeCount = 1229;
	
	private TextView tvTitle;
	private View btnComplete;
	private EditText editText;
	private FrameLayout photoFrame;
	private Button btnPhoto;
	private HoloStyleSpinnerPopup spinner;
	private View cover;
	
	private int downloadImageCount;
	private boolean isEdit;
	private boolean isFinding;
	private boolean isGethering;
	private String titleText;
	
	private File filePath;
	private String fileName;
	private ArrayList<FrameLayout> frames = new ArrayList<FrameLayout>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write);
		
		bindViews();
		setVariables();
		createPage();
		setSizes();
		setListeners();
		downloadUploadedImage();
	}
	
	@Override
	protected void bindViews() {

		tvTitle = (TextView) findViewById(R.id.writeActivity_tvTitle);
		btnComplete = findViewById(R.id.writeActivity_btnComplete);
		editText = (EditText) findViewById(R.id.writeActivity_editText);
		photoFrame = (FrameLayout) findViewById(R.id.writeActivity_photoFrame);
		btnPhoto = (Button) findViewById(R.id.writeActivity_btnPhoto);
		spinner = (HoloStyleSpinnerPopup) findViewById(R.id.writeActivity_spinner);
		cover = findViewById(R.id.writeActivity_cover);
		
		setLoadingView(R.id.writeActivity_loadingView);
	}

	@Override
	protected void setVariables() {
		
		titleText = "";
		
		if(getIntent() != null) {
			isGethering = getIntent().getBooleanExtra("isGethering", false);
			isEdit = getIntent().getBooleanExtra("isEdit", false);
			isFinding = getIntent().getBooleanExtra("isFinding", false);

			if(getIntent().hasExtra("titleText")) {
				titleText += getIntent().getStringExtra("titleText") + " "; 
			}
		}
		
		titleText += getString(R.string.write);
		tvTitle.setText(titleText);
		
		downloadKey = "WRITEACTIVITY" + (++madeCount);
	}

	@Override
	protected void createPage() {

		spinner.setTitle(getString(R.string.uploadPhoto));
		spinner.addItem(getString(R.string.photo_take));
		spinner.addItem(getString(R.string.photo_album));
		spinner.notifyDataSetChanged();
	}

	@Override
	protected void setSizes() {
		
		//ScrollView
		int p = ResizeUtils.getSpecificLength(8);
		findViewById(R.id.writeActivity_scrollView).setPadding(p, p, p, p);
		
		//titleBar.
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 90, findViewById(R.id.writeActivity_titleBar), 1, 0, null);
		FontInfo.setFontSize((TextView)findViewById(R.id.writeActivity_tvTitle), 36);
		ResizeUtils.viewResize(60, 30, findViewById(R.id.writeActivity_submitImage), 2, 
				Gravity.LEFT|Gravity.CENTER_VERTICAL, new int[]{0, 0, 20, 0});
		
		//EditText.
		FontInfo.setFontSize(editText, 36);
		
		ResizeUtils.viewResize(66, 53, btnPhoto, 1, Gravity.CENTER_VERTICAL, new int[]{10, 10, 10, 10});
	}

	@Override
	protected void setListeners() {
		
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
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					startActivityForResult(intent, ZoneConstants.REQUEST_GALLERY);
				}
			}
		});
	}

	@Override
	protected void downloadInfo() {
	}

	@Override
	protected void setPage() {
	}

	@Override
	protected int getContentViewId() {
		
		return 0;
	}
	
	@Override
	public void onBackPressed() {
		
		if(spinner.getVisibility() == View.VISIBLE) {
			spinner.hidePopup();
		} else {
			super.onBackPressed();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
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
	
	protected void onRefreshPage() {
	}
	
//////////////Custom methods.
	
	public void downloadUploadedImage() {
		
		Intent intent = getIntent();
		
		if(intent != null) {
			
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

						final String originalUrl = imageUrls[i];
						BitmapDownloader.OnCompletedListener ocl = new BitmapDownloader.OnCompletedListener() {
							
							@Override
							public void onErrorRaised(String url, Exception e) {
								ToastUtils.showToast(R.string.failToLoadBitmap);
								
								downloadImageCount ++;
								
								if(downloadImageCount == imageUrls.length) {
									hideLoadingView();
								}
							}
							
							@Override
							public void onCompleted(String url, Bitmap bitmap, ImageView view) {

								downloadImageCount ++;
								
								if(downloadImageCount == imageUrls.length) {
									hideLoadingView();
								}
								
								try {
									UploadImageInfo uii = new UploadImageInfo();
									uii.setImageUrl(originalUrl);
									uii.setImageWidth(bitmap.getWidth());
									uii.setImageHeight(bitmap.getHeight());
									addThumbnailView(bitmap, uii);
								} catch(Exception e) {
									LogUtils.trace(e);
									ToastUtils.showToast(R.string.failToLoadBitmap);
								}
							}
						};
						BitmapDownloader.download(originalUrl, downloadKey, ocl, null, null, false);
					}
				}
			}
		}
	}
	
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

			String url = ZoneConstants.BASE_URL;

			//모임 찾기.
			if(isFinding) {
				
				if(isEdit) {
					url += "spot/update" +
							"?spot_nid=" + getIntent().getIntExtra("spot_nid", 0);
				} else {
					url += "spot/write" +
							"?concern_kind=050" +
							"&origin_sb_id=" + ZoneConstants.PAPP_ID;
				}

				url += "&spot_content=" + URLEncoder.encode(editText.getText().toString(), "utf-8");
				
			} else if(isEdit) {
				
				//모임 글 수정.
				if(isGethering) {
					url += "boardspot/update" +
							"?content=" + URLEncoder.encode(editText.getText().toString(), "utf-8");

				//글 수정.
				} else {
					url += "spot/update" +
							"?spot_content=" + URLEncoder.encode(editText.getText().toString(), "utf-8");
				}
	
				int spot_nid = getIntent().getIntExtra("spot_nid", 0);
				url += "&spot_nid=" + spot_nid;
				
			//모임 글 작성.
			} else if(isGethering) {
				String sb_id = getIntent().getStringExtra("sb_id");
				url += "boardspot/write" +
						"?content=" + URLEncoder.encode(editText.getText().toString(), "utf-8") +
						"&sb_id=" + sb_id;
				
			//일반 글 작성.
			} else {

				/**
				 * board_id
				 * 2 : QnA
				 * 3 : 포토
				 * 4 : 나도 한마디!
				 * 5 : 마켓1
				 * 6 : 마켓2
				 * 7 : 마켓3
				 * 8 : 마켓4
				 * 9 : 출석부
				 * 10 : 리뷰
				 */
				int board_id = getIntent().getIntExtra("board_id", 2);
				
				url += "spot/write" +
						"?spot_content=" + URLEncoder.encode(editText.getText().toString(), "utf-8") +
						"&concern_kind=010" +
						"&sb_id=" + ZoneConstants.PAPP_ID +
						"&board_nid=" + board_id;
			}

			if(getIntent().hasExtra("mustValues")
					&& getIntent().getStringArrayExtra("mustValues") != null) {
				String[] mustValues = getIntent().getStringArrayExtra("mustValues");
				
				if(mustValues != null 
						&& mustValues.length == WriteForFleaActivity.NUMBER_OF_TEXTS) {
					for (int i = 0; i < WriteForFleaActivity.NUMBER_OF_TEXTS; i++) {
						
						try {
							url += "&mustvalue" + (i+1) + "=" + mustValues[i];
							LogUtils.log("############# " + (i+1) + mustValues[i]);
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (Error e) {
							LogUtils.trace(e);
						}
					}
				}
			}
			
			url += "&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_SB_ID) +
					"&media_src=" + imageUrls +
					"&img_width=" + imageWidths +
					"&img_height=" + imageHeights;
			
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					
					LogUtils.log("WriteActivity.onError.  url : " + url);
					
					ToastUtils.showToast(R.string.failToSendPost);
					cover.setVisibility(View.INVISIBLE);
					hideLoadingView();
				}
				
				@Override
				public void onCompleted(String url, String result) {

					LogUtils.log("WriteActivity.onCompleted.  url : " + url + "\nresult : " + result);
					
					ToastUtils.showToast(R.string.postingCompleted);
					SoftKeyboardUtils.hideKeyboard(editText.getContext(), editText);
					
					int spot_nid = 0;
					
					try {
						spot_nid = Integer.parseInt((new JSONObject(result)).getString("data"));
					} catch(Exception e) {
						LogUtils.trace(e);	
					}
					
					Intent intent = new Intent();
					intent.putExtra("spot_nid", spot_nid);
					intent.putExtra("isGethering", isGethering);

					LogUtils.log("###WriteActivity.onCompleted.  spot_nid : " + spot_nid + 
							", isGethering : " + isGethering);
					
					setResult(RESULT_OK, intent);
					finish();
				}
			};

			cover.setVisibility(View.VISIBLE);
			showLoadingView();
			AsyncStringDownloader.download(url, null, ocl);
		} catch(Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToSendPost);
		}
	}
}
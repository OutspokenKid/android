package com.zonecomms.napp;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Selection;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.outspoken_kid.activities.RecyclingActivity;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.BitmapUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup.OnItemClickedListener;
import com.zonecomms.common.adapters.GridAdapter;
import com.zonecomms.common.models.Papp;
import com.zonecomms.common.models.UploadImageInfo;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.utils.ImageUploadUtils;
import com.zonecomms.common.utils.ImageUploadUtils.OnAfterUploadImage;
import com.zonecomms.common.wrappers.ViewWrapperForPapp;
import com.zonecomms.napp.classes.ApplicationManager;
import com.zonecomms.napp.classes.ZoneConstants;

public class WriteActivity extends RecyclingActivity {

	private static View posting;
	private static String postingTargetText;
	private static int madeCount;
	private static boolean checked;
	
	private Context context;
	private TextView tvTitle;
	private View btnComplete;
	private FrameLayout frameForPosting;
	private EditText editText;
	private FrameLayout photoFrame;
	private Button btnPhoto;
	private HoloStyleSpinnerPopup spinner;
	private View cover;
	
	private PullToRefreshGridView gridView;
	private GridAdapter gridAdapter;
	private ArrayList<BaseModel> models = new ArrayList<BaseModel>();
	private int lastIndexno;
	private boolean isLastList;
	private boolean isRefreshing;
	private boolean isDownloading;
	private boolean successDownload;

	private TextView tvPreparingPlusApp;
	
	private int s_cate_id;
	private int downloadImageCount;
	private boolean isEdit;
	private boolean isFinding;
	private boolean isGethering;
	private boolean needPosting;
	private String titleText;
	
	private File filePath;
	private String fileName;
	private ArrayList<FrameLayout> frames = new ArrayList<FrameLayout>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_write);
		context = this;
		
		bindViews();
		setVariables();
		createPage();
		setSizes();
		setListeners();
		
		if(isEdit) {
			downloadUploadedImage();
		}
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
		
		if(getIntent() != null) {
			isEdit = getIntent().getBooleanExtra("isEdit", false);
			isFinding = getIntent().getBooleanExtra("isFinding", false);
			isGethering = getIntent().getBooleanExtra("isGethering", false);
			needPosting = getIntent().getBooleanExtra("needPosting", false);
			titleText = getIntent().getStringExtra("titleText");
			s_cate_id = getIntent().getIntExtra("s_cate_id", 0);
		}

		tvTitle.setText(titleText);
		
		downloadKey = "WRITEACTIVITY" + madeCount;
	}

	@Override
	protected void createPage() {

		spinner.setTitle(getString(R.string.uploadPhoto));
		spinner.addItem(getString(R.string.photo_take));
		spinner.addItem(getString(R.string.photo_album));
		spinner.notifyDataSetChanged();
		
		addGridView();
	}

	@Override
	protected void setSizes() {
		
		//ScrollView
		int p = ResizeUtils.getSpecificLength(8);
		findViewById(R.id.writeActivity_scrollView).setPadding(p, p, p, p);
		
		//titleBar.
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 90, findViewById(R.id.writeActivity_titleBar), 1, 0, null);
		ResizeUtils.viewResize(70, 70, btnComplete, 2, Gravity.RIGHT|Gravity.CENTER_VERTICAL, new int[]{0, 0, 20, 0});
		FontInfo.setFontSize((TextView)findViewById(R.id.writeActivity_tvTitle), 40);
		
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
				} else if(frameForPosting.getVisibility() != View.VISIBLE 
						&& needPosting){
					showGridView();
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

		if(isDownloading || isLastList) {
			return;
		}
		
		String url = ZoneConstants.BASE_URL + "sb/papp_list" +
				"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
				"&image_size=150" +
				"&last_sb_nid=" + lastIndexno +
				"&s_cate_id=" + s_cate_id;

		if(!isRefreshing) {
			showLoadingView();
			cover.setVisibility(View.VISIBLE);
		}
		
		isDownloading = true;
		
		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {
				successDownload = false;
				setPage();
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				try {
					LogUtils.log("WriteActivity.onCompleted.  url : " + url + "\nresult : " + result);
					
					JSONObject objJSON = new JSONObject(result);
					JSONArray arJSON = objJSON.getJSONArray("data");
					int length = arJSON.length();
					
					if(length > 0) {
						for(int i=0; i<length; i++) {
							try {
								Papp papp = new Papp(arJSON.getJSONObject(i));
								papp.setItemCode(ZoneConstants.ITEM_PAPP);
								papp.setSelectable(true);
								papp.setCheckable(true);
								models.add(papp);
							} catch(Exception e) {
							}
						}
					} else {
						isLastList = true;
					}
					
					successDownload = true;
					setPage();
				} catch(Exception e) {
					LogUtils.trace(e);
					successDownload = false;
					setPage();
				}
			}
		};
		AsyncStringDownloader.download(url, downloadKey, ocl);
	}

	@Override
	protected void setPage() {

		
		if(isRefreshing && gridView != null) {
			gridView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					gridView.onRefreshComplete();
				}
			}, 500);
		}

		hideLoadingView();
		cover.setVisibility(View.INVISIBLE);
		isRefreshing = false;
		isDownloading = false;
		
		if(successDownload) {
			
			if(models != null && models.size() > 0) {
				lastIndexno = models.get(models.size() - 1).getIndexno();
				
				if(tvPreparingPlusApp.getVisibility() == View.VISIBLE) {
					tvPreparingPlusApp.setVisibility(View.INVISIBLE);
				}
			} else {
				tvPreparingPlusApp.setVisibility(View.VISIBLE);
			}
			
			if(gridAdapter != null) {
				gridAdapter.notifyDataSetChanged();
			}
		} else {
			ToastUtils.showToast(R.string.failToLoadList);
		}
	}

	@Override
	protected int getContentViewId() {
		
		return 0;
	}
	
	@Override
	public void onBackPressed() {
		
		if(spinner.getVisibility() == View.VISIBLE) {
			spinner.hidePopup();
		} else if(frameForPosting.getVisibility() == View.VISIBLE) {
			hideGridView();
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

		if(isRefreshing) {
			return;
		}

		isLastList = false;
		isRefreshing = true;
		lastIndexno = 0;
		models.clear();
		gridAdapter.notifyDataSetChanged();
		
		downloadInfo();
	}
	
	@Override
	public void finish() {
		
		posting = null;
		checked = false;
		ViewWrapperForPapp.clearSelected();
		super.finish();
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
	
	public void addGridView() {
		
		frameForPosting = new FrameLayout(this);
		frameForPosting.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		frameForPosting.setBackgroundColor(Color.BLACK);
		frameForPosting.setVisibility(View.INVISIBLE);
		frameForPosting.setClickable(true);
		((FrameLayout)findViewById(R.id.writeActivity_innerFrame)).addView(frameForPosting);
		
		TextView tvPosting = new TextView(this);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 80, tvPosting, 2, Gravity.TOP, new int[]{8, 8, 8, 8});
		tvPosting.setText(R.string.textForPosting);
		tvPosting.setGravity(Gravity.CENTER);
		tvPosting.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvPosting, 24);
		frameForPosting.addView(tvPosting);
		
		TextView tvPostingTarget = new TextView(this);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 50, tvPostingTarget, 2, 
				Gravity.TOP|Gravity.RIGHT, new int[]{8, 96, 74, 0});

		if(getIntent().hasExtra("postTarget")
				&& getIntent().getStringExtra("postTarget") != null) {
			tvPostingTarget.setText(getIntent().getStringExtra("postTarget"));
		} else {
			tvPostingTarget.setText(R.string.noPosting);
		}
		
		postingTargetText = tvPostingTarget.getText().toString();
		
		tvPostingTarget.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				setChecked(!checked);
			}
		});
		tvPostingTarget.setGravity(Gravity.CENTER);
		tvPostingTarget.setTextColor(Color.WHITE);
		FontInfo.setFontSize(tvPostingTarget, 26);
		frameForPosting.addView(tvPostingTarget);
		
		posting = new View(this);
		ResizeUtils.viewResize(50, 50, posting, 2, Gravity.TOP|Gravity.RIGHT, new int[]{0, 96, 8, 0});
		setChecked(true);
		posting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setChecked(!checked);
			}
		});
		frameForPosting.addView(posting);
		
		gridView = new PullToRefreshGridView(this);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, gridView, 
				2, Gravity.BOTTOM, new int[]{0, 154, 0, 0});
		gridAdapter = new GridAdapter(this, models, false);
		gridView.setAdapter(gridAdapter);
		gridView.getRefreshableView().setNumColumns(4);
		gridView.getRefreshableView().setPadding(0, ResizeUtils.getSpecificLength(8), 0, 0);
		gridView.getRefreshableView().setSelector(R.drawable.list_selector);
	    if (android.os.Build.VERSION.SDK_INT >= 9) {
	    	gridView.setOverScrollMode(GridView.OVER_SCROLL_NEVER);
	    }
		
		gridView.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				onRefreshPage();
			}
		});
		frameForPosting.addView(gridView);
		
		tvPreparingPlusApp = new TextView(this);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, tvPreparingPlusApp, 2, Gravity.CENTER, null);
		tvPreparingPlusApp.setText(R.string.preparingPlusApp);
		tvPreparingPlusApp.setTextColor(Color.WHITE);
		tvPreparingPlusApp.setVisibility(View.INVISIBLE);
		FontInfo.setFontSize(tvPreparingPlusApp, 26);
		((FrameLayout)findViewById(R.id.writeActivity_innerFrame)).addView(tvPreparingPlusApp);
	}
	
	public void showGridView() {
		
		if(frameForPosting.getVisibility() != View.VISIBLE) {
			tvTitle.setText(R.string.selectApp);
			SoftKeyboardUtils.hideKeyboard(context, editText);
			frameForPosting.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					AlphaAnimation aaIn = new AlphaAnimation(0, 1);
					aaIn.setDuration(300);
					frameForPosting.setVisibility(View.VISIBLE);
					frameForPosting.startAnimation(aaIn);
				}
			}, 500);
			frameForPosting.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					
					if(models.size() == 0) {
						downloadInfo();
					}
				}
			}, 800);
		}
	}
	
	public void hideGridView() {
		
		if(frameForPosting.getVisibility() == View.VISIBLE) {

			isDownloading = false;
			isRefreshing = false;
			isLastList = false;
			lastIndexno = 0;
			ViewWrapperForPapp.clearSelected();
			models.clear();
			tvTitle.setText(titleText);
			
			AlphaAnimation aaOut = new AlphaAnimation(1, 0);
			aaOut.setDuration(300);
			frameForPosting.setVisibility(View.INVISIBLE);
			frameForPosting.startAnimation(aaOut);
			frameForPosting.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					SoftKeyboardUtils.showKeyboard(context, editText);
				}
			}, 300);
		}
	}

	public static void unCheckIfNeeded(Context context) {
		
		if(posting != null && postingTargetText == context.getString(R.string.noPosting)) {
			setChecked(false);
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

			//친구 하기, 모임 찾기.
			if(isFinding) {
				url += "spot/write" +
						"?spot_content=" + URLEncoder.encode(editText.getText().toString(), "utf-8") +
						"&concern_kind=" + (isGethering? "050" : "040");
				
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
				
			} else {
				
				//모임 글 작성.
				if(isGethering) {
					String sb_id = getIntent().getStringExtra("sb_id");
					url += "boardspot/write" +
							"?content=" + URLEncoder.encode(editText.getText().toString(), "utf-8") +
							"&sb_id=" + sb_id;
					
				//일반 글 작성.
				} else {
					
					url += "spot/write" +
							"?spot_content=" + URLEncoder.encode(editText.getText().toString(), "utf-8");
							
					//카테고리에 글 쓰기
					if(s_cate_id != 0) {
						url += "&s_cate_id=" + s_cate_id +
								"&sb_id=";
						
					//일반 Napp에 글쓰기
					} else {
						
						url += "&concern_kind=010" +
								"&sb_id=";
					}
			
					if(ViewWrapperForPapp.getSelectedId() != 0) {
						url += ViewWrapperForPapp.getSelectedPapp().getSb_id() +
								"&board_nid=1";
					} else {
						url += "&board_nid=0";
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

	public static void setChecked(boolean checked) {
		
		WriteActivity.checked = checked;
		
		try {
			if(checked) {
				posting.setBackgroundResource(R.drawable.img_checkbox_on);
			} else {
				posting.setBackgroundResource(R.drawable.img_checkbox_off);
			}
			
			if(ApplicationManager.getInstance().getActivity().getString(R.string.noPosting)
					.equals(postingTargetText) && checked) {
				ViewWrapperForPapp.clearSelected();
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		}
	}
}
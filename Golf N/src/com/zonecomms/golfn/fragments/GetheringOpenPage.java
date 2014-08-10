package com.zonecomms.golfn.fragments;

import java.io.File;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.bitmapdownloader.BitmapDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleButton;
import com.outspoken_kid.views.holo_dark.HoloStyleEditText;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerButton;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup.OnItemClickedListener;
import com.zonecomms.common.models.UploadImageInfo;
import com.zonecomms.common.utils.ImageUploadUtils.OnAfterUploadImage;
import com.zonecomms.golfn.MainActivity;
import com.zonecomms.golfn.R;
import com.zonecomms.golfn.classes.ApplicationManager;
import com.zonecomms.golfn.classes.BaseFragment;
import com.zonecomms.golfn.classes.ZoneConstants;

public class GetheringOpenPage extends BaseFragment {

	private final int NICK_MIN = 2; 
	private final int NICK_MAX = 14;
	private final int ID_MIN = 2;
	private final int ID_MAX = 15;
	private final int INTRO_MIN = 0;
	private final int INTRO_MAX = 100;
	
	private ImageView ivImage;
	private TextView tvImage;
	private FrameLayout frameForNickname;
	private HoloStyleEditText etNickname;
	private View coverForNickname;
	private FrameLayout frameForId;
	private HoloStyleEditText etId;
	private View coverForId;
	private HoloStyleSpinnerButton spPublic;
	private HoloStyleEditText etIntro;
	private HoloStyleButton btnOpen;
	private HoloStyleSpinnerPopup pPublic;
	private HoloStyleSpinnerPopup pPhoto;

	private boolean isEdit;
	private String nickname;
	private String id;
	private int needPublic;
	private String intro;
	private String imageUrl;
	private int img_width;
	private int img_height;
	private UploadImageInfo uploadImageInfo;
	
	@Override
	protected void bindViews() {

		ivImage = (ImageView) mThisView.findViewById(R.id.getheringopenPage_ivImage);
		tvImage = (TextView) mThisView.findViewById(R.id.getheringopenPage_tvImage);
		frameForNickname = (FrameLayout) mThisView.findViewById(R.id.getheringopenPage_frameForNickname);
		etNickname = (HoloStyleEditText) mThisView.findViewById(R.id.getheringopenPage_etNickname);
		coverForNickname = mThisView.findViewById(R.id.getheringopenPage_coverForNickname);
		frameForId = (FrameLayout) mThisView.findViewById(R.id.getheringopenPage_frameForId);
		etId = (HoloStyleEditText) mThisView.findViewById(R.id.getheringopenPage_etId);
		coverForId = mThisView.findViewById(R.id.getheringopenPage_coverForId);
		spPublic = (HoloStyleSpinnerButton) mThisView.findViewById(R.id.getheringopenPage_spPublic);
		etIntro = (HoloStyleEditText) mThisView.findViewById(R.id.getheringopenPage_etIntro);
		btnOpen = (HoloStyleButton) mThisView.findViewById(R.id.getheringopenPage_btnOpen);
		pPublic = (HoloStyleSpinnerPopup) mThisView.findViewById(R.id.getheringopenPage_pPublic);
	}

	@Override
	protected void setVariables() {

		title = getString(R.string.openGethering);
		
		if(getArguments() != null) {
			nickname = getArguments().getString("nickname");
			id = getArguments().getString("id");
			needPublic = getArguments().getInt("needPublic");
			intro = getArguments().getString("intro");
		}
		
		if(!StringUtils.isEmpty(nickname) && !StringUtils.isEmpty(id)) {
			etId.getEditText().setText(id);
			etId.setEnabled(false);
			coverForId.setVisibility(View.VISIBLE);
			etNickname.getEditText().setText(nickname);
			etNickname.setEnabled(false);
			coverForNickname.setVisibility(View.VISIBLE);
			
			etIntro.getEditText().setText(intro);
			
			if(needPublic == 1) {
				spPublic.getTextView().setText(R.string.toPublic);
			} else if(needPublic == 2) {
				spPublic.getTextView().setText(R.string.toPrivate);
			} else {
				spPublic.setText(R.string.needPublic);
			}
			
			btnOpen.setText(R.string.edit);
			isEdit = true;
		} else {
			coverForNickname.setVisibility(View.INVISIBLE);
			coverForId.setVisibility(View.INVISIBLE);
			spPublic.setText(R.string.needPublic);
			btnOpen.setText(R.string.open);
			isEdit = false;
		}

		etNickname.setHint(R.string.hintForGethering_name);
		etId.setHint(R.string.hintForGethering_id);
		etIntro.setHint(R.string.hintForGethering_introduction);
		
		pPublic.setTargetTextView(spPublic.getTextView());
		pPublic.setTitle(getString(R.string.needPublic));
		pPublic.addItem(getString(R.string.toPublic));
		pPublic.addItem(getString(R.string.toPrivate));
		pPublic.notifyDataSetChanged();
	}

	@Override
	protected void createPage() {

		pPhoto = new HoloStyleSpinnerPopup(mContext);
		pPhoto.setLayoutParams(new FrameLayout.LayoutParams(-1, -1));
		pPhoto.setTitle(getString(R.string.uploadPhoto));
		pPhoto.addItem(getString(R.string.photo_take));
		pPhoto.addItem(getString(R.string.photo_album));
		pPhoto.notifyDataSetChanged();
		((FrameLayout)(mThisView.findViewById(R.id.getheringopenPage_mainLayout))).addView(pPhoto);
	}

	@Override
	protected void setListeners() {

		coverForNickname.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Do nothing.
			}
		});
		
		coverForId.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Do nothing.
			}
		});
		
		ivImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				try {
					pPhoto.showPopup();
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			}
		});
		
		btnOpen.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(StringUtils.checkTextLength(etNickname.getEditText(), NICK_MIN, NICK_MAX) 
						!= StringUtils.PASS
						|| StringUtils.checkForbidContains(etNickname.getEditText(), 
								false, false, false, false, true, true)) {
					ToastUtils.showToast(getString(R.string.invalidGetheringNickname) +
							"\n" + getString(R.string.hintForGethering_name));
					return;
				}
				
				if(StringUtils.checkTextLength(etId.getEditText(), ID_MIN, ID_MAX) 
						!= StringUtils.PASS
						|| StringUtils.checkForbidContains(etNickname.getEditText(), 
								false, false, false, false, true, true)) {
					ToastUtils.showToast(getString(R.string.invalidGetheringId) +
							"\n" + getString(R.string.hintForGethering_id));
					return;
				}
				
				if(StringUtils.checkTextLength(etIntro.getEditText(), INTRO_MIN, INTRO_MAX) 
						!= StringUtils.PASS
						|| StringUtils.checkForbidContains(etIntro.getEditText(), 
								false, false, false, false, true, true)) {
					ToastUtils.showToast(getString(R.string.invalidGetheringIntro) +
							"\n" + getString(R.string.hintForGethering_introduction));
					return;
				}
				
				if(StringUtils.isEmpty(spPublic.getTextView().getText())
						|| spPublic.getTextView().getText().equals(getText(R.string.needPublic))) {
					ToastUtils.showToast(R.string.invalidGetheringPublic);
					return;
				}
				
				final String ID = etId.getEditText().getText().toString();
				final String NICKNAME = etNickname.getEditText().getText().toString();
				final String INTRO = etIntro.getEditText().getText().toString();
				final int NEEDPUBLIC = spPublic.getTextView().getText().equals(getText(R.string.toPublic))? 1:2;
				
				//Edit the gethering.
				if(isEdit) {
					editGethering(ID, INTRO, NEEDPUBLIC);
					
				//Open the gethering.
				} else {
					//Check nickname.
					final OnCompletedListener oclForNickname = new OnCompletedListener() {
						
						@Override
						public void onErrorRaised(String url, Exception e) {
							
							LogUtils.log("checkNickname.onError.\nurl : " + url);
							mActivity.hideLoadingView();
							ToastUtils.showToast(R.string.duplicatedNickname);
						}
						
						@Override
						public void onCompleted(String url, String result) {

							LogUtils.log("checkNickname.onCompleted.\nurl : " + url + "\nresult : " + result);
							
							try {
								if((new JSONObject(result)).getInt("errorCode") == 1) {
									openGethering(ID, NICKNAME, INTRO, NEEDPUBLIC, uploadImageInfo);
								} else {
									mActivity.hideLoadingView();
									ToastUtils.showToast(R.string.duplicatedNickname);
								}
							} catch(Exception e) {
								mActivity.hideLoadingView();
								ToastUtils.showToast(R.string.duplicatedNickname);
							}
						}
					};

					//Check id.
					final OnCompletedListener oclForId = new OnCompletedListener() {
						
						@Override
						public void onErrorRaised(String url, Exception e) {
							LogUtils.log("checkId.onError.\nurl : " + url);
							ToastUtils.showToast(R.string.duplicatedId);
							mActivity.hideLoadingView();
						}
						
						@Override
						public void onCompleted(String url, String result) {
							
							LogUtils.log("checkId.onCompleted.\nurl : " + url + "\nresult : " + result);
							
							try {
								if((new JSONObject(result)).getInt("errorCode") == 1) {
									url = ZoneConstants.BASE_URL + "sb/check_sb_nickname" +
											"?sb_nickname=" + URLEncoder.encode(NICKNAME, "utf-8");
									AsyncStringDownloader.download(url, getDownloadKey(), oclForNickname);
								} else {
									mActivity.hideLoadingView();
									ToastUtils.showToast(R.string.duplicatedId);
								}
							} catch(Exception e) {
								mActivity.hideLoadingView();
								ToastUtils.showToast(R.string.duplicatedId);
							}
						}
					};

					mActivity.showLoadingView();
					String url = ZoneConstants.BASE_URL + "sb/check_sb_id" 
								+ "?sb_id=" + id;
					AsyncStringDownloader.download(url, getDownloadKey(), oclForId);
				}
			}
		});
		
		spPublic.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				pPublic.showPopup();
			}
		});
		
		pPhoto.setOnItemClickedListener(new OnItemClickedListener() {
			
			@Override
			public void onItemClicked(int position, String itemString) {
				String filePath = null;
				String fileName = null;
				int requestCode = 0;
				Intent intent = new Intent();
				
				if(StringUtils.isEmpty(itemString)) {
					return;
				} else if(itemString.equals(getString(R.string.photo_take))){
					intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

				    File fileDerectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				    filePath = fileDerectory.getAbsolutePath();
				    fileName = System.currentTimeMillis() + ".jpg";
				    File file = new File(fileDerectory, fileName);
				    
				    if(!fileDerectory.exists()) {
				    	fileDerectory.mkdirs();
				    }
				    
				    Uri uri = Uri.fromFile(file);
				    intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, uri);
				    requestCode = ZoneConstants.REQUEST_CAMERA;
				    
				} else if(itemString.equals(getString(R.string.photo_album))){
//					intent.setAction(Intent.ACTION_GET_CONTENT);
					intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");
					requestCode = ZoneConstants.REQUEST_GALLERY;
				}
				
				OnAfterUploadImage oaui = new OnAfterUploadImage() {
					
					@Override
					public void onAfterUploadImage(UploadImageInfo uploadImageInfo, Bitmap thumbnail) {
						
						if(uploadImageInfo != null) {
							
							imageUrl = uploadImageInfo.getImageUrl();
							img_width = uploadImageInfo.getImageWidth();
							img_height = uploadImageInfo.getImageHeight();
							
							try {
								if(thumbnail != null && !thumbnail.isRecycled()) {
									ivImage.setImageBitmap(thumbnail);
									ToastUtils.showToast(R.string.updateGalleryCompleted);
									GetheringOpenPage.this.uploadImageInfo = uploadImageInfo;
								}
								
							} catch(Exception e) {
								LogUtils.trace(e);
								ToastUtils.showToast(R.string.failToLoadBitmap);
							} catch(Error e) {
								LogUtils.trace(e);
								ToastUtils.showToast(R.string.failToLoadBitmap);
							}
						}
					}
				};
				
				mActivity.imageUploadSetting(filePath, fileName, false, oaui);
				mActivity.startActivityForResult(intent, requestCode);
			}
		});
	}

	@Override
	protected void setSizes() {

		ResizeUtils.viewResize(300, 300, ivImage, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 40, 0, 0});
		ResizeUtils.viewResize(320, LayoutParams.WRAP_CONTENT, tvImage, 1, 
				Gravity.CENTER_HORIZONTAL, new int[]{0, 10, 0, 0});
		ResizeUtils.viewResize(540, 70, frameForNickname, 1, 0, new int[]{50, 40, 0, 0});
		ResizeUtils.viewResize(540, 70, frameForId, 1, 0, new int[]{50, 40, 0, 0});
		ResizeUtils.viewResize(240, 70, spPublic, 1, 0, new int[]{50, 40, 0, 0});
		ResizeUtils.viewResize(540, 70, etIntro, 1, 0, new int[]{50, 40, 0, 0});
		ResizeUtils.viewResize(540, 70, btnOpen, 1, 0, new int[]{50, 40, 0, 40});
		
		FontInfo.setFontAndHintSize(etNickname.getEditText(), 26, 20);
		FontInfo.setFontAndHintSize(etId.getEditText(), 26, 20);
		FontInfo.setFontAndHintSize(etIntro.getEditText(), 26, 20);
	}

	@Override
	public String getTitleText() {

		return title;
	}

	@Override
	protected int getContentViewId() {

		return R.id.getheringopenPage_mainLayout;
	}

	@Override
	public void onRefreshPage() {
	}

	@Override
	public boolean onBackKeyPressed() {
		
		if(pPublic.getVisibility() == View.VISIBLE) {
			pPublic.hidePopup();
			return true;
		} else if(pPhoto.getVisibility() == View.VISIBLE) {
			pPhoto.hidePopup();
			return true;
		}
		
		return false;
	}

	@Override
	public void onSoftKeyboardShown() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSoftKeyboardHidden() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		
		if(!hidden) {
			
			if(mActivity.getSponserBanner() != null) {
				mActivity.getSponserBanner().hideBanner();
			}
			
			if(!hidden) {
				mActivity.getTitleBar().showHomeButton();
				mActivity.getTitleBar().hideWriteButton();
				setImage();
			}
		}
	}
	
	public void setImage() {
		
		if(getArguments().containsKey("imageUrl")) {
			imageUrl = getArguments().getString("imageUrl");
		}
		
		if(imageUrl == null) {
			return;
		}
		
		BitmapDownloader.OnCompletedListener ocl = new BitmapDownloader.OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String _url, Exception e) {
				
//				ToastUtils.showToast(R.string.failToDownloadImage);
			}
			
			@Override
			public void onCompleted(String _url, Bitmap bitmap, ImageView view) {
			
				//Check bitmap.
				if(bitmap == null || bitmap.isRecycled()) {
					return;
				}
				
				//Check ImageView.
				if(view == null) {
					return;
				}

				imageUrl = _url;
				img_width = bitmap.getWidth();
				img_height = bitmap.getHeight();
				view.setImageBitmap(bitmap);
				
				if(view.getVisibility() != View.VISIBLE) {
					view.setVisibility(View.VISIBLE);
				}
			}
		};
		BitmapDownloader.downloadImmediately(imageUrl, getDownloadKey(), ocl, null, ivImage, false);
	}
	
	public void openGethering(final String id, String name, String intro, 
			int needPublic, UploadImageInfo uploadImageInfo) {
		
		try {
			String url = ZoneConstants.BASE_URL + "sb/open" +
					"?sb_id=" + URLEncoder.encode(id, "utf-8") +
					"&sb_nickname=" + URLEncoder.encode(name, "utf-8") +
					"&sb_description=" + URLEncoder.encode(intro, "utf-8") +
					"&sb_public=" + needPublic +
					"&member_id=" + MainActivity.myInfo.getMember_id() +
					"&origin_sb_id=" + ZoneConstants.PAPP_ID;
			
			if(uploadImageInfo != null) {
				url += "&m_image=" + uploadImageInfo.getImageUrl() +
						"&img_width=" + uploadImageInfo.getImageWidth() +
						"&img_height=" + uploadImageInfo.getImageHeight();
			}
			
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					
					LogUtils.log("openGethering.onError.\nurl : " + url);
					
					ToastUtils.showToast(R.string.failToOpenGethering);
					mActivity.hideLoadingView();
				}
				
				@Override
				public void onCompleted(String url, String result) {

					LogUtils.log("openGethering.onCompleted.\nurl : " + url + "\nresult : " + result);
					
					mActivity.hideLoadingView();
					
					try {
						if((new JSONObject(result)).getInt("errorCode") == 1) {
							ToastUtils.showToast(R.string.completeOpenGethering);
							
							mActivity.getMainLayout().postDelayed(new Runnable() {
								
								@Override
								public void run() {
									mActivity.showGetheringPage(id);
								}
							}, 600);
							mActivity.finishFragment(GetheringOpenPage.this);
						} else {
							ToastUtils.showToast(R.string.failToOpenGethering);
						}
					} catch(Exception e) {
						ToastUtils.showToast(R.string.failToOpenGethering);
					}
				}
			};

			mActivity.showLoadingView();
			
			AsyncStringDownloader.download(url, null, ocl);
		} catch(Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToOpenGethering);
		}
	}
	
	public void editGethering(String id, String intro, int needPublic) {

		try {
			String url = ZoneConstants.BASE_URL + "sb/edit"
					+ "?sb_id=" + URLEncoder.encode(id, "utf-8")
					+ "&sb_description=" + URLEncoder.encode(intro, "utf-8")
					+ "&sb_public=" + needPublic
					+ "&member_id=" + MainActivity.myInfo.getMember_id()
					+ "&origin_sb_id=" + ZoneConstants.PAPP_ID
					+ "&m_image=" + imageUrl
					+ "&img_width=" + img_width
					+ "&img_height=" + img_height;
			
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					
					LogUtils.log("GetheringOpenPage.editGethering.onError.\nurl : " + url);
					ToastUtils.showToast(R.string.failToEdit);
					mActivity.hideLoadingView();
				}
				
				@Override
				public void onCompleted(String url, String result) {
					
					LogUtils.log("GetheringOpenPage.editGethering.onCompleted.\nurl : "
							+ url + "\nresult : " + result);
					mActivity.hideLoadingView();
					
					try {
						if((new JSONObject(result)).getInt("errorCode") == 1) {
							ToastUtils.showToast(R.string.completeEdit);
							mActivity.getMainLayout().postDelayed(new Runnable() {
								
								@Override
								public void run() {
									if(ApplicationManager.getTopFragment() instanceof GetheringPage) {
										((GetheringPage)ApplicationManager.getTopFragment()).onRefreshPage();
									}
								}
							}, 600);
							
							mActivity.finishFragment(GetheringOpenPage.this);
						} else {
							ToastUtils.showToast(R.string.failToEdit);
						}
					} catch(Exception e) {
						ToastUtils.showToast(R.string.failToEdit);
					}
				}
			};
			
			mActivity.showLoadingView();
			
			AsyncStringDownloader.download(url, null, ocl);
		} catch(Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToEdit);
		}
	}

	@Override
	protected String generateDownloadKey() {
		return "GETHERINGOPENPAGE" + madeCount;
	}

	@Override
	protected int getXmlResId() {
		return R.layout.page_getheringopen;
	}
}

package com.zonecomms.golfn.fragments;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleButton;
import com.outspoken_kid.views.holo_dark.HoloStyleEditText;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup.OnItemClickedListener;
import com.zonecomms.common.adapters.ListAdapter;
import com.zonecomms.common.models.Message;
import com.zonecomms.common.models.UploadImageInfo;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.utils.ImageUploadUtils.OnAfterUploadImage;
import com.zonecomms.golfn.MainActivity;
import com.zonecomms.golfn.R;
import com.zonecomms.golfn.classes.ApplicationManager;
import com.zonecomms.golfn.classes.BaseListFragment;
import com.zonecomms.golfn.classes.ZoneConstants;

public class MessagePage extends BaseListFragment {

	private PullToRefreshListView listView;
	private FrameLayout mainLayout;
	private View photo;
	private HoloStyleEditText etMessage;
	private HoloStyleButton btnSend;
	private HoloStyleSpinnerPopup pPhoto;
	private HoloStyleSpinnerPopup pMessage;
	
	private String member_id;
	private boolean isFirstLoading = true;
	private int numOfNewMessages;
	private String message_content;
	private int message_microspot_nid;
	
	@Override
	protected void bindViews() {

		mainLayout = (FrameLayout) mThisView.findViewById(R.id.messagePage_mainLayout);
		listView = (PullToRefreshListView) mThisView.findViewById(R.id.messagePage_pullToRefreshView);
		photo = mThisView.findViewById(R.id.messagePage_photo);
		etMessage = (HoloStyleEditText) mThisView.findViewById(R.id.messagePage_etMessage);
		btnSend = (HoloStyleButton) mThisView.findViewById(R.id.messagePage_btnSend);
	}

	@Override
	protected void setVariables() {
		
		if(getArguments() != null) {
			member_id = getArguments().getString("member_id");
		}
	}

	@Override
	protected void createPage() {
		
		ListAdapter listAdapter = new ListAdapter(mContext, models, false);
		listView.setAdapter(listAdapter);
		listView.setBackgroundColor(Color.BLACK);
		listView.getRefreshableView().setDividerHeight(0);
		listView.getRefreshableView().setCacheColorHint(Color.argb(0, 0, 0, 0));
		listView.getRefreshableView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		listView.setLabels(getString(
				R.string.pull_to_loading_pull_label),
				getString(R.string.pull_to_loading_release_label));
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				downloadInfo();
			}
		});
		
		zoneAdapter = listAdapter;
		etMessage.getEditText().setSingleLine(false);
		etMessage.setBackgroundColor(Color.rgb(34, 34, 34));
		btnSend.setText(R.string.send);
		
		pPhoto = new HoloStyleSpinnerPopup(mContext);
		pPhoto.setTitle(getString(R.string.uploadPhoto));
		pPhoto.addItem(getString(R.string.photo_take));
		pPhoto.addItem(getString(R.string.photo_album));
		pPhoto.notifyDataSetChanged();
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

				    File fileDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
				    filePath = fileDirectory.getAbsolutePath();
				    fileName = System.currentTimeMillis() + ".jpg";
				    File file = new File(fileDirectory, fileName);
				    
				    if(!fileDirectory.exists()) {
				    	fileDirectory.mkdirs();
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

							try {
								String url = ZoneConstants.BASE_URL + "microspot/write" +
										"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
										"&friend_member_id=" + member_id +
										"&text=" +
										"&content_type=2" +
										"&media_src=" + uploadImageInfo.getImageUrl() +
										"&img_width=" + uploadImageInfo.getImageWidth() +
										"&img_height=" + + uploadImageInfo.getImageHeight();
								
								AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
									
									@Override
									public void onErrorRaised(String url, Exception e) {
										ToastUtils.showToast(R.string.failToLoadBitmap);
									}
									
									@Override
									public void onCompleted(String url, String result) {

										try {
											if((new JSONObject(result)).getInt("errorCode") == 1) {
												onRefreshPage();
											} else {
												ToastUtils.showToast(R.string.failToLoadBitmap);
											}
										} catch(Exception e) {
											LogUtils.trace(e);
											ToastUtils.showToast(R.string.failToLoadBitmap);
										}
									}
								};
								AsyncStringDownloader.download(url, getDownloadKey(), ocl);
							} catch(Exception e) {
								LogUtils.trace(e);
								ToastUtils.showToast(R.string.failToLoadBitmap);
							}
						}
					}
				};
				
				mActivity
					.imageUploadSetting(filePath, fileName, false, oaui);
				mActivity.startActivityForResult(intent, requestCode);
			}
		});
		mainLayout.addView(pPhoto);
		
		pMessage = new HoloStyleSpinnerPopup(mContext);
		pMessage.setTitle(null);
		pMessage.addItem(getString(R.string.copy));
		pMessage.addItem(getString(R.string.delete));
		pMessage.notifyDataSetChanged();
		pMessage.setOnItemClickedListener(new OnItemClickedListener() {

			@Override
			public void onItemClicked(int position, String itemString) {

				if(StringUtils.isEmpty(itemString)) {
					return;
					
				} else if(itemString.equals(getString(R.string.copy))){
					copyMesasge();
					
				} else if(itemString.equals(getString(R.string.delete))){
					deleteMessage();
				}
			}
		});
		mainLayout.addView(pMessage);
	}

	@Override
	protected void setListeners() {

		photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				pPhoto.showPopup();
			}
		});
		
		btnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if(etMessage.getEditText().getText() != null 
						&& !StringUtils.isEmpty(etMessage.getEditText().getText().toString())) {
					sendMessage(etMessage.getEditText().getText().toString());
				} else {
					ToastUtils.showToast(R.string.inputContent);
				}
			}
		});
	}

	@Override
	protected void setSizes() {
		
		ResizeUtils.viewResize(66, 53, photo, 1, Gravity.CENTER_VERTICAL, new int[]{8, 8, 8, 8});
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		etMessage.setLayoutParams(lp);
		etMessage.getEditText().setMinimumHeight(ResizeUtils.getSpecificLength(60));
		etMessage.getEditText().setMaxHeight(ResizeUtils.getSpecificLength(150));
		FontInfo.setFontSize(etMessage.getEditText(), 30);
		
		ResizeUtils.viewResize(100, 60, btnSend, 1, Gravity.CENTER_VERTICAL, new int[]{8, 8, 8, 8});
		FontInfo.setFontSize(btnSend.getTextView(), 30);
	}

	@Override
	protected void downloadInfo() {

		if(isDownloading) {
			return;
		}
		
		try {
			url = ZoneConstants.BASE_URL + "microspot/message_tap" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&friend_member_id=" + member_id +
					"&last_microspot_nid=" + lastIndexno +
					"&image_size=640";
			
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {

					LogUtils.log("###MessagePage.onErrorRaised.  \nurl : " + url);
					setPage(false);
				}
				
				@Override
				public void onCompleted(String url, String result) {
					
					LogUtils.log("###MessagePage.onCompleted.  \nurl : " + url + "\nresult : " + result);
					
					try {
						JSONArray arResult = (new JSONObject(result)).getJSONArray("data");
						int length = arResult.length();
						
						boolean addToTop = false;
						ArrayList<Message> messages = new ArrayList<Message>();
						if(models.size() != 0) {
							addToTop = true;
						}
						
						if(length > 0) {
							for(int i=0; i<length; i++) {
								try {
									Message message = new Message(arResult.getJSONObject(i));
									message.setItemCode(ZoneConstants.ITEM_MESSAGE);

									if(addToTop) {
										messages.add(message);
									} else {
										models.add(message);
									}

									if(i==0) {
										lastIndexno = message.getIndexno();

										if(message.getPost_member_id().equals(MainActivity.myInfo.getMember_id())) {
											title = message.getMystory_member_nickname();
										} else {
											title = message.getPost_member_nickname();
										}
										
										mActivity.setTitleText(title);
									}
								} catch(Exception e) {
								}
							}
							
							if(addToTop) {
								
								try {
									length = messages.size();
									for(int i=length - 1; i>=0; i--) {
										models.add(0, messages.get(i));
									}
									
									numOfNewMessages = length;
								} catch(Exception e) {
								}
							}
						} else {
							if(title != null && title.equals("MESSAGE")) {
								mActivity.setTitleText(member_id);
							}
							ToastUtils.showToast(R.string.lastPage);
							numOfNewMessages = 0;
						}

						setPage(true);
					} catch(Exception e) {
						LogUtils.trace(e);
						setPage(false);
					}
				}
			};
			
			if(models.size() == 0 && !isRefreshing) {
				mActivity.showLoadingView();
			}
			
			isDownloading = true;
			AsyncStringDownloader.download(url, getDownloadKey(), ocl);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	@Override
	protected void setPage(boolean successDownload) {

		mActivity.hideLoadingView();
		
		if(!isRefreshing && !isFirstLoading) {

			if(listView != null) {
				listView.getRefreshableView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
				listView.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						listView.onRefreshComplete();
						
						if(numOfNewMessages != 0) {
							listView.getRefreshableView().setSelectionFromTop(numOfNewMessages, 0);
						}
					}
				}, 300);
			}	
		} else {
			isFirstLoading = false;
			listView.getRefreshableView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			listView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					try {
						//Scroll to last item.
						listView.getRefreshableView().setSelection(zoneAdapter.getCount());
					} catch(Exception e) {
						LogUtils.trace(e);
					}
				}
			}, 500);
		}
		
		if(successDownload) {
			if(zoneAdapter != null) {
				zoneAdapter.notifyDataSetChanged();
			}
		} else {
			ToastUtils.showToast(R.string.failToLoadList);
		}

		isRefreshing = false;
		isDownloading = false;
	}

	@Override
	protected int getContentViewId() {

		return R.id.messagePage_mainLayout;
	}

	@Override
	public boolean onBackKeyPressed() {
		
		if(pPhoto.getVisibility() == View.VISIBLE) {
			pPhoto.hidePopup();
		} else if(pMessage.getVisibility() == View.VISIBLE) {
			pMessage.hidePopup();
		} else {
			return false;
		}
		return true;
	}

	@Override
	public void onRefreshPage() {

		LogUtils.log("###MessagePage.onRefreshPage.  ");
		
		isRefreshing = true;
		isFirstLoading = true;
		isDownloading = false;
		numOfNewMessages = 0;
		lastIndexno = 0;
		models.clear();
		downloadInfo();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if(!hidden) {

			if(mActivity.getSponserBanner() != null) {
				mActivity.getSponserBanner().hideBanner();
			}
			
			mActivity.getTitleBar().showHomeButton();
			mActivity.getTitleBar().hideWriteButton();
		}
	}

	@Override
	public void onSoftKeyboardShown() {
		
	}

	@Override
	public void onSoftKeyboardHidden() {
		
	}
	
//////////////////////// Custom methods.

	public String getFriend_member_id() {
		
		return member_id;
	}
	
	public void sendMessage(String input) {
		
		mActivity.showLoadingView();
		
		try {
			String url = ZoneConstants.BASE_URL + "microspot/write" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&friend_member_id=" + member_id +
					"&text=" + URLEncoder.encode(etMessage.getEditText().getText().toString(), "UTF-8") +
					"&content_type=1" +
					"&media_src=" +
					"&img_width=" +
					"&img_height=";
			
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
					ToastUtils.showToast(R.string.failToSendMessage);
					mActivity.hideLoadingView();
				}
				
				@Override
				public void onCompleted(String url, String result) {
					onRefreshPage();
					etMessage.getEditText().setText("");
				}
			};
			
			AsyncStringDownloader.download(url, getDownloadKey(), ocl);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	@Override
	protected String generateDownloadKey() {
		return "MESSAGEPAGE" + madeCount;
	}

	@Override
	protected int getXmlResId() {
		return R.layout.page_message;
	}
	
	public void showDialogForMessage(int microspot_nid, String content) {
		
		LogUtils.log("###MessagePage.showDialogForMessage.  microspot_nid : " + microspot_nid + ", content : " + content);
		
		this.message_microspot_nid = microspot_nid;
		this.message_content = content;

		pMessage.showPopup();
	}

	public void copyMesasge() {
		
		if(StringUtils.copyStringToClipboard(mContext, message_content)) {
			ToastUtils.showToast(R.string.copyReplyCompleted);
		} else {
			ToastUtils.showToast(R.string.failToCopyReply);
		}
	}
	
	public void deleteMessage() {
		
		String url = ZoneConstants.BASE_URL + "microspot/microspot_delete" +
				"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
				"&microspot_nid=" + message_microspot_nid;
		
		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {
				ToastUtils.showToast(R.string.failToDeleteMessage);
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				try {
					JSONObject objJSON = new JSONObject(result);
					
					if(objJSON.has("errorCode") && objJSON.getInt("errorCode") == 1) {
						ToastUtils.showToast(R.string.deleteCompleted);
						onRefreshPage();
					} else {
						ToastUtils.showToast(R.string.failToDeleteMessage);
					}
				} catch(Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToDeleteMessage);
				}
			}
		};
		ToastUtils.showToast(R.string.wait);
		AsyncStringDownloader.download(url, ApplicationManager.getDownloadKeyFromTopFragment(), ocl);
	}
}

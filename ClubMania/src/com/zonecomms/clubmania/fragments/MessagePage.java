package com.zonecomms.clubmania.fragments;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.outspoken_kid.classes.BaseListFragment;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.zonecomms.common.utils.AppInfoUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleButton;
import com.outspoken_kid.views.holo_dark.HoloStyleEditText;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup.OnItemClickedListener;
import com.zonecomms.clubmania.MainActivity;
import com.zonecomms.clubmania.R;
import com.zonecomms.clubmania.classes.ZoneConstants;
import com.zonecomms.common.adapters.ListAdapter;
import com.zonecomms.common.models.Message;
import com.zonecomms.common.models.UploadImageInfo;
import com.zonecomms.common.utils.ImageUploadUtils.OnAfterUploadImage;

public class MessagePage extends BaseListFragment {

	private PullToRefreshListView listView;
	private FrameLayout mainLayout;
	private View photo;
	private HoloStyleEditText etMessage;
	private HoloStyleButton btnSend;
	private HoloStyleSpinnerPopup pPhoto;
	
	private String member_id;
	private boolean isFirstLoading = true;
	private int numOfNewMessages;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(container == null) {
			return null;
		}
	
		mThisView = inflater.inflate(R.layout.page_message, null);
		return mThisView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		bindViews();
		setVariables();
		createPage();
		
		setListener();
		setSize();
	}
	
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
		
		setDownloadKey("MESSAGEPAGE" + madeCount);
		
		if(getArguments() != null) {
			member_id = getArguments().getString("member_id");
		}
	}

	@Override
	protected void createPage() {
		
		ListAdapter listAdapter = new ListAdapter(mContext, mActivity, models, false);
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
		
		targetAdapter = listAdapter;
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
				
				if(TextUtils.isEmpty(itemString)) {
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
					intent.setAction(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					requestCode = ZoneConstants.REQUEST_GALLERY;
				}
				
				OnAfterUploadImage oaui = new OnAfterUploadImage() {
					
					@Override
					public void onAfterUploadImage(UploadImageInfo uploadImageInfo,
							Bitmap thumbnail) {
						
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
											e.printStackTrace();
											ToastUtils.showToast(R.string.failToLoadBitmap);
										}
									}
								};
								AsyncStringDownloader.download(url, getDownloadKey(), ocl);
							} catch(Exception e) {
								e.printStackTrace();
								ToastUtils.showToast(R.string.failToLoadBitmap);
							}
						}
					}
				};
				
				mActivity.imageUploadSetting(filePath, fileName, false, oaui);
				mActivity.startActivityForResult(intent, requestCode);
			}
		});
		mainLayout.addView(pPhoto);
	}

	@Override
	protected void setListener() {

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
						&& !TextUtils.isEmpty(etMessage.getEditText().getText().toString())) {
					sendMessage(etMessage.getEditText().getText().toString());
				} else {
					ToastUtils.showToast(R.string.inputContent);
				}
			}
		});
	}

	@Override
	protected void setSize() {
		
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

					setPage(false);
				}
				
				@Override
				public void onCompleted(String url, String result) {
					
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
										
										mActivity.getTitleBar().setTitleText(title);
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
							if(title.equals("MESSAGE")) {
								mActivity.getTitleBar().setTitleText(member_id);
							}
							ToastUtils.showToast(R.string.lastPage);
							numOfNewMessages = 0;
						}

						setPage(true);
					} catch(Exception e) {
						e.printStackTrace();
						setPage(false);
					}
				}
			};
			
			if(models.size() == 0) {
				mActivity.showLoadingView();
			}
			
			isDownloading = true;
			AsyncStringDownloader.download(url, getDownloadKey(), ocl);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void setPage(boolean successDownload) {

		mActivity.hideLoadingView();
		mActivity.hideCover();
		
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
						listView.getRefreshableView().setSelection(targetAdapter.getCount());
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			}, 500);
		}
		
		if(successDownload) {
			if(targetAdapter != null) {
				targetAdapter.notifyDataSetChanged();
			}
		} else {
			ToastUtils.showToast(R.string.failToLoadList);
		}

		isRefreshing = false;
		isDownloading = false;
	}

	@Override
	protected String getTitleText() {

		if(TextUtils.isEmpty(title)) {
			title = "MESSAGE";
		}
		
		return title;
	}

	@Override
	protected int getContentViewId() {

		return R.id.messagePage_mainLayout;
	}

	@Override
	public boolean onBackKeyPressed() {
		
		if(pPhoto.getVisibility() == View.VISIBLE) {
			pPhoto.hidePopup();
		} else {
			return false;
		}
		return true;
	}

	@Override
	public void onRefreshPage() {

		isRefreshing = true;
		isFirstLoading = true;
		numOfNewMessages = 0;
		lastIndexno = 0;
		models.clear();
		downloadInfo();
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);

		if(!hidden) {
			mActivity.getTitleBar().showHomeButton();
			mActivity.getTitleBar().hideWriteButton();
			
			if(mActivity.getSponserBanner() != null) {
				mActivity.getSponserBanner().hideBanner();
			}
		}
	}

	@Override
	public void onSoftKeyboardShown() {
		
	}

	@Override
	public void onSoftKeyboardHidden() {
		
	}
	
	@Override
	public void finish(boolean needAnim, boolean isBeforeMain) {
		
		SoftKeyboardUtils.hideKeyboard(mContext, etMessage);
		super.finish(needAnim, isBeforeMain);
	}
	
//////////////////////// Custom methods.

	public String getFriend_member_id() {
		
		return member_id;
	}
	
	public void sendMessage(String input) {
		
		mActivity.showLoadingView();
		mActivity.showCover();
		
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
					mActivity.hideCover();
				}
				
				@Override
				public void onCompleted(String url, String result) {
					onRefreshPage();
					etMessage.getEditText().setText("");
				}
			};
			
			AsyncStringDownloader.download(url, getDownloadKey(), ocl);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

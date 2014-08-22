package com.zonecomms.clubvera.fragments;

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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo.holo_light.HoloStyleButton;
import com.outspoken_kid.views.holo.holo_light.HoloStyleEditText;
import com.outspoken_kid.views.holo.holo_light.HoloStyleSpinnerPopup;
import com.outspoken_kid.views.holo.holo_light.HoloStyleSpinnerPopup.OnItemClickedListener;
import com.zonecomms.clubvera.R;
import com.zonecomms.clubvera.classes.ZoneConstants;
import com.zonecomms.clubvera.classes.ZonecommsApplication;
import com.zonecomms.clubvera.classes.ZonecommsListFragment;
import com.zonecomms.common.adapters.ListAdapter;
import com.zonecomms.common.models.Message;
import com.zonecomms.common.models.UploadImageInfo;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.utils.ImageUploadUtils.OnAfterUploadImage;
import com.zonecomms.common.views.TitleBar;

public class MessagePage extends ZonecommsListFragment {

	private RelativeLayout mainLayout;
	private SwipeRefreshLayout swipeRefreshLayout;
	private ListView listView;
	private View photo;
	private HoloStyleEditText etMessage;
	private HoloStyleButton btnSend;
	private HoloStyleSpinnerPopup pPhoto;
	
	private String member_id;
	private boolean isFirstLoading = true;
	private int numOfNewMessages;
	
	@Override
	public void bindViews() {

		mainLayout = (RelativeLayout) mThisView.findViewById(R.id.messagePage_mainLayout);
		swipeRefreshLayout = (SwipeRefreshLayout) mThisView.findViewById(R.id.messagePage_swipeRefreshLayout);
		listView = (ListView) mThisView.findViewById(R.id.messagePage_listView);
		photo = mThisView.findViewById(R.id.messagePage_photo);
		etMessage = (HoloStyleEditText) mThisView.findViewById(R.id.messagePage_etMessage);
		btnSend = (HoloStyleButton) mThisView.findViewById(R.id.messagePage_btnSend);
	}

	@Override
	public void setVariables() {
		super.setVariables();
		
		if(getArguments() != null) {
			member_id = getArguments().getString("member_id");
		}
	}

	@Override
	public void createPage() {
		
		swipeRefreshLayout.setColorSchemeColors(
        		Color.argb(255, 255, 102, 153), 
        		Color.argb(255, 255, 153, 153), 
        		Color.argb(255, 255, 204, 153), 
        		Color.argb(255, 255, 255, 153));
        swipeRefreshLayout.setEnabled(true);
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {

				swipeRefreshLayout.setRefreshing(true);
				downloadInfo();
			}
		});
		
		ListAdapter listAdapter = new ListAdapter(mContext, models, false);
		listView.setAdapter(listAdapter);
		listView.setDividerHeight(0);
		listView.setCacheColorHint(Color.argb(0, 0, 0, 0));
		listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		
		targetAdapter = listAdapter;
		etMessage.getEditText().setSingleLine(false);
		btnSend.setText(R.string.send);
		
		pPhoto = new HoloStyleSpinnerPopup(mContext);
		pPhoto.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
								
								DownloadUtils.downloadJSONString(url,
										new OnJSONDownloadListener() {

											@Override
											public void onError(String url) {
												
												ToastUtils.showToast(R.string.failToLoadBitmap);
											}

											@Override
											public void onCompleted(String url,
													JSONObject objJSON) {

												try {
													LogUtils.log("MessagePage.onCompleted."
															+ "\nurl : "
															+ url
															+ "\nresult : "
															+ objJSON);

													if(objJSON.getInt("errorCode") == 1) {
														refreshPage();
													} else {
														ToastUtils.showToast(R.string.failToLoadBitmap);
													}
												} catch (Exception e) {
													LogUtils.trace(e);
													ToastUtils.showToast(R.string.failToLoadBitmap);
												} catch (OutOfMemoryError oom) {
													LogUtils.trace(oom);
													ToastUtils.showToast(R.string.failToLoadBitmap);
												}
											}
										});
							} catch(Exception e) {
								LogUtils.trace(e);
								ToastUtils.showToast(R.string.failToLoadBitmap);
							}
						}
					}
				};
				
				mainActivity.imageUploadSetting(filePath, fileName, false, oaui);
				mainActivity.startActivityForResult(intent, requestCode);
			}
		});
		mainLayout.addView(pPhoto);
		
		mThisView.findViewById(R.id.messagePage_line).setBackgroundColor(TitleBar.titleBarColor);
	}

	@Override
	public void setListeners() {

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
	public void setSizes() {
		
		ResizeUtils.viewResize(66, 53, photo, 1, Gravity.CENTER_VERTICAL, new int[]{8, 8, 8, 8});
		
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1);
		lp.gravity = Gravity.BOTTOM;
		etMessage.setLayoutParams(lp);
		etMessage.getEditText().setMinimumHeight(ResizeUtils.getSpecificLength(60));
		etMessage.getEditText().setMaxHeight(ResizeUtils.getSpecificLength(150));
		FontUtils.setFontSize(etMessage.getEditText(), 30);
		
		ResizeUtils.viewResize(100, 60, btnSend, 1, Gravity.CENTER_VERTICAL, new int[]{8, 8, 8, 8});
		FontUtils.setFontSize(btnSend.getTextView(), 30);
	}

	@Override
	public void downloadInfo() {

		if(isDownloading) {
			return;
		}
		
		try {
			if(models.size() == 0) {
				mainActivity.showLoadingView();
			}
			
			isDownloading = true;
			
			url = ZoneConstants.BASE_URL + "microspot/message_tap" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&friend_member_id=" + member_id +
					"&last_microspot_nid=" + lastIndexno +
					"&image_size=640";
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {
					
					setPage(false);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("MessagePage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						JSONArray arResult = objJSON.getJSONArray("data");
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

										if(message.getPost_member_id().equals(ZonecommsApplication.myInfo.getMember_id())) {
											title = message.getMystory_member_nickname();
										} else {
											title = message.getPost_member_nickname();
										}
										
										mainActivity.getTitleBar().setTitleText(title);
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
								mainActivity.getTitleBar().setTitleText(member_id);
							}
							ToastUtils.showToast(R.string.lastPage);
							numOfNewMessages = 0;
						}

						setPage(true);
					} catch (Exception e) {
						LogUtils.trace(e);
						setPage(false);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						setPage(false);
					}
				}
			});
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void setPage(boolean successDownload) {

		mainActivity.hideLoadingView();
		
		if(!isRefreshing && !isFirstLoading) {

			if(listView != null) {
				listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_DISABLED);
				listView.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						swipeRefreshLayout.setRefreshing(false);
						
						if(numOfNewMessages != 0) {
							listView.setSelectionFromTop(numOfNewMessages, 0);
						}
					}
				}, 500);
			}	
		} else {
			isFirstLoading = false;
			listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
			listView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					try {
						listView.setSelection(targetAdapter.getCount());
					} catch(Exception e) {
						LogUtils.trace(e);
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
	public String getTitleText() {

		if(StringUtils.isEmpty(title)) {
			title = "MESSAGE";
		}
		
		return title;
	}

	@Override
	public int getContentViewId() {

		return R.layout.page_message;
	}
	
	@Override
	public void hideLoadingView() {

		mainActivity.hideLoadingView();
	}

	@Override
	public void showLoadingView() {

		mainActivity.showLoadingView();
	}
	
	@Override
	public boolean onBackPressed() {
		
		if(pPhoto.getVisibility() == View.VISIBLE) {
			pPhoto.hidePopup();
		} else {
			return false;
		}
		return true;
	}
	
	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void refreshPage() {
		
		if(isRefreshing) {
			return;
		}
		
		isRefreshing = true;
		isFirstLoading = true;
		numOfNewMessages = 0;
		lastIndexno = 0;
		models.clear();
		downloadInfo();
	}

	@Override
	public void onResume() {
		super.onResume();
		
		mainActivity.showTitleBar();
		mainActivity.getTitleBar().hideCircleButton();
		mainActivity.getTitleBar().showHomeButton();
		mainActivity.getTitleBar().hideWriteButton();
		
		if(mainActivity.getSponserBanner() != null) {
			mainActivity.getSponserBanner().hideBanner();
		}
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
		
		mainActivity.showLoadingView();
		
		try {
			String url = ZoneConstants.BASE_URL + "microspot/write" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&friend_member_id=" + member_id +
					"&text=" + URLEncoder.encode(etMessage.getEditText().getText().toString(), "UTF-8") +
					"&content_type=1" +
					"&media_src=" +
					"&img_width=" +
					"&img_height=";
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {
					
					ToastUtils.showToast(R.string.failToSendMessage);
					mainActivity.hideLoadingView();
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("MessagePage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						refreshPage();
						etMessage.getEditText().setText("");
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			});
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
}

package com.byecar.fragments.user;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPDownloadUtils;
import com.byecar.classes.BCPFragment;
import com.byecar.models.AttatchedPictureInfo;
import com.byecar.models.Post;
import com.byecar.views.TitleBar;
import com.outspoken_kid.activities.BaseFragmentActivity;
import com.outspoken_kid.activities.ImageSelectActivity.OnAfterPickImageListener;
import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ImageUploadUtils;
import com.outspoken_kid.utils.ImageUploadUtils.OnAfterUploadImage;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class WriteForumPage extends BCPFragment {

	private Post post;
	
	private Button btnCategory;
	private EditText etTitle;
	private Button btnClear;
	private EditText etContent;
	private Button btnComplete;
	private Button btnAdd;
	private LinearLayout pictureLinear;
	
	private int board_id = -1;
	private boolean isUploading;
	
	private ArrayList<AttatchedPictureInfo> pictureInfos = new ArrayList<AttatchedPictureInfo>();
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		BaseFragmentActivity.onAfterPickImageListener = new OnAfterPickImageListener() {
			
			@Override
			public void onAfterPickImage(String[] sdCardPaths, Bitmap[] thumbnails) {
				
				if(thumbnails != null && thumbnails.length > 0) {
					AttatchedPictureInfo attatedPictureInfo = new AttatchedPictureInfo();
					attatedPictureInfo.setSdCardPath(sdCardPaths[0]);
					attatedPictureInfo.setThumbnail(thumbnails[0]);
					pictureInfos.add(attatedPictureInfo);
					
					addPictureView(attatedPictureInfo);
				}
			}
		};
	};
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.writeForumPage_titleBar);
		
		btnCategory = (Button) mThisView.findViewById(R.id.writeForumPage_btnCategory);
		etTitle = (EditText) mThisView.findViewById(R.id.writeForumPage_etTitle);
		btnClear = (Button) mThisView.findViewById(R.id.writeForumPage_btnClear);
		etContent = (EditText) mThisView.findViewById(R.id.writeForumPage_etContent);
		btnComplete = (Button) mThisView.findViewById(R.id.writeForumPage_btnComplete);
		btnAdd = (Button) mThisView.findViewById(R.id.writeForumPage_btnAdd);
		pictureLinear = (LinearLayout) mThisView.findViewById(R.id.writeForumPage_pictureLinear);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			
			if(getArguments().containsKey("post")) {
				this.post = (Post) getArguments().getSerializable("post");
			}
		}
	}

	@Override
	public void createPage() {
		// TODO Auto-generated method stub
	}

	@Override
	public void setListeners() {

		btnCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				int size = ForumListPage.boards.size();
				final String[] strings = new String[size];
				for(int i=0; i<size; i++) {
					strings[i] = ForumListPage.boards.get(i).getName();
				}
				
				mActivity.showSelectDialog(null, strings, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						board_id = ForumListPage.boards.get(which).getId();
						btnCategory.setText(ForumListPage.boards.get(which).getName());
						refreshPage();
					}
				});
			}
		});
		
		btnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				etTitle.setText(null);
			}
		});
		
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(pictureInfos.size() < 4) {
					mActivity.showUploadPhotoPopup(1, Color.rgb(254, 188, 42));
				} else {
					ToastUtils.showToast(R.string.uploadingImageCountError);
				}
			}
		});
		
		btnComplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(isUploading) {
					return;
				}
				
				uploadImages();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//buttonBg.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.writeForumPage_buttonBg).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(154);
		
		//btnCategory.
		rp = (RelativeLayout.LayoutParams) btnCategory.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(18);
		
		//etTitle.
		rp = (RelativeLayout.LayoutParams) etTitle.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(608);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(18);
		
		//btnClear.
		rp = (RelativeLayout.LayoutParams) btnClear.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(40);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		rp.rightMargin = ResizeUtils.getSpecificLength(2);
		
		//etContent.
		etContent.setPadding(ResizeUtils.getSpecificLength(18), ResizeUtils.getSpecificLength(24),
				ResizeUtils.getSpecificLength(18), ResizeUtils.getSpecificLength(24));
		
		//btnComplete.
		rp = (RelativeLayout.LayoutParams) btnComplete.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(132);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		rp.rightMargin = ResizeUtils.getSpecificLength(14);
		
		//writeForumPage_pictureBg
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.writeForumPage_pictureBg).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(126);
		
		//btnAdd.
		rp = (RelativeLayout.LayoutParams) btnAdd.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(60);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.leftMargin = ResizeUtils.getSpecificLength(13);
		rp.bottomMargin = ResizeUtils.getSpecificLength(13);
		
		//pictureLinear.
		rp = (RelativeLayout.LayoutParams) pictureLinear.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.leftMargin = ResizeUtils.getSpecificLength(13);
		rp.bottomMargin = ResizeUtils.getSpecificLength(13);
		
		FontUtils.setFontSize(btnCategory, 26);
		FontUtils.setFontSize(etTitle, 22);
		FontUtils.setFontSize(etContent, 22);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_write_forum;
	}

	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_writeForum;
	}

	@Override
	public int getRootViewResId() {

		return R.id.writeForumPage_mainLayout;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onResume() {
		super.onResume();
		
		mThisView.postDelayed(new Runnable() {

			@Override
			public void run() {

				SoftKeyboardUtils.showKeyboard(mContext, etTitle);
			}
		}, 500);
		
		if(post != null) {
			board_id = post.getBoard_id();
			btnCategory.setText(post.getBoard_title());
			etTitle.setText(post.getTitle());
			etContent.setText(post.getContent());
			
			if(pictureLinear.getChildCount() == 0) {
				addPicturesFromLastPost();
			}
		}
	}
	
//////////////////// Custom methods.
	
	public void addPicturesFromLastPost() {
		
		if(post.getImages() == null
				|| post.getImages().length == 0) {
			return;
		}
		
		for(int i=0; i<post.getImages().length; i++) {
			
			final int INDEX = i;
			
			AttatchedPictureInfo attatedPictureInfo = new AttatchedPictureInfo();
			attatedPictureInfo.setUrl(post.getImages()[i]);
			pictureInfos.add(attatedPictureInfo);
			
			BCPDownloadUtils.downloadBitmap(post.getImages()[i], new OnBitmapDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("from.onError." + "\nurl : " + url);
				}

				@Override
				public void onCompleted(String url, Bitmap bitmap) {

					try {
						LogUtils.log("from.onCompleted." + "\nurl : " + url);
						pictureInfos.get(INDEX).setThumbnail(bitmap);
						addPictureView(pictureInfos.get(INDEX));
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
					}
				}
			}, 60);
		}
	}
	
	public void addPictureView(final AttatchedPictureInfo attatedPictureInfo) {
		
		FrameLayout fl = new FrameLayout(mContext);
		ResizeUtils.viewResize(60, 60, fl, 1, 0, new int[]{0, 0, 16, 0});
		pictureLinear.addView(fl);
		
		final ImageView iv = new ImageView(mContext);
		iv.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		iv.setScaleType(ScaleType.CENTER_CROP);
		iv.setBackgroundColor(Color.WHITE);
		iv.setImageBitmap(attatedPictureInfo.getThumbnail());
		fl.addView(iv);
		
		View cover = new View(mContext);
		cover.setLayoutParams(new FrameLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		cover.setBackgroundResource(R.drawable.new_contents_pic_fame);
		fl.addView(cover);
		
		fl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				pictureInfos.remove(attatedPictureInfo);
				
				iv.setImageBitmap(null);
				pictureLinear.removeView(view);
				ViewUnbindHelper.unbindReferences(view);
			}
		});
	}

	public void uploadImages() {
		
		isUploading = true;
		mActivity.showLoadingView();
		
		int size = pictureInfos.size();
		for(int i=0; i<size; i++) {
			
			if(StringUtils.isEmpty(pictureInfos.get(i).getUrl())) {
				
				ToastUtils.showToast(R.string.uploadingImage);
				final int INDEX = i;
				
				OnAfterUploadImage oaui = new OnAfterUploadImage() {
					
					@Override
					public void onAfterUploadImage(String resultString) {

						try {
							pictureInfos.get(INDEX).setUrl(new JSONObject(resultString).getJSONObject("file").getString("url"));
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (Error e) {
							LogUtils.trace(e);
						}
						
						//Recursive call.
						uploadImages();
					}
				};
				
				ImageUploadUtils.uploadImage(BCPAPIs.UPLOAD_URL, oaui, pictureInfos.get(i).getSdCardPath());
				return;
			}
		}
		
		mActivity.hideLoadingView();
		
		//모든 이미지 업로드 완료.
		writeForum();
	}
	
	public void writeForum() {

		if(board_id == -1) {
			ToastUtils.showToast(R.string.selectCategory);
			isUploading = false;
			return;
		} else if(etTitle.length() == 0) {
			ToastUtils.showToast(R.string.inputTitle);
			isUploading = false;
			return;
		} else if(etContent.length() == 0) {
			ToastUtils.showToast(R.string.inputContent);
			isUploading = false;
			return;
		}
		
		/**
		 * http://byecar1.minsangk.com/posts/forum/save.json
		 * ?post[board_id]=1
		 * &post[title]=%EC%A0%9C%EB%AA%A9&
		 * &post[content]=%EB%82%B4%EC%9A%A9&
		 * &post[images][0]=http://175.126.232.36/src/20150401/fe98a93d39861c179c28f6958662e8b8.png
		 * &post[images][1]=http://175.126.232.36/src/20150401/3c337d7402adbf575cd11a569c9c9671.png
		 */
		String url = BCPAPIs.FORUM_WRITE_URL 
				+ "?post[board_id]=" + board_id
				+ "&post[title]=" + StringUtils.getUrlEncodedString(etTitle)
				+ "&post[content]=" + StringUtils.getUrlEncodedString(etContent);
		
		if(post != null) {
			url += "&post[id]=" + post.getId();
		}
		
		int size = pictureInfos.size();
		for(int i=0; i<size; i++) {
			url += "&post[images][" + i + "]=" + pictureInfos.get(i).getUrl();
		}
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WriteForumPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToWriteForum);
				isUploading = false;
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WriteForumPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_writeForum);
						SoftKeyboardUtils.hideKeyboard(mContext, etTitle);
						isUploading = false;
						
						if(post != null) {
							post.setTitle(etTitle.getText().toString());
							post.setContent(etContent.getText().toString());
							
							post.setImages(null);
							
							int size = pictureInfos.size();
							
							if(size > 0) {
								String[] images = new String[size];
								for(int i=0; i<size; i++) {
									images[i] = pictureInfos.get(i).getUrl();
								}
								
								post.setImages(images);
							}
						}
						
						refreshMainCover();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
						isUploading = false;
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					isUploading = false;
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					isUploading = false;
				}
			}
		});
	}

	public void refreshMainCover() {

		String coverUrl = BCPAPIs.MAIN_COVER_URL;
		DownloadUtils.downloadJSONString(coverUrl, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WriteForumPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WriteForumPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					try {
						MainPage.forums.clear();
						JSONArray arJSON = objJSON.getJSONArray("forum_best");
						int size = arJSON.length();
						for(int i=0; i<size; i++) {
							MainPage.forums.add(new Post(arJSON.getJSONObject(i)));
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					}
					
					if(post == null) {
						mActivity.closePageWithRefreshPreviousPage();
					} else {
						mActivity.closeTopPage();
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
}

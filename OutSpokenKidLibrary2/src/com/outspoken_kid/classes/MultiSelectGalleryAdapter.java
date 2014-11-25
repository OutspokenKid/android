package com.outspoken_kid.classes;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.outspoken_kid.R;
import com.outspoken_kid.model.MultiSelectImageInfo;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;

public class MultiSelectGalleryAdapter extends BaseAdapter {

	private ArrayList<MultiSelectImageInfo> imageInfos;
	private Context context;
	private View progressView;
	private AlphaAnimation aaIn, aaOut;
	private int loadingCount;
	
	private HashMap<String, Bitmap> latestThumbnails = new HashMap<String, Bitmap>(50) {
		
		private static final long serialVersionUID = 4993504198802439958L;

		@Override
		public Bitmap put(String key, Bitmap value) {
			
			if(containsKey(key)) {
				remove(key);
			}
			
			return super.put(key, value);
		}
	};
	
	public MultiSelectGalleryAdapter(Context context, 
			View progressView,
			ArrayList<MultiSelectImageInfo> imageInfos) {
		this.context = context;
		this.progressView = progressView;
		this.imageInfos = imageInfos;
		
		if(progressView != null) {
			aaIn = new AlphaAnimation(0, 1);
			aaIn.setDuration(200);
			
			aaOut = new AlphaAnimation(1, 0);
			aaOut.setDuration(200);
		}
	}
	
	@Override
	public int getCount() {
		return imageInfos.size();
	}

	@Override
	public MultiSelectImageInfo getItem(int position) {
		return imageInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public ArrayList<MultiSelectImageInfo> getSelected() {
		ArrayList<MultiSelectImageInfo> imageInfosT = new ArrayList<MultiSelectImageInfo>();

		for (int i = 0; i < imageInfos.size(); i++) {
			if (imageInfos.get(i).isSelected) {
				imageInfosT.add(imageInfos.get(i));
			}
		}

		return imageInfosT;
	}

	public void changeSelection(View v, int position) {

		if (imageInfos.get(position).isSelected) {
			imageInfos.get(position).isSelected = false;
		} else {
			imageInfos.get(position).isSelected = true;
		}
		
		if(v != null && v.getTag() != null) {
			setSelection((ViewHolder) v.getTag(), imageInfos
					.get(position).isSelected);
		}
	}
	
	public void setSelection(ViewHolder holder, boolean isSelected) {
		
		if(holder == null) {
			return;
		}
		
		holder.setSelected(isSelected);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder;
		
		if (convertView == null) {
			FrameLayout frame = new FrameLayout(context);
			int length = ResizeUtils.getScreenWidth()/4;
			frame.setLayoutParams(new AbsListView.LayoutParams(length, length));
			frame.setBackgroundColor(Color.WHITE);
			
			//ImageView.
			ImageView imageView = new ImageView(context);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, imageView, 2, 0, null);
			imageView.setBackgroundColor(Color.WHITE);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			frame.addView(imageView);
			
			View check = new View(context);
			ResizeUtils.viewResize(52, 52, check, 2, Gravity.RIGHT|Gravity.BOTTOM, new int[]{0, 0, 10, 10});
			check.setBackgroundResource(R.drawable.check);
			check.setVisibility(View.INVISIBLE);
			frame.addView(check);

			convertView = frame;
			
			holder = new ViewHolder();
			holder.imageView = imageView;
			holder.check = check;
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.setImage(position);
		setSelection(holder, imageInfos.get(position).isSelected);

		return convertView;
	}

	public void clear() {
		
		imageInfos.clear();
		notifyDataSetChanged();
		latestThumbnails.clear();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void executeDownloadTask(int id, ImageView imageView) {
		
		if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
			new AsyncLoadThumbnailBitmap(id, imageView)
					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		} else {
			new AsyncLoadThumbnailBitmap(id, imageView).execute();
		}
	}

	public void showProgressView() {
		
		if(progressView != null 
				&& progressView.getVisibility() != View.VISIBLE) {
			progressView.setVisibility(View.VISIBLE);
			progressView.startAnimation(aaIn);
			
			LogUtils.log("Show");
		}
	}
	
	public void hideProgressView() {
		
		if(progressView != null 
				&& progressView.getVisibility() == View.VISIBLE) {
			progressView.setVisibility(View.INVISIBLE);
			progressView.startAnimation(aaOut);
			
			LogUtils.log("Hide");
		}
	}
	
	public void checkProgressViewStatus() {
		
		if(loadingCount == 0) {
			hideProgressView();
		}
	}
	
//////////////////// Classes.
	
	public class ViewHolder {
		
		ImageView imageView;
		View check;
		
		public void setSelected(boolean isSelected) {
			
			if(isSelected) {
				ResizeUtils.setMargin(imageView, new int[]{10, 10, 10, 10});
				check.setVisibility(View.VISIBLE);
			} else {
				ResizeUtils.setMargin(imageView, new int[]{0, 0, 0, 0});
				check.setVisibility(View.INVISIBLE);
			}
		}

		public void setImage(int position) {

			if(imageView == null) {
				return;
			}

			int id = imageInfos.get(position).id;
			
			try {
				//Same image is already set.
				if(id == Integer.parseInt(imageView.getTag().toString().split("_")[0])) {
					return;
				}
			} catch (Exception e) {
			}
			
			imageView.setImageDrawable(null);
			
			String url = id + "_thumbnail";
			imageView.setTag(url);
			
			if(latestThumbnails.containsKey(url)) {
				imageView.setImageBitmap(latestThumbnails.get(url));
			} else {
				showProgressView();
				loadingCount++;
				
				DownloadUtils.loadBitmapFromMediaStore(url, true, new OnBitmapDownloadListener() {
					
					@Override
					public void onError(String url) {

						try {
							if(imageView != null) {
								String tag = imageView.getTag().toString();
								
								//태그가 다른 경우 아무 것도 하지 않음.
								if(!StringUtils.isEmpty(tag)
										&& tag.equals(url)) {
									int id = Integer.parseInt(url.split("_")[0]);
									executeDownloadTask(id, imageView);
									return;
								}
							}
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (Error e) {
							LogUtils.trace(e);
						}
						
						loadingCount--;
						checkProgressViewStatus();
					}
					
					@Override
					public void onCompleted(String url, Bitmap bitmap) {

						try {
							String tag = imageView.getTag().toString();
							
							//태그가 다른 경우 아무 것도 하지 않음.
							if(!StringUtils.isEmpty(tag)
									&& tag.equals(url)) {
								
								if(imageView != null) {
									imageView.setImageBitmap(bitmap);
									latestThumbnails.put(url, bitmap);
								}
							}
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (Error e) {
							LogUtils.trace(e);
						}
						
						loadingCount--;
						checkProgressViewStatus();
					}
				});
			}
		}
	}
	
	public class AsyncLoadThumbnailBitmap extends AsyncTask<Void, Void, Void> {

		private int id;
		private ImageView imageView;
		private Bitmap thumbnail;
		
		public AsyncLoadThumbnailBitmap(int id, ImageView imageView) {
			this.id = id;
			this.imageView = imageView;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			try {
				thumbnail = MediaStore.Images.Thumbnails.getThumbnail(
						context.getContentResolver(), 
						id, 
						MediaStore.Images.Thumbnails.MICRO_KIND, 
						null);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {

			try {
				if(imageView != null && thumbnail != null) {
					
					String url = id + "_thumbnail";
					String tag = imageView.getTag().toString();
					
					//태그가 다른 경우 아무 것도 하지 않음.
					if(!StringUtils.isEmpty(tag)
							&& tag.equals(url)) {
						imageView.setImageBitmap(thumbnail);
						latestThumbnails.put(url, thumbnail);
						DownloadUtils.getInstance().executeCacheTask(url, thumbnail);
					}
				}
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
			
			loadingCount--;
			checkProgressViewStatus();
		}
	}
}

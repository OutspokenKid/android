package com.outspoken_kid.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.outspoken_kid.classes.MultiSelectGalleryAdapter;
import com.outspoken_kid.model.MultiSelectImageInfo;

public abstract class MultiSelectGalleryActivity extends Activity {
	
	protected MultiSelectGalleryAdapter adapter;
	protected ArrayList<MultiSelectImageInfo> imageInfos = new ArrayList<MultiSelectImageInfo>();
	
	private Handler handler;
	private String action;
	private ImageLoader imageLoader;
	private OnCompleteButtonClickedListener onCompleteButtonClickedListener;

	public abstract View getNoMediaView();
	public abstract View getSelectCompleteButton();
	public abstract GridView getGridView();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		action = getIntent().getAction();
		
		if (action == null) {
			finish();
		}
		
		initImageLoader();
		init();
	}

	private void initImageLoader() {
		try {
			String CACHE_DIR = Environment.getExternalStorageDirectory()
					.getAbsolutePath() + "/.temp_tmp";
			new File(CACHE_DIR).mkdirs();

			File cacheDir = StorageUtils.getOwnCacheDirectory(getBaseContext(),
					CACHE_DIR);

			DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
					.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
					.bitmapConfig(Bitmap.Config.ARGB_8888).build();
			ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
					getBaseContext())
					.defaultDisplayImageOptions(defaultOptions)
					.discCache(new UnlimitedDiscCache(cacheDir))
					.memoryCache(new WeakMemoryCache());

			ImageLoaderConfiguration config = builder.build();
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);
		} catch (Exception e) {
		}
	}

	private void init() {

		handler = new Handler();
		adapter = new MultiSelectGalleryAdapter(getApplicationContext(), imageLoader);
		getGridView().setFastScrollEnabled(true);
		getGridView().setColumnWidth(4);
		getGridView().setCacheColorHint(Color.TRANSPARENT);
		getGridView().setOnScrollListener(
				new PauseOnScrollListener(imageLoader, true, true));
		getGridView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				adapter.changeSelection(v, position);
			}
		});
		
		getGridView().setAdapter(adapter);

		getSelectCompleteButton().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ArrayList<MultiSelectImageInfo> selected = adapter.getSelected();

				String[] allPath = new String[selected.size()];
				for (int i = 0; i < allPath.length; i++) {
					allPath[i] = selected.get(i).sdcardPath;
				}

				Intent data = new Intent().putExtra("all_path", allPath);
				setResult(RESULT_OK, data);
				
				if(onCompleteButtonClickedListener != null) {
					onCompleteButtonClickedListener.onCompleteButtonClicked();
				}
				
				finish();
			}
		});
 
		new Thread() {

			@Override
			public void run() {
				Looper.prepare();
				handler.post(new Runnable() {

					@Override
					public void run() {
						adapter.addAll(getGalleryPhotos());
						checkImageStatus();
					}
				});
				Looper.loop();
			};
		}.start();
	}

	private void checkImageStatus() {
		
		if (adapter.isEmpty()) {
			getNoMediaView().setVisibility(View.VISIBLE);
		} else {
			getNoMediaView().setVisibility(View.GONE);
		}
	}

	@SuppressWarnings("deprecation")
	private ArrayList<MultiSelectImageInfo> getGalleryPhotos() {

		try {
			final String[] columns = { MediaStore.Images.Media.DATA,
					MediaStore.Images.Media._ID };
			final String orderBy = MediaStore.Images.Media._ID;
			
			Cursor imagecursor = managedQuery(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
					null, null, orderBy);

			if (imagecursor != null && imagecursor.getCount() > 0) {

				while (imagecursor.moveToNext()) {
					MultiSelectImageInfo item = new MultiSelectImageInfo();

					int dataColumnIndex = imagecursor
							.getColumnIndex(MediaStore.Images.Media.DATA);
					item.sdcardPath = imagecursor.getString(dataColumnIndex);
					imageInfos.add(item);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// show newest photo at beginning of the list
		Collections.reverse(imageInfos);
		return imageInfos;
	}

	public void setOnCompleteButtonClickedListener(
			OnCompleteButtonClickedListener onCompleteButtonClickedListener) {
		this.onCompleteButtonClickedListener = onCompleteButtonClickedListener;
	}
	
//////////////////// Interfaces.
	
	public interface OnCompleteButtonClickedListener {
		
		public void onCompleteButtonClicked();
	}
}

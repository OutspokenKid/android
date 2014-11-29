package com.outspoken_kid.activities;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.outspoken_kid.R;
import com.outspoken_kid.classes.MultiSelectGalleryAdapter;
import com.outspoken_kid.model.MultiSelectImageInfo;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class MultiSelectGalleryActivity extends Activity {

	protected ArrayList<MultiSelectImageInfo> imageInfos = new ArrayList<MultiSelectImageInfo>();
	
	protected TextView tvTitle;
	protected TextView tvSelectComplete;
	protected GridView gridView;
	protected TextView tvNoMedia;
	protected TextView tvProgressView;
	
	protected MultiSelectGalleryAdapter adapter;
	protected int selectedImageCount;
	protected int maxImageCount = 1;
	protected int titleBgColor;
	protected int textColor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multiselectgallery);

		bindViews();
		setVariables();
		setSizes();
		setListeners();
	}
	
	public void bindViews() {
		
		tvTitle = (TextView) findViewById(R.id.multiSelectGalleryActivity_tvTitle);
		tvSelectComplete = (TextView) findViewById(R.id.multiSelectGalleryActivity_tvSelectComplete);
		gridView = (GridView) findViewById(R.id.multiSelectGalleryActivity_gridView);
		tvNoMedia = (TextView) findViewById(R.id.multiSelectGalleryActivity_tvNoMedia);
		tvProgressView = (TextView) findViewById(R.id.multiSelectGalleryActivity_tvProgressView);
	}
	
	public void setVariables() {
		
		if(getIntent() != null) {
			maxImageCount = getIntent().getIntExtra("maxImageCount", 1);
			
			titleBgColor = getIntent().getIntExtra("titleBgColor", Color.rgb(255, 63, 128));
			tvTitle.setBackgroundColor(titleBgColor);
			
			textColor = getIntent().getIntExtra("textColor", Color.WHITE);
			tvTitle.setTextColor(textColor);
		}
	}
	
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;
		
		rp = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(96);

		rp = (RelativeLayout.LayoutParams) tvSelectComplete.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(120);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.rightMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
		
		gridView.setVerticalSpacing(0);
		gridView.setHorizontalSpacing(0);
		
		FontUtils.setFontSize(tvTitle, 30);
		FontUtils.setFontSize(tvSelectComplete, 30);
		FontUtils.setFontSize(tvNoMedia, 40);
		FontUtils.setFontSize(tvProgressView, 24);
	}
	
	public void setListeners() {

		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				
				if(maxImageCount == 1) {
					Intent data = new Intent();
					Bundle bundle = new Bundle();
					bundle.putInt("size", 1);
					bundle.putSerializable("" + 0, imageInfos.get(position));
					data.putExtra("infos", bundle);
					setResult(RESULT_OK, data);
					finish();
					
				} else if(imageInfos.get(position).isSelected
						|| selectedImageCount < maxImageCount) {
					adapter.changeSelection(v, position);
					checkSelectedImageCount();
				} else {
					ToastUtils.showToast(getString(R.string.imageSelectLimit1) + 
							maxImageCount + 
							getString(R.string.imageSelectLimit2));
				}
			}
		});

		tvSelectComplete.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				ArrayList<MultiSelectImageInfo> selected = adapter.getSelected();
				Intent data = new Intent();
				Bundle bundle = new Bundle();
				int size = selected.size();
				bundle.putInt("size", size);
				
				for(int i=0; i<size; i++) {
					bundle.putSerializable("" + i, selected.get(i));
				}
				
				data.putExtra("infos", bundle);
				
				if(selectedImageCount > 0) {
					setResult(RESULT_OK, data);
				}
				
				finish();
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	
		new Handler().post(new Runnable() {
			
			@Override
			public void run() {

				getGalleryImageList();
				
				adapter = new MultiSelectGalleryAdapter(getApplicationContext(), tvProgressView, imageInfos);
				gridView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				checkSelectedImageCount();
			}
		});
	}
	
	public void getGalleryImageList() {

		Cursor cursor = null;
		
		try {
			Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

			String[] projection = {
					MediaStore.Images.Media._ID,
					MediaStore.Images.Media.DATA,
			};
		    
			cursor = getContentResolver().query(uri, projection, null, null, null);
			imageInfos.clear();
			
		    if (cursor != null) {
		    	
		    	int idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
	    		int pathColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
	    		
	    		while (cursor.moveToNext()) {
	    			MultiSelectImageInfo imageInfo = new MultiSelectImageInfo();
	    			imageInfo.id = cursor.getInt(idColumnIndex);
	    			imageInfo.sdcardPath = cursor.getString(pathColumnIndex);
	    			imageInfos.add(imageInfo);
	    		}
		    }
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		} finally {
			
			if(cursor != null) {
				cursor.close();
			}
		}
		
		if(imageInfos.size() == 0) {
			tvNoMedia.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.INVISIBLE);
			
		} else {
			Collections.reverse(imageInfos);
			tvNoMedia.setVisibility(View.INVISIBLE);
			gridView.setVisibility(View.VISIBLE);
		}
	}
	
	public void checkSelectedImageCount() {

		int count = 0;
		for(MultiSelectImageInfo info : imageInfos) {
			
			if(info.isSelected) {
				count ++;
			}
		}
		selectedImageCount = count;
		tvTitle.setText(selectedImageCount + "/" + maxImageCount);
	}
	
	@Override
	public void finish() {
		super.finish();
		adapter.clear();
	}

//////////////////// Interfaces.
	
	public interface OnAfterPickImageListener {
	
		public void onAfterPickImage(String[] sdCardPaths, Bitmap[] thumbnails);
	}
}

package com.outspoken_kid.activities;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.outspoken_kid.classes.MultiSelectGalleryAdapter;
import com.outspoken_kid.model.MultiSelectImageInfo;
import com.outspoken_kid.utils.LogUtils;

public abstract class MultiSelectGalleryActivity extends Activity {
	
	protected MultiSelectGalleryAdapter adapter;
	protected ArrayList<MultiSelectImageInfo> imageInfos = new ArrayList<MultiSelectImageInfo>();
	protected int maxImageCount;
	
	private OnCompleteButtonClickedListener onCompleteButtonClickedListener;

	public abstract View getSelectCompleteButton();
	public abstract GridView getGridView();
	public abstract void checkImageStatus(); 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if(getIntent() == null) {
			finish();
		}
		
		maxImageCount = getIntent().getIntExtra("maxImageCount", 1);
		init();
	}

	protected void init() {

		adapter = new MultiSelectGalleryAdapter(getApplicationContext(), imageInfos);
		getGridView().setAdapter(adapter);
		getGridView().setFastScrollEnabled(true);
		getGridView().setColumnWidth(4);
		getGridView().setCacheColorHint(Color.TRANSPARENT);
		getGridView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View v, int position, long id) {
				adapter.changeSelection(v, position);
			}
		});

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
		
		new Handler().post(new Runnable() {
			
			@Override
			public void run() {
				getGalleryImageList();
				adapter.notifyDataSetChanged();
				checkImageStatus();
			}
		});
	}
	
	public void getGalleryImageList() {
		
		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

		String[] projection = {
				MediaStore.Images.Media._ID,
				MediaStore.Images.Media.DATA,
		};
	    
		Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
		imageInfos.clear();
		
	    if (cursor != null) {
	    	
	    	try {
	    		int idColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
	    		int pathColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
	    		
	    		while (cursor.moveToNext()) {
	    			MultiSelectImageInfo imageInfo = new MultiSelectImageInfo();
	    			imageInfo.id = cursor.getInt(idColumnIndex);
	    			imageInfo.sdcardPath = cursor.getString(pathColumnIndex);
	    			imageInfos.add(imageInfo);
	    		}
	    	} catch(Exception e) {
	    		LogUtils.trace(e);
	    		
	    	} finally {
	    		cursor.close();
	    	}
	    }
	    
	    Collections.reverse(imageInfos);
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

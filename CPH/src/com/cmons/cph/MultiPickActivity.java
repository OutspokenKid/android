package com.cmons.cph;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.classes.CphConstants;
import com.outspoken_kid.activities.MultiSelectGalleryActivity;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;

public class MultiPickActivity extends MultiSelectGalleryActivity {

	private TextView tvTitle;
	private Button btnComplete;
	private GridView gridView;
	private TextView tvNoMedia;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_multipick);
		
		bindViews();
		setSizes();
		
		setOnCompleteButtonClickedListener(new OnCompleteButtonClickedListener() {
			
			@Override
			public void onCompleteButtonClicked() {
				SharedPrefsUtils.removeVariableFromPrefs(CphConstants.PREFS_IMAGE_UPLOAD, "uploading");
			}
		});
		
		super.onCreate(savedInstanceState);
	}
	
	public void bindViews() {
		
		tvTitle = (TextView) findViewById(R.id.multiPickActivity_tvTitle);
		btnComplete = (Button) findViewById(R.id.multiPickActivity_btnComplete);
		gridView = (GridView) findViewById(R.id.multiPickActivity_gridView);
		tvNoMedia = (TextView) findViewById(R.id.multiPickActivity_tvNoMedia);
	}
	
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		rp = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(96);

		rp = (RelativeLayout.LayoutParams) btnComplete.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(100);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.rightMargin = ResizeUtils.getSpecificLength(20);
		rp.topMargin = ResizeUtils.getSpecificLength(16);
	}
	
	@Override
	public View getNoMediaView() {
		
		return tvNoMedia;
	}

	@Override
	public View getSelectCompleteButton() {

		return btnComplete;
	}

	@Override
	public GridView getGridView() {

		return gridView;
	}
}

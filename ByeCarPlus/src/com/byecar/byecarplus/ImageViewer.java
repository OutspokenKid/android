/**
 * 
 */
package com.byecar.byecarplus;

import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.FrameLayout.LayoutParams;

import com.outspoken_kid.activities.ImageViewerActivity;

/**
 * @author HyungGunKim
 *
 */
public class ImageViewer extends ImageViewerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		findViewById(R.id.imageviewerActivity_mainLayout).setBackgroundColor(getResources().getColor(R.color.page_bg));
	}
	
	@Override
	protected LayoutParams getSaveButtonLayoutParams() {

		return null;
	}

	@Override
	protected int getSaveButtonBackgroundResId() {

		return 0;
	}
	
	@Override
	public int getCustomFontResId() {

		return 0;
	}

	@Override
	public void showAlertDialog(String title, String message, String positive,
			OnClickListener onPositive) {
	}
}

/**
 * 
 */
package com.zonecomms.clubcage;

import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.outspoken_kid.activities.ImageViewerActivity;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

/**
 * @author HyungGunKim
 *
 */
public class ImageViewer extends ImageViewerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		findViewById(R.id.imageviewerActivity_mainLayout).setBackgroundColor(Color.BLACK);
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		
		FontUtils.setGlobalFont(this, layoutResID, getString(R.string.customFont));
	}
	
	@Override
	protected LayoutParams getSaveButtonLayoutParams() {

		try {
			int width = ResizeUtils.getSpecificLength(70);
			int height = ResizeUtils.getSpecificLength(70);
			FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(width, height);
			fp.gravity = Gravity.TOP | Gravity.RIGHT;
			fp.setMargins(0, ResizeUtils.getSpecificLength(10), 
					ResizeUtils.getSpecificLength(20), 0);
			return fp;
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		return null;
	}

	@Override
	protected int getSaveButtonBackgroundResId() {

		return R.drawable.btn_save;
	}
	
	@Override
	public int getCustomFontResId() {

		return R.string.customFont;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMenuPressed() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View getLoadingView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Animation getLoadingViewAnimIn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Animation getLoadingViewAnimOut() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showAlertDialog(String title, String message, String positive,
			OnClickListener onPositive) {
		// TODO Auto-generated method stub
		
	}
}

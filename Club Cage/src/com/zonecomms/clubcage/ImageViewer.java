/**
 * 
 */
package com.zonecomms.clubcage;

import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

import com.outspoken_kid.activities.ImageViewerActivity;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

/**
 * @author HyungGunKim
 *
 */
public class ImageViewer extends ImageViewerActivity {

	@Override
	protected LayoutParams getSaveButtonLayoutParams() {

		try {

			int width = ResizeUtils.getSpecificLength(80);
			int height = ResizeUtils.getSpecificLength(50);
			FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(width, height);
			fp.gravity = Gravity.TOP | Gravity.RIGHT;
			fp.setMargins(0, ResizeUtils.getSpecificLength(10), 
					ResizeUtils.getSpecificLength(10), 0);
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
}

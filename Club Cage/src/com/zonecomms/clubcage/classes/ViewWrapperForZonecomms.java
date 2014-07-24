/**
 * 
 */
package com.zonecomms.clubcage.classes;

import android.widget.ImageView;

import com.zonecomms.common.wrapperviews.WrapperView;

/**
 * @author HyungGunKim
 *
 */
public abstract class ViewWrapperForZonecomms extends ViewWrapper {

	public ViewWrapperForZonecomms(WrapperView row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void downloadImageImmediately(String url, String key,
			ImageView ivImage, int size) {
		ImageDownloadUtils.downloadImageImmediately(url, key, ivImage, size, true);
	}
}

package com.zonecomms.common.models;

import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.clubmania.R;

import android.text.TextUtils;

public class UploadImageInfo {

	private String imageUrl;
	private int imageWidth;
	private int imageHeight;
	
	public UploadImageInfo(){}
	
	public UploadImageInfo(String resultString) {

		if(TextUtils.isEmpty(resultString) || !resultString.contains("result:OK")) {
			return;
		}
		
		String[] strs = resultString.split(",");
		
		int length = strs.length;
		for(int i=0; i<length; i++) {
			try {
				if(strs[i].contains("path:") && strs[i].indexOf("path:") == 0) {
					imageUrl = strs[i].split(":")[1];
				} else if(strs[i].contains("conv_width:")) {
					imageWidth = Integer.parseInt(strs[i].split(":")[1]);
				} else if(strs[i].contains("conv_height:")) {
					imageHeight = Integer.parseInt(strs[i].split(":")[1]);
				}
			} catch(Exception e) {
				e.printStackTrace();
				ToastUtils.showToast(R.string.failToLoadBitmap);
			}
		}
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public int getImageWidth() {
		return imageWidth;
	}
	public void setImageWidth(int imageWidth) {
		this.imageWidth = imageWidth;
	}
	public int getImageHeight() {
		return imageHeight;
	}
	public void setImageHeight(int imageHeight) {
		this.imageHeight = imageHeight;
	}
}

package com.outspoken_kid.model;

import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;

public class ImageLoadTask {
	
	public String url;
	public OnBitmapDownloadListener onBitmapDownloadListener;
	
	public ImageLoadTask(){};
	
	public ImageLoadTask(String url, OnBitmapDownloadListener onBitmapDownloadListener) {
		
		this.url = url;
		this.onBitmapDownloadListener = onBitmapDownloadListener;
	}
}
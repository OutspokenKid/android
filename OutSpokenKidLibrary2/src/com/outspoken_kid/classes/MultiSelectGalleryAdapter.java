package com.outspoken_kid.classes;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.outspoken_kid.model.MultiSelectImageInfo;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class MultiSelectGalleryAdapter extends BaseAdapter {

	private ArrayList<MultiSelectImageInfo> imageInfos = new ArrayList<MultiSelectImageInfo>();
	private Context context;
	private ImageLoader imageLoader;
	
	public MultiSelectGalleryAdapter(Context context, ImageLoader imageLoader) {
		this.context = context;
		this.imageLoader = imageLoader;
	}

	@Override
	public int getCount() {
		return imageInfos.size();
	}

	@Override
	public MultiSelectImageInfo getItem(int position) {
		return imageInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void selectAll(boolean selection) {
		for (int i = 0; i < imageInfos.size(); i++) {
			imageInfos.get(i).isSelected = selection;

		}
		notifyDataSetChanged();
	}

	public boolean isAllSelected() {
		boolean isAllSelected = true;

		for (int i = 0; i < imageInfos.size(); i++) {
			if (!imageInfos.get(i).isSelected) {
				isAllSelected = false;
				break;
			}
		}

		return isAllSelected;
	}

	public boolean isAnySelected() {
		boolean isAnySelected = false;

		for (int i = 0; i < imageInfos.size(); i++) {
			if (imageInfos.get(i).isSelected) {
				isAnySelected = true;
				break;
			}
		}

		return isAnySelected;
	}

	public ArrayList<MultiSelectImageInfo> getSelected() {
		ArrayList<MultiSelectImageInfo> imageInfosT = new ArrayList<MultiSelectImageInfo>();

		for (int i = 0; i < imageInfos.size(); i++) {
			if (imageInfos.get(i).isSelected) {
				imageInfosT.add(imageInfos.get(i));
			}
		}

		return imageInfosT;
	}

	public void addAll(ArrayList<MultiSelectImageInfo> imageInfos) {

		try {
			this.imageInfos.clear();
			this.imageInfos.addAll(imageInfos);

		} catch (Exception e) {
			e.printStackTrace();
		}

		notifyDataSetChanged();
	}

	public void changeSelection(View v, int position) {

		if (imageInfos.get(position).isSelected) {
			imageInfos.get(position).isSelected = false;
		} else {
			imageInfos.get(position).isSelected = true;
		}
		
		if(v != null && v.getTag() != null) {
			((ViewHolder) v.getTag()).setSelected(imageInfos
					.get(position).isSelected);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder holder;
		
		if (convertView == null) {
			FrameLayout frame = new FrameLayout(context);
			
			//ImageView.
			ImageView imageView = new ImageView(context);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, imageView, 2, 0, null);
			imageView.setBackgroundColor(Color.WHITE);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			frame.addView(imageView);
			
			View bg = new View(context);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, bg, 2, 0, null);
			bg.setBackgroundColor(Color.rgb(255, 63, 128));
			frame.addView(bg);
			
			convertView = frame;
			
			holder = new ViewHolder();
			holder.imageView = imageView;
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.imageView.setTag(position);

		try {
			imageLoader.displayImage("file://" + imageInfos.get(position).sdcardPath,
					holder.imageView, new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							holder.imageView.setImageDrawable(null);
							super.onLoadingStarted(imageUri, view);
						}
					});
			
			holder.setSelected(imageInfos.get(position).isSelected);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtils.trace(e);
		}

		return convertView;
	}

	public void clearCache() {
		imageLoader.clearDiscCache();
		imageLoader.clearMemoryCache();
	}

	public void clear() {
		imageInfos.clear();
		notifyDataSetChanged();
	}
	
//////////////////// Classes.
	
	public class ViewHolder {
		ImageView imageView;
		
		public void setSelected(boolean isSelected) {
			
			if(isSelected) {
				ResizeUtils.setMargin(imageView, new int[]{10, 10, 10, 10});
			} else {
				ResizeUtils.setMargin(imageView, new int[]{0, 0, 0, 0});
			}
		}
	}
}

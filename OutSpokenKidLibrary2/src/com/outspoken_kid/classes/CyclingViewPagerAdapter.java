package com.outspoken_kid.classes;

import java.util.ArrayList;

import com.outspoken_kid.utils.LogUtils;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

public abstract class CyclingViewPagerAdapter extends PagerAdapter {

	ArrayList<?> items;
	
	public CyclingViewPagerAdapter(ArrayList<?> items) {
		
		this.items = items;
	}
	
	@Override
	public int getCount() {
		
		return items.size();
	}

	public void destroyItem(ViewGroup container, int position, Object object) {

		try {
			View v = (View) object;
			container.removeView(v);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {

		return arg0 == arg1;
	}
	
	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
}

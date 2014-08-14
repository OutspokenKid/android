package com.cmons.cph.wrappers;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.Notice;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForNotice extends ViewWrapper {
	
	private Notice notice;
	
	public TextView tvNotice;
	public TextView tvRegdate;
	public View icon;
	
	public ViewWrapperForNotice(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			tvNotice = (TextView) row.findViewById(R.id.list_wholesale_notice_tvNotice);
			tvRegdate = (TextView) row.findViewById(R.id.list_wholesale_notice_tvRegedit);
			icon = row.findViewById(R.id.list_wholesale_notice_icon);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			ResizeUtils.viewResize(400, 120, tvNotice, 1, Gravity.CENTER_VERTICAL, null, new int[]{20, 0, 0, 0});
			ResizeUtils.viewResize(120, 120, tvRegdate, 1, Gravity.CENTER_VERTICAL, new int[]{0, 0, 10, 0});
			icon.getLayoutParams().width = ResizeUtils.getSpecificLength(29);
			icon.getLayoutParams().height = ResizeUtils.getSpecificLength(30);
			
			FontUtils.setFontSize(tvNotice, 28);
			FontUtils.setFontSize(tvRegdate, 20);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Notice) {
				notice = (Notice) baseModel;
				tvNotice.setText("주문요청이 들어왔습니다. 확인해주세요.");
				tvRegdate.setText("2014.08.13\nPM:11:14");
				icon.setBackgroundResource(R.drawable.mail_icon_a);
			} else {
				setUnusableView();
			}
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {
		
		if(notice != null) {
			row.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				}
			});
		}
	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}
}

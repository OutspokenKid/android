package com.zonecomms.common.wrappers;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.zonecomms.clubvera.R;
import com.zonecomms.clubvera.classes.ZonecommsApplication;
import com.zonecomms.common.models.Member;
import com.zonecomms.common.views.WrapperView;

public class ViewWrapperForMember extends ViewWrapper {
	
	private Member member;
	private ImageView ivImage;
	private TextView tvInfo;
	
	public ViewWrapperForMember(WrapperView row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			ivImage = (ImageView) row.findViewById(R.id.grid_member_ivImage);
			tvInfo = (TextView) row.findViewById(R.id.grid_member_tvInfo);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			int length = ResizeUtils.getScreenWidth()/4;
			row.setLayoutParams(new AbsListView.LayoutParams(length, length));
			
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 30,
					row.findViewById(R.id.grid_member_bg), 2, Gravity.BOTTOM, null);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 30, tvInfo, 2, Gravity.BOTTOM, null, new int[]{6, 0, 6, 0});
			
			FontUtils.setFontSize(tvInfo, 20);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Member) {
				member = (Member) baseModel;
				
				String infoString = "";
				
				if(!StringUtils.isEmpty(member.getMember_nickname())) {
					infoString += member.getMember_nickname();
				} else {
					infoString += row.getContext().getString(R.string.noname);
				}
				
				if(!StringUtils.isEmpty(member.getMember_gender()) && member.getMember_gender().equals("M")) {
					infoString += "/" + row.getContext().getString(R.string.maleInit);
				} else {
					infoString += "/" + row.getContext().getString(R.string.femaleInit);
				}
				
				if(member.getMember_age() > 0 && member.getMember_age() < 100) {
					infoString += "/" + member.getMember_age();
				}
				
				if(!StringUtils.isEmpty(infoString)) {
					tvInfo.setText(infoString);
				}
				
				setImage(ivImage, member.getMedia_src());
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
		
		if(member != null) {
			row.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(member != null && !StringUtils.isEmpty(member.getMember_id())) {
						ZonecommsApplication.getActivity().showProfilePopup(
								member.getMember_id(), member.getStatus());
					}
				}
			});
		}
	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}
}

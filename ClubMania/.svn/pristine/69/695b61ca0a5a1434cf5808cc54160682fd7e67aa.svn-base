package com.zonecomms.common.wrappers;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.classes.ApplicationManager;
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.clubmania.R;
import com.zonecomms.common.models.BaseModel;
import com.zonecomms.common.models.Member;
import com.zonecomms.common.utils.ImageDownloadUtils;
import com.zonecomms.common.wrapperviews.WrapperView;

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
			e.printStackTrace();
			setUnusableView();
		}
	}

	@Override
	public void setSize() {

		try {
			int length = ResizeUtils.getScreenWidth()/4;
			row.setLayoutParams(new AbsListView.LayoutParams(length, length));
			
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 30,
					row.findViewById(R.id.grid_member_bg), 2, Gravity.BOTTOM, null);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 30, tvInfo, 2, Gravity.BOTTOM, null, new int[]{6, 0, 6, 0});
			
			FontInfo.setFontSize(tvInfo, 20);
		} catch(Exception e) {
			e.printStackTrace();
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Member) {
				member = (Member) baseModel;
				
				String infoString = "";
				
				if(!TextUtils.isEmpty(member.getMember_nickname())) {
					infoString += member.getMember_nickname();
				} else {
					infoString += ApplicationManager.getTopFragment().getActivity().getString(R.string.noname);
				}
				
				if(!TextUtils.isEmpty(member.getMember_gender()) && member.getMember_gender().equals("M")) {
					infoString += "/" + ApplicationManager.getTopFragment().getActivity().getString(R.string.maleInit);
				} else {
					infoString += "/" + ApplicationManager.getTopFragment().getActivity().getString(R.string.femaleInit);
				}
				
				if(member.getMember_age() > 0 && member.getMember_age() < 100) {
					infoString += "/" + member.getMember_age();
				}
				
				if(!TextUtils.isEmpty(infoString)) {
					tvInfo.setText(infoString);
				}
				
				if(ivImage.getTag() == null || ivImage.getTag().toString() == null
						|| !ivImage.getTag().toString().equals(member.getMedia_src())) {
					ivImage.setVisibility(View.INVISIBLE);
				}
				
				String key = ApplicationManager.getDownloadKeyFromTopFragment();
				ImageDownloadUtils.downloadImageImmediately(member.getMedia_src(), key, ivImage, 150, true);
			} else {
				setUnusableView();
			}
		} catch(Exception e) {
			e.printStackTrace();
			setUnusableView();
		}
	}

	@Override
	public void setListener() {
		
		if(member != null) {
			row.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(member != null && !TextUtils.isEmpty(member.getMember_id())) {
						ApplicationManager.getInstance().getMainActivity().showProfilePopup(
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

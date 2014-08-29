package com.zonecomms.common.wrappers;

import java.util.ArrayList;

import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.StringUtils;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout.LayoutParams;

import com.outspoken_kid.classes.FontInfo;
import com.zonecomms.napp.classes.ApplicationManager;
import com.outspoken_kid.utils.ResizeUtils;
import com.zonecomms.common.views.WrapperView;
import com.zonecomms.common.models.Member;
import com.zonecomms.napp.MainActivity;
import com.zonecomms.napp.R;
import com.zonecomms.napp.classes.ViewWrapperForZonecomms;

public class ViewWrapperForGetheringMemberList extends ViewWrapperForZonecomms {

	private static ArrayList<Member> memberList = new ArrayList<Member>();
	
	private Member member;
	
	private View imageBg;
	private ImageView ivImage;
	private TextView tvNickname;
	private TextView tvJoin;
	private View check;

	private boolean checked;
	
	public ViewWrapperForGetheringMemberList(WrapperView row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		imageBg = row.findViewById(R.id.list_member_imageBg);
		ivImage = (ImageView) row.findViewById(R.id.list_member_ivImage);
		tvNickname = (TextView) row.findViewById(R.id.list_member_tvNickname);
		tvJoin = (TextView) row.findViewById(R.id.list_member_tvJoin);
		check = row.findViewById(R.id.list_member_check);
	}

	@Override
	public void setSizes() {

		int p = ResizeUtils.getSpecificLength(8);
		AbsListView.LayoutParams ap = new AbsListView.LayoutParams(ResizeUtils.getSpecificLength(640), 
				ResizeUtils.getSpecificLength(158));
		row.setLayoutParams(ap);
		row.setPadding(0, 0, 0, p);

		ResizeUtils.viewResize(150, 150, imageBg, 2, Gravity.CENTER_VERTICAL, null);
		ResizeUtils.viewResize(150, 150, ivImage, 2, Gravity.CENTER_VERTICAL, null);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 75, tvNickname, 2, Gravity.LEFT, new int[]{166, 0, 150, 0});
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 75, tvJoin, 2, Gravity.LEFT, new int[]{166, 75, 150, 0});
		ResizeUtils.viewResize(50, 50, check, 
				2, Gravity.CENTER_VERTICAL|Gravity.RIGHT, new int[]{0, 50, 42, 50});
		
		FontInfo.setFontSize(tvNickname, 32);
		FontInfo.setFontSize(tvJoin, 32);
	}

	@Override
	public void setListeners() {
		
		check.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setChecked(!checked);
			}
		});
	
		ivImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ApplicationManager.getInstance().getActivity()
					.showProfilePopup(member.getMember_id(), member.getStatus());
			}
		});

		row.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(member.isShownByAdmin()
						&& !member.getMember_id().equals(MainActivity.myInfo.getMember_id())) {
					setChecked(!checked);
				} else{
					ApplicationManager.getInstance().getActivity()
					.showProfilePopup(member.getMember_id(), member.getStatus());
				}
			}
		});
	}

	public static int getMembersSize() {
		
		return memberList.size();
	}
	
	public static String getMemberidString() {
		
		String nids = "";

		int size = memberList.size();
		for(int i=0; i<size; i++) {
			
			if(i != 0) {
				nids += ",";
			}
			
			nids += memberList.get(i).getMember_id();
		}
		
		return nids;
	}
	
	public static String getMemberidNicknameString() {
		
		String ids = "";
		Member member;
		
		int size = memberList.size();
		for(int i=0; i<size; i++) {
			
			if(i != 0) {
				ids += ", ";
			}
			
			member = memberList.get(i);
			ids += member.getMember_nickname() + "(" + member.getMember_id() + ")";
		}
		
		return ids;
	}
	
	public static void clearList() {
		memberList.clear();
	}

	public void setChecked(boolean checked) {

		this.checked = checked;
		
		try {
			if(checked) {
				check.setBackgroundResource(R.drawable.img_checkbox_on);
				memberList.add(member);
			} else {
				check.setBackgroundResource(R.drawable.img_checkbox_off);
				memberList.remove(member);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void setValues(com.outspoken_kid.model.BaseModel baseModel) {

		if(ivImage != null) {
			ivImage.setVisibility(View.INVISIBLE);
		}
		
		try {
			if(baseModel != null && baseModel instanceof Member) {
				member = (Member) baseModel;

				if(!StringUtils.isEmpty(member.getMember_nickname())) {
					tvNickname.setText(member.getMember_nickname());
				}
				
				if(!StringUtils.isEmpty(member.getReg_day())) {
					tvJoin.setText(member.getReg_day());
				}
				
				String key = ApplicationManager.getDownloadKeyFromTopFragment();
				setImage(ivImage, member.getMedia_src(), key, 320);
				
				if(member.isShownByAdmin()
						&& !member.getMember_id().equals(MainActivity.myInfo.getMember_id())) {
					check.setVisibility(View.VISIBLE);
					setChecked(checked);
				} else {
					check.setVisibility(View.INVISIBLE);
				}
				
			} else {
				check.setBackgroundResource(R.drawable.img_checkbox_off);
				check.setVisibility(View.INVISIBLE);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
			check.setBackgroundResource(R.drawable.img_checkbox_off);
			check.setVisibility(View.INVISIBLE);
		} catch (Error e) {
			LogUtils.trace(e);
			check.setBackgroundResource(R.drawable.img_checkbox_off);
			check.setVisibility(View.INVISIBLE);
		}
	}
}

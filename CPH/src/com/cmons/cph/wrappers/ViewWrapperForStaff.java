package com.cmons.cph.wrappers;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.ShopActivity;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.fragments.common.StaffPage;
import com.cmons.cph.models.User;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class ViewWrapperForStaff extends ViewWrapper {
	
	private User user;
	
	public View classIcon;
	public TextView textView;
	public View action;
	public View deny;
	
	public ViewWrapperForStaff(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			classIcon = row.findViewById(R.id.list_staff_class);
			textView = (TextView) row.findViewById(R.id.list_staff_textView);
			action = row.findViewById(R.id.list_staff_action);
			deny = row.findViewById(R.id.list_staff_deny);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			RelativeLayout.LayoutParams rp = null;
			
			rp = (RelativeLayout.LayoutParams) classIcon.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(30);
			rp.height = ResizeUtils.getSpecificLength(30);
			rp.leftMargin = ResizeUtils.getSpecificLength(20);
			
			rp = (RelativeLayout.LayoutParams) textView.getLayoutParams();
			rp.width = LayoutParams.MATCH_PARENT;
			rp.height = ResizeUtils.getSpecificLength(100);
			textView.setPadding(ResizeUtils.getSpecificLength(70), 0, 
					ResizeUtils.getSpecificLength(160), 0);
			FontUtils.setFontSize(textView, 30);

			rp = (RelativeLayout.LayoutParams) action.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(112);
			rp.height = ResizeUtils.getSpecificLength(56);
			rp.rightMargin = ResizeUtils.getSpecificLength(160);
			
			rp = (RelativeLayout.LayoutParams) deny.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(112);
			rp.height = ResizeUtils.getSpecificLength(56);
			rp.rightMargin = ResizeUtils.getSpecificLength(24);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof User) {

				user = (User) baseModel;
				textView.setText(user.getName() + 
						"(" + user.getId() + ") " + 
						"\n" + user.getPhone_number());
				
				if(user.getStatus() == 0) {
					deny.setVisibility(View.VISIBLE);
					
					action.setBackgroundResource(R.drawable.staff_approve_btn);
					RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) action.getLayoutParams();
					rp.rightMargin = ResizeUtils.getSpecificLength(160);
				} else {
					deny.setVisibility(View.GONE);
					
					action.setBackgroundResource(R.drawable.staff_layoff_btn);
					RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) action.getLayoutParams();
					rp.rightMargin = ResizeUtils.getSpecificLength(24);
				}
				
				switch(user.getRole()) {
				
				case 100:
				case 210:
				case 220:
					action.setVisibility(View.GONE);
					classIcon.setBackgroundResource(R.drawable.class1_icon);
					break;
				
				case 101:
				case 201:
					classIcon.setBackgroundResource(R.drawable.class2_icon);
					break;
				case 102:
					classIcon.setBackgroundResource(R.drawable.class3_icon);
					break;
				case 202:
					classIcon.setBackgroundResource(R.drawable.class4_icon);
					break;
				}
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

		action.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				final int menuIndex = ((StaffPage)ShopActivity.getInstance().getTopFragment()).getMenuIndex();
				
				int titleResId = 0;
				int messageResId = 0;
				
				if(menuIndex == 0) {
					titleResId = R.string.approval;
					messageResId = R.string.wannaApproval;
				} else {
					titleResId = R.string.fire;
					messageResId = R.string.wannaFire;
				}
				
				ShopActivity.getInstance().showAlertDialog(titleResId, messageResId, 
						R.string.confirm, R.string.cancel, 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {

								if(menuIndex == 0) {
									approval(user);
								} else {
									fire(user);
								}
							}
						}, 
						null);
			}
		});
		
		deny.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				int titleResId = R.string.deny;
				int messageResId = R.string.wannaDenyStaff;
				
				ShopActivity.getInstance().showAlertDialog(titleResId, messageResId, 
						R.string.confirm, R.string.cancel, 
						new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								
								deny(user);
							}
						}, 
						null);
			}
		});
	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}

	public void approval(User user) {

		String url = CphConstants.BASE_API_URL + "users/staffs/accept" +
				"?staff_id=" + user.getId();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("StaffPage.approval.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToApproval);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("StaffPage.approval.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_approval);
						ShopActivity.getInstance().getTopFragment().refreshPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToApproval);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToApproval);
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void fire(User user) {

		String url = CphConstants.BASE_API_URL + "users/staffs/fire" +
				"?staff_id=" + user.getId();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("StaffPage.fire.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToFire);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("StaffPage.fire.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_fire);
						ShopActivity.getInstance().getTopFragment().refreshPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToFire);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToFire);
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void deny(User user) {

		String url = CphConstants.BASE_API_URL + "users/staffs/decline" +
				"?staff_id=" + user.getId();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("StaffPage.deny.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToDeny);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("StaffPage.deny.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_deny);
						ShopActivity.getInstance().getTopFragment().refreshPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToDeny);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToDeny);
					LogUtils.trace(oom);
				}
			}
		});

	}
}

package com.cmons.classes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.cmons.cph.R;
import com.outspoken_kid.utils.LogUtils;

public abstract class BaseFragmentActivity extends FragmentActivity {

	private boolean fadePageAnim;
	
	protected abstract void bindViews();
	protected abstract void setVariables();
	protected abstract void createPage();
	protected abstract void setSizes();
	protected abstract void setListeners();
	protected abstract void downloadInfo();
	protected abstract void setPage(boolean successDownload);
	protected abstract int getXmlResId();
	protected abstract int getFragmentFrameId();
	public abstract void onRefreshPage();
	public abstract void setTitleText(String title);
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
		//Set layout xml.
		setContentView(getXmlResId());
		
		//Init application.
		CphApplication.initWithActivity(this);

		//Init activity.
		bindViews();
		setVariables();
		createPage();
		setSizes();
		setListeners();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		downloadInfo();
	}
	
	public void showAlertDialog(String title, String message,
			final OnPositiveClickedListener onPositiveClickedListener) {
		showAlertDialog(title, message, "OK", "Cancel", onPositiveClickedListener, false);
	}
	
	public void showAlertDialog(String title, String message,
			final OnPositiveClickedListener onPositiveClickedListener,
			boolean needCancel) {
		showAlertDialog(title, message, "OK", "Cancel", onPositiveClickedListener, needCancel);
	}
	
	public void showAlertDialog(String title, String message, 
			String positive, String negative,
			final OnPositiveClickedListener onPositiveClickedListener,
			boolean needCancel) {
		
		try {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle(title);
			adb.setPositiveButton(positive, 
					new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {

					if(onPositiveClickedListener != null) {
						
						try {
							onPositiveClickedListener.onPositiveClicked();
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (Error e) {
							LogUtils.trace(e);
						}
					}
				}
			});
			
			if(needCancel) {
				adb.setNegativeButton(negative, null);
				adb.setCancelable(true);
				adb.setOnCancelListener(null);
			}
			
			adb.setMessage(message);
			adb.show();
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public void startPage(BaseFragment fragment, Bundle bundle) {
		
		try {
			if(bundle != null) {
				fragment.setArguments(bundle);
			}

			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

			//Set Animation.
			if(fadePageAnim) {
				fadePageAnim = false;
				//Exclude animation when open page by slide menu.
				ft.setCustomAnimations(0, 0, 
						R.anim.slide_in_from_left, R.anim.slide_out_to_right);
				
			} else if(getSupportFragmentManager().getFragments() == null) {
				//First page doesn't have animation.
			} else {
				ft.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left,
						R.anim.slide_in_from_left, R.anim.slide_out_to_right);
			}
			
			if(getFragmentsSize() == 0) {
				ft.add(getFragmentFrameId(), fragment);
			} else {
				ft.replace(getFragmentFrameId(), fragment, fragment.getFragmentTag());
				ft.addToBackStack(fragment.getFragmentTag());
			}
			
			ft.commitAllowingStateLoss();
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public int getFragmentsSize() {
		
		try {
			//메인 실행 전.
			if(getSupportFragmentManager().getFragments() == null) {
				return 0;
				
			//메인 실행 후.
			} else {
				int entrySize = getSupportFragmentManager().getBackStackEntryCount();
				return entrySize + 1;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return 0;
	}

	public void closeTopPage() {
		
		getSupportFragmentManager().popBackStack();
	}
	
	public void clearFragmentsWithoutMain() {

		int size = getFragmentsSize();
		
		for(int i=0; i<size; i++) {
			((BaseFragment)getSupportFragmentManager().getFragments().get(i)).disableExitAnim();
		}
		
		getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
	
//////////////////////////// Interfaces.
	
	public interface OnPositiveClickedListener {
		
		public void onPositiveClicked();
	}
}

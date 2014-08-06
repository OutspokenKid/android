package com.outspoken_kid.activities;

import android.app.AlertDialog;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.outspoken_kid.classes.BaseFragment;
import com.outspoken_kid.interfaces.OutspokenActivityInterface;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;

public abstract class BaseFragmentActivity extends FragmentActivity 
		implements OutspokenActivityInterface {

	public abstract int getFragmentFrameResId();
	public abstract void setCustomAnimations(FragmentTransaction ft);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentViewId());
		
		bindViews();
		setVariables();
		createPage();
		setListeners();
		setSizes();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		downloadInfo();
	}
	
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		
		try {
			if(getCustomFontResId() != 0) {
				FontUtils.setGlobalFont(this, layoutResID, getString(getCustomFontResId()));
			}			
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

//	@Override
//	public void showLoadingView() {
//
//		if(getLoadingView() != null && getLoadingView().getVisibility() != View.VISIBLE) {
//			getLoadingView().setVisibility(View.VISIBLE);
//			
//			if(getLoadingViewAnimIn() != null) {
//				getLoadingView().startAnimation(getLoadingViewAnimIn());
//			}
//		}
//	}
//
//	@Override
//	public void hideLoadingView() {
//
//		if(getLoadingView() != null && getLoadingView().getVisibility() == View.VISIBLE) {
//			getLoadingView().setVisibility(View.INVISIBLE);
//			
//			if(getLoadingViewAnimOut() != null) {
//				getLoadingView().startAnimation(getLoadingViewAnimOut());
//			}
//		}
//	}

	@Override
	public void showAlertDialog(int title, int message, int positive,
			OnClickListener onPositive) {

		showAlertDialog(title, message, positive, 0, 
				onPositive, null);
	}
	
	@Override
	public void showAlertDialog(String title, String message, String positive,
			OnClickListener onPositive) {

		showAlertDialog(title, message, positive, null, 
				onPositive, null);
	}

	@Override
	public void showAlertDialog(int title, int message, int positive,
			int negative, OnClickListener onPositive, OnClickListener onNegative) {

		showAlertDialog(getString(title), getString(message), getString(positive), getString(negative), 
				onPositive, null);
	}
	
	@Override
	public void showAlertDialog(String title, String message, String positive,
			String negative, OnClickListener onPositive, OnClickListener onNegative) {

		try {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle(title);
			adb.setPositiveButton(positive, onPositive);
			
			if(negative != null) {
				adb.setNegativeButton(negative, onNegative);
			}
			adb.setCancelable(true);
			adb.setOnCancelListener(null);
			adb.setMessage(message);
			adb.show();
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public int getFragmentsSize() {
		
		try {
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
	
	public BaseFragment getTopFragment() {
		
		try {
			//메인 페이지 시작 전.
			if(getFragmentsSize() == 0) {
				return null;
				
			//메인 페이지.
			} else if(getFragmentsSize() == 1) {
				return (BaseFragment) getSupportFragmentManager().getFragments().get(0);
				
			//다른 최상단 페이지.
			} else {
				String fragmentTag = getSupportFragmentManager()
			    		.getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1)
			    		.getName();

			    return (BaseFragment) getSupportFragmentManager().findFragmentByTag(fragmentTag);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {	
			LogUtils.trace(e);
		}
		
		return null;
	}
	
	public void startPage(BaseFragment fragment, Bundle bundle) {
		
		try {
			if(bundle != null) {
				fragment.setArguments(bundle);
			}

			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

			//Set Animation.
			setCustomAnimations(ft);
			
			if(getFragmentsSize() == 0) {
				ft.add(getFragmentFrameResId(), fragment);
			} else {
				ft.replace(getFragmentFrameResId(), fragment, fragment.getFragmentTag());
				ft.addToBackStack(fragment.getFragmentTag());
			}
			
			ft.commitAllowingStateLoss();
			
			SoftKeyboardUtils.hideKeyboard(this, findViewById(android.R.id.content));
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public void clearFragmentsWithoutAnim() {

		int size = getFragmentsSize();	
		for(int i=0; i<size; i++) {
			try {
				((BaseFragment)getSupportFragmentManager().getFragments().get(i)).disableExitAnim(false);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
		
		getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
	
	public void clearFragments() {

		int size = getFragmentsSize();
		for(int i=0; i<size; i++) {
			try {
				if(i == 0) {
					//Do nothing.
				} else if(i == size - 1) {
					((BaseFragment)getSupportFragmentManager().getFragments().get(i)).disableExitAnim(true);
				} else {
					((BaseFragment)getSupportFragmentManager().getFragments().get(i)).disableExitAnim(false);
				}
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
		
		getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
}
package com.outspoken_kid.classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;

import com.outspoken_kid.utils.FontUtils;

public abstract class BaseActivity extends Activity 
		implements OutspokenActivityInterface {

	protected Context context;
	
	public abstract void onMenuPressed();
	public abstract void onBackPressed();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(getContentViewId());
		context = this;
		
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
		} catch (Error e) {
		}
	}
	
	@Override
	public void showAlertDialog(int title, int message, int positive,
			DialogInterface.OnClickListener onPositive) {

		showAlertDialog(title, message, positive, 0, 
				onPositive, null);
	}

	@Override
	public void showAlertDialog(String title, String message, String positive,
			DialogInterface.OnClickListener onPositive) {

		showAlertDialog(title, message, positive, null, 
				onPositive, null);
	}
	
	@Override
	public void showAlertDialog(int title, int message, int positive, 
			int negative, DialogInterface.OnClickListener onPositive,
			DialogInterface.OnClickListener onNegative) {

		showAlertDialog(getString(title), getString(message), getString(positive), 
				getString(negative), onPositive, onNegative);
	}
	
	@Override
	public void showAlertDialog(String title, String message, String positive, 
			String negative, DialogInterface.OnClickListener onPositive,
			DialogInterface.OnClickListener onNegative) {

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
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN) {

			switch (keyCode) {

			case KeyEvent.KEYCODE_MENU:

				try {
					onMenuPressed();
				} catch (Exception e) {
				}
				break;

			case KeyEvent.KEYCODE_BACK:

				try {
					onBackPressed();
				} catch (Exception e) {
				}
				break;

			default:
				return super.onKeyDown(keyCode, event);
			}
		}
		return true;
	}

}

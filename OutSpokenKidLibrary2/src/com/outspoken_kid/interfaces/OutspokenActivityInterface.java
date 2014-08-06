package com.outspoken_kid.interfaces;

import android.content.DialogInterface;
import android.view.View;
import android.view.animation.Animation;

public interface OutspokenActivityInterface {

	/**
	 * Bind views from xml.
	 */
	public void bindViews();
	
	/**
	 * Set variables from getIntent() or getArgument().
	 */
	public void setVariables();
	
	/**
	 * Set Views.
	 */
	public void createPage();
	
	/**
	 * Set listeners.
	 */
	public void setListeners();
	
	/**
	 * Set sizes.
	 */
	public void setSizes();
	
	/**
	 * Download information.
	 */
	public void downloadInfo();
	
	/**
	 * Call after downloadInfo().
	 * 
	 * @param successDownload
	 */
	public void setPage(boolean successDownload);
	
	/**
	 * Get Layout xml file's resource id.
	 * 
	 * @return Layout resource id.
	 */
	public int getContentViewId();
	
	/**
	 * Get Font file's resource id.
	 * 
	 * @return Font resource id.
	 */
	public int getCustomFontResId();
	
	/**
	 * Create or get loading view.
	 * 
	 * @return Loading view instance.
	 */
	public View getLoadingView();
	
	/**
	 * Show loading view.
	 */
	public void showLoadingView();
	
	/**
	 * Hide loading view.
	 */
	public void hideLoadingView();
	
	/**
	 * Create or get loading view animation in.
	 * 
	 * @return In animation.
	 */
	public Animation getLoadingViewAnimIn();
	
	/**
	 * Create or get loading view animation out.
	 * 
	 * @return Out animation.
	 */
	public Animation getLoadingViewAnimOut();

	/**
	 * Show alert dialog.
	 * 
	 * @param title title string resId.
	 * @param message message string resId.
	 * @param positive postive string resId.
	 * @param onPositiveClickedListener
	 */
	public void showAlertDialog(int title, int message, 
			int positive, 
			DialogInterface.OnClickListener onPositive);
	
	/**
	 * Show alert dialog.
	 * 
	 * @param title title string.
	 * @param message message string.
	 * @param positive postive string.
	 * @param onPositiveClickedListener
	 */
	public void showAlertDialog(String title, String message, 
			String positive, 
			DialogInterface.OnClickListener onPositive);
	
	/**
	 * Show alert dialog.
	 * 
	 * @param title title string resId.
	 * @param message message string resId.
	 * @param positive postive string resId.
	 * @param negative negative string resId.
	 * @param onPositiveClickedListener
	 * @param onNegativeClickedListener
	 */
	public void showAlertDialog(int title, int message, int positive, 
			int negative, DialogInterface.OnClickListener onPositive,
			DialogInterface.OnClickListener onNegative);
	
	/**
	 * Show alert dialog.
	 * 
	 * @param title title string.
	 * @param message message string.
	 * @param positive postive string.
	 * @param negative negative string.
	 * @param onPositiveClickedListener
	 * @param onNegativeClickedListener
	 */
	public void showAlertDialog(String title, String message, String positive, 
			String negative, DialogInterface.OnClickListener onPositive,
			DialogInterface.OnClickListener onNegative);
}

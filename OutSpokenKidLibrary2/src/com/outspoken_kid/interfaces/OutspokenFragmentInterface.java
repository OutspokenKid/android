package com.outspoken_kid.interfaces;


public interface OutspokenFragmentInterface {

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
	 * Refresh page.
	 */
	public void refreshPage();
	
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
	 * Get fragment tag.
	 * 
	 * @return Fragment tag.
	 */
	public String getFragmentTag();
	
	/**
	 * Disable exit anim. Using for ClearFragmentWithOutAnim() in BaseFragmentActivity.
	 * 
	 * @param isLastPage If true, don't disable exit anim.
	 */
	public void disableExitAnim(boolean isLastPage);
	
	/**
	 * Get animation for exit last page.
	 * 
	 * @return Animation resource id.
	 */
	public int getLastPageAnimResId();
}

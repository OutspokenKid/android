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
	
	public String getFragmentTag();
	
	public void disableExitAnim(boolean isLastPage);
	
	public int getLastPageAnimResId();
}

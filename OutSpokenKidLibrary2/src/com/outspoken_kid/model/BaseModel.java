/**
 * 
 */
package com.outspoken_kid.model;

/**
 * @author HyungGunKim
 *
 */
public abstract class BaseModel {
	
	private int indexno;
	private int itemCode;
	private boolean needClickListener = true;

	public int getIndexno() {
		return indexno;
	}
	public void setIndexno(int indexno) {
		this.indexno = indexno;
	}
	public int getItemCode() {
		return itemCode;
	}
	public void setItemCode(int itemCode) {
		this.itemCode = itemCode;
	}
	public boolean isNeedClickListener() {
		return needClickListener;
	}
	public void setNeedClickListener(boolean needClickListener) {
		this.needClickListener = needClickListener;
	}
}

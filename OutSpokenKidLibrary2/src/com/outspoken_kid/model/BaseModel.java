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
}

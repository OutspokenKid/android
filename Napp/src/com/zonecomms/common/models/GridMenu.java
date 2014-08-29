package com.zonecomms.common.models;

public class GridMenu {

	protected String text;
	protected boolean group;
	protected int s_cate_id;
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isGroup() {
		return group;
	}
	public void setGroup(boolean group) {
		this.group = group;
	}
	public int getS_cate_id() {
		return s_cate_id;
	}
	public void setS_cate_id(int s_cate_id) {
		this.s_cate_id = s_cate_id;
	}
}

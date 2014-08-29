package com.zonecomms.common.models;

import java.util.ArrayList;

public class GridMenuGroup extends GridMenu {

	private int color;
	private ArrayList<GridMenu> gridMenus = new ArrayList<GridMenu>();

	public GridMenuGroup(int color, String text) {
		this.setColor(color);
		this.text = text;
		
		setGroup(true);
	}
	
	public ArrayList<GridMenu> getGridMenus() {
		return gridMenus;
	}
	public void setGridMenus(ArrayList<GridMenu> gridMenus) {
		this.gridMenus = gridMenus;
	}
	public void addMenu(String text, int s_cate_id) {
		
		GridMenu menu = new GridMenu();
		menu.setText(text);
		menu.setS_cate_id(s_cate_id);
		getGridMenus().add(menu);
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}
}

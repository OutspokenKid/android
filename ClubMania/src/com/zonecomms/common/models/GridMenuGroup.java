package com.zonecomms.common.models;

import java.util.ArrayList;

public class GridMenuGroup extends GridMenu {

	private ArrayList<GridMenu> gridMenus = new ArrayList<GridMenu>();

	public GridMenuGroup() {
		isGroup = true;
	}
	
	public ArrayList<GridMenu> getGridMenus() {
		return gridMenus;
	}
	public void setGridMenus(ArrayList<GridMenu> gridMenus) {
		this.gridMenus = gridMenus;
	}
}

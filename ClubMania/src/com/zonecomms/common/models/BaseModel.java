package com.zonecomms.common.models;

import org.json.JSONObject;

public class BaseModel {

	protected String apiUrl;
	protected String reg_dt;
	protected int itemCode;
	protected int indexno;
	
	public BaseModel(){};
	
	public BaseModel(JSONObject objJSON) {
		
		if(objJSON != null) {
			
			try {
				if(objJSON.has("reg_dt")) {
					reg_dt = objJSON.getString("reg_dt");
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void copyData(BaseModel newModel) {
		
		if(newModel != null) {
			try {
				apiUrl = newModel.getApiUrl();
				reg_dt = newModel.getReg_dt();
				itemCode = newModel.getItemCode();
				indexno = newModel.getIndexno();
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public String getApiUrl() {
		return apiUrl;
	}
	
	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

	public int getItemCode() {
		return itemCode;
	}

	public void setItemCode(int itemCode) {
		this.itemCode = itemCode;
	}

	public int getIndexno() {
		return indexno;
	}

	public void setIndexno(int indexno) {
		this.indexno = indexno;
	}
}

package com.byecar.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class BCPBaseModel extends BaseModel {

	protected long priority;

	public BCPBaseModel() {
		
	}
	
	public BCPBaseModel(JSONObject objJSON) {
	
		try {
			if(objJSON.has("priority")) {
				this.priority = objJSON.getLong("priority");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public long getPriority() {
		return priority;
	}

	public void setPriority(long priority) {
		this.priority = priority;
	}
}

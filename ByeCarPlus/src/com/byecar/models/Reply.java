package com.byecar.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Reply extends BaseModel {

	public Reply() {
		
	}
	
	public Reply(JSONObject objJSON) {
		
		try {
			
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}

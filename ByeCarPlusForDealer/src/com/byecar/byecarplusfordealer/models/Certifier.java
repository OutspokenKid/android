package com.byecar.byecarplusfordealer.models;

import java.io.Serializable;

import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class Certifier extends User implements Serializable {

	private static final long serialVersionUID = -4996527772553786965L;
	
	/*
"id": "48",
"role": "999",
"email": "abc@def.com",
"profile_img_url": "http://byecar.minsangk.com/images/20150108/36ad7a0657d0fa0433e49f0c28cfffb7.png",
"nickname": "쿠라킴",
"name": "김구라",
"phone_number": "010-1234-5678",
"address": "인천시 연수구 송도동",
	"desc": "학시리 검증해드립니다잉",
"dealer_id": "0",
"status": "1",
"blocked_until": "0",
"to_get_pushed": "1",
"created_at": "1420696795"
	*/
	
	private String desc;
	
	public Certifier() {
		
	}
	
	public Certifier(JSONObject objJSON) {
		
		super(objJSON);
		
		try {
			if(objJSON.has("desc")) {
				this.desc = objJSON.getString("desc");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public String getDesc() {
		
		return desc;
	}
	
	public void setDesc(String desc) {
		
		this.desc = desc;
	}
}

package com.example.androidvolleytest;

import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class Post {

	//프로필.
	public String profileUrl;
	
	//닉네임.
	public String nickname;
	
	//작성시간.
	public String regdate;
	
	//본문.
	public String content;
	
	//이미지.
	public String imageUrl;
	
	//댓글 수.
	public int reply_cnt;
	
	public Post(JSONObject objJSON) {
		
		try {
			profileUrl = objJSON.getJSONObject("member").getString("media_src");
			nickname = objJSON.getJSONObject("member").getString("member_nickname");
			regdate = objJSON.getString("reg_dt");
			content = objJSON.getString("content");
			imageUrl = objJSON.getString("media_src");
			reply_cnt = objJSON.getInt("reply_cnt");
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}

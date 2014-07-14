/**
 * 
 */
package com.zonecomms.common.models;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

/**
 * @author HyungGunKim
 *
 */
public class Article extends BaseModel {

	private ContentObject[] content;
	private Member member;
	private int[] m_cate_id;
	private String media_src;
	private int spot_nid;
	private String title;
	private String reg_dt;
	
	public Article() {}
	
	public Article(JSONObject objJSON) {
		
		try {
			if(objJSON.has("content")) {
				JSONArray arJSON = objJSON.getJSONArray("content");
				
				int size = arJSON.length();
				content = new ContentObject[size]; 
				for(int i=0; i<size; i++) {
					content[i] = new ContentObject(arJSON.getJSONObject(i));
				}
			}
			
			if(objJSON.has("m_cate_id")) {
				
				JSONArray arJSON = objJSON.getJSONArray("m_cate_id");
				
				int size = arJSON.length();
				m_cate_id = new int[size];
				for(int i=0; i<size; i++) {
					m_cate_id[i] = arJSON.getInt(i);
				}
			}
			
			if(objJSON.has("media_src")) {
				this.media_src = objJSON.getString("media_src");
			}
			
			if(objJSON.has("member")) {
				this.member = new Member(objJSON.getJSONObject("member"));
			}
			
			if(objJSON.has("spot_nid")) {
				this.spot_nid = objJSON.getInt("spot_nid");
				setIndexno(spot_nid);
			}
			
			if(objJSON.has("title")) {
				this.title = objJSON.getString("title");
			}
			
			if(objJSON.has("reg_dt")) {
				this.reg_dt = objJSON.getString("reg_dt");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public ContentObject[] getContent() {
		return content;
	}

	public void setContent(ContentObject[] content) {
		this.content = content;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public int[] getM_cate_id() {
		return m_cate_id;
	}

	public void setM_cate_id(int[] m_cate_id) {
		this.m_cate_id = m_cate_id;
	}

	public String getMedia_src() {
		return media_src;
	}

	public void setMedia_src(String media_src) {
		this.media_src = media_src;
	}

	public int getSpot_nid() {
		return spot_nid;
	}

	public void setSpot_nid(int spot_nid) {
		this.spot_nid = spot_nid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

	public class ContentObject {

		public String type;
		public String data;
		public String thumbnail;
		
		public ContentObject() {
		}
		
		public ContentObject(JSONObject objContent) {
			
			try {
				if(objContent.has("type")) {
					this.type = objContent.getString("type");
				}
				
				if(objContent.has("data")) {
					this.data = objContent.getString("data");
				}
				
				if(objContent.has("thumbnail")) {
					this.thumbnail = objContent.getString("thumbnail");
				}
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
	}
}

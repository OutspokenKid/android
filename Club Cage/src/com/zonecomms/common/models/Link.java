package com.zonecomms.common.models;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;

public class Link extends BaseModel {

	/**
	 * 1 : Photo
	 * 2 : Video
	 * 3 : Music
	 */
	private int link_type;
	private String title;
	/**
	 * Image(On photo).
	 * Code for youtube(On video), Use "http://www.youtube.com/watch?v=(code)"
	 * Music's url(On music).
	 */
	private String link_data;
	private String[] link_datas;
	private int media_nid;
	private int[] media_nids;
	private String[] thumbnails;
	
	/**
	 * Artist(On music).
	 */
	private String name;
	
	/**
	 * Thumbnail image(On music).
	 */
	private String main_image;
	private int link_nid;
	private String reg_id;
	private String reg_dt;
	
	public Link() {}
	
	public Link(JSONObject objJSON) {
		
		try {
			if(objJSON.has("link_type")) {
				this.link_type = objJSON.getInt("link_type");
			}
			
			if(objJSON.has("title")) {
				this.title = objJSON.getString("title");
			}
			
			if(objJSON.has("link_datas")) {
				JSONArray arJSON = objJSON.getJSONArray("link_datas");
				
				if(arJSON != null && arJSON.length() > 0) {
					int length = arJSON.length();
					this.link_datas = new String[length];
					
					for(int i=0; i<length; i++) {
						this.link_datas[i] = arJSON.getString(i);
					}
				}
			}
			
			if(objJSON.has("link_data")) {
				this.link_data = objJSON.getString("link_data");
			}
			
			if(objJSON.has("media_nids")) {
				JSONArray arJSON = objJSON.getJSONArray("media_nids");
				
				if(arJSON != null && arJSON.length() > 0) {
					int length = arJSON.length();
					this.media_nids = new int[length];
					
					for(int i=0; i<length; i++) {
						this.media_nids[i] = arJSON.getInt(i);
					}
				}
			}
			
			if(objJSON.has("thumbnail")) {
				JSONArray arJSON = objJSON.getJSONArray("thumbnail");
				
				if(arJSON != null && arJSON.length() > 0) {
					int length = arJSON.length();
					this.thumbnails = new String[length];
					
					for(int i=0; i<length; i++) {
						this.thumbnails[i] = arJSON.getString(i);
					}
				}
			}
			
			if(objJSON.has("link_data")) {
				this.link_data = objJSON.getString("link_data");
			}
			
			if(objJSON.has("name")) {
				this.name = objJSON.getString("name");
			}
			
			if(objJSON.has("link_nid")) {
				this.link_nid = objJSON.getInt("link_nid");
				setIndexno(link_nid);
			}
			
			if(objJSON.has("reg_id")) {
				this.reg_id = objJSON.getString("reg_id");
			}
			
			if(objJSON.has("reg_dt")) {
				this.reg_dt = objJSON.getString("reg_dt");
			}
			
			if(objJSON.has("main_image")) {
				this.main_image = objJSON.getString("main_image");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public int getLink_type() {
		return link_type;
	}

	public void setLink_type(int link_type) {
		this.link_type = link_type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getLink_data() {
		return link_data;
	}

	public void setLink_data(String link_data) {
		this.link_data = link_data;
	}

	public String[] getLink_datas() {
		return link_datas;
	}

	public void setLink_datas(String[] link_datas) {
		this.link_datas = link_datas;
	}

	public int getMedia_nid() {
		return media_nid;
	}

	public void setMedia_nid(int media_nid) {
		this.media_nid = media_nid;
	}
	
	public int[] getMedia_nids() {
		return media_nids;
	}

	public void setMedia_nids(int[] media_nids) {
		this.media_nids = media_nids;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getLink_nid() {
		return link_nid;
	}

	public void setLink_nid(int link_nid) {
		this.link_nid = link_nid;
	}

	public String getReg_id() {
		return reg_id;
	}

	public void setReg_id(String reg_id) {
		this.reg_id = reg_id;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}

	public String getMain_image() {
		return main_image;
	}

	public void setMain_image(String main_image) {
		this.main_image = main_image;
	}

	public String[] getThumbnails() {
		return thumbnails;
	}

	public void setThumbnails(String[] thumbnails) {
		this.thumbnails = thumbnails;
	}
}

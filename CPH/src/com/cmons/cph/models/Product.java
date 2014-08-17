package com.cmons.cph.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Product extends BaseModel {

	private int id;
	private int category_id;
	private int wholesale_id;
	private String name;
	private String colors;
	private String sizes;
	private String mixture_rate;
	private String desc;
	private int need_push;
	private String created_at;
	private String updated_at;
	private int favorited_cnt;
	private int order_cnt;
	
	public Product() {
	}
	
	public Product(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("category_id")) {
				this.category_id = objJSON.getInt("category_id");
			}
			
			if(objJSON.has("wholesale_id")) {
				this.wholesale_id = objJSON.getInt("wholesale_id");
			}
			
			if(objJSON.has("name")) {
				this.name = objJSON.getString("name");
			}
			
			if(objJSON.has("colors")) {
				this.colors = objJSON.getString("colors");
			}
			
			if(objJSON.has("sizes")) {
				this.sizes = objJSON.getString("sizes");
			}
			
			if(objJSON.has("mixture_rate")) {
				this.mixture_rate = objJSON.getString("mixture_rate");
			}
			
			if(objJSON.has("desc")) {
				this.desc = objJSON.getString("desc");
			}
			
			if(objJSON.has("need_push")) {
				this.need_push = objJSON.getInt("need_push");
			}
			
			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getString("created_at");
			}
			
			if(objJSON.has("updated_at")) {
				this.updated_at = objJSON.getString("updated_at");
			}
			
			if(objJSON.has("favorited_cnt")) {
				this.favorited_cnt = objJSON.getInt("favorited_cnt");
			}
			
			if(objJSON.has("order_cnt")) {
				this.order_cnt = objJSON.getInt("order_cnt");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCategory_id() {
		return category_id;
	}
	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}
	public int getWholesale_id() {
		return wholesale_id;
	}
	public void setWholesale_id(int wholesale_id) {
		this.wholesale_id = wholesale_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColors() {
		return colors;
	}
	public void setColors(String colors) {
		this.colors = colors;
	}
	public String getSizes() {
		return sizes;
	}
	public void setSizes(String sizes) {
		this.sizes = sizes;
	}
	public String getMixture_rate() {
		return mixture_rate;
	}
	public void setMixture_rate(String mixture_rate) {
		this.mixture_rate = mixture_rate;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getNeed_push() {
		return need_push;
	}
	public void setNeed_push(int need_push) {
		this.need_push = need_push;
	}
	public String getCreated_at() {
		return created_at;
	}
	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}
	public String getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(String updated_at) {
		this.updated_at = updated_at;
	}
	public int getFavorited_cnt() {
		return favorited_cnt;
	}
	public void setFavorited_cnt(int favorited_cnt) {
		this.favorited_cnt = favorited_cnt;
	}
	public int getOrder_cnt() {
		return order_cnt;
	}
	public void setOrder_cnt(int order_cnt) {
		this.order_cnt = order_cnt;
	}
}

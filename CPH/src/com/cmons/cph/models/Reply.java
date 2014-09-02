package com.cmons.cph.models;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Reply extends BaseModel implements Serializable {

	private static final long serialVersionUID = 7669482845243054028L;
	
	private int id;
	private String type;
	private String author_id;
	private String title;
	private String content;
	private int need_push;
	private int wholesale_id;
	private int retail_id;
	private long created_at;
	private int product_id;
	private int parent_id;
	private int is_private;
	private String parent_author_id;
	private int hits_cnt;
	private String retail_name;
	private String retail_phone_number;
	private int retail_type;
	private Reply[] replies;
	
	public Reply() {
	}
	
	public Reply(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("type")) {
				this.type = objJSON.getString("type");
			}
			
			if(objJSON.has("author_id")) {
				this.author_id = objJSON.getString("author_id");
			}
			
			if(objJSON.has("title")) {
				this.title = objJSON.getString("title");
			}
			
			if(objJSON.has("content")) {
				this.content = objJSON.getString("content");
			}
			
			if(objJSON.has("need_push")) {
				this.need_push = objJSON.getInt("need_push");
			}
			
			if(objJSON.has("wholesale_id")) {
				this.wholesale_id = objJSON.getInt("wholesale_id");
			}
			
			if(objJSON.has("retail_id")) {
				this.retail_id = objJSON.getInt("retail_id");
			}
			
			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getLong("created_at");
			}
			
			if(objJSON.has("product_id")) {
				this.product_id = objJSON.getInt("product_id");
			}
			
			if(objJSON.has("parent_id")) {
				this.parent_id = objJSON.getInt("parent_id");
			}
			
			if(objJSON.has("is_private")) {
				this.is_private = objJSON.getInt("is_private");
			}
			
			if(objJSON.has("parent_author_id")) {
				this.parent_author_id = objJSON.getString("parent_author_id");
			}
			
			if(objJSON.has("hits_cnt")) {
				this.hits_cnt = objJSON.getInt("hits_cnt");
			}
			
			if(objJSON.has("retail_name")) {
				this.retail_name = objJSON.getString("retail_name");
			}
			
			if(objJSON.has("retail_phone_number")) {
				this.retail_phone_number = objJSON.getString("retail_phone_number");
			}
			
			if(objJSON.has("retail_type")) {
				this.retail_type = objJSON.getInt("retail_type");
			}
			
			if(objJSON.has("replies")) {
				
				JSONArray arJSON = objJSON.getJSONArray("replies");
				int size = arJSON.length();
				replies = new Reply[size];
				
				for(int i=0; i<size; i++) {
					replies[i] = new Reply(arJSON.getJSONObject(i));
				}
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(String author_id) {
		this.author_id = author_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getNeed_push() {
		return need_push;
	}

	public void setNeed_push(int need_push) {
		this.need_push = need_push;
	}

	public int getWholesale_id() {
		return wholesale_id;
	}

	public void setWholesale_id(int wholesale_id) {
		this.wholesale_id = wholesale_id;
	}

	public int getRetail_id() {
		return retail_id;
	}

	public void setRetail_id(int retail_id) {
		this.retail_id = retail_id;
	}

	public long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public int getIs_private() {
		return is_private;
	}

	public void setIs_private(int is_private) {
		this.is_private = is_private;
	}

	public String getParent_author_id() {
		return parent_author_id;
	}

	public void setParent_author_id(String parent_author_id) {
		this.parent_author_id = parent_author_id;
	}

	public int getHits_cnt() {
		return hits_cnt;
	}

	public void setHits_cnt(int hits_cnt) {
		this.hits_cnt = hits_cnt;
	}

	public String getRetail_name() {
		return retail_name;
	}

	public void setRetail_name(String retail_name) {
		this.retail_name = retail_name;
	}

	public String getRetail_phone_number() {
		return retail_phone_number;
	}

	public void setRetail_phone_number(String retail_phone_number) {
		this.retail_phone_number = retail_phone_number;
	}

	public int getRetail_type() {
		return retail_type;
	}

	public void setRetail_type(int retail_type) {
		this.retail_type = retail_type;
	}

	public Reply[] getReplies() {
		return replies;
	}

	public void setReplies(Reply[] replies) {
		this.replies = replies;
	}
}

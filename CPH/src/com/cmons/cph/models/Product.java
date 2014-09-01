package com.cmons.cph.models;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Product extends BaseModel implements Serializable {

	private static final long serialVersionUID = 8159757973801962712L;
	
	public static final int TYPE_WHOLESALE = 0;
	public static final int TYPE_RETAIL = 1;
	
	private int id;
	private int category_id;
	private int wholesale_id;
	private String name;
	private int price;
	private String colors;
	private String sizes;
	private String mixture_rate;
	private String desc;
	private int need_push;
	private String created_at;
	private String updated_at;
	private int favorited_cnt;
	private int ordered_cnt;
	private int purchased_cnt;
	private int customers_only;
	private int status;
	private String[] product_images;
	
	private int type;
	
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

			if(objJSON.has("price")) {
				this.price = objJSON.getInt("price");
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
			
			if(objJSON.has("ordered_cnt")) {
				this.ordered_cnt = objJSON.getInt("ordered_cnt");
			}
			
			if(objJSON.has("purchased_cnt")) {
				this.purchased_cnt = objJSON.getInt("purchased_cnt");
			}
			
			if(objJSON.has("customers_only")) {
				this.customers_only = objJSON.getInt("customers_only");
			}
			
			if(objJSON.has("status")) {
				this.status = objJSON.getInt("status");
			}
			
			if(objJSON.has("product_images")) {
				
				JSONArray arJSON = objJSON.getJSONArray("product_images");
				int size = arJSON.length();
				product_images = new String[size];
				for(int i=0; i<size; i++) {
					product_images[i] = arJSON.getString(i);
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
	public int getOrdered_cnt() {
		return ordered_cnt;
	}
	public void setOrdered_cnt(int ordered_cnt) {
		this.ordered_cnt = ordered_cnt;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getPurchased_cnt() {
		return purchased_cnt;
	}

	public void setPurchased_cnt(int purchased_cnt) {
		this.purchased_cnt = purchased_cnt;
	}

	public int getCustomers_only() {
		return customers_only;
	}

	public void setCustomers_only(int customers_only) {
		this.customers_only = customers_only;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String[] getProduct_images() {
		return product_images;
	}

	public void setProduct_images(String[] product_images) {
		this.product_images = product_images;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}

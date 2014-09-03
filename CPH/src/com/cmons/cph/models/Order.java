package com.cmons.cph.models;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Order extends BaseModel {
	
	//-1: 주문취소, 0: 주문요청, 1: 입금대기, 2: 입금완료, 3: 거래완료
	public static final int STATUS_CANCELED = -1;
	public static final int STATUS_REQUESTED = 0;
	public static final int STATUS_STANDBY = 1;
	public static final int STATUS_DEPOSIT = 2;
	public static final int STATUS_COMPLETED = 3;
	
	private int id;
	private int retail_id;
	private int product_id;
	private int wholesale_id;
	private String size;
	private String color;
	private int amount;
	private int status;
	private int payment_type;
	private int payment_account_id;
	private String payment_purchaser_info;
	private long created_at;
	private String product_name;
	private long product_price;
	
	private boolean checked = true;
	
	public Order() {
	}
	
	public Order(JSONObject objJSON) {
	
		try {
			
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("retail_id")) {
				this.retail_id = objJSON.getInt("retail_id");
			}
			
			if(objJSON.has("product_id")) {
				this.product_id = objJSON.getInt("product_id");
			}
			
			if(objJSON.has("wholesale_id")) {
				this.wholesale_id = objJSON.getInt("wholesale_id");
			}
			
			if(objJSON.has("size")) {
				this.size = objJSON.getString("size");
			}
			
			if(objJSON.has("color")) {
				this.color = objJSON.getString("color");
			}
			
			if(objJSON.has("amount")) {
				this.amount = objJSON.getInt("amount");
			}
			
			if(objJSON.has("status")) {
				this.status = objJSON.getInt("status");
			}

			if(objJSON.has("payment_type")) {
				this.payment_type = objJSON.getInt("payment_type");
			}
			
			if(objJSON.has("payment_account_id")) {
				this.payment_account_id = objJSON.getInt("payment_account_id");
			}
			
			if(objJSON.has("payment_purchaser_info")) {
				this.payment_purchaser_info = objJSON.getString("payment_purchaser_info");
			}
			
			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getLong("created_at");
			}
			
			if(objJSON.has("product_name")) {
				this.product_name = objJSON.getString("product_name");
			}
			
			if(objJSON.has("product_price")) {
				this.product_price = objJSON.getLong("product_price");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getRetail_id() {
		return retail_id;
	}

	public void setRetail_id(int retail_id) {
		this.retail_id = retail_id;
	}

	public int getProduct_id() {
		return product_id;
	}

	public void setProduct_id(int product_id) {
		this.product_id = product_id;
	}

	public int getWholesale_id() {
		return wholesale_id;
	}

	public void setWholesale_id(int wholesale_id) {
		this.wholesale_id = wholesale_id;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(int payment_type) {
		this.payment_type = payment_type;
	}

	public int getPayment_account_id() {
		return payment_account_id;
	}

	public void setPayment_account_id(int payment_account_id) {
		this.payment_account_id = payment_account_id;
	}

	public String getPayment_purchaser_info() {
		return payment_purchaser_info;
	}

	public void setPayment_purchaser_info(String payment_purchaser_info) {
		this.payment_purchaser_info = payment_purchaser_info;
	}

	public long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public long getProduct_price() {
		return product_price;
	}

	public void setProduct_price(long product_price) {
		this.product_price = product_price;
	}
}

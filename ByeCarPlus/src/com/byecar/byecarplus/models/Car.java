package com.byecar.byecarplus.models;

import java.io.Serializable;

import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Car extends BaseModel implements Serializable {

	private static final long serialVersionUID = 6978779511721704607L;
	
	public static final int TYPE_AUCTION = 1;
	public static final int TYPE_DEALER = 2;
	
	//0: 입찰대기, 10: 입찰중, 20: 입찰완료, 30: 거래완료
	public static final int STAND_BY = 0;
	public static final int BIDDING = 10;
	public static final int BID_COMPLETE = 20;
	public static final int TRADE_COMPLETE = 30;
	
	private int id;
	private int type;
	private int car_id;
	private int year;
	private String car_number;
	private String color;
	private int mileage;
	private int displacement;
	private String fuel_type;
	private String transmission_type;
	private int had_accident;
	private int is_oneman_owned;
	private String vehicle_id_number;
	private long price;
	private int manager_id;
	private String desc;
	private int seller_id;
	private String rep_img_url;
	private int status;
	private long bid_until_at;
	private String area;
	private long created_at;
	private String brand_name;
	private String model_name;
	private String car_full_name;
	private int bids_cnt;
	
	public Car() {
		
	}
	
	public Car(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
				setIndexno(id);
			}
			
			if(objJSON.has("type")) {
				this.type = objJSON.getInt("type");
			}
			
			if(objJSON.has("car_id")) {
				this.car_id = objJSON.getInt("car_id");
			}
			
			if(objJSON.has("year")) {
				this.year = objJSON.getInt("year");
			}
			
			if(objJSON.has("car_number")) {
				this.car_number = objJSON.getString("car_number");
			}
			
			if(objJSON.has("color")) {
				this.color = objJSON.getString("color");
			}
			
			if(objJSON.has("mileage")) {
				this.mileage = objJSON.getInt("mileage");
			}
			
			if(objJSON.has("displacement")) {
				this.displacement = objJSON.getInt("displacement");
			}
			
			if(objJSON.has("fuel_type")) {
				this.fuel_type = objJSON.getString("fuel_type");
			}
			
			if(objJSON.has("transmission_type")) {
				this.transmission_type = objJSON.getString("transmission_type");
			}
			
			if(objJSON.has("had_accident")) {
				this.had_accident = objJSON.getInt("had_accident");
			}
			
			if(objJSON.has("is_oneman_owned")) {
				this.is_oneman_owned = objJSON.getInt("is_oneman_owned");
			}

			if(objJSON.has("vehicle_id_number")) {
				this.vehicle_id_number = objJSON.getString("vehicle_id_number");
			}
			
			if(objJSON.has("price")) {
				this.price = objJSON.getLong("price");
			}
			
			if(objJSON.has("manager_id")) {
				this.manager_id = objJSON.getInt("manager_id");
			}
			
			if(objJSON.has("desc")) {
				this.desc = objJSON.getString("desc");
			}
			
			if(objJSON.has("seller_id")) {
				this.seller_id = objJSON.getInt("seller_id");
			}
			
			if(objJSON.has("rep_img_url")) {
				this.rep_img_url = objJSON.getString("rep_img_url");
			}
			
			if(objJSON.has("status")) {
				this.status = objJSON.getInt("status");
			}
			
			if(objJSON.has("bid_until_at")) {
				this.bid_until_at = objJSON.getLong("bid_until_at");
			}
			
			if(objJSON.has("area")) {
				this.area = objJSON.getString("area");
			}
			
			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getLong("created_at");
			}
			
			if(objJSON.has("brand_name")) {
				this.brand_name = objJSON.getString("brand_name");
			}
			
			if(objJSON.has("model_name")) {
				this.model_name = objJSON.getString("model_name");
			}
			
			if(objJSON.has("car_full_name")) {
				this.car_full_name = objJSON.getString("car_full_name");
			}
			
			if(objJSON.has("bids_cnt")) {
				this.bids_cnt = objJSON.getInt("bids_cnt");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getCar_id() {
		return car_id;
	}

	public void setCar_id(int car_id) {
		this.car_id = car_id;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getCar_number() {
		return car_number;
	}

	public void setCar_number(String car_number) {
		this.car_number = car_number;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public int getMileage() {
		return mileage;
	}

	public void setMileage(int mileage) {
		this.mileage = mileage;
	}

	public int getDisplacement() {
		return displacement;
	}

	public void setDisplacement(int displacement) {
		this.displacement = displacement;
	}

	public String getFuel_type() {
		return fuel_type;
	}

	public void setFuel_type(String fuel_type) {
		this.fuel_type = fuel_type;
	}

	public String getTransmission_type() {
		return transmission_type;
	}

	public void setTransmission_type(String transmission_type) {
		this.transmission_type = transmission_type;
	}

	public int getHad_accident() {
		return had_accident;
	}

	public void setHad_accident(int had_accident) {
		this.had_accident = had_accident;
	}

	public int getIs_oneman_owned() {
		return is_oneman_owned;
	}

	public void setIs_oneman_owned(int is_oneman_owned) {
		this.is_oneman_owned = is_oneman_owned;
	}

	public String getVehicle_id_number() {
		return vehicle_id_number;
	}

	public void setVehicle_id_number(String vehicle_id_number) {
		this.vehicle_id_number = vehicle_id_number;
	}

	public long getPrice() {
		return price;
	}

	public void setPrice(long price) {
		this.price = price;
	}

	public int getManager_id() {
		return manager_id;
	}

	public void setManager_id(int manager_id) {
		this.manager_id = manager_id;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(int seller_id) {
		this.seller_id = seller_id;
	}

	public String getRep_img_url() {
		return rep_img_url;
	}

	public void setRep_img_url(String rep_img_url) {
		this.rep_img_url = rep_img_url;
	}

	public int getStatus() {
		return status;
	}

	//0: 입찰대기, 10: 입찰중, 20: 입찰완료, 30: 거래완료
	public void setStatus(int status) {
		this.status = status;
	}

	public long getBid_until_at() {
		return bid_until_at;
	}

	public void setBid_until_at(long bid_until_at) {
		this.bid_until_at = bid_until_at;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}

	public String getBrand_name() {
		return brand_name;
	}

	public void setBrand_name(String brand_name) {
		this.brand_name = brand_name;
	}

	public String getModel_name() {
		return model_name;
	}

	public void setModel_name(String model_name) {
		this.model_name = model_name;
	}

	public String getCar_full_name() {
		return car_full_name;
	}

	public void setCar_full_name(String car_full_name) {
		this.car_full_name = car_full_name;
	}

	public int getBids_cnt() {
		return bids_cnt;
	}

	public void setBids_cnt(int bids_cnt) {
		this.bids_cnt = bids_cnt;
	}
}

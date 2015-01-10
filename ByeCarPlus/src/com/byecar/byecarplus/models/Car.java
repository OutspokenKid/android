package com.byecar.byecarplus.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;

import com.byecar.byecarplus.R;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Car extends BaseModel implements Serializable {

	private static final long serialVersionUID = 6978779511721704607L;
	
	public static final int TYPE_BID = 1;
	public static final int TYPE_DEALER = 2;
	public static final int TYPE_DIRECT_CERTIFIED = 3;
	public static final int TYPE_DIRECT_NORMAL = 4;
	
	//0: 입찰대기, 10: 입찰중, 20: 입찰완료, 30: 거래완료
	public static final int STAND_BY = 0;
	public static final int BIDDING = 10;
	public static final int BID_COMPLETE = 20;
	public static final int TRADE_COMPLETE = 30;
	
	private int id;
	private int type;
	private int car_id;
	private int year;
	private String color;
	private String car_number;
	private int mileage;
	private int displacement;
	private String fuel_type;
	private String transmission_type;
	private int had_accident;
	private int is_oneman_owned;
	private long price;
	private int manager_id;
	private String desc;
	private int seller_id;
	private String rep_img_url;
	private int status;
	private long bid_begin_at;
	private long bid_until_at;
	private int bids_cnt;
	private String area;
	private int to_sell_directly;
	private long end_at;
	private int dealer_id;
	private long created_at;
	private String brand_name;
	private String model_name;
	private String trim_name;
	private String car_full_name;
	
	private String seller_name;
	private String seller_phone_number;
	private String seller_address;
	private String manager_name;
	
	private String[] m_images;
	private String[] a_images;
	private int[] options;
	private ArrayList<Bid> bids = new ArrayList<Bid>();
	private ArrayList<String> images = new ArrayList<String>();

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
			
			if(objJSON.has("bid_begin_at")) {
				this.bid_begin_at = objJSON.getLong("bid_begin_at");
			}

			
			if(objJSON.has("bid_until_at")) {
				this.bid_until_at = objJSON.getLong("bid_until_at");
			}
			
			if(objJSON.has("area")) {
				this.area = objJSON.getString("area");
			}
			
			if(objJSON.has("to_sell_directly")) {
				this.to_sell_directly = objJSON.getInt("to_sell_directly");
			}
			
			if(objJSON.has("end_at")) {
				this.end_at = objJSON.getLong("end_at");
			}
			
			if(objJSON.has("dealer_id")) {
				this.dealer_id = objJSON.getInt("dealer_id");
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
			
			if(objJSON.has("trim_name")) {
				this.trim_name = objJSON.getString("trim_name");
			}
			
			if(objJSON.has("car_full_name")) {
				this.car_full_name = objJSON.getString("car_full_name");
			}
			
			if(objJSON.has("bids_cnt")) {
				this.bids_cnt = objJSON.getInt("bids_cnt");
			}
			
			if(objJSON.has("seller_name")) {
				this.seller_name = objJSON.getString("seller_name");
			}
			
			if(objJSON.has("seller_phone_number")) {
				this.seller_phone_number = objJSON.getString("seller_phone_number");
			}
			
			if(objJSON.has("seller_address")) {
				this.seller_address = objJSON.getString("seller_address");
			}
			
			if(objJSON.has("manager_name")) {
				this.manager_name = objJSON.getString("manager_name");
			}
			
			if(objJSON.has("m_images")) {
				
				JSONArray arJSON = objJSON.getJSONArray("m_images");
				
				int size = arJSON.length();
				this.m_images = new String[size];
				for(int i=0; i<size; i++) {
					m_images[i] = arJSON.getString(i);
					getImages().add(m_images[i]);
				}
			}
			
			if(objJSON.has("a_images")) {
				
				JSONArray arJSON = objJSON.getJSONArray("a_images");
				
				int size = arJSON.length();
				this.a_images = new String[size];
				for(int i=0; i<size; i++) {
					a_images[i] = arJSON.getString(i);
					getImages().add(a_images[i]);
				}
			}
			
			if(objJSON.has("options")) {
				JSONArray arJSON = objJSON.getJSONArray("options");
				
				int size = arJSON.length();
				options = new int[size];
				for(int i=0; i<size; i++) {
					options[i] = arJSON.getInt(i);
				}
			}
			
			if(objJSON.has("bids")) {
				JSONArray arJSON = objJSON.getJSONArray("bids");
				
				int size = arJSON.length();
				for(int i=0; i<size; i++) {
					bids.add(new Bid(arJSON.getJSONObject(i)));
				}
			}
			
		} catch (Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public static String getFuelTypeString(Context context, String origin) {
		
		if(context == null || origin == null) {
			return null;
		} else {
			if(origin.equals("gasoline")) {
				return context.getString(R.string.carSearchString_fuel1);
			} else if(origin.equals("diesel")) {
				return context.getString(R.string.carSearchString_fuel2);
			} else {
				return context.getString(R.string.carSearchString_fuel3);
			}
		}
	}

	public static String getFuelOriginString(Context context, String text) {
		
		if(context == null || text == null) {
			return null;
		} else {
			
			if(text.equals(context.getString(R.string.carSearchString_fuel1))) {
				return "gasoline";
			} else if(text.equals(context.getString(R.string.carSearchString_fuel2))) {
				return "diesel";
			} else {
				return "lpg";
			}
		}
	}
	
	public void setFuelTypeString(Context context, String text) {
		
		if(context == null || text == null) {
			return;
		} else {
			
			if(text.equals(context.getString(R.string.carSearchString_fuel1))) {
				setFuel_type("gasoline");
			} else if(text.equals(context.getString(R.string.carSearchString_fuel2))) {
				setFuel_type("diesel");
			} else {
				setFuel_type("lpg");
			}
		}
	}
	
	public static String getTransmissionTypeString(Context context, String origin) {
		
		if(context == null || origin == null) {
			return null;
		} else {
			if(origin.equals("auto")) {
				return context.getString(R.string.carSearchString_transmission1);
			} else {
				return context.getString(R.string.carSearchString_transmission2);
			}
		}
	}
	
	public static String getTransmissionOriginString(Context context, String text) {
		
		if(context == null || text == null) {
			return null;
		} else {
			
			if(text.equals(context.getString(R.string.carSearchString_transmission1))) {
				return "auto";
			} else {
				return "manual";
			}
		}
	}
	
	public void setTransmissionTypeString(Context context, String text) {
		
		if(context == null || text == null) {
			return;
		} else {
			
			if(text.equals(context.getString(R.string.carSearchString_transmission1))) {
				setTransmission_type("auto");
			} else {
				setTransmission_type("manual");
			}
		}
	}
	
	public static String getAccidentTypeString(Context context, int type) {
		
		if(context == null) {
			return null;
		} else {

			switch(type) {
			
			case 0:
				return context.getString(R.string.carSearchString_accident3);
				
			case 1:
				return context.getString(R.string.carSearchString_accident2);
				
				default:
					return context.getString(R.string.carSearchString_accident1);
			}
		}
	}
	
	public static String getAccidentOriginString(Context context, String text) {
		
		if(context == null || text == null) {
			return null;
		} else {
			
			if(text.equals(context.getString(R.string.carSearchString_accident3))) {
				return "0";
			} else if(text.equals(context.getString(R.string.carSearchString_accident2))) {
				return "1";
			} else {
				return "2";
			}
		}
	}
	
	public void setAccidentType(Context context, String text) {
		
		if(context == null || text == null) {
			return;
		} else {
			
			if(text.equals(context.getString(R.string.carSearchString_accident3))) {
				setHad_accident(0);
			} else if(text.equals(context.getString(R.string.carSearchString_accident2))) {
				setHad_accident(1);
			} else {
				setHad_accident(2);
			}
		}
	}
	
	public static String getOneManOwnedTypeString(Context context, int type) {

		if(context == null) {
			return null;
		} else {

			if(type == 0) {
				return context.getString(R.string.carSearchString_oneManOwned2);
			} else {
				return context.getString(R.string.carSearchString_oneManOwned1);
			}
		}
	}
	
	public static String getOneManOwnedOriginString(Context context, String text) {
		
		if(context == null || text == null) {
			return null;
		} else {
			
			if(text.equals(context.getString(R.string.carSearchString_oneManOwned2))) {
				return "0";
			} else {
				return "1";
			}
		}
	}
	
	public void setOneManOwnedType(Context context, String text) {
		
		if(context == null || text == null) {
			return;
		} else {
			
			if(text.equals(context.getString(R.string.carSearchString_oneManOwned2))) {
				setIs_oneman_owned(0);
			} else {
				setIs_oneman_owned(1);
			}
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

	public long getBid_begin_at() {
		return bid_begin_at;
	}

	public void setBid_begin_at(long bid_begin_at) {
		this.bid_begin_at = bid_begin_at;
	}

	public int getTo_sell_directly() {
		return to_sell_directly;
	}

	public void setTo_sell_directly(int to_sell_directly) {
		this.to_sell_directly = to_sell_directly;
	}

	public long getEnd_at() {
		return end_at;
	}

	public void setEnd_at(long end_at) {
		this.end_at = end_at;
	}

	public String getSeller_phone_number() {
		return seller_phone_number;
	}

	public void setSeller_phone_number(String seller_phone_number) {
		this.seller_phone_number = seller_phone_number;
	}

	public String getSeller_address() {
		return seller_address;
	}

	public void setSeller_address(String seller_address) {
		this.seller_address = seller_address;
	}

	public String getManager_name() {
		return manager_name;
	}

	public void setManager_name(String manager_name) {
		this.manager_name = manager_name;
	}

	public String[] getM_images() {
		return m_images;
	}

	public void setM_images(String[] m_images) {
		this.m_images = m_images;
	}

	public String[] getA_images() {
		return a_images;
	}

	public void setA_images(String[] a_images) {
		this.a_images = a_images;
	}

	public ArrayList<Bid> getBids() {
		return bids;
	}

	public void setBids(ArrayList<Bid> bids) {
		this.bids = bids;
	}

	public ArrayList<String> getImages() {
		return images;
	}

	public void setImages(ArrayList<String> images) {
		this.images = images;
	}

	public int[] getOptions() {
		return options;
	}

	public void setOptions(int[] options) {
		this.options = options;
	}

	public int getDealer_id() {
		return dealer_id;
	}

	public void setDealer_id(int dealer_id) {
		this.dealer_id = dealer_id;
	}

	public String getTrim_name() {
		return trim_name;
	}

	public void setTrim_name(String trim_name) {
		this.trim_name = trim_name;
	}

	public String getSeller_name() {
		return seller_name;
	}

	public void setSeller_name(String seller_name) {
		this.seller_name = seller_name;
	}
}

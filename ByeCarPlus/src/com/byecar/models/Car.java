package com.byecar.models;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class Car extends BCPBaseModel implements Serializable {

	private static final long serialVersionUID = 6978779511721704607L;
	
	public static final int TYPE_BID = 1;
	public static final int TYPE_DEALER = 2;
	public static final int TYPE_DIRECT = 4;
	
	//0: 승인대기, 5 : 입찰대기, 10: 입찰중, 15: 입찰종료, 20: 낙찰, 21: 유찰, 30: 결제 완료, 40 : 거래 완료.
	public static final int STATUS_STAND_BY_APPROVAL = 0;
	public static final int STATUS_STAND_BY_BIDING = 5;
	public static final int STATUS_BIDDING = 10;
	public static final int STATUS_BID_COMPLETE = 15;
	public static final int STATUS_BID_SUCCESS = 20;
	public static final int STATUS_BID_FAIL = 21;
	public static final int STATUS_PAYMENT_COMPLETED = 30;
	public static final int STATUS_TRADE_COMPLETE = 40;
	
	private int id;
	private int type;
	private int car_id;
	private int year;
	private int month;
	private String color;
	private String car_number;
	private int mileage;
	private int displacement;
	private String fuel_type;
	private String transmission_type;
	private int had_accident;
	private int is_oneman_owned;
	private long price;
	private String desc;
	private int seller_id;
	private String rep_img_url;
	private int status;
	private long bid_begin_at;
	private long bid_until_at;
	private int bids_cnt;
	private int bidders_cnt;
	private String area;
	private int to_sell_directly;
	private long end_at;

	private int dealer_id;
	private String dealer_name;
	private String dealer_phone_number;
	private String dealer_address;
	private String dealer_profile_img_url;
	private String dealer_company;
	private int dealer_level;

	private int manager_id;
	private String manager_name;
	private String manager_desc;
	private String manager_profile_img_url;
	
	private long created_at;
	private String brand_name;
	private String model_name;
	private String trim_name;
	private String car_full_name;
	
	private String seller_name;
	private String seller_nickname;
	private String seller_phone_number;
	private String seller_address;
	private String seller_profile_img_url;
	
	private String[] m_images;
	private String[] a_images;
	private int[] options;
	private ArrayList<Bid> bids = new ArrayList<Bid>();
	private ArrayList<String> images = new ArrayList<String>();
	
	private int is_liked;
	private int likes_cnt;
	private int has_purchased;
	private int has_review;
	private String car_wd;
	
	private Review review;
	
	private String inspection_note_url;
	private long liked_at;
	private int dong_id;
	private String accident_desc;
	
	private long my_bid_price;
	private int my_bid_ranking;
	private long max_bid_price;
	
	private int has_carhistory;
	private long desired_price;

	public void copyValuesFromNewItem(Car newCar) {
		
		if(newCar == null) {
			this.id = 0;
			this.type = 0;
			this.car_id = 0;
			this.year = 0;
			this.month = 0;
			this.color = null;
			this.car_number = null;
			this.mileage = 0;
			this.displacement = 0;
			this.fuel_type = null;
			this.transmission_type = null;
			this.had_accident = 0;
			this.is_oneman_owned = 0;
			this.price = 0;
			this.desc = null;
			this.seller_id = 0;
			this.rep_img_url = null;
			this.status = 0;
			this.bid_begin_at = 0;
			this.bid_until_at = 0;
			this.bids_cnt = 0;
			this.bidders_cnt = 0;
			this.area = null;
			this.to_sell_directly = 0;
			this.end_at = 0;
			
			this.dealer_id = 0;
			this.dealer_name = null;
			this.dealer_phone_number = null;
			this.dealer_address = null;
			this.dealer_profile_img_url = null;
			this.dealer_company = null;
			this.dealer_level = 0;

			this.manager_id = 0;
			this.manager_name = null;
			this.manager_desc = null;
			this.manager_profile_img_url = null;
			
			this.created_at = 0;
			this.brand_name = null;
			this.model_name = null;
			this.trim_name = null;
			this.car_full_name = null;
			
			this.seller_name = null;
			this.seller_nickname = null;
			this.seller_phone_number = null;
			this.seller_address = null;
			this.seller_profile_img_url = null;
			
			this.m_images = null;
			this.a_images = null;
			this.options = null;
			this.bids = null;
			this.images = null;
			
			this.is_liked = 0;
			this.likes_cnt = 0;
			this.has_purchased = 0;
			this.has_review = 0;
			this.car_wd = null;
			
			this.review = null;
			
			this.inspection_note_url = null;
			this.liked_at = 0;
			this.dong_id = 0;
			this.accident_desc = null;
			
			this.my_bid_price = 0;
			this.my_bid_ranking = 0;
			this.max_bid_price = 0;
			
			this.has_carhistory = 0;
			this.desired_price = 0;
		} else {
			this.id = newCar.id;
			this.type = newCar.type;
			this.car_id = newCar.car_id;
			this.year = newCar.year;
			this.month = newCar.month;
			this.color = newCar.color;
			this.car_number = newCar.car_number;
			this.mileage = newCar.mileage;
			this.displacement = newCar.displacement;
			this.fuel_type = newCar.fuel_type;
			this.transmission_type = newCar.transmission_type;
			this.had_accident = newCar.had_accident;
			this.is_oneman_owned = newCar.is_oneman_owned;
			this.price = newCar.price;
			this.desc = newCar.desc;
			this.seller_id = newCar.seller_id;
			this.rep_img_url = newCar.rep_img_url;
			this.status = newCar.status;
			this.bid_begin_at = newCar.bid_begin_at;
			this.bid_until_at = newCar.bid_until_at;
			this.bids_cnt = newCar.bids_cnt;
			this.bidders_cnt = newCar.bidders_cnt;
			this.area = newCar.area;
			this.to_sell_directly = newCar.to_sell_directly;
			this.end_at = newCar.end_at;
			
			this.dealer_id = newCar.dealer_id;
			this.dealer_name = newCar.dealer_name;
			this.dealer_phone_number = newCar.dealer_phone_number;
			this.dealer_address = newCar.dealer_address;
			this.dealer_profile_img_url = newCar.dealer_profile_img_url;
			this.dealer_company = newCar.dealer_company;
			this.dealer_level = newCar.dealer_level;

			this.manager_id = newCar.manager_id;
			this.manager_name = newCar.manager_name;
			this.manager_desc = newCar.manager_desc;
			this.manager_profile_img_url = newCar.manager_profile_img_url;
			
			this.created_at = newCar.created_at;
			this.brand_name = newCar.brand_name;
			this.model_name = newCar.model_name;
			this.trim_name = newCar.trim_name;
			this.car_full_name = newCar.car_full_name;
			
			this.seller_name = newCar.seller_name;
			this.seller_nickname = newCar.seller_nickname;
			this.seller_phone_number = newCar.seller_phone_number;
			this.seller_address = newCar.seller_address;
			this.seller_profile_img_url = newCar.seller_profile_img_url;
			
			this.m_images = newCar.m_images;
			this.a_images = newCar.a_images;
			this.options = newCar.options;
			this.bids = newCar.bids;
			this.images = newCar.images;
			
			this.is_liked = newCar.is_liked;
			this.likes_cnt = newCar.likes_cnt;
			this.has_purchased = newCar.has_purchased;
			this.has_review = newCar.has_review;
			this.car_wd = newCar.car_wd;
			
			this.review = newCar.review;
			
			this.inspection_note_url = newCar.inspection_note_url;
			this.liked_at = newCar.liked_at;
			this.dong_id = newCar.dong_id;
			this.accident_desc = newCar.accident_desc;
			
			this.my_bid_price = newCar.my_bid_price;
			this.my_bid_ranking = newCar.my_bid_ranking;
			this.max_bid_price = newCar.max_bid_price;
			
			this.has_carhistory = newCar.has_carhistory;
			this.desired_price = newCar.desired_price;
		}
	}
	
	public Car() {
		
	}
	
	public Car(JSONObject objJSON) {
		
		super(objJSON);
		
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
			
			if(objJSON.has("month")) {
				this.month = objJSON.getInt("month");
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
			
			if(objJSON.has("desc")) {
				this.desc = objJSON.getString("desc");
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
			
			if(objJSON.has("bidders_cnt")) {
				this.bidders_cnt = objJSON.getInt("bidders_cnt");
			}
			
			if(objJSON.has("seller_id")) {
				this.seller_id = objJSON.getInt("seller_id");
			}
			
			if(seller_id != 0) {
				
				if(objJSON.has("seller_name")) {
					this.seller_name = objJSON.getString("seller_name");
				}
				
				if(objJSON.has("seller_nickname")) {
					this.seller_nickname = objJSON.getString("seller_nickname");
				}
				
				if(objJSON.has("seller_phone_number")) {
					this.seller_phone_number = objJSON.getString("seller_phone_number");
				}
				
				if(objJSON.has("seller_address")) {
					this.seller_address = objJSON.getString("seller_address");
				}
				
				if(objJSON.has("seller_profile_img_url")) {
					this.seller_profile_img_url = objJSON.getString("seller_profile_img_url");
				}
			}

			if(objJSON.has("dealer_id")) {
				this.dealer_id = objJSON.getInt("dealer_id");
			}

			if(dealer_id != 0) {
				
				if(objJSON.has("dealer_name")) {
					this.dealer_name = objJSON.getString("dealer_name");
				}
				
				if(objJSON.has("dealer_phone_number")) {
					this.dealer_phone_number = objJSON.getString("dealer_phone_number");
				}
				
				if(objJSON.has("dealer_address")) {
					this.dealer_address = objJSON.getString("dealer_address");
				}
				
				if(objJSON.has("dealer_profile_img_url")) {
					this.dealer_profile_img_url = objJSON.getString("dealer_profile_img_url");
				}
				
				if(objJSON.has("dealer_company")) {
					this.dealer_company = objJSON.getString("dealer_company");
				}
				
				if(objJSON.has("dealer_level")) {
					this.dealer_level = objJSON.getInt("dealer_level");
				}
			}
			
			if(objJSON.has("manager_id")) {
				this.manager_id = objJSON.getInt("manager_id");
			}
			
			if(manager_id != 0) {
				
				if(objJSON.has("manager_name")) {
					this.manager_name = objJSON.getString("manager_name");
				}
				
				if(objJSON.has("manager_desc")) {
					this.manager_desc = objJSON.getString("manager_desc");
				}
				
				if(objJSON.has("manager_profile_img_url")) {
					this.manager_profile_img_url = objJSON.getString("manager_profile_img_url");
				}
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
					try {
						bids.add(new Bid(arJSON.getJSONObject(i)));
					} catch (Exception e) {
					}
				}
			}
			
			if(objJSON.has("is_liked")) {
				this.is_liked = objJSON.getInt("is_liked");
			}
			
			if(objJSON.has("likes_cnt")) {
				this.likes_cnt = objJSON.getInt("likes_cnt");
			}
			
			if(objJSON.has("has_purchased")) {
				this.has_purchased = objJSON.getInt("has_purchased");
			}
			
			if(objJSON.has("has_review")) {
				this.has_review = objJSON.getInt("has_review");
			}
			
			if(objJSON.has("car_wd")) {
				this.car_wd = objJSON.getString("car_wd");
			}
			
			try {
				if(objJSON.has("review")) {
					this.review = new Review(objJSON.getJSONObject("review"));
				}
			} catch (Exception e) {
			}
			
			if(objJSON.has("inspection_note_url")) {
				this.inspection_note_url = objJSON.getString("inspection_note_url");
			}
			
			if(objJSON.has("liked_at")) {
				this.liked_at = objJSON.getLong("liked_at");
			}
			
			if(objJSON.has("dong_id")) {
				this.dong_id = objJSON.getInt("dong_id");
			}
			
			if(objJSON.has("accident_desc")) {
				this.accident_desc = objJSON.getString("accident_desc");
			}
			
			if(objJSON.has("my_bid_price")) {
				this.my_bid_price = objJSON.getLong("my_bid_price");
			}
			
			if(objJSON.has("my_bid_ranking")) {
				this.my_bid_ranking = objJSON.getInt("my_bid_ranking");
			}
			
			if(objJSON.has("max_bid_price")) {
				this.max_bid_price = objJSON.getLong("max_bid_price");
			}
			
			if(objJSON.has("has_carhistory")) {
				this.has_carhistory = objJSON.getInt("has_carhistory");
			}
			
			if(objJSON.has("desired_price")) {
				this.desired_price = objJSON.getLong("desired_price");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public static int getFuelIndex(String fuel) {

		if("gasoline".equals(fuel)) {
			return 0;
		} else if("diesel".equals(fuel)) {
			return 1;
		} else if("lpg".equals(fuel)) {
			return 2;
		} else {
			return 3;
		}
	}
	
	public static String getFuelString_ToServer(int index) {
		
		switch(index) {
		case 0:
			return "gasoline";
			
		case 1:
			return "diesel";
			
		case 2:
			return "lpg";
			
		case 3:
			return "lpgd";
		
		}
		
		return null;
	}
	
	public static String getFuelString_ToUser(String fuel) {
		
		if("gasoline".equals(fuel)) {
			return "휘발유";
		} else if("diesel".equals(fuel)) {
			return "디젤";
		} else if("lpg".equals(fuel)) {
			return "LPG 일반";
		} else {
			return "LPG 장애";
		}
	}
	
	public static int getAccidentIndexToUser(int serverIndex) {
		
		switch(serverIndex) {
		
		//사고여부 모름.
		case 0:
			return 2;
			
		//유사고.
		case 1:
			return 1;
			
		//무사고.
		case 2:
			return 0;
		}
		
		return 0;
	}
	
	public static int getAccidentIndexToServer(int userIndex) {
		
		switch(userIndex) {

		//무사고.
		case 0:
			return 2;
		
		//유사고.
		case 1:
			return 1;
			
		//사고여부 모름.
		case 2:
			return 0;
		}
		
		return 0;
	}
	
	public static int getOwnedIndexToUser(int serverIndex) {
		
		switch(serverIndex) {
		
		//1인 신조 아님.
		case 0:
			return 1;
			
		//1인 신조.
		case 1:
			return 0;
		}
		
		return 0;
	}
	
	public static int getOwnedIndexToServer(int userIndex) {
		
		switch(userIndex) {

		//1인 신조.
		case 0:
			return 1;
		
		//1인 신조 아님.
		case 1:
			return 0;
		}
		
		return 0;
	}

	public static int getTransmissionIndex(String transmission) {
		
		if("auto".equals(transmission)) {
			return 0;
		} else {
			return 1;
		}
	}
	
	public static String getTransmissionSring_ToServer(int index) {
		
		if(index == 0) {
			return "auto";
		} else {
			return "manual";
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

	//0: 승인대기, 5 : 입찰대기, 10: 입찰중, 15: 입찰종료, 20: 낙찰, 21: 유찰, 30: 거래완료
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

	public int getIs_liked() {
		return is_liked;
	}

	public void setIs_liked(int is_liked) {
		this.is_liked = is_liked;
	}

	public int getLikes_cnt() {
		return likes_cnt;
	}

	public void setLikes_cnt(int likes_cnt) {
		this.likes_cnt = likes_cnt;
	}

	public String getDealer_name() {
		return dealer_name;
	}

	public void setDealer_name(String dealer_name) {
		this.dealer_name = dealer_name;
	}

	public String getDealer_phone_number() {
		return dealer_phone_number;
	}

	public void setDealer_phone_number(String dealer_phone_number) {
		this.dealer_phone_number = dealer_phone_number;
	}

	public String getDealer_address() {
		return dealer_address;
	}

	public void setDealer_address(String dealer_address) {
		this.dealer_address = dealer_address;
	}

	public String getDealer_profile_img_url() {
		return dealer_profile_img_url;
	}

	public void setDealer_profile_img_url(String dealer_profile_img_url) {
		this.dealer_profile_img_url = dealer_profile_img_url;
	}

	public String getDealer_company() {
		return dealer_company;
	}

	public void setDealer_company(String dealer_company) {
		this.dealer_company = dealer_company;
	}

	public int getDealer_level() {
		return dealer_level;
	}

	public void setDealer_level(int dealer_level) {
		this.dealer_level = dealer_level;
	}

	public String getManager_desc() {
		return manager_desc;
	}

	public void setManager_desc(String manager_desc) {
		this.manager_desc = manager_desc;
	}

	public String getManager_profile_img_url() {
		return manager_profile_img_url;
	}

	public void setManager_profile_img_url(String manager_profile_img_url) {
		this.manager_profile_img_url = manager_profile_img_url;
	}

	public String getSeller_profile_img_url() {
		return seller_profile_img_url;
	}

	public void setSeller_profile_img_url(String seller_profile_img_url) {
		this.seller_profile_img_url = seller_profile_img_url;
	}

	public int getHas_purchased() {
		return has_purchased;
	}

	public void setHas_purchased(int has_purchased) {
		this.has_purchased = has_purchased;
	}

	public int getHas_review() {
		return has_review;
	}

	public void setHas_review(int has_review) {
		this.has_review = has_review;
	}

	public int getBidders_cnt() {
		return bidders_cnt;
	}

	public void setBidders_cnt(int bidders_cnt) {
		this.bidders_cnt = bidders_cnt;
	}

	public String getCar_wd() {
		return car_wd;
	}

	public void setCar_wd(String car_wd) {
		this.car_wd = car_wd;
	}

	public Review getReview() {
		return review;
	}

	public void setReview(Review review) {
		this.review = review;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public String getInspection_note_url() {
		return inspection_note_url;
	}

	public void setInspection_note_url(String inspection_note_url) {
		this.inspection_note_url = inspection_note_url;
	}

	public long getLiked_at() {
		return liked_at;
	}

	public void setLiked_at(long liked_at) {
		this.liked_at = liked_at;
	}
	public int getDong_id() {
		return dong_id;
	}
	public void setDong_id(int dong_id) {
		this.dong_id = dong_id;
	}

	public String getAccident_desc() {
		return accident_desc;
	}

	public void setAccident_desc(String accident_desc) {
		this.accident_desc = accident_desc;
	}

	public String getSeller_nickname() {
		return seller_nickname;
	}

	public void setSeller_nickname(String seller_nickname) {
		this.seller_nickname = seller_nickname;
	}

	public long getMy_bid_price() {
		return my_bid_price;
	}

	public void setMy_bid_price(long my_bid_price) {
		this.my_bid_price = my_bid_price;
	}

	public int getMy_bid_ranking() {
		return my_bid_ranking;
	}

	public void setMy_bid_ranking(int my_bid_ranking) {
		this.my_bid_ranking = my_bid_ranking;
	}

	public long getMax_bid_price() {
		return max_bid_price;
	}

	public void setMax_bid_price(long max_bid_price) {
		this.max_bid_price = max_bid_price;
	}
	
	public int getHas_carhistory() {
		return has_carhistory;
	}

	public void setHas_carhistory(int has_carhistory) {
		this.has_carhistory = has_carhistory;
	}

	public long getDesired_price() {
		return desired_price;
	}

	public void setDesired_price(long desired_price) {
		this.desired_price = desired_price;
	}
}

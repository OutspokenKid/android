package com.cmons.cph.models;

import com.outspoken_kid.model.BaseModel;

public class Customer extends BaseModel {

	private int id;
	private int wholesale_id;
	private int retail_id;
	private int status;
	private long created_at;
	private String name;
	private int type;
	private String phone_number;
	private String address;
	private String mall_url;
	private String corp_reg_number;
	private String owner_id;
	private String owner_name;
	private String owner_phone_number;
	private int wholesales_cnt;
	private int favorite_wholesales_cnt;
	private int favorite_products_cnt;
}

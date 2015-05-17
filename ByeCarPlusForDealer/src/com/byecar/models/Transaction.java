package com.byecar.models;

import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class Transaction extends BCPBaseModel {

	public static final int TYPE_IN = 0;
	public static final int TYPE_CANCEL = 1;
	public static final int TYPE_OUT = 2;
	
//	"id": "1027",
//	"type": "1",
//	"dealer_id": "40",
//	"acct_no": "47200000097165",
//	"acct_nm": "한서우1",
//	"amount": "200000",
//	"created_at": "1431606995",
//	"status": "1",
//	"ahst_id": "0",
//	"title": "가상계좌 입금 테스트",
//	"onsalecar_id": "0",
//	"dealer_name": "한서우",
//	"dealer_cash": "480000"
	
	private int id;
	private int type;
	private int dealer_id;
	private String acct_no;
	private String acct_nm;
	private long amount;
	private long created_at;
	private int status;
	private int ahst_id;
	private String title;
	private int onsalecar_id;
	private String dealer_name;
	private long dealer_cash;
	
	public Transaction() {
		
	}
	
	public Transaction(JSONObject objJSON) {
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
			}
			
			if(objJSON.has("type")) {
				this.type = objJSON.getInt("type");
			}
			
			if(objJSON.has("dealer_id")) {
				this.dealer_id = objJSON.getInt("dealer_id");
			}
			
			if(objJSON.has("acct_no")) {
				this.acct_no = objJSON.getString("acct_no");
			}
			
			if(objJSON.has("acct_nm")) {
				this.acct_nm = objJSON.getString("acct_nm");
			}
			
			if(objJSON.has("amount")) {
				this.amount = objJSON.getLong("amount");
			}
			
			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getLong("created_at");
			}
			
			if(objJSON.has("status")) {
				this.status = objJSON.getInt("status");
			}
			
			if(objJSON.has("ahst_id")) {
				this.ahst_id = objJSON.getInt("ahst_id");
			}
			
			if(objJSON.has("title")) {
				this.title = objJSON.getString("title");
			}
			
			if(objJSON.has("onsalecar_id")) {
				this.onsalecar_id = objJSON.getInt("onsalecar_id");
			}
			
			if(objJSON.has("dealer_name")) {
				this.dealer_name = objJSON.getString("dealer_name");
			}
			
			if(objJSON.has("dealer_cash")) {
				this.dealer_cash = objJSON.getLong("dealer_cash");
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getDealer_id() {
		return dealer_id;
	}
	public void setDealer_id(int dealer_id) {
		this.dealer_id = dealer_id;
	}
	public String getAcct_no() {
		return acct_no;
	}
	public void setAcct_no(String acct_no) {
		this.acct_no = acct_no;
	}
	public String getAcct_nm() {
		return acct_nm;
	}
	public void setAcct_nm(String acct_nm) {
		this.acct_nm = acct_nm;
	}
	public long getAmount() {
		return amount;
	}
	public void setAmount(long amount) {
		this.amount = amount;
	}
	public long getCreated_at() {
		return created_at;
	}
	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getAhst_id() {
		return ahst_id;
	}
	public void setAhst_id(int ahst_id) {
		this.ahst_id = ahst_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getOnsalecar_id() {
		return onsalecar_id;
	}
	public void setOnsalecar_id(int onsalecar_id) {
		this.onsalecar_id = onsalecar_id;
	}
	public String getDealer_name() {
		return dealer_name;
	}
	public void setDealer_name(String dealer_name) {
		this.dealer_name = dealer_name;
	}
	public long getDealer_cash() {
		return dealer_cash;
	}
	public void setDealer_cash(long dealer_cash) {
		this.dealer_cash = dealer_cash;
	}
}

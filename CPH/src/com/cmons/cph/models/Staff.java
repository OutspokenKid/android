package com.cmons.cph.models;

import com.outspoken_kid.model.BaseModel;

public class Staff extends BaseModel {

	private boolean inRequest;

	public boolean isInRequest() {
		return inRequest;
	}

	public void setInRequest(boolean inRequest) {
		this.inRequest = inRequest;
	}
}

package com.cmons.cph.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.outspoken_kid.model.BaseModel;

public class Line extends BaseModel implements Serializable {

	private static final long serialVersionUID = 2079167050386712816L;
	
	public String lineName;
	public ArrayList<String> roomNumbers = new ArrayList<String>();
}

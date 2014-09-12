package com.cmons.cph.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.outspoken_kid.model.BaseModel;

public class Floor extends BaseModel implements Serializable {

	private static final long serialVersionUID = -1865915947752903138L;
	
	public String floorName;
	public ArrayList<Line> lines = new ArrayList<Line>();
}

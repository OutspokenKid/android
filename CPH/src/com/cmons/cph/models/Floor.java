package com.cmons.cph.models;

import java.util.ArrayList;

import com.cmons.cph.fragments.signup.SignUpForSearchPage.Line;
import com.outspoken_kid.model.BaseModel;

public class Floor extends BaseModel {

	public String floorName;
	public ArrayList<Line> lines = new ArrayList<Line>();
}

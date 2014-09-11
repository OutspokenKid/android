package com.cmons.cph.classes;

import com.cmons.cph.RetailActivity;
import com.cmons.cph.models.Retail;

public abstract class CmonsFragmentForRetail extends CmonsFragmentForShop {

	private Retail retail;

	public Retail getRetail() {
		
		if(retail == null) {
			retail = ((RetailActivity)mActivity).retail;
		}
		
		return retail;
	}
}
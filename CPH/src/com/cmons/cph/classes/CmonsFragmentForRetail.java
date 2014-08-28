package com.cmons.cph.classes;

import android.app.Activity;

import com.cmons.cph.RetailActivity;
import com.cmons.cph.models.Retail;

public abstract class CmonsFragmentForRetail extends CmonsFragmentForShop {

	protected Retail retail;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		retail = ((RetailActivity)activity).retail;
	}
}
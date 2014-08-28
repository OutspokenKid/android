package com.cmons.cph.classes;

import android.app.Activity;

import com.cmons.cph.WholesaleActivity;
import com.cmons.cph.models.Wholesale;

public abstract class CmonsFragmentForWholesale extends CmonsFragmentForShop {
	
	protected Wholesale wholesale;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		wholesale = ((WholesaleActivity)activity).wholesale;
	}
}

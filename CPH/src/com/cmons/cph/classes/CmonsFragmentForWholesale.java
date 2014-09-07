package com.cmons.cph.classes;

import com.cmons.cph.WholesaleActivity;
import com.cmons.cph.models.Wholesale;

public abstract class CmonsFragmentForWholesale extends CmonsFragmentForShop {
	
	private Wholesale wholesale;
	
	public Wholesale getWholesale() {
		
		if(wholesale == null) {
			wholesale = ((WholesaleActivity)mActivity).wholesale;
		}
		
		return wholesale;
	}
}

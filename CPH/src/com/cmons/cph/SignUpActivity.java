package com.cmons.cph;

import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.cmons.classes.BaseFragmentActivity;
import com.cmons.cph.fragments.SignUpForBusinessPage;
import com.cmons.cph.fragments.SignUpForPersonalPage;
import com.cmons.cph.fragments.SignUpForPositionPage;
import com.cmons.cph.fragments.SignUpForSearchPage;
import com.cmons.cph.fragments.SignUpForTermsPage;
import com.cmons.cph.fragments.SignUpForWritePage;
import com.outspoken_kid.model.FontInfo;
import com.outspoken_kid.utils.ResizeUtils;

public class SignUpActivity extends BaseFragmentActivity {

	/**
	 * type
	 * 0 : 도매 - 대표.
	 * 1 : 도매 - 직원.
	 * 2 : 도매 - 디자이너.
	 * 
	 * 3 : 소매(오프라인) - 대표.
	 * 4 : 소매(오프라인) - 직원.
	 * 5 : 소매(오프라인) - MD.
	 * 
	 * 6 : 소매(온라인) - 대표.
	 * 7 : 소매(온라인) - 직원.
	 * 8 : 소매(온라인) - MD.
	 */
	
	public static final int BUSINESS_WHOLESALE = 0;
	public static final int BUSINESS_RETAIL_OFFLINE = 3;
	public static final int BUSINESS_RETAIL_ONLINE = 6;

	public static final int POSITION_OWNER = 0;
	public static final int POSITION_EMPLOYEE1 = 1;
	public static final int POSITION_EMPLOYEE2 = 2;

	private int businessType;
	private int positionType;

	private TextView tvTitle;
	private SignUpForTermsPage mSignUpForTermsPage;
	private SignUpForBusinessPage mSignUpForBusinessPage;
	private SignUpForPositionPage mSignUpForPositionPage;
	private SignUpForSearchPage mSignUpForSearchPage;
	private SignUpForWritePage mSignUpForWritePage;
	private SignUpForPersonalPage mSignUpForPersonalPage;
	
	@Override
	protected void bindViews() {

		tvTitle = (TextView) findViewById(R.id.signUpActivity_tvTitle);
	}

	@Override
	protected void setVariables() {
	}

	@Override
	protected void createPage() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setSizes() {

		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 90, tvTitle, 1, 0, null);
		FontInfo.setFontSize(tvTitle, 36);
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void downloadInfo() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setPage(boolean successDownload) {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getXmlResId() {

		return R.layout.activity_sign_up;
	}

	@Override
	protected int getFragmentFrameId() {

		return 0;
	}
	
	@Override
	public void onRefreshPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTitleText(String title) {

	}
	
	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		
		if(mSignUpForTermsPage == null) {
			mSignUpForTermsPage = new SignUpForTermsPage();
		}
		
		startPage(mSignUpForTermsPage, null);
	}
	
/////////////////////////// Custom methods.
	
	public void agree() {
		
		if(mSignUpForBusinessPage == null) {
			mSignUpForBusinessPage = new SignUpForBusinessPage();
		}
		
		startPage(mSignUpForBusinessPage, null);
	}
	
	public void setBusiness(int type) {

		this.businessType = type;
		
		if(mSignUpForPositionPage == null) {
			mSignUpForPositionPage = new SignUpForPositionPage();
		}
		
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		
		startPage(mSignUpForPositionPage, bundle);
	}
	
	public void setPosition(int type) {
		
		this.positionType = type;

		int userType = getType();
		Bundle bundle = new Bundle();
		bundle.putInt("type", userType);
		
		switch(userType) {

		case BUSINESS_WHOLESALE + POSITION_OWNER:
		case BUSINESS_WHOLESALE + POSITION_EMPLOYEE1:
		case BUSINESS_WHOLESALE + POSITION_EMPLOYEE2:
		case BUSINESS_RETAIL_OFFLINE + POSITION_EMPLOYEE1:
		case BUSINESS_RETAIL_OFFLINE + POSITION_EMPLOYEE2:
		case BUSINESS_RETAIL_ONLINE + POSITION_EMPLOYEE1:
		case BUSINESS_RETAIL_ONLINE + POSITION_EMPLOYEE2:
			
			if(mSignUpForSearchPage == null) {
				mSignUpForSearchPage = new SignUpForSearchPage();
			}
			
			startPage(mSignUpForSearchPage, bundle);
			break;
		
		case BUSINESS_RETAIL_OFFLINE + POSITION_OWNER:
		case BUSINESS_RETAIL_ONLINE + POSITION_OWNER:
			
			if(mSignUpForWritePage == null) {
				mSignUpForWritePage = new SignUpForWritePage();
			}

			startPage(mSignUpForWritePage, bundle);
			break;
		}
	}
	
	public void showPersonalPage(int type) {
		
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		
		if(mSignUpForPersonalPage == null) {
			mSignUpForPersonalPage = new SignUpForPersonalPage();
		}
		
		startPage(mSignUpForPersonalPage, bundle);
	}
	
	public int getType() {
		
		return businessType + positionType;
	}
}

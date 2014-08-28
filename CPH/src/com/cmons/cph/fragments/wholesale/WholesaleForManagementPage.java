package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class WholesaleForManagementPage extends CmonsFragmentForWholesale {

	private Button btnNotice;
	private Button btnProfile;
	private Button btnAccount;
	private Button btnKakao;
	private Button btnSample;
	private TextView tvInfo;
	
	private boolean allowSampleRequest;
	
	@Override
	public void bindViews() {
		
		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleManagementPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.wholesaleManagementPage_ivBg);
		
		btnNotice = (Button) mThisView.findViewById(R.id.wholesaleManagementPage_btnNotice);
		btnProfile = (Button) mThisView.findViewById(R.id.wholesaleManagementPage_btnProfile);
		btnAccount = (Button) mThisView.findViewById(R.id.wholesaleManagementPage_btnAccount);
		btnKakao = (Button) mThisView.findViewById(R.id.wholesaleManagementPage_btnKakao);
		btnSample = (Button) mThisView.findViewById(R.id.wholesaleManagementPage_btnSample);
		
		tvInfo = (TextView) mThisView.findViewById(R.id.wholesaleManagementPage_tvInfo);
	}

	@Override
	public void setVariables() {

		title = "매장관리";
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		if(wholesale.getSample_available() == 1) {
			allowSampleRequest = true;
		} else {
			allowSampleRequest = false;
		}
		
		SpannableStringBuilder sp1 = new SpannableStringBuilder("매장전화번호");
		sp1.setSpan(new RelativeSizeSpan(1.2f), 0, sp1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvInfo.append(sp1); 
		
		SpannableStringBuilder sp2 = new SpannableStringBuilder("\n\n" + wholesale.getPhone_number());
		tvInfo.append(sp2); 
		
		SpannableStringBuilder sp3 = new SpannableStringBuilder("\n\n매장주소");
		sp3.setSpan(new RelativeSizeSpan(1.2f), 0, sp3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvInfo.append(sp3);
		
		SpannableStringBuilder sp4 = new SpannableStringBuilder("\n\n청평화몰 " + wholesale.getLocation());
		tvInfo.append(sp4);
		
		SpannableStringBuilder sp5 = new SpannableStringBuilder(
				"\n\n도매매장 전화번호, 주소 변경은 앱에서 지원하지 않습니다.\n청평화쇼핑몰 관리소에 문의해주세요.");
		sp5.setSpan(new ForegroundColorSpan(Color.RED), 0, sp5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvInfo.append(sp5);
		
		if(allowSampleRequest) {
			btnSample.setBackgroundResource(R.drawable.shop_sample_btn_b);
		} else{
			btnSample.setBackgroundResource(R.drawable.shop_sample_btn_a);
		}
	}

	@Override
	public void setListeners() {	

		btnNotice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				Bundle bundle = new Bundle();
				bundle.putBoolean("isAppNotice", false);
				mActivity.showPage(CphConstants.PAGE_WHOLESALE_NOTICE_LIST, bundle);
			}
		});
		
		btnProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(CphConstants.PAGE_WHOLESALE_PROFILE_IMAGE, null);
			}
		});

		btnAccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(CphConstants.PAGE_WHOLESALE_ACCOUNT, null);
			}
		});
			
		btnSample.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				allowSampleRequest = !allowSampleRequest;
				
				if(allowSampleRequest) {
					btnSample.setBackgroundResource(R.drawable.shop_sample_btn_b);
				} else{
					btnSample.setBackgroundResource(R.drawable.shop_sample_btn_a);
				}
				
				setSampleRequest(allowSampleRequest);
			}
		});
	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;
		int length = ResizeUtils.getScreenWidth()/2;
		
		//btnNotice.
		rp = (RelativeLayout.LayoutParams) btnNotice.getLayoutParams();
		rp.width = length;
		rp.height = length;
		
		//btnProfile.
		rp = (RelativeLayout.LayoutParams) btnProfile.getLayoutParams();
		rp.height = length;
		
		//btnAccount.
		rp = (RelativeLayout.LayoutParams) btnAccount.getLayoutParams();
		rp.width = length;
		rp.height = length;
		
		//btnKakao.
		rp = (RelativeLayout.LayoutParams) btnKakao.getLayoutParams();
		rp.height = length;
		
		//btnSample.
		rp = (RelativeLayout.LayoutParams) btnSample.getLayoutParams();
		rp.height = length/2;
		
		int p = ResizeUtils.getSpecificLength(30);
		tvInfo.setPadding(p, p, p, p);
		FontUtils.setFontSize(tvInfo, 24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_management;
	}

	@Override
	public void refreshPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.shop_bg;
	}

//////////////////// Custom methods.
	
	public void setSampleRequest(final boolean allow) {
		
		String url = CphConstants.BASE_API_URL + "wholesales/update/sample_available";
		
		if(allow) {
			url += "?sample_available=1";
		} else {
			url += "?sample_available=0";
		}
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForManagementPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForManagementPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					if(1 == objJSON.getInt("result")) {
						
						if(allow) {
							wholesale.setSample_available(1);
						} else {
							wholesale.setSample_available(0);
						}
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
}

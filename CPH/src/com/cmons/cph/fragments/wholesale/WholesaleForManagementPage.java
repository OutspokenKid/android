package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class WholesaleForManagementPage extends CmonsFragmentForWholesale {

	private Button btnNotice;
	private Button btnProfile;
	private Button btnAccount;
	private Button btnKakao;
	private Button btnSample;
	private TextView tvInfo;
	
	private boolean needSample;
	
	@Override
	public void bindViews() {
		
		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleManagementPage_titleBar);
		
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
		titleBar.getHomeButton().setVisibility(View.INVISIBLE);
		
		SpannableStringBuilder sp1 = new SpannableStringBuilder("매장전화번호");
		sp1.setSpan(new RelativeSizeSpan(1.2f), 0, sp1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvInfo.append(sp1); 
		
		SpannableStringBuilder sp2 = new SpannableStringBuilder("\n\n010-1234-5678.");
		tvInfo.append(sp2); 
		
		SpannableStringBuilder sp3 = new SpannableStringBuilder("\n\n매장주소");
		sp3.setSpan(new RelativeSizeSpan(1.2f), 0, sp3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvInfo.append(sp3);
		
		SpannableStringBuilder sp4 = new SpannableStringBuilder("\n\n서울특별시 중고 신당동 217-91 청평화 쇼핑 2층 305호");
		tvInfo.append(sp4);
		
		SpannableStringBuilder sp5 = new SpannableStringBuilder(
				"\n\n도매매장 전화번호, 주소 변경은 앱에서 지원하지 않습니다.\n청평화쇼핑몰 관리소에 문의해주세요.");
		sp5.setSpan(new ForegroundColorSpan(Color.RED), 0, sp5.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvInfo.append(sp5);
		
		if(needSample) {
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

				mActivity.showNoticeListPage();
			}
		});
		
		btnSample.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				needSample = !needSample;
				
				if(needSample) {
					btnSample.setBackgroundResource(R.drawable.shop_sample_btn_b);
				} else{
					btnSample.setBackgroundResource(R.drawable.shop_sample_btn_a);
				}
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
}

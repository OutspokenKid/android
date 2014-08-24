package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Sample;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.ToastUtils;

public class WholesaleForSamplePage extends CmonsFragmentForWholesale {

	private Button btnRequest;
	private Button btnApproval;
	private Button btnReturn;
	private Button btnComplete;
	private TextView tvSearch;
	private EditText editText;
	private Button btnSearch;
	
	private ListView listView;
	
	private RelativeLayout popupRelative;
	private Button btnClose;
	private TextView tvShopName;
	private TextView tvOwnerName;
	private TextView tvPhone;
	private TextView tvSample;
	private Button btnConfirm;
	
	private int menuIndex;
	private String keyword;
	
	private AlphaAnimation aaIn, aaOut;
	private boolean isAnimating;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleSamplePage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.wholesaleSamplePage_ivBg);
		
		btnRequest = (Button) mThisView.findViewById(R.id.wholesaleSamplePage_btnRequest);
		btnApproval = (Button) mThisView.findViewById(R.id.wholesaleSamplePage_btnApproval);
		btnReturn = (Button) mThisView.findViewById(R.id.wholesaleSamplePage_btnReturn);
		btnComplete = (Button) mThisView.findViewById(R.id.wholesaleSamplePage_btnComplete);
		tvSearch = (TextView) mThisView.findViewById(R.id.wholesaleSamplePage_tvSearch);
		editText = (EditText) mThisView.findViewById(R.id.wholesaleSamplePage_editText);
		btnSearch = (Button) mThisView.findViewById(R.id.wholesaleSamplePage_btnSearch);
		
		listView = (ListView) mThisView.findViewById(R.id.wholesaleSamplePage_listView);
		
		popupRelative = (RelativeLayout) mThisView.findViewById(R.id.wholesaleSamplePage_popupRelative);
		btnClose = (Button) mThisView.findViewById(R.id.wholesaleSamplePage_btnClose);
		tvShopName = (TextView) mThisView.findViewById(R.id.wholesaleSamplePage_tvShopName);
		tvOwnerName = (TextView) mThisView.findViewById(R.id.wholesaleSamplePage_tvOwnerName);
		tvPhone = (TextView) mThisView.findViewById(R.id.wholesaleSamplePage_tvPhone);
		tvSample = (TextView) mThisView.findViewById(R.id.wholesaleSamplePage_tvSample);
		btnConfirm = (Button) mThisView.findViewById(R.id.wholesaleSamplePage_btnConfirm);
	}

	@Override
	public void setVariables() {

		title = "샘플관리";
	
		AnimationListener al = new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				isAnimating = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				isAnimating = false;				
			}
		};
		
		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);
		aaIn.setAnimationListener(al);
		
		aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(300);
		aaOut.setAnimationListener(al);
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.INVISIBLE);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
	}

	@Override
	public void setListeners() {

		btnRequest.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(0);
			}
		});
		
		btnApproval.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(1);
			}
		});
		
		btnReturn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(2);
			}
		});
		
		btnComplete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(3);
			}
		});
		
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(editText.getEditableText() == null
						|| editText.getEditableText().length() == 0) {
					ToastUtils.showToast(R.string.wrongSearchKeyword);
					return;
				}
				
				keyword = editText.getEditableText().toString();
				refreshPage();
			}
		});
	
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView, int position,
					long id) {
				showPopup();
			}
		});
		
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hidePopup();
			}
		});
		
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hidePopup();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//ivBg.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.wholesaleSamplePage_ivBg).getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(96); 
		
		//btnRequest.
		rp = (RelativeLayout.LayoutParams) btnRequest.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/4;
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//btnApproval.
		rp = (RelativeLayout.LayoutParams) btnApproval.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/4;
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//btnReturn.
		rp = (RelativeLayout.LayoutParams) btnReturn.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/4;
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//btnComplete.
		rp = (RelativeLayout.LayoutParams) btnComplete.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvSearch.
		int p = ResizeUtils.getSpecificLength(10);
		tvSearch.setPadding(p, p*4, p, p);
		FontUtils.setFontSize(tvSearch, 30);
		
		//editText.
		rp = (RelativeLayout.LayoutParams) editText.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(550);
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontAndHintSize(editText, 30, 24);

		//btnSearch.
		rp = (RelativeLayout.LayoutParams) btnSearch.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//popupRelative.
		rp = (RelativeLayout.LayoutParams) popupRelative.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(625);
		rp.height = ResizeUtils.getSpecificLength(705);
		p = ResizeUtils.getSpecificLength(22);
		popupRelative.setPadding(p, p, p, p);
		
		//btnClose.
		rp = (RelativeLayout.LayoutParams) btnClose.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(52);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(6);
		rp.rightMargin = ResizeUtils.getSpecificLength(8);
		
		//tvShopName.
		rp = (RelativeLayout.LayoutParams) tvShopName.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(88);
		rp.topMargin = ResizeUtils.getSpecificLength(64);
		tvShopName.setPadding(p, 0, p, 0);
		FontUtils.setFontSize(tvShopName, 28);
		
		//tvOwnerName.
		rp = (RelativeLayout.LayoutParams) tvOwnerName.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(88);
		tvOwnerName.setPadding(p, 0, p, 0);
		FontUtils.setFontSize(tvOwnerName, 28);
		
		//tvPhone.
		rp = (RelativeLayout.LayoutParams) tvPhone.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(88);
		tvPhone.setPadding(p, 0, p, 0);
		FontUtils.setFontSize(tvPhone, 28);
		
		//tvSample.
		FontUtils.setFontSize(tvSample, 28);
		tvSample.setPadding(p, p, p, p);
		
		//btnConfirm.
		rp = (RelativeLayout.LayoutParams) btnConfirm.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(209);
		rp.height = ResizeUtils.getSpecificLength(62);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_sample;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {

		if(popupRelative.getVisibility() == View.VISIBLE) {
			hidePopup();
			return true;
		}
		
		return false;
	}

	@Override
	public void downloadInfo() {
		
		url = CphConstants.BASE_API_URL + "wholesales/notices";
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {
		
		for(int i=0; i<10; i++) {
			Sample sample = new Sample();
			sample.setItemCode(CphConstants.ITEM_SAMPLE);
			models.add(sample);
		}
		
		return false;
	}

//////////////////// Custom methods.
	
	public void setMenu(int menuIndex) {
		
		switch (menuIndex) {
		
		case 0:
			btnRequest.setBackgroundResource(R.drawable.sample_recommand_btn_a);
			btnApproval.setBackgroundResource(R.drawable.sample_confirm_btn_b);
			btnReturn.setBackgroundResource(R.drawable.sample_return_btn_b);
			btnComplete.setBackgroundResource(R.drawable.sample_return_done_btn_b);
			break;
			
		case 1:
			btnRequest.setBackgroundResource(R.drawable.sample_recommand_btn_b);
			btnApproval.setBackgroundResource(R.drawable.sample_confirm_btn_a);
			btnReturn.setBackgroundResource(R.drawable.sample_return_btn_b);
			btnComplete.setBackgroundResource(R.drawable.sample_return_done_btn_b);
			break;
			
		case 2:
			btnRequest.setBackgroundResource(R.drawable.sample_recommand_btn_b);
			btnApproval.setBackgroundResource(R.drawable.sample_confirm_btn_b);
			btnReturn.setBackgroundResource(R.drawable.sample_return_btn_a);
			btnComplete.setBackgroundResource(R.drawable.sample_return_done_btn_b);
			break;
			
		case 3:
			btnRequest.setBackgroundResource(R.drawable.sample_recommand_btn_b);
			btnApproval.setBackgroundResource(R.drawable.sample_confirm_btn_b);
			btnReturn.setBackgroundResource(R.drawable.sample_return_btn_b);
			btnComplete.setBackgroundResource(R.drawable.sample_return_done_btn_a);
			break;
		}
		
		this.menuIndex = menuIndex;
		
		refreshPage();
	}

	public void showPopup() {

		if(isAnimating || popupRelative.getVisibility() == View.VISIBLE) {
			return;
		}
		
		tvShopName.setText("상호명 : " + "매장이름1");
		tvOwnerName.setText("대표성함 : " + "대표이름1");
		tvPhone.setText("연락처 : " + "010-1111-1111");
		
		tvSample.setText(null);
		SpannableStringBuilder sp1 = new SpannableStringBuilder("샘플요청내역\n\n");
		tvSample.append(sp1);
		
		SpannableStringBuilder sp2 = new SpannableStringBuilder(
				"품명 : " + "티셔츠" +
				"\n\n색상 : " + "흰색" +
				"\n\n사이즈 : " + "S"
				);
		sp2.setSpan(new RelativeSizeSpan(0.8f), 0, sp2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		tvSample.append(sp2);
		
		switch (menuIndex) {
		case 0:
			btnConfirm.setBackgroundResource(R.drawable.sample_confirm_popup_btn);
			break;
			
		case 1:
			btnConfirm.setBackgroundResource(R.drawable.sample_return_popup_btn);
			break;
			
		case 2:
			btnConfirm.setBackgroundResource(R.drawable.sample_return_done_popup_btn);
			break;
			
		case 3:
			btnConfirm.setBackgroundResource(R.drawable.sample_delete_popup_btn);
			break;
		}
		
		SoftKeyboardUtils.hideKeyboard(mContext, popupRelative);
		popupRelative.setVisibility(View.VISIBLE);
		popupRelative.startAnimation(aaIn);
	}
	
	public void hidePopup() {
		
		if(isAnimating || popupRelative.getVisibility() == View.INVISIBLE) {
			return;
		}
		
		popupRelative.setVisibility(View.INVISIBLE);
		popupRelative.startAnimation(aaOut);
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.sample_bg;
	}
}

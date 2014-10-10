package com.cmons.cph.fragments.wholesale;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.ToastUtils;

public class WholesaleForSampleListPage extends CmonsFragmentForWholesale {

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
	
	private Sample selectedSample;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			setMenu(menuIndex);
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
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
		listView.setDivider(new ColorDrawable(Color.WHITE));
		listView.setDividerHeight(1);
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
				showPopup((Sample)models.get(position));
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
				
				switch(menuIndex) {
				
				case 0:
					changeStatus(selectedSample.getId(), 1);
					break;
				case 1:
					changeStatus(selectedSample.getId(), 2);
					break;
				case 2:
					changeStatus(selectedSample.getId(), 3);
					break;
				case 3:
					deleteSample(selectedSample.getId());
					break;
				}
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

		return R.layout.fragment_wholesale_samplelist;
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
		
		//http://cph.minsangk.com/wholesales/samples
		url = CphConstants.BASE_API_URL + "wholesales/samples" +
				"?status=" + menuIndex;
		
		try {
			if(keyword != null) {
				url += "&keyword=" + URLEncoder.encode(keyword, "utf-8");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {
		
		try {
			JSONArray arJSON = objJSON.getJSONArray("samples");
			
			int size = arJSON.length();
			for(int i=0; i<size; i++) {
				Sample sample = new Sample(arJSON.getJSONObject(i));
				sample.setItemCode(CphConstants.ITEM_SAMPLE);
				models.add(sample);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
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
			tvSearch.setVisibility(View.GONE);
			editText.setVisibility(View.GONE);
			btnSearch.setVisibility(View.GONE);
			break;
			
		case 1:
			btnRequest.setBackgroundResource(R.drawable.sample_recommand_btn_b);
			btnApproval.setBackgroundResource(R.drawable.sample_confirm_btn_a);
			btnReturn.setBackgroundResource(R.drawable.sample_return_btn_b);
			btnComplete.setBackgroundResource(R.drawable.sample_return_done_btn_b);
			tvSearch.setVisibility(View.VISIBLE);
			editText.setVisibility(View.VISIBLE);
			btnSearch.setVisibility(View.VISIBLE);
			break;
			
		case 2:
			btnRequest.setBackgroundResource(R.drawable.sample_recommand_btn_b);
			btnApproval.setBackgroundResource(R.drawable.sample_confirm_btn_b);
			btnReturn.setBackgroundResource(R.drawable.sample_return_btn_a);
			btnComplete.setBackgroundResource(R.drawable.sample_return_done_btn_b);
			tvSearch.setVisibility(View.VISIBLE);
			editText.setVisibility(View.VISIBLE);
			btnSearch.setVisibility(View.VISIBLE);
			break;
			
		case 3:
			btnRequest.setBackgroundResource(R.drawable.sample_recommand_btn_b);
			btnApproval.setBackgroundResource(R.drawable.sample_confirm_btn_b);
			btnReturn.setBackgroundResource(R.drawable.sample_return_btn_b);
			btnComplete.setBackgroundResource(R.drawable.sample_return_done_btn_a);
			tvSearch.setVisibility(View.VISIBLE);
			editText.setVisibility(View.VISIBLE);
			btnSearch.setVisibility(View.VISIBLE);
			break;
		}
		
		this.menuIndex = menuIndex;
		refreshPage();
	}

	public void showPopup(Sample sample) {

		selectedSample = sample;
		
		if(isAnimating || popupRelative.getVisibility() == View.VISIBLE) {
			return;
		}
		
		tvShopName.setText("상호명 : " + sample.getRetail_name());
		tvOwnerName.setText("대표성함 : " + sample.getRetail_owner_name());
		tvPhone.setText("연락처 : " + sample.getRetail_phone_number());
		
		tvSample.setText(null);
		SpannableStringBuilder sp1 = new SpannableStringBuilder("샘플요청내역\n\n");
		tvSample.append(sp1);
		
		SpannableStringBuilder sp2 = new SpannableStringBuilder(
				"품명 : " + sample.getProduct_name() +
				"\n\n색상 : " + sample.getColor() +
				"\n\n사이즈 : " + sample.getSize()
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

	public void changeStatus(int sample_id, int status) {
		
		String url = CphConstants.BASE_API_URL + "wholesales/samples/set_status" + 
				"?sample_id=" + sample_id + 
				"&status=" + status;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForSampleListPage.changeStatus.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToChangeSampleStatus);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForSampleListPage.changeStatus.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						
						hidePopup();
						
						if(menuIndex == 0) {
							ToastUtils.showToast(R.string.complete_changeSampleStatus1);
						} else if(menuIndex == 1) {
							ToastUtils.showToast(R.string.complete_changeSampleStatus2);
						} else if(menuIndex == 2) {	
							ToastUtils.showToast(R.string.complete_changeSampleStatus3);
						}
						
						setMenu(menuIndex + 1);
					} else {
						objJSON.getString("message");
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToChangeSampleStatus);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToChangeSampleStatus);
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void deleteSample(int sample_id) {
		
		//http://cph.minsangk.com/wholesales/samples/delete?sample_id=10001
		String url = CphConstants.BASE_API_URL + "wholesales/samples/delete" + 
				"?sample_id=" + sample_id;
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForSampleListPage.deleteSample.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToDeleteSample);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForSampleListPage.deleteSample.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_deleteSample);
						hidePopup();
						refreshPage();
					} else {
						objJSON.getString("message");
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToDeleteSample);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToDeleteSample);
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	@Override
	public int getBgResourceId() {

		return R.drawable.sample_bg;
	}
}

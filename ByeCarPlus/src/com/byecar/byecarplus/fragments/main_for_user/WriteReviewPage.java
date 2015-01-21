package com.byecar.byecarplus.fragments.main_for_user;

import org.json.JSONObject;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.byecarplus.classes.BCPAPIs;
import com.byecar.byecarplus.classes.BCPFragment;
import com.byecar.byecarplus.models.Car;
import com.byecar.byecarplus.models.Review;
import com.byecar.byecarplus.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.OutSpokenRatingBar;

public class WriteReviewPage extends BCPFragment {

	private Review review;
	private Car car;
	private int manager_id;
	private int dealer_id;
	
	private Button btnSubmit;
	private TextView tvCarName;
	private TextView tvRegdate;
	private TextView tvRatingText;
	private OutSpokenRatingBar ratingBar;
	private TextView tvTo;
	private EditText etContent;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.writeReviewPage_titleBar);
		
		btnSubmit = (Button) mThisView.findViewById(R.id.writeReviewPage_btnSubmit);
		tvCarName = (TextView) mThisView.findViewById(R.id.writeReviewPage_tvCarName);
		tvRegdate = (TextView) mThisView.findViewById(R.id.writeReviewPage_tvRegdate);
		tvRatingText = (TextView) mThisView.findViewById(R.id.writeReviewPage_tvRatingText);
		ratingBar = (OutSpokenRatingBar) mThisView.findViewById(R.id.writeReviewPage_ratingBar);
		tvTo = (TextView) mThisView.findViewById(R.id.writeReviewPage_tvTo);
		etContent = (EditText) mThisView.findViewById(R.id.writeReviewPage_etContent);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			review = (Review) getArguments().getSerializable("review");
			car = (Car) getArguments().getSerializable("car");
			manager_id = getArguments().getInt("manager_id");
			dealer_id = getArguments().getInt("dealer_id");
		}
	}

	@Override
	public void createPage() {

		//수정.
		if(review != null) {
			tvRegdate.setText(StringUtils.getDateString(
					"등록일 yyyy년 MM월 dd일", review.getCreated_at() * 1000));
			tvCarName.setText(review.getCar_full_name());
			
			if(review.getCertifier_id() != 0) {
				tvRatingText.setText(R.string.evaluateCertifier);
				tvTo.setText(review.getCertifier_name() + getString(R.string.toCertifier));
			} else if(review.getDealer_id() != 0) {
				tvRatingText.setText(R.string.evaluateDealer);
				tvTo.setText(review.getDealer_name() + getString(R.string.toDealer));
			}
			
			etContent.setText(review.getContent());
			
		//첫 작성.
		} else if(car != null) {
//			tvRegdate.setText(StringUtils.getDateString(
//					"등록일 yyyy년 MM월 dd일", review.getCreated_at() * 1000));
//			tvCarName.setText(review.getCar_full_name());
//			
//			if(c.getCertifier_id() != 0) {
//				tvRegdate.setText(R.string.evaluateCertifier);
//				tvTo.setText(review.getCertifier_name() + " 검증사에게");
//			} else if(car.getDealer_id() != 0) {
//				tvRegdate.setText(R.string.evaluateDealer);
//				tvTo.setText(review.getDealer_name() + " 딜러에게");
//			}
		}
		
		ratingBar.setMinRating(1);
		ratingBar.setMaxRating(5);
		ratingBar.setLengths(ResizeUtils.getSpecificLength(43),
				ResizeUtils.getSpecificLength(34));
		ratingBar.setRating(3);
		ratingBar.setEmptyStarColor(Color.rgb(195, 195, 195));
		ratingBar.setFilledStarColor(Color.rgb(254, 188, 42));
		ratingBar.setUnitRating(OutSpokenRatingBar.UNIT_ONE);
	}

	@Override
	public void setListeners() {

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(StringUtils.isEmpty(etContent)) {
					ToastUtils.showToast(R.string.checkReviewContent);
					return;
				} else {
					submit();
				}
			}
		});
	}

	@Override
	public void setSizes() {

		ResizeUtils.viewResizeForRelative(60, 60, btnSubmit, null, null, new int[]{0, 14, 14, 0});
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, tvCarName, null, null, null, 
				new int[]{20, 0, 200, 0});
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, 88, tvRegdate, null, null, null, 
				new int[]{0, 0, 20, 0});
		
		ResizeUtils.viewResizeForRelative(LayoutParams.MATCH_PARENT, 88, tvRatingText, null, null, null,
				new int[]{20, 0, 0, 0});
		ResizeUtils.viewResizeForRelative(386, 88, ratingBar, null, null, null);
		
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, 60, tvTo, null, null, null, 
				new int[]{20, 0, 0, 0});

		int p = ResizeUtils.getSpecificLength(20);
		etContent.setPadding(p, p, p, p);
		
		FontUtils.setFontSize(tvRegdate, 16);
		FontUtils.setFontSize(tvCarName, 30);
		FontUtils.setFontStyle(tvCarName, FontUtils.BOLD);
		FontUtils.setFontSize(tvRatingText, 20);
		FontUtils.setFontStyle(tvRatingText, FontUtils.BOLD);
		FontUtils.setFontSize(tvTo, 20);
		FontUtils.setFontSize(etContent, 20);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_write_review;
	}

	@Override
	public int getBackButtonResId() {

		return R.drawable.reveiw_episode_back_btn;
	}

	@Override
	public int getBackButtonWidth() {

		return 235;
	}

	@Override
	public int getBackButtonHeight() {

		return 60;
	}

	@Override
	public int getRootViewResId() {

		return R.id.writeReviewPage_mainLayout;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
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

//////////////////// Custom methods.
	
	public void submit() {
		
//		dealer_id : 딜러 ID						ok.
//		review[id] : 리뷰 ID (수정시에만 입력)		ok.
//		review[onsalecar_id] : 거래차량 ID			ok.
//		review[content] : 내용					ok.
//		review[rating] : 평점 (1~5)
		
		/*
		http://byecar.minsangk.com/dealers/reviews/save.json
		?review[content]=review+test
		&review[rating]=4.0
		&dealer_id=4
		&review[id]=12
		&review[onsalecar_id]14
		&review[rating]5

		*/

		String url = null;
		
		//딜러.
		if(dealer_id != 0) {
			url = BCPAPIs.REVIEW_DEALER_WRITE_URL
					+ "?dealer_id=" + dealer_id;
			
		//검증사.
		} else if(manager_id != 0){
			url = BCPAPIs.REVIEW_CERTIFIER_WRITE_URL
					+ "?certifier_id=" + manager_id;
			
		} else {
			ToastUtils.showToast(R.string.failToWriteReview);
			return;
		}
		
		url += "?review[content]=" + StringUtils.getUrlEncodedString(etContent)
				+ "&review[rating]=" + (int)ratingBar.getRating();
		
		//수정.
		if(review != null) {
			url += "&review[id]=" + review.getId()
					+ "&review[onsalecar_id]=" + review.getOnsalecar_id();
		} else {
			ToastUtils.showToast(R.string.failToWriteReview);
		}
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WriteReviewPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToWriteReview);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WriteReviewPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_writeReview);
						
						/*
						{
							"message":"OK",
							"result":1,
							"review":{
								"content":"review test",
								"id":"12",
								"reviewer_profile_img_url":"http:\/\/graph.facebook.com\/876446659062835\/picture?type=large",
								"dealer_id":"4",
								"reviewer_nickname":"HyunggunKim",
								"certifier_id":"0",
								"created_at":"1421103508",
								"reviewer_id":"28",
								"rating":"5",
								"type":"100",
								"onsalecar_id":"14"
							}
						}
						*/
						if(review != null) {
							review = new Review(objJSON.getJSONObject("review"));
						} else if(car != null) {
							//set Car's rating.
						}
						
						mActivity.closeTopPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToWriteReview);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToWriteReview);
				}
			}
		});
	}
}

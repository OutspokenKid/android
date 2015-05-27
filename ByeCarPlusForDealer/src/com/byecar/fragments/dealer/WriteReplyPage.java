package com.byecar.fragments.dealer;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.byecarplusfordealer.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Review;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class WriteReplyPage extends BCPFragment {

	private Review review;
	private String to;
	private int parent_id;
	
	private Button btnSubmit;
	private View replyBadge;
	private TextView tvTo;
	private EditText etContent;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.writeReplyPage_titleBar);
		
		btnSubmit = (Button) mThisView.findViewById(R.id.writeReplyPage_btnSubmit);
		replyBadge = mThisView.findViewById(R.id.writeReplyPage_replyBadge);
		tvTo = (TextView) mThisView.findViewById(R.id.writeReplyPage_tvTo);
		etContent = (EditText) mThisView.findViewById(R.id.writeReplyPage_etContent);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {

			//수정인 경우.
			if(getArguments().containsKey("review")) {
				this.review = (Review) getArguments().getSerializable("review");
			}
			
			if(getArguments().containsKey("to")) {
				this.to = getArguments().getString("to");
			}
			
			//댓글을 달 리뷰의 아이디.
			if(getArguments().containsKey("parent_id")) {
				this.parent_id = getArguments().getInt("parent_id");
			}
		}
	}

	@Override
	public void createPage() {

		if(review != null) {
			etContent.setText(review.getContent());
		}
		
		if(to != null) {
			tvTo.setText("RE : " + to);
		}
	}

	@Override
	public void setListeners() {

		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(StringUtils.isEmpty(etContent)) {
					ToastUtils.showToast(R.string.checkReplyContent);
					return;
				} else {
					submit();
				}
			}
		});
	}

	@Override
	public void setSizes() {

		ResizeUtils.viewResizeForRelative(132, 60, btnSubmit, null, null, new int[]{0, 14, 14, 0});
		
		ResizeUtils.viewResizeForRelative(17, 16, replyBadge, null, null, new int[]{21, 34, 0, 0});
		ResizeUtils.viewResizeForRelative(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 
				tvTo, null, null, null, new int[]{4, 26, 0, 0});

		int p = ResizeUtils.getSpecificLength(20);
		etContent.setPadding(p, p, p, p);
		
		FontUtils.setFontSize(tvTo, 30);
		FontUtils.setFontSize(etContent, 24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_write_reply;
	}

	@Override
	public int getPageTitleTextResId() {

		return R.string.pageTitle_writeReply;
	}

	@Override
	public int getRootViewResId() {

		return R.id.writeReplyPage_mainLayout;
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
		
		/*
		http://dev.bye-car.com/dealers/reviews/save.json
		?review[parent_id]=1
		&review[content]=%EB%82%B4%EC%9A%A9
		*/
		String url = BCPAPIs.REVIEW_WRITE_REPLY_URL
				+ "?dealer_id=" + MainActivity.dealer.getId()
				+ "&review[content]=" + StringUtils.getUrlEncodedString(etContent)
				+ "&review[parent_id]=" + parent_id;
		
		//수정인 경우.
		if(review != null) {
			url += "&review[id]=" + review.getId();
		}
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WriteReplyPage.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToWriteReply);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WriteReplyPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_writeReply);
						mActivity.closePageWithRefreshPreviousPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToWriteReply);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToWriteReply);
				}
			}
		});
	}
}

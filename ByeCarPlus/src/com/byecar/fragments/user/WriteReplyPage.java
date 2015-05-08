package com.byecar.fragments.user;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPAPIs;
import com.byecar.classes.BCPFragment;
import com.byecar.models.Post;
import com.byecar.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class WriteReplyPage extends BCPFragment {

	private Post post;
	private String to;
	private int post_id;
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
			
			if(getArguments().containsKey("post")) {
				this.post = (Post) getArguments().getSerializable("post");
			}
			
			if(getArguments().containsKey("to")) {
				this.to = getArguments().getString("to");
			}
			
			if(getArguments().containsKey("post_id")) {
				this.post_id = getArguments().getInt("post_id");
			}
			
			if(getArguments().containsKey("parent_id")) {
				this.parent_id = getArguments().getInt("parent_id");
			}
		}
	}

	@Override
	public void createPage() {

		if(post != null) {
			etContent.setText(post.getContent());
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
		
		//http://byecar1.minsangk.com/posts/reply/save.json?reply[post_id]=1&reply[content]=
		
		/*
		http://byecar1.minsangk.com/posts/reply/save.json
		?reply[content]=444444444
		&reply[post_id]=75
		*/
		String url = BCPAPIs.FORUM_REPLY_WRITE_URL
				+ "?reply[content]=" + StringUtils.getUrlEncodedString(etContent);

		if(post != null) {
			url += "&reply[id]=" + post.getId();
		}
		
		//reply[post_id] : 댓글을 달 포스트 id
		if(post_id != 0) {
			url += "&reply[post_id]=" + post_id;
		}
		
		//reply[parent_id] : (댓글의 댓글인 경우에만) 댓글 id
		if(parent_id != 0) {
			url += "&reply[parent_id]=" + parent_id;
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

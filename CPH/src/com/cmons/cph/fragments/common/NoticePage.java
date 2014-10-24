package com.cmons.cph.fragments.common;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForShop;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Notice;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.ToastUtils;

/**
 * @author HyungGunKim
 */
public class NoticePage extends CmonsFragmentForShop {

	private Notice notice;
	
	private TextView tvTitleText;
	private EditText etTitle;
	private TextView tvTitle;
	private Button btnPush;
	private TextView tvContentText;
	private EditText etContent;
	private TextView tvContent;
	
	private boolean isEdit;
	private boolean needPush;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.noticePage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.noticePage_ivBg);
		
		tvTitleText = (TextView) mThisView.findViewById(R.id.noticePage_tvTitleText);
		etTitle = (EditText) mThisView.findViewById(R.id.noticePage_etTitle);
		tvTitle = (TextView) mThisView.findViewById(R.id.noticePage_tvTitle);
		btnPush = (Button) mThisView.findViewById(R.id.noticePage_btnPush);
		tvContentText = (TextView) mThisView.findViewById(R.id.noticePage_tvContentText);
		etContent = (EditText) mThisView.findViewById(R.id.noticePage_etContent);
		tvContent = (TextView) mThisView.findViewById(R.id.noticePage_tvContent);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			
			if(getArguments().containsKey("notice")) {
				notice = (Notice) getArguments().getSerializable("notice");
				
				if(notice != null) {
					needPush = notice.getNeed_push() == 1;
				}
			}
			
			if(getArguments().containsKey("isEdit")) {
				isEdit = getArguments().getBoolean("isEdit");
			}
			
			if(getArguments().containsKey("title")) {
				title = getArguments().getString("title");
			}
		}
		
		LogUtils.log("###NoticePage.setVariables.  " +
				"\nisEdit : " + isEdit +
				"\ntitle : " + title +
				(notice == null? "\nNotice is null" : "Notice is not null, title : " + notice.getTitle()));
		
		if(title == null) {
			title = "공지사항";
		}
		
		checkReadList();
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
		
		if(isEdit) {
			btnPush.setVisibility(View.VISIBLE);
			etTitle.setVisibility(View.VISIBLE);
			tvTitle.setVisibility(View.INVISIBLE);
			etContent.setVisibility(View.VISIBLE);
			tvContent.setVisibility(View.INVISIBLE);
			
			if(needPush) {
				btnPush.setBackgroundResource(R.drawable.notice_notification_btn_b);
			} else {
				btnPush.setBackgroundResource(R.drawable.notice_notification_btn_a);
			}
			
			if(notice != null) {
				etTitle.setText(notice.getTitle());
				etContent.setText(notice.getContent());
			}
		} else{
			btnPush.setVisibility(View.INVISIBLE);
			etTitle.setVisibility(View.INVISIBLE);
			tvTitle.setVisibility(View.VISIBLE);
			etContent.setVisibility(View.INVISIBLE);
			tvContent.setVisibility(View.VISIBLE);
			
			if(notice != null) {
				tvTitle.setText(notice.getTitle());
				tvContent.setText(notice.getContent());
			}
		}
	}

	@Override
	public void setListeners() {

		if(isEdit) {
			titleBar.getBtnSubmit().setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					submit();
				}
			});
		}
		
		btnPush.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				needPush = !needPush;
				
				if(needPush) {
					btnPush.setBackgroundResource(R.drawable.notice_notification_btn_b);
				} else {
					btnPush.setBackgroundResource(R.drawable.notice_notification_btn_a);
				}
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		int p = ResizeUtils.getSpecificLength(10);
		
		//tvTitleText.
		rp = (RelativeLayout.LayoutParams) tvTitleText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(98);
		FontUtils.setFontSize(tvTitleText, 30);
		tvTitleText.setPadding(p, 0, p, p);
		
		//etTitle.
		rp = (RelativeLayout.LayoutParams) etTitle.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(550);
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontSize(etTitle, 24);
		
		//tvTitle.
		rp = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontSize(tvTitle, 24);
		
		//btnPush.
		rp = (RelativeLayout.LayoutParams) btnPush.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvContentText.
		rp = (RelativeLayout.LayoutParams) tvContentText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(98);
		FontUtils.setFontSize(tvContentText, 30);
		tvContentText.setPadding(p, 0, p, p);
		
		FontUtils.setFontSize(etContent, 24);
		etContent.setPadding(p, p, p, p);
		
		FontUtils.setFontSize(tvContent, 24);
		tvContent.setPadding(p, p, p, p);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_common_notice;
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

//////////////////// Custom methods.
	
	public void submit() {

		/*
		http://cph.minsangk.com/wholesales/notices/save
		?notice[title]=%EA%B8%B4%EA%80
		&notice[content]=%EB%82%B4%EC%%9A%A
		&notice[need_push]=1
	
		http://cph.minsangk.com/wholesales/notices/save
		?notice[id]=1
		&notice[title]=%EA%B8%B4%EA%B8%89%EA%B3%B5%EC%A7%80%EC%88%98%EC%A0%95%EC%82%AC%ED%95%AD
		*/
		try {
			String url = CphConstants.BASE_API_URL + "wholesales/notices/save" +
					"?notice[title]=" + URLEncoder.encode(etTitle.getText().toString(), "utf-8") +
					"&notice[content]=" + URLEncoder.encode(etContent.getText().toString(), "utf-8") +
					"&notice[need_push]=" + (needPush? 1 : 0);
					
			if(notice != null) {
				url += "&notice[id]=" + notice.getId();
			}
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("NoticePage.submit.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToWriteNotice);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("NoticePage.submit.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 1) {
							ToastUtils.showToast(R.string.complete_writeNotice);
							mActivity.closePageWithRefreshPreviousPage();
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						ToastUtils.showToast(R.string.failToWriteNotice);
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						ToastUtils.showToast(R.string.failToWriteNotice);
						LogUtils.trace(oom);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void checkReadList() {

		if(notice == null) {
			return;
		}
		
		String readListString = SharedPrefsUtils.getStringFromPrefs(CphConstants.PREFS_NOTICE, "readList");

		if(readListString != null) {

			if(!readListString.contains(notice.getId() + ",")) {
				readListString += notice.getId() + ",";
			}
		} else {
			readListString = notice.getId() + ",";
		}
		
		SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_NOTICE, "readList", readListString);
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.setting_bg2;
	}
}

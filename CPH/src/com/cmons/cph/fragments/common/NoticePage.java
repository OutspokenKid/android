package com.cmons.cph.fragments.common;

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
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;

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
				LogUtils.log("###NoticePage.setVariables.  notice is not null. title : " + notice.getTitle());
			} else {
				LogUtils.log("###NoticePage.setVariables.  notice is null");
			}
			
			if(getArguments().containsKey("isEdit")) {
				isEdit = getArguments().getBoolean("isEdit");
			}
			
			if(getArguments().containsKey("title")) {
				title = getArguments().getString("title");
			}
		}
		
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

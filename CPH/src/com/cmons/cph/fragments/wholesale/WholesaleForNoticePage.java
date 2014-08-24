package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Notice;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;

/**
 * 전체 공지사항(관리자)인지 매장 공지사항인지 isAppNotice로 구분. 
 * isEdit인 경우 작성 또는 수정(notice==null?작성:수정).
 * 
 * @author HyungGunKim
 *
 */
public class WholesaleForNoticePage extends CmonsFragmentForWholesale {

	private Notice notice;
	
	private TextView tvTitleText;
	private EditText etTitle;
	private TextView tvTitle;
	private Button btnPush;
	private TextView tvContentText;
	private EditText etContent;
	private TextView tvContent;
	
	private boolean isEdit;
	private boolean isAppNotice;
	private boolean needPush;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleNoticePage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.wholesaleNoticePage_ivBg);
		
		tvTitleText = (TextView) mThisView.findViewById(R.id.wholesaleNoticePage_tvTitleText);
		etTitle = (EditText) mThisView.findViewById(R.id.wholesaleNoticePage_etTitle);
		tvTitle = (TextView) mThisView.findViewById(R.id.wholesaleNoticePage_tvTitle);
		btnPush = (Button) mThisView.findViewById(R.id.wholesaleNoticePage_btnPush);
		tvContentText = (TextView) mThisView.findViewById(R.id.wholesaleNoticePage_tvContentText);
		etContent = (EditText) mThisView.findViewById(R.id.wholesaleNoticePage_etContent);
		tvContent = (TextView) mThisView.findViewById(R.id.wholesaleNoticePage_tvContent);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			
			if(getArguments().containsKey("notice")) {
				notice = (Notice) getArguments().getSerializable("notice");
			}
			
			if(getArguments().containsKey("isEdit")) {
				isEdit = getArguments().getBoolean("isEdit");
			}
			
			if(getArguments().containsKey("isAppNotice")) {
				isAppNotice = getArguments().getBoolean("isAppNotice");
			}
		}
		
		if(isEdit) {
			
			if(notice == null) {
				title = "공지사항 추가";
			} else {
				title = "공지사항 수정";
			}
		} else {
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

		return R.layout.fragment_wholesale_notice;
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

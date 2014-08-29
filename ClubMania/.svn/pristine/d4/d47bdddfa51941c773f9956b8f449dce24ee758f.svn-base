package com.zonecomms.clubmania;

import java.net.URLEncoder;

import org.json.JSONObject;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.zonecomms.common.utils.AppInfoUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleEditText;
import com.zonecomms.clubmania.classes.ZoneConstants;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class CooperateActivity extends Activity {

	private View titleBar;
	private TextView tvTitle;
	private Button btnSubmit;
	private HoloStyleEditText etTitle;
	private HoloStyleEditText etCompany;
	private HoloStyleEditText etPhoneNumber;
	private HoloStyleEditText etEmail;
	private HoloStyleEditText etContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cooperate);
		
		bindViews();
		setVariables();
		setSizes();
		setListeners();
	}
	
	public void bindViews() {
		
		titleBar = findViewById(R.id.cooperateActivity_titleBar);
		tvTitle = (TextView) findViewById(R.id.cooperateActivity_tvTitle);
		btnSubmit = (Button) findViewById(R.id.cooperateActivity_btnSubmit);
		etTitle = (HoloStyleEditText) findViewById(R.id.cooperateActivity_etTitle);
		etCompany = (HoloStyleEditText) findViewById(R.id.cooperateActivity_etCompany);
		etPhoneNumber = (HoloStyleEditText) findViewById(R.id.cooperateActivity_etPhoneNumber);
		etEmail = (HoloStyleEditText) findViewById(R.id.cooperateActivity_etEmail);
		etContent = (HoloStyleEditText) findViewById(R.id.cooperateActivity_etContent);
	}
	
	public void setVariables() {
		
		etTitle.setHint(R.string.hintForCooperate1);
		etCompany.setHint(R.string.hintForCooperate2);
		etPhoneNumber.setHint(R.string.hintForCooperate3);
		etEmail.setHint(R.string.hintForCooperate4);
		etContent.setHint(R.string.hintForCooperate5);
		
		etTitle.setInputType(InputType.TYPE_CLASS_TEXT);
		etCompany.setInputType(InputType.TYPE_CLASS_TEXT);
		etPhoneNumber.setInputType(InputType.TYPE_CLASS_PHONE);
		etEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
		etContent.setInputType(InputType.TYPE_CLASS_TEXT);
		
		etContent.getEditText().setSingleLine(false);
		etContent.getEditText().setMaxLines(20);
	}
	
	public void setSizes() {
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 90, titleBar, 1, 0, null);
		
		FontInfo.setFontSize(tvTitle, 30);
		FontInfo.setFontStyle(tvTitle, FontInfo.BOLD);
		tvTitle.setPadding(ResizeUtils.getSpecificLength(100), 0, 0, 0);
		
		ResizeUtils.viewResize(70, 70, btnSubmit, 1, Gravity.CENTER_VERTICAL, new int[]{0, 0, 30, 0});
		
		ResizeUtils.viewResize(540, 70, etTitle, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 90, 0, 0});
		ResizeUtils.viewResize(540, 70, etCompany, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 40, 0, 0});
		ResizeUtils.viewResize(540, 70, etPhoneNumber, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 40, 0, 0});
		ResizeUtils.viewResize(540, 70, etEmail, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 40, 0, 0});
		ResizeUtils.viewResize(540, 100, etContent, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 10, 0, 0});
		
		FontInfo.setFontSize(etTitle.getEditText(), 22);
		FontInfo.setFontSize(etCompany.getEditText(), 22);
		FontInfo.setFontSize(etPhoneNumber.getEditText(), 22);
		FontInfo.setFontSize(etEmail.getEditText(), 22);
		FontInfo.setFontSize(etContent.getEditText(), 22);
	}

	public void setListeners() {
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(etTitle.getEditText().getText() != null
						&& !TextUtils.isEmpty(etTitle.getEditText().getText().toString())
						
						&& etCompany.getEditText().getText() != null
						&& !TextUtils.isEmpty(etCompany.getEditText().getText().toString())
						
						&& etPhoneNumber.getEditText().getText() != null
						&& !TextUtils.isEmpty(etPhoneNumber.getEditText().getText().toString())
						
						&& etEmail.getEditText().getText() != null
						&& !TextUtils.isEmpty(etEmail.getEditText().getText().toString())
						
						&& etContent.getEditText().getText() != null
						&& !TextUtils.isEmpty(etContent.getEditText().getText().toString())) {
					ToastUtils.showToast(R.string.submittingToServer);
					submit();
				} else {
					ToastUtils.showToast(R.string.inputAll);
				}
			}
		});
	}
	
	public void submit() {
		
		try {
			String title = URLEncoder.encode(etTitle.getEditText().getText().toString(), "UTF-8");
			String company = URLEncoder.encode(etCompany.getEditText().getText().toString(), "UTF-8");
			String phoneNumber = URLEncoder.encode(etPhoneNumber.getEditText().getText().toString(), "UTF-8");
			String email = URLEncoder.encode(etEmail.getEditText().getText().toString(), "UTF-8");
			String content = URLEncoder.encode(etContent.getEditText().getText().toString(), "UTF-8");
			
			String url = ZoneConstants.BASE_URL + "common/partnerqa/write" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&p_type=2" +
					"&p_title=" + title +
					"&p_name=" + company +
					"&p_phone=" + phoneNumber +
					"&p_email=" + email +
					"&p_qa=" + content;
			
			AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
				
				@Override
				public void onErrorRaised(String url, Exception e) {
				}
				
				@Override
				public void onCompleted(String url, String result) {

					try {
						JSONObject objJSON = new JSONObject(result);
						
						if(objJSON.has("errorCode") && objJSON.getInt("errorCode") == 1) {
							ToastUtils.showToast(R.string.submitCompleted);
							finish();
						} else {
							ToastUtils.showToast(R.string.failToSubmitCooperate);
						}
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			};
			
			AsyncStringDownloader.download(url, null, ocl);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.slide_in_from_top, R.anim.slide_out_to_bottom);
	}
}

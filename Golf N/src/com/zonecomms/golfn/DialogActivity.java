package com.zonecomms.golfn;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.zonecomms.golfn.classes.ApplicationManager;
import com.zonecomms.golfn.classes.SetupClass;
import com.zonecomms.golfn.fragments.ArticlePage;
import com.zonecomms.golfn.fragments.GetheringPage;
import com.zonecomms.golfn.fragments.MessagePage;
import com.zonecomms.golfn.fragments.PostPage;

public class DialogActivity extends Activity {

	private TextView tvContent;
	private LinearLayout buttonLinear;
	private TextView button1, button2;
	
	private String push_msg;
	private String uriString;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED 
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		SetupClass.setupApplication(this);
		setContentView(R.layout.activity_dialog);
		
		bindViews();
		setVariables();
		setSizes();
		setListener();
	}
	
	public void bindViews() {
		
		tvContent = (TextView) findViewById(R.id.dialogActivity_tvContent);
		buttonLinear = (LinearLayout) findViewById(R.id.dialogActivity_buttonLinear);
		button1 = (TextView) findViewById(R.id.dialogActivity_button1);
		button2 = (TextView) findViewById(R.id.dialogActivity_button2);
	}
	
	public void setVariables() {
		
		if(getIntent() != null) {
			push_msg = getIntent().getStringExtra("push_msg");
			uriString = getIntent().getStringExtra("uriString");
		}
		
		tvContent.setText(push_msg);
	}
	
	public void setSizes() {
		
		ResizeUtils.viewResize(440, 140, tvContent, 1, Gravity.CENTER_HORIZONTAL, null, new int[]{130, 0, 20, 0});
		ResizeUtils.viewResize(440, 60, buttonLinear, 1, Gravity.CENTER_HORIZONTAL, null);
		
		FontInfo.setFontSize(tvContent, 22);
		FontInfo.setFontSize(button1, 26);
		FontInfo.setFontSize(button2, 26);
	}
	
	@SuppressLint("ClickableViewAccessibility") 
	public void setListener() {
		
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				handleIntent();
			}
		});
		
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				try {
					GCMIntentService.showNotification(DialogActivity.this, push_msg, uriString);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
				finish();
			}
		});
		
		button1.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch(event.getAction()) {
				
				case MotionEvent.ACTION_DOWN:
					buttonLinear.setBackgroundResource(R.drawable.btn_push_2);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					buttonLinear.setBackgroundResource(R.drawable.btn_push_1);
					break;
				}
				
				return false;
			}
		});
		
		button2.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				switch(event.getAction()) {
				
				case MotionEvent.ACTION_DOWN:
					buttonLinear.setBackgroundResource(R.drawable.btn_push_3);
					break;
				case MotionEvent.ACTION_UP:
				case MotionEvent.ACTION_CANCEL:
					buttonLinear.setBackgroundResource(R.drawable.btn_push_1);
					break;
				}
				
				return false;
			}
		});
	}
	
	public void handleIntent() {
		
		if(getIntent() == null) {
			return;
		}
		
		boolean handled = false;
		
		try {
			//앱이 실행 중인 경우.
			if(ApplicationManager.getFragmentsSize() != 0) {
				String msg_type = getIntent().getStringExtra("msg_type"); 
				String member_id = getIntent().getStringExtra("member_id"); 
				String post_member_id = getIntent().getStringExtra("member_id");
				int spot_nid = getIntent().getIntExtra("spot_nid", 0);
				String sb_id = getIntent().getStringExtra("sb_id");
				
				//해당 메세지 페이지가 열려있는 경우.
				if(msg_type.equals("010")
						&& member_id != null
						&& ApplicationManager.getTopFragment() instanceof MessagePage
						&& post_member_id.equals(((MessagePage) ApplicationManager.getTopFragment()).getFriend_member_id())
						) {
					ApplicationManager.getInstance().getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							ApplicationManager.getTopFragment().onRefreshPage();
						}
					});
					handled = true;
					
				//해당 글 상세페이지가 열려있는 경우.
				} else if((msg_type.equals("021") || msg_type.equals("022"))
						&& ApplicationManager.getTopFragment() instanceof PostPage
						&& ((PostPage)ApplicationManager.getTopFragment()).getSpotNid() == spot_nid) {
					ApplicationManager.getInstance().getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							((PostPage)ApplicationManager.getTopFragment()).setNeedToShowBottom(true);
							ApplicationManager.getTopFragment().onRefreshPage();
						}
					});
					handled = true;
					
				//해당 기사 상세페이지가 열려있는 경우.
				} else if(msg_type.equals("031")
						&& ApplicationManager.getTopFragment() instanceof ArticlePage
						&& ((ArticlePage)ApplicationManager.getTopFragment()).getSpotNid() == spot_nid) {
					ApplicationManager.getInstance().getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							((ArticlePage)ApplicationManager.getTopFragment()).setNeedToShowBottom(true);
							ApplicationManager.getTopFragment().onRefreshPage();
						}
					});
					return;
				
				//해당 모임 페이지가 열려있는 경우,
				} else if(msg_type.equals("050")
						&& ApplicationManager.getTopFragment() instanceof GetheringPage
						&& sb_id != null
						&& sb_id.equals(((GetheringPage)ApplicationManager.getTopFragment()).getSb_id())) {
					ApplicationManager.getInstance().getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							//새로고침(인덱스0).
							((GetheringPage)ApplicationManager.getTopFragment()).showMenu(0);
							ToastUtils.showToast(push_msg);
						}
					});
					handled = true;
				}
			}
			
			if(!handled && uriString != null) {
				Intent intent = new Intent(this, IntentHandlerActivity.class);
				intent.setData(Uri.parse(uriString));
				startActivity(intent);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		finish();
	}
}

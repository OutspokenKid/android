package com.zonecomms.golfn;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.outspoken_kid.activities.RecyclingActivity;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.SoftKeyboardDetector;
import com.outspoken_kid.views.SoftKeyboardDetector.OnHiddenSoftKeyboardListener;
import com.outspoken_kid.views.SoftKeyboardDetector.OnShownSoftKeyboardListener;
import com.outspoken_kid.views.holo_dark.HoloStyleButton;
import com.outspoken_kid.views.holo_dark.HoloStyleEditText;
import com.zonecomms.golfn.classes.ZoneConstants;

public class WriteForFleaActivity extends RecyclingActivity {

	public static final int NUMBER_OF_TEXTS = 7;
	private final int[] MUST_INPUT = new int[] {0};

	private int[] scrollOffsets;
	private String[] inputTexts;
	
	private SoftKeyboardDetector skd;
	private ScrollView scrollView;
	private TextView tvTitle;
	private TextView[] textViews;
	private TextView tvSpec;
	private HoloStyleEditText[] editTexts;
	private HoloStyleButton btnNext;
	
	private boolean shownKeyboard;
	private int board_id;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writeforflea);

		if(savedInstanceState != null 
				&& savedInstanceState.containsKey("inputTexts")
				&& savedInstanceState.getStringArray("inputTexts") != null) {
			inputTexts = savedInstanceState.getStringArray("inputTexts");

			if(inputTexts.length == NUMBER_OF_TEXTS) {
				
				for (int i = 0; i < NUMBER_OF_TEXTS; i++) {
					
					if(inputTexts[i] != null
							&& editTexts[i] != null) {
						editTexts[i].getEditText().setText(inputTexts[i]);
					}
				}
			}
		}
		
		bindViews();
		setVariables();
		createPage();
		setSizes();
		setListeners();
	}
	
	@Override
	protected void bindViews() {
		
		skd = (SoftKeyboardDetector) findViewById(R.id.writeForFleaActivity_softKeyboardDetector);
		scrollView = (ScrollView) findViewById(R.id.writeForFleaActivity_scrollView);
		tvTitle = (TextView) findViewById(R.id.writeForFleaActivity_tvTitle);
		
		textViews = new TextView[NUMBER_OF_TEXTS];
		editTexts = new HoloStyleEditText[NUMBER_OF_TEXTS];
		
		Resources res = getResources();
		int resId = 0;
		for(int i=0; i<NUMBER_OF_TEXTS; i++) {
			
			try {
				resId = res.getIdentifier("writeForFleaActivity_tvInput" + (i+1), "id", 
						"com.zonecomms." + ZoneConstants.PAPP_ID);
				textViews[i] = (TextView) findViewById(resId);
				
				resId = res.getIdentifier("writeForFleaActivity_etInput" + (i+1), "id", 
						"com.zonecomms." + ZoneConstants.PAPP_ID);
				editTexts[i] = (HoloStyleEditText) findViewById(resId);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
		
		tvSpec = (TextView) findViewById(R.id.writeForFleaActivity_tvSpec);
		btnNext = (HoloStyleButton) findViewById(R.id.writeForFleaActivity_btnNext);
	}

	@Override
	protected void setVariables() {

		board_id = getIntent().getIntExtra("board_id", 5);
		int titleResId = 0;
		
		switch(board_id) {
		
		case 5:
			titleResId = R.string.title_market1;
			break;
			
		case 6:
			titleResId = R.string.title_market2;
			break;
			
		case 7:
			titleResId = R.string.title_market3;
			break;
			
		case 8:
			titleResId = R.string.title_market4;
			break;
		}
		tvTitle.setText(getString(titleResId) + " " + getString(R.string.write));
		
		Resources res = getResources();
		int resId = 0;
		
		for(int i=0; i<NUMBER_OF_TEXTS; i++) {
			try {
				resId = res.getIdentifier("textForWriteFlea" + (i+1), "string", 
						"com.zonecomms." + ZoneConstants.PAPP_ID);
				textViews[i].setText(getString(resId));
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
				
		tvSpec.setText(R.string.textForWriteFlea_spec);
		btnNext.setText(R.string.next);

		int total = 0;
		scrollOffsets = new int[NUMBER_OF_TEXTS];
		for(int i=0; i<NUMBER_OF_TEXTS; i++) {
			scrollOffsets[i] = total;
			total += ResizeUtils.getSpecificLength(i==1?170:130);
		}
		
		if(getIntent() != null && getIntent().hasExtra("mustValues")) {
			String[] values = getIntent().getStringArrayExtra("mustValues");

			int size = values.length;
			for (int i = 0; i < size; i++) {
				try {
					editTexts[i].getEditText().setText(values[i]);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		}
	}

	@Override
	protected void createPage() {
		
		for(int i=0; i<NUMBER_OF_TEXTS; i++) { 
			textViews[i].setVisibility(View.GONE);
			editTexts[i].setVisibility(View.GONE);
		}
		
		//제품명
		//희망가격
		//물품 상세설명
		textViews[0].setVisibility(View.VISIBLE);
		textViews[5].setVisibility(View.VISIBLE);
		textViews[6].setVisibility(View.VISIBLE);
		editTexts[0].setVisibility(View.VISIBLE);
		editTexts[5].setVisibility(View.VISIBLE);
		editTexts[6].setVisibility(View.VISIBLE);
		
		switch(board_id) {

		//헤드로프트
		//샤프트강도
		//샤프트중량
		case 5:
		case 6:
			textViews[1].setVisibility(View.VISIBLE);
			textViews[2].setVisibility(View.VISIBLE);
			textViews[3].setVisibility(View.VISIBLE);
			editTexts[1].setVisibility(View.VISIBLE);
			editTexts[2].setVisibility(View.VISIBLE);
			editTexts[3].setVisibility(View.VISIBLE);
			break;

		//샤프트강도
		//샤프트중량
		//아이언 번호구성
		case 7:
			textViews[2].setVisibility(View.VISIBLE);
			textViews[3].setVisibility(View.VISIBLE);
			textViews[4].setVisibility(View.VISIBLE);
			editTexts[2].setVisibility(View.VISIBLE);
			editTexts[3].setVisibility(View.VISIBLE);
			editTexts[4].setVisibility(View.VISIBLE);
			break;
			
		case 8:
			break;
		}
	}

	@Override
	protected void setListeners() {

		skd.setOnShownKeyboardListener(new OnShownSoftKeyboardListener() {
			
			@Override
			public void onShownSoftKeyboard() {
				shownKeyboard = true;
			}
		});

		skd.setOnHiddenKeyboardListener(new OnHiddenSoftKeyboardListener() {
			
			@Override
			public void onHiddenSoftKeyboard() {
				shownKeyboard = false;
			}
		});
		
		btnNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				try {
					boolean checked = true;
					String text = getString(R.string.needMustInput);
					int mustInputIndex = -1;
					Resources res = getResources();
					
					int size = MUST_INPUT.length;
					for(int i=0; i<size; i++) {
						
						int index = MUST_INPUT[i];
						if(editTexts[index].getEditText().getText() == null
								|| StringUtils.isEmpty(editTexts[index].getEditText().getText())) {
							text += "\n" + getString(res.getIdentifier("textForWriteFlea" + (index+1), 
									"string", "com.zonecomms." + ZoneConstants.PAPP_ID));
							
							if(mustInputIndex == -1) {
								mustInputIndex = index;
							}
							
							checked = false;
						}
					}
					
					if(checked) {
						showWriteActivity();
					} else {
						ToastUtils.showToast(text);
						focusToEditText(mustInputIndex);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
	}

	@Override
	protected void setSizes() {

		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 90, tvTitle, 1, 0, null);
		FontInfo.setFontSize(tvTitle, 36);
		FontInfo.setFontStyle(tvTitle, FontInfo.BOLD);
		
		for(int i=0; i<NUMBER_OF_TEXTS; i++) {
			ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 40, textViews[i], 1, 0, new int[]{(i==0?20:60), (i==1?0:20), 0, 0});
			ResizeUtils.viewResize(540, 70, editTexts[i], 1, Gravity.CENTER_HORIZONTAL, null);
			
			FontInfo.setFontSize(textViews[i], 30);
			FontInfo.setFontSize(editTexts[i].getEditText(), 26);
		}
		
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 40, tvSpec, 1, 0, new int[]{20, 20, 0, 0});
		FontInfo.setFontSize(tvSpec, 30);
		ResizeUtils.viewResize(540, 80, btnNext, 1, Gravity.CENTER_HORIZONTAL, new int[]{0, 20, 0, 20});
	}

	@Override
	protected void downloadInfo() {
	}

	@Override
	protected void setPage() {
	}

	@Override
	protected int getContentViewId() {

		return R.id.writeForFleaActivity_scrollView;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		outState.putStringArray("inputTexts", inputTexts);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == ZoneConstants.REQUEST_WRITE && resultCode == RESULT_OK) {
			setResult(RESULT_OK, data);
			finish();
		}
	}
	
	public void focusToEditText(final int index) {

		if(!shownKeyboard) {
			SoftKeyboardUtils.showKeyboard(WriteForFleaActivity.this, editTexts[index]);
		}
		
		tvTitle.postDelayed(new Runnable() {

			@Override
			public void run() {
				editTexts[index].getEditText().requestFocus();
				scrollView.smoothScrollTo(0, scrollOffsets[index]);
			}
		}, 500);
	}
	
	public void showWriteActivity() {
		
		inputTexts = new String[NUMBER_OF_TEXTS];
		String printText = "WriteForFleaActivity.showWriteActivity.";
		String text = null;
		
		for(int i=0; i<NUMBER_OF_TEXTS; i++) {
			
			if(editTexts[i].getEditText().getText() == null
					|| StringUtils.isEmpty(editTexts[i].getEditText().getText().toString())) {
				inputTexts[i] = "";
				text = null;
			} else {
				text = editTexts[i].getEditText().getText().toString();
				inputTexts[i] = text;
				printText += "\nindex : " + i + ",  text : " + text;
			}
		}
		LogUtils.log(printText);

		Intent intent = new Intent(this, WriteActivity.class);

		int titleResId = 0;
		
		switch(board_id) {
		
		case 5:
			titleResId = R.string.title_market1;
			break;
			
		case 6:
			titleResId = R.string.title_market2;
			break;
			
		case 7:
			titleResId = R.string.title_market3;
			break;
			
		case 8:
			titleResId = R.string.title_market4;
			break;
		}
		
		intent.putExtra("titleText", getString(titleResId));
		intent.putExtra("board_id", board_id);
		intent.putExtra("isGethering", false);
		intent.putExtra("mustValues", inputTexts);
		intent.putExtra("needPosting", false);
		
		if(getIntent() != null) {
			if(getIntent().hasExtra("isEdit")) {
				intent.putExtra("isEdit", true);
			}
			
			if(getIntent().hasExtra("spot_nid")) {
				intent.putExtra("spot_nid", getIntent().getIntExtra("spot_nid", 0));	
			}
			
			if(getIntent().hasExtra("content")) {
				intent.putExtra("content", getIntent().getStringExtra("content"));
			}
			
			if(getIntent().hasExtra("imageUrls")) {
				intent.putExtra("imageUrls", getIntent().getStringArrayExtra("imageUrls"));
			}
		}
		
		startActivityForResult(intent, ZoneConstants.REQUEST_WRITE);
	}
}
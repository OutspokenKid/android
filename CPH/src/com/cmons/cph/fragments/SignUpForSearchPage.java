package com.cmons.cph.fragments;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.classes.BaseFragmentForSignUp;
import com.cmons.classes.CphConstants;
import com.cmons.cph.R;
import com.cmons.cph.SignUpActivity;
import com.cmons.cph.models.Retail;
import com.cmons.cph.models.Shop;
import com.cmons.cph.models.Wholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SignUpForSearchPage extends BaseFragmentForSignUp {

	public static final int SEARCH_TYPE_NAME = 0;
	public static final int SEARCH_TYPE_PHONE = 1;
	public static final int SEARCH_TYPE_REG = 2;
	public static final int SEARCH_TYPE_LOCATION = 3;
	public static final int SEARCH_TYPE_LOCATION_FLOOR = 4;
	public static final int SEARCH_TYPE_LOCATION_LINE = 5;
	public static final int SEARCH_TYPE_LOCATION_ROOM = 6;
	
	private ArrayList<Shop> shops = new ArrayList<Shop>();
	private ArrayList<String> strings = new ArrayList<String>();
	
	private TitleBar titleBar;
	
	private TextView tvCompanyName;
	private EditText etCompanyName;
	private Button btnCompanyName;
	
	private TextView tvCompanyPhone;
	private EditText etCompanyPhone;
	private Button btnCompanyPhone;
	
	private TextView tvCompanyLocation;
	private Button btnCompanyFloor;
	private Button btnCompanyLine;
	private Button btnCompanyRoomNumber;
	private Button btnCompanyLocation;
	
	private TextView tvCompanyRegistration;
	private EditText etCompanyRegistration;
	private Button btnCompanyRegistration;

	private View cover;
	private FrameLayout listFrame;
	private ListView listView;
	private Button btnClose;
	
	private WholesaleListAdapter listAdapter;
	
	private int type;
	private int searchType;
	private String categoryString;
	private ArrayList<Floor> floors = new ArrayList<Floor>();
	private Floor currentFloor;
	private Line currentLine;
	
	private AlphaAnimation aaIn, aaOut;
	
	@Override
	protected void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForSearchPage_titleBar);
		
		tvCompanyName = (TextView) mThisView.findViewById(R.id.signUpForSearchPage_tvCompanyName);
		etCompanyName = (EditText) mThisView.findViewById(R.id.signUpForSearchPage_etCompanyName);
		btnCompanyName = (Button) mThisView.findViewById(R.id.signUpForSearchPage_btnCompanyName);
		
		tvCompanyPhone = (TextView) mThisView.findViewById(R.id.signUpForSearchPage_tvCompanyPhone);
		etCompanyPhone = (EditText) mThisView.findViewById(R.id.signUpForSearchPage_etCompanyPhone);
		btnCompanyPhone = (Button) mThisView.findViewById(R.id.signUpForSearchPage_btnCompanyPhone);
		
		tvCompanyLocation = (TextView) mThisView.findViewById(R.id.signUpForSearchPage_tvCompanyLocation);
		btnCompanyFloor = (Button) mThisView.findViewById(R.id.signUpForSearchPage_btnCompanyFloor);
		btnCompanyLine = (Button) mThisView.findViewById(R.id.signUpForSearchPage_btnCompanyLine);
		btnCompanyRoomNumber = (Button) mThisView.findViewById(R.id.signUpForSearchPage_btnCompanyRoomNumber);
		btnCompanyLocation = (Button) mThisView.findViewById(R.id.signUpForSearchPage_btnCompanyLocation);
		
		tvCompanyRegistration = (TextView) mThisView.findViewById(R.id.signUpForSearchPage_tvCompanyRegistration);
		etCompanyRegistration = (EditText) mThisView.findViewById(R.id.signUpForSearchPage_etCompanyRegistration);
		btnCompanyRegistration = (Button) mThisView.findViewById(R.id.signUpForSearchPage_btnCompanyRegistration);
		
		cover = mThisView.findViewById(R.id.signUpForSearchPage_cover);
		listFrame = (FrameLayout) mThisView.findViewById(R.id.signUpForSearchPage_listFrame);
		listView = (ListView) mThisView.findViewById(R.id.signUpForSearchPage_listView);
		btnClose = (Button) mThisView.findViewById(R.id.signUpForSearchPage_btnClose);
	}

	@Override
	protected void setVariables() {

		if(getArguments() != null) {
			
			if(getArguments().containsKey("type")) {
				type = getArguments().getInt("type");
			}
			
			if(getArguments().containsKey("categoryString")) {
				categoryString = getArguments().getString("categoryString");
			}
		}
	}

	@Override
	protected void createPage() {

		titleBar.addBackButton(R.drawable.btn_back_category, 162, 92);
		titleBar.setTitleText(R.string.searchCompany);
		
		View bottomBlank = new View(mContext);
		RelativeLayout.LayoutParams rp = new RelativeLayout.LayoutParams(10, ResizeUtils.getSpecificLength(110));
		rp.addRule(RelativeLayout.BELOW, R.id.signUpForSearchPage_etCompanyRegistration);
		bottomBlank.setLayoutParams(rp);
		((RelativeLayout) mThisView.findViewById(R.id.signUpForSearchPage_relativeSearchInfo)).addView(bottomBlank);
		
		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);
		
		aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(300);
		
		listAdapter = new WholesaleListAdapter();
		listView.setAdapter(listAdapter);
		
		downloadLocation();
	}

	@Override
	protected void setListeners() {

		titleBar.getBackButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				getActivity().getSupportFragmentManager().popBackStack();
			}
		});
		
		OnClickListener ocl = new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				showPopup(Integer.parseInt(v.getTag().toString()));
			}
		};
		
		btnCompanyName.setTag("" + SEARCH_TYPE_NAME);
		btnCompanyPhone.setTag("" + SEARCH_TYPE_PHONE);
		btnCompanyLocation.setTag("" + SEARCH_TYPE_LOCATION);
		btnCompanyRegistration.setTag("" + SEARCH_TYPE_REG);
		
		btnCompanyName.setOnClickListener(ocl);
		btnCompanyPhone.setOnClickListener(ocl);
		btnCompanyLocation.setOnClickListener(ocl);
		btnCompanyRegistration.setOnClickListener(ocl);

		btnCompanyFloor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				showPopup(SEARCH_TYPE_LOCATION_FLOOR);
			}
		});
		
		btnCompanyLine.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				showPopup(SEARCH_TYPE_LOCATION_LINE);
			}
		});
		
		btnCompanyRoomNumber.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				showPopup(SEARCH_TYPE_LOCATION_ROOM);
			}
		});

		cover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hidePopup();
			}
		});
	
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hidePopup();
			}
		});
	
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View child, int position,
					long id) {
				
				switch (searchType) {
				case SEARCH_TYPE_LOCATION_FLOOR:
					btnCompanyFloor.setText(strings.get(position));
					currentFloor = floors.get(position);
					currentFloor.printFloor();
					hidePopup();
					break;
					
				case SEARCH_TYPE_LOCATION_LINE:
					btnCompanyLine.setText(strings.get(position));
					currentLine = currentFloor.lines.get(position);
					hidePopup();
					break;
					
				case SEARCH_TYPE_LOCATION_ROOM:
					btnCompanyRoomNumber.setText(strings.get(position));
					hidePopup();
					break;

				default:
					mActivity.showPersonalPage(type, shops.get(position), categoryString);
					break;
				}
			}
		});
	}

	@Override
	protected void setSizes() {

		//titleBar.
		titleBar.getLayoutParams().height = ResizeUtils.getSpecificLength(96);
		
		RelativeLayout.LayoutParams rp = null;
		
		//shadow.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForSearchPage_titleShadow).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(14);
		
		// tvCompanyName.
		rp = (RelativeLayout.LayoutParams) tvCompanyName.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.leftMargin = ResizeUtils.getSpecificLength(70);
		rp.topMargin = ResizeUtils.getSpecificLength(74);
		
		// etCompanyName.
		rp = (RelativeLayout.LayoutParams) etCompanyName.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(426);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		// btnCompanyName.
		rp = (RelativeLayout.LayoutParams) btnCompanyName.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(136);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		// tvCompanyPhone.
		rp = (RelativeLayout.LayoutParams) tvCompanyPhone.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(90);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
		// etCompanyPhone.
		rp = (RelativeLayout.LayoutParams) etCompanyPhone.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(426);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		// btnCompanyPhone.
		rp = (RelativeLayout.LayoutParams) btnCompanyPhone.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(136);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);

		//도매인 경우.
		if(type < SignUpActivity.BUSINESS_RETAIL_OFFLINE) {
			
			// tvCompanyLocation.
			rp = (RelativeLayout.LayoutParams) tvCompanyLocation.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(90);
			rp.topMargin = ResizeUtils.getSpecificLength(30);
			rp.bottomMargin = ResizeUtils.getSpecificLength(20);
			
			// btnCompanyFloor.
			rp = (RelativeLayout.LayoutParams) btnCompanyFloor.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(126);
			rp.height = ResizeUtils.getSpecificLength(92);
			
			rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
					R.id.signUpForSearchPage_tvFloor).getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(126);
			
			// btnCompanyLine.
			rp = (RelativeLayout.LayoutParams) btnCompanyLine.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(126);
			rp.height = ResizeUtils.getSpecificLength(92);
			rp.leftMargin = ResizeUtils.getSpecificLength(20);
			
			rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
					R.id.signUpForSearchPage_tvLine).getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(126);
			
			// btnCompanyRoomNumber.
			rp = (RelativeLayout.LayoutParams) btnCompanyRoomNumber.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(126);
			rp.height = ResizeUtils.getSpecificLength(92);
			rp.leftMargin = ResizeUtils.getSpecificLength(20);
			
			rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
					R.id.signUpForSearchPage_tvRoomNumber).getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(126);
			
			// btnCompanyLocation.
			rp = (RelativeLayout.LayoutParams) btnCompanyLocation.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(136);
			rp.height = ResizeUtils.getSpecificLength(92);
			rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
			// tvCompanyRegistration.
			rp = (RelativeLayout.LayoutParams) tvCompanyRegistration.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(90);
			rp.topMargin = ResizeUtils.getSpecificLength(242);
			
		//소매인 경우.
		} else {
			// tvCompanyRegistration.
			rp = (RelativeLayout.LayoutParams) tvCompanyRegistration.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(90);
			rp.topMargin = ResizeUtils.getSpecificLength(30);
		}
		
		// etCompanyRegistration.
		rp = (RelativeLayout.LayoutParams) etCompanyRegistration.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(426);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		// btnCompanyRegistration.
		rp = (RelativeLayout.LayoutParams) btnCompanyRegistration.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(136);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		//ListFrame.
		rp = (RelativeLayout.LayoutParams) listFrame.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(626);
		rp.height = ResizeUtils.getSpecificLength(695);
		listFrame.setPadding(ResizeUtils.getSpecificLength(5), 
				ResizeUtils.getSpecificLength(6), 
				ResizeUtils.getSpecificLength(6), 
				ResizeUtils.getSpecificLength(6));

		//ListView.
		FrameLayout.LayoutParams fp = (FrameLayout.LayoutParams) listView.getLayoutParams();
		fp.topMargin = ResizeUtils.getSpecificLength(101);
		
		//btnClose.
		fp = (FrameLayout.LayoutParams) btnClose.getLayoutParams();
		fp.width = ResizeUtils.getSpecificLength(97);
		fp.height = fp.width;

		FontUtils.setFontSize(tvCompanyName, 34);
		FontUtils.setFontSize(tvCompanyPhone, 34);
		FontUtils.setFontSize(tvCompanyLocation, 34);
		FontUtils.setFontSize(tvCompanyRegistration, 34);
		
		FontUtils.setFontSize(etCompanyName, 30);
		FontUtils.setFontSize(etCompanyPhone, 30);
		FontUtils.setFontAndHintSize(etCompanyRegistration, 30, 24);
		
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForSearchPage_ivCopyright).getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(352);
		rp.height = ResizeUtils.getSpecificLength(18);
		rp.bottomMargin = ResizeUtils.getSpecificLength(20);
	}

	@Override
	protected int getXmlResId() {

		return R.layout.fragment_sign_up_search;
	}

	@Override
	public void onRefreshPage() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onBackKeyPressed() {

		if(listFrame.getVisibility() == View.VISIBLE 
				|| cover.getVisibility() == View.VISIBLE) {
			return true;
		}
		
		return false;
	}

	public void downloadLocation() {
		
		String url = CphConstants.BASE_API_URL + "wholesales/location_parts";
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("SignUpForSearchPage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("SignUpForSearchPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					Iterator<?> floorKeys = objJSON.keys();
					
					while(floorKeys.hasNext()) {
						Floor floor = new Floor();
						floor.floorName = floorKeys.next().toString();
						
						JSONObject objLine = objJSON.getJSONObject(floor.floorName);
						Iterator<?> lineKeys = objLine.keys();
						
						while(lineKeys.hasNext()) {
							
							Line line = new Line();
							line.lineName = lineKeys.next().toString();
							JSONArray arJSON = objLine.getJSONArray(line.lineName);
							
							int size = arJSON.length();
							for(int i=0; i<size; i++) {
								line.roomNumbers.add(arJSON.getString(i));
							}
							
							floor.lines.add(line);
						}
						
						floors.add(floor);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void showPopup(int searchType) {

		try {
			if(searchType < SEARCH_TYPE_LOCATION) {
				
				EditText targetEditText = null;
				
				switch(searchType) {
				
				case SEARCH_TYPE_NAME:
					targetEditText = etCompanyName;
					break;
					
				case SEARCH_TYPE_PHONE:
					targetEditText = etCompanyPhone;
					break;
					
				case SEARCH_TYPE_REG:
					targetEditText = etCompanyRegistration;
					break;
				}
				
				if(targetEditText == null 
						|| targetEditText.getText() == null 
						|| StringUtils.isEmpty(targetEditText.getText().toString())) {
					ToastUtils.showToast(R.string.wrongSearchKeyword);
					return;
				}
			} else if(searchType == SEARCH_TYPE_LOCATION) {

				if(btnCompanyRoomNumber == null
						|| btnCompanyRoomNumber.getText() == null
						|| StringUtils.isEmpty(btnCompanyRoomNumber.getText().toString())) {
					ToastUtils.showToast(R.string.wrongSearchKeyword);
					return;
				}
			} else {
				
				if(floors.size() == 0) {
					return;
				}
			}
			
			if(listFrame.getVisibility() != View.VISIBLE) {
				listFrame.setVisibility(View.VISIBLE);
				listFrame.startAnimation(aaIn);
			}
			
			if(cover.getVisibility() != View.VISIBLE) {
				cover.setVisibility(View.VISIBLE);
				cover.startAnimation(aaIn);
			}
			
			this.searchType = searchType;
			
			switch (searchType) {
			
			case SEARCH_TYPE_NAME:
			case SEARCH_TYPE_PHONE:
			case SEARCH_TYPE_LOCATION:
			case SEARCH_TYPE_REG:
				downloadShops();
				break;

			case SEARCH_TYPE_LOCATION_FLOOR:
				int size = floors.size();
				for(int i=0; i<size; i++) {
					strings.add(floors.get(i).floorName);
				}
				listAdapter.notifyDataSetChanged();
				break;
				
			case SEARCH_TYPE_LOCATION_LINE:
				
				if(currentFloor != null) {
					LogUtils.log("###SignUpForSearchPage.showPopup.  lines.size : " + currentFloor.lines.size());
					
					size = currentFloor.lines.size();
					for(int i=0; i<size; i++) {
						strings.add(currentFloor.lines.get(i).lineName);
						LogUtils.log("###where.showPopup.  add line : " + strings.get(i));
					}
					listAdapter.notifyDataSetChanged();
				} else {
					LogUtils.log("###SignUpForSearchPage.showPopup.  6");
				}
				break;
				
			case SEARCH_TYPE_LOCATION_ROOM:

				if(currentLine != null) {
					size = currentLine.roomNumbers.size();
					for(int i=0; i<size; i++) {
						strings.add(currentLine.roomNumbers.get(i));
					}
					listAdapter.notifyDataSetChanged();
				}
				break;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void downloadShops() {
		
		String url = CphConstants.BASE_API_URL;
		
		if(type < SignUpActivity.BUSINESS_RETAIL_OFFLINE) {
			url += "wholesales/search/";
		} else{
			url += "retails/search/";
		}
		
		try {
			
			switch(searchType) {
			
			case SEARCH_TYPE_NAME:
				url += "name?keyword=" + URLEncoder.encode(etCompanyName.getText().toString(), "utf-8");
				break;
				
			case SEARCH_TYPE_PHONE:
				url += "phone_number?keyword=" + URLEncoder.encode(etCompanyPhone.getText().toString(), "utf-8");
				break;
				
			case SEARCH_TYPE_LOCATION:
				url += "location?keyword=" + 
						URLEncoder.encode(btnCompanyFloor.getText().toString() +
						btnCompanyLine.getText().toString() +
						btnCompanyRoomNumber.getText().toString(), "utf-8");
				break;
				
			case SEARCH_TYPE_REG:
				url += "corp_reg_number?keyword=" + URLEncoder.encode(etCompanyRegistration.getText().toString(), "utf-8");
				break;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("SignUpForSearchPage.onError." + "\nurl : " + url);
				hidePopup();
				ToastUtils.showToast(R.string.failToSearchShop);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("SignUpForSearchPage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					JSONArray arJSON = null;
					
					if(type < SignUpActivity.BUSINESS_RETAIL_OFFLINE) {
						arJSON = objJSON.getJSONArray("wholesales");
					} else {
						arJSON = objJSON.getJSONArray("retails");
					}
					
					int size = arJSON.length();
					
					if(size != 0) {
						
						if(type < SignUpActivity.BUSINESS_RETAIL_OFFLINE) {
							for(int i=0; i<size; i++) {
								shops.add(new Wholesale(arJSON.getJSONObject(i)));
							}
						} else {
							for(int i=0; i<size; i++) {
								shops.add(new Retail(arJSON.getJSONObject(i)));
							}
						}
						
						listAdapter.notifyDataSetChanged();
					} else {
						ToastUtils.showToast(R.string.noResult_searchWholsale);
						listFrame.postDelayed(new Runnable() {

							@Override
							public void run() {
								
								hidePopup();
							}
						}, 1000);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					hidePopup();
					ToastUtils.showToast(R.string.failToSearchShop);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					hidePopup();
					ToastUtils.showToast(R.string.failToSearchShop);
				}
			}
		});
	}
	
	public void hidePopup() {
		
		if(listFrame.getVisibility() == View.VISIBLE) {
			listFrame.setVisibility(View.INVISIBLE);
			listFrame.startAnimation(aaOut);
		}
		
		if(cover.getVisibility() == View.VISIBLE) {
			cover.setVisibility(View.INVISIBLE);
			cover.startAnimation(aaOut);
		}
		
		strings.clear();
		shops.clear();
		listAdapter.notifyDataSetChanged();
	}

////////////////////////// Classes.
	
	public class WholesaleListAdapter extends BaseAdapter {

		@Override
		public int getCount() {

			if(searchType < SEARCH_TYPE_LOCATION_FLOOR) {
				return shops.size();
			} else {
				return strings.size();
			}
		}
	
		@Override
		public Object getItem(int arg0) {
	
			if(searchType < SEARCH_TYPE_LOCATION_FLOOR) {
				return shops.get(arg0);
			} else {
				return strings.get(arg0);
			}
		}
	
		@Override
		public long getItemId(int arg0) {
	
			return arg0;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup arg2) {
	
			if(convertView == null) {
				TextView tvWholesale = new TextView(mContext);
				tvWholesale.setTextColor(Color.BLACK);
				tvWholesale.setLayoutParams(new AbsListView.LayoutParams(
						LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(100)));
				tvWholesale.setPadding(ResizeUtils.getSpecificLength(40), 0, 
						ResizeUtils.getSpecificLength(40), 0);
				tvWholesale.setGravity(Gravity.CENTER_VERTICAL);
				FontUtils.setFontSize(tvWholesale, 30);
				
				convertView = tvWholesale;
			}
			
			LogUtils.log("###where.getView.  position : " + position + ", searchType : " + searchType + ", strings.size : " + strings.size());

			switch(searchType) {
			
			case SEARCH_TYPE_NAME:
			case SEARCH_TYPE_PHONE:
			case SEARCH_TYPE_LOCATION:
			case SEARCH_TYPE_REG:
				((TextView)convertView).setText(shops.get(position).getName() + 
						"(" + shops.get(position).getPhone_number() + ")");
				break;
				
			case SEARCH_TYPE_LOCATION_FLOOR:
			case SEARCH_TYPE_LOCATION_LINE:
			case SEARCH_TYPE_LOCATION_ROOM:
				((TextView)convertView).setText(strings.get(position));
				break;
			}
			
			return convertView;
		}
	}
	
	public class Floor {
		
		public String floorName;
		public ArrayList<Line> lines = new ArrayList<Line>();
		
		public void printFloor() {
			
			String logString = "\nfloorName : " + floorName;
			
			int size = lines.size();
			for(int i=0; i<size; i++) {
				logString += "\n" + i + " : " + lines.get(i).lineName;
			}
			
			LogUtils.log("###Floor.printFloor.  ======================" +
					logString);
		}
	}
	
	public class Line {
		
		public String lineName;
		public ArrayList<String> roomNumbers = new ArrayList<String>();
	}
}

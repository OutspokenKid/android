package com.cmons.cph.fragments.signup;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.SignUpActivity;
import com.cmons.cph.classes.CmonsFragmentForSignUp;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Floor;
import com.cmons.cph.models.Line;
import com.cmons.cph.models.Retail;
import com.cmons.cph.models.Shop;
import com.cmons.cph.models.Wholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class SignUpForSearchPage extends CmonsFragmentForSignUp {

	public static final int SEARCH_TYPE_NAME = 0;
	public static final int SEARCH_TYPE_PHONE = 1;
	public static final int SEARCH_TYPE_REG = 2;
	public static final int SEARCH_TYPE_LOCATION = 3;
	public static final int SEARCH_TYPE_LOCATION_FLOOR = 4;
	public static final int SEARCH_TYPE_LOCATION_LINE = 5;
	public static final int SEARCH_TYPE_LOCATION_ROOM = 6;
	
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

	private ArrayList<Shop> shops = new ArrayList<Shop>();
	private ArrayList<Floor> floors = new ArrayList<Floor>();
	
	private int type;
	private boolean isDownloadLocation;
	private String categoryString;
	private Floor currentFloor;
	private Line currentLine;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.signUpForSearchPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.signUpForSearchPage_ivBg);
		
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
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			
			if(getArguments().containsKey("type")) {
				type = getArguments().getInt("type");
			}
			
			if(getArguments().containsKey("categoryString")) {
				categoryString = getArguments().getString("categoryString");
			}
		}
		
		title = getString(R.string.searchCompany); 
	}

	@Override
	public void createPage() {
		
		titleBar.getBackButton().setVisibility(View.VISIBLE);
		
		downloadLocation();
	}

	@Override
	public void setListeners() {
		
		btnCompanyName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(etCompanyName.getText() == null
						|| StringUtils.isEmpty(etCompanyName.getText().toString())) {
					ToastUtils.showToast(R.string.wrongSearchKeyword);
					return;
				}
				
				try {
					String keyword = URLEncoder.encode(etCompanyName.getText().toString(), "utf-8");
					searchShop(SEARCH_TYPE_NAME, keyword);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
		
		btnCompanyPhone.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(etCompanyPhone.getText() == null
						|| StringUtils.isEmpty(etCompanyPhone.getText().toString())) {
					ToastUtils.showToast(R.string.wrongSearchKeyword);
					return;
				}
				
				try {
					String keyword = URLEncoder.encode(etCompanyPhone.getText().toString(), "utf-8");
					searchShop(SEARCH_TYPE_PHONE, keyword);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
		
		btnCompanyLocation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				if(btnCompanyRoomNumber.getText() == null
						|| StringUtils.isEmpty(btnCompanyRoomNumber.getText().toString())) {
					ToastUtils.showToast(R.string.wrongSearchKeyword);
					return;
				}
				
				try {
					String keyword = URLEncoder.encode(btnCompanyFloor.getText().toString() +
							btnCompanyLine.getText().toString() +
							btnCompanyRoomNumber.getText().toString(), "utf-8");
					searchShop(SEARCH_TYPE_LOCATION, keyword);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
		
		btnCompanyRegistration.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(etCompanyRegistration.getText() == null
						|| StringUtils.isEmpty(etCompanyRegistration.getText().toString())) {
					ToastUtils.showToast(R.string.wrongSearchKeyword);
					return;
				}
				
				try {
					String keyword = URLEncoder.encode(etCompanyRegistration.getText().toString(), "utf-8");
					searchShop(SEARCH_TYPE_REG, keyword);
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});

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
	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;
		
		//shadow.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(R.id.signUpForSearchPage_titleShadow).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(14);
		
		// tvCompanyName.
		rp = (RelativeLayout.LayoutParams) tvCompanyName.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		rp.leftMargin = ResizeUtils.getSpecificLength(20);
		
		// etCompanyName.
		rp = (RelativeLayout.LayoutParams) etCompanyName.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(550);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		// btnCompanyName.
		rp = (RelativeLayout.LayoutParams) btnCompanyName.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		// tvCompanyPhone.
		rp = (RelativeLayout.LayoutParams) tvCompanyPhone.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		
		// etCompanyPhone.
		rp = (RelativeLayout.LayoutParams) etCompanyPhone.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(550);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		// btnCompanyPhone.
		rp = (RelativeLayout.LayoutParams) btnCompanyPhone.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);

		//도매인 경우.
		if(type < SignUpActivity.BUSINESS_RETAIL_OFFLINE) {
			
			// tvCompanyLocation.
			rp = (RelativeLayout.LayoutParams) tvCompanyLocation.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(120);
			rp.bottomMargin = ResizeUtils.getSpecificLength(20);
			
			// btnCompanyFloor.
			rp = (RelativeLayout.LayoutParams) btnCompanyFloor.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(183);
			rp.height = ResizeUtils.getSpecificLength(92);
			
			// btnCompanyLine.
			rp = (RelativeLayout.LayoutParams) btnCompanyLine.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(183);
			rp.height = ResizeUtils.getSpecificLength(92);
			
			// btnCompanyRoomNumber.
			rp = (RelativeLayout.LayoutParams) btnCompanyRoomNumber.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(183);
			rp.height = ResizeUtils.getSpecificLength(92);
			
			// btnCompanyLocation.
			rp = (RelativeLayout.LayoutParams) btnCompanyLocation.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(92);
		
			// tvCompanyRegistration.
			rp = (RelativeLayout.LayoutParams) tvCompanyRegistration.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(120);
			rp.topMargin = ResizeUtils.getSpecificLength(212);
			
		//소매인 경우.
		} else {
			// tvCompanyRegistration.
			rp = (RelativeLayout.LayoutParams) tvCompanyRegistration.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(120);
		}
		
		// etCompanyRegistration.
		rp = (RelativeLayout.LayoutParams) etCompanyRegistration.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(550);
		rp.height = ResizeUtils.getSpecificLength(92);
		
		// btnCompanyRegistration.
		rp = (RelativeLayout.LayoutParams) btnCompanyRegistration.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);

		FontUtils.setFontSize(tvCompanyName, 34);
		FontUtils.setFontSize(tvCompanyPhone, 34);
		FontUtils.setFontSize(tvCompanyLocation, 34);
		FontUtils.setFontSize(tvCompanyRegistration, 34);
		FontUtils.setFontSize(etCompanyName, 30);
		FontUtils.setFontSize(etCompanyPhone, 30);
		FontUtils.setFontSize(btnCompanyFloor, 26);
		FontUtils.setFontSize(btnCompanyLine, 26);
		FontUtils.setFontSize(btnCompanyRoomNumber, 26);
		FontUtils.setFontAndHintSize(etCompanyName, 30, 24);
		FontUtils.setFontAndHintSize(etCompanyPhone, 30, 24);
		FontUtils.setFontAndHintSize(etCompanyRegistration, 30, 24);
		
	}
	
	@Override
	public int getContentViewId() {

		return R.layout.fragment_sign_up_search;
	}

	@Override
	public void refreshPage() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public boolean onBackPressed() {
		
		return false;
	}
	
	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {

		if(isDownloadLocation) {
			isDownloadLocation = false;
			
			try {
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
					
					floor.setItemCode(CphConstants.ITEM_FLOOR);
					floors.add(floor);
				}
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (OutOfMemoryError oom) {
				LogUtils.trace(oom);
			}
		} else {
			try {
				JSONArray arJSON = null;
				
				if(type < SignUpActivity.BUSINESS_RETAIL_OFFLINE) {
					arJSON = objJSON.getJSONArray("wholesales");
				} else {
					arJSON = objJSON.getJSONArray("retails");
				}
				
				int size = arJSON.length();
				
				if(size != 0) {

					String[] strings = new String[size];
					
					if(type < SignUpActivity.BUSINESS_RETAIL_OFFLINE) {
						
						for(int i=0; i<size; i++) {
							Wholesale wholesale = new Wholesale(arJSON.getJSONObject(i));
							wholesale.setItemCode(CphConstants.ITEM_SHOP);
							wholesale.setType(Shop.TYPE_WHOLESALE);
							shops.add(wholesale);
							strings[i] = wholesale.getName() + "(" + wholesale.getLocation() + ")";
						}
					} else if(type < SignUpActivity.BUSINESS_RETAIL_ONLINE) {
						
						for(int i=0; i<size; i++) {
							Retail retail = new Retail(arJSON.getJSONObject(i));
							retail.setType(Shop.TYPE_RETAIL_OFFLINE);
							shops.add(retail);
							strings[i] = retail.getName() + "(" + retail.getAddress() + ")";
						}
						
					} else {
						
						for(int i=0; i<size; i++) {
							Retail retail = new Retail(arJSON.getJSONObject(i));
							retail.setType(Shop.TYPE_RETAIL_ONLINE);
							shops.add(retail);
							strings[i] = retail.getName() +"(" + retail.getMall_url() + ")";
						}
					}

					mActivity.showSelectDialog(null, strings, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {

							if(type < SignUpActivity.BUSINESS_RETAIL_OFFLINE) {
								mActivity.showPersonalPage(type, shops.get(which), categoryString);
							} else if(type < SignUpActivity.BUSINESS_RETAIL_ONLINE) {
								mActivity.showPersonalPage(type, shops.get(which), categoryString);
							} else {
							}
						}
					});
				} else {
					ToastUtils.showToast(R.string.noResult_searchWholsale);
				}
			} catch (Exception e) {
				LogUtils.trace(e);
				ToastUtils.showToast(R.string.failToSearchShop);
			} catch (OutOfMemoryError oom) {
				LogUtils.trace(oom);
				ToastUtils.showToast(R.string.failToSearchShop);
			}
		}
		
		return false;
	}
	
	public void downloadLocation() {
		
		isDownloadLocation = true;
		url = CphConstants.BASE_API_URL + "wholesales/location_parts?num=0";
		super.downloadInfo();
	}

	public void searchShop(int searchType, String keyword) {
		
		url = CphConstants.BASE_API_URL;
		
		if(type < SignUpActivity.BUSINESS_RETAIL_OFFLINE) {
			url += "wholesales/search/";
		} else{
			url += "retails/search/";
		}
		
		try {
			
			switch(searchType) {
			
			case SEARCH_TYPE_NAME:
				url += "name";
				break;
				
			case SEARCH_TYPE_PHONE:
				url += "phone_number";
				break;
				
			case SEARCH_TYPE_LOCATION:
				url += "location";
				break;
				
			case SEARCH_TYPE_REG:
				url += "corp_reg_number";
				break;
			}
			
			url += "?keyword=" + keyword + "&num=0";
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		super.downloadInfo();
	}

	public void showPopup(int searchType) {
		
		try {
			int size = 0;
			
			switch(searchType) {
			
			case SEARCH_TYPE_LOCATION_FLOOR:
				size = floors.size();
				final String[] floorStrings = new String[size];
				for(int i=0; i<size; i++) {
					floorStrings[i] = floors.get(i).floorName;
				}
				
				mActivity.showSelectDialog(null, floorStrings, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						btnCompanyFloor.setText(floorStrings[which]);
						btnCompanyLine.setText(R.string.line);
						btnCompanyRoomNumber.setText(R.string.roomNumber);
						currentFloor = (Floor)floors.get(which);
					}
				});
				break;
				
			case SEARCH_TYPE_LOCATION_LINE:
				size = currentFloor.lines.size();
				final String[] lineStrings = new String[size];
				for(int i=0; i<size; i++) {
					lineStrings[i] = currentFloor.lines.get(i).lineName;
				}
				
				mActivity.showSelectDialog(null, lineStrings, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						btnCompanyLine.setText(lineStrings[which]);
						btnCompanyRoomNumber.setText(R.string.roomNumber);
						currentLine = currentFloor.lines.get(which);
					}
				});
				break;
				
			case SEARCH_TYPE_LOCATION_ROOM:
				size = currentLine.roomNumbers.size();
				final String[] roomNumberStrings = new String[size];
				for(int i=0; i<size; i++) {
					roomNumberStrings[i] = currentLine.roomNumbers.get(i);
				}
				
				mActivity.showSelectDialog(null, roomNumberStrings, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {

						btnCompanyRoomNumber.setText(roomNumberStrings[which]);
					}
				});
				break;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.bg2;
	}
}

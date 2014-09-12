package com.cmons.cph.fragments.retail;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import com.cmons.cph.classes.CmonsFragmentForRetail;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Floor;
import com.cmons.cph.models.Line;
import com.cmons.cph.models.Shop;
import com.cmons.cph.models.Wholesale;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class RetailForAddPartner extends CmonsFragmentForRetail {

	public static final int SEARCH_TYPE_NAME = 0;
	public static final int SEARCH_TYPE_PHONE = 1;
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
	
	private ArrayList<Shop> shops = new ArrayList<Shop>();
	private ArrayList<Floor> floors = new ArrayList<Floor>();
	
	private boolean isDownloadLocation;
	private Floor currentFloor;
	private Line currentLine;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.retailForAddPartnerPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.retailForAddPartnerPage_ivBg);
		
		tvCompanyName = (TextView) mThisView.findViewById(R.id.retailForAddPartnerPage_tvCompanyName);
		etCompanyName = (EditText) mThisView.findViewById(R.id.retailForAddPartnerPage_etCompanyName);
		btnCompanyName = (Button) mThisView.findViewById(R.id.retailForAddPartnerPage_btnCompanyName);
		
		tvCompanyPhone = (TextView) mThisView.findViewById(R.id.retailForAddPartnerPage_tvCompanyPhone);
		etCompanyPhone = (EditText) mThisView.findViewById(R.id.retailForAddPartnerPage_etCompanyPhone);
		btnCompanyPhone = (Button) mThisView.findViewById(R.id.retailForAddPartnerPage_btnCompanyPhone);
		
		tvCompanyLocation = (TextView) mThisView.findViewById(R.id.retailForAddPartnerPage_tvCompanyLocation);
		btnCompanyFloor = (Button) mThisView.findViewById(R.id.retailForAddPartnerPage_btnCompanyFloor);
		btnCompanyLine = (Button) mThisView.findViewById(R.id.retailForAddPartnerPage_btnCompanyLine);
		btnCompanyRoomNumber = (Button) mThisView.findViewById(R.id.retailForAddPartnerPage_btnCompanyRoomNumber);
		btnCompanyLocation = (Button) mThisView.findViewById(R.id.retailForAddPartnerPage_btnCompanyLocation);
	}

	@Override
	public void setVariables() {

		title = getString(R.string.addPartner); 
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

		// tvCompanyLocation.
		rp = (RelativeLayout.LayoutParams) tvCompanyLocation.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(120);
		
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

		FontUtils.setFontSize(tvCompanyName, 34);
		FontUtils.setFontSize(tvCompanyPhone, 34);
		FontUtils.setFontSize(tvCompanyLocation, 34);
		FontUtils.setFontSize(etCompanyName, 30);
		FontUtils.setFontSize(etCompanyPhone, 30);
		FontUtils.setFontSize(btnCompanyFloor, 26);
		FontUtils.setFontSize(btnCompanyLine, 26);
		FontUtils.setFontSize(btnCompanyRoomNumber, 26);
		FontUtils.setFontAndHintSize(etCompanyName, 30, 24);
		FontUtils.setFontAndHintSize(etCompanyPhone, 30, 24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_retail_addpartner;
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
					
					Collections.sort(floor.lines, new Comparator<Line>(){

						@Override
						public int compare(Line l1, Line l2) {
				            return l1.lineName.compareToIgnoreCase(l2.lineName);
						}
				    });
					
					floor.setItemCode(CphConstants.ITEM_FLOOR);
					floors.add(floor);
				}
				
				Collections.sort(floors, new Comparator<Floor>(){

					@Override
					public int compare(Floor f1, Floor f2) {
			            return f1.floorName.compareToIgnoreCase(f2.floorName);
					}
			    });
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (OutOfMemoryError oom) {
				LogUtils.trace(oom);
			}
		} else {
			try {
				JSONArray arJSON = null;
				arJSON = objJSON.getJSONArray("wholesales");
				
				int size = arJSON.length();
				
				if(size != 0) {

					String[] strings = new String[size];
					
					for(int i=0; i<size; i++) {
						Wholesale wholesale = new Wholesale(arJSON.getJSONObject(i));
						wholesale.setItemCode(CphConstants.ITEM_SHOP);
						wholesale.setType(Shop.TYPE_WHOLESALE);
						shops.add(wholesale);
						strings[i] = wholesale.getName() + "(" + wholesale.getLocation() + ")";
					}

					mActivity.showSelectDialog(null, strings, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {

							addPartner(shops.get(which));
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

	@Override
	public int getBgResourceId() {

		return R.drawable.bg2;
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
	
	public void downloadLocation() {
		
		isDownloadLocation = true;
		url = CphConstants.BASE_API_URL + "wholesales/location_parts?num=0";
		super.downloadInfo();
	}

	//retails/search/phone_number?keyword=010-3993-7001
	public void searchShop(int searchType, String keyword) {
		
		url = CphConstants.BASE_API_URL + "wholesales/search/";
		
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

	public void addPartner(Shop shop) {
		
		String url = CphConstants.BASE_API_URL + "retails/customers/request" +
				"?wholesale_id=" + shop.getId();
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("RetailForAddPartner.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToAddPartner);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("RetailForAddPartner.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);
					
					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_addPartner);
						mActivity.closeTopPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToAddPartner);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToAddPartner);
				}
			}
		});
	}
}

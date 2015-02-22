package com.byecar.classes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

import com.byecar.byecarplusfordealer.MainActivity;
import com.byecar.fragments.CarDetailPage;
import com.byecar.fragments.SearchResultPage;
import com.byecar.fragments.dealer.MainPage;
import com.byecar.models.Car;
import com.outspoken_kid.classes.BaseFragment;
import com.outspoken_kid.utils.LogUtils;

public class SocketDataHandler {

	MainActivity activity;
	
	public SocketDataHandler(MainActivity activity) {
		
		this.activity = activity;
	}

	/*
	arJSON : 밀린 정보들.
	[
		arJSON[0] : 첫번째 아이템.
		{
			(Car + message_at)
		},
		
		arJSON[1] : 두번째 아이템.
		{
			(Car + message_at)
		}
	]
	*/
	public void onLastData(String event, JSONArray arJSON) {
		
		if(arJSON == null || activity == null) {
			return;
		}
		
		try {
			Log.i("socket", "###SocketDataHander.onLastData"
					+ "\n arJSON : " + arJSON.toString());

			ArrayList<Car> cars = new ArrayList<Car>();
			
			for(int i=0; i<arJSON.length(); i++) {
				JSONObject objInfo = arJSON.getJSONObject(i);
				cars.add(new Car(objInfo.getJSONObject("data")));
			}
			
		} catch (Exception e) {
			LogUtils.trace(e);
			Log.w("socket", "###SocketDataHandler.onLastData.  parseError"
					+ "\n event : " + event);
		}
	}

	/*
	objJSON : 추가된 정보.
	{
		(Car + message_at)
	}
	*/
	public void onData(final String event, final JSONObject objJSON) {
		
		if(objJSON == null || activity == null) {
			return;
		}
		
		try {
			Log.i("socket", "###SocketDataHander.onData"
					+ "\n objJSON : " + objJSON.toString());
			
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					
					Car car = new Car(objJSON);
					
					int size = activity.getFragmentsSize();
					for(int i=0; i<size; i++) {
						BaseFragment bf = activity.getFragmentAt(i);
						
						if(bf instanceof MainPage) {
							((MainPage) bf).bidStatusChanged(event, car);
							
						} else if(bf instanceof CarDetailPage) {
							((CarDetailPage) bf).bidStatusChanged(event, car);
							
						} else if(bf instanceof SearchResultPage) {
							((SearchResultPage) bf).bidStatusChanged(event, car);
						}
					}
				}
			});
			
			/*
			auction_begun : 경매가 시작되는 물건이 있는 경우.
			bid_price_updated : 경매 매물의 가격 변화가 있는 경우.
			auction_held : 관리자에 의해 보류된 경우.
			selection_time_ended : 딜러 선택 시간이 종료된 경우 (유찰).
			dealer_selected : 유저가 딜러를 선택한 경우 (낙찰).
			last_messages : 전송받지 못한 밀린 액션 전송
			*/
			
		} catch (Exception e) {
			LogUtils.trace(e);
			Log.w("socket", "###SocketDataHandler.onData.  parseError"
					+ "\n event : " + event);
		}
	}
}

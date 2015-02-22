package com.byecar.classes;

import android.annotation.TargetApi;
import android.os.Build;
import android.widget.ListView;

import com.byecar.models.Car;
import com.outspoken_kid.utils.AppInfoUtils;


public abstract class BCPAuctionableListFragment extends BCPFragment {

	protected int startIndex;
	protected ListView listView;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void bidStatusChanged(String event, Car car) {

		if(event == null) {
			return;
		}

		car.setItemCode(BCPConstants.ITEM_CAR_BID);
		
		//경매가 시작되는 물건이 있는 경우.
		if(event.equals("auction_begun")) {
			//시간에 맞게 추가.
			boolean added = false;
			
			//기존에 같은 매물이 있다면 최신화.
			for(int i=startIndex; i<models.size(); i++) {
				
				if(((Car)models.get(i)).getId() == car.getId()) {
					((Car)models.get(i)).copyValuesFromNewItem(car);
					adapter.notifyDataSetChanged();
					return;
				}
			}
			
			for(int i=startIndex; i<models.size(); i++) {
				//새로운 매물보다 더 늦게 끝나는 매물이 있거나 경매중이 아닌 매물이 있다면 그 위에 삽입.
				if(((Car)models.get(i)).getBid_until_at() < car.getBid_begin_at()
						|| ((Car)models.get(i)).getStatus() != Car.STATUS_BIDDING) {
					models.add(i, car);
					adapter.notifyDataSetChanged();
					added = true;
					
					if(i <= listView.getFirstVisiblePosition()) {
						
						if(AppInfoUtils.checkMinVersionLimit(android.os.Build.VERSION_CODES.HONEYCOMB)) {
							listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition() + 1, 
									listView.getChildAt(0).getTop(), 
									0);
						} else {
							listView.smoothScrollToPosition(listView.getFirstVisiblePosition() + 1);
						}
					}
					break;
				}
			}
			
			if(!added) {
				models.add(car);
				adapter.notifyDataSetChanged();
			}
			
		//경매 매물의 가격 변화가 있는 경우.
		} else if(event.equals("bid_price_updated")) {
			//해당 매물 수정.
			for(int i=startIndex; i<models.size(); i++) {
			
				if(((Car)models.get(i)).getId() == car.getId()) {
					((Car)models.get(i)).copyValuesFromNewItem(car);
					adapter.notifyDataSetChanged();
				}
			}
			
		//관리자에 의해 보류된 경우.
		} else if(event.equals("auction_held")) {
			//해당 매물 삭제.
			
			for(int i=startIndex; i<models.size(); i++) {
				
				if(((Car)models.get(i)).getId() == car.getId()) {
					models.remove(i);
					adapter.notifyDataSetChanged();
					
					if(i <= listView.getFirstVisiblePosition()
							&& listView.getFirstVisiblePosition() > 0) {
						
						if(AppInfoUtils.checkMinVersionLimit(android.os.Build.VERSION_CODES.HONEYCOMB)) {
							listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition() - 1, 
									listView.getChildAt(0).getTop(),
									0);
						} else {
							listView.smoothScrollToPosition(listView.getFirstVisiblePosition() - 1);
						}
					}
					break;
				}
			}
			
		//딜러 선택 시간이 종료된 경우 (유찰).
		//유저가 딜러를 선택한 경우 (낙찰).
		} else if(event.equals("selection_time_ended")
				|| event.equals("dealer_selected")) {
			
			boolean added = false;
			
			//해당 매물 수정 및 위치 변경(리스트에 거래종료인 매물이 있으면 등록순으로 삽입, 거래종료상태인 매물이 없으면 제일 아래로 삽입).
			for(int i=startIndex; i<models.size(); i++) {
				
				if(((Car)models.get(i)).getId() == car.getId()) {
					models.remove(i);
					continue;
				}
				
				//유찰, 거래종료를 상태이고 등록일이 빠른 매물이 있으면 해당 위치에 삽입.
				if(((Car)models.get(i)).getStatus() > Car.STATUS_BID_SUCCESS
						&& ((Car)models.get(i)).getCreated_at() < car.getCreated_at()) {
					models.add(i, car);
					adapter.notifyDataSetChanged();
					added = true;
					
					if(i <= listView.getFirstVisiblePosition()) {
						
						if(AppInfoUtils.checkMinVersionLimit(android.os.Build.VERSION_CODES.HONEYCOMB)) {
							listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition() + 1, 
									listView.getChildAt(0).getTop(), 
									0);
						} else {
							listView.smoothScrollToPosition(listView.getFirstVisiblePosition() + 1);
						}
					}
					break;
				}
			}
			
			if(!added) {
				models.add(car);
				adapter.notifyDataSetChanged();
			}
		} else if(event.equals("time_over")) {
			
			boolean added = false;
			
			//입찰 중 -> 입찰 종료. 
			if(car.getStatus() == Car.STATUS_BID_COMPLETE) {
				//해당 매물 수정 및 위치 변경(리스트에 입찰종료인 매물이 있으면 등록순으로 삽입, 거래종료상태인 매물이 없으면 제일 아래로 삽입).
				for(int i=startIndex; i<models.size(); i++) {
					
					if(((Car)models.get(i)).getId() == car.getId()) {
						models.remove(i);
						continue;
					}
					
					//입찰 종료 상태이고 남은 시간이 적은 매물이 있으면 해당 위치에 삽입.
					if(((Car)models.get(i)).getStatus() == Car.STATUS_BID_SUCCESS
							&& ((Car)models.get(i)).getBid_until_at() > car.getBid_until_at()) {
						models.add(i, car);
						adapter.notifyDataSetChanged();
						added = true;
						
						if(i <= listView.getFirstVisiblePosition()) {
							
							if(AppInfoUtils.checkMinVersionLimit(android.os.Build.VERSION_CODES.HONEYCOMB)) {
								listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition() + 1, 
										listView.getChildAt(0).getTop(), 
										0);
							} else {
								listView.smoothScrollToPosition(listView.getFirstVisiblePosition() + 1);
							}
						}
						break;
					}
				}
				
				if(!added) {
					models.add(car);
					adapter.notifyDataSetChanged();
				}
				
			//입찰 종료 -> 낙찰/유찰/판매 종료.
			} else if(car.getStatus() > Car.STATUS_BID_COMPLETE){
				//리스트에서 제거.
				//리스트에서 낙찰/유찰/판매 종료 상태 검색.
					//있다면 등록 시간에 맞게 삽입.
					//없다면 타입에 맞게 삽입.
				
				//해당 매물 수정 및 위치 변경(리스트에 거래종료인 매물이 있으면 등록순으로 삽입, 거래종료상태인 매물이 없으면 제일 아래로 삽입).
				for(int i=startIndex; i<models.size(); i++) {
					
					if(((Car)models.get(i)).getId() == car.getId()) {
						models.remove(i);
						continue;
					}
					
					//입찰 종료 상태이고 남은 시간이 적은 매물이 있으면 해당 위치에 삽입.
					if(((Car)models.get(i)).getStatus() > Car.STATUS_BID_SUCCESS
							&& ((Car)models.get(i)).getCreated_at() < car.getCreated_at()) {
						models.add(i, car);
						adapter.notifyDataSetChanged();
						added = true;
						
						if(i <= listView.getFirstVisiblePosition()) {
							
							if(AppInfoUtils.checkMinVersionLimit(android.os.Build.VERSION_CODES.HONEYCOMB)) {
								listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition() + 1, 
										listView.getChildAt(0).getTop(), 
										0);
							} else {
								listView.smoothScrollToPosition(listView.getFirstVisiblePosition() + 1);
							}
						}
						break;
					}
				}
				
				if(!added) {
					models.add(car);
					adapter.notifyDataSetChanged();
				}
			}
		} 
	}
}

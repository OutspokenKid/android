package com.byecar.classes;

import android.annotation.TargetApi;
import android.os.Build;
import android.widget.ListView;

import com.byecar.models.Car;
import com.outspoken_kid.utils.AppInfoUtils;
import com.outspoken_kid.utils.LogUtils;

public abstract class BCPAuctionableFragment extends BCPFragment {

	protected int startIndex;
	protected ListView listView;
	
	public abstract void bidStatusChanged(String event, Car car);

	/**
	 * Reorder list with this car.
	 * 
	 * @param car
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void reorderList(int startIndex, Car car) {

		boolean removedItem = false;
		boolean addedItem = false;
		boolean needToScrollUp = false;
		boolean needToScrollDown = false;
		
		//기존에 동일한 차를 갖고 있었다면 삭제.
		for(int i=startIndex; i<models.size(); i++) {
			
			if(((Car)models.get(i)).getId() == car.getId()) {
				models.remove(i);
				removedItem = true;
				
				if(i <= listView.getFirstVisiblePosition()) {
					needToScrollDown = true;
				}
			}
		}
		
		//새로운 경매.
		//경매 -> 경매.
		//경매 -> 딜러 선택.
		if(car.getStatus() <= Car.STATUS_BID_COMPLETE) {
			
			for(int i=startIndex; i<models.size(); i++) {
				
				//다음 상태인 매물이거나 더 빨리 끝나는 매물이 있는 경우.
				if(((Car)models.get(i)).getStatus() > car.getStatus()
						|| (((Car)models.get(i)).getStatus() == car.getStatus()
								 && ((Car)models.get(i)).getBid_until_at() > car.getBid_until_at())) {
					models.add(i, car);
					addedItem = true;
					
					if(i != listView.getLastVisiblePosition()) {
						needToScrollUp = true;
					}
					break;
				}
			}

		//딜러 선택 -> 종료.
		} else {
			
			//경매 진행, 딜러 선택 중인 매물이거나 더 늦게 끝난 매물이 있는 경우.
			for(int i=startIndex; i<models.size(); i++) {
				if(((Car)models.get(i)).getStatus() <= Car.STATUS_BID_COMPLETE
						|| (((Car)models.get(i)).getStatus() == car.getStatus()
								&& ((Car)models.get(i)).getEnd_at() < car.getEnd_at())) {
					models.add(i, car);
					addedItem = true;
					
					if(i <= listView.getFirstVisiblePosition()) {
						needToScrollUp = true;
					}
					break;
				}
			}
		}
		
		if(removedItem && !addedItem
				&& isLastList) {
			models.add(car);
			adapter.notifyDataSetChanged();
			last_priority = car.getPriority();
			isLastList = false;
		} else {
			adapter.notifyDataSetChanged();
		}
		
		//둘다 참이거나 둘다 거짓이면 상쇄.
		if(needToScrollDown == needToScrollUp) {
			//Don't scroll.
		} else if(needToScrollUp) {
//			scrollUp();
		} else if(needToScrollDown) {
//			scrollDown();
		}
	}
	
	/**
	 * If list has same car, update that car and nofify.
	 * 
	 * @param car
	 * @return Return true if list has same car else return false. 
	 */
	public boolean updateSelectedCar(Car car) {
		
		//기존에 같은 매물이 있다면 최신화, return true.
		for(int i=startIndex; i<models.size(); i++) {
			
			if(((Car)models.get(i)).getId() == car.getId()) {
				((Car)models.get(i)).copyValuesFromNewItem(car);
				adapter.notifyDataSetChanged();
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Delete selected car.
	 * 
	 * @param car
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void deleteSelectedCar(Car car) {
		
		//기존에 같은 매물이 있다면 최신화, return true.
		for(int i=startIndex; i<models.size(); i++) {
			
			if(((Car)models.get(i)).getId() == car.getId()) {
				models.remove(i);
				adapter.notifyDataSetChanged();
				
				if(i <= listView.getFirstVisiblePosition()) {
//					scrollDown();
				}
			}
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void scrollUp() {
		
		try {
			//첫번째로 보이는 아이템 이전 아이템이 없다면,
			if(listView.getFirstVisiblePosition() < 0) {
				return;
			}
			
			if(AppInfoUtils.checkMinVersionLimit(android.os.Build.VERSION_CODES.HONEYCOMB)) {
				listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition() - 1, 
						listView.getChildAt(0).getTop(), 0);
			} else {
				listView.smoothScrollToPosition(listView.getFirstVisiblePosition() - 1);
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void scrollDown() {
		
		try {
			//첫번째로 보이는 아이템이 마지막 아이템이라면,
			if(listView.getFirstVisiblePosition() == models.size() - 1) {
				return;
			}
			
			if(AppInfoUtils.checkMinVersionLimit(android.os.Build.VERSION_CODES.HONEYCOMB)) {
				listView.smoothScrollToPositionFromTop(listView.getFirstVisiblePosition() + 1, 
						listView.getChildAt(0).getTop(), 
						0);
			} else {
				listView.smoothScrollToPosition(listView.getFirstVisiblePosition() + 1);
			}

		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
}

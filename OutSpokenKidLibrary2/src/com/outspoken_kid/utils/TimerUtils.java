package com.outspoken_kid.utils;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;

public class TimerUtils {

	private static TimerTask timerTask;
	private static Timer timer;
	private static ArrayList<OnTimeChangedListener> listeners = new ArrayList<OnTimeChangedListener>();

	public static void startTimer(int delay, int period) {
		
		timerTask = new TimerTask() {
			
			@Override
			public void run() {

				for(int i=0; i<listeners.size(); i++) {

					try {
						final OnTimeChangedListener listener = listeners.get(i);
						
						if(listener != null && listener.getActivity() != null) {
							listener.getActivity().runOnUiThread(new Runnable() {
								
								@Override
								public void run() {

									listener.onTimeChanged();
								}
							});
						} else {
							listeners.remove(listener);
							i--;
						}
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (Error e) {
						LogUtils.trace(e);
					}
				}
			}
		};
		
		timer = new Timer();
		timer.schedule(timerTask, delay, period);
	}
	
	public static void addOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {

		LogUtils.log("###TimerUtils.addOnTimeChangedListener.  name : " 
				+ (onTimeChangedListener == null? "null" : onTimeChangedListener.getName()));
		
		if(!listeners.contains(onTimeChangedListener)) {
			listeners.add(onTimeChangedListener);
		}
	}
	
	public static void removeOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
		
		LogUtils.log("###TimerUtils.removeOnTimeChangedListener.  name : " 
				+ (onTimeChangedListener == null? "null" : onTimeChangedListener.getName()));

		listeners.remove(onTimeChangedListener);
	}
	
	public static void cancel() {
	
		if(timer != null) {
			timer.cancel();
		}
	}
	
	public static void clear() {
		
		timerTask.cancel();
		timer.purge();
		listeners.clear();
	}
	
//////////////////// Interfaces.
	
	public interface OnTimeChangedListener {
		
		public String getName();
		public Activity getActivity();
		public void onTimeChanged();
	}
}

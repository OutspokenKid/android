package com.outspoken_kid.utils;

public class MemoryUtils {

	public static void printMemoryState() {
		
		LogUtils.log(getMemoryStateString());
	}
	
	public static String getMemoryStateString() {

		try {
			final Runtime runtime = Runtime.getRuntime();
			final long totalMemInMB = runtime.totalMemory() / 1048576L;
			final long maxHeapSizeInMB = runtime.maxMemory() / 1048576L;
			
			return "###MemoryUtils.printMemoryState  "
						+ "\n		totalMemInMB : " + totalMemInMB
						+ "\n		maxHeapSizeInMB : " + maxHeapSizeInMB;
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return null; 
	}
}

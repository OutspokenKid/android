package com.outspoken_kid.downloader;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.os.AsyncTask;

import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.utils.StringUtils;

public abstract class BaseAsyncDownloader {
	
	protected final int RETRY_LIMIT = 3;
	protected ArrayList<BaseDownloadTask> queue = new ArrayList<BaseDownloadTask>();
	protected BaseDownloadTask currentTask;
	
	protected void checkIsDownloading() {
		
		if(queue.size() == 1) {
			currentTask = queue.get(0); 
			currentTask.execute();
		}
	}
	
	protected void addTaskToQueue(BaseDownloadTask task) {
		
		try {
			/* AsyncStringDownloader에서 추가한 경우,
			 * 기존에 있던것들 중 같은 url이 있는지 확인해보고,
			 * 있다면 제거, 만약 실행중이라면 취소하고 다음 task 실행.
			 */
			String requestUrl = task.getUrl();
			String url = null;
			int size = queue.size();
			
			if(size != 0) {
				for(int i=0; i<size; i++) {
					url = queue.get(i).getUrl();

					if(task instanceof AsyncStringDownloader.AsyncDownloadTask && url.equals(requestUrl)) {
						queue.remove(i);
						
						if(i==0 && queue.size() != 0) {
							//취소하고 다음 task 실행.
							currentTask.cancel(true);
							currentTask = queue.get(0); 
							currentTask.execute();
						}
						break;
					}
				}
			}

			queue.add(task);
			checkIsDownloading();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void removeTasks(ArrayList<BaseDownloadTask> arList, String key) {

		try {
			if(arList == null || arList.size() == 0 || StringUtils.checkNullOrDefault(key, "")) {
				return;
			}
			
			int size = arList.size();
			
			for(int i=size - 1; i>= 0; i--) {
				if(key.equals(arList.get(i).getKey())) {
					BaseDownloadTask bdt = arList.get(i);
					arList.remove(i);
					bdt.cancel(true);
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void cancelAllStringTasks(ArrayList<BaseDownloadTask> arList, String key) {

		try {
			if(arList == null || arList.size() == 0 || StringUtils.checkNullOrDefault(key, "")) {
				return;
			}
			
			int size = arList.size();
			
			for(int i=size - 1; i>= 0; i--) {
				if(key.equals(arList.get(i).getKey())
						&& arList.get(i) instanceof AsyncStringDownloader.AsyncDownloadTask) {
					BaseDownloadTask bdt = arList.get(i);
					arList.remove(i);
					bdt.cancel(true);
				}
			}
			
			if(currentTask.getKey().equals(key)) {
				currentTask.cancel(true);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void downloadComplete(AsyncTask<Void, Void, Void> task) {
		
		if(queue != null && queue.size() > 0 && queue.get(0) == task) {
			queue.remove(0);
			
			if(queue.size() > 0) {
				(queue.get(0)).execute();
			}
		}
	}

	public ArrayList<BaseDownloadTask> getDownloadQueue() {
		return queue;
	}
	
////////// classes.
	
	protected class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int b = read();
                    if (b < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
	
	protected abstract class BaseDownloadTask extends AsyncTask<Void, Void, Void> {
		
		protected String url;
		protected String key;
		
		public BaseDownloadTask(String url, String key) {
			if(!url.contains("http")) {
				this.url = "http://" + url;
			} else {
				this.url = url;
			}
			this.key = key;
		}
		
		public String getUrl() {
			return url;
		}
		
		public String getKey() {
			return key;
		}
	}
}

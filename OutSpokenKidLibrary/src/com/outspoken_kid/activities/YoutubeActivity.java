package com.outspoken_kid.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.PlaybackEventListener;
import com.google.android.youtube.player.YouTubePlayer.PlayerStateChangeListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.outspoken_kid.R;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public abstract class YoutubeActivity extends YouTubeBaseActivity implements
		YouTubePlayer.OnInitializedListener {

	private String videoId;
	private YouTubePlayer youTubePlayer;
	private YouTubePlayerView youTubePlayerView;

	private MyPlayerStateChangeListener myPlayerStateChangeListener;
	private MyPlaybackEventListener myPlaybackEventListener;

	public abstract String getAPIKey();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		youTubePlayerView = new YouTubePlayerView(this);
		youTubePlayerView.initialize(getAPIKey(), this);

		myPlayerStateChangeListener = new MyPlayerStateChangeListener();
		myPlaybackEventListener = new MyPlaybackEventListener();
		
		setContentView(youTubePlayerView);
		
		if(getIntent() == null 
				|| StringUtils.isEmpty(getIntent().getStringExtra("videoId"))) {
			finish();
		} else {
			videoId = getIntent().getStringExtra("videoId");
		}
	}

	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		LogUtils.log("###YoutubeActivity.onInitializationFailure.  ");
		ToastUtils.showToast(R.string.failToShowVideoFullScreenMode);
		
		if(arg1 == YouTubeInitializationResult.CLIENT_LIBRARY_UPDATE_REQUIRED) {
			LogUtils.log("###CLIENT_LIBRARY_UPDATE_REQUIRED");
		} else if(arg1 == YouTubeInitializationResult.DEVELOPER_KEY_INVALID) {
			LogUtils.log("###DEVELOPER_KEY_INVALID");
		} else if(arg1 == YouTubeInitializationResult.ERROR_CONNECTING_TO_SERVICE) {
			LogUtils.log("###ERROR_CONNECTING_TO_SERVICE");
		} else if(arg1 == YouTubeInitializationResult.INTERNAL_ERROR) {
			LogUtils.log("###INTERNAL_ERROR");
		} else if(arg1 == YouTubeInitializationResult.INVALID_APPLICATION_SIGNATURE) {
			LogUtils.log("###INVALID_APPLICATION_SIGNATURE");
		} else if(arg1 == YouTubeInitializationResult.NETWORK_ERROR) {
			LogUtils.log("###NETWORK_ERROR");
		} else if(arg1 == YouTubeInitializationResult.SERVICE_DISABLED) {
			LogUtils.log("###SERVICE_DISABLED");
		} else if(arg1 == YouTubeInitializationResult.SERVICE_INVALID) {
			LogUtils.log("###SERVICE_INVALID");
		} else if(arg1 == YouTubeInitializationResult.SERVICE_MISSING) {
			LogUtils.log("###SERVICE_MISSING");
		} else if(arg1 == YouTubeInitializationResult.SERVICE_VERSION_UPDATE_REQUIRED) {
			LogUtils.log("###SERVICE_VERSION_UPDATE_REQUIRED");
		} else if(arg1 == YouTubeInitializationResult.SUCCESS) {
			LogUtils.log("###SUCCESS");
		} else if(arg1 == YouTubeInitializationResult.UNKNOWN_ERROR) {
			LogUtils.log("###UNKNOWN_ERROR");
		} else{
			LogUtils.log("###???");
		}

		try {
			youTubePlayerView.postDelayed(new Runnable() {
				public void run() {
					finish();
					Uri uri = Uri.parse("http://www.youtube.com/watch?v=" + videoId);
					Intent i = new Intent(Intent.ACTION_VIEW, uri); 
					startActivity(i);
				}
			}, 2000);
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	@Override
	public void onInitializationSuccess(Provider provider, YouTubePlayer player,
			boolean wasRestored) {

		youTubePlayer = player;
		youTubePlayer.setPlayerStateChangeListener(myPlayerStateChangeListener);
		youTubePlayer.setPlaybackEventListener(myPlaybackEventListener);

		if (!wasRestored && videoId != null) {
			player.cueVideo(videoId);
		}
		
		LogUtils.log("###YoutubeActivity.onInitializationSuccess.  videoId : " + videoId);
	}

	private final class MyPlayerStateChangeListener implements
			PlayerStateChangeListener {

		@Override
		public void onAdStarted() {
			LogUtils.log("###YoutubeActivity.onAdStarted.  ");
		}

		@Override
		public void onError(
				com.google.android.youtube.player.YouTubePlayer.ErrorReason arg0) {
			LogUtils.log("###YoutubeActivity.onError.  ");
		}

		@Override
		public void onLoaded(String arg0) {
			LogUtils.log("###YoutubeActivity.onLoaded.  ");
			youTubePlayer.play();
		}

		@Override
		public void onLoading() {
			LogUtils.log("###YoutubeActivity.onLoading.  ");
		}

		@Override
		public void onVideoEnded() {
			LogUtils.log("###YoutubeActivity.onVideoEnded.  ");
		}

		@Override
		public void onVideoStarted() {
			LogUtils.log("###YoutubeActivity.onVideoStarted.  ");
		}

	}

	private final class MyPlaybackEventListener implements
			PlaybackEventListener {

		@Override
		public void onBuffering(boolean arg0) {
			LogUtils.log("###YoutubeActivity.onBuffering.  ");
		}

		@Override
		public void onPaused() {
			LogUtils.log("###YoutubeActivity.onPaused.  ");
		}

		@Override
		public void onPlaying() {
			LogUtils.log("###YoutubeActivity.onPlaying.  ");
		}

		@Override
		public void onSeekTo(int arg0) {
			LogUtils.log("###YoutubeActivity.onSeekTo.  ");
		}

		@Override
		public void onStopped() {
			LogUtils.log("###YoutubeActivity.onStopped.  ");
		}
	}
}

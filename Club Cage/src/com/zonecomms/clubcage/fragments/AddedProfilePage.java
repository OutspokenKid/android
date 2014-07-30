package com.zonecomms.clubcage.fragments;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.outspoken_kid.model.FontInfo;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo_dark.HoloConstants;
import com.outspoken_kid.views.holo_dark.HoloStyleButton;
import com.outspoken_kid.views.holo_dark.HoloStyleEditText;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerButton;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup;
import com.zonecomms.clubcage.MainActivity;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.BaseFragment;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.clubcage.classes.ZonecommsApplication;
import com.zonecomms.common.models.MyStoryInfo;
import com.zonecomms.common.utils.AppInfoUtils;

public class AddedProfilePage extends BaseFragment {

	private String[] arStatus;
	
	private MyStoryInfo myStoryInfo;
	
	private String status;
	private String interested;
	private String job;
	private String company;
	private String liveLocation;
	private String activeLocation;
	
	private FrameLayout innerFrame;
	private HoloStyleSpinnerButton spStatus;
	private HoloStyleSpinnerPopup pStatus;
	private HoloStyleEditText etInterested;
	private HoloStyleEditText etJob;
	private HoloStyleEditText etCompany;
	private HoloStyleEditText etLive;
	private HoloStyleEditText etActive;
	
	@Override
	protected void bindViews() {

		innerFrame = (FrameLayout) mThisView.findViewById(R.id.addedProfilePage_innerFrame);
	}

	@Override
	protected void setVariables() {
	}

	@Override
	protected void createPage() {

		spStatus = new HoloStyleSpinnerButton(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 70, spStatus, 2, Gravity.LEFT|Gravity.TOP, 
				new int[]{0, 40, 0, 0}, new int[]{160, 0, 0, 0});
		innerFrame.addView(spStatus);
		
		TextView tvStatus = new TextView(mContext);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 70, tvStatus, 2, Gravity.LEFT|Gravity.TOP, new int[]{40, 40, 0, 0});
		tvStatus.setGravity(Gravity.CENTER_VERTICAL);
		tvStatus.setText(R.string.status);
		tvStatus.setTextColor(HoloConstants.COLOR_HOLO_TEXT_HINT);
		FontInfo.setFontSize(tvStatus, 22);
		innerFrame.addView(tvStatus);

		etInterested = new HoloStyleEditText(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 70, etInterested, 2, Gravity.LEFT|Gravity.TOP, 
				new int[]{0, 130, 0, 0}, new int[]{160, 0, 0, 0});
		FontInfo.setFontSize(etInterested.getEditText(), 22);
		innerFrame.addView(etInterested);
		
		TextView tvInterested = new TextView(mContext);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 70, tvInterested, 2, Gravity.LEFT|Gravity.TOP, new int[]{40, 130, 0, 0});
		tvInterested.setGravity(Gravity.CENTER_VERTICAL);
		tvInterested.setText(R.string.interested);
		tvInterested.setTextColor(HoloConstants.COLOR_HOLO_TEXT_HINT);
		FontInfo.setFontSize(tvInterested, 22);
		innerFrame.addView(tvInterested);
		
		etJob = new HoloStyleEditText(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 70, etJob, 2, Gravity.LEFT|Gravity.TOP, 
				new int[]{0, 220, 0, 0}, new int[]{160, 0, 0, 0});
		FontInfo.setFontSize(etJob.getEditText(), 22);
		innerFrame.addView(etJob);
		
		TextView tvJob = new TextView(mContext);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 70, tvJob, 2, Gravity.LEFT|Gravity.TOP, new int[]{40, 220, 0, 0});
		tvJob.setGravity(Gravity.CENTER_VERTICAL);
		tvJob.setText(R.string.job);
		tvJob.setTextColor(HoloConstants.COLOR_HOLO_TEXT_HINT);
		FontInfo.setFontSize(tvJob, 22);
		innerFrame.addView(tvJob);
		
		etCompany = new HoloStyleEditText(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 70, etCompany, 2, Gravity.LEFT|Gravity.TOP, 
				new int[]{0, 310, 0, 0}, new int[]{160, 0, 0, 0});
		FontInfo.setFontSize(etCompany.getEditText(), 22);
		innerFrame.addView(etCompany);
		
		TextView tvCompany = new TextView(mContext);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 70, tvCompany, 2, Gravity.LEFT|Gravity.TOP, new int[]{40, 310, 0, 0});
		tvCompany.setGravity(Gravity.CENTER_VERTICAL);
		tvCompany.setText(R.string.company);
		tvCompany.setTextColor(HoloConstants.COLOR_HOLO_TEXT_HINT);
		FontInfo.setFontSize(tvCompany, 22);
		innerFrame.addView(tvCompany);
		
		etLive = new HoloStyleEditText(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 70, etLive, 2, Gravity.LEFT|Gravity.TOP, 
				new int[]{0, 400, 0, 0}, new int[]{160, 0, 0, 0});
		FontInfo.setFontSize(etLive.getEditText(), 22);
		innerFrame.addView(etLive);
		
		TextView tvLive = new TextView(mContext);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 70, tvLive, 2, Gravity.LEFT|Gravity.TOP, new int[]{40, 400, 0, 0});
		tvLive.setGravity(Gravity.CENTER_VERTICAL);
		tvLive.setText(R.string.liveLocation);
		tvLive.setTextColor(HoloConstants.COLOR_HOLO_TEXT_HINT);
		FontInfo.setFontSize(tvLive, 22);
		innerFrame.addView(tvLive);
		
		etActive = new HoloStyleEditText(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 70, etActive, 2, Gravity.LEFT|Gravity.TOP, 
				new int[]{0, 490, 0, 0}, new int[]{160, 0, 0, 0});
		FontInfo.setFontSize(etActive.getEditText(), 22);
		innerFrame.addView(etActive);
		
		TextView tvActive = new TextView(mContext);
		ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, 70, tvActive, 2, Gravity.LEFT|Gravity.TOP, new int[]{40, 490, 0, 0});
		tvActive.setGravity(Gravity.CENTER_VERTICAL);
		tvActive.setText(R.string.activeLocation);
		tvActive.setTextColor(HoloConstants.COLOR_HOLO_TEXT_HINT);
		FontInfo.setFontSize(tvActive, 22);
		innerFrame.addView(tvActive);
		
		HoloStyleButton btnSubmit = new HoloStyleButton(mContext);
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 70, btnSubmit, 2, Gravity.LEFT|Gravity.TOP, new int[]{0, 580, 0, 0});
		btnSubmit.setText(R.string.submit);
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				submit();
			}
		});
		innerFrame.addView(btnSubmit);
		
		View v = new View(mContext);
		ResizeUtils.viewResize(10, 690, v, 2, Gravity.LEFT|Gravity.TOP, null);
		innerFrame.addView(v);
		
		pStatus = new HoloStyleSpinnerPopup(mContext);
		pStatus.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		innerFrame.addView(pStatus);
	}

	@Override
	protected void setListeners() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setSizes() {

		int p = ResizeUtils.getSpecificLength(40);
		innerFrame.setPadding(p, p, p, p);
	}

	@Override
	protected void downloadInfo() {

		String url = ZoneConstants.BASE_URL + "member/mstatus_list" +
				"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL);

		DownloadUtils.downloadString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				ToastUtils.showToast(R.string.failToLoadUserStatus);
				setPage(false);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("AddedProfilePage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					JSONArray arJSON = objJSON.getJSONArray("data");
					
					int length = arJSON.length();
					
					arStatus = new String[length];
					
					for(int i=0; i<length; i++) {
						arStatus[i] = arJSON.getJSONObject(i).getString("mstatus_name");
					}
					
					String url2 = ZoneConstants.BASE_URL + "member/info" +
							"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
							"&mystory_member_id=" + MainActivity.myInfo.getMember_id() +
							"&image_size=" + ResizeUtils.getSpecificLength(308);
					
					DownloadUtils.downloadString(url2,
							new OnJSONDownloadListener() {

								@Override
								public void onError(String url) {
									
									ToastUtils.showToast(R.string.failToLoadUserInfo);
									setPage(false);
								}

								@Override
								public void onCompleted(String url,
										JSONObject objJSON) {

									try {
										LogUtils.log("AddedProfilePage.onCompleted."
												+ "\nurl : " + url
												+ "\nresult : " + objJSON);

										JSONArray arJSON = objJSON.getJSONArray("result");
										
										myStoryInfo = new MyStoryInfo(arJSON.getJSONObject(0));
										status = myStoryInfo.getMstatus_name();
										interested = myStoryInfo.getIlike();
										job = myStoryInfo.getJob();
										company = myStoryInfo.getJobname();
										liveLocation = myStoryInfo.getAddress();
										activeLocation = myStoryInfo.getPlayground();
										
										setPage(true);
									} catch (Exception e) {
										LogUtils.trace(e);
										ToastUtils.showToast(R.string.failToLoadUserInfo);
										setPage(false);
									} catch (OutOfMemoryError oom) {
										LogUtils.trace(oom);
										ToastUtils.showToast(R.string.failToLoadUserInfo);
										setPage(false);
									}
								}
							});
				} catch (Exception e) {
					LogUtils.trace(e);
					ToastUtils.showToast(R.string.failToLoadUserStatus);
					setPage(false);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
					ToastUtils.showToast(R.string.failToLoadUserStatus);
					setPage(false);
				}
			}
		});
		
		isDownloading = true;
		mActivity.showLoadingView();
		mActivity.showCover();
	}

	@Override
	protected void setPage(boolean downloadSuccess) {
		
		innerFrame.setVisibility(View.VISIBLE);
		
		if(downloadSuccess) {
			spStatus.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					pStatus.showPopup();
				}
			});
			
			pStatus.setTitle(getString(R.string.status));
			
			int length = arStatus.length;
			for(int i=0; i<length; i++) {
				pStatus.addItem(arStatus[i]);
			}

			pStatus.notifyDataSetChanged();
			pStatus.setTargetTextView(spStatus.getTextView());

			if(status != null) {
				spStatus.setText(status);
			}
			
			if(interested != null) {
				etInterested.getEditText().setText(interested);
			}

			if(job != null) {
				etJob.getEditText().setText(job);
			}
			
			if(company != null) {
				etCompany.getEditText().setText(company);
			}
			
			if(liveLocation != null) {
				etLive.getEditText().setText(liveLocation);
			}
			
			if(activeLocation != null) {
				etActive.getEditText().setText(activeLocation);
			}
		} else {
			status = null;
		}
		
		super.setPage(downloadSuccess);
	}

	@Override
	protected String getTitleText() {

		return getString(R.string.addedProfile);
	}

	@Override
	protected int getContentViewId() {
		
		return R.id.addedProfilePage_innerFrame;
	}

	@Override
	protected int getLayoutResId() {

		return R.layout.page_addedprofile;
	}
	
	@Override
	public boolean onBackKeyPressed() {
		
		if(pStatus.getVisibility() == View.VISIBLE) {
			pStatus.hidePopup();
			return true;
		}
		
		return false;
	}

	@Override
	public void onRefreshPage() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if(status == null) {
			downloadInfo();
		}
		
		if(mActivity.getSponserBanner() != null) {
			mActivity.getSponserBanner().hideBanner();
		}
	}
	
	@Override
	public void onSoftKeyboardShown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSoftKeyboardHidden() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finish(boolean needAnim, boolean isBeforeMain) {
		
		SoftKeyboardUtils.hideKeyboard(mContext, etActive);
		super.finish(needAnim, isBeforeMain);
	}
	
////////////////////////Custom methods.
	
	public void submit() {

		try {
			String status = "";
			String interested = "";
			String job = "";
			String company = "";
			String liveLocation = "";
			String activeLocation = "";
			
			if(spStatus.getTextView().getText() != null) {
				
				int length = this.arStatus.length;
				
				String targetString = spStatus.getTextView().getText().toString();
				for(int i=0; i<length; i++) {
					if(this.arStatus[i].equals(targetString)) {
						status = "" + i;
					}
				}
			}

			if(etInterested.getEditText().getText() != null) {
				interested = etInterested.getEditText().getText().toString();
			}
			
			if(etJob.getEditText().getText() != null) {
				job = etJob.getEditText().getText().toString();
			}
			
			if(etCompany.getEditText().getText() != null) {
				company = etCompany.getEditText().getText().toString();
			}
			
			if(etLive.getEditText().getText() != null) {
				liveLocation = etLive.getEditText().getText().toString();
			}
			
			if(etActive.getEditText().getText() != null) {
				activeLocation = etActive.getEditText().getText().toString();
			}
			
			String url = ZoneConstants.BASE_URL + "member/update/profile" +
					"?" + AppInfoUtils.getAppInfo(AppInfoUtils.ALL) +
					"&mstatus_nid=" + status +
					"&address=" + URLEncoder.encode(liveLocation, "UTF-8") +
					"&playground=" + URLEncoder.encode(activeLocation, "UTF-8") +
					"&job=" + URLEncoder.encode(job, "UTF-8") +
					"&jobname=" + URLEncoder.encode(company, "UTF-8") +
					"&ilike=" + URLEncoder.encode(interested, "UTF-8");
			
			DownloadUtils.downloadString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {
					
					ToastUtils.showToast(R.string.failToSubmitAddedProfile);
					setPage(false);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("AddedProfilePage.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("errorCode") == 1) {
							ToastUtils.showToast(R.string.submitCompleted);
							mActivity.closeTopPage();
							ZonecommsApplication.getTopFragment().onRefreshPage();
							setPage(true);
						} else {
							ToastUtils.showToast(R.string.failToSubmitAddedProfile);
							setPage(false);
						}
					} catch (Exception e) {
						LogUtils.trace(e);
						ToastUtils.showToast(R.string.failToSubmitAddedProfile);
						setPage(false);
					} catch (OutOfMemoryError oom) {
						LogUtils.trace(oom);
						ToastUtils.showToast(R.string.failToSubmitAddedProfile);
						setPage(false);
					}
				}
			});
			ToastUtils.showToast(R.string.submittingToServer);
			mActivity.showLoadingView();
			mActivity.showCover();
		} catch(Exception e) {
			LogUtils.trace(e);
			ToastUtils.showToast(R.string.failToSubmitAddedProfile);
		}
	}
}
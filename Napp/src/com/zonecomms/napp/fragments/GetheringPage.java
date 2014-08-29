package com.zonecomms.napp.fragments;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.outspoken_kid.classes.FontInfo;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader;
import com.outspoken_kid.downloader.stringdownloader.AsyncStringDownloader.OnCompletedListener;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;
import com.outspoken_kid.views.holo_dark.HoloStyleButton;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup;
import com.outspoken_kid.views.holo_dark.HoloStyleSpinnerPopup.OnItemClickedListener;
import com.zonecomms.common.adapters.GridAdapter;
import com.zonecomms.common.adapters.ListAdapter;
import com.zonecomms.common.models.Gethering;
import com.zonecomms.common.models.Member;
import com.zonecomms.common.models.Post;
import com.zonecomms.common.utils.AppInfoUtils;
import com.zonecomms.common.utils.ImageDownloadUtils;
import com.zonecomms.common.wrappers.ViewWrapperForGetheringMemberList;
import com.zonecomms.napp.BaseFragmentActivity.OnPositiveClickedListener;
import com.zonecomms.napp.MainActivity;
import com.zonecomms.napp.R;
import com.zonecomms.napp.classes.ApplicationManager;
import com.zonecomms.napp.classes.BaseListFragment;
import com.zonecomms.napp.classes.ZoneConstants;

public class GetheringPage extends BaseListFragment {

	private Button[] buttons;
	private ImageView ivProfile;
	private ProgressBar progress;
	private TextView tvPostCount1;
	private TextView tvPostCount2;
	private TextView tvMemberCount1;
	private TextView tvMemberCount2;
	private ImageView ivOwnerProfile;
	private ProgressBar progressForOwner;
	private View isPublic;
	private TextView tvNickname;
	private TextView tvGetheringName;
	private TextView tvRegdate;
	private TextView tvRegdate2;
	private TextView tvGetheringIntro;
	private HoloStyleButton btnForMember;
	private Button btnSeeMore;
	private HoloStyleButton btnForOwner;

	private ScrollView scrollView;
	private PullToRefreshListView listView;
	private ListAdapter listAdapter;
	private PullToRefreshGridView gridView;
	private GridAdapter gridAdapter;
	
	private HoloStyleSpinnerPopup pMore; 

	private Gethering gethering;
	
	private int menuIndex;
	private String sb_id;
	private int lastIndexno;
	private ArrayList<BaseModel> modelsForList = new ArrayList<BaseModel>();
	private ArrayList<BaseModel> modelsForGrid = new ArrayList<BaseModel>();
	
	@Override
	protected void bindViews() {

		buttons = new Button[4];
		buttons[0] = (Button) mThisView.findViewById(R.id.getheringPage_btnIntro);
		buttons[1] = (Button) mThisView.findViewById(R.id.getheringPage_btnNewPost);
		buttons[2] = (Button) mThisView.findViewById(R.id.getheringPage_btnMember);
		buttons[3] = (Button) mThisView.findViewById(R.id.getheringPage_btnStandBy);
		
		scrollView = (ScrollView) mThisView.findViewById(R.id.getheringPage_scrollView);
		ivProfile = (ImageView) mThisView.findViewById(R.id.getheringPage_ivProfile);
		progress = (ProgressBar) mThisView.findViewById(R.id.getheringPage_profileProgress);
		tvPostCount1 = (TextView) mThisView.findViewById(R.id.getheringPage_tvPostCount1);
		tvPostCount2 = (TextView) mThisView.findViewById(R.id.getheringPage_tvPostCount2);
		tvMemberCount1 = (TextView) mThisView.findViewById(R.id.getheringPage_tvMemberCount1);
		tvMemberCount2 = (TextView) mThisView.findViewById(R.id.getheringPage_tvMemberCount2);
		ivOwnerProfile = (ImageView) mThisView.findViewById(R.id.getheringPage_ivOwnerProfile);
		progressForOwner = (ProgressBar) mThisView.findViewById(R.id.getheringPage_ownerProgress);
		isPublic = mThisView.findViewById(R.id.getheringPage_isPublic);
		tvGetheringName = (TextView) mThisView.findViewById(R.id.getheringPage_tvGetheringName);
		tvNickname = (TextView) mThisView.findViewById(R.id.getheringPage_tvNickname);
		tvRegdate = (TextView) mThisView.findViewById(R.id.getheringPage_tvRegdate);
		tvRegdate2 = (TextView) mThisView.findViewById(R.id.getheringPage_tvRegdate2);
		tvGetheringIntro = (TextView) mThisView.findViewById(R.id.getheringPage_tvGetheringIntro);
		btnForMember = (HoloStyleButton) mThisView.findViewById(R.id.getheringPage_btnForMember);
		btnSeeMore = (Button) mThisView.findViewById(R.id.getheringPage_btnSeeMore);
		btnForOwner = (HoloStyleButton) mThisView.findViewById(R.id.getheringPage_btnForOwner);
		
		gridView = (PullToRefreshGridView) mThisView.findViewById(R.id.getheringPage_gridView);
		listView = (PullToRefreshListView) mThisView.findViewById(R.id.getheringPage_listView);
		
		pMore = (HoloStyleSpinnerPopup) mThisView.findViewById(R.id.getheringPage_pMore);
	}

	@Override
	protected void setVariables() {

		if(getArguments() != null) {
			menuIndex = getArguments().getInt("menuIndex", 0);
			sb_id = getArguments().getString("sb_id");
		}
		
		for(int i=0; i<4; i++) {
			FontInfo.setFontSize(buttons[i], 28);
			FontInfo.setFontStyle(buttons[i], FontInfo.BOLD);
		}
		
		gridAdapter = new GridAdapter(mContext, modelsForGrid, false);
		gridView.setAdapter(gridAdapter);
		gridView.getRefreshableView().setNumColumns(2);
		gridView.getRefreshableView().setPadding(0, 0, 0, 0);
		gridView.getRefreshableView().setSelector(R.drawable.list_selector);
		
		if (android.os.Build.VERSION.SDK_INT >= 9) {
	    	gridView.setOverScrollMode(GridView.OVER_SCROLL_NEVER);
	    }
		
		gridView.setOnRefreshListener(new OnRefreshListener<GridView>() {

			@Override
			public void onRefresh(PullToRefreshBase<GridView> refreshView) {
				onRefreshPage();
			}
		});
		gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(visibleItemCount < totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}
			}
		});
				
		listAdapter = new ListAdapter(mContext, modelsForList, false);
		listView.setAdapter(listAdapter);
		listView.getRefreshableView().setDividerHeight(0);
		listView.getRefreshableView().setCacheColorHint(Color.argb(0, 0, 0, 0));
		listView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				onRefreshPage();
			}
		});
		listView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(visibleItemCount < totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}
			}
		});
		
		if(pMore.getItems().size() == 0) {
			pMore.setTitle(null);
			pMore.addItem(getString(R.string.edit));
			pMore.addItem(getString(R.string.closeGethering));
			pMore.notifyDataSetChanged();
		}
	}

	@Override
	protected void createPage() {
	}
	
	@Override
	protected void setListeners() {

		for(int i=0; i<4; i++) {
			final int I = i;
			
			buttons[i].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(gethering != null) {
						if(I == 3 && gethering.getMember_sb_role() != 8) {
							//Don't need to use standing by menu.
						} else if(I != 0 && gethering.getMember_sb_role() == 0) {
							ToastUtils.showToast(R.string.useAfterJoin);
						} else {
							showMenu(I);
						}
					}
				}
			});
		}
		
		ivOwnerProfile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				mActivity.showProfilePopup(gethering.getReg_id(), 0);
			}
		});
		
		btnForMember.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String message = "";
				String current = getResources().getConfiguration().locale.getCountry();
				boolean isKorean = current.equals(Locale.KOREA.getCountry())
						|| current.equals(Locale.KOREAN.getCountry());
				final boolean isJoin = gethering.getMember_sb_role() == 0;
				boolean isStandingBy = !isJoin && gethering.getMember_sb_status() == 0;
				
				if(isStandingBy) {
					ToastUtils.showToast(R.string.alreadyMemberOrWaiting);
					return;
					
				} else if(isJoin) {
					if(isKorean) {
						//모임명 모임에 가입하시겠습니까?
						message = gethering.getSb_nickname() + " " + getString(R.string.wannaJoinGethering);
					} else {
						//Do you want to join the 'gethering'?
						message = getString(R.string.wannaJoinGethering) + " " + gethering.getSb_nickname() + "?";
					}
				} else {
					if(isKorean) {
						//모임명 모임을 탈퇴하시겠습니까?
						message = gethering.getSb_nickname() + " " + getString(R.string.wannaWithdraw);
					} else {
						//Do you want to withdraw the 'gethering'?
						message = getString(R.string.wannaWithdraw) + " " + gethering.getSb_nickname() + "?";
					}
				}
				
				mActivity.showAlertDialog(getString(R.string.joinGethering),
						message,
						getString(R.string.confirm), getString(R.string.cancel),
						new OnPositiveClickedListener() {
					
					@Override
					public void onPositiveClicked() {
						
						if(isJoin) {
							join(gethering.getSb_public() == 1);
						} else {
							withdraw();
						}
						
					}
				}, true);
			}
		});

		btnSeeMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				pMore.showPopup();
			}
		});
		
		btnForOwner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(ViewWrapperForGetheringMemberList.getMembersSize() == 0) {
					ToastUtils.showToast(R.string.selectMember);
					return;
				}
				
				//For banish.
				if(menuIndex == 2) {
					mActivity
						.showAlertDialog(getString(R.string.wannaBanish),
							ViewWrapperForGetheringMemberList.getMemberidNicknameString(), 
							new OnPositiveClickedListener() {
						
						@Override
						public void onPositiveClicked() {
							banish();
						}
					});
				//For approval.
				} else if(menuIndex == 3) {
					mActivity
						.showAlertDialog(getString(R.string.wannaApproval),
							ViewWrapperForGetheringMemberList.getMemberidNicknameString(),
							new OnPositiveClickedListener() {
						
						@Override
						public void onPositiveClicked() {
							approval();
						}
					});
				}
			}
		});

		pMore.setOnItemClickedListener(new OnItemClickedListener() {
			
			@Override
			public void onItemClicked(int position, String itemString) {

				//수정.
				if(position == 0) {
					mActivity
						.showGetheringOpenPage(
							gethering.getMedia_src(),
							gethering.getSb_nickname(),
							gethering.getSb_id(),
							gethering.getSb_public(),
							gethering.getSb_description());
					
				//폐쇄.
				} else {
					mActivity
						.showAlertDialog(null, getString(R.string.wannaCloseGethering),
							getString(R.string.keepGoing), getString(R.string.cancel),
							new OnPositiveClickedListener() {
						
						@Override
						public void onPositiveClicked() {
							
							mActivity
								.showAlertDialog(null, getString(R.string.messageForCloseGethering), 
									getString(R.string.confirm), getString(R.string.cancel), 
									new OnPositiveClickedListener() {
										
										@Override
										public void onPositiveClicked() {
											closeGethering();
										}
							}, true);
						}
					}, true);
				}
			}
		});
	}

	@Override
	protected void setSizes() {

		RelativeLayout.LayoutParams rp;

		int l1 = ResizeUtils.getSpecificLength(150);
		int s = ResizeUtils.getSpecificLength(8);
		int l2 = l1*2 + s;
		int progressLength = ResizeUtils.getSpecificLength(50);
		int buttonHeight = ResizeUtils.getSpecificLength(90);
		
		rp = (RelativeLayout.LayoutParams) buttons[0].getLayoutParams();
		rp.width = l1;
		rp.height = l1;
		rp.leftMargin = s;
		rp.topMargin = s;
		rp.bottomMargin = s;
		
		rp = (RelativeLayout.LayoutParams) buttons[1].getLayoutParams();
		rp.width = l1;
		rp.height = l1;
		rp.leftMargin = s;
		
		rp = (RelativeLayout.LayoutParams) buttons[2].getLayoutParams();
		rp.width = l1;
		rp.height = l1;
		rp.leftMargin = s;
		
		rp = (RelativeLayout.LayoutParams) buttons[3].getLayoutParams();
		rp.width = l1;
		rp.height = l1;
		rp.leftMargin = s;

		rp = (RelativeLayout.LayoutParams) mThisView.
				findViewById(R.id.getheringPage_scrollView).getLayoutParams();
		rp.width = LayoutParams.MATCH_PARENT;
		rp.height = LayoutParams.MATCH_PARENT;
		rp.bottomMargin = buttonHeight + 2*s; 
		
		rp = (RelativeLayout.LayoutParams) mThisView.
				findViewById(R.id.getheringPage_bgProfile).getLayoutParams();
		rp.width = l2;
		rp.height = l2;
		
		rp = (RelativeLayout.LayoutParams) mThisView.
				findViewById(R.id.getheringPage_profileProgress).getLayoutParams();
		rp.width = progressLength;
		rp.height = progressLength;
		rp.leftMargin = (l2 - progressLength) /2;
		rp.topMargin = (l2 - progressLength) /2;
		
		rp = (RelativeLayout.LayoutParams) ivProfile.getLayoutParams();
		rp.width = l2;
		rp.height = l2;
		
		rp = (RelativeLayout.LayoutParams) tvPostCount1.getLayoutParams();
		rp.width = l1;
		rp.height = l1;
		rp.leftMargin = s;
		
		rp = (RelativeLayout.LayoutParams) tvMemberCount1.getLayoutParams();
		rp.width = l1;
		rp.height = l1;
		rp.leftMargin = s;
		
		rp = (RelativeLayout.LayoutParams) mThisView.
				findViewById(R.id.getheringPage_bgOwnerProfile).getLayoutParams();
		rp.width = l1;
		rp.height = l1;
		rp.leftMargin = s;
		
		rp = (RelativeLayout.LayoutParams) mThisView.
				findViewById(R.id.getheringPage_ownerProgress).getLayoutParams();
		rp.width = progressLength;
		rp.height = progressLength;
		rp.leftMargin = (l1 - progressLength) /2;
		rp.topMargin = (l1 - progressLength) /2;
		
		rp = (RelativeLayout.LayoutParams) ivOwnerProfile.getLayoutParams();
		rp.width = l1;
		rp.height = l1;
		
		rp = (RelativeLayout.LayoutParams) isPublic.getLayoutParams();
		rp.width = l1;
		rp.height = l1;
		rp.leftMargin = s;
		isPublic.setPadding(s, s, s, s);
		
		rp = (RelativeLayout.LayoutParams) tvGetheringName.getLayoutParams();
		rp.width = LayoutParams.WRAP_CONTENT;
		rp.height = l1/2;
		tvGetheringName.setMaxWidth(ResizeUtils.getScreenWidth()/2 - s*2);
		tvGetheringName.setPadding(s, s, s, s);
		
		rp = (RelativeLayout.LayoutParams) tvNickname.getLayoutParams();
		rp.width = LayoutParams.WRAP_CONTENT;
		rp.height = l1/2;
		tvNickname.setMaxWidth(ResizeUtils.getScreenWidth()/2 - s*2);
		tvRegdate.setPadding(s, s, s, s);
		
		rp = (RelativeLayout.LayoutParams) tvRegdate.getLayoutParams();
		rp.width = LayoutParams.WRAP_CONTENT;
		rp.height = l1/2;
		tvRegdate.setPadding(s, s, s, s);
		
		rp = (RelativeLayout.LayoutParams) tvRegdate2.getLayoutParams();
		rp.width = LayoutParams.WRAP_CONTENT;
		rp.height = l1/2;
		tvRegdate2.setPadding(s, s, s, s+s/2);

		rp = (RelativeLayout.LayoutParams) tvGetheringIntro.getLayoutParams();
		tvGetheringIntro.setPadding(s, s, s, s);
		
		rp = (RelativeLayout.LayoutParams) btnForMember.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(540);
		rp.height = buttonHeight;
		rp.bottomMargin = s;
		
		rp = (RelativeLayout.LayoutParams) btnSeeMore.getLayoutParams();
		rp.width = buttonHeight;
		rp.height = buttonHeight;
		rp.rightMargin = s;
		rp.bottomMargin = s;
		
		rp = (RelativeLayout.LayoutParams) listView.getLayoutParams();
		rp.bottomMargin = s;
		
		rp = (RelativeLayout.LayoutParams) btnForOwner.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(540);
		rp.height = buttonHeight;
		rp.bottomMargin = s;

		rp = (RelativeLayout.LayoutParams) gridView.getLayoutParams();
		rp.bottomMargin = s;
		
		FontInfo.setFontSize(tvPostCount1, 30);
		FontInfo.setFontStyle(tvPostCount1, FontInfo.BOLD);
		tvPostCount1.setPadding(s, s, s, s);
		
		FontInfo.setFontSize(tvPostCount2, 55);
		FontInfo.setFontStyle(tvPostCount2, FontInfo.BOLD);
		tvPostCount2.setPadding(s, s, s, s);

		FontInfo.setFontSize(tvMemberCount1, 30);
		FontInfo.setFontStyle(tvMemberCount1, FontInfo.BOLD);
		tvMemberCount1.setPadding(s, s, s, s);
		
		FontInfo.setFontSize(tvMemberCount2, 55);
		FontInfo.setFontStyle(tvMemberCount2, FontInfo.BOLD);
		tvMemberCount2.setPadding(s, s, s, s);

		FontInfo.setFontSize(btnForMember.getTextView(), 30);
		FontInfo.setFontSize(btnForOwner.getTextView(), 30);
		FontInfo.setFontSize(tvGetheringName, 36);
		FontInfo.setFontSize(tvNickname, 30);
		FontInfo.setFontSize(tvRegdate, 30);
		FontInfo.setFontSize(tvRegdate2, 36);
		FontInfo.setFontSize(tvGetheringIntro, 30);
	}

	@Override
	public String getTitleText() {

		if(title == null) {
			title = getString(R.string.gethering);
		}
		
		return title;
	}

	@Override
	protected int getContentViewId() {

		return R.id.getheringPage_mainLayout;
	}
	
	@Override
	public void onRefreshPage() {

		super.onRefreshPage();
		showMenu(menuIndex);
		isRefreshing = true;
	}

	@Override
	public boolean onBackKeyPressed() {

		if(pMore.getVisibility() == View.VISIBLE) {
			pMore.hidePopup();
			return true;
		}
		
		return false;
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
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		
		if(!hidden) {

			if(mActivity.getSponserBanner() != null) {
				mActivity.getSponserBanner().hideBanner();
			}
			
			if(!hidden) {
				mActivity.getTitleBar().showHomeButton();
				mActivity.getTitleBar().hideWriteButton();
				mActivity.getTitleBar().showPlusAppButton();
				mActivity.getTitleBar().hideThemaButton();
				mActivity.getTitleBar().hideRegionButton();

				showMenu(menuIndex);
			}
		}
	}
	
	@Override
	protected void downloadInfo() {
		
		if(isDownloading || isLastList) {
			return;
		}
		
		super.downloadInfo();
		
		switch(menuIndex) {
		
		case 1:
			loadPost();
			break;
			
		case 2:
		case 3:
			loadMember(menuIndex == 3);
			break;
		}
	}
	
	@Override
	protected void setPage(boolean successDownload) {

		if(isRefreshing && listView != null) {
			listView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					listView.onRefreshComplete();					
				}
			}, 500);
		}
		
		if(isRefreshing && gridView != null) {
			gridView.postDelayed(new Runnable() {
				
				@Override
				public void run() {
					gridView.onRefreshComplete();					
				}
			}, 500);
		}
		
		super.setPage(successDownload);
		
		if(models != null && models.size() < MAX_LOADING_COUNT) {
			isLastList = true;
		}
	}
	
	public void showMenu(int menuIndex) {
		
		isRefreshing = false;
		isDownloading = false;
		isLastList = false;
		lastIndexno = 0;
		
		mActivity.showLoadingView();
		ViewWrapperForGetheringMemberList.clearList();
		
		if(modelsForList != null) {
			modelsForList.clear();
		}
		
		if(modelsForGrid != null) {
			modelsForGrid.clear();
		}
		
		if(zoneAdapter != null) {
			zoneAdapter.notifyDataSetChanged();
			zoneAdapter = null;
		}

		buttons[this.menuIndex].setBackgroundColor(Color.rgb(55, 55, 55));
		buttons[menuIndex].setBackgroundColor(Color.rgb(0, 0, 0));
		
		this.menuIndex = menuIndex;
		
		switch(menuIndex) {
		
		case 0:
			mActivity
				.getTitleBar().hideWriteButton();
			scrollView.setVisibility(View.VISIBLE);
			listView.setVisibility(View.INVISIBLE);
			gridView.setVisibility(View.INVISIBLE);
			btnForOwner.setVisibility(View.INVISIBLE);
			loadGetheringInfo();
			break;
		case 1:
			mActivity
				.getTitleBar().showWriteButton();
			scrollView.setVisibility(View.INVISIBLE);
			listView.setVisibility(View.INVISIBLE);
			gridView.setVisibility(View.VISIBLE);
			btnSeeMore.setVisibility(View.INVISIBLE);
			btnForMember.setVisibility(View.INVISIBLE);
			btnForOwner.setVisibility(View.INVISIBLE);
			zoneAdapter = gridAdapter;
			models = modelsForGrid;
			loadPost();
			break;
		case 2:
		case 3:
			mActivity
				.getTitleBar().hideWriteButton();
			scrollView.setVisibility(View.INVISIBLE);
			listView.setVisibility(View.VISIBLE);
			gridView.setVisibility(View.INVISIBLE);
			btnSeeMore.setVisibility(View.INVISIBLE);
			btnForMember.setVisibility(View.INVISIBLE);
			
			RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) listView.getLayoutParams();
			
			if(gethering.isOwner()) {
				rp.bottomMargin = ResizeUtils.getSpecificLength(108);
				btnForOwner.setVisibility(View.VISIBLE);
				btnForOwner.setText(menuIndex == 2? R.string.banish : R.string.approval);
			} else {
				rp.bottomMargin = ResizeUtils.getSpecificLength(8);
				btnForOwner.setVisibility(View.INVISIBLE);
			}
			
			zoneAdapter = listAdapter;
			models = modelsForList;
			loadMember(menuIndex == 3);
			break;
		}
	}
	
	public void loadGetheringInfo() {
		
		String url = ZoneConstants.BASE_URL + "sb/info" +
				"?sb_id=" + sb_id +
				"&image_size=" + ResizeUtils.getSpecificLength(308) +
				"&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_SB_ID);
		
		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {
				
				LogUtils.log("GetheringPage.loadGetheringInfo.onError.  url : " + url);
				ToastUtils.showToast(R.string.failToLoadGetheringInfo);
				
				mActivity.hideLoadingView();
				isRefreshing = false;
				isDownloading = false;
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				LogUtils.log("GetheringPage.loadGetheringInfo.onCompleted.\nurl : " + url + "\nresult : " + result);
				
				mActivity.hideLoadingView();
				isRefreshing = false;
				isDownloading = false;
				
				try {
					gethering = new Gethering((new JSONObject(result)).getJSONObject("data"));
					
					title = gethering.getSb_nickname();
					mActivity.setTitleText(title);
					
					//Download gethering profile.
					if(!StringUtils.isEmpty(gethering.getMedia_src())) {
						progress.setVisibility(View.VISIBLE);
						ImageDownloadUtils.downloadImageImmediately(gethering.getMedia_src(), getDownloadKey(), ivProfile, 308, false);
						ivProfile.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View arg0) {
								mActivity.showImageViewerActivity(null, new String[]{gethering.getMedia_src()}, null, 0);
							}
						});
					} else {
						progress.setVisibility(View.INVISIBLE);
						ivProfile.setOnClickListener(null);
					}
					
					//Set postCount.
					String postCount = "";
					if(gethering.getPosted_cnt() > 999) {
						postCount = "999+";
					} if(gethering.getPosted_cnt() < 0) {
						postCount = "0";
					} else {
						postCount = "" + gethering.getPosted_cnt();
					}
					
					tvPostCount2.setText(postCount);
					
					//Set memberCount.
					String memberCount = "";
					if(gethering.getMember_cnt() > 999) {
						memberCount = "999+";
					} else if(gethering.getMember_cnt() < 0) {
						memberCount = "0";
					} else{
						memberCount = "" + gethering.getMember_cnt(); 
					}

					tvMemberCount2.setText(memberCount);
					
					//Download owner profile.
					if(!StringUtils.isEmpty(gethering.getReg_image())) {
						progressForOwner.setVisibility(View.VISIBLE);
						ImageDownloadUtils.downloadImageImmediately(gethering.getReg_image(),
								getDownloadKey(), ivOwnerProfile, 150, false);
					} else {
						progressForOwner.setVisibility(View.INVISIBLE);
					}
					
					//Set owner nickname.
					tvNickname.setText(gethering.getReg_nickname());
					
					//Set gethering name.
					tvGetheringName.setText(gethering.getSb_nickname());
					
					//Set needPublic.
					if(gethering.getSb_public() == 1) {
						isPublic.setBackgroundResource(R.drawable.img_unlocked);
					} else {
						isPublic.setBackgroundResource(R.drawable.img_locked);
					}
					
					//Set regdate.
					tvRegdate.setText(gethering.getReg_dt());
					
					//Set gethering introduction.
					tvGetheringIntro.setText(gethering.getSb_description());
					
					//Set buttons.
					if(gethering.isOwner()) {
						btnSeeMore.setVisibility(View.VISIBLE);
						btnForMember.setVisibility(View.INVISIBLE);
					} else {
						btnSeeMore.setVisibility(View.INVISIBLE);
						btnForMember.setVisibility(View.VISIBLE);

						//미가입 상태 또는 승인 대기 상태.
						if(gethering.getMember_sb_role() == 0
								|| gethering.getMember_sb_status() == 0) {
							btnForMember.setText(R.string.join);
							
						//가입된 상태.
						} else {
							btnForMember.setText(R.string.withdraw);
						}
					}
				} catch(Exception e) {
					ToastUtils.showToast(R.string.failToLoadGetheringInfo);
				}
			}
		};

		super.downloadInfo();
		AsyncStringDownloader.download(url, getDownloadKey(), ocl);
	}
	
	public void loadPost() {
		
		String url = ZoneConstants.BASE_URL + "boardspot/list" +
				"?sb_id=" + sb_id +
				"&image_size=" + ResizeUtils.getSpecificLength(308) +
				"&last_spot_nid=" + lastIndexno +
				"&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_SB_ID);
		
		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {

				LogUtils.log("GetheringPage.loadPost.onError.  url : " + url);
				setPage(false);
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				LogUtils.log("GetheringPage.loadPost.onCompleted.\nurl : " + url + "\nresult : " + result);
				
				try {
					JSONObject objJSON = new JSONObject(result);
					JSONArray arJSON = objJSON.getJSONArray("data");
					int length = arJSON.length();
					
					if(length > 0) {
						for(int i=0; i<length; i++) {
							try {
								Post post = new Post(arJSON.getJSONObject(i));
								post.setItemCode(ZoneConstants.ITEM_POST);
								post.setFromGethering(true);
								post.setHideOrigin(true);
								models.add(post);
							} catch(Exception e) {
							}
						}
					} else {
						isLastList = true;
						ToastUtils.showToast(R.string.lastPage);
					}
					
					setPage(true);
				} catch(Exception e) {
					LogUtils.trace(e);
					setPage(false);
				}
			}
		};
		
		super.downloadInfo();
		AsyncStringDownloader.download(url, getDownloadKey(), ocl);
	}
	
	public void loadMember(boolean isStandingBy) {

		String url = ZoneConstants.BASE_URL + "sb/member_list" +
				"?sb_id=" + sb_id +
				"&image_size=" + ResizeUtils.getSpecificLength(150) +
				"&" + AppInfoUtils.getAppInfo(AppInfoUtils.WITHOUT_SB_ID);

		if(modelsForList.size() != 0) {
			url += "&max_sb_member_nid=" + 
					((Member)modelsForList.get(modelsForList.size()-1)).getSb_member_nid();
		} else {
			url += "&max_sb_member_nid=0";
		}
		
		if(isStandingBy) {
			url += "&status=0";
		} else {
			url += "&status=1";
		}
		
		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {

				LogUtils.log("GetheringPage.loadMember.onError.  url : " + url);
				setPage(false);
			}
			
			@Override
			public void onCompleted(String url, String result) {

				LogUtils.log("GetheringPage.loadMember.onCompleted.\nurl : " + url + "\nresult : " + result);
				
				try {
					JSONObject objJSON = new JSONObject(result);
					JSONArray arJSON = objJSON.getJSONArray("data");
					int length = arJSON.length();
					
					if(length > 0) {
						for(int i=0; i<length; i++) {
							try {
								Member member = new Member(arJSON.getJSONObject(i));
								member.setItemCode(ZoneConstants.ITEM_USER);
								member.setShownByAdmin(gethering.isOwner());
								models.add(member);
							} catch(Exception e) {
							}
						}
					} else {
						isLastList = true;
						ToastUtils.showToast(R.string.lastPage);
					}
					
					setPage(true);
				} catch(Exception e) {
					LogUtils.trace(e);
					setPage(false);
				}
			}
		};

		super.downloadInfo();
		AsyncStringDownloader.download(url, getDownloadKey(), ocl);
	}
	
	public void join(final boolean isPublic) {
		
		String url = ZoneConstants.BASE_URL + "sb/join" +
				"?sb_id=" + gethering.getSb_id() +
				"&member_id=" + MainActivity.myInfo.getMember_id();
		
		AsyncStringDownloader.download(url, getDownloadKey(), new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {
				
				LogUtils.log("JoinGethering.onError.\nurl : " + url);
				ToastUtils.showToast(R.string.failToJoinGethering);
			}
			
			@Override
			public void onCompleted(String url, String result) {

				LogUtils.log("JoinGethering.onCompleted.\nurl : " + url + "\nresult : " + result);
				
				try {
					if(("1").equals((new JSONObject(result)).getString("data"))) {
						ToastUtils.showToast(R.string.completeApplyForAdmission);
						
						if(!isPublic) {
							mActivity.getTitleBar().postDelayed(new Runnable() {
								
								@Override
								public void run() {
									ToastUtils.showToast(R.string.willBeCompletedWhenApply);
								}
							}, 2000);
						} else {
							showMenu(0);
						}
					} else if(("0").equals((new JSONObject(result)).getString("data"))) {
						ToastUtils.showToast(R.string.alreadyMemberOrWaiting);
					} else{
						ToastUtils.showToast(R.string.failToJoinGethering);
					}
						
				} catch(Exception e) {
					ToastUtils.showToast(R.string.failToJoinGethering);
				}
			}
		});
	}
	
	public void withdraw() {
		
		String url = ZoneConstants.BASE_URL + "sb/withdraw" +
				"?sb_id=" + gethering.getSb_id() +
				"&member_id=" + MainActivity.myInfo.getMember_id();
		
		AsyncStringDownloader.download(url, getDownloadKey(), new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {
				
				LogUtils.log("WithdrawGethering.onError.\nurl : " + url);
				ToastUtils.showToast(R.string.failToWithdrawGethering);
			}
			
			@Override
			public void onCompleted(String url, String result) {

				LogUtils.log("WithdrawGethering.onCompleted.\nurl : " + url + "\nresult : " + result);
				
				try {
					if(("true").equals((new JSONObject(result)).getString("data"))) {
						ToastUtils.showToast(R.string.completeWithdraw);
						showMenu(0);
					} else{
						ToastUtils.showToast(R.string.failToWithdrawGethering);
					}
				} catch(Exception e) {
					ToastUtils.showToast(R.string.failToWithdrawGethering);
				}
			}
		});
	}
	
	public void closeGethering() {

		String url = ZoneConstants.BASE_URL + "sb/remove" +
				"?sb_id=" + gethering.getSb_id() +
				"&member_id=" + MainActivity.myInfo.getMember_id();

		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {
				
				LogUtils.log("GetheringPage.closeGethering.onError.\nurl : "+ url);
				ToastUtils.showToast(R.string.failToCloseGethering);
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				LogUtils.log("GetheringPage.closeGethering.onCompleted.\nurl : "+ url + "\nresult : " + result);
				
				try {
					if((new JSONObject(result)).getInt("errorCode") == 1) {
						mActivity.finishFragment(GetheringPage.this);
						ApplicationManager.getTopFragment().onRefreshPage();
						((GetheringListPage)ApplicationManager.getTopFragment()).onRefreshPage();
					} else {
						ToastUtils.showToast(R.string.failToCloseGethering);
					}
				} catch(Exception e) {
					ToastUtils.showToast(R.string.failToCloseGethering);
				}
			}
		};
		
		AsyncStringDownloader.download(url, getDownloadKey(), ocl);
	}
	
	public void banish() {
				
		String url = ZoneConstants.BASE_URL + "sb/statusSBMember" +
				"?sb_id=" + gethering.getSb_id() +
				"&status=-9" +
				"&member_ids=" + ViewWrapperForGetheringMemberList.getMemberidString();
		
		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {

				LogUtils.log("GetheringPage.banish.onError.\nurl : " + url);
				ToastUtils.showToast(R.string.failToBanish);
			}
			
			@Override
			public void onCompleted(String url, String result) {
				
				LogUtils.log("GetheringPage.banish.onCompleted." +
						"\nurl : " + url + "\nresult : " + result);
				
				try {
					if((new JSONObject(result)).getInt("errorCode") == 1) {
						showMenu(2);
					} else {
						ToastUtils.showToast(R.string.failToBanish);
					}
				} catch(Exception e) {
					ToastUtils.showToast(R.string.failToBanish);
				}
			}
		};
		
		AsyncStringDownloader.download(url, getDownloadKey(), ocl);
	}
	
	public void approval() {
		
		String url = ZoneConstants.BASE_URL + "sb/statusSBMember" +
				"?sb_id=" + gethering.getSb_id() +
				"&status=1" +
				"&member_ids=" + ViewWrapperForGetheringMemberList.getMemberidString();
		
		AsyncStringDownloader.OnCompletedListener ocl = new OnCompletedListener() {
			
			@Override
			public void onErrorRaised(String url, Exception e) {

				LogUtils.log("GetheringPage.approval.onError." +
						"\nurl : " + url);
				ToastUtils.showToast(R.string.failToApproval);
			}
			
			@Override
			public void onCompleted(String url, String result) {

				LogUtils.log("GetheringPage.approval.onCompleted." +
						"\nurl : " + url + "\nresult : " + result);
				
				try {
					if((new JSONObject(result)).getInt("errorCode") == 1) {
						showMenu(3);
					} else {
						ToastUtils.showToast(R.string.failToApproval);
					}
				} catch(Exception e) {
					ToastUtils.showToast(R.string.failToApproval);
				}
			}
		};
		
		AsyncStringDownloader.download(url, getDownloadKey(), ocl);
	}
	
	public String getSb_id() {
		
		if(gethering == null) {
			return "";
		}
		
		return gethering.getSb_id();
	}

	@Override
	protected String generateDownloadKey() {
		return "GETHERINGPAGE" + madeCount;
	}

	@Override
	protected int getXmlResId() {
		return R.layout.page_gethering;
	}
}

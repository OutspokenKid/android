package com.cmons.cph.fragments.retail;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cmons.cph.R;
import com.cmons.cph.RetailActivity;
import com.cmons.cph.ShopActivity.OnAfterSelectCategoryListener;
import com.cmons.cph.classes.CmonsFragmentForRetail;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Category;
import com.cmons.cph.models.Notification;
import com.cmons.cph.models.Product;
import com.cmons.cph.views.HeaderViewForRetailMain;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.views.HeaderGridView;

public class RetailMainPage extends CmonsFragmentForRetail {
	
	private HeaderViewForRetailMain headerView;
	private View blankView;
	private HeaderGridView gridView;

	private View cover;
	private RelativeLayout noticeRelative;
	private View read;
	private Button btnClose;
	private ListView listView;
	private CphAdapter noticeAdapter;
	private ArrayList<BaseModel> notices = new ArrayList<BaseModel>();
	
	private RelativeLayout menuRelative;
	private Button[] menuButtons;
	
	private TranslateAnimation taIn, taOut;
	private AlphaAnimation aaIn, aaOut;
	private boolean animating;
	private boolean isMenuOpened;
	private int lastItemIndex;
	
	private String[] orders;
	
	private int categoryIndex = 0;
	private int order = 0;
	private int totalCount;
	private int defaultTopMargin = 0;
	private boolean headerShown;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(headerView != null) {
			headerView.refreshValues((RetailActivity)mActivity);
		}
		
		if(models.size() == 0) {
			downloadInfo();
		}
		
		if(headerShown) {
			headerView.setVisibility(View.VISIBLE);
		} else {
			headerView.setVisibility(View.INVISIBLE);
		}
		
		refreshNotices();
		
		if(noticeRelative.getVisibility() == View.VISIBLE
				&& lastItemIndex != 0) {
			
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					listView.setSelection(lastItemIndex);
				}
			}, 1000);
		}
		
		mActivity.checkGuidePage(mActivity.user.getId(), false);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		headerView.setPlaying(false);
		
		if(noticeRelative.getVisibility() == View.VISIBLE) {
			lastItemIndex = listView.getFirstVisiblePosition();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.retailMainPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.retailMainPage_ivBg);
		
		headerView = (HeaderViewForRetailMain) mThisView.findViewById(R.id.retailMainPage_headerView);
		gridView = (HeaderGridView) mThisView.findViewById(R.id.retailMainPage_gridView);
		menuRelative = (RelativeLayout) mThisView.findViewById(R.id.retailMainPage_menuRelative);

		menuButtons = new Button[9];
		menuButtons[0] = (Button) mThisView.findViewById(R.id.retailMainPage_btnSample);
		menuButtons[1] = (Button) mThisView.findViewById(R.id.retailMainPage_btnCustomer);
		menuButtons[2] = (Button) mThisView.findViewById(R.id.retailMainPage_btnSearch);
		menuButtons[3] = (Button) mThisView.findViewById(R.id.retailMainPage_btnBasket);
		menuButtons[4] = (Button) mThisView.findViewById(R.id.retailMainPage_btnFavoriteShop);
		menuButtons[5] = (Button) mThisView.findViewById(R.id.retailMainPage_btnFavoriteProduct);
		menuButtons[6] = (Button) mThisView.findViewById(R.id.retailMainPage_btnStaff);
		menuButtons[7] = (Button) mThisView.findViewById(R.id.retailMainPage_btnBusiness);
		menuButtons[8] = (Button) mThisView.findViewById(R.id.retailMainPage_btnSetting);
		
		cover = mThisView.findViewById(R.id.retailMainPage_cover);
		noticeRelative = (RelativeLayout) mThisView.findViewById(R.id.retailMainPage_noticeRelative);
		read = mThisView.findViewById(R.id.retailMainPage_read);
		btnClose = (Button) mThisView.findViewById(R.id.retailMainPage_btnClose);
		listView = (ListView) mThisView.findViewById(R.id.retailMainPage_listView);
	}

	@Override
	public void setVariables() {

		AnimationListener al = new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {

				animating = true;
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {

				animating = false;
			}
		};
		
		taIn = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_SELF, 0, 
				TranslateAnimation.RELATIVE_TO_SELF, 0,
				TranslateAnimation.RELATIVE_TO_SELF, -1, 
				TranslateAnimation.RELATIVE_TO_SELF, 0);
		taIn.setDuration(300);
		taIn.setAnimationListener(al);
		
		taOut = new TranslateAnimation(
				TranslateAnimation.RELATIVE_TO_SELF, 0, 
				TranslateAnimation.RELATIVE_TO_SELF, 0,
				TranslateAnimation.RELATIVE_TO_SELF, 0, 
				TranslateAnimation.RELATIVE_TO_SELF, -1);
		taOut.setDuration(300);
		taOut.setAnimationListener(al);
		
		aaIn = new AlphaAnimation(0, 1);
		aaIn.setDuration(300);
		aaIn.setAnimationListener(al);
		
		aaOut = new AlphaAnimation(1, 0);
		aaOut.setDuration(300);
		aaOut.setAnimationListener(al);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	public void createPage() {

		orders = new String[]{"최신순", "판매량순"};

		titleBar.getMenuButton().setVisibility(View.VISIBLE);

		defaultTopMargin = ResizeUtils.getSpecificLength(96);
		((RelativeLayout.LayoutParams)headerView.getLayoutParams()).topMargin = 
				defaultTopMargin;
		headerView.init((RetailActivity)mActivity);
		headerView.setTotalProduct(totalCount);
		
		//BlankView 더하기. 440 + 92 + 40= 572
		
		if(gridView.getHeaderViewCount() == 0) {
			blankView = new View(mContext);
			blankView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 
					ResizeUtils.getSpecificLength(572)));
			blankView.setBackgroundColor(Color.TRANSPARENT);
			gridView.addHeaderView(blankView);
		}
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		gridView.setAdapter(adapter);
		gridView.setNumColumns(2);
		
		noticeAdapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), notices);
		listView.setAdapter(noticeAdapter);
		
		//대표가 아닌 경우.
		if(mActivity.user.getRole() % 100 == 1
				|| mActivity.user.getRole() % 100 == 2) {
			
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				menuButtons[6].setAlpha(0.5f);
			}
			
			menuButtons[6].setEnabled(false);
		}
		
		boolean showNonReadNotification = SharedPrefsUtils.getBooleanFromPrefs(
				CphConstants.PREFS_NOTIFICATION, "showNonReadNotification"); 

		if(showNonReadNotification) {
			read.setBackgroundResource(R.drawable.main_notice_checkbox_b);
		} else {
			read.setBackgroundResource(R.drawable.main_notice_checkbox_a);
		}
		
		if(isMenuOpened) {
			noticeRelative.setVisibility(View.VISIBLE);
			cover.setVisibility(View.VISIBLE);
		}
		
		setButtons();
	}

	@Override
	public void setListeners() {

		titleBar.getMenuButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				if(menuRelative.getVisibility() == View.VISIBLE) {
					hideMenuRelative();
				} else {
					showMenuRelative();
				}
			}
		});
		
		titleBar.getBtnAddPartner().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showPage(CphConstants.PAGE_RETAIL_ADD_PARTNER, null);
			}
		});
		
		titleBar.getBtnNotice().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(noticeRelative.getVisibility() == View.VISIBLE) {
					hideNoticeRelative();
				} else {
					showNoticeRelative();
				}
			}
		});
		
		headerView.getBtnCategoryIndex().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				mActivity.showCategorySelectPopup(true, new OnAfterSelectCategoryListener() {
					
					@Override
					public void onAfterSelectCategory(Category category, Category subCategory) {

						if(category == null) {
							categoryIndex = 0;
							headerView.getBtnCategoryIndex().setText("전체");
						} else if(subCategory == null) {
							categoryIndex = category.getId();
							headerView.getBtnCategoryIndex().setText(category.getName());
						} else {
							categoryIndex = subCategory.getId();
							headerView.getBtnCategoryIndex().setText(subCategory.getName());
						}
						
						refreshPage();
					}
				});
			}
		});
		
		headerView.getBtnOrder().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mActivity.showSelectDialog(null, orders, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
					
						order = which;
						headerView.getBtnOrder().setText(orders[which]);
						
						refreshPage();
					}
				});
			}
		});

		gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				
				if(firstVisibleItem == 0 
						|| firstVisibleItem == 1) {
					headerView.setVisibility(View.VISIBLE);
					
					if(view.getChildAt(0) == null) {
						//Do nothing.
						
					} else {
						int offset = view.getChildAt(0).getTop();
						gridViewScrollChanged(offset);
					}
					headerShown = true;
				} else if(firstVisibleItem >= 2) {
					headerView.setVisibility(View.INVISIBLE);
					headerShown = false;
				}
				
				if(visibleItemCount < totalItemCount && firstVisibleItem + visibleItemCount == totalItemCount) {
					downloadInfo();
				}
			}
		});
		
		cover.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(noticeRelative.getVisibility() == View.VISIBLE) {
					hideNoticeRelative();
				} else if(menuRelative.getVisibility() == View.VISIBLE){
					hideMenuRelative();
				}
			}
		});
		
		read.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				boolean showNonReadNotification = SharedPrefsUtils.getBooleanFromPrefs(
						CphConstants.PREFS_NOTIFICATION, "showNonReadNotification"); 

				//SharedPrefs의 값을 반대로 저장, 없었다면 true로 저장.
				SharedPrefsUtils.addDataToPrefs(CphConstants.PREFS_NOTIFICATION, 
						"showNonReadNotification", 
						!showNonReadNotification);
				
				//안읽음 알림만 보기였다면 체크 해제.
				if(showNonReadNotification) {
					read.setBackgroundResource(R.drawable.main_notice_checkbox_a);
					
				//체크.
				} else {
					read.setBackgroundResource(R.drawable.main_notice_checkbox_b);
				}
				
				downloadNotices();
			}
		});
		
		btnClose.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				hideNoticeRelative();
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				
				try {
					String uriString = ((Notification)notices.get(position)).getUri();
					
					if(uriString.contains("cph://home")) {
						hideNoticeRelative();
					} else {
						mActivity.handleUri(Uri.parse(uriString));
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
				
				requestReadNotification((Notification)notices.get(position));
				checkNewMessage();
			}
		});

		int size = menuButtons.length;
		for(int i=0; i<size; i++) {
			
			final int INDEX = i;
			
			//대표가 아닌 경우.
			if((mActivity.user.getRole() % 100 == 1 || mActivity.user.getRole() % 100 == 2)
					&& i == 6) {
				continue;
			}
			
			menuButtons[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					hideMenuRelative();

					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							
							Bundle bundle = new Bundle();

							switch(INDEX) {
							
							case 0:
								mActivity.showPage(CphConstants.PAGE_RETAIL_SAMPLE, bundle);
								break;
							case 1:
								mActivity.showPage(CphConstants.PAGE_RETAIL_CUSTOMER_LIST, bundle);
								break;
							case 2:
								mActivity.showPage(CphConstants.PAGE_RETAIL_SEARCH, bundle);
								break;
							case 3:
								mActivity.showPage(CphConstants.PAGE_RETAIL_WISH_LIST, bundle);
								break;
							case 4:
								mActivity.showPage(CphConstants.PAGE_RETAIL_FAVORITE_SHOP, bundle);
								break;
							case 5:
								mActivity.showPage(CphConstants.PAGE_RETAIL_FAVORITE_PRODUCT, bundle);
								break;
							case 6:
								mActivity.showPage(CphConstants.PAGE_COMMON_STAFF, bundle);
								break;
							case 7:
								mActivity.showPage(CphConstants.PAGE_RETAIL_MANAGEMENT, bundle);
								break;
							case 8:
								mActivity.showPage(CphConstants.PAGE_COMMON_SETTING, bundle);
								break;
							}
						}
					}, 300);
				}
			});
		}
	}

	@Override
	public void setSizes() {
		
		RelativeLayout.LayoutParams rp = null;

		int length_short = ResizeUtils.getScreenWidth()/3;
		
		//titleBar2.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.retailMainPage_titleBar2).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(96);
		
		//cover.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.retailMainPage_cover).getLayoutParams();
		rp.topMargin = ResizeUtils.getSpecificLength(96);
		
		//noticeRelative.
		rp = (RelativeLayout.LayoutParams) menuRelative.getLayoutParams();
		rp.height = ResizeUtils.getScreenWidth() + ResizeUtils.getSpecificLength(30);
		
		//ivBg.
		rp = (RelativeLayout.LayoutParams) ivBg.getLayoutParams();
		rp.height = ResizeUtils.getScreenWidth();
		rp.bottomMargin = ResizeUtils.getSpecificLength(30);
	
		for(int i=0; i<menuButtons.length; i++) {
			rp = (RelativeLayout.LayoutParams) menuButtons[i].getLayoutParams();
			rp.width = (i % 3 == 2)? LayoutParams.MATCH_PARENT : length_short;
			rp.height = length_short;
		}
		
		//handle.
		rp = (RelativeLayout.LayoutParams) mThisView.findViewById(
				R.id.retailMainPage_handle).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(30);
		
		//noticeRelative.
		rp = (RelativeLayout.LayoutParams) noticeRelative.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(630);
		rp.height = ResizeUtils.getSpecificLength(711);
		rp.leftMargin = ResizeUtils.getSpecificLength(62);
		rp.topMargin = ResizeUtils.getSpecificLength(58);

		//read.
		rp = (RelativeLayout.LayoutParams) read.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(250);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.leftMargin = ResizeUtils.getSpecificLength(34);
		rp.topMargin = ResizeUtils.getSpecificLength(97);
		
		//btnClose.
		rp = (RelativeLayout.LayoutParams) btnClose.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(52);
		rp.height = ResizeUtils.getSpecificLength(52);
		rp.topMargin = ResizeUtils.getSpecificLength(96);
		rp.rightMargin = ResizeUtils.getSpecificLength(32);
		
		//listView.
		rp = (RelativeLayout.LayoutParams) listView.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(530);
		rp.leftMargin = ResizeUtils.getSpecificLength(24);
		rp.rightMargin = ResizeUtils.getSpecificLength(24);
		rp.bottomMargin = ResizeUtils.getSpecificLength(24);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_retail_main;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {

		if(noticeRelative.getVisibility() == View.VISIBLE) {
			hideNoticeRelative();
			return true;
		} else if(menuRelative.getVisibility() == View.VISIBLE) {
			hideMenuRelative();
			return true;
		}
		
		return false;
	}

	@Override
	public void downloadInfo() {

		//http://cph.minsangk.com/products?permission_type=retail&category_id=&sort=date-desc&wholesale_id=&num=10&page=1
		url = CphConstants.BASE_API_URL + "products" +
				"?permission_type=retail";
		
		if(categoryIndex != 0) {
			url += "&category_id=" + categoryIndex;
		}
		
		if(order == 0) {
			url +="&sort=date-desc";
		} else {
			url +="&sort=order-desc";
		}
		
		super.downloadInfo();
	}
	
	@Override
	public boolean parseJSON(JSONObject objJSON) {

		try {
			JSONArray arJSON = objJSON.getJSONArray("products");
			
			int size = arJSON.length();
			for(int i=0; i<size; i++) {
				Product product = new Product(arJSON.getJSONObject(i));
				product.setItemCode(CphConstants.ITEM_PRODUCT);
				product.setType(Product.TYPE_RETAIL);
				models.add(product);
			}

			if(size%2 == 1) {
				BaseModel emptyModel = new BaseModel() {};
				emptyModel.setItemCode(CphConstants.ITEM_PRODUCT);
				models.add(emptyModel);
			} else if(pageIndex == 1 && size == 0) {
				BaseModel emptyModel1 = new BaseModel() {};
				emptyModel1.setItemCode(CphConstants.ITEM_PRODUCT);
				models.add(emptyModel1);

				BaseModel emptyModel2 = new BaseModel() {};
				emptyModel2.setItemCode(CphConstants.ITEM_PRODUCT);
				models.add(emptyModel2);
			}
			
			totalCount = objJSON.getInt("productsCount");
			headerView.setTotalProduct(totalCount);
					
			if(size == 0 || size < NUMBER_OF_LISTITEMS) {
				return true;
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		return false;
	}
	
	@Override
	public int getBgResourceId() {

		return R.drawable.retail_bg;
	}
	
//////////////////// Custom methods.
	
	public void setButtons() {
		
		boolean bigFont = SharedPrefsUtils.getBooleanFromPrefs(CphConstants.PREFS_BIG_FONT, "bigfont");
		
		if(bigFont) {
			menuButtons[0].setBackgroundResource(R.drawable.big_retail_notice_btn);
			menuButtons[1].setBackgroundResource(R.drawable.big_retail_customer_btn);
			menuButtons[2].setBackgroundResource(R.drawable.big_retail_search_btn);
			menuButtons[3].setBackgroundResource(R.drawable.big_retail_basket_btn);
			menuButtons[4].setBackgroundResource(R.drawable.big_retail_favorite_shop_btn);
			menuButtons[5].setBackgroundResource(R.drawable.big_retail_favorite_goods_btn);
			menuButtons[6].setBackgroundResource(R.drawable.big_retail_staff);
			menuButtons[7].setBackgroundResource(R.drawable.big_retail_management_btn);
			menuButtons[8].setBackgroundResource(R.drawable.big_retail_setting_btn);
		} else {
			menuButtons[0].setBackgroundResource(R.drawable.retail_menu_sample_btn);
			menuButtons[1].setBackgroundResource(R.drawable.retail_customer_btn);
			menuButtons[2].setBackgroundResource(R.drawable.retail_search_btn);
			menuButtons[3].setBackgroundResource(R.drawable.retail_basket_btn);
			menuButtons[4].setBackgroundResource(R.drawable.retail_favorite_shop_btn);
			menuButtons[5].setBackgroundResource(R.drawable.retail_favorite_goods_btn);
			menuButtons[6].setBackgroundResource(R.drawable.retail_staff2_btn);
			menuButtons[7].setBackgroundResource(R.drawable.retail_business_btn);
			menuButtons[8].setBackgroundResource(R.drawable.retail_setting_btn);
		}
	}
	
	public void refreshNotices() {
		
		notices.clear();
		noticeAdapter.notifyDataSetChanged();
		downloadNotices();
	}
	
	public void downloadNotices() {
		
		String url = CphConstants.BASE_API_URL + "notifications/mine?num=0";

		if(SharedPrefsUtils.getBooleanFromPrefs(CphConstants.PREFS_NOTIFICATION, "showNonReadNotification")) {
			url += "&filter=unread";
		} else {
			url += "&filter=all";
		}
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("CmonsFragment.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("CmonsFragment.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					notices.clear();
					checkNewMessage();
					
					try {
						JSONArray arJSON = objJSON.getJSONArray("notifications");
						
						int size = arJSON.length();
						
						for(int i=0; i<size; i++) {
							Notification notification = new Notification(arJSON.getJSONObject(i));
							notification.setItemCode(CphConstants.ITEM_NOTIFICATION);
							LogUtils.log("###RetailMainPage.parseJSON.  message : " + notification.getMessage());
							notices.add(notification);
						}
						
						noticeAdapter.notifyDataSetChanged();
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (Error e) {
						LogUtils.trace(e);
					}
					
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	public void checkNewMessage() {
		
		String url = CphConstants.BASE_API_URL + "notifications/mine?num=0&filter=unread";
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("RetailMainPage.checkNewMessage.onError." + "\nurl : " + url);

			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("RetailMainPage.checkNewMessage.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					//새로운 메시지가 있느냐의 여부에 따라 new badge 노출 결정.
					if(objJSON.getJSONArray("notifications").length() > 0) {
						titleBar.getNewBadge().setVisibility(View.VISIBLE);
					} else {
						titleBar.getNewBadge().setVisibility(View.INVISIBLE);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public void showMenuRelative() {
		
		if(!animating && menuRelative.getVisibility() != View.VISIBLE) {
			
			menuRelative.setVisibility(View.VISIBLE);
			cover.setVisibility(View.VISIBLE);
			
			menuRelative.startAnimation(taIn);
			cover.startAnimation(aaIn);
		}
	}
	
	public void hideMenuRelative() {
		
		if(!animating && menuRelative.getVisibility() == View.VISIBLE) {
			
			menuRelative.setVisibility(View.INVISIBLE);
			cover.setVisibility(View.INVISIBLE);
			
			menuRelative.startAnimation(taOut);
			cover.startAnimation(aaOut);
		}
	}

	public void showNoticeRelative() {

		if(!animating && noticeRelative.getVisibility() != View.VISIBLE) {
			
			isMenuOpened = true;
			
			noticeRelative.setVisibility(View.VISIBLE);
			cover.setVisibility(View.VISIBLE);
			
			noticeRelative.startAnimation(aaIn);
			cover.startAnimation(aaIn);
		}
	}
	
	public void hideNoticeRelative() {

		if(!animating && noticeRelative.getVisibility() == View.VISIBLE) {
			
			isMenuOpened = false;
			
			noticeRelative.setVisibility(View.INVISIBLE);
			cover.setVisibility(View.INVISIBLE);
			
			noticeRelative.startAnimation(aaOut);
			cover.startAnimation(aaOut);
		}
	}

	public void requestReadNotification(Notification notification) {
		
		//http://cph.minsangk.com/notifications/read?notification_id=1
		String url = CphConstants.BASE_API_URL + "notifications/read" +
				"?notification_id=" + notification.getId();
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("RetailMainPage.requestReadNotification.onError." + "\nurl : " + url);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("RetailMainPage.requestReadNotification.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						refreshNotices();
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}

	public void gridViewScrollChanged(int scrollOffset) {

		if(scrollOffset <= 0) {
			((RelativeLayout.LayoutParams)headerView.getLayoutParams()).topMargin = 
					defaultTopMargin + scrollOffset;
			headerView.requestLayout();
		}
	}
}

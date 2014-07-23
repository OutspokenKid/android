//package com.zonecomms.clubcage.fragments;
//
//import java.util.ArrayList;
//
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.FrameLayout.LayoutParams;
//
//import com.outspoken_kid.classes.BaseFragment;
//import com.outspoken_kid.utils.ResizeUtils;
//import com.zonecomms.clubcage.R;
//import com.zonecomms.clubcage.models.GridMenu;
//import com.zonecomms.clubcage.models.GridMenuGroup;
//import com.zonecomms.clubcage.views.ButtonForGrid;
//
///**
// * baseColor.
// * 
// * menuGroup.
// * 타입은 두가지(텍스트가 들어가는 것과 이미지뷰)
// * 	1.getImageUrl이 null이 아닌 경우 다운로드.
// * 	2.getIconUrl이나 getIconResId가 null 또는 0이 아닌 경우, icon 설정 또는 다운로드.
// * 
// * getGridMenus가 null 인 경우 화면 전체 사이즈의 정사각형.
// * null이 아닌 경우 화면 절반 사이즈의 정사각형 + 1/4 사이즈의 정사각형 4개,
// * getGridMenus는 무조건 0개 아니면 4개여야 함.
// */
//public class CategoryPage extends BaseFragment {
//
//	private LinearLayout mainLinear;
//	private ArrayList<GridMenuGroup> menuGroups = new ArrayList<GridMenuGroup>();
//	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		
//		if(container == null) {
//			return null;
//		}
//	
//		mThisView = inflater.inflate(R.layout.page_category, null);
//		return mThisView;
//	}
//	
//	@Override
//	public void onActivityCreated(Bundle savedInstanceState) {
//		super.onActivityCreated(savedInstanceState);
//		
//		bindViews();
//		setVariables();
//		createPage();
//		
//		setListener();
//		setSize();
//	}
//	
//	@Override
//	protected void bindViews() {
//		mainLinear = (LinearLayout) mThisView.findViewById(R.id.categoryPage_mainLinear);
//	}
//
//	@Override
//	protected void setVariables() {
//
//		int[] baseColor = new int[]{230, 0, 19};
//		
//		for(int i=0; i<3; i++) {
//			GridMenuGroup menuGroup = new GridMenuGroup();
//			menuGroup.setBaseColor(baseColor);
//			menuGroup.setColorLevel(i);
//			menuGroup.setText("MenuGroup" + (i+1));
//			menuGroups.add(menuGroup);
//			
//			for(int j=0; j<4; j++) { 
//				GridMenu menu = new GridMenu();
//				menu.setBaseColor(baseColor);
//				menu.setColorLevel(i);
//				menu.setText("menu" + (i+1) + "-" + (j+1));
//				menuGroup.getGridMenus().add(menu);
//			}
//		}
//	}
//
//	@Override
//	protected void createPage() {
//		
//		int size = menuGroups.size();
//		for(int i=0; i<size; i++) {
//			
//			if(menuGroups.get(i).getGridMenus().size() == 0) {
//				ButtonForGrid btnGroup = new ButtonForGrid(mContext);
//				ResizeUtils.viewResize(624, 624, btnGroup, 1, 0, new int[]{0, i==0?8:0, 0, 8});
//				btnGroup.setButtonByMenu(menuGroups.get(i));
//				mainLinear.addView(btnGroup);
//			} else {
//				LinearLayout linearForGroup = new LinearLayout(mContext);
//				ResizeUtils.viewResize(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, linearForGroup, 
//						1, Gravity.CENTER_HORIZONTAL, new int[]{0, i==0?8:0, 0, 8});
//				linearForGroup.setOrientation(LinearLayout.HORIZONTAL);
//				mainLinear.addView(linearForGroup);
//				
//				ButtonForGrid btnGroup = new ButtonForGrid(mContext);
//				ResizeUtils.viewResize(308, 308, btnGroup, 1, 0, null);
//				btnGroup.setButtonByMenu(menuGroups.get(i));
//				
//				LinearLayout linearForGroup1 = new LinearLayout(mContext);
//				ResizeUtils.viewResize(150, LayoutParams.WRAP_CONTENT, linearForGroup1, 1, 0, null);
//				linearForGroup1.setOrientation(LinearLayout.VERTICAL);
//				
//				ButtonForGrid menu1 = new ButtonForGrid(mContext);
//				ResizeUtils.viewResize(150, 150, menu1, 1, 0, new int[]{0, 0, 0, 8});
//				menu1.setButtonByMenu(menuGroups.get(i).getGridMenus().get(0));
//				linearForGroup1.addView(menu1);
//				
//				ButtonForGrid menu2 = new ButtonForGrid(mContext);
//				ResizeUtils.viewResize(150, 150, menu2, 1, 0, null);
//				menu2.setButtonByMenu(menuGroups.get(i).getGridMenus().get(1));
//				linearForGroup1.addView(menu2);
//				
//				LinearLayout linearForGroup2 = new LinearLayout(mContext);
//				ResizeUtils.viewResize(150, LayoutParams.WRAP_CONTENT, linearForGroup2, 1, 0, null);
//				linearForGroup2.setOrientation(LinearLayout.VERTICAL);
//				
//				ButtonForGrid menu3 = new ButtonForGrid(mContext);
//				ResizeUtils.viewResize(150, 150, menu3, 1, 0, new int[]{0, 0, 0, 8});
//				menu3.setButtonByMenu(menuGroups.get(i).getGridMenus().get(2));
//				linearForGroup2.addView(menu3);
//				
//				ButtonForGrid menu4 = new ButtonForGrid(mContext);
//				ResizeUtils.viewResize(150, 150, menu4, 1, 0, null);
//				menu4.setButtonByMenu(menuGroups.get(i).getGridMenus().get(3));
//				linearForGroup2.addView(menu4);
//				
//				if(i%2 == 0) {
//					linearForGroup.addView(btnGroup);
//					linearForGroup.addView(linearForGroup1);
//					linearForGroup.addView(linearForGroup2);
//					ResizeUtils.setMargin(linearForGroup1, new int[]{8, 0, 8, 0});
//				} else{
//					linearForGroup.addView(linearForGroup1);
//					linearForGroup.addView(linearForGroup2);
//					linearForGroup.addView(btnGroup);
//					ResizeUtils.setMargin(linearForGroup2, new int[]{8, 0, 8, 0});
//				}
//			}
//		}
//	}
//
//	@Override
//	protected void setListener() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected void setSize() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected void downloadInfo() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected void setPage() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected String getTitleText() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	protected int getContentViewId() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//
//	@Override
//	public boolean onBackKeyPressed() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public void onRefreshFinished() {
//		// TODO Auto-generated method stub
//
//	}
//}

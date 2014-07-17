package com.zonecomms.golfn;

import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.zonecomms.golfn.classes.ApplicationManager;
import com.zonecomms.golfn.classes.BaseFragment;
import com.zonecomms.golfn.classes.SetupClass;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * @author HyungGunKim
 *
 */
public abstract class BaseFragmentActivity extends FragmentActivity {

	protected Context context;

	//For animationDrawable.
	protected AnimationDrawable animationDrawable;
	protected ImageView ivAnimationLoadingView;
	protected boolean animationLoaded;
	
	protected View loadingView;

	protected abstract void bindViews();
	protected abstract void setVariables(); 
	protected abstract void createPage();
	protected abstract void setListeners();
	protected abstract void setSizes();
	protected abstract void downloadInfo();
	protected abstract void setPage(boolean downloadSuccess);
	protected abstract void onMenuKeyPressed();
	protected abstract void onBackKeyPressed();
	protected abstract int getXmlResId();
	protected abstract int getMainLayoutResId();
	protected abstract int getFragmentFrameResId();
	public abstract View getMainLayout();
	
	/**
	 * Set title text.
	 * Called when page changed.
	 */
	public abstract void setTitleText(String title);
	
	/**
	 * Get loadingView.
	 * Create custom view and return it.
	 * 
	 * @return loadingView.
	 */
	public abstract void setLoadingView();
	public abstract void setAnimationLoadingView();

	/**
	 * Set page transition animation.
	 * 
	 * @param ft FragmentTransaction.
	 * @param beforeMain before main or not.
	 */
	public abstract void setAnim(FragmentTransaction ft, boolean atMain, boolean onStartPage);

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		LogUtils.log("BaseFragmentActivity.onCreate");
		
		try {
			setContentView(getXmlResId());
			SetupClass.setupApplication(this);
			context = this;
			
			bindViews();
			setVariables();
			createPage();
			setListeners();
			setSizes();
			
			downloadInfo();
		} catch (Exception e) {
			LogUtils.trace(e);
			finish();
		} catch (Error e) {
			LogUtils.trace(e);
			finish();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		LogUtils.log("BaseFragmentActivity.onResume.");
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		LogUtils.log("BaseFragmentActivity.onPause.");
	}

	@Override
	public void finish() {
		super.finish();
		
		ApplicationManager.clearFragments();
		ApplicationManager.clearActivities();
		ViewUnbindHelper.unbindReferences(this, getMainLayoutResId());
		SoftKeyboardUtils.hideKeyboard(this, getMainLayout());
		hideLoadingView();		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		LogUtils.log("BaseFragmentActivity.onDestory");
		ViewUnbindHelper.unbindReferences(this, getMainLayoutResId());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if(event.getAction() == KeyEvent.ACTION_DOWN) {
			
			switch(keyCode) {
			
			case KeyEvent.KEYCODE_MENU :

				try {
					onMenuKeyPressed();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
				break;
			
			case KeyEvent.KEYCODE_BACK :

				try {
					onBackKeyPressed();
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
				break;
				
				default:
					return super.onKeyDown(keyCode, event);
			}
		}
		
		return true;
	}

	protected void startPage(BaseFragment fragment, Bundle bundle) {

		try {
			if(bundle != null) {
				fragment.setArguments(bundle);
			}

			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

			if(ApplicationManager.getTopFragment() == null || ApplicationManager.getFragmentsSize() == 0) {
				//MainPage.
			} else if(ApplicationManager.getFragmentsSize() == 0) {
			} else if(ApplicationManager.getFragmentsSize() == 1) {
				setAnim(ft, true, true);
			} else {
				setAnim(ft, false, true);
			}

			if(ApplicationManager.getInstance().getFragments().size() != 0) {
				ft.hide(ApplicationManager.getTopFragment());
			}
			
			ft.add(getFragmentFrameResId(), fragment);
			ft.commitAllowingStateLoss();
			
			SoftKeyboardUtils.hideKeyboard(this, getMainLayout());
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void finishFragment(BaseFragment fragment) {
		
		try {
			ApplicationManager.removeFragment(fragment);
			
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			
			if(ApplicationManager.getFragmentsSize() != 0) {
				setAnim(ft, ApplicationManager.getFragmentsSize() == 1, false);
			}
			ft.remove(fragment);
			ft.show(ApplicationManager.getTopFragment());
			ft.commitAllowingStateLoss();
			
			SoftKeyboardUtils.hideKeyboard(this, getMainLayout());
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void clearFragments() {
		
		try {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			
			int size = ApplicationManager.getInstance().getFragments().size();
			for(int i=size-1; i==0; i--) {
				ft.remove(ApplicationManager.getInstance().getFragments().get(i));
			}
			
			ft.commitAllowingStateLoss();
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void clearFragmentsWithoutMain() {
		
		try {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			setAnim(ft, true, false);
			
			int size = ApplicationManager.getInstance().getFragments().size();
			for(int i=size-1; i>0; i--) {
				ft.remove(ApplicationManager.getInstance().getFragments().get(i));
			}
			
			ft.show(ApplicationManager.getInstance().getFragments().get(0));
			ft.commitAllowingStateLoss();
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void clearFragmentsForSideMenu() {
		
		try {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			
			int size = ApplicationManager.getInstance().getFragments().size();
			for(int i=size-1; i>0; i--) {
				ft.remove(ApplicationManager.getInstance().getFragments().get(i));
			}
			
			ft.show(ApplicationManager.getInstance().getFragments().get(0));
			ft.commitAllowingStateLoss();
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void showLoadingView() {
		
		LogUtils.log("###BFA.showLoadingView.  ");
		
		if(animationLoaded) {
			try {
				ivAnimationLoadingView.setVisibility(View.VISIBLE);
				
				if(animationDrawable != null) {
					animationDrawable.start();
				}
			} catch(Exception e) {
				animationLoaded = false;
				ivAnimationLoadingView = null;
				animationDrawable = null;
				showLoadingView();
			}
		} else if(loadingView != null && loadingView.getVisibility() != View.VISIBLE) {
			loadingView.setVisibility(View.VISIBLE);
		}
	}
	
	public void hideLoadingView() {
		
		LogUtils.log("###BFA.hideLoadingView.  ");
		
		if(animationLoaded
				&& ivAnimationLoadingView != null
				&& ivAnimationLoadingView.getVisibility() == View.VISIBLE) {
			try {
				ivAnimationLoadingView.setVisibility(View.INVISIBLE);
				
				if(animationDrawable != null) {
					animationDrawable.stop();
				}
			} catch(Exception e) {
				animationLoaded = false;
				ivAnimationLoadingView = null;
				animationDrawable = null;
			}
		} else if(loadingView != null && loadingView.getVisibility() == View.VISIBLE) {
			loadingView.setVisibility(View.INVISIBLE);
		}
	}

	public void showAlertDialog(String title, String message,
			final OnPositiveClickedListener onPositiveClickedListener) {
		showAlertDialog(title, message, "OK", "Cancel", onPositiveClickedListener, false);
	}
	
	public void showAlertDialog(String title, String message,
			final OnPositiveClickedListener onPositiveClickedListener,
			boolean needCancel) {
		showAlertDialog(title, message, "OK", "Cancel", onPositiveClickedListener, needCancel);
	}
	
	public void showAlertDialog(String title, String message, 
			String positive, String negative,
			final OnPositiveClickedListener onPositiveClickedListener,
			boolean needCancel) {
		
		try {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setTitle(title);
			adb.setPositiveButton(positive, 
					new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {

					if(onPositiveClickedListener != null) {
						
						try {
							onPositiveClickedListener.onPositiveClicked();
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (Error e) {
							LogUtils.trace(e);
						}
					}
				}
			});
			
			if(needCancel) {
				adb.setNegativeButton(negative, null);
				adb.setCancelable(true);
				adb.setOnCancelListener(null);
			}
			
			adb.setMessage(message);
			adb.show();
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
////////////////// Interfaces.
	
	public interface OnPositiveClickedListener {
		
		public void onPositiveClicked();
	}

	public interface OnLoadingViewShownListener {
		
		public void onLoadingViewShown(View loadingView);
	}
}

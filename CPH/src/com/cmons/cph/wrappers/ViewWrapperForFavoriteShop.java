package com.cmons.cph.wrappers;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.ShopActivity;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.classes.ViewWrapper;
import com.cmons.cph.models.Wholesale;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class ViewWrapperForFavoriteShop extends ViewWrapper {
	
	private Wholesale wholesale;
	
	public TextView textView;
	public View delete;
	
	public ViewWrapperForFavoriteShop(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		try {
			textView = (TextView) row.findViewById(R.id.list_wholesale_textView);
			delete = row.findViewById(R.id.list_wholesale_delete);
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setSizes() {

		try {
			int p = ResizeUtils.getSpecificLength(20);
			
			textView.getLayoutParams().height = ResizeUtils.getSpecificLength(120);
			textView.setPadding(p, 0, 0, 0);
			
			RelativeLayout.LayoutParams rp = (RelativeLayout.LayoutParams) delete.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(50);
			rp.height = ResizeUtils.getSpecificLength(50);
			rp.rightMargin = p;
			
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setValues(BaseModel baseModel) {

		try {
			if(baseModel instanceof Wholesale) {
				wholesale = (Wholesale) baseModel;
				textView.setText(wholesale.getName() + "(" + wholesale.getOwner_name() + 
						") 청평화몰 " + wholesale.getLocation());
			} else {
				setUnusableView();
			}
		} catch(Exception e) {
			LogUtils.trace(e);
			setUnusableView();
		}
	}

	@Override
	public void setListeners() {
		
		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ShopActivity.getInstance().showAlertDialog("즐겨찾기 삭제", "해당 매장을 삭제하시겠습니까?", 
						"확인", "취소", 
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								delete();
							} 
						}, null);
			}
		});
	}
	
	@Override
	public void setUnusableView() {
		// TODO Auto-generated method stub
		
	}
	
//////////////////// Custom methods.
	
	public void delete() {

		String url = CphConstants.BASE_API_URL + "retails/favorite/wholesales/delete" +
				"?id=" + wholesale.getId();
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("ViewWrapperForFavoriteShop.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToDeleteFavorite);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("ViewWrapperForFavoriteShop.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_deleteFavorite);
						ShopActivity.getInstance().getTopFragment().refreshPage();
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToDeleteFavorite);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToDeleteFavorite);
					LogUtils.trace(oom);
				}
			}
		});
	}
}

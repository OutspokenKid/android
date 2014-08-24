package com.cmons.cph.fragments.wholesale;

import org.json.JSONObject;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphAdapter;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.OrderSet;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.ToastUtils;

public class WholesaleForOrderListPage extends CmonsFragmentForWholesale {

	private Button btnOnGoing;
	private Button btnFinished;
	private TextView tvOrder;
	private EditText editText;
	private Button btnSearch;
	
	private ListView listView;
	
	private int menuIndex;
	private String keyword;
	
	@Override
	public void onResume() {
		super.onResume();
		
		if(models.size() == 0) {
			downloadInfo();
		}
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleOrderListPage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.wholesaleOrderListPage_ivBg);
		
		btnOnGoing = (Button) mThisView.findViewById(R.id.wholesaleOrderListPage_btnOnGoing);
		btnFinished = (Button) mThisView.findViewById(R.id.wholesaleOrderListPage_btnFinished);
		tvOrder = (TextView) mThisView.findViewById(R.id.wholesaleOrderListPage_tvOrder);
		editText = (EditText) mThisView.findViewById(R.id.wholesaleOrderListPage_editText);
		btnSearch = (Button) mThisView.findViewById(R.id.wholesaleOrderListPage_btnSearch);
		
		listView = (ListView) mThisView.findViewById(R.id.wholesaleOrderListPage_listView);
	}

	@Override
	public void setVariables() {

		title = "주문내역";
	}

	@Override
	public void createPage() {
		
		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.INVISIBLE);
		
		adapter = new CphAdapter(mContext, getActivity().getLayoutInflater(), models);
		listView.setAdapter(adapter);
	}

	@Override
	public void setListeners() {

		btnOnGoing.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(0);
			}
		});
		
		btnFinished.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				setMenu(1);
			}
		});
		
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(editText.getEditableText() == null
						|| editText.getEditableText().length() == 0) {
					ToastUtils.showToast(R.string.wrongSearchKeyword);
					return;
				}
				
				keyword = editText.getEditableText().toString();
				refreshPage();
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {

				mActivity.showOrderPage();
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;
		
		//btnOnGoing.
		rp = (RelativeLayout.LayoutParams) btnOnGoing.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/2;
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//btnFinished.
		rp = (RelativeLayout.LayoutParams) btnFinished.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
		
		//tvOrder.
		int p = ResizeUtils.getSpecificLength(10);
		tvOrder.setPadding(p, p*4, p, p);
		FontUtils.setFontSize(tvOrder, 30);
		
		//editText.
		rp = (RelativeLayout.LayoutParams) editText.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(550);
		rp.height = ResizeUtils.getSpecificLength(92);
		FontUtils.setFontAndHintSize(editText, 30, 24);

		//btnSearch.
		rp = (RelativeLayout.LayoutParams) btnSearch.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_orderlist;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		
		for(int i=0; i<10; i++) {
			OrderSet orderSet = new OrderSet();
			orderSet.setItemCode(CphConstants.ITEM_ORDERSET);
			models.add(orderSet);
		}
		return false;
	}

	@Override
	public void downloadInfo() {
		
		url = CphConstants.BASE_API_URL + "wholesales/notices";
		super.downloadInfo();
	}

//////////////////// Custom classes.
	
	public void setMenu(int menuIndex) {
		
		if(menuIndex == 0) {
			btnOnGoing.setBackgroundResource(R.drawable.order_progressing_btn_a);
			btnFinished.setBackgroundResource(R.drawable.order_complete_btn_b);
		} else {
			btnOnGoing.setBackgroundResource(R.drawable.order_progressing_btn_b);
			btnFinished.setBackgroundResource(R.drawable.order_complete_btn_a);
		}
		
		this.menuIndex = menuIndex;
		
		refreshPage();
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.order_bg;
	}
}

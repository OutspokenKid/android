package com.cmons.cph.fragments.wholesale;

import java.util.ArrayList;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.ShopActivity.OnAfterSelectCategoryListener;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.models.Category;
import com.cmons.cph.models.Product;
import com.cmons.cph.utils.ImageUploadUtils.OnAfterUploadImage;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class WholesaleForWritePage extends CmonsFragmentForWholesale {

    private TextView tvStatus;
    private TextView tvSoldOut;
    private View checkbox;
    private TextView tvImageText;
    private Button btnImage1;
    private Button btnImage2;
    private Button btnImage3;
    private ImageView[] ivImages;
    private TextView tvImageSizeText;
    private TextView tvName;
    private EditText etName;
    private TextView tvPrice;
    private EditText etPrice;
    private TextView tvCategory;
    private Button btnCategory;
    private TextView tvColor;
    private Button btnColor;
    private TextView tvSize;
    private Button btnSize;
    private TextView tvMixtureRate;
    private EditText etMixtureRate;
    private TextView tvDescription;
    private EditText etDescription;
    private TextView tvPublic;
    private Button btnPublic1;
    private Button btnPublic2;
    private TextView tvNotification;
    private Button btnNotification;
    private Button btnPullUp;
    private TextView tvNotificationText;
    private Button btnDelete;
    
    private RelativeLayout relativePopup;
    private EditText etAdd;
    private Button btnAdd;
    private ListView listView;
    private ChoiceAdapter adapter;

    private Product product;
    private boolean isSoldOut;
    private boolean isPublic;
	private boolean needPush;
	
	private String[] selectedImageUrls = new String[3];
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleWritePage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.wholesaleWritePage_ivBg);
		
		tvStatus = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvStatus);
		tvSoldOut = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvSoldOut);
		checkbox = mThisView.findViewById(R.id.wholesaleWritePage_checkbox);
		tvImageText = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvImageText);
		btnImage1 = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnImage1);
		btnImage2 = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnImage2);
		btnImage3 = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnImage3);
		
		ivImages = new ImageView[3];
		ivImages[0] = (ImageView) mThisView.findViewById(R.id.wholesaleWritePage_ivImage1);
		ivImages[1] = (ImageView) mThisView.findViewById(R.id.wholesaleWritePage_ivImage2);
		ivImages[2] = (ImageView) mThisView.findViewById(R.id.wholesaleWritePage_ivImage3);
		tvImageSizeText = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvImageSizeText);
		tvName = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvName);
		etName = (EditText) mThisView.findViewById(R.id.wholesaleWritePage_etName);
		tvPrice = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvPrice);
		etPrice = (EditText) mThisView.findViewById(R.id.wholesaleWritePage_etPrice);
		tvCategory = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvCategory);
		btnCategory = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnCategory);
		tvColor = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvColor);
		btnColor = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnColor);
		tvSize = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvSize);
		btnSize = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnSize);
		tvMixtureRate = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvMixtureRate);
		etMixtureRate = (EditText) mThisView.findViewById(R.id.wholesaleWritePage_etMixtureRate);
		tvDescription = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvDescription);
		etDescription = (EditText) mThisView.findViewById(R.id.wholesaleWritePage_etDescription);
		tvPublic = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvPublic);
		btnPublic1 = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnPublic1);
		btnPublic2 = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnPublic2);
		tvNotification = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvNotification);
		btnNotification = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnNotification);
		btnPullUp = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnPullUp);
		tvNotificationText = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvNotificationText);
		btnDelete = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnDelete);
		
		relativePopup = (RelativeLayout) mThisView.findViewById(R.id.wholesaleWritePage_relativePopup);
		etAdd = (EditText) mThisView.findViewById(R.id.wholesaleWritePage_etAdd);
		btnAdd = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnAdd);
		listView = (ListView) mThisView.findViewById(R.id.wholesaleWritePage_listView);
	}

	@Override
	public void setVariables() {

		if(getArguments() != null) {
			product = (Product) getArguments().getSerializable("product");
		}
		
		if(product != null) {
			title = "공지사항 수정";
			
			//isSoldOut 설정.
			isSoldOut = product.getStatus() == -1;
			
			//isPublic 설정.
			isPublic = product.getCustomers_only() == 0;
			
			//needPush 설정.
			needPush = product.getNeed_push() == 1;
			
		} else {
			title = "공지사항";
			
			isPublic = true;
			needPush = true;
		}
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
		
		//수정.
		if(product != null) {
			tvStatus.setVisibility(View.VISIBLE);
			tvSoldOut.setVisibility(View.VISIBLE);
			checkbox.setVisibility(View.VISIBLE);
			
			if(isSoldOut) {
				checkbox.setBackgroundResource(R.drawable.myshop_checkbox_b);
				tvStatus.setText(R.string.productSoldOut);
			} else{
				checkbox.setBackgroundResource(R.drawable.myshop_checkbox_a);
				tvStatus.setText(R.string.productSelling);
			}
			
			if(isPublic) {
				btnPublic1.setBackgroundResource(R.drawable.myshop_open1_a);
				btnPublic2.setBackgroundResource(R.drawable.myshop_open2_b);
			} else {
				btnPublic1.setBackgroundResource(R.drawable.myshop_open1_b);
				btnPublic2.setBackgroundResource(R.drawable.myshop_open2_a);
			}
			
			btnPullUp.setVisibility(View.VISIBLE);
			btnNotification.setVisibility(View.GONE);
			btnDelete.setVisibility(View.VISIBLE);
			
			tvNotificationText.setText(R.string.productPullUpDesc);
			
			//이미지 설정.
			if(product.getProduct_images() != null) {
				
				for(int i=0; i<product.getProduct_images().length; i++) {

					String imageUrl = product.getProduct_images()[i];
					
					if(!StringUtils.isEmpty(imageUrl)) {
						final ImageView imageView = ivImages[i];
						selectedImageUrls[i] = imageUrl;
						
						DownloadUtils.downloadBitmap(imageUrl,
								new OnBitmapDownloadListener() {

									@Override
									public void onError(String url) {

										LogUtils.log("WholesaleForWritePage.onError."
												+ "\nurl : " + url);
									}

									@Override
									public void onCompleted(String url,
											Bitmap bitmap) {

										try {
											LogUtils.log("WholesaleForWritePage.onCompleted."
													+ "\nurl : " + url);

											if(imageView != null) {
												imageView.setImageBitmap(bitmap);
											}
										} catch (Exception e) {
											LogUtils.trace(e);
										} catch (OutOfMemoryError oom) {
											LogUtils.trace(oom);
										}
									}
								});
					}
				}
			}
			
			etName.setText(product.getName());
			etPrice.setText(product.getPrice());
			btnColor.setText(product.getColors());
			etMixtureRate.setText(product.getMixture_rate());
			etDescription.setText(product.getDesc());
			
		//등록.
		} else{
			tvStatus.setVisibility(View.GONE);
			tvSoldOut.setVisibility(View.GONE);
			checkbox.setVisibility(View.GONE);
			
			btnPullUp.setVisibility(View.GONE);
			btnNotification.setVisibility(View.VISIBLE);
			btnDelete.setVisibility(View.GONE);
			
			tvNotificationText.setText(R.string.productNotificationDesc);
		}
	}

	@Override
	public void setListeners() {

		btnImage1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				uploadImage(ivImages[0], 0);
			}
		});
		
		btnImage2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				uploadImage(ivImages[1], 1);
			}
		});
		
		btnImage3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				uploadImage(ivImages[2], 2);
			}
		});

		btnCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				mActivity.showCategorySelectPopup(false, new OnAfterSelectCategoryListener() {
					
					@Override
					public void onAfterSelectCategory(Category category, Category subCategory) {

						String categoryString = category.getName();
						
						if(subCategory != null) {
							categoryString += " - " + subCategory.getName();
						}
						ToastUtils.showToast(categoryString);
					}
				});
			}
		});
		
		btnColor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ArrayList<Item> items = new ArrayList<Item>();
				items.add(new Item("black"));
				items.add(new Item("white"));
				items.add(new Item("read"));
				items.add(new Item("green"));
				items.add(new Item("blue"));
				items.add(new Item("yellow"));
				showPopup(items);
			}
		});
		
		btnSize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				ArrayList<Item> items = new ArrayList<Item>();
				items.add(new Item("S"));
				items.add(new Item("M"));
				items.add(new Item("L"));
				items.add(new Item("XL"));
				items.add(new Item("XXL"));
				showPopup(items);
			}
		});
		
		btnPublic1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				isPublic = true;
				
				btnPublic1.setBackgroundResource(R.drawable.myshop_open1_a);
				btnPublic2.setBackgroundResource(R.drawable.myshop_open2_b);
			}
		});
		
		btnPublic2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				isPublic = false;
				
				btnPublic1.setBackgroundResource(R.drawable.myshop_open1_b);
				btnPublic2.setBackgroundResource(R.drawable.myshop_open2_a);
			}
		});
		
		if(product != null) {
			btnPullUp.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					pullUp();
				}
			});
			
			btnDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					delete();
				}
			});
		} else {
			btnNotification.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					needPush = !needPush;
					
					if(needPush) {
						btnNotification.setBackgroundResource(R.drawable.myshop_notification_a);
					} else {
						btnNotification.setBackgroundResource(R.drawable.myshop_notification_b);
					}
				}
			});
		}
		
		btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(etAdd.getText() == null || etAdd.getText().toString().length() == 0) {
					return;
				}
				
				adapter.addItem(etAdd.getText().toString());
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				adapter.selectItem(arg2);
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		int padding = ResizeUtils.getSpecificLength(16);
		int imageViewWidth = ResizeUtils.getSpecificLength(240);
		int imageViewHeight = ResizeUtils.getSpecificLength(375);
		int titleTextHeight = ResizeUtils.getSpecificLength(100);
		int editTextHeight = ResizeUtils.getSpecificLength(92);
		int buttonHeight = ResizeUtils.getSpecificLength(180);

		if(product != null) {
			//tvStatus.
			rp = (RelativeLayout.LayoutParams) tvStatus.getLayoutParams();
			rp.height = titleTextHeight;
			FontUtils.setFontSize(tvStatus, 30);
			tvStatus.setPadding(padding, 0, padding, 0);
			
			//checkbox.
			rp = (RelativeLayout.LayoutParams) checkbox.getLayoutParams();
			rp.width = ResizeUtils.getSpecificLength(44);
			rp.height = ResizeUtils.getSpecificLength(43);
			rp.topMargin = ResizeUtils.getSpecificLength(28);
			rp.rightMargin = ResizeUtils.getSpecificLength(20);
			
			//tvSoldOut.
			rp = (RelativeLayout.LayoutParams) tvSoldOut.getLayoutParams();
			rp.height = ResizeUtils.getSpecificLength(43);
		}
		
		//tvImageText.
		rp = (RelativeLayout.LayoutParams) tvImageText.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvImageText, 30);
		tvImageText.setPadding(padding, 0, 0, padding);
		
		//btnImage1.
		rp = (RelativeLayout.LayoutParams) btnImage1.getLayoutParams();
		rp.width = imageViewWidth;
		rp.height = imageViewHeight;
		
		//ivImage1.
		rp = (RelativeLayout.LayoutParams) ivImages[0].getLayoutParams();
		rp.width = imageViewWidth;
		rp.height = imageViewHeight;

		//btnImage2.
		rp = (RelativeLayout.LayoutParams) btnImage2.getLayoutParams();
		rp.width = imageViewWidth;
		rp.height = imageViewHeight;
		
		//ivImage2.
		rp = (RelativeLayout.LayoutParams) ivImages[1].getLayoutParams();
		rp.width = imageViewWidth;
		rp.height = imageViewHeight;
		
		//btnImage3.
		rp = (RelativeLayout.LayoutParams) btnImage3.getLayoutParams();
		rp.height = imageViewHeight;
		
		//ivImage3.
		rp = (RelativeLayout.LayoutParams) ivImages[2].getLayoutParams();
		rp.height = imageViewHeight;
		
		//tvImageSizeText.
		rp = (RelativeLayout.LayoutParams) tvImageSizeText.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(66);
		FontUtils.setFontSize(tvImageSizeText, 20);
		tvImageText.setPadding(padding, 0, 0, padding);
		
		//tvName.
		rp = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvName, 30);
		tvName.setPadding(padding, 0, 0, padding);
		
		//etName.
		rp = (RelativeLayout.LayoutParams) etName.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontAndHintSize(etName, 30, 24);
		etName.setPadding(padding, 0, padding, 0);
		
		//tvPrice.
		rp = (RelativeLayout.LayoutParams) tvPrice.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvPrice, 30);
		tvPrice.setPadding(padding, 0, 0, padding);
		
		//etPrice.
		rp = (RelativeLayout.LayoutParams) etPrice.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontAndHintSize(etPrice, 30, 24);
		etPrice.setPadding(padding, 0, padding, 0);
		
		//tvCategory.
		rp = (RelativeLayout.LayoutParams) tvCategory.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvCategory, 30);
		tvCategory.setPadding(padding, 0, 0, padding);
		
		//btnCategory.
		rp = (RelativeLayout.LayoutParams) btnCategory.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontSize(btnCategory, 24);
		
		//tvColor.
		rp = (RelativeLayout.LayoutParams) tvColor.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvColor, 30);
		tvColor.setPadding(padding, 0, 0, padding);
		
		//btnColor.
		rp = (RelativeLayout.LayoutParams) btnColor.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontSize(btnColor, 24);
		
		//tvSize.
		rp = (RelativeLayout.LayoutParams) tvSize.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvSize, 30);
		tvSize.setPadding(padding, 0, 0, padding);
		
		//btnSize.
		rp = (RelativeLayout.LayoutParams) btnSize.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontSize(btnSize, 24);
		
		//tvMixtureRate.
		rp = (RelativeLayout.LayoutParams) tvMixtureRate.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvMixtureRate, 30);
		tvMixtureRate.setPadding(padding, 0, 0, padding);
		
		//etMixtureRate.
		rp = (RelativeLayout.LayoutParams) etMixtureRate.getLayoutParams();
		rp.height = editTextHeight;
		FontUtils.setFontAndHintSize(etMixtureRate, 30, 24);
		etMixtureRate.setPadding(padding, 0, padding, 0);
		
		//tvDescription.
		rp = (RelativeLayout.LayoutParams) tvDescription.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvDescription, 30);
		tvDescription.setPadding(padding, 0, 0, padding);
		
		//etDescription.
		rp = (RelativeLayout.LayoutParams) etDescription.getLayoutParams();
		rp.height = editTextHeight * 4;
		FontUtils.setFontAndHintSize(etDescription, 30, 24);
		etDescription.setPadding(padding, padding, padding, padding);
		
		//tvPublic.
		rp = (RelativeLayout.LayoutParams) tvPublic.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvPublic, 30);
		tvPublic.setPadding(padding, 0, 0, padding);
		
		//btnPublic1.
		rp = (RelativeLayout.LayoutParams) btnPublic1.getLayoutParams();
		rp.width = ResizeUtils.getScreenWidth()/2;
		rp.height = editTextHeight;
		
		//btnPublic2.
		rp = (RelativeLayout.LayoutParams) btnPublic2.getLayoutParams();
		rp.height = editTextHeight;
		
		//tvNotification.
		rp = (RelativeLayout.LayoutParams) tvNotification.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvNotification, 30);
		tvNotification.setPadding(padding, 0, 0, padding);
		
		if(product != null) {
			//btnPullUp.
			rp = (RelativeLayout.LayoutParams) btnPullUp.getLayoutParams();
			rp.height = buttonHeight;
			
			//btnDelete.
			rp = (RelativeLayout.LayoutParams) btnDelete.getLayoutParams();
			rp.height = buttonHeight;
		} else {
			//btnNotification.
			rp = (RelativeLayout.LayoutParams) btnNotification.getLayoutParams();
			rp.height = buttonHeight;
		}
		
		//tvNotificationText.
		rp = (RelativeLayout.LayoutParams) tvNotificationText.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvNotificationText, 20);
		tvNotificationText.setPadding(padding, padding, 0, 0);
		
		//etAdd.
		rp = (RelativeLayout.LayoutParams) etAdd.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(550);
		rp.height = ResizeUtils.getSpecificLength(92);
		rp.topMargin = ResizeUtils.getSpecificLength(96);
		
		//btnAdd.
		rp = (RelativeLayout.LayoutParams) btnAdd.getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(92);
	}

	@Override
	public int getContentViewId() {

		return R.layout.fragment_wholesale_write;
	}

	@Override
	public boolean onMenuPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onBackPressed() {

		if(relativePopup.getVisibility() == View.VISIBLE) {
			hidePopup();
			return true;
		}
		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}
	
//////////////////// Custom methods.
	
	public void pullUp() {
		
	}
	
	public void delete() {
		
	}
	
	public void uploadImage(final ImageView ivImage, final int index) {
		
		mActivity.showUploadPhotoPopup(new OnAfterUploadImage() {
			
			@Override
			public void onAfterUploadImage(String resultString, Bitmap thumbnail) {
				
				if(thumbnail != null && !thumbnail.isRecycled() && ivImage != null) {
					ivImage.setImageBitmap(thumbnail);
				}
				
				selectedImageUrls[index] = resultString;
			}
		});
	}

	public void showPopup(ArrayList<Item> items) {

		relativePopup.setVisibility(View.VISIBLE);
		adapter = new ChoiceAdapter(items);
		listView.setAdapter(adapter);
	}
	
	public void hidePopup() {
		
		relativePopup.setVisibility(View.INVISIBLE);
	}
	
////////////////////Custom classes.
	
	public class ChoiceAdapter extends BaseAdapter {

		private ArrayList<Item> items;
			
		public ChoiceAdapter(ArrayList<Item> items) {

			this.items = items;
		}
		
		@Override
		public int getCount() {

			return items.size();
		}
	
		@Override
		public Object getItem(int arg0) {

			return items.get(arg0);
		}
	
		@Override
		public long getItemId(int arg0) {

			return arg0;
		}
	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			TextView textView;
			
			if(convertView == null) {
				textView = new TextView(mContext);
				textView.setGravity(Gravity.CENTER);
				textView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(92)));
			} else {
				textView = (TextView) convertView;
			}
			
			textView.setText(items.get(position).text);
			
			if(items.get(position).selected) {
				textView.setTextColor(Color.WHITE);
				textView.setBackgroundColor(Color.argb(100, 255, 255, 255));
				FontUtils.setFontSize(textView, 32);
			} else{
				textView.setTextColor(Color.LTGRAY);
				FontUtils.setFontSize(textView, 26);
				textView.setBackgroundColor(Color.argb(0, 0, 0, 0));
			}
			
			return textView;
		}
	
		public void addItem(String string) {
			
			Item item = new Item(string);
			item.selected = true;
			items.add(item);
			notifyDataSetChanged();
		}
		
		public ArrayList<Item> getItemList() {
			
			return items;
		}
		
		public void selectItem(int position) {
			
			Item selectedItem = items.get(position);
			selectedItem.selected = !selectedItem.selected;
			notifyDataSetChanged();
		}
	}
	
	public class Item {
		
		public String text;
		boolean selected;
		
		public Item(String text) {
			this.text = text;
		}
	}

	@Override
	public int getBgResourceId() {

		return R.drawable.myshop_bg;
	}
}

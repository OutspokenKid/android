package com.cmons.cph.fragments.wholesale;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONObject;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.ShopActivity.OnAfterSelectCategoryListener;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Category;
import com.cmons.cph.models.Product;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.ImageUploadUtils.OnAfterUploadImage;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class WholesaleForWritePage extends CmonsFragmentForWholesale {

	private static final int MODE_COLOR = 0;
	private static final int MODE_SIZE = 1;
	
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
    private Button btnDelete;
    
    private RelativeLayout relativePopup;
    private EditText etAdd;
    private Button btnAdd;
    private ListView listView;
    private ChoiceAdapter colorAdapter;
    private ChoiceAdapter sizeAdapter;

    private Product product;
    private boolean isPublic;
	private boolean needPush;
	private int selectedCategoryIndex;
	
	private String[] selectedImageUrls = new String[3];
	
	private ArrayList<Item> colorItems;
	private ArrayList<Item> sizeItems;
	private int mode;
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleWritePage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.wholesaleWritePage_ivBg);
		
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
		
		colorItems = new ArrayList<Item>();
		colorItems.add(new Item("블랙"));
		colorItems.add(new Item("화이트"));
		colorItems.add(new Item("아이보리"));
		colorItems.add(new Item("그레이"));
		colorItems.add(new Item("블루"));
		colorItems.add(new Item("빨간색"));
		colorItems.add(new Item("주황색"));
		colorItems.add(new Item("노랑색"));
		colorItems.add(new Item("카키색"));
		colorItems.add(new Item("곤색"));
		colorItems.add(new Item("연두색"));
		colorItems.add(new Item("하늘색"));
		colorItems.add(new Item("보라색"));
		colorAdapter = new ChoiceAdapter(colorItems);
		
		sizeItems = new ArrayList<Item>();
		sizeItems.add(new Item("XS"));
		sizeItems.add(new Item("S"));
		sizeItems.add(new Item("M"));
		sizeItems.add(new Item("L"));
		sizeItems.add(new Item("XL"));
		sizeItems.add(new Item("XXL"));
		sizeItems.add(new Item("44"));
		sizeItems.add(new Item("55"));
		sizeItems.add(new Item("66"));
		sizeItems.add(new Item("77"));
		sizeItems.add(new Item("88"));
		sizeItems.add(new Item("95"));
		sizeItems.add(new Item("100"));
		sizeItems.add(new Item("105"));
		sizeItems.add(new Item("110"));
		sizeItems.add(new Item("210"));
		sizeItems.add(new Item("220"));
		sizeItems.add(new Item("230"));
		sizeItems.add(new Item("240"));
		sizeItems.add(new Item("250"));
		sizeItems.add(new Item("260"));
		sizeItems.add(new Item("270"));
		sizeItems.add(new Item("280"));
		sizeItems.add(new Item("290"));
		sizeAdapter = new ChoiceAdapter(sizeItems);
		
		if(product != null) {
			title = "상품 수정";
			
			//isPublic 설정.
			isPublic = product.getCustomers_only() == 0;
			
			//needPush 설정.
			needPush = product.getNeed_push() == 1;
			
			boolean duplicated = false;
			
			//추가 사이즈 설정.
			String[] sizeStrings = product.getSizes().split("\\|");

			int size = sizeStrings.length;
			for(int i=0; i<size; i++) {
				
				duplicated = false;
				
				for(Item item : sizeItems) {
					if(sizeStrings[i].equals(item.text)) {
						duplicated = true;
						item.selected = true;
					}
				}
				
				if(!duplicated) {
					Item item = new Item(sizeStrings[i]);
					item.selected = true;
					sizeItems.add(item);
				}
			}
			
			//추가 색상 설정.
			String[] colorStrings = product.getColors().split("\\|");

			size = colorStrings.length;
			for(int i=0; i<size; i++) {
				
				duplicated = false;
				
				for(Item item : colorItems) {
					if(colorStrings[i].equals(item.text)) {
						duplicated = true;
						item.selected = true;
					}
				}
				
				if(!duplicated) {
					Item item = new Item(colorStrings[i]);
					item.selected = true;
					colorItems.add(item);
				}
			}
			
		} else {
			title = "상품 등록";
			
			isPublic = true;
			needPush = true;
		}
	}

	@Override
	public void createPage() {

		titleBar.getBackButton().setVisibility(View.VISIBLE);
		titleBar.getHomeButton().setVisibility(View.VISIBLE);
		
		listView.setDivider(new ColorDrawable(Color.WHITE));
		listView.setDividerHeight(1);
		
		//수정.
		if(product != null) {
			
			if(isPublic) {
				btnPublic1.setBackgroundResource(R.drawable.myshop_open1_a);
				btnPublic2.setBackgroundResource(R.drawable.myshop_open2_b);
			} else {
				btnPublic1.setBackgroundResource(R.drawable.myshop_open1_b);
				btnPublic2.setBackgroundResource(R.drawable.myshop_open2_a);
			}
			
			tvNotification.setVisibility(View.INVISIBLE);
			btnNotification.setVisibility(View.INVISIBLE);
			btnDelete.setVisibility(View.VISIBLE);
			
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
			etPrice.setText("" + product.getPrice());
			
			if(mActivity.categories != null) {
				String categoryText = null;
				selectedCategoryIndex = product.getCategory_id();
				int size = mActivity.categories.length;
				
				for(int i=0; i<size; i++) {
					categoryText = mActivity.categories[i].getCategoryStringById(selectedCategoryIndex);
					
					if(categoryText != null) {
						btnCategory.setText(categoryText);
						break;
					}
				}
			}
			
			btnColor.setText(product.getColors().replace("|", "/"));
			btnSize.setText(product.getSizes().replace("|", "/"));
			etMixtureRate.setText(product.getMixture_rate());
			etDescription.setText(product.getDesc());
			
		//등록.
		} else{
			tvNotification.setVisibility(View.VISIBLE);
			btnNotification.setVisibility(View.VISIBLE);
			btnDelete.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	public void setListeners() {

		titleBar.getBtnSubmit().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				if(relativePopup.getVisibility() == View.VISIBLE) {
					
					if(mode == MODE_COLOR) {
						String colorString = null;

						int size = colorItems.size();
						for(int i=0; i<size; i++) {

							if(colorItems.get(i).selected) {
								if(colorString == null) {
									colorString = "";
								} else {
									colorString += "/";
								}
								
								colorString += colorItems.get(i).text;
							}
						}
						
						btnColor.setText(colorString);
					} else {
						String sizeString = null;

						int size = sizeItems.size();
						for(int i=0; i<size; i++) {

							if(sizeItems.get(i).selected) {
								if(sizeString == null) {
									sizeString = "";
								} else {
									sizeString += "/";
								}
								
								sizeString += sizeItems.get(i).text;
							}
						}
						
						btnSize.setText(sizeString);
					}
					
					hidePopup();
					return;
				}
				
				if(selectedImageUrls == null 
						|| (selectedImageUrls[0] == null
							&& selectedImageUrls[1] == null
							&& selectedImageUrls[2] == null)) {
					//이미지를 등록해주세요.
					ToastUtils.showToast(R.string.wrongProductImage);
					return;
					
				} else if(etName.getText() == null){
					//상품명을 입력해주세요.
					ToastUtils.showToast(R.string.wrongProductName);
					return;
					
				} else if(etPrice.getText() == null) {
					//상품가격을 입력해주세요.
					ToastUtils.showToast(R.string.wrongProductPrice);
					return;
					
				} else if(btnCategory.getText() == null) {
					//상품분류를 선택해주세요.
					ToastUtils.showToast(R.string.wrongProductCategory);
					return;
				}
				
				int selectedCount = 0;
				int size = colorItems.size();
				for(int i=0; i<size; i++) {
					if(colorItems.get(i).selected) {
						selectedCount++;
					}
				}
				
				//색상.
				if(selectedCount == 0) {
					ToastUtils.showToast(R.string.wrongProductColor);
					return;
				}
				
				selectedCount = 0;
				size = sizeItems.size();
				for(int i=0; i<size; i++) {
					if(sizeItems.get(i).selected) {
						selectedCount++;
					}
				}
				
				//사이즈.
				if(selectedCount == 0) {
					ToastUtils.showToast(R.string.wrongProductSize);
					return;
				}
				
				//혼용률.
				if(etMixtureRate.getText() == null) {
					ToastUtils.showToast(R.string.wrongProductMixtureRate);
					return;
				}
				
				//상품 설명.
				if(etDescription.getText() == null) {
					ToastUtils.showToast(R.string.wrongProductDesc);
					return;
				}
				
				if(product == null) {
					write();
				} else {
					edit();
				}
			}
		});
		
		btnImage1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				if(ivImages[0].getDrawable() == null) {
					uploadImage(ivImages[0], 0);
				} else {
					choiceMode(0);
				}
			}
		});
		
		btnImage2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(ivImages[1].getDrawable() == null) {
					uploadImage(ivImages[1], 1);
				} else {
					choiceMode(1);
				}
			}
		});
		
		btnImage3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(ivImages[2].getDrawable() == null) {
					uploadImage(ivImages[2], 2);
				} else {
					choiceMode(2);
				}
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
//							categoryString += " - " + subCategory.getName();
							categoryString = subCategory.getName();
							selectedCategoryIndex = subCategory.getId();
						} else {
							selectedCategoryIndex = category.getId();
						}
						
						btnCategory.setText(categoryString);
					}
				});
			}
		});
		
		btnColor.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mode = MODE_COLOR;
				listView.setAdapter(colorAdapter);
				colorAdapter.notifyDataSetChanged();
				showPopup();
				ToastUtils.showToast(R.string.wrongProductColor);
			}
		});
		
		btnSize.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mode = MODE_SIZE;
				listView.setAdapter(sizeAdapter);
				sizeAdapter.notifyDataSetChanged();
				showPopup();
				ToastUtils.showToast(R.string.wrongProductSize);
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
				
				if(mode == MODE_COLOR) {
					colorAdapter.addItem(etAdd.getText().toString());
				} else {
					sizeAdapter.addItem(etAdd.getText().toString());
				}
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if(mode == MODE_COLOR) {
					colorAdapter.selectItem(arg2);
				} else {
					sizeAdapter.selectItem(arg2);
				}
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
		tvImageSizeText.setPadding(padding, 0, 0, padding);
		
		//tvName.
		rp = (RelativeLayout.LayoutParams) tvName.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvName, 30);
		tvName.setPadding(padding, 0, 0, padding);
		
		//etName.
		rp = (RelativeLayout.LayoutParams) etName.getLayoutParams();
		FontUtils.setFontAndHintSize(etName, 30, 24);
		etName.setMinHeight(editTextHeight);
		etName.setPadding(padding, 0, padding, 0);
		
		//tvPrice.
		rp = (RelativeLayout.LayoutParams) tvPrice.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvPrice, 30);
		tvPrice.setPadding(padding, 0, 0, padding);
		
		//etPrice.
		rp = (RelativeLayout.LayoutParams) etPrice.getLayoutParams();
		etPrice.setMinHeight(editTextHeight);
		FontUtils.setFontAndHintSize(etPrice, 30, 24);
		etPrice.setPadding(padding, 0, padding, 0);
		
		//tvCategory.
		rp = (RelativeLayout.LayoutParams) tvCategory.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvCategory, 30);
		tvCategory.setPadding(padding, 0, 0, padding);
		
		//btnCategory.
		rp = (RelativeLayout.LayoutParams) btnCategory.getLayoutParams();
		btnCategory.setMinHeight(editTextHeight);
		FontUtils.setFontSize(btnCategory, 30);
		
		//tvColor.
		rp = (RelativeLayout.LayoutParams) tvColor.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvColor, 30);
		tvColor.setPadding(padding, 0, 0, padding);
		
		//btnColor.
		rp = (RelativeLayout.LayoutParams) btnColor.getLayoutParams();
		btnColor.setMinHeight(editTextHeight);
		FontUtils.setFontSize(btnColor, 30);
		
		//tvSize.
		rp = (RelativeLayout.LayoutParams) tvSize.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvSize, 30);
		tvSize.setPadding(padding, 0, 0, padding);
		
		//btnSize.
		rp = (RelativeLayout.LayoutParams) btnSize.getLayoutParams();
		btnSize.setMinHeight(editTextHeight);
		FontUtils.setFontSize(btnSize, 30);
		
		//tvMixtureRate.
		rp = (RelativeLayout.LayoutParams) tvMixtureRate.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvMixtureRate, 30);
		tvMixtureRate.setPadding(padding, 0, 0, padding);
		
		//etMixtureRate.
		rp = (RelativeLayout.LayoutParams) etMixtureRate.getLayoutParams();
		etMixtureRate.setMinHeight(editTextHeight);
		FontUtils.setFontAndHintSize(etMixtureRate, 30, 24);
		etMixtureRate.setPadding(padding, 0, padding, 0);
		
		//tvDescription.
		rp = (RelativeLayout.LayoutParams) tvDescription.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvDescription, 30);
		tvDescription.setPadding(padding, 0, 0, padding);
		
		//etDescription.
		rp = (RelativeLayout.LayoutParams) etDescription.getLayoutParams();
		etDescription.setMinHeight(editTextHeight * 4);
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

		//btnDelete.
		rp = (RelativeLayout.LayoutParams) btnDelete.getLayoutParams();
		rp.height = buttonHeight;

		//tvNotification.
		rp = (RelativeLayout.LayoutParams) tvNotification.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvNotification, 30);
		tvNotification.setPadding(padding, 0, 0, padding);
		
		//btnNotification.
		rp = (RelativeLayout.LayoutParams) btnNotification.getLayoutParams();
		rp.height = buttonHeight;
		
		//etAdd.
		rp = (RelativeLayout.LayoutParams) etAdd.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(550);
		rp.height = ResizeUtils.getSpecificLength(92);
		
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

	public void delete() {
		
		if(product == null) {
			return;
		}
		
		String url = CphConstants.BASE_API_URL + "products/delete" +
				"?product_id=" + product.getId();
		
		DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForWritePage.delete.onError." + "\nurl : " + url);
				ToastUtils.showToast(R.string.failToDeleteProduct);
			}

			@Override
			public void onCompleted(String url, JSONObject objJSON) {

				try {
					LogUtils.log("WholesaleForWritePage.delete.onCompleted." + "\nurl : " + url
							+ "\nresult : " + objJSON);

					if(objJSON.getInt("result") == 1) {
						ToastUtils.showToast(R.string.complete_deleteProduct);
						mActivity.closePagesWithRefreshPreviousPage(2);
					} else {
						ToastUtils.showToast(objJSON.getString("message"));
					}
				} catch (Exception e) {
					ToastUtils.showToast(R.string.failToDeleteProduct);
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					ToastUtils.showToast(R.string.failToDeleteProduct);
					LogUtils.trace(oom);
				}
			}
		});
	}

	public void write() {

		try {
			String url = CphConstants.BASE_API_URL + "products/save" +
					"?product[category_id]=" + selectedCategoryIndex +
					"&product[wholesale_id]=" + mActivity.user.getWholesale_id() +
					"&product[name]=" + URLEncoder.encode(etName.getText().toString(), "utf-8") +
					"&product[price]=" + etPrice.getText().toString() +
					"&product[mixture_rate]=" + URLEncoder.encode(etMixtureRate.getText().toString(), "utf-8") + 
					"&product[desc]=" + URLEncoder.encode(etDescription.getText().toString(), "utf-8") +
					"&product[need_push]=" + (needPush? 1 : 0) +
					"&product[customers_only]=" + (isPublic? 0 : 1);
			
			//색상 추가.
			for(Item item : colorItems) {
				
				if(item.selected) {
					url += "&product[colors][]=" + item.text;
				}
			}
			
			//사이즈 추가.
			for(Item item : sizeItems) {
				
				if(item.selected) {
					url += "&product[sizes][]=" + item.text;
				}
			}
			
			//이미지 주소 추가.
			for(String imageUrl : selectedImageUrls) {
				
				if(imageUrl != null && imageUrl.contains("http")) {
					url += "&product[product_images][]=" + imageUrl;
				}
			}
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("WholesaleForWritePage.write.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToWriteProduct);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("WholesaleForWritePage.write.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 1) {
							ToastUtils.showToast(R.string.complete_writeProduct);
							mActivity.closePageWithRefreshPreviousPage();
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						ToastUtils.showToast(R.string.failToWriteProduct);
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						ToastUtils.showToast(R.string.failToWriteProduct);
						LogUtils.trace(oom);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void edit() {
		
		/*
		http://cph.minsangk.com/products/save
		?product[id]=148
		&product[category_id]=0
		&product[wholesale_id]=korea10
		&product[name]=%EB%A7%A4%EC%9A%
		&product[price]=7777777777
		&product[mixture_rate]=%EB%A7%A4%EA%B8%B4
		&product[desc]=%EB%A7%A4%EB0+%
		&product[need_push]=0
		&product[customers_only]=0
		&product[colors][]=블랙
		&product[colors][]=화이트
		&product[colors][]=아이보리
		&product[colors][]=그레이
		&product[sizes][]=280
		&product[sizes][]=290
		&product[product_images][]=http://cph.minsangk.com/images/20140907/7c8114390ef4f4bcde419a8e5b24ee5b.jpg

		http://cph.minsangk.com/products/save
		?product[category_id]=1
		&product[wholesale_id]=1
		&product[name]=%EB%A7%A5%EC%A3%BC%EC%
		&product[price]=20000
		&product[colors][]=%ED%9D%B0%EC%83%89
		&product[colors][]=%EA%B2%80%EC%A0%95
		&product[sizes][]=S
		&product[sizes][]=L
		&product[mixture_rate]=75%
		&product[desc]=%EC%84%20%EC%88%98%
		&product[need_push]=1
		&product[customers_only]=0
		&product[product_images][]=http://jayworks.co.kr/wp-content/uploads/2013/12/STRIKE_02.png
		*/
		try {
			String url = CphConstants.BASE_API_URL + "products/save" +
					"?product[id]=" + product.getId() +
					"&product[category_id]=" + selectedCategoryIndex +
					"&product[wholesale_id]=" + mActivity.user.getWholesale_id() +
					"&product[name]=" + URLEncoder.encode(etName.getText().toString(), "utf-8") +
					"&product[price]=" + etPrice.getText().toString() +
					"&product[mixture_rate]=" + URLEncoder.encode(etMixtureRate.getText().toString(), "utf-8") + 
					"&product[desc]=" + URLEncoder.encode(etDescription.getText().toString(), "utf-8") +
					"&product[need_push]=" + (needPush? 1 : 0) +
					"&product[customers_only]=" + (isPublic? 0 : 1);
			
			//색상 추가.
			for(Item item : colorItems) {
				
				if(item.selected) {
					url += "&product[colors][]=" + item.text;
				}
			}
			
			//사이즈 추가.
			for(Item item : sizeItems) {
				
				if(item.selected) {
					url += "&product[sizes][]=" + item.text;
				}
			}
			
			//이미지 주소 추가.
			for(String imageUrl : selectedImageUrls) {
				
				if(imageUrl != null && imageUrl.contains("http")) {
					url += "&product[product_images][]=" + imageUrl;
				}
			}
			
			DownloadUtils.downloadJSONString(url, new OnJSONDownloadListener() {

				@Override
				public void onError(String url) {

					LogUtils.log("WholesaleForWritePage.edit.onError." + "\nurl : " + url);
					ToastUtils.showToast(R.string.failToEditProduct);
				}

				@Override
				public void onCompleted(String url, JSONObject objJSON) {

					try {
						LogUtils.log("WholesaleForWritePage.edit.onCompleted." + "\nurl : " + url
								+ "\nresult : " + objJSON);

						if(objJSON.getInt("result") == 1) {
							Product newProduct = new Product(objJSON.getJSONObject("product"));
							ToastUtils.showToast(R.string.complete_editProduct);
							
							product.setCategory_id(newProduct.getCategory_id());
							product.setWholesale_id(newProduct.getWholesale_id());
							product.setName(newProduct.getName());
							product.setPrice(newProduct.getPrice());
							product.setMixture_rate(newProduct.getMixture_rate());
							product.setDesc(newProduct.getDesc());
							product.setNeed_push(newProduct.getNeed_push());
							product.setCustomers_only(newProduct.getCustomers_only());
							product.setProduct_images(newProduct.getProduct_images());
							product.setColors(newProduct.getColors());
							product.setSizes(newProduct.getSizes());
							
							mActivity.closeTopPage();
						} else {
							ToastUtils.showToast(objJSON.getString("message"));
						}
					} catch (Exception e) {
						ToastUtils.showToast(R.string.failToEditProduct);
						LogUtils.trace(e);
					} catch (OutOfMemoryError oom) {
						ToastUtils.showToast(R.string.failToEditProduct);
						LogUtils.trace(oom);
					}
				}
			});
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void uploadImage(final ImageView ivImage, final int index) {
		
		mActivity.showUploadPhotoPopup(new OnAfterUploadImage() {
			
			@Override
			public void onAfterUploadImage(String resultString, Bitmap thumbnail) {

				LogUtils.log("###\n2\n2\n2.onAfterUploadImage.  resultString : " + resultString);
				
				try {
					JSONObject objJSON = new JSONObject(resultString);

					if(objJSON.getInt("result") == 1) {
						ivImage.setImageDrawable(null);
						JSONObject objFile = objJSON.getJSONObject("file");
						selectedImageUrls[index] = objFile.getString("url");
						getWholesale().setRep_image_url(selectedImageUrls[index]);
					}
					
					if(thumbnail == null) {
						LogUtils.log("###WholesaleForProfileImagepage.onAfterUploadImage.  bitmap is null.");
					}
					
					if(thumbnail != null && !thumbnail.isRecycled() && ivImage != null) {
						ivImage.setImageBitmap(thumbnail);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (Error e) {
					LogUtils.trace(e);
				}
			}
		});
	}
	
	public void choiceMode(final int index) {

		DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(which == 0) {
					uploadImage(ivImages[index], index);
				} else {
					ivImages[index].setImageDrawable(null);
					selectedImageUrls[index] = null;
				}
			}
		};
		
		mActivity.showSelectDialog(null, new String[]{
				"사진 등록",
				"사진 삭제"
		}, ocl);
	}
	
	public void showPopup() {
		
		relativePopup.setVisibility(View.VISIBLE);
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

			TextView textView = null;
			View checkBox = null;
			
			if(convertView == null) {
				LinearLayout linear = new LinearLayout(mContext);
				linear.setOrientation(LinearLayout.HORIZONTAL);
				linear.setLayoutParams(new AbsListView.LayoutParams(
						LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(92)));
				
				textView = new TextView(mContext);
				textView.setGravity(Gravity.CENTER);
				textView.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1));
				textView.setTextColor(Color.WHITE);
				FontUtils.setFontSize(textView, 32);
				linear.addView(textView);
				
				checkBox = new View(mContext);
				ResizeUtils.viewResize(52, 34, checkBox, 1, Gravity.CENTER_VERTICAL, new int[]{20, 0, 20, 0});
				checkBox.setBackgroundResource(R.drawable.check);
				linear.addView(checkBox);
				
				ViewHolder holder = new ViewHolder();
				holder.textView = textView;
				holder.checkBox = checkBox;
				
				linear.setTag(holder);
				
				convertView = linear;
			} else {
				ViewHolder holder = (ViewHolder)convertView.getTag();
				textView = holder.textView;
				checkBox = holder.checkBox;
			}

			textView.setText(items.get(position).text);
			
			if(items.get(position).selected) {
				checkBox.setVisibility(View.VISIBLE);
			} else{
				checkBox.setVisibility(View.INVISIBLE);
			}
			
			return convertView;
		}
	
		public void addItem(String string) {
			
			Item item = new Item(string);
			item.selected = true;
			items.add(item);
			notifyDataSetChanged();
            listView.setSelection(items.size() - 1);

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
	
	public class ViewHolder {
		
		public TextView textView;
		public View checkBox;
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

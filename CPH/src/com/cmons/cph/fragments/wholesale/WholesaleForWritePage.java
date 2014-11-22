package com.cmons.cph.fragments.wholesale;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmons.cph.R;
import com.cmons.cph.ShopActivity.OnAfterSelectCategoryListener;
import com.cmons.cph.classes.CmonsFragmentActivity;
import com.cmons.cph.classes.CmonsFragmentForWholesale;
import com.cmons.cph.classes.CphConstants;
import com.cmons.cph.models.Category;
import com.cmons.cph.models.Product;
import com.cmons.cph.views.TitleBar;
import com.outspoken_kid.activities.MultiSelectGalleryActivity.OnAfterPickImageListener;
import com.outspoken_kid.classes.ViewUnbindHelper;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.DownloadUtils.OnBitmapDownloadListener;
import com.outspoken_kid.utils.DownloadUtils.OnJSONDownloadListener;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.ImageUploadUtils;
import com.outspoken_kid.utils.ImageUploadUtils.OnAfterUploadImage;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.SharedPrefsUtils;
import com.outspoken_kid.utils.SoftKeyboardUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class WholesaleForWritePage extends CmonsFragmentForWholesale {

	private static final int MODE_CATEGORY = 0;
	private static final int MODE_COLOR = 1;
	private static final int MODE_SIZE = 2;
	private static final int MODE_MIXTURE = 3;
	
    private TextView tvImageText;
    private Button btnUploadImage;
    private TextView tvUploadImage;
    private HorizontalScrollView imageScroll;
    private LinearLayout imageLinear;
    private TextView tvImageSizeText;
    private TextView tvName;
    private EditText etName;
    private TextView tvPrice;
    private EditText etPrice;
    private TextView tvCategory;
    private Button btnCategory;
    private Button btnSave;
    private Button btnClear;
    private TextView tvColor;
    private Button btnColor;
    private TextView tvSize;
    private Button btnSize;
    private TextView tvMixtureRate;
    private Button btnMixtureRate;
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
    private ChoiceAdapter mixtureAdapter;

    private Product product;
    private boolean isPublic;
	private boolean needPush;
	private int selectedCategoryIndex;
	
	private ArrayList<Item> colorItems;
	private ArrayList<Item> sizeItems;
	private ArrayList<Item> mixtureItems;
	private int mode;

	private ArrayList<String> selectedImages = new ArrayList<String>();
	private ArrayList<FrameLayout> selectedFrames = new ArrayList<FrameLayout>();
	private int downloadCount;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		SharedPrefsUtils.removeVariableFromPrefs(CphConstants.PREFS_IMAGE_UPLOAD, "uploading");
		SharedPrefsUtils.removeVariableFromPrefs(CphConstants.PREFS_IMAGE_UPLOAD, "index");
		
		CmonsFragmentActivity.onAfterPickImageListener = new OnAfterPickImageListener() {

			@Override
			public void onAfterPickImage(String[] sdCardPaths,
					Bitmap[] thumbnails) {

				if(sdCardPaths == null) {
					LogUtils.log("###WholesaleForWritePage.onAfterPickImage.  sdCardPaths is null.");
				} else {
					
					LogUtils.log("###WholesaleForWritePage.onAfterPickImage.  sdCardPaths");
					
					int size = sdCardPaths.length;
					for(int i=0; i<size; i++) {
						LogUtils.log("path[" + i + "] : " + sdCardPaths[i]);
						
						if(thumbnails[i] == null) {
							LogUtils.log("thumbnail[" + i + "] : null");
						} else {
							LogUtils.log("thumbnail[" + i + "] : not null");
						}
						
						addThumbnailView(selectedImages.size(), sdCardPaths[i], thumbnails[i]);
						selectedImages.add(sdCardPaths[i]);
					}
				}
			}
		};
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		titleBar.getBackButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				getActivity().getSupportFragmentManager().popBackStack();
				clear();
			}
		});
	}
	
	@Override
	public void bindViews() {

		titleBar = (TitleBar) mThisView.findViewById(R.id.wholesaleWritePage_titleBar);
		ivBg = (ImageView) mThisView.findViewById(R.id.wholesaleWritePage_ivBg);
		
		tvImageText = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvImageText);
		btnUploadImage = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnUpload);
		
		tvUploadImage = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvUploadImage);
		imageScroll = (HorizontalScrollView) mThisView.findViewById(R.id.wholesaleWritePage_imageScroll);
		imageLinear = (LinearLayout) mThisView.findViewById(R.id.wholesaleWritePage_imageLinear);
		
		tvImageSizeText = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvImageSizeText);
		tvName = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvName);
		etName = (EditText) mThisView.findViewById(R.id.wholesaleWritePage_etName);
		tvPrice = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvPrice);
		etPrice = (EditText) mThisView.findViewById(R.id.wholesaleWritePage_etPrice);
		btnSave = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnSave);
		btnClear = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnClear);
		tvCategory = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvCategory);
		btnCategory = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnCategory);
		tvColor = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvColor);
		btnColor = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnColor);
		tvSize = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvSize);
		btnSize = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnSize);
		tvMixtureRate = (TextView) mThisView.findViewById(R.id.wholesaleWritePage_tvMixtureRate);
		btnMixtureRate = (Button) mThisView.findViewById(R.id.wholesaleWritePage_btnMixtureRate);
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
			LogUtils.log("###WholesaleForWritepage.setVariables.  has product.");
		} else {
			LogUtils.log("###WholesaleForWritepage.setVariables.  no product.");
		}
		
		colorItems = new ArrayList<Item>();
//		loadBestHitItems(MODE_COLOR);
		loadBasicItems(MODE_COLOR);
		colorAdapter = new ChoiceAdapter(colorItems);
		
		sizeItems = new ArrayList<Item>();
//		loadBestHitItems(MODE_SIZE);
		loadBasicItems(MODE_SIZE);
		sizeAdapter = new ChoiceAdapter(sizeItems);
		
		mixtureItems = new ArrayList<Item>();
		loadBestHitItems(MODE_MIXTURE);
		
		mixtureAdapter = new ChoiceAdapter(mixtureItems);
		
		if(product != null) {
			title = "상품 수정";

			//사진 설정.
			selectedImages.clear();
			if(product.getProduct_images() != null
					&& product.getProduct_images().length > 0) {
				
				for(String imageUrl : product.getProduct_images()) {
					selectedImages.add(imageUrl);
				}
			}
			
			//isPublic 설정.
			isPublic = product.getCustomers_only() == 0;
			
			//needPush 설정.
			needPush = product.getNeed_push() == 1;
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
		
		imageScroll.requestDisallowInterceptTouchEvent(true);
		
		//수정.
		if(product != null) {
			
			if(isPublic) {
				btnPublic1.setBackgroundResource(R.drawable.myshop_open1_a);
				btnPublic2.setBackgroundResource(R.drawable.myshop_open2_b);
			} else {
				btnPublic1.setBackgroundResource(R.drawable.myshop_open1_b);
				btnPublic2.setBackgroundResource(R.drawable.myshop_open2_a);
			}
			
			tvNotification.setVisibility(View.GONE);
			btnNotification.setVisibility(View.GONE);
			btnDelete.setVisibility(View.VISIBLE);
			
			//이미지 설정.
			if(product.getProduct_images() != null) {
				
				for(int i=0; i<product.getProduct_images().length; i++) {
					addThumbnailViewWithUrl(i, product.getProduct_images()[i]);
				}
			}
			
			//상품명 설정.
			etName.setText(product.getName());
			
			//상품 가격 설정.
			etPrice.setText("" + product.getPrice());
			
			//상품 분류 설정.
			btnCategory.setText(getCategoryStringUsingIndex(product.getCategory_id()));
			
			//상품 사이즈 설정.
			loadAddedItems(MODE_SIZE);
			btnSize.setText(product.getSizes().replace("|", "/"));
			
			//상품 색상 설정.
			loadAddedItems(MODE_COLOR);
			btnColor.setText(product.getColors().replace("|", "/"));
			
			//상품 혼용률 설정.
			loadAddedItems(MODE_MIXTURE);
			selectedCategoryIndex = product.getCategory_id();
			btnMixtureRate.setText(product.getMixture_rate());
			
			//상품 설명 설정.
			etDescription.setText(product.getDesc());
			
		//등록.
		} else{
			tvNotification.setVisibility(View.VISIBLE);
			btnNotification.setVisibility(View.VISIBLE);
			btnDelete.setVisibility(View.INVISIBLE);
			
			//저장된 카테고리 설정.
			loadSavedItems(MODE_CATEGORY);
			
			//저장된 사이즈 설정.
			loadSavedItems(MODE_SIZE);
			
			//저장된 색상 설정.
			loadSavedItems(MODE_COLOR);
			
			//저장된 혼용률 설정.
			loadSavedItems(MODE_MIXTURE);
		}
	}

	@Override
	public void setListeners() {

		titleBar.getBtnSubmit().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

				if(relativePopup.getVisibility() == View.VISIBLE) {

					String printString = null;
					ArrayList<Item> targetItems = null;
					Button targetButton = null;
					int hintResId = 0;
					
					switch(mode) {
					
					case MODE_COLOR:
						targetItems = colorItems;
						targetButton = btnColor;
						hintResId = R.string.hintForProductColor;
						break;
						
					case MODE_SIZE:
						targetItems = sizeItems;
						targetButton = btnSize;
						hintResId = R.string.hintForProductSize;
						break;
						
					case MODE_MIXTURE:
						targetItems = mixtureItems;
						targetButton = btnMixtureRate;
						hintResId = R.string.hintForProductMixtureRate;
						break;
					}
					
					ArrayList<Item> uniqueSelectedItems = getUniqueSelectedItemList(targetItems);
					for(Item item : uniqueSelectedItems) {
						
						if(printString == null) {
							printString = "";
						} else {
							printString += " / ";
						}
						
						printString += item.text;
					}
					
					if(printString != null) {
						targetButton.setText(printString);
					} else {
						targetButton.setText(hintResId);
					}
					
					hidePopup();
					return;
				}
				
				if(selectedImages.size() == 0) {
					//이미지를 등록해주세요.
					ToastUtils.showToast(R.string.wrongProductImage);
					return;
					
				} else if(StringUtils.isEmpty(etName)){
					//상품명을 입력해주세요.
					ToastUtils.showToast(R.string.wrongProductName);
					return;
					
				} else if(StringUtils.isEmpty(etPrice)) {
					//상품가격을 입력해주세요.
					ToastUtils.showToast(R.string.wrongProductPrice);
					return;
					
				} else if(selectedCategoryIndex == 0) {
					//상품분류를 선택해주세요.
					ToastUtils.showToast(R.string.wrongProductCategory);
					return;
				}
				
				//색상.
				int selectedCount = 0;
				int size = colorItems.size();
				for(int i=0; i<size; i++) {
					if(colorItems.get(i).selected) {
						selectedCount++;
					}
				}
				
				if(selectedCount == 0) {
					ToastUtils.showToast(R.string.wrongProductColor);
					return;
				}
				
				//사이즈.
				selectedCount = 0;
				size = sizeItems.size();
				for(int i=0; i<size; i++) {
					if(sizeItems.get(i).selected) {
						selectedCount++;
					}
				}
				
				if(selectedCount == 0) {
					ToastUtils.showToast(R.string.wrongProductSize);
					return;
				}
				
				//혼용률.
				selectedCount = 0;
				size = mixtureItems.size();
				for(int i=0; i<size; i++) {
					if(mixtureItems.get(i).selected) {
						selectedCount++;
					}
				}
				
				if(selectedCount == 0) {
					ToastUtils.showToast(R.string.wrongProductMixtureRate);
					return;
				}
				
				//상품 설명.
				if(StringUtils.isEmpty(etDescription)) {
					ToastUtils.showToast(R.string.wrongProductDesc);
					return;
				}
				
				uploadImages(product != null);
			}
		});
		
		btnUploadImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				if(selectedImages.size() >= 10) {
					ToastUtils.showToast(R.string.imageSelectLimit);
					
					return;
				}
				
				mActivity.showUploadPhotoPopup(10 - selectedImages.size());
			}
		});

		btnSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				saveSettings();
				mActivity.showAlertDialog(
						R.string.saveSettings, 
						R.string.complete_saveSettings, 
						R.string.confirm, null);
			}
		});
		
		btnClear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				clearSettings();
				mActivity.showAlertDialog(
						R.string.clearSettings, 
						R.string.complete_clearSettings, 
						R.string.confirm, null);
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
		
		btnMixtureRate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				mode = MODE_MIXTURE;
				listView.setAdapter(mixtureAdapter);
				mixtureAdapter.notifyDataSetChanged();
				showPopup();
				ToastUtils.showToast(R.string.wrongProductMixtureRate);
				SoftKeyboardUtils.showKeyboard(mContext, btnMixtureRate);
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

					mActivity.showAlertDialog("상품 삭제", "상품을 삭제하시겠습니까?", 
							"삭제", "취소", 
							new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {

							delete();
						}
					}, null);
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
				
				String item = etAdd.getText().toString();
				
				if(mode == MODE_COLOR) {
					colorAdapter.addItem(item);
					
				} else if(mode == MODE_SIZE){
					sizeAdapter.addItem(item);
					
				} else {
					mixtureAdapter.addItem(item);
					saveItemToPrefs(MODE_MIXTURE, item);
				}
				
				etAdd.setText(null);
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				switch(mode) {
				
				case MODE_COLOR:
					
					if(!colorItems.get(arg2).unusable) {
						colorAdapter.selectItem(arg2);
					}
					break;
					
				case MODE_SIZE:

					if (!sizeItems.get(arg2).unusable) {
						sizeAdapter.selectItem(arg2);
					}
					break;

				case MODE_MIXTURE:

					if (!mixtureItems.get(arg2).unusable) {
						mixtureAdapter.selectItem(arg2);
					}
					break;
				}
			}
		});
	}

	@Override
	public void setSizes() {

		RelativeLayout.LayoutParams rp = null;

		int padding = ResizeUtils.getSpecificLength(16);
		int imageHeight = ResizeUtils.getSpecificLength(375);
		int titleTextHeight = ResizeUtils.getSpecificLength(100);
		int editTextHeight = ResizeUtils.getSpecificLength(92);
		int buttonHeight = ResizeUtils.getSpecificLength(180);
		
		//tvImageText.
		rp = (RelativeLayout.LayoutParams) tvImageText.getLayoutParams();
		rp.height = titleTextHeight;
		FontUtils.setFontSize(tvImageText, 30);
		tvImageText.setPadding(padding, 0, 0, padding);

		//btnUploadImage.
		rp = (RelativeLayout.LayoutParams) btnUploadImage.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(200);
		rp.height = ResizeUtils.getSpecificLength(60);
		rp.bottomMargin = ResizeUtils.getSpecificLength(10);
		rp.rightMargin = ResizeUtils.getSpecificLength(30);
		
		//tvUploadImage.
		rp = (RelativeLayout.LayoutParams) tvUploadImage.getLayoutParams();
		rp.height = imageHeight;
		FontUtils.setFontSize(tvUploadImage, 30);

		//ImageScroll.
		rp = (RelativeLayout.LayoutParams) imageScroll.getLayoutParams();
		rp.height = imageHeight;
		
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
		
		//bg.
		rp = (RelativeLayout.LayoutParams) (mThisView.findViewById(
				R.id.wholesaleWritePage_bg)).getLayoutParams();
		rp.height = ResizeUtils.getSpecificLength(890);
		rp.topMargin = titleTextHeight;
		
		//btnSave.
		rp = (RelativeLayout.LayoutParams) btnSave.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(140);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		rp.rightMargin = ResizeUtils.getSpecificLength(20);
		
		//btnClear.
		rp = (RelativeLayout.LayoutParams) btnClear.getLayoutParams();
		rp.width = ResizeUtils.getSpecificLength(140);
		rp.height = ResizeUtils.getSpecificLength(40);
		rp.topMargin = ResizeUtils.getSpecificLength(30);
		
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
		rp = (RelativeLayout.LayoutParams) btnMixtureRate.getLayoutParams();
		btnMixtureRate.setMinHeight(editTextHeight);
		FontUtils.setFontSize(btnMixtureRate, 30);
		
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

		clear();
		return false;
	}

	@Override
	public boolean parseJSON(JSONObject objJSON) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public int getBgResourceId() {

		return R.drawable.myshop_bg;
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
						clear();
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
//					"&product[mixture_rate]=" + URLEncoder.encode(btnMixtureRate.getText().toString(), "utf-8") + 
					"&product[desc]=" + URLEncoder.encode(etDescription.getText().toString(), "utf-8") +
					"&product[need_push]=" + (needPush? 1 : 0) +
					"&product[customers_only]=" + (isPublic? 0 : 1);

			ArrayList<Item> uniqueSelectedItemList = null;
			
			//색상 추가.
			uniqueSelectedItemList = getUniqueSelectedItemList(colorItems);
			for(Item item : uniqueSelectedItemList) {
				url += "&product[colors][]=" + item.text;
//				saveItemToPrefs(MODE_COLOR, item.text);
			}
			
			//사이즈 추가.
			uniqueSelectedItemList = getUniqueSelectedItemList(sizeItems);
			for(Item item : uniqueSelectedItemList) {
				url += "&product[sizes][]=" + item.text;
//				saveItemToPrefs(MODE_SIZE, item.text);
			}
			
			//혼용률 추가.
			String mixtureString = null;
			uniqueSelectedItemList = getUniqueSelectedItemList(mixtureItems);
			for(Item item : uniqueSelectedItemList) {
				
				if(item.text != null) {

					if(mixtureString != null) {
						mixtureString += " / " + item.text;
					} else {
						mixtureString = item.text;
					}
				}
//				saveItemToPrefs(MODE_MIXTURE, item.text);
			}
			
			url += "&product[mixture_rate]=" + URLEncoder.encode(mixtureString, "utf-8");
			
			//이미지 주소 추가.
			for(String imageUrl : selectedImages) {
				
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
							clear();
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
//					"&product[mixture_rate]=" + URLEncoder.encode(etMixtureRate.getText().toString(), "utf-8") + 
					"&product[desc]=" + URLEncoder.encode(etDescription.getText().toString(), "utf-8") +
					"&product[need_push]=" + (needPush? 1 : 0) +
					"&product[customers_only]=" + (isPublic? 0 : 1);
			
			ArrayList<Item> uniqueSelectedItemList = null;
			
			//색상 추가.
			uniqueSelectedItemList = getUniqueSelectedItemList(colorItems);
			for(Item item : uniqueSelectedItemList) {
				url += "&product[colors][]=" + item.text;
//				saveItemToPrefs(MODE_COLOR, item.text);
			}
			
			//사이즈 추가.
			uniqueSelectedItemList = getUniqueSelectedItemList(sizeItems);
			for(Item item : uniqueSelectedItemList) {
				url += "&product[sizes][]=" + item.text;
//				saveItemToPrefs(MODE_SIZE, item.text);
			}
			
			//혼용률 추가.
			String mixtureString = null;
			uniqueSelectedItemList = getUniqueSelectedItemList(mixtureItems);
			for(Item item : uniqueSelectedItemList) {

				if(item.text != null) {
					
					if(mixtureString != null) {
						mixtureString += " / " + item.text;
					} else {
						mixtureString = item.text;
					}
				}
//				saveItemToPrefs(MODE_MIXTURE, item.text);
			}
			
			url += "&product[mixture_rate]=" + URLEncoder.encode(mixtureString, "utf-8");
			
			url += "&product[mixture_rate]=" + URLEncoder.encode(mixtureString, "utf-8");
			
			//이미지 주소 추가.
			for(String imageUrl : selectedImages) {
				
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
							
							mActivity.closePageWithRefreshPreviousPage();
							clear();
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
	
	public void uploadImages(final boolean isEdit) {
		
		ToastUtils.showToast(R.string.uploadingImage);
		
		//초기화.
		downloadCount = 0;
		
		int size = selectedImages.size();
		for(int i=0; i<size; i++) {
			
			if(!selectedImages.get(i).contains("http")) {
				downloadCount++;
			}
		}
		
		for(int i=0; i<size; i++) {
			
			if(!selectedImages.get(i).contains("http")) {
				
				final int INDEX = i;
				OnAfterUploadImage oaui = new OnAfterUploadImage() {
					
					@Override
					public void onAfterUploadImage(String resultString) {
						
						try {
							//sdCardPath를 imageUrl로 교체.
							String imageUrl = new JSONObject(resultString).getJSONObject("file").getString("url");
							selectedImages.remove(INDEX);
							selectedImages.add(INDEX, imageUrl);
						} catch (Exception e) {
							LogUtils.trace(e);
						} catch (Error e) {
							LogUtils.trace(e);
						}
						
						downloadCount--;
						check(isEdit);
					}
				};
				
				ImageUploadUtils.uploadImage(CphConstants.UPLOAD_URL, oaui, selectedImages.get(i));
			}
		}
		
		check(isEdit);
	}
	
	public void check(boolean isEdit) {
		
		if(downloadCount == 0) {
			
			LogUtils.log("WholesaleForWritePage.check.  url 변환 완료.");
			
			for(String url : selectedImages) {
				LogUtils.log(url);
			}
			
			if(isEdit) {
				edit();
			} else {
				write();
			}
		}
	}
	
	public void choiceMode(final int index) {

		DialogInterface.OnClickListener ocl = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {

				if(index == 0) {
					deleteImage(0);
				} else {
					
					if(which == 0) {
						setImageToMain(index);
					} else {
						deleteImage(index);
					}
				}
			}
		};
		
		if(index == 0) {
			mActivity.showSelectDialog(null, new String[]{
					"이미지 삭제"
			}, ocl);
		} else {
			mActivity.showSelectDialog(null, new String[]{
					"대표 이미지로 사용",
					"이미지 삭제"
			}, ocl);
		}
	}
	
	public void setImageToMain(int index) {
		
		String url = selectedImages.get(index);
		FrameLayout frame = selectedFrames.get(index);
		
		selectedImages.remove(index);
		selectedFrames.remove(index);
		
		imageLinear.removeViewAt(index);
		imageLinear.addView(frame, 0);
		imageScroll.smoothScrollTo(0, 0);

		selectedImages.add(0, url);
		selectedFrames.add(0, frame);
		
		frame.getChildAt(1).setVisibility(View.VISIBLE);
		selectedFrames.get(1).getChildAt(1).setVisibility(View.INVISIBLE);
		
		refreshImageFrameIndex();
	}
	
	public void deleteImage(int index) {

		FrameLayout frame = selectedFrames.get(index);
		
		selectedImages.remove(index);
		selectedFrames.remove(index);
		imageLinear.removeViewAt(index);
		
		ViewUnbindHelper.unbindReferences(frame);
		
		if(selectedFrames.size() > 0) {
			selectedFrames.get(0).getChildAt(1).setVisibility(View.VISIBLE);
		}
		
		refreshImageFrameIndex();
		
		if(selectedFrames.size() == 0) {
			tvUploadImage.setVisibility(View.VISIBLE);
		} else {
			tvUploadImage.setVisibility(View.INVISIBLE);
		}
	}
	
	public void refreshImageFrameIndex() {
		
		int size = selectedFrames.size();
		for(int i=0; i<size; i++) {
			selectedFrames.get(i).getChildAt(1).setTag("" + i);
		}
	}
	
	public void showPopup() {
		
		relativePopup.setVisibility(View.VISIBLE);
	}
	
	public void hidePopup() {
		
		relativePopup.setVisibility(View.INVISIBLE);
	}
	
	public void clear() {
		
		SharedPrefsUtils.removeVariableFromPrefs(CphConstants.PREFS_IMAGE_UPLOAD, "index");
//		selectedImageUrls = new String[3];
		SharedPrefsUtils.removeVariableFromPrefs(CphConstants.PREFS_IMAGE_UPLOAD, "uploading");
		SoftKeyboardUtils.hideKeyboard(mContext, etDescription);
	}
	
	@SuppressWarnings("unchecked")
	public void loadBestHitItems(int mode) {
//
		ArrayList<Item> targetItems = null;
//		int targetTitleResId = 0;
//
//		switch(mode) {
//
//		case MODE_COLOR:
//			targetItems = colorItems;
//			targetTitleResId = R.string.bestHitColor;
//			break;
//			
//		case MODE_SIZE:
//			targetItems = sizeItems;
//			targetTitleResId = R.string.bestHitSize;
//			break;
//			
//		case MODE_MIXTURE:
			targetItems = mixtureItems;
//			targetTitleResId = R.string.bestHitMixture;
//			break;
//		}
		
		SharedPreferences prefs = mContext.getSharedPreferences(
				CphConstants.PREFS_PRODUCT_UPLOAD + mode, Context.MODE_PRIVATE);
		
		Map<String, Integer> map = (Map<String, Integer>)prefs.getAll();

		if(map.size() != 0) {
			String printString = "================ mode : " + mode;

			for(String key : map.keySet()) {
				
				if(map.get(key) != null) {
					printString += "\n" + key + " : " + map.get(key);
					targetItems.add(new Item(key, map.get(key)));
				}
			}
			
			LogUtils.log(printString);
			
			Comparator<Item> compare = new Comparator<Item>() {
				
				@Override
				public int compare(Item lhs, Item rhs) {
					
					//lhs > rhs로 하면 오름차순, 반대로 하면 내림차순.
					return (lhs.count < rhs.count? 1 : -1);
				}
			};
			
			Collections.sort(targetItems, compare);

			while(targetItems.size() > 10) {
				targetItems.remove(targetItems.size() - 1);
			}
			
//			Item titleItem = new Item(getString(targetTitleResId));
//			titleItem.unusable = true;
//			targetItems.add(0, titleItem);
			
//			Item lineItem = new Item("*******");
//			lineItem.unusable = true;
//			targetItems.add(lineItem);
		}
	}
	
	public void loadBasicItems(int mode) {
		
		switch(mode) {
		
		case MODE_COLOR:
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
			break;
			
		case MODE_SIZE:
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
			break;
			
		case MODE_MIXTURE:
			break;
		}
	}
	
	public void loadAddedItems(int mode) {
	
		int size = 0;
		boolean duplicated = false;
		String[] targetStrings = null;
		ArrayList<Item> targetItems = null;
		
		switch(mode) {
		
		case MODE_COLOR:
			targetStrings = product.getColors().split("\\|");
			targetItems = colorItems;
			break;
			
		case MODE_SIZE:
			targetStrings = product.getSizes().split("\\|");
			targetItems = sizeItems;
			break;
			
		case MODE_MIXTURE:
			targetStrings = product.getMixture_rate().split(" / ");
			targetItems = mixtureItems;
			break;
		}
		
		size = targetStrings.length;
		for(int i=0; i<size; i++) {
			
			if(targetStrings[i] != null) {
				duplicated = false;
				
				for(Item item : targetItems) {
					if(targetStrings[i].equals(item.text)) {
						duplicated = true;
						item.selected = true;
					}
				}
				
				if(!duplicated) {
					Item item = new Item(targetStrings[i]);
					item.selected = true;
					targetItems.add(item);
				}
			}
		}
	}

	public void loadSavedItems(int mode) {
		
		int size = 0;
		boolean duplicated = false;
		String[] targetStrings = null;
		ArrayList<Item> targetItems = null;
		String prefsName = CphConstants.PREFS_PRODUCT_UPLOAD;
		Button targetButton = null;
		String key = null;
		String splitword = null;
		
		switch(mode) {
		
		case MODE_CATEGORY:
			
			if(SharedPrefsUtils.checkPrefs(prefsName, "categoryIndex")) {
				
				int categoryIndex = SharedPrefsUtils.getIntegerFromPrefs(prefsName, "categoryIndex");
				String category = SharedPrefsUtils.getStringFromPrefs(prefsName, "category");
				
				LogUtils.log("###WholesaleForWritePage.loadSavedItems.  " +
						"\ncategoryIndex : " + categoryIndex +
						"\ncategory : " + category);
				
				selectedCategoryIndex = categoryIndex;
				btnCategory.setText(category);
			}
			
			return;
		
		case MODE_COLOR:
			key = "colorString";
			splitword = "\\|";
			targetItems = colorItems;
			targetButton = btnColor;
			break;
			
		case MODE_SIZE:
			key = "sizeString";
			splitword = "\\|";
			targetItems = sizeItems;
			targetButton = btnSize;
			break;
			
		case MODE_MIXTURE:
			key = "mixtureString";
			splitword = " / ";
			targetItems = mixtureItems;
			targetButton = btnMixtureRate;
			break;
		}
		
		if(!SharedPrefsUtils.checkPrefs(prefsName, key)) {
			return;
		} else {
			targetStrings = SharedPrefsUtils.getStringFromPrefs(prefsName, key).split(splitword);
		}
		
		LogUtils.log("###WholesaleForWritePage.loadSavedItems.  " +
				"\targetStrings : " + targetStrings);

		String settingString = null;
		size = targetStrings.length;
		for(int i=0; i<size; i++) {
			
			if(targetStrings[i] != null) {
				duplicated = false;
				
				for(Item item : targetItems) {
					if(targetStrings[i].equals(item.text)) {
						duplicated = true;
						item.selected = true;
					}
				}
				
				if(!duplicated) {
					Item item = new Item(targetStrings[i]);
					item.selected = true;
					targetItems.add(item);
				}
				
				if(settingString == null) {
					settingString = targetStrings[i];
				} else {
					settingString += " / " + targetStrings[i];
				}
			}
		}
		
		if(settingString != null) {
			targetButton.setText(settingString);
		}
	}
	
	public void saveItemToPrefs(int mode, String item) {
		
		try {
			SharedPreferences prefs = mContext.getSharedPreferences(
					CphConstants.PREFS_PRODUCT_UPLOAD + mode, Context.MODE_PRIVATE);
			
			int count = prefs.getInt(item, 0);
			Editor ed = prefs.edit();
			
			if(prefs.contains(item)) {
				ed.remove(item);
			}
			
			ed.putInt(item, count + 1);
			ed.commit();
		} catch (Exception e) {
			LogUtils.trace(e);
		}
	}

	public ArrayList<Item> getUniqueSelectedItemList(ArrayList<Item> items) {

		ArrayList<Item> uniqueItemList = new ArrayList<Item>();
		boolean duplicated = false;
		
		for(Item item : items) {
			
			if(!item.selected) {
				continue;
			}
			
			try {
				duplicated = false;
				
				for(Item checkingItem : uniqueItemList) {
					
					if(!checkingItem.selected) {
						continue;
					}
					
					if(item.text.equals(checkingItem.text)) {
						duplicated = true;
					}
				}
				
				if(!duplicated) {
					uniqueItemList.add(item);
				}
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
		
		return uniqueItemList;
	}

	public void saveSettings() {
		
		ArrayList<Item> uniqueColors = getUniqueSelectedItemList(colorItems);
		ArrayList<Item> uniqueSize = getUniqueSelectedItemList(sizeItems);
		ArrayList<Item> uniqueMixtures = getUniqueSelectedItemList(mixtureItems);
		
		LogUtils.log("#################################");
		
		LogUtils.log("#####CategoryIndex.");
		LogUtils.log(selectedCategoryIndex + ", " + getCategoryStringUsingIndex(selectedCategoryIndex));
		
		LogUtils.log("#####UniqueColors.");
		for(Item item : uniqueColors) {
			LogUtils.log(item.text);
		}
		
		LogUtils.log("#####UniqueSizes.");
		for(Item item : uniqueSize) {
			LogUtils.log(item.text);
		}
		
		LogUtils.log("#####UniqueMixtures.");
		for(Item item : uniqueMixtures) {
			LogUtils.log(item.text);
		}
		
		LogUtils.log("#################################");
		
		try {
			String prefsName = CphConstants.PREFS_PRODUCT_UPLOAD;
			SharedPreferences prefs = mContext.getSharedPreferences(
					prefsName, Context.MODE_PRIVATE);
			Editor ed = prefs.edit();
			
			//카테고리.
			if(selectedCategoryIndex != 0) {
				ed.putInt("categoryIndex", selectedCategoryIndex);
				ed.putString("category", getCategoryStringUsingIndex(selectedCategoryIndex));
			} else {
				ed.remove("categoryIndex");
				ed.remove("category");
			}
			
			//색상.
			if(uniqueColors.size() > 0) {
				String colorString = null;
				
				for(Item item : uniqueColors) {
					
					if(colorString == null) {
						colorString = "";
					} else {
						colorString += "|";
					}
					
					colorString += item.text;
				}
				
				ed.putString("colorString", colorString);
			} else {
				ed.remove("colorString");
			}
			
			//사이즈.
			if(uniqueSize.size() > 0) {
				String sizeString = null;
				
				for(Item item : uniqueSize) {
					
					if(sizeString == null) {
						sizeString = "";
					} else {
						sizeString += "|";
					}
					
					sizeString += item.text;
				}
				
				ed.putString("sizeString", sizeString);
			} else {
				ed.remove("sizeString");
			}
			
			//혼용률.
			if(uniqueMixtures.size() > 0) {
				String mixtureString = null;
				
				for(Item item : uniqueMixtures) {
					
					if(item.text != null) {
						
						if(mixtureString == null) {
							mixtureString = "";
						} else {
							mixtureString += " / ";
						}
						
						mixtureString += item.text;
					}
				}

				if(mixtureString != null) {
					ed.putString("mixtureString", mixtureString);
				}
			} else {
				ed.remove("mixtureString");
			}
			
			ed.commit();
			
			LogUtils.log("#################################");
			
			LogUtils.log("#####CategoryIndex.");
			LogUtils.log(SharedPrefsUtils.getIntegerFromPrefs(prefsName, "categoryIndex") + 
					", " + SharedPrefsUtils.getStringFromPrefs(prefsName, "category"));
			
			LogUtils.log("#####UniqueColors.");
			LogUtils.log("colorString : " + 
					SharedPrefsUtils.getStringFromPrefs(prefsName, "colorString"));
			
			LogUtils.log("#####UniqueSizes.");
			LogUtils.log("sizeString : " + 
					SharedPrefsUtils.getStringFromPrefs(prefsName, "sizeString"));
			
			LogUtils.log("#####UniqueMixtures.");
			LogUtils.log("mixtureString : " + 
					SharedPrefsUtils.getStringFromPrefs(prefsName, "mixtureString"));
			
			LogUtils.log("#################################");
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}
	
	public void clearSettings() {
		
		try {
			SharedPreferences prefs = mContext.getSharedPreferences(
					CphConstants.PREFS_PRODUCT_UPLOAD, Context.MODE_PRIVATE);
			Editor ed = prefs.edit();

			ed.remove("category");
			ed.remove("categoryIndex");
			ed.remove("colorString");
			ed.remove("sizeString");
			ed.remove("mixtureString");
			
			ed.commit();
			
			selectedCategoryIndex = 0;
			
			colorItems.clear();
			loadBasicItems(MODE_COLOR);
			colorAdapter.notifyDataSetChanged();
			
			sizeItems.clear();
			loadBasicItems(MODE_SIZE);
			sizeAdapter.notifyDataSetChanged();
			
			mixtureItems.clear();
			loadBasicItems(MODE_MIXTURE);
			mixtureAdapter.notifyDataSetChanged();
			
			btnCategory.setText(R.string.hintForProductCategory);
			btnColor.setText(R.string.hintForProductColor);
			btnSize.setText(R.string.hintForProductSize);
			btnMixtureRate.setText(R.string.hintForProductMixtureRate);
			
			btnCategory.setText(R.string.hintForProductCategory);
			btnColor.setText(R.string.hintForProductColor);
			btnSize.setText(R.string.hintForProductSize);
			btnMixtureRate.setText(R.string.hintForProductMixtureRate);
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public String getCategoryStringUsingIndex(int index) {

		String categoryText = null;
		
		if(mActivity.categories != null) {
			int size = mActivity.categories.length;
			
			for(int i=0; i<size; i++) {
				categoryText = mActivity.categories[i].getCategoryStringById(index);
				
				if(categoryText != null) {
					break;
				}
			}
		}
		
		return categoryText;
	}
	
	public void addThumbnailViewWithUrl(int index, String url) {
		
		final ImageView ivImage = addThumbnailView(index, url, null);
		
		ivImage.setTag(url);
		DownloadUtils.downloadBitmap(url, new OnBitmapDownloadListener() {

			@Override
			public void onError(String url) {

				LogUtils.log("WholesaleForWritePage.addThumbnailViewWithUrl.onError." + "\nurl : " + url);
				ivImage.setBackgroundColor(Color.GRAY);
			}

			@Override
			public void onCompleted(String url, Bitmap bitmap) {

				try {
					LogUtils.log("WholesaleForWritePage.addThumbnailViewWithUrl.onCompleted." + "\nurl : " + url);

					if(bitmap != null && !bitmap.isRecycled()) {
						ivImage.setImageBitmap(bitmap);
						((FrameLayout)ivImage.getParent()).setTag(bitmap);
					}
				} catch (Exception e) {
					LogUtils.trace(e);
				} catch (OutOfMemoryError oom) {
					LogUtils.trace(oom);
				}
			}
		});
	}
	
	public ImageView addThumbnailView(int index, final String url, Bitmap thumbnail) {
		
		try {
			FrameLayout frame = new FrameLayout(mContext);
			ResizeUtils.viewResize(240, 375, frame, 1, 0, null);

			frame.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					int index = Integer.parseInt(((FrameLayout)view).getChildAt(1).getTag().toString());
					choiceMode(index);
				}
			});
			
			//이미지.
			ImageView ivImage = new ImageView(mContext);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, ivImage, 2, 0, null);
			ivImage.setScaleType(ScaleType.CENTER_CROP);
			ivImage.setImageBitmap(thumbnail);
			frame.addView(ivImage);
			
			//대표 사진 프레임.
			View mainImage = new View(mContext);
			ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, mainImage, 2, 0, null);
			mainImage.setBackgroundResource(R.drawable.myshop_multiupload_btn_frame);
			frame.addView(mainImage);
			
			frame.getChildAt(1).setTag("" + index);
			
			if(index == 0) {
				mainImage.setVisibility(View.VISIBLE);
			} else {
				mainImage.setVisibility(View.INVISIBLE);
			}

			
			imageLinear.addView(frame);
			selectedFrames.add(frame);
			
			if(selectedFrames.size() > 3) {
				mainImage.postDelayed(new Runnable() {

					@Override
					public void run() {
						imageScroll.smoothScrollTo(
								ResizeUtils.getSpecificLength(240) * (selectedImages.size() - 3), 
								0);
					}
				}, 500);
			}
			
			if(selectedFrames.size() == 0) {
				tvUploadImage.setVisibility(View.VISIBLE);
			} else {
				tvUploadImage.setVisibility(View.INVISIBLE);
			}
			
			return ivImage;
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return null;
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
			
			if(!items.get(position).unusable) {
				
				if(items.get(position).selected) {
					checkBox.setVisibility(View.VISIBLE);
					
				} else{
					checkBox.setVisibility(View.INVISIBLE);
				}
				
			} else {
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
		public int count;
		public boolean selected;
		public boolean unusable;
		
		public Item(String text) {
			this.text = text;
		}
		
		public Item(String text, int count) {
			this.text = text;
			this.count = count;
		}
	}
}

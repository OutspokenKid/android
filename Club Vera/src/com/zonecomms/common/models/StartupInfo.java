package com.zonecomms.common.models;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import com.outspoken_kid.utils.LogUtils;

/**
 * v1.0.1
 * @author HyungGunKim
 *
 * v1.0.1 - Add Schedules
 */
public class StartupInfo {

	private BgInfo[] bgInfos;
	private Banner[] banners;
	private MenuColorSet[] menuColorSets;
	private LoadingImageSet loadingImageSet;
	private Popup popup;
	private Notice[] schedules;
	
	public StartupInfo(){}
	
	public StartupInfo(JSONObject objJSON) {
		
		try {
			if(objJSON.has("banner")) {
				JSONArray arJSON = objJSON.getJSONArray("banner");
				
				int length = arJSON.length();
				banners = new Banner[length];
				for(int i=0; i<banners.length; i++) {
					
					banners[i] = new Banner(arJSON.getJSONObject(i));
				}
			}
			
			if(objJSON.has("color")) {
				JSONArray arJSON = objJSON.getJSONArray("color");
				
				int size = arJSON.length();
				menuColorSets = new MenuColorSet[size];
				for(int i=0; i<size; i++) {
					menuColorSets[i] = new MenuColorSet(arJSON.getJSONObject(i)); 
				}
			}
			
			if(objJSON.has("loading")) {
				loadingImageSet = new LoadingImageSet(objJSON.getJSONObject("loading"));
			}
			
			if(objJSON.has("popup")) {
				popup = new Popup(objJSON.getJSONObject("popup"));
			}
			
			if(objJSON.has("schedule")) {
				
				JSONArray arJSON = objJSON.getJSONArray("schedule");
				
				int size = arJSON.length();
				schedules = new Notice[size];
				for(int i=0; i<size; i++) {
					schedules[i] = new Notice(arJSON.getJSONObject(i));
				}
			}
			
			if(objJSON.has("bgInfo")) {
				
				JSONArray arJSON = objJSON.getJSONArray("bgInfo");
				
				int size = arJSON.length();
				bgInfos = new BgInfo[size];
				for(int i=0; i<size; i++) {
					bgInfos[i] = new BgInfo(arJSON.getJSONObject(i));
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public Banner[] getBanners() {
		
		return banners;
	}
	
	public void setBanners(Banner[] banners) {
		
		this.banners = banners;
	}
	
	public MenuColorSet[] getMenuColorSets() {
		return menuColorSets;
	}
	
	public void setMenuColorSets(MenuColorSet[] menuColorSets) {
		this.menuColorSets = menuColorSets;
	}

	public LoadingImageSet getLoadingImageSet() {
		return loadingImageSet;
	}

	public void setLoadingImageSet(LoadingImageSet loadingImageSet) {
		this.loadingImageSet = loadingImageSet;
	}

	public Popup getPopup() {
		
		return popup;
	}

	public void setSchedules(Notice[] schedules) {
		
		this.schedules = schedules;
	}
	
	public Notice[] getSchedules() {
		
		return schedules;
	}

	public BgInfo[] getBgInfos() {
		
		return bgInfos;
	}

	public void setBgInfos(BgInfo[] bgInfos) {
		
		this.bgInfos = bgInfos;
	}
	
///////////////////// Classes.

	public class Banner {

		private int status;
		private String ad_nid;
		private String img_url;
		private String reg_id;
		private String reg_date;
		private String target_link;
		
		public Banner(){};
		
		public Banner(JSONObject objJSON) {
			
			try {
				if(objJSON.has("status")) {
					this.status = objJSON.getInt("status");
				}
				
				if(objJSON.has("ad_nid")) {
					this.ad_nid = objJSON.getString("ad_nid");
				}
				
				if(objJSON.has("img_url")) {
					this.img_url = objJSON.getString("img_url");
				}
				
				if(objJSON.has("reg_id")) {
					this.reg_id = objJSON.getString("reg_id");
				}
				
				if(objJSON.has("reg_date")) {
					this.reg_date = objJSON.getString("reg_date");
				}
				
				if(objJSON.has("target_link")) {
					this.target_link = objJSON.getString("target_link");
				}
			} catch(Exception e) {
				LogUtils.trace(e);
			}
		}
		
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public String getAd_nid() {
			return ad_nid;
		}
		public void setAd_nid(String ad_nid) {
			this.ad_nid = ad_nid;
		}
		public String getImg_url() {
			return img_url;
		}
		public void setImg_url(String img_url) {
			this.img_url = img_url;
		}
		public String getReg_id() {
			return reg_id;
		}
		public void setReg_id(String reg_id) {
			this.reg_id = reg_id;
		}
		public String getReg_date() {
			return reg_date;
		}
		public void setReg_date(String reg_date) {
			this.reg_date = reg_date;
		}
		public String getTarget_link() {
			return target_link;
		}
		public void setTarget_link(String target_link) {
			this.target_link = target_link;
		}
	}

	public class MenuColorSet {

		private int color_type;
		private int[] colors;
		
		public MenuColorSet(){}
		
		public MenuColorSet(JSONObject objJSON) {

			try {
				if(objJSON.has("color_type")) {
					color_type = objJSON.getInt("color_type");
				}
				
				if(objJSON.has("color")) {
					JSONArray arJSON = objJSON.getJSONArray("color");
					
					int size = arJSON.length();
					colors = new int[size];
					for(int i=0; i<size; i++) {
						JSONObject objColor = arJSON.getJSONObject(i);
						
						int r = objColor.getInt("r");
						int g = objColor.getInt("g");
						int b = objColor.getInt("b");
						
						colors[i] = Color.rgb(r, g, b);
					}
				}
			} catch(Exception e) {}
		}
		
		public int getColor_type() {
			return color_type;
		}
		public void setColor_type(int color_type) {
			this.color_type = color_type;
		}
		public int[] getColors() {
			return colors;
		}
		public void setColors(int[] colors) {
			this.colors = colors;
		}
	}

	public class LoadingImageSet {

		private int time;
		private String[] images;
		private Drawable[] drawables;
		
		public LoadingImageSet(){}
		
		public LoadingImageSet(JSONObject objJSON) {
			
			try {
				if(objJSON.has("loading")) {
					
					JSONArray arJSON = objJSON.getJSONArray("loading");
					int size = arJSON.length();
					images = new String[size];
					drawables = new Drawable[size];
					for(int i=0; i<size; i++) {
						images[i] = arJSON.getString(i);
					}
				}
				
				if(objJSON.has("timer")) {
					String timerString = objJSON.getString("timer");
					float timer = Float.parseFloat(timerString);
					time = (int)(timer / ((float)images.length) * 1000);
				}
				
			} catch(Exception e) {}
		}
		
		public int getTime() {
			return time;
		}
		
		public void setTime(int time) {
			this.time = time;
		}
		
		public String[] getImages() {
			return images;
		}
		
		public void setImages(String[] images) {
			this.images = images;
		}

		public Drawable[] getDrawables() {
			return drawables;
		}

		public void setDrawables(Drawable[] drawables) {
			this.drawables = drawables;
		}
	}

	public class Popup {

	private String content;
	private String bg_img_url;
	private String link_url;
	private String notice_title;
	private int notice_nid;
	private String[] imageUrls;
	
	public Popup(JSONObject objJSON) {

		try {
			if(objJSON.has("content")) {
				this.content = objJSON.getString("content");
			}
			
			if(objJSON.has("bg_img_url")) {
				this.bg_img_url = objJSON.getString("bg_img_url");
			}
			
			if(objJSON.has("link_url")) {
				this.link_url = objJSON.getString("link_url");
			}
			
			if(objJSON.has("notice_title")) {
				this.notice_title = objJSON.getString("notice_title");
			}
			
			if(objJSON.has("notice_nid")) {
				this.notice_nid = objJSON.getInt("notice_nid");
			}
			
			if(objJSON.has("medias")) {
				JSONArray arJSON = objJSON.getJSONArray("medias");
				int size = arJSON.length();
				
				if(size > 0) {
					imageUrls = new String[size];
					for(int i=0; i<size; i++) {
						getImageUrls()[i] = arJSON.getJSONObject(i).getString("media_src");
					}
				}
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}
	
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getBg_img_url() {
		return bg_img_url;
	}
	public void setBg_img_url(String bg_img_url) {
		this.bg_img_url = bg_img_url;
	}
	public String getLink_url() {
		return link_url;
	}
	public void setLink_url(String link_url) {
		this.link_url = link_url;
	}
	public String getNotice_title() {
		return notice_title;
	}
	public void setNotice_title(String notice_title) {
		this.notice_title = notice_title;
	}
	public int getNotice_nid() {
		return notice_nid;
	}
	public void setNotice_nid(int notice_nid) {
		this.notice_nid = notice_nid;
	}
	public String[] getImageUrls() {
		return imageUrls;
	}
	public void setImageUrls(String[] imageUrls) {
		this.imageUrls = imageUrls;
	}
}

	public class BgInfo {

		public String color;
		public String url;

		public BgInfo(JSONObject objJSON) {
			
			try {
					color = objJSON.getString("colorBG");
					url = objJSON.getJSONArray("link_datas").getString(0);
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
	}
}

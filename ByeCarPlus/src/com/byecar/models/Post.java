package com.byecar.models;

import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class Post extends BCPBaseModel {
	
	private int id;
	private int type;
	private int board_id;
	private int author_id;
	private String title;
	private String content;
	private String rep_img_url;
	private int parent_id;
	private int likes_cnt;
	private int hits_cnt;
	private int to_show_cover;
	private int need_to_push;
	private long created_at;
	private String author_nickname;
	private String youtube_id;
	
	private boolean isOpened;
	
	public Post() {
		
	}
	
	public Post(JSONObject objJSON) {

		super(objJSON);
		
		try {
			if(objJSON.has("id")) {
				this.id = objJSON.getInt("id");
				setIndexno(id);
			}
			
			if(objJSON.has("type")) {
				this.type = objJSON.getInt("type");
			}
			
			if(objJSON.has("board_id")) {
				this.board_id = objJSON.getInt("board_id");
			}
			
			if(objJSON.has("author_id")) {
				this.author_id = objJSON.getInt("author_id");
			}
			
			if(objJSON.has("title")) {
				this.title = objJSON.getString("title");
			}
			
			if(objJSON.has("content")) {
				this.content = objJSON.getString("content");
			}
			
			if(objJSON.has("rep_img_url")) {
				this.rep_img_url = objJSON.getString("rep_img_url");
			}
			
			if(objJSON.has("parent_id")) {
				this.parent_id = objJSON.getInt("parent_id");
			}
			
			if(objJSON.has("likes_cnt")) {
				this.likes_cnt = objJSON.getInt("likes_cnt");
			}
			
			if(objJSON.has("hits_cnt")) {
				this.hits_cnt = objJSON.getInt("hits_cnt");
			}

			if(objJSON.has("to_show_cover")) {
				this.to_show_cover = objJSON.getInt("to_show_cover");
			}
			
			if(objJSON.has("need_to_push")) {
				this.need_to_push = objJSON.getInt("need_to_push");
			}
			
			if(objJSON.has("created_at")) {
				this.created_at = objJSON.getLong("created_at");
			}
			
			if(objJSON.has("priority")) {
				this.priority = objJSON.getLong("priority");
			}
			
			if(objJSON.has("author_nickname")) {
				this.author_nickname = objJSON.getString("author_nickname");
			}
			
			if(objJSON.has("author_nickname")) {
				this.author_nickname = objJSON.getString("author_nickname");
			}
			
			if(objJSON.has("youtube_id")) {
				this.youtube_id = objJSON.getString("youtube_id");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getBoard_id() {
		return board_id;
	}

	public void setBoard_id(int board_id) {
		this.board_id = board_id;
	}

	public int getAuthor_id() {
		return author_id;
	}

	public void setAuthor_id(int author_id) {
		this.author_id = author_id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getRep_img_url() {
		return rep_img_url;
	}

	public void setRep_img_url(String rep_img_url) {
		this.rep_img_url = rep_img_url;
	}

	public int getParent_id() {
		return parent_id;
	}

	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}

	public int getLikes_cnt() {
		return likes_cnt;
	}

	public void setLikes_cnt(int likes_cnt) {
		this.likes_cnt = likes_cnt;
	}

	public int getHits_cnt() {
		return hits_cnt;
	}

	public void setHits_cnt(int hits_cnt) {
		this.hits_cnt = hits_cnt;
	}

	public long getCreated_at() {
		return created_at;
	}

	public void setCreated_at(long created_at) {
		this.created_at = created_at;
	}

	public String getAuthor_nickname() {
		return author_nickname;
	}

	public void setAuthor_nickname(String author_nickname) {
		this.author_nickname = author_nickname;
	}

	public int getTo_show_cover() {
		return to_show_cover;
	}

	public void setTo_show_cover(int to_show_cover) {
		this.to_show_cover = to_show_cover;
	}

	public int getNeed_to_push() {
		return need_to_push;
	}

	public void setNeed_to_push(int need_to_push) {
		this.need_to_push = need_to_push;
	}

	public boolean isOpened() {
		return isOpened;
	}

	public void setOpened(boolean isOpened) {
		this.isOpened = isOpened;
	}

	public String getYoutube_id() {
		return youtube_id;
	}

	public void setYoutube_id(String youtube_id) {
		this.youtube_id = youtube_id;
	}
}

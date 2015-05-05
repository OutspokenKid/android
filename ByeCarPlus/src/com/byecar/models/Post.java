package com.byecar.models;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.utils.LogUtils;

public class Post extends BCPBaseModel implements Serializable {
	
	private static final long serialVersionUID = 1513094279636219158L;
	
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
	private String author_profile_img_url;
	private String youtube_id;
	private int replies_cnt;
	private String board_title; 
	private String[] images;
	private Post[] replies;
	private int is_liked;
	private int post_id;
	private int has_children;
	
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
			
			if(objJSON.has("author_profile_img_url")) {
				this.setAuthor_profile_img_url(objJSON.getString("author_profile_img_url"));
			}
			
			if(objJSON.has("youtube_id")) {
				this.youtube_id = objJSON.getString("youtube_id");
			}
			
			if(objJSON.has("replies_cnt")) {
				this.replies_cnt = objJSON.getInt("replies_cnt");
			}
			
			if(objJSON.has("board_title")) {
				this.board_title = objJSON.getString("board_title");
			}
			
			try {
				if(objJSON.has("images")) {
					JSONArray arJSON = objJSON.getJSONArray("images");
					
					int size = arJSON.length();
					images = new String[size];
					for(int i=0; i<size; i++) {
						images[i] = arJSON.getString(i);
					}
				}
			} catch (Exception e) {
			}
			
			
			try {
				if(objJSON.has("replies")) {
					JSONArray arJSON = objJSON.getJSONArray("replies");
					
					int size = arJSON.length();
					replies = new Post[size];
					for(int i=0; i<size; i++) {
						replies[i] = new Post(arJSON.getJSONObject(i));
					}
				}
			} catch (Exception e) {
			}
			
			if(objJSON.has("is_liked")) {
				this.is_liked = objJSON.getInt("is_liked");
			}
			
			if(objJSON.has("post_id")) {
				this.post_id = objJSON.getInt("post_id");
			}
			
			if(objJSON.has("has_children")) {
				this.has_children = objJSON.getInt("has_children");
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
	}

	public void copyValuesFromNewItem(Post newPost) {
		
		if(newPost == null) {
			this.id = 0;
			this.type = 0;
			this.board_id = 0;
			this.author_id = 0;
			this.title = null;
			this.content = null;
			this.rep_img_url = null;
			this.parent_id = 0;
			this.likes_cnt = 0;
			this.hits_cnt = 0;
			this.to_show_cover = 0;
			this.need_to_push = 0;
			this.created_at = 0;
			this.author_nickname = null;
			this.author_profile_img_url = null;
			this.youtube_id = null;
			this.replies_cnt = 0;
			this.board_title = null;
			this.images = null;
			this.replies = null;
			this.is_liked = 0;
			this.post_id = 0;
			this.has_children = 0;
		} else {
			this.id = newPost.getId();
			this.type = newPost.getType();
			this.board_id = newPost.getBoard_id();
			this.author_id = newPost.getAuthor_id();
			this.title = newPost.getTitle();
			this.content = newPost.getContent();
			this.rep_img_url = newPost.getRep_img_url();
			this.parent_id = newPost.getParent_id();
			this.likes_cnt = newPost.getLikes_cnt();
			this.hits_cnt = newPost.getHits_cnt();
			this.to_show_cover = newPost.getTo_show_cover();
			this.need_to_push = newPost.getNeed_to_push();
			this.created_at = newPost.getCreated_at();
			this.author_nickname = newPost.getAuthor_nickname();
			this.author_profile_img_url = newPost.getAuthor_profile_img_url();
			this.youtube_id = newPost.getYoutube_id();
			this.replies_cnt = newPost.getReplies_cnt();
			this.board_title = newPost.getBoard_title();
			this.images = newPost.getImages();
			this.replies = newPost.getReplies();
			this.is_liked = newPost.getIs_liked();
			this.post_id = newPost.getPost_id();
			this.has_children = newPost.getHas_children();
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

	public int getReplies_cnt() {
		return replies_cnt;
	}

	public void setReplies_cnt(int replies_cnt) {
		this.replies_cnt = replies_cnt;
	}

	public String getBoard_title() {
		return board_title;
	}

	public void setBoard_title(String board_title) {
		this.board_title = board_title;
	}

	public String[] getImages() {
		return images;
	}

	public void setImages(String[] images) {
		this.images = images;
	}

	public String getAuthor_profile_img_url() {
		return author_profile_img_url;
	}

	public void setAuthor_profile_img_url(String author_profile_img_url) {
		this.author_profile_img_url = author_profile_img_url;
	}

	public Post[] getReplies() {
		return replies;
	}

	public void setReplies(Post[] replies) {
		this.replies = replies;
	}

	public int getIs_liked() {
		return is_liked;
	}

	public void setIs_liked(int is_liked) {
		this.is_liked = is_liked;
	}

	public int getPost_id() {
		return post_id;
	}

	public void setPost_id(int post_id) {
		this.post_id = post_id;
	}

	public int getHas_children() {
		return has_children;
	}

	public void setHas_children(int has_children) {
		this.has_children = has_children;
	}
}

package com.zonecomms.common.models;

import org.json.JSONArray;
import org.json.JSONObject;

import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.LogUtils;

public class Post extends BaseModel {
	
	/**
상세페이지.
	spot_nid : 글 NID
	content : 글 내용
	media_src : 첨부이미지 URL
	reply_cnt : 글에 대한 댓글 수
	member {}
	copyright : 글 작성 출처 표시
	medias{}
	scrap_cnt : 스크랩 수
reg_id : 작성자 ID
========================
	
리스트.
	sb_spot_nid :  포스팅 글 NID
	sb_nickname : 앱 이름
	sb_nid : 앱 NID
	board_nid : 게시판 번호
	s_cate_id : 카테고리 NID (Papp이 속한)
	spot_nid : 글 NID
	content : 글내용
	media_src : 첨부 이미지 URL (1번째꺼)
	reply_cnt : 리플 갯수
	MEMBER{}
	copyright : 출처
	 */
	
	private int sb_spot_nid;
	private String sb_nickname;
	private String copyright;
	private int sb_nid;
	private int board_nid;
	private int[] s_cate_id;
	private int spot_nid;
	private String content;
	private int reply_cnt;
	private int read_cnt;
	private int scrap_cnt;
	/**
	 * Only using at list, using medias at post page.
	 */
	private String media_src;
	private Member member;
	private Media[] medias;
	
	private boolean postForNApp;
	private String reg_dt;
	
	public Post() {}
	
	public Post(JSONObject objJSON) {
		
		try {
			if(objJSON.has("sb_spot_nid")) {
				this.sb_spot_nid = objJSON.getInt("sb_spot_nid");
				setIndexno(sb_spot_nid);
			}
			
			if(objJSON.has("sb_nickname")) {
				this.sb_nickname = objJSON.getString("sb_nickname");
			}
			
			if(objJSON.has("copyright")) {
				this.copyright = objJSON.getString("copyright");
			}
			
			if(objJSON.has("sb_nid")) {
				this.sb_nid = objJSON.getInt("sb_nid");
			}
			
			if(objJSON.has("appBoard")) {
				JSONObject objBoard = objJSON.getJSONObject("appBoard");
				
				if(objBoard.has("board_nid")) {
					this.board_nid = objBoard.getInt("board_nid");
				}
			}
			
			if(objJSON.has("s_cate_id")) {
				try {
					JSONArray arJSON = objJSON.getJSONArray("s_cate_id");
					int length = arJSON.length();
					this.s_cate_id = new int[length];
					for(int i=0; i<length; i++) {
						this.s_cate_id[i] = arJSON.getInt(i);
					}
				} catch(Exception e) {
					LogUtils.trace(e);
				}
			}
			
			if(objJSON.has("spot_nid")) {
				this.spot_nid = objJSON.getInt("spot_nid");
			}

			if(objJSON.has("content")) {
				this.content = objJSON.getString("content");
			}
			
			if(objJSON.has("reply_cnt")) {
				this.reply_cnt = objJSON.getInt("reply_cnt");
			}
			
			if(objJSON.has("read_cnt")) {
				this.read_cnt = objJSON.getInt("read_cnt");
			}
			
			if(objJSON.has("scrap_cnt")) {
				this.scrap_cnt = objJSON.getInt("scrap_cnt");
			}
			
			if(objJSON.has("member")) {
				this.member = new Member(objJSON.getJSONObject("member"));
			}
			
			if(objJSON.has("media_src")) {
				this.media_src = objJSON.getString("media_src");
			}
			
			if(objJSON.has("medias")) {
				
				JSONArray arJSON = objJSON.getJSONArray("medias");
				
				int length = arJSON.length();
				
				if(length > 0) {
					medias = new Media[length];
					
					for(int i=0; i<length; i++) {
						try {
							Media media = new Media(arJSON.getJSONObject(i));
							medias[i] = media;
						} catch(Exception e) {
							LogUtils.trace(e);
						}
					}
				}
			}
			
			if(objJSON.has("reg_dt")) {
				this.reg_dt = objJSON.getString("reg_dt");
			}
		} catch(Exception e) {
			LogUtils.trace(e);
		}
	}

	public int getSb_spot_nid() {
		return sb_spot_nid;
	}

	public void setSb_spot_nid(int sb_spot_nid) {
		this.sb_spot_nid = sb_spot_nid;
	}

	public String getSb_nickname() {
		return sb_nickname;
	}

	public void setSb_nickname(String sb_nickname) {
		this.sb_nickname = sb_nickname;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public int getSb_nid() {
		return sb_nid;
	}

	public void setSb_nid(int sb_nid) {
		this.sb_nid = sb_nid;
	}

	public int getBoard_nid() {
		return board_nid;
	}

	public void setBoard_nid(int board_nid) {
		this.board_nid = board_nid;
	}

	public int[] getS_cate_id() {
		return s_cate_id;
	}

	public void setS_cate_id(int[] s_cate_id) {
		this.s_cate_id = s_cate_id;
	}

	public int getSpot_nid() {
		return spot_nid;
	}

	public void setSpot_nid(int spot_nid) {
		this.spot_nid = spot_nid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public int getReply_cnt() {
		return reply_cnt;
	}

	public void setReply_cnt(int reply_cnt) {
		this.reply_cnt = reply_cnt;
	}

	public int getRead_cnt() {
		return read_cnt;
	}

	public void setRead_cnt(int read_cnt) {
		this.read_cnt = read_cnt;
	}

	public int getScrap_cnt() {
		return scrap_cnt;
	}

	public void setScrap_cnt(int scrap_cnt) {
		this.scrap_cnt = scrap_cnt;
	}

	public String getMedia_src() {
		return media_src;
	}

	public void setMedia_src(String media_src) {
		this.media_src = media_src;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public Media[] getMedias() {
		return medias;
	}

	public void setMedias(Media[] medias) {
		this.medias = medias;
	}

	public boolean isPostForNApp() {
		return postForNApp;
	}

	public void setPostForNApp(boolean postForNApp) {
		this.postForNApp = postForNApp;
	}

	public String getReg_dt() {
		return reg_dt;
	}

	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
}

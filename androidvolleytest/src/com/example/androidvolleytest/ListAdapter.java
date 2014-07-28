package com.example.androidvolleytest;

import java.util.ArrayList;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.outspoken_kid.model.FontInfo;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.utils.ToastUtils;

public class ListAdapter extends BaseAdapter {

	public int color;
	
	private CircleHeaderView circleHeaderView;
	private LayoutInflater mInflater;
	private ArrayList<Post> posts = new ArrayList<Post>();
	private ArrayList<View> circleViews = new ArrayList<View>();
	
	public ListAdapter(Context context, CircleHeaderView circleHeaderView, 
			LayoutInflater mInflater, ArrayList<Post> posts) {

		this.circleHeaderView = circleHeaderView;
		this.mInflater = mInflater;
		this.posts = posts;
	}
	
	@Override
	public int getCount() {

		return posts.size() + 1;
	}

	@Override
	public Object getItem(int position) {

		if(posts.size() + 1 < position) {
			return posts.get(position + 1);
		} else{
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		
		return position + 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolderForCirclePost viewHolder;
		
		if(position == 0) {
			return circleHeaderView;
		} else {
			if(convertView == null || convertView instanceof CircleHeaderView) {
				View circleView = mInflater.inflate(R.layout.list_circlepost, parent, false);
				circleViews.add(circleView);
				convertView = circleView;
				
				viewHolder = new ViewHolderForCirclePost();
				viewHolder.bindViews(convertView);
				viewHolder.setSizes();
				viewHolder.setListeners();

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolderForCirclePost) convertView.getTag();
			}

			viewHolder.setValues(posts.get(position - 1));
			
			return convertView;
		}
	}
	
	public void changeColor(int color) {
		
		this.color = color;
		int size = circleViews.size();
		
		for(int i=0; i<size; i++) {
			try {
				if(circleViews.get(i) != null && circleViews.get(i).getTag() != null) {
					((ViewHolderForCirclePost)circleViews.get(i).getTag()).changeColor();
				}
			} catch (Exception e) {
				LogUtils.trace(e);
			} catch (Error e) {
				LogUtils.trace(e);
			}
		}
	}
	
//////////////////////////////////
	
	public class ViewHolderForCirclePost {

		public Post post;
		
		public FrameLayout profileFrame;
		public View profileBg;
		public NetworkImageView ivProfile;
		public TextView tvNickname;
		public TextView tvRegdate;
		public TextView tvContent;
		public NetworkImageView ivImage;
		public View bottomBg;
		public FrameLayout replyFrame;
		public TextView tvReply;
		public FrameLayout moreFrame;
		public View moreBg;
		public View more;
		
		public void bindViews(View convertView) {
			
			profileFrame = (FrameLayout) convertView.findViewById(R.id.list_circlepost_profileFrame);
			profileBg = convertView.findViewById(R.id.list_circlepost_profileBg);
			ivProfile = (NetworkImageView) convertView.findViewById(R.id.list_circlepost_ivProfile);
			tvNickname = (TextView) convertView.findViewById(R.id.list_circlepost_tvNickname);
			tvRegdate = (TextView) convertView.findViewById(R.id.list_circlepost_tvRegdate);
			tvContent = (TextView) convertView.findViewById(R.id.list_circlepost_tvContent);
			ivImage = (NetworkImageView) convertView.findViewById(R.id.list_circlepost_ivImage);
			bottomBg = convertView.findViewById(R.id.list_circlepost_bottomBg);
			replyFrame = (FrameLayout) convertView.findViewById(R.id.list_circlepost_replyFrame);
			tvReply = (TextView) convertView.findViewById(R.id.list_circlepost_tvReply);
			moreFrame = (FrameLayout) convertView.findViewById(R.id.list_circlepost_moreFrame);
			moreBg = convertView.findViewById(R.id.list_circlepost_moreBg);
			more = convertView.findViewById(R.id.list_circlepost_more);
		}
		
		public void setSizes() {
			
			RelativeLayout.LayoutParams rp = null;
			int margin = ResizeUtils.getSpecificLength(30);
			
			//profileFrame.
			rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(80), ResizeUtils.getSpecificLength(80));
			rp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			rp.leftMargin = margin;
			rp.topMargin = margin;
			rp.rightMargin = margin;
			rp.bottomMargin = margin;
			profileFrame.setLayoutParams(rp);
			
			//tvNickname.
			rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ResizeUtils.getSpecificLength(80));
			rp.addRule(RelativeLayout.ALIGN_TOP, R.id.list_circlepost_profileFrame);
			rp.addRule(RelativeLayout.RIGHT_OF, R.id.list_circlepost_profileFrame);
			tvNickname.setLayoutParams(rp);
			FontInfo.setFontSize(tvNickname, 24);
			
			//tvRegdate.
			rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ResizeUtils.getSpecificLength(80));
			rp.addRule(RelativeLayout.ALIGN_TOP, R.id.list_circlepost_profileFrame);
			rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			rp.rightMargin = margin;
			tvRegdate.setLayoutParams(rp);
			FontInfo.setFontSize(tvRegdate, 20);
			
			//tvContent.
			rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			rp.addRule(RelativeLayout.BELOW, R.id.list_circlepost_profileFrame);
			rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.list_circlepost_profileFrame);
			rp.rightMargin = margin;
			rp.bottomMargin = margin;
			tvContent.setLayoutParams(rp);
			FontInfo.setFontSize(tvContent, 30);
			
			//ivImage.
			rp = new RelativeLayout.LayoutParams(
					ResizeUtils.getSpecificLength(600), 
					ResizeUtils.getSpecificLength(640));
			rp.addRule(RelativeLayout.BELOW, R.id.list_circlepost_tvContent);
			rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			rp.rightMargin = margin;
			rp.bottomMargin = margin;
			ivImage.setLayoutParams(rp);
			
			//bottomBg.
			rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(68));
			rp.addRule(RelativeLayout.BELOW, R.id.list_circlepost_ivImage);
			bottomBg.setLayoutParams(rp);

			//ReplyFrame.
			rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(80), 
					ResizeUtils.getSpecificLength(40));
			rp.leftMargin = ResizeUtils.getSpecificLength(30);
			rp.topMargin = ResizeUtils.getSpecificLength(12);
			rp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			rp.addRule(RelativeLayout.ALIGN_TOP, R.id.list_circlepost_bottomBg);
			replyFrame.setLayoutParams(rp);
			
			//tvReply.
			ResizeUtils.viewResize(40, 40, tvReply, 2, Gravity.RIGHT, null);
			FontInfo.setFontSize(tvReply, 26);
			
			//moreFrame.
			rp = new RelativeLayout.LayoutParams(ResizeUtils.getSpecificLength(68), 
					ResizeUtils.getSpecificLength(68));
			rp.rightMargin = ResizeUtils.getSpecificLength(30);
			rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			rp.addRule(RelativeLayout.ALIGN_TOP, R.id.list_circlepost_bottomBg);
			moreFrame.setLayoutParams(rp);
			
			//moreBg.
			ResizeUtils.viewResize(50, 10, moreBg, 2, Gravity.TOP|Gravity.CENTER_HORIZONTAL, new int[]{0, 27, 0, 0});
			
			//more.
			ResizeUtils.viewResize(50, 20, more, 2, Gravity.TOP|Gravity.CENTER_HORIZONTAL, new int[]{0, 22, 0, 0});
		}
		
		public void setListeners() {

			moreFrame.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					ToastUtils.showToast("More clicked");
				}
			});
		}
	
		public void setValues(Post post) {

			if(!StringUtils.isEmpty(post.profileUrl)) {
				ivProfile.setVisibility(View.VISIBLE);
				DownloadUtils.downloadImage(ivProfile, post.profileUrl);
				
			} else {
				ivProfile.setVisibility(View.GONE);
			}
			
			tvNickname.setText(post.nickname);
			tvRegdate.setText(post.regdate);
			tvContent.setText(post.content);
			
			if(!StringUtils.isEmpty(post.imageUrl)) {
				ivImage.setVisibility(View.VISIBLE);
				DownloadUtils.downloadImage(ivImage, post.imageUrl);
				
			} else {
				ivImage.setVisibility(View.GONE);
			}
			
			if(post.reply_cnt > 99) {
				FontInfo.setFontSize(tvReply, 20);
				tvReply.setText("99+");
			} else {
				FontInfo.setFontSize(tvReply, 26);
				
				if(post.reply_cnt < 0) {
					tvReply.setText("0");
					
				} else {
					tvReply.setText("" + post.reply_cnt);
				}
			}

			changeColor();
		}
		
		public void changeColor() {
			
			//profileBg.
			profileBg.setBackgroundColor(color);
			
			//replyFrame.
			replyFrame.setBackgroundColor(color);
			
			//moreBg.
			moreBg.setBackgroundColor(color);
		}
	}
}

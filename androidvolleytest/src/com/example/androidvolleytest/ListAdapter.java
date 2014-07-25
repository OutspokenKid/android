package com.example.androidvolleytest;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.outspoken_kid.model.FontInfo;
import com.outspoken_kid.utils.DownloadUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;

public class ListAdapter extends BaseAdapter {

	private CircleHeaderView circleHeaderView;
	private LayoutInflater mInflater;
	private ArrayList<Post> posts = new ArrayList<Post>();
	
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

		ViewHolder viewHolder;
		
		if(position == 0) {
			return circleHeaderView;
		} else {
			if(convertView == null || convertView instanceof CircleHeaderView) {
				convertView = mInflater.inflate(R.layout.relative, parent, false);
				
				viewHolder = new ViewHolder();
				viewHolder.profileFrame = (FrameLayout) convertView.findViewById(R.id.profileFrame);
				viewHolder.profileBg = convertView.findViewById(R.id.profileBg);
				viewHolder.ivProfile = (NetworkImageView) convertView.findViewById(R.id.ivProfile);
				viewHolder.tvNickname = (TextView) convertView.findViewById(R.id.tvNickname);
				viewHolder.tvRegdate = (TextView) convertView.findViewById(R.id.tvRegdate);
				viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);
				viewHolder.ivImage = (NetworkImageView) convertView.findViewById(R.id.ivImage);
				
				viewHolder.ivImage.getLayoutParams().width = ResizeUtils.getScreenWidth();
				viewHolder.ivImage.getLayoutParams().height = ResizeUtils.getSpecificLength(480);
				
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
				viewHolder.profileFrame.setLayoutParams(rp);
				
				//tvNickname.
				rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ResizeUtils.getSpecificLength(80));
				rp.addRule(RelativeLayout.ALIGN_TOP, R.id.profileBg);
				rp.addRule(RelativeLayout.RIGHT_OF, R.id.profileBg);
				viewHolder.tvNickname.setLayoutParams(rp);
				FontInfo.setFontSize(viewHolder.tvNickname, 24);
				
				//tvRegdate.
				rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ResizeUtils.getSpecificLength(80));
				rp.addRule(RelativeLayout.ALIGN_TOP, R.id.profileBg);
				rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				rp.rightMargin = margin;
				viewHolder.tvRegdate.setLayoutParams(rp);
				FontInfo.setFontSize(viewHolder.tvRegdate, 20);
				
				//tvContent.
				rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				rp.addRule(RelativeLayout.BELOW, R.id.profileBg);
				rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.profileBg);
				rp.rightMargin = margin;
				rp.bottomMargin = margin;
				viewHolder.tvContent.setLayoutParams(rp);
				FontInfo.setFontSize(viewHolder.tvContent, 30);
				
				//ivImage.
				rp = new RelativeLayout.LayoutParams(
						ResizeUtils.getSpecificLength(600), 
						ResizeUtils.getSpecificLength(640));
				rp.addRule(RelativeLayout.BELOW, R.id.tvContent);
				rp.addRule(RelativeLayout.CENTER_HORIZONTAL);
				rp.rightMargin = margin;
				rp.bottomMargin = margin;
				viewHolder.ivImage.setLayoutParams(rp);
				
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			if(!StringUtils.isEmpty(posts.get(position-1).profileUrl)) {
				viewHolder.ivProfile.setVisibility(View.VISIBLE);
				DownloadUtils.downloadImage(viewHolder.ivProfile, posts.get(position-1).profileUrl);
			} else {
				viewHolder.ivProfile.setVisibility(View.GONE);
			}
			
			viewHolder.tvNickname.setText(posts.get(position-1).nickname);
			viewHolder.tvRegdate.setText(posts.get(position-1).regdate);
			viewHolder.tvContent.setText(posts.get(position-1).content);
			
			if(!StringUtils.isEmpty(posts.get(position-1).imageUrl)) {
				viewHolder.ivImage.setVisibility(View.VISIBLE);
				DownloadUtils.downloadImage(viewHolder.ivImage, posts.get(position-1).imageUrl);
			} else {
				viewHolder.ivImage.setVisibility(View.GONE);
			}
			
			return convertView;
		}
	}
	
//////////////////////////////////
	
	public class ViewHolder {

		public FrameLayout profileFrame;
		public View profileBg;
		public NetworkImageView ivProfile;
		public TextView tvNickname;
		public TextView tvRegdate;
		public TextView tvContent;
		public NetworkImageView ivImage;
	}
}

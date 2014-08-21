package com.zonecomms.common.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.calciumion.widget.BasePagerAdapter;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;
import com.outspoken_kid.utils.StringUtils;
import com.outspoken_kid.views.OutspokenImageView;
import com.zonecomms.clubcage.ImageViewer;
import com.zonecomms.clubcage.R;
import com.zonecomms.clubcage.classes.ZoneConstants;
import com.zonecomms.clubcage.classes.ZonecommsApplication;
import com.zonecomms.common.models.Media;
import com.zonecomms.common.models.Post;
import com.zonecomms.common.views.CircleHeaderView;

public class CircleListAdapter extends BaseAdapter {

	public int color;
	
	private CircleHeaderView circleHeaderView;
	private LayoutInflater mInflater;
	private ArrayList<BaseModel> models = new ArrayList<BaseModel>();
	private ArrayList<View> circleViews = new ArrayList<View>();
	
	public CircleListAdapter(Context context, CircleHeaderView circleHeaderView, 
			LayoutInflater mInflater, ArrayList<BaseModel> models) {

		this.circleHeaderView = circleHeaderView;
		this.mInflater = mInflater;
		this.models = models;
	}
	
	@Override
	public int getCount() {

		return models.size() + 1;
	}

	@Override
	public Object getItem(int position) {

		if(models.size() + 1 < position) {
			return models.get(position + 1);
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

		
		try {
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

				viewHolder.setValues((Post)models.get(position - 1));
			}
		} catch (Exception e) {
			LogUtils.trace(e);
		} catch (Error e) {
			LogUtils.trace(e);
		}
		
		return convertView;
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

		private Context context;
		public Post post;
		public View row;
		
		public FrameLayout profileFrame;
		public View profileBg;
		public OutspokenImageView ivProfile;
		public TextView tvNickname;
		public TextView tvRegdate;
		public TextView tvContent;
		public ViewPager viewPager;
		public View bottomBg;
		public FrameLayout replyFrame;
		public TextView tvReply;
		public FrameLayout moreFrame;
		public View moreBg;
		public View more;
		
		public void bindViews(View convertView) {
			
			context = convertView.getContext();
			row = convertView;
			
			profileFrame = (FrameLayout) convertView.findViewById(R.id.list_circlepost_profileFrame);
			profileBg = convertView.findViewById(R.id.list_circlepost_profileBg);
			ivProfile = (OutspokenImageView) convertView.findViewById(R.id.list_circlepost_ivProfile);
			tvNickname = (TextView) convertView.findViewById(R.id.list_circlepost_tvNickname);
			tvRegdate = (TextView) convertView.findViewById(R.id.list_circlepost_tvRegdate);
			tvContent = (TextView) convertView.findViewById(R.id.list_circlepost_tvContent);
			viewPager = (ViewPager) convertView.findViewById(R.id.list_circlepost_viewPager);
			bottomBg = convertView.findViewById(R.id.list_circlepost_bottomBg);
			replyFrame = (FrameLayout) convertView.findViewById(R.id.list_circlepost_replyFrame);
			tvReply = (TextView) convertView.findViewById(R.id.list_circlepost_tvReply);
			moreFrame = (FrameLayout) convertView.findViewById(R.id.list_circlepost_moreFrame);
			moreBg = convertView.findViewById(R.id.list_circlepost_moreBg);
			more = convertView.findViewById(R.id.list_circlepost_more);
			
			FontUtils.setGlobalFont(tvNickname);
			FontUtils.setGlobalFont(tvRegdate);
			FontUtils.setGlobalFont(tvContent);
			FontUtils.setGlobalFont(tvReply);
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
			FontUtils.setFontSize(tvNickname, 24);
			
			//tvRegdate.
			rp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, ResizeUtils.getSpecificLength(80));
			rp.addRule(RelativeLayout.ALIGN_TOP, R.id.list_circlepost_profileFrame);
			rp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			rp.rightMargin = margin;
			tvRegdate.setLayoutParams(rp);
			FontUtils.setFontSize(tvRegdate, 20);
			
			//tvContent.
			rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			rp.addRule(RelativeLayout.BELOW, R.id.list_circlepost_profileFrame);
			rp.addRule(RelativeLayout.ALIGN_LEFT, R.id.list_circlepost_profileFrame);
			rp.rightMargin = margin;
			rp.bottomMargin = margin;
			tvContent.setLayoutParams(rp);
			FontUtils.setFontSize(tvContent, 30);
			
			//viewPager.
			rp = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, 
					ResizeUtils.getSpecificLength(640));
			rp.addRule(RelativeLayout.BELOW, R.id.list_circlepost_tvContent);
			rp.bottomMargin = margin;
			viewPager.setLayoutParams(rp);
			int defaultGap = ResizeUtils.getSpecificLength(20);
			viewPager.setPadding(defaultGap, 0, defaultGap, 0);
			viewPager.setPageMargin(defaultGap/2);
			
			//bottomBg.
			rp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(68));
			rp.addRule(RelativeLayout.BELOW, R.id.list_circlepost_viewPager);
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
			FontUtils.setFontSize(tvReply, 26);
			
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
			
			profileFrame.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					ZonecommsApplication.getCircleActivity().showProfilePopup(
							post.getMember().getMember_id(), 
							post.getMember().getStatus());
				}
			});
			
			row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					try {
						String uriString = ZoneConstants.PAPP_ID + 
								"://android.zonecomms.com/post?spot_nid=" + 
								post.getSpot_nid();
						ZonecommsApplication.getCircleActivity()
								.launchToMainActivity(Uri.parse(uriString));
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (Error e) {
						LogUtils.trace(e);
					}
				}
			});
		}
	
		public void setValues(Post post) {

			this.post = post;
			
			if(!StringUtils.isEmpty(post.getMember().getMedia_src())) {
				ivProfile.setVisibility(View.VISIBLE);
				ivProfile.setImageUrl(post.getMember().getMedia_src());
			} else {
				ivProfile.setVisibility(View.GONE);
			}
			
			tvNickname.setText(post.getMember().getMember_nickname());
			tvRegdate.setText(post.getReg_dt());
			tvContent.setText(post.getContent());
			
			if(post.getMedias() == null
					|| post.getMedias().length == 0) {
				viewPager.setVisibility(View.GONE);
			} else {
				viewPager.setAdapter(new CirclePagerAdapter(context, post.getMedias()));
				viewPager.getAdapter().notifyDataSetChanged();
				viewPager.setVisibility(View.VISIBLE);
			}
			
			if(post.getReply_cnt() > 99) {
				FontUtils.setFontSize(tvReply, 20);
				tvReply.setText("99+");
			} else {
				FontUtils.setFontSize(tvReply, 26);
				
				if(post.getReply_cnt() < 0) {
					tvReply.setText("0");
					
				} else {
					tvReply.setText("" + post.getReply_cnt());
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
	
	public class CirclePagerAdapter extends BasePagerAdapter {

		private Context context;
		private Media[] medias;
		
		public CirclePagerAdapter(Context context, Media[] medias) {
			
			this.context = context;
			this.medias = medias; 
		}
		
		@Override
		protected Object getItem(int position) {
			
			if(medias != null) {
				return medias[position];
			} else {
				return 0;
			}
		}

		@Override
		protected View getView(final Object object, View convertView, ViewGroup parent) {

			/*
			http://112.169.61.103/externalapi/public/
			spot/detail
			?ver=13&lng=ko&os=android&device=android&
			device_token=c02c705e98588f724ca046ac59cafece65501e36
			&sb_id=clubcage
			&image_size=1080
			&spot_nid=1885
			*/
			
			OutspokenImageView ivImage;
			
			if(convertView == null) {
				ivImage = new OutspokenImageView(context);
				ivImage.setScaleType(ScaleType.CENTER_CROP);
			} else {
				ivImage = (OutspokenImageView) convertView;
			}
			
			ivImage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					Intent intent = new Intent(context, ImageViewer.class);

					int size = medias.length;
					String[] imageUrls = new String[size];
					String[] thumbnailUrls = new String[size];
					
					for(int i=0; i<size; i++) {
						imageUrls[i] = medias[i].getMedia_src();
						thumbnailUrls[i] = medias[i].getThumbnail();
					}
					
					if(imageUrls != null && imageUrls.length != 0) {
						intent.putExtra("imageUrls", imageUrls);
					}
					
					if(thumbnailUrls != null && thumbnailUrls.length != 0) {
						intent.putExtra("thumbnailUrls", thumbnailUrls);
					}

					intent.putExtra("index", getItemPosition(object));
					context.startActivity(intent);
				}
			});
			
			String imageUrl = ((Media)object).getMedia_src();
			ivImage.setImageUrl(imageUrl);
			
			return ivImage;
		}

		@Override
		public int getCount() {
			
			if(medias != null) {
				return medias.length;
			} else{
				return 0;
			}
		}
		
		@Override
		public float getPageWidth(int position) {
	
			return 1;	
		}
	}
}

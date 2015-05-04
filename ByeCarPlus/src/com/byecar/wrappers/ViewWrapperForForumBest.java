package com.byecar.wrappers;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.TextView;

import com.byecar.byecarplus.R;
import com.byecar.classes.BCPConstants;
import com.byecar.classes.BCPFragmentActivity;
import com.byecar.models.ForumBest;
import com.byecar.views.ForumView;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.FontUtils;
import com.outspoken_kid.utils.LogUtils;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForForumBest extends ViewWrapper {

	private ForumBest forumBest;
	
	private TextView title1;
	private ForumView[] forumViews;
	private TextView title2;
	private BCPFragmentActivity mActivity; 
	
	public ViewWrapperForForumBest(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		title1 = (TextView) row.findViewById(R.id.list_forum_best_title1);
		title2 = (TextView) row.findViewById(R.id.list_forum_best_title2);

		forumViews = new ForumView[3];
		forumViews[0] = (ForumView) row.findViewById(R.id.list_forum_best_forumView1);
		forumViews[1] = (ForumView) row.findViewById(R.id.list_forum_best_forumView2);
		forumViews[2] = (ForumView) row.findViewById(R.id.list_forum_best_forumView3);
	}

	@Override
	public void setSizes() {

		row.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 41, title1, 1, 0, new int[]{0, 0, 0, 32});
		ResizeUtils.viewResize(LayoutParams.MATCH_PARENT, 41, title2, 1, 0, new int[]{0, 32, 0, 0});
		
		title1.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		title2.setPadding(ResizeUtils.getSpecificLength(20), 0, 0, 0);
		
		FontUtils.setFontSize(title1, 24);
		FontUtils.setFontSize(title2, 24);
		FontUtils.setFontStyle(title1, FontUtils.BOLD);
		FontUtils.setFontStyle(title2, FontUtils.BOLD);
		
		int size = forumViews.length;
		for(int i=0; i<size; i++) {
			ResizeUtils.viewResize(578, 135, forumViews[i], 1, Gravity.CENTER_HORIZONTAL, new int[]{0, i==0?0:16, 0, 0});
		}
		
		row.findViewById(R.id.list_forum_best_bottomBlank).getLayoutParams().height = ResizeUtils.getSpecificLength(16);
	}

	@Override
	public void setValues(BaseModel baseModel) {

		if(baseModel instanceof ForumBest) {
			
			this.forumBest = (ForumBest) baseModel;
			
			for(int i=0; i<Math.min(3, forumBest.getPosts().size()); i++) {
				forumViews[i].setForum(forumBest.getPosts().get(i), i);
			}
		}
	}

	@Override
	public void setListeners() {

		for(int i=0; i<Math.min(3, forumBest.getPosts().size()); i++) {

			final int INDEX = i;
			forumViews[i].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					try {
						Bundle bundle = new Bundle();
						bundle.putSerializable("post", forumBest.getPosts().get(INDEX));
						mActivity.showPage(BCPConstants.PAGE_FORUM_DETAIL, bundle);
					} catch (Exception e) {
						LogUtils.trace(e);
					} catch (Error e) {
						LogUtils.trace(e);
					}
				}
			});
		}
	}

	@Override
	public void setUnusableView() {

	}
	
	public void setActivity(BCPFragmentActivity activity) {

		mActivity = activity;
	}
}

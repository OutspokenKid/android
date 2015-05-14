package com.byecar.wrappers;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;

import com.byecar.byecarplus.R;
import com.byecar.models.Post;
import com.byecar.views.ForumView;
import com.outspoken_kid.classes.ViewWrapper;
import com.outspoken_kid.model.BaseModel;
import com.outspoken_kid.utils.ResizeUtils;

public class ViewWrapperForForum extends ViewWrapper {

	private Post post;
	private ForumView forumView;
	
	public ViewWrapperForForum(View row, int itemCode) {
		super(row, itemCode);
	}

	@Override
	public void bindViews() {

		forumView = (ForumView) row.findViewById(R.id.list_forum_forumView);
	}

	@Override
	public void setSizes() {

		row.setLayoutParams(new AbsListView.LayoutParams(
				LayoutParams.MATCH_PARENT, ResizeUtils.getSpecificLength(135)));
		
		ResizeUtils.viewResize(578, 135, forumView, 1, Gravity.CENTER_HORIZONTAL, null);
	}

	@Override
	public void setValues(BaseModel baseModel) {

		if(baseModel instanceof Post) {
			
			this.post = (Post) baseModel;
			forumView.setForum(post, -1);
		}
	}

	@Override
	public void setListeners() {
		
	}

	@Override
	public void setUnusableView() {

	}
}

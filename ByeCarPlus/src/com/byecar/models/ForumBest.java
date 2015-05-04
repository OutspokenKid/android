package com.byecar.models;

import java.util.ArrayList;

public class ForumBest extends BCPBaseModel {

	private ArrayList<Post> posts = new ArrayList<Post>();

	public ArrayList<Post> getPosts() {
		return posts;
	}

	public void setPosts(ArrayList<Post> posts) {
		this.posts = posts;
	}

}

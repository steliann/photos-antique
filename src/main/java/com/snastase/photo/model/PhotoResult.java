package com.snastase.photo.model;

public class PhotoResult {
	
	private String url;
	private String title;
	private String imgUrl;
	private String highlights;
	
	private String id;
	
	public PhotoResult(String id) {
		this.id = id;
	}

	public String getId(){
		return id;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHighlights() {
		return highlights;
	}

	public void setHighlights(String highlights) {
		this.highlights = highlights;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

}

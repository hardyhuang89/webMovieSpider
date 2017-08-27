package com.example.demo.domain;
/**
 *  
 * @author hardyhuang 2017年8月21日
 * 电影的实体类
 */
public class MovieInfo {

	private String movieName;
	private String url;
	private String ratingNum;
	
	public String getMovieName() {
		return movieName;
	}
	public void setMovieName(String movieName) {
		this.movieName = movieName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRatingNum() {
		return ratingNum;
	}
	@Override
	public String toString() {
		return "MovieInfo [movieName=" + movieName + ", url=" + url + ", ratingNum=" + ratingNum + "]";
	}
	public void setRatingNum(String ratingNum) {
		this.ratingNum = ratingNum;
	}
	
	
}

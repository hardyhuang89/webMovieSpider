package com.example.demo.domain;

/**
 * 
 * @author hardyhuang 2017年8月23日 父类，要爬的对象
 */
public class WebSite {

	private String url;
	private String name;
	private String cookie;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

}

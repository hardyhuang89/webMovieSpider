package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.demo.domain.MovieInfo;
import com.example.demo.domain.ResultObj;
import com.example.demo.domain.SpiderRule;
import com.example.demo.service.utils.ExtractUtils;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 
 * @author hardyhuang 2017年8月20日 爬虫执行类
 */
@Service

public class ExtractService {

	public String getTopMovie(int page) {
		List<MovieInfo> movies = new ArrayList<MovieInfo>();
		String baseUrl = "http://www.ygdy8.net";
		String movieParadiseUrl = baseUrl + "/html/gndy/dyzz/";
		Pattern pattern = Pattern.compile("(?<=\\《)(.+?)(?=\\》)");
		for (int i = 1; i < page; i++) {
			String next = "list_23_" + i + ".html";
			String url = movieParadiseUrl + next;
			SpiderRule rule = new SpiderRule(url, null, null, "co_content8", SpiderRule.CLASS, SpiderRule.GET);
			List<ResultObj> extracts = ExtractUtils.extractByURL(rule);
			for (ResultObj result : extracts) {
				String linkText = result.getLinkText();
				Matcher matcher = pattern.matcher(linkText);
				while (matcher.find()) {
					MovieInfo movie = new MovieInfo();
					movie.setMovieName(matcher.group());
					movie.setUrl(baseUrl + result.getLinkHref());
					movies.add(movie);
				}
			}
		}
		for(MovieInfo movie:movies){
			movie.setRatingNum(getDouBangRantingNum(movie));
		}
		
		for(MovieInfo movie:movies){
			System.out.println(movie.toString());
		}
		
		
		return null;
	}

	public String getDouBangRantingNum(MovieInfo movie){
		String movieName = movie.getMovieName();
		String baseUrl = "https://movie.douban.com";
		String movieParadiseUrl = baseUrl + "/subject_search?search_text="+movieName;
		SpiderRule rule = new SpiderRule(movieParadiseUrl, null, null,
				"root", SpiderRule.CLASS, SpiderRule.GET);
		return ExtractUtils.extractGetRatingNumBySelenium(rule);
	}

}

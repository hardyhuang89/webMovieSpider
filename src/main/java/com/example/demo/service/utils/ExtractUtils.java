package com.example.demo.service.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.util.StringUtils;

import com.example.demo.domain.ResultObj;
import com.example.demo.domain.SpiderRule;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * 爬虫工具类
 * 
 * @author hardyhuang 2017年8月25日
 *
 */
public class ExtractUtils {

	/**
	 * 通用的，直接用jsoup解析url，主要搜索a
	 * 
	 * @param rule
	 * @return
	 */
	public static List<ResultObj> extractByURL(SpiderRule rule) {
		validateRule(rule);
		List<ResultObj> resultObjs = new ArrayList<ResultObj>();
		try {
			String url = rule.getUrl();
			String[] params = rule.getParams();
			String[] values = rule.getValues();
			String resultTagName = rule.getResultTagName();
			int type = rule.getType();
			int resultType = rule.getRequestMoethod();
			Connection conn = Jsoup.connect(url);
			if (params != null && values != null && params.length == values.length) {
				for (int i = 0; i < params.length; i++) {
					conn.data(params[i], values[i]);
				}
			}

			Document doc = null;
			switch (resultType) {
			case SpiderRule.GET:
				doc = conn.timeout(100000).get();
				break;
			case SpiderRule.POST:
				doc = conn.timeout(100000).post();
				break;
			}

			parseAndGetData(resultObjs, type, resultTagName, doc);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultObjs;
	}

	public static List<ResultObj> extractByHtmlunit(SpiderRule rule) {
		validateRule(rule);
		List<ResultObj> resultObjs = new ArrayList<ResultObj>();
		try {
			String url = rule.getUrl();
			String[] params = rule.getParams();
			String[] values = rule.getValues();
			String resultTagName = rule.getResultTagName();
			int type = rule.getType();
			int resultType = rule.getRequestMoethod();

			// 构造一个webClient 模拟Chrome 浏览器
			WebClient webClient = new WebClient(BrowserVersion.CHROME);
			// 屏蔽日志信息
			LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log",
					"org.apache.commons.logging.impl.NoOpLog");
			java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
			// 支持JavaScript
			webClient.getOptions().setUseInsecureSSL(true);
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setActiveXNative(false);
			webClient.getOptions().setCssEnabled(false);
			webClient.getOptions().setThrowExceptionOnScriptError(false);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
			webClient.getOptions().setTimeout(5000);
			HtmlPage rootPage = webClient.getPage(url);
			// 设置一个运行JavaScript的时间
			webClient.waitForBackgroundJavaScript(5000);
			String html = rootPage.asXml();
			Document doc = Jsoup.parse(html);

			parseAndGetData(resultObjs, type, resultTagName, doc);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultObjs;
	}

	public static List<ResultObj> extractBySelenium(SpiderRule rule) {
		validateRule(rule);
		List<ResultObj> resultObjs = new ArrayList<ResultObj>();
		WebDriver webDriver = null;
		try {
			String url = rule.getUrl();
			String[] params = rule.getParams();
			String[] values = rule.getValues();
			String resultTagName = rule.getResultTagName();
			int type = rule.getType();
			int resultType = rule.getRequestMoethod();

			Document doc = getDocBySelenium(webDriver, url);

			Elements results = new Elements();
			switch (type) {
			case SpiderRule.CLASS:
				results = doc.getElementsByClass(resultTagName);
				break;
			case SpiderRule.ID:
				Element element = doc.getElementById(resultTagName);
				results.add(element);
				break;
			case SpiderRule.SELECTION:
				results = doc.select(resultTagName);
				break;
			default:
				if (StringUtils.isEmpty(resultTagName)) {
					results = doc.getElementsByTag("body");
				}

			}

			parseAndGetData(resultObjs, type, resultTagName, doc);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (webDriver != null) {
				webDriver.close();
			}
		}
		return resultObjs;
	}

	public static String extractGetRatingNumBySelenium(SpiderRule rule) {
		validateRule(rule);
		List<ResultObj> resultObjs = new ArrayList<ResultObj>();
		WebDriver webDriver = null;
		String ratingNum = null;
		try {
			String url = rule.getUrl();
			String[] params = rule.getParams();
			String[] values = rule.getValues();
			String resultTagName = rule.getResultTagName();
			int type = rule.getType();
			int resultType = rule.getRequestMoethod();

			Document doc = getDocBySelenium(webDriver, url);

			Elements results = new Elements();
			switch (type) {
			case SpiderRule.CLASS:
				results = doc.getElementsByClass(resultTagName);
				break;
			case SpiderRule.ID:
				Element element = doc.getElementById(resultTagName);
				results.add(element);
				break;
			case SpiderRule.SELECTION:
				results = doc.select(resultTagName);
				break;
			default:
				if (StringUtils.isEmpty(resultTagName)) {
					results = doc.getElementsByTag("body");
				}

			}

			for (Element result : results) {
				Elements div = result.getElementsByClass("sc-ifAKCX");
				if (!div.isEmpty()) {
					Element first = div.first();
					Elements ratingNums = first.getElementsByClass("rating_nums");
					if (!ratingNums.isEmpty()) {
						ratingNum = ratingNums.first().text();
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (webDriver != null) {
				webDriver.close();
			}
		}
		return ratingNum;
	}

	private static Document getDocBySelenium(WebDriver webDriver, String url) {
		System.getProperties().setProperty("webdriver.chrome.driver", "E:\\chromedriver.exe");
		webDriver = new ChromeDriver();
		webDriver.get(url);
		String responseBody = webDriver.getPageSource();
		return Jsoup.parse(responseBody);
	}

	private static void parseAndGetData(List<ResultObj> resultObjs, int type, String resultTagName, Document doc) {
		Elements results = new Elements();
		switch (type) {
		case SpiderRule.CLASS:
			results = doc.getElementsByClass(resultTagName);
			break;
		case SpiderRule.ID:
			Element element = doc.getElementById(resultTagName);
			results.add(element);
			break;
		case SpiderRule.SELECTION:
			results = doc.select(resultTagName);
			break;
		default:
			if (StringUtils.isEmpty(resultTagName)) {
				results = doc.getElementsByTag("body");
			}

		}

		for (Element result : results) {
			Elements links = result.getElementsByTag("a");
			for (Element link : links) {
				String linkHref = link.attr("href");
				String linkText = link.text();
				ResultObj resultObj = new ResultObj();
				resultObj.setLinkHref(linkHref);
				resultObj.setLinkText(linkText);
				resultObjs.add(resultObj);
			}
		}
	}

	private static boolean validateRule(SpiderRule rule) {
		return true;
	}

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.itvieccrawler;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author nhatvm
 */
public class Helper {

	private static final Logger _Logger = Logger.getLogger(Helper.class);
	private static final Set<Integer> bannedHttpCode = new HashSet<>();
	private static Map<String, Integer> sourceRequestCount = new HashMap<>();
	private static final int retryCount = 4;

	static {
		bannedHttpCode.add(301);
		bannedHttpCode.add(401);
		bannedHttpCode.add(403);
		bannedHttpCode.add(408);
		bannedHttpCode.add(429);
		bannedHttpCode.add(503);
		bannedHttpCode.add(500);
	}

	private Helper() {

	}

	public static Document jsoupConnect(String url, String sourceName) {
		Document doc = null;
		Connection.Response res = null;
		try {
			for (int i = 0; i < retryCount; i++) {

				res = Jsoup.connect(url).ignoreHttpErrors(true).timeout(60000).execute();

//				_Logger.info(res.statusCode() + ": " + res.statusMessage());
				if (bannedHttpCode.contains(res.statusCode())) {
					_Logger.info("Sleep 10s. HTTP error code: " + res.statusCode() + ", status message: " + res.statusMessage());
					_Logger.info("retry request count: " + i);
					Thread.sleep(10 * 1000);
				} else {
					break;
				}
			}

			if (bannedHttpCode.contains(res.statusCode())) {
				return null;
			}

			doc = res.parse();
			Thread.sleep(AppConfig.SLEEP_PER_REQUEST);
//			int requestCount = sourceRequestCount.get(sourceName) == null ? 0 : sourceRequestCount.get(sourceName);
//			requestCount++;
//
//			if (requestCount > 100) {
////				_Logger.info(sourceName + " has already " + requestCount + " maked. Sleep for a while (s): " + (AppConfig.SLEEP_PER_REQUEST * 50 / 1000));
////				Thread.sleep(AppConfig.SLEEP_PER_REQUEST * 100);
//				requestCount = 0;
//			}
//			sourceRequestCount.put(sourceName, requestCount);
		} catch (Exception e) {
			_Logger.error(e.getMessage(), e);
		}
		return doc;
	}

	public static void main(String[] args) throws Exception {
		String url = "https://www.webtretho.com/forum/f3950/da-2765903/";
		url = "https://www.webtretho.com/forum/f3950/dan-ong-vo-tam-cach-may-cung-bi-khuat-phuc-neu-nhu-dan-ba-lam-2-dieu-nay-2765903/";
		Document doc = jsoupConnect(url, "webtretho");
		System.out.println(doc.title());
	}
}

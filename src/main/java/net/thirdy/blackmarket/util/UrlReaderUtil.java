package net.thirdy.blackmarket.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;

public class UrlReaderUtil {

	public static String getString(String url, Map<String, Object> parameters) throws UnirestException {
		
		return Unirest.get(url)
			.header("Host","api.exiletools.com")
			.header("User-Agent","Mozilla/5.0 (Windows NT 6.1 WOW64 rv:41.0) Gecko/20100101 Firefox/41.0")
			.header("Accept","text/html,application/xhtml+xml,application/xmlq=0.9,*/*q=0.8")
			.header("Accept-Language","en-US,enq=0.5")
			.header("Accept-Encoding","gzip, deflate")
			.header("Cookie","SERVERID=A")
			.header("Connection","keep-alive")
			.header("Cache-Control","max-age=0")
			.queryString(parameters)
			.asString().getBody();
		
//		InputStream in;
//		String result = "";
//		try {
//			URLConnection connection = new URL( url ).openConnection();
//			connection.setUseCaches(false);
//			connection.setRequestProperty("Cache-Control", "max-age=0");
//			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0");
//			connection.connect();
////			connection.setDefaultUseCaches(false);
//			in = connection.getInputStream();
//			try {
//				result = IOUtils.toString( in );
//			} finally {
//				IOUtils.closeQuietly(in);
//			}
//		} catch (MalformedURLException e) {
//			// shouldn't happen
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//		return result;
	}
	
	 
}

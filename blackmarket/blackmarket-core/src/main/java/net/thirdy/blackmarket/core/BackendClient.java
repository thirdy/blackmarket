/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thirdy.blackmarket.core;

/**
 *
 * @author thirdy
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import net.thirdy.blackmarket.core.ex.BlackmarketException;

public class BackendClient {

//    private String cookies;
    private HttpClient client = HttpClientBuilder.create().build();
    
    int timeout = 30;
	int CONNECTION_TIMEOUT = timeout  * 1000; // timeout in millis
    RequestConfig requestConfig = RequestConfig.custom()
        .setConnectionRequestTimeout(CONNECTION_TIMEOUT)
        .setConnectTimeout(CONNECTION_TIMEOUT)
        .setSocketTimeout(CONNECTION_TIMEOUT)
        .build();

    public BackendClient() {
	}

	public static void main(String[] args) throws Exception {
        
        // cookies doesn't seem to matter
        // make sure cookies is turn on
//        CookieHandler.setDefault(new CookieManager());

        BackendClient backendClient = new BackendClient();
        String searchPage = backendClient.search("league=Warbands&type=&base=&name=&dmg_min=&dmg_max=&aps_min=&aps_max=&crit_min=&crit_max=&dps_min=&dps_max=&edps_min=&edps_max=&pdps_min=&pdps_max=&armour_min=&armour_max=&evasion_min=&evasion_max=&shield_min=&shield_max=&block_min=&block_max=&sockets_min=&sockets_max=&link_min=&link_max=&sockets_r=&sockets_g=&sockets_b=&sockets_w=&linked_r=&linked_g=&linked_b=&linked_w=&rlevel_min=&rlevel_max=&rstr_min=&rstr_max=&rdex_min=&rdex_max=&rint_min=&rint_max=&impl=&impl_min=&impl_max=&mods=&modexclude=&modmin=&modmax=&mods=&modexclude=&modmin=&modmax=&q_min=&q_max=&level_min=&level_max=&mapq_min=&mapq_max=&rarity=&seller=&thread=&time=2015-08-29&corrupted=&online=&buyout=&altart=&capquality=x&buyout_min=&buyout_max=&buyout_currency=&crafted=&identified=", true);
        System.out.println(searchPage);
        System.out.println("Done");
    }
    
    public String search(String payload, boolean sortByBuyout) throws BlackmarketException {
        try {
            String url = "http://poe.trade/search";
            String location = post(url, payload);
            // Add a bit of delay, just in case
            Thread.sleep(30);
            String searchPage = "";
            if (sortByBuyout) {
            	searchPage = postXMLHttpRequest(location, "sort=price_in_chaos&bare=true");
			} else {
				searchPage = get(location);
			}
            return searchPage;
        } catch (Exception ex) {
            Logger.getLogger(BackendClient.class.getName()).log(Level.SEVERE, null, ex);
            throw new BlackmarketException(ex);
        }
    }

    private String post(String url, String payload)
            throws Exception {

        HttpPost post = new HttpPost(url);
        post.setConfig(requestConfig);

        // add header
        post.setHeader("Host", "poe.trade");
        post.setHeader("User-Agent", USER_AGENT);
        post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        post.setHeader("Accept-Language", "en-US,en;q=0.5");
        post.setHeader("Accept-Encoding", "gzip, deflate");
        post.setHeader("Referer", "http://poe.trade/");
//        post.setHeader("Cookie", "_ga=GA1.2.750449977.1440808734; league=Warbands; _gat=1; mb_uid2=6130147680410288830"); // _ga=GA1.2.750449977.1440808734; league=Warbands; _gat=1; mb_uid2=6130147680410288830
        post.setHeader("Connection", "keep-alive");
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");

//        post.setEntity(new UrlEncodedFormEntity(postParams));
        post.setEntity(new StringEntity(payload));

        System.out.println("\nSending 'POST' request to URL : " + url);
        // bombs away!
        HttpResponse response = client.execute(post);

        int responseCode = response.getStatusLine().getStatusCode();

        System.out.println("Response Code : " + responseCode);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        
        rd.close();
        
        String location = null;
        
        System.out.println("Response Headers:");
        final Header[] allHeaders = response.getAllHeaders();
        for (Header header : allHeaders) {
            System.out.println(header.toString());
            if (header.getName().equalsIgnoreCase("Location")) {
                location = header.getValue();
            }
        }
        
        return location;
//	 System.out.println(result.toString());
    }
    
    private String postXMLHttpRequest(String url, String payload)
    		throws Exception {
    	System.out.println("postXMLHttpRequest() payload: " + payload);
    	System.out.println("postXMLHttpRequest() url: " + url);
    	StringEntity entity = new StringEntity(payload);
    	
    	HttpPost post = new HttpPost(url);
    	post.setConfig(requestConfig);
    	
    	// add header
    	post.setHeader("Accept", "*/*");
    	post.setHeader("Accept-Encoding", "gzip, deflate");
    	post.setHeader("Accept-Language", "en-US,en;q=0.5");
    	post.setHeader("Cache-Control", "no-cache");
    	post.setHeader("Connection", "keep-alive");
//    	post.setHeader("Content-Length", String.valueOf(entity.getContentLength()));
    	post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//        post.setHeader("Cookie", "_ga=GA1.2.750449977.1440808734; league=Warbands; _gat=1; mb_uid2=6130147680410288830"); // _ga=GA1.2.750449977.1440808734; league=Warbands; _gat=1; mb_uid2=6130147680410288830
    	post.setHeader("Host", "poe.trade");
    	post.setHeader("Pragma", "no-cache");
    	post.setHeader("Referer", url);
    	post.setHeader("User-Agent", USER_AGENT);
    	post.setHeader("X-Requested-With", "XMLHttpRequest");
    	
//        post.setEntity(new UrlEncodedFormEntity(postParams));
		post.setEntity(entity);
    	
    	System.out.println("\nSending 'POST' request to URL : " + url);
    	// bombs away!
    	HttpResponse response = client.execute(post);
    	
    	int responseCode = response.getStatusLine().getStatusCode();
    	
    	System.out.println("Response Code : " + responseCode);
    	
    	BufferedReader rd = new BufferedReader(
    			new InputStreamReader(response.getEntity().getContent()));
    	
    	StringBuilder result = new StringBuilder();
    	String line = "";
    	while ((line = rd.readLine()) != null) {
    		result.append(line);
    	}
    	
    	rd.close();
    	
    	String location = null;
    	
    	System.out.println("Response Headers:");
    	final Header[] allHeaders = response.getAllHeaders();
    	for (Header header : allHeaders) {
    		System.out.println(header.toString());
    		if (header.getName().equalsIgnoreCase("Location")) {
    			location = header.getValue();
    		}
    	}
    	
    	return result.toString();
//	 System.out.println(result.toString());
    }
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0";

    private String get(String url) throws Exception {

        HttpGet get = new HttpGet(url);
        get.setConfig(requestConfig);
        
        get.setHeader("Host", "poe.trade");
        get.setHeader("User-Agent", USER_AGENT);
        get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        get.setHeader("Accept-Language", "en-US,en;q=0.5");
        get.setHeader("Accept-Encoding", "gzip, deflate");
        get.setHeader("Referer", "http://poe.trade/");
//        post.setHeader("Cookie", "_ga=GA1.2.750449977.1440808734; league=Warbands; _gat=1; mb_uid2=6130147680410288830"); // _ga=GA1.2.750449977.1440808734; league=Warbands; _gat=1; mb_uid2=6130147680410288830
        
        HttpResponse response = client.execute(get);
        int responseCode = response.getStatusLine().getStatusCode();

        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader rd = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));

        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        
        rd.close();

        // set cookies
//        setCookies(response.getFirstHeader("Set-Cookie") == null ? ""
//                : response.getFirstHeader("Set-Cookie").toString());

        return result.toString();
    }

//    public String getCookies() {
//        return cookies;
//    }
//
//    public void setCookies(String cookies) {
//        this.cookies = cookies;
//    }

}

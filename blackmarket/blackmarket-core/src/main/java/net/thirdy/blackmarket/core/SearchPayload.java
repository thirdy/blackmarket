package net.thirdy.blackmarket.core;

import static org.apache.commons.lang3.StringUtils.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;


public class SearchPayload {
	String payload;

	String formatted;

	public SearchPayload(String payload) {
		this.payload = payload;
		format();
	}

	private void format() {
		List<String> list = new LinkedList<>();
		try {
			String[] pairs = split(payload, System.getProperty("line.separator"));
			for (String pair : pairs) {
				if (pair.startsWith("#") || pair.startsWith("//")) {
					continue;
				}
				String key = substringBefore(pair, "=");
				String val = substringAfter(pair, "=");
				String encVal = URLEncoder.encode(val, "UTF-8");
				list.add(key + "=" + encVal);
			}
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
		formatted = join(list, "&");
	}

	public String getPayloadFormatted() {
		return formatted;
	}
}

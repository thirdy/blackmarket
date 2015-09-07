package net.thirdy.blackmarket.core;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;

public class SearchPageScraperTest {

	@Test
	public void testParse() throws Exception {
		URL url = this.getClass().getResource("DefaultWarbands.search.poetrade");
		String page = FileUtils.readFileToString(new File(url.toURI()));
		SearchPageScraper scraper = new SearchPageScraper(page);
		List<SearchResultItem> list = scraper.parse();
		for (SearchResultItem item : list) {
			System.out.println(item);
		}
	}

}

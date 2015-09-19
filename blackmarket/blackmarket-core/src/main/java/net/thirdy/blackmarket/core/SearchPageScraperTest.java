package net.thirdy.blackmarket.core;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem.Mod;

import static org.junit.Assert.*
;
public class SearchPageScraperTest {

	@Test
	public void testParse() throws Exception {
		URL url = this.getClass().getResource("DefaultWarbands.search.poetrade");
		String page = FileUtils.readFileToString(new File(url.toURI()));
		SearchPageScraper scraper = new SearchPageScraper(page);
		List<SearchResultItem> list = scraper.parse();
		for (SearchResultItem item : list) {
			if (item.getName().equals("The Magnate Studded Belt")) {
				assertEquals("$#% increased Stun Duration on Enemies", item.getImplicitMod().getName());
				assertEquals("30.0", item.getImplicitMod().getValue());
				// explicit mods
//				<li class='sortable ' data-name='##% increased Physical Damage' data-value='30.0' style=''>
//				<li class='sortable ' data-name='#+# to Strength' data-value='42.0' style=''>
//				<li class='sortable ' data-name='##% increased Flask Charges gained' data-value='50.0' style=''>
				assertEquals("##% increased Physical Damage", item.getExplicitMods().get(0).getName());
				assertEquals("30.0", item.getExplicitMods().get(0).getValue());
				assertEquals("#+# to Strength", item.getExplicitMods().get(1).getName());
				assertEquals("42.0", item.getExplicitMods().get(1).getValue());
				assertEquals("##% increased Flask Charges gained", item.getExplicitMods().get(2).getName());
				assertEquals("50.0", item.getExplicitMods().get(2).getValue());
			}
		}
	}
	
	@Test
	public void testParse2() throws Exception {
		URL url = this.getClass().getResource("SearchChest.search.poetrade");
		String page = FileUtils.readFileToString(new File(url.toURI()));
		SearchPageScraper scraper = new SearchPageScraper(page);
		List<SearchResultItem> list = scraper.parse();
		for (SearchResultItem item : list) {
			if (item.getName().equals("Victory Shroud Destiny Leather")) {
				assertEquals("79.0", item.getPseudoLife());
//				for (Mod mod : item.getExplicitMods()) {
//					System.out.println(mod);
//				}
			}
		}
	}

}

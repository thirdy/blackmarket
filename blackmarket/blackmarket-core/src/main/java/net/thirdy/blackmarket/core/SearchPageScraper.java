/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thirdy.blackmarket.core;

import static org.apache.commons.lang3.StringUtils.substringAfter;
import static org.apache.commons.lang3.StringUtils.substringBefore;
import static org.apache.commons.lang3.StringUtils.trim;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem.Mod;

/**
 *
 * @author thirdy
 */
public class SearchPageScraper {

	private String page;

	public SearchPageScraper(String page) {
		this.page = page;
	}

	public List<SearchResultItem> parse() {
		List<SearchResultItem> searchResultItems = new LinkedList<>();
		Document doc = Jsoup.parse(page);

		Element content = doc.getElementById("content");

		Elements items = content.getElementsByClass("item");

//		System.out.println(items.get(86).toString());

//		System.out.println("Items");
		for (Element element : items) {
			
			SearchResultItem item = new SearchResultItem();

			item.id = element.attr("id");
			item.seller = element.attr("data-seller");
			item.thread = element.attr("data-thread");
			item.sellerid = element.attr("data-sellerid");
			item.buyout = element.attr("data-buyout");
			item.ign = element.attr("data-ign");
			item.league = element.attr("data-league");
			item.name = element.attr("data-name");
			

//			System.out.println(String.format("Now parsing item id %s name %s", item.id, item.name));
			
			Element sockElem = element.getElementsByClass("sockets-raw").get(0);
			item.socketsRaw = sockElem.text();
			
			item.ageAndHighLvl = element.getElementsByAttributeValue("title", "account age and highest level").get(0).text();
			
			// ----- Requirements ----- //
			Element reqElem = element.getElementsByClass("requirements").get(0);
			List<TextNode> reqNodes = reqElem.textNodes();
			for (TextNode reqNode : reqNodes) {
				// sample [ Level:&nbsp;37 ,  Strength:&nbsp;42 ,  Intelligence:&nbsp;42 ] 
				String req = reqNode.getWholeText();
				String separator = "&nbsp;";
				String reqType = trim(substringBefore(req, separator));
				switch(reqType) {
				case "Level":
					item.reqLvl = trim(substringAfter(req, separator)); 
					break;
				case "Strength":
					item.reqStr = trim(substringAfter(req, separator)); 
					break;
				case "Intelligence":
					item.reqInt = trim(substringAfter(req, separator)); 
					break;
				case "Dexterity":
					item.reqDex = trim(substringAfter(req, separator)); 
					break;
				}
			}

			// ----- Mods ----- //
			Element itemMods = element.getElementsByClass("item-mods").get(0);
			if (itemMods.getElementsByClass("bullet-item").size() != 0) {
				Element bulletItem = itemMods.getElementsByClass("bullet-item").get(0);
				Elements ulMods = bulletItem.getElementsByTag("ul");
				if (ulMods.size() == 2) {
					// implicit mod
					Element implicitLi = ulMods.get(1).getElementsByTag("li").get(0);
					Mod impMod = new Mod(implicitLi.attr("data-name"), implicitLi.attr("data-value"));
					item.implicitMod = impMod;
				}
				Elements modsLi = ulMods.get(0).getElementsByTag("li");
				for (Element modLi : modsLi) {
					// explicit mods
					Mod mod = new Mod(modLi.attr("data-name"), modLi.attr("data-value"));
					item.mods.add(mod);
				}
			}
			
			// ----- Properties ----- //
			// this is the third column data (the first col is the image, second is the mods, reqs)
			item.quality = element.getElementsByAttributeValue("data-name", "q").get(0).text();
			item.physDmgRangeAtMaxQuality = element.getElementsByAttributeValue("data-name", "quality_pd").get(0).text();
			item.eleDmgRange = element.getElementsByAttributeValue("data-name", "ed").get(0).text();
			item.attackSpeed = element.getElementsByAttributeValue("data-name", "aps").get(0).text();
			item.dmgAtMaxQuality = element.getElementsByAttributeValue("data-name", "quality_dps").get(0).text();
			item.physDmgAtMaxQuality = element.getElementsByAttributeValue("data-name", "quality_pdps").get(0).text();
			item.eleDmg = element.getElementsByAttributeValue("data-name", "edps").get(0).text();
			item.armourAtMaxQuality = element.getElementsByAttributeValue("data-name", "quality_armour ").get(0).text();
			item.evasionAtMaxQuality = element.getElementsByAttributeValue("data-name", "quality_evasion").get(0).text();
			item.energyShieldAtMaxQuality = element.getElementsByAttributeValue("data-name", "quality_shield ").get(0).text();
			item.block = element.getElementsByAttributeValue("data-name", "block ").get(0).text();
			item.crit = element.getElementsByAttributeValue("data-name", "crit").get(0).text();
			// "level"
			
//			System.out.println(item);
			searchResultItems.add(item);
		}
//		System.out.println("DONE --- Items");

		return searchResultItems;
	}

	/**
	 * Models one item in the search results
	 */
	public static class SearchResultItem {

		String id; // the id in the search result html page
		String seller;
		String thread;
		String sellerid;
		String buyout;
		String ign;
		String league;
		String name;

		String threadUrl;
		String reqLvl;
		String reqStr;
		String reqInt;
		String reqDex;

		String socketsRaw;
		String ageAndHighLvl;
		
		String quality;
		String physDmgRangeAtMaxQuality;
		String eleDmgRange;
		String attackSpeed;
		String dmgAtMaxQuality;
		String physDmgAtMaxQuality;
		String eleDmg;
		String armourAtMaxQuality;
		String evasionAtMaxQuality;
		String energyShieldAtMaxQuality;
		String block;
		String crit;

		Mod implicitMod;
		List<Mod> mods = new ArrayList<>();

		/**
		 * @author thirdy
		 *
		 */
		public static class Mod {
			String name;
			String value;

			public Mod(String name, String value) {
				this.name = name;
				this.value = value;
			}

			@Override
			public String toString() {
				return "Mod [name=" + name + ", value=" + value + "]";
			}
		}

		@Override
		public String toString() {
			return "SearchResultItem [id=" + id + ", seller=" + seller + ", thread=" + thread + ", sellerid=" + sellerid
					+ ", buyout=" + buyout + ", ign=" + ign + ", league=" + league + ", name=" + name + ", threadUrl="
					+ threadUrl + ", reqLvl=" + reqLvl + ", reqStr=" + reqStr + ", reqInt=" + reqInt + ", reqDex="
					+ reqDex + ", socketsRaw=" + socketsRaw + ", ageAndHighLvl=" + ageAndHighLvl + ", quality="
					+ quality + ", physDmgRangeAtMaxQuality=" + physDmgRangeAtMaxQuality + ", eleDmgRange="
					+ eleDmgRange + ", attackSpeed=" + attackSpeed + ", dmgAtMaxQuality=" + dmgAtMaxQuality
					+ ", physDmgAtMaxQuality=" + physDmgAtMaxQuality + ", eleDmg=" + eleDmg + ", armourAtMaxQuality="
					+ armourAtMaxQuality + ", evasionAtMaxQuality=" + evasionAtMaxQuality
					+ ", energyShieldAtMaxQuality=" + energyShieldAtMaxQuality + ", block=" + block + ", crit=" + crit
					+ ", implicitMod=" + implicitMod + ", mods=" + mods + "]";
		}
	}

}

package net.thirdy.blackmarket.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

public class BlackmarketLanguageParserInstance {
	
	Map<String, String> map = new LinkedHashMap<>();
	
	Map<String, String> dictionary = new HashMap<>();
	
	public BlackmarketLanguageParserInstance() {
		map.put("league", "Warbands");
		map.put("type", "");
		map.put("base", "");
		map.put("name", "");
		map.put("dmg_min", "");
		map.put("dmg_max", "");
		map.put("aps_min", "");
		map.put("aps_max", "");
		map.put("crit_min", "");
		map.put("crit_max", "");
		map.put("dps_min", "");
		map.put("dps_max", "");
		map.put("edps_min", "");
		map.put("edps_max", "");
		map.put("pdps_min", "");
		map.put("pdps_max", "");
		map.put("armour_min", "");
		map.put("armour_max", "");
		map.put("evasion_min", "");
		map.put("evasion_max", "");
		map.put("shield_min", "");
		map.put("shield_max", "");
		map.put("block_min", "");
		map.put("block_max", "");
		map.put("sockets_min", "");
		map.put("sockets_max", "");
		map.put("link_min", "");
		map.put("link_max", "");
		map.put("sockets_r", "");
		map.put("sockets_g", "");
		map.put("sockets_b", "");
		map.put("sockets_w", "");
		map.put("linked_r", "");
		map.put("linked_g", "");
		map.put("linked_b", "");
		map.put("linked_w", "");
		map.put("rlevel_min", "");
		map.put("rlevel_max", "");
		map.put("rstr_min", "");
		map.put("rstr_max", "");
		map.put("rdex_min", "");
		map.put("rdex_max", "");
		map.put("rint_min", "");
		map.put("rint_max", "");
		map.put("impl", "");
		map.put("impl_min", "");
		map.put("impl_max", "");
		map.put("mods", "");
		map.put("modexclude", "");
		map.put("modmin", "");
		map.put("modmax", "");
		map.put("mods", "");
		map.put("modexclude", "");
		map.put("modmin", "");
		map.put("modmax", "");
		map.put("mods", "");
		map.put("modexclude", "");
		map.put("modmin", "");
		map.put("modmax", "");
		map.put("q_min", "");
		map.put("q_max", "");
		map.put("level_min", "");
		map.put("level_max", "");
		map.put("mapq_min", "");
		map.put("mapq_max", "");
		map.put("rarity", "");
		map.put("seller", "");
		map.put("thread", "");
		map.put("time", "");
		map.put("corrupted", "");
		map.put("online", "x");
		map.put("buyout", "x");
		map.put("altart", "");
		map.put("capquality", "x");
		map.put("buyout_min", "");
		map.put("buyout_max", "");
		map.put("buyout_currency", "");
		map.put("crafted", "");
		map.put("identified", "");
		
		Date date = BlackmarketUtil.getDateInThePast(7);
		String dateStr = DateFormatUtils.format(date, "yyyy-MM-dd");
		map.put("time", dateStr);
		
		dictionary = BlackmarketUtil.loadLanguageDictionary();
	}



	public String parse(String input) {
		String[] tokens = StringUtils.split(input);
		String[] parsedTokens = new String[tokens.length];
		for (String token : tokens) {
			String result = dictionary.get(token);
			
			String key = StringUtils.substringBefore(result, "=");
			String value = StringUtils.substringAfter(result, "=");
			map.put(key, value);
		}
		
		List<String> lines = Lists.transform(new ArrayList<>(map.entrySet()), new Function<Entry<String, String>, String>() {

			@Override
			public String apply(Entry<String, String> input) {
				return input.getKey() + "=" + input.getValue();
			}

		});
		String finalResult = StringUtils.join(lines.toArray(), BlackmarketUtil.lineSep()); 
		return finalResult;
	}

	public static void main(String[] args) {
		BlackmarketLanguageParserInstance bm = new BlackmarketLanguageParserInstance();
		String s = bm.parse("ring");
		System.out.println(s);
	}
}

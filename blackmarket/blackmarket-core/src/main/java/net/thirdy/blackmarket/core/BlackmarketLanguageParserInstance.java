package net.thirdy.blackmarket.core;

import static net.thirdy.blackmarket.core.util.BlackmarketConfig.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import com.google.common.base.Strings;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;

import net.thirdy.blackmarket.core.util.BlackmarketConfig;
import net.thirdy.blackmarket.core.util.BlackmarketUtil;

public class BlackmarketLanguageParserInstance {
	
	Map<String, String> map = new LinkedHashMap<>();
	List<String> explicitModParams = new LinkedList<>();
	
	Map<String, String> dictionary = new HashMap<>();
	
	public BlackmarketLanguageParserInstance() {
		loadDefaultsToMap();
		
		Date date = BlackmarketUtil.getDateInThePast(properties().lastDaysSeen());
		String dateStr = DateFormatUtils.format(date, "yyyy-MM-dd");
		map.put("time", dateStr);
		
		dictionary = BlackmarketConfig.loadLanguageDictionary();
	}



	private void loadDefaultsToMap() {
		map.put("league", properties().league());
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
		// As of Sept 15, these explicit mod params are optional and non-position dependent
		// we'll save them in a List instead, see explicitModParams
//		map.put("mods", "");
//		map.put("modexclude", "");
//		map.put("modmin", "");
//		map.put("modmax", "");
//		map.put("mods", "");
//		map.put("modexclude", "");
//		map.put("modmin", "");
//		map.put("modmax", "");
//		map.put("mods", "");
//		map.put("modexclude", "");
//		map.put("modmin", "");
//		map.put("modmax", "");
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
		map.put("online", properties().onlineOnly() ? "x" : "");
		map.put("buyout", properties().buyoutOnly() ? "x" : "");
		map.put("altart", "");
		map.put("capquality", "x");
		map.put("buyout_min", properties().buyoutOnlyMin());
		map.put("buyout_max", "");
		map.put("buyout_currency", properties().buyoutOnlyCurrency());
		map.put("crafted", "");
		map.put("identified", "");
	}



	public String parse(String input) {
		List<String> tokens = Arrays.asList(StringUtils.split(input));
		
		// translate tokens using language dictionary
		Collection<String> dictionaryResults = Lists.transform(tokens, functionProcessToken());
		dictionaryResults = Collections2.filter(dictionaryResults, BlackmarketUtil.NON_BLANK_STRING);
		
		for (String result : dictionaryResults) {
			String[] resultItems = StringUtils.split(result, '&');
			for (String resultItem : resultItems) {
				String key = StringUtils.substringBefore(resultItem, "=");
				String value = StringUtils.substringAfter(resultItem, "=");
				
				if (isExplicitMod(resultItem)) {
					// we need to put these into list since these are repeating data
					explicitModParams.add(resultItem);
				} else {
					map.put(key, value);
				}
			}
		}
		
		String finalResult = buildFinalOutput(); 
		return finalResult;
	}



	private Function<String, String> functionProcessToken() {
		return new Function<String, String>() {

			@Override
			public String apply(String token) {
				return processToken(token);
			}
		};
	}



	String processToken(String token) {
		String result = null;
		
		for (Entry<String, String> entry : dictionary.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			
			// if token matches directly
			if (key.equalsIgnoreCase(token)) {
				result = value;
				break;
			}
			
			// if matches by regex
			Pattern pattern = Pattern.compile(key, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(token);
			if (matcher.matches()) {
				result = value;
				// replace placeholder with values captured from regex
				for (int i = 1; i <= matcher.groupCount(); i++) {
					result = result.replace("$GROUP" + i, matcher.group(i));
				}
			}
		}
		
		return result;
	}



	private boolean isExplicitMod(String result) {
		return StringUtils.containsIgnoreCase(result, "mod");
	}



	private String buildFinalOutput() {
		// Non explicit mod params
		List<String> lines0 = Lists.transform(new ArrayList<>(map.entrySet()), new Function<Entry<String, String>, String>() {

			@Override
			public String apply(Entry<String, String> input) {
				return input.getKey() + "=" + input.getValue();
			}

		});
		
		// explicit mods
		// code below should produce something like this:
		// mods=
		// modexclude=
		// modmin=
		// modmax=
		// mods=(pseudo) (total) +# to maximum Life
		// modexclude=
		// modmin=50
		// modmax=
		// mods=(pseudo) +#% total Elemental Resistance
		// modexclude=
		// modmin=90
		// modmax=
		List<String> lines1 = new LinkedList<>(lines0);
		for (String explicitModParam : explicitModParams) {
			
			String[] modParams = StringUtils.split(explicitModParam, "&");
			String explicitModParamGroup = StringUtils.join(modParams, BlackmarketUtil.lineSep());
			
			lines1.add(explicitModParamGroup);
		}
		
		// finalResult should look something like ring-life.txt
		String finalResult = StringUtils.join(lines1.toArray(), BlackmarketUtil.lineSep());
		return finalResult;
	}


}

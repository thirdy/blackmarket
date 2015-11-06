/*
 * Copyright (C) 2015 thirdy
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.thirdy.blackmarket.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.jexiletools.es.model.json.ExileToolsHit;

/**
 * @author thirdy
 *
 */
public class Ladder {

	// TODO: auto map from http://exiletools.com/status
	//    {darkshrine|Darkshrine (IC003)|http://www.pathofexile.com/forum/view-forum/597/page},
	//    {darkshrinehc|Darkshrine HC (IC004)|http://www.pathofexile.com/forum/view-forum/598/page},
	//    {hardcore|Hardcore|http://www.pathofexile.com/forum/view-forum/305/page},
	//    {standard|Standard|http://www.pathofexile.com/forum/view-forum/306/page}
	private static final ImmutableMap<String, String> LADDER_INDEXER_LEAGUE_MAPPING = ImmutableMap.of(
			"darkshrine", "Darkshrine (IC003)",
			"darkshrinehc", "Darkshrine HC (IC004)",
			"hardcore", "Hardcore",
			"standard", "Standard");
	
	/**
	 * Map of Leagues
	 *  -> Map of PlayerAccountName.IGN,LadderHits
	 */
	private Map<String, Map<String, LadderHit>> ladderMapAllLeagues;
	
	public Ladder() {
		ladderMapAllLeagues = new HashMap<>();
	}

	public void addLeagueLadder(String league, JsonObject jsonObject) {
		Map<String, LadderHit> m = jsonObject.entrySet().stream()
				.map(this::toLadderHit).collect(Collectors.toMap(LadderHit::key, e -> e));
		String searchLeague = ladderLeagueToSearchLeague(league);
		ladderMapAllLeagues.put(searchLeague, m);
	}
	
	private String ladderLeagueToSearchLeague(String league) {
		return LADDER_INDEXER_LEAGUE_MAPPING.get(league);
	}
	
	private LadderHit toLadderHit(Map.Entry<String, JsonElement> entry) {
		return new LadderHit(entry.getKey(), entry.getValue());
	}

	public int size() {
		return ladderMapAllLeagues.entrySet().stream()
				.map(es -> es.getValue().size())
				.collect(Collectors.summingInt(size -> size));
	}
	
	public String leagueSizes() {
		String leagueSizes = ladderMapAllLeagues.entrySet().stream()
			.map(es -> String.format("%s - %d", es.getKey(), es.getValue().size()))
			.collect(Collectors.joining(System.lineSeparator()));
		return "Online players per league:" + System.lineSeparator() + leagueSizes;
	}
	
	public void addPlayerLadderData(ExileToolsHit exileToolsHit) {
		String account = exileToolsHit.getShop().getSellerAccount();
		String league = exileToolsHit.getAttributes().getLeague();
		LadderHit ladderHit = ladderMapAllLeagues.get(league).get(account);
		if (ladderHit != null) {
			String sellerIGN = StringUtils.trimToNull(exileToolsHit.getShop().getSellerIGN());
			if (sellerIGN == null) {
				exileToolsHit.getShop().setSellerIGN(ladderHit.charName());
			}
			exileToolsHit.setLadderHit(Optional.of(ladderHit));
		}
	}
	
}

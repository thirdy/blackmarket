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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.jexiletools.es.model.League;
import io.jexiletools.es.model.json.ExileToolsHit;
import net.thirdy.blackmarket.Main;

/**
 * @author thirdy
 *
 */
public class Ladder {
	
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
		return League.LADDER_INDEXER_LEAGUE_MAPPING.get(league);
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
		if (!Main.DISABLE_LADDER_FEATURE) {
			String account = exileToolsHit.getShop().getSellerAccount();
			String league = exileToolsHit.getAttributes().getLeague();
			Map<String, LadderHit> leagueMap = ladderMapAllLeagues.get(league);
			Optional<LadderHit> ladderHit = leagueMap.entrySet().stream()
					.map(es -> es.getValue())
					.filter(lh -> lh.accountName().equalsIgnoreCase(account))
					.findFirst();
			if (ladderHit.isPresent()) {
				String sellerIGN = StringUtils.trimToNull(exileToolsHit.getShop().getSellerIGN());
				if (sellerIGN == null) {
					exileToolsHit.getShop().setSellerIGN(ladderHit.get().charName());
				}
				exileToolsHit.setLadderHit(ladderHit);
			}
		}
	}
	
}

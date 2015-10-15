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
package net.thirdy.blackmarket.domain;

import static com.google.common.collect.Iterables.toArray;
import static org.elasticsearch.index.query.FilterBuilders.andFilter;
import static org.elasticsearch.index.query.FilterBuilders.orFilter;
import static org.elasticsearch.index.query.FilterBuilders.termFilter;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import io.jexiletools.es.model.ItemType;
import io.jexiletools.es.model.json.Range;

/**
 * Immutable class that represents an instance of a Search.
 * 
 * @author thirdy
 *
 */
public class Search {
	
	private boolean advanceMode;
	private String advanceOptionJson;

	private Optional<String> name;
	private String league;
	private List<ItemType> itemTypes;

	private Optional<Range> dps;
	private Optional<Range> pDps;
	private Optional<Range> eDps;
	private Optional<Range> aps;
	private Optional<Range> critchance;
	
	public Search(boolean advanceMode, String advanceOptionJson, Optional<String> name, String league,
			List<ItemType> itemTypes, Optional<Range> dps, Optional<Range> pDps, Optional<Range> eDps,
			Optional<Range> aps, Optional<Range> critchance) {
		super();
		this.advanceMode = advanceMode;
		this.advanceOptionJson = advanceOptionJson;
		this.name = name;
		this.league = league;
		this.itemTypes = itemTypes;
		this.dps = dps;
		this.pDps = pDps;
		this.eDps = eDps;
		this.aps = aps;
		this.critchance = critchance;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Search [name=");
		builder.append(name);
		builder.append(", league=");
		builder.append(league);
		builder.append(", itemTypes=");
		builder.append(itemTypes);
		builder.append(", advanceMode=");
		builder.append(advanceMode);
		builder.append(", json=");
		builder.append(advanceOptionJson);
		builder.append("]");
		return builder.toString();
	}
	public void setAdvanceMode(boolean advanceMode) {
		this.advanceMode = advanceMode;
	}
	public boolean isAdvanceMode() {
		return advanceMode;
	}
	public String getAdvanceOptionJson() {
		return advanceOptionJson;
	}
	public void setAdvanceOptionJson(String json) {
		this.advanceOptionJson = json;
	}
}

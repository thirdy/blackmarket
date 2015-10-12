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

/**
 * Immutable class that represents an instance of a Search.
 * 
 * @author thirdy
 *
 */
public class Search {
	private Optional<String> name;
	private String league;
	private List<ItemType> itemTypes;
	private boolean advanceMode;
	private String advanceOptionJson;
	public Search(Optional<String> name, String league, List<ItemType> itemTypes) {
		super();
		this.name = name;
		this.league = league;
		this.itemTypes = itemTypes;
	}
	public Optional<String> getName() {
		return name;
	}
	public String getLeague() {
		return league;
	}
	public List<ItemType> getItemTypes() {
		return itemTypes;
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
	
	public String buildSearchJson() {
		String json = null;
		if (isAdvanceMode()) {
			json = getAdvanceOptionJson();
		} else {
			List<FilterBuilder> filters = searchToFilters();
			FilterBuilder filter = FilterBuilders.andFilter(toArray(filters, FilterBuilder.class));
			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
			searchSourceBuilder.query(QueryBuilders.filteredQuery(null, filter));
			searchSourceBuilder.size(100);
			json = searchSourceBuilder.toString();
		}
		return json;
	}
	
	private List<FilterBuilder> searchToFilters() {
		List<FilterBuilder> filters = new LinkedList<>();

		filters.add(termFilter("attributes.league", getLeague()));
		getName().map(s -> filters.add(termFilter("info.name", s)));
		

		if (!getItemTypes().isEmpty()) {
			List<FilterBuilder> itemTypeFilters = getItemTypes()
					.stream().map(it -> {
						FilterBuilder itFilter = termFilter("attributes.itemType", it.itemType());
						if (it.equipType() != null) {
							itFilter = andFilter(itFilter, termFilter("attributes.equipType", it.equipType()));
						}
						return itFilter;
					}).collect(Collectors.toList());
			if(!itemTypeFilters.isEmpty()) filters.add( orFilter(toArray(itemTypeFilters, FilterBuilder.class)) );
		}
		
		return filters;
	}
}

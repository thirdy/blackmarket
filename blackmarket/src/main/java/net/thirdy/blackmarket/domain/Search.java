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

import java.util.List;
import java.util.Optional;

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
		builder.append("]");
		return builder.toString();
	}
	
	
}

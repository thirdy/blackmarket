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
package io.jexiletools.es.model;

import java.util.Arrays;

/**
 * @author thirdy
 *
 */
public enum Rarity {

	blank("", ""),
	Unique("Unique", "#af6025"),
	Rare("Rare", "#ffff77"),
	Magic("Magic", "#8888ff"),
	Normal("Normal", "#c8c8c8"),
	Unknown("Unknown", "#000000"),
	Gem("Gem", "#1ba29b"),
	Currency("Currency", "#aa9e82"),
	Quest_Item("Quest Item", "#4ae63a") 
	;

	private String displayName;
	private String webColor;

	Rarity(String displayName, String webColor) {
		this.displayName = displayName;
		this.webColor = webColor;
	}

	public String displayName() {
		return displayName;
	}
	
	public String webColor() {
		return webColor;
	}

	@Override
	public String toString() {
		return displayName;
	}
	
	public static Rarity fromDisplayName(String displayName) {
		return Arrays.asList(values())
			.stream()
			.filter(e -> e.displayName.equalsIgnoreCase(displayName))
			.findFirst()
			.orElseGet(() -> Rarity.Unknown);
	}
}

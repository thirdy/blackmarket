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

import java.util.Date;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/**
 * @author thirdy
 *
 */
public class LadderHit {
	
	private String key;
	private JsonElement jsonElement;
	private String charName;
	private String accountName;

	public LadderHit(String key, JsonElement jsonElement) {
		this.key = key;
		String[] jsonKey = key.split("\\.");
		this.accountName = jsonKey[0];
		this.charName = jsonKey[1];
		this.jsonElement = jsonElement;
	}
	
	public String charName() {
		return charName;
	}
	
	public String accountName() {
		return accountName;
	}
	
	public JsonElement jsonElement() {
		return jsonElement;
	}

	public boolean online() {
		return jsonElement.getAsJsonObject().get("online").getAsInt() == 1;
	}
	
	public Date lastOnline() {
		return new Date(jsonElement.getAsJsonObject().get("lastOnline").getAsLong());
	}
	
	public String key() {
		return key;
	}

	@Override
	public String toString() {
		String json = new GsonBuilder().setPrettyPrinting().create()
				.toJson(jsonElement);
		return json;
	}
	
}

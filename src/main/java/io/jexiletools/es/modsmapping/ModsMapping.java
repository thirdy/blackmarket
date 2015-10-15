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
package io.jexiletools.es.modsmapping;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.gson.Gson;

import io.jexiletools.es.model.ItemType;

/**
 * @author thirdy
 *
 */
public final class ModsMapping {
	private static ModsMapping instance = new ModsMapping();
	private List<ModMapping> modMappings;
	public static ModsMapping getInstance() { return instance; }
	@SuppressWarnings("unchecked")
	private ModsMapping() {
		Gson gson = new Gson();
		Map<String, Object> m = null;
		try(InputStreamReader isr 
				= new InputStreamReader(ModsMapping.class.getResourceAsStream("/mods.json"))) {
			m = gson.fromJson(isr, Map.class);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		m = (Map<String, Object>) m.get("poe");
		m = (Map<String, Object>) m.get("mappings");
		m = (Map<String, Object>) m.get("item");

		modMappings = m.entrySet().stream().map(e -> toMapping(e))
			.collect(Collectors.toList());
		
		// also add pseudo mods
		modMappings.add(new ModMapping("eleResistSumChaos", "modsPseudo.eleResistSumChaos", "[pseudo] eleResistSumChaos", Type.DOUBLE, ModType.PSEUDO, ItemType.Unknown));
		modMappings.add(new ModMapping("eleResistSumCold", "modsPseudo.eleResistSumCold", "[pseudo] eleResistSumCold", Type.DOUBLE, ModType.PSEUDO, ItemType.Unknown));
		modMappings.add(new ModMapping("eleResistSumFire", "modsPseudo.eleResistSumFire", "[pseudo] eleResistSumFire", Type.DOUBLE, ModType.PSEUDO, ItemType.Unknown));
		modMappings.add(new ModMapping("eleResistSumLightning", "modsPseudo.eleResistSumLightning", "[pseudo] eleResistSumLightning", Type.DOUBLE, ModType.PSEUDO, ItemType.Unknown));
		modMappings.add(new ModMapping("eleResistTotal", "modsPseudo.eleResistTotal", "[pseudo] eleResistTotal", Type.DOUBLE, ModType.PSEUDO, ItemType.Unknown));
		modMappings.add(new ModMapping("flatAttributesTotal", "modsPseudo.flatAttributesTotal", "[pseudo] flatAttributesTotal", Type.DOUBLE, ModType.PSEUDO, ItemType.Unknown));
		modMappings.add(new ModMapping("flatSumDex", "modsPseudo.flatSumDex", "[pseudo] flatSumDex", Type.DOUBLE, ModType.PSEUDO, ItemType.Unknown));
		modMappings.add(new ModMapping("flatSumInt", "modsPseudo.flatSumInt", "[pseudo] flatSumInt", Type.DOUBLE, ModType.PSEUDO, ItemType.Unknown));
		modMappings.add(new ModMapping("flatSumStr", "modsPseudo.flatSumStr", "[pseudo] flatSumStr", Type.DOUBLE, ModType.PSEUDO, ItemType.Unknown));
		modMappings.add(new ModMapping("maxLife", "modsPseudo.maxLife", "[pseudo] maxLife", Type.DOUBLE, ModType.PSEUDO, ItemType.Unknown));
 
	}
	public List<ModMapping> getModMappings() {
		return modMappings;
	}
	@SuppressWarnings("unchecked")
	private ModMapping toMapping(Entry<String, Object> e) {
		ModMapping mapping = new ModMapping();
		
		Map<String, Object> itemMap = (Map<String, Object>) e.getValue();
		Map<String, Object> mappingMap = (Map<String, Object>) itemMap.get("mapping");

		// Note that key is equal to fullName
		mapping.key = e.getKey();
		mapping.fullName = (String) itemMap.get("full_name");
		mapping.mapping = mappingMap.keySet().iterator().next();
		String typeStr 	= (String) ((Map) mappingMap.get(mapping.mapping)).get("type");
		mapping.type = Type.valueOf(typeStr.toUpperCase());
		mapping.parse();
		return mapping;
	}
	
	public enum Type {
		DOUBLE, BOOLEAN, DOUBLE_MIN, DOUBLE_MAX;
	}
	
	public enum ModType {
		IMPLICIT, EXPLICIT, CRAFTED, COSMETIC, PSEUDO;
	}
	
	public static class ModMapping {
		String key;
		String fullName;
		String mapping;
		Type type;
		
		ModType modType;
		ItemType itemType;
		
		public ModMapping() {
		}
		void parse() {
			// mods.Boots.explicit.Adds #-# Cold Damage to Spells.min
			String[] tokens = key.split("\\.");
			if(tokens.length < 4 || tokens.length > 5) throw new IllegalStateException("Mod key is not 4 or 5 tokens. Indexer mapping has changed?");
			
			itemType = ItemType.fromItemType(tokens[1]);
			modType = ModType.valueOf(tokens[2].toUpperCase());
			
			if (tokens.length == 5) {
				type = tokens[4].equalsIgnoreCase("min") ? Type.DOUBLE_MIN : Type.DOUBLE_MAX;
				mapping = tokens[3];
			}
		}
		
		@Override
		public String toString() {
			return modType == ModType.PSEUDO ?
					mapping
					: itemType.displayName() + " " + mapping;
		}
		
		public String getKey() {
			return key;
		}
		public String getMapping() {
			return mapping;
		}

		public Type getType() {
			return type;
		}

		public ModType getModType() {
			return modType;
		}

		public ItemType getItemType() {
			return itemType;
		}

		public ModMapping(String key, String fullName, String mapping, Type type, ModType modType, ItemType itemType) {
			this.key = key;
			this.fullName = fullName;
			this.mapping = mapping;
			this.type = type;
			this.modType = modType;
			this.itemType = itemType;
		}
	}
	
	public static void main(String[] args) {
		ModsMapping.getInstance();
	}
	
	public List<ModMapping> listByModType(ModType modType) {
		return getModMappings()
				.stream()
				.filter(m -> m.modType == modType)
				.sorted((m1, m2) -> m1.toString().compareTo(m2.toString()))
				.collect(Collectors.toList());
	}
}

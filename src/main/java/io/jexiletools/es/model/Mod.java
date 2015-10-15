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

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import io.jexiletools.es.model.json.Range;

/**
 * @author thirdy
 *
 */
public class Mod {
	
	String name;
	
	boolean isValueRanged; // true for mods like Adds #-# Lightning Damage to Attacks
	boolean isValueBoolean;
	
	boolean isCrafted;
	
	Double value;
	Range range;
	
	public String toDisplay() {
		String result = name;
		if (isValueRanged) {
			String minStr = String.valueOf(range.getMin().intValue());
			String maxStr = String.valueOf(range.getMax().intValue());
			result = StringUtils.replaceOnce(result, "#", minStr);
			result = StringUtils.replaceOnce(result, "#", maxStr);
		} else if (isValueBoolean) {
			// mod name is good enough
		} else {
			String valueStr = String.valueOf(value.intValue());
			result = StringUtils.replaceOnce(result, "#", valueStr);
		}
		return result;
	}
	
	public static Mod fromRaw(String name, Object value) {
		return fromRaw(name, value, false);
	}
	public static Mod fromRaw(String name, Object value, boolean isCrafted) {
		Mod mod = new Mod();
		mod.setCrafted(isCrafted);
		mod.setName(name);
		if (Double.class.isInstance(value)) {
			mod.setValue((Double)value);
		} else if (Boolean.class.isInstance(value)) {
			mod.setValueBoolean(true);
		} else if (Map.class.isInstance(value)) {
			mod.setValueRanged(true);
			@SuppressWarnings("unchecked")
			Map<String, Double> m =  (Map<String, Double>) value;
			Range r = new Range(m);
			mod.setRange(r);
		}
		return mod;
	}

	public boolean isCrafted() {
		return isCrafted;
	}

	public void setCrafted(boolean isCrafted) {
		this.isCrafted = isCrafted;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isValueRanged() {
		return isValueRanged;
	}

	public void setValueRanged(boolean isValueRanged) {
		this.isValueRanged = isValueRanged;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	public Range getRange() {
		return range;
	}

	public void setRange(Range range) {
		this.range = range;
	}

	public boolean isValueBoolean() {
		return isValueBoolean;
	}

	public void setValueBoolean(boolean isValueBoolean) {
		this.isValueBoolean = isValueBoolean;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Mod [name=");
		builder.append(name);
		builder.append(", isValueRanged=");
		builder.append(isValueRanged);
		builder.append(", isValueBoolean=");
		builder.append(isValueBoolean);
		builder.append(", value=");
		builder.append(value);
		builder.append(", range=");
		builder.append(range);
		builder.append("]");
		return builder.toString();
	}

	
	
	
}

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
package net.thirdy.blackmarket.fxcontrols;

import java.util.Optional;

import org.elasticsearch.common.lang3.StringUtils;

import javafx.scene.layout.HBox;

/**
 * @author thirdy
 *
 */
public class FourColorIntegerTextField extends HBox implements Clearable {
	IntegerTextField r = new IntegerTextField("R");
	IntegerTextField g = new IntegerTextField("G");
	IntegerTextField b = new IntegerTextField("B");
	IntegerTextField w = new IntegerTextField("W");
	public FourColorIntegerTextField() {
		getChildren().addAll(r, g, b, w);
	}
	
	public Optional<String> val() {
		StringBuilder sb = new StringBuilder();
		// must be sorted alphabetically
		b.getOptionalValue().ifPresent(i -> mul(sb, i, 'b'));
		g.getOptionalValue().ifPresent(i -> mul(sb, i, 'g'));
		r.getOptionalValue().ifPresent(i -> mul(sb, i, 'r'));
		w.getOptionalValue().ifPresent(i -> mul(sb, i, 'w'));
		return Optional.ofNullable(StringUtils.trimToNull(sb.toString()));
	} 
	
	private void mul(StringBuilder sb, int times, char c) {
		for (int i = 0; i < times; i++) {
			sb.append(c);
		}
	}
	
	public Optional<Integer> red() {
		return r.getOptionalValue();
	}
	
	public Optional<Integer> green() {
		return g.getOptionalValue();
	}
	
	public Optional<Integer> blue() {
		return b.getOptionalValue();
	}
	
	public Optional<Integer> white() {
		return w.getOptionalValue();
	}

	@Override
	public void clear() {
		r.clear();
		g.clear();
		b.clear();
		w.clear();
	}
}

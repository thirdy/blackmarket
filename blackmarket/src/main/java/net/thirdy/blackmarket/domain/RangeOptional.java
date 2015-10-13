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

import java.util.Optional;

import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.RangeFilterBuilder;

/**
 * @author thirdy
 *
 */
public class RangeOptional {
	public Optional<Double> min, max;

	public RangeOptional(Optional<Double> min, Optional<Double> max) {
		super();
		this.min = min;
		this.max = max;
	}

	public FilterBuilder rangeFilter(String name) {
		RangeFilterBuilder rangeFilter = FilterBuilders.rangeFilter(name);
		min.ifPresent(m -> rangeFilter.from(m));
		max.ifPresent(m -> rangeFilter.to(m));
		return rangeFilter;
	}
	
	
}

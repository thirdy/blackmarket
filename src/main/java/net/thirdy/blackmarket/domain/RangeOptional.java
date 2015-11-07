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
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeFilterBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;

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
		FilterBuilder filter = null;
		
		if(max.isPresent() && max.get() == 0) {
			filter = FilterBuilders.notFilter(FilterBuilders.existsFilter(name));
		} else {
			RangeFilterBuilder rfilter = FilterBuilders.rangeFilter(name);
			min.ifPresent(m -> rfilter.from(m));
			max.ifPresent(m -> rfilter.to(m));
			filter = rfilter;
		}
		
		return filter;
	}
	
	public QueryBuilder rangeQuery(String name) {
		QueryBuilder query = null;
		
		RangeQueryBuilder rquery = QueryBuilders.rangeQuery(name);
		min.ifPresent(m -> rquery.from(m));
		max.ifPresent(m -> rquery.to(m));
		query = rquery;
		
		return query;
	}

	public static final RangeOptional MIN_ZERO = new RangeOptional(Optional.of(0.0), Optional.empty());
}

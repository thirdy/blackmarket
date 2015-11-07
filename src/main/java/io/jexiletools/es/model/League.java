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
import java.util.List;

import com.google.common.collect.ImmutableMap;

/**
 * @author thirdy
 *
 */
public class League {

	public static List<String> names() {
		return Arrays.asList("Darkshrine (IC003)", "Darkshrine HC (IC004)", "Standard", "Hardcore");
	}

	// TODO: auto map from http://exiletools.com/status
	//    {darkshrine|Darkshrine (IC003)|http://www.pathofexile.com/forum/view-forum/597/page},
	//    {darkshrinehc|Darkshrine HC (IC004)|http://www.pathofexile.com/forum/view-forum/598/page},
	//    {hardcore|Hardcore|http://www.pathofexile.com/forum/view-forum/305/page},
	//    {standard|Standard|http://www.pathofexile.com/forum/view-forum/306/page}
	public static final ImmutableMap<String, String> LADDER_INDEXER_LEAGUE_MAPPING = ImmutableMap.of(
			"darkshrine", "Darkshrine (IC003)",
			"darkshrinehc", "Darkshrine HC (IC004)",
			"hardcore", "Hardcore",
			"standard", "Standard");
}

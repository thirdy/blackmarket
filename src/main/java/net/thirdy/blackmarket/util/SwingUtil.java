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
package net.thirdy.blackmarket.util;

import java.awt.Desktop;
import java.net.URI;

import net.thirdy.blackmarket.ex.BlackmarketException;

/**
 * @author thirdy
 *
 */
public class SwingUtil {
	public static void openUrlViaBrowser(String url) throws BlackmarketException {
		String s = url;
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop.getDesktop().browse(new URI(s));
			} catch (Exception e) {
				throw new BlackmarketException(
						"Error on opening browser, address: " + s + ": " + e.getMessage(), e
							);
			}
		} else {
			throw new BlackmarketException("Launch browser failed, please manually visit: " + s);
		}
	}
}

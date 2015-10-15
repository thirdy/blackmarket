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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Predicate;

import net.thirdy.blackmarket.ex.BlackmarketRuntimeException;

public class BlackmarketUtil {

	public static final Predicate<String> NON_BLANK_STRING = new Predicate<String>() {

		@Override
		public boolean apply(String input) {
			return StringUtils.isNotBlank(input);
		}
	};

	public static String loadFromClassPath(Class<?> clazz, String classPath) {
		System.out.println("loadFromClassPath: " + classPath);
		String page = "";
		try {
			InputStream is = clazz.getResourceAsStream(classPath);
			page = IOUtils.toString(is);
		} catch (IOException e) {
			System.out.println("Exception" + e.getMessage());
			// won't likely happen since file is in classpath
			e.printStackTrace();
		}
		return page;
	}
	
	public static String readFileToString(File file) {
		String raw = "";
		try {
			raw = FileUtils.readFileToString(file, "UTF-8");
		} catch (IOException e) {
			throw new BlackmarketRuntimeException(e);
		}
		return raw;
	}

	public static File loadOrCreateDirectory(File file) {
		if (!file.exists()) {
			file.mkdir();
		}
		return file;
	}
	
	public static File loadOrCreateFile(File file) {
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				throw new BlackmarketRuntimeException(e);
			}
		}
		return file;
	}
	
	public static Date getDateInThePast(int days) {
		Date dateNow = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateNow);
		cal.add(Calendar.DATE, -days);
		Date d = cal.getTime();
		return d;
	}

	public static String lineSep() {
		return System.getProperty("line.separator");
	}

	public static String homeDirectory() {
		return System.getProperty("user.home");
	}

}

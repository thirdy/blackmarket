package net.thirdy.blackmarket.core;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import net.thirdy.blackmarket.core.ex.BlackmarketRuntimeException;

public class BlackmarketUtil {

	
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

	public static Date getDateInThePast(int days) {
		Date dateNow = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(dateNow);
		cal.add(Calendar.DATE, - days);
		Date d = cal.getTime();
		return d;
	}

	public static Map<String, String> loadLanguageDictionary() {
		Map<String, String> map = new HashMap<>();
		File home = loadBmDir();
		File file = loadBmLangConfig(home);
		String raw = "";
		try {
			raw = FileUtils.readFileToString(file, "UTF-8");
		} catch (IOException e) {
			throw new BlackmarketRuntimeException(e);
		}
		if (StringUtils.isNotBlank(raw)) {
			String[] lines = StringUtils.split(raw, lineSep());
			for (String line : lines) {
				if (isLineNotComment(line) && StringUtils.isNotBlank(line)) {
					// substringBefore/substringAfter is first occurance
					String key = StringUtils.substringBefore(line, "=");
					String value = StringUtils.substringAfter(line, "=");
					key = StringUtils.trim(key);
					value = StringUtils.trim(value);
					map.put(key, value);
				}
			}
		}
		return map;
	}

	private static File loadBmLangConfig(File home) {
		File file = new File(home, "blackmarket.language");
		if (!file.exists()) {
			try {
				file.createNewFile();
				FileUtils.write(file, getDefaultLanguageConfig());
			} catch (IOException e) {
				throw new BlackmarketRuntimeException(e);
			}
		}
		return file;
	}

	private static String getDefaultLanguageConfig() {
		return loadFromClassPath(BlackmarketUtil.class, "/default.language");
	}

	private static File loadBmDir() {
		String home = System.getProperty("user.home");
		File userHome = new File(home);
		File bm = new File(userHome, ".blackmarket");
		if (!bm.exists()) {
			bm.mkdir();
		}
		return bm;
	}

	public static String lineSep() {
		return System.getProperty("line.separator");
	}

	private static boolean isLineNotComment(String line) {
		return !StringUtils.startsWith(line, "//") && !StringUtils.startsWith(line, ";");
	}
}

package net.thirdy.blackmarket.core.util;

import static net.thirdy.blackmarket.core.util.BlackmarketUtil.homeDirectory;
import static net.thirdy.blackmarket.core.util.BlackmarketUtil.loadOrCreateDirectory;
import static net.thirdy.blackmarket.core.util.BlackmarketUtil.loadOrCreateFile;
import static net.thirdy.blackmarket.core.util.BlackmarketUtil.readFileToString;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import net.thirdy.blackmarket.core.ex.BlackmarketRuntimeException;

public class BlackmarketConfig {

	public static final String VERSION = "v0.2-pre-alpha";
	public static final boolean TESTING_MODE = false;
	public static final boolean DEVELOPMENT_MODE = true;
	public static final String TITLE = "Blackmarket " + VERSION + (DEVELOPMENT_MODE ? " - DEVELOPMENT-MODE-ENABLED" : "");
	
	private static BlackmarketProperties blackmarketProperties;
	
	public static BlackmarketProperties properties() { return blackmarketProperties; }

	public static Map<String, String> loadLanguageDictionary() {
		Map<String, String> map = new HashMap<>();
		String raw = readFileToString(languageConfigFile());
		if (StringUtils.isNotBlank(raw)) {
			String[] lines = StringUtils.split(raw, BlackmarketUtil.lineSep());
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

	static File configDirectory() {
		File file = loadOrCreateDirectory(new File(homeDirectory(), ".blackmarket"));
		return file;
	}

	static File languageConfigFile() {
		return new File(configDirectory(), "blackmarket.language");
	}
	
	static File propertiesConfigFile() {
		return new File(configDirectory(), "blackmarket.properties");
	}
	
	public static void setupConfigFiles() {
		File propertiesFile = loadOrCreateFile(propertiesConfigFile());
		blackmarketProperties = new BlackmarketProperties(propertiesFile);
		File languageConfig = loadOrCreateFile(languageConfigFile());
		// if development mode, we always overwrite the config files
		if (blackmarketProperties.isNewInstall() || DEVELOPMENT_MODE) {
			System.out.println("New install detected, creating/overwriting config files");
			try {
				FileUtils.deleteQuietly(languageConfig);
				FileUtils.deleteQuietly(propertiesFile);
				FileUtils.writeStringToFile(languageConfig, getDefaultLanguageConfig());
				FileUtils.writeStringToFile(propertiesFile, getDefaultPropertiesConfig());
				// if nothing goes wrong with setup of files, new install is successful
				blackmarketProperties = new BlackmarketProperties(propertiesConfigFile());
			} catch (IOException e) {
				throw new BlackmarketRuntimeException(e);
			}
		}
		System.out.println("---Properties----");
		System.out.println(blackmarketProperties.toString());
	}

	static String getDefaultLanguageConfig() {
		return BlackmarketUtil.loadFromClassPath(BlackmarketConfig.class, "/default.language");
	}
	
	static String getDefaultPropertiesConfig() {
		return BlackmarketUtil.loadFromClassPath(BlackmarketConfig.class, "/default.properties");
	}

	static boolean isLineNotComment(String line) {
		return !StringUtils.startsWith(line, "//") && !StringUtils.startsWith(line, ";");
	}
}

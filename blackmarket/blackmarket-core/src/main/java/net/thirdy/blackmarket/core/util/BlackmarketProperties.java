/**
 * 
 */
package net.thirdy.blackmarket.core.util;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import net.thirdy.blackmarket.core.ex.BlackmarketRuntimeException;

/**
 *
 * @author thirdy
 *
 */
public class BlackmarketProperties extends Properties {

	private static final String KEY_LEAGUE = "league";
	private static final String KEY_VERSION = "version";
	private static final long serialVersionUID = 1L;
	private File propertiesFile;

	public BlackmarketProperties(File propertiesFile) {
		this.propertiesFile = propertiesFile;
		try {
			// note that Properties.load does not close the input stream
			FileInputStream is = new FileInputStream(propertiesFile);
			load(is);
			is.close();
		} catch (FileNotFoundException e) {
			throw new BlackmarketRuntimeException(e);
		} catch (IOException e) {
			throw new BlackmarketRuntimeException(e);
		}
	}

	public boolean isNewInstall() {
		String version = getProperty(KEY_VERSION, "");
		return !BlackmarketConfig.VERSION.equalsIgnoreCase(version);
	}
	
	public String league() {
		return getProperty(KEY_LEAGUE, "Standard");
	}

	public int lastDaysSeen() {
		return Integer.parseInt(getProperty("lastDaysSeen", "7"));
	}

	public boolean onlineOnly() {
		return Boolean.parseBoolean(getProperty("onlineOnly", "false").toString());
	}
	
	public boolean buyoutOnly() {
		return Boolean.parseBoolean(getProperty("buyoutOnly", "false").toString());
	}

	public String blankSearch() {
		return getProperty("blankSearch", "boots 4L 70life 50res");
	}

	public String lookAndFeel() {
		return getProperty("lookAndFeel", "org.pushingpixels.substance.api.skin.SubstanceModerateLookAndFeel");
	}

	public Color highlightColor() {
		String colorRaw = getProperty("highlightColor", "");
		if (!colorRaw.isEmpty()) {
			String[] rgb = StringUtils.split(colorRaw, ',');
			int r = Integer.parseInt(rgb[0]);
			int g = Integer.parseInt(rgb[1]);
			int b = Integer.parseInt(rgb[2]);
			return new Color(r, g, b);
		}
		return null;
	}

	public String buyoutOnlyCurrency() {
		return getProperty("buyoutOnlyCurrency", "");
	}

	public String buyoutOnlyMin() {
		return getProperty("buyoutOnlyMin", "1");
	}

	public boolean alwaysSortByBuyout() {
		return Boolean.parseBoolean(getProperty("alwaysSortByBuyout", "true").toString());
	}

	public String[] leagues() {
		String rawLeagues = getProperty("leagues", "Standard,Hardcore");
		String[] leagues = StringUtils.split(rawLeagues, ',');
		return leagues;
	}

	public void setLeague(String league) {
		setProperty(KEY_LEAGUE, league);
	}

	public void save() {
		// https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
		try(FileOutputStream fos = new FileOutputStream(propertiesFile)) {
			super.store(fos, "BlackmarketProperties.store()");
		} catch (IOException e) {
			throw new BlackmarketRuntimeException(e);
		};
	}
}

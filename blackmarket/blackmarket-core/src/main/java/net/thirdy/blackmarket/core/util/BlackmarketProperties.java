/**
 * 
 */
package net.thirdy.blackmarket.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import net.thirdy.blackmarket.core.ex.BlackmarketRuntimeException;

/**
 *
 * @author thirdy
 *
 */
public class BlackmarketProperties extends Properties {

	private static final String OPTION_EITHER = "either";
	private static final String KEY_VERSION = "version";
	private static final long serialVersionUID = 1L;

	public BlackmarketProperties(File propertiesFile) {
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
		return getProperty("league", "Standard");
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

//	public void updateVersion(String newVersion) {
//		setProperty(KEY_VERSION, newVersion);
//	}

//	public void storeToFile(File propertiesFile) {
//		// https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
//		try(FileOutputStream fos = new FileOutputStream(propertiesFile)) {
//			super.store(fos, "BlackmarketProperties.store()");
//		} catch (IOException e) {
//			throw new BlackmarketRuntimeException(e);
//		};
//	}
}

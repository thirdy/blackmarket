package net.thirdy.blackmarket;

import java.awt.GraphicsConfiguration;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class AppConfig {

	public static final String VERSION = "PROTOTYPE 2";
	public static final boolean TESTING_MODE = false;
	public static final String TITLE = "Blackmarket " + VERSION;
	public static String getAboutMessage() {
		return "<html><a href='http://google.com'>google</a></html>";
	}
	

}

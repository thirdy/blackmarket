package net.thirdy.blackmarket;


import javax.swing.SwingUtilities;

import net.thirdy.blackmarket.core.BackendClient;
import net.thirdy.blackmarket.core.util.BlackmarketConfig;
import net.thirdy.blackmarket.swing.BlackmarketJFrame;


public class MainApp {
	
	// TODO, is this okay to be a single instance?
	private static BackendClient poeTradeHttpClient = new BackendClient();
	public static BackendClient getPoeTradeHttpClient() {
		return poeTradeHttpClient;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				BlackmarketConfig.setupConfigFiles();
				new BlackmarketJFrame();
			}
		});
	}

}

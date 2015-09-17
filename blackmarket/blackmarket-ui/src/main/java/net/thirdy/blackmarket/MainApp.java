package net.thirdy.blackmarket;


import javax.swing.SwingUtilities;

import net.thirdy.blackmarket.core.PoeTradeHttpClient;
import net.thirdy.blackmarket.swing.BlackmarketJFrame;


public class MainApp {
	
	// TODO, is this okay to be a single instance?
	private static PoeTradeHttpClient poeTradeHttpClient = new PoeTradeHttpClient();
	public static PoeTradeHttpClient getPoeTradeHttpClient() {
		return poeTradeHttpClient;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new BlackmarketJFrame();
			}
		});
	}

}

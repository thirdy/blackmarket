package net.thirdy.blackmarket;


import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

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
		JFrame.setDefaultLookAndFeelDecorated(true);
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				BlackmarketConfig.setupConfigFiles();
				try {
					UIManager.setLookAndFeel(BlackmarketConfig.properties().lookAndFeel());
				} catch (Exception e) {
					System.out.println("Substance Look and Feel failed to initialize");
					JOptionPane.showMessageDialog(null, "Substance Look and Feel failed to initialize: " + e.getMessage());
				}
				
				new BlackmarketJFrame();
			}
		});
	}

}

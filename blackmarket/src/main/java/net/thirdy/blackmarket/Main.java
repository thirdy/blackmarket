package net.thirdy.blackmarket;


import io.jexiletools.es.ExileToolsESClient;
import javafx.application.Application;


public class Main {
	
	private static ExileToolsESClient exileToolESClient = new ExileToolsESClient();
	
	
	public static ExileToolsESClient getExileToolsESClient() {
		return exileToolESClient;
	}

	public static void main(String[] args) {
        Application.launch(BlackmarketApplication.class, args);
    }

}

package net.thirdy.blackmarket.service;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import net.thirdy.blackmarket.Main;
import net.thirdy.blackmarket.util.UrlReaderUtil;

public class ExileToolsLastIndexUpdateService extends Service<Void> {
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private StringProperty countdown = new SimpleStringProperty(this, "countdown", "Indexer Last Update: ");
    public final void setCountdown(String value) { countdown.set(value); }
    public final String getCountdown() { return countdown.get(); }
    public final StringProperty countdownProperty() { return countdown; }
    
	@Override
    protected Task<Void> createTask() {
        return new Task<Void>() {  
        	
            @Override protected Void call() throws Exception {
				String lastUpdate = retrieveIndexerLastUpdate();
				if (!Main.DEBUG_MODE) {
					// annoying, disable if on development
					logger.debug("Last Indexer Update Value: " + lastUpdate);
				}
				updateMessage(lastUpdate);

				for (int i = 60; i >= 0; i--) {
					Thread.sleep(1000);
					int _i = i;
					Platform.runLater(() -> setCountdown("(" + _i + ")" + " Indexer Last Update: "));
				}
            	return null;
            }
        };
	}
	
	private String retrieveIndexerLastUpdate() {
		String result = "";
		String url = "http://exiletools.com/last-index-update.html";
		try {
			result += StringUtils.trimToEmpty(UrlReaderUtil.getString(url));
		} catch (Exception e1) {
			result += String.format("Error downloading indexer status from %s, error: %s", url, e1.getMessage());
		}
		return result;
	}
}

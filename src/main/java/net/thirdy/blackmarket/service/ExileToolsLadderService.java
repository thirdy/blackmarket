package net.thirdy.blackmarket.service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mashape.unirest.http.exceptions.UnirestException;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import net.thirdy.blackmarket.Main;
import net.thirdy.blackmarket.controls.Dialogs;
import net.thirdy.blackmarket.ex.BlackmarketException;
import net.thirdy.blackmarket.util.UrlReaderUtil;

public class ExileToolsLadderService extends Service<Void> {
	
	private static final String LADDER_API_URL = "http://api.exiletools.com/ladder";

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private ObjectProperty<Ladder> result = new SimpleObjectProperty<>();
    public final ObjectProperty<Ladder> resultProperty() { return result; }
    
    private BooleanProperty sleeping = new SimpleBooleanProperty();
    public final BooleanProperty sleepingProperty() {return sleeping;}
    
    public ExileToolsLadderService() {
		setOnSucceeded(e -> restart());
		setOnFailed	 (e -> {
			getException().printStackTrace();
			Dialogs.showExceptionDialog(getException());
			restart();
		});
	}
    
	@Override
    protected Task<Void> createTask() {
        return new Task<Void>() {  
        	
			@Override protected Void call() throws Exception {
            	try {
            		sleeping.set(false);
            		updateMessage("Downloading player ladder data...");
            		List<String> leagues = Main.DISABLE_LADDER_FEATURE ? Collections.emptyList() : retriveActiveLeagues();
            		Ladder ladder = new Ladder();
            		for (String league : leagues) {
            		  updateMessage("Downloading player ladder data for " + league);
            		  JsonObject jsonObject = retrieveAllOnline(league);
            		  ladder.addLeagueLadder(league, jsonObject);
					}
					Platform.runLater(() -> result.setValue(ladder));
					// TODO: we can add a online count per league via http://api.exiletools.com/ladder?listleagues=1
					updateMessage(ladder.size() + " online players in ladder for all leagues");
					int fiveMins = 60 * 5;
					for (int i = fiveMins; i >= 0; i--) {
						sleeping.set(true);
						Thread.sleep(1000);
						updateMessage(ladder.size() + " online players in ladder for all leagues (" + i + " secs ago)");
					}
            	} catch (JsonSyntaxException | BlackmarketException e) {
            		int countdownToRetry = 3;
            		for (int i = countdownToRetry; i > 0; i--) {
            			String err = String.format("Error: %s. Gonna try again in %d secs..", e.getMessage(), i);
            			updateMessage(err);
            			Thread.sleep(1000);
					}
				}
            	return null;
            }
        };
	}

	private List<String> retriveActiveLeagues() throws BlackmarketException {
		List<String> activeLeagues = Collections.emptyList();
		try {
			String s = UrlReaderUtil.getString(LADDER_API_URL, ImmutableMap.of("activeleagues", "1"));
			activeLeagues = new JsonParser()
					.parse(s).getAsJsonObject().entrySet().stream()
					.map(entrySet -> entrySet.getValue().getAsString())
					.collect(Collectors.toList());
		} catch (UnirestException e) {
			throw new BlackmarketException("Problem while downloading Ladder data - active leagues", e);
		}
		return activeLeagues;
	}
	
	private JsonObject retrieveAllOnline(String league) throws BlackmarketException {
		String s = null;
		try {
			s = UrlReaderUtil.getString(LADDER_API_URL, ImmutableMap.of("showAllOnline", "1", "league", league));
		} catch (UnirestException e) {
			throw new BlackmarketException("Problem while downloading Ladder data for league " + league, e);
		}
		String result = StringUtils.trimToEmpty(s);
		JsonObject jsonContainer = new JsonParser().parse(result).getAsJsonObject();
		return jsonContainer;
	}

}

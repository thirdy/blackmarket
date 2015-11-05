package net.thirdy.blackmarket.service;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import net.thirdy.blackmarket.util.UrlReaderUtil;

public class ExileToolsLadderService extends Service<Void> {
	
	private static final String EXILE_TOOLS_LADDER_API_URL = "http://api.exiletools.com/ladder?showAllOnline=1";

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private ObjectProperty<Ladder> result = new SimpleObjectProperty<>();
    public final ObjectProperty<Ladder> resultProperty() { return result; }
    
    private BooleanProperty sleeping = new SimpleBooleanProperty();
    public final BooleanProperty sleepingProperty() {return sleeping;}
    
    public ExileToolsLadderService() {
		setOnSucceeded(e -> restart());
		setOnFailed	 (e -> restart());
	}
    
	@Override
    protected Task<Void> createTask() {
        return new Task<Void>() {  
        	
			@Override protected Void call() throws Exception {
            	try {
            		sleeping.set(false);
					Ladder ladder = retrieveAllOnline();
					result.setValue(ladder);
					// TODO: we can add a online count per league via http://api.exiletools.com/ladder?listleagues=1
					updateMessage(ladder.size() + " online players in ladder for all leagues");
					int fiveMins = 60 * 5;
					for (int i = fiveMins; i >= 0; i--) {
						sleeping.set(true);
						Thread.sleep(1000);
						updateMessage(ladder.size() + " online players in ladder for all leagues (" + i + " secs ago)");
					}
            	} catch (JsonSyntaxException | IOException e) {
            		int countdownToRetry = 3;
            		for (int i = countdownToRetry; i > 0; i--) {
            			String err = String.format(
            					"Error downloading Ladder API data from %s, error: %s. Gonna try again in %d secs..", 
            					EXILE_TOOLS_LADDER_API_URL, e.getMessage(), i);
            			updateMessage(err);
            			Thread.sleep(1000);
					}
				}
            	return null;
            }
        };
	}
	
	private Ladder retrieveAllOnline() throws IOException {
		String result = StringUtils.trimToEmpty(UrlReaderUtil.getString(EXILE_TOOLS_LADDER_API_URL));
		Gson gson = new Gson();
		@SuppressWarnings("unchecked")
		Map<String, Object> m = gson.fromJson(result, Map.class);
		return new Ladder(m);
	}
}

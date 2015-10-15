package net.thirdy.blackmarket.service;

import java.io.IOException;
import java.util.List;

import io.jexiletools.es.ExileToolsESClient.ExileToolsSearchResult;
import io.jexiletools.es.model.json.ExileToolsHit;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import net.thirdy.blackmarket.BlackmarketApplication;
import net.thirdy.blackmarket.ex.BlackmarketException;
import net.thirdy.blackmarket.util.ImageCache;

public class ExileToolsSearchService extends Service<ExileToolsSearchResult> {
	
	private StringProperty json = new SimpleStringProperty(this, "json");
    public final void setJson(String value) { json.set(value); }
    public final String getJson() { return json.get(); }
    public final StringProperty jsonProperty() { return json; }
    
	@Override
    protected Task<ExileToolsSearchResult> createTask() {
        return new Task<ExileToolsSearchResult>() {    
        	final String s = getJson();
            @Override protected ExileToolsSearchResult call() throws Exception {
            	try {
        			// FIXME: not sure if ExileToolsESClient is thread-safe, maybe we should just instantiate here and then shutdown afterwards
            		updateMessage("Querying against Exile Tools Elastic Search Public API...");
            		ExileToolsSearchResult result = BlackmarketApplication.getExileToolsESClient().execute(s);
        			List<ExileToolsHit> exileToolHits = result.getExileToolHits();
        			
        			// cache images
        			updateMessage("Caching images...");
        			
					for (ExileToolsHit h : exileToolHits) {
						String icon = h.getInfo().getIcon();
						updateMessage("Caching image: " + icon);
						ImageCache.getInstance().preLoad(icon);
					}
        			
        			return result;
        		} catch (IOException e) {
        			String msg = "Error while running search to the backend. "
        					+ e.getMessage() +
        					". Json query is: " + json;
        			updateMessage(msg);
					throw new BlackmarketException(msg, e);
        		}
            }
        };
	}
	
}

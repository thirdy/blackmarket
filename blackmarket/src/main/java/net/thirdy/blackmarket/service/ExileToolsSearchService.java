package net.thirdy.blackmarket.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import io.jexiletools.es.model.ExileToolsHit;
import io.searchbox.core.SearchResult;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import net.thirdy.blackmarket.BlackmarketApplication;
import net.thirdy.blackmarket.ex.BlackmarketException;
import net.thirdy.blackmarket.util.ImageCache;

public class ExileToolsSearchService extends Service<ObservableList<ExileToolsHit>> {
	
	private StringProperty json = new SimpleStringProperty(this, "json");
    public final void setJson(String value) { json.set(value); }
    public final String getJson() { return json.get(); }
    public final StringProperty jsonProperty() { return json; }
    
	@Override
    protected Task<ObservableList<ExileToolsHit>> createTask() {
        return new Task<ObservableList<ExileToolsHit>>() {    
        	final String s = getJson();
            @Override protected ObservableList<ExileToolsHit> call() throws Exception {
            	try {
        			// FIXME: not sure if ExileToolsESClient is thread-safe, maybe we should just instantiate here and then shutdown afterwards
            		updateMessage("Querying against Exile Tools Elastic Search Public API...");
        			SearchResult result = BlackmarketApplication.getExileToolsESClient().execute(s);
        			List<ExileToolsHit> exileToolHits = result.getHits(ExileToolsHit.class)
        					.stream()
        					.map(e -> e.source)
        					.collect(Collectors.toList());

        			
        			// cache images
        			updateMessage("Caching images...");
        			
					for (ExileToolsHit h : exileToolHits) {
						String icon = h.getInfo().getIcon();
						updateMessage("Caching image: " + icon);
						ImageCache.getInstance().preLoad(icon);
					}
        			
        			return FXCollections.observableArrayList(exileToolHits);
        		} catch (IOException e) {
        			String msg = "Error while running search to the backend. Json query is: " + json;
        			updateMessage(msg);
					throw new BlackmarketException(msg, e);
        		}
            }
        };
	}
}

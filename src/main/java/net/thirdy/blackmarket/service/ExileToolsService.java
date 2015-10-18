package net.thirdy.blackmarket.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.gson.JsonParser;

import io.jexiletools.es.ExileToolsSearchClient.ExileToolsSearchResult;
import io.jexiletools.es.ExileToolsSearchException;
import io.jexiletools.es.model.json.ExileToolsHit;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import net.thirdy.blackmarket.BlackmarketApplication;
import net.thirdy.blackmarket.ex.BlackmarketException;
import net.thirdy.blackmarket.util.ImageCache;
import net.thirdy.blackmarket.util.UrlReaderUtil;

public class ExileToolsService extends Service<ExileToolsSearchResult> {
    
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
					
					// online status
					updateMessage("Looking up player status...");
					
					Optional<String> _league = ladderApiLeague();
					Map<String, List<LadderHit>> accountLadderMap = new HashMap<>();
					
					if (_league.isPresent()) {
						String url = "http://api.exiletools.com/ladder?league=" + _league.get() + "&accountName=";
						List<String> accountNames = exileToolHits
								.parallelStream()
								.map(h -> h.getShop().getSellerAccount())
								.distinct()
								.collect(Collectors.toList());
						
						List<String> jsons = Lists.partition(accountNames, 200)
							.stream()
							.map(l -> joinByColons(l) )
							.map(s -> callLadderApi(url + s))
							.filter(r -> r.isPresent())
							.map(r -> r.get())
							.collect(Collectors.toList());
						
						jsons.stream()
							.map(j -> new JsonParser().parse(j).getAsJsonObject())
							.flatMap(jo -> jo.entrySet().stream())
							.forEach(e -> {
								// key is accountName.charName
								String[] jsonKey = e.getKey().split("\\.");
								String account = jsonKey[0];
								LadderHit hit = new LadderHit(jsonKey[0], jsonKey[1], e.getValue());
								if (!accountLadderMap.containsKey(account)) {
									accountLadderMap.put(account, new ArrayList<>());
								}
								accountLadderMap.get(account).add(hit);
							});
					}
					
					mapLadderHitsToSearchHits(result.getExileToolHits(), accountLadderMap);
					if (getOnlineOnly()) {
						System.out.println("REMOVING OFFLINE");
						System.out.println(result.getExileToolHits());
						List<ExileToolsHit> collect = result.getExileToolHits().parallelStream()
							.filter(e -> e.isOnline())
							.collect(Collectors.toList());
						result.setExileToolHits(collect);
						System.out.println(result.getExileToolHits());
					}
					
        			return result;
        		} catch (ExileToolsSearchException e) {
        			e.printStackTrace();
        			String msg = "Error while running search to Exile Tools ES API: "
        					+ e.getMessage();
        			updateMessage(msg);
					throw new BlackmarketException(msg, e);
        		}
            }
            
			private void mapLadderHitsToSearchHits(List<ExileToolsHit> exileToolHits,
					Map<String, List<LadderHit>> accountLadderMap) {
				exileToolHits.stream()
					.forEach(e -> {
						String account = e.getShop().getSellerAccount();
						List<LadderHit> ladderHits = accountLadderMap.get(account);
						if (ladderHits != null) {
							String sellerIGN = StringUtils.trimToNull(e.getShop().getSellerIGN());
							if (sellerIGN == null) {
								ladderHits.stream().findFirst().ifPresent( lh -> {
									e.getShop().setSellerIGN(lh.charName());
								} );
							}
							e.setLadderHits(ladderHits);
						}
					});
			}

			private Optional<String> callLadderApi(String url) {
				String json = null;
				try {
					json = UrlReaderUtil.getString(url);
					if (json.startsWith("ERROR")) {
						json = "";
					}
				} catch (IOException e) {
					e.printStackTrace();
					updateMessage("Error occured while calling Ladder API:" + e.getMessage());
				}
				return Optional.ofNullable(json);
			}
			private String joinByColons(List<String> l) {
				return StringUtils.join(l, ':');
			}
			private Optional<String> ladderApiLeague() {
				String league = null;
				switch (getLeague()) {
				case "Flashback Event (IC001)":
					league = "flashback2";
					break;
				case "Flashback Event HC (IC002)":
					league ="flashback2hc";
					break;
				case "Standard":
					league = "flashback2";
					break;
				case "Hardcore":
					league = "Hardcore";
					break;
				}
				return Optional.ofNullable(league);
			}
        };
	}
	

	private StringProperty json = new SimpleStringProperty(this, "json");
    public final void setJson(String value) { json.set(value); }
    public final String getJson() { return json.get(); }
    public final StringProperty jsonProperty() { return json; }
    
    private StringProperty league = new SimpleStringProperty(this, "league");
    public final void setLeague(String value) { league.set(value); }
    public final String getLeague() { return league.get(); }
    public final StringProperty leagueProperty() { return league; }
    
    private BooleanProperty onlineOnly = new SimpleBooleanProperty(this, "onlineOnly");
    public final void setOnlineOnly(boolean value) { onlineOnly.set(value); }
    public final boolean getOnlineOnly() { return onlineOnly.get(); }
    public final BooleanProperty onlineOnlyProperty() { return onlineOnly; }
}

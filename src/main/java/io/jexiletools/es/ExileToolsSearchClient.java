/*
 * Copyright (C) 2015 thirdy
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package io.jexiletools.es;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jexiletools.es.ExileToolsSearchAction.Builder;
import io.jexiletools.es.model.json.ExileToolsHit;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.http.JestHttpClient;
import io.searchbox.core.SearchResult;

/**
 * @author thirdy
 *
 */
public class ExileToolsSearchClient {
	
	final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
	
	private final JestHttpClient client;
	private final String apiKey;
	
	public ExileToolsSearchClient() {
		this("http://api.exiletools.com/index", "DEVELOPMENT-Indexer");
	}
	
	public ExileToolsSearchClient(String apiKey) {
		this("http://api.exiletools.com/index", apiKey);
	}

	public ExileToolsSearchClient(String url, String apiKey) {
		this.apiKey = apiKey;
		// Construct a new Jest client according to configuration via factory
		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder(url).multiThreaded(true).build());
		client = (JestHttpClient) factory.getObject();
		logger.debug("~~~~~~~~~~~~~~~~~~ Successfully started ExileToolsESClient ~~~~~~~~~~~~~~~~~~~~");
	}
	
	public ExileToolsSearchResult execute(String json) throws ExileToolsSearchException {
		 logger.debug("~~~~ Executing search: {}{}", System.lineSeparator(), json);

		 Builder builder = new ExileToolsSearchAction.Builder(json)
		                                 .setHeader("Authorization", apiKey);
		 ExileToolsSearchAction search = builder.build();
		 
		 SearchResult result;
		try {
			result = client.execute(search);
			logger.debug(result.getJsonString());
			return new ExileToolsSearchResult(result);
		} catch (IOException e) {
			throw new ExileToolsSearchException("Error while executing search for json: " + json, e);
		}
	}
	
	public static class ExileToolsSearchResult {
		private List<ExileToolsHit> exileToolHits;
		private SearchResult searchResult;
		
		public ExileToolsSearchResult(SearchResult searchResult) {
			this.searchResult = searchResult;
			exileToolHits = searchResult.getHits(ExileToolsHit.class)
					.stream()
					.map(e -> e.source)
					.collect(Collectors.toList());
		}
		public List<ExileToolsHit> getExileToolHits() {
			return exileToolHits;
		}
		public SearchResult getSearchResult() {
			return searchResult;
		}
		public void setExileToolHits(List<ExileToolsHit> exileToolHits) {
			this.exileToolHits = exileToolHits;
		}
	}
	
	public void shutdown() {
		client.shutdownClient();
	}
}

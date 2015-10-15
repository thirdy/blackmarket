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

import java.util.List;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import io.jexiletools.es.model.json.ExileToolsHit;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.http.JestHttpClient;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
/**
 * @author thirdy
 *
 */
public class Main {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		 // Construct a new Jest client according to configuration via factory
		 JestClientFactory factory = new JestClientFactory();
		 factory.setHttpClientConfig(new HttpClientConfig
		                        .Builder("http://api.exiletools.com/index")
		                        .multiThreaded(true)
		                        .build());
		 JestHttpClient client = (JestHttpClient) factory.getObject();
		 
		 SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//		 searchSourceBuilder.query(QueryBuilders.matchQuery("info.name", "Tabula Rasa"));
		 searchSourceBuilder.query(QueryBuilders.matchQuery("info.name", "Mjolner"));
		 searchSourceBuilder.size(1);

		 System.out.println(searchSourceBuilder.toString());

		 ExileToolsSearchAction search = new ExileToolsSearchAction.Builder(searchSourceBuilder.toString())
		                                 .setHeader("Authorization", "DEVELOPMENT-Indexer")
		                                 .build();
		 
		 SearchResult result = client.execute(search);
		 
		 System.out.println(result.getPathToResult());
		 System.out.println(result.getJsonString());
		 
		 List<Hit<ExileToolsHit, Void>> articles = result.getHits(ExileToolsHit.class);
		 System.out.println(articles);
		 articles.stream().forEach( (e) -> {
			 System.out.println(e.source.getMd5sum());
			 System.out.println(e.source.getInfo());
			 System.out.println(e.source.getAttributes());
			 System.out.println(e.source.getShop());
			 System.out.println(e.source.getSockets());
		 } );
		 client.shutdownClient();
	}

}

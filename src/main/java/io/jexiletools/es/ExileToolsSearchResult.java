package io.jexiletools.es;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import io.jexiletools.es.model.json.ExileToolsHit;
import io.searchbox.core.SearchResult;

public class ExileToolsSearchResult extends SearchResult {

	public ExileToolsSearchResult(Gson gson) {
		super(gson);
	}

	@Override
	protected <T, K> Hit<T, K> extractHit(Class<T> sourceType, Class<K> explanationType, JsonElement hitElement,
			String sourceKey) {
		Hit<T, K> extractHit = super.extractHit(sourceType, explanationType, hitElement, sourceKey);
		JsonObject hitJsonObject = hitElement.getAsJsonObject();
		ExileToolsHit source = (ExileToolsHit) extractHit.source;
		source.setHitJsonObject(hitJsonObject);
		return extractHit;
	}
}

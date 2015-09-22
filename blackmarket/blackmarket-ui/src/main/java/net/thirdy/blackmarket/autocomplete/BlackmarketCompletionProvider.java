package net.thirdy.blackmarket.autocomplete;

import java.util.Map;
import java.util.Map.Entry;

import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.DefaultCompletionProvider;

import net.thirdy.blackmarket.core.util.BlackmarketConfig;

public class BlackmarketCompletionProvider extends DefaultCompletionProvider {
	public BlackmarketCompletionProvider() {
		Map<String, String> map = BlackmarketConfig.loadAutoCompleteDictionary();
		
		for (Entry<String, String> entry : map.entrySet()) {
			addCompletion(new BasicCompletion(this, entry.getKey(), entry.getValue()));
		}
	}
	
//	Pattern pattern = Pattern.compile("(\\d+(\\.)?(\\d+)?).*", Pattern.DOTALL);
	
}

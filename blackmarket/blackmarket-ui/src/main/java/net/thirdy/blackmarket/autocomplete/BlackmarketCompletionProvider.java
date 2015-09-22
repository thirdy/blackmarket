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
	
	
	// TODO, why ain't this working?
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Completion> getCompletionByInputText(String inputText) {
//		String newInputText = inputText;
//		// Remove any digits or decimals
//		newInputText = StringUtils.replacePattern(newInputText, "\\d(\\.)?([0-9]{1,2})?", "");
//		System.out.println("getCompletionByInputText: inputText: " + inputText + " newInputText: " + newInputText);
//		return super.getCompletionByInputText(newInputText);
//
//	}
	
}

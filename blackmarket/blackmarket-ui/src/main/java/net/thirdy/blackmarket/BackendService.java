package net.thirdy.blackmarket;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import net.thirdy.blackmarket.core.SearchPageScraper;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;

public class BackendService extends Service<List<SearchResultItem>> {

	private String payload;

	public BackendService() {
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	protected Task<List<SearchResultItem>> createTask() {
		return new Task<List<SearchResultItem>>() {
			@Override
			protected List<SearchResultItem> call() throws Exception {
				List<SearchResultItem> list = null;
//				URL url = SearchPageScraper.class.getResource("DefaultWarbands.search.poetrade");
//				try {
//					String payload = FileUtils.readFileToString(new File(url.toURI()));
//					SearchPageScraper scraper = new SearchPageScraper(payload);
//					list = scraper.parse();
//					for (SearchResultItem item : list) {
//						System.out.println(item);
//					}
//				} catch (URISyntaxException e1) {
//				e1.printStackTrace();
//				// TODO use exception dialog from
//				// http://code.makery.ch/blog/javafx-dialogs-official/
//				throw e1;
//			}	
					String searchPage = MainApp.getPoeTradeHttpClient().search(payload);
			        SearchPageScraper scraper = new SearchPageScraper(searchPage);
					list = scraper.parse();
					for (SearchResultItem item : list) {
						System.out.println(item);
					}
					

				return list;
			}
		};
	}

	// Set<String> explicitMods =
	// list.stream().map(SearchResultItem::getExplicitModsNames).flatMap(Collection::stream).collect(Collectors.toSet());
	// explicitMods.stream().map(e -> new TableColumn(e)).forEach(e ->
	// searchResultTable.getColumns().add(e));

	// List<String> cols =
	// Arrays.asList(SearchResultItem.class.getDeclaredFields()).stream().map(Field::getName).collect(toList());
}

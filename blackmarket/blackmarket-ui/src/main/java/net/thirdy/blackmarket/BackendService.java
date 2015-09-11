package net.thirdy.blackmarket;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import net.thirdy.blackmarket.core.SearchPageScraper;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;

public class BackendService extends Service<List<SearchResultItem>> {

	private String payload;
	private ListView<SearchResultItem> itemListView;

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
				List<SearchResultItem> list = Collections.emptyList();
				if (AppConfig.TESTING_MODE) {
					String page = null;
					try {
						page = IOUtils.toString(this.getClass().getResourceAsStream("/DefaultWarbands.search.poetrade"));
					} catch (IOException e) {
						e.printStackTrace();
					}
					SearchPageScraper scraper = new SearchPageScraper(page);
					list = scraper.parse();
				} else {
					String searchPage = MainApp.getPoeTradeHttpClient().search(payload);
					SearchPageScraper scraper = new SearchPageScraper(searchPage);
					list = scraper.parse();
				}
				list.stream().forEach(e -> System.out.println(e));
				if (!list.isEmpty()) {
					ObservableList<SearchResultItem> fxList = FXCollections.observableArrayList(list);
					Platform.runLater(() -> itemListView.setItems(fxList));
				}
				return list;
			}
		};
	}

	public void setListView(ListView<SearchResultItem> itemListView) {
		this.itemListView = itemListView;
	}

	// Set<String> explicitMods =
	// list.stream().map(SearchResultItem::getExplicitModsNames).flatMap(Collection::stream).collect(Collectors.toSet());
	// explicitMods.stream().map(e -> new TableColumn(e)).forEach(e ->
	// searchResultTable.getColumns().add(e));

	// List<String> cols =
	// Arrays.asList(SearchResultItem.class.getDeclaredFields()).stream().map(Field::getName).collect(toList());
}

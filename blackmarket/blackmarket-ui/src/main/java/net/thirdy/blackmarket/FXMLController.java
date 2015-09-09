package net.thirdy.blackmarket;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.common.collect.Lists;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import net.thirdy.blackmarket.core.PoeTradeHttpClient;
import net.thirdy.blackmarket.core.PoeTradeHttpClientException;
import net.thirdy.blackmarket.core.SearchPageScraper;
import net.thirdy.blackmarket.core.SearchPayload;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem.Mod;

public class FXMLController implements Initializable {
	
	PoeTradeHttpClient poeTradeHttpClient = new PoeTradeHttpClient();
    
	@FXML TextArea searchTextArea;
	
	@FXML VBox itemPaneVBox;

    @FXML
    private void handleSearchButtonAction(ActionEvent event) throws PoeTradeHttpClientException {
        System.out.println("handleSearchButtonAction");
        
        URL url = SearchPageScraper.class.getResource("DefaultWarbands.search.poetrade");
		try {
			String payload = FileUtils.readFileToString(new File(url.toURI()));
			SearchPageScraper scraper = new SearchPageScraper(payload);
			List<SearchResultItem> list = scraper.parse();
			for (SearchResultItem item : list) {
				System.out.println(item);
			}
			list.stream().forEach(e -> addNewItemPane(e));
		} catch (IOException | URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
//        String payload = searchTextArea.getText();
//        payload = new SearchPayload(payload).getPayloadFormatted();
//        
//        String searchPage = poeTradeHttpClient.search(payload);
//        SearchPageScraper scraper = new SearchPageScraper(searchPage);
//		List<SearchResultItem> list = scraper.parse();
//		for (SearchResultItem item : list) {
//			System.out.println(item);
//		}
		
//		searchResultTable.getColumns().clear();
//		searchResultTable.getItems().clear();
		
//		Set<String> explicitMods = 
//				list.stream().map(SearchResultItem::getExplicitModsNames).flatMap(Collection::stream).collect(Collectors.toSet());
//		explicitMods.stream().map(e -> new TableColumn(e)).forEach(e -> searchResultTable.getColumns().add(e));
		
//		List<String> cols = Arrays.asList(SearchResultItem.class.getDeclaredFields()).stream().map(Field::getName).collect(toList());
//		cols.stream().map(new Function<String, TableColumn>() {
//
//			@Override
//			public TableColumn apply(String t) {
//				TableColumn col = new TableColumn(t);
//				// thanks to http://fxapps.blogspot.com/2012/09/showing-object-properties-in-tableview.html
//				col.setCellValueFactory(new PropertyValueFactory<>(t));
//				return col;
//			}
//		}).forEach(e -> searchResultTable.getColumns().add(e));
		
//		list.stream().forEach(e -> addNewItemPane(e));
    }

	private void addNewItemPane(SearchResultItem e) {
		ItemPaneController itemPane = new ItemPaneController(e, new Function<String, Void>() {

			@Override
			public Void apply(String field) {
				itemPaneVBox.getChildren().sort(new Comparator<Node>() {

					@Override
					public int compare(Node arg0, Node arg1) {
						ItemPaneController itemController0 = (ItemPaneController) arg0;
						ItemPaneController itemController1 = (ItemPaneController) arg1;

						SearchResultItem item0 = itemController0.getSearchResultItem();
						SearchResultItem item1 = itemController1.getSearchResultItem();

						String prop0 = item0.getFieldValue(field);
						String prop1 = item1.getFieldValue(field);
						return prop1.compareTo(prop0);
					
					}
				});
				return null;
			}
		});
		itemPaneVBox.getChildren().add(itemPane);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		String page = loadDefaultSearchFile();
		searchTextArea.setText(page);
	}

	private String loadDefaultSearchFile() {
		URL url = this.getClass().getResource("ring-life.txt");
		String page;
		try {
			page = FileUtils.readFileToString(new File(url.toURI()));
		} catch (Exception e) {
			// won't likely happen since file is in classpath
			throw new RuntimeException(e);
		}
		return page;
	}
    
 
}

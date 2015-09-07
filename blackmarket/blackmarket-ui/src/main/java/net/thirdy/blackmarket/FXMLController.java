package net.thirdy.blackmarket;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import net.thirdy.blackmarket.core.PoeTradeHttpClient;
import net.thirdy.blackmarket.core.PoeTradeHttpClientException;
import net.thirdy.blackmarket.core.SearchPageScraper;
import net.thirdy.blackmarket.core.SearchPayload;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem.Mod;

public class FXMLController implements Initializable {
	
	PoeTradeHttpClient poeTradeHttpClient = new PoeTradeHttpClient();
    
	@FXML TextArea searchTextArea;
	@FXML TableView searchResultTable;

    @FXML
    private void handleSearchButtonAction(ActionEvent event) throws PoeTradeHttpClientException {
        System.out.println("handleSearchButtonAction");
        
        String payload = searchTextArea.getText();
        payload = new SearchPayload(payload).getPayloadFormatted();
        
        String searchPage = poeTradeHttpClient.search(payload);
        SearchPageScraper scraper = new SearchPageScraper(searchPage);
		List<SearchResultItem> list = scraper.parse();
		for (SearchResultItem item : list) {
			System.out.println(item);
		}
		
		searchResultTable.getColumns().clear();
		
//		Set<String> explicitMods = 
//				list.stream().map(SearchResultItem::getExplicitModsNames).flatMap(Collection::stream).collect(Collectors.toSet());
//		explicitMods.stream().map(e -> new TableColumn(e)).forEach(e -> searchResultTable.getColumns().add(e));
		
		List<String> cols = Arrays.asList(SearchResultItem.class.getDeclaredFields()).stream().map(Field::getName).collect(toList());
		cols.stream().map(new Function<String, TableColumn>() {

			@Override
			public TableColumn apply(String t) {
				TableColumn col = new TableColumn(t);
				col.setCellValueFactory(new PropertyValueFactory<>(t));
				return col;
			}
		}).forEach(e -> searchResultTable.getColumns().add(e));
		
		list.stream().forEach(e -> searchResultTable.getItems().add(e));
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

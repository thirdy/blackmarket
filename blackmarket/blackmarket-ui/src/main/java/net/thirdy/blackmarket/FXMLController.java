package net.thirdy.blackmarket;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import net.thirdy.blackmarket.core.PoeTradeHttpClient;
import net.thirdy.blackmarket.core.PoeTradeHttpClientException;
import net.thirdy.blackmarket.core.SearchPageScraper;
import net.thirdy.blackmarket.core.SearchPayload;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;

public class FXMLController implements Initializable {
	
	PoeTradeHttpClient poeTradeHttpClient = new PoeTradeHttpClient();
    
	@FXML TextArea searchTextArea;
	@FXML TableView searchResultTable;

    @FXML
    private void handleSearchButtonAction(ActionEvent event) throws PoeTradeHttpClientException {
        System.out.println("handleSearchButtonAction");
        
//        String payload = searchTextArea.getText();
//        payload = new SearchPayload(payload).getPayloadFormatted();
//        
//        String searchPage = poeTradeHttpClient.search(payload);
//        SearchPageScraper scraper = new SearchPageScraper(searchPage);
//		List<SearchResultItem> list = scraper.parse();
//		for (SearchResultItem item : list) {
//			System.out.println(item);
//		}
		
//		for (SearchResultItem item : list) {
//			searchResultTable
//		}
		TableColumn lifeCol = new TableColumn("Life");
		searchResultTable.getColumns().add(lifeCol);
		
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

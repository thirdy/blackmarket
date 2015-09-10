package net.thirdy.blackmarket;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.io.IOUtils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.util.Callback;
import net.thirdy.blackmarket.core.PoeTradeHttpClient;
import net.thirdy.blackmarket.core.PoeTradeHttpClientException;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;
import net.thirdy.blackmarket.core.SearchPayload;

public class FXMLController implements Initializable {
	
	PoeTradeHttpClient poeTradeHttpClient = new PoeTradeHttpClient();
    
	@FXML TextArea searchTextArea;
	@FXML ListView<SearchResultItem> itemListView;
	@FXML ProgressIndicator progressIndicator;
	@FXML Button searchButton;
	BackendService backendService = new BackendService();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		progressIndicator.visibleProperty().bind(backendService.runningProperty());
		String page = loadDefaultSearchFile();
		searchTextArea.setText(page);
		itemListView.setCellFactory(new Callback<ListView<SearchResultItem>, ListCell<SearchResultItem>>() {
			
			@Override
			public ListCell<SearchResultItem> call(ListView<SearchResultItem> listView) {
				return new ListCellItem();
			}
		});
	}
	
	static class ListCellItem extends ListCell<SearchResultItem> {
		private ItemPaneController itemPane;
		
		@Override
		protected void updateItem(SearchResultItem item, boolean empty) {
			super.updateItem(item, empty);
			setText(null);  // No text in label of super class
            if (empty) {
                setGraphic(null);
            } else {
            	if(itemPane == null) {
            		itemPane = new ItemPaneController(item, super.getListView());
            		itemPane.setCache(true);
            	}
                setGraphic(itemPane);
            }
		}
	}

    @FXML private void handleSearchButtonAction(ActionEvent event) throws PoeTradeHttpClientException {
        System.out.println("handleSearchButtonAction");
        
        String payload = searchTextArea.getText();
        payload = new SearchPayload(payload).getPayloadFormatted();
        backendService.setPayload(payload);
        
        backendService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				List<SearchResultItem> list = backendService.getValue();
				if (!list.isEmpty()) {
					ObservableList<SearchResultItem> fxList = FXCollections.observableArrayList(list);
					itemListView.setItems(fxList);
				}
				searchButton.setDisable(false);
			}
		});
        backendService.setOnFailed(new EventHandler<WorkerStateEvent>() {
			
			@Override
			public void handle(WorkerStateEvent event) {
				backendService.getException().printStackTrace();
				searchButton.setDisable(false);
			}
		});
        searchButton.setDisable(true);
        backendService.restart();
        
        
    }

//    public void addNewItemPane(SearchResultItem e) {
//		itemPane.setCache(true);
//		itemListView.getChildren().add(itemPane);
//	}

	private String loadDefaultSearchFile() {
		String page = null;
		try {
			page = IOUtils.toString(this.getClass().getResourceAsStream("/ring-life.txt"));
		} catch (IOException e) {
			// won't likely happen since file is in classpath
			e.printStackTrace();
		}
		return page;
	}
    
	@FXML private void issuesSuggestionsClickHandler(ActionEvent event) {
		 Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
		        try {
		            desktop.browse(new URI("https://github.com/thirdy/blackmarket/issues"));
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		
	}
	

}

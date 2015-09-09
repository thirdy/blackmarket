package net.thirdy.blackmarket;

import static java.util.stream.Collectors.toList;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
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

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
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
	@FXML ProgressIndicator progressIndicator;
	@FXML Button searchButton;
	BackendService backendService = new BackendService();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		progressIndicator.visibleProperty().bind(backendService.runningProperty());
		String page = loadDefaultSearchFile();
		searchTextArea.setText(page);
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
				if(list != null) list.stream().forEach(e -> addNewItemPane(e));
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

    public void addNewItemPane(SearchResultItem e) {
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

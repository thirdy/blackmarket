package net.thirdy.blackmarket;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;

public class ItemPaneController extends AnchorPane {
//	@FXML
//	private TextField textField;
	
	@FXML Label itemName;

	public ItemPaneController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ItemPane.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public ItemPaneController(SearchResultItem e) {
		this();
		itemName.setText(e.getName());
	}

//	@FXML
//	protected void doSomething() {
//		System.out.println("The button was clicked!");
//	}
}

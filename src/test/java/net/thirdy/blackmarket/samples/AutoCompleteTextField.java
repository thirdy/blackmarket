package net.thirdy.blackmarket.samples;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.PopupWindow.AnchorLocation;

/**
 * Modified version of: https://gist.github.com/floralvikings/10290131
 * 
 * This class is a TextField which implements an "autocomplete" functionality,
 * based on a supplied list of entries.
 * 
 * @author Caleb Brinkman
 * @author Vicente de Rivera III
 */
public class AutoCompleteTextField extends TextField {
	/** The existing autocomplete entries. */
	private final SortedSet<String> entries;
	/** The popup used to select an entry. */
//	private ContextMenu entriesPopup;
	private Popup entriesPopup;
	/** Use for checking if current text value has actually changed, for efficiency. */
	private String currentTextValue; 

	/** Construct a new AutoCompleteTextField. */
	public AutoCompleteTextField() {
		super();
		entries = new TreeSet<>();
		entries.addAll(Arrays.asList("apple", "ball", "cat", "doll", "elephant", "fight", "georgeous", "height", "ice",
				"jug", "aplogize", "bank", "call", "done", "ego", "finger", "giant", "hollow", "internet", "jumbo",
				"kilo", "lion", "for", "length", "primary", "stage", "scene", "zoo", "jumble", "auto", "text", "root",
				"box", "items", "hip-hop", "himalaya", "nepal", "kathmandu", "kirtipur", "everest", "buddha", "epic",
				"hotel", 
				"loooooooooooooo000000000000000000f000000000000000oooong",
				"loooooooooooooo00000000d000000000000f0000000000000oooong",
				"loooooooooooooo000000000000000000000d000000000000oooong",
				"loooooooooooooo00000000000d000000d0000000000000000oooong",
				"loooooooooooooo0000000000d00f000000000000000000000oooong",
				"loooooooooooooo0000000000f00000000000000000000000oooong",
				"loooooooooooooo0000000000000f00000000000000000000oooong",
				"loooooooooooooo0000000x00000000000000000000000000oooong",
				"loooooooooooooo00000000000000d0000000000000000000oooong",
				"loooooooooooooo000000x000000000000000000000000000oooong",
				"loooooooooooooo00000000000f0000000000000000000000oooong",
				"loooooooooooooo000000x000000000000000000000000000oooong",
				"loooooooooooooo00000000000f0000000000000000000000oooong",
				"loooooooooooooo0000000000x00000000000000000000000oooong",
				"loooooooooooooo00000000000x0000000000000000000000oooong",
				"loooooooooooooo000000000000000000000000000000000oooong",
				"loooooooooooooo000x000000000x000000000000000000000oooong",
				"looooooooxoooooo000000000000000f000000000000000000oooong",
				"loooooooooooooo000000000000x000000000000000000000oooong",
				"loooooooooooooo00000000x000000000000000000000000oooong",
				"loooooooooooooox0000000000000000000dg0000000000000oooong           ", // can't figure out how to make the popup window width wider, workaround is to add spaces
				"loooooooooooooo000000000000000x000000000000000000oooong",
				"x"
				));
		entriesPopup = new Popup();
		entriesPopup.setAnchorLocation(AnchorLocation.WINDOW_TOP_LEFT);
//		entriesPopup.setWidth(Double.MAX_VALUE);
//		((Pane) entriesPopup.getScene().getRoot()).setMaxWidth(Double.MAX_VALUE);
//		((Pane) entriesPopup.getScene().getRoot()).setPrefWidth(600);
		
		
		textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
				updatePopup();
			}
		});
		
		setOnMouseClicked(e -> {
			updatePopup();
		});

		focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue,
					Boolean newValue) {
				entriesPopup.hide();
				currentTextValue = null;
			}
		});

	}
	
	private void updatePopup() {
		if (StringUtils.equals(currentTextValue, getText())) {
			return;
		} else {
			currentTextValue = getText();
		}
		LinkedList<String> searchResult = new LinkedList<>();
		searchResult.addAll(entries.stream().filter(x -> StringUtils.containsIgnoreCase(x, getText()))
				.collect(Collectors.toList()));
		if (entries.size() > 0) {
			populatePopup(searchResult);
			if (!entriesPopup.isShowing()) {
//				entriesPopup.show(AutoCompleteTextField.this, Side.BOTTOM, 0, 0);
				Point2D p = this.localToScene(0.0, 0.0);
				double x = p.getX() + this.getScene().getX() + this.getScene().getWindow().getX();
				double y = p.getY() + this.getScene().getY() + this.getScene().getWindow().getY() + this.getHeight();
				System.out.println(String.format("%f, %f", x,y));
				entriesPopup.show(AutoCompleteTextField.this, x, y);
//				entriesPopup.show(this.getScene().getWindow());
//				entriesPopup.
			}
		} else {
			entriesPopup.hide();
			currentTextValue = null;
		}
	}

	/**
	 * Get the existing set of autocomplete entries.
	 * 
	 * @return The existing autocomplete entries.
	 */
	public SortedSet<String> getEntries() {
		return entries;
	}

	/**
	 * Populate the entry set with the given search results. Display is limited
	 * to 10 entries, for performance.
	 * 
	 * @param searchResult
	 *            The set of matching strings.
	 */
	private void populatePopup(List<String> searchResult) {
		System.out.println("populatePopup: " + searchResult);
//		List<CustomMenuItem> menuItems = new LinkedList<>();
		List<Hyperlink> menuItems = new LinkedList<>();
		// If you'd like more entries, modify this line.
//		int maxEntries = 100;
//		int count = Math.min(searchResult.size(), maxEntries);
		int count = searchResult.size();
		for (int i = 0; i < count; i++) {
			final String result = searchResult.get(i);
			Hyperlink entryLabel = new Hyperlink(result);
			entryLabel.setFocusTraversable(false);
//			entryLabel.setMaxWidth(Double.MAX_VALUE);
//			CustomMenuItem item = new CustomMenuItem(entryLabel, true);
			entryLabel.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					setText(result);
					entriesPopup.hide();
				}
			});
			menuItems.add(entryLabel);
		}
		VBox vBox = new VBox();
//		vBox.setMaxWidth(Double.MAX_VALUE);
		vBox.getChildren().addAll(menuItems);
		ScrollPane scrollPane = new ScrollPane(vBox);
		scrollPane.setMaxHeight(500);//Adjust max height of the popup here
		scrollPane.setMaxWidth(Double.MAX_VALUE);//Adjust max width of the popup here
		scrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		// Focusing on this node makes it blurry for some reason
		// http://www.jensd.de/wordpress/?p=1245
		scrollPane.setStyle("-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2; -fx-background-radius: 5, 4, 3;");
//		entriesPopup.getItems().clear();
//		entriesPopup.getItems().addAll(menuItems);
		entriesPopup.getContent().clear();
		entriesPopup.getContent().add(scrollPane);
	}

}

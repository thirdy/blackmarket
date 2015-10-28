package net.thirdy.blackmarket.fxcontrols;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.PopupControl;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Popup;
import javafx.stage.PopupWindow.AnchorLocation;
import javafx.util.Callback;

/**
 * Modified version of: https://gist.github.com/floralvikings/10290131
 * 
 * This class is a TextField which implements an "autocomplete" functionality,
 * based on a supplied list of entries.
 * 
 * @author Caleb Brinkman
 * @author Vicente de Rivera III
 */
public class AutoCompleteTextField<T> extends TextField {
	/** The existing autocomplete entries. */
	private final SortedSet<T> entries;
	/** The popup used to select an entry. */
	private PopupControl entriesPopup;
	/**
	 * Use for checking if current text value has actually changed, for
	 * efficiency.
	 */
	private String currentTextValue;

	private ObjectProperty<T> selectedItemProperty;

	public ObjectProperty<T> selectedItemProperty() {
		return selectedItemProperty;
	}
	
	private ObservableList<T> listViewItems;
	private ListView<T> listView;

	/** Construct a new AutoCompleteTextField. */
	public AutoCompleteTextField(Collection<T> items, double prefWidth) {
		super();
		entries = new TreeSet<>();
		entriesPopup = new PopupControl();
		entriesPopup.setPrefWidth(USE_PREF_SIZE);
		entriesPopup.setAutoFix(true);
		entriesPopup.setAutoHide(true);
		setupListView(prefWidth);
		((Pane) entriesPopup.getScene().getRoot()).getChildren().setAll(listView);
		entriesPopup.setAnchorLocation(AnchorLocation.WINDOW_TOP_LEFT);
		setItems(items);

		textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observableValue, String oldVal, String newVal) {
				System.out.println(" textProperty old: " + oldVal + " new: " + newVal);
				updatePopup();
			}
		});

		setOnMouseClicked(e -> {
			showPopupAllItems();
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

	private void setupListView(double prefWidth) {
		listView = new ListView<>();
		listView.setPrefWidth(prefWidth);
		// Focusing on this node makes it blurry for some reason
		// http://www.jensd.de/wordpress/?p=1245
		listView.setStyle(
				"-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2; -fx-background-radius: 5, 4, 3;");
		setupListViewCellFactory();
		listView.getSelectionModel().selectedItemProperty().addListener((observe, oldVal, newVal) -> {
//			System.out.println("old: " + oldVal + " new: " + newVal);
			T selectedItem = listView.getSelectionModel().getSelectedItem();
			if (selectedItem != null) {
				System.out.println("CLICK: "  + selectedItem);
//				AutoCompleteTextField.this.setText(selectedItem.toString());
//				selectedItemProperty().setValue(selectedItem);
//				entriesPopup.hide();
			}
		});
	}

	private void setupListViewCellFactory() {
		 listView.setCellFactory(new Callback<ListView<T>, ListCell<T>>(){
	            @Override
	            public ListCell<T> call(ListView<T> p) {
	                return new ListCell<T>() {
	            		@Override
	                    protected void updateItem(T t, boolean empty) {
	                        super.updateItem(t, empty);
	                        if (!empty) {
	                        	Button button = new Button(t.toString());
//	                        	button.setStyle("-fx-background-color: transparent;");
	                        	button.setOnAction(e -> System.out.println("CLICKED: " + t.toString()));
	                        	button.setMaxWidth(Double.MAX_VALUE);
//	            				setGraphic(new TextFlow(button));
	            				setGraphic(button);
	            			} else {
	            				setGraphic(null);
	            			}
	                    }
	            		
	            		@Override
	            		public void startEdit() {
	            			super.startEdit();
	            			System.out.println("START EDIT");
	            		}
	            	};
	            }
	        });
	}

	public void setItems(Collection<T> items) {
		initializeSelectedItemProperty(items.iterator().next());
		initializeListViewItems(items);
		entries.clear();
		entries.addAll(items);
	}

	private void initializeListViewItems(Collection<T> items) {
		if (listViewItems == null) {
			listViewItems = FXCollections.observableArrayList(items);
		}
		listView.setItems(listViewItems);
	}

	private void initializeSelectedItemProperty(T t) {
		if (selectedItemProperty == null) {
			selectedItemProperty = new SimpleObjectProperty<>();
		}
		selectedItemProperty.set(t);
	}

	private void updatePopup() {
		System.out.println("updatePopup()");
		T selectedItem = listView.getSelectionModel().getSelectedItem();
		if (selectedItem != null && getText().equals(selectedItem.toString())) {
			System.out.println("listview selected is equal to getText()");
//			listView.getSelectionModel().clearSelection();
			return;
		}
		
		if (StringUtils.equals(currentTextValue, getText())) {
			return;
		} else {
			currentTextValue = getText();
		}
		LinkedList<T> searchResult = new LinkedList<>();
		searchResult.addAll(entries.stream().filter(x -> StringUtils.containsIgnoreCase(x.toString(), getText()))
				.collect(Collectors.toList()));
		if (entries.size() > 0) {
			listViewItems.setAll(searchResult);
			if (!entriesPopup.isShowing()) {
				Point2D p = this.localToScene(0.0, 0.0);
				double x = p.getX() + this.getScene().getX() + this.getScene().getWindow().getX();
				double y = p.getY() + this.getScene().getY() + this.getScene().getWindow().getY() + this.getHeight();
				entriesPopup.show(AutoCompleteTextField.this, x, y);
			}
		} else {
			entriesPopup.hide();
			currentTextValue = null;
		}
	}
	
	private void showPopupAllItems() {
		System.out.println("showPopupAllItems()");
		if (!entriesPopup.isShowing()) {
			listViewItems.setAll(entries);
			Point2D p = this.localToScene(0.0, 0.0);
			double x = p.getX() + this.getScene().getX() + this.getScene().getWindow().getX();
			double y = p.getY() + this.getScene().getY() + this.getScene().getWindow().getY() + this.getHeight();
			entriesPopup.show(AutoCompleteTextField.this, x, y);
		}
	}

	/**
	 * Get the existing set of autocomplete entries.
	 * 
	 * @return The existing autocomplete entries.
	 */
	public SortedSet<T> getEntries() {
		return entries;
	}

}

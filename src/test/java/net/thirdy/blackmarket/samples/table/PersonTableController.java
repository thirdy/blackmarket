package net.thirdy.blackmarket.samples.table;

import java.util.Arrays;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;


/**
 * View-Controller for the person table.
 * 
 * @author Marco Jakob
 */
public class PersonTableController extends Application {
	
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sorting and Filtering");
        initialize();
        	AnchorPane.setBottomAnchor(personTable, 10.0);
        	AnchorPane.setLeftAnchor(personTable, 10.0);
        	AnchorPane.setRightAnchor(personTable, 10.0);
        	AnchorPane.setTopAnchor(personTable, 40.0);
        	
        	HBox hBox = new HBox(filterField);
        	AnchorPane.setLeftAnchor(hBox, 10.0);
        	AnchorPane.setRightAnchor(hBox, 10.0);
        	AnchorPane.setTopAnchor(hBox, 10.0);
        	
        	
			AnchorPane page = new AnchorPane(personTable, hBox);
            
            Scene scene = new Scene(page);
            primaryStage.setScene(scene);
            primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private TextField filterField;
    private TableView<Person> personTable;
    private TableColumn<Person, String> firstNameColumn;
    private TableColumn<Person, String> lastNameColumn;

    private ObservableList<Person> masterData = FXCollections.observableArrayList();

    /**
     * Just add some sample data in the constructor.
     */
    public PersonTableController() {
        masterData.add(new Person("Hans", "Muster"));
        masterData.add(new Person("Ruth", "Mueller"));
        masterData.add(new Person("Heinz", "Kurz"));
        masterData.add(new Person("Cornelia", "Meier"));
        masterData.add(new Person("Werner", "Meyer"));
        masterData.add(new Person("Lydia", "Kunz"));
        masterData.add(new Person("Anna", "Best"));
        masterData.add(new Person("Stefan", "Meier"));
        masterData.add(new Person("Martin", "Mueller"));
    }

    private void initialize() {
        // 0. Initialize the columns.
    	firstNameColumn = new TableColumn<>("First Name");
    	lastNameColumn = new TableColumn<>("Last Name");
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Person> filteredData = new FilteredList<>(masterData, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterField = new TextField();
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(person -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (person.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (person.getLastName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        personTable = new TableView<>();
        personTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        personTable.getColumns().addAll(Arrays.asList(firstNameColumn, lastNameColumn));

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Person> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(personTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        personTable.setItems(sortedData);
    }
}
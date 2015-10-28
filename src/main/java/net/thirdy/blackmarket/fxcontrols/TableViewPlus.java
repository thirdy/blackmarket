/*
 * Copyright (C) 2015 thirdy
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.thirdy.blackmarket.fxcontrols;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class TableViewPlus<S> extends TableView<S> {
	
    private FilteredList<S> filteredData;

    /**
     * Just add some sample data in the constructor.
     */
    public TableViewPlus(Map<String, String> columnNameFieldMapping, 
    		double[] minWidths, 
    		ObservableList<S> masterData) {
    	
    	// remove focus
    	setStyle(
    			"-fx-background-color: -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-background-insets: 0, 1, 2; -fx-background-radius: 5, 4, 3;");
    	
    	// 0. Initialize the columns.
    	columnNameFieldMapping.entrySet().stream().forEachOrdered(entry -> {
    		TableColumn<S, String> column = new TableColumn<>(entry.getKey());
    		column.setCellValueFactory(new PropertyValueFactory<S, String>(entry.getValue()));
    		column.impl_setReorderable(false);
    		getColumns().add(column);
    	});
    	
    	IntStream.range(0, getColumns().size())
    		.forEach(i -> getColumns().get(i).setMinWidth(minWidths[i]));
    	
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        filteredData = new FilteredList<>(masterData, p -> true);

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<S> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        setItems(sortedData);
    }
    

    public FilteredList<S> getFilteredData() {
		return filteredData;
	}

//	private void initialize() {
//        // 2. Set the filter Predicate whenever the filter changes.
//        filterField = new TextField();
//        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
//            filteredData.setPredicate(person -> {
//                // If filter text is empty, display all persons.
//                if (newValue == null || newValue.isEmpty()) {
//                    return true;
//                }
//
//                // Compare first name and last name of every person with filter text.
//                String lowerCaseFilter = newValue.toLowerCase();
//
//                if (person.getFirstName().toLowerCase().contains(lowerCaseFilter)) {
//                    return true; // Filter matches first name.
//                } else if (person.getLastName().toLowerCase().contains(lowerCaseFilter)) {
//                    return true; // Filter matches last name.
//                }
//                return false; // Does not match.
//            });
//        });
//    }
}
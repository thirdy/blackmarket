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
package net.thirdy.blackmarket.controls;

import static javafx.collections.FXCollections.observableArrayList;

import java.util.Optional;

import io.jexiletools.es.model.League;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import net.thirdy.blackmarket.domain.Search;
import net.thirdy.blackmarket.domain.SearchEventHandler;
import net.thirdy.blackmarket.domain.Unique;
import net.thirdy.blackmarket.fxcontrols.AutoCompleteComboBoxListener;
import net.thirdy.blackmarket.fxcontrols.MultiStateButton;

/**
 * @author thirdy
 *
 */
public class ControlPane extends BorderPane {
	
	private HBox top;
	
	Button btnLeague;

	private ItemTypePane itemTypesPane;

	private ComboBox<String> tfName;
	private Button btnSearch;
	
	private Label lblHitCount = new Label();
	private Button btnAbout = new Button("About");
	private ToggleButton toggleAdvanceMode = new ToggleButton("Advance Mode");
	
	private TextArea txtAreaJson = new TextArea();

	private GridPane simpleSearchGridPane;
	
	public ControlPane(SearchEventHandler searchEventHandler) {
		// Do not propagate the CTRL key since it will trigger slide of control pane
		txtAreaJson.setOnKeyPressed(e -> {if(e.getCode()==KeyCode.CONTROL) e.consume();});
		
		btnAbout.setOnAction(e -> Dialogs.showAbout());
		
		toggleAdvanceMode.setOnAction(e -> {
			if(toggleAdvanceMode.isSelected()) {
				txtAreaJson.setText(buildSearchInstance(true).buildSearchJson());
				setCenter(txtAreaJson);
			}
			else setCenter(simpleSearchGridPane);
		});
		
		top = new HBox(5);
		top.getChildren().addAll(lblHitCount, newSpacer());
		setTop(top);
		
	    tfName = new ComboBox<>(observableArrayList(Unique.names));
	    new AutoCompleteComboBoxListener<String>(tfName);
		
	    btnLeague = new MultiStateButton(League.names());
	    
	    itemTypesPane = new ItemTypePane();
	    
	    simpleSearchGridPane = new GridPane();
	    simpleSearchGridPane.setPadding(new Insets(5));
	    simpleSearchGridPane.setHgap(5);
	    simpleSearchGridPane.setVgap(5);
	    ColumnConstraints column1 = new ColumnConstraints();
	    column1.setPercentWidth(4);
	    ColumnConstraints column2 = new ColumnConstraints();
	    column2.setPercentWidth(36);
	    ColumnConstraints column3 = new ColumnConstraints();
	    column3.setPercentWidth(1);
	    ColumnConstraints column4 = new ColumnConstraints();
	    column4.setPercentWidth(29);
	    ColumnConstraints column5 = new ColumnConstraints();
	    column5.setPercentWidth(34);
	    column5.setHgrow(Priority.ALWAYS);
	    simpleSearchGridPane.getColumnConstraints().addAll(column1, column2, column3, column4, column5);

	    // Column 1
	    simpleSearchGridPane.add(new Label("League:"), 0, 0);
	    simpleSearchGridPane.add(new Label("Name:"), 0, 1);
	    simpleSearchGridPane.add(new Label("Type:"), 0, 2);

	    // Column 2
	    simpleSearchGridPane.add(btnLeague, 1, 0);
	    simpleSearchGridPane.add(tfName, 1, 1);
	    simpleSearchGridPane.add(itemTypesPane, 1, 2);

	    // Column 3
	    Separator separator = new Separator(Orientation.VERTICAL);
		simpleSearchGridPane.add(separator, 2, 0, 1, 4); // col, row, colspan, rowspan
		
	    // Column 4
		simpleSearchGridPane.add(new Label("Online:"), 3, 0);
		simpleSearchGridPane.add(new Label("Name:"), 3, 1);
		simpleSearchGridPane.add(new Label("Type:"), 3, 2);

	    // Column 5
//		 Slider slider = new Slider(1, 1000, 10);
//		 slider.setShowTickMarks(true);
//		 slider.setShowTickLabels(true);
//		 slider.setMajorTickUnit(200);
//		 slider.setBlockIncrement(10);
//		simpleSearchGridPane.add(slider, 4, 0);
//		simpleSearchGridPane.add(tfName, 1, 1);
//		simpleSearchGridPane.add(itemTypesPane, 1, 2);
	    
		btnSearch = new Button("Search");
		btnSearch.setOnAction(e -> searchEventHandler.search(buildSearchInstance(false)));
		btnSearch.setPrefWidth(400);
//		HBox.setHalignment(btnSearch, HPos.CENTER);
		HBox bottomPane = new HBox(toggleAdvanceMode, newSpacer(), btnSearch, newSpacer(), btnAbout);
		
		GridPane.setHalignment(bottomPane, HPos.CENTER);
//		gridpane.add(btnSearch, 0, 4, 3, 1); // col, row, colspan, rowspan
	    
		setCenter(simpleSearchGridPane);
		setBottom(bottomPane);
	}


	private Region newSpacer() {
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		return spacer;
	}
	
	private Search buildSearchInstance(boolean forTextArea) {
		Optional<String> name = Optional.ofNullable(tfName.getSelectionModel().getSelectedItem());
		Search search = new Search(name, btnLeague.getText(), itemTypesPane.getSelected());
		search.setAdvanceMode(!forTextArea && toggleAdvanceMode.isSelected());
		search.setAdvanceOptionJson(txtAreaJson.getText());
		return search;
	}


	public void installShowCollapseButton(Button showCollapseButton) {
		top.getChildren().add(showCollapseButton);
	}


	public void fireSearchEvent() {
		btnSearch.fire();
	}
	
	public void setSearchHitCount(int count, int show) {
		lblHitCount.setText(String.format("%d items found. Showing %d items.", count, show));
	}
}

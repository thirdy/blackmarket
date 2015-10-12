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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import net.thirdy.blackmarket.domain.Search;
import net.thirdy.blackmarket.domain.SearchEventHandler;
import net.thirdy.blackmarket.domain.Unique;
import net.thirdy.blackmarket.ex.BlackmarketException;
import net.thirdy.blackmarket.fxcontrols.AutoCompleteComboBoxListener;
import net.thirdy.blackmarket.fxcontrols.MultiStateButton;
import net.thirdy.blackmarket.util.SwingUtil;

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
	private Button btnWebsite = new Button();
	
	public ControlPane(SearchEventHandler searchEventHandler) {
		btnWebsite.setId("website-button");
		btnWebsite.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent actionEvent) {
                try {
					SwingUtil.openUrlViaBrowser("http://thirdy.github.io/blackmarket/");
				} catch (BlackmarketException e) {
					Dialogs.showInfo(e.getMessage());
				}
            }
        });
		
		top = new HBox(5);
		Region spacer = new Region();
		HBox.setHgrow(spacer, Priority.ALWAYS);
		top.getChildren().addAll(lblHitCount, spacer, btnWebsite);
		setTop(top);
		
	    tfName = new ComboBox<>(observableArrayList(Unique.names));
	    new AutoCompleteComboBoxListener<String>(tfName);
		
	    btnLeague = new MultiStateButton(League.names());
	    
	    itemTypesPane = new ItemTypePane();
	    
	    GridPane gridpane = new GridPane();
	    gridpane.setPadding(new Insets(5));
	    gridpane.setHgap(5);
	    gridpane.setVgap(5);
	    ColumnConstraints column1 = new ColumnConstraints();
	    column1.setPercentWidth(5);
	    ColumnConstraints column2 = new ColumnConstraints();
	    column2.setPercentWidth(25);
	    ColumnConstraints column3 = new ColumnConstraints();
	    column3.setPercentWidth(70);
	    column2.setHgrow(Priority.ALWAYS);
	    gridpane.getColumnConstraints().addAll(column1, column2, column3);
	    
	    gridpane.add(new Label("League:"), 0, 0);
	    gridpane.add(new Label("Name:"), 0, 1);
	    gridpane.add(new Label("Type:"), 0, 2);
	    
	    gridpane.add(btnLeague, 1, 0);
	    gridpane.add(tfName, 1, 1);
	    gridpane.add(itemTypesPane, 1, 2);
	    
	    Separator separator = new Separator(Orientation.VERTICAL);
		gridpane.add(separator, 2, 0, 1, 4); // col, row, colspan, rowspan
		
//		gridpane.add(new Label("League:"), 0, 0);
//		gridpane.add(new Label("Name:"), 0, 1);
//		gridpane.add(new Label("Type:"), 0, 2);
//		
//		gridpane.add(btnLeague, 1, 0);
//		gridpane.add(tfName, 1, 1);
//		gridpane.add(itemTypesPane, 1, 2);
	    
		btnSearch = new Button("Search");
		btnSearch.setOnAction(e -> searchEventHandler.search(buildSearchInstance()));
		btnSearch.setPrefWidth(400);
		GridPane.setHalignment(btnSearch, HPos.CENTER);
		gridpane.add(btnSearch, 0, 4, 3, 1); // col, row, colspan, rowspan
	    
		setCenter(gridpane);
	}


	private Search buildSearchInstance() {
		Optional<String> name = Optional.ofNullable(tfName.getSelectionModel().getSelectedItem());
		Search search = new Search(name, btnLeague.getText(), itemTypesPane.getSelected());
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

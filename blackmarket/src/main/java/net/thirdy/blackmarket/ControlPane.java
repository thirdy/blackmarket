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
package net.thirdy.blackmarket;

import static javafx.collections.FXCollections.observableArrayList;


import org.controlsfx.control.textfield.TextFields;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.thirdy.blackmarket.controls.AutoCompleteComboBoxListener;
import net.thirdy.blackmarket.controls.ButtonToolBar;
import net.thirdy.blackmarket.controls.ToggleButtonGroup;
import net.thirdy.blackmarket.util.Uniques;

/**
 * @author thirdy
 *
 */
public class ControlPane extends BorderPane {
	
	private HBox top;
//	private GridPane center;
	
	public ControlPane() {
		top = new HBox();
		top.setAlignment(Pos.TOP_RIGHT);
		setTop(top);
		
//		ToggleButton btnScLeague = new ToggleButton();
//		btnScLeague.setId("btnScLeague");
		
//	    final ComboBox<String> nameComboBox = new ComboBox<>(observableArrayList(Uniques.names));
//	    new AutoCompleteComboBoxListener<String>(nameComboBox);
//	    nameComboBox.setEditable(true);
		
		TextField tfName = new TextField();
		
		TextFields.bindAutoCompletion(
				tfName,
	            "Hey", "Hello", "Hello World", "Apple", "Cool", "Costa", "Cola", "Coca Cola");
		
		Button btnLeague = new Button("Flashback Event (IC001)");
	    
//	    comboBox.getItems().addAll("Standard", "Hardcore", "1mo");
		
//		Image scImg = new Image(this.getClass().getResourceAsStream("/images/leagues/sc.png"));
//		btnScLeague.setGraphic(new ImageView(scImg));
	    
//	    ToggleButtonGroup leagueToggleGroup = new ToggleButtonGroup(
//	    			new ToggleButton("Flashback Event (IC001)"),
//	    			new ToggleButton("Flashback Event HC (IC002)"),
//	    			new ToggleButton("Standard"),
//	    			new ToggleButton("Hardcore")
//	    		);
//	    
//	    ButtonToolBar buttonBar = new ButtonToolBar(true, leagueToggleGroup);
		
//	    VBox center = new VBox();
//		center.getChildren().addAll(leagueToggleGroup.getToggleButtons());
//		center.getChildren().addAll(tfName);
	    
//	    GridPane center = new GridPane();
//	    center.setPadding(new Insets(0, 5, 0, 5));
//	    center.add
	    
	    GridPane gridpane = new GridPane();
	    gridpane.setPadding(new Insets(5));
	    gridpane.setHgap(5);
	    gridpane.setVgap(5);
	    ColumnConstraints column1 = new ColumnConstraints(40);
	    ColumnConstraints column2 = new ColumnConstraints(200);
	    column2.setHgrow(Priority.ALWAYS);
	    gridpane.getColumnConstraints().addAll(column1, column2);
//	    column1.set
//	    ColumnConstraints column2 = new ColumnConstraints(50, 150, 300);
	    
	    gridpane.add(new Label("League:"), 0, 0);
	    gridpane.add(new Label("Name:"), 0, 1);
	    
	    gridpane.add(btnLeague, 1, 0);
	    gridpane.add(tfName, 1, 1);
	    
	    Separator separator = new Separator(Orientation.VERTICAL);
		gridpane.add(separator, 2, 0, 1, 2);
	    
	    
		setCenter(gridpane);
	}

	public void installShowCollapseButton(Button showCollapseButton) {
		
//		Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//        top.getChildren().add(spacer);
		
//		showCollapseButton.setAlignment(Pos.CENTER_RIGHT);
//		BorderPane.setAlignment(showCollapseButton, Pos.CENTER_RIGHT);
//		HBox.
		top.getChildren().add(showCollapseButton);
	}
}

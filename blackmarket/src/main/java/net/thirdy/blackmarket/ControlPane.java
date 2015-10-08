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

import java.util.Arrays;
import java.util.TreeSet;

import javax.swing.ButtonGroup;

import org.controlsfx.control.textfield.TextFields;

import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.thirdy.blackmarket.controls.ItemTypePane;
import net.thirdy.blackmarket.controls.autofilltextbox.AutoFillTextBox;
import net.thirdy.blackmarket.fxcontrols.AutoCompleteComboBoxListener;
import net.thirdy.blackmarket.fxcontrols.AutoCompleteTextField;
import net.thirdy.blackmarket.fxcontrols.ButtonToolBar;
import net.thirdy.blackmarket.fxcontrols.MultiStateButton;
import net.thirdy.blackmarket.fxcontrols.ToggleButtonGroup;
import net.thirdy.blackmarket.util.Leagues;
import net.thirdy.blackmarket.util.Uniques;

/**
 * @author thirdy
 *
 */
public class ControlPane extends BorderPane {
	
	private HBox top;
//	private GridPane center;
	
	Button btnLeague;
	
	public ControlPane() {
		top = new HBox();
		top.setAlignment(Pos.TOP_RIGHT);
		setTop(top);
		
	    final ComboBox<String> tfName = new ComboBox<>(observableArrayList(Uniques.names));
	    new AutoCompleteComboBoxListener<String>(tfName);
		
	    btnLeague = new MultiStateButton(Leagues.names());
	    
	    FlowPane itemTypesPane = new ItemTypePane();
	    
//		Image scImg = new Image(this.getClass().getResourceAsStream("/images/leagues/sc.png"));
//		btnScLeague.setGraphic(new ImageView(scImg));
	    
	    
	    GridPane gridpane = new GridPane();
	    gridpane.setPadding(new Insets(5));
	    gridpane.setHgap(5);
	    gridpane.setVgap(5);
	    ColumnConstraints column1 = new ColumnConstraints();
	    column1.setPercentWidth(3);
	    ColumnConstraints column2 = new ColumnConstraints();
	    column2.setPercentWidth(20);
	    ColumnConstraints column3 = new ColumnConstraints();
	    column3.setPercentWidth(77);
	    column2.setHgrow(Priority.ALWAYS);
	    gridpane.getColumnConstraints().addAll(column1, column2, column3);
//	    column1.set
//	    ColumnConstraints column2 = new ColumnConstraints(50, 150, 300);
	    
	    gridpane.add(new Label("League:"), 0, 0);
	    gridpane.add(new Label("Name:"), 0, 1);
	    gridpane.add(new Label("Type:"), 0, 2);
	    gridpane.add(new Label("x:"), 0, 3);
	    gridpane.add(new Label("y:"), 0, 4);
	    
	    gridpane.add(btnLeague, 1, 0);
	    gridpane.add(tfName, 1, 1);
	    gridpane.add(itemTypesPane, 1, 2);
	    
	    Separator separator = new Separator(Orientation.VERTICAL);
		gridpane.add(separator, 2, 0, 1, 14); // col, row, colspan, rowspan
	    
		Button btnSearch = new Button("Search");
		btnSearch.setPrefWidth(400);
		GridPane.setHalignment(btnSearch, HPos.CENTER);
		gridpane.add(btnSearch, 0, 15, 3, 1); // col, row, colspan, rowspan
	    
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

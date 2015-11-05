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

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;

import io.jexiletools.es.model.ItemType;
import io.jexiletools.es.modsmapping.ModsMapping;
import io.jexiletools.es.modsmapping.ModsMapping.ModMapping;
import io.jexiletools.es.modsmapping.ModsMapping.ModType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import net.thirdy.blackmarket.fxcontrols.RangeDoubleTextField;
import net.thirdy.blackmarket.fxcontrols.TableViewPlus;
import net.thirdy.blackmarket.fxcontrols.TriStateCheckBox;

public class ModSelectionPane extends GridPane implements Consumer<List<ItemType>> {

	private TextField filterField;
	private TableViewPlus<ModMapping> modMappingTable;
	private ListView<Mod> modsListView;
	
	private ObservableList<ModMapping> masterData = FXCollections.observableList(new LinkedList<>());

	public ModSelectionPane() {
		setHgap(5.0);
		setMaxHeight(Double.MAX_VALUE);
		setMinHeight(560);
		setAlignment(Pos.CENTER);
		setupModListView();
		accept(Collections.emptyList());
		setupModMappingTable();
		setupFilterTextField();
		
		Button add = addButton();
		add.setPrefWidth(150);
		HBox hBox = new HBox(5, new Label("Filter: "), filterField, add);
		
		VBox.setVgrow(modMappingTable, Priority.ALWAYS);
		VBox left = new VBox(10, hBox, modMappingTable);
		VBox.setVgrow(modsListView, Priority.ALWAYS);
		Label modifiersLbl = new Label("Modifiers");
		modifiersLbl.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
		modifiersLbl.setPadding(new Insets(6));
		VBox right = new VBox(9, new StackPane(modifiersLbl), modsListView);
		
	    setupGridPaneColumns();
		
	    GridPane.setVgrow(left, Priority.ALWAYS);
	    GridPane.setVgrow(right, Priority.ALWAYS);
	    add(left, 0, 0);
	    add(right, 1, 0);
	}

	private void setupGridPaneColumns() {
		ColumnConstraints column1 = new ColumnConstraints();
	    column1.setPercentWidth(50);
	    ColumnConstraints column2 = new ColumnConstraints();
	    column2.setPercentWidth(50);
	    getColumnConstraints().addAll(column1, column2);
	}

	private Button addButton() {
		Button add = new Button("Add");
		add.setOnAction(e -> {
			modMappingTable.getSelectionModel().getSelectedItems()
				.stream().forEachOrdered(mm -> modsListView.getItems().add(new Mod(mm)));
		});
		return add;
	}

	private void setupModMappingTable() {
		modMappingTable = new TableViewPlus<>(ImmutableMap.of(
				"Item Type", "itemType",
				"Type", "modType",
				"Mod", "mapping"
				), 
				new double[] {100, 100, 600},
				masterData);
		modMappingTable.setMaxHeight(Double.MAX_VALUE);
		modMappingTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		modMappingTable.setId("modMappingTable");
	}

	private void setupModListView() {
		modsListView = new ListView<>();
		modsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		modsListView.setMaxHeight(Double.MAX_VALUE);
		// remove focus
		modsListView.setStyle("-fx-background-color: transparent, -fx-inner-border,"
				+ " -fx-body-color; -fx-background-insets: 0, 1, 2; -fx-background-radius: 5, 4, 3;");
		
		modsListView.setCellFactory((listView) -> new ModListCell(listView));
		modsListView.setId("modsListView");
	}

	private void setupFilterTextField() {
		filterField = new TextField();
		filterField.setMinWidth(320);
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			modMappingTable.getFilteredData().setPredicate(modmapping -> {
				// If filter text is empty, display all.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				String[] toks = lowerCaseFilter.split("\\s");
				
				String key = modmapping.getKey().toLowerCase();
				for (String token : toks) {
					if(key.contains(token)) {
						return true;
					}
				}
				return false; // Does not match.
			});
		});
	}

	@Override
	public void accept(List<ItemType> itemTypes) {
		masterData.clear();
		List<ModMapping> modMappings = ModsMapping.getInstance().getModMappings();
		if (!itemTypes.isEmpty()) {
			modMappings = modMappings.stream()
				.filter(mm -> itemTypes.contains(mm.getItemType()) 
						|| mm.getModType() == ModType.PSEUDO)
				.collect(Collectors.toList());
		}
		
		Comparator<ModMapping> byModType = (m1, m2) ->
	    	Integer.valueOf(m1.getModType().ordinal()).compareTo(Integer.valueOf(m2.getModType().ordinal()));
	    	
	    	Comparator<ModMapping> byModTypeThenKey = byModType.thenComparing((m1, m2) ->
	    	m1.getKey().compareTo(m2.getKey())
	    );
		modMappings.sort(byModTypeThenKey);
		masterData.addAll(modMappings);
	}
	
	public static class Mod {
		ModMapping modMapping;
		
		public Mod(ModMapping modMapping) {
			this.modMapping = modMapping;
		}
	}
	
	private static class ModListCell extends ListCell<Mod> {
		
		private Label tfMod = new Label();
		public RangeDoubleTextField rangeDoubleTf = new RangeDoubleTextField();
		public TriStateCheckBox logic = new TriStateCheckBox();
		private Region spacer = new Region();
		public HBox container = new HBox(5.0);
		Button remove = new Button("X");
		
		public ModListCell(ListView<Mod> listView) {
			remove.setOnAction(e -> listView.getItems().remove(getIndex()));
			HBox.setHgrow(spacer, Priority.ALWAYS);
			rangeDoubleTf.setPrefWidth(100);
			container.getChildren().addAll(tfMod, spacer, rangeDoubleTf, logic, remove);
		}
		
		@Override
		protected void updateItem(Mod item, boolean empty) {
			super.updateItem(item, empty);
			if (item != null) {
				String key = item.modMapping.getKey();
				key = StringUtils.removeEnd(key, ".min");
				tfMod.setText(key);
				setGraphic(container);
			} else {
				setGraphic(null);
			}
		}
	}
}
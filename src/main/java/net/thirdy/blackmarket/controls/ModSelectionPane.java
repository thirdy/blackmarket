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
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;

import io.jexiletools.es.model.ItemType;
import io.jexiletools.es.modsmapping.ModsMapping;
import io.jexiletools.es.modsmapping.ModsMapping.ModMapping;
import io.jexiletools.es.modsmapping.ModsMapping.ModType;
import io.jexiletools.es.modsmapping.ModsMapping.Type;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import net.thirdy.blackmarket.domain.RangeOptional;
import net.thirdy.blackmarket.fxcontrols.DoubleTextField;
import net.thirdy.blackmarket.fxcontrols.RangeDoubleTextField;
import net.thirdy.blackmarket.fxcontrols.TableViewPlus;
import net.thirdy.blackmarket.fxcontrols.TriStateButton;
import net.thirdy.blackmarket.fxcontrols.TriStateButton.State;

public class ModSelectionPane extends GridPane implements Consumer<List<ItemType>> {

	private TextField filterField;
	private DoubleTextField tfMinShouldMatch;
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
		tfMinShouldMatch = new DoubleTextField("Minimum number of OR modifiers to match");
		
		Button add = addButton();
		add.setPrefWidth(150);
		HBox hBox = new HBox(5, new Label("Filter: "), filterField, add);
		
		VBox.setVgrow(modMappingTable, Priority.ALWAYS);
		VBox left = new VBox(10, hBox, modMappingTable);
		VBox.setVgrow(modsListView, Priority.ALWAYS);
		Label modifiersLbl = new Label("Modifiers");
		modifiersLbl.setFont(Font.font("Verdana", FontWeight.MEDIUM, 14));
		modifiersLbl.setPadding(new Insets(4));
		VBox right = new VBox(3, new StackPane(modifiersLbl), new HBox(3, new Label("MinOrMatch:"), tfMinShouldMatch), modsListView);
		
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
		modsListView.setEditable(true);
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
		
		public ObjectProperty<Optional<RangeOptional>> lowerRange = new SimpleObjectProperty();
		public ObjectProperty<Optional<RangeOptional>> higherRange = new SimpleObjectProperty();
		
		public ObjectProperty<State> logic = new SimpleObjectProperty<>();
	}
	
	private static class ModListCell extends ListCell<Mod> {
		private Label lblMod = new Label();
		
		public RangeDoubleTextField lowerRangeDoubleTf = new RangeDoubleTextField();
		public RangeDoubleTextField higherRangeDoubleTf = new RangeDoubleTextField();
		
		public TriStateButton logic = new TriStateButton();
		private Region spacer = new Region();
		public HBox container = new HBox(5.0);
		Button remove = new Button("X");
		
		public ModListCell(ListView<Mod> listView) {
			remove.setOnAction(e -> listView.getItems().remove(getIndex()));
			HBox.setHgrow(spacer, Priority.ALWAYS);
			lowerRangeDoubleTf.setPrefWidth(100);
			higherRangeDoubleTf.setPrefWidth(100);
			container.getChildren().addAll(lblMod, spacer, lowerRangeDoubleTf, higherRangeDoubleTf, logic, remove);
		}
		
		@Override
		protected void updateItem(Mod item, boolean empty) {
			super.updateItem(item, empty);
			if (item != null) {
				Type modType = item.modMapping.getType();
				
				// Label
				String key = item.modMapping.getKey();
				key = StringUtils.removeEnd(key, ".min");
				lblMod.setText(key);
				
				// Lower Range
				boolean showLowerRange = modType == Type.DOUBLE || modType == Type.DOUBLE_MIN_MAX;
				lowerRangeDoubleTf.setVisible(showLowerRange);
				item.lowerRange.unbind();
				if (showLowerRange) {
					item.lowerRange.bind(Bindings.createObjectBinding(() -> {
						return lowerRangeDoubleTf.val();
					}, lowerRangeDoubleTf.getMin().textProperty(), lowerRangeDoubleTf.getMax().textProperty()));
				}
				
				// Higher Range
				boolean showHigherRange = modType == Type.DOUBLE_MIN_MAX;
				item.higherRange.unbind();
				if (showHigherRange) {
					item.higherRange.bind(Bindings.createObjectBinding(() -> {
						return higherRangeDoubleTf.val();
					}, higherRangeDoubleTf.getMin().textProperty(), higherRangeDoubleTf.getMax().textProperty()));
				}
				
				item.logic.unbind();
				item.logic.bind(logic.stateProperty());
				setGraphic(container);
			} else {
				setGraphic(null);
			}
		}
	}
	
	public Optional<List<Mod>> mods() {
		return modsListView.getItems().size() == 0 ? Optional.empty() : Optional.of(modsListView.getItems());
	}
	
	public Optional<Double> mininumShouldMatch() {
		return tfMinShouldMatch.getOptionalValue();
	}
}
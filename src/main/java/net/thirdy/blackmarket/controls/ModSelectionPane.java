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

import com.google.common.collect.ImmutableMap;

import io.jexiletools.es.model.ItemType;
import io.jexiletools.es.modsmapping.ModsMapping;
import io.jexiletools.es.modsmapping.ModsMapping.ModMapping;
import io.jexiletools.es.modsmapping.ModsMapping.ModType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import net.thirdy.blackmarket.fxcontrols.RangeDoubleTextField;
import net.thirdy.blackmarket.fxcontrols.TableViewPlus;
import net.thirdy.blackmarket.fxcontrols.TriStateCheckBox;
import net.thirdy.blackmarket.fxcontrols.autocomplete.BlackmarketTextField;

public class ModSelectionPane extends HBox implements Consumer<List<ItemType>> {

	private TextField filterField;
	private TableViewPlus<ModMapping> modMappingTable;
	private ListView<Mod> modsListView;
	
	private ObservableList<ModMapping> masterData = FXCollections.observableList(new LinkedList<>());

	public ModSelectionPane() {
		super(10);
		modsListView = new ListView<>();
		modsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		accept(Collections.emptyList());
		modMappingTable = new TableViewPlus<>(ImmutableMap.of(
				"Item Type", "itemType",
				"Type", "modType",
				"Mod", "mapping"
				), 
				new double[] {100, 100, 600},
				masterData);
		modMappingTable.setMinWidth(800);
		modMappingTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		filterField = new TextField();
		filterField.setMinWidth(350);
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

//		AnchorPane.setBottomAnchor(modMappingTable, 10.0);
//		AnchorPane.setLeftAnchor(modMappingTable, 10.0);
//		AnchorPane.setRightAnchor(modMappingTable, 10.0);
//		AnchorPane.setTopAnchor(modMappingTable, 50.0);
		
		HBox hBox = new HBox(new Label("Filter: "), filterField);
//		AnchorPane.setLeftAnchor(hBox, 10.0);
//		AnchorPane.setRightAnchor(hBox, 10.0);
//		AnchorPane.setTopAnchor(hBox, 10.0);
		
		Button add = new Button("Add");
		add.setOnAction(e -> {
			modMappingTable.getSelectionModel().getSelectedItems()
				.stream().forEachOrdered(mm -> modsListView.getItems().add(new Mod(mm)));
		});
		
		Button remove = new Button("Remove");
		remove.setOnAction(e -> {
			ObservableList<Integer> selectedIndices = modsListView.getSelectionModel().getSelectedIndices();
			for (Integer integer : selectedIndices) {
				modsListView.getItems().remove(integer.intValue());
			}
		});
		
		VBox modsListVBox = new VBox(5, new HBox(5, add, remove), modsListView);
		
		getChildren().addAll(new VBox(10, hBox, modMappingTable), modsListVBox);
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
		private ModMapping modMapping;
		
		public Mod(ModMapping modMapping) {
			this.modMapping = modMapping;
		}
	}
	
//	public static class Mod extends HBox {
//		private ModMapping modMapping;
//		public Label tfMod;
//		public RangeDoubleTextField rangeDoubleTf = new RangeDoubleTextField();
////		public TriStateCheckBox logic = new TriStateCheckBox("⊃");
//		public TriStateCheckBox logic = new TriStateCheckBox();
//		
//	    public Mod(ModMapping modMapping) {
//	    	tfMod = new Label(modMapping.getItemType() + ":" + modMapping.getMapping());
//	    	logic.setSelected(true);
//		}
//	}
}
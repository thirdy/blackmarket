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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.jexiletools.es.model.ItemType;
import io.jexiletools.es.modsmapping.ModsMapping;
import io.jexiletools.es.modsmapping.ModsMapping.ModMapping;
import io.jexiletools.es.modsmapping.ModsMapping.ModType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import net.thirdy.blackmarket.Main;
import net.thirdy.blackmarket.fxcontrols.RangeDoubleTextField;
import net.thirdy.blackmarket.fxcontrols.TriStateButton;
import net.thirdy.blackmarket.fxcontrols.autocomplete.BlackmarketTextField;

/**
 * @author thirdy
 *
 */
public class ModsSelectionPane extends GridPane implements Consumer<List<ItemType>> {
	
	private int row = 0;
	
	private Mod implicitMod;

	private List<ModMapping> allImplicits;
	private List<ModMapping> allExplicits;
	
	private ObservableList<ModMapping> implicits;
	private ObservableList<ModMapping> explicits;

	private List<Mod> explicitMods;
	
	public ModsSelectionPane() {
		setGridLinesVisible(Main.DEVELOPMENT_MODE);
		setHgap(8);
		setVgap(5);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setHalignment(HPos.RIGHT);
		column1.setHgrow(Priority.ALWAYS);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(22);
		column2.setHalignment(HPos.CENTER);
		ColumnConstraints column3 = new ColumnConstraints();
		column3.setPercentWidth(10);
		column3.setHalignment(HPos.CENTER);
		getColumnConstraints().addAll(column1, column2, column3);
		
		allImplicits = ModsMapping.getInstance().listByModType(ModType.IMPLICIT);
		allExplicits = ModsMapping.getInstance().listByModType(ModType.PSEUDO);
		allExplicits.addAll(ModsMapping.getInstance().listByModType(ModType.EXPLICIT));

		explicits = FXCollections.observableArrayList(allExplicits);
		implicits = FXCollections.observableArrayList(allImplicits);
		
		implicitMod = new Mod(implicits, "Implicit Modifier");
		add(implicitMod);
		explicitMods = Arrays.asList(
				new Mod(explicits, "1 Explicit Modifier"),
				new Mod(explicits, "2 Explicit Modifier"),
				new Mod(explicits, "3 Explicit Modifier"),
				new Mod(explicits, "4 Explicit Modifier"),
				new Mod(explicits, "5 Explicit Modifier"),
				new Mod(explicits, "6 Explicit Modifier"),
				new Mod(explicits, "7 Explicit Modifier"),
				new Mod(explicits, "8 Explicit Modifier"),
				new Mod(explicits, "9 Explicit Modifier"),
				new Mod(explicits, "10 Explicit Modifier"),
				new Mod(explicits, "11 Explicit Modifier"),
				new Mod(explicits, "12 Explicit Modifier"),
				new Mod(explicits, "13 Explicit Modifier")
				);
		explicitMods.forEach(m -> add(m));
	}

	
	
	public void clear() {
		getChildren().clear();
		row = 0;
	}

	public void add(Mod mod) {
		add(mod.tfMod, 0, row);
		add(mod.rangeDoubleTf, 1, row);
//		add(mod.logic, 2, row);
		row++;
	}
	
	public void add(Node node) {
		add(node, 0, row, 3, 1);
		row++;
	}
	
	public Optional<Mod> implicit() {
		if (implicitMod.tfMod.getText().isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(implicitMod);
	}
	
	public Optional<List<Mod>> explicitMods() {
		List<Mod> exMods = explicitMods.stream()
		.filter(m -> !m.tfMod.getText().isEmpty())
		.collect(Collectors.toList());
		
		if (exMods.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(exMods);
	}
	
	public static class Mod extends HBox {
		public BlackmarketTextField<ModMapping> tfMod;
		public RangeDoubleTextField rangeDoubleTf = new RangeDoubleTextField();
//		public TriStateCheckBox logic = new TriStateCheckBox("⊃");
//		public TriStateButton logic = new TriStateButton();
		
	    public Mod(ObservableList<ModMapping> list, String prompt) {
	    	tfMod = new BlackmarketTextField<ModMapping>(list, 15);
	    	tfMod.setPromptText(prompt);
//	    	logic.setSelected(true);
		}
	}
	

	@Override
	public void accept(List<ItemType> itemTypes) {
		implicits.clear();
		explicits.clear();
		
		List<ModMapping> imps = allImplicits.stream()
			.filter(m -> 
				m.getModType() == ModType.PSEUDO || itemTypes.contains(m.getItemType()))
			.collect(Collectors.toList());
		implicits.addAll(imps);
		
		List<ModMapping> exs = allExplicits.stream()
				.filter(m -> 
				m.getModType() == ModType.PSEUDO || itemTypes.contains(m.getItemType()))
				.collect(Collectors.toList());
		explicits.addAll(exs);
	}

}

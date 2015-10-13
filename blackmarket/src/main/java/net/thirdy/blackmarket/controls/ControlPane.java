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

import static com.google.common.collect.Iterables.toArray;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.collections.FXCollections.observableList;
import static org.elasticsearch.common.lang3.StringUtils.trimToEmpty;
import static org.elasticsearch.index.query.FilterBuilders.andFilter;
import static org.elasticsearch.index.query.FilterBuilders.boolFilter;
import static org.elasticsearch.index.query.FilterBuilders.existsFilter;
import static org.elasticsearch.index.query.FilterBuilders.orFilter;
import static org.elasticsearch.index.query.FilterBuilders.termFilter;
import static org.elasticsearch.index.query.FilterBuilders.rangeFilter;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.elasticsearch.common.lang3.StringUtils;
import org.elasticsearch.index.query.BoolFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.OrFilterBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.jexiletools.es.model.Currencies;
import io.jexiletools.es.model.League;
import io.jexiletools.es.model.Rarity;
import io.jexiletools.es.modsmapping.ModsMapping.ModMapping;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import net.thirdy.blackmarket.Main;
import net.thirdy.blackmarket.controls.ModsSelectionPane.Mod;
import net.thirdy.blackmarket.domain.RangeOptional;
import net.thirdy.blackmarket.domain.SearchEventHandler;
import net.thirdy.blackmarket.domain.Unique;
import net.thirdy.blackmarket.fxcontrols.AutoCompleteComboBoxListener;
import net.thirdy.blackmarket.fxcontrols.FourColorIntegerTextField;
import net.thirdy.blackmarket.fxcontrols.RangeDoubleTextField;
import net.thirdy.blackmarket.fxcontrols.RangeIntegerTextField;
import net.thirdy.blackmarket.fxcontrols.SmallCurrencyIcon;
import net.thirdy.blackmarket.fxcontrols.TriStateCheckBox;
import net.thirdy.blackmarket.fxcontrols.TriStateCheckBox.State;
import net.thirdy.blackmarket.fxcontrols.TwoColumnGridPane;

/**
 * @author thirdy
 *
 */
public class ControlPane extends BorderPane {
	
	private HBox top;
	
	private ComboBox<String> cmbxLeague;

	private ItemTypePane itemTypesPane;

	private ComboBox<String> tfName;
	private Button btnSearch;
	private Button btnDurianMode = new Button("Durian Mode");
	
	private Label lblHitCount = new Label();
	private Button btnAbout = new Button("About");
	private ToggleButton toggleAdvanceMode = new ToggleButton("Advance Mode");
	
	private TextArea txtAreaJson = new TextArea();

	private GridPane simpleSearchGridPane;

	private RangeDoubleTextField tfDPS = new RangeDoubleTextField();
	private RangeDoubleTextField tfeDPS = new RangeDoubleTextField();
	private RangeDoubleTextField tfpDPS = new RangeDoubleTextField();
	private RangeDoubleTextField tfAPS = new RangeDoubleTextField();
	private RangeDoubleTextField tfCritChance = new RangeDoubleTextField();
	private TriStateCheckBox btn3Corrupt = new TriStateCheckBox		("Corrupted      ");
	private TriStateCheckBox btn3Identified = new TriStateCheckBox	("Identified       ");
	private TriStateCheckBox btn3Crafted = new TriStateCheckBox		("Bench Crafted");
	
	private RangeIntegerTextField tfArmour = new RangeIntegerTextField();
	private RangeIntegerTextField tfEvasion = new RangeIntegerTextField();
	private RangeIntegerTextField tfEnergyShield = new RangeIntegerTextField();
	private RangeIntegerTextField tfBlock = new RangeIntegerTextField();
	private ComboBox<Rarity> cmbxRarity = new ComboBox<>(FXCollections.observableArrayList(Rarity.values()));
	
	private RangeIntegerTextField tfLvlReq = new RangeIntegerTextField();
	private RangeIntegerTextField tfStrReq = new RangeIntegerTextField();
	private RangeIntegerTextField tfDexReq = new RangeIntegerTextField();
	private RangeIntegerTextField tfIntReq = new RangeIntegerTextField();
	
	private RangeDoubleTextField tfQuality = new RangeDoubleTextField();
	
	private RangeIntegerTextField tfSockets = new RangeIntegerTextField();
	private RangeIntegerTextField tfLink = new RangeIntegerTextField();
	private FourColorIntegerTextField tfSockColors = new FourColorIntegerTextField();
	private FourColorIntegerTextField tfLinks = new FourColorIntegerTextField();
	
	private ToggleButton btnSortByShopUpdate = new ToggleButton("Sort by shop update");
	private PriceControl priceControl = new PriceControl();

	private ModsSelectionPane modsSelectionPane;
	
	public ControlPane(SearchEventHandler searchEventHandler) {
		// Do not propagate the CTRL key since it will trigger slide of control pane
		txtAreaJson.setOnKeyPressed(e -> {if(e.getCode()==KeyCode.CONTROL) e.consume();});
		
		btnAbout.setOnAction(e -> Dialogs.showAbout());
		
		toggleAdvanceMode.setOnAction(e -> {
			if(toggleAdvanceMode.isSelected()) {
				txtAreaJson.setText(buildSimpleSearch());
				setCenter(txtAreaJson);
			}
			else setCenter(simpleSearchGridPane);
		});
		
		top = new HBox(5);
		top.getChildren().addAll(lblHitCount, newSpacer());
		setTop(top);
		
	    tfName = new ComboBox<>(observableArrayList(Unique.names));
	    new AutoCompleteComboBoxListener<String>(tfName);
	    tfName.setPrefWidth(220);
		
	    cmbxLeague = new ComboBox<>(observableList(League.names()));
	    cmbxLeague.setEditable(true);
	    cmbxLeague.getSelectionModel().selectFirst();
	    cmbxLeague.setPrefWidth(220);
	    
	    modsSelectionPane = new ModsSelectionPane();
	    itemTypesPane = new ItemTypePane(modsSelectionPane);
	    
	    simpleSearchGridPane = new GridPane();
	    simpleSearchGridPane.setGridLinesVisible(Main.DEBUG_MODE);
	    simpleSearchGridPane.setPadding(new Insets(5));
	    simpleSearchGridPane.setHgap(5);
	    ColumnConstraints column1 = new ColumnConstraints();
	    column1.setPercentWidth(26);
	    ColumnConstraints column2 = new ColumnConstraints();
	    column2.setPercentWidth(13);
	    ColumnConstraints column3 = new ColumnConstraints();
	    column3.setPercentWidth(13);
	    ColumnConstraints column4 = new ColumnConstraints();
	    column4.setPercentWidth(13);
	    ColumnConstraints column5 = new ColumnConstraints();
	    column5.setPercentWidth(35);
	    
	    simpleSearchGridPane.getColumnConstraints().addAll(column1, column2, column3, column4, column5);

	    // Column 1
	    simpleSearchGridPane.add(new TwoColumnGridPane(
	    		"League:", cmbxLeague,
	    		"Name:"  , tfName,
	    		"Type:"  , itemTypesPane), 0, 0);
	    
	    // Column 2
		TwoColumnGridPane col2Pane = new TwoColumnGridPane(
	    		"DPS:"	 , tfDPS,
	    		"pDPS:"  , tfpDPS,
	    		"eDPS:"  , tfeDPS,
	    		"APS:"  ,  tfAPS,
	    		"CrtC:"  , tfCritChance,
	    		new SmallCurrencyIcon(Currencies.vaal) , btn3Corrupt,
	    		new SmallCurrencyIcon(Currencies.id) , btn3Identified,
	    		new SmallCurrencyIcon(Currencies.fuse) , btn3Crafted
	    		);
		simpleSearchGridPane.add(col2Pane, 1, 0);
		cmbxRarity.getItems().add(Rarity.blank);
	    // Column 3
		simpleSearchGridPane.add(new TwoColumnGridPane(
	    		"Ar:"	, tfArmour,
	    		"Ev:"   , tfEvasion,
	    		"ES:"   , tfEnergyShield,
	    		"Blk:"  , tfBlock,
	    		"Sock:" , tfSockets,
	    		"Link:" , tfLink,
	    		"Rarity", cmbxRarity,
	    		"Player", btnSortByShopUpdate
	    		), 2, 0);
		
		// Column 4
		tfLinks.setDisable(true); // TODO
		simpleSearchGridPane.add(new TwoColumnGridPane(
				"RLvl:"	, tfLvlReq,
				"RStr:"   , tfStrReq,
				"RDex:"   , tfDexReq,
				"RInt:"  , tfIntReq,
				"Q%:"	 , tfQuality,
				"Chrm:" , tfSockColors,
	    		"Lnks:" , tfLinks
				), 3, 0);
		
		// Column 5
		simpleSearchGridPane.add(modsSelectionPane , 4, 0);
		modsSelectionPane.add(priceControl);
		
		btnSearch = new Button("Search");
		btnSearch.setOnAction(e -> searchEventHandler.search(
				toggleAdvanceMode.isSelected() ?
						buildAdvanceSearch()
						: buildSimpleSearch()
				));
		btnSearch.setPrefWidth(400);
		btnDurianMode.setOnAction(e -> Dialogs.showInfo("Durian mode will be implemented next. In Durian mode, your searches will be ran in intervals and notify you if items are found. Stay tuned!", "Durian mode coming soon!"));
		
//		HBox.setHalignment(btnSearch, HPos.CENTER);
		HBox bottomPane = new HBox(toggleAdvanceMode, newSpacer(), btnSearch, newSpacer(), btnDurianMode, btnAbout);
		
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
	
	private String buildAdvanceSearch() {
		return trimToEmpty(txtAreaJson.getText());
	}
	
	private String buildSimpleSearch() {
		List<FilterBuilder> filters = new LinkedList<>();
		String json = null;
		
		// Col 1
		ofNullable(tfName.getSelectionModel().getSelectedItem()).map(s -> filters.add(termFilter("info.name", s)));
		filters.add(termFilter("attributes.league", cmbxLeague.getSelectionModel().getSelectedItem()));
		itemTypesFilter().ifPresent(t -> filters.add(t));
		
		// Col 2
		tfDPS.val().ifPresent(t -> filters.add(t.rangeFilter("properties.Weapon.Total DPS")));
		tfpDPS.val().ifPresent(t -> filters.add(t.rangeFilter("properties.Weapon.Physical DPS")));
		tfeDPS.val().ifPresent(t -> filters.add(t.rangeFilter("properties.Weapon.Elemental DPS")));
		tfAPS.val().ifPresent(t -> filters.add(t.rangeFilter("properties.Weapon.Attacks per Second")));
		tfCritChance.val().ifPresent(t -> filters.add(t.rangeFilter("properties.Critical Strike Chance")));
		if(btn3Corrupt.state() != State.unchecked) filters.add(termFilter("attributes.corrupted", btn3Corrupt.state() == State.checked)); 
		if(btn3Identified.state() != State.unchecked) filters.add(termFilter("attributes.identified", btn3Identified.state() == State.checked)); 
		if(btn3Crafted.state() != State.unchecked) filters.add(
				btn3Crafted.state() == State.checked ? rangeFilter("attributes.craftedModCount").gt(0)
						: rangeFilter("attributes.craftedModCount").from(null)
		); 
		
		// Col 3
		tfArmour.val().ifPresent(t -> filters.add(t.rangeFilter("properties.Armour.Armour")));
		tfEvasion.val().ifPresent(t -> filters.add(t.rangeFilter("properties.Armour.Evasion Rating")));
		tfEnergyShield.val().ifPresent(t -> filters.add(t.rangeFilter("properties.Armour.Energy Shield")));
		tfBlock.val().ifPresent(t -> filters.add(t.rangeFilter("properties.Armour.Chance to Block")));
		tfSockets.val().ifPresent(t -> filters.add(t.rangeFilter("sockets.socketCount")));
		tfLink.val().ifPresent(t -> filters.add(t.rangeFilter("sockets.largestLinkGroup")));
		Optional.ofNullable(cmbxRarity.getSelectionModel().getSelectedItem()).ifPresent(r -> 
		{
			if(StringUtils.trimToNull(r.displayName()) != null) 
				filters.add(termFilter("attributes.rarity", r.displayName()));
		}
		);
		
		// Col 4
		tfLvlReq.val().ifPresent(t -> filters.add(t.rangeFilter("requirements.Level")));
		tfStrReq.val().ifPresent(t -> filters.add(t.rangeFilter("requirements.Str")));
		tfDexReq.val().ifPresent(t -> filters.add(t.rangeFilter("requirements.Dex")));
		tfIntReq.val().ifPresent(t -> filters.add(t.rangeFilter("requirements.Int")));
		tfQuality.val().ifPresent(t -> qualityFilter(t).ifPresent(f -> filters.add(f)));
		tfSockColors.val().ifPresent(s -> filters.add(termFilter("sockets.allSocketsSorted", s)));
		
		// Col 5
		modsSelectionPane.implicit().ifPresent(mod -> filters.add(implicitModFilter(mod)));
		modsSelectionPane.explicitMods().ifPresent(mod -> filters.add(explicitModFilter(mod)));
		priceControl.val().ifPresent(price -> filters.add(price.rangeFilter("shop.chaosEquiv")));
		
		// Final Build
		FilterBuilder filter = FilterBuilders.andFilter(toArray(filters, FilterBuilder.class));
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.filteredQuery(null, filter));
		searchSourceBuilder.sort("shop.chaosEquiv", SortOrder.ASC);
		if(btnSortByShopUpdate.isSelected())
			searchSourceBuilder.sort("shop.updated", SortOrder.DESC);
//		searchSourceBuilder. sort(SortBuilders.
//				fieldSort("shop.chaosEquiv").order(SortOrder.ASC));
//		btnSortByShopUpdate.isSelected()
		
		
//        JsonObject sortJson=  new JsonObject();
//        json. put("sort", sortJson);
//      
//        JsonObject sortDateJson=new JsonObject();
//        sortJson.put("age", sortDateJson);
//        sortDateJson.put("order", "asc");
		
		searchSourceBuilder.size(150);
		json = searchSourceBuilder.toString();

		return json;
	}


	private FilterBuilder explicitModFilter(List<Mod> mod) {
		BoolFilterBuilder exFilter = boolFilter();
		mod.stream()
			.forEach(m -> {
				FilterBuilder fb = null;
				ModMapping selectedMod = m.tfMod.getSelectionModel().getSelectedItem();  
				if (m.rangeDoubleTf.val().isPresent()) {
					fb = m.rangeDoubleTf.val().get().rangeFilter(selectedMod.getKey());
				} else {
					fb = existsFilter(selectedMod.getKey());
				}
				switch (m.logic.state()) {
				case checked:
					exFilter.must(fb);
					break;
				case unchecked:
					exFilter.should(fb);
					break;
				case undefined:
					exFilter.mustNot(fb);
					break;
				}
			});
		return exFilter;
	}

	private FilterBuilder implicitModFilter(Mod mod) {
		BoolFilterBuilder impFil = boolFilter();
		FilterBuilder fb = null;
		ModMapping selectedMod = mod.tfMod.getSelectionModel().getSelectedItem(); 
		if (mod.rangeDoubleTf.val().isPresent()) {
			fb = mod.rangeDoubleTf.val().get().rangeFilter(selectedMod.getKey());
		} else {
			fb = existsFilter(selectedMod.getKey());
		}
		switch (mod.logic.state()) {
		case checked:
			impFil.must(fb);
			break;
		case unchecked:
			impFil.should(fb);
			break;
		case undefined:
			impFil.mustNot(fb);
			break;
		}
		return impFil;
	}


	private Optional<FilterBuilder> qualityFilter(RangeOptional t) {
		if(!itemTypesPane.getSelected().isEmpty()) {
			List<FilterBuilder> qualityFilters = itemTypesPane.getSelected().stream()
				.map(it -> format("properties.%s.Quality", it.itemType()))
				.map(name -> t.rangeFilter(name))
				.collect(Collectors.toList());
			return Optional.of(orFilter(toArray(qualityFilters, FilterBuilder.class)));
		}
		return Optional.empty();
	}
	
	private Optional<OrFilterBuilder> itemTypesFilter() {
		if (!itemTypesPane.getSelected().isEmpty()) {
			List<FilterBuilder> itemTypeFilters = itemTypesPane.getSelected()
					.stream()
					.map(it -> {
						FilterBuilder itFilter = termFilter("attributes.itemType", it.itemType());
						if (it.equipType() != null) {
							itFilter = andFilter(itFilter, termFilter("attributes.equipType", it.equipType()));
						}
						return itFilter;
					})
					.collect(Collectors.toList());
			return Optional.of(orFilter(toArray(itemTypeFilters, FilterBuilder.class)));
		}
		return Optional.empty();
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

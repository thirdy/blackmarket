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

import static io.jexiletools.es.model.Currencies.alch;
import static io.jexiletools.es.model.Currencies.alt;
import static io.jexiletools.es.model.Currencies.bless;
import static io.jexiletools.es.model.Currencies.cart;
import static io.jexiletools.es.model.Currencies.chance;
import static io.jexiletools.es.model.Currencies.chaos;
import static io.jexiletools.es.model.Currencies.chrom;
import static io.jexiletools.es.model.Currencies.divine;
import static io.jexiletools.es.model.Currencies.ex;
import static io.jexiletools.es.model.Currencies.fuse;
import static io.jexiletools.es.model.Currencies.gcp;
import static io.jexiletools.es.model.Currencies.jew;
import static io.jexiletools.es.model.Currencies.mirror;
import static io.jexiletools.es.model.Currencies.regal;
import static io.jexiletools.es.model.Currencies.regret;
import static io.jexiletools.es.model.Currencies.scour;
import static io.jexiletools.es.model.Currencies.vaal;

import java.util.Optional;

import io.jexiletools.es.model.Currencies;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import net.thirdy.blackmarket.domain.RangeOptional;
import net.thirdy.blackmarket.fxcontrols.RangeDoubleTextField;
import net.thirdy.blackmarket.fxcontrols.SmallCurrencyIcon;

/**
 * @author thirdy
 *
 */
public class PriceControl extends HBox {
	ToggleButton btnBuyoutOnly = new ToggleButton("B/o Only");
	ComboBox<Currencies> currenCmbx = new ComboBox<>(FXCollections.observableArrayList(
			chaos,
			ex,
			fuse,
			alch,
			alt,
			jew,
			chance,
			chrom,
			cart,
			regret,
			regal,
			gcp,
			divine,
			scour,
			vaal,
			mirror,
			bless
			));
	RangeDoubleTextField priceMinMax = new RangeDoubleTextField();
	public PriceControl() {
		super(1);
		currenCmbx.setCellFactory(new Callback<ListView<Currencies>, ListCell<Currencies>>() {
			
			@Override
			public ListCell<Currencies> call(ListView<Currencies> param) {
				return new ListCell<Currencies>() {
					 {
						 setContentDisplay(ContentDisplay.LEFT);
					 }
					 @Override protected void updateItem(Currencies item, boolean empty) {
	                        super.updateItem(item, empty);
	                        
	                        if (item == null || empty) {
	                            setGraphic(null);
	                            setText(null);
	                        } else {
	                        	setText(item.displayName());
								setGraphic(new SmallCurrencyIcon(item));
	                        }
	                   }
				};
			}
		});
		currenCmbx.getSelectionModel().selectFirst();
		btnBuyoutOnly.setSelected(true);
		btnBuyoutOnly.setPrefWidth(70);
		currenCmbx.setPrefWidth(200);
		priceMinMax.setPrefWidth(100);
		priceMinMax.getMin().setText("1");
		priceMinMax.getMax().setText("15");
		currenCmbx.disableProperty().bind(btnBuyoutOnly.selectedProperty().not());
		priceMinMax.disableProperty().bind(btnBuyoutOnly.selectedProperty().not());
		getChildren().addAll(btnBuyoutOnly, currenCmbx, priceMinMax);
	}
	public Optional<RangeOptional> val() {
		return !btnBuyoutOnly.isSelected() ?
				Optional.empty() :
					priceMinMax.val().map(p -> cevVals(p)) ;
				
	}
	private RangeOptional cevVals(RangeOptional p) {
		Currencies curren = currenCmbx.getSelectionModel().getSelectedItem();
		Optional<Double> min = p.min.map(m -> curren.cevOf(m));
		Optional<Double> max = p.max.map(m -> curren.cevOf(m));
		RangeOptional ro = new RangeOptional(min, max);
		return ro;
	}
}

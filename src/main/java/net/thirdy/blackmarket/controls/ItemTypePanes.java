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
import java.util.function.Consumer;
import java.util.stream.Collectors;

import io.jexiletools.es.model.Currencies;
import io.jexiletools.es.model.ItemType;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import net.thirdy.blackmarket.fxcontrols.SmallCurrencyIcon;
import net.thirdy.blackmarket.util.ImageCache;

/**
 * @author thirdy
 *
 */

public class ItemTypePanes  {
	
	private static final int PREF_TILE_WIDTH = 105;
	private static final int PREF_TILE_HEIGHT = 30;
	
    List<ToggleButton> itemTypesChkbxs;
    Consumer<List<ItemType>> onChangeConsumer;
    
    private ItemTypePane itemTypePane1;
    private ItemTypePane itemTypePane2;
    private ItemTypePane itemTypePane3;
    
    public ItemTypePanes(Consumer<List<ItemType>> onChangeConsumer) {
    	
		itemTypesChkbxs = Arrays.asList(ItemType.values())
				.stream()
				.map(it -> {
					ToggleButton chbBx = new ToggleButton(it.displayName());
					chbBx.setUserData(it);
					chbBx.setMinWidth(PREF_TILE_WIDTH);
					chbBx.setMinHeight(PREF_TILE_HEIGHT);
					chbBx.setGraphic(new ImageView(ImageCache.getInstance().get(it.icon())));
					chbBx.setContentDisplay(ContentDisplay.RIGHT);
					chbBx.setOnAction(e -> checked());
					return chbBx;
				})
				.collect(Collectors.toList());
		
		itemTypePane1 = new ItemTypePane(itemTypesChkbxs.subList(0, 8));
		itemTypePane2 = new ItemTypePane(itemTypesChkbxs.subList(8, 17));
		itemTypePane3 = new ItemTypePane(itemTypesChkbxs.subList(17, 30));
	}
	
	private void checked() {
		onChangeConsumer.accept(getSelected());
	}

	public List<ItemType> getSelected() {
		List<ItemType> collect = itemTypesChkbxs
				.stream()
				.filter(e -> e.isSelected())
				.map(e -> (ItemType) e.getUserData())
				.collect(Collectors.toList());
		return collect;
	}
	
	private static class ItemTypePane extends TilePane {

		public ItemTypePane(List<ToggleButton> itemTypesChkbxs) {
	    	setPrefTileHeight(PREF_TILE_HEIGHT);
	    	setPrefTileWidth(PREF_TILE_WIDTH);
			setTileAlignment(Pos.TOP_LEFT);
			setPrefColumns(2);
			this.getChildren().addAll(itemTypesChkbxs);
		}
	}

	public ItemTypePane getItemTypePane1() {
		return itemTypePane1;
	}

	public ItemTypePane getItemTypePane2() {
		return itemTypePane2;
	}

	public ItemTypePane getItemTypePane3() {
		return itemTypePane3;
	}
	
}

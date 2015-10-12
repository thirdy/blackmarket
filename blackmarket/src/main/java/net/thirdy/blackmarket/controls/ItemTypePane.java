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
import java.util.stream.Collectors;

import io.jexiletools.es.model.ItemType;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.TilePane;

/**
 * @author thirdy
 *
 */
public class ItemTypePane extends TilePane {
	
    List<CheckBox> itemTypesChkbxs;
    
    public ItemTypePane() {
		super(1, 1);
		setTileAlignment(Pos.TOP_LEFT);
		setPrefColumns(4);
		itemTypesChkbxs = Arrays.asList(ItemType.values())
				.stream()
				.map(it -> {
					CheckBox chbBx = new CheckBox(it.displayName());
					chbBx.setUserData(it);
					return chbBx;
				})
				.collect(Collectors.toList());
		
//        DropShadow ds1 = new DropShadow();
//        ds1.setOffsetY(0.1f);
//        ds1.setOffsetX(0.0f);
//        ds1.setColor(Color.BLACK);
        
//        DropShadow ds2 = new DropShadow();
//        ds2.setOffsetY(0.3f);
//        ds2.setOffsetX(0.3f);
//        ds2.setColor(Color.GHOSTWHITE);
		
//		itemTypesChkbxs.subList(0, 8).stream().forEach(c -> c.setEffect(ds1));
//		itemTypesChkbxs.subList(19, 28).stream().forEach(c -> c.setEffect(ds1));
		this.getChildren().addAll(itemTypesChkbxs);
	}
	
	public List<ItemType> getSelected() {
		List<ItemType> collect = itemTypesChkbxs
				.stream()
				.filter(e -> e.isSelected())
				.map(e -> (ItemType) e.getUserData())
				.collect(Collectors.toList());
		return collect;
	}
}

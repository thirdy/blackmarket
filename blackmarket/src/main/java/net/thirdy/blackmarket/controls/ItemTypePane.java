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

import javafx.scene.control.CheckBox;
import javafx.scene.layout.FlowPane;

/**
 * @author thirdy
 *
 */
public class ItemTypePane extends FlowPane {
	
	CheckBox chkbxArmour = new CheckBox("Body Armour");
    CheckBox chkbxHelm = new CheckBox("Helm");
    CheckBox chkbxGloves = new CheckBox("Gloves");
    CheckBox chkbx1h = new CheckBox("1h");
    CheckBox chkbx2h = new CheckBox("2h");
    CheckBox chkbxBow = new CheckBox("Bow");
    CheckBox chkbxAxe = new CheckBox("Axe");
    CheckBox chkbxMace = new CheckBox("Mace");
    CheckBox chkbxAxe2h = new CheckBox("Axe 2h");
    CheckBox chkbxMace2h = new CheckBox("Mace 2h");
    
    List<CheckBox> itemTypes;
    
	public ItemTypePane() {
		super(3, 3);
		itemTypes = Arrays.asList(chkbxArmour, chkbxHelm, chkbxGloves,
	    		chkbx1h, chkbx2h, chkbxBow, chkbxAxe, chkbxMace, chkbxAxe2h, chkbxMace2h);
		this.getChildren().addAll(itemTypes);
	}
	
	
}

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

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/**
 * @author thirdy
 *
 */
public class ButtonToolBar extends ToolBar {

	public ButtonToolBar(boolean isVeritcal, ButtonBase... nodes) {
		this.setOrientation(Orientation.VERTICAL);
        Pane buttonBar = isVeritcal ? new VBox() : new HBox();
        
		for (ButtonBase node : nodes) {
			node.setMaxWidth(Double.MAX_VALUE);
		}
        
//        if (isVeritcal) {
//			((VBox) buttonBar).setFillWidth(true);
//			for (Node node : nodes) {
//				VBox.setVgrow(node, Priority.ALWAYS);
//			}
//		} else {
//			((HBox) buttonBar).setFillHeight(true);
//			for (Node node : nodes) {
//				HBox.setHgrow(node, Priority.ALWAYS);
//			}
//		}
        
//        buttonBar.getStyleClass().setAll("button-tool-bar");
////        ToggleButton sampleButton = new ToggleButton("Tasks");
//        nodes[0].getStyleClass().addAll("first");
////        ToggleButton sampleButton2 = new ToggleButton("Administrator");
////        ToggleButton sampleButton3 = new ToggleButton("Search");
////        Button sampleButton4 = new Button("Line");
////        Button sampleButton5 = new Button("Process");
//        nodes[nodes.length-1].getStyleClass().addAll("last", "capsule");
        buttonBar.getChildren().addAll(nodes);
        this.getItems().addAll(nodes);
	}

	public ButtonToolBar(boolean isVeritcal, ToggleButtonGroup leagueToggleGroup) {
		this(isVeritcal, leagueToggleGroup.getToggleButtons());
	}
}

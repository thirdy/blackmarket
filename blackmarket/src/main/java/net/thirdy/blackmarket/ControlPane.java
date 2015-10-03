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
package net.thirdy.blackmarket;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import net.thirdy.blackmarket.controls.SlidingPane;

/**
 * @author thirdy
 *
 */
public class ControlPane extends BorderPane {
	
	HBox top;
	
	public ControlPane() {
		top = new HBox();
		top.setAlignment(Pos.TOP_RIGHT);
		setTop(top);
	}

	public void installShowCollapseButton(Button showCollapseButton) {
		
//		Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//        top.getChildren().add(spacer);
		
//		showCollapseButton.setAlignment(Pos.CENTER_RIGHT);
//		BorderPane.setAlignment(showCollapseButton, Pos.CENTER_RIGHT);
//		HBox.
		top.getChildren().add(showCollapseButton);
	}
}

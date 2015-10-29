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
package net.thirdy.blackmarket.fxcontrols;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.geometry.Orientation;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToolBar;

/**
 * @author thirdy
 *
 */
public class ToggleButtonToolBar<T extends LabelAndImageDisplayable> extends ToolBar {

	private List<ToggleButton> buttons;

	public ToggleButtonToolBar(boolean isVeritcal, List<T> list) {
		this.setOrientation(Orientation.VERTICAL);
//        Pane buttonBar = isVeritcal ? new VBox() : new HBox();
        
		buttons = list.stream()
				.map(l -> {
					ToggleButton tb = new ToggleButton(l.displayName(), new SmallIcon(l));
					tb.setUserData(l);
					return tb;
				})
				.collect(Collectors.toList());
		
		for (ButtonBase node : buttons) {
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
        this.getItems().addAll(buttons);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> selected() {
		return (List<T>) buttons.stream()
				.filter(b -> b.isSelected())
				.map(b -> b.getUserData())
				.collect(Collectors.toList());
	}
	
	public Optional<List<T>> val() {
		List<T> selected = selected();
		return selected.isEmpty() ? Optional.empty() : Optional.of(selected);
	}
}

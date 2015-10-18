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



import javafx.geometry.HPos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import net.thirdy.blackmarket.Main;

/**
 * @author thirdy
 *
 */
public class TwoColumnGridPane extends GridPane {
	
	private int row = 0;
	
	public TwoColumnGridPane(Object ... labelsAndNodes) {
		this();
		for (int i = 0; i < labelsAndNodes.length; i++) {
			// TODO, refactor this by instantiating a Label here
			if (labelsAndNodes[i] instanceof Node) {
				add((Node) labelsAndNodes[i++], (Node) labelsAndNodes[i]);
			} else {
				add((String) labelsAndNodes[i++], (Node) labelsAndNodes[i]);
			}
			
		}
	}
	
	public TwoColumnGridPane() {
		this(46.0);
	}
	
	public TwoColumnGridPane(Double col1Min) {
		setGridLinesVisible(Main.DEBUG_MODE);
		setHgap(8);
		setVgap(5);
//		setPadding(new Insets(5));
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setMinWidth(col1Min);
		column1.setHalignment(HPos.RIGHT);
		ColumnConstraints column2 = new ColumnConstraints();
//		column2.setPercentWidth(50);
//		column1.setHgrow(Priority.ALWAYS);
		column2.setHalignment(HPos.CENTER);
//		column2.setHgrow(Priority.ALWAYS);
		getColumnConstraints().addAll(column1, column2);
	}

	public void clear() {
		getChildren().clear();
		row = 0;
	}
	
	public void add(String label, Node node) {
		add(new Label(label), 0, row);
		add(node, 1, row);
		row++;
	}
	
	public void add(Node node1, Node node2) {
		add(node1, 0, row);
		add(node2, 1, row);
		row++;
	}
	
}

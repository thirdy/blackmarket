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



import java.text.DecimalFormat;

import io.jexiletools.es.model.json.Range;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * @author thirdy
 *
 */
public class ItemGridCellPropertiesPane extends GridPane {
	
	private int row = 0;
	
	public ItemGridCellPropertiesPane() {
		setHgap(2);
		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(50);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(50);
		column1.setHalignment(HPos.RIGHT);
//		column1.setHgrow(Priority.ALWAYS);
		column1.setHalignment(HPos.RIGHT);
		column2.setHgrow(Priority.ALWAYS);
		getColumnConstraints().addAll(column1, column2);
	}

	public void clear() {
		getChildren().clear();
		row = 0;
	}
	
//	public void add(Node node) {
//		add(node, index % 2, index % 1);
//		index++;
//	}
//	
//	public void add(Label node, String text) {
//		node.setText(text);
//		add(node, index % 2, index % 1);
//		index++;
//	}
	
	public void add(String s) {
		add(new Label(s), 0, row, 2, 1);
		row++;
	}
	
	public void add(String label, String s) {
		add(new Label(label), 0, row);
		add(new Label(s), 1, row);
		row++;
	}
	
	public void add(String label, Double d) {
		DecimalFormat df = new DecimalFormat("#.##");
		add(new Label(label), 0, row);
		add(new Label(df.format(d)), 1, row);
		row++;
	}
	
	public void add(String label, Integer i) {
		add(label, i.toString());
		row++;
	}
	
	public void add(String label, Range d) {
		DecimalFormat df = new DecimalFormat("#.##");
		add(new Label(label), 0, row);
		String minToMax = df.format(d.getMin()) + "-" + df.format(d.getMax());
		add(new Label(minToMax), 1, row);
		row++;
	}
}

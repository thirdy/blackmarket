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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jexiletools.es.model.json.ExileToolsHit;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import net.thirdy.blackmarket.BlackmarketApplication;
import net.thirdy.blackmarket.ex.BlackmarketException;
import net.thirdy.blackmarket.util.ImageCache;
import net.thirdy.blackmarket.util.SwingUtil;

/**
 * @author thirdy
 *
 */
public class Dialogs {
	
	private static final Logger logger = LoggerFactory.getLogger(Dialogs.class.getName());

	public static final String[] comics = new String[]{"craft", "sad", "rhoa"};
	
	public static void showExceptionDialog(Throwable throwable) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("An error occured");
		alert.setHeaderText("Sorry something wrong happened");
//		alert.setGraphic(new ImageView(ImageCache.getInstance().get("/images/gallio/gallio-"
//				+ comics[RandomUtils.nextInt(0, 3)] +
//				".png")));
		alert.setGraphic(new ImageView(ImageCache.getInstance().get("/images/gallio/gallio-sad.png")));
		alert.setContentText("Error: " + throwable.getMessage());

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		throwable.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);
		alert.getDialogPane().setExpanded(true);

		alert.showAndWait();
	}

	public static void showInfo(String string) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("");
		alert.setHeaderText("");
		alert.setContentText(string);
		alert.showAndWait();
	}
	
	public static void showAbout() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setGraphic(new Region());
		alert.setTitle("Blackmarket " + BlackmarketApplication.VERSION);
		alert.setContentText("");
		alert.setHeaderText("");
		
		TextArea textArea = new TextArea("Copyright 2015 Vicente de Rivera III"
				  + "\r\n"
				  + "\r\n http://thirdy.github.io/blackmarket"
				  + "\r\n"
				  + "\r\n This program is free software; you can redistribute it and/or"
				  + "\r\n modify it under the terms of the GNU General Public License"
				  + "\r\n as published by the Free Software Foundation; either version 2"
				  + "\r\n of the License, or (at your option) any later version."
				  + "\r\n"
				  + "\r\n This program is distributed in the hope that it will be useful,"
				  + "\r\n but WITHOUT ANY WARRANTY; without even the implied warranty of"
				  + "\r\n MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the"
				  + "\r\n GNU General Public License for more details."
				  + "\r\n"
				  + "\r\n You should have received a copy of the GNU General Public License"
				  + "\r\n along with this program; if not, write to the Free Software"
				  + "\r\n Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA."
				  + "\r\n"
				);
		textArea.setEditable(false);
		textArea.setWrapText(true);
		
		Hyperlink website = new Hyperlink("Visit Blackmarket Homepage");
		website.setOnAction(e -> {
			try {
				SwingUtil.openUrlViaBrowser("http://thirdy.github.io/blackmarket/");
			} catch (BlackmarketException ex) {
				logger.error(ex.getMessage(), ex);
			}
		});
		
		Hyperlink exwebsite = new Hyperlink("Visit Exile Tools Homepage");
		exwebsite.setOnAction(e -> {
			try {
				SwingUtil.openUrlViaBrowser("http://exiletools.com/");
			} catch (BlackmarketException ex) {
				logger.error(ex.getMessage(), ex);
			}
		});
		
		VBox content = new VBox(
				new ImageView(new Image("/images/blackmarket-logo.png")),
				new Label("Blackmarket is fan-made software for Path of Exile but is not affiliated with Grinding Gear Games in any way."),
				new Label("A few tips:"),
				new Label("CTRL - hot key to slide the search control pane"),
				new Label("SHIFT + Enter - hot key to run the search"),
				new Label("Advance mode grants you power overwhelming of Elastic Search"),
				new Label("For more information and updates,"),
				website,
				new Label("Blackmarket uses Exile Tools Shop Indexer Elastic Search API:"),
				exwebsite,
				new Label("Comics by /u/gallio"),
				new Label("Blackmarket is 100% Free and Open Source Software under GPLv2:"),
				textArea
				);
		content.setSpacing(8);
		alert.getDialogPane().setContent(content);
		alert.showAndWait();
		

	}
}

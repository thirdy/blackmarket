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

import com.google.gson.GsonBuilder;

import io.jexiletools.es.model.json.ExileToolsHit;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import net.thirdy.blackmarket.ex.BlackmarketException;
import net.thirdy.blackmarket.util.SwingUtil;

/**
 * @author thirdy
 *
 */
public class ItemDialog extends Alert {
	
	public static void show(ExileToolsHit item) {
		new ItemDialog(item).show();
	}

	public ItemDialog(ExileToolsHit item) {
		super(AlertType.INFORMATION);
		setTitle(item.getInfo().getFullName());
		setHeaderText("");
		
		String json = new GsonBuilder().setPrettyPrinting().create()
				.toJson(item.getHitJsonObject());
		
		TextArea textArea = new TextArea(json);
		textArea.setEditable(true);
		textArea.setWrapText(false);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMinWidth(650);
		textArea.setMinHeight(550);
		
		String shopUrl = "https://www.pathofexile.com/forum/view-thread/" + item.getShop().getThreadid();
		Hyperlink shopLink = new Hyperlink(shopUrl);
		shopLink.setOnAction(e -> openLink(shopUrl));
		
		getDialogPane().setContent(new VBox(textArea, shopLink));
		initModality(Modality.NONE);
	}
	
	private void openLink(String shopUrl) {
		try {
			SwingUtil.openUrlViaBrowser(shopUrl);
		} catch (BlackmarketException e) {
			Dialogs.showExceptionDialog(e);
		}
	}
}

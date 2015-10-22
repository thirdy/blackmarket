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

import org.apache.commons.lang3.StringUtils;

import io.jexiletools.es.model.json.ExileToolsHit;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import net.thirdy.blackmarket.util.LangContants;

/**
 * @author thirdy
 *
 */
public class LadderHitsDialog extends Alert {
	
	public static void show(ExileToolsHit item) {
		new LadderHitsDialog(item).show();
	}

	public LadderHitsDialog(ExileToolsHit item) {
		super(AlertType.INFORMATION);
		String title = item.getShop().getSellerAccount() + (
				   StringUtils.isBlank(item.getShop().getSellerIGN()) ? LangContants.STRING_EMPTY : 
							(" - " + item.getShop().getSellerIGN())
				);
		setTitle(title);
		setHeaderText("");
		setGraphic(null);
		String json = item.getLadderHits().toString();
		
		TextArea textArea = new TextArea(json);
		textArea.setEditable(true);
		textArea.setWrapText(false);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMinHeight(400);
		
		getDialogPane().setContent(textArea);
		initModality(Modality.NONE);
	}
}

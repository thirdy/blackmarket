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

import static java.lang.String.format;

import java.util.List;
import java.util.stream.Collectors;

import org.controlsfx.control.GridView;

import io.jexiletools.es.model.json.ExileToolsHit;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.thirdy.blackmarket.service.Ladder;

/**
 * @author thirdy
 *
 */
public class SearchResultsPane extends GridView<ExileToolsHit> {

	private ObservableList<ExileToolsHit> originalList;
	
	private BooleanProperty onlineOnly = new SimpleBooleanProperty();
	public BooleanProperty onlineOnlyProperty() { return onlineOnly; }
	
	private ObjectProperty<Ladder> ladder = new SimpleObjectProperty<>();
	public ObjectProperty<Ladder> ladderProperty() {return ladder;}

	private StringProperty searchLabelStatus = new SimpleStringProperty();
	public StringProperty searchLabelStatusProperty() {return searchLabelStatus;}
	
	public SearchResultsPane() {
		onlineOnly.addListener((obv, oldVal, newVal) -> {
			if (newVal != null && originalList != null) {
				boolean eitherWay = !newVal;
				List<ExileToolsHit> list = originalList.stream()
						.filter(hit -> hit.isOnline().orElse(Boolean.FALSE) || eitherWay || hit == ExileToolsHit.EMPTY)
						.collect(Collectors.toList());
				setItems(FXCollections.observableList(list));
				searchLabelStatus.set(format("%d items found. Showing %d%s items.", 
						originalList.size()-3, list.size()-3, newVal ? " online" : ""));
			}
		});
		ladder.addListener((obv, oldVal, newVal) -> {
			if (newVal != null && originalList != null) {
				originalList.stream()
					.filter(hit -> hit != ExileToolsHit.EMPTY)
					.forEach(e -> {
						Ladder ladder = newVal;
						ladder.addPlayerLadderData(e);
					});
			}
		});
	}
	
	public void setSearchResultItems(List<ExileToolsHit> exileToolHits) {
		this.originalList = FXCollections.observableList(exileToolHits);
		// add empty row
		originalList.addAll(ExileToolsHit.EMPTY, ExileToolsHit.EMPTY, ExileToolsHit.EMPTY);
		setItems(originalList);
		searchLabelStatus.set(format("%d items found. Showing %d items.", 
				originalList.size()-3, originalList.size()-3));
	}

}

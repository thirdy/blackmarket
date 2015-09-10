package net.thirdy.blackmarket;
import java.awt.datatransfer.*;
import java.awt.Toolkit;
import static java.lang.String.format;

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem.Mod;

public class ItemPaneController extends AnchorPane {

	@FXML Label id;
	@FXML Hyperlink buyout;
	@FXML Label name;
//	@FXML Hyperlink ign;
	           
	@FXML Label socketsRaw;
	           
	@FXML Hyperlink quality;
	           
	@FXML Hyperlink physDmgRangeAtMaxQuality;
	@FXML Hyperlink physDmgAtMaxQuality;
	@FXML Hyperlink eleDmgRange;
	@FXML Hyperlink attackSpeed;
	@FXML Hyperlink dmgAtMaxQuality;
	@FXML Hyperlink crit;
	@FXML Hyperlink eleDmg;
	           
	@FXML Hyperlink armourAtMaxQuality;
	@FXML Hyperlink evasionAtMaxQuality;
	@FXML Hyperlink energyShieldAtMaxQuality;
	@FXML Hyperlink block;
	           
	@FXML Hyperlink reqLvl;
	@FXML Hyperlink reqStr;
	@FXML Hyperlink reqInt;
	@FXML Hyperlink reqDex;
	           
	@FXML Label ageAndHighLvl;
//	@FXML Hyperlink league;
	@FXML Label seller;
	@FXML Label thread;
//	@FXML Hyperlink sellerid;
//	@FXML Hyperlink threadUrl;
	
	@FXML Hyperlink implicitMod;
	@FXML Hyperlink explicit1;
	@FXML Hyperlink explicit2;
	@FXML Hyperlink explicit3;
	@FXML Hyperlink explicit4;
	@FXML Hyperlink explicit5;
	@FXML Hyperlink explicit6;
	@FXML Hyperlink explicit7;
	
	
private ListView<SearchResultItem> listView;
private SearchResultItem searchResultItem;

	public ItemPaneController() {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ItemPane.fxml"));
		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		try {
			fxmlLoader.load();
		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}
	}

	public ItemPaneController(SearchResultItem item, ListView<SearchResultItem> listView) {
		this();
		this.listView = listView;
		this.searchResultItem = item;
		id.setText(item.getId());
		buyout.setText(item.getBuyout());
		name.setText(item.getName());
//		ign.setText(e.getIgn());

		socketsRaw.setText(item.getSocketsRaw());

		quality.setText(item.getQuality());

		physDmgRangeAtMaxQuality.setText(item.getPhysDmgRangeAtMaxQuality());
		physDmgAtMaxQuality.setText(item.getPhysDmgAtMaxQuality());
		eleDmgRange.setText(item.getEleDmgRange());
		attackSpeed.setText(item.getAttackSpeed());
		dmgAtMaxQuality.setText(item.getDmgAtMaxQuality());
		crit.setText(item.getCrit());
		eleDmg.setText(item.getEleDmg());

		armourAtMaxQuality.setText(item.getArmourAtMaxQuality());
		evasionAtMaxQuality.setText(item.getEvasionAtMaxQuality());
		energyShieldAtMaxQuality.setText(item.getEnergyShieldAtMaxQuality());
		block.setText(item.getBlock());

		reqLvl.setText(item.getReqLvl());
		reqStr.setText(item.getReqStr());
		reqInt.setText(item.getReqInt());
		reqDex.setText(item.getReqDex());

		ageAndHighLvl.setText(item.getAgeAndHighLvl());
//		league.setText(e.getLeague());
		seller.setText(item.getSeller());
		thread.setText(item.getThread());
//		sellerid.setText(e.getSellerid());
//		threadUrl.setText(e.getThreadUrl());
		
		if (item.getImplicitMod() != null) {
			implicitMod.setText(item.getImplicitMod().getName());
			implicitMod.setUserData(item.getImplicitMod());
		}
		
		if (item.getExplicitMods().size() > 7) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText("Problem while displaying explicit mods. Mods are more than 7." + item.getExplicitMods());
			alert.showAndWait();
		}
		
		List<Hyperlink> explicits = Arrays.asList(explicit1,explicit2,explicit3,explicit4,explicit5,explicit6,explicit7);
		int idx = 0;
		for(Mod explicitMod : item.getExplicitMods()) {
			Hyperlink hyperlink = explicits.get(idx);
			hyperlink.setUserData(explicitMod);
			hyperlink.setText(explicitMod.getName());
			idx += 1;
		}
	}

	@FXML
	protected void sortClickHandler(ActionEvent actionEvent) {
		Node node = (Node) actionEvent.getSource();
		System.out.println("sortClickHandler, got the ff id: " + node.getId());
		final String field = node.getId();
		ObservableList<SearchResultItem> items = listView.getItems();
		items.sort(new Comparator<SearchResultItem>() {

			@Override
			public int compare(SearchResultItem item0, SearchResultItem item1) {
				
//				String prop0 = item0.getFieldValue(field);
//				String prop1 = item1.getFieldValue(field);
//				if (prop0 == null || prop0.isEmpty()) {
//					System.out.println(prop1 + ":" + prop0 + ":" + prop1.compareTo(prop0));
//					System.out.println(item0.toString());
//					System.out.println(item1.toString());
//					
//				}
//				return prop1.compareTo(prop0);
				return item0.getName().compareTo(item1.getName());
			
			}
		});
	}
	
	@FXML
	protected void buyClickHandler(ActionEvent actionEvent) {
		SearchResultItem item = searchResultItem;
		TextInputDialog dialog = new TextInputDialog(format(
				"@%s Hi, I would like to buy your %s listed for %s in %s",
					item.getIgn(), item.getName(), item.getBuyout(), item.getLeague()));
//		dialog.setTitle("Text Input Dialog");
//		dialog.setHeaderText("Look, a Text Input Dialog");
//		dialog.setContentText("@TwoWeekGeorgFive Hi, I would like to buy your Generosity listed for 1 chaos in Warbands");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
		    System.out.println("Buy message: " + result.get());
		    StringSelection stringSelection = new StringSelection(result.get());
		    Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		    clpbrd.setContents(stringSelection, null);
		}

//		// The Java 8 way to get the response value (with lambda expression).
//		result.ifPresent(name -> System.out.println("Your name: " + name));
	}

	public SearchResultItem getSearchResultItem() {
		return searchResultItem;
	}
	
	
}

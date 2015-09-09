package net.thirdy.blackmarket;
import java.awt.datatransfer.*;
import java.awt.Toolkit;
import static java.lang.String.format;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;

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

	
private Function<String, Void> sortCallback;
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

	public ItemPaneController(SearchResultItem e, Function<String, Void> function) {
		this();
		this.sortCallback = function;
		this.searchResultItem = e;
		id.setText(e.getId());
		buyout.setText(e.getBuyout());
		name.setText(e.getName());
//		ign.setText(e.getIgn());

		socketsRaw.setText(e.getSocketsRaw());

		quality.setText(e.getQuality());

		physDmgRangeAtMaxQuality.setText(e.getPhysDmgRangeAtMaxQuality());
		physDmgAtMaxQuality.setText(e.getPhysDmgAtMaxQuality());
		eleDmgRange.setText(e.getEleDmgRange());
		attackSpeed.setText(e.getAttackSpeed());
		dmgAtMaxQuality.setText(e.getDmgAtMaxQuality());
		crit.setText(e.getCrit());
		eleDmg.setText(e.getEleDmg());

		armourAtMaxQuality.setText(e.getArmourAtMaxQuality());
		evasionAtMaxQuality.setText(e.getEvasionAtMaxQuality());
		energyShieldAtMaxQuality.setText(e.getEnergyShieldAtMaxQuality());
		block.setText(e.getBlock());

		reqLvl.setText(e.getReqLvl());
		reqStr.setText(e.getReqStr());
		reqInt.setText(e.getReqInt());
		reqDex.setText(e.getReqDex());

		ageAndHighLvl.setText(e.getAgeAndHighLvl());
//		league.setText(e.getLeague());
		seller.setText(e.getSeller());
		thread.setText(e.getThread());
//		sellerid.setText(e.getSellerid());
//		threadUrl.setText(e.getThreadUrl());
		
	}

	@FXML
	protected void sortClickHandler(ActionEvent actionEvent) {
		Node node = (Node) actionEvent.getSource();
		System.out.println("sortClickHandler, got the ff id: " + node.getId());
		sortCallback.apply(node.getId());
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

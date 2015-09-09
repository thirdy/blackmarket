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
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;

public class ItemPaneController extends AnchorPane {
//	@FXML
//	private TextField textField;
	
	@FXML Hyperlink buyout;
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
		buyout.setText(e.getBuyout());
		this.sortCallback = function;
		this.searchResultItem = e;
//		id	.setText(e.get	Id		());
//		buyout	.setText(e.get	Buyout		());
//		name	.setText(e.get	Name		());
//		ign	.setText(e.get	Ign		());
//			.setText(e.get			());
//		socketsRaw	.setText(e.get	SocketsRaw		());
//			.setText(e.get			());
//		quality	.setText(e.get	Quality		());
//			.setText(e.get			());
//		physDmgRangeAtMaxQuality	.setText(e.get	PhysDmgRangeAtMaxQuality		());
//		physDmgAtMaxQuality	.setText(e.get	PhysDmgAtMaxQuality		());
//		eleDmgRange	.setText(e.get	EleDmgRange		());
//		attackSpeed	.setText(e.get	AttackSpeed		());
//		dmgAtMaxQuality	.setText(e.get	DmgAtMaxQuality		());
//		crit	.setText(e.get	Crit		());
//		eleDmg	.setText(e.get	EleDmg		());
//			.setText(e.get			());
//		armourAtMaxQuality	.setText(e.get	ArmourAtMaxQuality		());
//		evasionAtMaxQuality	.setText(e.get	EvasionAtMaxQuality		());
//		energyShieldAtMaxQuality	.setText(e.get	EnergyShieldAtMaxQuality		());
//		block	.setText(e.get	Block		());
//			.setText(e.get			());
//		reqLvl	.setText(e.get	ReqLvl		());
//		reqStr	.setText(e.get	ReqStr		());
//		reqInt	.setText(e.get	ReqInt		());
//		reqDex	.setText(e.get	ReqDex		());
//			.setText(e.get			());
//		ageAndHighLvl	.setText(e.get	AgeAndHighLvl		());
//		league	.setText(e.get	League		());
//		seller	.setText(e.get	Seller		());
//		thread	.setText(e.get	Thread		());
//		sellerid	.setText(e.get	Sellerid		());
//		threadUrl	.setText(e.get	ThreadUrl		());
		
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

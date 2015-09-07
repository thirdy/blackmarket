package net.thirdy.blackmarket;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import net.thirdy.blackmarket.core.PoeTradeHttpClient;
import net.thirdy.blackmarket.core.PoeTradeHttpClientException;

public class FXMLController_complex implements Initializable {
    
    @FXML private Label label;

    @FXML private ToggleGroup toggleGroupType; 

    @FXML private ListView<String> baseItemListView;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws PoeTradeHttpClientException {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
        PoeTradeHttpClient poeTradeHttpClient = new PoeTradeHttpClient();
        String searchPage = poeTradeHttpClient.search("league=Warbands&type=&base=&name=&dmg_min=&dmg_max=&aps_min=&aps_max=&crit_min=&crit_max=&dps_min=&dps_max=&edps_min=&edps_max=&pdps_min=&pdps_max=&armour_min=&armour_max=&evasion_min=&evasion_max=&shield_min=&shield_max=&block_min=&block_max=&sockets_min=&sockets_max=&link_min=&link_max=&sockets_r=&sockets_g=&sockets_b=&sockets_w=&linked_r=&linked_g=&linked_b=&linked_w=&rlevel_min=&rlevel_max=&rstr_min=&rstr_max=&rdex_min=&rdex_max=&rint_min=&rint_max=&impl=&impl_min=&impl_max=&mods=&modexclude=&modmin=&modmax=&mods=&modexclude=&modmin=&modmax=&q_min=&q_max=&level_min=&level_max=&mapq_min=&mapq_max=&rarity=&seller=&thread=&time=2015-08-29&corrupted=&online=&buyout=&altart=&capquality=x&buyout_min=&buyout_max=&buyout_currency=&crafted=&identified=");
        label.setText(searchPage);
    }
    
    
//    @FXML
//    private void handleToggleGroupTypeSelectedChanged(BooleanProperty observable, boolean oldValue, boolean newValue) throws PoeTradeHttpClientException {
//        System.out.println("You clicked me!" + newValue);
//    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	toggleGroupType.selectedToggleProperty().addListener( 
    			(ObservableValue<? extends Toggle> ov, Toggle old_toggle, 
    		    Toggle new_toggle) -> {
    		    	RadioButton r = (RadioButton) new_toggle;
    		    	String val = r.getText();
    		    	// Our UI uses '2h' instead of 'Two Hand'
    		    	if (val.length() > 2) {
    		    		val = StringUtils.replace(val, "2h", "Two Hand");
        		    	val = StringUtils.replace(val, "1h", "One Hand");
					}
    		    	System.out.println(val);
    		    });
    }
}

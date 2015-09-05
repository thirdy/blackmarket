package net.thirdy.blackmarket;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import net.thirdy.blackmarket.core.PoeTradeHttpClient;
import net.thirdy.blackmarket.core.PoeTradeHttpClientException;

public class FXMLController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private void handleButtonAction(ActionEvent event) throws PoeTradeHttpClientException {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
        PoeTradeHttpClient poeTradeHttpClient = new PoeTradeHttpClient();
        String searchPage = poeTradeHttpClient.search("league=Warbands&type=&base=&name=&dmg_min=&dmg_max=&aps_min=&aps_max=&crit_min=&crit_max=&dps_min=&dps_max=&edps_min=&edps_max=&pdps_min=&pdps_max=&armour_min=&armour_max=&evasion_min=&evasion_max=&shield_min=&shield_max=&block_min=&block_max=&sockets_min=&sockets_max=&link_min=&link_max=&sockets_r=&sockets_g=&sockets_b=&sockets_w=&linked_r=&linked_g=&linked_b=&linked_w=&rlevel_min=&rlevel_max=&rstr_min=&rstr_max=&rdex_min=&rdex_max=&rint_min=&rint_max=&impl=&impl_min=&impl_max=&mods=&modexclude=&modmin=&modmax=&mods=&modexclude=&modmin=&modmax=&q_min=&q_max=&level_min=&level_max=&mapq_min=&mapq_max=&rarity=&seller=&thread=&time=2015-08-29&corrupted=&online=&buyout=&altart=&capquality=x&buyout_min=&buyout_max=&buyout_currency=&crafted=&identified=");
        label.setText(searchPage);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}

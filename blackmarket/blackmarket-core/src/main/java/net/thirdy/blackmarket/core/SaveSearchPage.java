/**
 * 
 */
package net.thirdy.blackmarket.core;

import java.io.File;

import org.apache.commons.io.FileUtils;

/**
 * @author thirdy
 *
 */
public class SaveSearchPage {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String payload = "league=Warbands&type=Body+Armour&base=&name=&dmg_min=&dmg_max=&aps_min=&aps_max=&crit_min=&crit_max=&dps_min=&dps_max=&edps_min=&edps_max=&pdps_min=&pdps_max=&armour_min=&armour_max=&evasion_min=&evasion_max=&shield_min=&shield_max=&block_min=&block_max=&sockets_min=&sockets_max=&link_min=&link_max=&sockets_r=&sockets_g=&sockets_b=&sockets_w=&linked_r=&linked_g=&linked_b=&linked_w=&rlevel_min=&rlevel_max=&rstr_min=&rstr_max=&rdex_min=&rdex_max=&rint_min=&rint_max=&impl=&impl_min=&impl_max=&mods=&modexclude=&modmin=&modmax=&mods=%28pseudo%29+%28total%29+%2B%23+to+maximum+Life&modexclude=&modmin=50&modmax=&mods=%28pseudo%29+%2B%23%25+total+Resistance&modexclude=&modmin=30&modmax=&q_min=&q_max=&level_min=&level_max=&mapq_min=&mapq_max=&rarity=&seller=&thread=&time=2015-09-09&corrupted=&online=x&buyout=x&altart=&capquality=x&buyout_min=&buyout_max=&buyout_currency=&crafted=&identified=";
		BackendClient poeTradeHttpClient = new BackendClient();
        String searchPage = poeTradeHttpClient.search(payload, true);
        File file = new File("SearchChest.search.poetrade");
        FileUtils.write(file, searchPage);
	}

}

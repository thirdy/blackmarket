/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.thirdy.blackmarket.core;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author thirdy
 */
public class SearchPageScraper {
    
    public static void main(String[] args) throws Exception {
        PoeTradeHttpClient poeTradeHttpClient = new PoeTradeHttpClient();
        String searchPage = poeTradeHttpClient.search("league=Warbands&type=&base=&name=&dmg_min=&dmg_max=&aps_min=&aps_max=&crit_min=&crit_max=&dps_min=&dps_max=&edps_min=&edps_max=&pdps_min=&pdps_max=&armour_min=&armour_max=&evasion_min=&evasion_max=&shield_min=&shield_max=&block_min=&block_max=&sockets_min=&sockets_max=&link_min=&link_max=&sockets_r=&sockets_g=&sockets_b=&sockets_w=&linked_r=&linked_g=&linked_b=&linked_w=&rlevel_min=&rlevel_max=&rstr_min=&rstr_max=&rdex_min=&rdex_max=&rint_min=&rint_max=&impl=&impl_min=&impl_max=&mods=&modexclude=&modmin=&modmax=&mods=&modexclude=&modmin=&modmax=&q_min=&q_max=&level_min=&level_max=&mapq_min=&mapq_max=&rarity=&seller=&thread=&time=2015-08-29&corrupted=&online=&buyout=&altart=&capquality=x&buyout_min=&buyout_max=&buyout_currency=&crafted=&identified=");
        System.out.println(searchPage);
        System.out.println("Done");
    }
    
    private String page;

    public SearchPageScraper(String page) {
        this.page = page;
    }
    
    public List<SearchResultItem> parse() {
        List<SearchResultItem> items = new LinkedList<>();
        
        return items;
    }
    
    /**
     * Models one item in the search results
     */
    public static class SearchResultItem {
        
    }
    
}

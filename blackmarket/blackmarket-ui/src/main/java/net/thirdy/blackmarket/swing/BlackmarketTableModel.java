package net.thirdy.blackmarket.swing;

import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import net.thirdy.blackmarket.core.SearchPageScraper.SearchResultItem;

public class BlackmarketTableModel extends DefaultTableModel {

	private List<SearchResultItem> data;

	public BlackmarketTableModel(List<SearchResultItem> data) {
		this.data = data;
		String[] columnNames = {
				"ID",
				"B/o",
				"WTB",
				"Name",
				"IGN",
				"Sockets",
				"%", // quality
				"Life",
				"EleRes",
				"pDPS%r", //physDmgRangeAtMaxQuality
				"pDPS%", // physDmgAtMaxQuality
				"eDPSr", // eleDmgRange
				"eDPS",	 // eleDmg
				"DPS%", // dmgAtMaxQuality
				"APS", // attackSpeed
				"Crit",
				"AR%", // armourAtMaxQuality
				"EV%", // evasionAtMaxQuality
				"ES%", // energyShieldAtMaxQuality
				"Block",
				"ReqLvl",
				"ReqStr",
				"ReqInt",
				"ReqDex",
				//"ageAndHighLvl",
				"League",
				//"Seller",
				"Thread"
				//"Sellerid",
				//"ThreadUrl"
		};
		List<Object[]> list = Lists.transform(data, new Function<SearchResultItem, Object[]>() {

			@Override
			public Object[] apply(SearchResultItem item) {
				return new Object[] {
						item.getId(),
						item.getBuyout(),
						"WTB",
						item.getName(),
						item.getIgn(),
						item.getSocketsRaw(),
						item.getQuality(),
						item.getPseudoLife(),
						item.getPseudoEleResistance(),
						item.getPhysDmgRangeAtMaxQuality(),
						item.getPhysDmgAtMaxQuality(),
						item.getEleDmgRange(),
						item.getAttackSpeed(),
						item.getDmgAtMaxQuality(),
						item.getCrit(),
						item.getEleDmg(),
						item.getArmourAtMaxQuality(),
						item.getEvasionAtMaxQuality(),
						item.getEnergyShieldAtMaxQuality(),
						item.getBlock(),
						item.getReqLvl(),
						item.getReqStr(),
						item.getReqInt(),
						item.getReqDex(),
//						item.getAgeAndHighLvl(),
						item.getLeague(),
//						item.getSeller(),
						item.getThread()
//						item.getSellerid(),
//						item.getThreadUrl()
				};
			}
		});
//		Object[][] arr =
//		{
//		    {"Homer", "Simpson", "delete Homer"},
//		    {"Madge", "Simpson", "delete Madge"},
//		    {"Bart",  "Simpson", "delete Bart"},
//		    {"Lisa",  "Simpson", "delete Lisa"},
//		};
		Object[][] arr = new Object[list.size()][];
		list.toArray(arr);
		setDataVector(arr, columnNames);
		
	}

	public String getWantToBuyMessage(int modelRow) {
		return data.get(modelRow).getWTB();
	}
	
	public SearchResultItem getItem(int row) {
		return data.get(row);
	}

}

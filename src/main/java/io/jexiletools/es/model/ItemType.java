package io.jexiletools.es.model;

import java.util.Arrays;

public enum ItemType {
	
	Body("Body", null, "/images/itemtypes/16px-Full_Plate.png"),
	Boots("Boots", null, "/images/itemtypes/16px-Reinforced_Greaves.png"),
	Gloves("Gloves", null, "/images/itemtypes/16px-Steel_Gauntlets.png"),
	Belt("Belt", null, "/images/itemtypes/16px-Leather_Belt.png"),
	Amulet("Amulet", null, "/images/itemtypes/12px-Amber_Amulet.png"),
	Helmet("Helmet", null, "/images/itemtypes/16px-Royal_Burgonet.png"),
	Ring("Ring", null, "/images/itemtypes/12px-Coral_Ring.png"),
	Shield("Shield", null, "/images/itemtypes/16px-Ornate_Spiked_Shield.png"),
	
	Currency("Currency", null, "/images/itemtypes/16px-Chaos_Orb.png"),
	Flask("Flask", null, "/images/itemtypes/24px-sanctified_life_flask.png"),
	Card("Card", null, "/images/itemtypes/24px-Divination_card_inventory_icon.png"),
	Jewel("Jewel", null, "/images/itemtypes/24px-Viridian_Jewel.png"),
	Gem("Gem", null, "/images/itemtypes/24px-shocknova.png"),
	Map("Map", null, "/images/itemtypes/24px-Canyon_Map.png"),
	Unknown("Unknown", null, "/images/itemtypes/16px-questionmark.png"),
	Vaal_Fragment("Vaal Fragment", null, "/images/itemtypes/24px-Sacrifice_at_Dusk.png"),
	Fishing_Rod("Fishing Rod", null, "/images/itemtypes/8px-Fishing_Rod.png"),
	
	Axe("Axe", "One Handed Melee Weapon", "/images/itemtypes/16px-Spectral_Axe.png"),
	Axe2h("Axe", "Two Handed Melee Weapon", "/images/itemtypes/16px-Double_Axe.png"),
	Sword("Sword", "One Handed Melee Weapon", "/images/itemtypes/16px-Elegant_Sword.png"),
	Sword2h("Sword", "Two Handed Melee Weapon", "/images/itemtypes/16px-Ornate_Sword.png"),
	Mace("Mace", "One Handed Melee Weapon", "/images/itemtypes/24px-bladed_mace.png"),
	Mace2h("Mace", "Two Handed Melee Weapon", "/images/itemtypes/16px-Mallet.png"),
	Claw("Claw", null, "/images/itemtypes/16px-Sharktooth_Claw.png"),
	Dagger("Dagger", null, "/images/itemtypes/24px-prong_dagger.png"),
	Bow("Bow", null, "/images/itemtypes/16px-Long_Bow.png"),
	Quiver("Quiver", null, "/images/itemtypes/16px-Conductive_Quiver.png"),
	Sceptre("Sceptre", null, "/images/itemtypes/16px-Quartz_Sceptre.png"),
	Staff("Staff", null, "/images/itemtypes/16px-Primitive_Staff.png"),
	Wand("Wand", null, "/images/itemtypes/16px-driftwood_wand.png");
	
	
	
	private String itemType;
	private String equipType;
	private String icon;

	ItemType(String itemType, String equipType, String icon) {
		this.itemType = itemType;
		this.equipType = equipType;
		this.icon = icon;
	}

	public String itemType() {
		return itemType;
	}
	
	public String equipType() {
		return equipType;
	}
	public String icon() {
		return icon;
	}

	public String displayName() {
		String suffix = equipType() != null ? equipType().startsWith("One") ? "1h" : "2h" : "";
		String display = itemType();
		display = display.equalsIgnoreCase("Vaal Fragment") ? "Frags" : display;
		display = display.equalsIgnoreCase("Fishing Rod") ? "Fish Rod" : display;
		return display + suffix; // TODO make this cleaner
	}

	public static ItemType fromItemType(String itemType) {
		return Arrays.asList(values())
			.stream()
			.filter(e -> e.itemType.equalsIgnoreCase(itemType))
			.findFirst()
			.orElse(ItemType.Unknown);
	}
}
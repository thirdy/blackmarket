package io.jexiletools.es.model;

import java.util.Arrays;

public enum ItemType {
	
	Body("Body"),
	Boots("Boots"),
	Gloves("Gloves"),
	Belt("Belt"),
	Amulet("Amulet"),
	Helmet("Helmet"),
	Ring("Ring"),
	Shield("Shield"),
	Quiver("Quiver"),
	
	Currency("Currency"),
	Flask("Flask"),
	Card("Card"),
	Fishing_Rod("Fishing Rod"),
	Jewel("Jewel"),
	Gem("Gem"),
	Map("Map"),
	Unknown("Unknown"),
	Vaal_Fragment("Vaal Fragment"),
	
	Axe("Axe", "One Handed Melee Weapon"),
	Axe2h("Axe", "Two Handed Melee Weapon"),
	Sword("Sword", "One Handed Melee Weapon"),
	Sword2h("Sword", "Two Handed Melee Weapon"),
	Mace("Mace", "One Handed Melee Weapon"),
	Mace2h("Mace", "Two Handed Melee Weapon"),
	Claw("Claw"),
	Dagger("Dagger"),
	Bow("Bow"),
	Sceptre("Sceptre"),
	Staff("Staff"),
	Wand("Wand");
	
	private String itemType;
	private String equipType;

	ItemType(String itemType) {
		this.itemType = itemType;
	}
	
	ItemType(String itemType, String equipType) {
		this.itemType = itemType;
		this.equipType = equipType;
	}

	public String itemType() {
		return itemType;
	}
	
	public String equipType() {
		return equipType;
	}
	
	public String displayName() {
		String suffix = equipType() != null ? equipType().startsWith("One") ? "1h" : "2h" : "";
		return itemType() + suffix; // TODO make this cleaner
	}

	public static ItemType fromItemType(String itemType) {
		return Arrays.asList(values())
			.stream()
			.filter(e -> e.itemType.equalsIgnoreCase(itemType))
			.findFirst()
			.orElse(ItemType.Unknown);
	}
}
package io.jexiletools.es.model.json;

import io.jexiletools.es.model.Rarity;

public class Attributes {
	String baseItemType;
	Boolean corrupted;
	Integer cosmeticModCount;
	Integer craftedModCount;
	String equipType;
	Integer explicitModCount;
	Integer explictModCount;
	Integer frameType;
	Boolean identified;
	Integer implicitModCount;
	Integer inventoryHeight;
	Integer inventoryWidth;
	String itemType;
	String league;
	Boolean lockedToCharacter;
	Boolean mirrored;
	String rarity;
	Boolean support;
	@Override
	public String toString() {
		return "Attributes [baseItemType=" + baseItemType + ", corrupted=" + corrupted + ", cosmeticModCount="
				+ cosmeticModCount + ", craftedModCount=" + craftedModCount + ", equipType=" + equipType
				+ ", explicitModCount=" + explicitModCount + ", explictModCount=" + explictModCount + ", frameType="
				+ frameType + ", identified=" + identified + ", implicitModCount=" + implicitModCount
				+ ", inventoryHeight=" + inventoryHeight + ", inventoryWidth=" + inventoryWidth + ", itemType="
				+ itemType + ", league=" + league + ", lockedToCharacter=" + lockedToCharacter + ", mirrored="
				+ mirrored + ", rarity=" + rarity + ", support=" + support + "]";
	}
	public String getBaseItemType() {
		return baseItemType;
	}
	public void setBaseItemType(String baseItemType) {
		this.baseItemType = baseItemType;
	}
	public Boolean getCorrupted() {
		return corrupted;
	}
	public void setCorrupted(Boolean corrupted) {
		this.corrupted = corrupted;
	}
	public Integer getCosmeticModCount() {
		return cosmeticModCount;
	}
	public void setCosmeticModCount(Integer cosmeticModCount) {
		this.cosmeticModCount = cosmeticModCount;
	}
	public Integer getCraftedModCount() {
		return craftedModCount;
	}
	public void setCraftedModCount(Integer craftedModCount) {
		this.craftedModCount = craftedModCount;
	}
	public String getEquipType() {
		return equipType;
	}
	public void setEquipType(String equipType) {
		this.equipType = equipType;
	}
	public Integer getExplicitModCount() {
		return explicitModCount;
	}
	public void setExplicitModCount(Integer explicitModCount) {
		this.explicitModCount = explicitModCount;
	}
	public Integer getExplictModCount() {
		return explictModCount;
	}
	public void setExplictModCount(Integer explictModCount) {
		this.explictModCount = explictModCount;
	}
	public Integer getFrameType() {
		return frameType;
	}
	public void setFrameType(Integer frameType) {
		this.frameType = frameType;
	}
	public Boolean getIdentified() {
		return identified;
	}
	public void setIdentified(Boolean identified) {
		this.identified = identified;
	}
	public Integer getImplicitModCount() {
		return implicitModCount;
	}
	public void setImplicitModCount(Integer implicitModCount) {
		this.implicitModCount = implicitModCount;
	}
	public Integer getInventoryHeight() {
		return inventoryHeight;
	}
	public void setInventoryHeight(Integer inventoryHeight) {
		this.inventoryHeight = inventoryHeight;
	}
	public Integer getInventoryWidth() {
		return inventoryWidth;
	}
	public void setInventoryWidth(Integer inventoryWidth) {
		this.inventoryWidth = inventoryWidth;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getLeague() {
		return league;
	}
	public void setLeague(String league) {
		this.league = league;
	}
	public Boolean getLockedToCharacter() {
		return lockedToCharacter;
	}
	public void setLockedToCharacter(Boolean lockedToCharacter) {
		this.lockedToCharacter = lockedToCharacter;
	}
	public Boolean getMirrored() {
		return mirrored;
	}
	public void setMirrored(Boolean mirrored) {
		this.mirrored = mirrored;
	}
	public String getRarity() {
		return rarity;
	}
	public void setRarity(String rarity) {
		this.rarity = rarity;
	}
	public Boolean getSupport() {
		return support;
	}
	public void setSupport(Boolean support) {
		this.support = support;
	}
	public Rarity getRarityAsEnum() {
		return Rarity.fromDisplayName(getRarity());
	}
	
	
}
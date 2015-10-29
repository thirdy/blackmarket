package io.jexiletools.es.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import net.thirdy.blackmarket.fxcontrols.LabelAndImageDisplayable;

public enum Currencies implements LabelAndImageDisplayable {
	
	// TODO ask Pete if there is a need to dynamically grab CEV from http://exiletools.com/rates
	
	chaos("1", "Chaos Orb", "/images/Currency/CurrencyRerollRare.png"),
	none("0", "NONE", null),
	fuse("0.500", "Orb of Fusing" , "/images/Currency/CurrencyRerollSocketLinks.png"),
	alt("0.062", "Orb of Alteration", "/images/Currency/CurrencyRerollMagic.png"),
	alch("0.500", "Orb of Alchemy", "/images/Currency/CurrencyUpgradeToRare.png"),
	ex("40.000", "Exalted Orb", "/images/Currency/CurrencyAddModToRare.png"),
	unknown("0", "Unknown", null),
	cart("0.333", "Cartographers Chisel", "/images/Currency/CurrencyMapQuality.png"),
	jew("0.125", "Jewellers Orb", "/images/Currency/CurrencyRerollSocketNumbers.png"),
	regal("2.000", "Regal Orb", "/images/Currency/CurrencyUpgradeMagicToRare.png"),
	chance("0.143", "Orb of Chance", "/images/Currency/CurrencyUpgradeRandomly.png"),
	gcp("2.000", "Gemcutters Prism", "/images/Currency/CurrencyGemQuality.png"),
	chrom("0.067", "Chromatic Orb", "/images/Currency/CurrencyRerollSocketColours.png"),
	regret("1.000", "Orb of Regret", "/images/Currency/CurrencyPassiveSkillRefund.png"),
	divine("17.000", "Divine Orb", "/images/Currency/CurrencyModValues.png"),
	scour("0.500", "Orb of Scouring", "/images/Currency/CurrencyConvertToNormal.png"),
	vaal("1.000", "Vaal Orb", "/images/Currency/CurrencyVaal.png"),
	mirror("100", "Mirror of Kalandra", "/images/Currency/CurrencyDuplicate.png"),
	id("0.006", "Scroll of Identity", "/images/Currency/CurrencyIdentification.png"),
	bless("0.750", "Blessed Orb", "/images/Currency/CurrencyImplicitMod.png");
	
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyAddModToMagic.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyAddModToRare.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyArmourQuality.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyConvertToNormal.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyDuplicate.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyFlaskQuality.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyGemQuality.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyIdentification.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyIdentificationFragment.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyImplicitMod.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyImprint.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyImprintOrb.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyMapQuality.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyModValues.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyPassiveSkillRefund.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyPortal.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyRerollMagic.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyRerollMagicShard.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyRerollRare.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyRerollSocketColours.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyRerollSocketLinks.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyRerollSocketNumbers.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyRhoaFeather.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyUpgradeMagicToRare.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyUpgradeRandomly.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyUpgradeToMagic.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyUpgradeToMagicShard.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyUpgradeToRare.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyUpgradeToRareShard.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyVaal.png
//		http://webcdn.pathofexile.com/image/Art/2DItems/Currency/CurrencyWeaponQuality.png
	
	private String displayName;
	private String icon;
	private BigDecimal cev;

	Currencies(String cev, String displayName, String icon) {
		this.cev = new BigDecimal(cev);
		this.displayName = displayName;
		this.icon = icon;
	}
	
	public Double cevOf(BigDecimal d){
		return cev.multiply(d).setScale(2, RoundingMode.CEILING).doubleValue();
	}
	
	public Double cevOf(Double d){
		return cevOf(new BigDecimal(d.toString()));
	}

	public String displayName() {
		return displayName;
	}
	
	public Optional<String> icon() {
		return Optional.ofNullable(icon);
	}
	
	@Override
	public String toString() {
		return displayName;
	}
	
	public static Currencies fromDisplayName(String displayName) {
		return Arrays.asList(values())
			.stream()
			.filter(e -> e.displayName.equalsIgnoreCase(displayName))
			.findFirst()
			.orElseGet(() -> Currencies.unknown);
	}

	public static List<String> validDisplayNames() {
		return Arrays.asList(values()).stream()
				.filter(c -> c != none && c != unknown)
				.map(c -> c.displayName())
				.collect(Collectors.toList());
	}
}

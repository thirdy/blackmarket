package io.jexiletools.es.model.json;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.elasticsearch.common.lang3.StringUtils;

import com.google.gson.JsonObject;

import io.jexiletools.es.model.BaseItemType;
import io.jexiletools.es.model.Mod;
import io.jexiletools.es.model.Price;
import net.thirdy.blackmarket.service.LadderHit;

public class ExileToolsHit {
	public static final ExileToolsHit EMPTY = new ExileToolsHit();
	
	private JsonObject hitJsonObject;
	
	public ExileToolsHit() { }
	
	// Ladder Data
	private Optional<LadderHit> ladderHit = Optional.empty();
	public Optional<Boolean> isOnline() {
		return ladderHit.map(e -> e.online());
	}
	public Optional<Date> lastOnline() {
		return ladderHit.map(e -> e.lastOnline());
	}
	// Ladder Data End
	
	
	String md5sum; //d5f3025826c8dba4bf8b6e182f5ca1a0
	String uuid;   //1319466:d5f3025826c8dba4bf8b6e182f5ca1a0
	
	Info info;
	Shop shop;
	Attributes attributes;
	Sockets sockets;
	Requirements requirements;
	Map<String, Map<String, Object>> properties;
	Map<String, Object> mods;
	Map<String, Object> modsTotal;
	Map<String, String> modsPseudo;
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ExileToolsHit [ladderHit=");
		builder.append(ladderHit);
		builder.append(", md5sum=");
		builder.append(md5sum);
		builder.append(", uuid=");
		builder.append(uuid);
		builder.append(", info=");
		builder.append(info);
		builder.append(", shop=");
		builder.append(shop);
		builder.append(", attributes=");
		builder.append(attributes);
		builder.append(", sockets=");
		builder.append(sockets);
		builder.append(", requirements=");
		builder.append(requirements);
		builder.append(", properties=");
		builder.append(properties);
		builder.append(", mods=");
		builder.append(mods);
		builder.append(", modsTotal=");
		builder.append(modsTotal);
		builder.append(", modsPseudo=");
		builder.append(modsPseudo);
		builder.append("]");
		return builder.toString();
	}

	public Optional<LadderHit> getLadderHit() {
		return ladderHit;
	}
	public void setLadderHit(Optional<LadderHit> ladderHit) {
		this.ladderHit = ladderHit;
	}
	public String getMd5sum() {
		return md5sum;
	}

	public void setMd5sum(String md5sum) {
		this.md5sum = md5sum;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public Sockets getSockets() {
		return sockets;
	}

	public void setSockets(Sockets sockets) {
		this.sockets = sockets;
	}

	public Requirements getRequirements() {
		return requirements;
	}

	public void setRequirements(Requirements requirements) {
		this.requirements = requirements;
	}

	public Map<String, Map<String, Object>> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Map<String, Object>> properties) {
		this.properties = properties;
	}

	public Map<String, Object> getMods() {
		return mods;
	}

	public void setMods(Map<String, Object> mods) {
		this.mods = mods;
	}

	public Map<String, Object> getModsTotal() {
		return modsTotal;
	}

	public void setModsTotal(Map<String, Object> modsTotal) {
		this.modsTotal = modsTotal;
	}
	
	public Map<String, String> getModsPseudo() {
		return modsPseudo;
	}

	public void setModsPseudo(Map<String, String> modsPseudo) {
		this.modsPseudo = modsPseudo;
	}
	
	public JsonObject getHitJsonObject() {
		return hitJsonObject;
	}
	public void setHitJsonObject(JsonObject hitJsonObject) {
		this.hitJsonObject = hitJsonObject;
	}
	
	@SuppressWarnings("unchecked")
	public List<Mod> getExplicitOrCraftedMods() {
		List<Mod> result = new ArrayList<>();
		Map<String, Object> _mods = getMods();
		if (_mods != null && !_mods.isEmpty()) {
			Map<String, Object> itemTypeMods = (Map<String, Object>) _mods.get(getAttributes().getItemType());
			Map<String, Object> explicitMods = (Map<String, Object>) itemTypeMods.get("explicit");
			if (explicitMods != null) {
				result.addAll(
						explicitMods
						.entrySet()
						.stream()
						.map(e -> Mod.fromRaw(e.getKey(), e.getValue()))
						.collect(Collectors.toList())
						);
			}
			Map<String, Object> craftedMods = (Map<String, Object>) itemTypeMods.get("crafted");
			if (craftedMods != null) {
				result.addAll(
						craftedMods
						.entrySet()
						.stream()
						.map(e -> Mod.fromRaw(e.getKey(), e.getValue(), true))
						.collect(Collectors.toList())
						);
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public Optional<Mod> getImplicitMod() {
		Optional<Mod> result = Optional.empty();
		Map<String, Object> _mods = getMods();
		if (_mods != null && !_mods.isEmpty()) {
			Map<String, Object> itemTypeMods = (Map<String, Object>) _mods.get(getAttributes().getItemType());
			Map<String, Object> implicitMods = (Map<String, Object>) itemTypeMods.get("implicit");
			if (implicitMods != null) {
				result = implicitMods
						.entrySet()
						.stream()
						.map(e -> Mod.fromRaw(e.getKey(), e.getValue()))
						.findFirst();
			}
		}
		return result;
	}

	public BaseItemType getBaseItemType() {
		return BaseItemType.fromDisplayName(getAttributes().getBaseItemType());
	}
	
	public Optional<Double> getArmour() {
		return getDoubleFromProperties("Armour");
	}
	
	public Optional<Double> getEnergyShield() {
		return getDoubleFromProperties("Energy Shield");
	}
	
	public Optional<Double> getEvasionRating() {
		return getDoubleFromProperties("Evasion Rating");
	}
	
	public Optional<Double> getChanceToBlock() {
		return getDoubleFromProperties("Chance to Block");
	}
	
	public Optional<Double> getAPS() {
		return getDoubleFromProperties("Attacks per Second");
	}
	
	public Optional<Double> getChaosDPS() {
		return getDoubleFromProperties("Chaos DPS");
	}
	
	public Optional<Range> getChaosDamage() {
		return getRangeFromProperties("Chaos Damage");
	}

	public Optional<Double> getQuality() {
		return getDoubleFromProperties("Quality");
	}
	
	public Optional<Double> getCriticalStrikeChance() {
		return getDoubleFromProperties("Critical Strike Chance");
	}

	public Optional<Double> getTotalDPS() {
		return getDoubleFromProperties("Total DPS");
	}
	
	public Optional<Double> getPhysicalDPS() {
		return getDoubleFromProperties("Physical DPS");
	}
	
	public Optional<Range> getPhysicalDamage() {
		return getRangeFromProperties("Physical Damage");
	}
	
	public Optional<Double> getElementalDPS() {
		return getDoubleFromProperties("Elemental DPS");
	}
	
	public Optional<Range> getElementalDamage() {
		return getRangeFromProperties("Elemental Damage");
	}
	
	public Optional<Double> getStackSize() {
		return getDoubleFromProperties("Stack Size");
	}
	
	public Optional<Integer> getRLvl() {
		return Optional.ofNullable(getRequirements()).map(r -> r.getLevel());
	}
	
	public Optional<Integer> getRStr() {
		return Optional.ofNullable(getRequirements()).map(r -> r.getStrength());
	}
	
	public Optional<Integer> getRDex() {
		return Optional.ofNullable(getRequirements()).map(r -> r.getDexterity());
	}
	
	public Optional<Integer> getRInt() {
		return Optional.ofNullable(getRequirements()).map(r -> r.getIntelligence());
	}
	
	public Optional<Integer> getPseudoChaos() {              
		return Optional.ofNullable(getModsPseudo()).map(m -> m.get("eleResistSumChaos")).map(s -> Integer.valueOf(s));
	}                                                        
	                                                         
	public Optional<Integer> getPseudoCold() {               
		return Optional.ofNullable(getModsPseudo()).map(m -> m.get("eleResistSumCold")).map(s -> Integer.valueOf(s));
	}                                                        
	                                                         
	public Optional<Integer> getPseudoFire() {               
		return Optional.ofNullable(getModsPseudo()).map(m -> m.get("eleResistSumFire")).map(s -> Integer.valueOf(s));
	}                                                        
	                                                         
	public Optional<Integer> getPseudoLightning() {          
		return Optional.ofNullable(getModsPseudo()).map(m -> m.get("eleResistSumLightning")).map(s -> Integer.valueOf(s));
	}                                                        
	                                                         
	public Optional<Integer> getPseudoEleRes() {             
		return Optional.ofNullable(getModsPseudo()).map(m -> m.get("eleResistTotal")).map(s -> Integer.valueOf(s));
	}                                                        
	                                                         
	public Optional<Integer> getPseudoAttr() {               
		return Optional.ofNullable(getModsPseudo()).map(m -> m.get("flatAttributesTotal")).map(s -> Integer.valueOf(s));
	}                                                        
	                                                         
	public Optional<Integer> getPseudoDex() {                
		return Optional.ofNullable(getModsPseudo()).map(m -> m.get("flatSumDex")).map(s -> Integer.valueOf(s));
	}                                                        
	                                                         
	public Optional<Integer> getPseudoInt() {                
		return Optional.ofNullable(getModsPseudo()).map(m -> m.get("flatSumInt")).map(s -> Integer.valueOf(s));
	}                                                        
	                                                         
	public Optional<Integer> getPseudoStr() {                
		return Optional.ofNullable(getModsPseudo()).map(m -> m.get("flatSumStr")).map(s -> Integer.valueOf(s));
	}                                                        
	                                                         
	public Optional<Integer> getPseudoLife() {               
		return Optional.ofNullable(getModsPseudo()).map(m -> m.get("maxLife")).map(s -> Integer.valueOf(s));
	}
	
	private Optional<Double> getDoubleFromProperties(String key) {
		Optional<Map<String, Map<String, Object>>> props = Optional.ofNullable(getProperties());
		
		Optional<Double> quality = props
			.map(e -> e.get(getBaseItemType().displayName()))
			.map(e -> {
				return e.get(key);
			})
			.map(e -> (Double) e);
		return quality;
	}
	
	private Optional<Range> getRangeFromProperties(String key) {
		Optional<Map<String, Map<String, Object>>> props = Optional.ofNullable(getProperties());
		
		@SuppressWarnings("unchecked")
		Optional<Range> quality = props
				.map(e -> e.get(getBaseItemType().displayName()))
				.map(e -> (Map<String, Double>) e.get(key))
				.map(e -> new Range(e));
		return quality;
	}

	public String toWTB() {
		Optional<String> sellerIGN = Optional.ofNullable(StringUtils.trimToNull(getShop().getSellerIGN()));
		Optional<Price> price = getShop().getPrice();
		DecimalFormat df = new DecimalFormat("#.##");
		String priceStr = price.isPresent() ?
				String.format(" listed for %s %s", df.format(price.get().getAmount()), price.get().getCurrency().displayName())
				: "" ;
		
		return sellerIGN.map(ign -> 
			String.format("@%s Hi, I would like to buy your %s%s in %s",
					ign, getInfo().getFullName(), priceStr, getAttributes().getLeague()))
				.orElse(
						"https://www.pathofexile.com/forum/view-thread/" + getShop().getThreadid() 
				);
	}

	// UI variable
	private int wtbCtr = 0;
	public int wtbCtr() {
		return wtbCtr;
	}
	public void incrWtbCtr() {
		this.wtbCtr++;
	}
	
}
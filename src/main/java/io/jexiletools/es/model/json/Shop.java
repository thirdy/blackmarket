package io.jexiletools.es.model.json;

import java.util.Optional;

import org.elasticsearch.common.lang3.StringUtils;

import io.jexiletools.es.model.Currencies;
import io.jexiletools.es.model.Price;

public class Shop {
	  Long	added;
      Double amount;
      Double chaosEquiv;
	  String currency;
	  String forumID;
      String generatedWith;
	  String lastUpdateDB; // "format" : "yyyy-MM-dd HH:mm:ss"
      Long modified;
      Double priceChanges;
	  String sellerAccount;
      String sellerIGN;
	  String threadid;
      Long updated;
      String verified;
	@Override
	public String toString() {
		return "Shop [added=" + added + ", amount=" + amount + ", chaosEquiv=" + chaosEquiv + ", currency="
				+ currency + ", forumID=" + forumID + ", generatedWith=" + generatedWith + ", lastUpdateDB="
				+ lastUpdateDB + ", modified=" + modified + ", priceChanges=" + priceChanges + ", sellerAccount="
				+ sellerAccount + ", sellerIGN=" + sellerIGN + ", threadid=" + threadid + ", updated=" + updated
				+ ", verified=" + verified + "]";
	}
	public Long getAdded() {
		return added;
	}
	public void setAdded(Long added) {
		this.added = added;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public Double getChaosEquiv() {
		return chaosEquiv;
	}
	public void setChaosEquiv(Double chaosEquiv) {
		this.chaosEquiv = chaosEquiv;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getForumID() {
		return forumID;
	}
	public void setForumID(String forumID) {
		this.forumID = forumID;
	}
	public String getGeneratedWith() {
		return generatedWith;
	}
	public void setGeneratedWith(String generatedWith) {
		this.generatedWith = generatedWith;
	}
	public String getLastUpdateDB() {
		return lastUpdateDB;
	}
	public void setLastUpdateDB(String lastUpdateDB) {
		this.lastUpdateDB = lastUpdateDB;
	}
	public Long getModified() {
		return modified;
	}
	public void setModified(Long modified) {
		this.modified = modified;
	}
	public Double getPriceChanges() {
		return priceChanges;
	}
	public void setPriceChanges(Double priceChanges) {
		this.priceChanges = priceChanges;
	}
	public String getSellerAccount() {
		return sellerAccount;
	}
	public void setSellerAccount(String sellerAccount) {
		this.sellerAccount = sellerAccount;
	}
	public String getSellerIGN() {
		return sellerIGN;
	}
	public void setSellerIGN(String sellerIGN) {
		this.sellerIGN = sellerIGN;
	}
	public String getThreadid() {
		return threadid;
	}
	public void setThreadid(String threadid) {
		this.threadid = threadid;
	}
	public Long getUpdated() {
		return updated;
	}
	public void setUpdated(Long updated) {
		this.updated = updated;
	}
	public String getVerified() {
		return verified;
	}
	public void setVerified(String verified) {
		this.verified = verified;
	}
	public Optional<Price> getPrice() {
		if (getAmount() != null 
				&& getAmount() != 0
				&& StringUtils.isNotBlank(getCurrency())) {
			Currencies currencies = Currencies.fromDisplayName(getCurrency());
			if (currencies == Currencies.unknown) {
			}
			return Optional.of(new Price(currencies, getAmount()));
		}
		return Optional.empty();
	}
	
	
}
package com.coins.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MinCoinsResponse {

	@JsonProperty("coins")
	Map<Double, Long> coins;

	@JsonProperty("coins_count")
	Long total;

	@JsonProperty("requested_amount")
	Double amount;

	public MinCoinsResponse() {

	}

	public MinCoinsResponse(Map<Double, Long> coins, long total, double amount) {
		this.coins = coins;
		this.total = total;
		this.amount = amount;
	}

	public Map<Double, Long> getCoins() {
		return coins;
	}

	public void setCoins(Map<Double, Long> coins) {
		this.coins = coins;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}

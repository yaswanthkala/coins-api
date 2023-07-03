package com.coins.service;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MinCoinsResponse {

	@JsonProperty("coins")
	Map<Double, Integer> coins;

	@JsonProperty("coins_count")
	int total;

	@JsonProperty("requested_amount")
	double amount;

	public MinCoinsResponse() {

	}

	public MinCoinsResponse(Map<Double, Integer> coins, int total, double amount) {
		this.coins = coins;
		this.total = total;
		this.amount = amount;
	}

	public Map<Double, Integer> getCoins() {
		return coins;
	}

	public void setCoins(Map<Double, Integer> coins) {
		this.coins = coins;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}

package com.coins.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.coins.exception.PossibilitiesNotFoundException;
import com.coins.model.MinCoinsResponse;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
public class CoinsService {

	Double[] denominations;

	private Map<Double, Long> coins;

	private double currentMaxSum = 0.0d;

	@PostConstruct
	public void display() {
		denominations = new Double[coins.size()];
		int index = 0;
		for (Map.Entry<Double, Long> entry : coins.entrySet()) {
			double denomination = entry.getKey();
			Long count = entry.getValue();
			denominations[index++] = denomination;
			currentMaxSum += (denomination * count);
		}
		System.out.println("Current max sum possible : " + currentMaxSum);
		Arrays.sort(denominations, Collections.reverseOrder());
	}

	public synchronized MinCoinsResponse fetchMinCoinsInfo(BigDecimal value) {
		if (value.doubleValue() > currentMaxSum)
			throw new PossibilitiesNotFoundException();

		BigDecimal requestedAmount = BigDecimal.valueOf(value.doubleValue());
		List<Double> minCoinsList = new ArrayList<>();
		generateCombinations(denominations, value.doubleValue(), minCoinsList);
		
		if (minCoinsList.size() == 0)
			throw new PossibilitiesNotFoundException();

		Map<Double, Long> minCombinationsMap = getCoinsCountMap(minCoinsList);
		updateCoins(minCombinationsMap);
		
		MinCoinsResponse minCoinsResponse = new MinCoinsResponse();
		minCoinsResponse.setCoins(minCombinationsMap);
		minCoinsResponse.setTotal(
				minCombinationsMap.entrySet().stream().map(e -> e.getValue()).mapToLong(Long::longValue).sum());
		minCoinsResponse.setAmount(requestedAmount.doubleValue());
		return minCoinsResponse;

	}
	
	private void updateCoins(Map<Double, Long> minCombinationsMap) {
		for(Map.Entry<Double, Long> entry:minCombinationsMap.entrySet()) {
			Double coin = entry.getKey();
			Long count = entry.getValue();
			coins.put(coin, coins.get(coin) - count);
			currentMaxSum -= (coin * count);
		}
		currentMaxSum = round(currentMaxSum);
		System.out.println("Current max sum possible : " + currentMaxSum);
	}

	public void generateCombinations(Double[] coins, double target, List<Double> minCoinsList) {
		List<Double> currentCombination = new ArrayList<>();
		generate(coins, target, 0, currentCombination, minCoinsList);
	}

	private void generate(Double[] coins, double remaining, int start, List<Double> currentCombination,
			List<Double> minCoinsList) {
		remaining = round(remaining);
		if (remaining == 0) {

			if (minCoinsList.size() == 0 && isWithdrawPossible(currentCombination)) {
				minCoinsList.addAll(currentCombination);
			}
			if (minCoinsList.size() > currentCombination.size() && isWithdrawPossible(currentCombination)) {
				minCoinsList.clear();
				minCoinsList.addAll(currentCombination);
				System.out.println(new Date() + " => " + minCoinsList);
			}
			return;
		}

		for (int i = start; i < coins.length; i++) {
			if (coins[i] <= remaining) {
				currentCombination.add(coins[i]);
				generate(coins, remaining - coins[i], i, currentCombination, minCoinsList);
				currentCombination.remove(currentCombination.size() - 1);
			}
		}
	}

	private boolean isWithdrawPossible(List<Double> combinations) {
		Map<Double, Long> coinCombMap = getCoinsCountMap(combinations);

		boolean isWithDrawPossible = true;
		for (Double coin : combinations) {
			if (coins.get(coin) >= coinCombMap.get(coin)) {
				continue;
			} else {
				isWithDrawPossible = false;
				break;
			}
		}
		return isWithDrawPossible;
	}

	private Map<Double, Long> getCoinsCountMap(List<Double> combinations) {
		return combinations.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}

	private static double round(double value) {
		BigDecimal bd = BigDecimal.valueOf(value);
		bd = bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	public void setCoins(Map<Double, Long> coins) {
		this.coins = coins;
	}

	public Map<Double, Long> getCoins() {
		return this.coins;
	}
}

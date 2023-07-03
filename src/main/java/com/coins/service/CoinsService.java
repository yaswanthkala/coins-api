package com.coins.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import com.coins.exception.PossibilitiesNotFoundException;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
public class CoinsService {

	int[] denominations;

	private Map<Double, Integer> coins;
	private double currentMaxSum = 0.0d;

	@PostConstruct
	public void display() {
		denominations = new int[coins.size()];
		int index = 0;
		for (Map.Entry<Double, Integer> entry : coins.entrySet()) {
			double denomination = entry.getKey();
			Integer count = entry.getValue();
			dnmns.put((int) (denomination * 100), count);
			denominations[index++] = (int) (denomination * 100);
			currentMaxSum += (denomination * count);
		}
		System.out.println("Current max sum possible : " + currentMaxSum);
		Arrays.sort(denominations);
	}

	public MinCoinsResponse fetchMinCoinsInfo(BigDecimal value) {
		if(value.doubleValue() >= currentMaxSum)
			throw new PossibilitiesNotFoundException();
		
		BigDecimal requestedAmount = BigDecimal.valueOf(value.doubleValue());
		Map<Integer, Integer> minCombinationsMap = minimumCoins((int) (value.doubleValue() * 100));
		if (minCombinationsMap.size() == 0)
			throw new PossibilitiesNotFoundException();

		MinCoinsResponse minCoinsResponse = new MinCoinsResponse();
		minCoinsResponse.setCoins(minCombinationsMap.entrySet().stream().collect(HashMap::new,
				(m, e) -> m.put((e.getKey() * 1.0) / 100, e.getValue()), HashMap::putAll));
		minCoinsResponse.setTotal(
				minCombinationsMap.entrySet().stream().map(e -> e.getValue()).mapToInt(Integer::intValue).sum());
		minCoinsResponse.setAmount(requestedAmount.doubleValue());
		return minCoinsResponse;

	}

	public Map<Integer, Integer> minimumCoins(int targetAmount) {
		List<List<Integer>> possibilities = coinChange(denominations, targetAmount);
		if (possibilities == null || possibilities.isEmpty())
			throw new PossibilitiesNotFoundException();

		possibilities.sort(Comparator.comparingInt(List::size));

		Map<Integer, Integer> currMap = new HashMap<>();
		for (List<Integer> combination : possibilities) {
			boolean areCombinationsAvailable = true;
			for (Integer sum : combination) {
				if (dnmns.get(sum) > 0) {
					if (currMap.containsKey(sum)) {
						currMap.put(sum, currMap.get(sum) + 1);
					} else {
						currMap.put(sum, 1);
					}
					dnmns.put(sum, dnmns.get(sum) - 1);
				} else {
					areCombinationsAvailable = false;
					break;
				}
			}
			if (!areCombinationsAvailable) {
				for (Map.Entry<Integer, Integer> entry : currMap.entrySet()) {
					Integer denomination = entry.getKey();
					Integer count = entry.getValue();
					dnmns.put(denomination, dnmns.get(denomination) + count);
				}
				currMap.clear();
			} else {
				System.out.println(currMap);
				for (Map.Entry<Integer, Integer> entry : currMap.entrySet()) {
					Integer denomination = entry.getKey();
					Integer count = entry.getValue();
					dnmns.put(denomination, dnmns.get(denomination) + count);
					currentMaxSum -= (count * ((denomination * 1.0) / 100));					
				}
				System.out.println("Current max sum possible : " + currentMaxSum);
				break;
			}
		}
		return currMap;
	}

	public List<List<Integer>> coinChange(int[] coins, int targetAmount) {
		List<List<Integer>>[] dp = new ArrayList[targetAmount + 1];
		dp[0] = new ArrayList<>();
		dp[0].add(new ArrayList<>());

		for (int amount = coins[0]; amount <= targetAmount; amount++) {
			dp[amount] = new ArrayList<>();
			for (int coin : coins) {
				if (coin <= amount && dp[amount - coin] != null) {
					for (List<Integer> combination : dp[amount - coin]) {
						List<Integer> newCombination = new ArrayList<>(combination);
						newCombination.add(coin);
						dp[amount].add(newCombination);
					}
				}
			}
		}

		return dp[targetAmount];
	}

	private Map<Integer, Integer> dnmns = new HashMap<>();

	public Map<Double, Integer> getCoins() {
		return coins;
	}

	public void setCoins(Map<Double, Integer> coins) {
		this.coins = coins;
	}

	public Map<String, Integer> getCoinsInfo() {
		return dnmns.entrySet().stream().collect(HashMap::new,
				(m, e) -> m.put(String.format("%.2f", (e.getKey() * 1.0) / 100), e.getValue()), HashMap::putAll);
	}
}

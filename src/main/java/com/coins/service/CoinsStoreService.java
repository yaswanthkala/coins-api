package com.coins.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "app")
public class CoinsStoreService {

	int coinTypesCount;
	double[] denos;

	private Map<Double, Integer> coins;
	
	@PostConstruct
	public void after() {
		int index = 0;
		denos = new double[coins.size()];
		for (Map.Entry<Double, Integer> entry : coins.entrySet()) {
			denos[index++] = entry.getKey();
		}
		Arrays.sort(denos);
	}

	

	public MinCoinsResponse fetchMinCoinsInfo(BigDecimal value) {
		BigDecimal requestedAmount = BigDecimal.valueOf(value.doubleValue());
		int counter = 0;
		Map<Double, Integer> currMap = new HashMap<>();

		for (int i = denos.length - 1; i >= 0; i--) {
			while (value.doubleValue() >= denos[i] && coins.get(denos[i]) > 0) {
				value = value.subtract(BigDecimal.valueOf(denos[i]));				
				counter++;
				coins.put(denos[i], coins.get(denos[i]) - 1);

				if (currMap.containsKey(denos[i])) {
					currMap.put(denos[i], currMap.get(denos[i]) + 1);
				} else {
					currMap.put(denos[i], 1);
				}

			}
			if (value.doubleValue() == 0.0) {
				break;
			}
		}
		if (value.doubleValue() == 0.0) {
			MinCoinsResponse minCoinsResponse = new MinCoinsResponse();
			minCoinsResponse.setCoins(currMap);
			minCoinsResponse.setTotal(counter);
			minCoinsResponse.setAmount(requestedAmount.doubleValue());
			return minCoinsResponse;
		} else {
			for (Map.Entry<Double, Integer> entry : currMap.entrySet()) {
				Double denomination = entry.getKey();
				Integer count = entry.getValue();
				coins.put(denomination, coins.get(denomination) + count);
			}
			return null;
		}
	}

	public Map<Double, Integer> getCoins() {
		return coins;
	}

	public void setCoins(Map<Double, Integer> coins) {
		this.coins = coins;
	}
}
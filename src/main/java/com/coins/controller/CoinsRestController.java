package com.coins.controller;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.coins.model.MinCoinsRequest;
import com.coins.service.CoinsService;
import com.coins.service.CoinsStoreService;
import com.coins.service.MinCoinsResponse;

@RestController
@RequestMapping("/coins")
public class CoinsRestController {

	@Autowired
	private CoinsService coinsService;

	@Autowired
	private CoinsStoreService coinsStoreService;

	
	@GetMapping
	public ResponseEntity<Map<Double, Integer>> fetchCurrentAvailableCoinsListTemp() {
		return new ResponseEntity<>(coinsStoreService.getCoins(), HttpStatus.OK);
	}

	@PostMapping(value = "/withdraw", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMinimumCoinsInfoTemp(@RequestBody MinCoinsRequest minCoinsRequest) {
		MinCoinsResponse response = coinsStoreService
				.fetchMinCoinsInfo(BigDecimal.valueOf(minCoinsRequest.getAmount()));
		if (response == null)
			return new ResponseEntity<>(minCoinsRequest.getAmount() + " cannot be formed with the given denominations",
					HttpStatus.BAD_REQUEST);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
/*
	@GetMapping(value = "/temp")
	public ResponseEntity<Map<String, Integer>> fetchCurrentAvailableCoinsList() {
		return new ResponseEntity<>(coinsService.getCoinsInfo(), HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/withdraw/temp", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMinimumCoinsInfo(@RequestBody MinCoinsRequest minCoinsRequest) {
		return new ResponseEntity<>(coinsService.fetchMinCoinsInfo(BigDecimal.valueOf(minCoinsRequest.getAmount())),
				HttpStatus.OK);
	}
*/

}

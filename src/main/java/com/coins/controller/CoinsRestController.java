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

@RestController
@RequestMapping("/coins")
public class CoinsRestController {

	@Autowired
	private CoinsService coinsService;

	@GetMapping
	public ResponseEntity<Map<Double, Long>> getCoinsInfo() {
		return new ResponseEntity<>(coinsService.getCoins(), HttpStatus.OK);
	}

	@PostMapping(value = "/withdraw", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getMinimumCoinsInfo(@RequestBody MinCoinsRequest minCoinsRequest) {
		return new ResponseEntity<>(coinsService.fetchMinCoinsInfo(BigDecimal.valueOf(minCoinsRequest.getAmount())),
				HttpStatus.OK);
	}

}

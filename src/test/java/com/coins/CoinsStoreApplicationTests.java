package com.coins;

import static io.restassured.RestAssured.get;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigInteger;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CoinsStoreApplicationTests {

	@LocalServerPort
	private int port;

	private String uri;

	@PostConstruct
	public void init() {
		uri = "http://localhost:" + port;
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void whenGetCoins_thenOK() {
		get(uri + "/coins").then().assertThat().statusCode(200);
	}

	
	@Test
	public void whenPostWithDrawCoinsWithInvalidAmount_thenBadReqest() {
		String requestBody = "{\"amount\": \"5000.0\"}";
		RestAssured.given().contentType(ContentType.JSON).body(requestBody).when().
		post(uri + "/coins/withdraw").then()
			.statusCode(400);
	}
	

	@Test
	public void whenPostWithdrawCoins_thenOK() {
		String requestBody = "{\"amount\": \"1.0\"}";
		RestAssured.given().contentType(ContentType.JSON).body(requestBody).when().
			post(uri + "/coins/withdraw").then()
				.statusCode(200);	
	}

}

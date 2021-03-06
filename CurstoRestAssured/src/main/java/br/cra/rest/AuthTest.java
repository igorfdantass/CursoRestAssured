package br.cra.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import io.restassured.http.ContentType;


public class AuthTest {
	
	private String token;
	
	@Test
	public void deveAcessarSWAPI() {
		given()
			.log().all()
		.when()
			.get("https://swapi.dev/api/people/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Luke Skywalker"))
		;
	}
	
	@Test
	public void deveObterClima() {
		given()
			.log().all()
			.header("X-RapidAPI-Key", "926e3742d1mshe78f2a84d56356fp18b0d0jsn2d3d65746ee9")
			.queryParam("q", "Monteiro,BR")
			.queryParam("units", "metric")
		.when()
			.get("https://community-open-weather-map.p.rapidapi.com/weather")
		.then()
			.log().all()
			.statusCode(200)
			.body("name", is("Monteiro"))
			.body("sys.country", is("BR"))
		;		
	}
	
	@Test
	public void naoDeveAcessarSemSenha() {
		given()
			.log().all()
		.when()
			.get("http://restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(401)
			.body("name", is("Luke Skywalker"))
		;
	}
	
	@Test
	public void deveAcessarComSenha() {
		given()
			.log().all()
		.when()
			.get("http://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	@Test
	public void deveAcessarComSenha2() {
		given()
			.log().all()
			.auth().basic("admin", "senha")
		.when()
			.get("http://admin:senha@restapi.wcaquino.me/basicauth")
		.then()
			.log().all()
			.statusCode(200)
			.body("status", is("logado"))
		;
	}
	
	
	@Test
	public void deveAcessarCOmTokenJWT() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "igor@fdantas");
		login.put("senha", "igor1234");
		
		this.token = 
		given()
			.log().all()
			.body(login)
			.contentType(ContentType.JSON)
		.when()
			.post("http://barrigarest.wcaquino.me/signin")
		.then()
			.log().all()
			.statusCode(200)
			.extract().path("token")
		;
	}
	
	@Test
	public void deveObterContasComToken() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "igor@fdantas");
		login.put("senha", "igor1234");
		
		this.token = 
				given()
				.log().all()
				.body(login)
				.contentType(ContentType.JSON)
			.when()
				.post("http://barrigarest.wcaquino.me/signin")
			.then()
				.log().all()
				.statusCode(200)
				.extract().path("token")
			;
				
				
		//Obter as contas		
		given()
			.log().all()
			.header("Authorization", "JWT "+ token)
		.when()
			.get("http://barrigarest.wcaquino.me/contas")
		.then()
			.log().all()
			//.statusCode(200)
			//.extract().path("token")
		;
	}
}

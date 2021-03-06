package br.cra.rest;

import static io.restassured.RestAssured.*;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.http.ContentType;


public class EnvioDadosTest {
	
	@BeforeClass
	public static void setup() {
		baseURI = "http://restapi.wcaquino.me";	
	}
	
	@Test
	public void deveEnviarFormatXMLViaQuery() {
		given()
			.log().all()
		.when()
			.get("/v2/users?format=xml")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.XML)
		;
	}
	
	@Test
	public void deveEnviarFormatJsonViaQuery() {
		given()
			.log().all()
		.when()
			.get("/users?format=json")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.JSON)
		;
	}
	
	@Test
	public void deveEnviarFormatJsonViaQueryParams() {
		given()
			.log().all()
			.queryParam("format", "json")
		.when()
			.get("/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.JSON)
		;
	}
	
	@Test
	public void deveEnviarFormatJsonViaHeader() {
		given()
			.log().all()
			.accept(ContentType.JSON)
		.when()
			.get("/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.JSON)
		;
	}

}

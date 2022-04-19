package br.cra.tests;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import br.cra.core.BaseTest;


public class ProjectTests extends BaseTest{
	private String TOKEN;
	
	@Before
	public void before() {
		Map<String, String> login = new HashMap<String, String>();
		login.put("email", "igor@projeto");
		login.put("senha", "projeto123");
		
		TOKEN = 
			given()
				.body(login)
			.when()
				.post("/signin")
			.then()
				.statusCode(200)
				.extract().path("token");
	}
	
	@Test
	public void falhaAoAcessarSemToken() {
		given()
		.when()
			.get("/contas")
		.then()
			.statusCode(401);
	}
	
	@Test
	public void deveAcessarComToken() {
	  given() 
	  	.header("Authorization", "JWT " + TOKEN)
	  .when()
	  	.get("/contas")
	  .then()
	  	.statusCode(200);
	}
	
	@Test
	public void deveIncluirConta() {
	given() 
  		.header("Authorization", "JWT " + TOKEN)
  		.body("{ \"nome\": \"conta1\" }")
  	.when()
  		.post("/contas")
  	.then()
  		.statusCode(201);
}
	
	@Test
	public void naoDeveIncluirSemCampoObrigatorio() {
		given() 
  		.header("Authorization", "JWT " + TOKEN)
  		.body("{ \"\": \"\" }")
  	.when()
  		.post("/contas")
  	.then()
  		.statusCode(400)
  		.body("msg[0]", is("Nome é um campo obrigatório"));
	}
	
	
	
	
}

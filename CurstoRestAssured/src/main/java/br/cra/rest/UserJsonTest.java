package br.cra.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class UserJsonTest {
	
	@BeforeClass
	public static void setup() {
		baseURI = "http://restapi.wcaquino.me";	
	}
	
	@Test
	public void deveVerificarPrimeiroNivel() {
		given()
		.when()
			.get("/users/1")
		.then()
			.statusCode(200)
			.body("id", is(1))
			.body("name", containsString("Silva"))
			.body("age", greaterThan(18))
			;
	}
	
	@Test
	public void deveVerificarPrimeiroNivelOutrasFormas() {
		Response response = get("http://restapi.wcaquino.me/users/1");
		
		//path
		assertEquals(1, response.path("id"));
		
		//Jsonpath
		JsonPath jpath = new JsonPath(response.asString());
		assertEquals(1,jpath.getInt("id"));
		
		//from
		int id = JsonPath.from(response.asString()).getInt("id");
		assertEquals(1, id);
		
	}
	
	@Test
	public void deveVerificarSegundoNivel() {
		given()
		.when()
			.get("/users/2")
		.then()
			.statusCode(200)
			.body("id", is(2))
			.body("name", containsString("Maria"))
			.body("endereco.rua", is("Rua dos bobos"))
			.body("endereco.numero", is(0))
			;
	}
	
	@Test
	public void deveVerificarLista() {
		given()
		.when()
			.get("/users/3")
		.then()
			.statusCode(200)
			.body("filhos", hasSize(2))
			.body("filhos[0].name", is("Zezinho"))
			.body("filhos[1].name", is("Luizinho"))
			.body("filhos.name", hasItem("Zezinho"))
			.body("filhos.name", hasItems("Luizinho", "Zezinho"))
			;
	}
	
	@Test
	public void deveVerificarUsuarioInexistente() {
		given()
		.when()
			.get("/users/4")
		.then()
			.statusCode(404)
			.body("error", is("Usu?rio inexistente"))
			;
	}
	
	@Test
	public void deveVerificarListaRaiz() {
		given()
		.when()
			.get("/users")
		.then()
			.statusCode(200)
			//Quando n?o h? uma chave a principio, ? convens?o usar o $, por?m, n?o obrigat?rio.
			.body("$", hasSize(3))
			.body("", hasSize(3))
			.body("name", hasItems("Jo?o da Silva", "Maria Joaquina", "Ana J?lia"))
			.body("age[1]", is(25))
			//consultar array dentro de lista
			.body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho")))
			//.body("salary", contains(1234.5678f,2500,null))
			;
	}
	
	@Test
	public void deveFazerVerificacoesAvancadas() {
		given()
		.when()
			.get("/users")
		.then()
			.statusCode(200)
			.body("", hasSize(3))
			.body("age.findAll{it >= 25}.size()", is(2))
			.body("age.findAll{it > 25 && it < 35}.size()", is(1))
			//nome de usuarios com mais de 25 anos
			.body("findAll{it.age >= 25}.name", hasItem("Jo?o da Silva"))
			.body("findAll{it.age >= 25}[0].name", is("Jo?o da Silva"))
			.body("find{it.age >= 25}.name", is("Jo?o da Silva"))
			.body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
			.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()", allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
			;
	}
	
	@Test
	public void deveUnirJsonPathComJava() {
		ArrayList<String> names =
			given()
			.when()
				.get("/users")
			.then()
				.statusCode(200)
				.body("", hasSize(3))
				.extract().path("name.findAll{it.startsWith('Maria')}")
				;
		assertEquals(1 , names.size());
		assertTrue(names.get(0).equalsIgnoreCase("maria JOaQuIna"));
		assertEquals(names.get(0).toUpperCase(), "maria JOaQuIna".toUpperCase());
	}
	
}

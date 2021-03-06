package br.cra.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;


import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.http.ContentType;


public class VerbosHTMLTest {
	@BeforeClass
	public static void setup() {
		baseURI = "http://restapi.wcaquino.me";	
	}

	@Test 
	public void deveSalvarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"igor\", \"age\": \"26\"}")
		.when()
			.post("/users")
		.then()
			.log().all()
			.statusCode(201)
		;
	}
	
	@Test 
	public void naoDeveSalvarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"age\": \"26\"}")
		.when()
			.post("/users")
		.then()
			.log().all()
			.statusCode(400)
			//.body("id", is(n))
			.body("error", is("Name ? um atributo obrigat?rio"))
		;
	}
	
	@Test 
	public void deveAlterarUsuario() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Usuario alterado\"}")
		.when()
			.put("/users/1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name",is("Usuario alterado"))
		;
	}
	
	@Test 
	public void devoAlterarComURLCustomizada() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Usuario alterado\"}")
		.when()
			.put("/{entity}/{userId}", "users", "1")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name",is("Usuario alterado"))
		;
	}
	
	@Test 
	public void devoAlterarComURLCustomizada2() {
		given()
			.log().all()
			.contentType("application/json")
			.body("{\"name\": \"Usuario alterado1\"}")
			.pathParam("entity", "users")
			.pathParam("userId", 1)
		.when()
			.put("/{entity}/{userId}")
		.then()
			.log().all()
			.statusCode(200)
			.body("id", is(1))
			.body("name",is("Usuario alterado1"))
		;
	}
	
	
	@Test
	public void devoRemoverUsuario() {
		given()
			.log().all()
		.when()
			.delete("users/3")
		.then()
			.statusCode(204)
		;
	}
	
	
	@Test 
	public void deveSalvarUsuarioUsandoMAP() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", "Usuario via map");
		params.put("age", 26);
		given()
			.log().all()
			.contentType("application/json")
			.body(params)
		.when()
			.post("/users")
		.then()
			.log().all()
			.statusCode(201)
			.body("name", is("Usuario via map"))
			.body("age", is(26))
		;
	}
	
	@Test 
	public void deveSalvarUsuarioViaXMLUsandoObjeto() {
		User user = new User("Usuario XML", 40);
		
		given()
			.log().all()
			.contentType(ContentType.XML)
			.body(user)
		.when()
			.post("/usersXML")
		.then()
			.log().all()
			.statusCode(201)
			.body("user.name", is("Usuario XML"))
			.body("user.age", is("40"))
		;
	}
	
}
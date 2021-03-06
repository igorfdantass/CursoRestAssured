package br.cra.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.xml.HasXPath.hasXPath;

import org.junit.BeforeClass;
import org.junit.Test;

import io.restassured.http.ContentType;


public class TestesHTML {

	@BeforeClass
	public static void setup() {
		baseURI = "http://restapi.wcaquino.me";	
	}
	
	@Test
	public void deveFazerBuscasHTML() {
		given()
			.log().all()
		.when()
			.get("/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.HTML)
			.body("html.body.div.table.tbody.tr.size()", is(3))
			.body("html.body.div.table.tbody.tr[1].td[2]", is("25"))
			.appendRootPath("html.body.div.table.tbody")
			.body("tr.find{it.toString().startsWith('2')}.td[1]", is("Maria Joaquina"))
		;
	}
	
	@Test
	public void deveFazerBuscasComXPATHemHTML() {
		given()
			.log().all()
			.queryParam("format", "clean")
		.when()
			.get("/v2/users")
		.then()
			.log().all()
			.statusCode(200)
			.contentType(ContentType.HTML)
			.body(hasXPath(("count(//table/tr)"), is("4")))
			;
	}
}

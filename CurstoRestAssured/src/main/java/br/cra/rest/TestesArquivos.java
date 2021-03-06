package br.cra.rest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.BeforeClass;
import org.junit.Test;


public class TestesArquivos {
	
	@BeforeClass
	public static void setup() {
		baseURI = "http://restapi.wcaquino.me";
	}
	
	@Test
	public void deveObrigarEnvioArquivo() {
		given()
			.log().all()
		.when()
			.post("/upload")
		.then()
			.log().all()
			.statusCode(404)
			.body("error", is("Arquivo n?o enviado"))
		;
	}
	
	@Test
	public void deveEnviarArquivo() {
		given()
			.log().all()
			.multiPart("arquivo", new File("src/main/resources/users.pdf"))
		.when()
			.post("/upload")
		.then()
			.log().all()
			.time(lessThan(5000L))
			.body("name", is("users.pdf"))
		;
	}
	
	@Test
	public void deveBaixarArquivo() throws IOException{
		byte[] image =
		given()
			.log().all()
		.when()
			.get("/download")
		.then()
			.statusCode(200)
			.extract().asByteArray()
		;
		File imagem = new File("src/main/resources/file.jpg");
		OutputStream out = new FileOutputStream(imagem);
		out.write(image);
		out.close();
	}
	
	
	
}

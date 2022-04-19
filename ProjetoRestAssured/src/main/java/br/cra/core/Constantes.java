package br.cra.core;

import io.restassured.http.ContentType;

public interface Constantes {
	String APP_BASE_URL =  "http://seubarriga.wcaquino.me/";
	ContentType APP_CONTENT_TYPE = ContentType.JSON;
	
	Long MAX_TIMEOUT = 1000L;
}

package org.loanwork.tests;

import org.testng.annotations.BeforeMethod;

import com.jayway.restassured.RestAssured;

public class TestBase {

	@BeforeMethod
	public void beforeMethod() {
		RestAssured.baseURI = "http://127.0.0.1";
		RestAssured.port = 8080;

	}

}

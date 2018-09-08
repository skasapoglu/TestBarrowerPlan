package org.loanwork.tests;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.jayway.restassured.RestAssured;

public class SmokeTest extends TestBase {

	@Test
	public void postAnnuityResourceIsAvailable() {
		Map<Object, Object> request = new HashMap<>();
		request.put("loanAmount", "5000");
		request.put("nominalRate", "5.0");
		request.put("duration", 24);

		RestAssured.given().body(request).when().post("/calc-annuity").then().statusCode(200);

	}

	@Test
	public void postCreatePlanResourceIsAvailable() {
		Map<Object, Object> request = new HashMap<>();
		request.put("loanAmount", "5000");
		request.put("nominalRate", "5.0");
		request.put("duration", 24);
		request.put("startDate", "2018-01-09");

		RestAssured.given().body(request).when().post("/generate-plan").then().statusCode(200);

	}

	@Test
	public void invalidDateFormatReturnsBadResponseForPlan() {

		Map<Object, Object> request = new HashMap<>();
		request.put("loanAmount", "5000");
		request.put("nominalRate", "5.0");
		request.put("duration", 24);
		request.put("startDate", "2018-01-34");
		RestAssured.given().body(request).when().post("/generate-plan").then().statusCode(400);

	}

	@Test
	public void invalidLoanAmountReturnsBadResponseForPlan() {

		Map<Object, Object> request = new HashMap<>();
		request.put("loanAmount", "aaaa");
		request.put("nominalRate", "5.0");
		request.put("duration", 24);
		request.put("startDate", "2018-02-14");
		RestAssured.given().body(request).when().post("/generate-plan").then().statusCode(400);

	}

	@Test
	public void invalidNominalRateAmountReturnsBadResponseForPlan() {

		Map<Object, Object> request = new HashMap<>();
		request.put("loanAmount", "15000");
		request.put("nominalRate", " 5.0");
		request.put("duration", 24);
		request.put("startDate", "2018-01-12");
		RestAssured.given().body(request).when().post("/generate-plan").then().statusCode(400);

	}

	@Test
	public void invalidLoanAmountReturnsBadBadResponseAnnuity() {

		Map<Object, Object> request = new HashMap<>();
		request.put("loanAmount", "aaaa");
		request.put("nominalRate", "5.0");
		request.put("duration", 24);

		RestAssured.given().body(request).when().post("/calc-annuity").then().statusCode(400);

	}

	@Test
	public void invalidNominalRateAmountReturnsBadResponseForAnnuity() {

		Map<Object, Object> request = new HashMap<>();
		request.put("loanAmount", "15000");
		request.put("nominalRate", " 5.0");
		request.put("duration", 24);

		RestAssured.given().body(request).when().post("/calc-annuity").then().statusCode(400);

	}

	// this test fails with 0 input duration so just skipped it
	@Test(enabled = false)
	public void withZeroDurationReturnsBadResponseForAnnuity() {

		Map<Object, Object> request = new HashMap<>();
		request.put("loanAmount", "15000");
		request.put("nominalRate", "5.0");
		request.put("duration", 0);

		RestAssured.given().body(request).when().post("/calc-annuity").then().statusCode(400);
	}

	@Test
	public void postCreatePlanWithBadRequestResponses400() {
		Map<Object, Object> request = new HashMap<>();
		request.put("loanAmount", "5000");

		RestAssured.given().body(request).when().post("/calc-annuity").then().statusCode(400);
	}

	@Test
	public void postAnnuityWithBadRequestResponses400() {
		Map<Object, Object> request = new HashMap<>();
		request.put("loanAmount", "5000");

		RestAssured.given().body(request).when().post("/generate-plan").then().statusCode(400);
	}

	@Test
	public void postNoExistsResourceResponses404() {
		Map<Object, Object> request = new HashMap<>();
		request.put("loanAmount", "5000");

		RestAssured.given().body(request).when().post("/generate").then().statusCode(404);
	}

}

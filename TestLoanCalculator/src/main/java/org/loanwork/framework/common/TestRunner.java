package org.loanwork.framework.common;

import org.loanwork.framework.model.AnnuityRequest;
import org.loanwork.framework.model.AnnuityResponse;
import org.loanwork.framework.model.LoanRequest;
import org.loanwork.framework.model.PaymentResponse;

import com.jayway.restassured.RestAssured;

public class TestRunner {

	public static PaymentResponse[] generatePlan(LoanRequest loanRequest) {
		JsonParser parser = new JsonParser();
		String requestBody = parser.serializeRequest(loanRequest);

		String responseBody = RestAssured.given().body(requestBody).when().post("/generate-plan").getBody().asString();

		return (PaymentResponse[]) parser.deserializeResponse(responseBody, PaymentResponse[].class);
	}

	public static AnnuityResponse getAnnuity(AnnuityRequest annuityRequest) {

		JsonParser parser = new JsonParser();
		String requestBody = parser.serializeRequest(annuityRequest);

		String responseBody = RestAssured.given().body(requestBody).when().post("/calc-annuity").getBody().asString();

		return (AnnuityResponse) parser.deserializeResponse(responseBody, AnnuityResponse.class);
	}

}

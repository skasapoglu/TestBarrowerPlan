package org.loanwork.tests;

import org.loanwork.framework.common.TestRunner;
import org.loanwork.framework.model.AnnuityRequest;
import org.loanwork.framework.model.AnnuityResponse;
import org.loanwork.framework.model.LoanRequest;
import org.loanwork.framework.model.PaymentResponse;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ContentTest {

	@Test
	public void annuityResponseHasExpectedContent() {
		
		AnnuityRequest annuityRequest = new AnnuityRequest("5000", "5.0", 24);
		AnnuityResponse response = TestRunner.getAnnuity(annuityRequest);

		Assert.assertNotNull(response.getAnnuity(), "The content for /calc-annuity call does not match");
	}

	@Test
	public void planResponseHasExpectedContents() {

		LoanRequest loanRequest = new LoanRequest("5000", "5.0", 24, "2018-01-09");
		PaymentResponse[] response = TestRunner.generatePlan(loanRequest);

		Assert.assertTrue(response.length > 0, "Content of generate-plan doesnot match");
		Assert.assertNotNull(response[0].getInterest(), "Content of generate-plan doesnot match");
	}

}

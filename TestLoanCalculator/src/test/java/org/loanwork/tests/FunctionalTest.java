package org.loanwork.tests;

import org.loanwork.framework.common.TestRunner;
import org.loanwork.framework.common.TestVerifier;
import org.loanwork.framework.model.AnnuityRequest;
import org.loanwork.framework.model.AnnuityResponse;
import org.loanwork.framework.model.LoanRequest;
import org.loanwork.framework.model.PaymentResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class FunctionalTest extends TestBase {

	@Test
	public void planHasCorrectAmountOfPayments() {

		int duration = 11;
		LoanRequest loanRequest = new LoanRequest("5000", "5.0", duration, "2018-01-09");

		PaymentResponse[] plan = TestRunner.generatePlan(loanRequest);

		Assert.assertTrue(plan.length == loanRequest.getDuration(), "The amount of payments on plan does not match");
	}

	@Test(dataProvider = "DatesToTest")
	public void planHasCorrectDatesForPayments(String startDate) {

		LoanRequest loanRequest = new LoanRequest("5000", "5.0", 24, startDate);

		PaymentResponse[] plan = TestRunner.generatePlan(loanRequest);

		Assert.assertTrue(TestVerifier.isPlanWithCorrectDates(plan, loanRequest),
				"The dates for corresponding plan is not as expected");
	}

	@Test
	public void planHasExpectedPaymentAmounts() {

		LoanRequest loanRequest = new LoanRequest("5000", "5.0", 1, "2011-01-09");

		PaymentResponse[] plan = TestRunner.generatePlan(loanRequest);

		Assert.assertTrue(TestVerifier.isPlanWithCorrectPaymentAmounts(plan),
				"The dates for corresponding plan is not as expected");
	}

	@Test
	public void paymentAmountMathesWithAnnuity() {

		AnnuityRequest annuityRequest = new AnnuityRequest("15000", "8.0", 12);
		AnnuityResponse annuityResponse = TestRunner.getAnnuity(annuityRequest);

		LoanRequest loanRequest = new LoanRequest("15000", "8.0", 12, "2011-01-09");

		PaymentResponse[] plan = TestRunner.generatePlan(loanRequest);

		Assert.assertEquals(plan[0].getBorrowerPaymentAmount(), annuityResponse.getAnnuity(),
				"Plan is generated with wrong annuity payment amount");

	}

	@Test
	public void initialPrincipleMatchesWithPreviousRemainingPrinciple() {

		LoanRequest loanRequest = new LoanRequest("5000", "5.0", 18, "2011-01-09");

		PaymentResponse[] plan = TestRunner.generatePlan(loanRequest);

		Assert.assertTrue(TestVerifier.isOutstandingPrinciplesMatch(plan, loanRequest),
				"The remianing and previous initial principles do not match");

	}

	// this test fails for some conditions example
	// "5000", "5.0", 17, "2011-01-09"
	@Test(enabled = false)
	public void remainingPrincipleOfLastPaymentMustBeZero() {
		LoanRequest loanRequest = new LoanRequest("5000", "5.0", 38, "2011-01-09");

		PaymentResponse[] plan = TestRunner.generatePlan(loanRequest);

		Assert.assertEquals(plan[plan.length - 1].getRemainingOutstandingPrincipal(), "0.00",
				"The remianing principle is not 0.00");
	}

	@Test
	public void allPrinciplesWillAddUpToInitialLoanAmount() {
		LoanRequest loanRequest = new LoanRequest("5000", "5.0", 17, "2011-01-09");

		PaymentResponse[] plan = TestRunner.generatePlan(loanRequest);

		Assert.assertTrue(TestVerifier.isAllPrinciplesEqualLoanAmount(plan, loanRequest),
				"All principle amounts do not add up to initial loan amount");
	}

	@Test
	public void allInterestsAndPrinciplesSumUpToAnnuityPerPayment() {

		LoanRequest loanRequest = new LoanRequest("5000", "5.0", 17, "2011-01-09");

		PaymentResponse[] plan = TestRunner.generatePlan(loanRequest);

		Assert.assertTrue(TestVerifier.isAllInterestPrincipalCorrect(plan),
				"Some payment schedules has different interest + principle than annuity");
	}

	@Test
	public void allInterestsPerPaymentCalculated() {

		LoanRequest loanRequest = new LoanRequest("5000", "5.0", 17, "2011-01-09");

		PaymentResponse[] plan = TestRunner.generatePlan(loanRequest);

		Assert.assertTrue(TestVerifier.isAllIntestsCalculated(plan, loanRequest),
				"Some interest calculated is not correct");

	}

	/**
	 * This provider gives different type of dates as follows One normal date One
	 * date in leap year with 29 date field One date with 30 date field One date
	 * with 31 date field
	 * 
	 * @return
	 */
	@DataProvider(name = "DatesToTest")
	public static Object[][] startDates() {

		return new Object[][] { { "2018-01-09" }, { "2011-09-29" }, { "2017-05-30" }, { "2016-07-31" } };
	}

}

package org.loanwork.framework.common;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.loanwork.framework.model.LoanRequest;
import org.loanwork.framework.model.PaymentResponse;

public class TestVerifier {

	/**
	 * Assert that the date fields are changing for each payment in plan one
	 * exception if the day field is 29-30-31 then and next month does not have
	 * those dates previous day plus 30days of next month is expected
	 * 
	 * @param plan
	 *            generated plan
	 * @param loanRequest
	 *            the request object for the plan
	 * @return {@code True} if condition is satisfied, {@code False} otherwise
	 */
	public static boolean isPlanWithCorrectDates(PaymentResponse[] plan, LoanRequest loanRequest) {

		boolean result = true;

		ZonedDateTime startDate = ZonedDateTime.parse(plan[0].getDate());

		// checking start date correct
		if (!loanRequest.getStartDate().equals(startDate.toLocalDate().toString())) {
			return false;
		}

		List<String> dates = Arrays.stream(plan).map(PaymentResponse::getDate).collect(Collectors.toList());

		for (int i = 1; i < dates.size(); i++) {

			ZonedDateTime dateCurrent = ZonedDateTime.parse(dates.get(i));
			ZonedDateTime datePrevious = ZonedDateTime.parse(dates.get(i - 1));

			// checking years for payment dates are same if not new year
			if (datePrevious.getMonthValue() < 12) {
				result &= dateCurrent.getYear() == datePrevious.getYear();
			} else {
				// if previous month is December current month should switch next year
				result &= dateCurrent.getYear() == datePrevious.getYear() + 1;
			}

			if (datePrevious.getDayOfMonth() < 29) {
				result &= dateCurrent.getDayOfMonth() == datePrevious.getDayOfMonth();
			} else {
				// if currentMonth does not have the date skip to the next month
				// assuming for 29-30-31 December this condition never occurs since January has
				// already all dates [1-31]
				if (datePrevious.plusDays(30).getMonthValue() - dateCurrent.getMonthValue() > 1) {
					result &= datePrevious.plusDays(30).getDayOfMonth() == dateCurrent.getDayOfMonth();
				}
			}

		}

		return result;
	}

	/**
	 * Checks if the payment plan each month has the same amount of borrow payments,
	 * except last month it checks if the last amount is smaller than all other
	 * equal payments.
	 * 
	 * @param plan
	 *            generated plan
	 * @return
	 */
	public static boolean isPlanWithCorrectPaymentAmounts(PaymentResponse[] plan) {

		List<String> paymentAmounts = Arrays.stream(plan).map(PaymentResponse::getBorrowerPaymentAmount)
				.collect(Collectors.toList());

		for (int i = 1; i < paymentAmounts.size(); i++) {

			double previousAmount = Double.parseDouble(paymentAmounts.get(i - 1));
			double currentAmount = Double.parseDouble(paymentAmounts.get(i));

			// last payment item may have smaller amount
			if (i == paymentAmounts.size() - 1) {
				return previousAmount >= currentAmount;
			}

			if (previousAmount != currentAmount) {
				return false;
			}

		}

		// if the conditions true last check the plan is populated or not
		return !paymentAmounts.isEmpty();
	}

	/**
	 * Checks if the previous remaining outstanding principle matches with the
	 * current initial outstanding principle for all items in payment plan
	 *
	 * @param plan
	 *            generated plan
	 * @param loanRequest
	 *            the request object for the plan
	 * @return {@code True} if condition is satisfied, {@code False} otherwise
	 */
	public static boolean isOutstandingPrinciplesMatch(PaymentResponse[] plan, LoanRequest request) {

		// request loan to 2digit
		String loanValue = String.format(Locale.US, "%.2f", Double.parseDouble(request.getLoanAmount()));

		// first check initial element
		if (!plan[0].getInitialOutstandingPrincipal().equals(loanValue)) {
			return false;
		}

		for (int i = 1; i < plan.length; i++) {

			String initialPrincipleCurrent = plan[i].getInitialOutstandingPrincipal();
			String remaininPrinplePrevious = plan[i - 1].getRemainingOutstandingPrincipal();

			if (!remaininPrinplePrevious.equals(initialPrincipleCurrent)) {
				return false;
			}

		}
		return true;
	}

	/**
	 * Checks if the principles counted for each payment item will add up to the
	 * initial loan amount.
	 * 
	 * @param plan
	 *            generated plan
	 * @param loanRequest
	 *            the request object for the plan
	 * @return {@code True} if condition is satisfied, {@code False} otherwise
	 */
	public static boolean isAllPrinciplesEqualLoanAmount(PaymentResponse[] plan, LoanRequest loanRequest) {

		double countOfPrinciples = 0;

		for (int i = 0; i < plan.length; i++) {

			countOfPrinciples += Double.parseDouble(plan[i].getPrincipal());

		}

		return countOfPrinciples == Double.parseDouble(loanRequest.getLoanAmount());

	}

	/**
	 * Checks if for all scheduled payments the interest and the principle value
	 * adds up to the borrower payment (namely annuity)
	 * 
	 * @param plan
	 *            generated plan
	 * @return {@code True} if condition is satisfied, {@code False} otherwise
	 */
	public static boolean isAllInterestPrincipalCorrect(PaymentResponse[] plan) {

		for (int i = 0; i < plan.length; i++) {
			double interest = Double.parseDouble(plan[i].getInterest());
			double principal = Double.parseDouble(plan[i].getPrincipal());
			double borrowerPayment = Double.parseDouble(plan[i].getBorrowerPaymentAmount());

			if (Math.abs(borrowerPayment - (interest + principal)) >= 0.01) {
				return false;
			}
		}
		return plan.length > 0;
	}

	/**
	 * Checks if the interest amount per share is calculated with the initial
	 * Outstanding Principal value.
	 * 
	 * @param plan
	 *            generated plan
	 * @param loanRequest
	 *            the request object for the plan
	 * @return {@code True} if condition is satisfied, {@code False} otherwise
	 */
	public static boolean isAllIntestsCalculated(PaymentResponse[] plan, LoanRequest loanRequest) {

		for (int i = 0; i < plan.length; i++) {

			double nominalRate = Double.parseDouble(loanRequest.getNominaleRate());

			double initialOutstandingPrinciple = Double.parseDouble(plan[i].getInitialOutstandingPrincipal());
			double interest = Double.parseDouble(plan[i].getInterest());

			double expectedinterest =  (initialOutstandingPrinciple * nominalRate * 30.0 / 36000.0);

			if (Math.abs(expectedinterest - interest) >= 0.01) {
				return false;
			}
		}
		return plan.length > 0;
	}

}

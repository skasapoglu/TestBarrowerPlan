package org.loanwork.framework.model;

public class LoanRequest {

	private String loanAmount;
	private String nominalRate;
	private int duration;
	private String startDate;

	public LoanRequest(String loanAmount, String nominalRate, int duration, String startDate) {
		this.loanAmount = loanAmount;
		this.nominalRate = nominalRate;
		this.duration = duration;
		this.startDate = startDate;
	}

	public String getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getNominaleRate() {
		return nominalRate;
	}

	public void setNominaleRate(String nominalRate) {
		this.nominalRate = nominalRate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

}

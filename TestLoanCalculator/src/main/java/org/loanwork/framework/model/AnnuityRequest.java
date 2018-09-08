package org.loanwork.framework.model;

public class AnnuityRequest {
	private String loanAmount;
	private String nominalRate;
	private int duration;

	public AnnuityRequest(String loanAmount, String nominalRate, int duration) {
		this.loanAmount = loanAmount;
		this.nominalRate = nominalRate;
		this.duration = duration;
	}

	public String getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getNominalRate() {
		return nominalRate;
	}

	public void setNominaleRate(String nominaleRate) {
		this.nominalRate = nominaleRate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

}

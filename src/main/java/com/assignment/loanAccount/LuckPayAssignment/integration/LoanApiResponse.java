package com.assignment.loanAccount.LuckPayAssignment.integration;

import com.assignment.loanAccount.LuckPayAssignment.model.LoanEmi;

import java.util.List;

public class LoanApiResponse {
    private String loanAccountNumber;
    private List<LoanEmi> emiDetails;

    public LoanApiResponse() {
    }

    public LoanApiResponse(String loanAccountNumber, List<LoanEmi> emiDetails) {
        this.loanAccountNumber = loanAccountNumber;
        this.emiDetails = emiDetails;
    }

    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public List<LoanEmi> getEmiDetails() {
        return emiDetails;
    }

    public void setEmiDetails(List<LoanEmi> emiDetails) {
        this.emiDetails = emiDetails;
    }
}

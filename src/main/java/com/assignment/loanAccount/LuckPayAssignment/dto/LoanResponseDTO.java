package com.assignment.loanAccount.LuckPayAssignment.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;

public class LoanResponseDTO {
    private String loanAccountNumber;
    private LocalDate dueDate;
    private int emiAmount;

    public LoanResponseDTO() {
    }

    public LoanResponseDTO(String loanAccountNumber, LocalDate dueDate, int emiAmount) {
        this.loanAccountNumber = loanAccountNumber;
        this.dueDate = dueDate;
        this.emiAmount = emiAmount;
    }

    public String getLoanAccountNumber() {
        return loanAccountNumber;
    }

    public void setLoanAccountNumber(String loanAccountNumber) {
        this.loanAccountNumber = loanAccountNumber;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getEmiAmount() {
        return emiAmount;
    }

    public void setEmiAmount(int emiAmount) {
        this.emiAmount = emiAmount;
    }
}

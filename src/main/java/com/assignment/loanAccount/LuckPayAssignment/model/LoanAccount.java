package com.assignment.loanAccount.LuckPayAssignment.model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "loan_account")
public class LoanAccount {
    @Id
    private String loanAccountNumber;
    private LocalDate dueDate;
    private int emiAmount;

    // Constructors, getters, and setters
    public LoanAccount() {}

    public LoanAccount(String loanAccountNumber, LocalDate dueDate, int emiAmount) {
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

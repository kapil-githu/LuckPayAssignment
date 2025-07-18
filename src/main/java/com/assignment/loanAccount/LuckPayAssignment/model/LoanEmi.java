package com.assignment.loanAccount.LuckPayAssignment.model;

public class LoanEmi {
    private String month;
    private int emiAmount;
    private boolean paidStatus;
    private boolean dueStatus;

    public LoanEmi() {
    }

    public LoanEmi(String month, int emiAmount, boolean paidStatus, boolean dueStatus) {
        this.month = month;
        this.emiAmount = emiAmount;
        this.paidStatus = paidStatus;
        this.dueStatus = dueStatus;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public int getEmiAmount() {
        return emiAmount;
    }

    public void setEmiAmount(int emiAmount) {
        this.emiAmount = emiAmount;
    }

    public boolean isPaidStatus() {
        return paidStatus;
    }

    public void setPaidStatus(boolean paidStatus) {
        this.paidStatus = paidStatus;
    }

    public boolean isDueStatus() {
        return dueStatus;
    }

    public void setDueStatus(boolean dueStatus) {
        this.dueStatus = dueStatus;
    }

}

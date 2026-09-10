package com.loan;

public class LoanApplication {

    private String applicationId;
    private Customer customer;
    private double loanAmount;

    public LoanApplication(String applicationId,
                            Customer customer,
                            double loanAmount) {

        this.applicationId = applicationId;
        this.customer = customer;
        this.loanAmount = loanAmount;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getLoanAmount() {
        return loanAmount;
    }
}

package com.loan;

public class CreditAssessment {

    private double maximumPermissibleLoan;
    private double debtToIncomeRatio;
    private String riskClassification;
    private boolean approved;
    private String rejectionReason;

    public CreditAssessment(double maximumPermissibleLoan,
                            double debtToIncomeRatio,
                            String riskClassification,
                            boolean approved,
                            String rejectionReason) {

        this.maximumPermissibleLoan = maximumPermissibleLoan;
        this.debtToIncomeRatio = debtToIncomeRatio;
        this.riskClassification = riskClassification;
        this.approved = approved;
        this.rejectionReason = rejectionReason;
    }

    public double getMaximumPermissibleLoan() {
        return maximumPermissibleLoan;
    }

    public double getDebtToIncomeRatio() {
        return debtToIncomeRatio;
    }

    public String getRiskClassification() {
        return riskClassification;
    }

    public boolean isApproved() {
        return approved;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }
}

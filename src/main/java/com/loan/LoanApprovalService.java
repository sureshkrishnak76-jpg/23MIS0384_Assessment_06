package com.loan;

import java.util.ArrayList;
import java.util.List;

public class LoanApprovalService {

    private static final int MINIMUM_AGE = 21;
    private static final int MINIMUM_CREDIT_SCORE = 650;
    private static final double MAXIMUM_DTI = 0.50;

    public CreditAssessment assessLoan(LoanApplication application) {

        // ==========================================
        // INPUT VALIDATION AND EXCEPTION HANDLING
        // ==========================================

        if (application == null) {
            throw new InvalidLoanDataException(
                    "Loan application cannot be null"
            );
        }

        if (application.getCustomer() == null) {
            throw new InvalidLoanDataException(
                    "Customer information cannot be null"
            );
        }

        if (application.getLoanAmount() <= 0) {
            throw new InvalidLoanDataException(
                    "Loan amount must be greater than zero"
            );
        }

        Customer customer = application.getCustomer();
        double loanAmount = application.getLoanAmount();

        if (customer.getCustomerId() == null ||
                customer.getCustomerId().trim().isEmpty()) {

            throw new InvalidLoanDataException(
                    "Customer ID cannot be empty"
            );
        }

        if (customer.getName() == null ||
                customer.getName().trim().isEmpty()) {

            throw new InvalidLoanDataException(
                    "Customer name cannot be empty"
            );
        }

        if (customer.getAge() < 0) {
            throw new InvalidLoanDataException(
                    "Age cannot be negative"
            );
        }

        if (customer.getMonthlyIncome() < 0) {
            throw new InvalidLoanDataException(
                    "Monthly income cannot be negative"
            );
        }

        if (customer.getCreditScore() < 0 ||
                customer.getCreditScore() > 900) {

            throw new InvalidLoanDataException(
                    "Credit score must be between 0 and 900"
            );
        }

        if (customer.getExistingLoanObligations() < 0) {
            throw new InvalidLoanDataException(
                    "Existing loan obligations cannot be negative"
            );
        }

        // ==========================================
        // BUSINESS VALIDATION
        // ==========================================

        List<String> rejectionReasons = new ArrayList<>();

        // Age validation
        if (customer.getAge() < MINIMUM_AGE) {
            rejectionReasons.add(
                    "Customer age is below 21"
            );
        }

        // Government ID validation
        if (customer.getGovernmentId() == null ||
                customer.getGovernmentId().trim().isEmpty()) {

            rejectionReasons.add(
                    "Invalid government ID"
            );
        }

        // Income validation
        if (customer.getMonthlyIncome() <= 0) {
            rejectionReasons.add(
                    "Monthly income must be greater than zero"
            );
        }

        // Calculate maximum permissible loan
        double maximumPermissibleLoan =
                calculateMaximumLoan(
                        customer.getMonthlyIncome()
                );

        // Loan amount validation
        if (loanAmount > maximumPermissibleLoan) {
            rejectionReasons.add(
                    "Requested loan exceeds maximum permissible loan"
            );
        }

        // Credit score validation
        if (customer.getCreditScore() < MINIMUM_CREDIT_SCORE) {
            rejectionReasons.add(
                    "Credit score is below minimum requirement"
            );
        }

        // Debt-to-income ratio
        double dti = calculateDTI(customer);

        if (dti > MAXIMUM_DTI) {
            rejectionReasons.add(
                    "Debt-to-income ratio exceeds 50%"
            );
        }

        // ==========================================
        // RISK CLASSIFICATION
        // ==========================================

        String riskClassification = determineRisk(
                customer.getCreditScore(),
                dti
        );

        // ==========================================
        // FINAL APPROVAL DECISION
        // ==========================================

        boolean approved = rejectionReasons.isEmpty();

        String rejectionReason;

        if (approved) {
            rejectionReason = "No rejection";
        } else {
            rejectionReason = String.join(
                    "; ",
                    rejectionReasons
            );
        }

        return new CreditAssessment(
                maximumPermissibleLoan,
                dti,
                riskClassification,
                approved,
                rejectionReason
        );
    }

    // ==========================================
    // MAXIMUM LOAN CALCULATION
    // ==========================================

    private double calculateMaximumLoan(double monthlyIncome) {

        if (monthlyIncome <= 0) {
            return 0;
        }

        if (monthlyIncome < 30000) {
            return monthlyIncome * 5;

        } else if (monthlyIncome < 60000) {
            return monthlyIncome * 8;

        } else {
            return monthlyIncome * 12;
        }
    }

    // ==========================================
    // DTI CALCULATION
    // ==========================================

    private double calculateDTI(Customer customer) {

        if (customer.getMonthlyIncome() <= 0) {
            return 1.0;
        }

        return customer.getExistingLoanObligations()
                / customer.getMonthlyIncome();
    }

    // ==========================================
    // RISK CLASSIFICATION
    // ==========================================

    private String determineRisk(
            int creditScore,
            double dti) {

        if (creditScore >= 750 &&
                dti <= 0.30) {

            return "Low Risk";
        }

        if (creditScore >= 650 &&
                dti <= 0.50) {

            return "Medium Risk";
        }

        return "High Risk";
    }
}

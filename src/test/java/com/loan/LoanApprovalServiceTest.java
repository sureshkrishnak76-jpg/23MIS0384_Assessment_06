package com.loan;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoanApprovalServiceTest {

    private final LoanApprovalService service =
            new LoanApprovalService();

    // 1. Fully eligible customer
    @Test
    void testEligibleCustomer() {

        Customer customer = new Customer(
                "C001",
                "Arun",
                30,
                "ID123",
                70000,
                780,
                15000
        );

        LoanApplication application =
                new LoanApplication(
                        "L001",
                        customer,
                        500000
                );

        CreditAssessment result =
                service.assessLoan(application);

        assertTrue(result.isApproved());
        assertEquals(
                "Low Risk",
                result.getRiskClassification()
        );
    }

    // 2. Minimum age boundary
    @Test
    void testMinimumAgeBoundary() {

        Customer customer = new Customer(
                "C002",
                "Kumar",
                21,
                "ID124",
                50000,
                700,
                10000
        );

        LoanApplication application =
                new LoanApplication(
                        "L002",
                        customer,
                        200000
                );

        CreditAssessment result =
                service.assessLoan(application);

        assertTrue(result.isApproved());
    }

    // 3. Below minimum age
    @Test
    void testBelowMinimumAge() {

        Customer customer = new Customer(
                "C003",
                "Ravi",
                20,
                "ID125",
                50000,
                700,
                10000
        );

        LoanApplication application =
                new LoanApplication(
                        "L003",
                        customer,
                        200000
                );

        CreditAssessment result =
                service.assessLoan(application);

        assertFalse(result.isApproved());

        assertTrue(
                result.getRejectionReason()
                        .contains("Customer age is below 21")
        );
    }

    // 4. Minimum credit score boundary
    @Test
    void testMinimumCreditScoreBoundary() {

        Customer customer = new Customer(
                "C004",
                "Suresh",
                30,
                "ID126",
                50000,
                650,
                10000
        );

        LoanApplication application =
                new LoanApplication(
                        "L004",
                        customer,
                        200000
                );

        CreditAssessment result =
                service.assessLoan(application);

        assertTrue(result.isApproved());

        assertEquals(
                "Medium Risk",
                result.getRiskClassification()
        );
    }

    // 5. Low credit score
    @Test
    void testLowCreditScore() {

        Customer customer = new Customer(
                "C005",
                "Vijay",
                30,
                "ID127",
                50000,
                600,
                10000
        );

        LoanApplication application =
                new LoanApplication(
                        "L005",
                        customer,
                        200000
                );

        CreditAssessment result =
                service.assessLoan(application);

        assertFalse(result.isApproved());

        assertTrue(
                result.getRejectionReason()
                        .contains(
                                "Credit score is below minimum requirement"
                        )
        );
    }

    // 6. Maximum DTI boundary - exactly 50%
    @Test
    void testMaximumDTIBoundary() {

        Customer customer = new Customer(
                "C006",
                "Ajay",
                30,
                "ID128",
                50000,
                700,
                25000
        );

        LoanApplication application =
                new LoanApplication(
                        "L006",
                        customer,
                        200000
                );

        CreditAssessment result =
                service.assessLoan(application);

        assertEquals(
                0.50,
                result.getDebtToIncomeRatio(),
                0.001
        );

        assertTrue(result.isApproved());
    }

    // 7. DTI above maximum
    @Test
    void testDTIAboveMaximum() {

        Customer customer = new Customer(
                "C007",
                "Manoj",
                30,
                "ID129",
                50000,
                700,
                30000
        );

        LoanApplication application =
                new LoanApplication(
                        "L007",
                        customer,
                        200000
                );

        CreditAssessment result =
                service.assessLoan(application);

        assertFalse(result.isApproved());

        assertTrue(
                result.getRejectionReason()
                        .contains(
                                "Debt-to-income ratio exceeds 50%"
                        )
        );
    }

    // 8. Invalid government ID
    @Test
    void testInvalidGovernmentId() {

        Customer customer = new Customer(
                "C008",
                "Karthik",
                30,
                "",
                50000,
                700,
                10000
        );

        LoanApplication application =
                new LoanApplication(
                        "L008",
                        customer,
                        200000
                );

        CreditAssessment result =
                service.assessLoan(application);

        assertFalse(result.isApproved());

        assertTrue(
                result.getRejectionReason()
                        .contains("Invalid government ID")
        );
    }

    // 9. Loan amount exceeds maximum
    @Test
    void testLoanAmountExceedsMaximum() {

        Customer customer = new Customer(
                "C009",
                "Prakash",
                30,
                "ID130",
                30000,
                700,
                5000
        );

        LoanApplication application =
                new LoanApplication(
                        "L009",
                        customer,
                        300000
                );

        CreditAssessment result =
                service.assessLoan(application);

        assertFalse(result.isApproved());

        assertTrue(
                result.getRejectionReason()
                        .contains(
                                "Requested loan exceeds maximum permissible loan"
                        )
        );
    }

    // 10. Multiple failure reasons
    @Test
    void testMultipleFailures() {

        Customer customer = new Customer(
                "C010",
                "Ramesh",
                20,
                "",
                30000,
                550,
                25000
        );

        LoanApplication application =
                new LoanApplication(
                        "L010",
                        customer,
                        500000
                );

        CreditAssessment result =
                service.assessLoan(application);

        assertFalse(result.isApproved());

        String reasons =
                result.getRejectionReason();

        assertTrue(
                reasons.contains("Customer age is below 21")
        );

        assertTrue(
                reasons.contains("Invalid government ID")
        );

        assertTrue(
                reasons.contains(
                        "Credit score is below minimum requirement"
                )
        );

        assertTrue(
                reasons.contains(
                        "Debt-to-income ratio exceeds 50%"
                )
        );
    }
}

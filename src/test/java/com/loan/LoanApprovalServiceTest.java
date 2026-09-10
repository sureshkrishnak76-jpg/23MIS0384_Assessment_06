package com.loan;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class LoanApprovalServiceTest {

    private final LoanApprovalService service =
            new LoanApprovalService();

    // =========================================================
    // 1. Eligible Customer
    // =========================================================
    @Test
    void testEligibleCustomer() {

        Customer customer = new Customer(
                "C001",
                "Arun",
                30,
                "ID123",
                50000,
                750,
                10000
        );

        LoanApplication application = new LoanApplication(
                "L001",
                customer,
                200000
        );

        CreditAssessment result =
                service.assessLoan(application);

        assertTrue(result.isApproved());
        assertEquals("Low Risk", result.getRiskClassification());
        assertEquals(0.20, result.getDebtToIncomeRatio(), 0.001);
    }

    // =========================================================
    // 2. Minimum Age Boundary
    // =========================================================
    @Test
    void testMinimumAgeBoundary() {

        Customer customer = new Customer(
                "C002",
                "Kumar",
                21,
                "ID124",
                40000,
                700,
                10000
        );

        LoanApplication application = new LoanApplication(
                "L002",
                customer,
                100000
        );

        CreditAssessment result =
                service.assessLoan(application);

        assertTrue(result.isApproved());
    }

    // =========================================================
    // 3. Age Below Minimum
    // =========================================================
    @Test
    void testAgeBelowMinimum() {

        Customer customer = new Customer(
                "C003",
                "Ravi",
                20,
                "ID125",
                40000,
                700,
                10000
        );

        LoanApplication application = new LoanApplication(
                "L003",
                customer,
                100000
        );

        CreditAssessment result =
                service.assessLoan(application);

        assertFalse(result.isApproved());
        assertTrue(
                result.getRejectionReason()
                        .contains("Customer age is below 21")
        );
    }

    // =========================================================
    // 4. Minimum Credit Score Boundary
    // =========================================================
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

        LoanApplication application = new LoanApplication(
                "L004",
                customer,
                100000
        );

        CreditAssessment result =
                service.assessLoan(application);

        assertTrue(result.isApproved());
        assertEquals(
                "Medium Risk",
                result.getRiskClassification()
        );
    }

    // =========================================================
    // 5. Credit Score Below Minimum
    // =========================================================
    @Test
    void testCreditScoreBelowMinimum() {

        Customer customer = new Customer(
                "C005",
                "Mani",
                30,
                "ID127",
                50000,
                600,
                10000
        );

        LoanApplication application = new LoanApplication(
                "L005",
                customer,
                100000
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

    // =========================================================
    // 6. Maximum DTI Boundary
    // =========================================================
    @Test
    void testMaximumDTIBoundary() {

        Customer customer = new Customer(
                "C006",
                "Vijay",
                30,
                "ID128",
                50000,
                700,
                25000
        );

        LoanApplication application = new LoanApplication(
                "L006",
                customer,
                100000
        );

        CreditAssessment result =
                service.assessLoan(application);

        assertTrue(result.isApproved());

        assertEquals(
                0.50,
                result.getDebtToIncomeRatio(),
                0.001
        );
    }

    // =========================================================
    // 7. DTI Above Maximum
    // =========================================================
    @Test
    void testDTIAboveMaximum() {

        Customer customer = new Customer(
                "C007",
                "Ajay",
                30,
                "ID129",
                50000,
                700,
                30000
        );

        LoanApplication application = new LoanApplication(
                "L007",
                customer,
                100000
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

    // =========================================================
    // 8. Invalid Government ID
    // =========================================================
    @Test
    void testInvalidGovernmentId() {

        Customer customer = new Customer(
                "C008",
                "Rahul",
                30,
                "",
                50000,
                700,
                10000
        );

        LoanApplication application = new LoanApplication(
                "L008",
                customer,
                100000
        );

        CreditAssessment result =
                service.assessLoan(application);

        assertFalse(result.isApproved());

        assertTrue(
                result.getRejectionReason()
                        .contains("Invalid government ID")
        );
    }

    // =========================================================
    // 9. Excessive Loan Amount
    // =========================================================
    @Test
    void testExcessiveLoanAmount() {

        Customer customer = new Customer(
                "C009",
                "Prakash",
                30,
                "ID130",
                30000,
                700,
                5000
        );

        LoanApplication application = new LoanApplication(
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

    // =========================================================
    // 10. Multiple Failures
    // =========================================================
    @Test
    void testMultipleFailures() {

        Customer customer = new Customer(
                "C010",
                "Test User",
                20,
                "",
                50000,
                600,
                30000
        );

        LoanApplication application = new LoanApplication(
                "L010",
                customer,
                500000
        );

        CreditAssessment result =
                service.assessLoan(application);

        assertFalse(result.isApproved());

        String reasons = result.getRejectionReason();

        assertTrue(
                reasons.contains(
                        "Customer age is below 21"
                )
        );

        assertTrue(
                reasons.contains(
                        "Invalid government ID"
                )
        );

        assertTrue(
                reasons.contains(
                        "Requested loan exceeds maximum permissible loan"
                )
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

    // =========================================================
    // 11. Invalid Loan Amount Exception
    // =========================================================
    @Test
    void testInvalidLoanAmountException() {

        Customer customer = new Customer(
                "C011",
                "Test User",
                30,
                "ID131",
                50000,
                700,
                10000
        );

        LoanApplication application = new LoanApplication(
                "L011",
                customer,
                -1000
        );

        assertThrows(
                InvalidLoanDataException.class,
                () -> service.assessLoan(application)
        );
    }

    // =========================================================
    // 12. Invalid Credit Score Exception
    // =========================================================
    @Test
    void testInvalidCreditScoreException() {

        Customer customer = new Customer(
                "C012",
                "Test User",
                30,
                "ID132",
                50000,
                950,
                10000
        );

        LoanApplication application = new LoanApplication(
                "L012",
                customer,
                200000
        );

        assertThrows(
                InvalidLoanDataException.class,
                () -> service.assessLoan(application)
        );
    }
}

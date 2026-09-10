package com.loan;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoanApprovalServiceTest {

    private final LoanApprovalService service =
            new LoanApprovalService();

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
                new LoanApplication("L001", customer, 500000);

        CreditAssessment result =
                service.assessLoan(application);

        assertTrue(result.isApproved());
        assertEquals("Low Risk",
                result.getRiskClassification());
    }

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
                new LoanApplication("L002", customer, 200000);

        CreditAssessment result =
                service.assessLoan(application);

        assertTrue(result.isApproved());
    }

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
                new LoanApplication("L003", customer, 200000);

        CreditAssessment result =
                service.assessLoan(application);

        assertFalse(result.isApproved());
        assertTrue(
                result.getRejectionReason()
                        .contains("age is below 21")
        );
    }

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
                new LoanApplication("L004", customer, 200000);

        CreditAssessment result =
                service.assessLoan(application);

        assertTrue(result.isApproved());
        assertEquals("Medium Risk",
                result.getRiskClassification());
    }

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
                new LoanApplication("L005", customer, 200000);

        CreditAssessment result =
                service.assessLoan(application);

        assertFalse(result.isApproved());
        assertTrue(
                result.getRejectionReason()
                        .contains("Credit score")
        );
    }

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
                new LoanApplication("L006", customer, 200000);

        CreditAssessment result =
                service.assessLoan(application);

        assertEquals(0.50,
                result.getDebtToIncomeRatio(),
                0.001);

        assertTrue(result.isApproved());
    }

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
                new LoanApplication("L007", customer, 200000);

        CreditAssessment result =
                service.assessLoan(application);

        assertFalse(result.isApproved());
        assertTrue(
                result.getRejectionReason()
                        .contains("debt-to-income ratio")
        );
    }

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
                new LoanApplication("L008", customer, 200000);

        CreditAssessment result =
                service.assessLoan(application);

        assertFalse(result.isApproved());
        assertTrue(
                result.getRejectionReason()
                        .contains("government ID")
        );
    }

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
                new LoanApplication("L009", customer, 300000);

        CreditAssessment result =
                service.assessLoan(application);

        assertFalse(result.isApproved());
        assertTrue(
                result.getRejectionReason()
                        .contains("maximum permissible loan")
        );
    }

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
                new LoanApplication("L010", customer, 500000);

        CreditAssessment result =
                service.assessLoan(application);

        assertFalse(result.isApproved());

        String reasons =
                result.getRejectionReason();

        assertTrue(reasons.contains("age"));
        assertTrue(reasons.contains("government ID"));
        assertTrue(reasons.contains("Credit score"));
        assertTrue(reasons.contains("debt-to-income"));
    }
}

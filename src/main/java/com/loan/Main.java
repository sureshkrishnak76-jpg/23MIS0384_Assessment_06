package com.loan;

public class Main {

    public static void main(String[] args) {

        LoanApprovalService service = new LoanApprovalService();

        Customer customer1 = new Customer(
                "C001",
                "Arun",
                30,
                "ID12345",
                70000,
                780,
                15000
        );

        Customer customer2 = new Customer(
                "C002",
                "Kumar",
                25,
                "ID23456",
                40000,
                680,
                15000
        );

        Customer customer3 = new Customer(
                "C003",
                "Ravi",
                20,
                "",
                25000,
                600,
                15000
        );

        LoanApplication application1 =
                new LoanApplication("L001", customer1, 500000);

        LoanApplication application2 =
                new LoanApplication("L002", customer2, 250000);

        LoanApplication application3 =
                new LoanApplication("L003", customer3, 200000);

        displayResult(service, application1);
        displayResult(service, application2);
        displayResult(service, application3);
    }

    private static void displayResult(
            LoanApprovalService service,
            LoanApplication application) {

        CreditAssessment result =
                service.assessLoan(application);

        System.out.println("--------------------------------");
        System.out.println(
                "Application ID: "
                        + application.getApplicationId()
        );

        System.out.println(
                "Customer: "
                        + application.getCustomer().getName()
        );

        System.out.println(
                "Requested Loan: "
                        + application.getLoanAmount()
        );

        System.out.println(
                "Maximum Permissible Loan: "
                        + result.getMaximumPermissibleLoan()
        );

        System.out.println(
                "DTI: "
                        + (result.getDebtToIncomeRatio() * 100)
                        + "%"
        );

        System.out.println(
                "Risk: "
                        + result.getRiskClassification()
        );

        System.out.println(
                "Approved: "
                        + result.isApproved()
        );

        System.out.println(
                "Reason: "
                        + result.getRejectionReason()
        );
    }
}

package com.employee;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        EligibilityService service = new EligibilityService();

        List<Employee> employees = Arrays.asList(

            new Employee(
                "EMP001",
                "Arun",
                25,
                "IT",
                "Active",
                "Confidential",
                true
            ),

            new Employee(
                "EMP002",
                "Kumar",
                20,
                "HR",
                "Active",
                "Basic",
                true
            ),

            new Employee(
                "EMP003",
                "Ravi",
                30,
                "Marketing",
                "Inactive",
                "Basic",
                false
            ),

            new Employee(
                "EMP004",
                "Suresh",
                28,
                "Finance",
                "Active",
                "Basic",
                true
            )
        );

        System.out.println("==========================================");
        System.out.println("   EMPLOYEE ACCESS ELIGIBILITY SYSTEM");
        System.out.println("==========================================");

        int employeeNumber = 1;

        for (Employee employee : employees) {

            EligibilityResult result =
                service.checkEligibility(employee, "Confidential");

            System.out.println("\nEmployee " + employeeNumber);
            System.out.println("-----------------------------");
            System.out.println("ID         : " + employee.getEmployeeId());
            System.out.println("Name       : " + employee.getName());
            System.out.println("Department : " + employee.getDepartment());
            System.out.println("Status     : " + result.getStatus());

            if (!result.getReasons().isEmpty()) {

                System.out.println("Reasons:");

                for (String reason : result.getReasons()) {
                    System.out.println(" - " + reason);
                }
            }

            employeeNumber++;
        }

        System.out.println("\n==========================================");
        System.out.println("             PROCESS COMPLETED");
        System.out.println("==========================================");
    }
}

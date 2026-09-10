package com.employee;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class EligibilityServiceTest {

    private final EligibilityService service = new EligibilityService();

    // Test 1: Fully eligible employee
    @Test
    void testEligibleEmployee() {

        Employee employee = new Employee(
                "EMP001",
                "Arun",
                25,
                "IT",
                "Active",
                "Confidential",
                true
        );

        EligibilityResult result =
                service.checkEligibility(employee, "Confidential");

        assertEquals("Eligible", result.getStatus());
        assertTrue(result.getReasons().isEmpty());
    }

    // Test 2: Boundary age - exactly 21
    @Test
    void testMinimumAgeBoundary() {

        Employee employee = new Employee(
                "EMP002",
                "Kumar",
                21,
                "HR",
                "Active",
                "Confidential",
                true
        );

        EligibilityResult result =
                service.checkEligibility(employee, "Confidential");

        assertEquals("Eligible", result.getStatus());
    }

    // Test 3: Under minimum age
    @Test
    void testUnderMinimumAge() {

        Employee employee = new Employee(
                "EMP003",
                "Ravi",
                20,
                "IT",
                "Active",
                "Confidential",
                true
        );

        EligibilityResult result =
                service.checkEligibility(employee, "Confidential");

        assertEquals("Not Eligible", result.getStatus());

        assertTrue(
                result.getReasons()
                        .contains("Employee must be at least 21 years old.")
        );
    }

    // Test 4: Unauthorized department
    @Test
    void testUnauthorizedDepartment() {

        Employee employee = new Employee(
                "EMP004",
                "Vijay",
                30,
                "Marketing",
                "Active",
                "Confidential",
                true
        );

        EligibilityResult result =
                service.checkEligibility(employee, "Confidential");

        assertEquals("Not Eligible", result.getStatus());

        assertTrue(
                result.getReasons()
                        .contains("Employee department is not authorized.")
        );
    }

    // Test 5: Invalid employee ID
    @Test
    void testInvalidEmployeeId() {

        Employee employee = new Employee(
                "EMP005",
                "Suresh",
                30,
                "IT",
                "Active",
                "Confidential",
                false
        );

        EligibilityResult result =
                service.checkEligibility(employee, "Confidential");

        assertEquals("Not Eligible", result.getStatus());

        assertTrue(
                result.getReasons()
                        .contains("Employee ID is invalid.")
        );
    }

    // Test 6: Inactive employee
    @Test
    void testInactiveEmployee() {

        Employee employee = new Employee(
                "EMP006",
                "Manoj",
                30,
                "Finance",
                "Inactive",
                "Confidential",
                true
        );

        EligibilityResult result =
                service.checkEligibility(employee, "Confidential");

        assertEquals("Not Eligible", result.getStatus());

        assertTrue(
                result.getReasons()
                        .contains(
                            "Employee does not have active employment status."
                        )
        );
    }

    // Test 7: Multiple failures
    @Test
    void testMultipleFailures() {

        Employee employee = new Employee(
                "EMP007",
                "Raj",
                19,
                "Marketing",
                "Inactive",
                "Basic",
                false
        );

        EligibilityResult result =
                service.checkEligibility(employee, "Secret");

        assertEquals("Not Eligible", result.getStatus());

        // Verify ALL applicable reasons are reported
        assertEquals(5, result.getReasons().size());

        assertTrue(result.getReasons().contains(
                "Employee must be at least 21 years old."));

        assertTrue(result.getReasons().contains(
                "Employee department is not authorized."));

        assertTrue(result.getReasons().contains(
                "Employee does not have active employment status."));

        assertTrue(result.getReasons().contains(
                "Employee ID is invalid."));

        assertTrue(result.getReasons().contains(
                "Security clearance is insufficient for requested access."));
    }

    // Test 8: Conditional eligibility
    @Test
    void testConditionallyEligibleEmployee() {

        Employee employee = new Employee(
                "EMP008",
                "Ajay",
                28,
                "IT",
                "Active",
                "Basic",
                true
        );

        EligibilityResult result =
                service.checkEligibility(employee, "Confidential");

        assertEquals("Conditionally Eligible", result.getStatus());

        assertEquals(1, result.getReasons().size());
    }
}

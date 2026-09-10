package com.employee;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EligibilityServiceTest {

    private final EligibilityService service =
            new EligibilityService();

    // 1. Positive test - fully eligible employee
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
                service.checkEligibility(
                        employee,
                        "Confidential"
                );

        assertEquals(
                "Eligible",
                result.getStatus()
        );

        assertTrue(
                result.getReasons().isEmpty()
        );
    }

    // 2. Boundary test - exactly 21 years old
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
                service.checkEligibility(
                        employee,
                        "Confidential"
                );

        assertEquals(
                "Eligible",
                result.getStatus()
        );
    }

    // 3. Negative test - employee below minimum age
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
                service.checkEligibility(
                        employee,
                        "Confidential"
                );

        assertEquals(
                "Not Eligible",
                result.getStatus()
        );

        assertTrue(
                result.getReasons().contains(
                        "Employee must be at least 21 years old."
                )
        );
    }

    // 4. Negative test - unauthorized department
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
                service.checkEligibility(
                        employee,
                        "Confidential"
                );

        assertEquals(
                "Not Eligible",
                result.getStatus()
        );

        assertTrue(
                result.getReasons().contains(
                        "Employee department is not authorized."
                )
        );
    }

    // 5. Negative test - invalid employee ID
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
                service.checkEligibility(
                        employee,
                        "Confidential"
                );

        assertEquals(
                "Not Eligible",
                result.getStatus()
        );

        assertTrue(
                result.getReasons().contains(
                        "Employee ID is invalid."
                )
        );
    }

    // 6. Negative test - inactive employee
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
                service.checkEligibility(
                        employee,
                        "Confidential"
                );

        assertEquals(
                "Not Eligible",
                result.getStatus()
        );

        assertTrue(
                result.getReasons().contains(
                        "Employee does not have active employment status."
                )
        );
    }

    // 7. Negative test - multiple failures
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
                service.checkEligibility(
                        employee,
                        "Secret"
                );

        assertEquals(
                "Not Eligible",
                result.getStatus()
        );

        // Verify all applicable rejection reasons
        assertEquals(
                5,
                result.getReasons().size()
        );

        assertTrue(
                result.getReasons().contains(
                        "Employee must be at least 21 years old."
                )
        );

        assertTrue(
                result.getReasons().contains(
                        "Employee department is not authorized."
                )
        );

        assertTrue(
                result.getReasons().contains(
                        "Employee does not have active employment status."
                )
        );

        assertTrue(
                result.getReasons().contains(
                        "Employee ID is invalid."
                )
        );

        assertTrue(
                result.getReasons().contains(
                        "Security clearance is insufficient for requested access."
                )
        );
    }

    // 8. Conditional eligibility - insufficient security clearance
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
                service.checkEligibility(
                        employee,
                        "Confidential"
                );

        assertEquals(
                "Conditionally Eligible",
                result.getStatus()
        );

        assertEquals(
                1,
                result.getReasons().size()
        );
    }

    // 9. Positive test - valid employee with Secret clearance
    @Test
    void testValidEmployeePositiveCase() {

        Employee employee = new Employee(
                "EMP009",
                "Test Employee",
                25,
                "IT",
                "Active",
                "Secret",
                true
        );

        EligibilityResult result =
                service.checkEligibility(
                        employee,
                        "Confidential"
                );

        assertEquals(
                "Eligible",
                result.getStatus()
        );

        assertTrue(
                result.getReasons().isEmpty()
        );
    }

    // 10. Negative test - negative age exception
    @Test
    void testNegativeAgeException() {

        Employee employee = new Employee(
                "EMP010",
                "Invalid Employee",
                -5,
                "IT",
                "Active",
                "Secret",
                true
        );

        assertThrows(
                InvalidEmployeeDataException.class,
                () -> service.checkEligibility(
                        employee,
                        "Confidential"
                )
        );
    }

    // 11. Negative test - empty employee ID exception
    @Test
    void testEmptyEmployeeIdException() {

        Employee employee = new Employee(
                "",
                "Invalid Employee",
                25,
                "IT",
                "Active",
                "Secret",
                true
        );

        assertThrows(
                InvalidEmployeeDataException.class,
                () -> service.checkEligibility(
                        employee,
                        "Confidential"
                )
        );
    }
}

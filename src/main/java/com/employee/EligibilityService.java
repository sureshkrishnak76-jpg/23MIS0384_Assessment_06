package com.employee;

import java.util.ArrayList;
import java.util.List;

public class EligibilityService {

    private static final String[] AUTHORIZED_DEPARTMENTS = {
            "IT",
            "HR",
            "Finance",
            "Administration"
    };

    public EligibilityResult checkEligibility(
            Employee employee,
            String requestedAccessLevel) {

        // Input validation
        if (employee == null) {
            throw new InvalidEmployeeDataException(
                    "Employee cannot be null"
            );
        }

        if (employee.getEmployeeId() == null ||
                employee.getEmployeeId().trim().isEmpty()) {
            throw new InvalidEmployeeDataException(
                    "Employee ID cannot be empty"
            );
        }

        if (employee.getName() == null ||
                employee.getName().trim().isEmpty()) {
            throw new InvalidEmployeeDataException(
                    "Employee name cannot be empty"
            );
        }

        if (employee.getAge() < 0) {
            throw new InvalidEmployeeDataException(
                    "Employee age cannot be negative"
            );
        }

        if (employee.getDepartment() == null ||
                employee.getDepartment().trim().isEmpty()) {
            throw new InvalidEmployeeDataException(
                    "Department cannot be empty"
            );
        }

        if (employee.getEmploymentType() == null ||
                employee.getEmploymentType().trim().isEmpty()) {
            throw new InvalidEmployeeDataException(
                    "Employment type cannot be empty"
            );
        }

        List<String> reasons = new ArrayList<>();

        // Positive / normal eligibility rules

        // Age
        if (employee.getAge() < 21) {
            reasons.add(
                    "Employee must be at least 21 years old."
            );
        }

        // Department
        if (!isAuthorizedDepartment(
                employee.getDepartment())) {

            reasons.add(
                    "Employee department is not authorized."
            );
        }

        // Employment
        if (!employee.getEmploymentType()
                .equalsIgnoreCase("Active")) {

            reasons.add(
                    "Employee does not have active employment status."
            );
        }

        // Employee ID
        if (!employee.isIdValid()) {
            reasons.add(
                    "Employee ID is invalid."
            );
        }

        // Security clearance
        if (!hasRequiredClearance(
                employee.getSecurityClearanceLevel(),
                requestedAccessLevel)) {

            reasons.add(
                    "Security clearance is insufficient for requested access."
            );
        }

        String status;

        if (reasons.isEmpty()) {
            status = "Eligible";
        } else if (isConditionallyEligible(reasons)) {
            status = "Conditionally Eligible";
        } else {
            status = "Not Eligible";
        }

        return new EligibilityResult(status, reasons);
    }

    private boolean isAuthorizedDepartment(
            String department) {

        for (String authorizedDepartment :
                AUTHORIZED_DEPARTMENTS) {

            if (authorizedDepartment
                    .equalsIgnoreCase(department)) {

                return true;
            }
        }

        return false;
    }

    private boolean hasRequiredClearance(
            String clearanceLevel,
            String requestedAccessLevel) {

        if (requestedAccessLevel == null ||
                requestedAccessLevel.equalsIgnoreCase("Public")) {

            return true;
        }

        if (clearanceLevel == null) {
            return false;
        }

        if (requestedAccessLevel.equalsIgnoreCase(
                "Confidential")) {

            return clearanceLevel.equalsIgnoreCase("Confidential")
                    || clearanceLevel.equalsIgnoreCase("Secret");
        }

        if (requestedAccessLevel.equalsIgnoreCase("Secret")) {
            return clearanceLevel.equalsIgnoreCase("Secret");
        }

        return false;
    }

    private boolean isConditionallyEligible(
            List<String> reasons) {

        return reasons.size() == 1
                && reasons.get(0)
                .contains("Security clearance");
    }
}

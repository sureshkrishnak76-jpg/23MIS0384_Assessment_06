package com.employee;

import java.util.List;

public class EligibilityResult {

    private String status;
    private List<String> reasons;

    public EligibilityResult(String status, List<String> reasons) {
        this.status = status;
        this.reasons = reasons;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getReasons() {
        return reasons;
    }
}

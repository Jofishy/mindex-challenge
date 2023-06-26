package com.mindex.challenge.service;

import com.mindex.challenge.data.ReportingStructure;

public interface ReportingStructureService {
    /**
     * Generates a {@link ReportingStructure} for the provided employee id
     * @param employeeId
     * @return generated ReportingStructure
     */
    ReportingStructure createReportingStructure(String employeeId);
}

package com.mindex.challenge.dao;

public interface EmployeeAggregateRepository {
    int countTotalReportsByEmployeeId(final String employeeId);
}
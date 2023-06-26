package com.mindex.challenge.data;

public class ReportingStructure {
    private final int numberOfReports;
    private final Employee employee;

    public ReportingStructure(Employee employee, int numberOfReports){
        this.numberOfReports = numberOfReports;
        this.employee = employee;
    }

    public int getNumberOfReports() {
        return numberOfReports;
    }

    public Employee getEmployee() {
        return employee;
    }
}

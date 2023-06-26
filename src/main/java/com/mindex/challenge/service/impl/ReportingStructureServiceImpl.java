package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import com.mindex.challenge.service.exception.EmployeeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportingStructureServiceImpl implements ReportingStructureService {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public ReportingStructure createReportingStructure(final String employeeId){
        LOG.debug("Creating reporting structure for employee with id [{}]", employeeId);

        final Employee employee = employeeRepository.findByEmployeeId(employeeId);

        if (employee == null){
            LOG.debug("Attempted to create a reporting structure for a non-existing employee [{}]", employeeId);
            throw new EmployeeNotFoundException(employeeId);
        }

        // TODO Would prefer EmployeeAggregateRepositoryImpl#countTotalReportsByEmployeeId to avoid multiple db queries.
        final int totalReports = countTotalReportsForEmployee(employee);

        return new ReportingStructure(employee, totalReports);
    }

    private int countTotalReportsForEmployee(final Employee employee){
        if (employee.getDirectReports() == null){
            return 0;
        }

        int reports = 0;

        for (Employee emptyEmployee : employee.getDirectReports()) {
            reports++;

            final Employee populatedEmployee = employeeRepository.findByEmployeeId(emptyEmployee.getEmployeeId());
            if (populatedEmployee == null){
                LOG.warn("Attempted to lookup direct report for [{}] with a missing employeeId [{}]",
                        employee.getEmployeeId(), employee.getEmployeeId());
                continue;
            }

            reports += countTotalReportsForEmployee(populatedEmployee);
        }

        return reports;
    }
}

package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.ReportingStructureService;
import com.mindex.challenge.service.exception.EmployeeNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static com.mindex.challenge.TestConstants.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReportingStructureServiceImplTest {
    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private final ReportingStructureService reportingStructureService = new ReportingStructureServiceImpl();

    @Test
    public void createReportingStructure_returnsNoReports(){
        Employee employee = new Employee();

        when(employeeRepository.findByEmployeeId(EMPLOYEE_ID)).thenReturn(employee);

        ReportingStructure reportingStructure = reportingStructureService.createReportingStructure(EMPLOYEE_ID);

        assertThat(reportingStructure.getNumberOfReports()).isEqualTo(0);
        assertThat(reportingStructure.getEmployee()).isEqualTo(employee);

        verify(employeeRepository, times(1)).findByEmployeeId(EMPLOYEE_ID);
    }

    @Test
    public void createReportingStructure_returnsNestedReports(){
        Employee topEmployee = new Employee();
        topEmployee.setEmployeeId(EMPLOYEE_ID);

        Employee directReport1 = new Employee();
        directReport1.setEmployeeId(EMPLOYEE_ID1);

        Employee directReport2 = new Employee();
        directReport2.setEmployeeId(EMPLOYEE_ID2);

        Employee indirectReport = new Employee();
        indirectReport.setEmployeeId(EMPLOYEE_ID3);

        topEmployee.setDirectReports(Arrays.asList(directReport1, directReport2));
        directReport2.setDirectReports(Arrays.asList(indirectReport));

        when(employeeRepository.findByEmployeeId(EMPLOYEE_ID)).thenReturn(topEmployee);
        when(employeeRepository.findByEmployeeId(EMPLOYEE_ID1)).thenReturn(directReport1);
        when(employeeRepository.findByEmployeeId(EMPLOYEE_ID2)).thenReturn(directReport2);
        when(employeeRepository.findByEmployeeId(EMPLOYEE_ID3)).thenReturn(indirectReport);

        ReportingStructure reportingStructure = reportingStructureService.createReportingStructure(EMPLOYEE_ID);

        assertThat(reportingStructure.getNumberOfReports()).isEqualTo(3);
        assertThat(reportingStructure.getEmployee()).isEqualTo(topEmployee);

        verify(employeeRepository, times(1)).findByEmployeeId(EMPLOYEE_ID);
        verify(employeeRepository, times(1)).findByEmployeeId(EMPLOYEE_ID1);
        verify(employeeRepository, times(1)).findByEmployeeId(EMPLOYEE_ID2);
        verify(employeeRepository, times(1)).findByEmployeeId(EMPLOYEE_ID3);
    }

    @Test
    public void createReportingStructure_countsMissingReport(){
        Employee topEmployee = new Employee();
        topEmployee.setEmployeeId(EMPLOYEE_ID);

        Employee directReport = new Employee();
        directReport.setEmployeeId(EMPLOYEE_ID1);

        topEmployee.setDirectReports(Arrays.asList(directReport));


        when(employeeRepository.findByEmployeeId(EMPLOYEE_ID)).thenReturn(topEmployee);
        when(employeeRepository.findByEmployeeId(EMPLOYEE_ID1)).thenReturn(null);

        ReportingStructure reportingStructure = reportingStructureService.createReportingStructure(EMPLOYEE_ID);

        assertThat(reportingStructure.getNumberOfReports()).isEqualTo(1);
        assertThat(reportingStructure.getEmployee()).isEqualTo(topEmployee);

        verify(employeeRepository, times(1)).findByEmployeeId(EMPLOYEE_ID);
        verify(employeeRepository, times(1)).findByEmployeeId(EMPLOYEE_ID1);
    }

    @Test
    public void createReportingStructure_failsMissingEmployee(){
        when(employeeRepository.findByEmployeeId(EMPLOYEE_ID)).thenReturn(null);

        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(()->reportingStructureService.createReportingStructure(EMPLOYEE_ID));

        verify(employeeRepository, times(1)).findByEmployeeId(EMPLOYEE_ID);
    }
}

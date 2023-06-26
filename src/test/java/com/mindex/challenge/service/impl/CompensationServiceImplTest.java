package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.exception.CompensationNotFoundException;
import com.mindex.challenge.service.exception.DuplicateCompensationException;
import com.mindex.challenge.service.exception.EmployeeNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.mindex.challenge.TestConstants.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CompensationServiceImplTest {

    @Mock
    EmployeeRepository employeeRepository;

    @Mock
    CompensationRepository compensationRepository;

    @InjectMocks
    private CompensationService compensationService = new CompensationServiceImpl();

    @Test
    public void create_savesAndReturns(){
        Compensation compensation = new Compensation(EMPLOYEE_ID, SALARY, START_DATE);

        when(employeeRepository.findByEmployeeId(EMPLOYEE_ID)).thenReturn(new Employee());
        when(compensationRepository.findByEmployeeId(EMPLOYEE_ID)).thenReturn(null);
        when(compensationRepository.save(compensation)).thenReturn(compensation);

        assertThat(compensationService.create(compensation)).isEqualTo(new Compensation(EMPLOYEE_ID, SALARY, START_DATE));

        verify(employeeRepository, times(1)).findByEmployeeId(EMPLOYEE_ID);
        verify(compensationRepository, times(1)).findByEmployeeId(EMPLOYEE_ID);
        verify(compensationRepository, times(1)).save(compensation);
    }

    @Test
    public void create_failsMissingEmployee(){
        Compensation compensation = new Compensation(EMPLOYEE_ID, SALARY, START_DATE);

        when(employeeRepository.findByEmployeeId(EMPLOYEE_ID)).thenReturn(null);

        assertThatExceptionOfType(EmployeeNotFoundException.class)
                .isThrownBy(()->compensationService.create(compensation));

        verify(employeeRepository, times(1)).findByEmployeeId(EMPLOYEE_ID);
        verify(compensationRepository, never()).findByEmployeeId(EMPLOYEE_ID);
        verify(compensationRepository, never()).save(compensation);
    }

    @Test
    public void create_failsDuplicateCompensation(){
        Compensation compensation = new Compensation(EMPLOYEE_ID, SALARY, START_DATE);

        when(employeeRepository.findByEmployeeId(EMPLOYEE_ID)).thenReturn(new Employee());
        when(compensationRepository.findByEmployeeId(EMPLOYEE_ID))
                .thenReturn(new Compensation(EMPLOYEE_ID, SALARY, START_DATE));

        assertThatExceptionOfType(DuplicateCompensationException.class)
                .isThrownBy(()->compensationService.create(compensation));

        verify(employeeRepository, times(1)).findByEmployeeId(EMPLOYEE_ID);
        verify(compensationRepository, times(1)).findByEmployeeId(EMPLOYEE_ID);
        verify(compensationRepository, never()).save(compensation);
    }

    @Test
    public void read_returns(){
        when(compensationRepository.findByEmployeeId(EMPLOYEE_ID))
                .thenReturn(new Compensation(EMPLOYEE_ID, SALARY, START_DATE));

        assertThat(compensationService.read(EMPLOYEE_ID)).isEqualTo(new Compensation(EMPLOYEE_ID, SALARY, START_DATE));

        verify(compensationRepository, times(1)).findByEmployeeId(EMPLOYEE_ID);
    }

    @Test
    public void read_failsWhenNoCompensationFound(){
        when(compensationRepository.findByEmployeeId(EMPLOYEE_ID)).thenReturn(null);

        assertThatExceptionOfType(CompensationNotFoundException.class)
                .isThrownBy(()->compensationService.read(EMPLOYEE_ID));

        verify(compensationRepository, times(1)).findByEmployeeId(EMPLOYEE_ID);
    }
}

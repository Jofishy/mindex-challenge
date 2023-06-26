package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.exception.CompensationNotFoundException;
import com.mindex.challenge.service.exception.DuplicateCompensationException;
import com.mindex.challenge.service.exception.EmployeeNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);
    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    CompensationRepository compensationRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating and saving compensation: [{}]", compensation);

        // Validate that the employee exists
        if (employeeRepository.findByEmployeeId(compensation.getEmployeeId()) == null){
            LOG.debug("Attempted to create a compensation with an employee id that doesn't exist: [{}]", compensation.getEmployeeId());
            throw new EmployeeNotFoundException(compensation.getEmployeeId());
        }

        // Avoid duplicate compensations
        if (compensationRepository.findByEmployeeId(compensation.getEmployeeId()) != null){
            LOG.debug("Attempted to create a duplicate compensation for employee [{}]", compensation.getEmployeeId());
            throw new DuplicateCompensationException(compensation.getEmployeeId());
        }

        return compensationRepository.save(compensation);
    }

    @Override
    public Compensation read(String employeeId) {
        LOG.debug("Reading compensation for employee with id [{}]", employeeId);

        final Compensation compensation = compensationRepository.findByEmployeeId(employeeId);

        if (compensation == null){
            LOG.debug("Attempted to look up non-existing compensation for employee with id [{}]", employeeId);
            throw new CompensationNotFoundException(employeeId);
        }

        return compensation;
    }
}

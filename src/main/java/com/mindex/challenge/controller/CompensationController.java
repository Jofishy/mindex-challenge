package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

    @PostMapping("/compensation")
    public Compensation create(@Valid @RequestBody Compensation compensation) {
        LOG.debug("Received compensation create request for [{}]", compensation);

        return compensationService.create(compensation);
    }

    @GetMapping("/compensation/{employeeId}")
    public Compensation read(@PathVariable String employeeId) {
        LOG.debug("Received compensation read request for employee with id [{}]", employeeId);

        return compensationService.read(employeeId);
    }
}

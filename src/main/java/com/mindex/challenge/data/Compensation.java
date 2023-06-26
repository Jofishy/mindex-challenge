package com.mindex.challenge.data;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.Objects;

public class Compensation {
    @NotEmpty(message = "employeeId must be populated")
    private final String employeeId;

    @NotNull(message = "salary must not be null")
    private final Integer salary;

    @NotNull(message = "startDate must not be null")
    @Future(message = "startDate must be a future date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private final Date startDate;

    public Compensation(String employeeId, Integer salary, Date startDate) {
        this.employeeId = employeeId;
        this.salary = salary;
        this.startDate = startDate;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public Integer getSalary() {
        return salary;
    }

    public Date getStartDate() {
        return startDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Compensation that = (Compensation) o;
        return Objects.equals(employeeId, that.employeeId)
                && Objects.equals(salary, that.salary)
                && Objects.equals(startDate, that.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, salary, startDate);
    }
}

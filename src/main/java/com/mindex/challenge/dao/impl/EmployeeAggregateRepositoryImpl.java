package com.mindex.challenge.dao.impl;

import com.mindex.challenge.dao.EmployeeAggregateRepository;
import com.mindex.challenge.data.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

public class EmployeeAggregateRepositoryImpl implements EmployeeAggregateRepository {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeAggregateRepositoryImpl.class);
    @Autowired
    private MongoOperations mongoOperations;

    /**
     * Uses <a href="https://www.mongodb.com/docs/v6.0/aggregation/">MongoDB aggregation</a> to first find the employee,
     * then count all of their reports recursively.
     *
     * @Deprecated because $graphLookup doesn't work properly with mongo-java-server
     * <a href="https://github.com/bwaldvogel/mongo-java-server/issues/218">due to this issue</a>
     * <br></br>
     * See <a href="https://mongoplayground.net/p/PRb3y4IsMJ1">this playground</a> for an example of the working query.
     *
     * @param employeeId
     * @return Total number of reports.
     */
    @Deprecated
    @Override
    public int countTotalReportsByEmployeeId(final String employeeId) {
        LOG.debug("Looking up all descendants for employee with id: [{}]", employeeId);

        final AggregationOperation matchOperation = Aggregation.match(Criteria.where("employeeId").is(employeeId));
        final AggregationOperation graphLookupOperation = GraphLookupOperation.builder()
                .from("employee")
                .startWith("$directReports.employeeId")
                .connectFrom("directReports.employeeId")
                .connectTo("employeeId")
                .as("allReports");

        final AggregationOperation projectOperation = Aggregation.project("employeeId").and("allReports").size().as("count");

        final Aggregation aggregate = Aggregation.newAggregation(matchOperation, graphLookupOperation, projectOperation);

        final AggregationResults<CountedResult> orderAggregate = mongoOperations.aggregate(aggregate, "employee", CountedResult.class);

        return orderAggregate.getUniqueMappedResult().getCount();
    }

    class CountedResult {
        private Integer count;

        public CountedResult(Integer count){
            this.count = count;
        }

        public Integer getCount() {
            return count;
        }
    }
}

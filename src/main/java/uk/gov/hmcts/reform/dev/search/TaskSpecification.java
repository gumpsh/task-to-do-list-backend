package uk.gov.hmcts.reform.dev.search;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskSpecification<T> implements Specification<T> {

    private final Map<String, Object> filters;

    public TaskSpecification(Map<String, Object> filters) {
        this.filters = filters;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Predicate toPredicate(Root root, CriteriaQuery query, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        LocalDateTime startDate = (LocalDateTime) filters.get("startDate");
        LocalDateTime endDate = (LocalDateTime) filters.get("endDate");

        if (startDate != null && endDate != null) {
            predicates.add(criteriaBuilder.between(
                root.get("dueDate"),
                startDate,
                endDate
            ));
        }

        if (filters != null && !filters.isEmpty()) {
            filters.forEach((key, value) -> {
                if (value != null && !"startDate".equals(key) && !"endDate".equals(key)) {

                    if (value instanceof String) {
                        predicates.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get(key)), "%" + value.toString().toLowerCase() + "%"));
                    } else {
                        predicates.add(criteriaBuilder.equal(root.get(key), value));
                    }
                }
            });
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}

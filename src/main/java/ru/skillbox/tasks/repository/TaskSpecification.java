package ru.skillbox.tasks.repository;

import org.springframework.data.jpa.domain.Specification;
import ru.skillbox.tasks.domain.model.Task;

public class TaskSpecification {

    public static Specification<Task> byCreatorId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("creator").get("id"), id);
        };
    }

    public static Specification<Task> byAssigneeId(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("assignee").get("id"), id);
        };
    }

}

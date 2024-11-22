package ru.skillbox.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.tasks.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Long id(Long id);
}

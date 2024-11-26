package ru.skillbox.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skillbox.tasks.domain.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}

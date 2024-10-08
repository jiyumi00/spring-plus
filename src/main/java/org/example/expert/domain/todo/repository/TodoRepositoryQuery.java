package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoRepositoryQuery {
    Optional<Todo> findByIdWithUser_Querydsl(Long todoId);
}

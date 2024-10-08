package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.response.TodoSearchCondition;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TodoRepositoryQuery {
    Optional<Todo> findByIdWithUser_Querydsl(Long todoId);

    Page<TodoSearchResponse> searchTodoPage(TodoSearchCondition condition, Pageable pageable);
}

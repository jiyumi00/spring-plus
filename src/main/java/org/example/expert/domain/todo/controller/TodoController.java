package org.example.expert.domain.todo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.expert.domain.common.annotation.Auth;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchCondition;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.service.TodoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/todos")
    public ResponseEntity<TodoSaveResponse> saveTodo(
            @Auth AuthUser authUser,
            @Valid @RequestBody TodoSaveRequest todoSaveRequest
    ) {
        return ResponseEntity.ok(todoService.saveTodo(authUser, todoSaveRequest));
    }

    @GetMapping("/todos")
    public ResponseEntity<Page<TodoResponse>> getTodos(
            @RequestParam(name="weather", required = false) String weather,
            @RequestParam(name="searchStartDate",required = false) String searchStartDate,
            @RequestParam(name="searchEndDate",required = false) String searchEndDate,
            @RequestParam(name="page", defaultValue = "1") int page,
            @RequestParam(name="size", defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(todoService.getTodos(weather,searchStartDate,searchEndDate,page, size));
    }

    @GetMapping("/todos/{todoId}")
    public ResponseEntity<TodoResponse> getTodo(@PathVariable("todoId") long todoId) {
        return ResponseEntity.ok(todoService.getTodo(todoId));
    }

    //일정 검색
    @GetMapping("/todos/search")
    public ResponseEntity<Page<TodoSearchResponse>> getSearchTodo(TodoSearchCondition condition, Pageable pageable){
        return ResponseEntity.ok(todoService.getSearchTodo(condition,pageable));
    }
}

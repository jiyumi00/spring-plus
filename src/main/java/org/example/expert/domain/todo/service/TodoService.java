package org.example.expert.domain.todo.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.request.TodoSaveRequest;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.dto.response.TodoSaveResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchCondition;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.dto.response.UserResponse;
import org.example.expert.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {

    private final TodoRepository todoRepository;
    private final WeatherClient weatherClient;

    @Transactional
    public TodoSaveResponse saveTodo(AuthUser authUser, TodoSaveRequest todoSaveRequest) {
        User user = User.fromAuthUser(authUser);

        String weather = weatherClient.getTodayWeather();

        Todo newTodo = new Todo(
                todoSaveRequest.getTitle(),
                todoSaveRequest.getContents(),
                weather,
                user
        );
        Todo savedTodo = todoRepository.save(newTodo);

        return new TodoSaveResponse(
                savedTodo.getId(),
                savedTodo.getTitle(),
                savedTodo.getContents(),
                weather,
                new UserResponse(user.getId(),user.getNickname(), user.getEmail())
        );
    }


    public Page<TodoResponse> getTodos(String weather,String searchStartDate,String searchEndDate, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Todo> todos;

        if(searchStartDate==null ||searchStartDate.isEmpty()){
            searchStartDate="00010101";
        }
        if(searchEndDate==null || searchEndDate.isEmpty()){
            searchEndDate="99991231";
        }

        LocalDateTime startModifiedDate=LocalDateTime.parse(searchStartDate, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDateTime endModifiedDate=LocalDateTime.parse(searchEndDate,DateTimeFormatter.ofPattern("yyyyMMdd"));

        if(weather==null || weather.isEmpty()){
            todos=todoRepository.findAllByOrderByModifiedAtDesc(pageable);
        }else{
            todos=todoRepository.findByWeatherOrderByModifiedAtDesc(weather,pageable);
        }


        return todos.map(todo -> new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(todo.getUser().getId(),todo.getUser().getNickname(), todo.getUser().getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        ));
    }


    public TodoResponse getTodo(long todoId) {
        Todo todo = todoRepository.findByIdWithUser_Querydsl(todoId)
                .orElseThrow(() -> new InvalidRequestException("Todo not found"));

        User user = todo.getUser();

        return new TodoResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getContents(),
                todo.getWeather(),
                new UserResponse(user.getId(),user.getNickname(), user.getEmail()),
                todo.getCreatedAt(),
                todo.getModifiedAt()
        );
    }


    public Page<TodoSearchResponse> getSearchTodo(TodoSearchCondition condition, Pageable pageable) {
        return todoRepository.searchTodoPage(condition,pageable);
    }
}

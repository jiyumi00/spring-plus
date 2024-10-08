package org.example.expert.domain.todo.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TodoSearchCondition {
    private String title;
    private String nickname;
}

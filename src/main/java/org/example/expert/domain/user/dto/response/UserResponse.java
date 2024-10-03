package org.example.expert.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserResponse {

    private final Long id;
    private final String nickname;
    private final String email;

    public UserResponse(Long id, String nickname,String email) {
        this.id = id;
        this.nickname=nickname;
        this.email = email;
    }
}

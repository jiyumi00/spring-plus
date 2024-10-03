package org.example.expert.domain.common.dto;

import lombok.Getter;
import org.example.expert.domain.user.enums.UserRole;

@Getter
public class AuthUser {

    private final Long id;
    private final String nickname;
    private final String email;
    private final UserRole userRole;

    public AuthUser(Long id, String nickname, String email, UserRole userRole) {
        this.id = id;
        this.nickname=nickname;
        this.email = email;
        this.userRole = userRole;
    }
}

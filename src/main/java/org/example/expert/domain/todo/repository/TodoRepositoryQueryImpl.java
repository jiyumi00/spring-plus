package org.example.expert.domain.todo.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.expert.domain.todo.entity.Todo;

import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.todo.entity.QTodo.todo;
import static org.example.expert.domain.user.entity.QUser.user;

public class TodoRepositoryQueryImpl implements TodoRepositoryQuery{
    private final JPAQueryFactory queryFactory;

    public TodoRepositoryQueryImpl(EntityManager em){
        this.queryFactory=new JPAQueryFactory(em);
    }

    @Override
    public Optional<Todo> findByIdWithUser_Querydsl(Long todoId) {
        return Optional.ofNullable(queryFactory
                .select(todo)
                .from(todo)
                .leftJoin(todo.user,user)
                .where(todo.id.eq(todoId))
                .fetchOne());
    }
}

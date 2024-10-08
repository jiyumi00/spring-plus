package org.example.expert.domain.todo.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.example.expert.domain.todo.dto.response.QTodoSearchResponse;
import org.example.expert.domain.todo.dto.response.TodoSearchCondition;
import org.example.expert.domain.todo.dto.response.TodoSearchResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static org.example.expert.domain.comment.entity.QComment.comment;
import static org.example.expert.domain.manager.entity.QManager.manager;
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

    @Override
    public Page<TodoSearchResponse> searchTodoPage(TodoSearchCondition condition, Pageable pageable) {
        List<TodoSearchResponse> content=queryFactory
                .select(new QTodoSearchResponse(
                        todo.title,
                        manager.count().as("managerCount"),
                        comment.count().as("commentCount")
                ))
                .from(todo)
                .leftJoin(todo.managers,manager)
                .leftJoin(todo.comments,comment)
                .leftJoin(todo.user,user)
                .where(
                    titleContain(condition.getTitle()),
                    nicknameContain(condition.getNickname())
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Todo> countQuery=queryFactory
                .select(todo)
                .from(todo)
                .leftJoin(todo.managers,manager)
                .leftJoin(todo.comments,comment)
                .leftJoin(todo.user,user)
                .where(
                        titleContain(condition.getTitle()),
                        nicknameContain(condition.getNickname())
                );

        return PageableExecutionUtils.getPage(content,pageable,countQuery::fetchCount);

    }
    private BooleanExpression titleContain(String title) {
        return StringUtils.hasText(title) ? todo.title.contains(title) : null;
    }

    private BooleanExpression nicknameContain(String nickname) {
        return StringUtils.hasText(nickname) ? user.nickname.contains(nickname) : null;
    }




}

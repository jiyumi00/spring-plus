package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryQuery{

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE t.modifiedAt >=:startModifiedDate AND t.modifiedAt<=:endModifiedDate ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc( @Param("startModifiedDate") LocalDateTime startModifiedDate, @Param("endModifiedDate") LocalDateTime endModifiedDate, Pageable pageable);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u WHERE t.weather =:weather AND t.modifiedAt >=:startModifiedDate AND t.modifiedAt<=:endModifiedDate ORDER BY t.modifiedAt DESC")
    Page<Todo> findByWeatherOrderByModifiedAtDesc(@Param("weather") String weather,@Param("startModifiedDate") LocalDateTime startModifiedDate, @Param("endModifiedDate") LocalDateTime endModifiedDate, Pageable pageable);

}

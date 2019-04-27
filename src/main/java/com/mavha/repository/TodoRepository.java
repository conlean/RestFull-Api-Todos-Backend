package com.mavha.repository;

import com.mavha.model.State;
import com.mavha.model.Todo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TodoRepository extends CrudRepository<Todo, Long> {

    List<Todo> findByDescriptionContains(String description);

    List<Todo> findByState(State state);

    List<Todo> findByDescriptionContainsAndState(String description, State state);


}

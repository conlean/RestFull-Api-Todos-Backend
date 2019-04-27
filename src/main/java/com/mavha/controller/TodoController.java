package com.mavha.controller;

import com.mavha.exception.ResourceNotFoundException;
import com.mavha.model.State;
import com.mavha.model.Todo;
import com.mavha.repository.TodoRepository;
import com.mavha.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class TodoController {

    private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

    private final StorageService storageService;
    private final TodoRepository todoRepository;

    public TodoController(StorageService storageService, TodoRepository todoRepository) {
        this.storageService = storageService;
        this.todoRepository = todoRepository;
    }

    @GetMapping("/todos")
    public Iterable<Todo> getAllToDos() {
        logger.info("getting all ToDos");
        return todoRepository.findAll();
    }

    @PostMapping("/todos")
    public Todo createToDo(@Valid @RequestPart("todo") Todo todo, @RequestPart("file") MultipartFile file) {
        logger.info(String.format("Saving todo Title: %s ", todo.getDescription()));
        storageService.store(file);
        todo.setImageName(file.getOriginalFilename());
        return todoRepository.save(todo);
    }


    @GetMapping("/todos/{id}")
    public Todo getTodoById(@PathVariable(value = "id") Long toDoid) {
        logger.info(String.format("Finding Todo id: %s ", toDoid));
        return todoRepository.findById(toDoid)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", toDoid));
    }

    @GetMapping("/todos/")
    public Iterable<Todo> getTodoBydescriptionAndState(@RequestParam("description") String description,
                                                       @RequestParam("state") State state) {
        logger.info(String.format("Finding Todo by Description: %s and state %s", description, state));
        return todoRepository.findByDescriptionContainsAndState(description, state);

    }

    @GetMapping("/todos/description/")
    public Iterable<Todo> getTodoBydescription(@RequestParam("description") String description) {
        logger.info(String.format("Finding Todo by Description: %s ", description));
        return todoRepository.findByDescriptionContains(description);

    }

    /**
     * @GetMapping("/todos/state/{state}") public Iterable<Todo> getTodoByState(@PathVariable(value = "state") State state) {
     * logger.info(String.format("Finding Todo by State: %s ", state));
     * return todoRepository.findByState(state);
     * <p>
     * }
     **/
    @PutMapping("/todos/{id}")
    public Todo updateProduct(@PathVariable(value = "id") Long toDoId,
                              @Valid @RequestBody Todo todoUpdated) {

        Todo todo = todoRepository.findById(toDoId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", toDoId));

        todo.setState(todoUpdated.getState());

        Todo updatedTodo = todoRepository.save(todo);
        logger.info(String.format("todo Title: %s was updated", todo.getDescription()));

        return updatedTodo;
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable(value = "id") Long toDoId) {
        Todo todo = todoRepository.findById(toDoId)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", toDoId));

        storageService.delete(todo.getImageName());
        todoRepository.delete(todo);

        logger.info(String.format("Todo id:  %s deleted ", toDoId));
        return ResponseEntity.accepted().build();
    }
}

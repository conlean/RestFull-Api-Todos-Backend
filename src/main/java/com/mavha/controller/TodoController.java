package com.mavha.controller;

import com.mavha.model.Todo;
import com.mavha.repository.TodoRepository;
import com.mavha.exception.ResourceNotFoundException;
import com.mavha.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


@RestController
@RequestMapping("/api")
public class TodoController {

    Logger logger = LoggerFactory.getLogger(TodoController.class);

    private final StorageService storageService;

    @Autowired
    public TodoController(StorageService storageService) {
        this.storageService = storageService;
    }


    @Autowired
    TodoRepository todoRepository;

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
        logger.info(String.format("Finding Todo id: %s ",toDoid));
        return todoRepository.findById(toDoid)
                .orElseThrow(() -> new ResourceNotFoundException("Todo", "id", toDoid));
    }

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

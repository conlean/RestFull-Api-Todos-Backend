package com.mavha.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mavha.model.State;
import com.mavha.model.Todo;
import com.mavha.repository.TodoRepository;
import com.mavha.storage.StorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TodoControllerTest {

    private TodoController todoController;

    @MockBean
    private TodoRepository todoRepository;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StorageService storageService;

    @Before
    public void setup() {
        todoController = new TodoController(storageService, todoRepository);
    }


    @Test
    @WithMockUser(value = "user")
    public void saveToDO_shouldSucceedWith200() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "other-file-name.jpg", "text/plain", "some other type".getBytes());
        MockMultipartFile todoMp = new MockMultipartFile("todo", "", "application/json", "{\"description\":\"Test API\", \"state\": \"CREATED\"}".getBytes());

        this.mvc.perform(MockMvcRequestBuilders.multipart("/api/todos")
                .file(todoMp)
                .file(file))
                .andDo(print())
                .andExpect(status().isOk());
        //then
        then(this.storageService).should().store(file);
        verify(todoRepository, times(1)).save(any(Todo.class));


    }


    @WithMockUser(value = "user")
    @Test
    public void givenTodo_shouldSucceedWith200() throws Exception {

        // given
        Optional<Todo> todo = Optional.of(new Todo("Todo1", State.COMPLETED));
        when(todoRepository.findById(1L)).thenReturn(todo);
        //when
        mvc.perform(MockMvcRequestBuilders.get("/api/todos/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        // then
        verify(todoRepository, times(1)).findById(anyLong());


    }

    @WithMockUser(value = "user")
    @Test
    public void given_shouldSucceedWith200() throws Exception {

        // given
        Optional<Todo> todo = Optional.of(new Todo("Todo1", State.COMPLETED));
        when(todoRepository.findById(1L)).thenReturn(todo);
        //when
        mvc.perform(MockMvcRequestBuilders.get("/api/todos/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
        // then
        verify(todoRepository, times(1)).findById(anyLong());

    }

    @WithMockUser(value = "user")
    @Test
    public void updateTodo_shouldSucceedWith200() throws Exception {

        // given
        Todo todo = new Todo("Description", State.COMPLETED);
        Optional<Todo> todoOptional = Optional.of(todo);

        //when
        when(todoRepository.findById(1L)).thenReturn(todoOptional);

        ObjectMapper mapper = new ObjectMapper();
        mvc.perform(put("/api/todos/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(todo))
        ).andDo(print())
                .andExpect(status().isOk());

        // then
        verify(todoRepository, times(1)).findById(anyLong());
        verify(todoRepository, times(1)).save(any(Todo.class));

    }


    @WithMockUser(value = "user")
    @Test
    public void deleteTodo_shouldAcceptedWith202() throws Exception {

        // given
        Optional<Todo> todo = Optional.of(new Todo("Todo1", State.COMPLETED));
        when(todoRepository.findById(1L)).thenReturn(todo);

        // when
        mvc.perform(MockMvcRequestBuilders.delete("/api/todos/{id}", 1))
                .andDo(print()).andExpect(status().isAccepted());

        // then
        verify(todoRepository, times(1)).findById(anyLong());
        verify(todoRepository, times(1)).delete(any(Todo.class));
        verify(storageService, times(1)).delete(any());

    }

    @WithMockUser(value = "user")
    @Test
    public void givenAllTodosByDescriptionAndState_shouldSucceedWith200() throws Exception {

        //given
        List<Todo> mockedList = new LinkedList<>();
        Todo todo = new Todo("Todo1", State.COMPLETED);
        Todo todo2 = new Todo("Todo1", State.COMPLETED);
        mockedList.add(todo);
        mockedList.add(todo2);

        when(todoRepository.findByDescriptionContainsAndState("Todo1", State.CREATED)).thenReturn(mockedList);

        // when
        mvc.perform(get("/api/todos/search?description=Todo1&state=CREATED")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        verify(todoRepository, times(1)).findByDescriptionContainsAndState("Todo1",State.CREATED);

    }

    @WithMockUser(value = "user")
    @Test
    public void givenAllTodosByDescription_shouldSucceedWith200() throws Exception {

        //given
        List<Todo> mockedList = new LinkedList<>();
        Todo todo = new Todo("Todo1", State.COMPLETED);
        Todo todo2 = new Todo("Todo1", State.COMPLETED);
        mockedList.add(todo);
        mockedList.add(todo2);

        when(todoRepository.findByDescriptionContains("description")).thenReturn(mockedList);

        // when
        mvc.perform(get("/api/todos/search?description=Todo1")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        verify(todoRepository, times(1)).findByDescriptionContains(anyString());

    }

    @WithMockUser(value = "user")
    @Test
    public void givenAllTodosByState_shouldSucceedWith200() throws Exception {

        //given
        List<Todo> mockedList = new LinkedList<>();
        Todo todo = new Todo("Todo1", State.COMPLETED);
        Todo todo2 = new Todo("Todo1", State.COMPLETED);
        mockedList.add(todo);
        mockedList.add(todo2);

        when(todoRepository.findByState(State.CREATED)).thenReturn(mockedList);

        // when
        mvc.perform(get("/api/todos/search?state=CREATED")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        // then
        verify(todoRepository, times(1)).findByState(State.CREATED);

    }



}

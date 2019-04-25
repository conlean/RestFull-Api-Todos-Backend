package com.mavha;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mavha.storage.StorageService;
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

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TodoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private StorageService storageService;

    @WithMockUser(value = "user")
    @Test
    public void givenTodo_shouldSucceedWith200() throws Exception {
        mvc.perform(get("/api/todos")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(value = "user")
    @Test
    public void deleteTodo_shouldSucceedWith200() throws Exception {

        mvc.perform(MockMvcRequestBuilders.delete("/api/todos/{id}", 1))
                .andExpect(status().isAccepted());
    }

    @Test
    @WithMockUser(value = "user")
    public void shouldSaveToDO_shouldSucceedWith200() throws Exception {

        MockMultipartFile file = new MockMultipartFile("file", "other-file-name.jpg", "text/plain", "some other type".getBytes());
        MockMultipartFile todo = new MockMultipartFile("todo", "", "application/json", "{\"description\":\"Test API\", \"state\": \"CREATED\"}".getBytes());

        this.mvc.perform(MockMvcRequestBuilders.multipart("/api/todos")
                .file(todo)
                .file(file))
                .andDo(print())
                .andExpect(status().isOk());

        then(this.storageService).should().store(file);
    }


    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}

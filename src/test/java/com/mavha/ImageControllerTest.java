package com.mavha;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ImageControllerTest {


    @Autowired
    private MockMvc mvc;

    @Test
    public void givenImage_shouldSucceedWith200() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/resources/image/{filename:.+}", "river.jfif"))
                .andExpect(status().isOk());
    }

}

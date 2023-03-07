package com.s3example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class RegisterRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void whenValidInput_thenReturns200() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/bucket/objects")
                        .accept(MediaType.APPLICATION_JSON)
                        .param("start_date", "01-01-2023")
                        .param("end_date", "05-02-2023"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void whenInValidInput_thenReturns400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/bucket/objects")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }
}

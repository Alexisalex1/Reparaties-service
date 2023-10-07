package nl.backend.reparatieservice.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getAllCustomers() throws Exception {
        String createRequestJson = """
                {
                "optionName"    :   "Rubber bands",
                "description"   :   "new rubber bands",
                "cost"          :   "30.0"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/repair-options")
                        .contentType("application/json")
                        .content(createRequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.get("/repair-options")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].optionName").value("Rubber bands"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description").value("new rubber bands"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].cost").value("30.0"));
    }

}


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
class RepairOptionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void createRepairOption() throws Exception {
        String requestJson = """
                {
                "optionName"    :   "Rubber bands",
                "description"   :   "new rubber bands",
                "cost"          :   "30"
                }
                """;


        mockMvc.perform(MockMvcRequestBuilders.post("/repair-options")
                        .contentType("application/json")
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.optionName").value("Rubber bands"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("new rubber bands"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cost").value("30"));
    }


    @Test
    void getAllRepairOptions() throws Exception {
        String createRequestJson = """
                {
                "name"    :   "Alexis",
                "email"   :   "Alexis@java.nl",
                "phoneNumber"         :   "069876544"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.post("/customers")
                        .contentType("application/json")
                        .content(createRequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        mockMvc.perform(MockMvcRequestBuilders.get("/customers")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Alexis"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("Alexis@java.nl"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].phoneNumber").value("069876544"));
    }

}
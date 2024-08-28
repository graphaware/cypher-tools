package com.graphaware.cypher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.graphaware.cypher.web.CypherRequest;
import com.graphaware.cypher.web.CypherResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
public class CypherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testPostCypher() throws Exception {
        var rels = List.of(
                "(Person, LIVES_IN, City)",
                "(Person, HAS_ACCOUNT, UserAccount)",
                "(Vehicle, LICENCED_TO, LicensePlate)",
                "(Vehicle, OWNED_BY, Person)",
                "(AlprEvent, CAPTURED, LicensePlate)"
        );

        var cypher = """
                MATCH path=(p:Person)-[:OWNED_BY]->(v:Vehicle)-[:LICENCED_TO]-(l:LicensePlate)
                WHERE p.gender = 'M'
                AND l.registrationNumber CONTAINS '88'
                RETURN path
                """;
        var request = new CypherRequest(cypher, rels);
        String jsonRequest = objectMapper.writeValueAsString(request);
        var result = mockMvc.perform(MockMvcRequestBuilders.post("/enforceSchema")
                        .contentType("application/json")
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        CypherResponse cypherResponse = objectMapper.readValue(result.getResponse().getContentAsString(), CypherResponse.class);

        assertThat(cypherResponse.cypher()).contains("(p:`Person`)<-[:`OWNED_BY`]-(v:`Vehicle`)");
    }
}
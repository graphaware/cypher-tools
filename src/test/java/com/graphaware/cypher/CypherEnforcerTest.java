package com.graphaware.cypher;

import com.graphaware.cypher.services.SchemaEnforcerService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class CypherEnforcerTest {

    @Test
    void test_enforce_schema() {
        var service = new SchemaEnforcerService();
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

        var result = service.enforceSchema(cypher, rels);
        assertThat(result).contains("(p:`Person`)<-[:`OWNED_BY`]-(v:`Vehicle`)");
    }
}

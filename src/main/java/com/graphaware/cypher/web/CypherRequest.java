package com.graphaware.cypher.web;

import java.util.List;
import java.util.Optional;

public record CypherRequest(String cypher, List<String> relationships) {
    public CypherRequest {
        relationships = List.copyOf(Optional.ofNullable(relationships).orElse(List.of()));
    }
}

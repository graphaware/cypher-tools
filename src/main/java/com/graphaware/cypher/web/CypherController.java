package com.graphaware.cypher.web;

import com.graphaware.cypher.services.SchemaEnforcerService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CypherController {

    private final SchemaEnforcerService schemaEnforcerService;

    public CypherController(SchemaEnforcerService schemaEnforcerService) {
        this.schemaEnforcerService = schemaEnforcerService;
    }

    @PostMapping("/enforceSchema")
    public ResponseEntity<CypherResponse> enforce(@RequestBody CypherRequest request) {
        return ResponseEntity.ok(
                new CypherResponse(
                        schemaEnforcerService.enforceSchema(request.cypher(), request.relationships())
                ));
    }
}

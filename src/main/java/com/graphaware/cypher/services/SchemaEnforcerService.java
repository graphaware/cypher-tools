package com.graphaware.cypher.services;

import org.neo4j.cypherdsl.core.renderer.Configuration;
import org.neo4j.cypherdsl.core.renderer.Renderer;
import org.neo4j.cypherdsl.parser.CypherParser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchemaEnforcerService {

    public String enforceSchema(String cypherQuery, List<String> relationships) {
        var statement = CypherParser.parse(cypherQuery);

        var configuration = Configuration.newConfig()
                .withPrettyPrint(true)
                .alwaysEscapeNames(true)
                .withEnforceSchema(true);

        relationships.stream().map(Configuration::relationshipDefinition)
                .forEach(configuration::withRelationshipDefinition);

        return Renderer.getRenderer(configuration.build()).render(statement);
    }
}

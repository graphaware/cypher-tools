# Cypher Tools

Experimental project exposing cypher tooling features as a rest service.

## Docker image

The docker image is available as `graphaware/cypher-tools:<tag>`.

Ex : 

```bash
docker run --rm -it --name cypher-tools-1 -p 8080:8080 graphaware/cypher-tools:1.0.0
```

## Schema enforcement

LLM based Cypher query generation can lead to relationships being in the wrong direction, the following endpoint allows 
you to fix the directions of a query based on a provided list of schema relationships.

Example : 

**Generated query** : 

```cypher
MATCH (n:Person)-[:OWNED_BY]->(v:Vehicle)
RETURN n.name, v.model
```

However, the database schema has the relationship between the vehicle and the person instead `(Vehicle)-[:OWNED_BY]->(Person)`

Fix it by calling the `cypher` endpoint

```bash
curl --location 'http://localhost:8080/enforceSchema' \
--header 'Content-Type: application/json' \
--data '{"cypher": "MATCH (n:Person)-[:OWNED_BY]->(v:Vehicle) RETURN n.name, v.model", "relationships": ["(Person, LIVES_IN, City)","(Person, HAS_ACCOUNT, UserAccount)","(Vehicle, LICENCED_TO, LicensePlate)","(Vehicle, OWNED_BY, Person)"]}'
```

Response : 

```json
{
    "cypher": "MATCH (n:`Person`)<-[:`OWNED_BY`]-(v:`Vehicle`)\nRETURN n.name, v.model"
}
```
package org.example.QueryProc;

import org.example.Exceptions.InvalidQueryException;

import java.util.*;

public class PreProc {
    private final List<String> entities = List.of(
            "procedure", "stmtLst", "stmt", "assign", "if", "while"
    );
    private final Map<String, RelationSignature> relations = Map.of(
            "Calls", new RelationSignature(Set.of("procedure", "_", "string"),
                    Set.of("procedure", "_", "string")),

            "Calls*", new RelationSignature(Set.of("procedure", "_", "string"),
                    Set.of("procedure", "_", "string")),

            "Modifies", new RelationSignature(Set.of("procedure", "prog_line", "stmt", "assign", "string", "integer"),
                    Set.of("variable", "string", "_"))
    );


    public QueryTree parseQuery(String query) throws InvalidQueryException {
        List<String> queryElements = List.of(query.split(";"));

        List<String> queryArguments = queryElements.subList(0,queryElements.size()-1);
        String queryBody = queryElements.get(queryElements.size()-1);

        //creating synonym map (separate them, so they can be easily used)
        Map<String,String> synonyms = new HashMap<>();
        for(String element : queryArguments) {
            String[] tmp = Arrays.stream(element
                    .split("[ ,]"))
                    .filter(s->!s.isBlank())
                    .toArray(String[]::new);

            if(!entities.contains(tmp[0])) {
                throw new InvalidQueryException("Invalid synonym type: " + tmp[0]);
            }

            for(int i=1;i<tmp.length;i++) {
                if(!Character.isLetter(tmp[i].charAt(0))) {
                    throw new InvalidQueryException("Invalid synonym name: " + tmp[i]);
                }
                synonyms.put(tmp[i],tmp[0]);
            }
        }

//        How we will validate relations arguments
//        RelationSignature rs = relations.get("Modifies");
//        if(!rs.isArg1TypeAllowed(synonyms.get("a"))) {
//            throw new InvalidQueryException("Invalid argument: " + synonyms.get("a"));
//        }
//        else {
//            System.out.println("valid argument");
//        }

        System.out.println(synonyms);
//        System.out.println(queryArguments);
//        System.out.println(queryBody);

        if(!validate(query)) {
            throw new InvalidQueryException("Invalid query: " + query);
        }
        return crateQueryTree(query);
    }
    private boolean validate(String query) {
        return false;
    }
    private QueryTree crateQueryTree(String query) {
        return new QueryTree();
    }
}

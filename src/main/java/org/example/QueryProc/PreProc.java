package org.example.QueryProc;

import org.example.Exceptions.InvalidQueryException;

import java.util.*;

public class PreProc {
    private final Set<String> entities = Set.of(
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
    private static final Map<String, Set<String>> attributes = Map.of(
            "procName", Set.of("procedure", "call"),
            "varName", Set.of("variable"),
            "value", Set.of("constant"),
            "stmt#", Set.of("stmt", "assign", "while", "if", "call")
    );
    public QueryTree parseQuery(String query) throws InvalidQueryException {
        List<String> queryElements = List.of(query.split(";"));

        List<String> queryArguments = queryElements.subList(0,queryElements.size()-1);
        String queryBody = queryElements.get(queryElements.size()-1);

        Map<String,String> synonyms = separateSynonyms(queryArguments);

        String[] bodyElements = Arrays.stream(queryBody.split("(?=\\b(such that|with)\\b)")).filter(e->!e.isBlank()).toArray(String[]::new);

        String[] returnValues = Arrays.stream(bodyElements[0]
                        .split("[ ,]"))
                .filter(s->!s.isBlank())
                .toArray(String[]::new);

        if(!returnValues[0].equals("Select") && !returnValues[0].equals("Boolean")) {
            throw new InvalidQueryException("Missing Select: " + query);
        }
        for (int i=1;i<returnValues.length;i++) {
            if(!synonyms.containsKey(returnValues[i])) {
                throw new InvalidQueryException("Not recognize synonym: " + returnValues[i]);
            }
        }

        List<String[]> suchThatStatements = new ArrayList<>();
        List<String[]> withStatements = new ArrayList<>();
        for(int i=1;i<bodyElements.length;i++) {
            if(bodyElements[i].startsWith("such that")) {
                bodyElements[i] = bodyElements[i].substring(10).trim();
                String[] tmp = Arrays.stream(bodyElements[i].split("[ ,()]")).filter(t->!t.isBlank()).toArray(String[]::new);
                suchThatStatements.add(tmp);
            }
            else if(bodyElements[i].startsWith("with")) {
                bodyElements[i] = bodyElements[i].substring(5).trim();
                String[] tmp = Arrays.stream(bodyElements[i].split("[ .=]")).filter(t->!t.isBlank()).toArray(String[]::new);
                withStatements.add(tmp);
            }
        }

        validateSuchThatStatements(suchThatStatements,synonyms);
        validateWithStatements(withStatements,synonyms);

        return new QueryTree(synonyms,returnValues,suchThatStatements,withStatements);
    }
    private Map<String,String> separateSynonyms(List<String> queryArguments) throws InvalidQueryException {
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
        return synonyms;
    }
    private void validateSuchThatStatements(List<String[]> suchThatStatements, Map<String,String> synonyms) throws InvalidQueryException {
        for(String[] relation : suchThatStatements) {
            RelationSignature signature = relations.get(relation[0]);
            if(signature == null) {
                throw new InvalidQueryException("Incorrect relation name: " + relation[0]);
            }
            String[] type =  new String[2];
            for(int i=1;i<relation.length;i++) {
                if(relation[i].matches("\\d+")) {
                    type[i-1] = "integer";
                }
                else if(relation[i].charAt(0) == '"' && relation[i].charAt(relation[i].length()-1) == '"') {
                    type[i-1] = "string";
                }
                else if(relation[i].equals("_")) {
                    type[i-1] = "_";
                }
                else {
                    type[i-1] = synonyms.get(relation[i]);
                }

                if(type[i-1] == null) {
                    throw new InvalidQueryException("Synonym " + relation[i] + " not found");
                }
            }
            if(!signature.isArg1TypeAllowed(type[0])) {
                throw new InvalidQueryException("Incorrect argument type: " + relation[1] + " - " + type[0] + " in relation " + relation[0]);
            }
            else if(!signature.isArg2TypeAllowed(type[1])) {
                throw new InvalidQueryException("Incorrect argument type: " + relation[2] + " - " + type[1] + " in relation " + relation[0]);
            }
        }
    }
    private void validateWithStatements(List<String[]> withStatements, Map<String,String> synonyms) throws InvalidQueryException {
        for(String[] statement : withStatements) {
            String type = synonyms.get(statement[0]);
            if(type == null) {
                throw new InvalidQueryException("There is no such synonym as : " + statement[0]);
            }
            if(!attributes.get(statement[1]).contains(type)) {
                throw new InvalidQueryException("Synonym " + statement[0] + " does not have attribute " + statement[1]);
            }
            if((statement[1].equals("procName") || statement[1].equals("valName")) &&
                    (statement[2].charAt(0) != '"' || statement[2].charAt(statement[2].length()-1) != '"')) {

                throw new InvalidQueryException("Incorrect type " + statement[2] + ". Type should be string");
            }
            else if((statement[1].equals("value") || statement[1].equals("stmt#")) && !statement[2].matches("\\d+")) {
                throw new InvalidQueryException("Incorrect type " + statement[2] + ". Type should be integer");
            }
        }
    }
}

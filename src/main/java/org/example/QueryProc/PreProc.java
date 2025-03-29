package org.example.QueryProc;

import org.example.Exceptions.InvalidQueryException;
import org.example.QueryProc.model.*;
import org.example.QueryProc.staticVal.GrammarRules;

import java.util.*;

public class PreProc {
    public QueryTree parseQuery(String query) throws InvalidQueryException {
        List<String> elements = List.of(query.split(";"));

        List<String> args = elements.subList(0,elements.size()-1);
        String body = elements.get(elements.size()-1);

        if(body.isEmpty()) {
            throw new InvalidQueryException("Query can't have empty body");
        }

        Map<String,String> synonyms = separateSynonyms(args);

        String[] bodyElements = Arrays.stream(body.split("(?=\\b(such that|with)\\b)"))
                .filter(e->!e.isBlank())
                .toArray(String[]::new);

        ReturnDesc returnDesc = determineReturnValues(bodyElements[0],synonyms);
        BodyDesc bodyDesc = determineQueryBody(Arrays.copyOfRange(bodyElements,1,bodyElements.length), synonyms);

        List<Relation> relations = validateSuchThatStatements(bodyDesc.suchThatStatements,synonyms);
        List<WithStatement> withStatements = validateWithStatements(bodyDesc.withStatements,synonyms);

        return new QueryTree(returnDesc.isBoolean,returnDesc.returnValues,relations,withStatements);
    }
    private Map<String,String> separateSynonyms(List<String> queryArguments) throws InvalidQueryException {
        Map<String,String> synonyms = new HashMap<>();
        for(String element : queryArguments) {
            String[] tmp = Arrays.stream(element
                            .split("[ ,]"))
                    .filter(s->!s.isBlank())
                    .toArray(String[]::new);

            if(!GrammarRules.ENTITIES.contains(tmp[0])) {
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
    private record ReturnDesc(boolean isBoolean, List<Argument> returnValues) {}
    private ReturnDesc determineReturnValues(String returnBody,  Map<String,String> synonyms) throws InvalidQueryException {
        String[] returnElements = Arrays.stream(returnBody
                        .split("[ ,]"))
                .filter(s->!s.isBlank())
                .toArray(String[]::new);

        boolean isBoolean = false;
        if(returnElements[0].equals("Boolean")) {
            isBoolean = true;
        }
        else if(!returnElements[0].equals("Select")) {
            throw new InvalidQueryException("Missing Select or Boolean");
        }

        List<Argument> returnValues = new ArrayList<>();
        for (int i=1;i<returnElements.length;i++) {
            if(synonyms.containsKey(returnElements[i])) {
                returnValues.add(new Argument(returnElements[i], synonyms.get(returnElements[i])));
            }
            else {
                throw new InvalidQueryException("Not recognize synonym: " + returnElements[i]);
            }
        }
        return new ReturnDesc(isBoolean,returnValues);
    }
    private record BodyDesc(List<String[]> suchThatStatements, List<String[]> withStatements) {}

    private BodyDesc determineQueryBody(String[] queryBody, Map<String, String> synonyms) throws InvalidQueryException {
        List<String[]> suchThatStatements = new ArrayList<>();
        List<String[]> withStatements = new ArrayList<>();

        for (int i = 0; i < queryBody.length; i++) {
            if (queryBody[i].startsWith("such that")) {
                String[] relation = Arrays.stream(queryBody[i]
                                .substring(10)
                                .trim().split("[ ,()]"))
                        .filter(t -> !t.isBlank())
                        .toArray(String[]::new);

                suchThatStatements.add(relation);

            } else if (queryBody[i].startsWith("with")) {
                String[] tmp = Arrays.stream(queryBody[i]
                                .substring(5)
                                .trim().split("[ .=]"))
                        .filter(t -> !t.isBlank())
                        .toArray(String[]::new);

                withStatements.add(tmp);
            }
        }
        return new BodyDesc(suchThatStatements, withStatements);
    }
    private List<Relation> validateSuchThatStatements(List<String[]> suchThatStatements, Map<String,String> synonyms) throws InvalidQueryException {
        List<Relation> relations = new ArrayList<>();
        for(String[] relation : suchThatStatements) {
            ArgumentDefinition signature = GrammarRules.RELATION_DEFINITIONS.get(relation[0]);
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
            relations.add(new Relation(relation[0],new Argument(relation[1],type[0]),new Argument(relation[2],type[1])));
        }
        return relations;
    }
    private List<WithStatement> validateWithStatements(List<String[]> withStatements, Map<String,String> synonyms) throws InvalidQueryException {
        List<WithStatement> result = new ArrayList<>();
        for(String[] statement : withStatements) {
            String type = synonyms.get(statement[0]);
            if(type == null) {
                throw new InvalidQueryException("There is no such synonym as : " + statement[0]);
            }
            if(!GrammarRules.ATTRIBUTES.get(statement[1]).contains(type)) {
                throw new InvalidQueryException("Synonym " + statement[0] + " does not have attribute " + statement[1]);
            }
            if((statement[1].equals("procName") || statement[1].equals("valName")) &&
                    (statement[2].charAt(0) != '"' || statement[2].charAt(statement[2].length()-1) != '"')) {

                throw new InvalidQueryException("Incorrect type " + statement[2] + ". Type should be string");
            }
            else if((statement[1].equals("value") || statement[1].equals("stmt#")) && !statement[2].matches("\\d+")) {
                throw new InvalidQueryException("Incorrect type " + statement[2] + ". Type should be integer");
            }
            result.add(new WithStatement(statement[0], type, statement[1], statement[2]));
        }
        return result;
    }
}

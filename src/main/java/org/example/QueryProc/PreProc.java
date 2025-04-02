package org.example.QueryProc;

import org.example.Exceptions.InvalidQueryException;
import org.example.PKB.API.EntityType;
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

        Map<String,EntityType> synonyms = separateSynonyms(args);

        String[] bodyElements = Arrays.stream(body.split("(?=\\b(such that|with)\\b)"))
                .filter(e->!e.isBlank())
                .toArray(String[]::new);

        ReturnDesc returnDesc = determineReturnValues(bodyElements[0],synonyms);
        BodyDesc bodyDesc = determineQueryBody(Arrays.copyOfRange(bodyElements,1,bodyElements.length));

        List<Relation> relations = validateSuchThatStatements(bodyDesc.suchThatStatements,synonyms);
        List<WithStatement> withStatements = validateWithStatements(bodyDesc.withStatements,synonyms);

        return new QueryTree(returnDesc.isBoolean,returnDesc.returnValues,relations,withStatements);
    }
    private Map<String,EntityType> separateSynonyms(List<String> queryArguments) throws InvalidQueryException {
        Map<String,EntityType> synonyms = new HashMap<>();
        for(String element : queryArguments) {
            String[] tmp = Arrays.stream(element
                            .split("[ ,]"))
                    .filter(s->!s.isBlank())
                    .toArray(String[]::new);

            EntityType entityType = GrammarRules.ENTITIES
                    .stream()
                    .filter(entity->entity.name().toLowerCase().equals(tmp[0]))
                    .findFirst()
                    .orElseThrow(()->new InvalidQueryException("Invalid synonym type: " + tmp[0]));

            for(int i=1;i<tmp.length;i++) {
                if(!Character.isLetter(tmp[i].charAt(0))) {
                    throw new InvalidQueryException("Invalid synonym name: " + tmp[i]);
                }
                synonyms.put(tmp[i],entityType);
            }
        }
        return synonyms;
    }
  
    private record ReturnDesc(boolean isBoolean, List<Argument> returnValues) {}
    private ReturnDesc determineReturnValues(String returnBody,  Map<String,EntityType> synonyms) throws InvalidQueryException {
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

        if(!isBoolean && returnElements.length == 1) {
            throw new InvalidQueryException("Missing return values");
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
    private BodyDesc determineQueryBody(String[] queryBody) throws InvalidQueryException {
        List<String[]> suchThatStatements = new ArrayList<>();
        List<String[]> withStatements = new ArrayList<>();

        for (String s : queryBody) {
            if (s.startsWith("such that")) {
                String[] relation = Arrays.stream(s
                                .replace("such that", "")
                                .trim().split("[ ,()]"))
                        .filter(t -> !t.isBlank())
                        .toArray(String[]::new);

                suchThatStatements.add(relation);

            } else if (s.startsWith("with")) {
                if (s.chars().filter(ch -> ch == '=').count() != 1) {
                    throw new InvalidQueryException("Invalid number of =: " + s.chars().filter(ch -> ch == '=').count() + " In statement: " + s);
                }
                String[] tmp = Arrays.stream(s
                                .replace("with", "")
                                .trim().split("[ .=]"))
                        .filter(t -> !t.isBlank())
                        .toArray(String[]::new);

                withStatements.add(tmp);
            }
        }
        return new BodyDesc(suchThatStatements, withStatements);
    }
    private List<Relation> validateSuchThatStatements(List<String[]> suchThatStatements, Map<String,EntityType> synonyms) throws InvalidQueryException {
        List<Relation> relations = new ArrayList<>();
        for(String[] relation : suchThatStatements) {
            if(relation.length == 0) {
                throw new InvalidQueryException("Empty such that statement");
            }
            if(relation.length != 3) {
                throw new InvalidQueryException("Incorrect number of arguments in relation " + relation[0] + ": " + (relation.length-1));
            }

            ArgumentDefinition signature = GrammarRules.RELATION_DEFINITIONS.get(relation[0]);
            if(signature == null) {
                throw new InvalidQueryException("Incorrect relation name: " + relation[0]);
            }
            EntityType[] type =  new EntityType[2];
            for(int i=1;i<relation.length;i++) {
                if(relation[i].matches("\\d+")) {
                    type[i-1] = EntityType.INTEGER;
                }
                else if(relation[i].charAt(0) == '"' && relation[i].charAt(relation[i].length()-1) == '"') {
                    type[i-1] = EntityType.STRING;
                }
                else if(relation[i].equals("_")) {
                    type[i-1] = EntityType.BLANK;
                }
                else {
                    type[i-1] = synonyms.get(relation[i]);
                }

                if(type[i-1] == null) {
                    throw new InvalidQueryException("Synonym " + relation[i] + " not found");
                }
            }
            if(!signature.isArg1TypeAllowed(type[0].name().toLowerCase())) {
                throw new InvalidQueryException("Incorrect argument type: " + relation[1] + " - " + type[0] + " in relation " + relation[0]);
            }
            else if(!signature.isArg2TypeAllowed(type[1].name().toLowerCase())) {
                throw new InvalidQueryException("Incorrect argument type: " + relation[2] + " - " + type[1] + " in relation " + relation[0]);
            }
            relations.add(new Relation(relation[0],new Argument(relation[1],type[0]),new Argument(relation[2],type[1])));
        }
        return relations;
    }

    private void validateWithStatements(List<String[]> withStatements, Map<String,EntityType> synonyms) throws InvalidQueryException {
        List<WithStatement> result = new ArrayList<>();
        for(String[] statement : withStatements) {
            EntityType type = synonyms.get(statement[0]);
            if(type == null) {
                throw new InvalidQueryException("There is no such synonym as : " + statement[0]);
            }
            if(statement.length != 3 && statement.length != 4) {
                throw new InvalidQueryException("Invalid number of arguments: " + statement.length + ". In statement: " + Arrays.toString(statement));
            }
            if(!GrammarRules.ATTRIBUTES.containsKey(statement[1])) {
                throw new InvalidQueryException("Invalid attribute: " + statement[1] + ". In statement: " + Arrays.toString(statement));
            }
            if(!GrammarRules.ATTRIBUTES.get(statement[1]).contains(type)) {
                throw new InvalidQueryException("Synonym " + statement[0] + " does not have attribute " + statement[1]);
            }
            if((statement[1].equals("procName") || statement[1].equals("varName")) &&
                    (statement[2].charAt(0) != '"' || statement[2].charAt(statement[2].length()-1) != '"')) {

                throw new InvalidQueryException("Incorrect value " + statement[2] + ". " + statement[1] + " value should be of type string");
            }
            else if((statement[1].equals("value") || statement[1].equals("stmt#")) && !statement[2].matches("\\d+")) {
                throw new InvalidQueryException("Incorrect type " + statement[2] + ". Type should be integer");
            }
            result.add(new WithStatement(statement[0], type, statement[1], statement[2]));
        }
        return result;
    }
}

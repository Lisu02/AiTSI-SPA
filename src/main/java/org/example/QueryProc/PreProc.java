package org.example.QueryProc;

import org.example.Exceptions.InvalidQueryException;
import org.example.PKB.API.EntityType;
import org.example.QueryProc.model.*;
import org.example.QueryProc.staticVal.GrammarRules;

import java.util.*;
import java.util.stream.Collectors;

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

        Set<Argument> synonymArg = synonyms.entrySet().stream()
                .map(entry -> new Argument(entry.getKey(), entry.getValue()))
                .collect(Collectors.toSet());

        return new QueryTree(synonymArg,returnDesc.isBoolean,returnDesc.returnValues,relations,withStatements);
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
                if(synonyms.containsKey(tmp[i])) {
                    throw new InvalidQueryException("Synonym of name " + tmp[i] + " already exists");
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

//        if(returnElements[0].equals("Boolean")) {
//            isBoolean = true;
//        }
        if(!returnElements[0].equals("Select")) {
            throw new InvalidQueryException("Missing Select");
        }
        if(returnElements.length >= 2 && returnElements[1].equals("BOOLEAN")) {
            if(returnElements.length != 2) {
                throw new InvalidQueryException("BOOLEAN query can't have return values");
            }
            return new ReturnDesc(true,new ArrayList<>());
        }
        if(returnElements.length == 1) {
            throw new InvalidQueryException("Missing return values");
        }

        List<Argument> returnValues = new ArrayList<>();
        for (int i=1;i<returnElements.length;i++) {
            if(!synonyms.containsKey(returnElements[i])) {
                throw new InvalidQueryException("Not recognize synonym: " + returnElements[i]);
            }
            Argument arg = new Argument(returnElements[i], synonyms.get(returnElements[i]));
            if(returnValues.contains(arg)) {
                throw new InvalidQueryException("Return value: " + arg + " already exists");
            }
            returnValues.add(arg);
        }
        return new ReturnDesc(false,returnValues);
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
                    type[i-1] = signature.defaultType();
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

//    private List<WithStatement> validateWithStatements(List<String[]> withStatements, Map<String,EntityType> synonyms) throws InvalidQueryException {
//        List<WithStatement> result = new ArrayList<>();
//        for(String[] statement : withStatements) {
//            if(statement.length != 3 && statement.length != 4) {
//                throw new InvalidQueryException("Invalid number of arguments: " + statement.length + ". In statement: " + Arrays.toString(statement));
//            }
//
//            EntityType firstArgType = synonyms.get(statement[0]);
//            EntityType secondArgType = synonyms.get(statement[2]);
//
//            if(firstArgType == null) {
//                throw new InvalidQueryException("There is no such synonym as : " + statement[0]);
//            }
//            if(secondArgType != null && statement.length != 4) {
//                throw new InvalidQueryException(statement[2] + "is missing an attribute");
//            }
//            if(!GrammarRules.ATTRIBUTES.containsKey(statement[1])) {
//                throw new InvalidQueryException("Invalid attribute: " + statement[1] + ". In statement: " + Arrays.toString(statement));
//            }
//            if(secondArgType != null && !GrammarRules.ATTRIBUTES.containsKey(statement[3])) {
//                throw new InvalidQueryException("Invalid attribute: " + statement[3] + ". In statement: " + Arrays.toString(statement));
//            }
//            if(!GrammarRules.ATTRIBUTES.get(statement[1]).contains(firstArgType)) {
//                throw new InvalidQueryException("Synonym " + statement[0] + " does not have attribute " + statement[1]);
//            }
//            if(secondArgType != null  && !GrammarRules.ATTRIBUTES.get(statement[3]).contains(secondArgType)) {
//                throw new InvalidQueryException("Synonym " + statement[2] + " does not have attribute " + statement[3]);
//            }
//            if(secondArgType != null && !statement[1].equals(statement[3])) {
//                throw new InvalidQueryException("Attribute " + statement[1] + " and attribute " + statement[3] + " must be the same");
//            }
//
//            if(statement[1].equals("procName") || statement[1].equals("varName")) {
//                if(statement[2].charAt(0) == '"' && statement[2].charAt(statement[2].length()-1) == '"') {
//                    secondArgType = EntityType.STRING;
//                }
//                else if (secondArgType == null) {
//                    throw new InvalidQueryException("Incorrect value " + statement[2] + ". " + statement[1] + " value should be of type string");
//                }
//            }
//            else if(statement[1].equals("value") || statement[1].equals("stmt#")) {
//                if(statement[2].matches("\\d+")) {
//                    secondArgType = EntityType.INTEGER;
//                }
//                else if (secondArgType == null) {
//                    throw new InvalidQueryException("Incorrect type " + statement[2] + ". Type should be integer");
//                }
//            }
//            result.add(new WithStatement(new Argument(statement[0], firstArgType), statement[1], new Argument(statement[2], secondArgType)));
//        }
//        return result;
//    }

    private List<WithStatement> validateWithStatements(List<String[]> withStatements, Map<String,EntityType> synonyms) throws InvalidQueryException {
        List<WithStatement> result = new ArrayList<>();
        for(String[] statement : withStatements) {

            if(statement.length != 3 && statement.length != 4) {
                throw new InvalidQueryException("Invalid number of arguments: " + statement.length + ". In statement: " + Arrays.toString(statement));
            }

            String name1 = statement[0];
            EntityType type1 = synonyms.get(name1);
            String attribute1 = statement[1];

            String name2 = statement[2];
            EntityType type2 = synonyms.get(name2);
            String attribute2 = statement.length == 4 ? statement[3] : null;

            if(type1 == null) {
                throw new InvalidQueryException("There is no such synonym as : " + name1);
            }
            if(type2 == null) {
                type2 = inferLiteralType(name2);
                attribute2 = attribute1;
            }

            validateArgument(name1, type1, attribute1);
            validateArgument(name2, type2, attribute2);

            if(!attribute1.equals(attribute2)) {
                throw new InvalidQueryException("First attribute: " +attribute1 + " and second attribute: " + attribute2 + " must be the same");
            }

            result.add(new WithStatement(new Argument(name1, type1), attribute1, new Argument(name2, type2)));
        }
        return result;
    }
    private EntityType inferLiteralType(String name) throws InvalidQueryException {
        if(name.startsWith("\"") && name.endsWith("\"")) {
            return EntityType.STRING;
        }
        else if(name.matches("\\d+")) {
            return EntityType.INTEGER;
        }
        else {
            throw new InvalidQueryException("Incorrect value " + name);
        }
    }
    private void validateArgument(String name, EntityType type, String attribute) throws InvalidQueryException {
        if(!GrammarRules.ATTRIBUTES.containsKey(attribute)) {
            throw new InvalidQueryException("Invalid attribute: " + attribute);
        }
        if(!GrammarRules.ATTRIBUTES.get(attribute).contains(type)) {
            throw new InvalidQueryException("Synonym " + name + " does not have attribute " + attribute);
        }
    }
}
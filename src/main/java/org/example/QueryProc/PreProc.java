package org.example.QueryProc;

import org.example.Exceptions.InvalidQueryException;

import java.util.*;

public class PreProc {
    private final Set<String> entities = Set.of(
            "procedure", "stmtLst", "stmt", "assign", "if", "while"
    );
    private final Map<String, RelationSignature> relationsDefinition = Map.of(
            "Follows", new RelationSignature(Set.of("stmt", "assign", "call", "while", "if", "_", "integer","prog_line"),
                    Set.of("stmt", "assign", "call", "while", "if", "_", "integer","prog_line")),
            "Follows*", new RelationSignature(Set.of("stmt", "assign", "call", "while", "if", "_", "integer","prog_line"),
                    Set.of("stmt", "assign", "call", "while", "if", "_", "integer","prog_line")),
            "Parent", new RelationSignature(Set.of("stmt", "assign", "call", "while", "if", "_", "integer","prog_line"),
                    Set.of("stmt", "assign", "call", "while", "if", "_", "integer","prog_line")),
            "Parent*", new RelationSignature(Set.of("stmt", "assign", "call", "while", "if", "_", "integer","prog_line"),
                    Set.of("stmt", "assign", "call", "while", "if", "_", "integer","prog_line")),
            "Modifies", new RelationSignature(Set.of("procedure", "prog_line", "stmt", "assign", "call", "while", "if", "string", "integer"),
                    Set.of("variable", "string", "_")),
            "Modifies*", new RelationSignature(Set.of("procedure", "prog_line", "stmt", "assign", "call", "while", "if", "string", "integer"),
                    Set.of("variable", "string", "_")),
            "Uses", new RelationSignature(Set.of("procedure", "stmt", "assign", "call", "while", "if", "_", "integer","prog_line"),
                    Set.of("variable", "_", "string")),
            "Uses*", new RelationSignature(Set.of("procedure", "stmt", "assign", "call", "while", "if", "_", "integer","prog_line"),
                    Set.of("variable", "_", "string")),
            "Calls", new RelationSignature(Set.of("procedure", "_", "string"),
                    Set.of("procedure", "_", "string")),
            "Calls*", new RelationSignature(Set.of("procedure", "_", "string"),
                    Set.of("procedure", "_", "string"))
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

        String[] returnElements = Arrays.stream(bodyElements[0]
                        .split("[ ,]"))
                .filter(s->!s.isBlank())
                .toArray(String[]::new);

        if(!returnElements[0].equals("Select") && !returnElements[0].equals("Boolean")) {
            throw new InvalidQueryException("Missing Select: " + query);
        }

        String returnType = returnElements[0];
        List<Argument> returnValues = new ArrayList<>();
        for (int i=1;i<returnElements.length;i++) {
            if(synonyms.containsKey(returnElements[i])) {
                returnValues.add(new Argument(returnElements[i], synonyms.get(returnElements[i])));
            }
            else {
                throw new InvalidQueryException("Not recognize synonym: " + returnElements[i]);
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

        suchThatStatements.forEach(s->System.out.println(Arrays.toString(s)));

        List<Relation> relations = validateSuchThatStatements(suchThatStatements,synonyms);
        validateWithStatements(withStatements,synonyms);

        return new QueryTree(returnType,returnValues,relations,withStatements);
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
    private List<Relation> validateSuchThatStatements(List<String[]> suchThatStatements, Map<String,String> synonyms) throws InvalidQueryException {
        List<Relation> relations = new ArrayList<>();
        for(String[] relation : suchThatStatements) {
            RelationSignature signature = relationsDefinition.get(relation[0]);
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

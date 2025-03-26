package org.example.QueryProc;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public class QueryTree {
    private Map<String,String> synonyms;
    private List<String> returnValues;
    private List<Relation> relations;
    private List<String[]> withStatements;

    @Override
    public String toString() {
        return "QueryTree {\n" +
                "  synonyms=" + (synonyms != null ? synonyms : "{}") + ",\n" +
                "  returnValues=" +returnValues + ",\n" +
                "  suchThatStatements=\n    " + relations + ",\n" +
                "  withStatements=\n    " + formatList(withStatements) + "\n" +
                '}';
    }

    private String formatList(List<String[]> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }
        return list.stream()
                .map(Arrays::toString)
                .collect(Collectors.joining("\n    "));
    }
    public boolean isThisArgumentReturnValue(Argument arg) {
        return returnValues.contains(arg.getName()) && synonyms.get(arg.getName()).equals(arg.getType());
    }
}

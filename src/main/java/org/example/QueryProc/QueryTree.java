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
    private String[] returnValues;
    private List<String[]> suchThatStatements;
    private List<String[]> withStatements;

    @Override
    public String toString() {
        return "QueryTree {\n" +
                "  synonyms=" + (synonyms != null ? synonyms : "{}") + ",\n" +
                "  returnValues=" + Arrays.toString(returnValues) + ",\n" +
                "  suchThatStatements=\n    " + formatList(suchThatStatements) + ",\n" +
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
}

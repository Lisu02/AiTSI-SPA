package org.example.QueryProc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.PKB.API.EntityType;

import java.util.Objects;
@Data
@AllArgsConstructor
public class Argument {
    private String name;
    private EntityType type;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Argument argument = (Argument) o;

        if (!Objects.equals(name, argument.name)) return false;
        return Objects.equals(type, argument.type);
    }
    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }
}

package org.example.QueryProc.model;

import org.example.PKB.API.EntityType;

import java.util.Objects;
public record Argument(String name, EntityType type) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Argument argument = (Argument) o;

        if (!Objects.equals(name, argument.name)) return false;
        return Objects.equals(type, argument.type);
    }
}

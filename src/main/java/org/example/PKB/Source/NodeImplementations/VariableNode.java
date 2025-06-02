package org.example.PKB.Source.NodeImplementations;

import org.example.PKB.API.Attr;
import org.example.PKB.API.EntityType;
import org.example.PKB.Source.ASTNode;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class VariableNode extends RefNode{
    public VariableNode() {
        super(EntityType.VARIABLE);
    }

    @Override
    public Set<EntityType> getAllEntityTypes() {
        Set<EntityType> set = super.getAllEntityTypes();
        set.add(EntityType.VARIABLE);
        return set;
    }

//    @Override
//    public boolean equals(Object o) // Dodac jezeli konieczne do identyfikowania tych samych zmiennych
//    {
//        if (this == o) return true;
//        if (!(o instanceof VariableNode)) return false;
//        VariableNode that = (VariableNode) o;
//        if (this.getAttr() == null && that.getAttr() == null) return true;
//        if (this.getAttr() == null && that.getAttr() != null) return false;
//        if (this.getAttr() != null && that.getAttr() == null) return false;
//        return Objects.equals(this.getAttr().getVarName(), that.getAttr().getVarName());
//    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        VariableNode other = (VariableNode) obj;
        String thisName = this.getAttr() != null ? this.getAttr().getVarName() : null;
        String otherName = other.getAttr() != null ? other.getAttr().getVarName() : null;

        return Objects.equals(thisName, otherName);
    }

    @Override
    public int hashCode() {
        String varName = getAttr() != null ? getAttr().getVarName() : null;
        return Objects.hash(varName);
    }

}

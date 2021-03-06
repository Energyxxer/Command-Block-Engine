package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

import com.energyxxer.enxlex.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.AbstractFileComponent;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ValueWrapper;

public class ActualParameter extends AbstractFileComponent {
    private String name = null;
    private Value value;
    private boolean used = false;

    public ActualParameter(TokenPattern<?> pattern, String name, Value value) {
        super(pattern);
        this.name = name;
        this.setValue(value);
    }

    public ActualParameter(TokenPattern<?> pattern, Value value) {
        super(pattern);
        this.setValue(value);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        if(value instanceof ValueWrapper) {
            throw new IllegalArgumentException("Should not have been passed a wrapped value");
        }
        else this.value = value;
    }

    public FormalParameter toFormal() {
        return new FormalParameter(getDataType(), name);
    }

    public DataType getDataType() {
        return (value != null) ? value.getDataType() : DataType.OBJECT;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}

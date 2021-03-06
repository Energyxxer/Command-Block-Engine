package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.functions.FunctionSection;
import com.energyxxer.enxlex.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.DataReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.ScoreReference;
import com.energyxxer.craftrlang.compiler.semantic_analysis.references.explicit.ExplicitBoolean;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations.Operator;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public class BooleanValue extends Value {

    public BooleanValue(boolean value, SemanticContext semanticContext) {
        super(new ExplicitBoolean(value), semanticContext);
    }

    public BooleanValue(@NotNull DataReference reference, SemanticContext semanticContext) {
        super(reference, semanticContext);
    }

    @Override
    public DataType getDataType() {
        return DataType.BOOLEAN;
    }

    @Override
    public SymbolTable getSubSymbolTable() {
        return null;
    }

    @Override
    public MethodLog getMethodLog() {
        return null;
    }

    @Override
    public Value runOperation(Operator operator, TokenPattern<?> pattern, FunctionSection section, boolean silent) {
        //TODO
        return null;
        //return (operator == NOT) ? new BooleanValue(!this.value, semanticContext) : null;
    }

    @Override
    public Value runOperation(Operator operator, Value operand, TokenPattern<?> pattern, FunctionSection section, SemanticContext semanticContext, ScoreReference resultReference, boolean silent) {
        //TODO

        /*if(operator == Operator.ASSIGN) {
            if(operand instanceof BooleanValue) {
                this.value = ((BooleanValue) operand).value;
                this.reference = operand.clone(function).getReference();
                return this.clone(function);
            } else {
                if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Incompatible types: " + operand.getDataType() + " cannot be converted to " + this.getDataType(), pattern));
                return null;
            }
        }

        if(operand instanceof BooleanValue) switch(operator) {
            case AND:
                return new BooleanValue(this.value && ((BooleanValue) operand).value, this.semanticContext);
            case OR:
                return new BooleanValue(this.value || ((BooleanValue) operand).value, this.semanticContext);
            case EQUAL:
                return new BooleanValue(this.value == ((BooleanValue) operand).value, this.semanticContext);
        }*/

        return null;
    }

    @Override
    public String toString() {
        return "BooleanValue(" + reference + ")";
    }

    @Override
    public BooleanValue clone(Function function) {
        return new BooleanValue(reference, semanticContext);
    }
}

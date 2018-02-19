package com.energyxxer.craftrlang.compiler.semantic_analysis.values;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;

/**
 * Created by Energyxxer on 07/11/2017.
 */
public class FloatValue extends NumericalValue {

    private float value = 0;

    public FloatValue(float value, SemanticContext semanticContext) {
        super(semanticContext);
        this.value = value;
    }

    public FloatValue(LocalScore reference, SemanticContext semanticContext) {
        super(reference, semanticContext);
    }

    @Override
    public NumericalValue coerce(NumericalValue value) {
        return this;
    }

    @Override
    protected Value operation(Operator operator, TokenPattern<?> pattern, Function function, boolean fromVariable, boolean silent) {
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern, Function function, boolean fromVariable, boolean silent) {

        if(operator == Operator.ASSIGN) {
            if(operand instanceof NumericalValue && ((NumericalValue) operand).getWeight()<=this.getWeight()) {
                if(operand instanceof IntegerValue) this.value = ((IntegerValue) operand).getRawValue().floatValue();
                if(operand instanceof FloatValue) this.value = ((FloatValue) operand).value;
                this.reference = operand.clone(function).getReference();
                return this.clone(function);
            } else {
                if(!silent) semanticContext.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Incompatible types: " + operand.getDataType() + " cannot be converted to " + this.getDataType(), pattern.getFormattedPath()));
                return null;
            }
        }

        if(operand instanceof NumericalValue) {
            int weightDiff = this.getWeight() - ((NumericalValue) operand).getWeight();
            if(weightDiff > 0) return operation(operator, ((NumericalValue) operand).coerce(this), pattern, function, fromVariable, silent);
            else if(weightDiff < 0) return this.coerce((NumericalValue) operand).operation(operator, operand, pattern, function, fromVariable, silent);
            else {
                //We can be certain that if this code is running, then both operands are FloatValues

                FloatValue floatOperand = (FloatValue) operand;

                switch(operator) {
                    case ADD: return new FloatValue(this.value + floatOperand.value, semanticContext);
                    case SUBTRACT: return new FloatValue(this.value - floatOperand.value, semanticContext);
                    case MULTIPLY: return new FloatValue(this.value * floatOperand.value, semanticContext);
                    case DIVIDE: return new FloatValue(this.value / floatOperand.value, semanticContext);//Should probably add a case for division by zero
                    case MODULO: return new FloatValue(this.value % floatOperand.value, semanticContext); //Should probably add a case for division by zero
                    case EQUAL: return new BooleanValue(this.value == floatOperand.value, semanticContext);
                    case LESS_THAN: return new BooleanValue(this.value < floatOperand.value, semanticContext);
                    case LESS_THAN_OR_EQUAL: return new BooleanValue(this.value <= floatOperand.value, semanticContext);
                    case GREATER_THAN: return new BooleanValue(this.value > floatOperand.value, semanticContext);
                    case GREATER_THAN_OR_EQUAL: return new BooleanValue(this.value >= floatOperand.value, semanticContext);
                }
                return null;
            }
        } else if(operand instanceof StringValue && operator == Operator.ADD) {
            return new StringValue(String.valueOf(this.value)+((StringValue)operand).getRawValue(), this.semanticContext);
        }
        return null;
    }

    @Override
    public int getWeight() {
        return 1;
    }

    @Override
    public DataType getDataType() {
        return DataType.FLOAT;
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
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public Float getRawValue() {
        return this.value;
    }

    @Override
    public FloatValue clone(Function function) {
        if(this.isExplicit()) {
            return new FloatValue(this.value, semanticContext);
        } else {
            //TODO
            return null;
        }
    }
}

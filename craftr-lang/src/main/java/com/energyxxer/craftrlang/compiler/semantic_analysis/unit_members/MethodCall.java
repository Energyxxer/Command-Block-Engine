package com.energyxxer.craftrlang.compiler.semantic_analysis.unit_members;

import com.energyxxer.craftrlang.CraftrLang;
import com.energyxxer.craftrlang.compiler.code_generation.functions.FunctionWriter;
import com.energyxxer.craftrlang.compiler.code_generation.functions.MCFunction;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenGroup;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenItem;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenList;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.report.Notice;
import com.energyxxer.craftrlang.compiler.report.NoticeType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.TraversableStructure;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SymbolTable;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataHolder;
import com.energyxxer.craftrlang.compiler.semantic_analysis.data_types.DataType;
import com.energyxxer.craftrlang.compiler.semantic_analysis.managers.MethodLog;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ExprResolver;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.ObjectInstance;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Operator;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;
import com.energyxxer.craftrlang.compiler.semantic_analysis.variables.Variable;

import java.util.ArrayList;
import java.util.HashMap;

public class MethodCall extends Value implements FunctionWriter, TraversableStructure {

    private String methodName;
    private ArrayList<Value> positionalParams = new ArrayList<>();
    private HashMap<String, Value> keywordParams = new HashMap<>();

    private Method method = null;

    public MethodCall(TokenPattern<?> pattern, DataHolder dataHolder, MCFunction function, Context context) {
        super(context);

        this.methodName = ((TokenItem) pattern.find("METHOD_CALL_NAME")).getContents().value;

        TokenList parameterListWrapper = (TokenList) pattern.find("PARAMETER_LIST");

        if(parameterListWrapper != null) {
            TokenPattern<?>[] parameterList = parameterListWrapper.getContents();
            for(TokenPattern<?> entry : parameterList) {
                if(entry.getName().equals("PARAMETER")) {
                    TokenGroup rawParam = (TokenGroup) entry;

                    String label = null;

                    TokenPattern<?> rawLabel = rawParam.find("PARAMETER_LABEL_WRAPPER");
                    if(rawLabel != null) {
                        label = ((TokenItem) rawLabel.find("PARAMETER_LABEL")).getContents().value;
                        if(CraftrLang.isPseudoIdentifier(label)) {
                            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Illegal keyword parameter label", rawLabel.getFormattedPath()));
                        }
                    }
                    TokenPattern<?> rawValue = rawParam.find("VALUE");
                    Value value = ExprResolver.analyzeValueOrReference(rawValue, context, (!context.isStatic()) ? context.getUnit().getGenericInstance() : null, function);
                    context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "OBTAINED VALUE IS: " + value, rawValue.getFormattedPath()));

                    if(label == null) {
                        positionalParams.add(value);
                        if(value == null) {
                            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "Actual positional parameter is null: " + rawValue, rawValue.getFormattedPath()));
                            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "... context:" + context, rawValue.getFormattedPath()));
                            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "... context.isStatic():" + context.isStatic(), rawValue.getFormattedPath()));
                            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice("Something went wrong", NoticeType.WARNING, "... context.getUnit():" + context.getUnit(), rawValue.getFormattedPath()));
                        }
                    } else {
                        if(keywordParams.keySet().contains(label)) {
                            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Duplicate keyword parameter", rawLabel.getFormattedPath()));
                        }
                        keywordParams.put(label, value);
                    }
                }
            }
        }

        ArrayList<FormalParameter> formalParams = new ArrayList<>();

        for(Value value : positionalParams) {
            formalParams.add(new FormalParameter((value != null) ? value.getDataType() : DataType.OBJECT, null));
        }

        for(int i = 0; i < positionalParams.size(); i++) {
            Value param = positionalParams.get(i);
            if(param instanceof Variable) {
                positionalParams.set(i, ((Variable) param).getValue());
            }
        }

        if(dataHolder.getMethodLog() == null) {
            context.getAnalyzer().getCompiler().getReport().addNotice(new Notice(NoticeType.ERROR, "Cannot resolve method from an undefined data holder", pattern.getFormattedPath()));
        } else {
            MethodSignature signature = new MethodSignature(dataHolder.getMethodLog().getDeclaringUnit(), methodName, formalParams);

            this.method = dataHolder.getMethodLog().findMethod(signature, pattern, context, (dataHolder instanceof ObjectInstance) ? ((ObjectInstance) dataHolder) : null);
        }
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public DataType getDataType() {
        return (method != null) ? method.getReturnType() : DataType.VOID;
    }

    @Override
    public SymbolTable getSubSymbolTable() {
        return (method != null) ? method.getReturnType().getSubSymbolTable() : null;
    }

    @Override
    public MethodLog getMethodLog() {
        return (method != null) ? method.getReturnType().getMethodLog() : null;
    }

    @Override
    protected Value operation(Operator operator, TokenPattern<?> pattern) {
        return null;
    }

    @Override
    protected Value operation(Operator operator, Value operand, TokenPattern<?> pattern) {
        return null;
    }

    @Override
    public void writeToFunction(MCFunction function) {
        if(method != null && method.getCodeBlock() != null)
            method.getCodeBlock().writeToFunction(function);
        //TEMPORARY. DO MORE STUFF TO CHANGE CODE BLOCK VARIABLES AND BLAH BLAH TO OPTIMIZE
    }
}
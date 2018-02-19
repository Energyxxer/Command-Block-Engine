package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.functions.FunctionComment;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.Context;

public class ForStatement extends Statement {
    public ForStatement(TokenPattern<?> pattern, Context context, Function function) {
        super(pattern, context, function);
    }

    @Override
    public void writeCommands(Function function) {
        function.append(new FunctionComment("For statement"));
    }
}
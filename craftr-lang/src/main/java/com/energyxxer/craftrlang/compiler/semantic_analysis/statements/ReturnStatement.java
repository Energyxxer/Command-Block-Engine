package com.energyxxer.craftrlang.compiler.semantic_analysis.statements;

import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.functions.FunctionComment;
import com.energyxxer.craftrlang.compiler.parsing.pattern_matching.structures.TokenPattern;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;

public class ReturnStatement extends Statement {

    public ReturnStatement(TokenPattern<?> pattern, SemanticContext semanticContext, Function function) {
        super(pattern, semanticContext, function);
    }

    @Override
    public void writeCommands(Function function) {
        function.append(new FunctionComment("Return statement"));
    }
}

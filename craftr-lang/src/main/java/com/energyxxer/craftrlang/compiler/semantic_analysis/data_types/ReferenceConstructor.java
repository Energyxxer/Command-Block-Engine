package com.energyxxer.craftrlang.compiler.semantic_analysis.data_types;

import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;
import com.energyxxer.craftrlang.compiler.semantic_analysis.values.Value;

public interface ReferenceConstructor {
    Value create(LocalScore reference, SemanticContext semanticContext);
}

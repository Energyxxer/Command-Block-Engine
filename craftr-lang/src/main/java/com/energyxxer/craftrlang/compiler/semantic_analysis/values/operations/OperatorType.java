package com.energyxxer.craftrlang.compiler.semantic_analysis.values.operations;

public enum OperatorType {
    UNARY_LEFT, // *O
    UNARY_RIGHT, // O*
    UNARY_ANY, // *O or O*
    BINARY, // A * B
    TERNARY_LEFT, //TODO: A * B + C
    TERNARY_RIGHT //TODO: A + B * C
}

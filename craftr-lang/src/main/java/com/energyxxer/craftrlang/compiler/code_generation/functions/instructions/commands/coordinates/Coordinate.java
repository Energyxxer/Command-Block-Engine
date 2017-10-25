package com.energyxxer.craftrlang.compiler.code_generation.functions.instructions.commands.coordinates;

public class Coordinate {
    public enum Type {
        ABSOLUTE(""), RELATIVE("~"), LOCAL("^");

        public final String prefix;

        Type(String prefix) {
            this.prefix = prefix;
        }
    }

    private Type type;
    private double coord;

    public Coordinate(double coord) {
        this(Type.ABSOLUTE, coord);
    }

    public Coordinate(Type type, double coord) {
        this.type = type;
        this.coord = coord;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public double getCoord() {
        return coord;
    }

    public void setCoord(double coord) {
        this.coord = coord;
    }

    @Override
    public String toString() {
        return type.prefix + coord;
    }
}
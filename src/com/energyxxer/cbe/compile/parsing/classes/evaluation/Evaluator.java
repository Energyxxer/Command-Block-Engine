package com.energyxxer.cbe.compile.parsing.classes.evaluation;

import com.energyxxer.cbe.compile.analysis.token.structures.TokenPattern;
import com.energyxxer.cbe.compile.parsing.classes.values.CBEValue;
import com.energyxxer.cbe.global.Console;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 1/30/2017.
 */
public class Evaluator {

    private static HashMap<String, ArrayList<PatternParser>> evaluators = new HashMap<>();

    public static void addEvaluator(String name, PatternParser p) {
        if(!evaluators.containsKey(name)) {
            evaluators.put(name,new ArrayList<>());
        }
        evaluators.get(name).add(p);
    }

    public static void eval(TokenPattern<?> p) {
        for(String key : evaluators.keySet()) {
            if(p.name.equals(key)) {
                ArrayList<PatternParser> entries = evaluators.get(key);
                for(PatternParser entry : entries) {
                    CBEValue val = entry.eval(p);
                    if(val != null) {
                        Console.debug.println(val);
                        break;
                    }
                }
                Console.debug.println();
            }
        }
    }
}
package com.energyxxer.craftrlang.compiler.semantic_analysis.references;

import com.energyxxer.commodore.commands.execute.ExecuteCommand;
import com.energyxxer.commodore.commands.execute.ExecuteStoreScore;
import com.energyxxer.commodore.commands.gamerule.GameruleQueryCommand;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.score.LocalScore;

public class GameruleReference implements DataReference {

    private final String gamerule;

    public GameruleReference(String gamerule) {
        this.gamerule = gamerule;
    }

    @Override
    public ScoreReference toScore(Function function, LocalScore score) {
        ExecuteCommand exec = new ExecuteCommand(new GameruleQueryCommand(gamerule));
        exec.addModifier(new ExecuteStoreScore(score));

        function.append(exec);

        return new ScoreReference(score);
    }
}

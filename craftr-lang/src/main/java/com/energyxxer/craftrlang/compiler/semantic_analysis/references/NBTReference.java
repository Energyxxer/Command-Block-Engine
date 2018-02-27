package com.energyxxer.craftrlang.compiler.semantic_analysis.references;

import com.energyxxer.commodore.commands.data.DataGetCommand;
import com.energyxxer.commodore.commands.execute.ExecuteCommand;
import com.energyxxer.commodore.commands.execute.ExecuteStoreEntity;
import com.energyxxer.commodore.commands.execute.ExecuteStoreScore;
import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.nbt.NumericNBTType;
import com.energyxxer.commodore.score.LocalScore;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;

public class NBTReference implements DataReference {
    private Entity entity;
    private NBTPath path;

    public NBTReference(Entity entity, NBTPath path) {
        this.entity = entity;
        this.path = path;
    }

    @Override
    public ScoreReference toScore(Function function, LocalScore score, SemanticContext semanticContext) {
        ExecuteCommand exec = new ExecuteCommand(new DataGetCommand(this.entity.limitToOne(), this.path));
        exec.addModifier(new ExecuteStoreScore(score));

        function.append(exec);

        return new ScoreReference(score);
    }

    @Override
    public NBTReference toNBT(Function function, Entity entity, NBTPath path, SemanticContext semanticContext) {
        if(!this.entity.equals(entity) || !this.path.equals(path)) {
            ExecuteCommand exec = new ExecuteCommand(new DataGetCommand(this.entity.limitToOne(), this.path));
            exec.addModifier(new ExecuteStoreEntity(entity, path, NumericNBTType.DOUBLE)); //TODO: Dynamically choose data type

            function.append(exec);

            return new NBTReference(entity, path);
        } else return this;
    }

    @Override
    public String toString() {
        return "nbt: " + entity + ":" + path;
    }
}
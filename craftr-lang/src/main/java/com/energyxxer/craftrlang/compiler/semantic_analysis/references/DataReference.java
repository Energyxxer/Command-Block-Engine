package com.energyxxer.craftrlang.compiler.semantic_analysis.references;

import com.energyxxer.commodore.entity.Entity;
import com.energyxxer.commodore.functions.Function;
import com.energyxxer.commodore.nbt.NBTPath;
import com.energyxxer.commodore.score.LocalScore;

public interface DataReference {
    ScoreReference toScore(Function function, LocalScore score);
    NBTReference toNBT(Function function, Entity entity, NBTPath path);
}

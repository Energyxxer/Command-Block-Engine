package com.energyxxer.craftrlang.compiler.codegen.objectives;

import com.energyxxer.craftrlang.compiler.CraftrCommandModule;
import com.energyxxer.craftrlang.compiler.semantic_analysis.context.SemanticContext;

public class LocalizedObjectiveManager {
    private final CraftrCommandModule module;
    private final SemanticContext semanticContext;

    public final LocalizedObjectiveGroup GENERIC;
    public final LocalizedObjectiveGroup PARAMETER;
    public final LocalizedObjectiveGroup OPERATION;
    public final LocalizedObjectiveGroup RETURN;

    public LocalizedObjectiveManager(CraftrCommandModule module, SemanticContext semanticContext) {
        this.module = module;
        this.semanticContext = semanticContext;

        GENERIC = new LocalizedObjectiveGroup(this, "g");
        PARAMETER = new LocalizedObjectiveGroup(this, "p");
        OPERATION = new LocalizedObjectiveGroup(this, "op");
        RETURN = new LocalizedObjectiveGroup(this, "r");
    }

    public SemanticContext getSemanticContext() {
        return semanticContext;
    }

    public CraftrCommandModule getModule() {
        return module;
    }

    //
    // TODO: Hey, you know what you're doing with these stupid objective builders?
    // TODO: That's really dumb. Make a ScoreHolderManager with each of these objective groups in them to make objective use
    // TODO: on a player-by-player basis instead of a global thing.
    // TODO: We don't want 500 objectives when each entity could be reusing the same 5
    //
    // TODO: ^^^^^^^^^
    // TODO: That above was in codegen_old terms. In new Commodore library terms:
    // TODO: It would mean to make scoreboard objective resolutions on a macro-score-holder-by-macro-score-holder basis
    // TODO: But that doesn't work because entities have multiple macro score holders
    // TODO: We can't do it on an entity-by-entity basis (btw we have to make our official CraftrEntity extension of Commodore's Entity)
    // TODO: Instead, do it on a semanticContext-by-semanticContext basis.
    // TODO: Each semanticContext will have its own objective group. Linked to this class, this will keep track of objectives created and used.
    // TODO: When a semanticContext's objective is created, that objective will be "captured", and further requests for objectives will
    // TODO: fallback to another objective name. Once a semanticContext is done with an objective, it will be "released", for
    // TODO: its name to be used again in that semanticContext. Note that an objective name being captured in one semanticContext doesn't
    // TODO: mean it will be captured on another.

    // class Objective:
    //     <Commodore's implementation>
    // class LocalizedObjectiveManager:
    //     + CraftrObjectiveManager parent: Craftr objective manager it belongs to, used to call Commodore's objective
    //           manager to create and get scoreboard objectives
    //     + SemanticContext semanticContext: SemanticContext it belongs to
    //     <Multiple localized objective groups for public access. e.g: GENERIC, OPERATION, RETURN...>
    //     >> Owned by a semantic context, contains several common localized objective groups
    // class LocalizedObjectiveGroup:
    //     + LocalizedObjectiveManager parent: Localized objective manager it belongs to
    //     + String name: Objective name
    //     + SemanticContext semanticContext: SemanticContext it belongs to
    //     + List<LocalizedObjective> localizedObjectives: List of all captured objectives
    //     >> Creates localized objectives on demand for the semanticContext. Each semanticContext must have one, though it can use one
    //        from a parent semanticContext (such as a parent code block)
    // class LocalizedObjective:
    //     + LocalizedObjectiveGroup: Group it belongs to
    //     + Objective objective: Commodore objective it references
    // class CraftrObjectiveManager:
    //     + CraftrCommandModule: Craftr-specialized command module it belongs to
    //     >> May decide later how to establish default objective groups
    //
    // Also good morning :)
    //
}
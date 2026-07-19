package net.kenji.epic_colonies.gameasset;

import com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState;
import com.minecolonies.api.entity.ai.statemachine.states.CitizenAIState;
import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.mojang.datafixers.util.Pair;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;

public enum EpicColoniesLivingMotions implements LivingMotion {
    JOG(null, null, false),
    USE(null, null, true),
    DIG(AIWorkerState.MINE_BLOCK, LivingMotions.DIGGING,true),
    SIT_SLEEP(AIWorkerState.GUARD_SLEEP, null, false);


    private final IState citizenState;
    private final LivingMotion parentMotion;
    private final boolean isComposite;
    public final int id;

    EpicColoniesLivingMotions(IState citizenState, LivingMotion parentMotion, boolean isComposite){
        this.citizenState = citizenState;
        this.parentMotion = parentMotion;
        this.id = LivingMotion.ENUM_MANAGER.assign(this);
        this.isComposite = isComposite;
    }

    public static Pair<LivingMotion, Boolean> getLivingMotionFromAiState(IState iState){
        for(EpicColoniesLivingMotions livingMotion : EpicColoniesLivingMotions.values()){
            if(livingMotion.citizenState == null) continue;

            if(livingMotion.citizenState == iState){
                if(livingMotion.parentMotion != null)
                    return new Pair<>(livingMotion.parentMotion, livingMotion.isComposite);
                return new Pair<>(livingMotion, livingMotion.isComposite);
            }
        }
        return null;
    }

    @Override
    public int universalOrdinal() {
        return id;
    }
}

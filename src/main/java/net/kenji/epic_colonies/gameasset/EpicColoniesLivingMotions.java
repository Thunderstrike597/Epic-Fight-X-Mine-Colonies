package net.kenji.epic_colonies.gameasset;

import com.minecolonies.api.entity.ai.statemachine.states.CitizenAIState;
import com.minecolonies.api.entity.ai.statemachine.states.IState;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;

public enum EpicColoniesLivingMotions implements LivingMotion {
    EMPTY(null, null),
    JOG(null, null),
    SLEEPING(CitizenAIState.SLEEP, LivingMotions.SLEEP),
    EATING(CitizenAIState.EATING, LivingMotions.EAT);


    private final IState citizenState;
    private final LivingMotion parentMotion;
    public final int id;

    EpicColoniesLivingMotions(IState citizenState, LivingMotion parentMotion){
        this.citizenState = citizenState;
        this.parentMotion = parentMotion;
        this.id = LivingMotion.ENUM_MANAGER.assign(this);
    }

    public static LivingMotion getLivingMotionFromAiState(IState iState){
        for(EpicColoniesLivingMotions livingMotion : EpicColoniesLivingMotions.values()){
            if(livingMotion.parentMotion == null || livingMotion.citizenState == null) continue;

            if(livingMotion.citizenState == iState){
                return livingMotion.parentMotion;
            }
        }
        return null;
    }

    @Override
    public int universalOrdinal() {
        return id;
    }
}

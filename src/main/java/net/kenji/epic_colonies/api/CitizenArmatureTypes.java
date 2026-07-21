package net.kenji.epic_colonies.api;

import net.kenji.epic_colonies.gameasset.EpicColoniesArmatures;
import yesman.epicfight.api.model.Armature;
import yesman.epicfight.model.armature.HumanoidArmature;

public enum CitizenArmatureTypes {
    REGULAR(EpicColoniesArmatures.CITIZEN_REGULAR.get()),
    LOW_EYES(EpicColoniesArmatures.CITIZEN_LOW_EYES.get());

    final HumanoidArmature armature;

    CitizenArmatureTypes(HumanoidArmature armature){
        this.armature = armature;
    }

    public HumanoidArmature getArmature() {
        return armature;
    }
}

package net.kenji.epic_colonies.gameasset;

import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.gameasset.armatures.CitizenArmature;
import yesman.epicfight.gameasset.Armatures;

public class EpicColoniesArmatures {
    public static final Armatures.ArmatureAccessor<CitizenArmature> CITIZEN_REGULAR = Armatures.ArmatureAccessor.<CitizenArmature>create(EpicColonies.MODID, "entity/citizen/citizen_male", CitizenArmature::new);
    public static final Armatures.ArmatureAccessor<CitizenArmature> CITIZEN_LOW_EYES = Armatures.ArmatureAccessor.<CitizenArmature>create(EpicColonies.MODID, "entity/citizen/citizen_female", CitizenArmature::new);


}

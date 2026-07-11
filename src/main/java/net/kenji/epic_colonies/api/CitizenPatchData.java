package net.kenji.epic_colonies.api;

import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;

import javax.annotation.Nullable;
import java.util.UUID;

public class CitizenPatchData{
        public LivingMotion currentOptionalMotion = LivingMotions.NONE;
        public LivingMotion currentOptionalCompositeMotion = LivingMotions.NONE;
        public LivingMotion prevOptionalCompositeMotion = null;
        public LivingMotion prevOptionalMotion = null;

        public boolean isAsleep = false;
        public CitizenPatchData(UUID uuid, @Nullable LivingMotion optionalMotion,
                                @Nullable LivingMotion optionalCompositeMotion,
                                @Nullable LivingMotion prevOptionalCompositeMotion,
                                @Nullable LivingMotion prevOptionalMotion,

                                boolean isAsleep){


            this.currentOptionalMotion = optionalMotion;

            this.currentOptionalCompositeMotion = optionalCompositeMotion;


            this.prevOptionalCompositeMotion = prevOptionalCompositeMotion;
            this.prevOptionalMotion = prevOptionalMotion;

            this.isAsleep = isAsleep;
        }
        public CitizenPatchData(){

        }
    }

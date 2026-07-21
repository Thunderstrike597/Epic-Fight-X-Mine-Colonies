package net.kenji.epic_colonies.gameasset.patch;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.ICitizenDataView;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.jobs.IJob;
import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.entity.ai.minimal.EntityAICitizenAvoidEntity;
import com.minecolonies.core.entity.ai.minimal.EntityAIEatTask;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import com.minecolonies.core.entity.other.SittingEntity;
import com.mojang.datafixers.util.Pair;
import net.kenji.epic_colonies.api.CitizenArmatureTypes;
import net.kenji.epic_colonies.api.FacialEmotionExpressions;
import net.kenji.epic_colonies.api.data.CitizenMeshCache;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMesh;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.gameasset.EpicColoniesAnimations;
import net.kenji.epic_colonies.gameasset.EpicColoniesArmatures;
import net.kenji.epic_colonies.gameasset.EpicColoniesLivingMotions;
import net.kenji.epic_colonies.gameasset.patch.base.AbstractExpressiveHumanoidPatch;
import net.kenji.epic_colonies.mixins.LivingEntityAccessor;
import net.kenji.epic_colonies.network.ClientCitizenSyncPacket;
import net.kenji.epic_colonies.network.EpicColoniesPacketHandler;
import net.kenji.epic_colonies.network.ServerCitizenArmaturePacket;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.*;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.Factions;
import yesman.epicfight.world.capabilities.item.CapabilityItem;

public class CitizenEntityPatch<C extends AbstractEntityCitizen> extends AbstractExpressiveHumanoidPatch<C> {

    public CitizenEntityPatch() {
        super(Factions.VILLAGER);
    }

    private HumanoidArmature currentCitizenArmature = EpicColoniesArmatures.CITIZEN_REGULAR.get();

    public static AssetAccessor<EpicColoniesMesh> getMeshFromTexture(AbstractEntityCitizen citizen, boolean isChild){

        if(isChild) {
            if (EpicColoniesMeshes.bigEyeTextures.contains(citizen.getTexture())) {
                if (citizen.isFemale())
                    return EpicColoniesMeshes.CHILD_FEMALE_BIG_EYES;
            }
            if (EpicColoniesMeshes.lowerEyeTextures.contains(citizen.getTexture())) {
                if (citizen.isFemale())
                    return EpicColoniesMeshes.CHILD_FEMALE_LOWER_EYES;
            }
        }
        return null;
    }


    public HumanoidArmature getCurrentCitizenArmature() {
        return currentCitizenArmature;
    }

    public void setCurrentCitizenArmatureFromArmatureType(CitizenArmatureTypes currentCitizenArmature) {
        if(this.getOriginal().level().isClientSide()){
            EpicColoniesPacketHandler.sendToServer(new ServerCitizenArmaturePacket(this.getOriginal().getUUID(), CitizenArmatureTypes.REGULAR));
        }
        this.currentCitizenArmature = currentCitizenArmature.getArmature();
    }
    @Override
    public void poseTick(DynamicAnimation animation,
                         yesman.epicfight.api.animation.Pose pose,
                         float elapsedTime, float partialTick) {
        super.poseTick(animation, pose, elapsedTime, partialTick);

        if (!pose.hasTransform("Eye_L") && !pose.hasTransform("Eye_R")) {
            return;
        }

        AbstractEntityCitizen citizen = this.getOriginal();
        float sideOffset = 0.0F;
        Player nearest = getNearestPlayer(citizen);

        if (nearest != null) {
            double d0 = nearest.getX() - citizen.getX();
            double d2 = nearest.getZ() - citizen.getZ();
            float wantedYRot = (float) (Mth.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;

            float headYaw = Mth.rotLerp(partialTick, citizen.yHeadRotO, citizen.yHeadRot);
            float relativeYaw = Mth.wrapDegrees(wantedYRot - headYaw);
            float absRelativeYaw = Math.abs(relativeYaw);

            // field-of-view gate: fades to 0 as the player approaches MAX_TRACKING_ANGLE_DEG,
            // independent of how the eyes are clamped/normalized below
            float angleFade = Mth.clamp((MAX_TRACKING_ANGLE_DEG - absRelativeYaw) / ANGLE_FADE_DEG, 0.0F, 1.0F);

            float clamped = Mth.clamp(relativeYaw, -EYE_ANGLE_SENSITIVITY_DEG, EYE_ANGLE_SENSITIVITY_DEG);
            float t = clamped / EYE_ANGLE_SENSITIVITY_DEG;

            double dist = Math.sqrt(d0 * d0 + d2 * d2);
            float distFade = Mth.clamp((float) ((LOOK_RANGE - dist) / FADE_DISTANCE), 0.0F, 1.0F);

            sideOffset = t * MAX_EYE_OFFSET * distFade * angleFade;
        }

        Vec3f translation = new Vec3f(sideOffset, 0.0F, 0.0F);

        if (pose.hasTransform("Eye_R")) {
            pose.orElseEmpty("Eye_R").frontResult(JointTransform.translation(translation), OpenMatrix4f::mul);
        }
        if (pose.hasTransform("Eye_L")) {
            pose.orElseEmpty("Eye_L").frontResult(JointTransform.translation(translation), OpenMatrix4f::mul);
        }
    }

    public void manageHeadRotWithEyes(){
        AbstractEntityCitizen citizen = this.getOriginal();
        Player nearest = citizen.level().getNearestPlayer(citizen, LOOK_RANGE);

        if (nearest != null) {
            double d0 = nearest.getX() - citizen.getX();
            double d2 = nearest.getZ() - citizen.getZ();
            float wantedYRot = (float) (Mth.atan2(d2, d0) * (180D / Math.PI)) - 90.0F;

            float relativeToHead = Mth.wrapDegrees(wantedYRot - citizen.yHeadRot);

            if (Math.abs(relativeToHead) > EYE_ANGLE_SENSITIVITY_DEG / 1.15F) {
                // eyes alone can't cover this angle - close the remaining gap with the head, gradually
                float overshoot = relativeToHead - Math.signum(relativeToHead) * EYE_ANGLE_SENSITIVITY_DEG;
                float step = Mth.clamp(overshoot, - HEAD_TURN_SPEED_DEG, HEAD_TURN_SPEED_DEG);
                citizen.yHeadRot = Mth.wrapDegrees(citizen.yHeadRot + step);
            }
        }
    }

    @Override
    public void tick(LivingEvent.LivingTickEvent event) {
        super.tick(event);

        if(getNearestPlayer(this.getOriginal()) != null){
            getAnimator().playAnimation(eyeMoveAnim, 0F);
        }

        boolean clientSide = this.getOriginal().level().isClientSide();
        ICitizenDataView iCitizenDataView = clientSide ? this.getOriginal().getCitizenDataView() : null;
        ICitizenData iCitizenData = !clientSide ? this.getOriginal().getCitizenData() : null;

        if (iCitizenDataView != null) {
            if(iCitizenDataView.isChild()){
                getAnimator().playAnimation(facialAnim, 0F);
            }
        }
        if (iCitizenData != null) {
            if(iCitizenData.isChild()){
                getAnimator().playAnimation(facialAnim, 0F);
            }
        }
    }

    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        DEFAULT_BROW_ANIM = EpicColoniesAnimations.CITIZEN_BLINK;
        DEFAULT_EYES_ANIM = EpicColoniesAnimations.CITIZEN_EYES_MOVE;

        facialAnim = EpicColoniesAnimations.CITIZEN_BLINK;
        eyeMoveAnim = EpicColoniesAnimations.CITIZEN_EYES_MOVE;

        animator.playAnimation(facialAnim, 0F);
        animator.playAnimation(eyeMoveAnim, 0F);


        AbstractEntityCitizen citizen = this.getOriginal();


        if (this.getOriginal().level().isClientSide() && citizen.getCitizenDataView() == null) {
                CitizenMeshCache.Entry cached = CitizenMeshCache.get(citizen.getUUID());
                if (cached != null) {
                    this.getOriginal().setIsChild(cached.isChild());
                }
        }
        if (!this.getOriginal().level().isClientSide() && citizen.getCitizenData() == null) {
            CitizenMeshCache.Entry cached = CitizenMeshCache.get(citizen.getUUID());
            if (cached != null) {
                this.getOriginal().setIsChild(cached.isChild());
            }
        }
    }


    @Override
    public HumanoidArmature getArmature() {
        ICitizenData data = this.getOriginal().getCitizenData();
        ICitizenDataView dataView = this.getOriginal().level().isClientSide()
                ? this.getOriginal().getCitizenDataView()
                : null;

        HumanoidArmature childArmature = !this.getOriginal().isFemale() ? EpicColoniesArmatures.CHILD_MALE.get() : EpicColoniesArmatures.CHILD_FEMALE.get();


        if(data != null) {
            AssetAccessor<EpicColoniesMesh> finalChildMesh = getMeshFromTexture(this.getOriginal(), data.isChild());
            if(finalChildMesh != null){
                if(finalChildMesh == EpicColoniesMeshes.CHILD_FEMALE_BIG_EYES){
                    childArmature = EpicColoniesArmatures.CHILD_FEMALE_BIG_EYES.get();
                }
                else if(finalChildMesh == EpicColoniesMeshes.CHILD_FEMALE_LOWER_EYES){
                    childArmature = EpicColoniesArmatures.CHILD_FEMALE_LOWER_EYES.get();
                }
            }

            return !data.isChild() ? getCurrentCitizenArmature() : childArmature;
        }
        if(dataView != null){
            return !dataView.isChild() ? getCurrentCitizenArmature() : childArmature;
        }
        return getCurrentCitizenArmature();
    }
    public boolean isCitizenAsleep(){
        return this.getOriginal().isSleeping() || this.original.getCitizenSleepHandler().isAsleep() || citizenPatchData.isAsleep || this.citizenPatchData.currentOptionalMotion == EpicColoniesLivingMotions.SIT_SLEEP;
    }

    public void tickFacialAnim(){
        if(isCitizenAsleep())
            this.facialAnim = EpicColoniesAnimations.CITIZEN_EYES_CLOSED;
        else {
            AnimationManager.AnimationAccessor<? extends StaticAnimation> newFacialAnim = getFacialBrowAnimation();

            if(facialAnim != newFacialAnim){
                this.facialAnim = newFacialAnim;
            }
        }
    }

    private void onCitizenTick(){
        tickFacialAnim();
        setSleepDir();
        handleHeldItem();

        if(!this.isLogicalClient()) {
            tickCurrentOptionalMotion();
            if (citizenPatchData.currentOptionalCompositeMotion != null) {
                citizenPatchData.prevOptionalCompositeMotion = citizenPatchData.currentOptionalCompositeMotion;
            }
            if (citizenPatchData.currentOptionalMotion != null) {
                citizenPatchData.prevOptionalMotion = citizenPatchData.currentOptionalMotion;
            }
        }
    }

    public AnimationManager.AnimationAccessor<? extends StaticAnimation> getFacialBrowAnimation() {
        AbstractEntityCitizen citizen = this.getOriginal();
        ICitizenDataView citizenDataView = citizen.level().isClientSide() ? citizen.getCitizenDataView() : null;
        ICitizenData citizenData = !citizen.level().isClientSide() ? citizen.getCitizenData() : null;
        double happiness = 5.0;
        if(citizenDataView != null && citizen.level().isClientSide()){
            if(citizenDataView.isChild()) return DEFAULT_BROW_ANIM;
           happiness = citizenDataView.getHappiness();
        }
        if(citizen.getCitizenColonyHandler() == null || citizen.getCitizenColonyHandler().getColony() == null) return facialAnim;
        IColony colony = citizen.getCitizenColonyHandler().getColony();
        if(citizenData != null){
            if(citizenData.isChild()) return DEFAULT_BROW_ANIM;
            happiness = citizenData.getCitizenHappinessHandler().getHappiness(colony, citizenData);
        }


        return FacialEmotionExpressions.getAnimationFromHappiness(happiness);
    }

    public void handleHeldItem(){
        ItemStack mainStack = this.getOriginal().getMainHandItem();
        CapabilityItem fromCap = EpicFightCapabilities.getItemStackCapability(mainStack);
        if(this.getOriginal().equipmentHasChanged(mainStack, lastHeldItem)){
            CapabilityItem toCap = EpicFightCapabilities.getItemStackCapability(lastHeldItem);
            updateHeldItem(fromCap, toCap, lastHeldItem, mainStack, InteractionHand.MAIN_HAND);
            lastHeldItem = mainStack;
        }

    }


    @Override
    public void serverTick(LivingEvent.LivingTickEvent event) {
        super.serverTick(event); // already dispatches to clientTick()/serverTick() internally, including onCitizenTick() on the client
        onCitizenTick(); // only need to run it here for the server, since clientTick() already covers the client path
        manageHeadRotWithEyes();
    }

    protected void setSleepDir(){
        Direction bedDir = this.getOriginal().getBedOrientation();
        if (citizenPatchData.currentOptionalMotion == LivingMotions.SLEEP && bedDir != null) {
            float bedRot = Mth.wrapDegrees(bedDir.toYRot() + 180.0F);
            AbstractEntityCitizen c = this.getOriginal();
            c.setYRot(bedRot);
            c.yBodyRot = bedRot;
            c.yBodyRotO = bedRot;
            c.yHeadRot = bedRot;
            c.yHeadRotO = bedRot;
        }
    }



    public void tickCurrentOptionalMotion() {
        AbstractEntityCitizen citizen = this.getOriginal();
        IState state = citizen instanceof EntityCitizen entityCitizen ? entityCitizen.getCitizenAI().getState() : citizen.getEntityStateController().getState();
        IJob iJob = citizen.getCitizenJobHandler().getColonyJob();

        IState workerState = iJob != null ? iJob.getWorkerAI().getState() : null;
        AnimationPlayer animPlayer = animator.getPlayerFor(null);
        LivingMotion motion = null;
        LivingMotion compositeMotion = null;

        if (this.getOriginal().onClimbable() && !actuallyOnGround())
            motion = LivingMotions.CLIMB;
        else if (((LivingEntityAccessor)citizen).isJumping()) {
            if(!didJump) {
                motion = LivingMotions.JUMP;
                didJump = true;
            }
        }
        else if (citizen.getVehicle() instanceof SittingEntity) {
            motion = LivingMotions.SIT;

        }
        else if (citizen.getPose() == Pose.SLEEPING) {
            motion = LivingMotions.SLEEP;
        }

        if(isMoving()) {
            if (citizen.isCrouching()) {
                motion = LivingMotions.SNEAK;
            }
            if(animPlayer != null)
                animPlayer.setReversed(getCurrentForwardSpeed() < (double)0.0F);
        }
        else {
            if(animPlayer != null)
                animPlayer.setReversed(false);

            if (this.getOriginal().isCrouching()) {
                motion = LivingMotions.KNEEL;
            }
        }

        if (state == EntityAIEatTask.EatingState.EAT) {
            compositeMotion = LivingMotions.EAT;
        }
        if((citizen.isUsingItem()) && !(citizen.getMainHandItem().getItem() instanceof ProjectileWeaponItem)){
            compositeMotion = EpicColoniesLivingMotions.USE;
        }

        if(workerState != null) {
            Pair<LivingMotion, Boolean> statePair = EpicColoniesLivingMotions.getLivingMotionFromAiState(workerState);
            if(statePair != null){
                if(statePair.getSecond())
                    compositeMotion = statePair.getFirst();
                else motion = statePair.getFirst();
            }
        }


        citizenPatchData.isAsleep = citizen.getCitizenSleepHandler().isAsleep();

        citizenPatchData.currentOptionalMotion = motion;
        citizenPatchData.currentOptionalCompositeMotion = compositeMotion;

        EpicColoniesPacketHandler.sendToAll(new ClientCitizenSyncPacket(citizen.getId(), this.getOriginal().getUUID(), citizenPatchData));
        if(didJump && citizen.onGround()){
            didJump = false;
        }
    }
    protected boolean isMoving() {
        return Math.abs(this.getOriginal().xxa) > (double)0.01F || Math.abs(this.getOriginal().zza) > (double)0.01F;
    }

    public void playCompositeOnLayer(AnimationManager.AnimationAccessor<? extends StaticAnimation> anim, Layer.Priority layerPriority){
        AnimationPlayer animPlayer = this.getClientAnimator().getCompositeLayer(layerPriority).animationPlayer;

        if(animPlayer.getAnimation() == null || animPlayer.getAnimation().get() != anim.get()){
            animator.playAnimationInstantly(anim);
        }
    }
    public void stopCompositeOnLayer(AnimationManager.AnimationAccessor<? extends StaticAnimation> anim, Layer.Priority layerPriority){
        AnimationPlayer animPlayer = this.getClientAnimator().getCompositeLayer(layerPriority).animationPlayer;

        if(animPlayer.getAnimation() != null || animPlayer.getAnimation().get() == anim.get()){
            animator.stopPlaying(anim);
        }
    }


    public void playCompositeOptionalAnimation(){
        if (citizenPatchData.currentOptionalCompositeMotion != null) {
            AssetAccessor<? extends StaticAnimation> anim =
                    this.getClientAnimator().getLivingAnimation(citizenPatchData.currentOptionalCompositeMotion, null);
            if(anim == null){
                anim = this.getClientAnimator().getCompositeLivingMotion(citizenPatchData.currentOptionalCompositeMotion);
            }

            if (anim != null) {
                Layer compositeLayer = this.getClientAnimator().getCompositeLayer(anim.get().getPriority());
                AnimationPlayer animPlayer = compositeLayer.animationPlayer;
                StaticAnimation playingAnim = animPlayer.getAnimation().get().getRealAnimation().get();
                StaticAnimation checkingAnim = anim.get().getRealAnimation().get();


                if(animPlayer.getAnimation() == null || playingAnim != checkingAnim) {
                    getAnimator().playAnimation(anim, 0.18F);

                    compositeAnimPlayCounter = MAX_COMPOSITE_PLAY_COUNTER;
                }
            }
        } else {
            AssetAccessor<? extends StaticAnimation> anim =
                    this.getClientAnimator().getCompositeLivingMotion(citizenPatchData.prevOptionalCompositeMotion);
            if (anim != null) {
                AnimationPlayer animPlayer = this.getClientAnimator().getCompositeLayer(anim.get().getPriority()).animationPlayer;
                if (animPlayer.getAnimation() != null && animPlayer.getAnimation().get() == anim.get()) {
                    getAnimator().stopPlaying(anim);
                }
            }
        }
        if(compositeAnimPlayCounter > 0){
            compositeAnimPlayCounter--;
        }
    }

    @Override
    public boolean shouldRun() {
        return this.getTarget() != null || this.getOriginal().getEntityStateController().getState() == EntityAICitizenAvoidEntity.FleeStates.RUNNING;
    }

    @Override
    protected void clientTick(LivingEvent.LivingTickEvent event) {
        super.clientTick(event);
        onCitizenTick();

        playCompositeOnLayer(facialAnim, Layer.Priority.HIGHEST);
        playCompositeOnLayer(eyeMoveAnim, Layer.Priority.LOWEST);

        playCompositeOptionalAnimation();
        tryStopAnim(LivingMotions.CLIMB);
        tryStopAnim(LivingMotions.DIGGING);
        tryStopAnim(LivingMotions.SIT);
    }

    private void tryStopAnim(LivingMotion motion) {

        AssetAccessor<? extends StaticAnimation> anim = animator.getLivingAnimation(motion, null);

        if (anim == null) {
            anim = this.getClientAnimator().getCompositeLivingMotion(motion);
        }

        if (anim != null) {

            AnimationPlayer animPlayer = getClientAnimator().getPlayerFor(anim);
            if (animPlayer != null) {


                AssetAccessor<? extends DynamicAnimation> dynamicAnim = animPlayer.getAnimation();

                if (dynamicAnim != null) {
                    if (dynamicAnim.get() == anim.get()) {
                        if (citizenPatchData.currentOptionalMotion != motion)
                            stopCompositeOnLayer(anim.get().getAccessor(), anim.get().getPriority());
                    }
                }
            }
        }
    }
}
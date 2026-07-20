package net.kenji.epic_colonies.gameasset.patch;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.ICitizenDataView;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.jobs.IJob;
import com.minecolonies.api.colony.jobs.IJobView;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.entity.ai.minimal.EntityAICitizenAvoidEntity;
import com.minecolonies.core.entity.ai.minimal.EntityAIEatTask;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import com.minecolonies.core.entity.other.SittingEntity;
import com.mojang.datafixers.util.Pair;
import net.kenji.epic_colonies.api.CitizenPatchData;
import net.kenji.epic_colonies.api.FacialEmotionExpressions;
import net.kenji.epic_colonies.api.data.CitizenMeshCache;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMesh;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.compat.CombatBehaviourBase;
import net.kenji.epic_colonies.gameasset.EpicColoniesAnimations;
import net.kenji.epic_colonies.gameasset.EpicColoniesArmatures;
import net.kenji.epic_colonies.gameasset.EpicColoniesLivingMotions;
import net.kenji.epic_colonies.mixins.LivingEntityAccessor;
import net.kenji.epic_colonies.network.ChangeLivingMotion;
import net.kenji.epic_colonies.network.ClientCitizenSyncPacket;
import net.kenji.epic_colonies.network.EpicColoniesPacketHandler;
import net.kenji.epic_colonies.network.ServerBowActionPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.*;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.api.utils.math.OpenMatrix4f;
import yesman.epicfight.api.utils.math.Vec3f;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.Factions;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

import java.util.*;

public class CitizenEntityPatch<E extends AbstractEntityCitizen> extends HumanoidMobPatch<AbstractEntityCitizen> {
    public static Map<UUID, CitizenPatchData> citizenPatchDataMap = new HashMap<>();
    protected CitizenPatchData citizenPatchData = new CitizenPatchData();

    public static int MAX_COMPOSITE_PLAY_COUNTER = 20;
    public int compositeAnimPlayCounter = MAX_COMPOSITE_PLAY_COUNTER;
    public static AnimationManager.AnimationAccessor<? extends StaticAnimation> DEFAULT_BROW_ANIM = null;
    public static AnimationManager.AnimationAccessor<? extends StaticAnimation> DEFAULT_EYES_ANIM = null;

    public AnimationManager.AnimationAccessor<? extends StaticAnimation> facialAnim;
    public AnimationManager.AnimationAccessor<? extends StaticAnimation> eyeMoveAnim;

    private int lastYTickCount = 0;
    public double lastY = -1;
    private ItemStack lastHeldItem = ItemStack.EMPTY;
    public boolean wasUsingBow = false;
    public int bowUseCounter = 0;
    public CitizenEntityPatch() {
        super(Factions.VILLAGER);
    }
    public boolean didJump = false;


    private static float LOOK_RANGE = 8.0F;
    private static float FADE_DISTANCE = 2.0F;
    private static float EYE_ANGLE_SENSITIVITY_DEG = 88F;        // how far off-center = full eye deflection (unchanged)
    private static float MAX_TRACKING_ANGLE_DEG = 60;  // beyond this angle, citizen can't plausibly "see" the player - eyes go centered
    private static float ANGLE_FADE_DEG = 15F;
    private static float MAX_EYE_OFFSET = 0.075F;
    private static float HEAD_TURN_SPEED_DEG = 8.0F;
    @Override
    public boolean overrideRender() {
        return true;
    }


    public void setWasUsingBow(boolean value){
        if(this.getCurrentLivingMotion() == EpicColoniesLivingMotions.JOG) return;
        this.wasUsingBow = value;

        if(this.getOriginal().level().isClientSide()) {
            EpicColoniesPacketHandler.sendToServer(new ServerBowActionPacket(this.getOriginal().getUUID(), this.wasUsingBow));
        }
    }

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


    @Override
    public void setAIAsInfantry(boolean holdingRangedWeapon) {
        if(!holdingRangedWeapon){
            super.setAIAsInfantry(false);
        }
    }

    Player getNearestPlayer(Entity entity){
        Player nearest = entity.level().getNearestPlayer(entity, LOOK_RANGE);

        if(nearest != null && !nearest.isSpectator() && !nearest.isInvisible()){
            if(this.getTarget() == null)
                return nearest;
        }
        return null;
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

        if(lastY == -1 || lastYTickCount <= 0){
            lastY = this.getOriginal().position().y();
            lastYTickCount = 5;
        }
        else {
            lastYTickCount--;
        }
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

            return !data.isChild() ? EpicColoniesArmatures.CITIZEN_REGULAR.get() : childArmature;
        }
        if(dataView != null){
            return !dataView.isChild() ? EpicColoniesArmatures.CITIZEN_REGULAR.get() : childArmature;
        }
        return EpicColoniesArmatures.CITIZEN_REGULAR.get();
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
    public CitizenPatchData getCitizenPatchData(){
        return this.citizenPatchData;
    }

    public void setCitizenPatchData(CitizenPatchData citizenPatchData) {
        this.citizenPatchData = citizenPatchData;
    }

    @Override
    public void serverTick(LivingEvent.LivingTickEvent event) {
        super.serverTick(event); // already dispatches to clientTick()/serverTick() internally, including onCitizenTick() on the client
        onCitizenTick(); // only need to run it here for the server, since clientTick() already covers the client path
        manageHeadRotWithEyes();
        if(wasUsingBow && this.getCurrentLivingMotion() != EpicColoniesLivingMotions.JOG){
            bowUseCounter++;
        }
        else{
            bowUseCounter = 0;
        }
    }


    public JobEntry getJobEntryFromDataView(){
        AbstractEntityCitizen citizen = this.getOriginal();
        if (citizen.level().isClientSide()) {
            ICitizenDataView view = citizen.getCitizenDataView();
            if (view != null) {
                IJobView jobView = view.getJobView();
                if (jobView != null)
                    return jobView.getEntry();
            }
        }
        return null;
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

    public boolean actuallyOnGround(){
        Vec3 pos = this.getOriginal().position();
        double offset = -0.1;
        int xPos = Mth.floor(pos.x());
        int yPos = Mth.floor(pos.y() + offset);
        int zPos = Mth.floor(pos.z());
        BlockPos blockPos = new BlockPos(xPos, yPos, zPos);
        BlockState state = this.getOriginal().level().getBlockState(blockPos);
        return state.isSolidRender(this.getOriginal().level(), blockPos) && !state.canBeReplaced();
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

    @Override
    public void updateMotion(boolean considerInaction) {
        if(this.getOriginal().getMainHandItem().getItem() instanceof ProjectileWeaponItem)
            super.commonAggressiveRangedMobUpdateMotion(considerInaction);
        else super.commonMobUpdateMotion(considerInaction);
    }


    @Override
    protected void setWeaponMotions() {
        super.setWeaponMotions();

        Map<WeaponCategory, Map<Style, Set<Pair<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>>>>> livingByCategory = new HashMap<>();
        Map<WeaponCategory, Map<Style, CombatBehaviors.Builder<HumanoidMobPatch<?>>>> attackByCategory = new HashMap<>();

        for (CombatBehaviourBase.WeaponMotionDetails details : CombatBehaviourBase.behaviourList) {
            for (CombatBehaviourBase.WeaponMotions motions : details.motions()) {
                Set<Pair<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>>> livingMotionSet = Set.of(
                        Pair.of(LivingMotions.IDLE, motions.idleMotion()),
                        Pair.of(LivingMotions.WALK, motions.walkMotion()),
                        Pair.of(EpicColoniesLivingMotions.JOG, motions.jogMotion()),
                        Pair.of(LivingMotions.CHASE, motions.runMotion())
                );

                livingByCategory
                        .computeIfAbsent(details.category(), c -> new HashMap<>())
                        .put(motions.style(), livingMotionSet); // later registration for the same (category, style) wins

                attackByCategory
                        .computeIfAbsent(details.category(), c -> new HashMap<>())
                        .put(motions.style(), motions.behaviour());
            }
        }

        livingByCategory.forEach((category, byStyle) -> this.weaponLivingMotions.put(category, ImmutableMap.copyOf(byStyle)));
        attackByCategory.forEach((category, byStyle) -> this.weaponAttackMotions.put(category, ImmutableMap.copyOf(byStyle)));
    }


    @Override
    public void modifyLivingMotionByCurrentItem(boolean onStartTracking){
            Map<LivingMotion, AssetAccessor<? extends StaticAnimation>> oldLivingAnimations = this.getAnimator().getLivingAnimations();
            Map<LivingMotion, AssetAccessor<? extends StaticAnimation>> newLivingAnimations = Maps.newHashMap();
            CapabilityItem mainhandCap = this.getHoldingItemCapability(InteractionHand.MAIN_HAND);
            CapabilityItem offhandCap = this.getAdvancedHoldingItemCapability(InteractionHand.OFF_HAND);
            Map<LivingMotion, AssetAccessor<? extends StaticAnimation>> livingMotionModifiers = new HashMap(mainhandCap.getLivingMotionModifier(this, InteractionHand.MAIN_HAND));
            livingMotionModifiers.putAll(offhandCap.getLivingMotionModifier(this, InteractionHand.OFF_HAND));
            boolean hasChange = false;

            for(Map.Entry<LivingMotion, AssetAccessor<? extends StaticAnimation>> entry : livingMotionModifiers.entrySet()) {
                AssetAccessor<? extends StaticAnimation> aniamtion = (AssetAccessor)entry.getValue();
                if (!oldLivingAnimations.containsKey(entry.getKey())) {
                    hasChange = true;
                } else if (oldLivingAnimations.get(entry.getKey()) != aniamtion) {
                    hasChange = true;
                }

                newLivingAnimations.put((LivingMotion)entry.getKey(), aniamtion);
            }

            if (this.weaponLivingMotions != null && this.weaponLivingMotions.containsKey(mainhandCap.getWeaponCategory())) {

                Map<Style, Set<Pair<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>>>> byStyle = (Map)this.weaponLivingMotions.get(mainhandCap.getWeaponCategory());
                Style style = mainhandCap.getStyle(this);
                if (byStyle.containsKey(style) || byStyle.containsKey(CapabilityItem.Styles.COMMON)) {

                    for(Pair<LivingMotion, AnimationManager.AnimationAccessor<? extends StaticAnimation>> pair : byStyle.getOrDefault(style, byStyle.get(CapabilityItem.Styles.COMMON))) {
                        newLivingAnimations.put((LivingMotion)pair.getFirst(), (AssetAccessor)pair.getSecond());
                    }
                }
            }

            if (!hasChange) {
                for(LivingMotion oldLivingMotion : oldLivingAnimations.keySet()) {
                    if (!newLivingAnimations.containsKey(oldLivingMotion)) {
                        hasChange = true;
                        break;
                    }
                }
            }

            if (hasChange || onStartTracking) {
                this.getAnimator().resetLivingAnimations();
                Animator var10001 = this.getAnimator();
                Objects.requireNonNull(var10001);
                newLivingAnimations.forEach(var10001::addLivingAnimation);
                ChangeLivingMotion msg = new ChangeLivingMotion(((PathfinderMob)this.original).getId());
                msg.putEntries(newLivingAnimations.entrySet());
                EpicColoniesPacketHandler.sendToAll(msg);
            }
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

    public boolean shouldRun() {

        return this.getTarget() != null || this.getOriginal().getEntityStateController().getState() == EntityAICitizenAvoidEntity.FleeStates.RUNNING;
    }

    public boolean shouldJogWithAnim() {
        return (getCurrentForwardSpeed() > 0.08);
    }

    public double getCurrentForwardSpeed() {
        Vec3 movement = this.getOriginal().getDeltaMovement();
        Vec3 forward = this.getOriginal().getForward();
        return movement.dot(forward);
    }

    public float getAnimForwardSpeed(float minSpeed, float maxSpeed) {

        double forwardSpeed = getCurrentForwardSpeed();

        // Get the horse's actual max walk speed attribute
        double maxWalkSpeed = this.getOriginal().getAttributeValue(Attributes.MOVEMENT_SPEED);

        // Normalize: 0.0 when still, 1.0 at full walk speed
        double normalized = Math.min(forwardSpeed / maxWalkSpeed, 1.0);

        // Scale between your known good minimum and maximum playback speed
        return (float)(minSpeed + normalized * (maxSpeed - minSpeed));
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

    @Override
    public float getImpact(InteractionHand hand) {
        return 1.5F;
    }

    @Override
    public void initAnimator(Animator animator) {
        // All available living motions are listed in this enum: https://github.com/Epic-Fight/epicfight/blob/1.21.1/src/main/java/yesman/epicfight/api/animation/LivingMotions.java#L4-L6
        super.initAnimator(animator);
        animator.addLivingAnimation(LivingMotions.EAT, EpicColoniesAnimations.CITIZEN_EAT);
        animator.addLivingAnimation(LivingMotions.CLIMB, EpicColoniesAnimations.CITIZEN_CLIMB);
        animator.addLivingAnimation(LivingMotions.DIGGING, EpicColoniesAnimations.CITIZEN_DIG);
        animator.addLivingAnimation(EpicColoniesLivingMotions.USE, EpicColoniesAnimations.CITIZEN_DIG);

        animator.addLivingAnimation(EpicColoniesLivingMotions.SIT_SLEEP, Animations.BIPED_SIT);

        animator.addLivingAnimation(LivingMotions.IDLE, Animations.BIPED_IDLE);
        animator.addLivingAnimation(LivingMotions.WALK, EpicColoniesAnimations.CITIZEN_WALK);
        animator.addLivingAnimation(EpicColoniesLivingMotions.JOG, EpicColoniesAnimations.CITIZEN_JOG);
        animator.addLivingAnimation(LivingMotions.CHASE, Animations.BIPED_RUN);
        animator.addLivingAnimation(LivingMotions.RUN, Animations.BIPED_RUN);
        animator.addLivingAnimation(LivingMotions.FALL, Animations.BIPED_FALL);
        animator.addLivingAnimation(LivingMotions.SIT, Animations.BIPED_SIT);
        animator.addLivingAnimation(LivingMotions.DEATH, Animations.BIPED_DEATH);
        animator.addLivingAnimation(LivingMotions.JUMP, Animations.BIPED_JUMP);
        animator.addLivingAnimation(LivingMotions.SLEEP, Animations.BIPED_SLEEPING);
        animator.addLivingAnimation(LivingMotions.AIM, Animations.BIPED_BOW_AIM);
        animator.addLivingAnimation(LivingMotions.SHOT, Animations.BIPED_BOW_SHOT);
        animator.addLivingAnimation(LivingMotions.DRINK, Animations.BIPED_DRINK);
    }

}
package net.kenji.epic_colonies.gameasset.patch;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.ICitizenDataView;
import com.minecolonies.api.colony.jobs.IJob;
import com.minecolonies.api.colony.jobs.IJobView;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.entity.ai.ITickingStateAI;
import com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.colony.CitizenData;
import com.minecolonies.core.colony.jobs.JobBuilder;
import com.minecolonies.core.entity.ai.minimal.EntityAICitizenAvoidEntity;
import com.minecolonies.core.entity.ai.minimal.EntityAIEatTask;
import com.minecolonies.core.entity.ai.minimal.EntityAIFloat;
import com.minecolonies.core.entity.ai.workers.builder.EntityAIStructureBuilder;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import com.minecolonies.core.entity.other.SittingEntity;
import com.mojang.datafixers.util.Pair;
import net.kenji.epic_colonies.api.CitizenPatchData;
import net.kenji.epic_colonies.api.data.CitizenMeshCache;
import net.kenji.epic_colonies.gameasset.EpicColoniesAnimations;
import net.kenji.epic_colonies.gameasset.EpicColoniesArmatures;
import net.kenji.epic_colonies.gameasset.EpicColoniesLivingMotions;
import net.kenji.epic_colonies.gameasset.armatures.CitizenArmature;
import net.kenji.epic_colonies.mixins.LivingEntityAccessor;
import net.kenji.epic_colonies.network.ChangeLivingMotion;
import net.kenji.epic_colonies.network.ClientCitizenSyncPacket;
import net.kenji.epic_colonies.network.EpicColoniesPacketHandler;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import org.jline.utils.Log;
import yesman.epicfight.api.animation.*;
import yesman.epicfight.api.animation.types.DynamicAnimation;
import yesman.epicfight.api.animation.types.SelectiveAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.animation.ClientAnimator;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.client.world.capabilites.entitypatch.player.AbstractClientPlayerPatch;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.MobCombatBehaviors;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.network.EpicFightNetworkManager;
import yesman.epicfight.network.server.SPChangeLivingMotion;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.Factions;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.entitypatch.mob.SkeletonPatch;
import yesman.epicfight.world.capabilities.entitypatch.mob.WitherSkeletonPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.ai.goal.AnimatedAttackGoal;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import yesman.epicfight.world.entity.ai.goal.TargetChasingGoal;
import yesman.epicfight.world.item.*;

import javax.annotation.Nullable;
import java.util.*;

public class CitizenEntityPatch<E extends AbstractEntityCitizen> extends HumanoidMobPatch<AbstractEntityCitizen> {
    public boolean shouldRun = false;
    public static Map<UUID, CitizenPatchData> citizenPatchDataMap = new HashMap<>();
    protected CitizenPatchData citizenPatchData = new CitizenPatchData();

    public static int MAX_COMPOSITE_PLAY_COUNTER = 20;
    public int compositeAnimPlayCounter = MAX_COMPOSITE_PLAY_COUNTER;
    public static AnimationManager.AnimationAccessor<? extends StaticAnimation> DEFAULT_BROW_ANIM = null;
    public static AnimationManager.AnimationAccessor<? extends StaticAnimation> DEFAULT_EYES_ANIM = null;

    public AnimationManager.AnimationAccessor<? extends StaticAnimation> eyebrowAnim;
    public AnimationManager.AnimationAccessor<? extends StaticAnimation> eyeMoveAnim;

    public CitizenEntityPatch() {
        super(Factions.VILLAGER);
    }
    public boolean didJump = false;

    private WeaponCategory lastWeaponCategory = null;
    private Style lastStyle = null;
    public static int MAX_BLINK_COUNTER = 20 * 20;
    public int blinkCounter = 0;
    @Override
    public boolean overrideRender() {
        return true;
    }



    @Override
    public void setAIAsInfantry(boolean holdingRangedWeapon) {
        if(!holdingRangedWeapon){
            super.setAIAsInfantry(false);
        }
    }


    @Override
    public void onAddedToWorld() {
        super.onAddedToWorld();
        DEFAULT_BROW_ANIM = EpicColoniesAnimations.CITIZEN_BLINK;
        DEFAULT_EYES_ANIM = EpicColoniesAnimations.CITIZEN_EYES_MOVE;

        eyebrowAnim = EpicColoniesAnimations.CITIZEN_BLINK;
        eyeMoveAnim = EpicColoniesAnimations.CITIZEN_EYES_MOVE;

        animator.playAnimation(eyebrowAnim, 0F);
        animator.playAnimation(eyeMoveAnim, 0F);


        boolean isChild = false;
        AbstractEntityCitizen citizen = this.getOriginal();


        if (citizen.getCitizenDataView() == null) {
                CitizenMeshCache.Entry cached = CitizenMeshCache.get(citizen.getUUID());
                if (cached != null) {
                    this.getOriginal().setIsChild(cached.isChild());
                }
        }
        if (citizen.getCitizenData() == null) {
            CitizenMeshCache.Entry cached = CitizenMeshCache.get(citizen.getUUID());
            if (cached != null) {
                this.getOriginal().setIsChild(cached.isChild());
            }
        }
    }

    @Override
    public Joint getParentJointOfHand(InteractionHand hand) {
        if(this.getArmature() instanceof CitizenArmature citizenArmature){
            return hand == InteractionHand.MAIN_HAND ? citizenArmature.rightToolJoint() : citizenArmature.leftToolJoint();
        }
        return (Joint)this.parentJointOfHands.getOrDefault(hand, this.armature.rootJoint);
    }
    @Override
    public HumanoidArmature getArmature() {
        ICitizenData data  = this.getOriginal().getCitizenData();
        ICitizenDataView dataView  = this.getOriginal().getCitizenDataView();
        HumanoidArmature childArmature = !this.getOriginal().isFemale() ? EpicColoniesArmatures.CHILD_MALE.get() : EpicColoniesArmatures.CHILD_FEMALE.get();
        if(data != null) {
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

    public void tickEyesAnim(){
        if(isCitizenAsleep())
            this.eyebrowAnim = EpicColoniesAnimations.CITIZEN_EYES_CLOSED;
        else this.eyebrowAnim = DEFAULT_BROW_ANIM;
    }

    private void onCitizenTick(){
        tickEyesAnim();
        setSleepDir();

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
        this.weaponLivingMotions.put(
                CapabilityItem.WeaponCategories.TACHI, ImmutableMap.of(
                        CapabilityItem.Styles.TWO_HAND,
                        Set.of(
                                Pair.of(LivingMotions.IDLE, Animations.BIPED_HOLD_TACHI),

                                Pair.of(LivingMotions.WALK, Animations.BIPED_HOLD_TACHI),
                                Pair.of(EpicColoniesLivingMotions.JOG, Animations.BIPED_HOLD_TACHI),

                                Pair.of(LivingMotions.CHASE, Animations.BIPED_RUN_SPEAR)
                        ))

        );

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
        if(this.getOriginal().getMainHandItem().getItem() instanceof TachiItem) {
            Log.info("Current Motions: " + this.getAnimator().getLivingAnimation(LivingMotions.IDLE, null));
        }
        super.clientTick(event);

        onCitizenTick();

        playCompositeOnLayer(eyebrowAnim, Layer.Priority.HIGHEST);
        playCompositeOnLayer(eyeMoveAnim, Layer.Priority.LOWEST);

        playCompositeOptionalAnimation();
        tryStopAnim(LivingMotions.CLIMB);

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
    public void initAnimator(Animator animator) {
        // All available living motions are listed in this enum: https://github.com/Epic-Fight/epicfight/blob/1.21.1/src/main/java/yesman/epicfight/api/animation/LivingMotions.java#L4-L6
        animator.addLivingAnimation(LivingMotions.EAT, EpicColoniesAnimations.CITIZEN_EAT);
        animator.addLivingAnimation(LivingMotions.CLIMB, EpicColoniesAnimations.CITIZEN_CLIMB);
        animator.addLivingAnimation(LivingMotions.DIGGING, EpicColoniesAnimations.CITIZEN_DIG);
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
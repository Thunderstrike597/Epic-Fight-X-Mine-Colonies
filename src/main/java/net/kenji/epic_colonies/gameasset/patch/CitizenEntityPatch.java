package net.kenji.epic_colonies.gameasset.patch;

import com.minecolonies.api.entity.ai.statemachine.states.AIWorkerState;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.colony.jobs.JobBuilder;
import com.minecolonies.core.entity.ai.minimal.EntityAICitizenAvoidEntity;
import com.minecolonies.core.entity.ai.minimal.EntityAIEatTask;
import com.minecolonies.core.entity.ai.minimal.EntityAIFloat;
import com.minecolonies.core.entity.ai.workers.builder.EntityAIStructureBuilder;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import com.minecolonies.core.entity.other.SittingEntity;
import net.kenji.epic_colonies.gameasset.EpicColoniesAnimations;
import net.kenji.epic_colonies.gameasset.EpicColoniesArmatures;
import net.kenji.epic_colonies.gameasset.EpicColoniesLivingMotions;
import net.kenji.epic_colonies.gameasset.armatures.CitizenArmature;
import net.kenji.epic_colonies.mixins.LivingEntityAccessor;
import net.kenji.epic_colonies.network.ClientCitizenSyncPacket;
import net.kenji.epic_colonies.network.EpicColoniesPacketHandler;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.UseAnim;
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
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.client.world.capabilites.entitypatch.player.AbstractClientPlayerPatch;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.gameasset.MobCombatBehaviors;
import yesman.epicfight.model.armature.HumanoidArmature;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.Factions;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;
import yesman.epicfight.world.item.*;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

    public static int MAX_BLINK_COUNTER = 20 * 20;
    public int blinkCounter = 0;
    @Override
    public boolean overrideRender() {
        return true;
    }

    public static class CitizenPatchData{
        public LivingMotion currentOptionalMotion = EpicColoniesLivingMotions.EMPTY;
        public LivingMotion currentOptionalCompositeMotion = EpicColoniesLivingMotions.EMPTY;
        public LivingMotion prevOptionalCompositeMotion = null;
        public LivingMotion prevOptionalMotion = null;

        public boolean isAsleep = false;
        public CitizenPatchData(UUID uuid, @Nullable LivingMotion optionalMotion,
                                @Nullable LivingMotion optionalCompositeMotion,
                                @Nullable LivingMotion prevOptionalCompositeMotion,
                                @Nullable LivingMotion prevOptionalMotion,

                                boolean isAsleep){

            CitizenPatchData prevData = citizenPatchDataMap.get(uuid); 

            this.currentOptionalMotion = optionalMotion;

            this.currentOptionalCompositeMotion = optionalCompositeMotion;


            this.prevOptionalCompositeMotion = prevOptionalCompositeMotion;
            this.prevOptionalMotion = prevOptionalMotion;

            this.isAsleep = isAsleep;
            CitizenEntityPatch.citizenPatchDataMap.put(uuid, this);

        }
        public CitizenPatchData(){

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
        return EpicColoniesArmatures.CITIZEN_REGULAR.get();
    }
    public void tickEyesAnim(){
        if(this.getOriginal().isSleeping() || this.original.getCitizenSleepHandler().isAsleep() || citizenPatchData.isAsleep)
            this.eyebrowAnim = EpicColoniesAnimations.CITIZEN_EYES_CLOSED;
        else this.eyebrowAnim = DEFAULT_BROW_ANIM;
    }

    private void onCitizenTick(){
        if(!this.isLogicalClient())
            tickCurrentOptionalMotion();

        tickEyesAnim();
        setSleepDir();
        if(citizenPatchData.currentOptionalCompositeMotion != null){
            citizenPatchData.prevOptionalCompositeMotion = citizenPatchData.currentOptionalCompositeMotion;
        }
        if(citizenPatchData.currentOptionalMotion != null){
            citizenPatchData.prevOptionalMotion = citizenPatchData.currentOptionalMotion;
        }
    }

    public CitizenPatchData getCitizenPatchData(){
        return this.citizenPatchData;
    }

    public void setCitizenPatchData(CitizenPatchData citizenPatchData) {
        this.citizenPatchData = citizenPatchData;
    }

    @Override
    public void tick(LivingEvent.LivingTickEvent event) {
        super.tick(event);
        onCitizenTick();
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
        if(!(citizen instanceof EntityCitizen entityCitizen)) return;
        IState state = entityCitizen.getCitizenAI().getState();
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
        else if (citizen.getVehicle() instanceof SittingEntity || (citizen.getCitizenSleepHandler().isAsleep() && citizen.getFeetBlockState().isSolidRender(citizen.level(), citizen.blockPosition()) && citizen.getPose() != Pose.SLEEPING)) {
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


        if(citizen.getCitizenJobHandler().getColonyJob() != null) {
            if (citizen.getCitizenJobHandler().getColonyJob().getWorkerAI().getState() == AIWorkerState.MINE_BLOCK) {
                compositeMotion = LivingMotions.DIGGING;
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
        super.commonMobUpdateMotion(considerInaction);

        if (this.original.isUsingItem() && citizenPatchData.currentOptionalCompositeMotion == null) {
            CapabilityItem activeItem = this.getHoldingItemCapability(this.original.getUsedItemHand());
            UseAnim useAnim = this.original.getItemInHand(this.original.getUsedItemHand()).getUseAnimation();
            UseAnim secondUseAnim = activeItem.getUseAnimation(this);

            if (useAnim == UseAnim.BLOCK || secondUseAnim == UseAnim.BLOCK)
                if (activeItem.getWeaponCategory() == CapabilityItem.WeaponCategories.SHIELD)
                    currentCompositeMotion = LivingMotions.BLOCK_SHIELD;
                else
                    currentCompositeMotion = LivingMotions.BLOCK;
            else if (useAnim == UseAnim.BOW || useAnim == UseAnim.SPEAR)
                currentCompositeMotion = LivingMotions.AIM;
            else if (useAnim == UseAnim.CROSSBOW)
                currentCompositeMotion = LivingMotions.RELOAD;
            else
                currentCompositeMotion = currentLivingMotion;
        } else if(citizenPatchData.currentOptionalCompositeMotion == null) {
            if (CrossbowItem.isCharged(this.original.getMainHandItem()))
                currentCompositeMotion = LivingMotions.AIM;
            else if (this.getClientAnimator().getCompositeLayer(Layer.Priority.MIDDLE).animationPlayer.getAnimation().get().isReboundAnimation())
                currentCompositeMotion = LivingMotions.NONE;
            else if (this.original.swinging && this.original.getSleepingPos().isEmpty())
                currentCompositeMotion = LivingMotions.DIGGING;
            else
                currentCompositeMotion = currentLivingMotion;
        }
    }

    @Override
    protected CombatBehaviors.Builder<HumanoidMobPatch<?>> getHoldingItemWeaponMotionBuilder() {
        CapabilityItem mainHandCap = EpicFightCapabilities.getItemStackCapability(this.getOriginal().getMainHandItem());
        if (mainHandCap == null) return MobCombatBehaviors.HUMANOID_FIST;
        WeaponCategory category = mainHandCap.getWeaponCategory();
        if (category == CapabilityItem.WeaponCategories.SWORD) {
            return MobCombatBehaviors.SKELETON_SWORD;
        }
        if (this.getOriginal().getMainHandItem().getItem() instanceof SpearItem || category == CapabilityItem.WeaponCategories.SPEAR) {
            if (this.getOriginal().getOffhandItem().isEmpty())
                return MobCombatBehaviors.HUMANOID_SPEAR_TWOHAND;
            return MobCombatBehaviors.HUMANOID_SPEAR_ONEHAND;
        }
        if (this.getOriginal().getMainHandItem().getItem() instanceof DaggerItem || category == CapabilityItem.WeaponCategories.DAGGER) {
            if (this.getOriginal().getOffhandItem().getItem() instanceof DaggerItem || category == CapabilityItem.WeaponCategories.DAGGER)
                return MobCombatBehaviors.HUMANOID_TWOHAND_DAGGER;
            return MobCombatBehaviors.HUMANOID_ONEHAND_DAGGER;
        }
        if (this.getOriginal().getMainHandItem().getItem() instanceof LongswordItem || category == CapabilityItem.WeaponCategories.LONGSWORD) {
                return MobCombatBehaviors.HUMANOID_LONGSWORD;
        }
        if (this.getOriginal().getMainHandItem().getItem() instanceof TachiItem || category == CapabilityItem.WeaponCategories.TACHI) {
            return MobCombatBehaviors.HUMANOID_TACHI;
        }
        if (this.getOriginal().getMainHandItem().getItem() instanceof GreatswordItem || category == CapabilityItem.WeaponCategories.GREATSWORD) {
            return MobCombatBehaviors.HUMANOID_GREATSWORD;
        }

        return super.getHoldingItemWeaponMotionBuilder();
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
                Log.info("Living Anim Null, Reassigning! | Anim: " + anim);
            }

            if (anim != null) {
                Layer compositeLayer = this.getClientAnimator().getCompositeLayer(anim.get().getPriority());
                AnimationPlayer animPlayer = compositeLayer.animationPlayer;
                StaticAnimation playingAnim = animPlayer.getAnimation().get().getRealAnimation().get();
                StaticAnimation checkingAnim = anim.get().getRealAnimation().get();


                if(animPlayer.getAnimation() == null || playingAnim != checkingAnim) {
                    getAnimator().playAnimation(anim, 0.18F);
                    if(animPlayer.getAnimation() != null) {
                        Log.info("Playing Anim From Player: " + playingAnim);

                        Log.info("Playing Anim: " + checkingAnim);
                    }
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

        playCompositeOnLayer(eyebrowAnim, Layer.Priority.HIGHEST);
        playCompositeOnLayer(eyeMoveAnim, Layer.Priority.LOWEST);

        playCompositeOptionalAnimation();
        tryStopAnim(citizenPatchData.prevOptionalMotion);
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
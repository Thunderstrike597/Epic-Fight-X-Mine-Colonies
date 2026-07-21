package net.kenji.epic_colonies.gameasset.patch.base;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.datafixers.util.Pair;
import net.kenji.epic_colonies.api.CitizenPatchData;
import net.kenji.epic_colonies.compat.CombatBehaviourBase;
import net.kenji.epic_colonies.gameasset.EpicColoniesAnimations;
import net.kenji.epic_colonies.gameasset.EpicColoniesLivingMotions;
import net.kenji.epic_colonies.network.ChangeLivingMotion;
import net.kenji.epic_colonies.network.EpicColoniesPacketHandler;
import net.kenji.epic_colonies.network.ServerBowActionPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.Animator;
import yesman.epicfight.api.animation.LivingMotion;
import yesman.epicfight.api.animation.LivingMotions;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.gameasset.Animations;
import yesman.epicfight.world.capabilities.entitypatch.Faction;
import yesman.epicfight.world.capabilities.entitypatch.HumanoidMobPatch;
import yesman.epicfight.world.capabilities.item.CapabilityItem;
import yesman.epicfight.world.capabilities.item.Style;
import yesman.epicfight.world.capabilities.item.WeaponCategory;
import yesman.epicfight.world.entity.ai.goal.CombatBehaviors;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractExpressiveHumanoidPatch<T extends PathfinderMob> extends HumanoidMobPatch<T> {
    protected static int MAX_COMPOSITE_PLAY_COUNTER = 20;
    protected int compositeAnimPlayCounter = MAX_COMPOSITE_PLAY_COUNTER;
    protected static AnimationManager.AnimationAccessor<? extends StaticAnimation> DEFAULT_BROW_ANIM = null;
    protected static AnimationManager.AnimationAccessor<? extends StaticAnimation> DEFAULT_EYES_ANIM = null;
    protected CitizenPatchData citizenPatchData = new CitizenPatchData();

    protected AnimationManager.AnimationAccessor<? extends StaticAnimation> facialAnim;
    protected AnimationManager.AnimationAccessor<? extends StaticAnimation> eyeMoveAnim;

    protected boolean wasUsingBow = false;
    protected boolean didJump = false;

    protected int bowUseCounter = 0;
    protected int lastYTickCount = 0;
    protected double lastY = -1;
    protected ItemStack lastHeldItem = ItemStack.EMPTY;

    protected static float LOOK_RANGE = 8.0F;
    protected static float FADE_DISTANCE = 2.0F;
    protected static float EYE_ANGLE_SENSITIVITY_DEG = 88F;        // how far off-center = full eye deflection (unchanged)
    protected static float MAX_TRACKING_ANGLE_DEG = 60;  // beyond this angle, citizen can't plausibly "see" the player - eyes go centered
    protected static float ANGLE_FADE_DEG = 15F;
    protected static float MAX_EYE_OFFSET = 0.075F;
    protected static float HEAD_TURN_SPEED_DEG = 8.0F;


    protected Player getNearestPlayer(Entity entity){
        Player nearest = entity.level().getNearestPlayer(entity, LOOK_RANGE);

        if(nearest != null && !nearest.isSpectator() && !nearest.isInvisible()){
            if(this.getTarget() == null)
                return nearest;
        }
        return null;
    }
    public AbstractExpressiveHumanoidPatch(Faction faction) {
        super(faction);
    }

    public float getJogSpeed(){
        return 0.08F;
    }

    public boolean isWasUsingBow(){
        return wasUsingBow && bowUseCounter >= 32;
    }

    public double getLastY() {
        return lastY;
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
    public void setWasUsingBow(boolean value){
        if(this.getCurrentLivingMotion() == EpicColoniesLivingMotions.JOG) return;
        this.wasUsingBow = value;
        this.bowUseCounter = 0;
        if(this.getOriginal().level().isClientSide()) {
            EpicColoniesPacketHandler.sendToServer(new ServerBowActionPacket(this.getOriginal().getUUID(), this.wasUsingBow));
        }
    }

    public boolean shouldRun() {
        return this.getTarget() != null;
    }

    public boolean shouldJogWithAnim() {
        return (getCurrentForwardSpeed() > getJogSpeed());
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

    public CitizenPatchData getCitizenPatchData(){
        return this.citizenPatchData;
    }

    public void setCitizenPatchData(CitizenPatchData citizenPatchData) {
        this.citizenPatchData = citizenPatchData;
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
    public void updateMotion(boolean considerInaction) {
        if(this.getOriginal().getMainHandItem().getItem() instanceof ProjectileWeaponItem)
            super.commonAggressiveRangedMobUpdateMotion(considerInaction);
        else super.commonMobUpdateMotion(considerInaction);
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
    }

    @Override
    protected void serverTick(LivingEvent.LivingTickEvent event) {
        super.serverTick(event);
        if(wasUsingBow && this.getCurrentLivingMotion() != EpicColoniesLivingMotions.JOG){
            bowUseCounter++;
        }
        else{
            bowUseCounter = 0;
        }
    }
    @Override
    public void setAIAsInfantry(boolean holdingRangedWeapon) {
        if(!holdingRangedWeapon){
            super.setAIAsInfantry(false);
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
            if (!this.isLogicalClient()) {
                ChangeLivingMotion msg = new ChangeLivingMotion(((PathfinderMob)this.original).getId());
                msg.putEntries(newLivingAnimations.entrySet());
                EpicColoniesPacketHandler.sendToAll(msg);
            }
        }
    }

}

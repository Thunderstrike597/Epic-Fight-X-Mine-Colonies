package net.kenji.epic_colonies.mixins;

import net.minecraft.world.entity.projectile.AbstractArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractArrow.class)
public interface AccessorAbstractArrow {
    @Invoker("setPierceLevel")
    void invokeSetPierceLevel(byte level);
    
    @Accessor("knockback")
    void setKnockback(int knockback);
}
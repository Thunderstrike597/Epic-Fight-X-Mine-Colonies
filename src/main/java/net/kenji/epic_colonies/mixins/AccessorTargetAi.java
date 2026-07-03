package net.kenji.epic_colonies.mixins;

import com.minecolonies.api.entity.ai.combat.threat.IThreatTableEntity;
import com.minecolonies.core.entity.ai.combat.TargetAI;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = TargetAI.class, remap = false)
public interface AccessorTargetAi<T extends Mob & IThreatTableEntity> {

    @Accessor("user")
    T getUser();

    @Invoker("checkForTarget")
    boolean invokeCheckForTarget();
}

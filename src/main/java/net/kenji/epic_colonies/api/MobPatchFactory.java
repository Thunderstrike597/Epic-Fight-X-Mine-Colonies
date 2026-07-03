package net.kenji.epic_colonies.api;

import com.minecolonies.api.entity.ModEntities;
import net.kenji.epic_colonies.client.render.patched_renderer.CitizenPatchRenderer;
import net.kenji.epic_colonies.client.render.patched_renderer.NorsemanRaiderPatchRenderer;
import net.kenji.epic_colonies.gameasset.EpicColoniesArmatures;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.kenji.epic_colonies.gameasset.patch.NorsemanRaiderPatch;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;
import yesman.epicfight.gameasset.Armatures;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class MobPatchFactory {
    public static class MobPatchDefinitions {
        public final EntityType<?> entityType;
        public final Armatures.ArmatureAccessor<?> armature;
        public final RendererFactory rendererFactory;
        public final Function<Entity, Supplier<EntityPatch<?>>> mobPatch;

        public MobPatchDefinitions(
                EntityType<?> entityType,
                Armatures.ArmatureAccessor<?> accessor,
                Function<Entity, Supplier<EntityPatch<?>>> patch,
                RendererFactory rendererFactory
        ){
            this.entityType = entityType;
            this.armature = accessor;
            this.mobPatch = patch;
            this.rendererFactory = rendererFactory;
        }
    }
    public static final List<MobPatchDefinitions> mobPatches = new ArrayList<>();

    static {
        mobPatches.add(new MobPatchDefinitions(
                ModEntities.CITIZEN,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> CitizenEntityPatch::new,
                (context, type) -> new CitizenPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                ModEntities.NORSEMEN_CHIEF,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> NorsemanRaiderPatch::new,
                (context, type) -> new NorsemanRaiderPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
    }
    @FunctionalInterface
    public interface RendererFactory {
        PatchedLivingEntityRenderer create(Object context, EntityType<?> type);
    }
}

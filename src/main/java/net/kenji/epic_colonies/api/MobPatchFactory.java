package net.kenji.epic_colonies.api;

import com.minecolonies.api.entity.ModEntities;
import net.kenji.epic_colonies.client.render.patched_renderer.*;
import net.kenji.epic_colonies.gameasset.EpicColoniesArmatures;
import net.kenji.epic_colonies.gameasset.patch.CitizenEntityPatch;
import net.kenji.epic_colonies.gameasset.patch.MinecoloniesMonsterPatch;
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
                ModEntities.NORSEMEN_ARCHER,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new NorsemanArcherPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                ModEntities.CAMP_NORSEMEN_ARCHER,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new NorsemanArcherPatchRenderer((EntityRendererProvider.Context) context, type)
        ));


        mobPatches.add(new MobPatchDefinitions(
                ModEntities.NORSEMEN_CHIEF,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new NorsemanChiefPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                ModEntities.CAMP_NORSEMEN_CHIEF,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new NorsemanChiefPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                ModEntities.AMAZON,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new AmazonPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                ModEntities.CAMP_AMAZON,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new AmazonPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                ModEntities.AMAZONCHIEF,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new AmazonChiefPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                ModEntities.CAMP_AMAZONCHIEF,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new AmazonChiefPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                ModEntities.BARBARIAN,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new BarbarianPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                ModEntities.CAMP_BARBARIAN,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new BarbarianPatchRenderer((EntityRendererProvider.Context) context, type)
        ));


        mobPatches.add(new MobPatchDefinitions(
                ModEntities.ARCHERBARBARIAN,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new BarbarianPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                ModEntities.CAMP_ARCHERBARBARIAN,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new BarbarianPatchRenderer((EntityRendererProvider.Context) context, type)
        ));

        mobPatches.add(new MobPatchDefinitions(
                ModEntities.CHIEFBARBARIAN,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new BarbarianChiefPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                ModEntities.CAMP_CHIEFBARBARIAN,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new BarbarianChiefPatchRenderer((EntityRendererProvider.Context) context, type)
        ));

        mobPatches.add(new MobPatchDefinitions(
                ModEntities.CHIEFPIRATE,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new PirateChiefPatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                ModEntities.CAMP_CHIEFPIRATE,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new PirateChiefPatchRenderer((EntityRendererProvider.Context) context, type)
        ));

        mobPatches.add(new MobPatchDefinitions(
                ModEntities.PIRATE,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new PiratePatchRenderer((EntityRendererProvider.Context) context, type)
        ));
        mobPatches.add(new MobPatchDefinitions(
                ModEntities.CAMP_PIRATE,
                EpicColoniesArmatures.CITIZEN_REGULAR,
                (e) -> MinecoloniesMonsterPatch::new,
                (context, type) -> new PiratePatchRenderer((EntityRendererProvider.Context) context, type)
        ));
    }
    @FunctionalInterface
    public interface RendererFactory {
        PatchedLivingEntityRenderer create(Object context, EntityType<?> type);
    }
}

package net.kenji.epic_colonies.client.meshes;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.minecolonies.api.client.render.modeltype.IModelType;
import com.minecolonies.api.client.render.modeltype.ModModelTypes;
import com.minecolonies.api.colony.jobs.ModJobs;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.mojang.datafixers.util.Pair;
import net.kenji.epic_colonies.EpicColonies;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import yesman.epicfight.api.client.model.Meshes;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class EpicColoniesMeshes implements PreparableReloadListener {

    public static Map<Pair<Boolean, ResourceLocation>, Meshes.MeshAccessor<EpicColoniesMesh>> meshMap = new HashMap<>();
    public static Map<Pair<Boolean, ResourceLocation>, Meshes.MeshAccessor<EpicColoniesMesh>> defaultMeshMap = new HashMap<>();


    public static List<ResourceLocation> bigEyeTextures = new ArrayList<>();
    public static List<ResourceLocation> lowerEyeTextures = new ArrayList<>();

    public static final Meshes.MeshAccessor<EpicColoniesMesh> DEFAULT_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CITIZEN_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CHILD_MALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> COURIER_MALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> BUILDER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> KNIGHT_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> ARCHER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> NOBLE_MALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> COOK_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> BAKER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> MINER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> NETHERWORKER_MALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> BLACKSMITH_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> SMELTER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> GLASSBLOWER_MALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> FLETCHER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CRAFTER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> MECHANIST_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CONCRETE_MIXER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> ENCHANTER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> ALCHEMIST_MALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> FARMER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> SHEPHERD_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> COWHERDER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CHICKENHERDER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> SWINEHERDER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> RABBITHERDER_MALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> PLANTER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> COMPOSTER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> FLORIST_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> FORESTER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CARPENTER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> DYER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> APIARY_MALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> FISHER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> HEALER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> DRUID_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> TEACHER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> STUDENT_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> UNDERTAKER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> ARISTOCRAT_MALE;



    public static final Meshes.MeshAccessor<EpicColoniesMesh> DEFAULT_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> DEFAULT_FEMALE_LOWER_EYES;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> CITIZEN_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CHILD_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CHILD_FEMALE_BIG_EYES;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CHILD_FEMALE_LOWER_EYES;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> COURIER_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> BUILDER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> KNIGHT_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> ARCHER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> NOBLE_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> COOK_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> BAKER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> MINER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> NETHERWORKER_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> BLACKSMITH_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> SMELTER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> GLASSBLOWER_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> FLETCHER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CRAFTER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> MECHANIST_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CONCRETE_MIXER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> ENCHANTER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> ALCHEMIST_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> FARMER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> SHEPHERD_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> COWHERDER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CHICKENHERDER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> SWINEHERDER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> RABBITHERDER_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> PLANTER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> COMPOSTER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> FLORIST_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> FORESTER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CARPENTER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> DYER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> APIARY_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> FISHER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> HEALER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> DRUID_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> TEACHER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> STUDENT_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> UNDERTAKER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> ARISTOCRAT_FEMALE;



    public static final Meshes.MeshAccessor<EpicColoniesMesh> NORSEMAN_ARCHER;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> NORSEMAN_CHIEF;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> AMAZON;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> AMAZON_SPEARMAN;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> AMAZON_CHIEF;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> MUMMY;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> PHARAO;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> SHIELDMAIDEN;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> HUMANOID_RAIDER;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> MERCENARY;


    static {
        DEFAULT_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/default_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        CITIZEN_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/citizen_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        meshMap.put(new Pair<>(false, ModModelTypes.CHILD_ID), CHILD_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/child_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));

        meshMap.put(new Pair<>(false, ModModelTypes.COURIER_ID), COURIER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/courier_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));

        meshMap.put(new Pair<>(false, ModModelTypes.BUILDER_ID), BUILDER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/builder_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));

        meshMap.put(new Pair<>(false, ModModelTypes.KNIGHT_GUARD_ID), KNIGHT_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/knight_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.KNIGHT_ID), KNIGHT_MALE);

        meshMap.put(new Pair<>(false, ModModelTypes.ARCHER_GUARD_ID), ARCHER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/archer_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.NOBLE_ID), NOBLE_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/noble_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));

        meshMap.put(new Pair<>(false, ModModelTypes.COOK_ID), COOK_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/cook_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.BAKER_ID), BAKER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/baker_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.MINER_ID),  MINER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/miner_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.NETHERWORKER_ID),  NETHERWORKER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/netherworker_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.BLACKSMITH_ID),  BLACKSMITH_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/blacksmith_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.SMELTER_ID),  SMELTER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/smelter_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.GLASSBLOWER_ID),  GLASSBLOWER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/glassblower_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.FLETCHER_ID),  FLETCHER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/fletcher_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.CRAFTER_ID),  CRAFTER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/crafter_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.MECHANIST_ID),  MECHANIST_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/mechanist_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.CONCRETE_MIXER_ID),  CONCRETE_MIXER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/concrete_mixer_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.ENCHANTER_ID),  ENCHANTER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/enchanter_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.ALCHEMIST_ID),  ALCHEMIST_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/alchemist_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.FARMER_ID),  FARMER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/farmer_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.SHEEP_FARMER_ID),  SHEPHERD_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/shepherd_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.COW_FARMER_ID),  COWHERDER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/cow_herder_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.CHICKEN_FARMER_ID),  CHICKENHERDER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/chicken_herder_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.PIG_FARMER_ID),  SWINEHERDER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/swine_herder_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.RABBIT_HERDER_ID),  RABBITHERDER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/rabbit_herder_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.PLANTER_ID),  PLANTER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/planter_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.COMPOSTER_ID),  COMPOSTER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/composter_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.FLORIST_ID),  FLORIST_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/florist_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.LUMBERJACK_ID),  FORESTER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/forester_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.CARPENTER_ID),  CARPENTER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/carpenter_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.DYER_ID),  DYER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/dyer_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.BEEKEEPER_ID),  APIARY_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/apiary_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.FISHERMAN_ID),  FISHER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/fisher_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.HEALER_ID),  HEALER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/healer_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.DRUID_ID),  DRUID_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/druid_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.TEACHER_ID),  TEACHER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/teacher_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.STUDENT_ID),  STUDENT_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/student_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.UNDERTAKER_ID),  UNDERTAKER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/undertaker_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(false, ModModelTypes.ARISTOCRAT_ID),  ARISTOCRAT_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/aristocrat_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));


        DEFAULT_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/default_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        DEFAULT_FEMALE_LOWER_EYES = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/default_female_lower_eyes", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        CITIZEN_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/citizen_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        CHILD_FEMALE_BIG_EYES = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/child_female_big_eyes", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        CHILD_FEMALE_LOWER_EYES = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/child_female_eyes_lower", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        meshMap.put(new Pair<>(true, ModModelTypes.CHILD_ID), CHILD_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/child_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));

        meshMap.put(new Pair<>(true, ModModelTypes.COURIER_ID), COURIER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/courier_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));

        meshMap.put(new Pair<>(true, ModModelTypes.BUILDER_ID), BUILDER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/builder_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));

        meshMap.put(new Pair<>(true, ModModelTypes.KNIGHT_GUARD_ID), KNIGHT_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/knight_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.KNIGHT_ID), KNIGHT_FEMALE);

        meshMap.put(new Pair<>(true, ModModelTypes.ARCHER_GUARD_ID), ARCHER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/archer_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.NOBLE_ID), NOBLE_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/noble_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));

        meshMap.put(new Pair<>(true, ModModelTypes.COOK_ID), COOK_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/cook_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.BAKER_ID), BAKER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/baker_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.MINER_ID),  MINER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/miner_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.NETHERWORKER_ID),  NETHERWORKER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/netherworker_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.BLACKSMITH_ID),  BLACKSMITH_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/blacksmith_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.SMELTER_ID),  SMELTER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/smelter_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.GLASSBLOWER_ID),  GLASSBLOWER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/glassblower_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.FLETCHER_ID),  FLETCHER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/fletcher_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.CRAFTER_ID),  CRAFTER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/crafter_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.MECHANIST_ID),  MECHANIST_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/mechanist_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.CONCRETE_MIXER_ID),  CONCRETE_MIXER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/concrete_mixer_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.ENCHANTER_ID),  ENCHANTER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/enchanter_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.ALCHEMIST_ID),  ALCHEMIST_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/alchemist_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.FARMER_ID),  FARMER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/farmer_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.SHEEP_FARMER_ID),  SHEPHERD_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/shepherd_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.COW_FARMER_ID),  COWHERDER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/cow_herder_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.CHICKEN_FARMER_ID),  CHICKENHERDER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/chicken_herder_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.PIG_FARMER_ID),  SWINEHERDER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/swine_herder_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
                meshMap.put(new Pair<>(true, ModModelTypes.RABBIT_HERDER_ID),  RABBITHERDER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/rabbit_herder_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.PLANTER_ID),  PLANTER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/planter_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.COMPOSTER_ID),  COMPOSTER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/composter_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.FLORIST_ID),  FLORIST_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/florist_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.LUMBERJACK_ID),  FORESTER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/forester_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.CARPENTER_ID),  CARPENTER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/carpenter_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.DYER_ID),  DYER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/dyer_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.BEEKEEPER_ID),  APIARY_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/apiary_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.FISHERMAN_ID),  FISHER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/fisher_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.HEALER_ID),  HEALER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/healer_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.DRUID_ID),  DRUID_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/druid_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.TEACHER_ID),  TEACHER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/teacher_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.STUDENT_ID),  STUDENT_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/student_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.UNDERTAKER_ID),  UNDERTAKER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/undertaker_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));
        meshMap.put(new Pair<>(true, ModModelTypes.ARISTOCRAT_ID),  ARISTOCRAT_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/aristocrat_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new)));


        NORSEMAN_ARCHER = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/raider/norseman_archer", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        NORSEMAN_CHIEF = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/raider/norseman_chief", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        AMAZON = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/raider/amazon", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        AMAZON_SPEARMAN = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/raider/amazon_spearman", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        AMAZON_CHIEF = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/raider/amazon_chief", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        MUMMY = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/raider/mummy", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        PHARAO = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/raider/pharao", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        SHIELDMAIDEN = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/raider/shieldmaiden", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        HUMANOID_RAIDER = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/raider/barbarian", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));


        MERCENARY = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/other/mercenary", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

    }


    /**
     * Must be called AFTER RegisterEvent<JobEntry> has fired (e.g. in FMLClientSetupEvent),
     * not from the static initializer — RegistryObjects aren't bound yet at class-load time.
     */
    public static void buildJobMeshMaps() {



        /// ARISTOCRAT MISSING FOR NOW
        bigEyeTextures = getTexturesFromJson("child_bigeyes");
        lowerEyeTextures = getTexturesFromJson("child_lowereyes");
    }
    public static List<ResourceLocation> getTexturesFromJson(String fileName) {
        List<ResourceLocation> locations = new ArrayList<>();
        String path = "data/" + EpicColonies.MODID + "/texture_data/" + fileName + ".json";

        try (InputStream is = EpicColonies.class.getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException("Could not find texture data file: " + path);
            }

            try (InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
                JsonArray textures = json.getAsJsonArray("valid_textures");

                for (JsonElement element : textures) {
                    String texturePath = element.getAsJsonObject().get("texture").getAsString();
                    locations.add(ResourceLocation.fromNamespaceAndPath(
                            "minecolonies", "textures/entity/citizen/" + texturePath
                    ));
                }
            }
        } catch (IOException e) {
            EpicColonies.LOGGER.error("Failed to load texture data: {}", path, e);
        }

        return locations;
    }
    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller1, Executor executor, Executor executor1) {
        return null;
    }
}
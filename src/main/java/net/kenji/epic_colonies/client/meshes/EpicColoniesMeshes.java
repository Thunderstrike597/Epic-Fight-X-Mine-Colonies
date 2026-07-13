package net.kenji.epic_colonies.client.meshes;

import com.minecolonies.api.colony.jobs.ModJobs;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.entity.ModEntities;
import com.minecolonies.core.client.model.MercenaryModel;
import com.minecolonies.core.colony.buildings.modules.CourierAssignmentModule;
import net.kenji.epic_colonies.EpicColonies;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import yesman.epicfight.api.client.model.Meshes;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class EpicColoniesMeshes implements PreparableReloadListener {
    public static Map<JobEntry, Meshes.MeshAccessor<EpicColoniesMesh>> jobMeshMapMale = new HashMap<>();
    public static Map<JobEntry, Meshes.MeshAccessor<EpicColoniesMesh>> jobMeshMapFemale = new HashMap<>();

    public static final Meshes.MeshAccessor<EpicColoniesMesh> DEFAULT_MALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> CITIZEN_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CHILD_MALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> COURIER_MALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> BUILDER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> KNIGHT_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> ARCHER_MALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> COOK_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> BAKER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> MINER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> BLACKSMITH_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> SMELTER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> GLASSBLOWER_MALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> FLETCHER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CRAFTER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> FARMER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> SHEPHERD_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> COWHERDER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CHICKENHERDER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> SWINEHERDER_MALE;


    public static final Meshes.MeshAccessor<EpicColoniesMesh> PLANTER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> COMPOSTER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> FORESTER_MALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> FISHER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> HEALER_MALE;



    public static final Meshes.MeshAccessor<EpicColoniesMesh> DEFAULT_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> CITIZEN_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CHILD_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> COURIER_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> BUILDER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> KNIGHT_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> ARCHER_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> COOK_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> BAKER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> MINER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> BLACKSMITH_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> SMELTER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> GLASSBLOWER_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> FLETCHER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CRAFTER_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> FARMER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> SHEPHERD_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> COWHERDER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CHICKENHERDER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> SWINEHERDER_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> PLANTER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> COMPOSTER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> FORESTER_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> FISHER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> HEALER_FEMALE;



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
        CHILD_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/child_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        COURIER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/courier_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        BUILDER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/builder_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        KNIGHT_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/knight_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        ARCHER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/archer_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        COOK_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/cook_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        BAKER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/baker_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        MINER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/miner_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        BLACKSMITH_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/blacksmith_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        SMELTER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/smelter_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        GLASSBLOWER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/glassblower_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        FLETCHER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/fletcher_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        CRAFTER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/crafter_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        FARMER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/farmer_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        SHEPHERD_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/shepherd_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        COWHERDER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/cow_herder_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        CHICKENHERDER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/chicken_herder_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        SWINEHERDER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/swine_herder_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        PLANTER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/planter_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        COMPOSTER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/composter_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        FORESTER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/forester_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        FISHER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/fisher_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        HEALER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/healer_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));


        DEFAULT_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/default_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        CITIZEN_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/citizen_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        CHILD_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/child_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        COURIER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/courier_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        BUILDER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/builder_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        KNIGHT_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/knight_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        ARCHER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/archer_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        COOK_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/cook_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        BAKER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/baker_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        MINER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/miner_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        BLACKSMITH_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/blacksmith_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        SMELTER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/smelter_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        GLASSBLOWER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/glassblower_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        FLETCHER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/fletcher_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        CRAFTER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/crafter_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        FARMER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/farmer_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        SHEPHERD_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/shepherd_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        COWHERDER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/cow_herder_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        CHICKENHERDER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/chicken_herder_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        SWINEHERDER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/swine_herder_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        PLANTER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/planter_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        COMPOSTER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/composter_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        FORESTER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/forester_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        FISHER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/fisher_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        HEALER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/healer_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));


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

        jobMeshMapMale.put(ModJobs.delivery.get(), COURIER_MALE);

        jobMeshMapMale.put(ModJobs.builder.get(), BUILDER_MALE);
        jobMeshMapMale.put(ModJobs.knight.get(), KNIGHT_MALE);
        jobMeshMapMale.put(ModJobs.archer.get(), ARCHER_MALE);
        jobMeshMapMale.put(ModJobs.cook.get(), COOK_MALE);
        jobMeshMapMale.put(ModJobs.baker.get(), BAKER_MALE);
        jobMeshMapMale.put(ModJobs.miner.get(), MINER_MALE);
        jobMeshMapMale.put(ModJobs.blacksmith.get(), BLACKSMITH_MALE);
        jobMeshMapMale.put(ModJobs.smelter.get(), SMELTER_MALE);
        jobMeshMapMale.put(ModJobs.glassblower.get(), GLASSBLOWER_MALE);
        jobMeshMapMale.put(ModJobs.fletcher.get(), FLETCHER_MALE);
        jobMeshMapMale.put(ModJobs.farmer.get(), FARMER_MALE);
        jobMeshMapMale.put(ModJobs.shepherd.get(), SHEPHERD_MALE);
        jobMeshMapMale.put(ModJobs.cowboy.get(), COWHERDER_MALE);
        jobMeshMapMale.put(ModJobs.chickenHerder.get(), CHICKENHERDER_MALE);
        jobMeshMapMale.put(ModJobs.swineHerder.get(), SWINEHERDER_MALE);
        jobMeshMapMale.put(ModJobs.planter.get(), PLANTER_MALE);
        jobMeshMapMale.put(ModJobs.composter.get(), COMPOSTER_MALE);
        jobMeshMapMale.put(ModJobs.lumberjack.get(), FORESTER_MALE);
        jobMeshMapMale.put(ModJobs.fisherman.get(), FISHER_MALE);
        jobMeshMapMale.put(ModJobs.healer.get(), HEALER_MALE);


        jobMeshMapFemale.put(ModJobs.delivery.get(), COURIER_FEMALE);

        jobMeshMapFemale.put(ModJobs.builder.get(), BUILDER_FEMALE);
        jobMeshMapFemale.put(ModJobs.knight.get(), KNIGHT_FEMALE);
        jobMeshMapFemale.put(ModJobs.archer.get(), ARCHER_FEMALE);
        jobMeshMapFemale.put(ModJobs.cook.get(), COOK_FEMALE);
        jobMeshMapFemale.put(ModJobs.baker.get(), BAKER_FEMALE);
        jobMeshMapFemale.put(ModJobs.miner.get(), MINER_FEMALE);
        jobMeshMapFemale.put(ModJobs.blacksmith.get(), BLACKSMITH_FEMALE);
        jobMeshMapFemale.put(ModJobs.smelter.get(), SMELTER_FEMALE);
        jobMeshMapFemale.put(ModJobs.glassblower.get(), GLASSBLOWER_FEMALE);
        jobMeshMapFemale.put(ModJobs.fletcher.get(), FLETCHER_FEMALE);
        jobMeshMapFemale.put(ModJobs.farmer.get(), FARMER_FEMALE);
        jobMeshMapFemale.put(ModJobs.shepherd.get(), SHEPHERD_FEMALE);
        jobMeshMapFemale.put(ModJobs.cowboy.get(), COWHERDER_FEMALE);
        jobMeshMapFemale.put(ModJobs.chickenHerder.get(), CHICKENHERDER_FEMALE);
        jobMeshMapFemale.put(ModJobs.swineHerder.get(), SWINEHERDER_FEMALE);
        jobMeshMapFemale.put(ModJobs.planter.get(), PLANTER_FEMALE);
        jobMeshMapFemale.put(ModJobs.composter.get(), COMPOSTER_FEMALE);
        jobMeshMapFemale.put(ModJobs.lumberjack.get(), FORESTER_FEMALE);
        jobMeshMapFemale.put(ModJobs.fisherman.get(), FISHER_FEMALE);
        jobMeshMapFemale.put(ModJobs.healer.get(), HEALER_FEMALE);

    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller1, Executor executor, Executor executor1) {
        return null;
    }
}
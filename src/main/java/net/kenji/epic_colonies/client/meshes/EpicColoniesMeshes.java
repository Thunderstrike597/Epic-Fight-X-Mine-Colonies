package net.kenji.epic_colonies.client.meshes;

import com.minecolonies.api.colony.jobs.ModJobs;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
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

    public static final Meshes.MeshAccessor<EpicColoniesMesh> BUILDER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> KNIGHT_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> COOK_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> BAKER_MALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> DEFAULT_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CHILD_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> CITIZEN_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> BUILDER_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> KNIGHT_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> COOK_FEMALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> BAKER_FEMALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> NORSEMAN_CHIEF;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> NORSEMAN_ARCHER;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> AMAZON;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> AMAZON_CHIEF;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> HUMANOID_RAIDER;


    static {
        DEFAULT_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/default_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        CITIZEN_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/citizen_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        CHILD_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/child_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        BUILDER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/builder_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        KNIGHT_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/knight_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        COOK_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/cook_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        BAKER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/baker_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        DEFAULT_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/default_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        CITIZEN_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/citizen_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        CHILD_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/child_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        BUILDER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/builder_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        KNIGHT_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/knight_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        COOK_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/cook_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        BAKER_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen/baker_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        NORSEMAN_CHIEF = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/raider/norseman_chief", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        NORSEMAN_ARCHER = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/raider/norseman_archer", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        AMAZON = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/raider/amazon", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        AMAZON_CHIEF = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/raider/amazon_chief", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        HUMANOID_RAIDER = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/raider/barbarian", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

    }

    /**
     * Must be called AFTER RegisterEvent<JobEntry> has fired (e.g. in FMLClientSetupEvent),
     * not from the static initializer — RegistryObjects aren't bound yet at class-load time.
     */
    public static void buildJobMeshMaps() {
        jobMeshMapMale.put(ModJobs.builder.get(), BUILDER_MALE);
        jobMeshMapMale.put(ModJobs.knight.get(), KNIGHT_MALE);
        jobMeshMapMale.put(ModJobs.cook.get(), COOK_MALE);
        jobMeshMapMale.put(ModJobs.baker.get(), BAKER_MALE);

        jobMeshMapFemale.put(ModJobs.builder.get(), BUILDER_FEMALE);
        jobMeshMapFemale.put(ModJobs.knight.get(), KNIGHT_FEMALE);
        jobMeshMapFemale.put(ModJobs.cook.get(), COOK_FEMALE);
        jobMeshMapFemale.put(ModJobs.baker.get(), BAKER_FEMALE);


    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller1, Executor executor, Executor executor1) {
        return null;
    }
}
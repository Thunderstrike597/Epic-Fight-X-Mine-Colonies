package net.kenji.epic_colonies.client;

import com.minecolonies.api.colony.jobs.ModJobs;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMesh;
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

    public static final Meshes.MeshAccessor<EpicColoniesMesh> CITIZEN_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> BUILDER_MALE;
    public static final Meshes.MeshAccessor<EpicColoniesMesh> KNIGHT_MALE;

    public static final Meshes.MeshAccessor<EpicColoniesMesh> CITIZEN_FEMALE;

    static {
        CITIZEN_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        BUILDER_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/builder_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
        KNIGHT_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/knight_male", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

        CITIZEN_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen_female", (jsonModelLoader) -> (EpicColoniesMesh) jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
    }

    /**
     * Must be called AFTER RegisterEvent<JobEntry> has fired (e.g. in FMLClientSetupEvent),
     * not from the static initializer — RegistryObjects aren't bound yet at class-load time.
     */
    public static void buildJobMeshMaps() {
        jobMeshMapMale.put(ModJobs.builder.get(), BUILDER_MALE);
        jobMeshMapMale.put(ModJobs.knight.get(), KNIGHT_MALE);
        // jobMeshMapFemale.put(ModJobs.builder.get(), BUILDER_FEMALE); etc.
    }

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller1, Executor executor, Executor executor1) {
        return null;
    }
}
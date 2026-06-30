package net.kenji.epic_colonies.client;

import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMesh;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import yesman.epicfight.api.client.model.Meshes;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class EpicColoniesMeshes implements PreparableReloadListener {
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CITIZEN_MALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen_male", (jsonModelLoader) -> (EpicColoniesMesh)jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));
    public static final Meshes.MeshAccessor<EpicColoniesMesh> CITIZEN_FEMALE = Meshes.MeshAccessor.create(EpicColonies.MODID, "entity/citizen_female", (jsonModelLoader) -> (EpicColoniesMesh)jsonModelLoader.loadSkinnedMesh(EpicColoniesMesh::new));

    @Override
    public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller profilerFiller, ProfilerFiller profilerFiller1, Executor executor, Executor executor1) {
        return null;
    }
}
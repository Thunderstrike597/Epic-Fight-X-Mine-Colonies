package net.kenji.epic_colonies.client.meshes;

import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.client.model.MeshPartDefinition;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.client.model.VertexBuilder;
import yesman.epicfight.client.mesh.HumanoidMesh;

import java.util.List;
import java.util.Map;

public class EpicColoniesMesh extends SkinnedMesh {
    public final SkinnedMeshPart main;
    public EpicColoniesMesh(@Nullable Map<String, Number[]> arrayMap, @Nullable Map<MeshPartDefinition, List<VertexBuilder>> partBuilders, @Nullable SkinnedMesh parent, RenderProperties properties) {
        super(arrayMap, partBuilders, parent, properties);
        this.main = this.getOrLogException(this.parts, "noGroups");
    }
}

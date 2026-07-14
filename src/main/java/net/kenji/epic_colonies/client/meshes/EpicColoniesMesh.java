package net.kenji.epic_colonies.client.meshes;

import org.jetbrains.annotations.Nullable;
import yesman.epicfight.api.client.model.MeshPartDefinition;
import yesman.epicfight.api.client.model.SkinnedMesh;
import yesman.epicfight.api.client.model.VertexBuilder;
import yesman.epicfight.client.mesh.HumanoidMesh;

import java.util.List;
import java.util.Map;

public class EpicColoniesMesh extends HumanoidMesh {
    public final SkinnedMeshPart main;
    public final SkinnedMeshPart breast;
    public final SkinnedMeshPart pants;
    public final SkinnedMeshPart featuresOn;
    public final SkinnedMeshPart featuresOff;

    public EpicColoniesMesh(@Nullable Map<String, Number[]> arrayMap, @Nullable Map<MeshPartDefinition, List<VertexBuilder>> partBuilders, @Nullable SkinnedMesh parent, RenderProperties properties) {
        super(arrayMap, partBuilders, parent, properties);
        this.main = this.getOrLogException(this.parts, "noGroups");
        this.breast = this.getOrLogException(this.parts, "breast");
        this.pants = this.getOrLogException(this.parts, "pants");
        this.featuresOn = this.getOrLogException(this.parts, "featuresOn");
        this.featuresOff = this.getOrLogException(this.parts, "featuresOff");

    }


}

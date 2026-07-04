package net.kenji.epic_colonies.client.render.patched_renderer.p_renderer;

import com.minecolonies.api.entity.mobs.AbstractEntityMinecoloniesMonster;
import com.minecolonies.core.client.render.CitizenArmorLayer;
import com.minecolonies.core.client.render.mobs.barbarians.RendererChiefBarbarian;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMesh;
import net.kenji.epic_colonies.client.meshes.EpicColoniesMeshes;
import net.kenji.epic_colonies.gameasset.patch.MinecoloniesMonsterPatch;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.world.entity.EntityType;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.model.Meshes;
import yesman.epicfight.client.renderer.patched.entity.PatchedLivingEntityRenderer;
import yesman.epicfight.client.renderer.patched.layer.PatchedItemInHandLayer;
import yesman.epicfight.client.renderer.patched.layer.WearableItemLayer;

public class PChiefBarbarianRenderer extends PatchedLivingEntityRenderer<AbstractEntityMinecoloniesMonster, MinecoloniesMonsterPatch<AbstractEntityMinecoloniesMonster>, HumanoidModel<AbstractEntityMinecoloniesMonster>, RendererChiefBarbarian,
EpicColoniesMesh> {
    public PChiefBarbarianRenderer(Meshes.MeshAccessor<EpicColoniesMesh> mesh, EntityRendererProvider.Context context, EntityType<?> entityType) {
        super(context, entityType);
        this.addPatchedLayer(ItemInHandLayer.class, new PatchedItemInHandLayer<>());
        this.addPatchedLayer(CitizenArmorLayer.class, new WearableItemLayer<>(mesh, false, context.getModelManager()));    }

    @Override
    public AssetAccessor<EpicColoniesMesh> getMeshProvider(MinecoloniesMonsterPatch<AbstractEntityMinecoloniesMonster> entitypatch) {
        return super.getMeshProvider(entitypatch);
    }

    public AssetAccessor<EpicColoniesMesh> getDefaultMesh() {
        return EpicColoniesMeshes.HUMANOID_RAIDER;
    }
}

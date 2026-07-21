package net.kenji.epic_colonies.tab;

import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.item.EpicColoniesItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EpicColoniesTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EpicColonies.MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WOH_TAB = CREATIVE_MODE_TABS.register(EpicColonies.MODID, () -> CreativeModeTab.builder()
            .title(Component.translatable("creativetab.epic_colonies"))
            .icon(() -> new ItemStack(EpicColoniesItems.IRON_DUAL_SWORDS.get()))
            .displayItems((itemDisplayParameters, output) -> {
                output.accept(EpicColoniesItems.WOODEN_DUAL_SWORDS.get());
                output.accept(EpicColoniesItems.STONE_DUAL_SWORDS.get());
                output.accept(EpicColoniesItems.IRON_DUAL_SWORDS.get());
                output.accept(EpicColoniesItems.DIAMOND_DUAL_SWORDS.get());
                output.accept(EpicColoniesItems.NETHERITE_DUAL_SWORDS.get());

                output.accept(EpicColoniesItems.WOODEN_DUAL_DAGGERS.get());
                output.accept(EpicColoniesItems.STONE_DUAL_DAGGERS.get());
                output.accept(EpicColoniesItems.IRON_DUAL_DAGGERS.get());
                output.accept(EpicColoniesItems.DIAMOND_DUAL_DAGGERS.get());
                output.accept(EpicColoniesItems.NETHERITE_DUAL_DAGGERS.get());

            })
            .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
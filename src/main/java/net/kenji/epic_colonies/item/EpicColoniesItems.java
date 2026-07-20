package net.kenji.epic_colonies.item;

import net.kenji.epic_colonies.EpicColonies;
import net.kenji.epic_colonies.item.custom.DualWeaponItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EpicColoniesItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, EpicColonies.MODID);
    public static final RegistryObject<Item> WOODEN_DUAL_SWORDS = ITEMS.register("wooden_dual_swords", () -> new DualWeaponItem(Tiers.WOOD, 0, 0,new Item.Properties()) {
    });
    public static final RegistryObject<Item> STONE_DUAL_SWORDS = ITEMS.register("stone_dual_swords", () -> new DualWeaponItem(Tiers.STONE, 0, 0,new Item.Properties()) {
    });
    public static final RegistryObject<Item> IRON_DUAL_SWORDS = ITEMS.register("iron_dual_swords", () -> new DualWeaponItem(Tiers.IRON, 0, 0,new Item.Properties()) {
    });
    public static final RegistryObject<Item> DIAMOND_DUAL_SWORDS = ITEMS.register("diamond_dual_swords", () -> new DualWeaponItem(Tiers.DIAMOND, 0, 0,new Item.Properties()) {
    });
    public static final RegistryObject<Item> NETHERITE_DUAL_SWORDS = ITEMS.register("netherite_dual_swords", () -> new DualWeaponItem(Tiers.NETHERITE, 0, 0,new Item.Properties()) {
    });
    public static final RegistryObject<Item> WOODEN_DUAL_DAGGERS = ITEMS.register("wooden_dual_daggers", () -> new DualWeaponItem(Tiers.WOOD, 0, 0,new Item.Properties()) {
    });
    public static final RegistryObject<Item> STONE_DUAL_DAGGERS = ITEMS.register("stone_dual_daggers", () -> new DualWeaponItem(Tiers.STONE, 0, 0,new Item.Properties()) {
    });
    public static final RegistryObject<Item> IRON_DUAL_DAGGERS = ITEMS.register("iron_dual_daggers", () -> new DualWeaponItem(Tiers.IRON, 0, 0,new Item.Properties()) {
    });
    public static final RegistryObject<Item> DIAMOND_DUAL_DAGGERS = ITEMS.register("diamond_dual_daggers", () -> new DualWeaponItem(Tiers.DIAMOND, 0, 0,new Item.Properties()) {
    });
    public static final RegistryObject<Item> NETHERITE_DUAL_DAGGERS = ITEMS.register("netherite_dual_daggers", () -> new DualWeaponItem(Tiers.NETHERITE, 0, 0,new Item.Properties()) {
    });

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}

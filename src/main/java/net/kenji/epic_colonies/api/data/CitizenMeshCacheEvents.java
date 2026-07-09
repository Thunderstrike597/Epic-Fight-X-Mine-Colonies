package net.kenji.epic_colonies.api.data;

import net.kenji.epic_colonies.EpicColonies;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EpicColonies.MODID, value = Dist.CLIENT)
public class CitizenMeshCacheEvents {
    private static int saveTimer = 0;

    @SubscribeEvent
    public static void onLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        CitizenMeshCache.load();
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        CitizenMeshCache.save();
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        if (++saveTimer >= 600) { // every 30s at 20 tps
            saveTimer = 0;
            CitizenMeshCache.save(); // no-op if not dirty
        }
    }
}
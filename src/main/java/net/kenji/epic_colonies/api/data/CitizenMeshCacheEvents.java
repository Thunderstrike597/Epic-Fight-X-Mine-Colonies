package net.kenji.epic_colonies.api.data;

import net.kenji.epic_colonies.EpicColonies;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = EpicColonies.MODID, value = Dist.CLIENT)
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
    public static void onClientTick(ClientTickEvent.Post event) {
        if (++saveTimer >= 600) { // every 30s at 20 tps
            saveTimer = 0;
            CitizenMeshCache.save(); // no-op if not dirty
        }
    }
}
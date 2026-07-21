package net.kenji.epic_colonies.api.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.minecolonies.api.colony.jobs.registry.IJobRegistry;
import com.minecolonies.api.colony.jobs.registry.JobEntry;
import net.kenji.epic_colonies.EpicColonies;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.loading.FMLPaths;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class CitizenMeshCache {
    private static final Gson GSON = new GsonBuilder().create();
    private static final Path CACHE_FILE = FMLPaths.GAMEDIR.get().resolve("epiccolonies").resolve("citizen_mesh_cache.json");
    private static final Map<UUID, Entry> CACHE = new ConcurrentHashMap<>();
    private static volatile boolean dirty = false;

    private CitizenMeshCache() {}

    public record Entry(boolean isChild, @Nullable String jobId, @Nullable String skinTextureId) {}

    /** Call on world/server join, before any citizens render. */
    public static void load() {
        CACHE.clear();
        if (!Files.exists(CACHE_FILE)) return;

        try (Reader reader = Files.newBufferedReader(CACHE_FILE)) {
            Type type = new TypeToken<Map<UUID, Entry>>() {}.getType();
            Map<UUID, Entry> loaded = GSON.fromJson(reader, type);
            if (loaded != null) CACHE.putAll(loaded);
        } catch (IOException e) {
            EpicColonies.LOGGER.warn("Failed to load citizen mesh cache", e);
        }
    }

    /** Call on disconnect and periodically (e.g. every 30s) - never every frame. */
    public static void save() {
        if (!dirty) return;

        try {
            Files.createDirectories(CACHE_FILE.getParent());
            try (Writer writer = Files.newBufferedWriter(CACHE_FILE)) {
                GSON.toJson(CACHE, writer);
            }
            dirty = false;
        } catch (IOException e) {
            EpicColonies.LOGGER.warn("Failed to save citizen mesh cache", e);
        }
    }

    public static void put(UUID citizenId, boolean isChild, @Nullable JobEntry job, @Nullable String textureId) {
        String jobId = job == null ? null : job.getKey().toString();
        Entry entry = new Entry(isChild, jobId, textureId);
        Entry old = CACHE.put(citizenId, entry);


        if (!entry.equals(old)) {
            dirty = true;
        }
    }

    @Nullable
    public static Entry get(UUID citizenId) {
        return CACHE.get(citizenId);
    }

    @Nullable
    public static JobEntry resolveJob(@Nullable String jobId) {
        if (jobId == null) return null;
        return IJobRegistry.getInstance().get(ResourceLocation.parse(jobId));
    }
    @Nullable
    public static ResourceLocation resolveTextLocation(@Nullable String jobId) {
        if (!isValidTexture(jobId)) return null;
        return ResourceLocation.parse(jobId);
    }

    public static boolean isValidTexture(@Nullable String textureStr) {
        return textureStr != null
                && textureStr.contains(":")           // rules out bare 0 (no namespace separator)
                && !textureStr.endsWith(":0")          // rules out minecraft:0 specifically
                && !textureStr.equals("minecraft:0");
    }
}
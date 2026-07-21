package net.kenji.epic_colonies;

import com.minecolonies.api.colony.jobs.ModJobs;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class EpicColoniesConfigClient {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static ModConfigSpec.ConfigValue<Boolean> USE_CITIZEN_SKIN_DETECTION;

    public static ModConfigSpec.ConfigValue<Boolean> HIDE_CITIZEN_HELMET;
    public static ModConfigSpec.ConfigValue<Boolean> JOB_ONLY_ARMOR;
    public static ModConfigSpec.ConfigValue<List<? extends String>> VISIBLE_ARMOR_JOBS;
    public static ModConfigSpec.ConfigValue<Double> JOG_PLAYBACK_SPEED_MIN;
    public static ModConfigSpec.ConfigValue<Double> JOG_PLAYBACK_SPEED_MAX;


   private static List<ResourceLocation> defaultArmorJobsLoc = new ArrayList<>();
    private static List<String> defaultArmorJobs = new ArrayList<>();

    static {
        defaultArmorJobsLoc.add(ModJobs.KNIGHT_ID);
        defaultArmorJobsLoc.add(ModJobs.KNIGHT_TRAINING_ID);
        defaultArmorJobsLoc.add(ModJobs.ARCHER_ID);
        defaultArmorJobsLoc.add(ModJobs.ARCHER_TRAINING_ID);

        for(ResourceLocation loc : defaultArmorJobsLoc){
            defaultArmorJobs.add(loc.toString());
        }
    }

    static {
        BUILDER.push("Cosmetic");

        USE_CITIZEN_SKIN_DETECTION = BUILDER
                .comment("Should Citizens Use The Skin Detection For Citizens With Lower Eye Textures (Such as some which are part of MineColonies 'Patreon Exclusive' Packs)")
                .define("Use Citizen Skin Detection", true);
        HIDE_CITIZEN_HELMET = BUILDER
                .comment("Should Citizens With Visible Armor Have Their Helmet Hidden")
                .define("Hide Citizen Helmet", false);
        JOB_ONLY_ARMOR = BUILDER
                .comment("Whether or not Armor Should Only Be Shown For Specific Citizen Jobs (Jobs Defined In Config Below)")
                .define("Show Armor For Specific Jobs", true);

        VISIBLE_ARMOR_JOBS = BUILDER
                .comment("Which Citizen Jobs Should Have Visible Armor")
                .defineList("Visible Armor Jobs", defaultArmorJobs, o -> o instanceof String);

        BUILDER.pop();
        BUILDER.push("Animations");
        JOG_PLAYBACK_SPEED_MIN = BUILDER
                .comment("The minimum speed of he citizen jog animation according to their current movement speed")
                .defineInRange("Jog Animation Min Speed", 1F, 0.01F, 10.0F);
        JOG_PLAYBACK_SPEED_MAX = BUILDER
                .comment("The maximum speed of he citizen jog animation according to their current movement speed")
                .defineInRange("Jog Animation Max Speed", 3.25F, 0.01F, 10.0F);


        SPEC = BUILDER.build();
    }
}
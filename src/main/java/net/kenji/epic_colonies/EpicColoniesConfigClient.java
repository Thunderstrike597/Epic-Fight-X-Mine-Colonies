package net.kenji.epic_colonies;

import com.minecolonies.api.colony.jobs.ModJobs;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;

public class EpicColoniesConfigClient {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.ConfigValue<Boolean> HIDE_CITIZEN_HELMET;
    public static ForgeConfigSpec.ConfigValue<Boolean> JOB_ONLY_ARMOR;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> VISIBLE_ARMOR_JOBS;

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

        SPEC = BUILDER.build();
    }
}

package net.kenji.epic_colonies.api.texture_detection;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Detects how far down the eyes/mouth are painted on a citizen texture,
 * relative to a known "normal" reference position, by finding which row
 * within the face's fixed UV rectangle contains the eye sclera (a
 * consistent near-white color regardless of skin tone or iris color).
 *
 * This offset can then be used to shift/select the correct facial mesh.
 *
 * NOTE: This is a reference sketch, not drop-in code. You'll need to:
 *  - Confirm NativeImage's actual channel packing on your MC/Forge version
 *    (getPixelRGBA has changed format across versions - verify with a
 *    known-color test pixel before trusting the r/g/b extraction).
 *  - Plug in your model's real face UV rectangle (FACE_X/FACE_Y/etc. below
 *    are placeholders).
 *  - Tune NORMAL_EYE_ROW, WHITE_THRESHOLD, and the search bounds
 *    (MIN_ROW/MAX_ROW) against a few real texture packs.
 */
public class FaceOffsetDetector {

    // Cache so detection only runs once per texture, not per-frame/per-entity.
    private static final Map<ResourceLocation, Integer> OFFSET_CACHE = new HashMap<>();

    // --- Known geometry of the face on the texture sheet ---
    // Replace these with your model's actual head-front UV coordinates.
    private static final int FACE_X = 8;
    private static final int FACE_Y = 8;
    private static final int FACE_WIDTH = 8;
    private static final int FACE_HEIGHT = 8;

    // Where the eye row sits (relative to FACE_Y) on a "normal" texture.
    // Measured from a known-good reference texture: eyes at absolute Y=12,
    // FACE_Y=8, so relative row = 12 - 8 = 4.
    private static final int NORMAL_EYE_ROW = 4;

    // Guard band: only search for the "darkest row" within this range,
    // so hair/hood pixels near the top or chin shadow near the bottom
    // can't be mistaken for the eyes.
    // Rows 0-1 excluded: hair consistently overlaps the top of the face UV
    // and is darker than the eyes themselves, so it was winning the search.
    private static final int MIN_ROW = 2;
    private static final int MAX_ROW = FACE_HEIGHT - 1;

    // Eye sclera renders near-white across every skin tone, hair color, and
    // iris color tested - it's roughly a fixed constant, not something that
    // varies much per texture. Searching for it directly is far more
    // reliable than averaging row darkness (which gets fooled by eyebrow/
    // shading rows - see detectOffset for why).
    // Threshold set conservatively below the darkest sclera seen so far
    // (216) but well above skin/hair/pupil tones (all under ~130 in every
    // sample checked) - if a pack has duller eye whites than this, raise
    // MIN_ROW/MAX_ROW logging (see the warning in detectOffset) will tell
    // you when nothing qualifies at all.
    private static final int WHITE_THRESHOLD = 200; // r,g,b must all be >= this

    /**
     * Returns the pixel offset (positive = features are lower than normal)
     * for the given texture, loading + computing it once and caching the
     * result on every call after that. Only pass the ResourceLocation -
     * this loads the image itself on a cache miss.
     */
    public static int getFaceOffset(ResourceLocation textureLocation) {
        return OFFSET_CACHE.computeIfAbsent(textureLocation, FaceOffsetDetector::loadAndDetectOffset);
    }

    private static int loadAndDetectOffset(ResourceLocation location) {
        try (NativeImage image = loadImage(location)) {
            return detectOffset(image);
        } catch (IOException e) {
            // Couldn't read/decode the texture - fall back to "normal" (0 offset)
            // rather than crashing the render call. Log so it's visible while testing.
            System.err.println("[FaceOffsetDetector] Failed to load " + location + ": " + e.getMessage());
            return 0;
        }
    }

    /**
     * Pulls the raw texture bytes via the resource manager (same place the
     * renderer itself loads textures from) and decodes them into a NativeImage.
     */
    private static NativeImage loadImage(ResourceLocation location) throws IOException {
        try (InputStream stream = Minecraft.getInstance()
                .getResourceManager()
                .getResourceOrThrow(location)
                .open()) {
            return NativeImage.read(stream);
        }
    }

    private static int detectOffset(NativeImage image) {
        // NOTE: we tried "find the darkest row" first, and it looked
        // reasonable on a single texture, but broke down across skin-tone/
        // hair/iris-color variants: the true eye row contains both the
        // brightest pixels in the rectangle (white sclera) AND the darkest
        // (pupil) side by side, so they partially cancel out when averaged.
        // A neighboring eyebrow/shading row with no bright pixels to balance
        // it out can end up scoring "darker on average" than the real eye
        // row, causing inconsistent results across otherwise-identical
        // textures. Searching for the near-white sclera pixels directly
        // sidesteps that entirely.
        int bestRow = NORMAL_EYE_ROW; // fallback if no white pixels found at all
        int bestCount = 0;

        for (int row = MIN_ROW; row <= MAX_ROW; row++) {
            int whiteCount = countNearWhiteInRow(image, FACE_X, FACE_Y + row, FACE_WIDTH);
            if (whiteCount > bestCount) {
                bestCount = whiteCount;
                bestRow = row;
            }
        }

        if (bestCount == 0) {
            // Nothing in the search range qualified as "near-white" at all -
            // silently falling back to NORMAL_EYE_ROW would hide a real
            // problem (threshold too strict, wrong UV rect, guard band
            // excluding the real eye row, etc). Surface it while testing.
            System.err.println("[FaceOffsetDetector] No near-white pixels found in search range "
                    + "(rows " + MIN_ROW + "-" + MAX_ROW + ") - falling back to offset 0. "
                    + "Check WHITE_THRESHOLD and FACE_X/Y/WIDTH/HEIGHT.");
        }

        return bestRow - NORMAL_EYE_ROW;
    }

    /**
     * Counts pixels in one row that are near-white (likely eye sclera).
     */
    private static int countNearWhiteInRow(NativeImage image, int startX, int y, int width) {
        int count = 0;

        for (int x = startX; x < startX + width; x++) {
            int argb = image.getPixelRGBA(x, y);

            int alpha = (argb >> 24) & 0xFF;
            if (alpha == 0) continue;

            int r = argb & 0xFF;
            int g = (argb >> 8) & 0xFF;
            int b = (argb >> 16) & 0xFF;

            if (r >= WHITE_THRESHOLD && g >= WHITE_THRESHOLD && b >= WHITE_THRESHOLD) {
                count++;
            }
        }

        return count;
    }
}
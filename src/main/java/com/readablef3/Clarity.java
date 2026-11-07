package com.readablef3;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Mod(
        modid = Clarity.MODID,
        name = Clarity.NAME,
        version = Clarity.VERSION,
        clientSideOnly = true,
        acceptedMinecraftVersions = "[1.8.9]",
        guiFactory = "com.readablef3.ClarityGuiFactory"
)
public class Clarity {

    public static final String MODID = "clarity";
    public static final String NAME = "clarity";
    public static final String VERSION = "1.0.0";

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        ClarityConfig.load(event.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClarityKeybinds.register();

        MinecraftForge.EVENT_BUS.register(new ClarityOverlay());

        FMLCommonHandler.instance().bus().register(new ClarityKeybinds());
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (Clarity.MODID.equals(event.modID)) {
            ClarityConfig.syncFromConfig();
        }
    }
}

class ClarityConfig {

    public static final String CATEGORY_GENERAL = "general";
    public static final String CATEGORY_LINES = "lines";

    public static Configuration config;

    public static boolean enabled;
    public static boolean hideWhenVanillaF3Open;

    public static boolean showFps;
    public static boolean showCoordinates;
    public static boolean showBlockPosition;
    public static boolean showChunkPosition;
    public static boolean showFacing;
    public static boolean showYawPitch;
    public static boolean showMovementSpeed;
    public static boolean showBiome;
    public static boolean showLight;
    public static boolean showDimension;
    public static boolean showWorldTime;
    public static boolean showLookingAt;
    public static boolean showEntityCount;
    public static boolean showMemory;
    public static boolean showJavaVersion;
    public static boolean showDisplaySize;

    public static void load(File file) {
        config = new Configuration(file);

        try {
            config.load();
            readValues();
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    public static void syncFromConfig() {
        if (config == null) {
            return;
        }

        readValues();

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void save() {
        if (config == null) {
            return;
        }

        config.get(CATEGORY_GENERAL, "enabled", true).set(enabled);
        config.get(CATEGORY_GENERAL, "hideWhenVanillaF3Open", true).set(hideWhenVanillaF3Open);

        config.get(CATEGORY_LINES, "showFps", true).set(showFps);
        config.get(CATEGORY_LINES, "showCoordinates", true).set(showCoordinates);
        config.get(CATEGORY_LINES, "showBlockPosition", true).set(showBlockPosition);
        config.get(CATEGORY_LINES, "showChunkPosition", true).set(showChunkPosition);
        config.get(CATEGORY_LINES, "showFacing", true).set(showFacing);
        config.get(CATEGORY_LINES, "showYawPitch", true).set(showYawPitch);
        config.get(CATEGORY_LINES, "showMovementSpeed", true).set(showMovementSpeed);
        config.get(CATEGORY_LINES, "showBiome", true).set(showBiome);
        config.get(CATEGORY_LINES, "showLight", true).set(showLight);
        config.get(CATEGORY_LINES, "showDimension", true).set(showDimension);
        config.get(CATEGORY_LINES, "showWorldTime", false).set(showWorldTime);
        config.get(CATEGORY_LINES, "showLookingAt", true).set(showLookingAt);
        config.get(CATEGORY_LINES, "showEntityCount", false).set(showEntityCount);
        config.get(CATEGORY_LINES, "showMemory", false).set(showMemory);
        config.get(CATEGORY_LINES, "showJavaVersion", false).set(showJavaVersion);
        config.get(CATEGORY_LINES, "showDisplaySize", false).set(showDisplaySize);

        config.save();
    }

    private static void readValues() {
        enabled = config.getBoolean(
                "enabled",
                CATEGORY_GENERAL,
                true,
                "Master toggle for the clarity overlay. This can also be toggled with the clarity keybind."
        );

        hideWhenVanillaF3Open = config.getBoolean(
                "hideWhenVanillaF3Open",
                CATEGORY_GENERAL,
                true,
                "Hide the clarity overlay while vanilla F3 debug info is open."
        );

        showFps = config.getBoolean("showFps", CATEGORY_LINES, true, "Show FPS.");
        showCoordinates = config.getBoolean("showCoordinates", CATEGORY_LINES, true, "Show precise XYZ coordinates.");
        showBlockPosition = config.getBoolean("showBlockPosition", CATEGORY_LINES, true, "Show integer block position.");
        showChunkPosition = config.getBoolean("showChunkPosition", CATEGORY_LINES, true, "Show chunk position.");
        showFacing = config.getBoolean("showFacing", CATEGORY_LINES, true, "Show facing direction.");
        showYawPitch = config.getBoolean("showYawPitch", CATEGORY_LINES, true, "Show yaw and pitch.");
        showMovementSpeed = config.getBoolean("showMovementSpeed", CATEGORY_LINES, true, "Show movement speed.");
        showBiome = config.getBoolean("showBiome", CATEGORY_LINES, true, "Show biome.");
        showLight = config.getBoolean("showLight", CATEGORY_LINES, true, "Show block and sky light.");
        showDimension = config.getBoolean("showDimension", CATEGORY_LINES, true, "Show dimension.");
        showWorldTime = config.getBoolean("showWorldTime", CATEGORY_LINES, false, "Show world time.");
        showLookingAt = config.getBoolean("showLookingAt", CATEGORY_LINES, true, "Show looked-at block or entity.");
        showEntityCount = config.getBoolean("showEntityCount", CATEGORY_LINES, false, "Show loaded entity count.");
        showMemory = config.getBoolean("showMemory", CATEGORY_LINES, false, "Show JVM memory usage.");
        showJavaVersion = config.getBoolean("showJavaVersion", CATEGORY_LINES, false, "Show Java version.");
        showDisplaySize = config.getBoolean("showDisplaySize", CATEGORY_LINES, false, "Show display size.");
    }
}

class ClarityKeybinds {

    private static final String CATEGORY = "clarity";

    private static KeyBinding toggleDisplay;

    public static void register() {
        toggleDisplay = new KeyBinding(
                "Toggle clarity display",
                Keyboard.KEY_NONE,
                CATEGORY
        );

        ClientRegistry.registerKeyBinding(toggleDisplay);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        boolean changed = false;

        while (toggleDisplay.isPressed()) {
            ClarityConfig.enabled = !ClarityConfig.enabled;
            changed = true;
        }

        if (changed) {
            ClarityConfig.save();
        }
    }
}

class ClarityOverlay {

    private static final int F3_TEXT_COLOR = 0xE0E0E0;
    private static final int START_X = 2;
    private static final int START_Y = 2;

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }

        if (!ClarityConfig.enabled) {
            return;
        }

        Minecraft mc = Minecraft.getMinecraft();

        if (mc == null || mc.thePlayer == null || mc.theWorld == null) {
            return;
        }

        if (mc.gameSettings.hideGUI) {
            return;
        }

        if (ClarityConfig.hideWhenVanillaF3Open && mc.gameSettings.showDebugInfo) {
            return;
        }

        List<String> lines = buildLines(mc);
        FontRenderer font = mc.fontRendererObj;

        int y = START_Y;

        for (String line : lines) {
            font.drawStringWithShadow(line, START_X, y, F3_TEXT_COLOR);
            y += font.FONT_HEIGHT;
        }
    }

    private List<String> buildLines(Minecraft mc) {
        List<String> lines = new ArrayList<String>();

        EntityPlayer player = mc.thePlayer;
        World world = mc.theWorld;
        BlockPos playerPos = new BlockPos(player.posX, player.posY, player.posZ);

        if (ClarityConfig.showFps) {
            lines.add("FPS: " + Minecraft.getDebugFPS());
        }

        if (ClarityConfig.showCoordinates) {
            lines.add(String.format(
                    Locale.US,
                    "XYZ: %d | %d | %d",
                    Math.round(player.posX),
                    Math.round(player.posY),
                    Math.round(player.posZ)
            ));
        }


        if (ClarityConfig.showBlockPosition) {
            lines.add(String.format(
                    Locale.US,
                    "Block: %d %d %d",
                    playerPos.getX(),
                    playerPos.getY(),
                    playerPos.getZ()
            ));
        }

        if (ClarityConfig.showChunkPosition) {
            int chunkX = playerPos.getX() >> 4;
            int chunkZ = playerPos.getZ() >> 4;

            int localX = playerPos.getX() & 15;
            int localY = playerPos.getY() & 15;
            int localZ = playerPos.getZ() & 15;

            lines.add(String.format(
                    Locale.US,
                    "Chunk: %d %d %d in %d %d",
                    localX,
                    localY,
                    localZ,
                    chunkX,
                    chunkZ
            ));
        }

        if (ClarityConfig.showFacing) {
            lines.add("Facing: " + humanFacing(player.getHorizontalFacing()));
        }

        if (ClarityConfig.showYawPitch) {
            float wrappedYaw = MathHelper.wrapAngleTo180_float(player.rotationYaw);
            float yaw360 = normalizeYawTo360(player.rotationYaw);

            lines.add(String.format(
                    Locale.US,
                    "Rotation: yaw %.1f / pitch %.1f",
                    wrappedYaw,
                    player.rotationPitch
            ));

            lines.add(String.format(
                    Locale.US,
                    "Yaw 0-360: %.1f",
                    yaw360
            ));
        }

        if (ClarityConfig.showMovementSpeed) {
            double dx = player.posX - player.prevPosX;
            double dy = player.posY - player.prevPosY;
            double dz = player.posZ - player.prevPosZ;

            double horizontalBlocksPerSecond = Math.sqrt(dx * dx + dz * dz) * 20.0D;
            double totalBlocksPerSecond = Math.sqrt(dx * dx + dy * dy + dz * dz) * 20.0D;
            double verticalBlocksPerSecond = dy * 20.0D;

            lines.add(String.format(
                    Locale.US,
                    "Speed: %.2f blocks/s horizontal, %.2f blocks/s total",
                    horizontalBlocksPerSecond,
                    totalBlocksPerSecond
            ));

            lines.add(String.format(
                    Locale.US,
                    "Vertical Speed: %.2f blocks/s",
                    verticalBlocksPerSecond
            ));
        }

        if (ClarityConfig.showBiome) {
            lines.add(world.getBiomeGenForCoords(playerPos).biomeName);
        }

        if (ClarityConfig.showLight) {
            int blockLight = world.getLightFor(EnumSkyBlock.BLOCK, playerPos);
            int skyLight = world.getLightFor(EnumSkyBlock.SKY, playerPos);

            lines.add("Light: block " + blockLight + ", sky " + skyLight);
        }

        if (ClarityConfig.showDimension) {
            lines.add("Dimension: " + world.provider.getDimensionName() + " [" + world.provider.getDimensionId() + "]");
        }

        if (ClarityConfig.showWorldTime) {
            long totalTime = world.getTotalWorldTime();
            long dayTime = world.getWorldTime() % 24000L;

            lines.add("World Time: " + dayTime + " / total " + totalTime);
        }

        if (ClarityConfig.showLookingAt) {
            addLookingAtLines(mc, lines);
        }

        if (ClarityConfig.showEntityCount) {
            lines.add("Loaded Entities: " + world.loadedEntityList.size());
        }

        if (ClarityConfig.showMemory) {
            Runtime runtime = Runtime.getRuntime();

            long max = runtime.maxMemory();
            long total = runtime.totalMemory();
            long free = runtime.freeMemory();
            long used = total - free;

            long usedMb = bytesToMiB(used);
            long maxMb = bytesToMiB(max);

            int percent = max > 0L ? (int) ((used * 100L) / max) : 0;

            lines.add("Memory: " + usedMb + " / " + maxMb + " MiB (" + percent + "%)");
        }

        if (ClarityConfig.showJavaVersion) {
            lines.add("Java: " + System.getProperty("java.version"));
        }

        if (ClarityConfig.showDisplaySize) {
            lines.add("Display: " + mc.displayWidth + "x" + mc.displayHeight);
        }

        return lines;
    }

    private static void addLookingAtLines(Minecraft mc, List<String> lines) {
        MovingObjectPosition target = mc.objectMouseOver;

        if (target == null || target.typeOfHit == null) {
            lines.add("Looking At: nothing");
            return;
        }

        if (target.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            BlockPos hitPos = target.getBlockPos();

            if (hitPos == null) {
                lines.add("Looking At Block: unknown");
                return;
            }

            Block block = mc.theWorld.getBlockState(hitPos).getBlock();
            ResourceLocation registryName = Block.blockRegistry.getNameForObject(block);

            String blockName = block.getLocalizedName();
            String id = registryName == null ? "unknown" : registryName.toString();

            lines.add(String.format(
                    Locale.US,
                    "Looking At Block: %s [%s] at %d %d %d",
                    blockName,
                    id,
                    hitPos.getX(),
                    hitPos.getY(),
                    hitPos.getZ()
            ));

            if (target.sideHit != null) {
                lines.add("Looking At Side: " + humanFacing(target.sideHit));
            }

            return;
        }

        if (target.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY) {
            Entity entity = target.entityHit;

            if (entity == null) {
                lines.add("Looking At Entity: unknown");
                return;
            }

            lines.add("Looking At Entity: " + entity.getName() + " [" + entity.getClass().getSimpleName() + "]");
            return;
        }

        if (target.typeOfHit == MovingObjectPosition.MovingObjectType.MISS) {
            lines.add("Looking At: nothing");
        }
    }

    private static String humanFacing(EnumFacing facing) {
        if (facing == null) {
            return "Unknown";
        }

        switch (facing) {
            case NORTH:
                return "North (-Z)";
            case SOUTH:
                return "South (+Z)";
            case WEST:
                return "West (-X)";
            case EAST:
                return "East (+X)";
            case UP:
                return "Up (+Y)";
            case DOWN:
                return "Down (-Y)";
            default:
                return facing.getName();
        }
    }

    private static float normalizeYawTo360(float yaw) {
        float normalized = yaw % 360.0F;

        if (normalized < 0.0F) {
            normalized += 360.0F;
        }

        return normalized;
    }

    private static long bytesToMiB(long bytes) {
        return bytes / 1024L / 1024L;
    }
}
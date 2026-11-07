# clarity

A small client-side Forge mod for **Minecraft 1.8.9** that shows a clean, human-readable debug overlay instead of the cluttered vanilla F3 screen.

## Features

Each line can be toggled individually in the in-game config (Mods → clarity → Config):

| On by default | Off by default |
| --- | --- |
| FPS | World time |
| Coordinates | Loaded entity count |
| Block position | JVM memory usage |
| Chunk position | Java version |
| Facing (e.g. `North (-Z)`) | Display size |
| Yaw / pitch | |
| Movement speed (blocks/s) | |
| Biome | |
| Block & sky light | |
| Dimension | |
| Looked-at block / entity | |

- **Toggle key:** "Toggle clarity display" (unbound by default; set it in Controls → clarity).
- The overlay hides automatically while vanilla F3 is open (configurable).

## Installing

1. Install [Forge 1.8.9](https://files.minecraftforge.net/net/minecraftforge/forge/index_1.8.9.html) (11.15.1.2318 or newer).
2. Drop `clarity-<version>.jar` into your `.minecraft/mods` folder.

## Building

Requires **JDK 8** (ForgeGradle 2.1 does not work on newer Java versions).

```sh
# point JAVA_HOME at a JDK 8 install first
./gradlew build          # jar is written to build/libs/
./gradlew runClient      # launch a dev client with the mod loaded
```

First-time setup for IDE work: `./gradlew setupDecompWorkspace`, then `./gradlew eclipse` or `./gradlew idea`.

## License

[MIT](LICENSE)

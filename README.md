# Epic Fight: Epicfied (Epic Colonies) — 1.21.1 NeoForge Port

[![NeoForge](https://img.shields.io/badge/NeoForge-21.1.241-blue.svg)](https://neoforged.net/)
[![Epic Fight](https://img.shields.io/badge/Epic%20Fight-21.17.3.1-orange.svg)](https://www.curseforge.com/minecraft/mc-mods/epic-fight-mod)
A highly polished, fully updated, and optimized **NeoForge 1.21.1** port of **EpicColonies**, bringing seamless compatibilities between **MineColonies** and **Epic Fight** to modern Minecraft!

This branch is completely updated to the **1.0.7 release**, featuring full integration of all new base class refactorings, citizen skin eye-detection features, and server-side packet safety fixes.

---

 Features

* **MineColonies Combat Epicfied**: Adds immersive, custom-tailored weapon configurations, behaviors, and combat motions to all MineColonies citizen jobs.
* **Dual Weapon Arsenal**: Fully implements Dual Swords and Dual Daggers with complete attack animations, capability registries, and custom rendering.
* **Expressive Humanoids**: Added blinking layers, eye-tracking (citizens follow you with their eyes), and emotional facial expressions depending on happiness levels.
* **Lower Eyes Detection**: Detects and adapts armatures dynamically for lower-eye child skin variants.
* **Server-Side Safety**: Fully refactored and ported packet handling to prevent common multiplayer and item-switching connection crashes.

---

  Development & Compilation

 To compile or work on this project locally, ensure you meet the requirements and follow the build steps below*

 Prerequisites
* **Java Development Kit (JDK)**: **JDK 21** (Adoptium / Eclipse Temurin is highly recommended).
* **IDE**: IntelliJ IDEA (with the Minecraft Development plugin) or Eclipse.

 Build Steps
1. Clone your fork of the repository:
   `bash
   git clone https://github.com/YOUR_GITHUB_USERNAME/EpicColonies.git
   `
[. Navigate into the prompt folder and compile the mod JAR:
   `bash
   cd EpicColonies
   ./gradlew clean build
   `
3. Locate the compiled JAR inside the output directory:
   `bash
   build/libs/epic_colonies-1.0.7.jar
   `

---

 Dependency Management (build.gradle)

In this NeoForge 1.21.1 version, all major dependencies (MineColonies, Structurize, MultiPiston, BlockUI, and Curios) are pulled dynamically from official Maven repositories. 

For optional or companion mods (like Embeddium, Oculus, JEI, and others), you can easily customize or toggle their CurseForge File IDs inside `gradle.properties`:
```poperties
# To toggle a mod off, set its File ID to 'placeholder'
embeddium_file_id=placeholder
jei_file_id=5533463
```

---

 Credits & Licensing

 **Original Mod Creator**: Developed by [Kenji](https://www.curseforge.com/members/kenji_597) / [Thunderstrike597](https://github.com/Thunderstrike597).
* **Port Maintainer**: Ported to 1.21.1 NeoForge by [ahmet](https://github.com/ahmet).
* **Code License**: All Rights Reserved (unless specified otherwise by the original author). Original assets, code logic, and resources belong to the upstream EpicColonies project.

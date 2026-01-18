# Project Context: ZombieSurvivor (Roguelike/Fishing Combat)

## Tech Stack
*   **Language:** Java 25
*   **Engine:** LibGDX (LWJGL3 backend)
*   **Build Tool:** Gradle 9.2.1
*   **Assets:** Aseprite (.png), Tiled (.tmx/.tsx)

## Current Progress (Updated Jan 18, 2026)

### Architecture
*   **Screens:** Screen-based system (`GameScreen.java`). `Main` acts as the Director.
*   **UI System:** Integrated **Scene2D**. Added `Stage`, `Skin`, and a `Table`-based layout for menus.
*   **Entities:** `Player.java` handles movement and collision.
*   **World:** `LevelManager` handles map loading, rendering, and collision/water queries.
*   **Systems:** `FishingController` manages the fishing state machine (Idle -> Waiting -> Biting).

### Features Implemented
1.  **Movement:** Smooth WASD movement + Sprint (Shift). Collision refactored to check 3 points along the leading edge.
2.  **Map:** Tiled Map loading via `LevelManager`.
3.  **Collision:** Sliding collision against `"walkable"` property.
4.  **UI Menu (TAB):** Basic inventory/skills tabs.
5.  **Fishing Logic:**
    *   Press 'E' to cast bobber (checks for `"fishable"` tile property).
    *   Wait ~6s for bite -> Press 'E' within 2s to catch.
    *   Console output for feedback (Visuals pending).

### Key Code Details
*   **Player.java:** Collision logic and movement updates. Now uses `LevelManager`.
*   **GameScreen.java:** Orchestrates Map, Player, Fishing, and Scene2D UI. 
*   **LevelManager.java:** Encapsulates TiledMap, Renderer, and layer queries (`isWalkable`, `isFishable`).
*   **FishingController.java:** State machine for fishing mechanics (Timers, States, Bobber position).

### Assets Setup
*   `assets/uiskin.json`: Skin for buttons, windows, and labels.
*   `assets/tileset.tmx`: The map file (requires `"walkable"` and `"fishable"` properties).
*   `assets/player1.png`: Player sprite.

### Next Steps / To-Do
1.  **Fishing Visuals:** Draw the bobber and "!" alert in `GameScreen`.
2.  **Inventory Data:** Complete `Item.java` and `Inventory.java` to store caught fish.
3.  **Loot Table:** Create a system to determine *what* fish is caught.
4.  **Drag and Drop:** Implement `DragAndDrop` for moving items between slots.
5.  **Combat:** Add enemies (Zombies) with basic AI.

### Notes for Next Session
*   The fishing logic works in the console. Next priority is rendering the bobber so the player can see where they cast.
*   Ensure `tileset.tsx` has `"fishable"` boolean property on water tiles.

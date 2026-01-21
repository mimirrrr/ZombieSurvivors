# Project Context: ZombieSurvivor (Roguelike/Fishing Combat)

## Tech Stack
*   **Language:** Java 25
*   **Engine:** LibGDX (LWJGL3 backend)
*   **Build Tool:** Gradle 9.2.1
*   **Assets:** Aseprite (.png), Tiled (.tmx/.tsx)

## Current Progress (Updated Jan 20, 2026)

### Architecture
*   **Screens:** Screen-based system (`GameScreen.java`). `Main` acts as the Director.
*   **UI System:** Integrated **Scene2D**. Added `Stage`, `Skin`, and a `Table`-based layout for menus.
*   **Entities:** `Player.java` handles movement and collision. Now supports 8-directional movement and spritesheet rendering.
*   **World:** `LevelManager` handles map loading, rendering, and collision/water queries.
*   **Systems:** `FishingController` manages the fishing state machine (Idle -> Waiting -> Biting).

### Features Implemented
1.  **Movement:** Smooth WASD movement + Sprint (Shift). 
    *   **8-Directional Support:** Added diagonal movement and facing logic.
    *   **Visuals:** Integrated a 256x32 spritesheet for the player (8 frames: Up, Up-Right, Right, Down-Right, Down, Down-Left, Left, Up-Left).
2.  **Map:** Tiled Map loading via `LevelManager`.
3.  **Collision:** Sliding collision against `"walkable"` property. Collision box tuned to 28x28 for better feel.
4.  **UI Menu (TAB):** Basic inventory/skills tabs.
5.  **Fishing Logic:**
    *   Press 'E' to cast bobber (checks for `"fishable"` tile property).
    *   Wait ~6s for bite -> Press 'E' within 2s to catch.
    *   Console output for feedback (Visuals pending).

### Key Code Details
*   **Player.java:** Uses `TextureRegion` to display the correct frame from a spritesheet. Input logic handles diagonal combinations.
*   **Direction.java:** Enum expanded to 8 directions.
*   **GameScreen.java:** Orchestrates Map, Player, Fishing, and Scene2D UI. 
*   **LevelManager.java:** Encapsulates TiledMap, Renderer, and layer queries (`isWalkable`, `isFishable`).
*   **FishingController.java:** State machine for fishing mechanics.

### Assets Setup
*   `assets/uiskin.json`: Skin for buttons, windows, and labels.
*   `assets/tileset.tmx`: The map file (requires `"walkable"` and `"fishable"` properties).
*   `assets/player1.png`: Player spritesheet (256x32).

### Next Steps / To-Do
1.  **Fishing Visuals:** Draw the bobber and "!" alert in `GameScreen`. Use the 8-directional `facing` to position the bobber correctly.
2.  **Inventory Data:** Complete `Item.java` and `Inventory.java` to store caught fish.
3.  **Loot Table:** Create a system to determine *what* fish is caught.
4.  **Drag and Drop:** Implement `DragAndDrop` for moving items between slots.
5.  **Combat:** Add enemies (Zombies) with basic AI.

### Notes for Next Session
*   The fishing logic works in the console. Next priority is rendering the bobber so the player can see where they cast.
*   Ensure `tileset.tsx` has `"fishable"` boolean property on water tiles.

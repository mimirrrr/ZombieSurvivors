# Project Context: ZombieSurvivor (Roguelike/Fishing Combat)

## Tech Stack
*   **Language:** Java 25
*   **Engine:** LibGDX (LWJGL3 backend)
*   **Build Tool:** Gradle 9.2.1
*   **Assets:** Aseprite (.png), Tiled (.tmx/.tsx)

## Current Progress (Updated Dec 30, 2025)

### Architecture
*   **Screens:** Screen-based system (`GameScreen.java`). `Main` acts as the Director.
*   **UI System:** Integrated **Scene2D**. Added `Stage`, `Skin`, and a `Table`-based layout for menus.
*   **Entities:** `Player.java` handles movement and collision.
*   **World:** `FitViewport` (1600x800) and `OrthographicCamera` following the player.

### Features Implemented
1.  **Movement:** Smooth WASD movement + Sprint (Shift).
2.  **Map:** Tiled Map loading and rendering.
3.  **Collision:** Sliding collision against `"walkable"` property in Tiled.
4.  **UI Menu (TAB):** 
    *   Pressing TAB toggles the UI overlay.
    *   Input focus switches between Player and UI Stage.
    *   Tabbed interface: "Inventory" and "Skills" buttons switch content.
    *   Basic 5x5 Grid layout established for the Inventory.

### Key Code Details
*   **Player.java:** Collision logic and movement updates.
*   **GameScreen.java:** Orchestrates Map, Player, and Scene2D UI. 
*   **Inventory Logic:** Designed `Item` and `Inventory` classes to decouple logic from UI.

### Assets Setup
*   `assets/uiskin.json`: Skin for buttons, windows, and labels.
*   `assets/tileset.tmx`: The map file.
*   `assets/player1.png`: Player sprite.

### Next Steps / To-Do
1.  **Inventory Data:** Complete `Item.java` and `Inventory.java`.
2.  **Drag and Drop:** Implement `DragAndDrop` for moving items between slots.
3.  **Item Interaction:** Right-click to drop items on the map.
4.  **Fishing Mechanic:** Detect water tiles and trigger fishing state.
5.  **Combat:** Add enemies (Zombies) with basic AI.

### Notes for Next Session
*   The project is fully runnable (`./gradlew lwjgl3:run`).
*   The UI system is ready to be populated with real data from the `Inventory` class.
*   Need to ensure `Item` icons are loaded from `TextureAtlas` or individual textures for the grid.

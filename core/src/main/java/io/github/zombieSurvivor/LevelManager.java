package io.github.zombieSurvivor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class LevelManager {

    //GENEROVANI MAPY
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer collisionLayer;

    private TiledMapTileLayer.Cell getCell(float x, float y) {
        if (x < 0 || y < 0) return null;
        
        int cellX = (int)(x / collisionLayer.getTileWidth());
        int cellY = (int)(y / collisionLayer.getTileHeight());
        
        if (cellX >= collisionLayer.getWidth() || cellY >= collisionLayer.getHeight()) {
            return null;
        }
        return collisionLayer.getCell(cellX, cellY);
    }

    public boolean isWalkable(float x, float y){
        TiledMapTileLayer.Cell cell = getCell(x,y);
        if(cell == null) return false;
        Object prop = cell.getTile().getProperties().get("walkable");
        return prop != null && (boolean) prop;
    }
    public boolean isFishable(float x, float y){
        TiledMapTileLayer.Cell cell = getCell(x,y);

        if(cell == null) return false;
        Object prop = cell.getTile().getProperties().get("fishable");
        return prop != null && (boolean) prop;
    }

    public LevelManager(String tilesetLocation) {
        this.map = new TmxMapLoader().load(tilesetLocation);
        this.mapRenderer = new OrthogonalTiledMapRenderer(map);
        this.collisionLayer = (TiledMapTileLayer) map.getLayers().get(0);
    }

    public TiledMap getMap() {
        return map;
    }
    public OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }
    public TiledMapTileLayer getCollisionLayer() {
        return collisionLayer;
    }

    public void render(OrthographicCamera camera) {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
    }
}

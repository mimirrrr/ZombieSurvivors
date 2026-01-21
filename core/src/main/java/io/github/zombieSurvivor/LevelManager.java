package io.github.zombieSurvivor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;

public class LevelManager {

    //GENEROVANI MAPY
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private TiledMapTileLayer baseLayer;

    private TiledMapTileLayer.Cell getCell(TiledMapTileLayer layer, float x, float y) {
        if (x < 0 || y < 0) return null;
        
        int cellX = (int)(x / layer.getTileWidth());
        int cellY = (int)(y / layer.getTileHeight());
        
        if (cellX >= layer.getWidth() || cellY >= layer.getHeight()) {
            return null;
        }
        return layer.getCell(cellX, cellY);
    }

    public boolean isWalkable(float x, float y){
        // Bounds check based on the base layer
        if (x < 0 || y < 0 || 
            x >= baseLayer.getWidth() * baseLayer.getTileWidth() || 
            y >= baseLayer.getHeight() * baseLayer.getTileHeight()) {
            return false;
        }

        for (MapLayer layer : map.getLayers()) {
            if (layer instanceof TiledMapTileLayer) {
                TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
                TiledMapTileLayer.Cell cell = getCell(tileLayer, x, y);

                if (cell != null && cell.getTile() != null) {
                    MapObjects objects = cell.getTile().getObjects();
                    if (objects.getCount() > 0) {
                        // Calculate tile world position
                        int cellX = (int) (x / tileLayer.getTileWidth());
                        int cellY = (int) (y / tileLayer.getTileHeight());
                        float tileX = cellX * tileLayer.getTileWidth();
                        float tileY = cellY * tileLayer.getTileHeight();

                        for (MapObject object : objects) {
                            if (object instanceof RectangleMapObject) {
                                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                                if (x >= tileX + rect.x && x <= tileX + rect.x + rect.width &&
                                        y >= tileY + rect.y && y <= tileY + rect.y + rect.height) {
                                    return false;
                                }
                            } else if (object instanceof PolygonMapObject) {
                                Polygon poly = ((PolygonMapObject) object).getPolygon();
                                // Create a copy or modify position temporarily (Poly is relative)
                                // Standard libgdx polygons are relative to 0,0 origin of the shape
                                float originalX = poly.getX();
                                float originalY = poly.getY();
                                
                                poly.setPosition(tileX + originalX, tileY + originalY);
                                boolean contains = poly.contains(x, y);
                                poly.setPosition(originalX, originalY); // Reset just in case shared
                                
                                if (contains) return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    public boolean isFishable(float x, float y){
        for (MapLayer layer : map.getLayers()) {
            if (layer instanceof TiledMapTileLayer) {
                TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
                TiledMapTileLayer.Cell cell = getCell(tileLayer, x, y);
                if (cell != null && cell.getTile() != null) {
                    Object prop = cell.getTile().getProperties().get("fishable");
                    if (prop != null && (boolean) prop) {
                        MapObjects objects = cell.getTile().getObjects();
                        if (objects.getCount() == 0) {
                            return true;
                        }

                        int cellX = (int) (x / tileLayer.getTileWidth());
                        int cellY = (int) (y / tileLayer.getTileHeight());
                        float tileX = cellX * tileLayer.getTileWidth();
                        float tileY = cellY * tileLayer.getTileHeight();

                        for (MapObject object : objects) {
                            if (object instanceof RectangleMapObject) {
                                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                                if (x >= tileX + rect.x && x <= tileX + rect.x + rect.width &&
                                        y >= tileY + rect.y && y <= tileY + rect.y + rect.height) {
                                    return true;
                                }
                            } else if (object instanceof PolygonMapObject) {
                                Polygon poly = ((PolygonMapObject) object).getPolygon();
                                float originalX = poly.getX();
                                float originalY = poly.getY();
                                poly.setPosition(tileX + originalX, tileY + originalY);
                                boolean contains = poly.contains(x, y);
                                poly.setPosition(originalX, originalY);
                                if (contains) return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public LevelManager(String tilesetLocation) {
        this.map = new TmxMapLoader().load(tilesetLocation);
        this.mapRenderer = new OrthogonalTiledMapRenderer(map);
        this.baseLayer = (TiledMapTileLayer) map.getLayers().get(0);
    }

    public TiledMap getMap() {
        return map;
    }
    public OrthogonalTiledMapRenderer getMapRenderer() {
        return mapRenderer;
    }
    public TiledMapTileLayer getCollisionLayer() {
        return baseLayer;
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

package io.github.zombieSurvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;

public class Player {

    private Texture texture;
    private TiledMapTileLayer collisionLayer;
    private float speed = 200f;
    private float x=400;
    private float y=800;

    private boolean isWalkable(float x, float y){
        int cellX = (int)(x/collisionLayer.getTileWidth());
        int cellY = (int)(y/collisionLayer.getTileWidth());
        TiledMapTileLayer.Cell cell = collisionLayer.getCell(cellX,cellY);
        if(cell == null) return false;
        return (boolean) cell.getTile().getProperties().get("walkable");
    }

    Player(float x, float y, String pathImage, TiledMapTileLayer collisionLayer) {
        this.x = x;
        this.y = y;
        texture = new Texture(pathImage);
        this.collisionLayer = collisionLayer;
    }
    public void update(float delta) {
        float currentSpeed = speed;
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            currentSpeed *= 2;
        }
        float nextX=x;
        float nextY=y;

        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            nextX-= delta*currentSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            nextX+= delta*currentSpeed;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            nextY+= delta*currentSpeed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            nextY-= delta*currentSpeed;
        }

        if(isWalkable(nextX, y)){
            x=nextX;
        }
        if(isWalkable(x, nextY)){
            y=nextY;
        }

    }
    public void render(SpriteBatch batch){
        batch.draw(texture, x - texture.getWidth()/2f, y - texture.getHeight()/2f);
    }
    public void dispose() {
        texture.dispose();
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
}

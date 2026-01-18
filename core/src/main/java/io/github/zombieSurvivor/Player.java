package io.github.zombieSurvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;

public class Player {
    private Texture texture;
    private LevelManager levelManager;
    private Direction facing = Direction.UP;
    private float speed = 200f;
    private float x;
    private float y;
    private float collisionWidth = 62f;
    private float collisionHeight = 94f;

    Player(float x, float y, String pathImage, LevelManager levelManager) {
        this.x = x;
        this.y = y;
        texture = new Texture(pathImage);
        this.levelManager = levelManager;
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
            facing = Direction.LEFT;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            nextX+= delta*currentSpeed;
            facing = Direction.RIGHT;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            nextY+= delta*currentSpeed;
            facing = Direction.UP;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            nextY-= delta*currentSpeed;
            facing = Direction.DOWN;
        }

        // Check Horizontal (Left/Right)
        if (nextX != x) {
            float checkX = (nextX > x) ? nextX + collisionWidth/2 : nextX - collisionWidth/2;
            // Check top, middle, and bottom to ensure we don't straddle a tile
            if(levelManager.isWalkable(checkX, y - collisionHeight/2) &&
               levelManager.isWalkable(checkX, y) &&
               levelManager.isWalkable(checkX, y + collisionHeight/2)){
                x = nextX;
            }
        }

        // Check Vertical (Up/Down)
        if (nextY != y) {
            float checkY = (nextY > y) ? nextY + collisionHeight/2 : nextY - collisionHeight/2;
            // Check left, middle, and right
            if(levelManager.isWalkable(x - collisionWidth/2, checkY) &&
               levelManager.isWalkable(x, checkY) &&
               levelManager.isWalkable(x + collisionWidth/2, checkY)){
                y = nextY;
            }
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
    public Direction getFacing() {
        return facing;
    }
}

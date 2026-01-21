package io.github.zombieSurvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player {
    private Texture texture;
    private TextureRegion[] frames;
    private LevelManager levelManager;
    private Direction facing = Direction.UP;
    private float speed = 200f;
    private float x;
    private float y;
    private float collisionWidth = 28f;
    private float collisionHeight = 28f;

    Player(float x, float y, String pathImage, LevelManager levelManager) {
        this.x = x;
        this.y = y;
        TextureRegion[][] tmp = TextureRegion.split(new Texture(pathImage),32,32);
        //HARDCODED 8 DIRECTIONS IN SPRITE
        frames = new TextureRegion[8];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = tmp[0][i];
        }
        this.levelManager = levelManager;
    }
    public void update(float delta) {
        float currentSpeed = speed;
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            currentSpeed *= 1.5;
        }

        boolean up = Gdx.input.isKeyPressed(Input.Keys.W);
        boolean right = Gdx.input.isKeyPressed(Input.Keys.D);
        boolean down = Gdx.input.isKeyPressed(Input.Keys.S);
        boolean left = Gdx.input.isKeyPressed(Input.Keys.A);

        if(up && right) facing = Direction.UP_RIGHT;
        else if(down && right) facing = Direction.DOWN_RIGHT;
        else if(down && left) facing = Direction.DOWN_LEFT;
        else if(up && left) facing = Direction.UP_LEFT;
        else if(up) facing = Direction.UP;
        else if(right) facing = Direction.RIGHT;
        else if(down) facing = Direction.DOWN;
        else if(left) facing = Direction.LEFT;

        float nextX=x;
        float nextY=y;

        if(up) nextY+=delta*currentSpeed;
        if(right) nextX+=delta*currentSpeed;
        if(down) nextY-=delta*currentSpeed;
        if(left) nextX-=delta*currentSpeed;

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
        TextureRegion currentFrame = frames[0];
        switch(facing){
            case UP: currentFrame = frames[0]; break;
            case UP_RIGHT: currentFrame = frames[1]; break;
            case RIGHT: currentFrame = frames[2]; break;
            case DOWN_RIGHT: currentFrame = frames[3]; break;
            case DOWN: currentFrame = frames[4]; break;
            case DOWN_LEFT: currentFrame = frames[5]; break;
            case LEFT: currentFrame = frames[6]; break;
            case UP_LEFT: currentFrame = frames[7]; break;
        }
        batch.draw(currentFrame, x - 16, y - 16);
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

package io.github.zombieSurvivor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class FishingController {
    private LevelManager levelManager;
    public enum State {IDLE, CASTING, WAITING, BITING}
    private State state=State.IDLE;
    private float timer=0;
    private Vector2 targetPosition =new Vector2();
    private Random random=new Random();
    private Vector2 currentPosition=new Vector2();


    public FishingController(LevelManager levelManager) {
        this.levelManager = levelManager;
    }

    public boolean startFishing(float playerX, float playerY, Direction direction){
        float offset = 64f; //2 tiles
        float targetX = playerX;
        float targetY = playerY;

        switch (direction){
            case UP: targetY+=offset; break;
            case UP_RIGHT: targetX+=offset; targetY+=offset; break;
            case RIGHT: targetX+=offset; break;
            case DOWN_RIGHT: targetX+=offset; targetY-=offset; break;
            case DOWN: targetY-=offset; break;
            case DOWN_LEFT: targetX-=offset; targetY-=offset; break;
            case LEFT: targetX-=offset; break;
            case UP_LEFT: targetX-=offset; targetY+=offset; break;

        }
        if(levelManager.isFishable(targetX, targetY)){
            state=State.CASTING;
            targetPosition.set(targetX, targetY);
            currentPosition.set(playerX, playerY);
        }else{
            return false;
        }
        timer = 0;
        return true;
    }

    public void update(float delta) {
        if(state==State.IDLE){
            return;
        }
        timer+=delta;
        if(state==State.CASTING){
            float speed = 5f * delta;
            currentPosition.lerp(targetPosition, speed);
            if(currentPosition.dst(targetPosition)<0.3f){
                currentPosition.set(targetPosition);
                state=State.WAITING;
            }
        }
        if(state==State.WAITING && timer>6){
            state=State.BITING;
            timer=0;
            System.out.println("FISH BIT");
            //didnt react to the fish starting to bite
        }
        if(state==State.BITING && timer>2){
            state=State.IDLE;
            timer=0;
            //didnt take fish out ot water fast enough
        }
        if(timer>=60){
            timer=0;
            state=State.IDLE;
            //fishing took too long
        }
    }
    public boolean reelFish(){
        if(state==State.BITING){
            state=State.IDLE;
            return true;
        }else if(state==State.WAITING){
            state=State.IDLE;
            return false;
        }
        return false;
    }
    public boolean isFishing(){
        return(state!=State.IDLE);
    }
    public void render(float playerX, float playerY, ShapeRenderer shapeRenderer){
        if(state==State.IDLE){
            return;
        }
        int shakingX=0;
        int shakingY=0;
        if(state == State.BITING){
            shakingX= random.nextInt(-1,1);
            shakingY= random.nextInt(-1,1);
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.line(playerX, playerY, currentPosition.x+shakingX, currentPosition.y+shakingY);
        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(currentPosition.x+shakingX, currentPosition.y+shakingY,5);
        shapeRenderer.end();
    }

    public State getState() {
        return state;
    }
}

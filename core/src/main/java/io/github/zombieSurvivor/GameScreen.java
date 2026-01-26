package io.github.zombieSurvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen implements Screen, InputProcessor {

    private FitViewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Player player;
    // XY CORDS
    private BitmapFont font;
    //GENEROVANI MAPY
    LevelManager levelManager;
    //FISHING
    FishingController fishingController;
    ShapeRenderer shapeRenderer;
    //UI COMPONENTS
    private Stage stage;
    private Skin skin;
    private boolean menuOpened=false;
    private Table contentTable;

    private void showInventory(){
        contentTable.clear();
        contentTable.add(new Label("This is your Inventory", skin));
    }
    private void showSkills(){
        contentTable.clear();
        contentTable.add(new Label("This is your skills window", skin));
    }
    private void quitGame(){
        System.out.println("Quitting game");
        Gdx.app.exit();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        font =  new BitmapFont();
        levelManager = new LevelManager("map.tmx");
        fishingController = new FishingController(levelManager);
        player = new Player(400,800, "player1.png",levelManager);
        camera = new OrthographicCamera();

        int viewportMultiplier = 60;
        viewport = new FitViewport(16* viewportMultiplier, 9* viewportMultiplier, camera);
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
        batch = new SpriteBatch();
        Table root = new Table();
        root.setFillParent(true);
        root.top();
        stage.addActor(root);
        TextButton inventory = new TextButton("Inventory", skin);
        inventory.addListener(new ClickListener() {
           public void clicked(InputEvent event, float x, float y){
               showInventory();
           }
        });
        TextButton skills = new TextButton("Skills", skin);
        skills.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                showSkills();
            }
        });
        TextButton quit = new TextButton("Quit Game", skin);
        quit.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y){
                quitGame();
            }
        });
        Table tabTable = new Table();
        tabTable.add(inventory).pad(10);
        tabTable.add(skills).pad(10);
        tabTable.add(quit).pad(10);
        contentTable = new Table();
        root.add(tabTable);
        root.row();
        stage.getRoot().setVisible(false);
        root.add(contentTable).expandY().fill().expandX().fill();
        showInventory();
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void render(float delta) {
        camera.position.set(player.getX(),player.getY(),0);
        camera.update();
        Color color = new Color(0f, 0f, 0f, 1f);
        ScreenUtils.clear(color);
        levelManager.render(camera);
        if(Gdx.input.isKeyJustPressed(Input.Keys.TAB)){
            menuOpened = !menuOpened;
            stage.getRoot().setVisible(menuOpened);
            if(menuOpened){
                Gdx.input.setInputProcessor(stage);
            }else{
                Gdx.input.setInputProcessor(this);
            }
        }
        batch.setProjectionMatrix(camera.combined);
        fishingController.update(delta);
        if(Gdx.input.isKeyJustPressed(Input.Keys.E)){
            if(!fishingController.isFishing()){
                fishingController.startFishing(player.getX(), player.getY(),player.getFacing());
            }else{
                boolean cought =  fishingController.reelFish();
                if(cought){
                    System.out.println("YOU COUGHT A FISH YEEEY :) ");
                }else{
                    System.out.println("Nothing on the hook.");
                }
            }
        }
        shapeRenderer.setProjectionMatrix(camera.combined);
        fishingController.render(player.getX(), player.getY(),shapeRenderer);
        batch.begin();

        float leftEdge = camera.position.x - (viewport.getWorldWidth() / 2 * camera.zoom);
        float topEdge = camera.position.y + (viewport.getWorldHeight() / 2 * camera.zoom);
        float padding = 10 * camera.zoom;
        font.getData().setScale(camera.zoom*0.8f);
        font.draw(batch, "State: " + fishingController.getState(), leftEdge + padding, topEdge - padding);
        font.draw(batch, "X: " + String.format("%.2f", player.getX()), leftEdge + padding, topEdge - (padding * 2));
        font.draw(batch, "Y: " + String.format("%.2f", player.getY()), leftEdge + padding, topEdge - (padding * 3));

        player.render(batch);
        batch.end();
        boolean canMove = !menuOpened && !fishingController.isFishing();
        if(canMove){
            player.update(delta);
        }
        if(menuOpened){
            stage.act(delta);
            stage.draw();
        }

    }
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
    }
    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
        stage.dispose();
        skin.dispose();
        levelManager.dispose();
        shapeRenderer.dispose();
    }

    @Override
    public boolean keyDown(int i) {return false;}
    @Override
    public boolean keyUp(int i) {return false;}
    @Override
    public boolean keyTyped(char c) {return false;}
    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {return false;}
    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {return false;}
    @Override
    public boolean touchCancelled(int i, int i1, int i2, int i3) {return false;}
    @Override
    public boolean touchDragged(int i, int i1, int i2) {return false;}
    @Override
    public boolean mouseMoved(int i, int i1) {return false;}
    @Override
    public boolean scrolled(float ammountX, float ammountY) {
        if(ammountY>0){
            if(camera.zoom<2f){
                camera.zoom+=0.1f;
            }
        }else if(ammountY<0){
            if(camera.zoom>0.5f){
                camera.zoom-=0.1f;
            }
        }
        return true;
    }
}

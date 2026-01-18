package io.github.zombieSurvivor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
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

public class GameScreen implements Screen {

    private FitViewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Player player;
    private Texture napis;
    // XY CORDS
    private BitmapFont font;
    //GENEROVANI MAPY
    LevelManager levelManager;
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


    @Override
    public void show() {
        font =  new BitmapFont();
        napis = new Texture("libgdx.png");
        levelManager = new LevelManager("tileset.tmx");

        player = new Player(400,800, "player1.png",levelManager);
        camera = new OrthographicCamera();
        viewport = new FitViewport(1040, 520, camera);
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
        Table tabTable = new Table();
        tabTable.add(inventory).pad(10);
        tabTable.add(skills).pad(10);
        contentTable = new Table();
        root.add(tabTable);
        root.row();
        stage.getRoot().setVisible(false);
        root.add(contentTable).expandY().fill().expandX().fill();
        showInventory();
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
                Gdx.input.setInputProcessor(null);
            }
        }
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        font.draw(batch, "X: " +  String.format("%.2f", player.getX()), camera.position.x-750, camera.position.y+350);
        font.draw(batch, "Y: " + String.format("%.2f", player.getY()), camera.position.x-750, camera.position.y+330);
        player.render(batch);
        batch.draw(napis, 1000, 1000);
        batch.end();
        if(menuOpened){
            stage.act(delta);
            stage.draw();
        }else{
            player.update(delta);
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
        napis.dispose();
        player.dispose();
        stage.dispose();
        skin.dispose();
        levelManager.dispose();
    }
}

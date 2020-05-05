package mainGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import mainGame.OrchardValley;
import tools.*;
import mainGame.Entity;
import time.*;

public class PlayScreen implements Screen{
    private OrchardValley game;


    //Camera variables
    OrthographicCamera cam;
    Viewport gameView;
    Vector3 tp;

    //Tiled map variables
    TiledMap map;
    OrthogonalTiledMapRenderer renderer;
    MapLoader loader;
    int mapWidth;
    int mapHeight;
    MapObject object;

    //Variables for player
    Entity player;
    TextureRegion currentPlayerFrame;
    Sprite currentPlayerSprite;
    inputClass controller;
    ShapeRenderer shapeRenderer;


    //Clock variables
    GameTimeClock clock;
    Timer_ timer;
    int currentDays;

    //Tool variables
    public Items.ItemType currentType;
    public Items currentItem;
    public Texture bucketTexture;
    public Items bucket;
    public Items tomatoSeed;
    public Items potatoSeed;
    public Items carrotSeed;
    public Items cornSeed;
    public Items artSeed;
    public Items pepperSeed;
    public Items gourdSeed;
    public int intType;

    //Crop variables
    public Array<Crop> crops;
    public int numCrops;
    private TextureRegion[][] textureFrames;
    private TextureRegion mouseFrame;
    public Texture mouseCrop;
    public Array<Seeds> seeds;

    //Variables for score
    public static Integer money;
    static Label timeLabel;
    static Label timeStringLabel;
    static Label scoreLabel;
    static Label scoreStringLabel;
    static Label coporateLabel;
    static Label goalLabel;
    static int goal;
    static Label daysLeftLabel;
    static Label daysLeftNum;
    private Viewport viewport;
    public Stage stage;
    private String time;
    private Label lose;
    private Label win;

    //Inventory
    private Array<Items> items;
    private Texture box;
    private Texture border;


    //Other
    boolean sleep;
    Dialog bedMenu;
    Skin skin;
    static int daysLeft;
    boolean showStage;
    BitmapFont font;

    public PlayScreen(OrchardValley game) {
        this.game = game;
        font = new BitmapFont();
        font.getData().setScale(.75f);
        cam = new OrthographicCamera();
        gameView = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), cam);
        cam.setToOrtho(false, gameView.getWorldWidth(), gameView.getWorldHeight());
        map = new TmxMapLoader().load("FarmLand.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        renderer.setView(cam);
        seeds = new Array<Seeds>();
        loader = new MapLoader(this);
        mapWidth = map.getProperties().get("width", Integer.class) * 32;
        mapHeight = map.getProperties().get("height", Integer.class) * 32;

        player = new Entity();
        player.startingPosition(loader.getPlayerSpawn().x, loader.getPlayerSpawn().y);
        currentPlayerSprite = player.getSprite();
        controller = new inputClass(this, player);
        shapeRenderer = new ShapeRenderer();

        crops = new Array<Crop>();

        cam.zoom = .5f;
        timer = new Timer_();
        timer.StartNew(60, true, true);
        timer.setStartTime(0,12,0,0);
        clock = new GameTimeClock(timer);
        currentDays = 0;
        numCrops = 0;
        intType = 0;
        daysLeft = 30;

        time = timer.getFormattedTimeofDay();
        mouseCrop = new Texture("plants/plants.png");
        textureFrames = TextureRegion.split(mouseCrop, 32, 64);
        tp = new Vector3();
        bucketTexture = new Texture("bucket.png");
        bucket = new Items(bucketTexture, Items.ItemType.TOOL, Items.Item.BUCKET);
        tomatoSeed = new Items(textureFrames[5][0], Items.ItemType.SEED, Items.Item.TOMATO);
        potatoSeed = new Items(textureFrames[5][1], Items.ItemType.SEED, Items.Item.POTATO);
        carrotSeed = new Items(textureFrames[5][2], Items.ItemType.SEED, Items.Item.CARROT);
        cornSeed = new Items(textureFrames[5][6], Items.ItemType.SEED, Items.Item.CORN);
        gourdSeed = new Items(textureFrames[5][5], Items.ItemType.SEED, Items.Item.GOURD);
        artSeed = new Items(textureFrames[5][3], Items.ItemType.SEED, Items.Item.ARTICHOKE);
        pepperSeed = new Items(textureFrames[5][4], Items.ItemType.SEED, Items.Item.PEPPER);

        items = new Array<Items>(9);
        items.add(bucket);
        items.add(tomatoSeed);
        items.add(potatoSeed);
        items.add(carrotSeed);
        items.add(cornSeed);
        items.add(gourdSeed);
        items.add(artSeed);
        items.add(pepperSeed);
        setMouseCrop(bucket);

        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport, game.batch);
        Table loseTable = new Table();
        Table winTable = new Table();
        Table table = new Table();
        winTable.center();
        winTable.setFillParent(true);
        loseTable.center();
        loseTable.setFillParent(true);
        table.top();
        table.setFillParent(true);
        money = 100;
        goal= OrchardValley.cashGoal;

        timeLabel = new Label(time, new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeStringLabel = new Label("Time", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel = new Label(String.format("$%d", money), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        scoreStringLabel = new Label("Money", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        coporateLabel = new Label("Bank Debt", new Label.LabelStyle(new BitmapFont(), Color.RED));
        goalLabel = new Label(String.format("$%d", goal), new Label.LabelStyle(new BitmapFont(), Color.RED));
        daysLeftLabel = new Label("Days Left", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        daysLeftNum = new Label(String.format("%d", daysLeft), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        lose = new Label("You lose! The evil bank repossessed your land!", new Label.LabelStyle(new BitmapFont(), Color.RED));
        win = new Label("You win! The evil bank doesn't get your land!", new Label.LabelStyle(new BitmapFont(), Color.GREEN));
        win.setVisible(false);
        win.setFontScale(3);
        lose.setVisible(false);
        lose.setFontScale(3);
        timeStringLabel.setFontScale(2);
        timeLabel.setFontScale(2);
        scoreStringLabel.setFontScale(2);
        scoreLabel.setFontScale(2);
        goalLabel.setFontScale(2);
        coporateLabel.setFontScale(2);
        daysLeftLabel.setFontScale(2);
        daysLeftNum.setFontScale(2);
        
        
        table.add(scoreStringLabel).padRight(100f);
        table.add(coporateLabel).padRight(100f);
        table.add(timeStringLabel).padRight(100f);
        table.add(daysLeftLabel);
        table.row();
        table.add(scoreLabel).padRight(100f);
        table.add(goalLabel).padRight(100f);
        table.add(timeLabel).padRight(100f);
        table.add(daysLeftNum);
        loseTable.add(lose);
        winTable.add(win);
        stage.addActor(table);
        stage.addActor(loseTable);
        stage.addActor(winTable);

        box = new Texture("box.png");
        border = new Texture("border.png");


        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        bedMenu = new Dialog("",skin, "dialog") {
            protected void result(Object object) {
                if (object.equals(true)) {
                    sleep = true;
                }
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        bedMenu.remove();
                        showStage = false;
                    }
                }, 0);

            }
        };
        bedMenu.text("Would you like to sleep?");
        bedMenu.button("Yes", true);
        bedMenu.button("No",false);

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(controller);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        
        if(OrchardValley.cashGoal<5000) {
        for(int i = 0; i < 5; i++)
        	addSeeds(tomatoSeed.getItem());
            addSeeds(potatoSeed.getItem());
            addSeeds(carrotSeed.getItem());
            addSeeds(cornSeed.getItem());
            addSeeds(gourdSeed.getItem());
            addSeeds(artSeed.getItem());
            addSeeds(pepperSeed.getItem());
        }

    }

    public void setMouseCrop(Items item){
        mouseFrame = item.getTextureRegion();
        currentItem = item;
        currentType = item.getType();

        }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(daysLeft == 0){
            if(goal > money) {
                lose.setVisible(true);
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        game.setScreen(new MainMenu(game));
                        dispose();
                    }
                }, 7);
            }
            if(goal < money) {
                win.setVisible(true);
                Timer.schedule(new Timer.Task(){
                    @Override
                    public void run() {
                        game.setScreen(new MainMenu(game));
                        dispose();
                    }
                }, 7);
            }

        }
        if (currentPlayerSprite.getX() + (cam.viewportWidth / 2 * cam.zoom) < mapWidth &&
                currentPlayerSprite.getX() - (cam.viewportWidth / 2 * cam.zoom) > 0)
            cam.position.x = player.getPlayerCenterX();

        if (currentPlayerSprite.getY() + cam.viewportHeight / 2 < mapHeight &&
                currentPlayerSprite.getY() - cam.viewportHeight / 2 > 0)
            cam.position.y = player.getPlayerCenterY();

        if (!isCollision(player.getBoundingBox()))
            player.setCurrentToNext();


        if ((object = isTeleport(player.getBoundingBox())) != null) {
            map.dispose();
            System.out.print(object.getName() + ".tmx");
            map = new TmxMapLoader().load(object.getName() + ".tmx");
            renderer.setMap(map);
            mapWidth = map.getProperties().get("width", Integer.class) * 32;
            mapHeight = map.getProperties().get("height", Integer.class) * 32;
            for (MapObject spawn : map.getLayers().get("PlayerSpawn").getObjects()) {
                Rectangle rectangle = ((RectangleMapObject) spawn).getRectangle();
                if (spawn.getName().equals(object.getName())) {
                    player.setCurrentPosition(rectangle.x, rectangle.y);
                    cam.position.set(rectangle.x, rectangle.y, 0);
                }
            }
        }
        if (isBed(player.getBoundingBox())) {
            if(showStage == false) {
                showStage = true;
                bedMenu.show(stage);
            }
            if (sleep == true) {

                timer.setDaysPassed(currentDays + 1);
                timer.setStartTime(currentDays + 1, 7, 0, 0);
                for (MapObject spawn : map.getLayers().get("PlayerSpawn").getObjects()) {
                    Rectangle rectangle = ((RectangleMapObject) spawn).getRectangle();
                    player.setCurrentPosition(rectangle.x, rectangle.y);
                    cam.position.set(rectangle.x, rectangle.y, 0);
                }
            }

        }
        sleep = false;
        cam.update();
        player.update(delta);

        renderer.render();
        clock.act(delta);
        if(timer.getDaysPast() != currentDays){
            for (Crop crops : crops)
                    crops.addDay();
            currentDays = timer.getDaysPast();
            daysLeft--;
            daysLeftNum.setText(String.format("%d", daysLeft));
            
            if(OrchardValley.interest) {
            	goal+= 150;
                goalLabel.setText(String.format("$%d", goal));
            }else {
            	goal+= 50;
            	goalLabel.setText(String.format("$%d", goal));
            }
            
            
        }

        time = timer.getFormattedTimeofDay();
        timeLabel.setText(time);
        controller.movePlayer(delta);
        game.batch.setProjectionMatrix(cam.combined);
        currentPlayerFrame = player.getCurrentFrame();
        renderer.setView(cam);
        game.batch.begin();


        for(int i = 0; i < numCrops; i++) {
            if (crops.get(i).isWatered())
                game.batch.setColor(Color.BROWN);
            game.batch.draw(textureFrames[4][1], crops.get(i).getFrameSprite().getX(), crops.get(i).getFrameSprite().getY() - 6);
            game.batch.setColor(Color.WHITE);
            game.batch.draw(crops.get(i).getCurrentFrame(), crops.get(i).getFrameSprite().getX(), crops.get(i).getFrameSprite().getY());
            if (crops.get(i).isDead()) {
                crops.removeIndex(i);
                numCrops--;
            }
        }

        for(int i = 0; i < seeds.size; i++){
            game.batch.draw(seeds.get(i).getTexture(), seeds.get(i).getBoundingRect().x, seeds.get(i).getBoundingRect().y);
        }

        game.batch.draw(currentPlayerFrame, currentPlayerSprite.getX(), currentPlayerSprite.getY());
        for(int i = 0; i < 9; i++) {
            game.batch.draw(box, (cam.position.x + 32 * i) - (cam.viewportWidth / 2 * (cam.zoom / 2)), cam.position.y - (cam.viewportHeight / 2 * cam.zoom));
            if(i < items.size) {
                game.batch.draw(items.get(i).getTextureRegion(), (cam.position.x + 32 * i) - (cam.viewportWidth / 2 * (cam.zoom / 2)), cam.position.y - (cam.viewportHeight / 2 * cam.zoom));
                if(items.get(i).getType() == Items.ItemType.SEED)
                    font.draw(game.batch, String.format("%d", items.get(i).getNum()), (cam.position.x + 32 * i) - (cam.viewportWidth / 2 * (cam.zoom / 2)-6), cam.position.y - (cam.viewportHeight / 2 * cam.zoom)+12);
                if (items.get(i).getItem() == currentItem.getItem())
                    game.batch.draw(border, (cam.position.x + 32 * i) - (cam.viewportWidth / 2 * (cam.zoom / 2)), cam.position.y - (cam.viewportHeight / 2 * cam.zoom));
            }
        }

        game.batch.end();
        shapeRenderer.setProjectionMatrix(cam.combined);
        Gdx.gl20.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(clock.getAmbientLighting());
        Matrix4 mat = cam.combined.cpy();
        shapeRenderer.setProjectionMatrix(mat);
        mat.setToOrtho2D(0, 0, Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
        shapeRenderer.rect(cam.position.x - gameView.getWorldWidth()/2, cam.position.y - gameView.getWorldHeight()/2, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();
        Gdx.gl20.glDisable(GL20.GL_BLEND);
        game.batch.setProjectionMatrix(mat);
        shapeRenderer.setColor(Color.WHITE);
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gameView.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

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
    public void dispose() {
        stage.dispose();
        bucketTexture.dispose();
        skin.dispose();
        font.dispose();
        map.dispose();
        mouseCrop.dispose();
        box.dispose();
        border.dispose();
    }

    public TiledMap getMap() {
        return map;
    }
    public boolean isCollision(Rectangle boundingBox){
        MapLayer objectLayer = map.getLayers().get("Collision");
        return checkCollision(boundingBox, objectLayer);
    }

    public MapObject isTeleport(Rectangle boundingBox) {
        MapLayer objectLayer = map.getLayers().get("NewScreen");
        for(MapObject object: objectLayer.getObjects()){
            if(object instanceof RectangleMapObject){
                    Rectangle rectangle = ((RectangleMapObject) object). getRectangle();
                    if(boundingBox.overlaps(rectangle))
                        return object;
            }
        }
        return null;
    }

    public boolean isBed(Rectangle boundingBox) {
        if(map.getProperties().get("name").equals("Home")){
            MapLayer objectLayer = map.getLayers().get("Bed");
            return checkCollision(boundingBox, objectLayer);
        }
        else
            return false;
    }

    public boolean checkCollision(Rectangle boundingBox, MapLayer objectLayer) {
        for (MapObject object : objectLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                if (boundingBox.overlaps(rectangle))
                    return true;
            }

        }
        return false;
    }

    public void buySeed(Seeds seed){
        if(seed.getPrice() <= money) {
            money -= seed.getPrice();
            addSeeds(seed.getItem());
        }
        scoreLabel.setText(String.format("$%d", money));
    }

    public void addSeeds(Items.Item item) {
        switch(item) {
            case GOURD:
                gourdSeed.add();
                break;
            case TOMATO:
                tomatoSeed.add();
                break;
            case PEPPER:
                pepperSeed.add();
                break;
            case CORN:
                cornSeed.add();
                break;
            case CARROT:
                carrotSeed.add();
                break;
            case POTATO:
                potatoSeed.add();
                break;
            case ARTICHOKE:
                artSeed.add();
        }
    }

    public void removeSeeds(Items.Item item) {
        switch(item) {
            case GOURD:
                gourdSeed.remove();
                break;
            case TOMATO:
                tomatoSeed.remove();
                break;
            case PEPPER:
                pepperSeed.remove();
                break;
            case CORN:
                cornSeed.remove();
                break;
            case CARROT:
                carrotSeed.remove();
                break;
            case POTATO:
                potatoSeed.remove();
                break;
            case ARTICHOKE:
                artSeed.remove();
        }
    }



    public OrthographicCamera getCam() {
        return cam;
    }

    public void addCrop(Crop crop){
        crops.add(crop);
    }

    public Array<Crop> getCrops() {
        return crops;
    }

    public void addMoney(int price){
        money += price;
        scoreLabel.setText(String.format("$%d", money));
    }

    public Array<Items> getItems() {
        return items;
    }

    public OrchardValley getGame() {
        return game;
    }

    public Timer_ getTimer() {
        return timer;
    }

    public Array<Seeds> getSeeds() {
        return seeds;
    }

    public int getCurrentDays() {
        return currentDays;
    }
}

package mainGame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import mainGame.OrchardValley;

public class MainMenu implements Screen{
    private Viewport viewPort;
    private Stage mainStage;
    private Stage tutorialStage;
    private Stage selectStage;
    private OrchardValley game;
    private Skin skin;
    private Texture background;
    private Table table;

    public MainMenu(final OrchardValley game) {
        this.game = game;
        viewPort = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        mainStage = new Stage(viewPort, game.batch);
        tutorialStage = new Stage(viewPort, game.batch);
        selectStage = new Stage(viewPort, game.batch);
        Gdx.input.setInputProcessor(mainStage);
        background = new Texture("HomePage.jpg");
        skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        final TextButton startGame = new TextButton("New Game", skin);
        final TextButton tutorialButton = new TextButton("How to Play", skin);
        final TextButton backButton = new TextButton("Back", skin);
        final TextButton backButton2 = new TextButton("Back", skin);
        final TextButton easy = new TextButton("Easy",skin);
        final TextButton medium = new TextButton("Kinda Hard",skin);
        final TextButton hard	= new TextButton("Hard!!",skin);
        startGame.getLabel().setFontScale(1.5f);
        tutorialButton.getLabel().setFontScale(1.5f);
        backButton.getLabel().setFontScale(1.5f);
        backButton2.getLabel().setFontScale(1.5f);
        easy.getLabel().setFontScale(1.5f);
        medium.getLabel().setFontScale(1.5f);
        hard.getLabel().setFontScale(1.5f);
        
        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        font.font.getData().setScale(2);
        final Table table = new Table();
        mainStage.addActor(table);
        table.center();
        table.setFillParent(true);

        table.add(startGame).padBottom(50f).padTop(100f).width(250).height(75);
        table.row();
        table.add(tutorialButton).width(250).height(75);

        final Table tutorial = new Table();
        tutorialStage.addActor(tutorial);
        tutorial.center();
        tutorial.setFillParent(true);
        final Label playAgainLabel = new Label("" +
                "Welcome to Orchard Valley!\n" +
                "\n" +
                "Your farm is in terrible danger as the banks close in to\n"+
                "claim the money you owe them, that or your land is going to be theirs!\n"+
                "\n" +
                "\nHow to play:\n" +
                "-Move around using WASD keys \n" +
                "-Cycle crop seeds and tools using the number keys\n" +
                "-When seeds are equiped, click on any grass on your farm to plant crops (must have seeds)\n"+
                "-Crops need to be watered everday to grow. If they go two days without water they will die\n" +
                "-Click on full grown crops to harvest for cash\n" +
                "-Go to shop to click and buy more seeds\n" +
                "-Sleep in your bed to advance a day\n" +
                "", font);

        tutorial.add(playAgainLabel).padBottom(50);
        tutorial.row();
        tutorial.add(backButton).width(250).height(75);
        tutorial.setVisible(false);
        
        final Table select = new Table();
        selectStage.addActor(select);
        select.center();
        select.setFillParent(true);
        final Label key = new Label("" +
                "Choose your difficulty\n" +
                "\n" +
                "Easy will give you some starter seeds.\n"+
                "The harder difficulties will have more bank debt\n"+
                "And perhaps higher interest! \n" +
                "How far will you go to save YOUR LAAND!\n" +
                "", font);

        select.add(key).padBottom(50).padRight(500);
        select.row();
        select.add(easy).padTop(20f).width(250).height(75);
        select.row();
        select.add(medium).padTop(20f).width(250).height(75);
        select.row();
        select.add(hard).padTop(20f).width(250).height(75);
        select.row();
        select.add(backButton2).padTop(25f).width(250).height(75);
        select.setVisible(false);
      
        tutorialButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
              table.setVisible(false);
              tutorial.setVisible(true);
              Gdx.input.setInputProcessor(tutorialStage);
                return true;
            }
        });
        backButton.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                table.setVisible(true);
                tutorial.setVisible(false);
                Gdx.input.setInputProcessor(mainStage);

                return true;
            }
        });
        backButton2.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                table.setVisible(true);
                select.setVisible(false);
                Gdx.input.setInputProcessor(mainStage);

                return true;
            }
        });
        easy.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                game.setScreen(new PlayScreen(game));
                dispose();
                return true;
            }

        });
        medium.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
            	OrchardValley.cashGoal = 5000;
                game.setScreen(new PlayScreen(game));
                dispose();
                return true;
            }

        });
        hard.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
            	OrchardValley.cashGoal = 8000;
            	OrchardValley.interest = true;
                game.setScreen(new PlayScreen(game));
                dispose();
                return true;
            }

        });
        startGame.addListener(new InputListener() {
        	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
                table.setVisible(false);
                select.setVisible(true);
                Gdx.input.setInputProcessor(selectStage);

                return true;
            }

        });



    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background, 0, 0, 1600, 921);
        game.batch.end();
        mainStage.act(delta);
        tutorialStage.act(delta);
        selectStage.act(delta);
        selectStage.draw();
        tutorialStage.draw();
        mainStage.draw();
        viewPort.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

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
        background.dispose();
        skin.dispose();
        mainStage.dispose();
        tutorialStage.dispose();
        selectStage.dispose();
    }

}


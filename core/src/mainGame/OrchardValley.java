package mainGame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector3;

import mainGame.MainMenu;
import mainGame.PlayScreen;
import tools.Crop;
import tools.Items;
import mainGame.Entity;

public class OrchardValley extends Game{
	

//debugging
	BitmapFont font;
	String message;
//-----------
	Music mainMusic;
	public static final float UNIT_SCALE = 1/32f;
	public SpriteBatch batch;
	public static AssetManager manager;
	public static boolean interest;
	public static int cashGoal;
	

	TiledMap map;
	OrthographicCamera camera;
	
	@Override
	public void create () {
		font = new BitmapFont();
		font.setColor(Color.WHITE); 
		message = "";
		
		manager = new AssetManager();
	    manager.load("water.mp3", Sound.class);
	    manager.load("dirt.mp3", Sound.class);
	    manager.finishLoading();
	    
		mainMusic = Gdx.audio.newMusic(Gdx.files.internal("MainMusic.mp3"));
		
		mainMusic.setLooping(true);
		mainMusic.play();
		batch = new SpriteBatch();
		
		interest = false;
		cashGoal = 500;
       
		setScreen(new MainMenu(this));
		
	}



	@Override
	public void render () {	
		super.render();
	}
	
	@Override
	public void dispose () {
		font.dispose();
		batch.dispose();
	}
	
}
	

class inputClass implements InputProcessor{
	Entity player;
    Crop crop;
    boolean left;
    boolean right;
    boolean up;
    boolean down;
    PlayScreen screen;
    Vector3 tp;
	
	
	inputClass(PlayScreen screen, Entity player) {
        this.player = player;
        this.screen = screen;
        tp = new Vector3();
        left = false;
        right = false;
        up = false;
        down = false;
    }
	
	public void movePlayer(float delta)
	{
	        if(up){
	            player.move(Entity.Direction.UP, delta);
	            player.setState(Entity.State.WALKING);
	            player.setDirection(Entity.Direction.UP, delta);
	        }
	        else if(down){
	            player.move(Entity.Direction.DOWN, delta);
	            player.setState(Entity.State.WALKING);
	            player.setDirection(Entity.Direction.DOWN, delta);
	        }
	        else if(right){
	            player.move(Entity.Direction.RIGHT, delta);
	            player.setState(Entity.State.WALKING);
	            player.setDirection(Entity.Direction.RIGHT, delta);
	        }
	        else if(left) {
	            player.move(Entity.Direction.LEFT, delta);
	            player.setState(Entity.State.WALKING);
	            player.setDirection(Entity.Direction.LEFT, delta);
	        }
	        else {
	            player.setState(Entity.State.IDLE);
	            player.setDirection(player.getDirection(), delta);
	        }
	    }
	
	public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.A)
            this.left = true;
        if(keycode == Input.Keys.W)
            this.up = true;
        if(keycode == Input.Keys.S)
            this.down = true;
        if(keycode == Input.Keys.D)
           this.right = true;
        
        if(keycode == Input.Keys.NUM_1)
            screen.setMouseCrop(screen.getItems().get(0));
        if(keycode == Input.Keys.NUM_2)
            screen.setMouseCrop(screen.getItems().get(1));
        if(keycode == Input.Keys.NUM_3)
            screen.setMouseCrop(screen.getItems().get(2));
        if(keycode == Input.Keys.NUM_4)
            screen.setMouseCrop(screen.getItems().get(3));
        if(keycode == Input.Keys.NUM_5)
            screen.setMouseCrop(screen.getItems().get(4));
        if(keycode == Input.Keys.NUM_6)
            screen.setMouseCrop(screen.getItems().get(5));
        if(keycode == Input.Keys.NUM_7)
            screen.setMouseCrop(screen.getItems().get(6));
        if(keycode == Input.Keys.NUM_8)
            screen.setMouseCrop(screen.getItems().get(7));
        if(keycode == Input.Keys.NUMPAD_0) {
            screen.getTimer().setDaysPassed(screen.getCurrentDays() + 1);
            screen.getTimer().setStartTime(screen.getCurrentDays() + 1, 7, 0, 0);
        }


        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.A)
            this.left = false;
        if(keycode == Input.Keys.W)
            this.up = false;
        if(keycode == Input.Keys.S)
            this.down = false;
        if(keycode == Input.Keys.D)
           this.right = false;
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 coords = screen.getCam().unproject(tp.set(screenX, screenY,0));
        if(Math.abs(player.getPlayerCenterX()- coords.x) < 50 && Math.abs(player.getPlayerCenterY()-coords.y) < 50) {

            for (int i = 0; i < screen.getSeeds().size; i++) {
                if(screen.getSeeds().get(i).getBoundingRect().contains(coords.x, coords.y)){
                    screen.buySeed(screen.getSeeds().get(i));
                }
           }

            for (int i = 0; i < screen.numCrops; i++) {
                if (screen.getCrops().get(i).getFrameSprite().getBoundingRectangle().contains(coords.x, coords.y)) {
                    if (screen.currentItem.getItem() == Items.Item.BUCKET) {
                        screen.getCrops().get(i).setWatered(true);
                        OrchardValley.manager.get("water.mp3", Sound.class).play(2);
                    }
                    if (screen.getCrops().get(i).getGrowthStage() == 3) {
                        screen.addMoney(screen.getCrops().get(i).getPrice());
                        screen.getCrops().removeIndex(i);
                        screen.numCrops--;
                        OrchardValley.manager.get("dirt.mp3", Sound.class).play();
                        return false;
                    } else {
                        return false;
                    }
                }
            }
            if (screen.currentType == Items.ItemType.SEED) {
            	//debug
            	System.out.println("x: "+coords.x+" y: "+coords.y);
                if (coords.y<=2400 && coords.y>=2100 && coords.x<=796 && coords.x>=232) {
                	
                    if (screen.currentItem.getNum() > 0) {
                        crop = new Crop(screen.currentItem.getItem(), coords.x, coords.y);
                        screen.addCrop(crop);
                        screen.removeSeeds(screen.currentItem.getItem());
                        screen.numCrops++;
                        OrchardValley.manager.get("dirt.mp3", Sound.class).play();
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
       screen.intType+= amount;
       if(screen.intType > screen.getItems().size-1)
           screen.intType = 0;
       if(screen.intType < 0)
           screen.intType = screen.getItems().size-1;
       screen.setMouseCrop(screen.getItems().get(screen.intType));
            return false;
    }
 
}

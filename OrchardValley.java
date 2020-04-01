package com.orchard.valley;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;

public class OrchardValley extends ApplicationAdapter implements InputProcessor{
	

//debugging
	BitmapFont font;
	String message;
//-----------
	Texture farmer;
    Texture homePage;
	Music mainMusic;
	Sprite sprite;
	int dx;
	int dy;
	int mapWidth;
	int mapHeight;
	boolean starting  = true;
	
	
	TiledMap map;
	OrthographicCamera camera;
	MyTiledMapRenderer renderer;
//	MyTiledMapRenderer renderer2;
	
	@Override
	public void create () {
		font = new BitmapFont();
		font.setColor(Color.WHITE); 
		message = "";
		
		//images
		homePage = new Texture("HomePage.jpg");
		farmer= new Texture("People Objects/farmer.png");
		sprite= new Sprite(farmer);
		sprite.setPosition(404,250);
		sprite.setSize(70,70);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update(); 	
		map = new TmxMapLoader().load("FarmLand.tmx");
		
		
		renderer = new MyTiledMapRenderer(map);		// Use our custom renderer
		renderer.addSprite(sprite);
		
		TiledMapTileLayer layer = new TiledMapTileLayer(map.getProperties().get("width",Integer.class), map.getProperties().get("height",Integer.class), 16, 16); 	
		TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();	
		
		//sounds or music
		mainMusic = Gdx.audio.newMusic(Gdx.files.internal("MainMusic.mp3"));
		
		mainMusic.setLooping(true);
		mainMusic.play();
		
		if(map.getLayers().get(1) instanceof TiledMapTileLayer) {
		     TiledMapTileLayer newLayer = (TiledMapTileLayer)map.getLayers().get(1);
		     mapWidth = (int) (newLayer.getTileWidth() * newLayer.getWidth());
			 mapHeight = (int) (newLayer.getTileHeight() * newLayer.getHeight());
			}
			// Allow this class to process input
			Gdx.input.setInputProcessor(this);
		
	}

	
	@Override
	public void render () {
		
		Gdx.gl.glClearColor(1, 1, 1, 1);	
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
			
		movePlayer();
	    camera.update();
	    renderer.setView(camera);
	    renderer.render();	
	    
		
	}
	
	@Override
	public void dispose () {
		font.dispose();
		farmer.dispose();
		homePage.dispose();
	}
	
	public void movePlayer()
	{
		// Check to see if the player can go where they want to go.
        Vector2 newPos = new Vector2(sprite.getX() + dx, sprite.getY() + dy);
		
		TiledMapTileLayer topLayer = (TiledMapTileLayer) map.getLayers().get(1);
		TiledMapTileLayer.Cell cell = topLayer.getCell((int)(newPos.x /16), (int)(newPos.y /16));
		
		if(cell != null)
			return;
		
		
		camera.translate(dx, dy);
        sprite.translate(dx, dy);
	}
	
	
	  @Override
	  public boolean keyDown(int keycode) {
	        
	    	if(keycode == Input.Keys.LEFT) 
	            dx = -2;
	    	
	    	if(keycode == Input.Keys.RIGHT) 
	            dx = 2;
	        
	    	if(keycode == Input.Keys.UP) 
	        	dy = 2;
	        
	    	if(keycode == Input.Keys.DOWN)
	        	dy = -2;
	       
	        //speed him up
	        if(keycode == Input.Keys.SPACE) {
	        	dx = dx *2;
	        	dy = dy *2;
	        }
	        
	        if(keycode == Input.Keys.ENTER) {
	        	starting = false;
	        }
	      
	        return true;
	    }

	    public boolean keyUp(int keycode) {

	    	if(keycode == Input.Keys.LEFT)
	            dx = 0;
	    	
	        if(keycode == Input.Keys.RIGHT)
	            dx = 0;
	        
	        if(keycode == Input.Keys.UP)
	        	dy = 0;
	        
	        if(keycode == Input.Keys.DOWN)
	        	dy = 0;
	        
	        //slow him down
	        if(keycode == Input.Keys.SPACE) {
	        	dx = dx/2;
	        	dy = dy/2;
	        }
	      
	      
	        return true;
	    }
	    
	    public boolean keyTyped(char character) {return false;}
	    public boolean touchDown(int screenX, int screenY, int pointer, int button) {return false;}
	    public boolean touchUp(int screenX, int screenY, int pointer, int button) {return false;}
	    public boolean touchDragged(int screenX, int screenY, int pointer) {return false;}
	    public boolean mouseMoved(int screenX, int screenY) {return false;}
	    public boolean scrolled(int amount) {return false;}
}

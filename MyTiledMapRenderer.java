package com.orchard.valley;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class MyTiledMapRenderer extends OrthogonalTiledMapRenderer {
	
	// Class variables
	private int spriteLayer = 1;		// We'll render our sprites after this layer
	private List<Sprite> sprites;		// Pointers to all the sprites we want to render


	
	public MyTiledMapRenderer(TiledMap map)
	{
		super(map);
		sprites = new ArrayList<Sprite>();
	}
	
	public void addSprite(Sprite sprite) { sprites.add(sprite); }
	
	// Here's the important stuff!
	@Override
	public void render()
	{
		
		beginRender();		// Call the superclass code that starts the rendering process
//		this.getBatch().setProjectionMatrix(camera.combined); 	// Adjust our built-in spritebatch to match the camera.
				
		int layerCount = 0;
		for(MapLayer layer : map.getLayers())
		{
			
			if(layer.isVisible())
			{
				renderTileLayer((TiledMapTileLayer)layer);	// Call the built-in OrthogonalRenderer code for this layer
				layerCount++;
				if(layerCount == spriteLayer)				// Check to see if this is where we want to render our sprites
				{
					for(Sprite sprite: sprites)
					{
						sprite.draw(this.getBatch());	
					}
				}
			}
		}
			
		endRender();		// Call the superclass code that wraps up the rendering process
	}
	
	
	
}

package time;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import mainGame.PlayScreen;
import tools.Seeds;

public class MapLoader {


    private Vector2 playerSpawn;
    private TiledMap map;

    public MapLoader(PlayScreen screen) {
        map = screen.getMap();
        playerSpawn = new Vector2();


        for (MapObject object : map.getLayers().get("PlayerSpawn").getObjects()) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            playerSpawn.set(rectangle.x, rectangle.y);
        }
        
        for (MapObject object : map.getLayers().get("Seeds").getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                screen.getSeeds().add(new Seeds(object.getName(), rectangle));
            }
        }

    }



    public Vector2 getPlayerSpawn() {
        return playerSpawn;
    }
}

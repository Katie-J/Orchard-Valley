package mainGame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;




public class Entity extends Sprite {
   private Vector2 velocity;
   private Direction currentDirection;
   private Direction previousDirection;
   
   
   private Vector2 nextPosition;
   private Vector2 currentPosition;
   private State state = State.IDLE;
   private float frameTime;
   private Sprite sprite;
   private TextureRegion currentFrame;

   private Texture farmer;

   public static Rectangle boundingBox;



    public enum State {
        IDLE, WALKING
    }

    public enum Direction {
        UP,
        RIGHT,
        DOWN,
        LEFT
    }

    public Entity(){
        this.nextPosition = new Vector2();
        this.currentPosition = new Vector2();
        this.boundingBox = new Rectangle();
        this.velocity = new Vector2(2.5f,2.5f);
        frameTime = 0f;
        currentDirection = Direction.DOWN;
        farmer = new Texture("People Objects/farmer.png");
        loadSprite();

    }
    public void startingPosition(float x, float y){
        this.currentPosition.set(x,y);
        this.nextPosition.set(x,y);
    }
    public void update(float delta) {
        if(state == State.WALKING)
            frameTime = (frameTime + delta)%5;
        else
            frameTime = 0;
        boundingBox.set(nextPosition.x + 20, nextPosition.y, 24, 12);
    }
    
    public void setDirection(Direction direction, float delta){
        this.previousDirection = this.currentDirection;
        this.currentDirection = direction;
    }

    private void loadSprite() {
        TextureRegion[][] textureFrames = TextureRegion.split(farmer, 47, 64);
        sprite = new Sprite(textureFrames[0][0].getTexture());
        currentFrame = textureFrames[0][0];
    }



    public void move(Direction direction, float delta) {
        float x = currentPosition.x;
        float y = currentPosition.y;

        switch (direction){
            case DOWN:
                y -= velocity.y;
                break;
            case UP:
                y += velocity.y;
                break;
            case LEFT:
                x -= velocity.x;
                break;
            case RIGHT:
                x += velocity.x;
                break;
            default:
                break;
        }
        nextPosition.x = x;
        nextPosition.y = y;


    }

    public void setState(State state) {
        this.state = state;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public Direction getDirection() {
        return currentDirection;
    }

    public void setCurrentPosition(float x, float y){
        sprite.setX(x);
        sprite.setY(y);
        this.currentPosition.x = x;
        this.currentPosition.y = y;
        this.nextPosition.x = x;
        this.nextPosition.y = y;
    }
    public void setCurrentToNext(){
        setCurrentPosition(nextPosition.x, nextPosition.y);
    }

    public static Rectangle getBoundingBox() {
        return boundingBox;
    }

    public static float getPlayerCenterX() {
        return boundingBox.x + boundingBox.width/2;
    }
    public static float getPlayerCenterY() {
        return boundingBox.y + boundingBox.height/2;
    }


}
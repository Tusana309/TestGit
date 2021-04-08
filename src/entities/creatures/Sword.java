package entities.creatures;

import entities.Entity;
import game.Handler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import settings.Settings;
import sounds.Sound;

public class Sword extends Creature {

    private boolean isCut = false;
    private int xLong = 0, yLong = 0;

    public Sword(Handler handler, Image image, double x, double y, int damage, int direction) {
        super(handler, image, x, y, 10, 11, damage);
        this.direction = direction;

        setSpeed(8);

        bounds.setX(0);
        bounds.setY(0);
        bounds.setWidth(40);
        bounds.setHeight(40);

    }

    @Override
    public void move() {
        moveX();
        moveY();
    }

    public void moveX(){
        if(xMove > 0){ //Moving right
            int tx = (int) (x + bounds.getX() + bounds.getWidth()) / Settings.TILE_WIDTH;

            if(!collisionWithTile(tx, (int) (y + bounds.getY()) / Settings.TILE_HEIGHT) &&
                    !collisionWithTile(tx, (int) (y + bounds.getY() + bounds.getHeight()) / Settings.TILE_HEIGHT)){
                x += xMove/2;
                xLong += xMove/2;
            } else {
                die();
            }
        } else if (xMove < 0){ //Moving left
            int tx = (int) (x + bounds.getX()) / Settings.TILE_WIDTH;

            if (!collisionWithTile(tx, (int) (y + bounds.getY()) / Settings.TILE_HEIGHT) &&
                    !collisionWithTile(tx, (int) (y + bounds.getY() + bounds.getHeight()) / Settings.TILE_HEIGHT)) {
                x += xMove/2;
                xLong += xMove/2;
            } else {
                die();
            }
        }
    }

    public void moveY(){
        if(yMove < 0){ //Moving up
            int ty = (int) (y + bounds.getY()) / Settings.TILE_HEIGHT;

            if(!collisionWithTile((int) (x + bounds.getX()) / Settings.TILE_WIDTH, ty) &&
                    !collisionWithTile((int) (x + bounds.getX() + bounds.getWidth()) / Settings.TILE_WIDTH, ty)){
                y += yMove/2;
                yLong += yMove/2;
            } else {
                die();
            }
        } else if (yMove > 0){ //Moving down
            int ty = (int) (y + bounds.getY() + bounds.getHeight()) / Settings.TILE_HEIGHT;

            if(!collisionWithTile((int) (x + bounds.getX()) / Settings.TILE_WIDTH, ty) &&
                    !collisionWithTile((int) (x + bounds.getX() + bounds.getWidth()) / Settings.TILE_WIDTH, ty)){
                y += yMove/2;
                yLong += yMove/2;
            } else {
                die();
            }
        }
    }

    @Override
    public void tick() {
        cut();
        move();
        checkHit();
    }

    public void cut(){
        if(!isCut){
            if(direction == 1){ //up
                yMove = -speed;
            }

            if(direction == 2){ //down
                yMove = speed;
            }

            if(direction == 3){ //left
                xMove = -speed;
            }

            if(direction == 4){ //right
                xMove = speed;
            }
            isCut = true;
        }
    }

    public void checkHit(){
        //Enemy hit
        for(Entity e : handler.getWorld().getEntityManager().getEntities()){
            if(e.equals(handler.getWorld().getEntityManager().getPlayer()))
                continue;
            if(e.getCollisionBounds(0, 0).intersects(getCollisionBounds(0,0).getBoundsInLocal())){
                e.takeDamage(damage);

                if(!Settings.IS_MUTE){
                    if(Sound.cut.getStatus() == MediaPlayer.Status.PLAYING)
                        Sound.cut.stop();
                    Sound.cut.play();
                }
            }
        }

        //Tile hit

        //Bullet move
        if(Math.abs(xLong) > 23 || Math.abs(yLong) > 23)
            die();
    }

    @Override
    public void render(GraphicsContext g) {
        g.drawImage(image, (int)(x - handler.getGameCamera().getxOffset()),
                (int) (y - handler.getGameCamera().getyOffset()));

    }

    @Override
    public void die() {
        active = false;
        System.out.println("Chet :D");
    }
}


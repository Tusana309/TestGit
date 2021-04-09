package entities.creatures;

import entities.Entity;
import entities.creatures.weapon.Weapon;
import game.Handler;
import javafx.scene.image.Image;
import javafx.scene.media.MediaPlayer;
import settings.Settings;
import sounds.Sound;

public class Sword extends Weapon {

	public Sword(Handler handler, Image image, double x, double y, int damage,int direction) {
		super(handler, image, x, y, 10,11, damage, 23, 23);
		this.direction = direction;
		
		setSpeed(8);
		bounds.setX(4);
		bounds.setY(4);
		bounds.setHeight(40);
		bounds.setWidth(40);
		
	}
	@Override
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
        if(Math.abs(xLong) > xFar || Math.abs(yLong) > yFar)
            die();
    }
}
package entities.creatures.weapon;

import entities.Entity;
import entities.creatures.Creature;
import game.Handler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import settings.Settings;


public abstract class Weapon extends Creature {
	protected boolean attack = false;
	protected int xLong =0, yLong = 0;
	protected int xFar, yFar;
	
	
	public Weapon(Handler handler, Image image, double x, double y, int width, int height, int damage,int xFar,int yFar) {
		super(handler, image, x, y, width, height, damage);
			this.xFar = xFar;
			this.yFar = yFar;
			
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
    
   

    public void cut(){
        if(!attack){
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
            attack = true;
        }
    }

    public void checkHit(){
        //Enemy hit
        for(Entity e : handler.getWorld().getEntityManager().getEntities()){
            if(e.equals(handler.getWorld().getEntityManager().getPlayer()))
                continue;
            if(e.getCollisionBounds(0, 0).intersects(getCollisionBounds(0,0).getBoundsInLocal())){
                e.takeDamage(damage);
              
            }
        }

        //Tile hit

        //Bullet move
        if(Math.abs(xLong) > xFar || Math.abs(yLong) > yFar)
            die();
    }
   @Override
	public void tick() {
		 cut();
	     move();
	     checkHit();	 
		
	}

	@Override
	public void render(GraphicsContext g) {
		g.drawImage(image, (int)(x - handler.getGameCamera().getxOffset()),
                (int) (y - handler.getGameCamera().getyOffset()));
		
	}

	@Override
	public void die() {
		active = false;
        System.out.println("Chết :D");
	}
	

	



}

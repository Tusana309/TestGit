package entities.creatures;

import game.Handler;
import gfx.Assets;
import gfx.ImageAnimation;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Slime extends Enemy{

    private ImageAnimation slimeUp, slimeDown, slimeLeft, slimeRight;


    public Slime(Handler handler, Image image, double x, double y) {
        super(handler, image, x, y);

        setDamage(50);

        setWidth(32);
        setHeight(32);
        setSpeed(1);
        setHealth(50);

        bounds.setX(4);
        bounds.setY(12);
        bounds.setWidth(24);
        bounds.setHeight(20);

        slimeUp = new ImageAnimation(135, Assets.slime_up);
        slimeDown = new ImageAnimation(135, Assets.slime_down);
        slimeLeft = new ImageAnimation(135, Assets.slime_left);
        slimeRight = new ImageAnimation(135, Assets.slime_right);
    }

    @Override
    public void tick() {
        super.tick();
        slimeUp.tick();
        slimeDown.tick();
        slimeLeft.tick();
        slimeRight.tick();
    }


    @Override
    public void render(GraphicsContext g) {
        g.drawImage(getCurrentAnimationFrame(), (int)(x - handler.getGameCamera().getxOffset()),
                (int) (y - handler.getGameCamera().getyOffset()));


        //draw health bar
        g.setFill(Color.BLACK);
        g.strokeRoundRect((int)(x - handler.getGameCamera().getxOffset() - 4),
                (int) (y - handler.getGameCamera().getyOffset() - 5),40,5, 10, 10);
        g.setFill(Color.GREEN);
        g.fillRoundRect((int)(x - handler.getGameCamera().getxOffset() - 4),
                (int) (y - handler.getGameCamera().getyOffset() - 5), 40 * ((double) (health) /(double) maxHealth), 4, 10, 10);
    }

    private Image getCurrentAnimationFrame(){
        if(xMove < 0){
            return slimeLeft.getCurrentFrame();
        } else if(xMove > 0){
            return slimeRight.getCurrentFrame();
        } else if(yMove < 0){
            return slimeUp.getCurrentFrame();
        } else {
            return slimeDown.getCurrentFrame();
        }
    }

}



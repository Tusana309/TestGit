package entities.statics;

import entities.Entity;
import game.Handler;
import javafx.scene.image.Image;

public abstract class StaticEntity extends Entity {

    public StaticEntity(Handler handler, Image image, float x, float y, int width, int height){
        super(handler, image, x, y, width, height);
    }

}

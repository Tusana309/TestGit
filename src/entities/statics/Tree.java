package entities.statics;

import game.Handler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import settings.Settings;

public class Tree extends StaticEntity{

    Image tree;

    public Tree(Handler handler, Image image, float x, float y){
        super(handler, image, x, y, Settings.TILE_WIDTH, (int) (Settings.TILE_HEIGHT*2*0.7));

        imageView = new ImageView(image);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);

        bounds.setX(36);
        bounds.setY(65);
        bounds.setWidth(10);
        bounds.setHeight(25);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(GraphicsContext g) {

        tree = imageView.snapshot(params, null);
        g.drawImage(tree, (int)(x - handler.getGameCamera().getxOffset()),
                (int) (y - handler.getGameCamera().getyOffset()));
    }

    @Override
    public void die() {

    }
}

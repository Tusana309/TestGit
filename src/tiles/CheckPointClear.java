package tiles;

import gfx.Assets;
import javafx.scene.image.Image;

public class CheckPointClear extends Tile{
    public CheckPointClear(int id) {
        super(Assets.clear, id);
    }

    @Override
    public boolean isCheckPoint() {
        return true;
    }
}

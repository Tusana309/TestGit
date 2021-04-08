package tiles;

import gfx.Assets;
import javafx.scene.image.Image;

public class ArchitectureMap4 extends Tile{
    public ArchitectureMap4(int id) {
        super(Assets.map4[(id-1501)%25][(id-1501-((id-1501)%25))/25], id);
    }

    @Override
    public boolean isSolid() {
        return false;
    }
}

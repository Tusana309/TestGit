package game;

import gfx.GameCamera;
import input.KeyManager;
import input.MouseManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import sounds.SoundManager;
import worlds.World;

public class Handler {

    private Game game;
    private World world;
    private boolean tele = true;

    public Handler(Game game){
        this.game = game;
    }

    public Game getGame(){
        return game;
    }

    public GraphicsContext getGraphicsContext(){
        return game.getGraphicsContext();
    }

    public StackPane getPane(){
        return game.getPane();
    }

    public KeyManager getKeyManager(){
        return game.getKeyManager();
    }

    public MouseManager getMouseManager() {
        return game.getMouseManager();
    }

    public GameCamera getGameCamera(){
        return game.getGameCamera();
    }

    public World getWorld() {
        return world;
    }

    public boolean isTele() {
        return tele;
    }

    public void setTele(boolean tele) {
        this.tele = tele;
    }

    public void setWorld(World world, boolean tele) {
        this.tele = tele;
        this.world = world;

    }

    public int getWidth(){
        return game.getWidth();
    }

    public int getHeight(){
        return game.getHeight();
    }

    public SoundManager getSoundManager(){
        return game.getSoundManager();
    }
}

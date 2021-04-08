package worlds;

import entities.EntityManager;
import entities.creatures.Player;
import entities.creatures.Skeleton;
import entities.creatures.Slime;
import entities.statics.Tree;
import game.Handler;
import gfx.Assets;
import items.Item;
import items.ItemManager;
import javafx.scene.canvas.GraphicsContext;
import settings.Settings;
import tiles.Tile;
import utils.Utils;


public class World {

    private int width, height;
    private Player player;
    private int spawnXNext;
    private int spawnYNext;
    private int spawnXPre;
    private int spawnYPre;
    private int spawnX;
    private int spawnY;
    private int layer = 3;
    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getCountWorld() {
        return countWorld;
    }

    public void setCountWorld(int countWorld) {
        this.countWorld = countWorld;
    }

    private int countWorld;
    private int[][][] tiles;


    //Entities
    private EntityManager entityManager;
    private EntityManager world1Manager, world2Manager, world3Manager;
    private Handler handler;
    private long lastSpawnTimer, spawnCoolDown = 500, spawnTimer = spawnCoolDown;
    private int enemyOnBoard = 0;

    //Items
    private ItemManager itemManager;

    public World(Handler handler, String path){
        this.handler = handler;

        loadWorld(path);
        if(handler.isTele()){
            spawnX = spawnXNext;
            spawnY = spawnYNext;
        }else{
            spawnX = spawnXPre;
            spawnY = spawnYPre;
        }

        world1Manager = new EntityManager(handler);
        world2Manager = new EntityManager(handler);
        world3Manager = new EntityManager(handler);
        entityManager = world1Manager;

        player = new Player(handler, spawnX, spawnY, 25);
        entityManager.setPlayer(player);
        itemManager = new ItemManager(handler);

        //spawn enemies in world 1:
        for(int i = 0; i < 3; ++i){
            world1Manager.addEntity(new Skeleton(handler, Assets.skeleton, 550 + 55 * i, 1050));
            world1Manager.addEntity(new Slime(handler, Assets.skeleton, 600 + 45 * i, 1150));
            world1Manager.addEntity(new Skeleton(handler, Assets.skeleton, 550 + 55 * i, 1200));

            world1Manager.addEntity(new Skeleton(handler, Assets.skeleton, 1300 + 55 * i, 1050));
            world1Manager.addEntity(new Slime(handler, Assets.skeleton, 1350 + 45 * i, 1150));
            world1Manager.addEntity(new Skeleton(handler, Assets.skeleton, 1300 + 55 * i, 1200));

            world1Manager.addEntity(new Skeleton(handler, Assets.skeleton, 1100 + 55 * i, 70));
            world1Manager.addEntity(new Slime(handler, Assets.skeleton, 1150 + 45 * i, 130));
            world1Manager.addEntity(new Skeleton(handler, Assets.skeleton, 1100 + 55 * i, 150));
        }

        //enemies in world 2
        for(int i = 0; i < 3; ++i){
            world2Manager.addEntity(new Skeleton(handler, Assets.skeleton, 550 + 55 * i, 1050));
            world2Manager.addEntity(new Slime(handler, Assets.skeleton, 150 + 45 * i, 650));
            world2Manager.addEntity(new Skeleton(handler, Assets.skeleton, 150 + 55 * i, 700));

            world2Manager.addEntity(new Skeleton(handler, Assets.skeleton, 1200 + 55 * i, 150));
            world2Manager.addEntity(new Slime(handler, Assets.skeleton, 1350 + 45 * i, 250));
            world2Manager.addEntity(new Skeleton(handler, Assets.skeleton, 1100 + 55 * i, 650));

            world2Manager.addEntity(new Skeleton(handler, Assets.skeleton, 750 + 55 * i, 300));
            world2Manager.addEntity(new Slime(handler, Assets.skeleton, 900 + 45 * i, 400));
        }
    }



    public void tick() {
        itemManager.tick();
        entityManager.tick();
        updateEntityManager();
    }

    public void updateEntityManager(){
        if(handler.getWorld().getCountWorld() == 1){
            entityManager = world1Manager;
        } else if(handler.getWorld().getCountWorld() == 2){
            entityManager = world2Manager;
        } else if(handler.getWorld().getCountWorld() == 3){
            entityManager = world3Manager;
        }
        entityManager.removePlayer(player);
        entityManager.setPlayer(player);
    }

    public void spawnEnemy(){
        if(enemyOnBoard < 5){
            spawnTimer += System.currentTimeMillis() - lastSpawnTimer;
            lastSpawnTimer = System.currentTimeMillis();
            if(spawnTimer < spawnCoolDown){
                return;
            }

            enemyOnBoard++;
            int enemyType = 1 + (int) (Math.random()*(2-1+1));
            switch (enemyType){
                case 1: world1Manager.addEntity(new Slime(handler, Assets.skeleton,
                        Math.random()*(1000 - 500 + 1) + 500, Math.random()*(1200 - 700 + 1) + 700)); break;
                case 2: world1Manager.addEntity(new Skeleton(handler, Assets.skeleton,
                        Math.random()*(1000 - 500 + 1) + 500, Math.random()*(1200 - 700 + 1) + 700)); break;
                default: break;
            }

            spawnTimer = 0;
        }

    }

    public void render(GraphicsContext g){

        int xStart = (int) (Math.max(0, handler.getGameCamera().getxOffset() / Settings.TILE_WIDTH));
        int xEnd = (int) (Math.min(width, (handler.getGameCamera().getxOffset() + handler.getWidth()) / Settings.TILE_WIDTH + 1));
        int yStart = (int) (Math.max(0, handler.getGameCamera().getyOffset() / Settings.TILE_HEIGHT));
        int yEnd = (int) (Math.min(height, (handler.getGameCamera().getyOffset() + handler.getHeight()) / Settings.TILE_HEIGHT + 1));

        for(int z = 0; z< layer; z++) {
            for (int y = yStart; y < yEnd; y++) {
                for (int x = xStart; x < xEnd; x++) {

                    getTile(x, y, z).render(g, (int) (x * Settings.TILE_WIDTH - handler.getGameCamera().getxOffset()),
                            (int) (y * Settings.TILE_HEIGHT - handler.getGameCamera().getyOffset()));

                }
            }
        }

        itemManager.render(g);
        entityManager.render(g);
    }

    public Tile getTile(int x, int y, int z){
        if(x < 0 || y < 0 || x >= width || y >= height){
            return Tile.rockTile;
        }
        Tile t = Tile.tiles[tiles[x][y][z]];
        if(t == null)
            return Tile.clear;
        return t;
    }


    public void loadWorld(String path){
        String file = Utils.loadFileAsString(path);
        String[] tokens = file.split("\\s+");
        width = Utils.parseInt(tokens[0]);
        height = Utils.parseInt(tokens[1]);
        spawnXNext = Utils.parseInt(tokens[2]);
        spawnYNext = Utils.parseInt(tokens[3]);
        spawnXPre = Utils.parseInt(tokens[4]);
        spawnYPre = Utils.parseInt(tokens[5]);
        countWorld = Utils.parseInt(tokens[6]);
        layer = Utils.parseInt(tokens[7]);

        tiles = new int[width][height][layer];

        for(int z = 0; z < layer; z++) {
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    tiles[i][j][z] = Utils.parseInt(tokens[width*height*z + i + j * width + 8]);
                }
            }
        }


    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public int getEnemyOnBoard() {
        return enemyOnBoard;
    }

    public void setEnemyOnBoard(int enemyOnBoard) {
        this.enemyOnBoard = enemyOnBoard;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public void setItemManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }
}

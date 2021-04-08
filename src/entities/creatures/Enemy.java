package entities.creatures;

import game.Handler;
import items.Item;
import javafx.scene.image.Image;


import javafx.scene.media.MediaPlayer;
import settings.Settings;
import sounds.Sound;

import java.util.Random;


public abstract class Enemy extends Creature{

    //Music

    //Attack Timer
    private long lastAttackTimer, attackCoolDown = 1000, attackTimer = attackCoolDown;
    private long lastHealthRecover, recoverCoolDown = 100, recoverTimer = recoverCoolDown;

    //Zone
    double enemyX, enemyY, playerX, playerY, distance;
    double homeX, homeY;
    public Enemy(Handler handler,  Image image, double x, double y){
        super(handler, image, x, y, Settings.DEFAULT_CREATURE_WIDTH, Settings.DEFAULT_CREATURE_HEIGHT, 25);
        homeX = x;
        homeY = y;
    }

    @Override
    public void tick() {
        //Movements
        chasePlayerMove();
        backHomeMove();
        move();

        //Recover
        selfRecovery();

        //Attack
        checkAttacks();
    }

    private void checkAttacks(){
        attackTimer += System.currentTimeMillis() - lastAttackTimer;
        lastAttackTimer = System.currentTimeMillis();
        if(attackTimer < attackCoolDown){
            return;
        }

        attackTimer = 0;

        if(checkAttackZone()){
            handler.getWorld().getEntityManager().getPlayer().takeDamage(damage);
            if(!Settings.IS_MUTE){
                if(Sound.hurt.getStatus() == MediaPlayer.Status.PLAYING)
                    Sound.hurt.stop();
                Sound.hurt.play();
            }
        }
    }

    protected boolean checkPlayerZone() {
        enemyX = getCollisionBounds(0,0).getX();
        enemyY = getCollisionBounds(0,0).getY();
        playerX = handler.getWorld().getEntityManager().getPlayer().getCollisionBounds(0,0).getX();
        playerY = handler.getWorld().getEntityManager().getPlayer().getCollisionBounds(0,0).getY();
        distance = (enemyX - playerX)*(enemyX - playerX) + (enemyY - playerY)*(enemyY - playerY);

        return distance < Settings.DISTANCE_PLAYER;
    }

    protected boolean checkAttackZone() {
        enemyX = getCollisionBounds(0,0).getX();
        enemyY = getCollisionBounds(0,0).getY();
        playerX = handler.getWorld().getEntityManager().getPlayer().getCollisionBounds(0,0).getX();
        playerY = handler.getWorld().getEntityManager().getPlayer().getCollisionBounds(0,0).getY();
        distance = (enemyX - playerX)*(enemyX - playerX) + (enemyY - playerY)*(enemyY - playerY);

        return distance < Settings.ATTACK_ZONE;
    }

    public void backHomeMove(){
        if(!checkPlayerZone()){
            if(y > homeY + 1){ //up
                direction = 1;
                yMove = -speed;
            }

            if(y < homeY - 1){ //down
                direction = 2;
                yMove = speed;
            }

            if(x < homeX - 1){ //right
                direction = 3;
                xMove = speed;
            }

            if(x > homeX + 1){ //left
                direction = 4;
                xMove = -speed;
            }

        }
    }

    public void chasePlayerMove(){
        xMove = 0;
        yMove = 0;

        if(checkPlayerZone()){
            if(y > handler.getWorld().getEntityManager().getPlayer().getY() + 1){ //up
                direction = 1;
                yMove = -speed;
            }
            if(y < handler.getWorld().getEntityManager().getPlayer().getY() - 1){ //down
                direction = 2;
                yMove = speed;
            }
            if(x < handler.getWorld().getEntityManager().getPlayer().getX() - 1){ //right
                direction = 3;
                xMove = speed;
            }
            if(x > handler.getWorld().getEntityManager().getPlayer().getX() + 1){ //left
                direction = 4;
                xMove = -speed;
            }
        }
    }

    public void selfRecovery(){
        recoverTimer += System.currentTimeMillis() - lastHealthRecover;
        lastHealthRecover = System.currentTimeMillis();
        if(recoverTimer < recoverCoolDown){
            return;
        }

        recoverTimer = 0;

        if(x == homeX + 1 || x == homeX - 1 || y == homeY + 1 || y == homeY - 1){
            if(health < maxHealth){
                health ++;
            }
        }
    }

    @Override
    public void die() {
        Settings.SCORES++;
        handler.getWorld().setEnemyOnBoard(handler.getWorld().getEnemyOnBoard() - 1);
        System.out.println(Settings.SCORES);
        System.out.println("xin lũiiii mà :(");

        int rand = (int) (Math.random() * 5);
        if(rand == 1){
            handler.getWorld().getItemManager().addItem(Item.lotionHP.createNew((int) x, (int) y));
        }else if(rand == 2){
            handler.getWorld().getItemManager().addItem(Item.lotionMana.createNew((int) x, (int) y));
        }else if(rand == 3) {
            handler.getWorld().getItemManager().addItem(Item.lotionAttack.createNew((int) x, (int) y));
        }
    }
}

/**
 * Purpose: This file holds the functionality for the bullets. A bullet can be
 *          spawned by 3 different entities: player, alien, and boss (alien). The boss
 *          has a different movement (moveHoming) where as the others just use move. A different
 *          velocity is applied depending on if a player shot or an alien shot so that the bullet
 *          can either move up or down. There are booleans attached to who shot.
 *
 * Authors: Camila Grubb, Federico Fernandez, Kateyln Rohrer, Lydia Dufek
 */
package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

import static java.lang.Math.min;

public class Bullet extends Sprite {
    private boolean playerShot;
    private boolean bossShot;
    private GraphicsContext gc;

    public Bullet(Image image, double x, double y) {
        super(image, x, y);
        this.playerShot = false;
    }

    public void drawFrame(GraphicsContext gc) {
        this.gc = gc;
        gc.drawImage(image, x, y);
        updateAABB();
    }

    public void setPlayerShot() {
        playerShot = true;
    }

    public void setBossShot() {
        bossShot = true;
    }

    public boolean getPlayerShot() {
        return playerShot;
    }

    public boolean getBossShot() {
        return bossShot;
    }

    public void move(GraphicsContext gc) {
        if (playerShot) {
            yVelocity = -10;  // Player's bullets move upward
        } else {
            yVelocity = 5;   // Alien's bullets move downward
        }
        y += yVelocity;
    }

    public void moveHoming(GraphicsContext gc, Player player) {
        this.gc = gc;
        double directionX = player.getX() - x;
        double directionY = player.getY() - y;

        double distanceToPlayer = Math.sqrt(directionX * directionX + directionY * directionY);

        if (distanceToPlayer > 0) {
            directionX /= distanceToPlayer;

            double newX = x + directionX * 2; //speed

            x = newX;
            y += (yVelocity*2);
        }
    }

    public void updateAABB(int add) {
        this.AABB = new Rectangle(x, y, this.width + add, this.height + add);
    }

    public void setX(double x) {
        this.x = x;
        updateAABB();
        drawFrame(gc);
    }

    public void setY(double y) {
        this.y = y;
        updateAABB();
        drawFrame(gc);
    }

    @Override
    public String toString() {
        return "Bullet";
    }
}

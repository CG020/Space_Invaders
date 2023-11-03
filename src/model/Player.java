package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import view_controller.GamePane;

public class Player extends Sprite {
    private int lifeAmount;
    private GraphicsContext gc;
    
    public Player(Image image, double x, double y) {
        super(image, x, y);
        lifeAmount = 3; //3 total
        xVelocity = 10;
        yVelocity = 0;
    }

    public void drawFrame(GraphicsContext gc) {
        gc.drawImage(image, x, y);
    }

    public void shoot(Bullet bullet, GraphicsContext gc, Player player, GamePane pane) {
        bullet.moveUp(gc);
//        System.out.println("PEW PEW PEW!!");
    }

    public void moveLeft(GraphicsContext gc) {
        x -= xVelocity;
        drawFrame(gc);
        updateAABB();
    }

    public void moveRight(GraphicsContext gc) {
        x += xVelocity;
        drawFrame(gc);
        updateAABB();
    }

    public void updateLives() {
        lifeAmount--;
    }

    public void updateAABB() {
        this.AABB = new Rectangle(x, y, this.width, this.height);
    }

}

package model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;

public class Player extends Sprite {
    private int lifeAmount;

    public Player(Image image, double x, double y) {
        super(image, x, y);
        lifeAmount = 3; //3 total
        xVelocity = 10;
        yVelocity = 0;
    }

    public void drawFrame(GraphicsContext gc) {
        gc.drawImage(image, x, y);
    }

    public void shoot() {
        System.out.println("PEW PEW PEW!!");
    }

    public void moveLeft(GraphicsContext gc) {
        x -= xVelocity;
        drawFrame(gc);
    }

    public void moveRight(GraphicsContext gc) {
        x += xVelocity;
        drawFrame(gc);
    }

    public void updateLives() {
        lifeAmount--;
    }
}
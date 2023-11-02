package view_controller;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import model.Alien;
import model.Bullet;
import model.Player;
import model.Sprite;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class GamePane {
    private Player player;
    private ArrayList<Sprite> objects;
    private Canvas canvas;
    private GraphicsContext gc;
    private boolean isShooting = false;
    private Scene scene;

    public GamePane(Scene scene) {
        this.scene = scene; // Store a reference to the scene
        canvas = new Canvas(500, 650);
        gc = canvas.getGraphicsContext2D();
        objects = new ArrayList<Sprite>();

        drawPlayer();
        drawAliens();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<Sprite> getObjects() {
        return objects;
    }

    private void drawPlayer() {
        Image image = readImage("realShip.png");
        player = new Player(image, (canvas.getWidth() / 2) - (image.getWidth() / 2), canvas.getHeight() - image.getHeight() * 2.8);
        objects.add(player);
        player.drawFrame(gc);
    }

    private void drawAliens() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 11; j++) {
                Image image = readImage("bullet.png");
                Alien alien = new Alien(image, 25 + 40*j, 100 + 50*i, 1);
                objects.add(alien);
                alien.drawFrame(gc);
            }
        }
    }

    public void shoot() {
        isShooting = true;
        Image image = readImage("bullet.png");
        Bullet bullet = new Bullet(image, player.getX() + player.getWidth() / 2 - (image.getWidth() / 2), player.getY()-50);
        objects.add(bullet);
    }

    public void moveLeft() {
        player.moveLeft(gc);
    }

    public void moveRight() {
        player.moveRight(gc);
    }

    public void gameLoop() {
        new AnimationTimer() {
            long lastNanoTime = System.nanoTime();

            @Override
            public void handle(long currentNanoTime) {
                double elapsedTime = (currentNanoTime - lastNanoTime) / 1e9;

                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

                scene.setOnKeyPressed(keyEvent -> {
                    if (keyEvent.getCode() == KeyCode.SPACE) {
                        // The user pressed the spacebar, handle shooting
                        shoot();
                    } else if (keyEvent.getCode() == KeyCode.LEFT) {
                        moveLeft();
                    } else if (keyEvent.getCode() == KeyCode.RIGHT) {
                        moveRight();
                    }
                });

                if (isShooting) {
                    for (Sprite object : objects) {
                        if (object instanceof Bullet) {
                            ((Bullet) object).moveUp(gc);
                        }
                    }
                }

                for (int i = objects.size() - 1; i >= 0; i--) {
                    Sprite object = objects.get(i);
                    if (object instanceof Bullet) {
                        if (object.getX() < 0 || object.getX() > canvas.getWidth() || object.getY() < 0 || object.getY() > canvas.getHeight()) {
                            objects.remove(i);
                        }
                    }
                }

                detectAndHandleCollisions();

                //Rendering
                drawFrame();

                lastNanoTime = currentNanoTime;
            }
        }.start();
    }

    public void drawAABB(Sprite object) {
        gc.setStroke(Color.WHITE);
        gc.strokeRect(object.getAABB().getX(), object.getAABB().getY(), object.getAABB().getWidth(), object.getAABB().getHeight());
    }

    public void drawFrame() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Sprite object : objects) {
            object.drawFrame(gc);
            drawAABB(object); //draw aabb
        }
    }

    private void detectAndHandleCollisions() {
        List<Sprite> objectsToRemove = new ArrayList<>();

        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                Sprite object1 = objects.get(i);
                Sprite object2 = objects.get(j);

                if (isCollided(object1.getAABB(), object2.getAABB())) {
                    if(object1 instanceof Alien && object2 instanceof Bullet) {
                        objects.remove(object1);
                        objects.remove(object2);
                    }
                }
            }
        }
    }

    public Image readImage(String filePath) {
        FileInputStream imagePath;
        try {
            imagePath = new FileInputStream("lib/" + filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return new Image(imagePath);
    }


    public boolean isCollided(Rectangle obj1, Rectangle obj2) {
        double obj1Top = obj1.getY();
        double obj1Bottom = obj1Top + obj1.getHeight();
        double obj1Left = obj1.getX();
        double obj1Right = obj1Left + obj1.getWidth();

        double obj2Top = obj2.getY();
        double obj2Bottom = obj2Top + obj2.getHeight();
        double obj2Left = obj2.getX();
        double obj2Right = obj2Left + obj2.getWidth();

        boolean comp1 = obj1Bottom > obj2Top;
        boolean comp2 = obj1Top < obj2Bottom;
        boolean comp3 = obj1Right > obj2Left;
        boolean comp4 = obj1Left < obj2Right;

        return comp1 && comp2 && comp3 && comp4;
    }
}
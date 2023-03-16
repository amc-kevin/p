

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.Arrays;
public class Entity extends ImageView {
    double width, height;
    int[] freeDir = new int[4];
    int dirCounter = 0;
    int opDir = 0;
    int lastDir = 0;
    
    public Entity(String fileName, double locX, double locY, double width, double height) {
        Image image = new Image(fileName, width, height, false, false);
        setImage(image);

        setLayoutX(locX);
        setLayoutY(locY);

        this.width = width;
        this.height = height;
    }
    
    public void playerMove(int speedX, int speedY) {
        setLayoutX(speedX + getLayoutX());
        setLayoutY(speedY + getLayoutY());
    }
    public boolean collision(Entity col) {
        double dx = Math.abs(getLayoutX() - col.getLayoutX());
        double dy = Math.abs(getLayoutY() - col.getLayoutY());
        return dx < width && dy < height;
    }
    public boolean coinGet(Entity coin) {
        double dx = Math.abs(getLayoutX() - coin.getLayoutX());
        double dy = Math.abs(getLayoutY() - coin.getLayoutY());
        return dx < width - 12 && dy < height - 12;
    }

    public boolean starGet(Entity star) {
        double dx = Math.abs(getLayoutX() - star.getLayoutX());
        double dy = Math.abs(getLayoutY() - star.getLayoutY());
        return dx < width - 12 && dy < height - 12;
    }

    public void ghostMove(Entity ghost, ArrayList<Entity> wall) {
        if (ghost.getLayoutX() % 50 == 0 && ghost.getLayoutY() % 50 == 0) {
            directionView(ghost, wall);
            newDir(ghost);
        } else {
            stopMoving(ghost);
        }
    }
    private void stopMoving(Entity ghost) {
        switch (lastDir) {
            case 0 -> ghost.setLayoutY(ghost.getLayoutY() - 10);
            case 1 -> ghost.setLayoutX(ghost.getLayoutX() - 10);
            case 2 -> ghost.setLayoutY(ghost.getLayoutY() + 10);
            case 3 -> ghost.setLayoutX(ghost.getLayoutX() + 10);
        }
    }

    private void directionView(Entity ghost, ArrayList<Entity> wall) {
        double x = ghost.getLayoutX();
        double y = ghost.getLayoutY();

        for (int i = 0; i < 4; i++) {
            double dx = (i == 1) ? -10 : (i == 3) ? 20 : 0;
            double dy = (i == 0) ? -10 : (i == 2) ? 20 : 0;
            ghost.setLayoutX(x + dx);
            ghost.setLayoutY(y + dy);
            freeDir[i] = 1;
            for (var s : wall) {
                if (s.collision(ghost)) {
                    freeDir[i] = 0;
                    break;
                }
            }
        }
        ghost.setLayoutX(x);
        ghost.setLayoutY(y);
        dirCounter = (int) Arrays.stream(freeDir).filter(dir -> dir == 1).count();
    }

    private void newDir(Entity ghost) {
        int direction;
        if (dirCounter == 1) {
            direction = lastDir ^ 2;
        } else if (dirCounter == 2 && freeDir[lastDir] == 1) {
            direction = lastDir;
        } else {
            do {
                direction = (int) (Math.random() * 4);
            } while (direction == opDir || freeDir[direction] != 1);
        }
        int[] xy = {0, -10, 0, 10};
        ghost.setLayoutX(ghost.getLayoutX() + xy[direction]);
        ghost.setLayoutY(ghost.getLayoutY() + xy[direction ^ 1]);
        lastDir = direction;
        opDir = direction ^ 2;
    }
}

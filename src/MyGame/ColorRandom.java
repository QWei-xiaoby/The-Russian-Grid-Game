package MyGame;

import java.awt.*;
import java.util.Random;

public class ColorRandom {
    Random rand = new Random();
    int r;
    int g;
    int b;

    public Color getRandomColor() {
        r = rand.nextInt(256);
        g = rand.nextInt(256);
        b = rand.nextInt(256);
        return new Color(r, g, b);
    }
}

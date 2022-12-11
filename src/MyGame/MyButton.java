package MyGame;

import java.awt.*;

public class MyButton extends Button {
    public MyButton(String label){
        super(label);
        setPreferredSize (new Dimension (70,25));
        setBackground(new Color(231, 228, 228));
        setFocusable(false);
    }
}

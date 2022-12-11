package MyGame;

import javax.swing.*;
import java.awt.*;

public class Header extends JPanel{
    public Header(){
        setBounds(0,0,680,25);
        setBackground(new Color(248, 247, 247));
        MyButton setting = new MyButton("Setting");
        MyButton option = new MyButton("Option");
        MyButton help = new MyButton("Help");
        setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        add(setting);
        add(option);
        add(help);
    }
}

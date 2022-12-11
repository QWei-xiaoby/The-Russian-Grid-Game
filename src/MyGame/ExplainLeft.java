package MyGame;

import javax.swing.*;
import java.awt.*;

public class ExplainLeft extends JPanel {
    public ExplainLeft() {
        setBounds(0, 25, 115, 850);
        setBackground(new Color(232, 195, 195));


        setLayout(null);
        add(new MyLabel("变换:\nW Up",100));
        add(new MyLabel("向右移动：D",200));
        add(new MyLabel("向左移动：A",300));
        add(new MyLabel("向下移动：S",400));
    }
    public class MyLabel extends JLabel{
        public MyLabel(String text,int x){
            super(text);
            setPreferredSize(new Dimension(250, 100));
            setHorizontalAlignment(JLabel.CENTER);
            setBackground(Color.white);
            setFont(new Font("宋体", Font.PLAIN, 18));
            setBounds(5,x,100,26);
        }
    }
}

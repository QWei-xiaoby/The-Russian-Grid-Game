package Area;

import javax.swing.*;
import java.awt.*;

public class ExplainLeft extends JPanel {
    public static final int width = 167;
    public static final int height = 121;

    public ExplainLeft() {
        setBounds(0, 25, 165, 850);
        setBackground(new Color(230, 230, 230));
        setLayout(new GridLayout(6, 1));

        //六个说明的图片
        add(ex("img/up.png"));
        add(ex("img/down.png"));
        add(ex("img/left.png"));
        add(ex("img/right.png"));
        add(ex("img/r.png"));
        add(ex("img/space.png"));

    }

    public JLabel ex(String s) {
        JLabel jlb = new JLabel();
        ImageIcon image = new ImageIcon(s);//实例化ImageIcon 对象
        image.setImage(image.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));//可以用下面三句代码来代替
        jlb.setIcon(image);
        jlb.setSize(width, height);
        return jlb;
    }
}

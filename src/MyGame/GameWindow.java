package MyGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameWindow extends JFrame implements KeyListener {
    public static void main(String[] args) {
        new GameWindow();
    }

    private static final int x_game = 12;
    private static final int y_game = 26;
    private JTextArea[][] text;// 把整个界面变为一个文本区域，整个游戏在里面进行
    private int[][] data; // 对于每个格子的数据，1 代表有方块，0 代表为空白区

    GameWindow() {
        //初始化大小
        setBounds(420, 100, 698, 915);
        //窗口可见
        setVisible(true);
        //关闭窗口结束程序
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
        //设置禁止调整大小
        setResizable(false);
        //空布局
        setLayout(null);

        //四个面板 对应菜单、左右说明、游戏区域
        add(new Header());
        add(new ExplainLeft());
        add(new ExplainRight());
        initContent();
    }

    private void initContent() {
        text = new JTextArea[x_game][y_game];
        data = new int[x_game][y_game];


        JPanel content = new JPanel();
        content.setBounds(115, 25, 450, 850);
        content.setBackground(new Color(144, 241, 144));
        content.setLayout(new GridLayout(26,12,1,1));

        add(content);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

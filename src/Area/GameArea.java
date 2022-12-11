package Area;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;

import static MyGame.GameWindow.game_y;
import static MyGame.GameWindow.game_x;

public class GameArea extends JPanel {
    public final int[] allRect;
    public JTextArea[][] text;// 把整个界面变为一个文本区域，整个游戏在里面进行
    public int[][] data; // 对于每个格子的数据，1 代表有方块，0 代表为空白区

    public GameArea() {
        setBounds(115, 25, 450, 850);
        setBackground(new Color(144, 241, 144));
        setLayout(new GridLayout(game_y, game_x, 1, 1));
        text = new JTextArea[game_y][game_x];
        data = new int[game_y][game_x];
        allRect = new int[]{//19 种方块形状，如 0x00cc 就是 0000 表示一个 2 * 2 的正方形方块
                0x00cc, 0x8888, 0x000f, 0x0c44, 0x002e,
                0x088c, 0x00e8, 0x0c88, 0x00e2, 0x044c,
                0x008e, 0x08c4, 0x006c, 0x04c8, 0x00c6,
                0x08c8, 0x004e, 0x04c4, 0x00e4};
        for (int i = 0; i < text.length; i++) {
            for (int j = 0; j < text[i].length; j++) {
                text[i][j] = new JTextArea(20, 20);
                text[i][j].setBackground(new Color(232, 103, 103));
                text[i][j].addKeyListener((KeyListener) this);

                if (i == 0 || j == 0 || j == 11) {
                    text[i][j].setBackground(new Color(0, 0, 0));
                    data[i][j] = 1;
                }
                text[i][j].setEditable(false);
                add(text[i][j]);
            }
        }
    }
    public JPanel get(){
        return this;
    }

}

package MyGame;

import Area.ExplainLeft;
import Area.ExplainRight;
import Area.Header;

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

    //常量_行列数
    public static final int game_y = 26;
    public static final int game_x = 12;

    // 把一个20x20的textarea假装它是一个方块
    public JTextArea[][] text;

    // 对于每个格子的数据，1 代表有方块，0 代表为空白区
    public boolean[][] data;

    // 所有的方块类型，用 16 个字节来存储，俄罗斯方块图形都是在 4*4 格子里
    public int[] allRect;

    //左右头部、左右说明区域、游戏区域的引用
    Header header;
    ExplainRight labelRight;
    ExplainLeft labelLeft;
    JPanel cen;

    // 全局布尔用于判断游戏是否结束
    public boolean running;

    //当前方块；下一个方块；坐标；成绩
    public int rect, nextRect, x, y, score = 0;
    public Color nowColor, nextColor;


    GameWindow() {
        //初始化主界面大小
        setSize(800, 915);
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
        // 居中
        setLocationRelativeTo(null);
        //把四个引用都拿上
        header = new Header();
        labelLeft = new ExplainLeft();
        labelRight = new ExplainRight();
        add(header);
        add(labelRight);
        add(labelLeft);
        cen = initContent();
    }

    private JPanel initContent() {
        text = new JTextArea[game_y][game_x];
        data = new boolean[game_y][game_x];
        allRect = new int[]{//19 种方块形状，如 0x00cc 就是 0000 表示一个 2 * 2 的正方形方块
                0x00cc, 0x8888, 0x000f, 0x0c44, 0x002e,
                0x088c, 0x00e8, 0x0c88, 0x00e2, 0x044c,
                0x008e, 0x08c4, 0x006c, 0x04c8, 0x00c6,
                0x08c8, 0x004e, 0x04c4, 0x00e4};

        //中间区域初始化
        JPanel content = new JPanel();
        content.setBounds(163, 25, 450, 850);
        content.setBackground(new Color(241, 241, 241));
        content.setLayout(new GridLayout(game_y, game_x, 0, 0));
        for (int i = 0; i < text.length; i++) {
            for (int j = 0; j < text[i].length; j++) {
                text[i][j] = new JTextArea(20, 20);
                text[i][j].setBackground(new Color(255, 255, 255));
                text[i][j].addKeyListener(this);

                if (i == 25 || j == 0 || j == 11) {
                    text[i][j].setBackground(new Color(0, 0, 0));
                    data[i][j] = true;
                }
                text[i][j].setEditable(false);
                content.add(text[i][j]);
            }
        }
        this.add(content);
        this.setVisible(true);// 刷新可

        //初始化下一个方块和颜色，初始化running为true
        nextColor = new ColorRandom().getRandomColor();
        nextRect = allRect[(int) (Math.random() * 19)];
        running = true;
        start();
        return content;
    }

    //游戏循环
    public void start() {
        while (running) {
            ranRect();
            try {
                dropDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("游戏结束");
    }

    //随机下个方块，并随机出颜色
    public void ranRect() {
        nowColor = nextColor;
        nextColor = new ColorRandom().getRandomColor();
        rect = nextRect;
        nextRect = allRect[(int) (Math.random() * 19)];
        // 随机生成方块类型（共 7 种，19 个形状）
    }

    //下落生产的方块
    public void dropDown() throws InterruptedException {
        x = 5;
        y = 0;
        for (int i = 0; i < 26; i++) {
            //下落延时
            Thread.sleep(100);
            //如果下面有东西
            if (isDropDown(x, y)) {
                //如果下面有东西,说明不能落了，所以把这个方块所在的格子变为true
                saveDate(x, y);
                //判断刚刚落下的方块影响到的四层是否能消；
                for (int k = x; k < x + 4; k++) {
                    int j = 1;
                    while (!data[k][j++]) {
                        if (j == 11)
                            removeRow(k);
                    }
                }
                for (int j = 1; j <= 10; j++) {//游戏最上面的 4 层不能有方块，否则游戏失败
                    if (data[3][j]) {
                        running = false;
                        break;
                    }
                }
            } else {//如果可以掉落
                y++;// 层加一
                fall(x, y);// 掉下来一层
            }
        }
    }


    //方块往下移动一层
    private void fall(int x, int y) {
        if (y > 0)// 方块下落一层时
            clear(x, y - 1);// 清除上一层有颜色的方块
        draw(x, y);// 重新绘制方块图像
    }

    //把当前方块在x，y处画出来
    private void draw(int x, int y) {
        int temp = 0x8000;//表示 1000 0000 0000 0000
        for (int i = 0; i < 4; i++) {//循环遍历 16 个方格（4*4）
            for (int j = 0; j < 4; j++) {
                if ((temp & rect) != 0) {// 此处有方块时
                    text[y][x].setBackground(nowColor);//有方块的地方变为nowColor
                }
                x++;//下一列
                temp >>= 1;
            }
            y++;//下一行
            x = x - 4;//回到首列
        }
    }

    //把x，y处的当前方块删了
    private void clear(int x, int y) {
        int temp = 0x8000;//表示 1000 0000 0000 0000
        for (int i = 0; i < 4; i++) {//循环遍历 16 个方格（4*4）
            for (int j = 0; j < 4; j++) {
                if ((temp & rect) != 0) {// 此处有方块时
                    text[y][x].setBackground(new Color(255, 255, 255));//清除颜色，变为白色
                }
                x++;//下一列
                temp >>= 1;
            }
            y++;//下一行
            x = x - 4;//回到首列
        }
    }

    private void removeRow(int k) {


    }

    //判断下面有没有东西 ；有的话返回true
    public boolean isDropDown(int x, int y) {
        int temp = 0x8000;//表示 1000 0000 0000 0000
        for (int i = 0; i < 4; i++) {//循环遍历 16 个方格（4*4）
            for (int j = 0; j < 4; j++) {
                //此处有方块,并且下一格data域已存在方块
                if ((temp & rect) != 0 && data[y + 1][x]) {// 此处有方块时
                    return true;
                }
                x++;//列加一
                temp >>= 1;
            }
            y++;// 下一行
            x = x - 4;// 回到首列
        }
        return false;
    }

    //固定当前方块，刷新data域
    public void saveDate(int x, int y) {
        int temp = 0x8000;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((temp & rect) != 0) {
                    data[y][x] = true;
                }
                x++;
                temp >>= 1;
            }
            y++;
            x = x - 4;
        }
    }


    //键盘监听事件


    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == 65 || code == 37) {
//            myMove(-1, 0);
            moveLeft();

        } else if (code == 68 || code == 39) {
            myMove(1, 0);
        }
    }

    private void moveLeft() {
        //到了最左边直接返回
        if (!running || x <= 1)
            return;
        //先判断能不能左移
        int temp = 0x8000;
        for (int i = x; i < x + 4; i++) {
            for (int j = y; j < y + 4; j++) {
                //若此处有方块，并且左边也有
                if ((rect & temp) != 0 && data[i - 1][j]) {
                    return;
                }
                temp >>= 1;
            }
        }
        //没return说明能左移
        //先清除当前图像，再在左一格绘制
        clear(x, y);
        x--;
        draw(x, y);
    }

    private void myMove(int moveX, int moveY) {
        //到了最左边直接返回
        if (!running || x <= 1 && moveX < 0)
            return;
        else if (x >= 10 && moveX > 0)
            return;

        //先判断能不能移动
        int temp = 0x8000;
        for (int i = x; i < x + 4; i++) {
            for (int j = y; j < y + 4; j++) {
                //若此处有方块，并且移动moveX,Y格后也有
                if ((rect & temp) != 0 && data[i + moveX][j + moveY]) {
                    return;
                }
                temp >>= 1;
            }
        }
        //没return说明能左移
        //先清除当前图像，再在左一格绘制
        clear(x, y);
        x += moveX;
        y += moveY;
        draw(x, y);
    }

    private void moveRight() {
        if (!running || x >= 10)
            return;


    }


    //这两个先不写
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}

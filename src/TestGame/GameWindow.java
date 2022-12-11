package TestGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * 游戏界面 实际上是一个 二维数组
 * date数组代表着格子
 * allRect 方块的类型 存在一个int数组里面
 * x y ：当前方块的位置
 * score ：  成绩
 */

public class GameWindow extends JFrame implements KeyListener {
    public static void main(String[] args) {
        //创建 Main 对象，主要用于初始化数据
        new GameWindow().go();
    }

    private final JTextArea[][] grids;// 把整个界面变为一个文本区域，整个游戏在里面进行
    private final int[][] data; // 对于每个格子的数据，1 代表有方块，0 代表为空白区
    private final int[] allRect; // 所有的方块类型，用 16 个字节来存储，俄罗斯方块图形都是在 4*4 格子里
    private final JLabel label; // 显示分数的标签
    private final JLabel label1;// 显示游戏是否结束
    private boolean running; // 全局布尔用于判断游戏是否结束

    //游戏框架的无参构造方法
    public GameWindow() {
        grids = new JTextArea[26][12];//设置游戏区域行和列
        data = new int[26][12];//开辟 data 数组空间与游戏区域行和列一致
        allRect = new int[]{//19 种方块形状，如 0x00cc 就是 0000 表示一个 2 * 2 的正方形方块
                0x00cc, 0x8888, 0x000f, 0x0c44, 0x002e,
                0x088c, 0x00e8, 0x0c88, 0x00e2, 0x044c,
                0x008e, 0x08c4, 0x006c, 0x04c8, 0x00c6,
                0x08c8, 0x004e, 0x04c4, 0x00e4};

        label = new JLabel("分数: 0"); //此标签存放得分情况，初始化为 0 分
        label1 = new JLabel("开始游戏"); //此标签为提示游戏状态：开始还是结束
        running = false; //为标志变量，false 为游戏结束，true 为游戏正在进行
        init(); // 游戏界面初始化
    }

    public void init() {
        JPanel center = new JPanel(); //此面板为游戏核心区域
        JPanel right = new JPanel(); //此面板为游戏说明区域
        center.setLayout(new GridLayout(26, 12, 1, 1)); //给游戏核心区域划分行、列共 26 行，12 列
        for (int i = 0; i < grids.length; i++) {//初始化面板
            for (int j = 0; j < grids[i].length; j++) {
                grids[i][j] = new JTextArea(20, 20);
                grids[i][j].setBackground(Color.WHITE);
                grids[i][j].addKeyListener(this);// 添加键盘监听事件
                // 初始化游戏边界
                if (j == 0 || j == grids[i].length - 1 || i == grids.length - 1) {
                    grids[i][j].setBackground(Color.black);
                    data[i][j] = 1;
                }
                grids[i][j].setEditable(false);// 文本区域不可编辑
                center.add(grids[i][j]); //把文本区域添加到主面板上
            }
        }
        //初始化游戏说明面板
        right.setLayout(new GridLayout(4, 1));
        right.add(new JLabel(" 左 : left 右 :right"));
        right.add(new JLabel(" 下 : down 变化 : change"));
        right.add(label);
        label1.setForeground(Color.black);// 设置标签内容为红色字体
        right.add(label1);
        //把主面板和说明面板添加到窗体中
        this.setLayout(new BorderLayout());
        this.add(center, BorderLayout.CENTER);
        this.add(right, BorderLayout.EAST);
        running = true; //初始化 running 状态为 true,表示程序运行即游戏开始
        this.setSize(600, 850);// 设置窗体大小
        this.setVisible(true);// 窗体可见
        this.setLocationRelativeTo(null);// 设置窗体居中
        this.setResizable(false);// 窗体大小不可改变
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 释放窗体
    }

    private int rect; // 当前游戏下落的方块类型；
    private int x, y; // 当前方块的坐标位置，x 代表行，y 代表列
    private int score = 0; // 记录当前游戏得分情况，每消一层得 10 分

    public void go() {
        //游戏循环进行的条件为 running
        while (running) {
            //若running为false,则必然是被游戏中的游戏结束判断函数调整了

            ranRect();// 绘制下落方格形状
            start();// 开始游戏
        }
        label1.setText("游戏结束！");//则游戏结束
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'a') {// 方格进行左移
            if (!running) {
                return;
            }
            if (y <= 1)//碰到左边墙壁时
                return;
            int temp = 0x8000;//表示 1000 0000 0000 0000
            for (int i = x; i < x + 4; i++) {//循环遍历 16 个方格（4*4）
                for (int j = y; j < y + 4; j++) {
                    if ((rect & temp) != 0) {// 此处有方块时
                        if (data[i][j - 1] == 1) {//如果左移一格有方块时
                            return;
                        }
                    }
                    temp >>= 1;
                }
            }
            clear(x, y);//可以进行左移操作时，清除左移前方块颜色
            y--;
            draw(x, y);//然后重新绘制左移后方块的图像
        }
        if (e.getKeyChar() == 'd') {//方块进行右移操作
            if (!running) {
                return;
            }

            int temp = 0x8000;
            int m = x, n = y;
            int num = 7;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if ((temp & rect) != 0) {
                        if (n > num) {
                            num = n;
                        }
                    }
                    temp >>= 1;
                    n++;
                }
                m++;
                n = n - 4;
            }
            if (num >= 10) {
                return;
            }
            temp = 0x8000;
            for (int i = x; i < x + 4; i++) {
                for (int j = y; j < y + 4; j++) {
                    if ((rect & temp) != 0) {
                        if (data[i][j + 1] == 1) {
                            return;
                        }
                    }
                    temp >>= 1;
                }
            }
            clear(x, y);//可以进行右移操作时，清除右移前方块颜色
            y++;
            draw(x, y);//然后重新绘制右移后方块的图像
        }
        if (e.getKeyChar() == 's') {//方块进行下移操作
            if (!running) {
                return;
            }
            if (canFall(x, y)) {
                saveData(x, y);
                return;
            }
            clear(x, y);//可以进行下移操作时，清除下移前方块颜色
            x++;
            draw(x, y);//然后重新绘制下移后方块的图像
        }
        if (e.getKeyChar() == 'w' || e.getKeyCode() == KeyEvent.VK_UP) {//改变方块形状
            if (!running) {
                return;
            }
            int i;
            for (i = 0; i < allRect.length; i++) {//循环遍历 19 个方块形状
                if (allRect[i] == rect)//找到下落的方块对应的形状，然后进行形状改变
                    break;
            }
            if (i == 0)//为正方形方块无需形状改变，为方块图形种类 1
                return;
            clear(x, y);
            if (i == 1 || i == 2) {//为方块图形种类 2
                rect = allRect[i == 1 ? 2 : 1];
                if (y > 7)
                    y = 7;
            }
            if (i >= 3 && i <= 6) {//为方块图形种类 3
                rect = allRect[i + 1 > 6 ? 3 : i + 1];
            }
            if (i >= 7 && i <= 10) {//为方块图形种类 4
                rect = allRect[i + 1 > 10 ? 7 : i + 1];
            }
            if (i == 11 || i == 12) {//为方块图形种类 5
                rect = allRect[i == 11 ? 12 : 11];
            }
            if (i == 13 || i == 14) {//为方块图形种类 6
                rect = allRect[i == 13 ? 14 : 13];
            }
            if (i >= 15 && i <= 18) {//为方块图形种类 7
                rect = allRect[i + 1 > 18 ? 15 : i + 1];
            }
            draw(x, y);
        }
    }


    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {//改变方块形状
            if (!running) {
                return;
            }
            int i;
            for (i = 0; i < allRect.length; i++) {//循环遍历 19 个方块形状
                if (allRect[i] == rect)//找到下落的方块对应的形状，然后进行形状改变
                    break;
            }
            if (i == 0)//为正方形方块无需形状改变，为方块图形种类 1
                return;
            clear(x, y);
            if (i == 1 || i == 2) {//为方块图形种类 2
                rect = allRect[i == 1 ? 2 : 1];
                if (y > 7)
                    y = 7;
            }
            if (i >= 3 && i <= 6) {//为方块图形种类 3
                rect = allRect[i + 1 > 6 ? 3 : i + 1];
            }
            if (i >= 7 && i <= 10) {//为方块图形种类 4
                rect = allRect[i + 1 > 10 ? 7 : i + 1];
            }
            if (i == 11 || i == 12) {//为方块图形种类 5
                rect = allRect[i == 11 ? 12 : 11];
            }
            if (i == 13 || i == 14) {//为方块图形种类 6
                rect = allRect[i == 13 ? 14 : 13];
            }
            if (i >= 15 && i <= 18) {//为方块图形种类 7
                rect = allRect[i + 1 > 18 ? 15 : i + 1];
            }
            draw(x, y);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }




    public void ranRect() {
        rect = allRect[(int) (Math.random() * 19)];// 随机生成方块类型（共 7 种，19 个形状）
    }

    public void start() {
        x = 0;
        y = 5; //初始化下落方块的位置
        for (int i = 0; i < 26; i++) {//共 26 层，一层一层下落
            try {
                Thread.sleep(1000);//每层延时 1 秒
                if (canFall(x, y)) {// 如果不可以掉落
                    saveData(x, y);//把此方块区域 data[][]标志为 1，表示有数据
                    for (int k = x; k < x + 4; k++) {//循环遍历 4 层，看是否有哪一层都有方块的情况，以便消除那一行方格和统计得分
                        int sum = 0;
                        for (int j = 1; j <= 10; j++) {
                            if (data[k][j] == 1) {
                                sum++;
                            }
                        }
                        if (sum == 10) {//如果 k 层都有方块，则消除 k 层方块
                            removeRow(k);
                        }
                    }
                    for (int j = 1; j <= 10; j++) {//游戏最上面的 4 层不能有方块，否则游戏失败
                        if (data[3][j] == 1) {
                            running = false;
                            break;
                        }
                    }
                    break;
                }
// 如果可以掉落
                x++;// 层加一
                fall(x, y);// 掉下来一层
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean canFall(int m, int n) {
        int temp = 0x8000;//表示 1000 0000 0000 0000
        for (int i = 0; i < 4; i++) {//循环遍历 16 个方格（4*4）
            for (int j = 0; j < 4; j++) {
                if ((temp & rect) != 0) {// 此处有方块时
                    if (data[m + 1][n] == 1)// 如果下一个地方有方块，则直接返回 false
                        return true;
                }
                n++;//列加一
                temp >>= 1;
            }
            m++;// 下一行
            n = n - 4;// 回到首列
        }
        return false;//可以掉落返回 true
    }

    public void saveData(int m, int n) {
        int temp = 0x8000;//表示 1000 0000 0000 0000
        for (int i = 0; i < 4; i++) {//循环遍历 16 个方格（4*4）
            for (int j = 0; j < 4; j++) {
                if ((temp & rect) != 0) {// 此处有方块时
                    data[m][n] = 1;//data 数组存放为 1
                }
                n++;//下一列
                temp >>= 1;
            }
            m++;// 下一行
            n = n - 4;// 回到首列
        }
    }

    public void removeRow(int row) {
        for (int i = row; i >= 1; i--) {
            System.arraycopy(data[i - 1], 1, data[i], 1, 10);
        }
        refresh();// 刷新移除 row 行方块后的游戏主面板区域
        score += 10;// 分数加 10；
        label.setText("分数: " + score);//显示得分
    }

    public void refresh() {
        for (int i = 1; i < 25; i++) {
            for (int j = 1; j < 11; j++) {
                if (data[i][j] == 1) {//有方块的地方把方块设置为绿色
                    grids[i][j].setBackground(Color.red);
                } else {//无方块的地方把方块设置为白色
                    grids[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    public void fall(int m, int n) {
        if (m > 0)// 方块下落一层时
            clear(m - 1, n);// 清除上一层有颜色的方块
        draw(m, n);// 重新绘制方块图像
    }

    public void clear(int m, int n) {
        int temp = 0x8000;//表示 1000 0000 0000 0000
        for (int i = 0; i < 4; i++) {//循环遍历 16 个方格（4*4）
            for (int j = 0; j < 4; j++) {
                if ((temp & rect) != 0) {// 此处有方块时
                    grids[m][n].setBackground(Color.WHITE);//清除颜色，变为白色
                }
                n++;//下一列
                temp >>= 1;
            }
            m++;//下一行
            n = n - 4;//回到首列
        }
    }

    public void draw(int m, int n) {
        int temp = 0x8000;//表示 1000 0000 0000 0000
        for (int i = 0; i < 4; i++) {//循环遍历 16 个方格（4*4）
            for (int j = 0; j < 4; j++) {
                if ((temp & rect) != 0) {// 此处有方块时
                    grids[m][n].setBackground(Color.red);//有方块的地方变为绿色
                }
                n++;//下一列
                temp >>= 1;
            }
            m++;//下一行
            n = n - 4;//回到首列
        }
    }

}

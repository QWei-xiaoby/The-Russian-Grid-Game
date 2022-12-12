package MyGame;

public class Main {
    public static void main(String[] args) throws InterruptedException {
//        GameWindow gameWindow = new GameWindow();
        int rect = 0x0008;
        int x = 5;
        int rightLocation = 1;
        int tmp = 1;
        for (int i = 0; i < 4; i++) {
            if((rect & (tmp<<i)) != 0
                    || (rect & (tmp<<i+4)) != 0
                    || (rect & (tmp<<i+8)) != 0
                    || (rect & (tmp<<i+12)) != 0){
                rightLocation = x+3-i;
                break;
            }
        }
        System.out.println(rightLocation);
    }
}

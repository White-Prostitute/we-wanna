import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.*;
import java.util.Timer;

public class MainFrame extends JFrame {

    Image bg_img;
    Mouse mouse = new Mouse(this);
    Setting setting = new Setting(this);
    Mario mario = Mario.getInstance(this);
    Map map = Map.getInstance(this);
    InnerMapEditor innerMapEditor = InnerMapEditor.getInstance();
    Menu menu = new Menu(this, mouse);
    Help help = new Help();
    Bgm bgm = Bgm.getInstance();
    //线程的创建
    Refresh refresh = new Refresh();
    marioTask marioTask = new marioTask();
    Timer refreshTimer = new Timer();
    Timer marioTimer = new Timer();
    //鼠标与键盘监听
    MarioKeyListener marioKeyListener = new MarioKeyListener();
    MarioMouseListener marioMouseListener = new MarioMouseListener();

    int flag = 0;//游戏状态的记录
    int fps = 100;//固定的刷新率
    int timer = 0;
    //构造方法
    public MainFrame() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        //窗口初始化
        this.setSize(1000, 700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("we wanna");
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setLayout(null);
        this.setVisible(true);
        this.addKeyListener(marioKeyListener);
        this.addMouseListener(marioMouseListener);

        bg_img = new ImageIcon("image/background.png").getImage();
        bgm.loadBGM();
        bgm.playMenuBGM();
        refreshTimer.scheduleAtFixedRate(refresh, 0, 1000 / fps);
        marioTimer.scheduleAtFixedRate(marioTask, 0, 1000/fps);
    }
    //主方法
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        new MainFrame();
    }
    @Override//绘制图形
    public void paint(Graphics g) {
        Image img = this.createImage(this.getWidth(), this.getHeight());
        Graphics g2 = img.getGraphics();
        g2.drawImage(bg_img, 0, 0, 1000, 700, null);
        // 游戏内部
        if (flag == 1){
            map.draw(g2);
            mario.draw(g2);
        }
        // 帮助界面
        else if (flag == 2){
            help.draw(g2);
        }
        // 设置界面
        else if (flag == 4){
            setting.draw(g2);
        }
        //DIY界面
        else if(flag == 5){
            innerMapEditor.draw(g2);
        }
        menu.draw(g2);
        g.drawImage(img, 0, 0, null);
    }
    //键盘监听
    public class MarioKeyListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            int code = e.getKeyCode();
            if (flag == 1){
                if (code == 37 || code == 65) {
                    mario.left = true;
                    mario.direction = "left";
                } else if (code == 38||code == 87) {
                    mario.jump();
                } else if (code == 39 || code == 68) {
                    mario.right = true;
                    mario.direction = "right";
                }else if(code == 32){
                    mario.shadow.create();
                } else if (code == 74){
                    map.turnOnHandle();
                }else if (code == 75){
                    map.transform(map.transformGateA);
                    map.transform(map.transformGateB);
                }else if (code == 82){
                    try {
                        mario.isDead = false;
                        map.refreshMap();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
            else if(flag == 5){
               innerMapEditor.getKeyInfo(code);
            }
        }
        @Override
        public void keyReleased(KeyEvent e) {
            int code = e.getKeyCode();
            if (code == 37 || code == 65) {
                mario.left = false;
            } else if (code == 38 || code == 87){
                mario.isJump = false;
            } else if (code == 39 || code == 68) {
                mario.right = false;
            } else if (code == 27){
                flag = 0;
                bgm.playMenuBGM();
            }
            if(code == 16){
                innerMapEditor.isOnShift = false;
            }
        }
    }
    //鼠标监听
    public class MarioMouseListener implements MouseListener{

        @Override
        public void mouseClicked(MouseEvent e) {
            int[] position = {e.getX(), e.getY()};
            if (flag == 0||flag == 3||flag == 6||flag == 7){
                try {
                    menu.MouseClicked(position);
                } catch (IOException | LineUnavailableException ioException) {
                    ioException.printStackTrace();
                }
            }
            else if (flag == 4){
                setting.click();
            }
            else if(flag == 5){
                try {
                    innerMapEditor.click(position);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
            if(flag!=0){
                int[] rect = {menu.backX,menu.backY,menu.backW,menu.backH};
                if(GameFunction.checkMouseOn(mouse.position, rect)){
                    if(flag == 1){
                        if(map.stage == 0)flag = 3;
                        else flag = 7;
                    }
                    else if (flag == 5||flag == 7) flag = 6;
                    else flag = 0;
                    if(flag == 0||flag == 3){
                        bgm.playMenuBGM();
                    }else {
                        bgm.playDIY();
                    }
                }
            }
            if(flag==1){
                bg_img = new ImageIcon("image/bg.png").getImage();
            }else{
                bg_img = new ImageIcon("image/background.png").getImage();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }
    //线程
    public class Refresh extends TimerTask {
        @Override
        public void run() {
            mouse.update();
            menu.update();
            if(flag == 4){
                setting.update();
            }
            else if(flag == 1){
                timer ++;
                try {
                    map.update();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            repaint();
        }
    }
    //mario线程
    public class marioTask extends TimerTask{

        @Override
        public void run() {
            if (flag == 1){
                mario.update();
            }
        }
    }
}

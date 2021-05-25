package MapEditor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MapEditor extends JFrame {
    Cursor cursor = new Cursor();
    MouseIml mouseIml = new MouseIml();
    KeyIml keyIml = new KeyIml();
    char[][] map = new char[15][20];
    String[] text = new String[15];
    String mapPath = "map7.txt";
    int[] gateAPosition = new int[4];
    int[] gateBPosition = new int[4];

    Image brick,trap,transformGate,gate,key,upBrick,downBrick,leftBrick,rightBrick,handle,noticeBoard;
    boolean isOnShift = false;
    String[] notice = {"\n","\n","\n","\n"};

    public MapEditor(){
        this.setSize(1000, 700);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setTitle("Map Editor");
        this.setVisible(true);
        this.addMouseListener(mouseIml);
        this.addKeyListener(keyIml);
        //初始化
        for (int i = 0; i < 15; i++) {
            text[i] = "";
            for (int i1 = 0; i1 < 20; i1++) {
                if (i == 0 || i == 13){
                    map[i][i1] = '1';
                }
                else map[i][i1] = '0';
            }
        }
        //加载图片
        brick = new ImageIcon("image/brick/brick.png").getImage();
        trap = new ImageIcon("image/Trap.png").getImage();
        key = new ImageIcon("image/Key.png").getImage();
        gate = new ImageIcon("image/Gate1.png").getImage();
        transformGate = new ImageIcon("image/TransformGate.png").getImage();
        upBrick = new ImageIcon("image/upBrick.png").getImage();
        downBrick = new ImageIcon("image/downBrick.png").getImage();
        leftBrick = new ImageIcon("image/leftBrick.png").getImage();
        rightBrick = new ImageIcon("image/rightBrick.png").getImage();
        handle = new ImageIcon("image/handle.png").getImage();
        noticeBoard = new ImageIcon("image/noticeBoard.png").getImage();
    }//构造方法

    public static void main(String[] args){
        new MapEditor();
    }//main方法
    @Override
    public void paint(Graphics g){
        Image img = this.createImage(this.getWidth(), this.getHeight());
        Graphics g2 = img.getGraphics();
        for (int i = 0; i < 15; i++) {
            for (int i1 = 0; i1 < 20; i1++) {
                int a = map[i][i1];
                int x = i1 * 50;
                int y = i * 50;
                if (a == '1'){
                    g2.drawImage(brick, x, y, null);
                }
                else if (a == '2'){
                    g2.drawImage(gate, x, y, null);
                }
                else if (a == '3'){
                    g2.drawImage(key, x, y, null);
                }
                else if (a == '4'){
                    g2.drawImage(trap, x, y, 50, 50, null);
                }
                else if(a == '5'){
                    g2.drawImage(handle, x, y, 50, 50, null);
                }
                else if (a=='a'||a=='A'||a=='b'||a=='B'){
                    g2.drawImage(transformGate, x, y, null);
                }
                else if (a == 'u'){
                    g2.drawImage(upBrick, x, y, null);
                }
                else if (a == 'd'){
                    g2.drawImage(downBrick, x, y, null);
                }
                else if (a == 'l'){
                    g2.drawImage(leftBrick, x, y, null);
                }
                else if (a == 'r'){
                    g2.drawImage(rightBrick, x, y, null);
                }
                else if(a=='z'||a=='x'||a=='c'||a=='v'){
                    g2.drawImage(noticeBoard, x, y, null);
                }
            }
        }
        g2.drawLine(gateAPosition[0],gateAPosition[1],gateAPosition[2],gateAPosition[3]);
        g2.drawLine(gateBPosition[0],gateBPosition[1],gateBPosition[2],gateBPosition[3]);
        cursor.draw(g2);
        g.drawImage(img, 0, 0, null);
    }//绘制界面

    public void save() throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(mapPath));
        for (int i = 0; i < 15; i++) {
            text[i] = "";
        }
        for (int i = 0; i < 15; i++) {
            for (int i1 = 0; i1 < 19; i1++) {
                text[i] += "" + map[i][i1] + ",";
            }
            text[i] += "" + map[i][19] + "\n";
        }
        for (int i = 0; i < 15; i++) {
            bw.write(text[i]);
        }
        bw.write("\n");//请书写限制分身次数的相关代码
        for (int i = 0; i < 4; i++) {
            bw.write(notice[i]);
        }
        bw.close();
    }//保存

    public void rightNoticeBoard(int code){
        String text = JOptionPane.showInputDialog("请输入信息")+"\n";
        switch (code){
            case 90:notice[0]=text;break;
            case 88:notice[1]=text;break;
            case 67:notice[2]=text;break;
            case 86:notice[3]=text;break;
        }
    }//告示板信息

    public class MouseIml implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            cursor.x = (x/50)*50;
            cursor.y = (y/50)*50;
            repaint();
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
    }//鼠标监听

    public class KeyIml implements KeyListener{
        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            int code = e.getKeyCode();
            int x = cursor.x / 50;
            int y = cursor.y / 50;
            if (code == 16){
                isOnShift = true;
            }
            if (code == 37){
                if (cursor.x >= 50)
                cursor.x -= 50;
            }else if (code == 38){
                if (cursor.y >= 50)
                cursor.y -= 50;
            }else if (code == 39){
                if (cursor.x <= 900)
                cursor.x += 50;
            }else if (code == 40){
                if (cursor.y <= 600)
                cursor.y += 50;
            }
            if (code == 49){
                map[y][x] = '1';
            }
            else if (code == 50){
                map[y][x] = '2';
            }
            else if (code == 51){
                map[y][x] = '3';
            }
            else if (code == 52){
                map[y][x] = '4';
            }
            else if (code == 53){
                map[y][x] = '5';
            }
            else if (code == 65&&!isOnShift){
                map[y][x] = 'a';
                gateAPosition[0] = x * 50 + 25;
                gateAPosition[1] = y * 50 + 25;
            }
            else if (code == 65){
                System.out.println(gateAPosition[0] + " " + gateAPosition[1]);
                map[y][x] = 'A';
                gateAPosition[2] = x * 50 + 25;
                gateAPosition[3] = y * 50 + 25;
            }
            else if (code == 66&&!isOnShift){
                map[y][x] = 'b';
                gateBPosition[0] = x * 50 + 25;
                gateBPosition[1] = y * 50 + 25;
            }
            else if (code == 66){
                map[y][x] = 'B';
                gateBPosition[2] = x * 50 + 25;
                gateBPosition[3] = y * 50 + 25;
            }
            else if (code == 85){
                map[y][x] = 'u';
            }
            else if (code == 68){
                map[y][x] = 'd';
            }
            else if (code == 76){
                map[y][x] = 'l';
            }
            else if (code == 82){
                map[y][x] = 'r';
            }
            else if (code == 8||code == 32){
                map[y][x] = '0';
            }
            else if (code==90||code==88||code==67||code==86){
                rightNoticeBoard(code);
                switch (code){
                    case 90:map[y][x]='z';break;
                    case 88:map[y][x]='x';break;
                    case 67:map[y][x]='c';break;
                    case 86:map[y][x]='v';break;
                }
            }
            repaint();
            try {
                save();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == 16){
                isOnShift = false;
            }
        }
    }//键盘监听
}

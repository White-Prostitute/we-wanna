import javax.swing.*;
import java.awt.*;
import java.io.*;

public class InnerMapEditor {
    static InnerMapEditor innerMapEditor = new InnerMapEditor();
    Cursor cursor = new Cursor();
    Image imageSave;
    boolean save = false;
    int[] saveRect;

    Image brick, trap, transformGate, gate, key, upBrick, downBrick, leftBrick, rightBrick, handle, noticeBoard;
    int[] gateAPosition = new int[4];
    int[] gateBPosition = new int[4];
    char[][] map = new char[20][20];
    String[] text = new String[20];
    String[] notice = new String[4];
    boolean isOnShift = false;
    boolean isMapCorrect = true;
    boolean isaGateBeCreate = false;
    boolean isAGateBeCreate = false;
    boolean isbGateBeCreate = false;
    boolean isBGateBeCreate = false;

    private InnerMapEditor() {
        imageSave = new ImageIcon("image/menu/DIY/save.png").getImage();
        saveRect = new int[]{800, 600, imageSave.getWidth(null), imageSave.getHeight(null)};

        //初始化
        for (int i = 0; i < 20; i++) {
            text[i] = "";
            for (int i1 = 0; i1 < 20; i1++) {
                if(i<15){
                    if (i == 0 || i == 13) {
                        map[i][i1] = '1';
                    } else map[i][i1] = '0';
                }
                else map[i][i1] = ' ';
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

    public static InnerMapEditor getInstance() {
        return innerMapEditor;
    }//实例化方法

    public void save() throws IOException {
        checkMapCorrect();
        if (!isMapCorrect) {
            JOptionPane.showMessageDialog(null, "构造地图有误");
            return;
        }
        int shadowTime;
        try{
            shadowTime = Integer.parseInt(JOptionPane.showInputDialog("请输入分身次数"));
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "输入格式有误");
            return;
        }

        String name = JOptionPane.showInputDialog("为地图命名");
        if (name == null) return;
        if (name.equals("")) return;
        String path = "map/DiyMap/" + name + ".txt";
        for (String s : Map.DIYPath) {
            if (s.equals(path)) {
                JOptionPane.showMessageDialog(null, "地图:" + name + " 已存在");
                return;
            }
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(path));
        for (int i = 0; i < 15; i++) {
            text[i] = "";
        }
        for (int i = 0; i < 20; i++) {
            if(i<15) {
                for (int i1 = 0; i1 < 19; i1++) {
                    text[i] += "" + map[i][i1] + ",";
                }
                text[i] += "" + map[i][19] + "\n";
            }
            else if(i>=16){
                notice[i-16]="";
                for (int i1 = 0; i1 < 20; i1++) {
                    if(!(map[i][i1]==' ')) notice[i-16] = notice[i-16].concat(""+map[i][i1]);
                }
                //notice[i-16] = notice[i-16].concat("\n");
            }
        }
        for (int i = 0; i < 15; i++) {
            bw.write(text[i]);
        }
        bw.write(shadowTime + "\n");
        for (int i = 0; i < 4; i++) {
            bw.write(notice[i]);
        }
        bw.close();
        System.out.println("保存成功!!!");
    }//保存

    public void checkMapCorrect() {
        int gateNum = 0;
        int keyNum = 0;
        boolean a = false;
        boolean a1 = false;
        boolean b = false;
        boolean b1 = false;
        for (char[] chars : map) {
            for (int i1 = 0; i1 < map[0].length; i1++) {
                char k = chars[i1];
                switch (k) {
                    case '2':
                        gateNum++;
                        break;
                    case '3':
                        keyNum++;
                        break;
                    case 'a':
                        a = true;
                        break;
                    case 'A':
                        a1 = true;
                        break;
                    case 'b':
                        b = true;
                        break;
                    case 'B':
                        b1 = true;
                        break;
                    default:
                        break;
                }
            }
            if(gateNum != 1|| keyNum != 1){
                System.out.println("大门或钥匙数量异常");
            }
            isMapCorrect = gateNum == 1 && keyNum == 1;
        }
        if (!a || !a1) {
            map[14][0] = 'a';
            map[14][1] = 'A';
        }
        if (!b || !b1) {
            map[14][2] = 'b';
            map[14][3] = 'B';
        }
    }

    public void wrightNoticeBoard(int code) {
        String text = JOptionPane.showInputDialog("请输入信息") + "\n";
        switch (code) {
            case 90:
                for (int i = 0; i < text.length(); i++) {
                    map[16][i] = text.charAt(i);
                }
                break;
            case 88:
                for (int i = 0; i < text.length(); i++) {
                    map[17][i] = text.charAt(i);
                }
                break;
            case 67:
                for (int i = 0; i < text.length(); i++) {
                    map[18][i] = text.charAt(i);
                }
                break;
            case 86:
                for (int i = 0; i < text.length(); i++) {
                    map[19][i] = text.charAt(i);
                }
                break;
        }
    }//告示板信息

    public void getKeyInfo(int code) {
        //System.out.println("a"+isaGateBeCreate+" A"+isAGateBeCreate+" b"+isbGateBeCreate+" B"+isBGateBeCreate);
        int x = cursor.x / 50;
        int y = cursor.y / 50;
        if (code == 16) isOnShift = true;
        if (code == 37) {
            if (cursor.x >= 50)
                cursor.x -= 50;
        } else if (code == 38) {
            if (cursor.y >= 50)
                cursor.y -= 50;
        } else if (code == 39) {
            if (cursor.x <= 900)
                cursor.x += 50;
        } else if (code == 40) {
            if (cursor.y <= 600)
                cursor.y += 50;
        }
        if (code == 49) {
            map[y][x] = '1';
        } else if (code == 50) {
            map[y][x] = '2';
        } else if (code == 51) {
            map[y][x] = '3';
        } else if (code == 52) {
            map[y][x] = '4';
        } else if (code == 53) {
            map[y][x] = '5';
        } else if (code == 65 && !isOnShift) {
            if (!isaGateBeCreate) {
                isaGateBeCreate = true;
                map[y][x] = 'a';
                gateAPosition[0] = x * 50 + 25;
                gateAPosition[1] = y * 50 + 25;
            }
        } else if (code == 65) {
            if (!isAGateBeCreate) {
                isAGateBeCreate = true;
                map[y][x] = 'A';
                gateAPosition[2] = x * 50 + 25;
                gateAPosition[3] = y * 50 + 25;
            }
        } else if (code == 66 && !isOnShift) {
            if (!isbGateBeCreate) {
                isbGateBeCreate = true;
                map[y][x] = 'b';
                gateBPosition[0] = x * 50 + 25;
                gateBPosition[1] = y * 50 + 25;
            }
        } else if (code == 66) {
            if (!isBGateBeCreate) {
                isBGateBeCreate = true;
                map[y][x] = 'B';
                gateBPosition[2] = x * 50 + 25;
                gateBPosition[3] = y * 50 + 25;
            }
        } else if (code == 85) {
            map[y][x] = 'u';
        } else if (code == 68) {
            map[y][x] = 'd';
        } else if (code == 76) {
            map[y][x] = 'l';
        } else if (code == 82) {
            map[y][x] = 'r';
        } else if (code == 8 || code == 32) {
            map[y][x] = '0';
        } else if (code == 90 || code == 88 || code == 67 || code == 86) {
            wrightNoticeBoard(code);
            switch (code) {
                case 90:
                    map[y][x] = 'z';
                    break;
                case 88:
                    map[y][x] = 'x';
                    break;
                case 67:
                    map[y][x] = 'c';
                    break;
                case 86:
                    map[y][x] = 'v';
                    break;
            }
        }

    }//获取键盘信息

    public void click(int[] position) throws IOException {
        if (GameFunction.checkMouseOn(position, saveRect)) {
            save();
        }
    }//鼠标点击

    public void draw(Graphics g2) {
        for (int i = 0; i < 15; i++) {
            for (int i1 = 0; i1 < 20; i1++) {
                int a = map[i][i1];
                int x = i1 * 50;
                int y = i * 50;
                if (a == '1') {
                    g2.drawImage(brick, x, y, null);
                } else if (a == '2') {
                    g2.drawImage(gate, x, y, null);
                } else if (a == '3') {
                    g2.drawImage(key, x, y, null);
                } else if (a == '4') {
                    g2.drawImage(trap, x, y, 50, 50, null);
                } else if (a == '5') {
                    g2.drawImage(handle, x, y, 50, 50, null);
                } else if (a == 'a' || a == 'A' || a == 'b' || a == 'B') {
                    g2.drawImage(transformGate, x, y, null);
                } else if (a == 'u') {
                    g2.drawImage(upBrick, x, y, null);
                } else if (a == 'd') {
                    g2.drawImage(downBrick, x, y, null);
                } else if (a == 'l') {
                    g2.drawImage(leftBrick, x, y, null);
                } else if (a == 'r') {
                    g2.drawImage(rightBrick, x, y, null);
                } else if (a == 'z' || a == 'x' || a == 'c' || a == 'v') {
                    g2.drawImage(noticeBoard, x, y, null);
                }
            }
        }
        int w = imageSave.getWidth(null);
        int h = imageSave.getHeight(null);
        if(save) g2.drawImage(imageSave, 780, 590, w+40, h+20, null);

        else g2.drawImage(imageSave, 800, 600, null);
        cursor.draw(g2);
    }//绘制

    public static class Cursor extends GameObject {
        public Cursor() {
            img = new ImageIcon("image/cursor.png").getImage();
            x = 50;
            y = 50;
        }//构造方法

        @Override
        public void draw(Graphics g) {
            g.drawImage(img, x, y, 50, 50, null);
        }
    }//光标
}

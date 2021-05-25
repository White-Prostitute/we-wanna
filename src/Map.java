import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Map{
    static Map mapInstance;
    String[] map;
    static String[] DIYPath;
    MainFrame mf;
    Gate gateA;
    Key key;
    TransformGate transformGateA;
    TransformGate transformGateB;
    int level = 0;
    int stage = 0;
    int timer = 0;
    List<Brick> bricks = Collections.synchronizedList(new ArrayList<>());
    List<Trap> traps = new ArrayList<>();
    List<OneWayBrick> oneWayBricks = new ArrayList<>();
    List<NoticeBoard> noticeBoards = new ArrayList<>();
    List<Handle> handles = new ArrayList<>();
    String mapPath = "map/map" + level + ".txt";
    private Map(MainFrame mf) throws IOException {
        this.mf = mf;
        transformGateA = new TransformGate(mf.setting);
        transformGateB = new TransformGate(mf.setting);
        map = GameFunction.readMap(mapPath);
        DIYPath = GameFunction.readMapList();
        loadMap(map);
    }//构造方法

    public static Map getInstance(MainFrame mf) throws IOException {
        mapInstance = new Map(mf);
        return mapInstance;
    }

    public void loadMap(String[] map){
        for (int i = 0; i < 15; i++) {
            String[] line = map[i].split(",");
            for (int i1 = 0; i1 < line.length; i1++) {
                int x = i1 * 50;
                int y = i* 50;
                char a = line[i1].charAt(0);
                if (a == '1'){
                    Brick brick = new Brick(x, y, mf.setting);
                    bricks.add(brick);
                }
                else if (a == '2'){
                    gateA = new Gate(x, y);
                }
                else if (a == '3'){
                    key = new Key(x, y);
                }
                else if (a == '4'){
                    Trap trap = new Trap(mf, x, y);
                    traps.add(trap);
                }
                else if(a == '5'){
                    Handle handle = new Handle(x, y);
                    handles.add(handle);
                }
                else if (a == 'a'){
                    transformGateA.x = x;
                    transformGateA.y = y;
                }
                else if (a == 'A'){
                    transformGateA.x1 = x;
                    transformGateA.y1 = y;
                }
                else if (a == 'b'){
                    transformGateB.x = x;
                    transformGateB.y = y;
                }
                else if (a == 'B'){
                    transformGateB.x1 = x;
                    transformGateB.y1 = y;
                }
                else if (a=='u'||a=='d'||a=='l'||a=='r'){
                    String direction;
                    switch (a){
                        case 'u':direction = "up";break;
                        case 'd':direction = "down";break;
                        case 'l':direction = "left";break;
                        case 'r':direction = "right";break;
                        default:direction = " ";
                    }
                    oneWayBricks.add(new OneWayBrick(direction, x, y));
                }
                else if (a == 'z'){
                    noticeBoards.add(new NoticeBoard(mf.mario, map[16], x, y));
                }
                else if (a == 'x'){
                    noticeBoards.add(new NoticeBoard(mf.mario, map[17], x, y));
                }
                else if (a == 'c'){
                    noticeBoards.add(new NoticeBoard(mf.mario, map[18], x, y));
                }
                else if (a == 'v'){
                    noticeBoards.add(new NoticeBoard(mf.mario, map[19], x, y));
                }
            }
        }
        mf.mario.shadowTimes = Integer.parseInt(map[15]);
    }//加载地图信息

    public void refreshMap() throws IOException {
        //mario的重置
        mf.mario.isGetKey = false;
        mf.mario.isCreateShadow = false;
        mf.mario.x = 50;
        mf.mario.y = 600;
        //地图刷新
        bricks.clear();
        traps.clear();
        oneWayBricks.clear();
        noticeBoards.clear();
        handles.clear();
        if(stage == 0){
            mapPath = "map/map" + level + ".txt";
            map = GameFunction.readMap(mapPath);
        }
        loadMap(map);
    }//刷新地图

    public void update() throws IOException {
        if (timer == 1000){
            timer = 0;
        }
        timer ++;
        gateA.update();
        for (int i = traps.size() - 1 ; i >= 0; i--){
            traps.get(i).hit();
        }
        //告示板
        for (int i = noticeBoards.size() - 1 ; i>=0;i--){
            noticeBoards.get(i).update();
        }
        //获取KEY
        if (GameFunction.isCollide(mf.mario, key)){
            if(!mf.mario.isGetKey)mf.bgm.getKey();
            mf.mario.isGetKey = true;
        }
        //到达通关门
        if (GameFunction.isCollide(mf.mario, gateA)&&mf.mario.isGetKey){
            pass();
        }
    }//内部更新

    public void transform(TransformGate transformGate){
        if (GameFunction.isCollide(transformGate, mf.mario)){
            mf.mario.x = transformGate.x1;
            mf.mario.y = transformGate.y1;
        }
        else if (transformGate.checkCollide(mf.mario)){
            mf.mario.x = transformGate.x;
            mf.mario.y = transformGate.y;
        }
    }//传送

    public void turnOnHandle(){
        for (int i = handles.size()-1; i >=0 ; i--) {
            if(GameFunction.isCollide(mf.mario, handles.get(i))){
                handles.get(i).turn();
                break;
            }
        }
    }//使用开关

    public void draw(Graphics g2){
        try {
            for (int i = bricks.size() - 1; i >= 0; i--) {
                bricks.get(i).draw(g2);
            }
            for (int i = traps.size() - 1; i >= 0; i--) {
                traps.get(i).draw(g2);
            }
            for (int i = oneWayBricks.size() - 1; i >= 0; i--) {
                oneWayBricks.get(i).draw(g2);
            }
            for (int i = noticeBoards.size() - 1; i >= 0; i--) {
                noticeBoards.get(i).draw(g2);
            }
            for (int i = handles.size() - 1; i >= 0; i--) {
                handles.get(i).draw(g2);
            }
            gateA.draw(g2);
            key.draw(g2);
            transformGateA.draw(g2);
            transformGateB.draw(g2);
        }catch (Exception e){
            draw(g2);
        }
    }//绘制地图

    public void pass(){
            if(stage == 0){
                level++;
                try {
                    refreshMap();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else{
                mf.flag = 7;
                mf.bgm.playDIY();
            }
    }//通关
    //Gate内部类
    public class Gate extends GameObject{
        Image img1,img2;

        public Gate(int x, int y){
            this.x = x;
            this.y = y;
            img = new ImageIcon("image/Gate1.png").getImage();
            w = img.getWidth(null);
            h = img.getHeight(null);
            img1 = new ImageIcon("image/Gate1.png").getImage();
            img2 = new ImageIcon("image/Gate2.png").getImage();
        }

        public void update(){
            if (timer % 50 >= 25){
                img = img1;
            }
            else{
                img = img2;
            }
        }
        public void draw(Graphics g2){
            g2.drawImage(img, x, y, null);
        }
    }//Gate类
    //Key内部类
    public class Key extends GameObject{
        public Key(int x, int y){
            img = new ImageIcon("image/Key.png").getImage();
            w = 50;
            h = 50;
            this.x = x;
            this.y = y;
        }

        public void draw(Graphics g2){
            if (!mf.mario.isGetKey)
            g2.drawImage(img, x, y, 50, 50, null);
        }
    }//Key内部类
    //TransformGate内部类
    public static class TransformGate extends GameObject{
        int x1,y1;
        int w1,h1;
        public TransformGate(Setting setting){
            img = new ImageIcon(setting.transformGateImgPath).getImage();
            w = img.getWidth(null);
            h = img.getHeight(null);
            w1 = w;
            h1 = h;
        }
        public boolean checkCollide(Mario mario){
            Rectangle myRect = new Rectangle(x1 , y1, w1, h1);
            Rectangle marioRect = new Rectangle(mario.x, mario.y, mario.w, mario.h);
            return myRect.intersects(marioRect);
        }
        public void draw(Graphics g2){
            g2.drawImage(img, x, y, null);
            g2.drawImage(img, x1, y1, null);
        }
    }//TransformGate内部类
    //Trap内部类
    public static class Trap extends GameObject{
        MainFrame mf;
        public Trap(MainFrame mf, int x, int y){
            this.img = new ImageIcon("image/Trap.png").getImage();
            this.mf = mf;
            this.x = x;
            this.y = y;
            w = 25;
            h = 25;
        }

        public void hit(){
            x += 13;
            y += 13;
            if (GameFunction.isCollide(this, mf.mario)){
                mf.mario.dead();
            }
            x -= 13;
            y -= 13;
        }

        public void draw(Graphics g2){
            g2.drawImage(img, x, y,50, 50, null);
        }
    }
    //OneWayBrick内部类
    public static class OneWayBrick extends GameObject{
        String direction;
        public OneWayBrick(String direction, int x, int y){
            this.direction = direction;
            String imgPath ="image/" +  direction + "Brick.png";
            img = new ImageIcon(imgPath).getImage();
            this.x = x;
            this.y = y;
            w = img.getWidth(null);
            h = img.getHeight(null);
        }

        public void changeDirection(){
            switch (direction){
                case "left":direction="right";break;
                case "right":direction="left";break;
                case "up":direction="down";break;
                case "down":direction="up";break;
            }
            String imgPath ="image/" +  direction + "Brick.png";
            img = new ImageIcon(imgPath).getImage();
        }

        public void draw(Graphics g2){
            g2.drawImage(img, x, y, null);
        }
    }
    //noticeBoard内部类
    public static class NoticeBoard extends GameObject{
        String text;
        boolean isMarioOn = false;
        Mario mario;
        public NoticeBoard(Mario mario, String text, int x, int y){
            img = new ImageIcon("image/noticeBoard.png").getImage();
            this.text = text;
            this.mario = mario;
            this.x = x;
            this.y = y;
            w = img.getWidth(null);
            h = img.getHeight(null);
        }

        public void update(){
            isMarioOn = GameFunction.isCollide(this, mario);
        }

        public void draw(Graphics g2){
            g2.drawImage(img, x, y, null);
            if (isMarioOn){
                g2.drawString(text, x, y-10);
            }
        }
    }
    //Handle内部类
    public class Handle extends GameObject{
        boolean isOn = false;
        public Handle(int x, int y){
            img = new ImageIcon("image/handle.png").getImage();
            this.x = x;
            this.y = y;
            w = img.getWidth(null);
            h = img.getHeight(null);
        }
        public void turn(){
            mf.bgm.handle();
            for (int i = oneWayBricks.size()-1; i>=0; i--) {
                oneWayBricks.get(i).changeDirection();
            }
            if(isOn){
                img = new ImageIcon("image/handle.png").getImage();
            }else {
                img = new ImageIcon("image/handleOn.png").getImage();
            }
            isOn = !isOn;
        }
        @Override
        public void draw(Graphics g) {
            g.drawImage(img, x, y, null);
        }
    }
}
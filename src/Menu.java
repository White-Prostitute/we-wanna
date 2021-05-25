import javax.sound.sampled.LineUnavailableException;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Menu implements Update{

    MainFrame mf;
    Mouse mouse;
    String imgPath = "image/menu/";
    ChoseLevel choseLevel = new ChoseLevel();
    boolean begin,setting,help,exit,DIY,play,create = false;
    boolean buttonSound = false;
    boolean back = false;

    Image imageBegin;
    int beginX,beginY,beginW,beginH;

    Image imageSetting;
    int settingX,settingY,settingW,settingH;

    Image imageHelp;
    int helpX,helpY,helpW,helpH;

    Image imageDIY,imagePlay,imageCreate;
    int DIYx,DIYy,DIYw,DIYh;
    int playX,playY,playW,playH;
    int createX,createY,createW,createH;

    Image imageExit;
    int exitX,exitY,exitW,exitH;

    Image imageBack;
    int backX,backY,backW,backH;

    public Menu(MainFrame mf, Mouse mouse){

        this.mf = mf;
        this.mouse = mouse;

        imageBegin = new ImageIcon(imgPath + "begin.png").getImage();
        beginW = imageBegin.getWidth(null);
        beginH = imageBegin.getHeight(null);
        beginX = 500 - beginW / 2;
        beginY = 100;

        imageSetting = new ImageIcon(imgPath + "settings.png").getImage();
        settingW = imageSetting.getWidth(null);
        settingH = imageSetting.getHeight(null);
        settingX = 500 - settingW / 2;
        settingY = beginY + beginH + 15;

        imageHelp = new ImageIcon(imgPath + "help.png").getImage();
        helpW = imageHelp.getWidth(null);
        helpH = imageHelp.getHeight(null);
        helpX = 500 - helpW / 2;
        helpY = settingY + settingH + 15;

        imageDIY = new ImageIcon(imgPath + "DIY.png").getImage();
        DIYw = imageDIY.getWidth(null);
        DIYh = imageDIY.getHeight(null);
        DIYx = 500 - DIYw / 2;
        DIYy = helpY + helpH + 15;

        imageExit = new ImageIcon(imgPath + "exit.png").getImage();
        exitW = imageExit.getWidth(null);
        exitH = imageExit.getHeight(null);
        exitX = 500 - exitW / 2;
        exitY = DIYy + DIYh + 15;

        imageBack = new ImageIcon(imgPath + "back.png").getImage();
        backW = imageBack.getWidth(null);
        backH = imageBack.getHeight(null);
        backX = 25;
        backY = 30;

        imagePlay = new ImageIcon(imgPath+"/DIY/play.png").getImage();
        playW = imagePlay.getWidth(null);
        playH = imagePlay.getHeight(null);
        playX = 500-playW/2;
        playY = 100;
        imageCreate = new ImageIcon(imgPath+"/DIY/create.png").getImage();
        createW = imageCreate.getWidth(null);
        createH = imageCreate.getHeight(null);
        createX = 500-createW/2;
        createY = playY + playH + 20;
    }//构造方法

    public void MouseClicked(int[] position) throws IOException, LineUnavailableException {
        if (mf.flag == 0){
            if(begin)mf.flag = 3;
            else if(setting)mf.flag = 4;
            else if(help)mf.flag = 2;
            else if(DIY) {
                mf.flag = 6;
                mf.bgm.playDIY();
            }
            else if(exit)System.exit(0);
        }
        else if (mf.flag == 3){
            choseLevel.mouseClicked();
        }
        else if (mf.flag == 6){
            if(play){
                mf.flag = 7;
                Map.DIYPath = GameFunction.readMapList();
            }
            else if(create) mf.flag = 5;
        }
        else if (mf.flag == 7){
            int x = position[0];
            int y = position[1];
            if(x>200&&x<800&&y>50){
                int k = (y-50)/50;
                if(k< Map.DIYPath.length){
                    if(x<400) {
                        mf.bgm.playBGM();
                        mf.map.stage = 1;
                        mf.map.map = GameFunction.readMap(Map.DIYPath[k]);
                        mf.map.refreshMap();
                        mf.map.loadMap(mf.map.map);
                        mf.flag = 1;
                    }
                    else if(x<600){
                        GameFunction.deleteMap(Map.DIYPath[k]);
                        Map.DIYPath = GameFunction.readMapList();
                    }
                    else{
                        mf.flag = 5;
                        String[] editMap = GameFunction.readMap(Map.DIYPath[k]);
                        for (int i = 0; i < 15; i++) {
                            for (int i1 = 0; i1 < editMap[i].length(); i1++) {
                                if(i1%2 == 0) mf.innerMapEditor.map[i][i1/2] = editMap[i].charAt(i1);
                            }
                        }
                    }
                }
            }
        }
    }//鼠标监听

    public class ChoseLevel{
        String path;
        int[] rect;
        int X,Y;
        public ChoseLevel(){
            path = "image/menu/choseLevel/level_";
            rect =new int[] {100, 100, 800, 600};
        }

        public void update(int[] position){
            //实时获取鼠标位置
            if(position[0]%200<100||position[1]%200<100){
                X = -1;
                Y = -1;
                return;
            }
            X = (position[0] - 100) / 200;
            Y = (position[1] - 100) / 200;
        }
        public void draw(Graphics g2){
            for (int i = 0; i < 11; i++) {
                int x = i%4;
                int y = i/4;
                Image image = new ImageIcon(path+i+".png").getImage();
                if (x == X&&y == Y){
                    g2.drawImage(image,90+x*200,90+y*200,120,120,null);
                }
                else{
                    g2.drawImage(image, 100 + x*200, 100 + y*200, null);
                }

            }
        }
        public void mouseClicked() throws IOException {
            if (GameFunction.checkMouseOn(mouse.position, rect)){
                mf.map.stage = 0;
                if (Y*4+X<=10&&X>=0){
                    mf.map.level=Y*4+X;
                    mf.flag = 1;
                    mf.bgm.playBGM();
                }
                mf.map.refreshMap();
            }
        }
    }//选择关卡

    @Override
    public void update(){
        int[] position = mouse.position;
        int[] backRect = {backX, backY, backW, backH};
        back = GameFunction.checkMouseOn(position, backRect);
        if (mf.flag == 0) {
            //begin
            int[] beginRect = {beginX, beginY, beginW, beginH};
            begin = GameFunction.checkMouseOn(position, beginRect);
            //setting
            int[] settingRect = {settingX, settingY, settingW, settingH};
            setting = GameFunction.checkMouseOn(position, settingRect);
            //help
            int[] helpRect = {helpX, helpY, helpW, helpH};
            help = GameFunction.checkMouseOn(position, helpRect);
            //DIY
            int[] DIYRect = {DIYx, DIYy, DIYw, DIYh};
            DIY = GameFunction.checkMouseOn(position, DIYRect);
            //exit
            int[] exitRect = {exitX, exitY, exitW, exitH};
            exit = GameFunction.checkMouseOn(position, exitRect);
        }
        else if (mf.flag == 3){
            choseLevel.update(position);
        }
        else if (mf.flag == 6){
            //play
            int[] playRect = {playX,playY,playW,playH};
            play = GameFunction.checkMouseOn(position, playRect);
            //create
            int[] createRect = {createX,createY,createW,createH};
            create = GameFunction.checkMouseOn(position, createRect);
        }
        else if (mf.flag == 5){
            mf.innerMapEditor.save= GameFunction.checkMouseOn(position, mf.innerMapEditor.saveRect);
        }
        //buttonSound
        if(begin||setting||help||DIY||exit||play||create){
            if(buttonSound)mf.bgm.button();
            buttonSound = false;
        }
        else buttonSound = true;
    }//更新

    public void draw(Graphics g2){
        //主菜单界面
        if (mf.flag == 0){
            if(!begin)g2.drawImage(imageBegin, beginX, beginY, null);
            else g2.drawImage(imageBegin,beginX-15,beginY-10,beginW+30,beginH+20,null);

            if(!setting) g2.drawImage(imageSetting, settingX, settingY, null);
            else g2.drawImage(imageSetting,settingX-15,settingY-10,settingW+30,settingH+20,null);

            if(!help) g2.drawImage(imageHelp, helpX, helpY, null);
            else g2.drawImage(imageHelp,helpX-15,helpY-10,helpW+30,helpH+20,null);

            if(!DIY) g2.drawImage(imageDIY, DIYx, DIYy, null);
            else g2.drawImage(imageDIY,DIYx-15,DIYy-10,DIYw+30,DIYh+20,null);

            if(!exit) g2.drawImage(imageExit, exitX, exitY, null);
            else g2.drawImage(imageExit,exitX-15,exitY-10,exitW+30,exitH+20,null);
        }
        //选关界面
        else if (mf.flag == 3){
            choseLevel.draw(g2);
        }
        //DIY选择play与create界面
        else if (mf.flag == 6){
            if(!play)g2.drawImage(imagePlay, playX, playY, null);
            else g2.drawImage(imagePlay,playX-15,playY-10,playW+30,playH+20,null);

            if(!create)g2.drawImage(imageCreate, createX, createY, null);
            else g2.drawImage(imageCreate,createX-15,createY-10,createW+30,createH+20,null);
        }
        //DIY选关界面
        else if(mf.flag == 7){
            if(Map.DIYPath ==null){
                g2.drawString("你还未创建任意地图",300,200);
            }
            else{
                for (int i = 0; i < Map.DIYPath.length; i++) {
                    String s = Map.DIYPath[i].split("/")[2].split("\\.")[0];
                    Font font = new Font(Font.SANS_SERIF, Font.BOLD, 30);
                    g2.setFont(font);
                    g2.setColor(Color.WHITE);
                    g2.drawString(s, 200, 100+i*50);
                    g2.drawString("delete", 400, 100+i*50);
                    g2.drawString("edit", 600, 100+i*50);
                }
            }
        }
        if(mf.flag!=0){
            if(!back)g2.drawImage(imageBack, backX, backY, backW, backH, null);
            else g2.drawImage(imageBack, backX-8, backY-4,backW+16, backH+8, null);
        }
    }//绘制
}

import javax.swing.*;
import java.awt.*;

public class Setting {
    String marioImgPath;
    String brickImgPath;
    String transformGateImgPath;

    Image changeSkin;

    int[] rectChangeSkin;

    String mouseOn;
    MainFrame mf;
    public Setting(MainFrame mf){
        marioImgPath = "image/character/theme1/";
        brickImgPath = "image/brick/brick.png";
        transformGateImgPath = "image/TransformGate.png";

        mouseOn = "";
        this.mf = mf;
        changeSkin = new ImageIcon("image/menu/setting/changeSkin.png").getImage();

        rectChangeSkin =new int[] {200, 200, 600,100};
    }

    public void click(){
        if (mouseOn.equals("changeSkin")){
            int k = Integer.parseInt(marioImgPath.substring(marioImgPath.length()-2, marioImgPath.length()-1));
            k ++;
            if (k > 3)k =1;
            marioImgPath = "image/character/theme" + k + "/";
            mf.mario.loadImage();
        }
    }

    public void update(){
        if(GameFunction.checkMouseOn(mf.mouse.position, rectChangeSkin))mouseOn="changeSkin";
        else mouseOn="";
    }

    public void draw(Graphics g){
        if(!mouseOn.equals("changeSkin")) g.drawImage(changeSkin, rectChangeSkin[0], rectChangeSkin[1],
                rectChangeSkin[2], rectChangeSkin[3],null);
        else g.drawImage(changeSkin, rectChangeSkin[0]-20, rectChangeSkin[1]-5,
                rectChangeSkin[2]+40, rectChangeSkin[3]+10,null);
        g.drawImage(new ImageIcon(marioImgPath+"standRight.png").getImage(),820,240,null);
    }
}

package MapEditor;

import javax.swing.*;
import java.awt.*;

public class Cursor {
    Image img;
    int x,y;

    public Cursor(){
        img = new ImageIcon("image/cursor.png").getImage();
        x = 50;
        y = 50;
    }//构造方法

    public void draw(Graphics g2){
        g2.drawImage(img, x, y, 50, 50, null);
    }//绘制
}

import javax.swing.*;
import java.awt.*;

public class Brick extends GameObject{
    public Brick(int x,int y,Setting setting){
        this.x = x;
        this.y = y;
        img = new ImageIcon(setting.brickImgPath).getImage();
        w = img.getWidth(null);
        h = img.getHeight(null);
    }

    public void draw(Graphics g2){
        g2.drawImage(img, x, y, null);
    }
}

import javax.swing.*;
import java.awt.*;

public class Help extends GameObject {

    public Help(){
        img = new ImageIcon("image/helpInfo.png").getImage();
        x = 0;
        y = 0;
    }

    public void draw(Graphics g2){
        g2.drawImage(img, x, y, null);
    }
}

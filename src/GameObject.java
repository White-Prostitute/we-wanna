import java.awt.*;

public abstract class GameObject {
    Image img;
    int x,y,w,h;

    public abstract void draw(Graphics g);
}

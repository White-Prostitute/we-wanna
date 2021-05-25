import java.awt.*;

public class Mouse implements Update{

    Point p;
    MainFrame mf;
    int x;
    int y;
    int[] position = new int[2];
    public Mouse(MainFrame mf){
       this.mf = mf;
    }

    @Override
    public void update(){
        p = MouseInfo.getPointerInfo().getLocation();
        x = p.x - mf.getX();
        y = p.y - mf.getY() + 1;
        position[0] = x;
        position[1] = y;
    }
}

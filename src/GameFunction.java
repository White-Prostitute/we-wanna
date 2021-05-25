import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class GameFunction {
    //读取地图地图的txt文件
    public static String[] readMap(String fileName) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String[] map = new String[20];
        for (int i = 0; i < 20; i++) {
            map[i] = bufferedReader.readLine();
        }
        return map;
    }
    //读取DIY地图列表
    public static String[] readMapList(){
        ArrayList<String> list = new ArrayList<>();
        String path = "map/DiyMap";
        File file = new File(path);
        File[] array = file.listFiles();
        assert array != null;
        for (File file1 : array) {
            if(file1.isFile()){
                list.add("map/DiyMap/"+file1.getName());
            }
        }
        String[] mapList = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            mapList[i]=list.get(i);
        }
        return mapList;
    }
    //检查两个物体的碰撞
    public static boolean isCollide(GameObject a, GameObject b){
       Rectangle rectBrick = new Rectangle(a.x, a.y, a.w, a.h);
       Rectangle rectBullet = new Rectangle(b.x, b.y, b.w, b.h);
       return rectBrick.intersects(rectBullet);
    }
    //检查鼠标是否在目标上
    public static boolean checkMouseOn(int[] position, int[] rect){
        int mx = position[0];
        int my = position[1];
        int x = rect[0];
        int y = rect[1];
        int w = rect[2];
        int h = rect[3];
        return mx>x&&mx<x+w&&my>y&&my<y+h;
    }
    //删除指定文件
    public static void deleteMap(String mapName){
        File file = new File(mapName);
        if(file.exists()){
            if(file.delete()){
                System.out.println("删除成功");
            }
        }
    }
}

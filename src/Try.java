import java.io.File;

public class Try {

    public static void main(String[] args) {
        String path = "map/DiyMap";
        File file = new File(path);
        File[] array = file.listFiles();

        for (int i = 0; i < array.length; i++) {
            if(array[i].isFile()){
                System.out.println(array[i].getName());
            }
        }
    }

}

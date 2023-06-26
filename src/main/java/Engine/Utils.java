package Engine;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static String readFile(String filePath) {
        String str;
        try {
            str = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException excp) {
            throw new RuntimeException("Error reading file [" + filePath + "]", excp);
        }
        return str;
    }

    public static float[] listoFloat(List<Vector3f> arraylist){
        float[] arr = new float[arraylist.size()*3];
        int index = 0;
        for(int i = 0;i<arraylist.size();i++){
            arr[index++] = arraylist.get(i).x;
            arr[index++] = arraylist.get(i).y;
            arr[index++] = arraylist.get(i).z;
        }
        return arr;
    }

    public static int[] listoInt(List<Integer> arraylist){
        int[] arr = new int[arraylist.size()];
        for(int i = 0;i<arraylist.size();i++){
            arr[i] = arraylist.get(i);
        }
        return arr;
    }

    public static List<Integer> intToList(int[] arr){
        List<Integer> arrayList = new ArrayList<>();
        for(int i = 0;i<arr.length;i++){
            arrayList.add(arr[i]);
        }
        return arrayList;
    }

    public static List<Vector3f> floatToList( float[] arr){
        List<Vector3f> arraylist = new ArrayList<>();
        int index = 0;
        for(int i = 0;i<arr.length;i+=3){
            arraylist.add(new Vector3f(arr[i], arr[i+1], arr[i+2]));
        }
        return arraylist;
    }

    public static List<String> readAllLines(String filename){
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            String line;

            while ((line=br.readLine()) != null){
                list.add(line);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return list;
    }


    public static List<Vector2f> floatToList2(float[] arr) {
        List<Vector2f> arraylist = new ArrayList<>();
        for(int i = 0;i<arr.length;i+=2){
            arraylist.add(new Vector2f(arr[i], arr[i+1]));
        }
        return arraylist;

    }

    public static float[] listoFloat2(List<Vector2f> arraylist){
        float[] arr = new float[arraylist.size()*2];
        int index = 0;
        for(int i = 0;i<arraylist.size();i++){
            arr[index++] = arraylist.get(i).x;
            arr[index++] = arraylist.get(i).y;
        }
        return arr;
    }
}

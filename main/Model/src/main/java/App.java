import java.util.ArrayList;
import java.util.List;

public class App {
    public static List lista = new ArrayList<Integer>();

    public static void Main(){
        lista.add(1);
        lista.add(2);
        lista.add(3);
        lista.add(4);
        lista.add(null);
        lista.remove(2);
        lista.remove(null);
    }
}

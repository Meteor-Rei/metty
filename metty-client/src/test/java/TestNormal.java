import java.util.LinkedList;

/**
 * @Author: meteor
 * @Version: 1.0
 * @ClassName: TestNormal
 * @Created Time: 2024-04-07 13:39
 **/
public class TestNormal {
    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();

        list.add(1);
        list.add(3);

        list.add(7);
        list.remove((Object) 3);

        System.out.println(list.size());
        for(int i = 0 ; i < list.size() ; ++i){
            System.out.println(list.get(i));
        }
    }
}

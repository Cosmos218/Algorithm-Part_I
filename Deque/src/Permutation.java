import java.util.Iterator;
import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<>();
        int k = Integer.parseInt(args[0]);
        while (!StdIn.isEmpty())
            rq.enqueue(StdIn.readString());
        Iterator<String> it = rq.iterator();
        int i = 0;
        while (it.hasNext() && i < k) {
            System.out.println(it.next());
            i++;
        }

    }
}

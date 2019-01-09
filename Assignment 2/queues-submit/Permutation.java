import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {

    public static void main(String[] args) {
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        int k = Integer.parseInt(args[0]);
        double count = 0.0;
        while (!StdIn.isEmpty()) {
            String in = StdIn.readString();
            count++;
            if (rq.size() >= k) {
                if (StdRandom.uniform() >= (count - k) / count)
                    rq.dequeue();
                else
                    continue;
            }
            rq.enqueue(in); 
        }
        for (String s : rq) 
            StdOut.println(s); 
    }

}

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int n = s.length(); 
        char[] t = new char[n];
        for (int i = 0; i < n; i++) {
            int id = csa.index(i);
            if (id == 0) BinaryStdOut.write(i);
            t[i] = s.charAt((id - 1 + n) % n); 
        }
        BinaryStdOut.write(new String(t));
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        char[] a = BinaryStdIn.readString().toCharArray();
        int n = a.length;
        int[] next = new int[n];
        int[] count = new int[R + 1];

        for (int i = 0; i < n; i++) {
            count[a[i] + 1]++;
        }

        // transform counts to indicies
        for (int r = 0; r < R; r++)
            count[r + 1] += count[r];

        // distribute
        for (int i = 0; i < n; i++) {
            next[count[a[i]]++] = i;
        }

        // copy back
        int id = next[first];
        for (int i = 0; i < n; i++) {
            BinaryStdOut.write(a[id]);
            id = next[id];
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) BurrowsWheeler.transform();
        if (args[0].equals("+")) BurrowsWheeler.inverseTransform();
    }
}

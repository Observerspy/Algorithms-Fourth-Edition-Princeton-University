import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] seq = new char[R];
        for (char i = 0; i < R; i++)
            seq[i] = i;
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int i = 0;
            for (i = 0; i < R; i++) {
                if (c == seq[i]) 
                    break;
            }
            BinaryStdOut.write(i, 8);
            System.arraycopy(seq, 0, seq, 1, i); // seq[0:i]->seq[1:i+1]
            seq[0] = c;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] seq = new char[R];
        for (char i = 0; i < R; i++)
            seq[i] = i;
        while (!BinaryStdIn.isEmpty()) {
            int id = BinaryStdIn.readChar();
            char c = seq[id];
            BinaryStdOut.write(c);
            System.arraycopy(seq, 0, seq, 1, id); // seq[0:i]->seq[1:i+1]
            seq[0] = c;
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) MoveToFront.encode();
        if (args[0].equals("+")) MoveToFront.decode();
    }
}

import java.util.ArrayList;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private static final int R = 26;
    private Node root;
    private Bag<String> words;
    private char[] myboard;
    private boolean[] marked;
    private ArrayList<ArrayList<Integer>> adj;
    private final StringBuilder sb;
    private Bag<Node> nodes;    // nodes of found words
    private class Node {
        private boolean val;
        private Node[] next = new Node[R];
    }
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        root = new Node();
        sb = new StringBuilder(32);
        for (int i = 0; i < dictionary.length; i++)
            put(dictionary[i]);
    }
    private void put(String word) {
        root = put(root, word, 0);
    }
    private Node put(Node x, String word, int d) {
        if (x == null) x = new Node();
        if (word.length() == d) 
            x.val = true;
        else {
            char c = word.charAt(d);
            x.next[c - 'A'] = put(x.next[c - 'A'], word, d+1);
        }
        return x;
    }
    private void dfs(Node x, int idx) {
        char c = myboard[idx];
        Node next = x.next[c - 'A'];
        if (c == 'Q' && next != null)  
            next = next.next['U' - 'A'];
        if (next == null) return;
        sb.append(c);
        if (c == 'Q') sb.append('U');
        marked[idx] = true;

        if (next.val && sb.length() > 2) {
            words.add(sb.toString());
            nodes.add(next);
            next.val = false;
        }

        for (int i = 0; i < adj.get(idx).size(); i++) {
            int k = adj.get(idx).get(i);
            if (!marked[k] && next != null)
                dfs(next, k);
        } 

        marked[idx] = false;
        sb.deleteCharAt(sb.length()-1);
        if (sb.length() > 0 && sb.charAt(sb.length()-1) == 'Q') 
            sb.deleteCharAt(sb.length()-1);
    }
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        final int H = board.rows(), W = board.cols();
        marked = new boolean[H * W];
        this.myboard = new char[H * W];
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                int idx = i * W + j;
                char c = board.getLetter(i, j);
                this.myboard[idx] = c;
            }
        }
        words = new Bag<String>();
        nodes = new Bag<Node>();
        adj =  new ArrayList<ArrayList<Integer>>(H * W);
        for (int i = 0; i < H; i++) {
            for (int j = 0; j < W; j++) {
                int idx = i * W + j;
                ArrayList<Integer> adj_idx = new ArrayList<Integer>();
                if (i > 0 && j > 0) adj_idx.add(idx - W - 1); // left up
                if (i > 0 && j < W - 1) adj_idx.add(idx - W + 1); // up
                if (i > 0) adj_idx.add(idx - W); // right up
                if (j > 0) adj_idx.add(idx - 1); // left 
                if (j < W - 1) adj_idx.add(idx + 1); // right
                if (i < H - 1 && j > 0) adj_idx.add(idx + W - 1); // left down
                if (i < H - 1) adj_idx.add(idx + W); // down
                if (i < H - 1 && j < W - 1) adj_idx.add(idx + W + 1); // right down
                adj.add(adj_idx);
            }
        }
        for (int i = 0; i < W*H; i++) {
            dfs(root, i);
        }
        for (Node n : nodes)
            n.val = true;
        return words;
    }
    private boolean get(String word) {
        Node x = get(root, word, 0); 
        if (x == null) return false;
        return x.val;
    }
    private Node get(Node x, String word, int d) {
        if (x == null) return null;
        if (word.length() == d) return x;
        return get(x.next[word.charAt(d)-'A'], word, d+1);
    }
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!get(word)) return 0;
        int len = word.length();
        if (len < 3) return 0;
        else if (len < 5) return 1;
        else if (len < 6) return 2;
        else if (len < 7) return 3;
        else if (len < 8) return 5;
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word+" "+solver.scoreOf(word));
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}

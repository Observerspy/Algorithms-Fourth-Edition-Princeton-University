
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;
import edu.princeton.cs.algs4.Bag;

public class BoggleSolver {
    
    private static final int A = 'A';
    private static final char Q = 'Q';
    private static final char U = 'U';
    private static final int R = 26;        // extended ASCII
    private Node root;                      // root of trie
    
    private boolean[] marked;   // is visited
    private final StringBuilder sb;   // string builder
    private int size;           // current buffer size
    private Bag<Integer>[] adj; // adjacent list of the current board
    private char[] letter;      // current board letters
    private Bag<String> words;  // found words
    private Bag<Node> nodes;    // nodes of found words
    
    // Initializes the data structure 
    // using the given array of strings as the dictionary.
    public BoggleSolver(String[] dictionary) {
        root = new Node();
        for (int i = 0; i < dictionary.length; i++)
            put(dictionary[i], 1);
        size = 32;
        marked = new boolean[size];
        sb = new StringBuilder(32);
        adj = (Bag<Integer>[]) new Bag[size];
        letter = new char[size];
    }
    
    private static class Node {
        private int val;
        private Node[] next = new Node[R];
    }
    
    // recursive implementation
    private int get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return 0;
        return x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c-A], key, d+1);
    }

    private void put(String key, int val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, int val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        int c = key.charAt(d) - A;
        x.next[c] = put(x.next[c], key, val, d+1);
        return x;
    }
    
    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        words = new Bag<String>();
        nodes = new Bag<Node>();
        final int H = board.rows(), W = board.cols();
        if (H == 0 || W == 0) return words;
        if (H * W > size) {
            size = H * W;
            marked = new boolean[size];
            adj = (Bag<Integer>[]) new Bag[size];
            letter = new char[size];
        }
        for (int i = 0; i < H; i++) {
            boolean u = i > 0, d = i < H-1;
            int idx = i*W;
            for (int j = 0; j < W; j++, idx++) {
                boolean l = j > 0, r = j < W-1;
                adj[idx] = new Bag<Integer>();
                letter[idx] = board.getLetter(i, j);
                if (u && l) adj[idx].add(idx-W-1);
                if (u)      adj[idx].add(idx-W);
                if (u && r) adj[idx].add(idx-W+1);
                if (l)      adj[idx].add(idx-1);
                if (r)      adj[idx].add(idx+1);
                if (d && l) adj[idx].add(idx+W-1);
                if (d)      adj[idx].add(idx+W);
                if (d && r) adj[idx].add(idx+W+1);
            }
        }
        Node node;
        for (int i = 0; i < W*H; i++) {
            node = root.next[letter[i]-A];
            if (!marked[i] && node != null) dfs(node, i);
        }
        for (Node n : nodes)
            n.val = 1;
        return words;
    }
    
    private void dfs(Node node, int cur) {
        char ltr = letter[cur];
        if (ltr == Q) node = node.next[U-A];
        if (node == null) return;
        sb.append(ltr);
        if (ltr == Q) sb.append(U);
        marked[cur] = true;
        
        if (node.val == 1 && sb.length() > 2) {
            words.add(sb.toString());
            nodes.add(node);
            node.val = 2;
        }
        
        for (int next : adj[cur]) {
            Node temp = node.next[letter[next]-A];
            if (!marked[next] && temp != null) 
                dfs(temp, next);
        }
        
        marked[cur] = false;
        sb.deleteCharAt(sb.length()-1);
        if (sb.length() > 0 && sb.charAt(sb.length()-1) == Q) 
            sb.deleteCharAt(sb.length()-1);
    }
    
    // Returns the score of the given word if it is in the dictionary, 
    // zero otherwise.
    public int scoreOf(String word) {
        if (get(word) == 0) return 0;
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
        if (args.length > 2) {
            // algs4 ~ 37000 yawl ~ 7000
            int N = Integer.parseInt(args[2]);
            BoggleBoard[] boards = new BoggleBoard[N];
            for (int i = 0; i < N; i++)
                boards[i] = new BoggleBoard();
            Stopwatch sw = new Stopwatch();
            for (int i = 0; i < N; i++)
                solver.getAllValidWords(boards[i]);
            StdOut.println(sw.elapsedTime());
        } else {
            BoggleBoard board = new BoggleBoard(args[1]);
            int score = 0;
            Stopwatch sw = new Stopwatch();
            Iterable<String> words = solver.getAllValidWords(board);
            StdOut.println(sw.elapsedTime()*1000);
            for (String word : words)
            {
                StdOut.println(word);
                score += solver.scoreOf(word);
            }
            StdOut.println("Score = " + score);
        }
    }
    
}

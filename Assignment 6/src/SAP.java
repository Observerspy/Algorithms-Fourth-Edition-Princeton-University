import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph g;
    private int length;
    private int ancestor;
    //    private boolean[] marked1;
    //    private boolean[] marked2;
    //    private int[] dist1;
    //    private int[] dist2;
    private final HashMap<Integer, Integer> vs, ws;
    private final HashMap<String, Integer> len, anc;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException();
        g = new Digraph(G);
        //        dist1 = new int[G.V()];
        //        dist2 = new int[G.V()];
        //        marked1 = new boolean[G.V()];
        //        marked2 = new boolean[G.V()];
        vs = new HashMap<Integer, Integer>();
        ws = new HashMap<Integer, Integer>();
        len = new HashMap<String, Integer>();
        anc = new HashMap<String, Integer>();
    }
    private void bfs(Queue<Integer> q1, Queue<Integer> q2, String pair) {
        int step = 1;
        len.put(pair, g.V());
        anc.put(pair, g.V());
        while (!q1.isEmpty() || !q2.isEmpty()) {
            Queue<Integer> qv = new Queue<Integer>();
            Queue<Integer> qw = new Queue<Integer>();
            if (step >= len.get(pair)) break;
            while (!q1.isEmpty()) {
                int v = q1.dequeue();
                for (int w : g.adj(v)) {
                    if (vs.get(w) != null) continue;
                    if (ws.get(w) != null && step + ws.get(w) < len.get(pair)) {
                        ancestor = w;
                        length = step + ws.get(w);
                        len.put(pair, length);
                        anc.put(pair, ancestor);
                    }
                    vs.put(w, step);
                    qv.enqueue(w);
                }
            }
            while (!q2.isEmpty()) {
                int v = q2.dequeue();
                for (int w : g.adj(v)) {
                    if (ws.get(w) != null) continue;
                    if (vs.get(w) != null && step + vs.get(w) < len.get(pair)) {
                        ancestor = w;
                        length = step + vs.get(w);
                        len.put(pair, length);
                        anc.put(pair, ancestor);
                    }
                    ws.put(w, step);
                    qw.enqueue(w);
                }
            }
            q1 = qv;
            q2 = qw;
            step++;
        }
        if (len.get(pair) == g.V()) {
            len.put(pair, -1);
            anc.put(pair, -1);
        }
    }
    //    private void bfs2(Queue<Integer> q1, Queue<Integer> q2, String pair) {
    //        while (!q1.isEmpty() || !q2.isEmpty()) {
    //            if (!q1.isEmpty()) {
    //                int v = q1.dequeue();
    //                if (marked2[v]) {
    //                    if (dist1[v] + dist2[v] < length || length == -1) {
    //                        ancestor = v;
    //                        length = dist1[v] + dist2[v];
    //                        len.put(pair, length);
    //                        anc.put(pair, ancestor);
    //                    }
    //                }
    //                if (dist1[v] < length || length == -1) {
    //                    for (int w : g.adj(v)) {
    //                        if (!marked1[w]) {
    //                            dist1[w] = dist1[v] + 1;
    //                            marked1[w] = true;
    //                            q1.enqueue(w);
    //                        }
    //                    }
    //                }
    //            }
    //            if (!q2.isEmpty()) {
    //                int v = q2.dequeue();
    //                if (marked1[v]) {
    //                    if (dist1[v] + dist2[v] < length || length == -1) {
    //                        ancestor = v;
    //                        length = dist1[v] + dist2[v];
    //                        len.put(pair, length);
    //                        anc.put(pair, ancestor);
    //                    }
    //                }
    //                if (dist2[v] < length || length == -1) {
    //                    for (int w : g.adj(v)) {
    //                        if (!marked2[w]) {
    //                            dist2[w] = dist2[v] + 1;
    //                            marked2[w] = true;
    //                            q2.enqueue(w);
    //                        }
    //                    }
    //                }
    //            }
    //        }
    //        marked1 = new boolean[g.V()];
    //        marked2 = new boolean[g.V()];
    //    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (v < 0 || v >= g.V()) throw new IllegalArgumentException();
        if (w < 0 || w >= g.V()) throw new IllegalArgumentException();
        if (v == w) return 0;
        int temp = 0;
        if (v > w) {
            temp = v;
            v = w;
            w = temp;
        }
        String pair = new StringBuilder().append(v).append('+').append(w).toString();
        if (len.get(pair) != null) return len.get(pair);
        length = -1;
        Queue<Integer> q1 = new Queue<Integer>();
        Queue<Integer> q2 = new Queue<Integer>();
        vs.clear();
        ws.clear();
        q1.enqueue(v);
        q2.enqueue(w);
        vs.put(v, 0);
        ws.put(w, 0);
        len.put(pair, -1);
        bfs(q1, q2, pair);
        return len.get(pair);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (v < 0 || v >= g.V()) throw new IllegalArgumentException();
        if (w < 0 || w >= g.V()) throw new IllegalArgumentException();
        if (v == w) return v;
        int temp = 0;
        if (v > w) {
            temp = v;
            v = w;
            w = temp;
        }
        String pair = new StringBuilder().append(v).append('+').append(w).toString();
        if (anc.get(pair) != null) return anc.get(pair);
        length = -1;
        Queue<Integer> q1 = new Queue<Integer>();
        Queue<Integer> q2 = new Queue<Integer>();
        vs.clear();
        ws.clear();
        q1.enqueue(v);
        q2.enqueue(w);
        vs.put(v, 0);
        ws.put(w, 0);
        anc.put(pair, -1);
        bfs(q1, q2, pair);
        return anc.get(pair);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> vss, Iterable<Integer> wss) {
        if (vss == null || wss == null) throw new IllegalArgumentException();
        Iterator<Integer> iteratorv = vss.iterator();  
        Iterator<Integer> iteratorw = wss.iterator();  
        while (iteratorv.hasNext())                  
            if (iteratorv.next() == null) throw new IllegalArgumentException();     
        while (iteratorw.hasNext())                   
            if (iteratorw.next() == null) throw new IllegalArgumentException();    
        for (int v : vss)
            if (v < 0 || v >= g.V()) throw new IllegalArgumentException();
        for (int w : wss)
            if (w < 0 || w >= g.V()) throw new IllegalArgumentException();
        length = -1;
        Queue<Integer> q1 = new Queue<Integer>();
        Queue<Integer> q2 = new Queue<Integer>();
        vs.clear();
        ws.clear();
        ArrayList<Integer> vq = new ArrayList<Integer>(); 
        for (int v : vss) {
            q1.enqueue(v);
            vq.add(v);
            vs.put(v, 0);
        }
        for (int w : wss) {
            if (vq.contains(w)) return 0;
            q2.enqueue(w);
            ws.put(w, 0);
        }
        len.put("set", -1);
        bfs(q1, q2, "set");
        return len.get("set");
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> vss, Iterable<Integer> wss) {
        if (vss == null || wss == null) throw new IllegalArgumentException();
        Iterator<Integer> iteratorv = vss.iterator();  
        Iterator<Integer> iteratorw = wss.iterator();  
        while (iteratorv.hasNext())                  
            if (iteratorv.next() == null) throw new IllegalArgumentException();     
        while (iteratorw.hasNext())                   
            if (iteratorw.next() == null) throw new IllegalArgumentException();
        for (int v : vss) {
            if (v < 0 || v >= g.V()) throw new IllegalArgumentException();
        }
        for (int w : wss)
            if (w < 0 || w >= g.V()) throw new IllegalArgumentException();
        length = -1;
        Queue<Integer> q1 = new Queue<Integer>();
        Queue<Integer> q2 = new Queue<Integer>();
        vs.clear();
        ws.clear();
        ArrayList<Integer> vq = new ArrayList<Integer>(); 
        for (int v : vss) {
            q1.enqueue(v);
            vq.add(v);
            vs.put(v, 0);
        }
        for (int w : wss) {
            q2.enqueue(w);
            if (vq.contains(w)) return w;
            ws.put(w, 0);
        }
        anc.put("set", -1);
        bfs(q1, q2, "set");
        return anc.get("set");
    }

    //    do unit testing of this class
    public static void main(String[] args) {
        //        Digraph G = DigraphGenerator.rootedInDAG(20,100);
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

public class WordNet {
    private final HashSet<String> ns;
    private final HashMap<String, ArrayList<Integer>> ids;
    private final HashMap<Integer, String> id2noun;
    private final SAP sap;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException();
        ids = new HashMap<String, ArrayList<Integer>>();
        id2noun = new HashMap<Integer, String>();
        In s = new In(synsets);
        int v = 0;
        while (s.hasNextLine()) {
            v++;
            String[] line = s.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            id2noun.put(id, line[1]);
            String[] nonus = line[1].split(" ");
            for (String nonu : nonus) {
                if (ids.get(nonu) == null) ids.put(nonu, new ArrayList<Integer>());
                ids.get(nonu).add(id);
            }
        }
        ns = new HashSet<String>(ids.keySet());
        Digraph G = new Digraph(v);
        In h = new In(hypernyms);
        while (h.hasNextLine()) {
            String[] line = h.readLine().split(",");
            int source = Integer.parseInt(line[0]);
            for (int i = 1; i < line.length; i++) {
                int target = Integer.parseInt(line[i]);
                G.addEdge(source, target);
            }
        }
        DirectedCycle directedCycle = new DirectedCycle(G);
        if (directedCycle.hasCycle())
            throw new java.lang.IllegalArgumentException();

        int cnt = 0;
        for (int i = 0; i < G.V(); i++) 
            if (!G.adj(i).iterator().hasNext()) 
                cnt++;
        if (cnt != 1) 
            throw new IllegalArgumentException("Not a rooted DAG");
        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return ns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return ns.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        ArrayList<Integer> a = ids.get(nounA);
        ArrayList<Integer> b = ids.get(nounB);
        if (a.size() == 1 && b.size() == 1)
            return sap.length(a.get(0), b.get(0));
        else
            return sap.length(a, b);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        ArrayList<Integer> a = ids.get(nounA);
        ArrayList<Integer> b = ids.get(nounB);
        int anc;
        if (a.size() == 1 && b.size() == 1)
            anc = sap.ancestor(a.get(0), b.get(0));
        else
            anc =  sap.ancestor(a, b);
        return id2noun.get(anc);
    }

    // do unit testing of this class
    //    public static void main(String[] args) {
    //
    //    }
}

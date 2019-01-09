public class Outcast {
    private final WordNet wordnet;
    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }
    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int dmax = 0, index = 0;
        for (int i = 0; i < nouns.length; i++) {
            int d = 0;
            for (int j = 0; j < nouns.length; j++) {
                d += wordnet.distance(nouns[i], nouns[j]);
                if (d > dmax) {
                    dmax = d;
                    index = i;
                }
            }
        }
        return nouns[index];
    }
    // see test client below
    //    public static void main(String[] args) {
    //        WordNet wordnet = new WordNet(args[0], args[1]);
    //        Outcast outcast = new Outcast(wordnet);
    //        for (int t = 2; t < args.length; t++) {
    //            In in = new In(args[t]);
    //            String[] nouns = in.readAllStrings();
    //            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
    //        }
    //    }
}
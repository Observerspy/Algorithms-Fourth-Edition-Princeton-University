
public class CircularSuffixArray {
    private static final int R = 256;
    private final int n;
    private final int[] index;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        n = s.length();
        index = new int[n];
        for (int i = 0; i < n; i++)
            index[i] = i;
        int[] aux = new int[n];
        msd(s, index, aux, 0, 0, n-1);
    }
    // is v less than w, starting at character d
    private boolean less(String s, int v, int w, int d) {
        for (int i = d; i < n; i++) {
            int a1 = s.charAt((v + i) % n);
            int a2 = s.charAt((w + i) % n);
            if (a1 < a2) return true;
            if (a1 > a2) return false;
        }
        return false;
    }
    // sort from a[lo] to a[hi], starting at the dth character
    private void insertion(String s, int[] a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(s, a[j], a[j-1], d); j--) {
                int temp = a[j];
                a[j] = a[j - 1];
                a[j - 1] = temp;
            }
    }
    private void msd(String s, int[] a, int[] aux, int d, int lo, int hi) {
        // cutoff to insertion sort for small subarrays
        if (hi <= lo + 15) {
            insertion(s, a, lo, hi, d);
            return;
        }
        // compute frequency counts
        int[] count = new int[R + 2];
        for (int i = lo; i <= hi; i++) {
            int c = s.charAt((a[i] + d) % n); //
            if (d == s.length()) 
                c = -1;
            count[c + 2]++;
        }

        // transform counts to indicies
        for (int r = 0; r < R; r++)
            count[r + 1] += count[r];

        // distribute
        for (int i = lo; i <= hi; i++) {
            int c = s.charAt((a[i] + d) % n);
            if (d == s.length()) 
                c = -1;
            aux[count[c + 1]++] = a[i];
        }

        // copy back
        for (int i = lo; i <= hi; i++) 
            a[i] = aux[i - lo];

        // recursively sort for each character
        for (int r = 0; r < R; r++) {
            int left = lo + count[r], h = lo + count[r+1] - 1;
            if (left < h) msd(s, a, aux, d+1, left, h);
        }
    }
    // length of s
    public int length() {
        return n;
    }
    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= n) throw new IllegalArgumentException();
        return index[i];
    }
    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for (int i = 0; i < csa.length(); i++)
            System.out.print(csa.index(i) + " ");
    }
}

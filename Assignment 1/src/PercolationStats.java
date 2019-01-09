import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double RATE = 1.96;
    private int trial;
    private double[] exp;
    private double mean;
    private double std;

    // perform trials independent experiments on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        else {
            this.trial = trials;
            exp = new double[this.trial];

            for (int i = 0; i < this.trial; i++) {
                Percolation percolation = new Percolation(n); 
                int count = 0;
                while (!percolation.percolates()) {
                    int row = StdRandom.uniform(n) + 1;
                    int col = StdRandom.uniform(n) + 1;
                    if (!percolation.isOpen(row, col)) {
                        percolation.open(row, col);
                        count++;
                    }
                }
                exp[i] = (double) count / (n*n);
                // System.out.println(i+" "+exp[i]);
            }

            mean = StdStats.mean(exp);
            std = StdStats.stddev(exp);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }   

    // sample standard deviation of percolation threshold
    public double stddev() {
        return std;
    }   

    // low  endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean()-RATE*stddev()/Math.sqrt(trial);
    }   

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean()+RATE*stddev()/Math.sqrt(trial);
    }           

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats pcs = new PercolationStats(n, trials);
        System.out.println("mean:  " + pcs.mean()); 
        System.out.println("stddev:  " + pcs.stddev()); 
        System.out.println("confidence Low:  " + pcs.confidenceLo()); 
        System.out.println("confidence High:  " + pcs.confidenceHi());
    }
}

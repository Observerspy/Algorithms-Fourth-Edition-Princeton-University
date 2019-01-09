// import edu.princeton.cs.algs4.StdIn; 
import edu.princeton.cs.algs4.WeightedQuickUnionUF; 
// import java.io.FileInputStream; 
// import java.io.FileNotFoundException;

public class Percolation {
    private WeightedQuickUnionUF tree;
    private boolean[] site;
    private boolean[] top; // true if i connected row = 0
    private boolean[] bottom; // true if i connected row = n
    private int n;
    private int count;
    private boolean percolates = false;
    // create n-by-n grid, with all sites blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        else {
            this.n = n;
            tree = new WeightedQuickUnionUF(n*n);
            site = new boolean[n*n];
            top = new boolean[n*n];
            bottom = new boolean[n*n];
            //            site[0] = true;
            //            site[n*n+1] = true;
        }
    } 

    // open site (row, col) if it is not open already
    public    void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();

        if (isOpen(row, col))
            return;
        site[(row-1)*n+col-1] = true;
        count++;
        boolean topFlag = false;
        boolean bottomFlag = false;

        if (row == 1)
            topFlag = true;
        //            tree.union(0, (row-1)*n+col);
        if (row == n)
            bottomFlag = true;
        //            tree.union((row-1)*n+col, n*n+1);

        if (col != 1 && site[(row-1)*n+col-2]) {
            if (top[tree.find((row-1)*n+col-2)])
                topFlag = true;
            if (bottom[tree.find((row-1)*n+col-2)])
                bottomFlag = true;
            tree.union((row-1)*n+col-1, (row-1)*n+col-2);
        } 
        if (col != n && site[(row-1)*n+col]) {
            if (top[tree.find((row-1)*n+col)])
                topFlag = true;
            if (bottom[tree.find((row-1)*n+col)])
                bottomFlag = true;
            tree.union((row-1)*n+col-1, (row-1)*n+col);
        }
        if (row != 1 && site[(row-2)*n+col-1]) {
            if (top[tree.find((row-2)*n+col-1)])
                topFlag = true;
            if (bottom[tree.find((row-2)*n+col-1)])
                bottomFlag = true;
            tree.union((row-1)*n+col-1, (row-2)*n+col-1);
        }
        if (row != n && site[row*n+col-1]) {
            if (top[tree.find(row*n+col-1)])
                topFlag = true;
            if (bottom[tree.find(row*n+col-1)])
                bottomFlag = true;
            tree.union((row-1)*n+col-1, row*n+col-1);
        }

        int root = tree.find(id(row, col));
        top[root] = topFlag;
        bottom[root] = bottomFlag;
        if (top[root] && bottom[root])
            percolates = true;
    }  

    // is site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
        return site[(row-1)*n+col-1];
    }

    // is site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n)
            throw new IllegalArgumentException();
        return top[tree.find((row-1)*n+col-1)]; // tree.connected((row-1)*n+col, 0);
    }  

    // number of open sites
    public     int numberOfOpenSites() {
        return count;
    }      

    // does the system percolate?
    public boolean percolates() {
        return percolates; // tree.connected(0, n*n+1);
    }             

    private int id(int row, int col) {
        return (row-1)*n+col-1;
    }
}

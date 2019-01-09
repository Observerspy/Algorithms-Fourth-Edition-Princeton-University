import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture p;
    private int height;
    private int width;
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        p = new Picture(picture);
        height = p.height();
        width = p.width();

    }               
    // current picture
    public Picture picture() {
        return new Picture(p);

    }
    // width of current picture    
    public int width() {
        return width;

    }
    // height of current picture
    public int height() {
        return height;

    }
    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            throw new IllegalArgumentException();
        if (x == 0 || x == width -1 || y == 0 || y == height -1)
            return 1000;

        int left = p.getRGB(x-1, y);
        int right = p.getRGB(x+1, y);
        int up = p.getRGB(x, y-1);
        int down = p.getRGB(x, y+1);
        double xgradient = gradient(left, right);
        double ygradient = gradient(up, down);
        return Math.sqrt(xgradient + ygradient);
    }
    private double gradient(int rgb1, int rgb2) {
        int r1 = (rgb1 >> 16) & 0xFF;
        int g1 = (rgb1 >>  8) & 0xFF;
        int b1 = (rgb1 >>  0) & 0xFF;
        int r2 = (rgb2 >> 16) & 0xFF;
        int g2 = (rgb2 >>  8) & 0xFF;
        int b2 = (rgb2 >>  0) & 0xFF;

        return Math.pow(r1 - r2, 2) + Math.pow(g1 - g2, 2) 
        + Math.pow(b1 - b2, 2);
    }
    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] distTo = new double[width][height];
        int[][] edgeTo = new int[width][height];
        double[][] energy = new double[width][height];
        for (int j = 0; j < height; j++) 
            for (int i = 0; i < width; i++)
                energy[i][j] = energy(i, j);

        for (int i = 0; i < width; i++) 
            for (int j = 0; j < height; j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
                if (i == 0) distTo[i][j] = energy[i][j];
            }

        for (int i = 0; i < width - 1; i++)
            for (int j = 0; j < height; j++) {
                // right
                if (distTo[i + 1][j] > distTo[i][j] + energy[i + 1][j]) {
                    distTo[i + 1][j] = distTo[i][j] + energy[i + 1][j];
                    edgeTo[i + 1][j] = j;
                }
                // right up
                if (j > 0 && distTo[i + 1][j - 1] > distTo[i][j] + energy[i + 1][j - 1]) {
                    distTo[i + 1][j - 1] = distTo[i][j] + energy[i + 1][j - 1];
                    edgeTo[i + 1][j - 1] = j;
                }
                // right down
                if (j < height -1 && distTo[i + 1][j + 1] > distTo[i][j] + energy[i + 1][j + 1]) {
                    distTo[i + 1][j + 1] = distTo[i][j] + energy[i + 1][j + 1];
                    edgeTo[i + 1][j + 1] = j;
                }
            }

        int minRow = 0;
        double minEnergy = Double.POSITIVE_INFINITY;
        for (int j = 0; j < height; j++) {
            if (minEnergy > distTo[width - 1][j]) {
                minEnergy = distTo[width - 1][j];
                minRow = j;
            }
        }

        // construct Seam
        int[] seam = new int[width];
        int minCol = width - 1;
        while (minCol >= 0) {
            seam[minCol] = minRow;
            minRow = edgeTo[minCol--][minRow];
        }
        return seam;
    }
    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        transpose();
        int[] Seam = findHorizontalSeam();
        transpose();
        return Seam;
    }
    private void transpose() {
        Picture tmpPicture = new Picture(height, width);
        for (int j = 0; j < width; j++) {
            for (int i = 0; i < height; i++) {
                tmpPicture.setRGB(i, j, p.getRGB(j, i));
            }
        } 
        p = tmpPicture;
        int tmp = height;
        height = width;
        width = tmp;
    }
    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != width) throw new IllegalArgumentException();
        if (height <= 1) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length - 1; i++) {
            if (seam[i] < 0 || seam[i] >= height)
                throw new IllegalArgumentException();
            if (Math.abs(seam[i] - seam[i+1]) > 1)
                throw new IllegalArgumentException();
        }
        if (seam[seam.length - 1] < 0 || seam[seam.length - 1] >= height) throw new IllegalArgumentException();
        Picture tmpPicture = new Picture(width, height - 1);
        for (int j = 0; j < height - 1; j++) {
            for (int i = 0; i < width; i++) {
                //                validateColumnIndex(seam[i]);
                if (j < seam[i]) {
                    tmpPicture.setRGB(i, j, p.getRGB(i, j));
                } else {
                    tmpPicture.setRGB(i, j, p.getRGB(i, j + 1));
                }
            }
        }
        p = tmpPicture;
        height--;
    }
    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        transpose();
        removeHorizontalSeam(seam);
        transpose();
    }
}

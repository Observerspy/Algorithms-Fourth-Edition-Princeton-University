import java.util.ArrayList;

public class Board {
    private int[][] blocks;
    private final int n;
    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] inblocks) {
        n = inblocks.length;
        blocks = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                blocks[i][j] = inblocks[i][j];
            }
        }
    }
    // board dimension n
    public int dimension() {
        return n;
    }  
    // number of blocks out of place
    public int hamming() {
        int ham = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != 0 && blocks[i][j] != i*n+j+1)
                    ham++;
            }
        }
        return ham;
    }                   
    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int man = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != 0 && blocks[i][j] != i*n+j+1) {
                    int row = (blocks[i][j] - 1) / n;
                    int col = (blocks[i][j] - 1) % n;
                    man += Math.abs(row - i) + Math.abs(col - j);    
                }
            }
        }
        return man;
    } 
    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != 0 && blocks[i][j] != i*n+j+1)
                    return false;
            }
        }
        return true;
    }
    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        Board twinBoard = new Board(blocks);
        if (blocks[0][0] != 0 && blocks[0][1] != 0) {
            int temp = twinBoard.blocks[0][1];
            twinBoard.blocks[0][1] = twinBoard.blocks[0][0];
            twinBoard.blocks[0][0] = temp;
            return twinBoard;
        }
        else {
            int temp = twinBoard.blocks[n-1][n-2];
            twinBoard.blocks[n-1][n-2] = twinBoard.blocks[n-1][n-1];
            twinBoard.blocks[n-1][n-1] = temp;
            return twinBoard;
        }
    } 
    // does this board equal y?
    @Override
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y == this) return true;
        if (y.getClass() != this.getClass()) return false;
        Board myY = (Board) y;
        if (myY.n != n) return false;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] != myY.blocks[i][j])
                    return false;
            }
        }
        return true;
    }    
    // all neighboring boards
    public Iterable<Board> neighbors() {
        ArrayList<Board> nei = new ArrayList<Board>();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    if (i > 0) {
                        Board board1 = new Board(blocks);
                        int temp = board1.blocks[i][j];
                        board1.blocks[i][j] = board1.blocks[i-1][j];
                        board1.blocks[i-1][j] = temp;
                        nei.add(board1);
                    }
                    if (i < n-1) {
                        Board board2 = new Board(blocks);
                        int temp = board2.blocks[i][j];
                        board2.blocks[i][j] = board2.blocks[i+1][j];
                        board2.blocks[i+1][j] = temp;
                        nei.add(board2);
                    }
                    if (j > 0) {
                        Board board3 = new Board(blocks);
                        int temp = board3.blocks[i][j];
                        board3.blocks[i][j] = board3.blocks[i][j-1];
                        board3.blocks[i][j-1] = temp;
                        nei.add(board3);
                    }
                    if (j < n-1) {
                        Board board4 = new Board(blocks);
                        int temp = board4.blocks[i][j];
                        board4.blocks[i][j] = board4.blocks[i][j+1];
                        board4.blocks[i][j+1] = temp;
                        nei.add(board4);
                    }
                }
            }
        }    
        return nei;
    }    
    // string representation of this board (in the output format specified below)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sb.append(String.format("%2d ", blocks[i][j]));
            }
        }
        return sb.toString();
    }              

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}

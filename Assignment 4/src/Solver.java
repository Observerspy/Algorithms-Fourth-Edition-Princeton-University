import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode current;

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private int moves;
        private final SearchNode pre;
        private final int priority;

        public SearchNode(Board inboard, SearchNode inPreSearchNode) {
            board = inboard;
            pre = inPreSearchNode;
            if (pre == null) moves = 0;
            else moves = inPreSearchNode.moves + 1;
            priority = moves + board.manhattan();
        }
        @Override
        public int compareTo(SearchNode other) {
            return Integer.compare(this.priority, other.priority);
        }

    }
    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        current = new SearchNode(initial, null);
        SearchNode twin = new SearchNode(initial.twin(), null);
        MinPQ<SearchNode> pQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>();
        pQ.insert(current);
        twinPQ.insert(twin);
        while (true) {
            current = pQ.delMin();
            if (current.board.isGoal()) break;
            for (Board nei: current.board.neighbors()) {
                if (current.pre == null || !current.pre.board.equals(nei))
                    pQ.insert(new SearchNode(nei, current));
            }

            SearchNode twinNode = twinPQ.delMin();
            if (twinNode.board.isGoal()) break;
            for (Board nei: twinNode.board.neighbors()) {
                if (twinNode.pre == null || !twinNode.pre.board.equals(nei))
                    twinPQ.insert(new SearchNode(nei, twinNode));
            }
        }
    }      
    // is the initial board solvable?
    public boolean isSolvable() {
        return current.board.isGoal();
    }   
    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (isSolvable()) return current.moves;
        else return -1;
    } 
    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> solution = new Stack<Board>();
        SearchNode node = current;
        while (node != null) {
            solution.push(node.board);
            node = node.pre;
        }
        return solution;
    }     

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}

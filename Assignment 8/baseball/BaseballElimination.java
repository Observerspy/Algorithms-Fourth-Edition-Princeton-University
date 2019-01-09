import java.util.HashMap;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private HashMap<String, int[]> teamData;
    private HashMap<Integer, String> id2name;
    private int N;
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        if (filename == null) throw new IllegalArgumentException();
        In input = new In(filename);
        N = input.readInt(); 
        int len = N + 4;
        teamData = new HashMap<String, int[]>();
        id2name = new HashMap<Integer, String>();
        for (int i = 0; i < N; i++) {
            String s = input.readString();
            int[] l = new int[len];
            for (int j = 0; j < len - 1; j++) {
                l[j] = input.readInt();
            }
            l[len - 1] = i;
            id2name.put(i, s);
            teamData.put(s, l);
        }
    }
    // number of teams
    public int numberOfTeams() {
        return N;
    }
    // all teams
    public Iterable<String> teams() {
        return teamData.keySet();
    }
    // number of wins for given team
    public int wins(String team) {
        if (team == null || !teamData.containsKey(team))
            throw new IllegalArgumentException();
        int[] l = teamData.get(team);
        if (l != null) return l[0];
        else throw new NoSuchElementException();
    }
    // number of losses for given team
    public int losses(String team) {
        if (team == null || !teamData.containsKey(team))
            throw new IllegalArgumentException();
        int[] l = teamData.get(team);
        if (l != null) return l[1];
        else throw new NoSuchElementException();
    }
    // number of remaining games for given team
    public int remaining(String team) {
        if (team == null || !teamData.containsKey(team))
            throw new IllegalArgumentException();
        int[] l = teamData.get(team);
        if (l != null) return l[2];
        else throw new NoSuchElementException();
    }
    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (team1 == null || !teamData.containsKey(team1))
            throw new IllegalArgumentException();
        if (team2 == null || !teamData.containsKey(team2))
            throw new IllegalArgumentException();
        int[] l1 = teamData.get(team1);
        int[] l2 = teamData.get(team2);
        return l1[l2[l2.length - 1] + 3];
    }
    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (team == null || !teamData.containsKey(team))
            throw new IllegalArgumentException();
        return certificateOfElimination(team) != null;
    }
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (team == null || !teamData.containsKey(team))
            throw new IllegalArgumentException();
        Queue<String> R = new Queue<String>();
        HashMap<Integer, int[]> game = new HashMap<Integer, int[]>();
        int[] l = teamData.get(team);
        int id = l[l.length-1];
        // Trivial elimination
        int maxWin = l[0] + l[2];
        for (int i = 0; i < N; i++) {
            if (i != id) {
                String tname = id2name.get(i);
                if (teamData.get(tname)[0] > maxWin)
                    R.enqueue(tname);
            }
        }
        if (R.size() == 0) {
            int count = 0;
            for (int i = 0; i < N; i++) {
                if (i != id) {
                    String tname = id2name.get(i);
                    int[] data = teamData.get(tname);
                    for (int j = i + 3; j < data.length - 1; j++) {
                        if (data[j] != 0 && id != j - 3) {
                            int[] against = new int[3];
                            against[0] = i;
                            against[1] = j - 3;
                            against[2] = data[j];
                            game.put(count + N, against);
                            count++;
                        }
                    }
                }
            }
            int v = N + count + 2;
            FlowNetwork fn = new FlowNetwork(v);
            // team to t (v-1)
            for (int i = 0; i < N; i++) {
                if (i != id) {
                    String tname = id2name.get(i);
                    int[] data = teamData.get(tname);
                    fn.addEdge(new FlowEdge(i, v - 1, l[0] + l[2] - data[0]));
                }
            }
            for (int i = N; i < v - 2; i++) {
                int[] against = game.get(i);
                // s (v-2) to game
                fn.addEdge(new FlowEdge(v - 2, i, against[2]));
                // game to team
                fn.addEdge(new FlowEdge(i, against[0], Double.POSITIVE_INFINITY));
                fn.addEdge(new FlowEdge(i, against[1], Double.POSITIVE_INFINITY));
            }
            FordFulkerson ff = new FordFulkerson(fn, v-2, v-1);
            for (int i = 0; i < N; i++) {
                if (i != id) {
                    if (ff.inCut(i))
                        R.enqueue(id2name.get(i));
                }
            }
        }
        if (R.size() != 0) return R;
        else return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}

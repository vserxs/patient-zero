import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     *
     * State for every person v (looking only at v and everything below it): the smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed") and with the real costs ("Cheapest testing").
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        // TODO Mission 5a
        boolean[] tested = new boolean[n];

        if (n == 0) return tested;

        int[] parent = new int[n];
        Arrays.fill(parent, -2);

        int[] order = new int[n];
        int[] stack = new int[n];

        int top = 0;
        int size = 0;

        parent[0] = -1;
        stack[top++] = 0;

        while (top > 0) {
            int u = stack[--top];
            order[size++] = u;

            for (int v : nb[u]) {
                if (v != parent[u] && parent[v] == -2) {
                    parent[v] = u;
                    stack[top++] = v;
                }
            }
        }
        long[][] dp = new long[n][2];

        for (int index = size - 1; index >= 0; index--) {
            int u = order[index];

            dp[u][0] = 0;
            dp[u][1] = cost[u];

            for (int v : nb[u]) {
                if (parent[v] == u) {
                    dp[u][0] += dp[v][1];

                    dp[u][1] += Math.min(dp[v][0], dp[v][1]);
                }
            }
        }
        int[] state = new int[n];

        state[0] = dp[0][1] < dp[0][0] ? 1 : 0;

        for (int index = 0; index < size; index++) {
            int u = order[index];

            for (int v : nb[u]) {
                if (parent[v] == u) {

                    if (state[u] == 0) {
                        state[v] = 1;
                    } else {
                        state[v] = dp[v][1] < dp[v][0] ? 1 : 0;
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            tested[i] = state[i] == 1;
        }
        return tested;
    }

    /**
     * The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3 has chain 2.
     */
    public static int longestChain(int n, int[][] nb) {
        // TODO Mission 5b
        if (n <= 1) return 0;

        int[] parent = new int[n];
        Arrays.fill(parent, -2);

        int[] order = new int[n];
        int[] stack = new int[n];

        int top = 0;
        int size = 0;

        parent[0] = -1;
        stack[top++] = 0;

        while (top > 0) {
            int u = stack[--top];
            order[size++] = u;

            for (int v : nb[u]) {
                if (v != parent[u] && parent[v] == -2) {
                    parent[v] = u;
                    stack[top++] = v;
                }
            }
        }
        int[] down = new int[n];
        int diameter = 0;

        for (int index = size - 1; index >= 0; index--) {
            int u = order[index];

            int longest = 0;
            int secondLongest = 0;

            for (int v : nb[u]) {
                if (parent[v] == u) {
                    int branch = down[v] + 1;

                    if (branch > longest) {
                        secondLongest = longest;
                        longest = branch;
                    } else if (branch > secondLongest) {
                        secondLongest = branch;
                    }
                }
            }
            down[u] = longest;

            diameter = Math.max(diameter,longest + secondLongest
            );
        }
        return diameter;
    }
}

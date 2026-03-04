import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class LCATree {
    public static void main(String[] args) {
        // Tree:       1
        //            / \
        //           2   3
        //          / \   \
        //         4   5   6
        //        /
        //       7
        int n = 7;
        int[][] edges = {{1,2},{1,3},{2,4},{2,5},{3,6},{4,7}};
        Solution sol = new Solution(n, edges);

        int[][] queries = {
            {4, 5},  // siblings       -> expected LCA = 2
            {4, 6},  // cousins        -> expected LCA = 1
            {7, 5},  // diff depths    -> expected LCA = 2
            {7, 6},  // diff subtrees  -> expected LCA = 1
            {2, 4},  // ancestor-desc  -> expected LCA = 2
            {3, 3},  // same node      -> expected LCA = 3
            {7, 3},  // deep vs shallow-> expected LCA = 1
        };
        int[] expected = {2, 1, 2, 1, 2, 3, 1};

        int[] result = sol.queries(queries);
        boolean allPassed = true;
        for (int i = 0; i < queries.length; i++) {
            boolean pass = result[i] == expected[i];
            if (!pass) allPassed = false;
            System.out.printf("LCA(%d,%d) = %d | expected %d | %s%n",
                queries[i][0], queries[i][1], result[i], expected[i], pass ? "PASS" : "FAIL");
        }
        System.out.println(allPassed ? "\nAll tests passed!" : "\nSome tests FAILED.");
    }
}

class Solution {
    int n;
    int[][] parent;
    List<List<Integer>> adj;
    int[] depth;
    public Solution(int n, int[][] edges) {
        this.n = n;
        parent = new int[n+1][20];
        adj = new ArrayList<>();
        for(int i=0; i<n+1; i++) {
            Arrays.fill(parent[i],-1);
            adj.add(new ArrayList<>());
        }
        doBinaryLifting(edges);
        for(int[] edge: edges) {
            int parent = edge[0], child = edge[1];
            adj.get(parent).add(child);
        }
        depth = new int[n+1];
        dfs(1,0);
        //2^20 is around = 1e6
    }

    public int[] queries(int[][] queries) {
        int[] ans  = new int[queries.length];
        int ind =0;
        for(int[] query: queries) {
            int a = query[0], b = query[1];
            //lca of a and b
            int d1 = depth[a], d2= depth[b];
            int[] equalLevelNodes = equalizeDepth(a,b);
            a = equalLevelNodes[0];
            b = equalLevelNodes[1];
            if(a==b) {
                ans[ind++]=a;
                continue;
            }
            int i = 19;
            for(;i>=0; i--) {
                if(parent[a][i]!=parent[b][i]) {
                    a= parent[a][i];
                    b= parent[b][i];
                }
            }
            ans[ind]=parent[a][0];
            ind++;
        }
        return ans;
    }

    private void doBinaryLifting(int[][] edges) {
        for(int[] edge: edges) {
            parent[edge[1]][0]= edge[0];
        }
        for(int i=1; i<=n; i++) {
            for(int j=1; j<20; j++) {
                int node = parent[i][j-1];
                if(node!=-1) {
                    parent[i][j]= parent[node][j-1];
                }
            }
        }
    }

    private int[] equalizeDepth(int a, int b) {
        int d1 = depth[a];
        int d2 = depth[b];
        if(d1==d2) {
            return new int[]{a,b};
        }
        if(d2<d1) {
            int temp =b;
            b=a;
            a= temp;
            d1 = depth[a];
            d2= depth[b];
        }
        int nLev = d2-d1;
        int node = b;
        for(int i=19;i>=0;i--) {
            if(((1<<i)&nLev)>0) {
                node = parent[node][i];
            }
        }
        return new int[]{a, node};
    }

    void dfs(int node, int val) {
        depth[node]= val;
        for(int x: adj.get(node)) {
            dfs(x, val+1);
        }
    }
}

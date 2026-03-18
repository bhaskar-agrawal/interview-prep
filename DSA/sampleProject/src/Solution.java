import java.util.Arrays;

class Solution {
    int[] depth;
    int[] vis;
    public int longestCycle(int[] edges) {
        int n = edges.length;
        depth = new int[n];
        vis = new int[n];
        Arrays.fill(depth, -1);
        int len =0;
        for(int i=0; i<n; i++) {
            if(vis[i]==0) {
                len = Math.max(len, dfs(i, edges, 0));
            }
        }
        if(len==0) {
            return -1;
        }
        return len;
    }

    int dfs(int node, int[] edges, int newDep) {
        if(node==-1) {
            return 0;
        }
        if(vis[node]==2) {
            return 0;
        }
        if(vis[node]==1) {
            vis[node]=2;
            return newDep-depth[node];
        }
        vis[node]=1;
        depth[node]=newDep;
        int len =0;
        len = dfs(edges[node], edges, newDep+1);
        vis[node]=2;
        return len;
    }
}
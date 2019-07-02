package problems;

public class UnionFind {
    /**
     * Problem547:朋友圈
     * 班上有 N 名学生。其中有些人是朋友，有些则不是。
     * 们的友谊具有是传递性。如果已知 A 是 B 的朋友，B 是 C 的朋友，那么我们可以认为 A 也是 C 的朋友。所谓的朋友圈，是指所有朋友的集合。
     *
     * 给定一个 N * N 的矩阵 M，表示班级中学生之间的朋友关系。如果M[i][j] = 1，表示已知第 i 个和 j 个学生互为朋友关系，否则为不知道。
     * 你必须输出所有学生中的已知的朋友圈总数。
     * 示例 1:
     *
     * 输入:
     * [[1,1,0],
     *  [1,1,0],
     *  [0,0,1]]
     * 输出: 2
     * 说明：已知学生0和学生1互为朋友，他们在一个朋友圈。
     * 第2个学生自己在一个朋友圈。所以返回2。
     */
    //dfs解法，建立一个标记数组，已经遍历过的小孩不需要做操作。
    public int findCircleNum(int[][] M) {
        boolean[] isVisited = new boolean[M.length];
        int cnt = 0;
        for(int i = 0; i < M.length; i++){
            if (!isVisited[i]){
                dfs(M, isVisited, i);
                cnt++;
            }
        }
        return cnt;
    }

    private void dfs(int[][] M, boolean[] isVisited, int i){
        isVisited[i] = true;
        for(int j = 0; j < M.length; j++){
            if (M[i][j]==1&&!isVisited[j]){
                dfs(M, isVisited, j);
            }
        }
    }
    //并查集
    private int find(int num, int[] ufArray){
        while(ufArray[num]!=num){
            ufArray[num] = ufArray[ufArray[num]];//路径压缩，跨级提升。
            num = ufArray[num];
        }
        return num;
    }
    private boolean union(int a, int b, int[] ufArray, int[] rank){
        int aRoot = find(a, ufArray);
        int bRoot = find(b, ufArray);
        if (aRoot == bRoot) return false;
        if (rank[aRoot] > rank[bRoot]){
            ufArray[bRoot] = aRoot;
        } else {
            ufArray[aRoot] = bRoot;
            if (rank[aRoot] == rank[bRoot]){//增加rank值
                rank[bRoot]++;
            }
        }
        return true;
    }
    public int findCircleNumII(int[][] M) {
        int[] ufArray = new int[M.length];
        int[] rank = new int[M.length];
        int cnt = M.length;//朋友圈数
        for (int i = 0; i < M.length; i++) {//初始化，每个节点父节点为自己
            ufArray[i] = i;
        }
        for (int i = 0; i < M.length-1; i++)//上三角遍历
            for(int j = i+1; j < M.length; j++){
                if (M[i][j] == 1) {
                    boolean suc = union(i, j, ufArray, rank);
                    if(suc)
                        cnt--;
                }
            }
        return cnt;
    }
}
